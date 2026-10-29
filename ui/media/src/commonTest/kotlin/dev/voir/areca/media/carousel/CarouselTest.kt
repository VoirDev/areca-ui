package dev.voir.areca.media.carousel

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/** Tests for the maths behind [Carousel] pages and sizes. */
class CarouselTest {
    @Test
    fun `items one to a page make a page each`() {
        assertEquals(7, carouselPageCount(itemCount = 7, group = 1))
    }

    @Test
    fun `grouped items make a short last page rather than dropping items`() {
        assertEquals(4, carouselPageCount(itemCount = 7, group = 2))
        assertEquals(3, carouselPageCount(itemCount = 9, group = 3))
    }

    @Test
    fun `no items make no pages`() {
        assertEquals(0, carouselPageCount(itemCount = 0, group = 2))
    }

    @Test
    fun `items per page share the width after the gaps`() {
        // 400 wide, two gaps of 10 between three items: 380 left, 126 each.
        assertEquals(126, perPageWidth(availableSpace = 400, spacing = 10, count = 3))
        assertEquals(400, perPageWidth(availableSpace = 400, spacing = 10, count = 1))
    }

    @Test
    fun `sizes outside their range are refused`() {
        assertFailsWith<IllegalArgumentException> { CarouselPageSize.Fraction(0f) }
        assertFailsWith<IllegalArgumentException> { CarouselPageSize.Fraction(1.2f) }
        assertFailsWith<IllegalArgumentException> { CarouselPageSize.PerPage(0) }
    }
}
