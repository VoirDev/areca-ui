package dev.voir.areca.charts

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/** Tests for [LineChartMath], the scale and line-breaking rules behind [LineChart]. */
class LineChartMathTest {

    @Test
    fun `the axis is as long as the longest series`() {
        val series = listOf(LineChartSeries(List(23) { 1f }, "October"), LineChartSeries(List(31) { 1f }, "September"))

        assertEquals(31, LineChartMath.steps(series))
        assertEquals(1, LineChartMath.steps(emptyList()))
    }

    @Test
    fun `the top is the largest finite point unless one is stated`() {
        val series = listOf(LineChartSeries(listOf(10f, null, Float.NaN, 40f), "A"))

        assertEquals(40f, LineChartMath.top(series, maxValue = null))
        assertEquals(100f, LineChartMath.top(series, maxValue = 100f))
        assertEquals(1f, LineChartMath.top(listOf(LineChartSeries(listOf(null), "A")), maxValue = null))
    }

    @Test
    fun `steps spread across the width - and one step sits in the middle`() {
        assertEquals(0f, LineChartMath.xOf(0, steps = 5, width = 400f))
        assertEquals(400f, LineChartMath.xOf(4, steps = 5, width = 400f))
        assertEquals(200f, LineChartMath.xOf(0, steps = 1, width = 400f))
    }

    @Test
    fun `values are clamped into the plot`() {
        assertEquals(100f, LineChartMath.yOf(0f, top = 10f, height = 100f))
        assertEquals(0f, LineChartMath.yOf(20f, top = 10f, height = 100f))
    }

    @Test
    fun `a line breaks where a point has no value`() {
        assertEquals(
            listOf(listOf(0, 1), listOf(3), listOf(5, 6)),
            LineChartMath.runs(listOf(1f, 2f, null, 3f, Float.NaN, 4f, 5f)),
        )
        assertEquals(6, LineChartMath.lastIndex(listOf(1f, 2f, null, 3f, Float.NaN, 4f, 5f, null)))
        assertNull(LineChartMath.lastIndex(listOf(null, null)))
    }

    @Test
    fun `a bubble stays inside the plot`() {
        assertEquals(0f, LineChartMath.bubbleLeft(x = 10f, bubble = 80f, width = 300f))
        assertEquals(220f, LineChartMath.bubbleLeft(x = 295f, bubble = 80f, width = 300f))
        assertEquals(110f, LineChartMath.bubbleLeft(x = 150f, bubble = 80f, width = 300f))
    }
}
