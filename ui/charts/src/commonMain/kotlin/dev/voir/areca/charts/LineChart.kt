package dev.voir.areca.charts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.LayoutDirection
import dev.voir.areca.foundation.text.Text
import kotlin.math.roundToInt

/**
 * Lines across a run of steps — a running total through the month against last month's — with
 * grid lines, a vertical rule and a bubble over one point.
 *
 * Every series is plotted against one scale from zero to [maxValue], or to the largest point when
 * it is `null`. A series' `null` points are steps it has no value for yet; its line stops there and
 * its end marker sits on its last value. The first series drawn is the first in [series], so put
 * the comparison first and the series the chart is about last, on top.
 *
 * The chart reads; it does not pick. Assistive technology reads it as one node: [contentDescription],
 * or the series labels and the bubble's text.
 *
 * The chart mirrors in a right-to-left layout: the first step sits at the start edge and the y
 * labels at the end edge.
 *
 * Requires `ArecaThemeProvider` above it.
 *
 * @param series Lines, drawn in order.
 * @param modifier Modifier applied to the chart, which fills the width it is given.
 * @param xLabels Labels under the plot at chosen steps.
 * @param yLabels Grid lines with their labels at the plot's end edge.
 * @param rule Step to draw a dashed vertical rule at — today — or `null` for none.
 * @param tooltip The bubble over one point, or `null` for none.
 * @param maxValue Value at the top of the scale, or `null` for the largest point.
 * @param style Colours. Defaults to [LocalLineChartStyle], or [LineChartStyle.Default].
 * @param size Dimensions and type. Defaults to [LocalLineChartSize], or [LineChartSize.Default].
 * @param contentDescription What assistive technology reads, or `null` for the labels and bubble.
 */
@Composable
public fun LineChart(
    series: List<LineChartSeries>,
    modifier: Modifier = Modifier,
    xLabels: List<LineChartXLabel> = emptyList(),
    yLabels: List<LineChartYLabel> = emptyList(),
    rule: Int? = null,
    tooltip: LineChartTooltip? = null,
    maxValue: Float? = null,
    style: LineChartStyle = LocalLineChartStyle.current ?: LineChartStyle.Default,
    size: LineChartSize = LocalLineChartSize.current ?: LineChartSize.Default,
    contentDescription: String? = null,
) {
    val steps = remember(series) { LineChartMath.steps(series) }
    val top = remember(series, maxValue) { LineChartMath.top(series, maxValue) }
    val visibleY = remember(yLabels, top) { yLabels.filter { it.value.isFinite() && it.value in 0f..top } }
    val visibleX = remember(xLabels, steps) { xLabels.filter { it.index in 0 until steps } }
    val tooltipPoint = tooltip?.let { tip ->
        series.getOrNull(tip.series)?.points?.getOrNull(tip.index)?.let(LineChartMath::finite)
    }
    val description = contentDescription ?: listOfNotNull(
        series.joinToString(", ") { it.label }.takeIf { it.isNotBlank() },
        tooltip?.text?.takeIf { tooltipPoint != null },
    ).joinToString(". ")

    Layout(
        modifier = modifier.clearAndSetSemantics { if (description.isNotEmpty()) this.contentDescription = description },
        content = {
            Canvas(modifier = Modifier) {
                scale(scaleX = if (layoutDirection == LayoutDirection.Rtl) -1f else 1f, scaleY = 1f, pivot = center) {
                    drawLinePlot(series, steps, top, visibleY, rule, style, size)
                }
            }
            visibleY.forEach { Text(text = it.label, style = size.labelTextStyle, color = style.label, maxLines = 1) }
            visibleX.forEach { Text(text = it.label, style = size.labelTextStyle, color = style.label, maxLines = 1) }
            if (tooltip != null && tooltipPoint != null) {
                Text(
                    text = tooltip.text,
                    style = size.tooltipTextStyle,
                    color = style.tooltipContent,
                    maxLines = 1,
                    modifier = Modifier
                        .background(style.tooltipBg, size.tooltipShape)
                        .padding(size.tooltipPadding),
                )
            }
        },
    ) { measurables, constraints ->
        val width = constraints.maxWidth
        val loose = Constraints(maxWidth = width)
        val yPlaceables = measurables.subList(1, 1 + visibleY.size).map { it.measure(loose) }
        val xPlaceables = measurables.subList(1 + visibleY.size, 1 + visibleY.size + visibleX.size).map { it.measure(loose) }
        val bubble = measurables.getOrNull(1 + visibleY.size + visibleX.size)?.measure(loose)

        val yColumn = yPlaceables.maxOfOrNull { it.width }?.let { it + size.yLabelGap.roundToPx() } ?: 0
        val plotWidth = (width - yColumn).coerceAtLeast(0)
        val plotHeight = size.height.roundToPx()
        val markerReach = (size.markerRadius + size.markerRingWidth).roundToPx()
        val labelHalf = (yPlaceables.maxOfOrNull { it.height } ?: 0) / 2
        val topSpace = maxOf(
            labelHalf,
            markerReach,
            bubble?.let { it.height + size.tooltipGap.roundToPx() + markerReach } ?: 0,
        )
        val xRow = xPlaceables.maxOfOrNull { it.height }?.let { it + size.xLabelGap.roundToPx() } ?: 0
        val height = topSpace + plotHeight + xRow
        val canvas = measurables[0].measure(Constraints.fixed(plotWidth, plotHeight))

        layout(width, height) {
            canvas.placeRelative(0, topSpace)
            yPlaceables.forEachIndexed { i, label ->
                val y = topSpace + LineChartMath.yOf(visibleY[i].value, top, plotHeight.toFloat())
                label.placeRelative(width - label.width, (y - label.height / 2f).roundToInt())
            }
            xPlaceables.forEachIndexed { i, label ->
                val x = LineChartMath.xOf(visibleX[i].index, steps, plotWidth.toFloat())
                val left = LineChartMath.bubbleLeft(x, label.width.toFloat(), plotWidth.toFloat())
                label.placeRelative(left.roundToInt(), topSpace + plotHeight + size.xLabelGap.roundToPx())
            }
            if (bubble != null && tooltip != null && tooltipPoint != null) {
                val x = LineChartMath.xOf(tooltip.index, steps, plotWidth.toFloat())
                val y = topSpace + LineChartMath.yOf(tooltipPoint, top, plotHeight.toFloat())
                val left = LineChartMath.bubbleLeft(x, bubble.width.toFloat(), plotWidth.toFloat())
                val bubbleTop = (y - markerReach - size.tooltipGap.toPx() - bubble.height).roundToInt().coerceAtLeast(0)
                bubble.placeRelative(left.roundToInt(), bubbleTop)
            }
        }
    }
}

/** Draws the grid, the rule, every series' area and line, and the end markers, left to right. */
private fun DrawScope.drawLinePlot(
    series: List<LineChartSeries>,
    steps: Int,
    top: Float,
    grid: List<LineChartYLabel>,
    rule: Int?,
    style: LineChartStyle,
    size: LineChartSize,
) {
    val width = this.size.width
    val height = this.size.height
    fun x(index: Int) = LineChartMath.xOf(index, steps, width)
    fun y(value: Float) = LineChartMath.yOf(value, top, height)

    grid.forEach { line ->
        val gy = y(line.value)
        drawLine(style.grid, Offset(0f, gy), Offset(width, gy), strokeWidth = size.gridStroke.toPx())
    }
    if (rule != null && rule in 0 until steps) {
        val dash = size.dash.toPx()
        drawLine(
            color = style.rule,
            start = Offset(x(rule), 0f),
            end = Offset(x(rule), height),
            strokeWidth = size.ruleStroke.toPx(),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(dash / 2f, dash)),
        )
    }

    series.forEachIndexed { index, line ->
        val color = line.color ?: style.palette[index % style.palette.size]
        LineChartMath.runs(line.points).forEach { run ->
            val path = Path().apply {
                run.forEachIndexed { i, step ->
                    val point = Offset(x(step), y(line.points[step]!!))
                    if (i == 0) moveTo(point.x, point.y) else lineTo(point.x, point.y)
                }
            }
            if (line.area && run.size > 1) {
                val area = Path().apply {
                    addPath(path)
                    lineTo(x(run.last()), height)
                    lineTo(x(run.first()), height)
                    close()
                }
                drawPath(
                    path = area,
                    brush = Brush.verticalGradient(
                        colors = listOf(color.copy(alpha = style.areaAlpha), color.copy(alpha = 0f)),
                        startY = 0f,
                        endY = height,
                    ),
                )
            }
            if (run.size == 1) {
                // A lone value has no segment to stroke; it is drawn as a dot of the line's width.
                drawCircle(color, radius = size.lineStroke.toPx() / 2f, center = Offset(x(run[0]), y(line.points[run[0]]!!)))
                return@forEach
            }
            val dashed = line.stroke == LineChartStroke.Dashed
            val dash = size.dash.toPx()
            drawPath(
                path = path,
                color = color,
                style = Stroke(
                    width = (if (dashed) size.dashedStroke else size.lineStroke).toPx(),
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round,
                    pathEffect = if (dashed) PathEffect.dashPathEffect(floatArrayOf(dash * 1.25f, dash)) else null,
                ),
            )
        }
    }

    series.forEachIndexed { index, line ->
        val marker = line.endMarker ?: return@forEachIndexed
        val last = LineChartMath.lastIndex(line.points) ?: return@forEachIndexed
        val color = line.color ?: style.palette[index % style.palette.size]
        val center = Offset(x(last), y(line.points[last]!!))
        val ring = size.markerRingWidth.toPx()
        when (marker) {
            LineChartMarker.Filled -> {
                drawCircle(style.markerRing, radius = size.markerRadius.toPx() + ring, center = center)
                drawCircle(color, radius = size.markerRadius.toPx(), center = center)
            }

            LineChartMarker.Hollow -> {
                drawCircle(style.markerRing, radius = size.hollowMarkerRadius.toPx(), center = center)
                drawCircle(
                    color,
                    radius = size.hollowMarkerRadius.toPx(),
                    center = center,
                    style = Stroke(width = ring),
                )
            }
        }
    }
}
