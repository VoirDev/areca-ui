package dev.voir.areca.media.zoom

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.isUnspecified
import kotlin.math.min

/**
 * The size a picture is drawn at in its frame before any zoom: whole, as large as fits, centred.
 *
 * @param picture The picture's own size, or [Size.Unspecified] when it has none.
 * @param frame Size of the frame.
 * @return The fitted size, or [frame] for a picture with no size of its own, which fills it.
 */
internal fun fittedSize(picture: Size, frame: Size): Size {
    if (picture.isUnspecified || picture.width <= 0f || picture.height <= 0f) return frame

    val fit = min(frame.width / picture.width, frame.height / picture.height)

    return Size(width = picture.width * fit, height = picture.height * fit)
}

/**
 * Keeps a zoomed picture's edges from coming inside the frame.
 *
 * On an axis where the zoomed picture is still no larger than the frame — the long way of a
 * letterboxed photograph — it stays centred.
 *
 * @param offset Proposed translation from the centred position.
 * @param frame Size of the frame.
 * @param picture Size the picture is drawn at before zoom; see [fittedSize].
 * @param scale Current zoom.
 * @return [offset], limited on each axis to how far the zoomed picture overhangs the frame.
 */
internal fun clampOffset(offset: Offset, frame: Size, picture: Size, scale: Float): Offset {
    val maxX = ((picture.width * scale - frame.width) / 2f).coerceAtLeast(0f)
    val maxY = ((picture.height * scale - frame.height) / 2f).coerceAtLeast(0f)

    return Offset(
        x = offset.x.coerceIn(-maxX, maxX),
        y = offset.y.coerceIn(-maxY, maxY),
    )
}

/**
 * The translation that keeps the point under [focus] where it is while the zoom changes by
 * [zoomChange].
 *
 * The picture scales about the frame's centre and is then translated, so a point at `p` from the
 * centre is drawn at `p × scale + offset`; holding that still for the point under [focus] gives the
 * new offset.
 *
 * @param offset Translation before the change.
 * @param focus Point to zoom around, in the frame's coordinates — the fingers' centroid, a tap.
 * @param frame Size of the frame.
 * @param zoomChange Factor the zoom is multiplied by.
 * @return The translation after the change, before [clampOffset].
 */
internal fun zoomOffset(offset: Offset, focus: Offset, frame: Size, zoomChange: Float): Offset {
    val fromCentre = focus - Offset(frame.width / 2f, frame.height / 2f)

    return offset * zoomChange + fromCentre * (1f - zoomChange)
}

/**
 * The longest edge, in pixels, of a frame a picture is fitted into: what the viewer asks the loader
 * for, so a picture is decoded at the size it is shown rather than at the size it was taken.
 *
 * @param maxWidth Widest the frame can be, in pixels, or [Int.MAX_VALUE] when unbounded.
 * @param maxHeight Tallest the frame can be, in pixels, or [Int.MAX_VALUE] when unbounded.
 * @param fallback Edge to ask for when neither side is bounded.
 * @return The longer bounded side, or [fallback].
 */
internal fun displayEdge(maxWidth: Int, maxHeight: Int, fallback: Int): Int =
    listOf(maxWidth, maxHeight)
        .filter { side -> side in 1 until Int.MAX_VALUE }
        .maxOrNull()
        ?: fallback

/**
 * A pan applied as far as the zoomed content's edges allow, and what was left over.
 *
 * @property offset Translation after the pan, inside the content's edges.
 * @property remainder Part of the pan the edges stopped — for a scrolling column, the part that
 *   scrolls it instead.
 */
internal data class PanResult(val offset: Offset, val remainder: Offset)

/**
 * Pans zoomed content by [pan], stopping at its edges.
 *
 * @param offset Translation before the pan.
 * @param pan How far the fingers moved.
 * @param frame Size of the frame.
 * @param content Size the content is drawn at before zoom; see [fittedSize].
 * @param scale Current zoom.
 * @return The new translation, and the part of [pan] the edges stopped.
 */
internal fun panWithin(offset: Offset, pan: Offset, frame: Size, content: Size, scale: Float): PanResult {
    val proposed = offset + pan
    val clamped = clampOffset(proposed, frame, content, scale)

    return PanResult(offset = clamped, remainder = proposed - clamped)
}
