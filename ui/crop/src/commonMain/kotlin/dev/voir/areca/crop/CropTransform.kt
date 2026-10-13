package dev.voir.areca.crop

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.round
import kotlin.math.sign
import kotlin.math.sin

/**
 * Where a photo sits under a crop frame.
 *
 * The frame is in viewport pixels. Everything else is in **image coordinates**: source pixels with
 * the origin at the image's centre and y pointing down. A point `x` of the image is drawn at
 *
 * ```
 * frame.center + R(rotation + straighten) · diag(±scale, scale) · (x − focus)
 * ```
 *
 * where the sign is negative when [isMirrored], and `scale` is [zoom] times the scale at which the
 * image just covers the frame (see [CropProjection.coverScale]). Measuring zoom from that cover scale
 * rather than in absolute terms is what lets straightening zoom in exactly enough to keep the corners
 * filled, without the zoom the user chose changing.
 *
 * @property frame Crop frame in viewport pixels.
 * @property focus Image point under the frame's centre.
 * @property zoom Magnification over the cover scale. At least 1.
 * @property rotation Quarter-turn rotation in degrees, clockwise. A multiple of 90 except while a turn
 *   animates.
 * @property straighten Fine rotation in degrees, clockwise, within ±45.
 * @property isMirrored Whether the image is flipped left to right in its own axes, before rotating.
 */
@Immutable
internal data class CropTransform(
    val frame: Rect,
    val focus: Offset = Offset.Zero,
    val zoom: Float = 1f,
    val rotation: Float = 0f,
    val straighten: Float = 0f,
    val isMirrored: Boolean = false,
) {
    /** Total clockwise rotation in degrees. */
    val angle: Float get() = rotation + straighten

    /** `-1` when mirrored, otherwise `1`: the sign of the image's horizontal scale. */
    val mirrorSign: Float get() = if (isMirrored) -1f else 1f
}

/**
 * Quantities derived from a [CropTransform] for one image size.
 *
 * @property cos Cosine of the total angle.
 * @property sin Sine of the total angle.
 * @property coverScale Smallest scale at which the rotated image still covers the whole frame.
 * @property scale Viewport pixels per image pixel.
 * @property limit How far [CropTransform.focus] may move from the image centre on each axis before
 *   the frame would show past the image's edge.
 */
@Immutable
internal data class CropProjection(
    val cos: Float,
    val sin: Float,
    val coverScale: Float,
    val scale: Float,
    val limit: Offset,
)

/**
 * Derives the scale and the focus limits for [image].
 *
 * The frame, rotated into image space, has an axis-aligned bounding box of `span / scale`. A rotated
 * rectangle lies inside an axis-aligned one exactly when its corners do, and its corners are what
 * reach that bounding box, so keeping the box inside the image is both necessary and sufficient.
 *
 * @param image Source size in pixels.
 * @return The projection.
 */
internal fun CropTransform.project(image: Size): CropProjection {
    val radians = angle * PI.toFloat() / 180f
    val c = cos(radians)
    val s = sin(radians)
    val spanX = frame.width * abs(c) + frame.height * abs(s)
    val spanY = frame.width * abs(s) + frame.height * abs(c)
    val cover = max(spanX / image.width, spanY / image.height)
    val scale = zoom * cover
    return CropProjection(
        cos = c,
        sin = s,
        coverScale = cover,
        scale = scale,
        limit = Offset(
            x = max(0f, image.width / 2f - spanX / (2f * scale)),
            y = max(0f, image.height / 2f - spanY / (2f * scale)),
        ),
    )
}

/**
 * Maps a viewport point to the image point drawn there.
 *
 * @param point Viewport point in pixels.
 * @param projection [project] of this transform for the same image.
 * @return Image point, in image coordinates.
 */
internal fun CropTransform.toImage(point: Offset, projection: CropProjection): Offset {
    val unrotated = unrotate(point - frame.center, projection)
    return Offset(
        x = focus.x + unrotated.x / (mirrorSign * projection.scale),
        y = focus.y + unrotated.y / projection.scale,
    )
}

/**
 * Maps an image point to where it is drawn in the viewport.
 *
 * @param point Image point, in image coordinates.
 * @param projection [project] of this transform for the same image.
 * @return Viewport point in pixels.
 */
internal fun CropTransform.toViewport(point: Offset, projection: CropProjection): Offset {
    val ax = mirrorSign * projection.scale * (point.x - focus.x)
    val ay = projection.scale * (point.y - focus.y)
    return frame.center + Offset(
        x = projection.cos * ax - projection.sin * ay,
        y = projection.sin * ax + projection.cos * ay,
    )
}

/**
 * Keeps the frame inside the image by pulling [CropTransform.focus] back within its limits.
 *
 * @param image Source size in pixels.
 * @return The nearest transform whose frame shows only the image.
 */
internal fun CropTransform.clamped(image: Size): CropTransform {
    val limit = project(image).limit
    return copy(focus = Offset(focus.x.coerceIn(-limit.x, limit.x), focus.y.coerceIn(-limit.y, limit.y)))
}

/**
 * Lets the focus run past its limits with resistance, for the stretch of a drag past an edge.
 *
 * @param image Source size in pixels.
 * @return The transform as drawn mid-drag. Settle to [clamped] when the drag ends.
 */
internal fun CropTransform.rubberBanded(image: Size): CropTransform {
    val limit = project(image).limit
    return copy(focus = Offset(rubberBand(focus.x, limit.x), rubberBand(focus.y, limit.y)))
}

/**
 * Moves the photo by a viewport distance, as a finger dragging it would.
 *
 * Not clamped, so a drag can run past an edge; the caller decides whether to clamp or rubber-band.
 *
 * @param delta Distance in viewport pixels.
 * @param image Source size in pixels.
 * @return The moved transform.
 */
internal fun CropTransform.panned(delta: Offset, image: Size): CropTransform {
    val projection = project(image)
    val unrotated = unrotate(delta, projection)
    return copy(
        focus = Offset(
            x = focus.x - unrotated.x / (mirrorSign * projection.scale),
            y = focus.y - unrotated.y / projection.scale,
        ),
    )
}

/**
 * Changes the zoom while keeping the image point under [anchor] where it is.
 *
 * @param zoom New zoom, coerced into `1..maxZoom`.
 * @param anchor Viewport point that holds still — a pinch's centroid, the pointer of a scroll. `null`
 *   holds the frame's centre.
 * @param image Source size in pixels.
 * @param maxZoom Largest zoom allowed.
 * @return The zoomed transform, clamped.
 */
internal fun CropTransform.zoomed(zoom: Float, anchor: Offset?, image: Size, maxZoom: Float): CropTransform {
    val pivot = anchor ?: frame.center
    val held = toImage(pivot, project(image))
    val next = copy(zoom = zoom.coerceIn(1f, maxZoom))
    val projection = next.project(image)
    val unrotated = unrotate(pivot - frame.center, projection)
    return next.copy(
        focus = Offset(
            x = held.x - unrotated.x / (mirrorSign * projection.scale),
            y = held.y - unrotated.y / projection.scale,
        ),
    ).clamped(image)
}

/**
 * Moves the frame while the photo stays where it is on screen, as dragging a frame handle does.
 *
 * The photo only grows when the new frame would otherwise show past its edges.
 *
 * @param frame New frame in viewport pixels.
 * @param image Source size in pixels.
 * @param maxZoom Largest zoom allowed. A frame dragged very small is held at this zoom, so the photo
 *   shrinks under it rather than magnifying without bound.
 * @return The transform for the new frame, clamped.
 */
internal fun CropTransform.withFrame(frame: Rect, image: Size, maxZoom: Float): CropTransform {
    val before = project(image)
    val focus = toImage(frame.center, before)
    val cover = copy(frame = frame, zoom = 1f).project(image).coverScale
    return copy(
        frame = frame,
        focus = focus,
        zoom = (before.scale / cover).coerceIn(1f, maxZoom),
    ).clamped(image)
}

/**
 * The same crop mirrored left to right on screen.
 *
 * Mirroring the image's own axis would flip along a tilted line once the photo is straightened.
 * Mirroring on screen instead is `M · R(θ) = R(−θ) · M`, so the rotation and the straightening both
 * change sign and the focus stays put.
 *
 * @return The mirrored transform.
 */
internal fun CropTransform.mirrored(): CropTransform = copy(
    rotation = normalizeQuarterTurns(-rotation),
    straighten = -straighten,
    isMirrored = !isMirrored,
)

/**
 * Interpolates two transforms for an animation frame.
 *
 * Mirroring does not interpolate: it takes [end]'s value throughout. A mirror is animated separately,
 * as a horizontal squeeze of the drawing.
 *
 * @param start Transform at [fraction] 0.
 * @param end Transform at [fraction] 1.
 * @param fraction Progress, normally `0..1`.
 * @return The transform in between.
 */
internal fun lerp(start: CropTransform, end: CropTransform, fraction: Float): CropTransform = CropTransform(
    frame = Rect(
        left = lerp(start.frame.left, end.frame.left, fraction),
        top = lerp(start.frame.top, end.frame.top, fraction),
        right = lerp(start.frame.right, end.frame.right, fraction),
        bottom = lerp(start.frame.bottom, end.frame.bottom, fraction),
    ),
    focus = Offset(lerp(start.focus.x, end.focus.x, fraction), lerp(start.focus.y, end.focus.y, fraction)),
    zoom = lerp(start.zoom, end.zoom, fraction),
    rotation = lerp(start.rotation, end.rotation, fraction),
    straighten = lerp(start.straighten, end.straighten, fraction),
    isMirrored = end.isMirrored,
)

/**
 * The transform an animation from [start] to [target] interpolates towards, so it takes the short way.
 *
 * Its rotation is [target]'s shifted by whole turns to within half a turn of [start]'s, so a step back
 * from 270° to 0° turns a quarter turn forward rather than three quarters back. When the two differ in
 * mirroring it is [target] un-mirrored on screen ([mirrored]): the animation interpolates the geometry
 * with [start]'s mirroring and squeezes the drawing through zero meanwhile, which lands exactly on
 * [target], instead of flipping on the first frame and then spinning.
 *
 * @param start Transform the animation starts from.
 * @param target Transform the animation ends at.
 * @return The transform to interpolate towards; drawn as [target] once any mirror squeeze completes.
 */
internal fun animationPath(start: CropTransform, target: CropTransform): CropTransform {
    val path = if (start.isMirrored != target.isMirrored) target.mirrored() else target
    return path.copy(rotation = nearestTurn(path.rotation, start.rotation))
}

/**
 * The angle equivalent to [degrees], shifted by whole turns, that lies closest to [reference].
 *
 * @param degrees Angle to shift, in degrees.
 * @param reference Angle to come closest to, in degrees.
 * @return An angle within `reference - 180 .. reference + 180` that differs from [degrees] by whole
 *   turns.
 */
internal fun nearestTurn(degrees: Float, reference: Float): Float =
    reference + ((degrees - reference) % 360f + 540f) % 360f - 180f

/**
 * The largest frame of [aspectRatio] that fits [bounds], centred in it.
 *
 * @param bounds Area the frame may occupy, in viewport pixels.
 * @param aspectRatio Width over height of the frame.
 * @return The frame.
 */
internal fun fitFrame(bounds: Rect, aspectRatio: Float): Rect {
    val width = min(bounds.width, bounds.height * aspectRatio)
    val height = width / aspectRatio
    return Rect(
        left = bounds.left + (bounds.width - width) / 2f,
        top = bounds.top + (bounds.height - height) / 2f,
        right = bounds.left + (bounds.width + width) / 2f,
        bottom = bounds.top + (bounds.height + height) / 2f,
    )
}

/**
 * Brings a rotation into `0 until 360`, rounded to the nearest quarter turn.
 *
 * @param degrees Rotation in degrees.
 * @return The equivalent quarter turn, in degrees.
 */
internal fun normalizeQuarterTurns(degrees: Float): Float {
    val turns = round(degrees / 90f).toInt()
    return (((turns % 4) + 4) % 4) * 90f
}

/**
 * Rotates a viewport vector by the negative of the projection's angle, into the frame's own axes.
 *
 * @param vector Vector in viewport pixels.
 * @param projection Projection supplying the angle.
 * @return The vector in the rotated axes.
 */
private fun unrotate(vector: Offset, projection: CropProjection): Offset = Offset(
    x = projection.cos * vector.x + projection.sin * vector.y,
    y = -projection.sin * vector.x + projection.cos * vector.y,
)

/**
 * Passes [value] through inside `±limit`, and lets only a third of any excess through beyond it.
 *
 * @param value Unconstrained value.
 * @param limit Non-negative bound.
 * @return The resisted value.
 */
private fun rubberBand(value: Float, limit: Float): Float {
    val magnitude = abs(value)
    return if (magnitude <= limit) value else sign(value) * (limit + (magnitude - limit) * RubberBandResistance)
}

/**
 * Linear interpolation of two floats.
 *
 * @param start Value at [fraction] 0.
 * @param end Value at [fraction] 1.
 * @param fraction Progress.
 * @return The value in between.
 */
private fun lerp(start: Float, end: Float, fraction: Float): Float = start + (end - start) * fraction

/** Share of a drag past an edge that still moves the photo. */
private const val RubberBandResistance = 0.35f
