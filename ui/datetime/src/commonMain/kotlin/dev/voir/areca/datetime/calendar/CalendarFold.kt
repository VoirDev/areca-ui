package dev.voir.areca.datetime.calendar

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.YearMonth
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.yearMonth

/**
 * The first day of the week holding the receiver.
 *
 * @param startDayOfWeek Day each week begins on.
 * @return The receiver, or the latest earlier day that is [startDayOfWeek].
 */
internal fun LocalDate.weekStart(startDayOfWeek: DayOfWeek): LocalDate =
    minus(dayOfWeek.columnIn(startDayOfWeek), DateTimeUnit.DAY)

/**
 * Which row of [month]'s grid holds the receiver.
 *
 * @param month Month whose grid is meant.
 * @param startDayOfWeek Day each row begins on.
 * @return Row index from `0`; `0` when the receiver lies before [month], and the last row when it
 *   lies after it.
 */
internal fun LocalDate.rowIn(month: YearMonth, startDayOfWeek: DayOfWeek): Int = when {
    yearMonth < month -> 0
    yearMonth > month -> month.weekCount(startDayOfWeek) - 1
    else -> (month.firstDay.dayOfWeek.columnIn(startDayOfWeek) + day - 1) / DAYS_IN_WEEK
}

/**
 * The day a folded calendar moves to when stepped by [weeks]: the same weekday that many weeks away,
 * pulled back inside [bounds] when it would fall outside them.
 *
 * @param weeks Weeks to step; negative steps back.
 * @param startDayOfWeek Day each week begins on.
 * @param bounds Days the calendar can reach.
 * @return The day to show, or `null` when the step would not leave the receiver's week — the
 *   receiver already sits in the first or last reachable week.
 */
internal fun LocalDate.foldStep(weeks: Int, startDayOfWeek: DayOfWeek, bounds: CalendarBounds): LocalDate? {
    val target = plus(weeks * DAYS_IN_WEEK, DateTimeUnit.DAY).coerceIn(bounds.minDate, bounds.maxDate)
    return target.takeIf { it.weekStart(startDayOfWeek) != weekStart(startDayOfWeek) }
}

/**
 * Height of a folding grid: [fullHeight] open, one row folded, and in between while it folds.
 *
 * @param fullHeight Height of the whole grid.
 * @param rowHeight Height of one week row.
 * @param fraction How far it is folded, `0` open to `1` one row.
 * @return Height in pixels.
 */
internal fun foldedHeight(fullHeight: Int, rowHeight: Int, fraction: Float): Int {
    val folded = fraction.coerceIn(0f, 1f)
    return (fullHeight + (rowHeight - fullHeight) * folded).toInt().coerceAtLeast(0)
}
