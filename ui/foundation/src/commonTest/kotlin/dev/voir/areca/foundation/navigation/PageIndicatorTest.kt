package dev.voir.areca.foundation.navigation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/** Tests for the maths behind the [PageIndicator] variants. */
class PageIndicatorTest {
    @Test
    fun `a shrinking window stays put while the page moves inside it`() {
        assertEquals(0, shrinkingWindowStart(page = 3, pageCount = 20, window = 5, previous = 0))
        assertEquals(4, shrinkingWindowStart(page = 6, pageCount = 20, window = 5, previous = 4))
    }

    @Test
    fun `a shrinking window moves just far enough to take a page past its end`() {
        assertEquals(1, shrinkingWindowStart(page = 5, pageCount = 20, window = 5, previous = 0))
        assertEquals(8, shrinkingWindowStart(page = 12, pageCount = 20, window = 5, previous = 0))
    }

    @Test
    fun `a shrinking window moves back to a page before its start`() {
        assertEquals(2, shrinkingWindowStart(page = 2, pageCount = 20, window = 5, previous = 6))
    }

    @Test
    fun `a shrinking window never runs past the last pages`() {
        assertEquals(15, shrinkingWindowStart(page = 19, pageCount = 20, window = 5, previous = 40))
        assertEquals(0, shrinkingWindowStart(page = 2, pageCount = 3, window = 5, previous = 0))
    }

    @Test
    fun `dots inside the window are full size and its edges shrink only with pages beyond`() {
        // Window 3..7 of 20: pages lie before and after it.
        assertEquals(0.66f, shrinkingScale(page = 3, current = 5, windowStart = 3, window = 5, pageCount = 20))
        assertEquals(1f, shrinkingScale(page = 5, current = 5, windowStart = 3, window = 5, pageCount = 20))
        assertEquals(0.66f, shrinkingScale(page = 7, current = 5, windowStart = 3, window = 5, pageCount = 20))
        // At the start nothing lies before the window, so its first dot stays full.
        assertEquals(1f, shrinkingScale(page = 0, current = 2, windowStart = 0, window = 5, pageCount = 20))
    }

    @Test
    fun `the page on screen is full size even at the window's edge`() {
        assertEquals(1f, shrinkingScale(page = 7, current = 7, windowStart = 3, window = 5, pageCount = 20))
    }

    @Test
    fun `the dot either side of the window is small and the rest are hidden`() {
        assertEquals(0.4f, shrinkingScale(page = 2, current = 5, windowStart = 3, window = 5, pageCount = 20))
        assertEquals(0.4f, shrinkingScale(page = 8, current = 5, windowStart = 3, window = 5, pageCount = 20))
        assertEquals(0f, shrinkingScale(page = 1, current = 5, windowStart = 3, window = 5, pageCount = 20))
        assertEquals(0f, shrinkingScale(page = 12, current = 5, windowStart = 3, window = 5, pageCount = 20))
    }

    @Test
    fun `segments fill before the current page and to its progress at it`() {
        assertEquals(1f, segmentFill(page = 0, current = 2, progress = 0.3f))
        assertEquals(0.3f, segmentFill(page = 2, current = 2, progress = 0.3f))
        assertEquals(0f, segmentFill(page = 3, current = 2, progress = 0.3f))
        assertEquals(1f, segmentFill(page = 2, current = 2, progress = 4f))
    }

    @Test
    fun `a shrinking window narrower than three dots is refused`() {
        assertFailsWith<IllegalArgumentException> { PageIndicatorVariant.Shrinking(window = 2) }
    }
}
