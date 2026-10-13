package dev.voir.areca.crop

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Metrics of a [CropDial].
 *
 * @property height Height of the dial, and so of its touch target.
 * @property tickSpacing Distance between neighbouring ticks; dragging one spacing moves the value one
 *   tick step.
 * @property minorTick Length of a minor tick.
 * @property majorTick Length of a major tick.
 * @property tickWidth Width of every tick.
 * @property indicatorLength Length of the centre mark.
 * @property indicatorWidth Width of the centre mark.
 * @property originRadius Radius of the origin dot.
 */
@Immutable
public data class CropDialSize(
    public val height: Dp = 48.dp,
    public val tickSpacing: Dp = 7.dp,
    public val minorTick: Dp = 8.dp,
    public val majorTick: Dp = 16.dp,
    public val tickWidth: Dp = 1.dp,
    public val indicatorLength: Dp = 30.dp,
    public val indicatorWidth: Dp = 2.dp,
    public val originRadius: Dp = 2.5.dp,
) {
    /** Presets. */
    public companion object {
        /** The standard metrics. */
        public val Default: CropDialSize = CropDialSize()
    }
}

/**
 * Default [CropDialSize] in scope, or `null` to use [CropDialSize.Default].
 *
 * A [CropDial] of your own reads it, and so does [CropToolbarSize.dial]'s default.
 *
 * Not seeded by [dev.voir.areca.theme.ArecaThemeProvider] — see [LocalImageCropperStyle] for why.
 */
public val LocalCropDialSize: ProvidableCompositionLocal<CropDialSize?> = compositionLocalOf { null }
