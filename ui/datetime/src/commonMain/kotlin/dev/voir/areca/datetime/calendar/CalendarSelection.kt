package dev.voir.areca.datetime.calendar

import androidx.compose.runtime.Immutable
import kotlinx.datetime.LocalDate

/**
 * What a calendar is showing as chosen.
 *
 * One value rather than the pair of a nullable date and a set, so "single" and "multiple" are
 * alternatives the type system knows about. With two parameters a caller can pass both, and the only
 * sensible thing a calendar can then do is quietly union them — which is a bug the caller never
 * finds out about.
 *
 * The calendar never mutates this. It reports taps through `onDateClick` and draws whatever it is
 * given back, which is what lets a caller apply its own rules — a maximum number of days, snapping
 * to whole weeks, refusing to deselect the last one — without fighting internal state.
 *
 * [Range] is the one variant that is not simply a set of days. It exists because a run of
 * consecutive days is the only selection whose *shape* a cell has to know about — which end it is,
 * and whether the day beside it continues — and because holding one as a set would mean carrying
 * every day between the ends when two of them describe it exactly.
 */
@Immutable
public sealed interface CalendarSelection {
    /**
     * Whether a day is part of the selection.
     *
     * @param date Day to test.
     * @return `true` when the day is drawn as selected.
     */
    public operator fun contains(date: LocalDate): Boolean

    /** Days in the selection, in no particular order. Empty when nothing is chosen. */
    public val dates: Set<LocalDate>

    /**
     * One day at a time, or none.
     *
     * @property date Chosen day, or `null` while nothing is chosen.
     */
    @Immutable
    public data class Single(public val date: LocalDate? = null) : CalendarSelection {
        /** [date] alone, or no days while nothing is chosen. */
        public override val dates: Set<LocalDate> get() = setOfNotNull(date)

        /**
         * Whether [date] is the chosen day.
         *
         * @param date Day to test.
         * @return `true` when [date] equals the chosen day.
         */
        public override fun contains(date: LocalDate): Boolean = date == this.date
    }

    /**
     * Any number of days, not necessarily adjacent.
     *
     * @property dates Chosen days. Order is not meaningful.
     */
    @Immutable
    public data class Multiple(
        public override val dates: Set<LocalDate> = emptySet(),
    ) : CalendarSelection {
        /**
         * Whether [date] is one of the chosen days.
         *
         * @param date Day to test.
         * @return `true` when [date] is in [dates].
         */
        public override fun contains(date: LocalDate): Boolean = date in dates
    }

    /**
     * A run of consecutive days, or none.
     *
     * Stored as its two ends rather than as the days between them: a range picked out of the
     * default [CalendarBounds] can be seventy thousand days long, and [contains] — the only thing a
     * grid asks — is two comparisons either way.
     *
     * This says which days are *chosen*, and nothing about where in the range each one sits. That is
     * [dev.voir.areca.datetime.calendar.range.DateRangeDayPosition]'s job, because a cell needs it
     * only to decide how to draw itself, and a half-picked range has a position for its anchor
     * while having no range at all.
     *
     * @property range Chosen days, or `null` while nothing is chosen.
     */
    @Immutable
    public data class Range(public val range: CalendarDateRange? = null) : CalendarSelection {
        /**
         * Days of [range].
         *
         * Built on first read and kept, because it is the one operation here whose cost grows with
         * the length of the range, and nothing in a calendar's drawing path asks for it.
         */
        public override val dates: Set<LocalDate> by lazy {
            range?.asSequence()?.toSet().orEmpty()
        }

        /**
         * Whether [date] falls inside [range], ends included.
         *
         * @param date Day to test.
         * @return `true` when a range is chosen and [date] lies within it.
         */
        public override fun contains(date: LocalDate): Boolean = range != null && date in range
    }

    /** The empty selection calendars start from. */
    public companion object {
        /** Nothing chosen, single-day semantics. The default for a calendar. */
        public val Empty: CalendarSelection = Single()
    }
}
