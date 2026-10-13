package dev.voir.areca.foundation.layout

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Tests for [collapsingHeaderPostScrollConsumption] and [collapsingHeaderOffsetAfter], the scroll
 * arithmetic of a [CollapsingHeaderLayout].
 */
class CollapsingHeaderLayoutTest {
    @Test
    fun `a small leftover never reports more than it offers`() {
        assertEquals(
            expected = 4f,
            actual = collapsingHeaderPostScrollConsumption(availableY = 4f, collapseOffsetPx = -200f),
        )
    }

    @Test
    fun `a large leftover claims only the distance the header still has to travel`() {
        assertEquals(
            expected = 30f,
            actual = collapsingHeaderPostScrollConsumption(availableY = 120f, collapseOffsetPx = -30f),
        )
    }

    @Test
    fun `an expanded header leaves the leftover to the parent`() {
        assertEquals(
            expected = 0f,
            actual = collapsingHeaderPostScrollConsumption(availableY = 50f, collapseOffsetPx = 0f),
        )
    }

    @Test
    fun `consecutive deltas within a frame each claim only what is left`() {
        val afterFirst = collapsingHeaderOffsetAfter(offsetPx = 0f, deltaY = -60f, headerHeightPx = 100)
        val afterSecond = collapsingHeaderOffsetAfter(offsetPx = afterFirst, deltaY = -60f, headerHeightPx = 100)

        assertEquals(expected = -60f, actual = afterFirst)
        assertEquals(expected = -100f, actual = afterSecond)
        // The second delta claims the 40px left, not another 60px.
        assertEquals(expected = -40f, actual = afterSecond - afterFirst)
    }

    @Test
    fun `a downward delta never expands past fully open`() {
        assertEquals(
            expected = 0f,
            actual = collapsingHeaderOffsetAfter(offsetPx = -20f, deltaY = 50f, headerHeightPx = 100),
        )
    }

    @Test
    fun `an upward leftover is not consumed`() {
        assertEquals(
            expected = 0f,
            actual = collapsingHeaderPostScrollConsumption(availableY = -50f, collapseOffsetPx = -100f),
        )
    }
}
