package dev.voir.areca.keyboard

import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

/**
 * Behaviour of [KeyboardController] — visibility, target tracking, scroll requests, focus clearing,
 * and saving.
 *
 * Focus is asserted only through the focus manager's `clearFocus`, with targets left `null` where a
 * focus request would be made: a [FocusRequester] cannot be exercised outside a composition.
 */
class KeyboardControllerTest {
    @Test
    fun `starts closed with no target`() {
        val controller = KeyboardController()

        assertFalse(controller.isVisible)
        assertNull(controller.target)
    }

    @Test
    fun `open with no target shows the keyboard`() {
        val controller = KeyboardController()

        controller.open()

        assertTrue(controller.isVisible)
        assertNull(controller.target)
    }

    @Test
    fun `open records the target it was opened for`() {
        val controller = KeyboardController()
        val target = keyboardTarget()

        controller.open(target)

        assertTrue(controller.isVisible)
        assertSame(target, controller.target)
    }

    @Test
    fun `close hides the keyboard and forgets the target`() {
        val controller = KeyboardController()
        controller.open(keyboardTarget())

        controller.close()

        assertFalse(controller.isVisible)
        assertNull(controller.target)
    }

    @Test
    fun `close keeps the last target shown so the keyboard can slide out with its keys`() {
        val controller = KeyboardController()
        val target = keyboardTarget()
        controller.open(target)

        controller.close()

        assertSame(target, controller.shownTarget)
    }

    @Test
    fun `opening for no target shows no target`() {
        val controller = KeyboardController()
        controller.open(keyboardTarget())
        controller.close()

        controller.open()

        assertNull(controller.shownTarget)
    }

    @Test
    fun `toggle closes the keyboard when it is already open for that target`() {
        val controller = KeyboardController()
        val target = keyboardTarget()
        controller.open(target)

        controller.toggle(target)

        assertFalse(controller.isVisible)
    }

    @Test
    fun `toggle switches to a different target rather than closing`() {
        val controller = KeyboardController()
        val amount = keyboardTarget(key = "amount")
        val date = keyboardTarget(key = "date")
        controller.open(amount)

        controller.toggle(date)

        assertTrue(controller.isVisible)
        assertSame(date, controller.target)
    }

    @Test
    fun `toggle opens the keyboard when it is closed`() {
        val controller = KeyboardController()
        val target = keyboardTarget()

        controller.toggle(target)

        assertTrue(controller.isVisible)
        assertSame(target, controller.target)
    }

    @Test
    fun `opening a target raises one scroll request per open`() = runTest {
        val controller = KeyboardController()
        val target = keyboardTarget()
        val requests = mutableListOf<KeyboardTarget>()
        val collection = collectRequests(controller, requests)

        // Two opens of the same target must scroll twice: the second is a real request, not a
        // repeated state value that a conflating stream would swallow.
        controller.open(target)
        controller.close()
        controller.open(target)
        collection.cancel()

        assertEquals(listOf(target, target), requests)
    }

    @Test
    fun `opening without scroll raises no request`() = runTest {
        val controller = KeyboardController()
        val target = keyboardTarget()
        val requests = mutableListOf<KeyboardTarget>()
        val collection = collectRequests(controller, requests)

        controller.open(target, scroll = false)
        collection.cancel()

        assertTrue(requests.isEmpty())
    }

    @Test
    fun `close clears focus the keyboard took when it opened`() {
        val controller = KeyboardController()
        val focus = RecordingFocusManager()
        controller.attachFocusManager(focus)
        controller.open(stealFocus = true)
        focus.clears = 0

        controller.close()

        assertEquals(1, focus.clears)
    }

    @Test
    fun `close leaves focus alone when the keyboard never took it`() {
        val controller = KeyboardController()
        val focus = RecordingFocusManager()
        controller.attachFocusManager(focus)
        controller.open(keyboardTarget(), stealFocus = false)

        controller.close()

        assertEquals(0, focus.clears)
    }

    @Test
    fun `a second close does not clear focus again`() {
        val controller = KeyboardController()
        val focus = RecordingFocusManager()
        controller.attachFocusManager(focus)
        controller.open(stealFocus = true)
        controller.close()
        focus.clears = 0

        controller.close()

        assertEquals(0, focus.clears)
    }

    @Test
    fun `a host leaving does not detach the host that replaced it`() {
        val controller = KeyboardController()
        val first = RecordingFocusManager()
        val second = RecordingFocusManager()
        controller.attachFocusManager(first)
        controller.attachFocusManager(second)

        controller.detachFocusManager(first)
        controller.open(stealFocus = true)

        assertEquals(1, second.clears)
        assertEquals(0, first.clears)
    }

    @Test
    fun `a closed keyboard saves nothing`() {
        assertNull(save(KeyboardController()))
    }

    @Test
    fun `an open keyboard restores open for a target with the same key`() {
        val controller = KeyboardController()
        controller.open(keyboardTarget(key = "amount"))

        val restored = restore(save(controller))

        assertTrue(restored.isVisible)
        assertEquals("amount", restored.target?.key)
        assertSame(restored.target, restored.shownTarget)
    }

    @Test
    fun `a keyboard open for no target restores open for none`() {
        val controller = KeyboardController()
        controller.open()

        val restored = restore(save(controller))

        assertTrue(restored.isVisible)
        assertNull(restored.target)
    }

    @Test
    fun `a keyboard open for a key that cannot be saved is not saved`() {
        val controller = KeyboardController()
        controller.open(keyboardTarget(key = Any()))

        assertNull(save(controller, canBeSaved = { it is String }))
    }

    /**
     * Saves [controller] with [KeyboardController.Saver].
     *
     * @param controller Controller to save.
     * @param canBeSaved Which values the saving registry accepts.
     * @return The saved value, or `null` when nothing is saved.
     */
    private fun save(controller: KeyboardController, canBeSaved: (Any) -> Boolean = { true }): Any? =
        with(KeyboardController.Saver) { SaverScope(canBeSaved).save(controller) }

    /**
     * Restores a controller saved by [save].
     *
     * @param saved The saved value. Must not be `null`.
     * @return The restored controller.
     */
    private fun restore(saved: Any?): KeyboardController =
        assertNotNull(KeyboardController.Saver.restore(assertNotNull(saved)))

    /**
     * Starts collecting [KeyboardController.bringIntoViewRequests] into [into], eagerly.
     *
     * The flow has no replay, so a collector that has not subscribed yet misses everything emitted
     * before it. An unconfined dispatcher subscribes at `launch` rather than at the next suspension
     * point, which is what lets a test emit on the very next line.
     *
     * @param controller Controller to observe.
     * @param into List the requests are appended to.
     * @return The collecting job, which the caller must cancel.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    private fun TestScope.collectRequests(
        controller: KeyboardController,
        into: MutableList<KeyboardTarget>,
    ) = launch(UnconfinedTestDispatcher(testScheduler)) {
        controller.bringIntoViewRequests.toList(into)
    }

    /**
     * Builds a target without a composition.
     *
     * @param key Identity carried on the target.
     * @return A target whose requesters are unattached, which is all these tests need.
     */
    private fun keyboardTarget(key: Any? = null) = KeyboardTarget(
        key = key,
        focusRequester = FocusRequester(),
        bringIntoViewRequester = BringIntoViewRequester(),
    )

    /** A focus manager that counts how often focus is cleared. */
    private class RecordingFocusManager : FocusManager {
        /** Calls to [clearFocus] so far. */
        var clears: Int = 0

        /** Counts the call. */
        override fun clearFocus(force: Boolean) {
            clears++
        }

        /** Moves nothing. */
        override fun moveFocus(focusDirection: FocusDirection): Boolean = false
    }
}
