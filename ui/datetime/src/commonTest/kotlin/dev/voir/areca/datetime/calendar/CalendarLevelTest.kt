package dev.voir.areca.datetime.calendar

import kotlinx.datetime.LocalDate
import kotlinx.datetime.YearMonth
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

/** Stepping a calendar between its days, months and years. */
class CalendarLevelTest {

    private val today = LocalDate(2026, 11, 25)

    private fun state(
        bounds: CalendarBounds = CalendarBounds.Default,
        month: YearMonth = YearMonth(2026, 11),
    ) = CalendarState(bounds = bounds, today = today, initialMonth = month)

    @Test
    fun `a calendar opens on its days`() {
        assertEquals(CalendarLevel.Days, state().level)
    }

    @Test
    fun `zooming out shows the visible month's year and then the page of years around it`() {
        val state = state()

        state.zoomOut()
        assertEquals(CalendarLevel.Months, state.level)
        assertEquals(2026, state.browsedYear)

        state.zoomOut()
        assertEquals(CalendarLevel.Years, state.level)
        // Pages count from 1900, the first year of the default bounds: 1900 + 12 * 10 = 2020.
        assertEquals(2020..2031, state.browsedYears)
        assertFalse(state.canZoomOut)
    }

    @Test
    fun `choosing a year shows its months and choosing a month shows its days`() {
        val state = state()
        state.zoomOut()
        state.zoomOut()

        state.selectYear(2031)
        assertEquals(CalendarLevel.Months, state.level)
        assertEquals(2031, state.browsedYear)

        state.selectMonth(YearMonth(2031, 3))
        assertEquals(CalendarLevel.Days, state.level)
        assertEquals(YearMonth(2031, 3), state.visibleMonth)
    }

    @Test
    fun `showing the days again leaves the month unchanged`() {
        val state = state()
        state.zoomOut()
        state.browseForward()

        state.showDays()

        assertEquals(CalendarLevel.Days, state.level)
        assertEquals(YearMonth(2026, 11), state.visibleMonth)
    }

    @Test
    fun `browsing steps a year over months and a page over years within the bounds`() {
        val bounds = CalendarBounds(LocalDate(2020, 6, 1), LocalDate(2040, 6, 30))
        val state = state(bounds = bounds)

        state.zoomOut()
        state.browseForward()
        assertEquals(2027, state.browsedYear)

        state.zoomOut()
        assertEquals(2020..2031, state.browsedYears)
        assertFalse(state.canBrowseBackward)
        state.browseForward()
        assertEquals(2032..2043, state.browsedYears)
        assertFalse(state.canBrowseForward)
        state.browseForward()
        assertEquals(2032..2043, state.browsedYears)
    }

    @Test
    fun `browsing does nothing on the days`() {
        val state = state()

        assertFalse(state.canBrowseBackward)
        assertFalse(state.canBrowseForward)
        state.browseForward()
        assertEquals(2026, state.browsedYear)
    }

    @Test
    fun `a calendar bounded to one year has no years level`() {
        val state = state(bounds = CalendarBounds(LocalDate(2026, 1, 1), LocalDate(2026, 12, 31)))

        state.zoomOut()
        assertEquals(CalendarLevel.Months, state.level)
        assertFalse(state.canZoomOut)

        state.zoomOut()
        assertEquals(CalendarLevel.Months, state.level)
    }

    @Test
    fun `a calendar bounded to one month has no months level`() {
        val state = state(bounds = CalendarBounds(LocalDate(2026, 11, 1), LocalDate(2026, 11, 30)))

        assertFalse(state.canZoomOut)
        state.zoomOut()
        assertEquals(CalendarLevel.Days, state.level)
    }

    @Test
    fun `a chosen year outside the bounds is clamped into them`() {
        val state = state(bounds = CalendarBounds(LocalDate(2020, 1, 1), LocalDate(2030, 12, 31)))

        state.selectYear(2035)

        assertEquals(2030, state.browsedYear)
    }

    @Test
    fun `a page of years holds the year and starts from the first year of the bounds`() {
        val bounds = CalendarBounds(LocalDate(2001, 1, 1), LocalDate(2100, 12, 31))

        assertEquals(2001..2012, bounds.yearPageOf(2001))
        assertEquals(2025..2036, bounds.yearPageOf(2026))
        assertEquals(2001..2012, bounds.yearPageOf(1990))
    }
}
