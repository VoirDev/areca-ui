package dev.voir.areca.datetime.calendar

import androidx.compose.runtime.Immutable
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.daysUntil
import kotlinx.datetime.plus

/**
 * A run of consecutive days, both ends inclusive.
 *
 * A [ClosedRange] rather than a pair of dates, which buys three things that a pair would each need
 * spelling out at every call site: `date in range` for free, ordering enforced once on construction
 * rather than wherever someone remembers, and direct use as a [CalendarDateRule.inRange] argument —
 * so the range a user picked can be handed straight back to the calendar as the days it may now
 * choose from.
 *
 * Length is counted in **days**, inclusive, and [nights] is derived from it. One unit rather than
 * two, because a type carrying both invites the caller to state a limit in whichever one they
 * happened to be thinking in and the reader to guess which. A stay of the 1st to the 3rd is three
 * days and two nights; [dev.voir.areca.datetime.calendar.range.DateRangeConstraints] states its
 * limits in days for the same reason.
 *
 * @property start First day of the range.
 * @property endInclusive Last day of the range. Never before [start].
 */
@Immutable
public data class CalendarDateRange(
    public override val start: LocalDate,
    public override val endInclusive: LocalDate,
) : ClosedRange<LocalDate> {
    init {
        require(start <= endInclusive) {
            "start must not be after endInclusive, was $start..$endInclusive"
        }
    }

    /** Days in the range, counting both ends. Always at least 1. */
    public val dayCount: Int = start.daysUntil(endInclusive) + 1

    /** Nights the range spans, which is one fewer than [dayCount]. Zero for a single day. */
    public val nights: Int get() = dayCount - 1

    /** Whether the range begins and ends on the same day. */
    public val isSingleDay: Boolean get() = start == endInclusive

    /**
     * The days of the range, in order.
     *
     * Lazy, because a range is bounded only by the calendar it was picked in: two centuries of
     * default [CalendarBounds] is seventy thousand dates, and almost every caller wants to iterate
     * or test membership rather than hold them all.
     *
     * @return Days from [start] to [endInclusive], one at a time.
     */
    public fun asSequence(): Sequence<LocalDate> = generateSequence(start) { previous ->
        previous.plus(1, DateTimeUnit.DAY).takeIf { it <= endInclusive }
    }
}
