package dev.voir.areca.keyboard

import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * Opens and closes the keyboard a [KeyboardHost] renders, and remembers which target it serves.
 *
 * Visibility is Compose state rather than a flow: a host reads it during composition, and a
 * `StateFlow` collected into state would render one frame as closed before the first value arrived.
 * Scroll requests stay a flow because scrolling is a genuine event — opening the same target twice
 * has to scroll twice, which no state value can express.
 *
 * Ownership of the keyboard's *content* is not here. A host decides what to draw from [target], so
 * one host can serve an amount field and a date field with two different keyboards.
 *
 * Holds no Compose dependencies beyond snapshot state, so it can be created and driven outside a
 * composition. Prefer [rememberKeyboardController] in UI code, which also keeps the keyboard open
 * across configuration changes and process recreation; see [Saver].
 */
@Stable
public class KeyboardController {
    /** Whether the keyboard is shown. Reading this in a composable subscribes to it. */
    public var isVisible: Boolean by mutableStateOf(false)
        private set

    /**
     * Target the keyboard is currently open for.
     *
     * `null` while the keyboard is closed, and also while it is open for no particular target —
     * `open()` with no argument, such as a keyboard shown by a toolbar button.
     */
    public var target: KeyboardTarget? by mutableStateOf(null)
        private set

    /**
     * Target of the most recent [open], kept after [close].
     *
     * What a host draws its keyboard for: equal to [target] while the keyboard is open, and still the
     * closed target while the exit animation plays, so the keyboard slides out with its keys rather
     * than as an empty surface.
     */
    internal var shownTarget: KeyboardTarget? by mutableStateOf(null)
        private set

    /**
     * Focus manager used to move and clear focus, supplied by the host. `null` until a host attaches
     * one, and while none is attached opening and closing leave focus where it is.
     */
    private var focusManager: FocusManager? = null

    /**
     * Whether an [open] since the last [close] moved focus, which is what makes clearing it on
     * [close] the keyboard's business. A keyboard opened without taking focus leaves whatever holds
     * focus alone when it closes.
     */
    private var tookFocus: Boolean = false

    /**
     * Pending scroll requests, one per open with `scroll = true`.
     *
     * Holds up to 16 requests and drops the oldest beyond that, and replays nothing: a request raised
     * while no host is collecting is lost rather than delivered to the next one.
     */
    private val bringIntoView = MutableSharedFlow<KeyboardTarget>(
        replay = 0,
        extraBufferCapacity = 16,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    /**
     * Targets to scroll clear of the keyboard, raised when a target is opened with `scroll = true`.
     *
     * A [KeyboardHost] collects this and scrolls each target once the keyboard has finished opening.
     */
    internal val bringIntoViewRequests: Flow<KeyboardTarget> = bringIntoView.asSharedFlow()

    /**
     * Supplies the focus manager used to move and clear focus.
     *
     * Called by [KeyboardHost] when it enters the composition. The most recent host wins, so a
     * controller shared by two hosts follows the one attached last.
     *
     * @param focusManager Focus manager to use.
     */
    internal fun attachFocusManager(focusManager: FocusManager) {
        this.focusManager = focusManager
    }

    /**
     * Withdraws a focus manager supplied by [attachFocusManager].
     *
     * Does nothing when another host has attached its own since, so a host leaving the composition
     * never detaches the host that replaced it.
     *
     * @param focusManager Focus manager the leaving host attached.
     */
    internal fun detachFocusManager(focusManager: FocusManager) {
        if (this.focusManager === focusManager) this.focusManager = null
    }

    /**
     * Shows the keyboard for [target].
     *
     * @param target Target to serve, or `null` for a keyboard that belongs to no particular one.
     * @param scroll Whether to scroll the target clear of the keyboard once the keyboard has finished
     *   opening. See [KeyboardHost] for how the scroll is measured.
     * @param stealFocus Whether to move focus to the target, which is what lets it receive physical
     *   key presses while the on-screen keyboard is up. With a `null` [target], focus is cleared and
     *   moves nowhere. Focus moved here is cleared again by [close].
     */
    public fun open(
        target: KeyboardTarget? = null,
        scroll: Boolean = true,
        stealFocus: Boolean = true,
    ) {
        this.target = target
        shownTarget = target
        if (stealFocus) {
            tookFocus = true
            // Clearing first matters when moving between two targets: requesting focus on the new
            // one while the old one still holds it can be refused by the focus system.
            focusManager?.clearFocus(force = true)
            target?.focusRequester?.requestFocus()
        }
        isVisible = true
        if (scroll && target != null) bringIntoView.tryEmit(target)
    }

    /**
     * Hides the keyboard and forgets the current [target].
     *
     * Clears focus only when an [open] since the last close moved it: a keyboard opened with
     * `stealFocus = false`, such as one following a read-only text field, leaves that field focused.
     */
    public fun close() {
        isVisible = false
        target = null
        if (tookFocus) focusManager?.clearFocus(force = true)
        tookFocus = false
    }

    /**
     * Closes the keyboard when it is already open for [target], and opens it otherwise.
     *
     * Tapping a *different* target while the keyboard is up switches to it rather than closing, so
     * moving between two fields never needs two taps.
     *
     * @param target Target to serve. See [open].
     * @param scroll Whether to scroll the target clear of the keyboard. See [open].
     * @param stealFocus Whether to move focus to the target. See [open].
     */
    public fun toggle(
        target: KeyboardTarget? = null,
        scroll: Boolean = true,
        stealFocus: Boolean = true,
    ) {
        if (isOpenFor(target)) close() else open(target, scroll, stealFocus)
    }

    /**
     * Whether the keyboard is open for [target].
     *
     * @param target Target to ask about.
     * @return `true` while the keyboard is visible and [target] is the one it serves.
     */
    internal fun isOpenFor(target: KeyboardTarget?): Boolean = isVisible && this.target == target

    /**
     * Reopens the keyboard after a restore, for a target with [key].
     *
     * The target that was open belongs to a composition that no longer exists, so it is stood in for
     * by one carrying the same key: enough for a host to draw the same keyboard. It is attached to
     * nothing, so focus is not restored and nothing is scrolled.
     *
     * @param hasTarget Whether the keyboard was open for a target rather than for none.
     * @param key [KeyboardTarget.key] of that target.
     */
    private fun reopen(hasTarget: Boolean, key: Any?) {
        val restored = if (hasTarget) {
            KeyboardTarget(key, FocusRequester(), BringIntoViewRequester())
        } else {
            null
        }
        target = restored
        shownTarget = restored
        isVisible = true
    }

    /** State saving for a [KeyboardController]. */
    public companion object {
        /**
         * Saves whether the keyboard is open, and the [KeyboardTarget.key] it is open for.
         *
         * A keyboard open for a target whose key cannot be saved is restored closed rather than open
         * for the wrong keyboard. A restored keyboard is open for a stand-in target carrying the
         * saved key: the host draws the same keyboard, but focus is not restored, and pressing the
         * real target opens the keyboard for it again.
         */
        public val Saver: Saver<KeyboardController, Any> = Saver(
            save = { controller ->
                val target = controller.target
                val key = target?.key
                when {
                    !controller.isVisible -> null
                    key != null && !canBeSaved(key) -> null
                    else -> arrayListOf(target != null, key)
                }
            },
            restore = { saved ->
                val state = saved as List<*>
                KeyboardController().apply { reopen(hasTarget = state[0] == true, key = state[1]) }
            },
        )
    }
}

/**
 * The [KeyboardController] in scope.
 *
 * Provided by [KeyboardHost]. Reading it outside one is a wiring mistake rather than a recoverable
 * state, so it throws instead of returning a no-op controller that would silently do nothing.
 */
public val LocalKeyboardController: ProvidableCompositionLocal<KeyboardController> =
    staticCompositionLocalOf {
        error("No KeyboardController in scope. Wrap the screen in a KeyboardHost.")
    }

/**
 * Creates a [KeyboardController] that survives recomposition, configuration changes, and process
 * recreation.
 *
 * An open keyboard stays open across an Android rotation, for a target with the same
 * [KeyboardTarget.key]; see [KeyboardController.Saver].
 *
 * @return A controller scoped to the calling composition.
 */
@Composable
public fun rememberKeyboardController(): KeyboardController =
    rememberSaveable(saver = KeyboardController.Saver) { KeyboardController() }
