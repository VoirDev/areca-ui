package dev.voir.areca.datetime.calendar

import androidx.compose.runtime.Immutable
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.YearMonth
import kotlinx.datetime.monthsUntil
import kotlinx.datetime.number
import kotlinx.datetime.plus
import kotlinx.datetime.yearMonth

/**
 * The window of time a calendar can reach.
 *
 * Bounds are not the same thing as a [CalendarDateRule]. A rule decides whether a rendered day
 * accepts selection; bounds decide which months exist at all. A day outside the bounds is never
 * drawn and its month can never be paged to, which is why a booking calendar limited to the next
 * ninety days should express that here rather than as a rule — otherwise the user can still swipe
 * through 2087 finding nothing but greyed-out cells.
 *
 * The window is inclusive at both ends and is stated in days rather than months so a half-month
 * limit ("up to the 15th") stays exact. Paging works in whole months, so [minMonth] and [maxMonth]
 * are the months those days fall in: a `maxDate` of the 15th still renders the whole of its month,
 * and the calendar itself draws the 16th onward as unavailable — no [CalendarDateRule] is needed
 * for it.
 *
 * @property minDate Earliest reachable day, inclusive.
 * @property maxDate Latest reachable day, inclusive.
 */
@Immutable
public data class CalendarBounds(
    public val minDate: LocalDate,
    public val maxDate: LocalDate,
) {
    init {
        require(minDate <= maxDate) {
            "minDate must not be after maxDate, was $minDate..$maxDate"
        }
    }

    /** Month [minDate] falls in, and so the first page. */
    public val minMonth: YearMonth = minDate.yearMonth

    /** Month [maxDate] falls in, and so the last page. */
    public val maxMonth: YearMonth = maxDate.yearMonth

    /** Number of months in the window, and so the calendar's page count. Always at least 1. */
    internal val monthCount: Int = minMonth.monthsUntil(maxMonth) + 1

    /** Years the window touches, for the year column of a [MonthYearPicker]. */
    internal val yearRange: IntRange = minMonth.year..maxMonth.year

    /**
     * Whether [date] falls inside the window.
     *
     * @param date Day to test.
     * @return `true` when the day is on or between [minDate] and [maxDate].
     */
    public operator fun contains(date: LocalDate): Boolean = date in minDate..maxDate

    /**
     * Whether [month] is a reachable page.
     *
     * @param month Month to test.
     * @return `true` when the month is on or between [minMonth] and [maxMonth].
     */
    public operator fun contains(month: YearMonth): Boolean = month in minMonth..maxMonth

    /**
     * Pulls [month] into the window.
     *
     * @param month Month to clamp.
     * @return [month] itself when reachable, otherwise the nearer of [minMonth] and [maxMonth].
     */
    public fun coerce(month: YearMonth): YearMonth = month.coerceIn(minMonth, maxMonth)

    /**
     * Page index a month is drawn on.
     *
     * Out-of-window months are clamped rather than rejected, so a caller that computes a month from
     * arithmetic — "today minus eighteen years" — cannot hand the pager an index it has no page for.
     *
     * @param month Month to locate.
     * @return Index in `0 until [monthCount]`.
     */
    internal fun pageOf(month: YearMonth): Int = minMonth.monthsUntil(coerce(month))

    /**
     * Month drawn on a page.
     *
     * @param page Index, clamped into `0 until [monthCount]`.
     * @return Month that page shows.
     */
    internal fun monthAt(page: Int): YearMonth =
        minMonth.plus(page.coerceIn(0, monthCount - 1), DateTimeUnit.MONTH)

    /**
     * Months of [year] that are reachable, as 1-based month numbers.
     *
     * Only the first and last years of the window are ever partial. A [MonthYearPicker] uses this to
     * drop the unreachable months from its month column, so the wheel cannot rest on a month that
     * would immediately be corrected underneath the user.
     *
     * @param year Year to inspect.
     * @return Month numbers in `1..12`, narrowed at the edges of the window. Empty when [year] lies
     *   outside [yearRange].
     */
    internal fun monthNumbersIn(year: Int): IntRange = when {
        year !in yearRange -> IntRange.EMPTY
        else -> {
            val first = if (year == minMonth.year) minMonth.month.number else 1
            val last = if (year == maxMonth.year) maxMonth.month.number else 12
            first..last
        }
    }

    /** Ready-made windows and factories for common shapes of window. */
    public companion object {
        /**
         * 1 January 1900 to 31 December 2100.
         *
         * Wide enough for a date of birth at one end and a long-dated contract at the other, and
         * fixed rather than relative to today so it does not change under a running composition.
         * Any calendar with a real limit should state it instead of inheriting this.
         */
        public val Default: CalendarBounds = CalendarBounds(
            minDate = LocalDate(1900, 1, 1),
            maxDate = LocalDate(2100, 12, 31),
        )

        /**
         * A window of whole years centred on a day.
         *
         * @param today Day to centre on, normally the calendar's own today.
         * @param years Years to allow on each side. Must not be negative.
         * @return Window running from 1 January [years] years before [today] to 31 December [years]
         *   years after it.
         */
        public fun around(today: LocalDate, years: Int): CalendarBounds {
            require(years >= 0) { "years must not be negative, was $years" }
            return CalendarBounds(
                minDate = LocalDate(today.year - years, 1, 1),
                maxDate = LocalDate(today.year + years, 12, 31),
            )
        }

        /**
         * A window from optional ends, falling back to [Default] where an end is absent.
         *
         * Convenience for callers holding nullable limits, which is the usual shape of a limit that
         * comes from configuration or from a form.
         *
         * @param minDate Earliest day, or `null` for [Default]'s.
         * @param maxDate Latest day, or `null` for [Default]'s.
         * @return Window over the given ends.
         */
        public fun of(minDate: LocalDate?, maxDate: LocalDate?): CalendarBounds = CalendarBounds(
            minDate = minDate ?: Default.minDate,
            maxDate = maxDate ?: Default.maxDate,
        )
    }
}
