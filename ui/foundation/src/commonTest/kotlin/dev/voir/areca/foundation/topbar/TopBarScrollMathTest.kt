package dev.voir.areca.foundation.topbar

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** Tests for [TopBarScrollMath], the rules a [TopBar] collapses and travels by. */
class TopBarScrollMathTest {
    @Test
    fun `a pinned bar never moves`() {
        val offset = TopBarScrollMath.offsetAfterPreScroll(
            behavior = TopBarScrollBehavior.Pinned,
            offsetPx = 0f,
            availablePx = -200f,
            collapseRangePx = 140f,
        )

        assertEquals(0f, offset)
    }

    @Test
    fun `scrolling content up moves the bar out by the same distance`() {
        val offset = TopBarScrollMath.offsetAfterPreScroll(
            behavior = TopBarScrollBehavior.EnterAlways,
            offsetPx = 0f,
            availablePx = -30f,
            collapseRangePx = 140f,
        )

        assertEquals(-30f, offset)
    }

    @Test
    fun `the bar stops at the end of its travel however far the content scrolls`() {
        val offset = TopBarScrollMath.offsetAfterPreScroll(
            behavior = TopBarScrollBehavior.EnterAlways,
            offsetPx = -120f,
            availablePx = -500f,
            collapseRangePx = 140f,
        )

        assertEquals(-140f, offset)
    }

    @Test
    fun `enter always returns the bar the moment the gesture reverses`() {
        val offset = TopBarScrollMath.offsetAfterPreScroll(
            behavior = TopBarScrollBehavior.EnterAlways,
            offsetPx = -140f,
            availablePx = 20f,
            collapseRangePx = 140f,
        )

        assertEquals(-120f, offset)
    }

    @Test
    fun `scrolling back down never pushes the bar past fully shown`() {
        val offset = TopBarScrollMath.offsetAfterPreScroll(
            behavior = TopBarScrollBehavior.EnterAlways,
            offsetPx = -20f,
            availablePx = 500f,
            collapseRangePx = 140f,
        )

        assertEquals(0f, offset)
    }

    @Test
    fun `a bar with nowhere to travel stays put`() {
        val offset = TopBarScrollMath.offsetAfterPreScroll(
            behavior = TopBarScrollBehavior.ExitUntilCollapsed,
            offsetPx = 0f,
            availablePx = -50f,
            collapseRangePx = 0f,
        )

        assertEquals(0f, offset)
    }

    @Test
    fun `a collapsing headline gives way as the content comes up`() {
        val offset = TopBarScrollMath.offsetAfterPreScroll(
            behavior = TopBarScrollBehavior.ExitUntilCollapsed,
            offsetPx = 0f,
            availablePx = -40f,
            collapseRangePx = 140f,
        )

        assertEquals(-40f, offset)
    }

    @Test
    fun `a collapsed headline stays collapsed while there is still list above the user`() {
        // The content took the whole gesture, so none of it reaches onPostScroll — and the bar
        // must not have brought the headline back on the way past.
        val duringScroll = TopBarScrollMath.offsetAfterPreScroll(
            behavior = TopBarScrollBehavior.ExitUntilCollapsed,
            offsetPx = -140f,
            availablePx = 60f,
            collapseRangePx = 140f,
        )

        assertEquals(-140f, duringScroll)

        val contentTookItAll = TopBarScrollMath.offsetAfterPostScroll(
            behavior = TopBarScrollBehavior.ExitUntilCollapsed,
            offsetPx = -140f,
            availablePx = 0f,
            collapseRangePx = 140f,
        )

        assertEquals(-140f, contentTookItAll)
    }

    @Test
    fun `a collapsing headline returns once the content reaches its own top`() {
        // Scroll the content could not use is scroll the content has run out of.
        val offset = TopBarScrollMath.offsetAfterPostScroll(
            behavior = TopBarScrollBehavior.ExitUntilCollapsed,
            offsetPx = -140f,
            availablePx = 50f,
            collapseRangePx = 140f,
        )

        assertEquals(-90f, offset)
    }

    @Test
    fun `leftover scroll leaves the other behaviors alone`() {
        listOf(TopBarScrollBehavior.Pinned, TopBarScrollBehavior.EnterAlways).forEach { behavior ->
            assertEquals(
                -60f,
                TopBarScrollMath.offsetAfterPostScroll(
                    behavior = behavior,
                    offsetPx = -60f,
                    availablePx = 200f,
                    collapseRangePx = 140f,
                ),
            )
        }
    }

    @Test
    fun `half the travel is half collapsed`() {
        val fraction = TopBarScrollMath.collapsedFraction(offsetPx = -70f, collapseRangePx = 140f)

        assertEquals(0.5f, fraction)
    }

    @Test
    fun `an unmeasured bar reports itself expanded rather than collapsed`() {
        val fraction = TopBarScrollMath.collapsedFraction(offsetPx = 0f, collapseRangePx = 0f)

        assertEquals(0f, fraction)
    }

    @Test
    fun `the collapsed fraction stays within its range past the bar's travel`() {
        assertEquals(
            1f,
            TopBarScrollMath.collapsedFraction(offsetPx = -300f, collapseRangePx = 140f),
        )
        assertEquals(
            0f,
            TopBarScrollMath.collapsedFraction(offsetPx = 40f, collapseRangePx = 140f),
        )
    }

    @Test
    fun `content scrolled under the bar asks for a separator`() {
        val offset = TopBarScrollMath.contentOffsetAfterScroll(
            contentOffsetPx = 0f,
            consumedPx = -12f,
        )

        assertEquals(-12f, offset)
        assertTrue(TopBarScrollMath.isScrolled(offset))
    }

    @Test
    fun `content resting at its top asks for no separator`() {
        assertFalse(TopBarScrollMath.isScrolled(0f))
    }

    @Test
    fun `a settling list leaves a sub-pixel offset that is not a scroll`() {
        assertFalse(TopBarScrollMath.isScrolled(-0.25f))
    }

    @Test
    fun `overscrolling past the top builds no debt to scroll back through`() {
        val overscrolled = TopBarScrollMath.contentOffsetAfterScroll(
            contentOffsetPx = 0f,
            consumedPx = 400f,
        )

        assertEquals(0f, overscrolled)

        // One small scroll up is enough to show the separator again, rather than 400 pixels of it.
        val afterOneScroll = TopBarScrollMath.contentOffsetAfterScroll(
            contentOffsetPx = overscrolled,
            consumedPx = -10f,
        )

        assertTrue(TopBarScrollMath.isScrolled(afterOneScroll))
    }

    @Test
    fun `scrolling back to the top clears the separator`() {
        val scrolled = TopBarScrollMath.contentOffsetAfterScroll(
            contentOffsetPx = 0f,
            consumedPx = -80f,
        )
        val returned = TopBarScrollMath.contentOffsetAfterScroll(
            contentOffsetPx = scrolled,
            consumedPx = 80f,
        )

        assertFalse(TopBarScrollMath.isScrolled(returned))
    }

    @Test
    fun `an offset that still fits a new range is kept`() {
        assertEquals(-40f, TopBarScrollMath.offsetWithinRange(offsetPx = -40f, collapseRangePx = 60f))
    }

    @Test
    fun `an offset past a shorter range stops at its end`() {
        assertEquals(-60f, TopBarScrollMath.offsetWithinRange(offsetPx = -90f, collapseRangePx = 60f))
    }

    @Test
    fun `an offset with no range left is exactly zero`() {
        val offset = TopBarScrollMath.offsetWithinRange(offsetPx = -90f, collapseRangePx = 0f)

        // Compared by bits, so a negative zero does not pass as zero.
        assertEquals(0f.toRawBits(), offset.toRawBits())
    }
}
