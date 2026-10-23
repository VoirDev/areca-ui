package dev.voir.areca.charts

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.theme.ArecaTheme

/**
 * Colours of a [LineChart].
 *
 * @property palette Colours of series without one of their own, by series index. Never empty.
 * @property grid Horizontal grid lines.
 * @property label X and y labels.
 * @property rule The dashed vertical rule, such as today.
 * @property markerRing Ring around a filled end marker and fill of a hollow one. Match it to what
 *   the chart sits on.
 * @property tooltipBg Fill of the bubble.
 * @property tooltipContent Text of the bubble.
 * @property areaAlpha Opacity of a shaded area at the line, fading to nothing at the baseline.
 * @throws IllegalArgumentException When [palette] is empty or [areaAlpha] is outside `0f..1f`.
 */
@Immutable
public data class LineChartStyle(
    public val palette: List<Color>,
    public val grid: Color,
    public val label: Color,
    public val rule: Color,
    public val markerRing: Color,
    public val tooltipBg: Color,
    public val tooltipContent: Color,
    public val areaAlpha: Float,
) {
    init {
        require(palette.isNotEmpty()) { "A line chart palette needs at least one colour." }
        require(areaAlpha in 0f..1f) { "areaAlpha must be within 0..1, was $areaAlpha." }
    }

    /** Theme-backed presets and builders. */
    public companion object {
        /** Theme default: a brand line against a muted comparison, on the canvas. */
        public val Default: LineChartStyle
            @Composable
            get() = default()

        /**
         * Builds a style from the current theme.
         *
         * @param palette Colours of series by index.
         * @param grid Grid lines.
         * @param label Axis labels.
         * @param rule The vertical rule.
         * @param markerRing Ring around a filled marker, fill of a hollow one.
         * @param tooltipBg Fill of the bubble.
         * @param tooltipContent Text of the bubble.
         * @param areaAlpha Opacity of a shaded area at the line.
         * @return Style built from the supplied colours.
         */
        @Composable
        public fun default(
            palette: List<Color> = listOf(ArecaTheme.colors.bgBrandBase, ArecaTheme.colors.fgMuted),
            grid: Color = ArecaTheme.colors.bgNeutralMuted,
            label: Color = ArecaTheme.colors.fgMuted,
            rule: Color = ArecaTheme.colors.bgNeutralIntense,
            markerRing: Color = ArecaTheme.colors.bgCanvas,
            tooltipBg: Color = ArecaTheme.colors.bgContrast,
            tooltipContent: Color = ArecaTheme.colors.fgOnContrast,
            areaAlpha: Float = 0.22f,
        ): LineChartStyle = LineChartStyle(
            palette = palette,
            grid = grid,
            label = label,
            rule = rule,
            markerRing = markerRing,
            tooltipBg = tooltipBg,
            tooltipContent = tooltipContent,
            areaAlpha = areaAlpha,
        )
    }
}

/**
 * Dimensions and type of a [LineChart].
 *
 * @property height Height of the plot. The chart grows by its x labels below and its bubble above.
 * @property lineStroke Width of a solid line.
 * @property dashedStroke Width of a dashed line.
 * @property dash Length of each dash of a dashed line, and of the gap after it.
 * @property gridStroke Width of a grid line.
 * @property ruleStroke Width of the vertical rule.
 * @property markerRadius Radius of a filled end marker.
 * @property hollowMarkerRadius Radius of a hollow end marker.
 * @property markerRingWidth Width of the ring around a marker.
 * @property yLabelGap Space between the plot and the y labels at its end edge.
 * @property xLabelGap Space between the plot and the x labels.
 * @property labelTextStyle Type of the axis labels.
 * @property tooltipTextStyle Type of the bubble.
 * @property tooltipPadding Space inside the bubble.
 * @property tooltipGap Space between the bubble and its point's marker.
 * @property tooltipShape Shape of the bubble.
 */
@Immutable
public data class LineChartSize(
    public val height: Dp,
    public val lineStroke: Dp,
    public val dashedStroke: Dp,
    public val dash: Dp,
    public val gridStroke: Dp,
    public val ruleStroke: Dp,
    public val markerRadius: Dp,
    public val hollowMarkerRadius: Dp,
    public val markerRingWidth: Dp,
    public val yLabelGap: Dp,
    public val xLabelGap: Dp,
    public val labelTextStyle: TextStyle,
    public val tooltipTextStyle: TextStyle,
    public val tooltipPadding: PaddingValues,
    public val tooltipGap: Dp,
    public val tooltipShape: Shape,
) {
    /** Theme-backed presets and builders. */
    public companion object {
        /** A 140dp plot: a section of its screen. */
        public val Default: LineChartSize
            @Composable
            get() = default()

        /**
         * Builds a size from the current theme.
         *
         * @param height Height of the plot.
         * @param lineStroke Width of a solid line.
         * @param dashedStroke Width of a dashed line.
         * @param dash Dash length of a dashed line.
         * @param gridStroke Width of a grid line.
         * @param ruleStroke Width of the rule.
         * @param markerRadius Radius of a filled marker.
         * @param hollowMarkerRadius Radius of a hollow marker.
         * @param markerRingWidth Width of a marker's ring.
         * @param yLabelGap Space before the y labels.
         * @param xLabelGap Space above the x labels.
         * @param labelTextStyle Type of the axis labels.
         * @param tooltipTextStyle Type of the bubble.
         * @param tooltipPadding Space inside the bubble.
         * @param tooltipGap Space between the bubble and its marker.
         * @param tooltipShape Shape of the bubble.
         * @return Size built from the supplied metrics.
         */
        @Composable
        public fun default(
            height: Dp = 140.dp,
            lineStroke: Dp = 3.dp,
            dashedStroke: Dp = 2.dp,
            dash: Dp = ArecaTheme.spacing.xs,
            gridStroke: Dp = 1.dp,
            ruleStroke: Dp = 1.dp,
            markerRadius: Dp = 5.dp,
            hollowMarkerRadius: Dp = 4.dp,
            markerRingWidth: Dp = 2.dp,
            yLabelGap: Dp = ArecaTheme.spacing.md,
            xLabelGap: Dp = ArecaTheme.spacing.sm,
            labelTextStyle: TextStyle = ArecaTheme.typography.bodyExtraSmall,
            tooltipTextStyle: TextStyle = ArecaTheme.typography.bodyExtraSmall,
            tooltipPadding: PaddingValues = PaddingValues(horizontal = ArecaTheme.spacing.md, vertical = ArecaTheme.spacing.xs2),
            tooltipGap: Dp = ArecaTheme.spacing.sm,
            tooltipShape: Shape = ArecaTheme.shapes.full,
        ): LineChartSize = LineChartSize(
            height = height,
            lineStroke = lineStroke,
            dashedStroke = dashedStroke,
            dash = dash,
            gridStroke = gridStroke,
            ruleStroke = ruleStroke,
            markerRadius = markerRadius,
            hollowMarkerRadius = hollowMarkerRadius,
            markerRingWidth = markerRingWidth,
            yLabelGap = yLabelGap,
            xLabelGap = xLabelGap,
            labelTextStyle = labelTextStyle,
            tooltipTextStyle = tooltipTextStyle,
            tooltipPadding = tooltipPadding,
            tooltipGap = tooltipGap,
            tooltipShape = tooltipShape,
        )
    }
}

/**
 * Default [LineChartStyle] in scope, or `null` to use [LineChartStyle.Default].
 *
 * Not seeded by [dev.voir.areca.theme.ArecaThemeProvider] — see [LocalBarChartStyle] for why.
 */
public val LocalLineChartStyle: ProvidableCompositionLocal<LineChartStyle?> = compositionLocalOf { null }

/**
 * Default [LineChartSize] in scope, or `null` to use [LineChartSize.Default].
 *
 * Not seeded by [dev.voir.areca.theme.ArecaThemeProvider] — see [LocalBarChartStyle] for why.
 */
public val LocalLineChartSize: ProvidableCompositionLocal<LineChartSize?> = compositionLocalOf { null }
