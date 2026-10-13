package dev.voir.areca.crop

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import dev.voir.areca.theme.ArecaTheme

/**
 * Colors of an [ImageCropper].
 *
 * The veil is what tells the kept part of the photo from the cut-away part. It lifts while the user
 * works, from [veil] to [veilActive], so they can see what they are about to cut.
 *
 * @property stage Ground behind the photo, seen where the photo does not reach.
 * @property veil Wash over the part of the photo outside the frame, at rest.
 * @property veilActive The same wash while the user is adjusting.
 * @property frame Outline of the crop shape, and of the frame's bounding box when the shape is not a
 *   rectangle.
 * @property handle The frame's corner and edge grips.
 * @property grid Guide lines drawn inside the frame while adjusting. Drawn over the photo only, so a
 *   light line reads on almost any photo.
 * @property focusRing Color of the focus ring foundation's indications draw, here just inside the
 *   stage's edge, while the stage has keyboard focus.
 */
@Immutable
public data class ImageCropperStyle(
    public val stage: Color,
    public val veil: Color,
    public val veilActive: Color,
    public val frame: Color,
    public val handle: Color,
    public val grid: Color,
    public val focusRing: Color,
) {
    /** Presets. */
    public companion object {
        /**
         * The cropper's look: the photo on black, a black veil outside the frame and a light outline.
         * Dark whatever the application's theme — the convention of photo editors, where a neutral
         * dark surround keeps colors judged fairly. Paint the crop screen with [stage] so the system
         * bars' regions match.
         */
        public val Default: ImageCropperStyle
            @Composable
            get() = default()

        /**
         * Builds a style from the current theme's dark colors.
         *
         * @param stage Ground behind the photo.
         * @param veil Wash outside the frame at rest.
         * @param veilActive Wash outside the frame while adjusting.
         * @param frame Outline of the crop shape.
         * @param handle Frame grips.
         * @param grid Guide lines inside the frame.
         * @param focusRing Ring shown while the stage has keyboard focus.
         * @return The style.
         */
        @Composable
        public fun default(
            stage: Color = ArecaTheme.baseColors.baseBlack,
            veil: Color = ArecaTheme.baseColors.baseBlack.copy(alpha = 0.62f),
            veilActive: Color = ArecaTheme.baseColors.baseBlack.copy(alpha = 0.38f),
            frame: Color = ArecaTheme.darkColors.fgBase,
            handle: Color = frame,
            grid: Color = ArecaTheme.baseColors.baseWhite.copy(alpha = 0.7f),
            focusRing: Color = ArecaTheme.darkColors.accentBase,
        ): ImageCropperStyle = ImageCropperStyle(
            stage = stage,
            veil = veil,
            veilActive = veilActive,
            frame = frame,
            handle = handle,
            grid = grid,
            focusRing = focusRing,
        )
    }
}

/**
 * Default [ImageCropperStyle] in scope, or `null` to use [ImageCropperStyle.Default].
 *
 * [ImageCropper] reads it, and so do the defaults that follow the stage's ground: [CropDialog]'s
 * color and [CropToolbarStyle.bg].
 *
 * Unlike foundation's component defaults this is **not** seeded by
 * [dev.voir.areca.theme.ArecaThemeProvider]: that provider lives in `:foundation`, and `:foundation`
 * does not depend on `:crop`. Provide it yourself around a subtree that wants a different default:
 *
 * ```kotlin
 * CompositionLocalProvider(LocalImageCropperStyle provides ImageCropperStyle.default(veil = …)) { … }
 * ```
 */
public val LocalImageCropperStyle: ProvidableCompositionLocal<ImageCropperStyle?> = compositionLocalOf { null }
