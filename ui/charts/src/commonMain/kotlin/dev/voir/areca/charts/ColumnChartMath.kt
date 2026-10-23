package dev.voir.areca.charts

/**
 * Vertical extent of one stacked segment, in pixels from the bottom of the bar area.
 *
 * @property index Position of the segment in its bar's [ColumnChartBar.segments].
 * @property bottom Distance of the segment's bottom edge above the baseline.
 * @property height Height of the segment.
 * @property isTop Whether it is the highest drawn segment, which alone is rounded.
 */
internal data class ColumnSegmentSpan(
    val index: Int,
    val bottom: Float,
    val height: Float,
    val isTop: Boolean,
)

/**
 * The arithmetic behind [ColumnChart], kept apart from drawing so it can be tested as plain
 * functions. Everything here is in pixels unless it says otherwise.
 */
internal object ColumnChartMath {
    /**
     * The value at the top of a chart's scale.
     *
     * A bar counts as the sum of its drawable segments, so a stacked bar is measured whole.
     *
     * @param columns Every column, placeholders included.
     * @param maxValue Value the caller states for the top, or `null` to use the tallest bar.
     * @param lines Reference lines, which raise the automatic top so a line above every bar — a
     *   budget no month reached — is still drawn.
     * @return The stated top when it is finite and positive; otherwise the tallest bar or the
     *   highest finite reference line, whichever is higher, or `1f` when neither is positive.
     */
    fun top(
        columns: List<ColumnChartColumn<*>>,
        maxValue: Float?,
        lines: List<ColumnChartReferenceLine> = emptyList(),
    ): Float {
        if (maxValue != null && maxValue.isFinite() && maxValue > 0f) return maxValue
        val tallest = columns.maxOfOrNull { column -> column.bars.maxOfOrNull(::total) ?: 0f } ?: 0f
        val highestLine = lines.mapNotNull { it.value.takeIf(Float::isFinite) }.maxOrNull() ?: 0f
        return maxOf(tallest, highestLine).takeIf { it > 0f } ?: 1f
    }

    /**
     * Total of a bar's drawable segments.
     *
     * @param bar The bar.
     * @return Sum of its positive, finite segment values.
     */
    fun total(bar: ColumnChartBar): Float = bar.segments.sumOf { drawable(it.value).toDouble() }.toFloat()

    /**
     * Where each drawable segment of a bar sits.
     *
     * Segments stack from the baseline in order. The bar's whole height is its total against [top],
     * at least [minHeight] when the total is positive and never above [area]. Each segment takes its
     * share of that height, less [gap] below every segment but the first, so the gaps come out of
     * the segments rather than pushing the bar taller.
     *
     * @param bar The bar.
     * @param top Value at the top of the scale. Positive.
     * @param area Height of the bar area.
     * @param minHeight Least height of a bar with a positive total.
     * @param gap Space between stacked segments.
     * @return One span per drawable segment, bottom first. Empty when nothing is drawn.
     */
    fun segments(
        bar: ColumnChartBar,
        top: Float,
        area: Float,
        minHeight: Float,
        gap: Float,
    ): List<ColumnSegmentSpan> {
        val total = total(bar)
        if (total <= 0f || area <= 0f) return emptyList()
        val height = (total / top * area).coerceAtLeast(minHeight).coerceAtMost(area)
        val drawn = bar.segments.withIndex().filter { drawable(it.value.value) > 0f }
        var bottom = 0f
        return drawn.mapIndexed { position, (index, segment) ->
            val share = segment.value / total * height
            val lead = if (position == 0) 0f else gap.coerceAtMost(share)
            val span = ColumnSegmentSpan(
                index = index,
                bottom = bottom + lead,
                height = (share - lead).coerceAtLeast(0f),
                isTop = position == drawn.lastIndex,
            )
            bottom += share
            span
        }
    }

    /**
     * Height of a bar from the baseline, as [segments] draws it.
     *
     * @return The bar's drawn height, `0f` when nothing is drawn.
     */
    fun barHeight(bar: ColumnChartBar, top: Float, area: Float, minHeight: Float): Float {
        val total = total(bar)
        if (total <= 0f || area <= 0f) return 0f
        return (total / top * area).coerceAtLeast(minHeight).coerceAtMost(area)
    }

    /**
     * Distance of a reference line above the baseline.
     *
     * @param value The line's value.
     * @param top Value at the top of the scale.
     * @param area Height of the bar area.
     * @return Height above the baseline, or `null` when the value is not finite, negative, or above
     *   the scale's top.
     */
    fun lineHeight(value: Float, top: Float, area: Float): Float? {
        if (!value.isFinite() || value < 0f || value > top) return null
        return value / top * area
    }

    /**
     * Width of a column's bars side by side.
     *
     * @param bars The column's bars.
     * @param barWidth Width of a regular bar.
     * @param companionWidth Width of a companion bar.
     * @param gap Space between neighbouring bars.
     * @return Total width, `0f` for no bars.
     */
    fun groupWidth(bars: List<ColumnChartBar>, barWidth: Float, companionWidth: Float, gap: Float): Float {
        if (bars.isEmpty()) return 0f
        return bars.sumOf { (if (it.companion) companionWidth else barWidth).toDouble() }.toFloat() +
            gap * (bars.size - 1)
    }

    /**
     * Scroll position that brings a column fully into view, or `null` when it already is.
     *
     * @param start Column's start edge, measured along the scrolling content.
     * @param end Column's end edge.
     * @param scroll Current scroll position.
     * @param viewport Width of the visible window.
     * @param max Largest scroll position.
     * @return The nearest position showing the whole column, clamped to `0..max`.
     */
    fun reveal(start: Int, end: Int, scroll: Int, viewport: Int, max: Int): Int? {
        val target = when {
            start < scroll -> start
            end > scroll + viewport -> end - viewport
            else -> return null
        }
        return target.coerceIn(0, max.coerceAtLeast(0))
    }

    /**
     * Factor a column's bars shrink by to fit its width.
     *
     * @param groupWidth Width the bars take at their own size, from [groupWidth].
     * @param width Width of the column.
     * @return `1f` when they fit; otherwise the factor that makes them exactly as wide as the column.
     */
    fun fit(groupWidth: Float, width: Float): Float =
        if (groupWidth <= 0f || groupWidth <= width) 1f else (width / groupWidth).coerceAtLeast(0f)

    /**
     * Columns at least partly inside the visible window.
     *
     * @param scroll Current scroll position.
     * @param viewport Width of the visible window.
     * @param leadingPadding Space before the first column.
     * @param stride Distance from one column's start to the next one's.
     * @param columnWidth Width of a column.
     * @param count How many columns there are. Positive.
     * @return Indices of the visible columns, clamped to `0 until count`.
     */
    fun visibleColumns(
        scroll: Int,
        viewport: Int,
        leadingPadding: Int,
        stride: Int,
        columnWidth: Int,
        count: Int,
    ): IntRange {
        val left = scroll - leadingPadding
        val right = left + viewport
        // A column is in view when its end is past the window's start and its start before its end.
        val first = ((left - columnWidth).floorDiv(stride) + 1).coerceIn(0, count - 1)
        val last = (right - 1).floorDiv(stride).coerceIn(0, count - 1)
        return first..maxOf(first, last)
    }

    /**
     * Text assistive technology reads for a column.
     *
     * @param column The column.
     * @return Its own description, or its label, sublabel and value label joined by commas.
     */
    fun description(column: ColumnChartColumn<*>): String =
        column.contentDescription ?: listOfNotNull(
            column.label.takeIf { it.isNotBlank() },
            column.sublabel?.takeIf { it.isNotBlank() },
            column.valueLabel?.takeIf { it.isNotBlank() },
        ).joinToString(", ")

    /** A segment value that draws: positive and finite, or `0f`. */
    private fun drawable(value: Float): Float = if (value.isFinite() && value > 0f) value else 0f
}
