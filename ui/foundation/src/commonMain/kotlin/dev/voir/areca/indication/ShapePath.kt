package dev.voir.areca.indication

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

/**
 * Traces [shape] into [target] as a path, optionally offset from the shape's own edge.
 *
 * Every indication that takes a `Shape` goes through here, for three jobs: filling a shape, clipping
 * to it, and stroking a path outset from it. Rectangle and rounded outlines are both rebuilt
 * clockwise starting on the top edge just past the top-left corner. Orientation is irrelevant for
 * fills and clips, but a fixed starting point is what makes `PathMeasure`-driven animation —
 * [StrokeIndication]'s draw-in — advance the same way on every element.
 *
 * A generic outline (a custom path) cannot be offset exactly: a true offset is a Minkowski
 * expansion, which is not worth computing per frame. It is approximated by re-sampling the shape at
 * an adjusted size and translating the result back, which holds for shapes whose outline scales
 * linearly with size and drifts for shapes with a fixed-size detail.
 *
 * @param shape Shape to trace.
 * @param size Element size the shape is resolved against.
 * @param layoutDirection Layout direction the shape is resolved against.
 * @param density Density the shape is resolved against.
 * @param offsetPx Distance from the shape's edge: `0` traces it, positive outsets it for a ring
 *   drawn outside the element, negative insets it for one drawn inside.
 * @param target Path to overwrite. Reused across frames; its previous contents are discarded.
 */
internal fun buildShapePath(
    shape: Shape,
    size: Size,
    layoutDirection: LayoutDirection,
    density: Density,
    offsetPx: Float,
    target: Path,
) {
    target.reset()
    if (size.width <= 0f || size.height <= 0f) return

    val outline = shape.createOutline(size, layoutDirection, density)
    when (outline) {
        is Outline.Rectangle -> {
            buildRoundedClockwisePath(
                rr = RoundRect(outline.rect, CornerRadius.Zero),
                offsetPx = offsetPx,
                target = target,
            )
        }

        is Outline.Rounded -> {
            buildRoundedClockwisePath(outline.roundRect, offsetPx, target)
        }

        is Outline.Generic -> {
            // Approximate offset by re-sampling the shape at an adjusted size
            // and translating the result back to keep it centered on the original
            // bounds. Works for shapes whose visual scales linearly with size.
            if (offsetPx != 0f) {
                val adjustedSize = Size(
                    size.width + 2f * offsetPx,
                    size.height + 2f * offsetPx,
                )
                if (adjustedSize.width > 0f && adjustedSize.height > 0f) {
                    val adjusted = shape.createOutline(adjustedSize, layoutDirection, density)
                    if (adjusted is Outline.Generic) {
                        target.addPath(adjusted.path, Offset(-offsetPx, -offsetPx))
                        return
                    }
                }
            }
            target.addPath(outline.path)
        }
    }
}

/**
 * Traces [rr] clockwise from the top edge, just past the top-left corner.
 *
 * Each corner radius is adjusted by [offsetPx] rather than left alone, so an outset path stays
 * concentric with the original instead of growing squarer as it moves away from it. Radii are
 * clamped to half the offset rectangle, which is what keeps a fully-rounded shape from inverting
 * when inset.
 *
 * @param rr Rounded rectangle to trace.
 * @param offsetPx Distance from the rectangle's edge; positive outsets, negative insets.
 * @param target Path to append to. Assumed empty.
 */
private fun buildRoundedClockwisePath(
    rr: RoundRect,
    offsetPx: Float,
    target: Path,
) {
    val left = rr.left - offsetPx
    val top = rr.top - offsetPx
    val right = rr.right + offsetPx
    val bottom = rr.bottom + offsetPx
    if (right <= left || bottom <= top) return

    val maxCx = (right - left) / 2f
    val maxCy = (bottom - top) / 2f
    val tlx = (rr.topLeftCornerRadius.x + offsetPx).coerceIn(0f, maxCx)
    val tly = (rr.topLeftCornerRadius.y + offsetPx).coerceIn(0f, maxCy)
    val trx = (rr.topRightCornerRadius.x + offsetPx).coerceIn(0f, maxCx)
    val tryY = (rr.topRightCornerRadius.y + offsetPx).coerceIn(0f, maxCy)
    val brx = (rr.bottomRightCornerRadius.x + offsetPx).coerceIn(0f, maxCx)
    val bry = (rr.bottomRightCornerRadius.y + offsetPx).coerceIn(0f, maxCy)
    val blx = (rr.bottomLeftCornerRadius.x + offsetPx).coerceIn(0f, maxCx)
    val bly = (rr.bottomLeftCornerRadius.y + offsetPx).coerceIn(0f, maxCy)

    target.moveTo(left + tlx, top)
    target.lineTo(right - trx, top)
    if (trx > 0f && tryY > 0f) {
        target.arcTo(
            rect = Rect(right - 2f * trx, top, right, top + 2f * tryY),
            startAngleDegrees = -90f, sweepAngleDegrees = 90f, forceMoveTo = false,
        )
    }
    target.lineTo(right, bottom - bry)
    if (brx > 0f && bry > 0f) {
        target.arcTo(
            rect = Rect(right - 2f * brx, bottom - 2f * bry, right, bottom),
            startAngleDegrees = 0f, sweepAngleDegrees = 90f, forceMoveTo = false,
        )
    }
    target.lineTo(left + blx, bottom)
    if (blx > 0f && bly > 0f) {
        target.arcTo(
            rect = Rect(left, bottom - 2f * bly, left + 2f * blx, bottom),
            startAngleDegrees = 90f, sweepAngleDegrees = 90f, forceMoveTo = false,
        )
    }
    target.lineTo(left, top + tly)
    if (tlx > 0f && tly > 0f) {
        target.arcTo(
            rect = Rect(left, top, left + 2f * tlx, top + 2f * tly),
            startAngleDegrees = 180f, sweepAngleDegrees = 90f, forceMoveTo = false,
        )
    }
    target.close()
}
