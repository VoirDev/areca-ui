package dev.voir.areca.datetime.calendar.agenda

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import dev.voir.areca.theme.ArecaTheme

/**
 * Colours of a [CalendarAgenda]: the grounds of the calendar and the list, the day headings and
 * their highlight, and the handle.
 *
 * A ground left [Color.Unspecified] is the raised fill of the surface the agenda is drawn on
 * (`ArecaTheme.surface.raised`), resolved where it is drawn — a white card on a grey page, a grey one
 * on a white page.
 *
 * @property calendarBg Ground of the calendar.
 * @property listBg Ground of the list, behind every row.
 * @property headingBg Ground of a day heading, which must be opaque: the pinned heading covers the
 *   rows scrolling under it.
 * @property highlight Tint a heading flashes when a tap on its day scrolls to it.
 * @property handle The bar under a phone's calendar that folds and unfolds it.
 * @property lift Colour of the shadow under the calendar while rows scroll beneath it.
 */
@Immutable
public data class CalendarAgendaStyle(
    public val calendarBg: Color,
    public val listBg: Color,
    public val headingBg: Color,
    public val highlight: Color,
    public val handle: Color,
    public val lift: Color,
) {
    /** Theme-derived styles and the builder they share. */
    public companion object {
        /** Theme default: the surface's raised fill, a brand-tinted highlight, a hairline handle. */
        public val Default: CalendarAgendaStyle
            @Composable
            get() = default()

        /**
         * Builds a style from the current theme.
         *
         * @param calendarBg Ground of the calendar, or [Color.Unspecified] for the surface's raised
         *   fill.
         * @param listBg Ground of the list, or [Color.Unspecified] for the surface's raised fill.
         * @param headingBg Ground of a day heading, or [Color.Unspecified] for [listBg].
         * @param highlight Tint a heading flashes when its day is tapped.
         * @param handle The bar that folds and unfolds the calendar.
         * @param lift Colour of the shadow under the calendar.
         * @return Style built from the given colours.
         */
        @Composable
        public fun default(
            calendarBg: Color = Color.Unspecified,
            listBg: Color = Color.Unspecified,
            headingBg: Color = Color.Unspecified,
            highlight: Color = ArecaTheme.colors.bgBrandMuted,
            handle: Color = ArecaTheme.colors.borderBase,
            lift: Color = ArecaTheme.colors.bgOverlayDark,
        ): CalendarAgendaStyle = CalendarAgendaStyle(
            calendarBg = calendarBg,
            listBg = listBg,
            headingBg = headingBg,
            highlight = highlight,
            handle = handle,
            lift = lift,
        )
    }
}

/**
 * Default [CalendarAgendaStyle] in scope, or `null` to use [CalendarAgendaStyle.Default]. Not
 * seeded by `ArecaThemeProvider`; provide it around the screens that want another look.
 */
public val LocalCalendarAgendaStyle: ProvidableCompositionLocal<CalendarAgendaStyle?> =
    compositionLocalOf { null }

/**
 * Dimensions of a [CalendarAgenda].
 *
 * @property shape Corners of the cards the calendar and the list are drawn on. On a phone the two
 *   share one card running to the bottom edge, so only its top corners are drawn.
 * @property headingPadding Inset around a day heading's content.
 * @property handleSize Size of the handle's bar.
 * @property handleHeight Height of the band the handle sits in, all of it pressable.
 * @property lift Elevation of the shadow under the folded calendar.
 * @property paneSpacing Gap between the calendar card and the list card on a wide window, and
 *   around them.
 * @property calendarPaneWidth Width of the calendar card on a wide window.
 */
@Immutable
public data class CalendarAgendaSize(
    public val shape: Shape,
    public val headingPadding: PaddingValues,
    public val handleSize: DpSize,
    public val handleHeight: Dp,
    public val lift: Dp,
    public val paneSpacing: Dp,
    public val calendarPaneWidth: Dp,
) {
    /** Theme-derived sizes and the builder they share. */
    public companion object {
        /** Theme default. */
        public val Default: CalendarAgendaSize
            @Composable
            get() = default()

        /**
         * Builds a size from the current theme.
         *
         * @param shape Corners of the calendar and list cards.
         * @param headingPadding Inset around a day heading's content.
         * @param handleSize Size of the handle's bar.
         * @param handleHeight Height of the handle's band.
         * @param lift Elevation of the shadow under the folded calendar.
         * @param paneSpacing Gap between and around the cards on a wide window.
         * @param calendarPaneWidth Width of the calendar card on a wide window.
         * @return Size built from the given values.
         */
        @Composable
        public fun default(
            shape: Shape = ArecaTheme.shapes.surface,
            headingPadding: PaddingValues = PaddingValues(
                start = ArecaTheme.spacing.xl,
                end = ArecaTheme.spacing.xl,
                top = ArecaTheme.spacing.lg,
                bottom = ArecaTheme.spacing.sm,
            ),
            handleSize: DpSize = DpSize(32.dp, 4.dp),
            handleHeight: Dp = ArecaTheme.spacing.xl2,
            lift: Dp = ArecaTheme.elevation.md,
            paneSpacing: Dp = ArecaTheme.spacing.xl,
            calendarPaneWidth: Dp = 360.dp,
        ): CalendarAgendaSize = CalendarAgendaSize(
            shape = shape,
            headingPadding = headingPadding,
            handleSize = handleSize,
            handleHeight = handleHeight,
            lift = lift,
            paneSpacing = paneSpacing,
            calendarPaneWidth = calendarPaneWidth,
        )
    }
}

/**
 * Default [CalendarAgendaSize] in scope, or `null` to use [CalendarAgendaSize.Default]. Not seeded
 * by `ArecaThemeProvider`.
 */
public val LocalCalendarAgendaSize: ProvidableCompositionLocal<CalendarAgendaSize?> =
    compositionLocalOf { null }

/**
 * Words a [CalendarAgenda] adds to its calendar's [dev.voir.areca.datetime.calendar.CalendarLabels].
 *
 * @property showMonthAction What assistive technology reads for the handle while the calendar is
 *   folded to a week.
 * @property showWeekAction What assistive technology reads for the handle while the whole month
 *   shows.
 */
@Immutable
public data class CalendarAgendaLabels(
    public val showMonthAction: String,
    public val showWeekAction: String,
) {
    /** Ready-made label sets. */
    public companion object {
        /** English: `Show the whole month`, `Show one week`. */
        public val English: CalendarAgendaLabels = CalendarAgendaLabels(
            showMonthAction = "Show the whole month",
            showWeekAction = "Show one week",
        )
    }
}

/**
 * [shape] with its bottom corners squared, for the card that runs to the bottom edge on a phone.
 *
 * @return The top-rounded shape, or [shape] itself when it has no corners to square.
 */
internal fun Shape.topOnly(): Shape = when (this) {
    is CornerBasedShape -> copy(bottomStart = CornerSize(0.dp), bottomEnd = CornerSize(0.dp))
    else -> this
}
