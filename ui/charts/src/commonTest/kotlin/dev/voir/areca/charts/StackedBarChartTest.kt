package dev.voir.areca.charts

import kotlin.math.roundToInt
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Tests for [stackedBarShares] and [stackedBarDescription], how a [StackedBarChart] sizes its
 * segments and what it reads aloud.
 */
class StackedBarChartTest {
    /** Formats a share as a whole percentage, as the chart's default formatter does. */
    private val percent: (Float) -> String = { "${(it * 100).roundToInt()}%" }

    @Test
    fun `shares summing to at most one are kept as given`() {
        val segments = listOf(StackedBarSegment(0.5f), StackedBarSegment(0.3f))

        assertEquals(listOf(0.5f, 0.3f), stackedBarShares(segments))
    }

    @Test
    fun `shares summing above one are scaled down to fill the bar`() {
        val segments = listOf(StackedBarSegment(0.75f), StackedBarSegment(0.75f))

        assertEquals(listOf(0.5f, 0.5f), stackedBarShares(segments))
    }

    @Test
    fun `negative and non-finite fractions take no share and do not offset the others`() {
        val segments = listOf(
            StackedBarSegment(0.8f),
            StackedBarSegment(-0.5f),
            StackedBarSegment(Float.NaN),
            StackedBarSegment(Float.POSITIVE_INFINITY),
            StackedBarSegment(0.6f),
        )

        val shares = stackedBarShares(segments)

        assertEquals(listOf(0f, 0f, 0f), shares.subList(1, 4))
        assertEquals(0.8f / 1.4f, shares[0], absoluteTolerance = 0.0001f)
        assertEquals(0.6f / 1.4f, shares[4], absoluteTolerance = 0.0001f)
    }

    @Test
    fun `each segment reads as its label and share`() {
        val segments = listOf(
            StackedBarSegment(fraction = 0.42f, label = "Photos"),
            StackedBarSegment(fraction = 0.25f, label = "Apps"),
        )

        assertEquals("Photos 42%, Apps 25%", describe(segments))
    }

    @Test
    fun `an unlabelled segment reads its share alone`() {
        assertEquals("50%", describe(listOf(StackedBarSegment(fraction = 0.5f))))
    }

    @Test
    fun `segments the chart does not draw are left out`() {
        val segments = listOf(
            StackedBarSegment(fraction = 0f, label = "Empty"),
            StackedBarSegment(fraction = -0.2f, label = "Negative"),
            StackedBarSegment(fraction = Float.NaN, label = "Unknown"),
            StackedBarSegment(fraction = 0.3f, label = "Mail"),
        )

        assertEquals("Mail 30%", describe(segments))
    }

    @Test
    fun `the description reads the scaled shares the bar draws`() {
        val segments = listOf(
            StackedBarSegment(fraction = 0.8f, label = "Photos"),
            StackedBarSegment(fraction = -0.4f, label = "Refund"),
            StackedBarSegment(fraction = 0.8f, label = "Apps"),
        )

        assertEquals("Photos 50%, Apps 50%", describe(segments))
    }

    @Test
    fun `no drawn segment gives an empty description`() {
        assertEquals("", describe(emptyList()))
    }

    /**
     * Describes [segments] at the shares the chart draws them.
     *
     * @param segments Segments to describe.
     * @return The generated description.
     */
    private fun describe(segments: List<StackedBarSegment>): String =
        stackedBarDescription(segments, stackedBarShares(segments), percent)
}
