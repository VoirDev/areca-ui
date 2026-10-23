package dev.voir.areca.datetime.calendar

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

/** Tests for [calendarHeatLevel] and [calendarHeatLegendShades], how a heat calendar shades days. */
class CalendarHeatTest {

    @Test
    fun `a day with nothing has no shade`() {
        assertNull(calendarHeatLevel(0f, max = 100f, steps = 8))
        assertNull(calendarHeatLevel(-5f, max = 100f, steps = 8))
        assertNull(calendarHeatLevel(Float.NaN, max = 100f, steps = 8))
    }

    @Test
    fun `any positive figure skips the faintest shade - and the busiest day takes the strongest`() {
        assertEquals(1, calendarHeatLevel(0.5f, max = 100f, steps = 8))
        assertEquals(4, calendarHeatLevel(50f, max = 100f, steps = 8))
        assertEquals(7, calendarHeatLevel(100f, max = 100f, steps = 8))
        assertEquals(7, calendarHeatLevel(400f, max = 100f, steps = 8))
    }

    @Test
    fun `a useless maximum shades every positive day fully`() {
        assertEquals(7, calendarHeatLevel(10f, max = 0f, steps = 8))
    }

    @Test
    fun `a ramp needs two shades`() {
        assertFailsWith<IllegalArgumentException> { calendarHeatLevel(1f, max = 1f, steps = 1) }
    }

    @Test
    fun `the legend spreads its shades from the first to the last`() {
        assertEquals(listOf(0, 2, 5, 7), calendarHeatLegendShades(rampSize = 8, steps = 4))
        assertEquals(listOf(0, 7), calendarHeatLegendShades(rampSize = 8, steps = 1))
        assertEquals((0..7).toList(), calendarHeatLegendShades(rampSize = 8, steps = 20))
    }
}
