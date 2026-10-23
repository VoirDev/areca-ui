package dev.voir.areca.charts

/**
 * The arithmetic behind [LineChart], kept apart from drawing so it can be tested as plain
 * functions. Positions are in pixels from the plot's left and top edges.
 */
internal object LineChartMath {
    /**
     * How many steps the x axis has.
     *
     * @param series Every series.
     * @return The longest series' length, at least `1`.
     */
    fun steps(series: List<LineChartSeries>): Int = series.maxOfOrNull { it.points.size }?.coerceAtLeast(1) ?: 1

    /**
     * The value at the top of the scale.
     *
     * @param series Every series.
     * @param maxValue Value the caller states, or `null` for the largest point.
     * @return The stated top when finite and positive; otherwise the largest finite point, or `1f`
     *   when no point is positive.
     */
    fun top(series: List<LineChartSeries>, maxValue: Float?): Float {
        if (maxValue != null && maxValue.isFinite() && maxValue > 0f) return maxValue
        val largest = series.flatMap { it.points }.mapNotNull(::finite).maxOrNull() ?: 0f
        return largest.takeIf { it > 0f } ?: 1f
    }

    /**
     * Horizontal position of a step.
     *
     * @param index The step.
     * @param steps How many steps the axis has.
     * @param width Width of the plot.
     * @return `0f` for the first step and [width] for the last; the middle for a one-step axis.
     */
    fun xOf(index: Int, steps: Int, width: Float): Float =
        if (steps <= 1) width / 2f else index.toFloat() / (steps - 1) * width

    /**
     * Vertical position of a value.
     *
     * @param value The value, clamped into `0..top`.
     * @param top Value at the top of the scale.
     * @param height Height of the plot.
     * @return [height] at zero, `0f` at [top].
     */
    fun yOf(value: Float, top: Float, height: Float): Float = height * (1f - (value / top).coerceIn(0f, 1f))

    /**
     * The unbroken runs of a series, split wherever a point has no value.
     *
     * @param points The series' points.
     * @return Runs of step indices, each at least one long, in order.
     */
    fun runs(points: List<Float?>): List<List<Int>> {
        val runs = mutableListOf<List<Int>>()
        var current = mutableListOf<Int>()
        points.forEachIndexed { index, point ->
            if (finite(point) != null) {
                current += index
            } else if (current.isNotEmpty()) {
                runs += current
                current = mutableListOf()
            }
        }
        if (current.isNotEmpty()) runs += current
        return runs
    }

    /**
     * The last step of a series with a value.
     *
     * @param points The series' points.
     * @return Its index, or `null` when no point has a value.
     */
    fun lastIndex(points: List<Float?>): Int? = points.indexOfLast { finite(it) != null }.takeIf { it >= 0 }

    /**
     * Left edge of a bubble centred on a point, kept inside the plot.
     *
     * @param x The point's horizontal position.
     * @param bubble Width of the bubble.
     * @param width Width of the plot.
     * @return The left edge, so the bubble neither starts before `0` nor ends past [width].
     */
    fun bubbleLeft(x: Float, bubble: Float, width: Float): Float =
        (x - bubble / 2f).coerceIn(0f, (width - bubble).coerceAtLeast(0f))

    /**
     * A point's value when it is one.
     *
     * @return [value] when finite, otherwise `null`.
     */
    fun finite(value: Float?): Float? = value?.takeIf { it.isFinite() }
}
