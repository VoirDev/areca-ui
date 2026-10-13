package dev.voir.areca.charts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import dev.voir.areca.foundation.text.Text

/** Corner shape of a [BarChart]'s bars and their tracks. */
public enum class BarChartShape {
    /** Corners rounded by [BarChartSize.barCornerRadius]. */
    Rounded,

    /** Square corners. */
    Square,
}

/**
 * Where a [BarChart]'s bars sit when together they are narrower than the plot.
 *
 * Start and end follow the layout direction.
 */
public enum class BarChartHorizontalAlignment {
    /** Against the plot's start edge, beside the y axis. */
    Start,

    /** Centred in the plot. */
    Center,

    /** Against the plot's end edge. */
    End,
}

/** How one bar of a [BarChart] is filled. */
@Immutable
public sealed interface BarChartFill {
    /**
     * A single colour.
     *
     * @property color Fill of the bar.
     */
    public data class Solid(public val color: Color) : BarChartFill

    /**
     * A brush, such as a gradient.
     *
     * The brush is sized to the whole plot, not to the bar, so a gradient across the plot shades
     * every bar from one ramp. Horizontal gradients mirror with the chart in a right-to-left
     * layout, so their first colour stays at the start edge.
     *
     * @property brush Paint of the bar.
     */
    public data class Gradient(public val brush: Brush) : BarChartFill
}

/**
 * How wide each bar of a [BarChart] is.
 *
 * Whatever the mode, bars never extend past the plot: a width wider than the plot's share for each
 * bar (after the gaps) shrinks to that share. Given unbounded width, the chart takes the width its
 * bars need instead — see [BarChartSize.unboundedBarWidth].
 */
public sealed interface BarChartBarWidth {
    /**
     * Bars share the plot's width equally, after the gaps.
     *
     * With unbounded width each bar is [BarChartSize.unboundedBarWidth] wide.
     */
    public data object Fill : BarChartBarWidth

    /**
     * Every bar is [width] wide, or narrower when that many bars do not fit.
     *
     * @property width Width of each bar. Not negative.
     * @throws IllegalArgumentException When [width] is negative or unspecified.
     */
    public data class Fixed(public val width: Dp) : BarChartBarWidth {
        init {
            require(width >= 0.dp) { "Bar width must not be negative, was $width." }
        }
    }

    /**
     * Bars share the plot's width as [Fill] does, kept within [min]..[max].
     *
     * [min] applies only while that many bars of that width fit; otherwise bars shrink so every bar
     * stays inside the plot. With unbounded width each bar is [max] wide.
     *
     * @property min Narrowest a bar is drawn when it fits. Not negative.
     * @property max Widest a bar is drawn. Not below [min].
     * @throws IllegalArgumentException When [min] is negative or [max] is below [min].
     */
    public data class Range(
        public val min: Dp,
        public val max: Dp,
    ) : BarChartBarWidth {
        init {
            require(min >= 0.dp) { "Bar width range must not start below zero, was $min." }
            require(max >= min) { "Bar width range must not end below its start, was $min..$max." }
        }
    }
}

/**
 * One bar of a [BarChart].
 *
 * @property value Value the bar shows, in the units of the chart's scale. The bar grows from zero
 *   (clamped into the scale) up to the value, or down to it when the scale reaches below zero, and is
 *   clamped to the scale's ends. A non-finite value (`NaN`, infinity) is treated as "no value": only
 *   the bar's track is drawn, and the value is left out of the auto-scale and the description.
 * @property label Text shown under the bar and read with its value, or `null` for none.
 * @property fill Fill of the bar, or `null` for the chart style's [BarChartStyle.bar].
 * @property trackColor Color of the track behind the bar, or `null` for the chart style's
 *   [BarChartStyle.track].
 */
@Immutable
public data class BarChartBar(
    public val value: Float,
    public val label: String? = null,
    public val fill: BarChartFill? = null,
    public val trackColor: Color? = null,
) {
    /** The default value formatter of a [BarChart]'s generated description. */
    public companion object {
        /**
         * Default text for a bar value in a [BarChart]'s generated description.
         *
         * Whole numbers read without a fractional part (`42`, not `42.0`); other values use Kotlin's
         * `Float.toString`. Neither is localized — no grouping separators, and `.` as the decimal
         * mark — so pass a locale-aware formatter to [BarChart] when values are not whole numbers or
         * are large.
         *
         * @param value Value to format.
         * @return Text for [value].
         */
        public fun formatValue(value: Float): String {
            val whole = value.toLong()
            return if (whole.toFloat() == value) whole.toString() else value.toString()
        }
    }
}

/**
 * A group of bars drawn side by side in a [BarChart], with an optional label under them.
 *
 * @property label Text shown under the group and read before its bars, or `null` for none.
 * @property bars Bars of the group, drawn start to end. A group without bars is not drawn.
 */
@Immutable
public data class BarChartGroup(
    public val label: String? = null,
    public val bars: List<BarChartBar>,
) {
    /** Builders of [BarChartGroup] lists for charts that do not group their bars. */
    public companion object {
        /**
         * Puts each bar in a group of its own, for a chart of ungrouped bars.
         *
         * @param bars Bars to draw, start to end.
         * @return One unlabelled group per bar.
         */
        public fun singleBars(bars: List<BarChartBar>): List<BarChartGroup> =
            bars.map { bar -> BarChartGroup(label = null, bars = listOf(bar)) }
    }
}

/**
 * A tick label drawn beside a [BarChart]'s plot.
 *
 * @property value Value the label marks, in the units of the chart's scale. It sits level with that
 *   value, and is hidden when the value is outside the scale.
 * @property label Text of the tick.
 */
@Immutable
public data class BarChartYAxisLabel(
    public val value: Float,
    public val label: String,
)

/**
 * Layout, scale, and visibility options of a [BarChart].
 *
 * @property showXAxis Whether the horizontal axis line is drawn. It sits on the baseline bars grow
 *   from: the bottom of the plot unless the scale reaches below zero.
 * @property showYAxis Whether the vertical axis line is drawn on the plot's start edge. Y-tick labels
 *   are controlled separately, by the chart's `yAxisLabels`.
 * @property showYGuides Whether a dashed guide is drawn across the plot at each visible y-tick label.
 * @property showBarTrack Whether a full-height track is drawn behind each bar.
 * @property shape Corner shape of the bars and their tracks.
 * @property contentPadding Padding inside the chart's container.
 * @property minValue Value at the bottom of the plot. Bars grow from zero clamped into the scale, so
 *   with the default `0f` a negative value clamps to zero and draws no bar; set a negative
 *   [minValue] to show negative values as bars growing down from zero.
 * @property maxValue Value at the top of the plot, or `null` to use the largest finite bar value.
 *   The auto-scale falls back to `minValue + 1f` only when no finite value lies above [minValue].
 *   Values above the top are clamped to full height.
 * @property barWidth How wide each bar is.
 * @property horizontalAlignment Where the bars sit when they do not fill the plot. Start and end
 *   follow the layout direction.
 */
@Stable
public data class BarChartOptions(
    public val showXAxis: Boolean = true,
    public val showYAxis: Boolean = true,
    public val showYGuides: Boolean = true,
    public val showBarTrack: Boolean = true,
    public val shape: BarChartShape = BarChartShape.Rounded,
    public val contentPadding: PaddingValues = PaddingValues(0.dp),
    public val minValue: Float = 0f,
    public val maxValue: Float? = null,
    public val barWidth: BarChartBarWidth = BarChartBarWidth.Fill,
    public val horizontalAlignment: BarChartHorizontalAlignment =
        BarChartHorizontalAlignment.Center,
)

/**
 * A vertical bar chart of grouped bars, drawn with the Areca theme.
 *
 * Bars grow from zero, clamped into the scale set by [options]: with the default
 * [BarChartOptions.minValue] of `0f`, negative values clamp to zero and draw no bar. Non-finite
 * values draw no bar and are left out of the scale and the description. Groups without bars are
 * dropped; with no bars at all the chart still draws its empty plot at its full size.
 *
 * The chart fills the width it is given. Given unbounded width — inside a horizontally scrolling
 * row — it takes the width its bars need instead, at their natural width (see [BarChartBarWidth]).
 *
 * The chart mirrors in a right-to-left layout: the first bar sits at the start edge, beside the y
 * axis, and horizontal gradients mirror with it.
 *
 * The bar-label row is left out when no bar has a label, and the group-label row when no group has
 * one, so an unlabelled chart ends at its plot. Labels wrap up to [BarChartSize.xLabelMaxLines] and
 * [BarChartSize.groupLabelMaxLines] lines and are then cut short with an ellipsis.
 *
 * Assistive technology reads the chart as one node carrying [contentDescription], or, when that is
 * `null`, a description generated from the group labels, bar labels, and values formatted by
 * [valueFormatter]. The visible tick and bar labels are not read separately.
 *
 * Requires `ArecaThemeProvider` above it.
 *
 * @param groups Groups of bars, drawn start to end.
 * @param modifier Modifier applied to the chart's container.
 * @param style Colors of the chart. Defaults to [LocalBarChartStyle], or [BarChartStyle.Default].
 * @param size Dimensions and type of the chart, including its plot height. Defaults to
 *   [LocalBarChartSize], or [BarChartSize.Medium].
 * @param options Layout, scale, and visibility options.
 * @param yAxisLabels Tick labels drawn beside the plot at their values. Labels outside the scale
 *   are hidden.
 * @param contentDescription What assistive technology reads for the chart, or `null` to generate a
 *   description of the data. Pass one when the generated wording does not suit the locale.
 * @param valueFormatter Turns a bar value into text for the generated description. The default
 *   [BarChartBar.formatValue] is not localized.
 */
@Composable
public fun BarChart(
    groups: List<BarChartGroup>,
    modifier: Modifier = Modifier,
    style: BarChartStyle = LocalBarChartStyle.current ?: BarChartStyle.Default,
    size: BarChartSize = LocalBarChartSize.current ?: BarChartSize.Medium,
    options: BarChartOptions = BarChartOptions(),
    yAxisLabels: List<BarChartYAxisLabel> = emptyList(),
    contentDescription: String? = null,
    valueFormatter: (Float) -> String = BarChartBar.Companion::formatValue,
) {
    val normalizedGroups = remember(groups) { groups.filter { it.bars.isNotEmpty() } }
    val groupSizes = remember(normalizedGroups) { normalizedGroups.map { it.bars.size } }
    val allBars = remember(normalizedGroups) { normalizedGroups.flatMap { it.bars } }
    val scale = remember(allBars, options.minValue, options.maxValue) {
        BarChartMath.scale(
            values = allBars.map { it.value },
            minValue = options.minValue,
            maxValue = options.maxValue,
        )
    }
    val visibleYLabels = remember(yAxisLabels, scale) {
        yAxisLabels.filter { it.value in scale.min..scale.max }
    }
    val showBarLabels = remember(normalizedGroups) { BarChartMath.hasBarLabels(normalizedGroups) }
    val showGroupLabels = remember(normalizedGroups) {
        BarChartMath.hasGroupLabels(normalizedGroups)
    }
    val description = remember(contentDescription, normalizedGroups, valueFormatter) {
        contentDescription ?: BarChartMath.description(normalizedGroups, valueFormatter)
    }

    BoxWithConstraints(
        modifier = modifier
            .clearAndSetSemantics {
                if (description.isNotEmpty()) this.contentDescription = description
            }
            .background(style.bg)
            .padding(options.contentPadding),
    ) {
        val density = LocalDensity.current
        val yLabelReservedWidth =
            if (visibleYLabels.isNotEmpty()) size.yLabelWidth + size.yLabelGap else 0.dp
        val availablePlotWidth = if (constraints.hasBoundedWidth) {
            val reserved = with(density) { yLabelReservedWidth.roundToPx() }
            (constraints.maxWidth - reserved).coerceAtLeast(0).toFloat()
        } else {
            Float.POSITIVE_INFINITY
        }
        val layout = remember(groupSizes, availablePlotWidth, size, options, density) {
            with(density) {
                BarChartMath.layout(
                    groupSizes = groupSizes,
                    plotWidth = availablePlotWidth,
                    barGap = size.barGap.toPx(),
                    groupGap = size.groupGap.toPx(),
                    barWidth = options.barWidth,
                    alignment = options.horizontalAlignment,
                    unboundedBarWidth = size.unboundedBarWidth.toPx(),
                    density = density,
                )
            }
        }
        val plotWidth = with(density) {
            (if (availablePlotWidth.isFinite()) availablePlotWidth else layout.contentWidth).toDp()
        }

        Column {
            Row {
                if (visibleYLabels.isNotEmpty()) {
                    YAxisLabels(
                        labels = visibleYLabels,
                        scale = scale,
                        style = style,
                        size = size,
                        modifier = Modifier
                            .width(size.yLabelWidth)
                            .height(size.height),
                    )
                    Spacer(modifier = Modifier.width(size.yLabelGap))
                }

                Canvas(
                    modifier = Modifier
                        .width(plotWidth)
                        .height(size.height)
                        .background(color = style.plotBg, shape = size.plotShape),
                ) {
                    // Drawn left to right; right to left, the whole plot is mirrored about its centre,
                    // which keeps the first bar and the y axis at the start edge and mirrors a
                    // horizontal gradient with them.
                    scale(
                        scaleX = if (layoutDirection == LayoutDirection.Rtl) -1f else 1f,
                        scaleY = 1f,
                        pivot = center,
                    ) {
                        drawPlot(
                            bars = allBars,
                            layout = layout,
                            scale = scale,
                            guides = if (options.showYGuides) visibleYLabels else emptyList(),
                            options = options,
                            style = style,
                            size = size,
                        )
                    }
                }
            }

            if (showBarLabels || showGroupLabels) {
                Spacer(modifier = Modifier.height(size.xLabelGap))

                BarChartLabels(
                    groups = normalizedGroups,
                    showBarLabels = showBarLabels,
                    showGroupLabels = showGroupLabels,
                    yLabelReservedWidth = yLabelReservedWidth,
                    startOffset = with(density) { layout.startOffset.toDp() },
                    barWidth = with(density) { layout.barWidth.toDp() },
                    style = style,
                    size = size,
                )
            }
        }
    }
}

/**
 * Draws the plot's guides, tracks, bars, and axes, left to right.
 *
 * Lines are inset so their whole stroke stays inside the plot.
 *
 * @param bars Every bar, in layout order.
 * @param layout Horizontal placement of the bars.
 * @param scale Value range the plot shows.
 * @param guides Tick labels to draw a dashed guide at. Empty for none.
 * @param options Which parts are drawn, and the bars' corner shape.
 * @param style Colors to draw with.
 * @param size Strokes, dash, corner radius, and minimum bar height.
 */
private fun DrawScope.drawPlot(
    bars: List<BarChartBar>,
    layout: BarChartLayout,
    scale: BarChartScale,
    guides: List<BarChartYAxisLabel>,
    options: BarChartOptions,
    style: BarChartStyle,
    size: BarChartSize,
) {
    val plotHeight = this.size.height
    val plotWidth = this.size.width
    val axisStroke = size.axisStroke.toPx()
    val guideStroke = size.guideStroke.toPx()
    val dash = size.guideDash.toPx()
    val radius = if (options.shape == BarChartShape.Rounded) size.barCornerRadius.toPx() else 0f
    val cornerRadius = CornerRadius(radius, radius)
    val minBarHeight = size.minBarHeight.toPx()
    val barWidth = layout.barWidth

    guides.forEach { guide ->
        val y = BarChartMath.insetLine(scale.yOf(guide.value, plotHeight), guideStroke, plotHeight)
        drawLine(
            color = style.guide,
            start = Offset(0f, y),
            end = Offset(plotWidth, y),
            strokeWidth = guideStroke,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(dash, dash)),
        )
    }

    bars.forEachIndexed { index, bar ->
        val x = layout.barStarts[index]

        if (options.showBarTrack) {
            drawRoundRect(
                color = bar.trackColor ?: style.track,
                topLeft = Offset(x, 0f),
                size = Size(barWidth, plotHeight),
                cornerRadius = cornerRadius,
            )
        }

        val span = BarChartMath.barSpan(
            scale = scale,
            value = bar.value,
            plotHeight = plotHeight,
            minBarHeight = minBarHeight,
        ) ?: return@forEachIndexed
        val topLeft = Offset(x, span.top)
        val barSize = Size(barWidth, span.height)

        when (val fill = bar.fill) {
            is BarChartFill.Gradient ->
                drawRoundRect(brush = fill.brush, topLeft = topLeft, size = barSize, cornerRadius = cornerRadius)

            is BarChartFill.Solid ->
                drawRoundRect(color = fill.color, topLeft = topLeft, size = barSize, cornerRadius = cornerRadius)

            null ->
                drawRoundRect(color = style.bar, topLeft = topLeft, size = barSize, cornerRadius = cornerRadius)
        }
    }

    if (options.showYAxis) {
        val x = BarChartMath.insetLine(0f, axisStroke, plotWidth)
        drawLine(
            color = style.axis,
            start = Offset(x, 0f),
            end = Offset(x, plotHeight),
            strokeWidth = axisStroke,
        )
    }

    if (options.showXAxis) {
        val y = BarChartMath.insetLine(scale.yOf(scale.baseline, plotHeight), axisStroke, plotHeight)
        drawLine(
            color = style.axis,
            start = Offset(0f, y),
            end = Offset(plotWidth, y),
            strokeWidth = axisStroke,
        )
    }
}

/**
 * The column of y-tick labels beside a [BarChart]'s plot.
 *
 * Each label is centred on its value's height, kept inside the column, and set against the column's
 * end edge, nearest the plot.
 *
 * @param labels Visible tick labels.
 * @param scale Value range the plot shows.
 * @param style Source of the label colour.
 * @param size Source of the label type.
 * @param modifier Modifier applied to the column, which sets its width and height.
 */
@Composable
private fun YAxisLabels(
    labels: List<BarChartYAxisLabel>,
    scale: BarChartScale,
    style: BarChartStyle,
    size: BarChartSize,
    modifier: Modifier = Modifier,
) {
    Layout(
        modifier = modifier,
        content = {
            labels.forEach { item ->
                Text(
                    text = item.label,
                    style = size.yLabelTextStyle,
                    color = style.yLabel,
                    textAlign = TextAlign.End,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
            }
        },
    ) { measurables, constraints ->
        val labelConstraints = constraints.copy(minWidth = 0, minHeight = 0)
        val placeables = measurables.map { it.measure(labelConstraints) }

        layout(constraints.maxWidth, constraints.maxHeight) {
            placeables.forEachIndexed { index, placeable ->
                val centerY = scale.yOf(labels[index].value, constraints.maxHeight.toFloat()).toInt()
                val y = (centerY - placeable.height / 2)
                    .coerceIn(0, (constraints.maxHeight - placeable.height).coerceAtLeast(0))

                placeable.placeRelative(constraints.maxWidth - placeable.width, y)
            }
        }
    }
}

/**
 * The rows of bar and group labels under a [BarChart]'s plot.
 *
 * Lays labels out with the bar width and leading space the plot uses, so each label sits under its
 * bar. The rows follow the layout direction, as the plot's bars do.
 *
 * @param groups Groups whose labels are drawn, in order.
 * @param showBarLabels Whether the row of per-bar labels is drawn.
 * @param showGroupLabels Whether the row of per-group labels is drawn.
 * @param yLabelReservedWidth Space before the plot taken by the y-tick labels and their gap.
 * @param startOffset Space before the first bar inside the plot.
 * @param barWidth Width of each bar.
 * @param style Label colours.
 * @param size Gaps, type, and line caps of the labels.
 */
@Composable
private fun BarChartLabels(
    groups: List<BarChartGroup>,
    showBarLabels: Boolean,
    showGroupLabels: Boolean,
    yLabelReservedWidth: Dp,
    startOffset: Dp,
    barWidth: Dp,
    style: BarChartStyle,
    size: BarChartSize,
) {
    Row(modifier = Modifier.padding(start = yLabelReservedWidth + startOffset)) {
        groups.forEachIndexed { groupIndex, group ->
            val groupWidth = group.bars.size * barWidth +
                (group.bars.size - 1).coerceAtLeast(0) * size.barGap

            Column(
                modifier = Modifier.width(groupWidth),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (showBarLabels) {
                    Row(horizontalArrangement = Arrangement.spacedBy(size.barGap)) {
                        group.bars.forEach { bar ->
                            Box(
                                modifier = Modifier.width(barWidth),
                                contentAlignment = Alignment.TopCenter,
                            ) {
                                val label = bar.label
                                if (!label.isNullOrBlank()) {
                                    Text(
                                        text = label,
                                        style = size.xLabelTextStyle,
                                        color = style.xLabel,
                                        textAlign = TextAlign.Center,
                                        overflow = TextOverflow.Ellipsis,
                                        maxLines = size.xLabelMaxLines,
                                        modifier = Modifier.fillMaxWidth(),
                                    )
                                }
                            }
                        }
                    }
                }

                val groupLabel = group.label
                if (showGroupLabels && !groupLabel.isNullOrBlank()) {
                    if (showBarLabels) Spacer(modifier = Modifier.height(size.groupLabelGap))
                    Text(
                        text = groupLabel,
                        style = size.groupLabelTextStyle,
                        color = style.groupLabel,
                        textAlign = TextAlign.Center,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = size.groupLabelMaxLines,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = size.groupLabelBottomPadding),
                    )
                }
            }

            if (groupIndex != groups.lastIndex) {
                Spacer(modifier = Modifier.width(size.groupGap))
            }
        }
    }
}
