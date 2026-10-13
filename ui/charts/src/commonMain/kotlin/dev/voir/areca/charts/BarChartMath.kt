package dev.voir.areca.charts

import androidx.compose.ui.unit.Density

/**
 * Value range a [BarChart] plots, and the baseline its bars grow from.
 *
 * @property min Value at the bottom of the plot.
 * @property max Value at the top of the plot. Never below [min].
 * @property baseline Value bars grow from: zero, clamped into [min]..[max]. A value above it grows
 *   up, a value below it grows down.
 */
internal data class BarChartScale(
    val min: Float,
    val max: Float,
    val baseline: Float,
) {
    /**
     * Position of [value] on the scale.
     *
     * @param value Value to place. Values outside the scale are clamped to its ends.
     * @return `0f` at [min] and `1f` at [max]. A scale whose [max] equals [min] treats every value
     *   above [min] as one unit up the plot, so it still answers inside `0f..1f`.
     */
    fun fractionOf(value: Float): Float {
        val range = (max - min).takeIf { it > 0f } ?: 1f
        return ((value - min) / range).coerceIn(0f, 1f)
    }

    /**
     * Vertical pixel position of [value] in a plot drawn top-down.
     *
     * @param value Value to place, clamped to the scale.
     * @param plotHeight Height of the plot in pixels.
     * @return Distance from the top of the plot: `plotHeight` at [min], `0f` at [max].
     */
    fun yOf(value: Float, plotHeight: Float): Float = plotHeight * (1f - fractionOf(value))
}

/**
 * Vertical extent of one drawn bar, in pixels from the top of the plot.
 *
 * @property top Top edge of the bar.
 * @property height Height of the bar. `0f` for a value on the baseline.
 */
internal data class BarSpan(
    val top: Float,
    val height: Float,
)

/**
 * Horizontal placement of every bar in a [BarChart], measured from the plot's start edge.
 *
 * @property barWidth Width of each bar in pixels.
 * @property startOffset Space before the first bar, set by the chart's horizontal alignment.
 * @property barStarts Distance of each bar's start edge from the plot's start edge, in the order
 *   the bars appear across all groups.
 * @property contentWidth Width the bars and the gaps between them take, without [startOffset].
 */
internal data class BarChartLayout(
    val barWidth: Float,
    val startOffset: Float,
    val barStarts: List<Float>,
    val contentWidth: Float,
)

/**
 * The arithmetic behind [BarChart], kept apart from the drawing and label passes that use it.
 *
 * Separated so the scale, baseline, and bar placement rules can be stated and tested as plain
 * functions, and so the canvas and the label row lay bars out from one source. Everything here is
 * in pixels.
 */
internal object BarChartMath {
    /**
     * Resolves the value range of a chart.
     *
     * Non-finite values are ignored. With no [maxValue], the top of the scale is the largest finite
     * value; it falls back to `minValue + 1f` only when that would leave no range above [minValue]
     * (no finite values, or none above [minValue]).
     *
     * @param values Bar values to fit.
     * @param minValue Bottom of the scale.
     * @param maxValue Top of the scale, or `null` to fit [values].
     * @return Scale whose baseline is zero clamped into its range.
     */
    fun scale(values: List<Float>, minValue: Float, maxValue: Float?): BarChartScale {
        val dataMax = values.filter { it.isFinite() }.maxOrNull()
        val max = maxValue ?: dataMax?.takeIf { it > minValue } ?: (minValue + 1f)
        val min = minValue.coerceAtMost(max)
        return BarChartScale(min = min, max = max, baseline = 0f.coerceIn(min, max))
    }

    /**
     * Vertical extent of the bar for [value].
     *
     * The bar runs from the scale's baseline to [value] clamped into the scale. A value off the
     * baseline is drawn at least [minBarHeight] tall, so small values stay visible, but never past
     * the plot's edge.
     *
     * @param scale Scale the chart plots.
     * @param value Bar value.
     * @param plotHeight Height of the plot in pixels.
     * @param minBarHeight Smallest height of a bar off the baseline, in pixels.
     * @return Extent of the bar, or `null` when [value] is not finite and no bar is drawn.
     */
    fun barSpan(
        scale: BarChartScale,
        value: Float,
        plotHeight: Float,
        minBarHeight: Float,
    ): BarSpan? {
        if (!value.isFinite()) return null

        val clamped = value.coerceIn(scale.min, scale.max)
        val baselineY = scale.yOf(scale.baseline, plotHeight)
        val valueY = scale.yOf(clamped, plotHeight)

        return when {
            clamped > scale.baseline -> {
                val height = (baselineY - valueY).coerceAtLeast(minBarHeight).coerceAtMost(baselineY)
                BarSpan(top = baselineY - height, height = height)
            }

            clamped < scale.baseline -> {
                val height = (valueY - baselineY)
                    .coerceAtLeast(minBarHeight)
                    .coerceAtMost(plotHeight - baselineY)
                BarSpan(top = baselineY, height = height)
            }

            else -> BarSpan(top = baselineY, height = 0f)
        }
    }

    /**
     * Places every bar across the plot.
     *
     * Bars never extend past the plot: a [BarChartBarWidth.Fixed] or [BarChartBarWidth.Range] width
     * wider than the plot's share for each bar shrinks to that share.
     *
     * An infinite [plotWidth] means the chart was given unbounded width. Bars then take their
     * natural width — [unboundedBarWidth] for [BarChartBarWidth.Fill], the fixed width, or a range's
     * largest — start at the plot's start edge, and [BarChartLayout.contentWidth] is the width the
     * plot should take.
     *
     * @param groupSizes Number of bars in each group, in order.
     * @param plotWidth Width of the plot in pixels, or [Float.POSITIVE_INFINITY] when unbounded.
     * @param barGap Space between bars of one group, in pixels.
     * @param groupGap Space between groups, in pixels.
     * @param barWidth How wide each bar is.
     * @param alignment Where the bars sit when they do not fill the plot.
     * @param unboundedBarWidth Width of a [BarChartBarWidth.Fill] bar when [plotWidth] is infinite,
     *   in pixels.
     * @param density Density converting [barWidth]'s dp into pixels.
     * @return Bar width, leading space, each bar's start-edge position, and the content's width.
     */
    fun layout(
        groupSizes: List<Int>,
        plotWidth: Float,
        barGap: Float,
        groupGap: Float,
        barWidth: BarChartBarWidth,
        alignment: BarChartHorizontalAlignment,
        unboundedBarWidth: Float,
        density: Density,
    ): BarChartLayout {
        val barCount = groupSizes.sum()
        val totalGaps = groupGap * (groupSizes.size - 1).coerceAtLeast(0) +
            barGap * groupSizes.sumOf { (it - 1).coerceAtLeast(0) }
        val bounded = plotWidth.isFinite()
        val fillWidth = when {
            barCount == 0 -> 0f
            bounded -> (plotWidth - totalGaps).coerceAtLeast(0f) / barCount
            else -> Float.POSITIVE_INFINITY
        }

        val resolvedWidth = if (barCount == 0) {
            0f
        } else {
            with(density) {
                when (barWidth) {
                    BarChartBarWidth.Fill -> if (bounded) fillWidth else unboundedBarWidth
                    is BarChartBarWidth.Fixed -> barWidth.width.toPx()
                    is BarChartBarWidth.Range ->
                        fillWidth.coerceIn(barWidth.min.toPx(), barWidth.max.toPx())
                }
            }.coerceAtMost(fillWidth)
        }

        val contentWidth = barCount * resolvedWidth + totalGaps
        val startOffset = if (!bounded) 0f else when (alignment) {
            BarChartHorizontalAlignment.Start -> 0f
            BarChartHorizontalAlignment.Center -> ((plotWidth - contentWidth) / 2f).coerceAtLeast(0f)
            BarChartHorizontalAlignment.End -> (plotWidth - contentWidth).coerceAtLeast(0f)
        }

        val barStarts = ArrayList<Float>(barCount)
        var x = startOffset
        groupSizes.forEachIndexed { groupIndex, size ->
            if (groupIndex > 0) x += groupGap
            repeat(size) { barIndex ->
                if (barIndex > 0) x += barGap
                barStarts += x
                x += resolvedWidth
            }
        }

        return BarChartLayout(
            barWidth = resolvedWidth,
            startOffset = startOffset,
            barStarts = barStarts,
            contentWidth = contentWidth,
        )
    }

    /**
     * Position of a straight line's centre so its whole stroke stays inside the plot.
     *
     * A line is drawn centred on its position, so one on the plot's edge would put half its stroke
     * outside the plot — over the labels, or past a rounded plot background.
     *
     * @param position Where the line belongs, in pixels along the axis it crosses.
     * @param stroke Width of the line's stroke, in pixels.
     * @param extent Length of the plot along that axis, in pixels.
     * @return [position] moved inward by up to half the stroke from either edge. A plot thinner than
     *   the stroke centres the line.
     */
    fun insetLine(position: Float, stroke: Float, extent: Float): Float {
        val half = stroke / 2f
        if (extent <= stroke) return extent / 2f
        return position.coerceIn(half, extent - half)
    }

    /**
     * Whether any bar carries a label worth a row under the plot.
     *
     * @param groups Groups to inspect.
     * @return `true` when at least one bar has a non-blank label.
     */
    fun hasBarLabels(groups: List<BarChartGroup>): Boolean =
        groups.any { group -> group.bars.any { !it.label.isNullOrBlank() } }

    /**
     * Whether any group carries a label worth a row under the bar labels.
     *
     * @param groups Groups to inspect.
     * @return `true` when at least one group has a non-blank label.
     */
    fun hasGroupLabels(groups: List<BarChartGroup>): Boolean =
        groups.any { !it.label.isNullOrBlank() }

    /**
     * Builds the text assistive technology reads for a chart's data.
     *
     * Each group reads as its label, a colon, and its bars; each bar as its label followed by its
     * formatted value. Groups are separated by semicolons and bars by commas. Bars with a
     * non-finite value are left out, and blank labels are skipped.
     *
     * @param groups Groups to describe.
     * @param formatValue Turns a bar value into text.
     * @return Description of the data, empty when there is nothing to read.
     */
    fun description(groups: List<BarChartGroup>, formatValue: (Float) -> String): String =
        groups.mapNotNull { group ->
            val bars = group.bars
                .filter { it.value.isFinite() }
                .joinToString(", ") { bar ->
                    listOfNotNull(bar.label?.takeIf { it.isNotBlank() }, formatValue(bar.value))
                        .joinToString(" ")
                }
            listOfNotNull(group.label?.takeIf { it.isNotBlank() }, bars.takeIf { it.isNotEmpty() })
                .joinToString(": ")
                .takeIf { it.isNotEmpty() }
        }.joinToString("; ")
}
