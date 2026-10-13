package dev.voir.areca.datetime.calendar

import kotlinx.datetime.LocalDate
import kotlinx.datetime.YearMonth
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** The window a calendar may page through. */
class CalendarBoundsTest {

    private val bounds = CalendarBounds(
        minDate = LocalDate(2026, 3, 10),
        maxDate = LocalDate(2027, 6, 15),
    )

    @Test
    fun `a window ending before it starts is rejected`() {
        assertFailsWith<IllegalArgumentException> {
            CalendarBounds(minDate = LocalDate(2026, 5, 1), maxDate = LocalDate(2026, 4, 30))
        }
    }

    @Test
    fun `a single-day window is one page`() {
        val single = CalendarBounds(LocalDate(2026, 5, 4), LocalDate(2026, 5, 4))

        assertEquals(1, single.monthCount)
        assertEquals(YearMonth(2026, 5), single.monthAt(0))
    }

    @Test
    fun `the page count covers both partial end months`() {
        // March to June inclusive: 10 months of 2026 plus 6 of 2027.
        assertEquals(16, bounds.monthCount)
    }

    @Test
    fun `a month maps to a page and back`() {
        (0 until bounds.monthCount).forEach { page ->
            assertEquals(page, bounds.pageOf(bounds.monthAt(page)), "page $page")
        }
    }

    @Test
    fun `the first and last pages are the months the window ends in`() {
        assertEquals(YearMonth(2026, 3), bounds.monthAt(0))
        assertEquals(YearMonth(2027, 6), bounds.monthAt(bounds.monthCount - 1))
    }

    @Test
    fun `a month outside the window clamps to the nearest page`() {
        assertEquals(0, bounds.pageOf(YearMonth(1999, 1)))
        assertEquals(bounds.monthCount - 1, bounds.pageOf(YearMonth(2099, 12)))
    }

    @Test
    fun `a page outside the window clamps to the nearest month`() {
        assertEquals(YearMonth(2026, 3), bounds.monthAt(-5))
        assertEquals(YearMonth(2027, 6), bounds.monthAt(500))
    }

    @Test
    fun `coercing pulls a month to the nearer end and leaves reachable months alone`() {
        assertEquals(bounds.minMonth, bounds.coerce(YearMonth(2020, 1)))
        assertEquals(bounds.maxMonth, bounds.coerce(YearMonth(2030, 1)))
        assertEquals(YearMonth(2026, 9), bounds.coerce(YearMonth(2026, 9)))
    }

    @Test
    fun `the window contains its ends but not the days beyond them`() {
        assertTrue(LocalDate(2026, 3, 10) in bounds)
        assertTrue(LocalDate(2027, 6, 15) in bounds)
        assertFalse(LocalDate(2026, 3, 9) in bounds)
        assertFalse(LocalDate(2027, 6, 16) in bounds)
    }

    @Test
    fun `the whole of an end month is reachable even when only part of it is selectable`() {
        // The 9th is before minDate, so it cannot be chosen — but March is still a page, because a
        // calendar that hid it would have no way to show the 10th.
        assertTrue(YearMonth(2026, 3) in bounds)
        assertFalse(LocalDate(2026, 3, 9) in bounds)
    }

    @Test
    fun `only the end years have a partial month column`() {
        assertEquals(3..12, bounds.monthNumbersIn(2026))
        assertEquals(1..6, bounds.monthNumbersIn(2027))
    }

    @Test
    fun `a year in the middle of the window offers every month`() {
        val wide = CalendarBounds(LocalDate(2020, 5, 1), LocalDate(2030, 5, 1))

        assertEquals(1..12, wide.monthNumbersIn(2025))
    }

    @Test
    fun `a single-year window narrows at both ends at once`() {
        val oneYear = CalendarBounds(LocalDate(2026, 4, 1), LocalDate(2026, 9, 30))

        assertEquals(4..9, oneYear.monthNumbersIn(2026))
    }

    @Test
    fun `a year outside the window offers no months`() {
        assertTrue(bounds.monthNumbersIn(2019).isEmpty())
        assertTrue(bounds.monthNumbersIn(2031).isEmpty())
    }

    @Test
    fun `the year range spans both ends of the window`() {
        assertEquals(2026..2027, bounds.yearRange)
    }

    @Test
    fun `a window around a day covers whole years on both sides`() {
        val around = CalendarBounds.around(LocalDate(2026, 9, 12), years = 5)

        assertEquals(LocalDate(2021, 1, 1), around.minDate)
        assertEquals(LocalDate(2031, 12, 31), around.maxDate)
    }

    @Test
    fun `a window around a day with no years either side is that one year`() {
        val around = CalendarBounds.around(LocalDate(2026, 9, 12), years = 0)

        assertEquals(2026..2026, around.yearRange)
        assertEquals(12, around.monthCount)
    }

    @Test
    fun `a negative span is rejected`() {
        assertFailsWith<IllegalArgumentException> {
            CalendarBounds.around(LocalDate(2026, 9, 12), years = -1)
        }
    }

    @Test
    fun `absent ends fall back to the default window`() {
        val open = CalendarBounds.of(minDate = null, maxDate = LocalDate(2026, 12, 31))

        assertEquals(CalendarBounds.Default.minDate, open.minDate)
        assertEquals(LocalDate(2026, 12, 31), open.maxDate)
    }

    @Test
    fun `the default window reaches back far enough for a date of birth`() {
        assertTrue(LocalDate(1937, 4, 2) in CalendarBounds.Default)
    }
}
