package dev.voir.areca.crop

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/**
 * A grip on the crop frame that resizes it when dragged.
 *
 * Corners resize along both axes; edges along one, and only while the frame is [CropAspect.Free],
 * because an edge alone cannot keep a ratio.
 *
 * @property movesLeft Whether dragging moves the frame's left side.
 * @property movesTop Whether dragging moves the frame's top side.
 * @property movesRight Whether dragging moves the frame's right side.
 * @property movesBottom Whether dragging moves the frame's bottom side.
 */
internal enum class CropHandle(
    val movesLeft: Boolean = false,
    val movesTop: Boolean = false,
    val movesRight: Boolean = false,
    val movesBottom: Boolean = false,
) {
    /** Top-left corner. */
    TopLeft(movesLeft = true, movesTop = true),

    /** Top-right corner. */
    TopRight(movesTop = true, movesRight = true),

    /** Bottom-right corner. */
    BottomRight(movesRight = true, movesBottom = true),

    /** Bottom-left corner. */
    BottomLeft(movesLeft = true, movesBottom = true),

    /** Top edge. */
    Top(movesTop = true),

    /** Right edge. */
    Right(movesRight = true),

    /** Bottom edge. */
    Bottom(movesBottom = true),

    /** Left edge. */
    Left(movesLeft = true),
    ;

    /** Whether this grip is a corner rather than an edge. */
    val isCorner: Boolean get() = (movesLeft || movesRight) && (movesTop || movesBottom)
}

/**
 * The grip under [point], if any.
 *
 * Corners win over edges, and an edge is only reachable away from its corners, so a press near a
 * corner never resizes along one axis by accident.
 *
 * @param frame Crop frame in viewport pixels.
 * @param point Press position in viewport pixels.
 * @param touchRadius How far from a grip a press still lands on it, in pixels.
 * @param includeEdges Whether edge grips are live.
 * @return The grip, or `null` when the press is on the photo.
 */
internal fun handleAt(frame: Rect, point: Offset, touchRadius: Float, includeEdges: Boolean): CropHandle? {
    val corners = mapOf(
        CropHandle.TopLeft to frame.topLeft,
        CropHandle.TopRight to frame.topRight,
        CropHandle.BottomRight to frame.bottomRight,
        CropHandle.BottomLeft to frame.bottomLeft,
    )
    corners.entries.firstOrNull { (point - it.value).getDistance() < touchRadius }?.let { return it.key }
    if (!includeEdges) return null

    val alongX = point.x > frame.left + touchRadius && point.x < frame.right - touchRadius
    val alongY = point.y > frame.top + touchRadius && point.y < frame.bottom - touchRadius
    val edgeReach = touchRadius * EdgeReachShare
    return when {
        alongX && abs(point.y - frame.top) < edgeReach -> CropHandle.Top
        alongX && abs(point.y - frame.bottom) < edgeReach -> CropHandle.Bottom
        alongY && abs(point.x - frame.left) < edgeReach -> CropHandle.Left
        alongY && abs(point.x - frame.right) < edgeReach -> CropHandle.Right
        else -> null
    }
}

/**
 * The frame [start] becomes when [handle] is dragged by [delta].
 *
 * A free frame moves each dragged side on its own, stopping at [bounds] and at [minSize] — or at its
 * starting size along an axis where it started smaller than [minSize]. A frame with a [ratio] grows
 * from the opposite corner, following whichever axis the finger moved further along, and stops when
 * either axis reaches [bounds].
 *
 * @param start Frame when the drag began.
 * @param handle Grip being dragged. Edge grips are only valid without a [ratio].
 * @param delta Total drag distance since the press, in pixels.
 * @param bounds Area the frame may occupy.
 * @param minSize Smallest side the frame may shrink to, in pixels.
 * @param ratio Width over height to keep, or `null` for a free frame.
 * @return The resized frame.
 */
internal fun resizeFrame(
    start: Rect,
    handle: CropHandle,
    delta: Offset,
    bounds: Rect,
    minSize: Float,
    ratio: Float?,
): Rect {
    if (ratio == null) {
        // A frame already smaller than minSize, as on a stage shorter than it, may keep its size but
        // not shrink further; and a side outside the bounds may stay there. Either way the range
        // each side moves in is never empty.
        val minWidth = min(minSize, start.width)
        val minHeight = min(minSize, start.height)
        val left = if (handle.movesLeft) {
            (start.left + delta.x).coerceIn(min(bounds.left, start.left), start.right - minWidth)
        } else {
            start.left
        }
        val right = if (handle.movesRight) {
            (start.right + delta.x).coerceIn(start.left + minWidth, max(bounds.right, start.right))
        } else {
            start.right
        }
        val top = if (handle.movesTop) {
            (start.top + delta.y).coerceIn(min(bounds.top, start.top), start.bottom - minHeight)
        } else {
            start.top
        }
        val bottom = if (handle.movesBottom) {
            (start.bottom + delta.y).coerceIn(start.top + minHeight, max(bounds.bottom, start.bottom))
        } else {
            start.bottom
        }
        return Rect(left, top, right, bottom)
    }

    val anchorX = if (handle.movesLeft) start.right else start.left
    val anchorY = if (handle.movesTop) start.bottom else start.top
    val draggedWidth = if (handle.movesLeft) start.width - delta.x else start.width + delta.x
    val draggedHeight = if (handle.movesTop) start.height - delta.y else start.height + delta.y
    val roomX = if (handle.movesLeft) anchorX - bounds.left else bounds.right - anchorX
    val roomY = if (handle.movesTop) anchorY - bounds.top else bounds.bottom - anchorY
    val maxWidth = min(roomX, roomY * ratio)
    val minWidth = min(maxWidth, max(minSize, minSize * ratio))
    val width = max(draggedWidth, draggedHeight * ratio).coerceIn(minWidth, maxWidth)
    val height = width / ratio
    val left = if (handle.movesLeft) anchorX - width else anchorX
    val top = if (handle.movesTop) anchorY - height else anchorY
    return Rect(left, top, left + width, top + height)
}

/** Share of the touch radius an edge grip reaches, narrower than a corner's so photo drags near an edge still pan. */
private const val EdgeReachShare = 0.6f
