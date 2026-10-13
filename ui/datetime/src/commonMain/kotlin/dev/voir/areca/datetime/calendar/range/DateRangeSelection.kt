package dev.voir.areca.datetime.calendar.range

import androidx.compose.runtime.Immutable
import dev.voir.areca.datetime.calendar.CalendarBounds
import dev.voir.areca.datetime.calendar.CalendarDateRange
import kotlinx.datetime.LocalDate

/**
 * How far through picking a range the user is.
 *
 * Three states rather than a pair of nullable dates, because "started but not finished" is a state
 * the interface has to draw — one lit day and a grid that has just greyed everything it can no
 * longer reach — and a nullable end cannot say whether it is absent because nothing has been picked
 * or because only half has. With two nullables the illegal fourth combination (an end without a
 * start) is one the compiler cannot rule out and every reader has to.
 *
 * Nothing here holds the selection: a range calendar reports transitions and draws what it is handed
 * back, the same contract [dev.voir.areca.datetime.calendar.Calendar] keeps for single and multiple
 * selection. [select] is the transition, and it is a pure function, so the rules a range obeys can
 * be tested without a composition.
 */
@Immutable
public sealed interface DateRangeSelection {
    /** The finished range, or `null` while one is still being picked. */
    public val range: CalendarDateRange?

    /**
     * Whether a day is part of what has been picked so far.
     *
     * @param date Day to test.
     * @return `true` for any day of a finished range, and for the anchor of an unfinished one.
     */
    public operator fun contains(date: LocalDate): Boolean

    /** Nothing picked. What a range calendar opens on. */
    @Immutable
    public data object Empty : DateRangeSelection {
        /** Always `null`: nothing has been picked. */
        public override val range: CalendarDateRange? get() = null

        /**
         * Never true: nothing has been picked.
         *
         * @param date Day to test.
         * @return `false` for every day.
         */
        public override fun contains(date: LocalDate): Boolean = false
    }

    /**
     * One end picked, waiting for the other.
     *
     * @property anchor The day that was tapped first. The range will run from here unless the user
     *   taps an earlier day, which starts again rather than filling backwards — see [select].
     */
    @Immutable
    public data class Started(public val anchor: LocalDate) : DateRangeSelection {
        /** Always `null`: the range has no far end yet. */
        public override val range: CalendarDateRange? get() = null

        /**
         * Whether [date] is the anchor.
         *
         * @param date Day to test.
         * @return `true` only for [anchor].
         */
        public override fun contains(date: LocalDate): Boolean = date == anchor
    }

    /**
     * Both ends picked.
     *
     * @property range The chosen days.
     */
    @Immutable
    public data class Complete(public override val range: CalendarDateRange) : DateRangeSelection {
        /**
         * Whether [date] falls inside [range], ends included.
         *
         * @param date Day to test.
         * @return `true` for every day from the range's start to its end.
         */
        public override fun contains(date: LocalDate): Boolean = date in range
    }
}

/**
 * Where one day sits in a range, which is what decides how its cell is drawn.
 *
 * Geometry, not colour: it says which ends of the track are capped, and nothing about whether the
 * range is real or merely previewed under the pointer. Keeping those apart is what lets a preview
 * reuse the drawing code with a lighter palette instead of duplicating it.
 */
public enum class DateRangeDayPosition {
    /** Outside the range. Drawn as an ordinary day. */
    None,

    /** First day of a range of two or more. Capped on the leading edge only. */
    Start,

    /** Between the ends. An uncapped track running the full width of the cell. */
    InRange,

    /** Last day of a range of two or more. Capped on the trailing edge only. */
    End,

    /**
     * The whole range in one cell: a finished single-day range, or an anchor with no end yet.
     *
     * Both draw as a plain circle with no track, which is why they share a position. A start cap
     * with nothing to attach to reads as a rendering fault rather than as a choice in progress.
     */
    Single,
}

/**
 * Where a day sits in this selection.
 *
 * @param date Day the calendar is about to draw.
 * @return Position governing the day's caps and track.
 */
public fun DateRangeSelection.positionOf(date: LocalDate): DateRangeDayPosition = when (this) {
    DateRangeSelection.Empty -> DateRangeDayPosition.None
    is DateRangeSelection.Started -> when (date) {
        anchor -> DateRangeDayPosition.Single
        else -> DateRangeDayPosition.None
    }

    is DateRangeSelection.Complete -> when {
        date !in range -> DateRangeDayPosition.None
        range.isSingleDay -> DateRangeDayPosition.Single
        date == range.start -> DateRangeDayPosition.Start
        date == range.endInclusive -> DateRangeDayPosition.End
        else -> DateRangeDayPosition.InRange
    }
}

/**
 * The selection as it would look if the range ended at [date].
 *
 * For the tentative range a pointer paints while hovering the grid. Drawing it through the same
 * [positionOf] as a real one is what keeps a preview from drifting from the thing it is previewing.
 *
 * A preview is exactly the range a tap on [date] would finish ([select]), and nothing otherwise. An
 * earlier day starts the range again rather than filling backwards, and a day the rule, the span
 * limits or the bounds refuse does nothing, so painting a range to either would promise one the tap
 * would not deliver.
 *
 * @param date Day under the pointer, or `null` when nothing is hovered.
 * @param constraints Limits the range must satisfy. The same ones the tap is checked against.
 * @param bounds Days the calendar can reach at all.
 * @return A [DateRangeSelection.Complete] while a tap on [date] would finish the range, otherwise
 *   the receiver unchanged.
 */
internal fun DateRangeSelection.withPreviewEnd(
    date: LocalDate?,
    constraints: DateRangeConstraints,
    bounds: CalendarBounds,
): DateRangeSelection = when {
    this !is DateRangeSelection.Started || date == null -> this
    else -> select(date, constraints, bounds) as? DateRangeSelection.Complete ?: this
}

/**
 * The selection after a day is tapped.
 *
 * The whole state machine, in one pure function:
 *
 * - Nothing picked, or a finished range: the day becomes the new anchor. A second range always
 *   starts from scratch rather than editing the nearer end of the last one, because "which end am I
 *   dragging?" is a question a grid of identical cells cannot answer and the user cannot see the
 *   answer to until it is wrong.
 * - An anchor, and a later day inside the reachable window: the range is finished.
 * - An anchor, and an earlier day: that day becomes the new anchor. Filling backwards would finish a
 *   range the user may only have been re-aiming, and it is the tap they would have made anyway to
 *   correct a mis-hit.
 * - An anchor, and a later day the limits refuse: nothing happens. Such a day is drawn as
 *   unavailable, so this is only reachable by calling the model directly.
 *
 * A day outside [bounds], or one [DateRangeConstraints.dateRule] refuses, never changes the
 * selection. The calendar does not make those cells tappable, so the check is for direct callers —
 * but it is what makes this function total, and safe to drive from a keyboard or a test.
 *
 * @param date Day the user tapped.
 * @param constraints Limits the range must satisfy.
 * @param bounds Days the calendar can reach at all.
 * @return The selection to draw next. The receiver itself when the tap does nothing.
 */
public fun DateRangeSelection.select(
    date: LocalDate,
    constraints: DateRangeConstraints,
    bounds: CalendarBounds,
): DateRangeSelection {
    if (date !in bounds || !constraints.dateRule.isEnabled(date)) return this

    return when (this) {
        DateRangeSelection.Empty, is DateRangeSelection.Complete -> DateRangeSelection.Started(date)
        is DateRangeSelection.Started -> {
            val window = constraints.endWindow(anchor, bounds)
            when {
                date < anchor -> DateRangeSelection.Started(date)
                window != null && date in window ->
                    DateRangeSelection.Complete(CalendarDateRange(anchor, date))

                else -> this
            }
        }
    }
}
