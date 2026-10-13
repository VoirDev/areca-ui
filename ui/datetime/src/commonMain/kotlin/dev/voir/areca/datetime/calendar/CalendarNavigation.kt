package dev.voir.areca.datetime.calendar

import androidx.compose.ui.input.key.Key
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.YearMonth
import kotlinx.datetime.daysUntil
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.yearMonth

/**
 * A step the keyboard takes through a calendar grid.
 *
 * The moves of the WAI-ARIA date-grid pattern: arrows by day and by week, Home and End to the ends
 * of the week, Page Up and Page Down by month, and with Shift by year.
 */
internal enum class CalendarMove {
    /** One day earlier. */
    PreviousDay,

    /** One day later. */
    NextDay,

    /** Seven days earlier. */
    PreviousWeek,

    /** Seven days later. */
    NextWeek,

    /** The first day of the week the day is in, as the grid's columns order it. */
    WeekStart,

    /** The last day of the week the day is in, as the grid's columns order it. */
    WeekEnd,

    /** The same day of the previous month, or that month's last day when it is shorter. */
    PreviousMonth,

    /** The same day of the next month, or that month's last day when it is shorter. */
    NextMonth,

    /** The same day a year earlier; 29 February lands on the 28th. */
    PreviousYear,

    /** The same day a year later; 29 February lands on the 28th. */
    NextYear,
}

/**
 * The move a key asks for.
 *
 * Left and right follow the screen: in a right-to-left layout the week runs right to left, so the
 * left arrow moves a day later.
 *
 * @param key Key pressed.
 * @param isShiftPressed Whether Shift is held, which turns a month move into a year move.
 * @param isRtl Whether the grid is laid out right to left.
 * @return The move, or `null` for a key the grid does not handle.
 */
internal fun calendarMoveFor(key: Key, isShiftPressed: Boolean, isRtl: Boolean): CalendarMove? =
    when (key) {
        Key.DirectionLeft -> if (isRtl) CalendarMove.NextDay else CalendarMove.PreviousDay
        Key.DirectionRight -> if (isRtl) CalendarMove.PreviousDay else CalendarMove.NextDay
        Key.DirectionUp -> CalendarMove.PreviousWeek
        Key.DirectionDown -> CalendarMove.NextWeek
        Key.MoveHome -> CalendarMove.WeekStart
        Key.MoveEnd -> CalendarMove.WeekEnd
        Key.PageUp -> if (isShiftPressed) CalendarMove.PreviousYear else CalendarMove.PreviousMonth
        Key.PageDown -> if (isShiftPressed) CalendarMove.NextYear else CalendarMove.NextMonth
        else -> null
    }

/**
 * The day a move lands on.
 *
 * @param move Step to take.
 * @param startDayOfWeek Day each grid row begins on, which decides where a week starts and ends.
 * @param bounds Days the calendar can reach. The result is pulled inside them, so a move never
 *   leaves the calendar.
 * @return The day reached, clamped into [bounds].
 */
internal fun LocalDate.moved(
    move: CalendarMove,
    startDayOfWeek: DayOfWeek,
    bounds: CalendarBounds,
): LocalDate {
    val target = when (move) {
        CalendarMove.PreviousDay -> minus(1, DateTimeUnit.DAY)
        CalendarMove.NextDay -> plus(1, DateTimeUnit.DAY)
        CalendarMove.PreviousWeek -> minus(DAYS_IN_WEEK, DateTimeUnit.DAY)
        CalendarMove.NextWeek -> plus(DAYS_IN_WEEK, DateTimeUnit.DAY)
        CalendarMove.WeekStart -> minus(dayOfWeek.columnIn(startDayOfWeek), DateTimeUnit.DAY)
        CalendarMove.WeekEnd ->
            plus(DAYS_IN_WEEK - 1 - dayOfWeek.columnIn(startDayOfWeek), DateTimeUnit.DAY)
        CalendarMove.PreviousMonth -> minus(1, DateTimeUnit.MONTH)
        CalendarMove.NextMonth -> plus(1, DateTimeUnit.MONTH)
        CalendarMove.PreviousYear -> minus(1, DateTimeUnit.YEAR)
        CalendarMove.NextYear -> plus(1, DateTimeUnit.YEAR)
    }
    return target.coerceIn(bounds.minDate, bounds.maxDate)
}

/**
 * Where a day's cell sits in a month's grid.
 *
 * @property row Week row, from `0` at the top.
 * @property column Column, from `0` at the start edge.
 */
internal data class GridCell(val row: Int, val column: Int)

/**
 * The cell [date] occupies on this month's page.
 *
 * Works for the spill-over days either side too, which sit in the first and last rows.
 *
 * @param date Day drawn on this page: of this month, or adjacent to it.
 * @param startDayOfWeek Day each row begins on.
 * @return Row and column of the day's cell.
 */
internal fun YearMonth.cellOf(date: LocalDate, startDayOfWeek: DayOfWeek): GridCell {
    val leadingCells = firstDay.dayOfWeek.columnIn(startDayOfWeek)
    val index = leadingCells + firstDay.daysUntil(date)
    return GridCell(row = index.floorDiv(DAYS_IN_WEEK), column = index.mod(DAYS_IN_WEEK))
}

/**
 * The one day of a month's grid that takes keyboard focus when the grid is tabbed into.
 *
 * In order of preference: the day the keyboard last rested on, the first chosen day, today, and
 * otherwise the first day of the month the calendar can reach. One day only, so the grid is one tab
 * stop and the arrows move within it.
 *
 * @param month Month the grid is showing.
 * @param active Day the keyboard last rested on, or `null` before it has rested anywhere.
 * @param selection Days drawn as chosen.
 * @param today The calendar's today.
 * @param bounds Days the calendar can reach.
 * @return The day of [month] that holds the tab stop.
 */
internal fun tabStopIn(
    month: YearMonth,
    active: LocalDate?,
    selection: CalendarSelection,
    today: LocalDate,
    bounds: CalendarBounds,
): LocalDate {
    if (active != null && active.yearMonth == month) return active
    val days = generateSequence(month.firstDay) { previous ->
        previous.plus(1, DateTimeUnit.DAY).takeIf { it.yearMonth == month }
    }
    days.firstOrNull { it in selection }?.let { return it }
    if (today.yearMonth == month) return today
    return month.firstDay.coerceIn(bounds.minDate, bounds.maxDate)
}

/**
 * Whether a control returning the calendar to today has anywhere to go.
 *
 * @param visibleMonth Month the calendar is showing.
 * @param today The calendar's today.
 * @param bounds Months the calendar can reach.
 * @return `true` when today's month is reachable and not already showing.
 */
internal fun canGoToToday(
    visibleMonth: YearMonth,
    today: LocalDate,
    bounds: CalendarBounds,
): Boolean = today.yearMonth in bounds && visibleMonth != today.yearMonth
