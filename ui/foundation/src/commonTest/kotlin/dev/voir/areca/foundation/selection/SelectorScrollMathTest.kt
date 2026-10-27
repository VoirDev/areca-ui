package dev.voir.areca.foundation.selection

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/** Tests for [SelectorScrollMath], which keeps an overflowing selector's chosen option in view. */
class SelectorScrollMathTest {
    /**
     * Calls [SelectorScrollMath.scrollToReveal] with defaults for everything a case does not vary.
     *
     * @param itemStart Start edge of the option, in pixels along the selector.
     * @param itemEnd End edge of the option, in pixels along the selector.
     * @param scroll Current scroll position, in pixels.
     * @param viewport Visible extent of the selector, in pixels.
     * @param content Full extent of the selector's content, in pixels.
     * @param reversed Whether the selector scrolls in the reverse direction.
     * @return The scroll position that reveals the option, or `null` when it is already in view.
     */
    private fun reveal(
        itemStart: Float,
        itemEnd: Float,
        scroll: Int = 0,
        viewport: Int = 300,
        content: Int = 1000,
        reversed: Boolean = false,
    ): Int? = SelectorScrollMath.scrollToReveal(
        itemStart = itemStart,
        itemEnd = itemEnd,
        viewportExtent = viewport,
        contentExtent = content,
        scroll = scroll,
        reversed = reversed,
    )

    @Test
    fun `a fully visible option does not scroll`() {
        assertNull(reveal(itemStart = 100f, itemEnd = 200f))
    }

    @Test
    fun `an option past the end scrolls just far enough to show its end`() {
        assertEquals(250, reveal(itemStart = 450f, itemEnd = 550f))
    }

    @Test
    fun `an option before the start scrolls back to its start`() {
        assertEquals(120, reveal(itemStart = 120f, itemEnd = 220f, scroll = 400))
    }

    @Test
    fun `the scroll never passes the end of the content`() {
        assertEquals(700, reveal(itemStart = 950f, itemEnd = 1100f))
    }

    @Test
    fun `an unmeasured selector does not scroll`() {
        assertNull(reveal(itemStart = 450f, itemEnd = 550f, viewport = 0))
    }

    @Test
    fun `a right-to-left selector counts its scroll from the right edge`() {
        // Measured from the left, this option sits 450 to 550 from the right edge of the content.
        assertEquals(250, reveal(itemStart = 450f, itemEnd = 550f, reversed = true))
    }

    @Test
    fun `the first option of a right-to-left selector is visible without scrolling`() {
        assertNull(reveal(itemStart = 900f, itemEnd = 1000f, reversed = true))
    }

    @Test
    fun `an option longer than the selector is aligned by its start`() {
        assertEquals(100, reveal(itemStart = 100f, itemEnd = 600f, scroll = 300))
    }
}
