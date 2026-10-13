package dev.voir.areca.crop

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import dev.voir.areca.theme.ArecaTheme

/**
 * Colors of a [CropDial].
 *
 * @property tick Tick marks. Major ticks draw at full strength, minor ones at half.
 * @property origin Dot above the tick of the dial's origin — level, or no zoom.
 * @property indicator Fixed centre mark the ticks slide under; it reads the current value.
 * @property focusRing Color of the focus ring foundation's indications draw around the dial while it
 *   has keyboard focus.
 */
@Immutable
public data class CropDialStyle(
    public val tick: Color,
    public val origin: Color,
    public val indicator: Color,
    public val focusRing: Color,
) {
    /** Presets. */
    public companion object {
        /** Muted ticks, a light origin dot and an accent indicator, in the dark colors the cropper always uses. */
        public val Default: CropDialStyle
            @Composable
            get() = default()

        /**
         * Builds a style from the current theme's dark colors.
         *
         * @param tick Tick marks.
         * @param origin Origin dot.
         * @param indicator Centre mark.
         * @param focusRing Keyboard focus ring.
         * @return The style.
         */
        @Composable
        public fun default(
            tick: Color = ArecaTheme.darkColors.fgMuted,
            origin: Color = ArecaTheme.darkColors.fgBase,
            indicator: Color = ArecaTheme.darkColors.accentBase,
            focusRing: Color = ArecaTheme.darkColors.accentBase,
        ): CropDialStyle = CropDialStyle(tick = tick, origin = origin, indicator = indicator, focusRing = focusRing)
    }
}

/**
 * Default [CropDialStyle] in scope, or `null` to use [CropDialStyle.Default].
 *
 * A [CropDial] of your own reads it, and so does [CropToolbarStyle.dial]'s default, so one provider
 * styles the toolbar's dial too.
 *
 * Not seeded by [dev.voir.areca.theme.ArecaThemeProvider] — see [LocalImageCropperStyle] for why.
 */
public val LocalCropDialStyle: ProvidableCompositionLocal<CropDialStyle?> = compositionLocalOf { null }
