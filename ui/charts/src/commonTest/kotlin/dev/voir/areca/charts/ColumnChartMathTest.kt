package dev.voir.areca.charts

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** Tests for [ColumnChartMath], the scale, stacking and scrolling rules behind [ColumnChart]. */
class ColumnChartMathTest {

    private fun column(vararg bars: ColumnChartBar) = ColumnChartColumn(key = 0, bars = bars.toList(), label = "")

    @Test
    fun `the top is the tallest bar - a stack counted whole`() {
        val columns = listOf(
            column(ColumnChartBar.of(40f)),
            column(ColumnChartBar(listOf(ColumnChartSegment(30f), ColumnChartSegment(25f)))),
        )

        assertEquals(55f, ColumnChartMath.top(columns, maxValue = null))
    }

    @Test
    fun `a stated top wins - and a useless one falls back`() {
        val columns = listOf(column(ColumnChartBar.of(40f)))

        assertEquals(100f, ColumnChartMath.top(columns, maxValue = 100f))
        assertEquals(40f, ColumnChartMath.top(columns, maxValue = Float.NaN))
        assertEquals(1f, ColumnChartMath.top(listOf(column(ColumnChartBar.of(0f))), maxValue = null))
    }

    @Test
    fun `negative and non-finite segments draw nothing and count for nothing`() {
        val bar = ColumnChartBar(
            listOf(ColumnChartSegment(-5f), ColumnChartSegment(Float.NaN), ColumnChartSegment(10f)),
        )

        val spans = ColumnChartMath.segments(bar, top = 10f, area = 100f, minHeight = 0f, gap = 0f)

        assertEquals(1, spans.size)
        assertEquals(2, spans.single().index)
        assertEquals(100f, spans.single().height)
        assertTrue(spans.single().isTop)
    }

    @Test
    fun `stacked segments share the bar's height - the gaps coming out of the upper ones`() {
        val bar = ColumnChartBar(listOf(ColumnChartSegment(30f), ColumnChartSegment(10f)))

        val spans = ColumnChartMath.segments(bar, top = 40f, area = 80f, minHeight = 0f, gap = 2f)

        assertEquals(ColumnSegmentSpan(index = 0, bottom = 0f, height = 60f, isTop = false), spans[0])
        assertEquals(ColumnSegmentSpan(index = 1, bottom = 62f, height = 18f, isTop = true), spans[1])
    }

    @Test
    fun `a small positive bar keeps its least height and a big one stops at the area`() {
        assertEquals(3f, ColumnChartMath.barHeight(ColumnChartBar.of(0.01f), top = 100f, area = 100f, minHeight = 3f))
        assertEquals(100f, ColumnChartMath.barHeight(ColumnChartBar.of(500f), top = 100f, area = 100f, minHeight = 3f))
        assertEquals(0f, ColumnChartMath.barHeight(ColumnChartBar.of(0f), top = 100f, area = 100f, minHeight = 3f))
    }

    @Test
    fun `a reference line above the top or below zero is not drawn`() {
        assertEquals(50f, ColumnChartMath.lineHeight(5f, top = 10f, area = 100f))
        assertNull(ColumnChartMath.lineHeight(11f, top = 10f, area = 100f))
        assertNull(ColumnChartMath.lineHeight(-1f, top = 10f, area = 100f))
    }

    @Test
    fun `a column's bars and companions are measured side by side`() {
        val bars = listOf(ColumnChartBar.of(1f), ColumnChartBar.companion(1f))

        assertEquals(19f + 4f + 7f, ColumnChartMath.groupWidth(bars, barWidth = 19f, companionWidth = 7f, gap = 4f))
        assertEquals(0f, ColumnChartMath.groupWidth(emptyList(), barWidth = 19f, companionWidth = 7f, gap = 4f))
    }

    @Test
    fun `revealing a column scrolls by as little as it takes`() {
        assertNull(ColumnChartMath.reveal(start = 100, end = 160, scroll = 50, viewport = 300, max = 1000))
        assertEquals(20, ColumnChartMath.reveal(start = 20, end = 80, scroll = 50, viewport = 300, max = 1000))
        assertEquals(160, ColumnChartMath.reveal(start = 400, end = 460, scroll = 50, viewport = 300, max = 1000))
        assertEquals(100, ColumnChartMath.reveal(start = 1300, end = 1360, scroll = 0, viewport = 300, max = 100))
    }

    @Test
    fun `a reference line above every bar raises the automatic top`() {
        val columns = listOf(column(ColumnChartBar.of(2500f)))

        assertEquals(3000f, ColumnChartMath.top(columns, maxValue = null, lines = listOf(ColumnChartReferenceLine(3000f))))
        assertEquals(2500f, ColumnChartMath.top(columns, maxValue = null, lines = listOf(ColumnChartReferenceLine(Float.NaN))))
    }

    @Test
    fun `bars wider than their column shrink to fit it`() {
        assertEquals(1f, ColumnChartMath.fit(groupWidth = 40f, width = 58f))
        assertEquals(0.5f, ColumnChartMath.fit(groupWidth = 44f, width = 22f))
    }

    @Test
    fun `the visible columns are those the window overlaps`() {
        // Columns 58 wide every 62, after 16 of padding; a 300-wide window.
        assertEquals(0..4, ColumnChartMath.visibleColumns(scroll = 0, viewport = 300, leadingPadding = 16, stride = 62, columnWidth = 58, count = 12))
        assertEquals(1..5, ColumnChartMath.visibleColumns(scroll = 80, viewport = 300, leadingPadding = 16, stride = 62, columnWidth = 58, count = 12))
        assertEquals(7..11, ColumnChartMath.visibleColumns(scroll = 460, viewport = 300, leadingPadding = 16, stride = 62, columnWidth = 58, count = 12))
    }

    @Test
    fun `a column reads its own description or its labels`() {
        val labelled = ColumnChartColumn(key = 0, bars = emptyList(), label = "Oct", sublabel = "so far", valueLabel = "€2,340")

        assertEquals("Oct, so far, €2,340", ColumnChartMath.description(labelled))
        assertEquals("October", ColumnChartMath.description(labelled.copy(contentDescription = "October")))
    }
}
