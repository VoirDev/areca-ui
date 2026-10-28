package dev.voir.areca.media.image

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import dev.voir.areca.theme.ArecaTheme

/**
 * Colors and shape of a [MediaFrame] and [MediaImage].
 *
 * @property bg The frame's fill: the ground a whole picture stands on, and what shows while it loads.
 * @property shape The frame's shape; the picture, its faces and its scrim are clipped to it.
 * @property content Glyph and words of the failed face.
 * @property scrim Darkest step of a [MediaScrim] gradient.
 */
@Immutable
public data class MediaFrameStyle(
    public val bg: Color,
    public val shape: Shape,
    public val content: Color,
    public val scrim: Color,
) {
    /** The theme's default style, its presets, and a builder for overriding it. */
    public companion object {
        /** Theme default: the subtle fill under the medium radius. */
        public val Default: MediaFrameStyle
            @Composable
            get() = default()

        /**
         * Builds a style from the current theme.
         *
         * @param bg The frame's fill.
         * @param shape The frame's shape.
         * @param content Glyph and words of the failed face.
         * @param scrim Darkest step of a scrim.
         * @return Style for a frame.
         */
        @Composable
        public fun default(
            bg: Color = ArecaTheme.colors.bgSubtle,
            shape: Shape = ArecaTheme.shapes.md,
            content: Color = ArecaTheme.colors.fgMuted,
            scrim: Color = ArecaTheme.baseColors.baseBlack.copy(alpha = SCRIM_ALPHA),
        ): MediaFrameStyle = MediaFrameStyle(bg = bg, shape = shape, content = content, scrim = scrim)

        /**
         * A round frame — a recently viewed product, a person's photo.
         *
         * @param bg The frame's fill.
         * @return Style for a round frame.
         */
        @Composable
        public fun circle(bg: Color = ArecaTheme.colors.bgSubtle): MediaFrameStyle = default(bg = bg, shape = CircleShape)

        /**
         * A frame with no fill or corners, for a picture laid on a ground of its own — the viewer's.
         *
         * @param content Glyph and words of the failed face, light on a dark ground.
         * @return Style for a bare frame.
         */
        @Composable
        public fun bare(content: Color = ArecaTheme.colors.fgMuted): MediaFrameStyle =
            default(bg = Color.Transparent, shape = ArecaTheme.shapes.none, content = content)
    }
}

/** Default [MediaFrameStyle] in scope, or `null` for [MediaFrameStyle.Default]. Not seeded by the theme. */
public val LocalMediaFrameStyle: ProvidableCompositionLocal<MediaFrameStyle?> = compositionLocalOf { null }

/** How dark a scrim's darkest step is: enough for white text over a white shot. */
private const val SCRIM_ALPHA = 0.55f
