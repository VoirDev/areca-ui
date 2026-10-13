package dev.voir.areca.playground.registry

import dev.voir.areca.playground.datetime.CalendarShowcase
import dev.voir.areca.playground.datetime.DateRangeCalendarShowcase
import dev.voir.areca.playground.datetime.DateTimeFieldShowcase
import dev.voir.areca.playground.datetime.TimePickerShowcase

/**
 * Component pages for `:ui:datetime`, in the [ShowcaseGroup.DateTime] group.
 *
 * Source order is for reading only: the sidebar sorts each section by title.
 */
internal val DateTimeShowcases: List<ShowcaseEntry> = listOf(
    ShowcaseEntry(
        id = "datetime/calendar",
        group = ShowcaseGroup.DateTime,
        title = "Calendar",
        summary = "Month grid with an inline month and year wheel, selection rules and a paging range.",
        keywords = listOf(
            "date", "picker", "month", "year", "day", "schedule", "navigate", "wheel",
        ),
        content = { CalendarShowcase() },
    ),
    ShowcaseEntry(
        id = "datetime/date-time-field",
        group = ShowcaseGroup.DateTime,
        title = "Date & time field",
        summary = "One field holding a date and a time, each chosen from its own picker.",
        keywords = listOf(
            "date", "time", "field", "composite", "calendar", "wheel", "schedule", "form",
        ),
        content = { DateTimeFieldShowcase() },
    ),
    ShowcaseEntry(
        id = "datetime/date-range-calendar",
        group = ShowcaseGroup.DateTime,
        title = "Date range calendar",
        summary = "Two-tap range picking on the month grid, with lengths, excluded days and a " +
            "preview under the pointer.",
        keywords = listOf(
            "date", "range", "picker", "period", "stay", "booking", "from", "to", "between",
        ),
        content = { DateRangeCalendarShowcase() },
    ),
    ShowcaseEntry(
        id = "datetime/time-picker",
        group = ShowcaseGroup.DateTime,
        title = "Time picker",
        summary = "Hour, minute and an optional AM/PM wheel, reporting the time they settle on.",
        keywords = listOf("time", "picker", "hour", "minute", "clock", "wheel", "am", "pm"),
        content = { TimePickerShowcase() },
    ),
)
