package dev.voir.areca.datetime.calendar.range

import androidx.compose.runtime.Immutable
import dev.voir.areca.datetime.calendar.CalendarBounds
import dev.voir.areca.datetime.calendar.CalendarDateRange
import dev.voir.areca.datetime.calendar.CalendarDateRule
import dev.voir.areca.datetime.calendar.plus
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

/**
 * What a range may do about days it is not allowed to contain.
 *
 * The distinction only exists once a [DateRangeConstraints.dateRule] refuses something. Both
 * policies still refuse to *land* on a blocked day; they differ over whether one may be stepped
 * over.
 */
public enum class BlockedDayPolicy {
    /**
     * A range may not contain a blocked day at all, so it stops at the first one after its anchor.
     *
     * What a booking means: a room taken on Wednesday does not merely make Wednesday unavailable, it
     * makes Tuesday-to-Thursday impossible. The days beyond the blockage are drawn as unavailable
     * for as long as the anchor stands, which is how the user learns the stay is interrupted
     * without tapping into it.
     */
    StopAtBlocked,

    /**
     * A range may span blocked days, though it may still not start or end on one.
     *
     * What a reporting period or a leave request means: a holiday in the middle of a fortnight off
     * is not a reason the fortnight cannot be requested.
     *
     * The blocked days stay inside the range and are counted like any other:
     * [DateRangeConstraints.minDays] and [DateRangeConstraints.maxDays] measure calendar days from
     * end to end, and so does [CalendarDateRange.dayCount]. Excluding them from what the range
     * means — working days taken, nights billed — is the application's to do with the range it is
     * handed.
     */
    AllowGaps,
}

/**
 * The limits a picked range has to satisfy.
 *
 * Separate from [CalendarBounds] and from [CalendarDateRule] because it constrains something neither
 * of them can see. Bounds decide which days exist, a rule decides which days are available one at a
 * time; only a range has a *length*, and "at least two nights", "no more than a fortnight" and "not
 * across a day somebody else has booked" are all statements about a pair of days that no per-day
 * predicate can express.
 *
 * Lengths are counted in days, inclusive, matching [CalendarDateRange.dayCount]: a minimum of two
 * nights is `minDays = 3`. One unit throughout, stated once here, rather than a type that accepts
 * both and leaves the reader to work out which the author meant.
 *
 * @property dateRule Which days are available individually. The same rule the calendar greys cells
 *   with, kept here so the span limits and the per-day ones travel together — [dateRuleFor] hands
 *   the calendar the combination.
 * @property minDays Shortest range that may be completed, counting both ends. `1` allows a
 *   single-day range; `2` and above refuse one, which is why there is no separate flag for it. Must
 *   be at least 1.
 * @property maxDays Longest range that may be completed, counting both ends, or `null` for no limit.
 *   Must not be less than [minDays].
 * @property blockedDays Whether a range may step over a day [dateRule] refuses.
 */
@Immutable
public data class DateRangeConstraints(
    public val dateRule: CalendarDateRule = CalendarDateRule.All,
    public val minDays: Int = 1,
    public val maxDays: Int? = null,
    public val blockedDays: BlockedDayPolicy = BlockedDayPolicy.StopAtBlocked,
) {
    init {
        require(minDays >= 1) { "minDays must be at least 1, was $minDays" }
        require(maxDays == null || maxDays >= minDays) {
            "maxDays must not be less than minDays, was $maxDays for minDays $minDays"
        }
    }

    /**
     * The window the far end of a range may fall in, given where it started.
     *
     * Forward only, and about *length* only. [dateRule] still applies inside the window — under
     * [BlockedDayPolicy.AllowGaps] the window deliberately spans blocked days, because they may sit
     * inside a range even though the range may not end on one. [dateRuleFor] applies both.
     *
     * Days at or before the anchor are absent for a different reason: an earlier tap starts the
     * range again rather than completing it ([select]), so it is governed by [dateRule] alone and is
     * never limited by a length.
     *
     * Under [BlockedDayPolicy.StopAtBlocked] this walks forward from the anchor to the first
     * unavailable day. The walk is bounded by [maxDays] when one is given, and otherwise by
     * [CalendarBounds.maxDate] — which with the default two-century bounds is tens of thousands of
     * steps. It runs once per anchor rather than once per cell, but it is the reason a calendar that
     * blocks days should state a [maxDays] or narrow its bounds.
     *
     * @param anchor Day the range started from.
     * @param bounds Days the calendar can reach at all.
     * @return Days that would complete a valid range, or `null` when none would — an anchor that
     *   cannot be built on, because the limits, the blocked days or the end of the calendar leave it
     *   nothing to reach.
     */
    public fun endWindow(anchor: LocalDate, bounds: CalendarBounds): CalendarDateRange? {
        if (anchor !in bounds || !dateRule.isEnabled(anchor)) return null

        val first = anchor.plus(minDays - 1, DateTimeUnit.DAY)
        val longest = maxDays?.let { anchor.plus(it - 1, DateTimeUnit.DAY) } ?: bounds.maxDate
        val limit = minOf(longest, bounds.maxDate)
        if (first > limit) return null

        val last = when (blockedDays) {
            BlockedDayPolicy.AllowGaps -> limit
            BlockedDayPolicy.StopAtBlocked -> lastAvailableDayFrom(anchor, limit)
        }
        return if (first > last) null else CalendarDateRange(first, last)
    }

    /**
     * The rule a calendar should grey its cells with while this selection stands.
     *
     * The per-day [dateRule] always applies. While a range is half-picked it is joined by the length
     * limits, so the moment an anchor is set every day the range could not reach goes grey — the
     * fortnight beyond a month's maximum, the days past somebody else's booking, the night before a
     * two-night minimum. That is the difference between limits the user discovers by being refused
     * and limits they can see.
     *
     * Days at or before the anchor stay available, because tapping one starts again rather than
     * completing.
     *
     * The rule is called for every cell the calendar draws, so the window is resolved once here and
     * captured; hold the result in a `remember` keyed on the selection rather than rebuilding it per
     * recomposition.
     *
     * @param selection How far through picking the user is.
     * @param bounds Days the calendar can reach at all.
     * @return [dateRule] itself unless a range is half-picked, and the combination when one is.
     */
    public fun dateRuleFor(
        selection: DateRangeSelection,
        bounds: CalendarBounds,
    ): CalendarDateRule = when (selection) {
        is DateRangeSelection.Started -> {
            val anchor = selection.anchor
            val window = endWindow(anchor, bounds)
            dateRule + CalendarDateRule { date ->
                date <= anchor || (window != null && date in window)
            }
        }

        DateRangeSelection.Empty, is DateRangeSelection.Complete -> dateRule
    }

    /**
     * Walks forward to the last day reachable without crossing an unavailable one.
     *
     * @param anchor Day to walk from. Assumed available.
     * @param limit Day to stop at, whether or not anything is blocked before it.
     * @return [limit], or the day before the first unavailable day after [anchor]. [anchor] itself
     *   when the very next day is unavailable.
     */
    private fun lastAvailableDayFrom(anchor: LocalDate, limit: LocalDate): LocalDate {
        var reached = anchor
        while (reached < limit) {
            val next = reached.plus(1, DateTimeUnit.DAY)
            if (!dateRule.isEnabled(next)) return reached
            reached = next
        }
        return limit
    }
}
