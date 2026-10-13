package dev.voir.areca.charts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.theme.ArecaTheme

/**
 * Dimensions and type of one step of [BarChart].
 *
 * Take a step — [Small], [Medium], [Large] — and change one value with `copy`.
 *
 * @property height Height of the plot area. The chart grows by its labels below it.
 * @property plotShape Shape the plot's [BarChartStyle.plotBg] is drawn in.
 * @property axisStroke Width of the x and y axis lines.
 * @property guideStroke Width of the dashed y guides.
 * @property guideDash Length of each dash of a y guide, and of the gap after it.
 * @property barCornerRadius Corner radius of the bars and their tracks under
 *   [BarChartShape.Rounded].
 * @property barGap Space between the bars of one group.
 * @property groupGap Space between groups.
 * @property minBarHeight Smallest height a bar off the baseline is drawn at, so small values stay
 *   visible.
 * @property unboundedBarWidth Width of a [BarChartBarWidth.Fill] bar when the chart is given
 *   unbounded width, such as inside a horizontally scrolling row, where there is no width to fill.
 * @property yLabelWidth Width of the column the y-tick labels are set in.
 * @property yLabelGap Space between the y-tick labels and the plot.
 * @property xLabelGap Space between the plot and the per-bar labels.
 * @property groupLabelGap Space between the per-bar labels and a group label.
 * @property groupLabelBottomPadding Space below a group label.
 * @property xLabelTextStyle Type of the per-bar labels.
 * @property groupLabelTextStyle Type of the per-group labels.
 * @property yLabelTextStyle Type of the y-tick labels.
 * @property xLabelMaxLines Most lines a per-bar label wraps to before it is cut short with an
 *   ellipsis. At least `1`.
 * @property groupLabelMaxLines Most lines a group label wraps to before it is cut short with an
 *   ellipsis. At least `1`.
 * @throws IllegalArgumentException When [xLabelMaxLines] or [groupLabelMaxLines] is below `1`.
 */
@Immutable
public data class BarChartSize(
    public val height: Dp,
    public val plotShape: Shape,
    public val axisStroke: Dp,
    public val guideStroke: Dp,
    public val guideDash: Dp,
    public val barCornerRadius: Dp,
    public val barGap: Dp,
    public val groupGap: Dp,
    public val minBarHeight: Dp,
    public val unboundedBarWidth: Dp,
    public val yLabelWidth: Dp,
    public val yLabelGap: Dp,
    public val xLabelGap: Dp,
    public val groupLabelGap: Dp,
    public val groupLabelBottomPadding: Dp,
    public val xLabelTextStyle: TextStyle,
    public val groupLabelTextStyle: TextStyle,
    public val yLabelTextStyle: TextStyle,
    public val xLabelMaxLines: Int = 2,
    public val groupLabelMaxLines: Int = 1,
) {
    init {
        require(xLabelMaxLines >= 1) { "Bar labels need at least one line, was $xLabelMaxLines." }
        require(groupLabelMaxLines >= 1) {
            "Group labels need at least one line, was $groupLabelMaxLines."
        }
    }

    /** The size steps of a [BarChart]. */
    public companion object {
        /** A 160dp plot with tight gaps, for a chart inside a card or beside other content. */
        public val Small: BarChartSize
            @Composable
            get() = BarChartSize(
                height = 160.dp,
                plotShape = ArecaTheme.shapes.md,
                axisStroke = 1.dp,
                guideStroke = 1.dp,
                guideDash = ArecaTheme.spacing.xs,
                barCornerRadius = 8.dp,
                barGap = ArecaTheme.spacing.sm,
                groupGap = ArecaTheme.spacing.lg,
                minBarHeight = 6.dp,
                unboundedBarWidth = 16.dp,
                yLabelWidth = 36.dp,
                yLabelGap = ArecaTheme.spacing.md,
                xLabelGap = ArecaTheme.spacing.md,
                groupLabelGap = ArecaTheme.spacing.xs,
                groupLabelBottomPadding = ArecaTheme.spacing.xs,
                xLabelTextStyle = ArecaTheme.typography.labelSmall,
                groupLabelTextStyle = ArecaTheme.typography.labelMedium,
                yLabelTextStyle = ArecaTheme.typography.labelSmall,
            )

        /** A 220dp plot. The default, for a chart that is a section of its screen. */
        public val Medium: BarChartSize
            @Composable
            get() = BarChartSize(
                height = 220.dp,
                plotShape = ArecaTheme.shapes.md,
                axisStroke = 1.dp,
                guideStroke = 1.dp,
                guideDash = ArecaTheme.spacing.xs,
                barCornerRadius = 10.dp,
                barGap = ArecaTheme.spacing.md,
                groupGap = ArecaTheme.spacing.xl,
                minBarHeight = 8.dp,
                unboundedBarWidth = 24.dp,
                yLabelWidth = 44.dp,
                yLabelGap = ArecaTheme.spacing.lg,
                xLabelGap = ArecaTheme.spacing.md,
                groupLabelGap = ArecaTheme.spacing.sm,
                groupLabelBottomPadding = ArecaTheme.spacing.sm,
                xLabelTextStyle = ArecaTheme.typography.caption,
                groupLabelTextStyle = ArecaTheme.typography.labelLarge,
                yLabelTextStyle = ArecaTheme.typography.caption,
            )

        /** A 280dp plot with roomy gaps, for a chart that is the subject of its screen. */
        public val Large: BarChartSize
            @Composable
            get() = BarChartSize(
                height = 280.dp,
                plotShape = ArecaTheme.shapes.lg,
                axisStroke = 2.dp,
                guideStroke = 1.dp,
                guideDash = ArecaTheme.spacing.xs,
                barCornerRadius = 12.dp,
                barGap = ArecaTheme.spacing.lg,
                groupGap = ArecaTheme.spacing.xl2,
                minBarHeight = 10.dp,
                unboundedBarWidth = 32.dp,
                yLabelWidth = 52.dp,
                yLabelGap = ArecaTheme.spacing.lg,
                xLabelGap = ArecaTheme.spacing.lg,
                groupLabelGap = ArecaTheme.spacing.md,
                groupLabelBottomPadding = ArecaTheme.spacing.md,
                xLabelTextStyle = ArecaTheme.typography.caption,
                groupLabelTextStyle = ArecaTheme.typography.labelLarge,
                yLabelTextStyle = ArecaTheme.typography.caption,
            )
    }
}

/**
 * Default [BarChartSize] in scope, or `null` to use [BarChartSize.Medium].
 *
 * Not seeded by [dev.voir.areca.theme.ArecaThemeProvider] — see [LocalBarChartStyle] for why.
 */
public val LocalBarChartSize: ProvidableCompositionLocal<BarChartSize?> =
    compositionLocalOf { null }
