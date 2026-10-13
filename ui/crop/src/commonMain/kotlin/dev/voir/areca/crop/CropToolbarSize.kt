package dev.voir.areca.crop

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.modifier.MinimumTouchTarget

/**
 * Metrics of a [CropToolbar].
 *
 * The bar's buttons are foundation components and follow their own sizes; this covers what the
 * toolbar lays out itself.
 *
 * @property optionsHeight Height of the slot the option row and the open tool take turns in.
 * @property optionWidth Width of one option, its touch target included.
 * @property optionCircle Diameter of an option's circle.
 * @property optionIcon Size of an option's icon.
 * @property changedDot Diameter of the dot on an option whose value has left its start.
 * @property changedDotInset Inset of the changed dot from the circle's box, which puts it on the
 *   circle's edge.
 * @property separatorWidth Width of the rule after Reset.
 * @property separatorHeight Height of the rule after Reset.
 * @property chipHeight Height of an aspect chip as drawn.
 * @property chipTouchTarget Smallest height an aspect chip occupies in layout; the chip is centred in
 *   it, and the band around it still answers a touch.
 * @property chipBorder Outline width of an aspect chip.
 * @property chipGlyph Side of the box an aspect chip's glyph is drawn in.
 * @property chipGlyphStroke Stroke of an aspect chip's glyph.
 * @property chipGlyphCorner Corner radius of an aspect chip's glyph.
 * @property dial Metrics of the Angle and Zoom dials.
 */
@Immutable
public data class CropToolbarSize(
    public val optionsHeight: Dp = 104.dp,
    public val optionWidth: Dp = 56.dp,
    public val optionCircle: Dp = 44.dp,
    public val optionIcon: Dp = 22.dp,
    public val changedDot: Dp = 8.dp,
    public val changedDotInset: Dp = 2.5.dp,
    public val separatorWidth: Dp = 1.dp,
    public val separatorHeight: Dp = 32.dp,
    public val chipHeight: Dp = 36.dp,
    public val chipTouchTarget: Dp = MinimumTouchTarget,
    public val chipBorder: Dp = 1.dp,
    public val chipGlyph: Dp = 14.dp,
    public val chipGlyphStroke: Dp = 1.5.dp,
    public val chipGlyphCorner: Dp = 2.dp,
    public val dial: CropDialSize = CropDialSize.Default,
) {
    /** Presets. */
    public companion object {
        /** The standard metrics, with the dial's from [LocalCropDialSize] when one is provided. */
        public val Default: CropToolbarSize
            @Composable
            get() = CropToolbarSize(dial = LocalCropDialSize.current ?: CropDialSize.Default)
    }
}

/**
 * Default [CropToolbarSize] in scope, or `null` to use [CropToolbarSize.Default].
 *
 * Not seeded by [dev.voir.areca.theme.ArecaThemeProvider] — see [LocalImageCropperStyle] for why.
 */
public val LocalCropToolbarSize: ProvidableCompositionLocal<CropToolbarSize?> = compositionLocalOf { null }
