package dev.voir.areca.datetime.calendar

import androidx.compose.runtime.Immutable
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.YearMonth

/**
 * Every piece of text a calendar renders.
 *
 * Functions rather than a map of strings. A map can be incomplete — a day of the week the caller
 * forgot would show up as a blank column rather than as a compile error — and it cannot express
 * anything computed, which a month heading and a spoken date both are.
 *
 * Collecting them here is also what makes the calendar translatable without a resources dependency
 * in the library: an application passes its own [CalendarLabels] built from whatever localization it
 * already has. [English] is the default so a calendar renders sensibly with no configuration.
 *
 * Numbers are text too. [dayNumber] and [year] render the digits a calendar draws, so a locale that
 * writes its own numerals — Arabic-Indic, Devanagari, Thai — can draw them in the grid and on the
 * year wheel.
 *
 * @property weekday Column heading for a day of the week. Kept to one or two characters — the
 *   columns are square and sized by the day numbers, not by these.
 * @property monthHeader The month named above the grid, normally month and year.
 * @property pickerMonth A month in the [MonthYearPicker] wheel. Separate from [monthHeader] because
 *   the wheel already has a year column beside it, so repeating the year there is noise.
 * @property dayDescription What assistive technology reads for one day. Should be unambiguous on its
 *   own, weekday included: a screen reader visits a cell without the column heading above it, so the
 *   weekday is part of the day's own description.
 * @property monthPickerAction What assistive technology reads for the control that opens the month
 *   picker.
 * @property monthPickerMonth What assistive technology reads for the month wheel.
 * @property monthPickerYear What assistive technology reads for the year wheel.
 * @property previousMonthAction What assistive technology reads for the back control.
 * @property nextMonthAction What assistive technology reads for the forward control.
 * @property todayAction Label of the control that returns to today.
 * @property monthPickerExpanded What assistive technology reads as the state of the month control
 *   while the month picker is open.
 * @property monthPickerCollapsed What assistive technology reads as the state of the month control
 *   while the month picker is closed.
 * @property weekdayName What assistive technology reads for a column heading: the day's full name,
 *   since the drawn [weekday] abbreviation reads aloud as a syllable rather than a day.
 * @property dayNumber The number drawn in a day cell, from `1` to `31`.
 * @property year A year as drawn on the year wheel of the [MonthYearPicker], and as a year's
 *   heading in a [MonthGrid].
 * @property shortMonth A month's short name, as drawn on a [MonthGrid] tile and a
 *   [dev.voir.areca.datetime.DateTile]: `Sep`.
 */
@Immutable
public data class CalendarLabels(
    public val weekday: (DayOfWeek) -> String,
    public val monthHeader: (YearMonth) -> String,
    public val pickerMonth: (Month) -> String,
    public val dayDescription: (LocalDate) -> String,
    public val monthPickerAction: String,
    public val monthPickerMonth: String,
    public val monthPickerYear: String,
    public val previousMonthAction: String,
    public val nextMonthAction: String,
    public val todayAction: String,
    public val monthPickerExpanded: String = "Expanded",
    public val monthPickerCollapsed: String = "Collapsed",
    public val weekdayName: (DayOfWeek) -> String = { it.englishName },
    public val dayNumber: (Int) -> String = { it.toString() },
    public val year: (Int) -> String = { it.toString() },
    public val shortMonth: (Month) -> String = { it.englishName.take(3) },
) {
    /** Ready-made label sets. */
    public companion object {
        /**
         * English labels: `Mo`, `September 2026`, `September`, `Tuesday 15 September 2026`.
         *
         * The library ships no resources, so this is a plain-English fallback rather than a
         * localization. An application that translates its interface should build its own.
         */
        public val English: CalendarLabels = CalendarLabels(
            weekday = { it.englishName.take(2) },
            monthHeader = { "${it.month.englishName} ${it.year}" },
            pickerMonth = { it.englishName },
            dayDescription = {
                "${it.dayOfWeek.englishName} ${it.day} ${it.month.englishName} ${it.year}"
            },
            monthPickerAction = "Choose month and year",
            monthPickerMonth = "Month",
            monthPickerYear = "Year",
            previousMonthAction = "Previous month",
            nextMonthAction = "Next month",
            todayAction = "Today",
            monthPickerExpanded = "Expanded",
            monthPickerCollapsed = "Collapsed",
            weekdayName = { it.englishName },
            dayNumber = { it.toString() },
            year = { it.toString() },
            shortMonth = { it.englishName.take(3) },
        )

        /**
         * English labels with single-letter weekday headings: `M`, `T`, `W`.
         *
         * For a narrow calendar, where two characters crowd the columns. Ambiguous by design —
         * Tuesday and Thursday share `T` — which is why it is not the default. The headings are
         * still read aloud by their full names.
         */
        public val EnglishNarrow: CalendarLabels =
            English.copy(weekday = { it.englishName.take(1) })
    }
}

/** Title-cased English name of a day of the week, such as `Monday`. */
private val DayOfWeek.englishName: String
    get() = name.titleCase()

/** Title-cased English name of a month, such as `September`. */
internal val Month.englishName: String
    get() = name.titleCase()

/**
 * Turns an enum's `SCREAMING_CASE` name into a title-cased word.
 *
 * @return The receiver with only its first character upper case.
 */
private fun String.titleCase(): String =
    lowercase().replaceFirstChar { it.uppercase() }
