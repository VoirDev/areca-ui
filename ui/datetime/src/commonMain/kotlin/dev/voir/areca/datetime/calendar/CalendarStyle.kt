package dev.voir.areca.datetime.calendar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.ButtonStyle
import dev.voir.areca.foundation.button.IconButtonStyle
import dev.voir.areca.foundation.wheel.WheelPickerStyle
import dev.voir.areca.theme.ArecaTheme

/**
 * The three colors one day cell draws in.
 *
 * A triple rather than three loose fields per state, for the same reason
 * [dev.voir.areca.foundation.selection.SelectionColors] is one: a calendar day has nine states, and
 * twenty-seven flat color fields is a list nobody can check for gaps. Grouped, a state that was
 * never given colors is a missing property rather than three fields nobody noticed.
 *
 * @property bg Fill of the circle. [Color.Transparent] draws none.
 * @property content The day number.
 * @property border Outline of the circle. [Color.Transparent] draws none — which is the usual case;
 *   only today is ringed by default.
 */
@Immutable
public data class CalendarDayColors(
    public val bg: Color,
    public val content: Color,
    public val border: Color = Color.Transparent,
)

/**
 * Colors of a [Calendar].
 *
 * Days are the whole of it. A cell is described by four independent facts — chosen or not, today or
 * not, available or not, and inside this month or spilling in from a neighbour — and the nine
 * combinations that are drawn differently each get their own [CalendarDayColors]. [resolveDay] holds
 * the precedence between them in one place, so no caller has to rediscover that a disabled selected
 * day is not simply a grey selected day.
 *
 * @property weekdayLabel The Mo/Tu/We headings.
 * @property monthLabel The month and year above the grid.
 * @property day An ordinary day of this month.
 * @property selectedDay A chosen day.
 * @property todayDay Today, unchosen.
 * @property disabledDay A day of this month that refuses selection.
 * @property selectedDisabledDay A chosen day that has since become unavailable — a booking whose
 *   slot was taken. Distinct from [disabledDay] because the user needs to see that their choice is
 *   still there and no longer valid.
 * @property todayDisabledDay Today, when today itself cannot be chosen.
 * @property adjacentDay A day spilling in from the previous or next month.
 * @property adjacentSelectedDay A chosen day from a neighbouring month.
 * @property adjacentTodayDay Today, seen from a neighbouring month's page.
 * @property monthControl Style of the previous and next month buttons.
 * @property monthPickerToggle Style of the control that opens the month picker. Plain and neutral
 *   like [monthControl], so the two answer a hover and a press identically, but carrying
 *   [monthLabel] as its content colour — the month is the thing being read, and it should not be
 *   greyed to match the buttons that merely step it.
 * @property monthPicker Colors of the [MonthYearPicker]. Its
 *   [surface][dev.voir.areca.foundation.wheel.WheelPickerStyle.surface] must match whatever the
 *   calendar is sitting on, or the fade at the picker's edges will read as a band.
 */
@Immutable
public data class CalendarStyle(
    public val weekdayLabel: Color,
    public val monthLabel: Color,
    public val day: CalendarDayColors,
    public val selectedDay: CalendarDayColors,
    public val todayDay: CalendarDayColors,
    public val disabledDay: CalendarDayColors,
    public val selectedDisabledDay: CalendarDayColors,
    public val todayDisabledDay: CalendarDayColors,
    public val adjacentDay: CalendarDayColors,
    public val adjacentSelectedDay: CalendarDayColors,
    public val adjacentTodayDay: CalendarDayColors,
    public val monthControl: IconButtonStyle,
    public val monthPickerToggle: ButtonStyle,
    public val monthPicker: WheelPickerStyle,
) {
    /**
     * Picks the colors for one day cell.
     *
     * Precedence, outermost first:
     *
     * 1. **Adjacent** wins over everything. A day from a neighbouring month is context, not content,
     *    and must stay quieter than this month's days however it is otherwise flagged — including
     *    when it is disabled, which is why the adjacent states have no disabled variants of their
     *    own.
     * 2. **Chosen** wins over **today**. Today is a fact about the calendar; a chosen day is the
     *    user's answer, and a chosen today should read as chosen.
     * 3. **Unavailable** modifies the two above rather than replacing them, so a disabled chosen day
     *    is still visibly chosen.
     *
     * @param selected Whether the day is in the selection.
     * @param isToday Whether the day is the calendar's today.
     * @param enabled Whether the day accepts a tap.
     * @param adjacent Whether the day belongs to a neighbouring month.
     * @return Colors to draw the cell with.
     */
    public fun resolveDay(
        selected: Boolean,
        isToday: Boolean,
        enabled: Boolean,
        adjacent: Boolean,
    ): CalendarDayColors = when (dayAppearanceOf(selected, isToday, enabled, adjacent)) {
        DayAppearance.AdjacentSelected -> adjacentSelectedDay
        DayAppearance.AdjacentToday -> adjacentTodayDay
        DayAppearance.Adjacent -> adjacentDay
        DayAppearance.SelectedDisabled -> selectedDisabledDay
        DayAppearance.TodayDisabled -> todayDisabledDay
        DayAppearance.Disabled -> disabledDay
        DayAppearance.Selected -> selectedDay
        DayAppearance.Today -> todayDay
        DayAppearance.Plain -> day
    }

    /** Theme-derived styles and the builder they share. */
    public companion object {
        /** Theme default: brand-filled selection, an accent ring on today. */
        public val Default: CalendarStyle
            @Composable
            get() = default()

        /**
         * Builds a style from the current theme.
         *
         * Parameters are flat colors rather than [CalendarDayColors] triples, so overriding one
         * color stays a one-line change.
         *
         * @param weekdayLabel The Mo/Tu/We headings.
         * @param monthLabel The month and year above the grid.
         * @param dayBg Fill of an ordinary day.
         * @param dayContent Number of an ordinary day.
         * @param selectedBg Fill of a chosen day.
         * @param selectedContent Number of a chosen day.
         * @param todayBg Fill of today.
         * @param todayContent Number of today.
         * @param todayBorder Ring around today. Today is distinguished by shape as well as color so
         *   it survives a color-blind reading and a low-contrast screen.
         * @param disabledBg Fill of an unavailable day.
         * @param disabledContent Number of an unavailable day.
         * @param selectedDisabledBg Fill of a chosen day that became unavailable.
         * @param selectedDisabledContent Number of a chosen day that became unavailable.
         * @param adjacentContent Number of a day from a neighbouring month.
         * @param adjacentSelectedBg Fill of a chosen day from a neighbouring month.
         * @param monthControl Style of the previous and next month buttons.
         * @param monthPickerToggle Style of the control that opens the month picker.
         * @param monthPicker Colors of the month picker.
         * @return Style built from the given colors.
         */
        @Composable
        public fun default(
            weekdayLabel: Color = ArecaTheme.colors.fgSubtle,
            monthLabel: Color = ArecaTheme.colors.fgBase,
            dayBg: Color = Color.Transparent,
            dayContent: Color = ArecaTheme.colors.fgBase,
            selectedBg: Color = ArecaTheme.colors.bgBrandBase,
            selectedContent: Color = ArecaTheme.colors.fgOnFill,
            todayBg: Color = Color.Transparent,
            todayContent: Color = ArecaTheme.colors.accentBase,
            todayBorder: Color = ArecaTheme.colors.accentBase,
            disabledBg: Color = Color.Transparent,
            disabledContent: Color = ArecaTheme.colors.fgDisabled,
            selectedDisabledBg: Color = ArecaTheme.colors.bgDisabled,
            selectedDisabledContent: Color = ArecaTheme.colors.fgDisabled,
            adjacentContent: Color = ArecaTheme.colors.fgMuted,
            adjacentSelectedBg: Color = ArecaTheme.colors.bgSubtle,
            monthControl: IconButtonStyle = IconButtonStyle.plain(ActionColor.Neutral),
            monthPickerToggle: ButtonStyle = ButtonStyle
                .plain(color = ActionColor.Neutral, shape = ArecaTheme.shapes.full)
                .copy(content = monthLabel),
            monthPicker: WheelPickerStyle = WheelPickerStyle.Default,
        ): CalendarStyle = CalendarStyle(
            weekdayLabel = weekdayLabel,
            monthLabel = monthLabel,
            day = CalendarDayColors(bg = dayBg, content = dayContent),
            selectedDay = CalendarDayColors(bg = selectedBg, content = selectedContent),
            todayDay = CalendarDayColors(
                bg = todayBg,
                content = todayContent,
                border = todayBorder,
            ),
            disabledDay = CalendarDayColors(bg = disabledBg, content = disabledContent),
            selectedDisabledDay = CalendarDayColors(
                bg = selectedDisabledBg,
                content = selectedDisabledContent,
            ),
            todayDisabledDay = CalendarDayColors(
                bg = todayBg,
                content = ArecaTheme.colors.accentMuted,
                border = ArecaTheme.colors.accentMuted,
            ),
            adjacentDay = CalendarDayColors(bg = Color.Transparent, content = adjacentContent),
            adjacentSelectedDay = CalendarDayColors(
                bg = adjacentSelectedBg,
                content = adjacentContent,
            ),
            adjacentTodayDay = CalendarDayColors(
                bg = Color.Transparent,
                content = ArecaTheme.colors.accentMuted,
                border = ArecaTheme.colors.accentMuted,
            ),
            monthControl = monthControl,
            monthPickerToggle = monthPickerToggle,
            monthPicker = monthPicker,
        )
    }
}

/**
 * The nine ways a day cell can look, one per [CalendarDayColors] of a [CalendarStyle].
 *
 * Kept apart from the colors so the precedence between them is a pure function of four facts.
 */
internal enum class DayAppearance {
    /** An ordinary day of this month. */
    Plain,

    /** A chosen day. */
    Selected,

    /** Today, unchosen. */
    Today,

    /** A day of this month that refuses selection. */
    Disabled,

    /** A chosen day that has since become unavailable. */
    SelectedDisabled,

    /** Today, when today itself cannot be chosen. */
    TodayDisabled,

    /** A day spilling in from a neighbouring month. */
    Adjacent,

    /** A chosen day from a neighbouring month. */
    AdjacentSelected,

    /** Today, seen from a neighbouring month's page. */
    AdjacentToday,
}

/**
 * Which of the nine appearances a day takes. The precedence [CalendarStyle.resolveDay] documents.
 *
 * @param selected Whether the day is in the selection.
 * @param isToday Whether the day is the calendar's today.
 * @param enabled Whether the day accepts a tap.
 * @param adjacent Whether the day belongs to a neighbouring month.
 * @return The appearance to draw the day with.
 */
internal fun dayAppearanceOf(
    selected: Boolean,
    isToday: Boolean,
    enabled: Boolean,
    adjacent: Boolean,
): DayAppearance = when {
    adjacent && selected -> DayAppearance.AdjacentSelected
    adjacent && isToday -> DayAppearance.AdjacentToday
    adjacent -> DayAppearance.Adjacent
    !enabled && selected -> DayAppearance.SelectedDisabled
    !enabled && isToday -> DayAppearance.TodayDisabled
    !enabled -> DayAppearance.Disabled
    selected -> DayAppearance.Selected
    isToday -> DayAppearance.Today
    else -> DayAppearance.Plain
}

/**
 * Default [CalendarStyle] in scope, or `null` to use [CalendarStyle.Default].
 *
 * Not seeded by [dev.voir.areca.theme.ArecaThemeProvider] — see [LocalCalendarSize] for why.
 */
public val LocalCalendarStyle: ProvidableCompositionLocal<CalendarStyle?> =
    compositionLocalOf { null }
