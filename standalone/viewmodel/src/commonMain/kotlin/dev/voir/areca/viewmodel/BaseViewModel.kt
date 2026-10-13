package dev.voir.areca.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Base ViewModel for unidirectional state handling.
 *
 * The UI calls public methods that [dispatch] actions. One coroutine in [viewModelScope] takes the
 * actions in dispatch order and runs [reduce] for each, one at a time. Reducers publish the
 * top-level [state] and may send one-off [effects].
 *
 * Threading: [viewModelScope] runs on `Dispatchers.Main.immediate` when the platform has a Main
 * dispatcher (Android, iOS, and JVM with `kotlinx-coroutines-swing`, `-javafx`, or a test
 * `Dispatchers.setMain`). On a JVM without one, it runs on a single-parallelism view of
 * `Dispatchers.Default`, so constructing a ViewModel never fails for lack of a Main dispatcher.
 * Either way, reducers and work launched in [viewModelScope] never run in parallel. When a
 * dispatch is made on the main thread, `Main.immediate` may run its reducer before [dispatch]
 * returns.
 *
 * Confinement: the protected state helpers ([display], [setInitial], [setLoading], [setDisplay],
 * [mutateDisplay], [setError], [currentDisplayOrNull], [runAction]) are not atomic. They read and
 * write state that only this ViewModel's own context touches: [reduce], or a coroutine launched in
 * [viewModelScope] that has not switched dispatcher. Call them only from there; a call from another
 * thread, or after `withContext` to another dispatcher, can lose an update. The confinement, not a
 * lock, is what keeps a read-modify-write such as [mutateDisplay] consistent, and it also covers
 * the work a subclass spreads across suspension points, which no single atomic helper could.
 *
 * Lifecycle: [viewModelScope] is cancelled when the owning `ViewModelStore` clears the ViewModel.
 * Obtain ViewModels through a `ViewModelStoreOwner`; one held without a store is never cleared.
 *
 * @param A Action type sent from the UI.
 * @param E Best-effort, one-off effect type, such as a focus request or a non-critical message.
 * @param S Screen-specific display state.
 * @param initial Initial display data, published as `UiState.Display(initial)` and stored as the
 * latest known good state.
 */
public abstract class BaseViewModel<A : Any, E : Any, S : Any>(
    initial: S,
) : ViewModel(CoroutineScope(SupervisorJob() + defaultViewModelDispatcher())) {

    /**
     * Queue of dispatched actions waiting for [reduce].
     *
     * It is unbounded so that no action is ever dropped, whether it is dispatched before the
     * reducer coroutine first runs or while a long reducer is suspended.
     */
    private val actions = Channel<A>(capacity = Channel.UNLIMITED)

    /**
     * Queue of effects waiting for a collector.
     *
     * It is unbounded so that sending never drops an effect and never suspends the reducer.
     */
    private val effectQueue = Channel<E>(capacity = Channel.UNLIMITED)

    /**
     * One-off effects for the UI, each delivered to exactly one collector.
     *
     * Effects sent while no collector is active stay queued and are delivered, in order, when a
     * collector starts. They can therefore arrive late, after the UI that caused them is gone.
     * Several collectors compete for effects rather than each receiving a copy, so keep exactly one
     * lifecycle-aware collector. Navigation requests, transient messages and permission prompts
     * are effects; an outcome the screen must keep showing belongs in state.
     */
    public val effects: Flow<E> = effectQueue.receiveAsFlow()

    /** Backing state flow for the top-level screen state. */
    private val mutableState = MutableStateFlow<UiState<S>>(UiState.Display(initial))

    /** Top-level screen state consumed by the UI. Starts at `UiState.Display(initial)`. */
    public val state: StateFlow<UiState<S>> = mutableState.asStateFlow()

    /**
     * Last display model published through [setDisplay] or [mutateDisplay].
     *
     * Kept while the top-level state is [UiState.Loading], [UiState.Error], or [UiState.Initial],
     * so it is not atomic with [mutableState]; it is only safe because every write happens in the
     * ViewModel's own non-parallel context.
     */
    private var lastDisplay: S = initial

    init {
        viewModelScope.launch {
            for (action in actions) {
                try {
                    reduce(action)
                } catch (throwable: Throwable) {
                    // Rethrows only when this loop itself is cancelled; any other failure,
                    // including a stray CancellationException from a child, belongs to the action.
                    currentCoroutineContext().ensureActive()
                    onReduceFailure(action, throwable)
                }
            }
        }
    }

    /**
     * Queues [action] for [reduce].
     *
     * Never suspends and never drops an action: actions dispatched from the constructor, before the
     * reducer coroutine starts, are kept. Actions run strictly in dispatch order, one at a time,
     * and the queue is unbounded, so do not dispatch from an unbounded loop or use actions as a
     * background job queue. Actions still pending when the ViewModel is cleared are discarded.
     *
     * @param action Action emitted by the UI.
     */
    public fun dispatch(action: A) {
        actions.trySend(action)
    }

    /**
     * Reducer entry point implemented by subclasses.
     *
     * Runs once per dispatched action, in dispatch order, never in parallel with itself, in the
     * ViewModel's own context (see the class documentation). While a reducer suspends, later actions
     * wait. Long or cancellable work belongs in a job launched in [viewModelScope].
     *
     * A throwable that escapes, other than the cancellation of the ViewModel itself, is passed to
     * [onReduceFailure]; later actions keep being processed.
     *
     * @param action Action received from the UI.
     */
    protected abstract suspend fun reduce(action: A)

    /**
     * Handles a throwable that escaped [reduce] for [action].
     *
     * The default publishes it through [setError] as a full-screen error, so a reducer bug stays
     * visible without stopping later actions. Override to log it or to keep the screen as it is.
     * Must not throw.
     *
     * @param action Action whose reducer failed.
     * @param throwable Failure thrown by [reduce].
     */
    protected open fun onReduceFailure(action: A, throwable: Throwable) {
        setError(throwable)
    }

    /**
     * Latest known display data.
     *
     * Unlike [currentDisplayOrNull], this always has a value: it keeps the last data published
     * through [setDisplay] or [mutateDisplay] while the top-level state is loading or failed.
     * Read it only from the ViewModel's own context (see the class documentation).
     */
    protected val display: S
        get() = lastDisplay

    /**
     * Moves the screen into [UiState.Initial].
     *
     * Use only when the screen explicitly needs an untouched phase. [display] is kept. Call only
     * from the ViewModel's own context (see the class documentation).
     */
    protected fun setInitial() {
        mutableState.value = UiState.Initial
    }

    /**
     * Moves the screen into a full-screen [UiState.Loading] state. [display] is kept.
     *
     * Call only from the ViewModel's own context (see the class documentation).
     */
    protected fun setLoading() {
        mutableState.value = UiState.Loading
    }

    /**
     * Publishes [data] as [UiState.Display] and stores it as [display].
     *
     * Call only from the ViewModel's own context (see the class documentation).
     *
     * @param data New screen data to render.
     */
    protected fun setDisplay(data: S) {
        lastDisplay = data
        mutableState.value = UiState.Display(data)
    }

    /**
     * Replaces [display] without publishing it, for example to remember the query behind a
     * full-screen load that has not produced content yet.
     *
     * Call only from the ViewModel's own context (see the class documentation).
     *
     * @param data New latest known display data.
     */
    internal fun storeDisplay(data: S) {
        lastDisplay = data
    }

    /**
     * Transforms [display] and publishes the result as [UiState.Display].
     *
     * Always publishes `Display`, even when the top-level state is [UiState.Loading] or
     * [UiState.Error]; check [currentDisplayOrNull] first when that would show stale content.
     *
     * Not atomic: [transform] reads [display] and its result is written back without a
     * compare-and-set. Call only from the ViewModel's own context (see the class documentation),
     * where no other write can interleave.
     *
     * @param transform Function that receives the latest display model and returns the updated one.
     */
    protected fun mutateDisplay(transform: (S) -> S) {
        val updated = transform(lastDisplay)
        lastDisplay = updated
        mutableState.value = UiState.Display(updated)
    }

    /**
     * Moves the screen into a full-screen [UiState.Error] with a message the ViewModel supplies.
     *
     * [display] is kept. Call only from the ViewModel's own context (see the class documentation).
     *
     * @param message Localized, presentation-safe text, or an empty string when the UI words the
     * failure itself from [cause].
     * @param cause Failure behind the error, for logging or for the UI to map, or `null`.
     */
    protected fun setError(message: String, cause: Throwable? = null) {
        mutableState.value = UiState.Error(message = message, cause = cause)
    }

    /**
     * Moves the screen into a full-screen [UiState.Error] caused by [throwable].
     *
     * The message comes from [errorMessage]; [throwable] is kept as the cause. [display] is kept.
     * Call only from the ViewModel's own context (see the class documentation).
     *
     * @param throwable Failure behind the error.
     */
    protected fun setError(throwable: Throwable) {
        setError(message = errorMessage(throwable), cause = throwable)
    }

    /**
     * Maps a failure to the [UiState.Error.message] that [setError] publishes.
     *
     * The default returns an empty string: the library never shows exception text and owns no
     * wording. Override to return localized, presentation-safe text, or let the UI word the error
     * from [UiState.Error.cause].
     *
     * @param throwable Failure to describe.
     * @return Presentation-safe message, or an empty string when the UI words the failure itself.
     */
    protected open fun errorMessage(throwable: Throwable): String = ""

    /**
     * Returns the current display model only if the current top-level state is [UiState.Display].
     *
     * Unlike [display], this returns `null` while the screen is initial, loading, or failed. To run
     * work only while content is shown, use `currentDisplayOrNull()?.let { … }`, which may also call
     * suspend functions.
     *
     * @return Current displayed state, or `null` if the top-level state is not `Display`.
     */
    protected fun currentDisplayOrNull(): S? = (mutableState.value as? UiState.Display)?.data

    /**
     * Queues a best-effort, one-off effect for the UI.
     *
     * Never suspends and never drops, so a missing collector never blocks the reducer: see [effects]
     * for delivery, including late delivery when no collector is active.
     *
     * @param effect Effect to deliver to the UI.
     */
    protected fun emitEffect(effect: E) {
        effectQueue.trySend(effect)
    }

    /**
     * Runs [block] inline behind a full-screen loading state.
     *
     * Publishes [UiState.Loading], suspends the caller until [block] completes, then calls
     * [onSuccess] with its result, or [onFailure] when it throws. Nothing is launched: called from
     * [reduce], it holds later actions until it returns; launch it in [viewModelScope] when later
     * actions must not wait. Call only from the ViewModel's own context (see the class
     * documentation).
     *
     * Cancellation is never treated as a failure: when the calling coroutine is cancelled, the
     * cancellation is rethrown, [onFailure] is not called, and the state stays `Loading`. A throwable
     * thrown by [onSuccess] propagates to the caller.
     *
     * @param T Result type returned by [block].
     * @param block Suspend work to run.
     * @param onSuccess Called with the result when [block] succeeds.
     * @param onFailure Called with the failure when [block] throws. Defaults to [setError].
     */
    protected suspend fun <T> runAction(
        block: suspend () -> T,
        onSuccess: suspend (T) -> Unit,
        onFailure: suspend (Throwable) -> Unit = { throwable -> setError(throwable) },
    ) {
        setLoading()

        val result = try {
            block()
        } catch (throwable: Throwable) {
            currentCoroutineContext().ensureActive()
            onFailure(throwable)
            return
        }
        onSuccess(result)
    }
}

/**
 * Picks the dispatcher for a new ViewModel's `viewModelScope`.
 *
 * @return `Dispatchers.Main.immediate` when the platform provides a Main dispatcher, otherwise a
 * single-parallelism view of `Dispatchers.Default`.
 */
private fun defaultViewModelDispatcher(): CoroutineDispatcher = try {
    // A JVM without a Main module hands out a placeholder whose methods throw; probing it here keeps
    // that failure out of the ViewModel constructor and out of every later launch.
    Dispatchers.Main.immediate.also { it.isDispatchNeeded(EmptyCoroutineContext) }
} catch (missingMain: IllegalStateException) {
    Dispatchers.Default.limitedParallelism(1)
} catch (unsupportedMain: NotImplementedError) {
    Dispatchers.Default.limitedParallelism(1)
}
