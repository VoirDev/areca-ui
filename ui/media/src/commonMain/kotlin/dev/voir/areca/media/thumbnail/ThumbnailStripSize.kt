package dev.voir.areca.media.thumbnail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.theme.ArecaTheme

/**
 * Metrics of a [ThumbnailStrip].
 *
 * The selected thumbnail keeps its [side]; its picture draws [ringWidth] plus [ringGap] in from the
 * edge, inside its ring, so selecting one moves nothing around it.
 *
 * @property side Side of each square thumbnail.
 * @property spacing Gap between neighbouring thumbnails.
 * @property ringWidth Thickness of the selected thumbnail's ring.
 * @property ringGap Space between the ring and the picture.
 */
@Immutable
public data class ThumbnailStripSize(
    public val side: Dp,
    public val spacing: Dp,
    public val ringWidth: Dp,
    public val ringGap: Dp,
) {
    /** The theme's default size, and a builder for overriding it. */
    public companion object {
        /** Theme default: thumbnails a finger can pick. */
        public val Default: ThumbnailStripSize
            @Composable
            get() = default()

        /**
         * Builds a size from the current theme.
         *
         * @param side Side of each thumbnail.
         * @param spacing Gap between thumbnails.
         * @param ringWidth Thickness of the ring.
         * @param ringGap Space between the ring and the picture.
         * @return Size for a strip.
         */
        @Composable
        public fun default(
            side: Dp = ArecaTheme.spacing.xl7 - ArecaTheme.spacing.md,
            spacing: Dp = ArecaTheme.spacing.md,
            ringWidth: Dp = 2.dp,
            ringGap: Dp = ArecaTheme.spacing.xs2,
        ): ThumbnailStripSize = ThumbnailStripSize(side = side, spacing = spacing, ringWidth = ringWidth, ringGap = ringGap)
    }
}

/** Default [ThumbnailStripSize] in scope, or `null` for [ThumbnailStripSize.Default]. Not seeded by the theme. */
public val LocalThumbnailStripSize: ProvidableCompositionLocal<ThumbnailStripSize?> = compositionLocalOf { null }
