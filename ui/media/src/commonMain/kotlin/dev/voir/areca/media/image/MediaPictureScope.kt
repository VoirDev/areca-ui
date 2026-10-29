package dev.voir.areca.media.image

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.isUnspecified
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import dev.voir.areca.media.zoom.ZoomState
import dev.voir.areca.media.zoom.fittedSize
import kotlin.math.max
import kotlin.math.roundToInt

/**
 * Where things laid on a picture go: at points of the picture itself, not of its frame, so a marker
 * stays on the chair it names whatever the frame's shape, and follows the picture as it zooms.
 */
@Stable
public interface MediaPictureScope {
    /**
     * Centres this element on the point of the picture [x] across and [y] down, each from `0f` at
     * its left or top edge to `1f` at its right or bottom.
     *
     * The element keeps its size while the picture zooms.
     *
     * @param x Fraction of the picture's width.
     * @param y Fraction of the picture's height.
     * @return The modifier.
     */
    public fun Modifier.pinned(x: Float, y: Float): Modifier
}

/** The one [MediaPictureScope]: it only tags elements with their point. */
private object PictureScope : MediaPictureScope {
    override fun Modifier.pinned(x: Float, y: Float): Modifier = this.then(Pin(x, y))
}

/**
 * An element's point on the picture.
 *
 * @property x Fraction of the picture's width.
 * @property y Fraction of the picture's height.
 */
@Immutable
private data class Pin(val x: Float, val y: Float) : ParentDataModifier {
    override fun Density.modifyParentData(parentData: Any?): Any = this@Pin
}

/**
 * Lays [content] out on the picture drawn in this layout's bounds: each element centred on its
 * [MediaPictureScope.pinned] point, scaled back by the zoom so it keeps its size.
 *
 * @param pictureSize The picture's own size.
 * @param fit How the picture fills the bounds.
 * @param zoomState The picture's zoom, or `null` when it does not zoom here.
 * @param content The elements.
 */
@Composable
internal fun PictureOverlay(
    pictureSize: Size,
    fit: MediaFit,
    zoomState: ZoomState?,
    content: @Composable MediaPictureScope.() -> Unit,
) {
    Layout(
        content = { PictureScope.content() },
        modifier = Modifier.fillMaxSize(),
    ) { measurables, constraints ->
        val frame = Size(constraints.maxWidth.toFloat(), constraints.maxHeight.toFloat())
        val picture = pictureRect(pictureSize, frame, fit)
        val loose = Constraints(maxWidth = constraints.maxWidth, maxHeight = constraints.maxHeight)
        val placeables = measurables.map { measurable -> measurable to measurable.measure(loose) }

        layout(constraints.maxWidth, constraints.maxHeight) {
            placeables.forEach { (measurable, placeable) ->
                val pin = measurable.parentData as? Pin ?: return@forEach
                val x = picture.first + pin.x * picture.second.width - placeable.width / 2f
                val y = picture.third + pin.y * picture.second.height - placeable.height / 2f
                placeable.placeWithLayer(x.roundToInt(), y.roundToInt()) {
                    val scale = zoomState?.scale ?: 1f
                    scaleX = 1f / scale
                    scaleY = 1f / scale
                }
            }
        }
    }
}

/**
 * Where a picture of [pictureSize] is drawn in [frame]: whole and centred, or covering it.
 *
 * @param pictureSize The picture's own size; unspecified fills the frame.
 * @param frame The frame's size.
 * @param fit How the picture fills the frame.
 * @return Its left edge, its size and its top edge.
 */
internal fun pictureRect(pictureSize: Size, frame: Size, fit: MediaFit): Triple<Float, Size, Float> {
    val size = when {
        pictureSize.isUnspecified || pictureSize.width <= 0f || pictureSize.height <= 0f -> frame
        fit == MediaFit.Whole -> fittedSize(pictureSize, frame)
        else -> {
            val cover = max(frame.width / pictureSize.width, frame.height / pictureSize.height)
            Size(pictureSize.width * cover, pictureSize.height * cover)
        }
    }
    return Triple((frame.width - size.width) / 2f, size, (frame.height - size.height) / 2f)
}
