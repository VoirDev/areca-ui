package dev.voir.areca.viewmodel

import androidx.lifecycle.ViewModelStore
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertSame
import kotlin.test.assertTrue
import kotlin.time.Duration

/** Verifies action ordering, failure containment, effects, errors, and [BaseViewModel.runAction]. */
@OptIn(ExperimentalCoroutinesApi::class)
class BaseViewModelTest {
    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `actions dispatched from init are kept until the reducer starts`() = runTest {
        val viewModel = CounterViewModel(incrementOnInit = true)
        viewModel.dispatch(CounterAction.Increment)

        advanceUntilIdle()

        assertEquals(UiState.Display(2), viewModel.state.value)
    }

    @Test
    fun `actions are reduced in dispatch order`() = runTest {
        val viewModel = CounterViewModel()
        viewModel.dispatch(CounterAction.Record("first"))
        viewModel.dispatch(CounterAction.SlowRecord("second"))
        viewModel.dispatch(CounterAction.Record("third"))

        advanceUntilIdle()

        assertEquals(listOf("first", "second", "third"), viewModel.recorded)
    }

    @Test
    fun `a refresh reserved before the reducer starts reaches runReserved`() = runTest {
        val viewModel = CounterViewModel()

        assertTrue(viewModel.refresh())
        advanceUntilIdle()

        assertEquals(UiState.Display(100), viewModel.state.value)
        assertTrue(viewModel.refresh())
        advanceUntilIdle()
        assertEquals(UiState.Display(200), viewModel.state.value)
    }

    @Test
    fun `a throwing reducer publishes an error and later actions still run`() = runTest {
        val viewModel = CounterViewModel()
        viewModel.dispatch(CounterAction.Fail)
        advanceUntilIdle()

        val error = assertIs<UiState.Error>(viewModel.state.value)
        assertIs<IllegalStateException>(error.cause)

        viewModel.dispatch(CounterAction.Increment)
        viewModel.dispatch(CounterAction.Increment)
        advanceUntilIdle()
        assertEquals(UiState.Display(2), viewModel.state.value)
    }

    @Test
    fun `a stray cancellation from a reducer is a failure of that action only`() = runTest {
        val viewModel = CounterViewModel()
        viewModel.dispatch(CounterAction.ThrowStrayCancellation)
        viewModel.dispatch(CounterAction.Increment)

        advanceUntilIdle()

        assertEquals(UiState.Display(1), viewModel.state.value)
        assertIs<CancellationException>(viewModel.reduceFailures.single())
    }

    @Test
    fun `runAction shows loading and then the result`() = runTest {
        val viewModel = CounterViewModel()
        viewModel.dispatch(CounterAction.Load(result = 5))
        runCurrent()
        assertEquals(UiState.Loading, viewModel.state.value)

        advanceUntilIdle()

        assertEquals(UiState.Display(5), viewModel.state.value)
    }

    @Test
    fun `runAction failure reaches the default error`() = runTest {
        val viewModel = CounterViewModel()
        viewModel.dispatch(CounterAction.Load(result = null))

        advanceUntilIdle()

        val error = assertIs<UiState.Error>(viewModel.state.value)
        assertIs<IllegalArgumentException>(error.cause)
    }

    @Test
    fun `clearing the ViewModel during runAction publishes no error`() = runTest {
        val viewModel = CounterViewModel()
        val store = ViewModelStore()
        store.put("counter", viewModel)
        viewModel.dispatch(CounterAction.LoadForever)
        runCurrent()

        store.clear()
        advanceUntilIdle()

        assertEquals(UiState.Loading, viewModel.state.value)
        assertEquals(emptyList(), viewModel.launchFailures)
    }

    @Test
    fun `effects sent without a collector never block the reducer`() = runTest {
        val viewModel = CounterViewModel()
        viewModel.dispatch(CounterAction.EmitEffects(count = 100))
        viewModel.dispatch(CounterAction.Increment)
        advanceUntilIdle()
        assertEquals(UiState.Display(1), viewModel.state.value)

        val received = mutableListOf<String>()
        val collector = launch { viewModel.effects.collect { received += it } }
        advanceUntilIdle()
        collector.cancel()

        assertEquals(List(100) { "effect $it" }, received)
    }

    @Test
    fun `setError with a throwable never shows the exception text`() = runTest {
        val viewModel = CounterViewModel()
        val failure = IllegalStateException("SQL error near x")
        viewModel.dispatch(CounterAction.FailWith(failure))

        advanceUntilIdle()

        val error = assertIs<UiState.Error>(viewModel.state.value)
        assertEquals("", error.message)
        assertSame(failure, error.cause)
    }

    @Test
    fun `errorMessage supplies the text for setError with a throwable`() = runTest {
        val viewModel = CounterViewModel(errorText = "Something went wrong")
        viewModel.dispatch(CounterAction.FailWith(IllegalStateException("internal")))

        advanceUntilIdle()

        assertEquals(UiState.Error("Something went wrong", viewModel.lastFailure), viewModel.state.value)
    }

    @Test
    fun `setError with a message publishes that message`() = runTest {
        val viewModel = CounterViewModel()
        viewModel.dispatch(CounterAction.ShowMessage("Could not load"))

        advanceUntilIdle()

        assertEquals(UiState.Error("Could not load"), viewModel.state.value)
    }
}

/** Inputs accepted by [CounterViewModel]. */
private sealed interface CounterAction {
    data object Increment : CounterAction
    data class Record(val label: String) : CounterAction
    data class SlowRecord(val label: String) : CounterAction
    data object Refresh : CounterAction
    data object Fail : CounterAction
    data object ThrowStrayCancellation : CounterAction
    data class FailWith(val failure: Throwable) : CounterAction
    data class ShowMessage(val message: String) : CounterAction
    data class Load(val result: Int?) : CounterAction
    data object LoadForever : CounterAction
    data class EmitEffects(val count: Int) : CounterAction
}

/** Counter used to exercise the [BaseViewModel] contract. */
private class CounterViewModel(
    incrementOnInit: Boolean = false,
    private val errorText: String? = null,
) : BaseViewModel<CounterAction, String, Int>(initial = 0) {
    private val refreshGate = RefreshGate(cooldown = Duration.ZERO)
    val recorded = mutableListOf<String>()
    val reduceFailures = mutableListOf<Throwable>()
    val launchFailures = mutableListOf<Throwable>()
    var lastFailure: Throwable? = null

    init {
        if (incrementOnInit) dispatch(CounterAction.Increment)
    }

    fun refresh(): Boolean = refreshGate.tryReserve { dispatch(CounterAction.Refresh) }

    override suspend fun reduce(action: CounterAction) {
        when (action) {
            CounterAction.Increment -> mutateDisplay { it + 1 }
            is CounterAction.Record -> recorded += action.label
            is CounterAction.SlowRecord -> {
                delay(1_000)
                recorded += action.label
            }
            CounterAction.Refresh -> refreshGate.runReserved { mutateDisplay { it + 100 } }
            CounterAction.Fail -> error("Reducer failed")
            CounterAction.ThrowStrayCancellation -> throw CancellationException("Not this ViewModel")
            is CounterAction.FailWith -> {
                lastFailure = action.failure
                setError(action.failure)
            }
            is CounterAction.ShowMessage -> setError(action.message)
            is CounterAction.Load -> runAction(
                block = {
                    delay(1_000)
                    requireNotNull(action.result) { "No result" }
                },
                onSuccess = { setDisplay(it) },
            )
            CounterAction.LoadForever -> runAction(
                block = { awaitCancellation() },
                onSuccess = { },
                onFailure = { launchFailures += it },
            )
            is CounterAction.EmitEffects -> repeat(action.count) { emitEffect("effect $it") }
        }
    }

    override fun onReduceFailure(action: CounterAction, throwable: Throwable) {
        reduceFailures += throwable
        super.onReduceFailure(action, throwable)
    }

    override fun errorMessage(throwable: Throwable): String = errorText ?: super.errorMessage(throwable)
}
