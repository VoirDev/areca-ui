package dev.voir.areca.media.image

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import dev.voir.areca.theme.ArecaTheme

/**
 * Metrics of a [MediaFrame] and [MediaImage].
 *
 * @property inset Space between the frame's edge and a picture drawn [MediaFit.Whole], so a product
 *   never touches the corners. A [MediaFit.Fill] picture ignores it.
 * @property failureGlyph Side of the failed face's glyph.
 * @property compactBelow Narrowest frame whose failed face carries its words and retry button;
 *   below it, a thumbnail, the glyph alone says it.
 */
@Immutable
public data class MediaFrameSize(
    public val inset: Dp,
    public val failureGlyph: Dp,
    public val compactBelow: Dp,
) {
    /** The theme's default size, its presets, and a builder for overriding it. */
    public companion object {
        /** Theme default: the picture to the frame's edges. */
        public val Default: MediaFrameSize
            @Composable
            get() = default()

        /** A product standing whole on its fill, clear of the corners. */
        public val Product: MediaFrameSize
            @Composable
            get() = default(inset = ArecaTheme.spacing.xl3)

        /**
         * Builds a size from the current theme.
         *
         * @param inset Space between the frame's edge and a whole picture.
         * @param failureGlyph Side of the failed face's glyph.
         * @param compactBelow Narrowest frame whose failed face carries words.
         * @return Size for a frame.
         */
        @Composable
        public fun default(
            inset: Dp = ArecaTheme.spacing.none,
            failureGlyph: Dp = ArecaTheme.spacing.xl3,
            compactBelow: Dp = ArecaTheme.spacing.xl9 + ArecaTheme.spacing.xl6,
        ): MediaFrameSize = MediaFrameSize(inset = inset, failureGlyph = failureGlyph, compactBelow = compactBelow)
    }
}

/** Default [MediaFrameSize] in scope, or `null` for [MediaFrameSize.Default]. Not seeded by the theme. */
public val LocalMediaFrameSize: ProvidableCompositionLocal<MediaFrameSize?> = compositionLocalOf { null }
