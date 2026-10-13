package dev.voir.areca.charts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import kotlin.math.roundToInt

/**
 * One segment of a [StackedBarChart].
 *
 * @property fraction Share of the whole bar, between `0f` and `1f`. The shares of all segments
 *   should sum to at most `1f`; a larger sum is scaled down to fill the bar. A share of zero, below
 *   zero, or not finite draws nothing and counts toward nothing.
 * @property color Fill of the segment, or `null` for the style's
 *   [palette][StackedBarChartStyle.palette] colour at the segment's index.
 * @property label What the segment stands for, read by assistive technology with its share, or
 *   `null` to read the share alone. Color is the only visual cue, so pair the chart with a visible
 *   legend.
 */
@Immutable
public data class StackedBarSegment(
    public val fraction: Float,
    public val color: Color? = null,
    public val label: String? = null,
)

/**
 * A single horizontal bar split into coloured segments, for part-of-whole summaries.
 *
 * Segments run from the start edge in order, so they mirror in a right-to-left layout. Each is
 * drawn at its share as [stackedBarShares] resolves it: a segment whose fraction is zero, negative,
 * or not finite is skipped, and when the remaining shares sum above `1f` they are scaled down
 * together to fill the bar. Whatever the segments leave uncovered shows the style's
 * [track][StackedBarChartStyle.track].
 *
 * Segments are told apart only by colour, so show a text legend beside the chart. Assistive
 * technology reads [contentDescription], or, when that is `null`, a description generated from the
 * drawn segments' labels and shares formatted by [fractionFormatter].
 *
 * Requires `ArecaThemeProvider` above it.
 *
 * @param segments Segments drawn from the start edge.
 * @param modifier Modifier applied to the bar.
 * @param style Track and palette. Defaults to [LocalStackedBarChartStyle], or
 *   [StackedBarChartStyle.Default].
 * @param size Height and shape of the bar. Defaults to [LocalStackedBarChartSize], or
 *   [StackedBarChartSize.Medium].
 * @param contentDescription What assistive technology reads for the chart, or `null` to generate a
 *   description of the segments. Pass one when the generated wording does not suit the locale.
 * @param fractionFormatter Turns a segment's share into text for the generated description. The
 *   default reads a whole percentage, such as `42%`, and is not localized.
 */
@Composable
public fun StackedBarChart(
    segments: List<StackedBarSegment>,
    modifier: Modifier = Modifier,
    style: StackedBarChartStyle = LocalStackedBarChartStyle.current ?: StackedBarChartStyle.Default,
    size: StackedBarChartSize = LocalStackedBarChartSize.current ?: StackedBarChartSize.Medium,
    contentDescription: String? = null,
    fractionFormatter: (Float) -> String = ::formatPercent,
) {
    val shares = remember(segments) { stackedBarShares(segments) }
    val description = remember(contentDescription, segments, shares, fractionFormatter) {
        contentDescription ?: stackedBarDescription(segments, shares, fractionFormatter)
    }

    Row(
        modifier = modifier
            .then(
                if (description.isNotEmpty()) {
                    Modifier.semantics { this.contentDescription = description }
                } else {
                    Modifier
                },
            )
            .height(size.height)
            .clip(size.shape)
            .background(style.track),
    ) {
        segments.forEachIndexed { index, segment ->
            val share = shares[index]
            if (share > 0f) {
                Box(
                    modifier = Modifier
                        .weight(share)
                        .fillMaxHeight()
                        .background(segment.color ?: style.palette[index % style.palette.size]),
                )
            }
        }

        // The uncovered remainder takes its share of the row explicitly, so the segments keep their
        // shares rather than being stretched across the whole bar by their weights.
        val remainder = 1f - shares.sum()
        if (remainder > RemainderEpsilon) {
            Box(modifier = Modifier.weight(remainder).fillMaxHeight())
        }
    }
}

/**
 * Share of a [StackedBarChart] each segment is drawn at.
 *
 * A fraction that is zero, negative, or not finite gives a share of zero and is left out of the
 * total. When the remaining fractions sum above `1f` they are scaled down together so the bar is
 * exactly full; otherwise they are kept as given.
 *
 * @param segments Segments in drawing order.
 * @return One share per segment, in the same order, each in `0f..1f` and together at most `1f`.
 */
internal fun stackedBarShares(segments: List<StackedBarSegment>): List<Float> {
    val fractions = segments.map { segment ->
        segment.fraction.takeIf { it.isFinite() && it > 0f } ?: 0f
    }
    val total = fractions.sum()
    return if (total > 1f) fractions.map { it / total } else fractions
}

/**
 * Builds the text assistive technology reads for a [StackedBarChart].
 *
 * Reads each drawn segment, in order, as its label followed by its formatted share, separated by
 * commas. Segments the chart does not draw (a share of zero) are left out, and a blank label is
 * skipped.
 *
 * @param segments Segments in drawing order.
 * @param shares Share of each segment, from [stackedBarShares].
 * @param formatFraction Turns a share into text.
 * @return Description of the segments, empty when none is drawn.
 */
internal fun stackedBarDescription(
    segments: List<StackedBarSegment>,
    shares: List<Float>,
    formatFraction: (Float) -> String,
): String = segments
    .zip(shares)
    .filter { (_, share) -> share > 0f }
    .joinToString(", ") { (segment, share) ->
        listOfNotNull(segment.label?.takeIf { it.isNotBlank() }, formatFraction(share)).joinToString(" ")
    }

/**
 * Default share text of a [StackedBarChart]: a whole percentage such as `42%`.
 *
 * @param fraction Share between `0f` and `1f`.
 * @return Share as a rounded percentage followed by `%`.
 */
private fun formatPercent(fraction: Float): String = "${(fraction * 100f).roundToInt()}%"

/**
 * Smallest uncovered share given its own slot.
 *
 * Shares scaled down to fill the bar can sum a rounding error short of `1f`; a remainder that small
 * is not a gap anyone meant.
 */
private const val RemainderEpsilon = 0.0001f
