package dev.voir.areca.datetime.calendar

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.YearMonth
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.yearMonth

/** Columns in a calendar week. */
internal const val DAYS_IN_WEEK: Int = 7

/**
 * Rows a month grid uses when every month is given the same height.
 *
 * Six, because that is the worst case: a 31-day month whose first day sits in the last column needs
 * `6 + 31 = 37` cells, which is six rows.
 */
internal const val MAX_WEEKS_IN_MONTH: Int = 6

/**
 * Which column a day of the week falls in.
 *
 * The whole point of the calculation is that column 0 is [startDayOfWeek], not Monday — a calendar
 * that starts its week on Sunday puts Sunday first and Saturday last.
 *
 * @param startDayOfWeek Day the week is drawn from.
 * @return Column index in `0 until [DAYS_IN_WEEK]`.
 */
internal fun DayOfWeek.columnIn(startDayOfWeek: DayOfWeek): Int =
    (isoDayNumber - startDayOfWeek.isoDayNumber + DAYS_IN_WEEK) % DAYS_IN_WEEK

/**
 * The days of the week in the order a calendar draws them.
 *
 * @param startDayOfWeek Day the week is drawn from.
 * @return Seven days beginning with [startDayOfWeek] and wrapping around.
 */
internal fun daysOfWeekFrom(startDayOfWeek: DayOfWeek): List<DayOfWeek> =
    List(DAYS_IN_WEEK) {
        DayOfWeek((startDayOfWeek.isoDayNumber - 1 + it) % DAYS_IN_WEEK + 1)
    }

/**
 * How many rows a month needs.
 *
 * The calendar measures its body with this before laying anything out, so that the month picker can
 * be given exactly the height the grid would have taken and opening it does not resize the calendar.
 *
 * @param startDayOfWeek Day each row begins on, which decides how many cells the 1st is pushed by.
 * @return Rows, between four and [MAX_WEEKS_IN_MONTH].
 */
internal fun YearMonth.weekCount(startDayOfWeek: DayOfWeek): Int {
    val filledCells = firstDay.dayOfWeek.columnIn(startDayOfWeek) + numberOfDays
    // Round up to whole weeks: 35 cells is five rows, 36 is six.
    return (filledCells + DAYS_IN_WEEK - 1) / DAYS_IN_WEEK
}

/**
 * Lays a month out as rows of seven cells.
 *
 * Cells before the 1st and after the last day belong to the neighbouring months. They are either the
 * real dates or `null`, depending on [showAdjacentMonths]; either way they occupy their slot, so the
 * 1st always lands in the column its day of the week calls for.
 *
 * The row count is derived from how many cells the month actually fills, rounded up to whole weeks —
 * a month that fills exactly five rows gets five, not five plus a blank one.
 *
 * @param startDayOfWeek Day each row begins on.
 * @param showAdjacentMonths Whether the leading and trailing cells carry their real dates. When
 *   `false` they are `null` and the calendar draws nothing in them.
 * @param fixedWeekCount Whether every month is laid out over [MAX_WEEKS_IN_MONTH] rows. Keeps the
 *   grid one height, so paging between a five-row and a six-row month does not resize the calendar
 *   under the user. A five-row month then gains a sixth row, of adjacent days or of blanks.
 * @return Rows of seven cells, outer list top to bottom, inner list left to right.
 */
internal fun YearMonth.calendarWeeks(
    startDayOfWeek: DayOfWeek,
    showAdjacentMonths: Boolean,
    fixedWeekCount: Boolean,
): List<List<LocalDate?>> {
    val leadingCells = firstDay.dayOfWeek.columnIn(startDayOfWeek)
    val weeks = if (fixedWeekCount) MAX_WEEKS_IN_MONTH else weekCount(startDayOfWeek)

    val month = this
    val gridStart = firstDay.minus(leadingCells, DateTimeUnit.DAY)
    return List(weeks) { week ->
        List(DAYS_IN_WEEK) { column ->
            val date = gridStart.plus(week * DAYS_IN_WEEK + column, DateTimeUnit.DAY)
            when {
                date.yearMonth == month -> date
                showAdjacentMonths -> date
                else -> null
            }
        }
    }
}
