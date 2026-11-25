package dev.voir.areca.datetime.calendar

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.button.ButtonSize
import dev.voir.areca.foundation.button.IconButtonSize
import dev.voir.areca.foundation.icon.ChevronSize
import dev.voir.areca.theme.ArecaTheme

/**
 * Dimensions and type of one step of [Calendar].
 *
 * Day cells are square and share the calendar's width equally, so their size is not stated here —
 * it follows from how wide the calendar is. What is stated is everything around them: the insets
 * that decide how much of each square the day's circle fills, the gaps between the three bands, and
 * the type each band is set in.
 *
 * @property contentPadding Inset around the day grid. Matches [weekdayContentPadding] and
 *   [headerContentPadding] so the three bands line up.
 * @property monthSpacing Gap between adjacent months in the pager. Visible only mid-swipe.
 * @property headerSpacing Gap below the month header. Applied by [Calendar], so a custom header does
 *   not have to carry its own spacer.
 * @property weekSpacing Vertical gap between week rows.
 * @property weekdaySpacing Gap below the weekday row.
 * @property dayPadding Inset between a day's square cell and its circle. Larger values give smaller,
 *   more separated circles.
 * @property dayTextStyle Type of the day numbers.
 * @property dayTilePadding Inset between a day's cell and its tile, under [CalendarDayShape.Tile].
 *   Smaller than [dayPadding]: a tile is the whole cell, with only a hairline between neighbours.
 * @property dayTileShape Shape of a day's tile under [CalendarDayShape.Tile].
 * @property dayBorderWidth Width of a day's outline, used to ring today.
 * @property weekdayContentPadding Inset around the weekday row.
 * @property weekdayPadding Inset inside one weekday heading.
 * @property weekdayTextStyle Type of the weekday headings.
 * @property headerContentPadding Inset around the month header.
 * @property headerTextStyle Type of the title. Also what [levelToggle] is set in by default, so the
 *   header reads the same whether or not the title steps out a level.
 * @property levelToggle Size of the title control that steps out a level: its inset, the gap
 *   before its chevron, and its height. Its minimum height matches [monthControl] by default, which
 *   is what keeps the label and the stepping buttons on one line rather than merely near one.
 * @property headerChevron Size of **every** chevron in the header — the disclosure beside the month
 *   and the two nav buttons alike. One token rather than three, because chevrons sitting on the same
 *   line at different sizes read as a mistake. Match it to [headerTextStyle]: these glyphs sit beside
 *   the month name, so they should agree with the type rather than with the buttons carrying them.
 *   [monthControl] sizes those buttons' tap targets and is independent of this.
 * @property monthControlSpacing Gap between the previous and next month buttons.
 * @property monthControl Size of the previous and next month buttons.
 * @property markerLaneHeight Height of the row each day keeps under its number once a calendar is
 *   given markers. A day cell is then this much taller than wide.
 * @property markerDotSize Diameter of a [CalendarMarker.Dots] dot.
 * @property markerSpacing Gap between dots, and half a count pill's side inset.
 * @property markerTextStyle Type of a [CalendarMarker.Label], a [CalendarMarker.Count] and a dot
 *   overflow.
 * @property markerIconSize Size of a [CalendarMarker.Icon].
 * @property markerBarHeight Thickness of a [CalendarMarker.Load] bar.
 * @property levelHintTextStyle Type of the hint beside the title while months or years are showing.
 * @property tileSpacing Gap between month and year tiles, across and down.
 * @property tileShape Shape of a month or year tile.
 * @property tileTextStyle Type of a month or year tile.
 */
@Immutable
public data class CalendarSize(
    public val contentPadding: PaddingValues,
    public val monthSpacing: Dp,
    public val headerSpacing: Dp,
    public val weekSpacing: Dp,
    public val weekdaySpacing: Dp,
    public val dayPadding: PaddingValues,
    public val dayTextStyle: TextStyle,
    public val dayTilePadding: PaddingValues,
    public val dayTileShape: Shape,
    public val dayBorderWidth: Dp,
    public val weekdayContentPadding: PaddingValues,
    public val weekdayPadding: PaddingValues,
    public val weekdayTextStyle: TextStyle,
    public val headerContentPadding: PaddingValues,
    public val headerTextStyle: TextStyle,
    public val levelToggle: ButtonSize,
    public val headerChevron: ChevronSize,
    public val monthControlSpacing: Dp,
    public val monthControl: IconButtonSize,
    public val markerLaneHeight: Dp,
    public val markerDotSize: Dp,
    public val markerSpacing: Dp,
    public val markerTextStyle: TextStyle,
    public val markerIconSize: Dp,
    public val markerBarHeight: Dp,
    public val levelHintTextStyle: TextStyle,
    public val tileSpacing: Dp,
    public val tileShape: Shape,
    public val tileTextStyle: TextStyle,
) {
    /** Theme-derived sizes and the builder they share. */
    public companion object {
        /** Theme default. A calendar with room around it — a page, a sheet, a wide dialog. */
        public val Default: CalendarSize
            @Composable
            get() = default()

        /**
         * Tighter throughout, for a calendar inside something else: a date field's dropdown, a
         * sidebar, a popover.
         *
         * Only the insets and the gaps shrink; the type does not, because day numbers are the one
         * thing in a calendar nobody should have to squint at.
         */
        public val Compact: CalendarSize
            @Composable
            get() = default(
                contentPadding = PaddingValues(horizontal = ArecaTheme.spacing.md),
                monthSpacing = ArecaTheme.spacing.md,
                headerSpacing = ArecaTheme.spacing.md,
                weekSpacing = ArecaTheme.spacing.none,
                weekdaySpacing = ArecaTheme.spacing.none,
                dayPadding = PaddingValues(ArecaTheme.spacing.xs),
                weekdayContentPadding = PaddingValues(horizontal = ArecaTheme.spacing.md),
                weekdayPadding = PaddingValues(ArecaTheme.spacing.xs),
                headerContentPadding = PaddingValues(horizontal = ArecaTheme.spacing.md),
                // headerChevron is deliberately not shrunk: Compact keeps the type, and the header
                // chevrons are sized against the type. Only the button around them tightens.
                monthControl = IconButtonSize.ExtraSmall,
                tileSpacing = ArecaTheme.spacing.xs,
                markerLaneHeight = 12.dp,
                markerDotSize = 4.dp,
                markerIconSize = 10.dp,
            )

        /**
         * Builds a size from the current theme.
         *
         * Every parameter defaults to the value [Default] uses, so overriding one stays a one-line
         * change.
         *
         * @param contentPadding Inset around the day grid.
         * @param monthSpacing Gap between adjacent months in the pager.
         * @param headerSpacing Gap below the month header.
         * @param weekSpacing Vertical gap between week rows.
         * @param weekdaySpacing Gap below the weekday row.
         * @param dayPadding Inset between a day's cell and its circle.
         * @param dayTextStyle Type of the day numbers.
         * @param dayTilePadding Inset between a day's cell and its tile.
         * @param dayTileShape Shape of a day's tile.
         * @param dayBorderWidth Width of a day's outline.
         * @param weekdayContentPadding Inset around the weekday row.
         * @param weekdayPadding Inset inside one weekday heading.
         * @param weekdayTextStyle Type of the weekday headings.
         * @param headerContentPadding Inset around the month header.
         * @param headerTextStyle Type of the month and year.
         * @param levelToggle Size of the title control that steps out a level.
         * @param headerChevron Size of every chevron in the header.
         * @param monthControlSpacing Gap between the previous and next month buttons.
         * @param monthControl Size of the previous and next month buttons.
         * @param markerLaneHeight Height of the row under each day's number once markers are given.
         * @param markerDotSize Diameter of a dot marker.
         * @param markerSpacing Gap between dots.
         * @param markerTextStyle Type of label and count markers.
         * @param markerIconSize Size of an icon marker.
         * @param markerBarHeight Thickness of a load marker.
         * @param levelHintTextStyle Type of the hint beside the title.
         * @param tileSpacing Gap between month and year tiles.
         * @param tileShape Shape of a month or year tile.
         * @param tileTextStyle Type of a month or year tile.
         * @return Size built from the given values.
         */
        @Composable
        public fun default(
            contentPadding: PaddingValues = PaddingValues(horizontal = ArecaTheme.spacing.xl),
            monthSpacing: Dp = ArecaTheme.spacing.xl,
            headerSpacing: Dp = ArecaTheme.spacing.lg,
            weekSpacing: Dp = ArecaTheme.spacing.xs,
            weekdaySpacing: Dp = ArecaTheme.spacing.xs,
            dayPadding: PaddingValues = PaddingValues(ArecaTheme.spacing.xs),
            dayTextStyle: TextStyle = ArecaTheme.baseTypography.sm,
            dayTilePadding: PaddingValues = PaddingValues(ArecaTheme.spacing.xs2),
            dayTileShape: Shape = ArecaTheme.shapes.md,
            dayBorderWidth: Dp = DayBorderWidth,
            weekdayContentPadding: PaddingValues = PaddingValues(horizontal = ArecaTheme.spacing.xl),
            weekdayPadding: PaddingValues = PaddingValues(ArecaTheme.spacing.xs),
            weekdayTextStyle: TextStyle = ArecaTheme.baseTypography.sm.merge(
                fontWeight = FontWeight.Medium,
            ),
            headerContentPadding: PaddingValues = PaddingValues(horizontal = ArecaTheme.spacing.xl),
            headerTextStyle: TextStyle = ArecaTheme.baseTypography.md.merge(
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Start,
            ),
            headerChevron: ChevronSize = ChevronSize.Small,
            monthControlSpacing: Dp = ArecaTheme.spacing.xs,
            monthControl: IconButtonSize = IconButtonSize.Small,
            levelToggle: ButtonSize = ButtonSize.Small.copy(
                borderWidth = 0.dp,
                padding = PaddingValues(
                    horizontal = ArecaTheme.spacing.sm,
                    vertical = ArecaTheme.spacing.xs,
                ),
                gap = ArecaTheme.spacing.md,
                textStyle = headerTextStyle,
                // Wraps the month name rather than reserving a button's worth of width, so what is
                // pressable is what looks pressable.
                minWidth = 0.dp,
                // The stepping buttons' own height, so the two sit on one line.
                minHeight = monthControl.container,
            ),
            markerLaneHeight: Dp = 14.dp,
            markerDotSize: Dp = 5.dp,
            markerSpacing: Dp = ArecaTheme.spacing.xs2,
            markerTextStyle: TextStyle = ArecaTheme.baseTypography.xxs.merge(fontWeight = FontWeight.Medium),
            markerIconSize: Dp = 12.dp,
            markerBarHeight: Dp = 3.dp,
            levelHintTextStyle: TextStyle = ArecaTheme.typography.bodyExtraSmall,
            tileSpacing: Dp = ArecaTheme.spacing.sm,
            tileShape: Shape = ArecaTheme.shapes.md,
            tileTextStyle: TextStyle = dayTextStyle,
        ): CalendarSize = CalendarSize(
            contentPadding = contentPadding,
            monthSpacing = monthSpacing,
            headerSpacing = headerSpacing,
            weekSpacing = weekSpacing,
            weekdaySpacing = weekdaySpacing,
            dayPadding = dayPadding,
            dayTextStyle = dayTextStyle,
            dayTilePadding = dayTilePadding,
            dayTileShape = dayTileShape,
            dayBorderWidth = dayBorderWidth,
            weekdayContentPadding = weekdayContentPadding,
            weekdayPadding = weekdayPadding,
            weekdayTextStyle = weekdayTextStyle,
            headerContentPadding = headerContentPadding,
            headerTextStyle = headerTextStyle,
            headerChevron = headerChevron,
            monthControlSpacing = monthControlSpacing,
            monthControl = monthControl,
            levelToggle = levelToggle,
            markerLaneHeight = markerLaneHeight,
            markerDotSize = markerDotSize,
            markerSpacing = markerSpacing,
            markerTextStyle = markerTextStyle,
            markerIconSize = markerIconSize,
            markerBarHeight = markerBarHeight,
            levelHintTextStyle = levelHintTextStyle,
            tileSpacing = tileSpacing,
            tileShape = tileShape,
            tileTextStyle = tileTextStyle,
        )
    }
}

/**
 * Default [CalendarSize] in scope, or `null` to use [CalendarSize.Default].
 *
 * Unlike the foundation's component defaults this is **not** seeded by
 * [dev.voir.areca.theme.ArecaThemeProvider]: that provider lives in `:foundation`, and `:foundation`
 * does not depend on `:datetime`. Wire it yourself around a subtree that wants a different default:
 *
 * ```kotlin
 * CompositionLocalProvider(LocalCalendarSize provides CalendarSize.Compact) { … }
 * ```
 */
public val LocalCalendarSize: ProvidableCompositionLocal<CalendarSize?> =
    compositionLocalOf { null }

/**
 * Width of the ring around today: the stroke foundation's checkbox and radio draw their outlines
 * with, so the calendar's one outlined state reads at the same weight as the selection controls.
 */
private val DayBorderWidth: Dp = 1.5.dp
