package dev.voir.areca.crop

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape

/**
 * Everything one use of the cropper decides, in one value: an avatar, a cover, a photo post.
 *
 * Pass it to [rememberImageCropState]; [ImageCropper], [CropToolbar] and [ImageCropState.render]
 * then read the mask, the aspect choices and the output size from the state, so none of them is
 * repeated. An application defines one spec per place it crops — usually as a top-level value, or a
 * `copy` of a preset — and nothing about cropping lives in the screens that lead to it.
 *
 * Specs compare by value, and a spec that compares unequal starts a fresh crop. Define a spec once
 * rather than building it during composition, and give a custom [mask] a stable instance: a
 * `GenericShape` built inline is a new, unequal shape every time.
 *
 * @property aspect Proportions the frame starts with, and returns to on reset.
 * @property isAspectLocked Whether the frame keeps [aspect] whatever happens: rotating turns the photo
 *   under an unchanging frame, and the toolbar offers no aspect choices.
 * @property isResizable Whether the frame has grips. Without them the frame always fills the stage and
 *   the photo is framed by moving and zooming it.
 * @property mask Shape the frame cuts: drawn by the stage, and applied by [ImageCropState.render].
 * @property aspects Aspect choices the toolbar offers, in order. Empty, or a locked aspect, offers none.
 * @property maxZoom Largest magnification over the size at which the photo just covers the frame.
 * @property maxOutputDimension Longest side of the rendered result in pixels, or `null` to keep the
 *   source's full detail. The result is never scaled up to reach it.
 */
@Immutable
public data class CropSpec(
    public val aspect: CropAspect = CropAspect.Free,
    public val isAspectLocked: Boolean = false,
    public val isResizable: Boolean = true,
    public val mask: Shape = RectangleShape,
    public val aspects: List<CropAspect> = emptyList(),
    public val maxZoom: Float = 10f,
    public val maxOutputDimension: Int? = null,
) {
    init {
        require(maxZoom >= 1f) { "maxZoom must be at least 1, was $maxZoom." }
        require(maxOutputDimension == null || maxOutputDimension > 0) {
            "maxOutputDimension must be positive, was $maxOutputDimension."
        }
    }

    /** Presets for the common places an application crops. */
    public companion object {
        /**
         * A general photo editor: any proportions, the common ratios on chips, a frame with grips and
         * the photo's full detail.
         */
        public val Photo: CropSpec = CropSpec(aspects = CropAspect.PhotoPresets)

        /**
         * A profile picture: a fixed circle the photo is moved and zoomed under, rendered at 512 px.
         *
         * `copy(mask = …)` for a product whose avatars are rounded squares or squircles.
         */
        public val Avatar: CropSpec = CropSpec(
            aspect = CropAspect.Square,
            isAspectLocked = true,
            isResizable = false,
            mask = CropShapes.Oval,
            maxOutputDimension = 512,
        )
    }
}
