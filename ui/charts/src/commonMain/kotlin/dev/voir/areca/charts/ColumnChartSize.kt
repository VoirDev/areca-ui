package dev.voir.areca.charts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.theme.ArecaTheme

/**
 * Dimensions and type of one step of [ColumnChart].
 *
 * Take a step — [Small], [Medium], [Large] — and change one value with `copy`.
 *
 * @property plotHeight Height of the area the bars and value labels share. The chart grows by its
 *   labels below it.
 * @property valueLabelSpace Room kept at the top of the plot for a value label above the tallest
 *   bar, so a bar at the top of the scale never collides with its label.
 * @property columnWidth Width of a column under [ColumnChartLayout.Scroll].
 * @property columnGap Space between neighbouring columns.
 * @property barWidth Width of a regular bar.
 * @property companionWidth Width of a companion bar.
 * @property barGap Space between the bars of one column.
 * @property stackGap Space between the segments of a stacked bar.
 * @property barCornerRadius Rounding of a bar's top corners. A bar stands square on the baseline.
 * @property minBarHeight Least height of a bar with a positive value, so small values stay visible.
 * @property placeholderStroke Width of the dashed outline of a placeholder column's bars.
 * @property haloShape Shape of the ground behind the selected column.
 * @property baselineStroke Thickness of the baseline.
 * @property referenceStroke Thickness of a reference line.
 * @property referenceDash Length of each dash of a reference line, and of the gap after it.
 * @property labelGap Space between the plot and the column labels.
 * @property labelBottomPadding Space under the last label line, inside the halo.
 * @property labelTextStyle Type of a column label.
 * @property labelSelectedTextStyle Type of the selected column's label.
 * @property sublabelTextStyle Type of the line under a label.
 * @property valueLabelTextStyle Type of a value label.
 * @property referenceLabelTextStyle Type of a reference line label.
 */
@Immutable
public data class ColumnChartSize(
    public val plotHeight: Dp,
    public val valueLabelSpace: Dp,
    public val columnWidth: Dp,
    public val columnGap: Dp,
    public val barWidth: Dp,
    public val companionWidth: Dp,
    public val barGap: Dp,
    public val stackGap: Dp,
    public val barCornerRadius: Dp,
    public val minBarHeight: Dp,
    public val placeholderStroke: Dp,
    public val haloShape: Shape,
    public val baselineStroke: Dp,
    public val referenceStroke: Dp,
    public val referenceDash: Dp,
    public val labelGap: Dp,
    public val labelBottomPadding: Dp,
    public val labelTextStyle: TextStyle,
    public val labelSelectedTextStyle: TextStyle,
    public val sublabelTextStyle: TextStyle,
    public val valueLabelTextStyle: TextStyle,
    public val referenceLabelTextStyle: TextStyle,
) {
    /** The size steps of a [ColumnChart]. */
    public companion object {
        /** A 110dp plot of narrow bars: a year of months or a week of days across a card. */
        public val Small: ColumnChartSize
            @Composable
            get() = step(
                plotHeight = 110.dp,
                columnWidth = 48.dp,
                barWidth = 16.dp,
                companionWidth = 6.dp,
                barCornerRadius = 6.dp,
            )

        /** A 150dp plot of wide columns. The default, for a period selector a screen opens on. */
        public val Medium: ColumnChartSize
            @Composable
            get() = step(
                plotHeight = 150.dp,
                columnWidth = 58.dp,
                barWidth = 19.dp,
                companionWidth = 7.dp,
                barCornerRadius = 6.dp,
            )

        /** A 200dp plot of wider bars, for a chart that is the subject of its screen. */
        public val Large: ColumnChartSize
            @Composable
            get() = step(
                plotHeight = 200.dp,
                columnWidth = 72.dp,
                barWidth = 26.dp,
                companionWidth = 9.dp,
                barCornerRadius = 8.dp,
            )

        /** One step, sharing every metric the steps do not differ in. */
        @Composable
        private fun step(
            plotHeight: Dp,
            columnWidth: Dp,
            barWidth: Dp,
            companionWidth: Dp,
            barCornerRadius: Dp,
        ): ColumnChartSize {
            val type = ArecaTheme.typography
            return ColumnChartSize(
                plotHeight = plotHeight,
                valueLabelSpace = ArecaTheme.spacing.xl,
                columnWidth = columnWidth,
                columnGap = ArecaTheme.spacing.xs,
                barWidth = barWidth,
                companionWidth = companionWidth,
                barGap = ArecaTheme.spacing.xs,
                stackGap = ArecaTheme.spacing.xs2,
                barCornerRadius = barCornerRadius,
                minBarHeight = 3.dp,
                placeholderStroke = 1.5.dp,
                haloShape = ArecaTheme.shapes.lg,
                baselineStroke = 1.dp,
                referenceStroke = 1.5.dp,
                referenceDash = ArecaTheme.spacing.xs,
                labelGap = ArecaTheme.spacing.sm,
                labelBottomPadding = ArecaTheme.spacing.sm,
                labelTextStyle = type.bodyExtraSmall,
                labelSelectedTextStyle = type.bodyExtraSmall.copy(fontWeight = FontWeight.SemiBold),
                sublabelTextStyle = type.bodyExtraSmall,
                valueLabelTextStyle = type.bodyExtraSmall,
                referenceLabelTextStyle = type.bodyExtraSmall,
            )
        }
    }
}

/**
 * Default [ColumnChartSize] in scope, or `null` to use [ColumnChartSize.Medium].
 *
 * Not seeded by [dev.voir.areca.theme.ArecaThemeProvider] — see [LocalBarChartStyle] for why.
 */
public val LocalColumnChartSize: ProvidableCompositionLocal<ColumnChartSize?> =
    compositionLocalOf { null }
