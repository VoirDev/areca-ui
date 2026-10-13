package dev.voir.areca.datetime.calendar.range

import androidx.compose.runtime.Immutable
import dev.voir.areca.datetime.calendar.CalendarDateRange

/**
 * Text a range adds to the calendar it is drawn in.
 *
 * Separate from [dev.voir.areca.datetime.calendar.CalendarLabels] rather than added to it, so an
 * application that already builds its own calendar labels does not have to revisit them to use a
 * range, and so a plain [dev.voir.areca.datetime.calendar.Calendar] does not carry strings it can
 * never render.
 *
 * What a range adds is almost entirely for assistive technology. The band and the two circles say
 * everything a sighted user needs; a screen reader arrives at one cell with none of that, and "10
 * June 2026" is the same announcement whether the day is the start of a stay, the end of one, or a
 * day in between.
 *
 * @property position What is appended to a day's description for where it sits in the range.
 *   Returning `null` appends nothing, which is what [DateRangeDayPosition.None] should do, and what
 *   [DateRangeDayPosition.Single] does in English: a lone chosen day is already announced as
 *   selected, and saying so again in the description reads it twice.
 * @property length How a chosen range's extent is described — for a footer, a field, or a live
 *   announcement once both ends are picked.
 */
@Immutable
public data class DateRangeLabels(
    public val position: (DateRangeDayPosition) -> String?,
    public val length: (CalendarDateRange) -> String,
) {
    /** Ready-made label sets. */
    public companion object {
        /**
         * English labels: `start of range`, `in range`, `end of range`, `3 nights`.
         *
         * The library ships no resources, so this is a plain-English fallback rather than a
         * localization — and in particular it does not pluralize in any language but this one.
         */
        public val English: DateRangeLabels = DateRangeLabels(
            position = { position ->
                when (position) {
                    DateRangeDayPosition.None -> null
                    DateRangeDayPosition.Start -> "start of range"
                    DateRangeDayPosition.InRange -> "in range"
                    DateRangeDayPosition.End -> "end of range"
                    DateRangeDayPosition.Single -> null
                }
            },
            length = { range ->
                when (range.nights) {
                    0 -> "1 day"
                    1 -> "1 night"
                    else -> "${range.nights} nights"
                }
            },
        )
    }
}
