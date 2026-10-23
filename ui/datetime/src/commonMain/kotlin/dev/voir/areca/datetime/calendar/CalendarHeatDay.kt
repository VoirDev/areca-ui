package dev.voir.areca.datetime.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.theme.ArecaTheme

/**
 * One day of a heat-map calendar: a rounded tile shaded by how much the day held, with its number
 * over a short amount.
 *
 * Pass it as a [Calendar]'s `day` slot. The tile keeps the grid's keyboard navigation, selection
 * and tap behaviour of [CalendarDay]; only the drawing differs:
 *
 * - **A day with something** is filled with [level]'s shade of [CalendarHeatStyle.ramp], and its
 *   number and amount take whichever ink — dark or light — reads on that shade.
 * - **A day with nothing** ([level] `null`, still available) is left empty inside a hairline ring,
 *   so "nothing" is told apart from "not yet".
 * - **A day still to come** — one the calendar's `dateRule` refuses — is a flat pale tile with a
 *   muted number and no amount, and takes no tap.
 * - **Today** and **the selected day** are ringed, the selection over today.
 *
 * Map amounts to levels with [calendarHeatLevel], so every screen shades on the same rule, passing
 * the size of the ramp the tiles are drawn from — [CalendarHeatStyle.ramp], which defaults to the
 * theme's accent scale.
 *
 * Assistive technology reads [contentDescription], or the calendar's description of the day
 * followed by [amount].
 *
 * @param day Everything about the day this tile draws.
 * @param level Index into [CalendarHeatStyle.ramp] — higher is more — or `null` for a day with
 *   nothing. Clamped into the ramp.
 * @param modifier Modifier applied to the cell.
 * @param amount The day's figure, short: "€84", "€1.2k". `null` draws the number alone.
 * @param onClick Called when the day is tapped. Ignored when the day is unavailable, and `null` for
 *   a read-only calendar.
 * @param contentDescription What assistive technology reads for the day, or `null` for the date and
 *   the amount.
 * @param style Colours. Defaults to [LocalCalendarHeatStyle], or [CalendarHeatStyle.Default].
 * @param size Shape, rings and type. Defaults to [LocalCalendarHeatSize], or
 *   [CalendarHeatSize.Default].
 */
@Composable
public fun CalendarScope.CalendarHeatDay(
    day: CalendarDayState,
    level: Int?,
    modifier: Modifier = Modifier,
    amount: String? = null,
    onClick: (() -> Unit)? = null,
    contentDescription: String? = null,
    style: CalendarHeatStyle = LocalCalendarHeatStyle.current ?: CalendarHeatStyle.Default,
    size: CalendarHeatSize = LocalCalendarHeatSize.current ?: CalendarHeatSize.Default,
) {
    val source = remember { MutableInteractionSource() }
    val focusable = onClick != null && !day.isAdjacent
    val future = !day.isEnabled
    val shade = level?.takeIf { !future }?.coerceIn(0, style.ramp.lastIndex)?.let { style.ramp[it] }
    val fill = when {
        future -> style.future
        shade != null -> shade
        else -> style.empty
    }
    val ink = when {
        future -> style.futureContent
        shade != null -> style.inkOn(shade)
        else -> style.content
    }
    val (ringColor, ringWidth) = when {
        day.isSelected -> style.selectedRing to size.ringWidth
        day.isToday -> style.todayRing to size.ringWidth
        !future && shade == null -> style.emptyRing to size.emptyRingWidth
        else -> Color.Transparent to 0.dp
    }
    val description = contentDescription
        ?: listOfNotNull(labels.dayDescription(day.date), amount?.takeIf { !future }).joinToString(", ")

    Box(
        modifier = modifier
            .fillMaxSize()
            .then(dayCell(day = day, focusable = focusable))
            .semantics { this.contentDescription = description }
            .dayInteraction(day = day, onClick = onClick, focusable = focusable, interactionSource = source),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .padding(size.cellPadding)
                .fillMaxSize()
                .background(fill, size.shape)
                .border(ringWidth, ringColor, size.shape)
                .dayIndication(
                    visible = onClick != null && (day.isEnabled || focusable),
                    interactionSource = source,
                    shape = size.shape,
                )
                .clearAndSetSemantics { },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(size.lineGap, Alignment.CenterVertically),
        ) {
            Text(text = labels.dayNumber(day.date.day), style = size.numberTextStyle, color = ink, maxLines = 1)
            if (amount != null && !future) {
                Text(
                    text = amount,
                    style = size.amountTextStyle,
                    color = ink,
                    maxLines = 1,
                    overflow = TextOverflow.Clip,
                )
            }
        }
    }
}

/**
 * The key to a heat-map calendar: the lowest figure, a row of the ramp's shades, the highest
 * figure, and optionally the mark for a day with nothing. Read as one node.
 *
 * @param lowLabel The figure at the faint end: "€0".
 * @param highLabel The figure at the strong end: "€280".
 * @param modifier Modifier applied to the legend, which fills the width it is given.
 * @param emptyLabel What an empty ringed tile means — "nothing spent" — or `null` to leave it out.
 * @param steps How many shades the row shows, picked evenly from the ramp. At least `2`.
 * @param contentDescription What assistive technology reads for the whole legend — "Shaded from €0
 *   to €280; a ringed day had nothing" — or `null` for its labels in order.
 * @param style Colours. Defaults to [LocalCalendarHeatStyle], or [CalendarHeatStyle.Default].
 * @param size Swatch size and type. Defaults to [LocalCalendarHeatSize], or
 *   [CalendarHeatSize.Default].
 */
@Composable
public fun CalendarHeatLegend(
    lowLabel: String,
    highLabel: String,
    modifier: Modifier = Modifier,
    emptyLabel: String? = null,
    steps: Int = 4,
    contentDescription: String? = null,
    style: CalendarHeatStyle = LocalCalendarHeatStyle.current ?: CalendarHeatStyle.Default,
    size: CalendarHeatSize = LocalCalendarHeatSize.current ?: CalendarHeatSize.Default,
) {
    val description = contentDescription ?: listOfNotNull(lowLabel, highLabel, emptyLabel).joinToString(", ")
    val shades = calendarHeatLegendShades(style.ramp.size, steps).map { style.ramp[it] }
    Row(
        modifier = modifier.fillMaxWidth().clearAndSetSemantics { this.contentDescription = description },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(size.legendGap),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = lowLabel, style = size.legendTextStyle, color = style.legendLabel)
            shades.forEach { shade ->
                Box(modifier = Modifier.size(size.legendSwatch).background(shade, size.legendSwatchShape))
            }
            Text(text = highLabel, style = size.legendTextStyle, color = style.legendLabel)
        }
        if (emptyLabel != null) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(size.legendGap),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(size.legendSwatch.height)
                        .background(style.empty, size.legendSwatchShape)
                        .border(size.emptyRingWidth, style.emptyRing, size.legendSwatchShape),
                )
                Text(text = emptyLabel, style = size.legendTextStyle, color = style.legendLabel)
            }
        }
    }
}

/**
 * The shade a day's figure takes on a heat-map calendar.
 *
 * Zero, negative and non-finite figures have no shade — the day had nothing. Any positive figure
 * takes one of the ramp's shades after the faintest, which is kept for the legend's low end, so a
 * small figure still reads as something: `1 + ⌊value ÷ max × (steps − 1)⌋`, at most the last.
 *
 * @param value The day's figure.
 * @param max The figure that takes the strongest shade, such as the month's busiest day. Figures
 *   above it take the strongest shade too.
 * @param steps How many shades the ramp has. At least `2`.
 * @return Index into the ramp, or `null` for a day with nothing.
 * @throws IllegalArgumentException When [steps] is below `2`.
 */
public fun calendarHeatLevel(value: Float, max: Float, steps: Int): Int? {
    require(steps >= 2) { "A heat ramp needs at least two shades, was $steps." }
    if (!value.isFinite() || value <= 0f) return null
    if (!max.isFinite() || max <= 0f) return steps - 1
    val share = (value / max).coerceAtMost(1f)
    return (1 + (share * (steps - 1)).toInt()).coerceAtMost(steps - 1)
}

/**
 * Indices of the shades a [CalendarHeatLegend] shows: [steps] of them, spread evenly from the
 * first to the last.
 *
 * @param rampSize How many shades the ramp has.
 * @param steps How many to show, clamped to `2..rampSize`.
 * @return Indices in increasing order.
 */
internal fun calendarHeatLegendShades(rampSize: Int, steps: Int): List<Int> {
    if (rampSize <= 1) return List(rampSize) { it }
    val count = steps.coerceIn(2, rampSize)
    return List(count) { i -> (i * (rampSize - 1) + (count - 1) / 2) / (count - 1) }
}

/**
 * Colours of a [CalendarHeatDay] and [CalendarHeatLegend].
 *
 * @property ramp Shades from the faintest to the strongest. Never empty.
 * @property empty Fill of an available day with nothing.
 * @property emptyRing Hairline ring of an available day with nothing.
 * @property future Fill of a day still to come.
 * @property futureContent Number of a day still to come.
 * @property content Number and amount of a day with nothing, on [empty].
 * @property inkOnLight Number and amount on a light shade.
 * @property inkOnDark Number and amount on a dark shade.
 * @property todayRing Ring around today.
 * @property selectedRing Ring around the selected day, drawn over today's.
 * @property legendLabel Text of the legend.
 * @throws IllegalArgumentException When [ramp] is empty.
 */
@Immutable
public data class CalendarHeatStyle(
    public val ramp: List<Color>,
    public val empty: Color,
    public val emptyRing: Color,
    public val future: Color,
    public val futureContent: Color,
    public val content: Color,
    public val inkOnLight: Color,
    public val inkOnDark: Color,
    public val todayRing: Color,
    public val selectedRing: Color,
    public val legendLabel: Color,
) {
    init {
        require(ramp.isNotEmpty()) { "A heat ramp needs at least one shade." }
    }

    /**
     * The ink that reads on [shade]: [inkOnLight] on a light one, [inkOnDark] on a dark one.
     *
     * @param shade A fill from the ramp.
     * @return The ink for text on it.
     */
    public fun inkOn(shade: Color): Color = if (shade.luminance() > InkLuminanceSplit) inkOnLight else inkOnDark

    /** Theme-backed presets and builders. */
    public companion object {
        /** Theme default: the accent scale, an accent ring for today and an ink ring for the pick. */
        public val Default: CalendarHeatStyle
            @Composable
            get() = default()

        /**
         * Builds a style from the current theme.
         *
         * @param ramp Shades from the faintest to the strongest.
         * @param empty Fill of a day with nothing.
         * @param emptyRing Ring of a day with nothing.
         * @param future Fill of a day still to come.
         * @param futureContent Number of a day still to come.
         * @param content Number and amount of a day with nothing.
         * @param inkOnLight Text on a light shade.
         * @param inkOnDark Text on a dark shade.
         * @param todayRing Ring around today.
         * @param selectedRing Ring around the selected day.
         * @param legendLabel Text of the legend.
         * @return Style built from the supplied colours.
         */
        @Composable
        public fun default(
            ramp: List<Color> = ArecaTheme.colors.accentScale,
            empty: Color = ArecaTheme.colors.bgCanvas,
            emptyRing: Color = ArecaTheme.colors.bgNeutralIntense,
            future: Color = ArecaTheme.colors.bgSoft,
            futureContent: Color = ArecaTheme.colors.fgDisabled,
            content: Color = ArecaTheme.colors.fgBase,
            inkOnLight: Color = ArecaTheme.baseColors.neutral100,
            inkOnDark: Color = ArecaTheme.baseColors.baseWhite,
            todayRing: Color = ArecaTheme.colors.accentBase,
            selectedRing: Color = ArecaTheme.colors.fgBase,
            legendLabel: Color = ArecaTheme.colors.fgSubtle,
        ): CalendarHeatStyle = CalendarHeatStyle(
            ramp = ramp,
            empty = empty,
            emptyRing = emptyRing,
            future = future,
            futureContent = futureContent,
            content = content,
            inkOnLight = inkOnLight,
            inkOnDark = inkOnDark,
            todayRing = todayRing,
            selectedRing = selectedRing,
            legendLabel = legendLabel,
        )
    }
}

/**
 * Shape, rings and type of a [CalendarHeatDay] and [CalendarHeatLegend].
 *
 * @property cellPadding Space between the tile and its grid cell, which spaces the tiles apart.
 * @property shape Shape of a tile.
 * @property ringWidth Width of the today and selection rings.
 * @property emptyRingWidth Width of the hairline ring of a day with nothing.
 * @property lineGap Space between the number and the amount.
 * @property numberTextStyle Type of the day number.
 * @property amountTextStyle Type of the amount.
 * @property legendSwatch Size of a legend shade.
 * @property legendSwatchShape Shape of a legend shade.
 * @property legendGap Space between the legend's parts.
 * @property legendTextStyle Type of the legend.
 */
@Immutable
public data class CalendarHeatSize(
    public val cellPadding: Dp,
    public val shape: Shape,
    public val ringWidth: Dp,
    public val emptyRingWidth: Dp,
    public val lineGap: Dp,
    public val numberTextStyle: TextStyle,
    public val amountTextStyle: TextStyle,
    public val legendSwatch: DpSize,
    public val legendSwatchShape: Shape,
    public val legendGap: Dp,
    public val legendTextStyle: TextStyle,
) {
    /** Theme-backed presets and builders. */
    public companion object {
        /** Theme default. */
        public val Default: CalendarHeatSize
            @Composable
            get() = default()

        /**
         * Builds a size from the current theme.
         *
         * @param cellPadding Space between the tile and its grid cell.
         * @param shape Shape of a tile.
         * @param ringWidth Width of the today and selection rings.
         * @param emptyRingWidth Width of the ring of a day with nothing.
         * @param lineGap Space between the number and the amount.
         * @param numberTextStyle Type of the day number.
         * @param amountTextStyle Type of the amount.
         * @param legendSwatch Size of a legend shade.
         * @param legendSwatchShape Shape of a legend shade.
         * @param legendGap Space between the legend's parts.
         * @param legendTextStyle Type of the legend.
         * @return Size built from the supplied metrics.
         */
        @Composable
        public fun default(
            cellPadding: Dp = ArecaTheme.spacing.xs2,
            shape: Shape = ArecaTheme.shapes.md,
            ringWidth: Dp = 2.dp,
            emptyRingWidth: Dp = 1.dp,
            lineGap: Dp = 0.dp,
            numberTextStyle: TextStyle = ArecaTheme.typography.bodySmall,
            amountTextStyle: TextStyle = ArecaTheme.typography.bodyExtraSmall,
            legendSwatch: DpSize = DpSize(14.dp, 10.dp),
            legendSwatchShape: Shape = ArecaTheme.shapes.xs,
            legendGap: Dp = ArecaTheme.spacing.xs,
            legendTextStyle: TextStyle = ArecaTheme.typography.bodyExtraSmall,
        ): CalendarHeatSize = CalendarHeatSize(
            cellPadding = cellPadding,
            shape = shape,
            ringWidth = ringWidth,
            emptyRingWidth = emptyRingWidth,
            lineGap = lineGap,
            numberTextStyle = numberTextStyle,
            amountTextStyle = amountTextStyle,
            legendSwatch = legendSwatch,
            legendSwatchShape = legendSwatchShape,
            legendGap = legendGap,
            legendTextStyle = legendTextStyle,
        )
    }
}

/** Relative luminance above which a shade takes [CalendarHeatStyle.inkOnLight]. */
private const val InkLuminanceSplit = 0.4f

/**
 * Default [CalendarHeatStyle] in scope, or `null` to use [CalendarHeatStyle.Default].
 *
 * Not seeded by [dev.voir.areca.theme.ArecaThemeProvider], which lives in `foundation` and does not
 * know this module; provide it yourself around the screens that want another default.
 */
public val LocalCalendarHeatStyle: ProvidableCompositionLocal<CalendarHeatStyle?> =
    compositionLocalOf { null }

/**
 * Default [CalendarHeatSize] in scope, or `null` to use [CalendarHeatSize.Default].
 *
 * Not seeded by [dev.voir.areca.theme.ArecaThemeProvider], which lives in `foundation` and does not
 * know this module; provide it yourself around the screens that want another default.
 */
public val LocalCalendarHeatSize: ProvidableCompositionLocal<CalendarHeatSize?> =
    compositionLocalOf { null }
