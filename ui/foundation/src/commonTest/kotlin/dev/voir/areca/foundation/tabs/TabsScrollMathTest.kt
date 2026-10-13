package dev.voir.areca.foundation.tabs

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/** Tests for [TabsScrollMath], which keeps an overflowing strip's selected tab in view. */
class TabsScrollMathTest {
    /**
     * Calls [TabsScrollMath.scrollToReveal] with defaults for everything a case does not vary.
     *
     * @param tabStart Start edge of the tab, in pixels along the strip.
     * @param tabEnd End edge of the tab, in pixels along the strip.
     * @param scroll Current scroll position, in pixels.
     * @param viewport Visible extent of the strip, in pixels.
     * @param content Full extent of the strip's content, in pixels.
     * @param reversed Whether the strip scrolls in the reverse direction.
     * @return The scroll position that reveals the tab, or `null` when it is already in view.
     */
    private fun reveal(
        tabStart: Float,
        tabEnd: Float,
        scroll: Int = 0,
        viewport: Int = 300,
        content: Int = 1000,
        reversed: Boolean = false,
    ): Int? = TabsScrollMath.scrollToReveal(
        tabStart = tabStart,
        tabEnd = tabEnd,
        viewportExtent = viewport,
        contentExtent = content,
        scroll = scroll,
        reversed = reversed,
    )

    @Test
    fun `a fully visible tab does not scroll`() {
        assertNull(reveal(tabStart = 100f, tabEnd = 200f))
    }

    @Test
    fun `a tab past the end scrolls just far enough to show its end`() {
        assertEquals(250, reveal(tabStart = 450f, tabEnd = 550f))
    }

    @Test
    fun `a tab before the start scrolls back to its start`() {
        assertEquals(120, reveal(tabStart = 120f, tabEnd = 220f, scroll = 400))
    }

    @Test
    fun `the scroll never passes the end of the content`() {
        assertEquals(700, reveal(tabStart = 950f, tabEnd = 1100f))
    }

    @Test
    fun `an unmeasured strip does not scroll`() {
        assertNull(reveal(tabStart = 450f, tabEnd = 550f, viewport = 0))
    }

    @Test
    fun `a right-to-left strip counts its scroll from the right edge`() {
        // Measured from the left, this tab sits 450 to 550 from the right edge of the content.
        assertEquals(250, reveal(tabStart = 450f, tabEnd = 550f, reversed = true))
    }

    @Test
    fun `the first tab of a right-to-left strip is visible without scrolling`() {
        assertNull(reveal(tabStart = 900f, tabEnd = 1000f, reversed = true))
    }

    @Test
    fun `a tab longer than the strip is aligned by its start`() {
        assertEquals(100, reveal(tabStart = 100f, tabEnd = 600f, scroll = 300))
    }
}
