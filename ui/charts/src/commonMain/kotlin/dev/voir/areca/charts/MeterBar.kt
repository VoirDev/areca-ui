package dev.voir.areca.charts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.LayoutDirection

/**
 * A horizontal bar showing one amount against a track, with an optional marker for a reference —
 * a usual amount, a target, last month's figure.
 *
 * Not a progress indicator: it reads [contentDescription] and nothing else, with no progress
 * semantics, because what it measures is an amount, not work being done. Use
 * [dev.voir.areca.foundation.feedback.ProgressBar] for progress.
 *
 * The fill runs from the start edge to [fraction] of the track, and the marker sits at [marker],
 * both clamped to `0f..1f`; a non-finite value draws no fill or no marker. The bar mirrors in a
 * right-to-left layout. The marker overhangs the bar by [MeterBarSize.markerOverhang] above and
 * below without taking space, so leave at least that much room around the bar.
 *
 * Requires `ArecaThemeProvider` above it.
 *
 * @param fraction Share of the track the fill covers, `0f..1f`.
 * @param modifier Modifier applied to the bar, which fills the width it is given.
 * @param marker Where the reference marker sits, `0f..1f`, or `null` for no marker.
 * @param color Fill of the bar, or `null` for the style's [MeterBarStyle.fill].
 * @param style Track, fill and marker colours. Defaults to [LocalMeterBarStyle], or
 *   [MeterBarStyle.Default].
 * @param size Thickness and marker metrics. Defaults to [LocalMeterBarSize], or
 *   [MeterBarSize.Medium].
 * @param contentDescription What assistive technology reads for the bar — "€612, usual €550" — or
 *   `null` when adjacent text already says it.
 */
@Composable
public fun MeterBar(
    fraction: Float,
    modifier: Modifier = Modifier,
    marker: Float? = null,
    color: Color? = null,
    style: MeterBarStyle = LocalMeterBarStyle.current ?: MeterBarStyle.Default,
    size: MeterBarSize = LocalMeterBarSize.current ?: MeterBarSize.Medium,
    contentDescription: String? = null,
) {
    val fill = color ?: style.fill
    Canvas(
        modifier = modifier
            .clearAndSetSemantics {
                if (contentDescription != null) this.contentDescription = contentDescription
            }
            .fillMaxWidth()
            .height(size.height),
    ) {
        val width = this.size.width
        val height = this.size.height
        val rtl = layoutDirection == LayoutDirection.Rtl
        val radius = CornerRadius(height / 2f, height / 2f)

        drawRoundRect(color = style.track, size = Size(width, height), cornerRadius = radius)

        val filled = meterFraction(fraction)
        if (filled != null && filled > 0f) {
            val fillWidth = (width * filled).coerceAtLeast(height.coerceAtMost(width))
            drawRoundRect(
                color = fill,
                topLeft = Offset(if (rtl) width - fillWidth else 0f, 0f),
                size = Size(fillWidth, height),
                cornerRadius = radius,
            )
        }

        val at = marker?.let(::meterFraction)
        if (at != null) {
            val markerWidth = size.markerWidth.toPx()
            val overhang = size.markerOverhang.toPx()
            val center = (if (rtl) 1f - at else at) * width
            val left = (center - markerWidth / 2f).coerceIn(0f, (width - markerWidth).coerceAtLeast(0f))
            drawRoundRect(
                color = style.marker,
                topLeft = Offset(left, -overhang),
                size = Size(markerWidth, height + overhang * 2f),
                cornerRadius = CornerRadius(markerWidth / 2f, markerWidth / 2f),
            )
        }
    }
}

/**
 * Clamps a [MeterBar] position into the track.
 *
 * @param value A share of the track.
 * @return [value] clamped to `0f..1f`, or `null` when it is not finite.
 */
internal fun meterFraction(value: Float): Float? = value.takeIf { it.isFinite() }?.coerceIn(0f, 1f)
