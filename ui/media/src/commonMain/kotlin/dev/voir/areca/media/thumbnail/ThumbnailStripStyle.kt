package dev.voir.areca.media.thumbnail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import dev.voir.areca.theme.ArecaTheme

/**
 * Look of a [ThumbnailStrip].
 *
 * @property ring Color of the ring around the selected thumbnail.
 * @property shape Shape of every thumbnail and its ring.
 * @property otherAlpha Opacity of the thumbnails not selected, so the selected one leads without its
 *   ring too.
 */
@Immutable
public data class ThumbnailStripStyle(
    public val ring: Color,
    public val shape: Shape,
    public val otherAlpha: Float,
) {
    /** The theme's default style, its presets, and a builder for overriding it. */
    public companion object {
        /** Theme default: the foreground ring, for a strip on the page. */
        public val Default: ThumbnailStripStyle
            @Composable
            get() = default()

        /**
         * Builds a style from the current theme.
         *
         * @param ring Color of the ring.
         * @param shape Shape of the thumbnails.
         * @param otherAlpha Opacity of the thumbnails not selected.
         * @return Style for a strip.
         */
        @Composable
        public fun default(
            ring: Color = ArecaTheme.colors.fgBase,
            shape: Shape = ArecaTheme.shapes.md,
            otherAlpha: Float = OTHER_ALPHA,
        ): ThumbnailStripStyle = ThumbnailStripStyle(ring = ring, shape = shape, otherAlpha = otherAlpha)

        /**
         * A strip over a dark viewer, ringed in white whatever the theme.
         *
         * @return Style for a strip on a dark ground.
         */
        @Composable
        public fun onDark(): ThumbnailStripStyle = default(ring = ArecaTheme.baseColors.baseWhite)

        /**
         * A strip over a light viewer, ringed in black whatever the theme.
         *
         * @return Style for a strip on a light ground.
         */
        @Composable
        public fun onLight(): ThumbnailStripStyle = default(ring = ArecaTheme.baseColors.baseBlack)
    }
}

/** Default [ThumbnailStripStyle] in scope, or `null` for [ThumbnailStripStyle.Default]. Not seeded by the theme. */
public val LocalThumbnailStripStyle: ProvidableCompositionLocal<ThumbnailStripStyle?> = compositionLocalOf { null }

/** How faint the thumbnails not selected are. */
private const val OTHER_ALPHA = 0.6f
