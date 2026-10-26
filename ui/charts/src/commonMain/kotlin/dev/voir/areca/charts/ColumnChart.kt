package dev.voir.areca.charts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.indication.TouchIndication
import dev.voir.areca.modifier.clickCursor
import kotlin.math.roundToInt
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first

/**
 * A row of columns, one per period, that reads as a chart and works as a period picker.
 *
 * Each column holds bars side by side — one, a pair to compare, a stack of parts, a slim companion
 * such as the same period a year earlier — over a label and an optional second line. Selecting a
 * column puts a rounded [halo][ColumnChartStyle.halo] behind its bars and labels, sets its label in
 * the emphasised style, and dims every other column's bars, so the selection is what the reader
 * sees first. Dashed [referenceLines] — the usual amount, an average — cross the whole chart and
 * stay put while the columns scroll under them.
 *
 * **Layout.** Under [ColumnChartLayout.Scroll] every column is [ColumnChartSize.columnWidth] wide
 * and the chart scrolls sideways, opening on its last column; put the latest period last. Whenever
 * the selection changes, from a tap or from code, the selected column is scrolled into view. Under
 * [ColumnChartLayout.Fill] the columns share the width and nothing scrolls.
 *
 * **Scale.** Bars grow from zero to their value against [maxValue], or against the tallest bar —
 * a stacked bar counted whole — when it is `null`. Zero, negative and non-finite values draw
 * nothing. A bar with a positive value is at least [ColumnChartSize.minBarHeight] tall.
 *
 * **Selection.** The chart holds none: it draws [selectedKey] and reports taps through [onSelect].
 * Without [onSelect] the columns are not interactive, and [selectedKey] only highlights one — the
 * current month in a year of months, the busiest weekday. A disabled or placeholder column cannot
 * be selected.
 *
 * **Accessibility.** Each column is its own node, read as its
 * [contentDescription][ColumnChartColumn.contentDescription] or its labels, and announced as
 * selected; with [onSelect] the columns form a selectable group. Placeholder columns are skipped.
 * Reference line labels are read once.
 *
 * The chart mirrors in a right-to-left layout: the first column sits at the start edge, the end
 * edge holds the latest, and the bars of a column follow the reading direction.
 *
 * Requires `ArecaThemeProvider` above it.
 *
 * @param K Type of the columns' identity.
 * @param columns Columns from the start edge, the latest period last.
 * @param modifier Modifier applied to the chart, which fills the width it is given.
 * @param selectedKey Key of the selected column, or `null` for none.
 * @param onSelect Called with a column's key when the user selects it, or `null` for a chart that
 *   is read but not used to pick. Never called for the selected, a disabled or a placeholder column.
 * @param state Scroll position, under [ColumnChartLayout.Scroll]. Hoist it to scroll from outside or
 *   to read [ColumnChartState.isAtEnd].
 * @param referenceLines Dashed lines across the chart.
 * @param valueLabels Which columns show their value label.
 * @param layout Whether the columns scroll at their own width or share the chart's.
 * @param maxValue Value at the top of the scale, or `null` for the tallest bar.
 * @param contentPadding Space at the start and end of the columns, inside the scroll — usually the
 *   screen gutter, so the columns scroll to the screen's edge. The reference lines keep within it.
 * @param style Colours. Defaults to [LocalColumnChartStyle], or [ColumnChartStyle.Default].
 * @param size Dimensions and type. Defaults to [LocalColumnChartSize], or [ColumnChartSize.Medium].
 */
@Composable
public fun <K> ColumnChart(
    columns: List<ColumnChartColumn<K>>,
    modifier: Modifier = Modifier,
    selectedKey: K? = null,
    onSelect: ((K) -> Unit)? = null,
    state: ColumnChartState = rememberColumnChartState(),
    referenceLines: List<ColumnChartReferenceLine> = emptyList(),
    valueLabels: ColumnChartValueLabels = ColumnChartValueLabels.Selected,
    layout: ColumnChartLayout = ColumnChartLayout.Scroll,
    maxValue: Float? = null,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    style: ColumnChartStyle = LocalColumnChartStyle.current ?: ColumnChartStyle.Default,
    size: ColumnChartSize = LocalColumnChartSize.current ?: ColumnChartSize.Medium,
) {
    val density = LocalDensity.current
    val direction = LocalLayoutDirection.current
    val scroll = layout == ColumnChartLayout.Scroll
    val top = remember(columns, maxValue, referenceLines) { ColumnChartMath.top(columns, maxValue, referenceLines) }
    val hasSublabel = remember(columns) { columns.any { !it.sublabel.isNullOrBlank() } }
    val hasSelection = selectedKey != null && columns.any { it.key == selectedKey }
    val startPadding = contentPadding.calculateStartPadding(direction)
    val endPadding = contentPadding.calculateEndPadding(direction)

    if (scroll) {
        SideEffect {
            with(density) {
                state.columnWidth = size.columnWidth.roundToPx()
                state.columnStride = (size.columnWidth + size.columnGap).roundToPx()
                state.leadingPadding = startPadding.roundToPx()
                state.trailingPadding = endPadding.roundToPx()
            }
            state.columnCount = columns.size
        }
        // While pinned, the end is followed as columns arrive and the chart resizes.
        LaunchedEffect(state) {
            snapshotFlow { state.pinnedToEnd to state.scroll.maxValue }
                .collect { (pinned, max) -> if (pinned && state.scroll.value != max) state.scroll.scrollTo(max) }
        }
        // A scroll that ends away from the end unpins the position; one that ends there pins it.
        LaunchedEffect(state) {
            snapshotFlow { state.scroll.isScrollInProgress }
                .filter { inProgress -> !inProgress }
                .collect { state.pinnedToEnd = state.isAtEnd }
        }
        // A new selection is scrolled into view; the same selection never moves the user again.
        val currentColumns by rememberUpdatedState(columns)
        LaunchedEffect(state, selectedKey) {
            if (selectedKey == null) return@LaunchedEffect
            snapshotFlow { state.scroll.viewportSize > 0 && state.columnStride > 0 }.first { laidOut -> laidOut }
            val index = currentColumns.indexOfFirst { it.key == selectedKey }
            if (index >= 0) state.scrollToColumn(index)
        }
        Box(modifier = modifier.fillMaxWidth()) {
            ColumnRow(
                modifier = Modifier
                    .horizontalScroll(state.scroll)
                    .padding(start = startPadding, end = endPadding),
                columns = columns,
                selectedKey = selectedKey,
                hasSelection = hasSelection,
                onSelect = onSelect,
                valueLabels = valueLabels,
                top = top,
                hasSublabel = hasSublabel,
                fill = false,
                style = style,
                size = size,
            )
            ReferenceLines(
                lines = referenceLines,
                top = top,
                modifier = Modifier.padding(start = startPadding, end = endPadding),
                style = style,
                size = size,
            )
        }
    } else {
        Box(modifier = modifier.fillMaxWidth()) {
            ColumnRow(
                modifier = Modifier.fillMaxWidth().padding(start = startPadding, end = endPadding),
                columns = columns,
                selectedKey = selectedKey,
                hasSelection = hasSelection,
                onSelect = onSelect,
                valueLabels = valueLabels,
                top = top,
                hasSublabel = hasSublabel,
                fill = true,
                style = style,
                size = size,
            )
            ReferenceLines(
                lines = referenceLines,
                top = top,
                modifier = Modifier.padding(start = startPadding, end = endPadding),
                style = style,
                size = size,
            )
        }
    }
}

/**
 * The columns side by side, with the baseline drawn across them.
 *
 * @param fill Whether the columns share the row's width rather than taking their own.
 */
@Composable
private fun <K> ColumnRow(
    modifier: Modifier,
    columns: List<ColumnChartColumn<K>>,
    selectedKey: K?,
    hasSelection: Boolean,
    onSelect: ((K) -> Unit)?,
    valueLabels: ColumnChartValueLabels,
    top: Float,
    hasSublabel: Boolean,
    fill: Boolean,
    style: ColumnChartStyle,
    size: ColumnChartSize,
) {
    Row(
        modifier = modifier
            .then(if (onSelect != null) Modifier.selectableGroup() else Modifier)
            .drawWithContent {
                drawContent()
                // Over the columns, so the selected column's halo does not hide it.
                val stroke = size.baselineStroke.toPx()
                val y = size.plotHeight.toPx() - stroke / 2f
                drawLine(
                    color = style.baseline,
                    start = Offset(0f, y),
                    end = Offset(this.size.width, y),
                    strokeWidth = stroke,
                )
            },
        horizontalArrangement = Arrangement.spacedBy(size.columnGap),
    ) {
        columns.forEach { column ->
            val selected = column.key == selectedKey
            ColumnCell(
                column = column,
                selected = selected,
                dimmed = hasSelection && !selected,
                onSelect = onSelect?.let { select -> { select(column.key) } },
                showValue = when (valueLabels) {
                    ColumnChartValueLabels.None -> false
                    ColumnChartValueLabels.Selected -> selected
                    ColumnChartValueLabels.All -> true
                },
                top = top,
                hasSublabel = hasSublabel,
                fill = fill,
                style = style,
                size = size,
            )
        }
    }
}

/**
 * One column: its plot, label and second line, on the halo when selected.
 *
 * @param dimmed Whether another column is selected, so this one's bars are drawn faded.
 * @param onSelect Called when the column is pressed, or `null` for a chart that does not pick. A
 *   disabled column is still announced among its selectable neighbours, as unavailable.
 * @param showValue Whether the column's value label is drawn.
 */
@Composable
private fun <K> RowScope.ColumnCell(
    column: ColumnChartColumn<K>,
    selected: Boolean,
    dimmed: Boolean,
    onSelect: (() -> Unit)?,
    showValue: Boolean,
    top: Float,
    hasSublabel: Boolean,
    fill: Boolean,
    style: ColumnChartStyle,
    size: ColumnChartSize,
) {
    val description = remember(column) { ColumnChartMath.description(column) }
    Column(
        modifier = Modifier
            .then(if (fill) Modifier.weight(1f) else Modifier.width(size.columnWidth))
            .background(if (selected) style.halo else Color.Transparent, size.haloShape)
            .then(
                when {
                    column.placeholder -> Modifier.clearAndSetSemantics {}
                    onSelect != null -> Modifier
                        .selectable(
                            selected = selected,
                            enabled = column.enabled,
                            interactionSource = null,
                            indication = TouchIndication(shape = size.haloShape),
                            role = Role.Tab,
                            onClick = { if (!selected) onSelect() },
                        )
                        .clickCursor(column.enabled)
                        .semantics { contentDescription = description }

                    else -> Modifier.semantics {
                        contentDescription = description
                        this.selected = selected
                    }
                },
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ColumnPlot(
            column = column,
            dimmed = dimmed,
            showValue = showValue && !column.valueLabel.isNullOrBlank(),
            top = top,
            style = style,
            size = size,
        )
        Spacer(modifier = Modifier.height(size.labelGap))
        Text(
            text = column.label,
            style = if (selected) size.labelSelectedTextStyle else size.labelTextStyle,
            color = if (selected) style.labelSelected else style.label,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth().clearAndSetSemantics {},
        )
        if (hasSublabel) {
            Text(
                text = column.sublabel.orEmpty(),
                style = size.sublabelTextStyle,
                color = style.sublabel,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth().clearAndSetSemantics {},
            )
        }
        Spacer(modifier = Modifier.height(size.labelBottomPadding))
    }
}

/**
 * A column's plot: its bars on the baseline, and its value label above the tallest one.
 *
 * The value label is measured at its own width and centred on the column, so a figure wider than a
 * narrow column runs over the gaps either side rather than wrapping.
 */
@Composable
private fun ColumnPlot(
    column: ColumnChartColumn<*>,
    dimmed: Boolean,
    showValue: Boolean,
    top: Float,
    style: ColumnChartStyle,
    size: ColumnChartSize,
) {
    Layout(
        modifier = Modifier.fillMaxWidth().height(size.plotHeight),
        content = {
            Canvas(modifier = Modifier) {
                drawColumnBars(column = column, dimmed = dimmed, top = top, style = style, size = size)
            }
            if (showValue) {
                Text(
                    text = column.valueLabel.orEmpty(),
                    style = size.valueLabelTextStyle,
                    color = style.valueLabel,
                    maxLines = 1,
                    modifier = Modifier.clearAndSetSemantics {},
                )
            }
        },
    ) { measurables, constraints ->
        val width = constraints.maxWidth
        val height = constraints.maxHeight
        val plot = measurables[0].measure(Constraints.fixed(width, height))
        val label = measurables.getOrNull(1)?.measure(Constraints())
        layout(width, height) {
            plot.place(0, 0)
            if (label != null) {
                val area = height - size.valueLabelSpace.toPx()
                val tallest = column.bars.maxOfOrNull { bar ->
                    ColumnChartMath.barHeight(bar, top, area, size.minBarHeight.toPx())
                } ?: 0f
                val y = (height - tallest - label.height - size.stackGap.toPx()).roundToInt().coerceAtLeast(0)
                label.placeRelative((width - label.width) / 2, y)
            }
        }
    }
}

/** Draws a column's bars, centred, standing on the bottom edge. */
private fun DrawScope.drawColumnBars(
    column: ColumnChartColumn<*>,
    dimmed: Boolean,
    top: Float,
    style: ColumnChartStyle,
    size: ColumnChartSize,
) {
    val width = this.size.width
    val height = this.size.height
    val area = height - size.valueLabelSpace.toPx()
    val barWidth = size.barWidth.toPx()
    val companionWidth = size.companionWidth.toPx()
    val gap = size.barGap.toPx()
    val radius = size.barCornerRadius.toPx()
    val baseRadius = size.barBaseCornerRadius.toPx()
    val minHeight = size.minBarHeight.toPx()
    val stackGap = size.stackGap.toPx()
    val alpha = if (dimmed) style.dimmedAlpha else 1f
    // Under ColumnChartLayout.Fill a column can be narrower than its bars; they shrink to fit.
    val fit = ColumnChartMath.fit(ColumnChartMath.groupWidth(column.bars, barWidth, companionWidth, gap), width)
    val regularWidth = barWidth * fit
    val slimWidth = companionWidth * fit
    val barGap = gap * fit
    val groupWidth = ColumnChartMath.groupWidth(column.bars, regularWidth, slimWidth, barGap)

    // Drawn left to right; right to left, the column is mirrored about its centre, so the first
    // bar sits at the start edge.
    scale(scaleX = if (layoutDirection == LayoutDirection.Rtl) -1f else 1f, scaleY = 1f, pivot = center) {
        var x = (width - groupWidth) / 2f
        var regularIndex = 0
        column.bars.forEach { bar ->
            val w = if (bar.companion) slimWidth else regularWidth
            val spans = ColumnChartMath.segments(bar, top, area, minHeight, stackGap)
            spans.forEach { span ->
                val segment = bar.segments[span.index]
                val color = segment.color ?: when {
                    bar.companion -> style.companion
                    bar.segments.size > 1 -> style.palette[span.index % style.palette.size]
                    else -> style.palette[regularIndex % style.palette.size]
                }
                val r = if (span.isTop) minOf(radius, w / 2f, span.height) else 0f
                // Only the lowest segment stands on the baseline, so only it takes the base corners.
                val b = if (span.bottom == 0f) minOf(baseRadius, w / 2f, (span.height - r).coerceAtLeast(0f)) else 0f
                val path = Path().apply {
                    addRoundRect(
                        RoundRect(
                            left = x,
                            top = height - span.bottom - span.height,
                            right = x + w,
                            bottom = height - span.bottom,
                            topLeftCornerRadius = CornerRadius(r, r),
                            topRightCornerRadius = CornerRadius(r, r),
                            bottomLeftCornerRadius = CornerRadius(b, b),
                            bottomRightCornerRadius = CornerRadius(b, b),
                        ),
                    )
                }
                if (column.placeholder) {
                    val stroke = size.placeholderStroke.toPx()
                    drawPath(
                        path = path,
                        color = style.placeholder,
                        style = Stroke(
                            width = stroke,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(stroke * 3f, stroke * 2f)),
                        ),
                    )
                } else if (segment.brush != null) {
                    drawPath(path = path, brush = segment.brush, alpha = alpha)
                } else {
                    drawPath(path = path, color = color, alpha = alpha)
                }
            }
            if (!bar.companion) regularIndex++
            x += w + barGap
        }
    }
}

/**
 * The reference lines over the plot, with their labels at the end edge.
 *
 * Sits over the columns without scrolling, and takes no input, so presses reach the columns under
 * it.
 */
@Composable
private fun ReferenceLines(
    lines: List<ColumnChartReferenceLine>,
    top: Float,
    modifier: Modifier,
    style: ColumnChartStyle,
    size: ColumnChartSize,
) {
    if (lines.isEmpty()) return
    Layout(
        modifier = modifier.fillMaxWidth().height(size.plotHeight),
        content = {
            Canvas(modifier = Modifier) {
                val height = this.size.height
                val area = height - size.valueLabelSpace.toPx()
                val stroke = size.referenceStroke.toPx()
                val dash = size.referenceDash.toPx()
                lines.forEach { line ->
                    val lift = ColumnChartMath.lineHeight(line.value, top, area) ?: return@forEach
                    val y = height - lift
                    drawLine(
                        color = line.color ?: style.referenceLine,
                        start = Offset(0f, y),
                        end = Offset(this.size.width, y),
                        strokeWidth = stroke,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(dash, dash)),
                    )
                }
            }
            lines.forEach { line ->
                Text(
                    text = line.label.orEmpty(),
                    style = size.referenceLabelTextStyle,
                    color = line.color ?: style.referenceLabel,
                    maxLines = 1,
                    modifier = Modifier
                        .background(style.referenceLabelBg)
                        .padding(start = size.columnGap),
                )
            }
        },
    ) { measurables, constraints ->
        val width = constraints.maxWidth
        val height = constraints.maxHeight
        val canvas = measurables[0].measure(Constraints.fixed(width, height))
        val labels = measurables.drop(1).map { it.measure(Constraints(maxWidth = width)) }
        val area = height - size.valueLabelSpace.toPx()
        layout(width, height) {
            canvas.place(0, 0)
            labels.forEachIndexed { index, label ->
                val line = lines[index]
                if (line.label.isNullOrBlank()) return@forEachIndexed
                val lift = ColumnChartMath.lineHeight(line.value, top, area) ?: return@forEachIndexed
                val y = (height - lift - label.height - size.referenceStroke.toPx()).roundToInt()
                    .coerceAtLeast(0)
                label.placeRelative(width - label.width, y)
            }
        }
    }
}
