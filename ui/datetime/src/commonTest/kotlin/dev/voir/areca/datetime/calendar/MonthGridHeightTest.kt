package dev.voir.areca.datetime.calendar

import kotlin.test.Test
import kotlin.test.assertEquals

class MonthGridHeightTest {
    @Test
    fun settledMonthTakesItsOwnHeight() {
        assertEquals(5 * 40 + 4 * 2, monthGridHeight(5, 6, fraction = 0f, rowHeight = 40, weekSpacing = 2))
        assertEquals(6 * 40 + 5 * 2, monthGridHeight(5, 6, fraction = 1f, rowHeight = 40, weekSpacing = 2))
    }

    @Test
    fun heightMovesWithThePager() {
        assertEquals(229, monthGridHeight(5, 6, fraction = 0.5f, rowHeight = 40, weekSpacing = 2))
        assertEquals(229, monthGridHeight(6, 5, fraction = 0.5f, rowHeight = 40, weekSpacing = 2))
    }

    @Test
    fun fractionIsClamped() {
        assertEquals(4 * 40, monthGridHeight(4, 6, fraction = -1f, rowHeight = 40, weekSpacing = 0))
    }
}
