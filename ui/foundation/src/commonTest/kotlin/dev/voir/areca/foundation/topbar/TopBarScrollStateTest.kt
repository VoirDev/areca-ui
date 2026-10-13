package dev.voir.areca.foundation.topbar

import androidx.compose.runtime.MonotonicFrameClock
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/** Tests for [TopBarScrollStateImpl]: its nested-scroll connection, [TopBarScrollState.expand] and its saver. */
class TopBarScrollStateTest {
    /** Frame clock that advances one 60 Hz frame per request, so animations finish without real time. */
    private class FakeFrameClock : MonotonicFrameClock {
        /** Time of the last frame handed out, in nanoseconds. */
        private var frameTimeNanos = 0L

        override suspend fun <R> withFrameNanos(onFrame: (frameTimeNanos: Long) -> R): R {
            frameTimeNanos += FRAME_NANOS
            return onFrame(frameTimeNanos)
        }
    }

    /** Registry that accepts everything, as the save path is all these tests look at. */
    private val anything = SaverScope { true }

    /**
     * Creates a state with its collapse range already measured.
     *
     * @param behavior How the bar responds to scrolling.
     * @param rangePx Distance the bar can travel, in pixels.
     * @return The state.
     */
    private fun state(behavior: TopBarScrollBehavior, rangePx: Float = 100f): TopBarScrollStateImpl =
        TopBarScrollStateImpl(behavior).apply { setCollapseRange(rangePx) }

    /**
     * Offers a user drag to the bar before the content.
     *
     * @param dy Vertical drag, in pixels; negative scrolls content up.
     * @return The part of the drag the bar consumed.
     */
    private fun TopBarScrollStateImpl.preScroll(dy: Float): Offset =
        nestedScrollConnection.onPreScroll(Offset(0f, dy), NestedScrollSource.UserInput)

    /**
     * Offers the bar what is left of a user drag after the content scrolled.
     *
     * @param consumedY Part of the drag the content consumed, in pixels.
     * @param availableY Part of the drag left over, in pixels.
     * @return The part of the leftover the bar consumed.
     */
    private fun TopBarScrollStateImpl.postScroll(consumedY: Float, availableY: Float): Offset =
        nestedScrollConnection.onPostScroll(
            consumed = Offset(0f, consumedY),
            available = Offset(0f, availableY),
            source = NestedScrollSource.UserInput,
        )

    @Test
    fun `an entering bar takes the scroll it travels and leaves the rest to the content`() {
        val state = state(TopBarScrollBehavior.EnterAlways)

        val consumed = state.preScroll(-130f)

        assertEquals(Offset(0f, -100f), consumed)
        assertEquals(-100f, state.offsetPx)
        assertEquals(1f, state.collapsedFraction)
    }

    @Test
    fun `an entering bar comes back on the first downward scroll`() {
        val state = state(TopBarScrollBehavior.EnterAlways)
        state.preScroll(-60f)

        val consumed = state.preScroll(20f)

        assertEquals(Offset(0f, 20f), consumed)
        assertEquals(-40f, state.offsetPx)
    }

    @Test
    fun `a pinned bar leaves the whole gesture to the content`() {
        val state = state(TopBarScrollBehavior.Pinned)

        val consumed = state.preScroll(-60f)

        assertEquals(Offset.Zero, consumed)
        assertEquals(0f, state.offsetPx)
    }

    @Test
    fun `a collapsing bar does not expand before the content reaches its top`() {
        val state = state(TopBarScrollBehavior.ExitUntilCollapsed)
        state.preScroll(-100f)

        val consumed = state.preScroll(30f)

        assertEquals(Offset.Zero, consumed)
        assertEquals(-100f, state.offsetPx)
    }

    @Test
    fun `a collapsing bar expands with the scroll the content could not use`() {
        val state = state(TopBarScrollBehavior.ExitUntilCollapsed)
        state.preScroll(-100f)

        val consumed = state.postScroll(consumedY = 0f, availableY = 30f)

        assertEquals(Offset(0f, 30f), consumed)
        assertEquals(-70f, state.offsetPx)
    }

    @Test
    fun `content scrolled under the bar marks it as scrolled`() {
        val state = state(TopBarScrollBehavior.Pinned)

        state.postScroll(consumedY = -10f, availableY = 0f)

        assertTrue(state.isScrolled)
    }

    @Test
    fun `a bar with no measured range does not move`() {
        val state = state(TopBarScrollBehavior.EnterAlways, rangePx = 0f)

        val consumed = state.preScroll(-50f)

        assertEquals(Offset.Zero, consumed)
        assertEquals(0f, state.collapsedFraction)
    }

    @Test
    fun `a range that shrinks pulls the offset back inside it`() {
        val state = state(TopBarScrollBehavior.EnterAlways)
        state.preScroll(-100f)

        state.setCollapseRange(60f)

        assertEquals(-60f, state.offsetPx)
    }

    @Test
    fun `expand shows the bar again and clears the separator`() = runTest {
        val state = state(TopBarScrollBehavior.EnterAlways)
        state.postScroll(consumedY = -200f, availableY = 0f)
        state.preScroll(-80f)

        withContext(FakeFrameClock()) { state.expand() }

        assertEquals(0f, state.offsetPx)
        assertFalse(state.isScrolled)
    }

    @Test
    fun `saved state is restored with its behavior offset and separator`() {
        val state = state(TopBarScrollBehavior.ExitUntilCollapsed)
        state.preScroll(-40f)
        state.postScroll(consumedY = -300f, availableY = 0f)

        val saved = with(TopBarScrollStateImpl.Saver) { anything.save(state) }
        val restored = assertNotNull(saved?.let(TopBarScrollStateImpl.Saver::restore))

        assertEquals(TopBarScrollBehavior.ExitUntilCollapsed, restored.behavior)
        assertEquals(-40f, restored.offsetPx)
        assertEquals(100f, restored.collapseRangePx)
        assertTrue(restored.isScrolled)
    }

    /** Constants shared by the tests. */
    private companion object {
        /** Length of one 60 Hz frame in nanoseconds. */
        const val FRAME_NANOS = 16_666_667L
    }
}
