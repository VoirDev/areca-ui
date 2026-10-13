package dev.voir.areca.datetime.calendar

import androidx.compose.ui.input.key.Key
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.YearMonth
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** How the keyboard moves through a calendar grid, and where the grid's one tab stop sits. */
class CalendarNavigationTest {

    private val bounds = CalendarBounds.Default

    /** Wednesday. */
    private val day = LocalDate(2026, 9, 16)

    private fun LocalDate.move(move: CalendarMove, start: DayOfWeek = DayOfWeek.MONDAY) =
        moved(move, start, bounds)

    @Test
    fun `arrows move by a day and by a week`() {
        assertEquals(LocalDate(2026, 9, 15), day.move(CalendarMove.PreviousDay))
        assertEquals(LocalDate(2026, 9, 17), day.move(CalendarMove.NextDay))
        assertEquals(LocalDate(2026, 9, 9), day.move(CalendarMove.PreviousWeek))
        assertEquals(LocalDate(2026, 9, 23), day.move(CalendarMove.NextWeek))
    }

    @Test
    fun `a day move crosses into the neighbouring month`() {
        assertEquals(LocalDate(2026, 8, 31), LocalDate(2026, 9, 1).move(CalendarMove.PreviousDay))
        assertEquals(LocalDate(2026, 10, 7), LocalDate(2026, 9, 30).move(CalendarMove.NextWeek))
    }

    @Test
    fun `home and end follow the week start of the grid`() {
        assertEquals(LocalDate(2026, 9, 14), day.move(CalendarMove.WeekStart))
        assertEquals(LocalDate(2026, 9, 20), day.move(CalendarMove.WeekEnd))
        assertEquals(LocalDate(2026, 9, 13), day.move(CalendarMove.WeekStart, DayOfWeek.SUNDAY))
        assertEquals(LocalDate(2026, 9, 19), day.move(CalendarMove.WeekEnd, DayOfWeek.SUNDAY))
    }

    @Test
    fun `a month move keeps the day or lands on the last day of a shorter month`() {
        assertEquals(LocalDate(2026, 10, 16), day.move(CalendarMove.NextMonth))
        assertEquals(LocalDate(2026, 2, 28), LocalDate(2026, 1, 31).move(CalendarMove.NextMonth))
        assertEquals(LocalDate(2026, 2, 28), LocalDate(2026, 3, 31).move(CalendarMove.PreviousMonth))
    }

    @Test
    fun `a year move lands the leap day on the 28th`() {
        assertEquals(LocalDate(2025, 9, 16), day.move(CalendarMove.PreviousYear))
        assertEquals(LocalDate(2029, 2, 28), LocalDate(2028, 2, 29).move(CalendarMove.NextYear))
    }

    @Test
    fun `a move never leaves the bounds`() {
        val narrow = CalendarBounds(LocalDate(2026, 9, 10), LocalDate(2026, 9, 20))

        assertEquals(
            LocalDate(2026, 9, 10),
            LocalDate(2026, 9, 12).moved(CalendarMove.PreviousWeek, DayOfWeek.MONDAY, narrow),
        )
        assertEquals(
            LocalDate(2026, 9, 20),
            LocalDate(2026, 9, 16).moved(CalendarMove.NextMonth, DayOfWeek.MONDAY, narrow),
        )
    }

    @Test
    fun `left and right follow the layout direction`() {
        fun move(key: Key, isRtl: Boolean) = calendarMoveFor(key, isShiftPressed = false, isRtl)

        assertEquals(CalendarMove.PreviousDay, move(Key.DirectionLeft, isRtl = false))
        assertEquals(CalendarMove.NextDay, move(Key.DirectionLeft, isRtl = true))
        assertEquals(CalendarMove.NextDay, move(Key.DirectionRight, isRtl = false))
        assertEquals(CalendarMove.PreviousDay, move(Key.DirectionRight, isRtl = true))
    }

    @Test
    fun `shift turns a page key into a year move`() {
        assertEquals(CalendarMove.PreviousMonth, calendarMoveFor(Key.PageUp, false, false))
        assertEquals(CalendarMove.PreviousYear, calendarMoveFor(Key.PageUp, true, false))
        assertEquals(CalendarMove.NextMonth, calendarMoveFor(Key.PageDown, false, false))
        assertEquals(CalendarMove.NextYear, calendarMoveFor(Key.PageDown, true, false))
    }

    @Test
    fun `keys the grid does not handle move nowhere`() {
        assertNull(calendarMoveFor(Key.Enter, false, false))
        assertNull(calendarMoveFor(Key.Tab, false, false))
    }

    @Test
    fun `the cell of a day is its week row and weekday column`() {
        val september = YearMonth(2026, 9)

        val first = LocalDate(2026, 9, 1)

        assertEquals(GridCell(row = 0, column = 1), september.cellOf(first, DayOfWeek.MONDAY))
        assertEquals(GridCell(row = 2, column = 2), september.cellOf(day, DayOfWeek.MONDAY))
        assertEquals(GridCell(row = 2, column = 3), september.cellOf(day, DayOfWeek.SUNDAY))
    }

    @Test
    fun `a spill-over day sits in the first or last row`() {
        val september = YearMonth(2026, 9)
        val before = LocalDate(2026, 8, 31)
        val after = LocalDate(2026, 10, 2)

        assertEquals(GridCell(row = 0, column = 0), september.cellOf(before, DayOfWeek.MONDAY))
        assertEquals(GridCell(row = 4, column = 4), september.cellOf(after, DayOfWeek.MONDAY))
    }

    @Test
    fun `the tab stop is the day the keyboard last rested on in this month`() {
        val stop = tabStopIn(
            month = YearMonth(2026, 9),
            active = LocalDate(2026, 9, 3),
            selection = CalendarSelection.Single(day),
            today = day,
            bounds = bounds,
        )
        assertEquals(LocalDate(2026, 9, 3), stop)
    }

    @Test
    fun `the tab stop falls to the first chosen day of the month`() {
        val stop = tabStopIn(
            month = YearMonth(2026, 9),
            active = LocalDate(2026, 8, 3),
            selection = CalendarSelection.Multiple(
                setOf(LocalDate(2026, 9, 20), LocalDate(2026, 9, 5)),
            ),
            today = day,
            bounds = bounds,
        )
        assertEquals(LocalDate(2026, 9, 5), stop)
    }

    @Test
    fun `the tab stop falls to today and then to the first reachable day`() {
        val month = YearMonth(2026, 9)

        assertEquals(day, tabStopIn(month, null, CalendarSelection.Empty, day, bounds))
        assertEquals(
            LocalDate(2026, 9, 1),
            tabStopIn(month, null, CalendarSelection.Empty, LocalDate(2027, 1, 1), bounds),
        )
        assertEquals(
            LocalDate(2026, 9, 10),
            tabStopIn(
                month = month,
                active = null,
                selection = CalendarSelection.Empty,
                today = LocalDate(2027, 1, 1),
                bounds = CalendarBounds(LocalDate(2026, 9, 10), LocalDate(2026, 12, 31)),
            ),
        )
    }

    @Test
    fun `today is out of reach when it lies outside the bounds`() {
        val past = CalendarBounds(LocalDate(2020, 1, 1), LocalDate(2020, 12, 31))

        assertFalse(canGoToToday(YearMonth(2020, 6), day, past))
    }

    @Test
    fun `going to today is offered only away from the month of today`() {
        assertTrue(canGoToToday(YearMonth(2026, 1), day, bounds))
        assertFalse(canGoToToday(YearMonth(2026, 9), day, bounds))
    }
}
