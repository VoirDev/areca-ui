package dev.voir.areca.crop

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Metrics of an [ImageCropper].
 *
 * @property inset Space kept between the stage's edge and the largest frame. Leaves room for the grips
 *   outside the frame and for a finger to reach them.
 * @property frameStroke Width of the crop outline.
 * @property gridStroke Width of the guide lines.
 * @property handleLength Length of each arm of a corner grip, and of an edge grip.
 * @property handleThickness Thickness of a grip.
 * @property handleTouchRadius How far from a grip a press still takes it.
 * @property minFrame Smallest side a frame can be dragged down to.
 * @property focusRingWidth Width of the focus ring drawn just inside the stage's edge while it has
 *   keyboard focus.
 */
@Immutable
public data class ImageCropperSize(
    public val inset: Dp = 24.dp,
    public val frameStroke: Dp = 1.5.dp,
    public val gridStroke: Dp = 1.dp,
    public val handleLength: Dp = 20.dp,
    public val handleThickness: Dp = 3.dp,
    public val handleTouchRadius: Dp = 28.dp,
    public val minFrame: Dp = 72.dp,
    public val focusRingWidth: Dp = 3.dp,
) {
    /** Presets. */
    public companion object {
        /** The standard metrics. */
        public val Default: ImageCropperSize = ImageCropperSize()
    }
}

/**
 * Default [ImageCropperSize] in scope, or `null` to use [ImageCropperSize.Default].
 *
 * Not seeded by [dev.voir.areca.theme.ArecaThemeProvider] — see [LocalImageCropperStyle] for why.
 */
public val LocalImageCropperSize: ProvidableCompositionLocal<ImageCropperSize?> = compositionLocalOf { null }
