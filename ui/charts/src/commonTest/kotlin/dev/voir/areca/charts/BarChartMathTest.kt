package dev.voir.areca.charts

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** Tests for [BarChartMath], the scale, baseline, and placement rules a [BarChart] draws by. */
class BarChartMathTest {
    /** Two pixels per dp, so a dp width shows up doubled in a layout's pixels. */
    private val density = Density(2f)

    @Test
    fun `auto-scale tops out at the data max when it is below one`() {
        val scale = BarChartMath.scale(values = listOf(0.2f, 0.5f), minValue = 0f, maxValue = null)

        assertEquals(0.5f, scale.max)
        assertSpan(BarSpan(top = 0f, height = 100f), barSpan(scale, 0.5f))
    }

    @Test
    fun `auto-scale falls back to one unit above the minimum when no value lies above it`() {
        assertEquals(1f, BarChartMath.scale(listOf(0f, 0f), minValue = 0f, maxValue = null).max)
        assertEquals(1f, BarChartMath.scale(emptyList(), minValue = 0f, maxValue = null).max)
        assertEquals(1f, BarChartMath.scale(listOf(-4f), minValue = 0f, maxValue = null).max)
    }

    @Test
    fun `an explicit max wins over the data`() {
        val scale = BarChartMath.scale(listOf(10f, 500f), minValue = 0f, maxValue = 100f)

        assertEquals(100f, scale.max)
        assertSpan(BarSpan(top = 0f, height = 100f), barSpan(scale, 500f))
    }

    @Test
    fun `non-finite values are left out of the auto-scale`() {
        val values = listOf(Float.NaN, 10f, Float.POSITIVE_INFINITY, 20f, Float.NEGATIVE_INFINITY)

        val scale = BarChartMath.scale(values, minValue = 0f, maxValue = null)

        assertEquals(20f, scale.max)
        assertSpan(BarSpan(top = 50f, height = 50f), barSpan(scale, 10f))
    }

    @Test
    fun `a non-finite value draws no bar`() {
        val scale = BarChartMath.scale(listOf(10f), minValue = 0f, maxValue = null)

        assertNull(barSpan(scale, Float.NaN))
        assertNull(barSpan(scale, Float.POSITIVE_INFINITY))
        assertNull(barSpan(scale, Float.NEGATIVE_INFINITY))
    }

    @Test
    fun `a negative value on the default scale clamps to zero and draws no height`() {
        val scale = BarChartMath.scale(listOf(-10f, 20f), minValue = 0f, maxValue = null)

        assertEquals(0f, scale.baseline)
        assertSpan(BarSpan(top = 100f, height = 0f), barSpan(scale, -10f, minBarHeight = 8f))
    }

    @Test
    fun `a negative value grows down from zero when the scale reaches below zero`() {
        val scale = BarChartMath.scale(listOf(-30f, 60f), minValue = -50f, maxValue = 100f)

        val span = BarChartMath.barSpan(scale, value = -30f, plotHeight = 150f, minBarHeight = 0f)

        assertEquals(0f, scale.baseline)
        assertSpan(BarSpan(top = 100f, height = 30f), span)
    }

    @Test
    fun `a positive value grows up from zero when the scale reaches below zero`() {
        val scale = BarChartMath.scale(listOf(60f), minValue = -50f, maxValue = 100f)

        val span = BarChartMath.barSpan(scale, value = 60f, plotHeight = 150f, minBarHeight = 0f)

        assertSpan(BarSpan(top = 40f, height = 60f), span)
    }

    @Test
    fun `a value below a negative minimum is clamped to the bottom of the plot`() {
        val scale = BarChartMath.scale(listOf(-500f), minValue = -50f, maxValue = 100f)

        val span = BarChartMath.barSpan(scale, value = -500f, plotHeight = 150f, minBarHeight = 0f)

        assertSpan(BarSpan(top = 100f, height = 50f), span)
    }

    @Test
    fun `bars grow from the minimum when the whole scale is above zero`() {
        val scale = BarChartMath.scale(listOf(75f), minValue = 50f, maxValue = 100f)

        assertEquals(50f, scale.baseline)
        assertSpan(BarSpan(top = 50f, height = 50f), barSpan(scale, 75f))
    }

    @Test
    fun `a tiny value is drawn at the minimum bar height`() {
        val scale = BarChartMath.scale(listOf(1000f), minValue = 0f, maxValue = null)

        assertSpan(BarSpan(top = 92f, height = 8f), barSpan(scale, 1f, minBarHeight = 8f))
    }

    @Test
    fun `the minimum bar height never pushes a bar past the plot edge`() {
        val scale = BarChartMath.scale(listOf(-100f, 1000f), minValue = -1f, maxValue = 1000f)

        val span = BarChartMath.barSpan(scale, value = -100f, plotHeight = 100f, minBarHeight = 8f)

        assertEquals(100f, (span?.top ?: 0f) + (span?.height ?: 0f), absoluteTolerance = 0.001f)
    }

    @Test
    fun `fill bars share the plot after the gaps`() {
        val layout = layout(groupSizes = listOf(2, 2), barWidth = BarChartBarWidth.Fill)

        // 200 wide, minus one 10px bar gap per group and one 20px group gap, over four bars.
        assertEquals(40f, layout.barWidth)
        assertEquals(listOf(0f, 50f, 110f, 160f), layout.barStarts)
    }

    @Test
    fun `a fixed width wider than the plot's share shrinks so every bar fits`() {
        val layout = layout(groupSizes = listOf(4), barWidth = BarChartBarWidth.Fixed(100.dp))

        assertEquals(42.5f, layout.barWidth)
        assertEquals(200f, layout.barStarts.last() + layout.barWidth)
    }

    @Test
    fun `a range caps the fill width at its max`() {
        val layout = layout(
            groupSizes = listOf(1, 1),
            barWidth = BarChartBarWidth.Range(min = 4.dp, max = 10.dp),
        )

        assertEquals(20f, layout.barWidth)
    }

    @Test
    fun `a range min yields when that many bars do not fit`() {
        val layout = layout(
            groupSizes = listOf(10),
            barWidth = BarChartBarWidth.Range(min = 40.dp, max = 50.dp),
        )

        assertEquals(11f, layout.barWidth)
    }

    @Test
    fun `alignment places narrow bars at the start or centre or end`() {
        val width = BarChartBarWidth.Fixed(10.dp)

        assertEquals(0f, layout(listOf(1), width, BarChartHorizontalAlignment.Start).startOffset)
        assertEquals(90f, layout(listOf(1), width, BarChartHorizontalAlignment.Center).startOffset)
        assertEquals(180f, layout(listOf(1), width, BarChartHorizontalAlignment.End).startOffset)
    }

    @Test
    fun `an empty chart lays out no bars`() {
        val layout = layout(groupSizes = emptyList(), barWidth = BarChartBarWidth.Fill)

        assertEquals(BarChartLayout(barWidth = 0f, startOffset = 100f, barStarts = emptyList(), contentWidth = 0f), layout)
    }

    @Test
    fun `a range ending below its start is rejected`() {
        assertFailsWith<IllegalArgumentException> {
            BarChartBarWidth.Range(min = 20.dp, max = 10.dp)
        }
    }

    @Test
    fun `a negative bar width is rejected`() {
        assertFailsWith<IllegalArgumentException> { BarChartBarWidth.Fixed((-1).dp) }
        assertFailsWith<IllegalArgumentException> { BarChartBarWidth.Range((-1).dp, 4.dp) }
    }

    @Test
    fun `unbounded fill bars take the unbounded width from the start edge`() {
        val layout = layout(
            groupSizes = listOf(2, 1),
            barWidth = BarChartBarWidth.Fill,
            plotWidth = Float.POSITIVE_INFINITY,
        )

        assertEquals(12f, layout.barWidth)
        assertEquals(0f, layout.startOffset)
        assertEquals(listOf(0f, 22f, 54f), layout.barStarts)
        assertEquals(66f, layout.contentWidth)
    }

    @Test
    fun `unbounded fixed and range bars take their natural width`() {
        val fixed = layout(listOf(1), BarChartBarWidth.Fixed(10.dp), plotWidth = Float.POSITIVE_INFINITY)
        val range = layout(
            groupSizes = listOf(1),
            barWidth = BarChartBarWidth.Range(min = 4.dp, max = 15.dp),
            plotWidth = Float.POSITIVE_INFINITY,
        )

        assertEquals(20f, fixed.barWidth)
        assertEquals(30f, range.barWidth)
    }

    @Test
    fun `an unbounded empty chart takes no width`() {
        val layout = layout(emptyList(), BarChartBarWidth.Fill, plotWidth = Float.POSITIVE_INFINITY)

        assertEquals(0f, layout.contentWidth)
    }

    @Test
    fun `a bounded layout reports the width of its bars and gaps`() {
        val layout = layout(groupSizes = listOf(1), barWidth = BarChartBarWidth.Fixed(10.dp))

        assertEquals(20f, layout.contentWidth)
    }

    @Test
    fun `a line on the plot edge moves inward by half its stroke`() {
        assertEquals(1f, BarChartMath.insetLine(position = 0f, stroke = 2f, extent = 100f))
        assertEquals(99f, BarChartMath.insetLine(position = 100f, stroke = 2f, extent = 100f))
    }

    @Test
    fun `a line inside the plot keeps its position`() {
        assertEquals(40f, BarChartMath.insetLine(position = 40f, stroke = 2f, extent = 100f))
    }

    @Test
    fun `a plot thinner than the stroke centres the line`() {
        assertEquals(0.5f, BarChartMath.insetLine(position = 0f, stroke = 2f, extent = 1f))
    }

    @Test
    fun `bar labels are shown only when a bar has a non-blank label`() {
        val unlabelled = listOf(BarChartGroup(label = "Q1", bars = listOf(BarChartBar(1f, label = " "))))
        val labelled = listOf(BarChartGroup(bars = listOf(BarChartBar(1f), BarChartBar(2f, label = "B"))))

        assertFalse(BarChartMath.hasBarLabels(unlabelled))
        assertTrue(BarChartMath.hasBarLabels(labelled))
    }

    @Test
    fun `group labels are shown only when a group has a non-blank label`() {
        val unlabelled = listOf(BarChartGroup(label = "", bars = listOf(BarChartBar(1f, label = "A"))))
        val labelled = listOf(BarChartGroup(label = "Q1", bars = listOf(BarChartBar(1f))))

        assertFalse(BarChartMath.hasGroupLabels(unlabelled))
        assertTrue(BarChartMath.hasGroupLabels(labelled))
    }

    @Test
    fun `the description reads group labels and bar labels and values`() {
        val groups = listOf(
            BarChartGroup(
                label = "Q1",
                bars = listOf(BarChartBar(42f, label = "2025"), BarChartBar(28.5f, label = "2024")),
            ),
            BarChartGroup(label = null, bars = listOf(BarChartBar(7f))),
        )

        val description = BarChartMath.description(groups, BarChartBar.Companion::formatValue)

        assertEquals("Q1: 2025 42, 2024 28.5; 7", description)
    }

    @Test
    fun `the description leaves out non-finite values and blank labels`() {
        val groups = listOf(
            BarChartGroup(
                label = " ",
                bars = listOf(BarChartBar(Float.NaN, label = "Gone"), BarChartBar(3f, label = "")),
            ),
        )

        assertEquals("3", BarChartMath.description(groups, BarChartBar.Companion::formatValue))
    }

    @Test
    fun `the description uses the caller's formatter`() {
        val groups = BarChartGroup.singleBars(listOf(BarChartBar(0.25f, label = "Jan")))

        assertEquals("Jan 25 %", BarChartMath.description(groups) { "${(it * 100).toInt()} %" })
    }

    @Test
    fun `whole values format without a fractional part`() {
        assertEquals("42", BarChartBar.formatValue(42f))
        assertEquals("-3", BarChartBar.formatValue(-3f))
        assertEquals("0", BarChartBar.formatValue(-0f))
    }

    /**
     * Asserts that [actual] matches [expected] to within a thousandth of a pixel.
     *
     * @param expected Span the bar should take.
     * @param actual Span the bar took, or `null` when no bar was drawn, which fails the assertion.
     */
    private fun assertSpan(expected: BarSpan, actual: BarSpan?) {
        assertEquals(expected.top, actual?.top ?: Float.NaN, absoluteTolerance = 0.001f)
        assertEquals(expected.height, actual?.height ?: Float.NaN, absoluteTolerance = 0.001f)
    }

    /**
     * [BarChartMath.barSpan] on a plot 100 pixels tall.
     *
     * @param scale Scale the chart plots.
     * @param value Bar value.
     * @param minBarHeight Smallest height of a bar off the baseline, in pixels.
     * @return Extent of the bar, or `null` when no bar is drawn.
     */
    private fun barSpan(scale: BarChartScale, value: Float, minBarHeight: Float = 0f): BarSpan? =
        BarChartMath.barSpan(scale, value, plotHeight = 100f, minBarHeight = minBarHeight)

    /**
     * [BarChartMath.layout] with 10-pixel bar gaps, 20-pixel group gaps, a 12-pixel unbounded bar
     * width, and [density].
     *
     * @param groupSizes Number of bars in each group, in order.
     * @param barWidth How wide each bar is.
     * @param alignment Where the bars sit when they do not fill the plot.
     * @param plotWidth Width of the plot in pixels, or [Float.POSITIVE_INFINITY] when unbounded.
     * @return Placement of the bars.
     */
    private fun layout(
        groupSizes: List<Int>,
        barWidth: BarChartBarWidth,
        alignment: BarChartHorizontalAlignment = BarChartHorizontalAlignment.Center,
        plotWidth: Float = 200f,
    ): BarChartLayout = BarChartMath.layout(
        groupSizes = groupSizes,
        plotWidth = plotWidth,
        barGap = 10f,
        groupGap = 20f,
        barWidth = barWidth,
        alignment = alignment,
        unboundedBarWidth = 12f,
        density = density,
    )
}
