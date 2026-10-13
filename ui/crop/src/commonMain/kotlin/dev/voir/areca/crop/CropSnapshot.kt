package dev.voir.areca.crop

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import kotlin.math.abs

/**
 * Everything the user can change about a crop, captured so it can be compared with, returned to, or
 * saved.
 *
 * [ImageCropState] takes one before every change, for undo. The frame is in the stage's current
 * viewport pixels: when the stage is resized, the state refits every snapshot it keeps along with the
 * crop itself, so a snapshot always compares with the crop on screen.
 *
 * @property transform Where the photo sat, kept inside the photo.
 * @property aspect Proportions the frame kept.
 * @property isOrientationSwapped Whether a ratio was applied the other way round.
 */
@Immutable
internal data class CropSnapshot(
    val transform: CropTransform,
    val aspect: CropAspect,
    val isOrientationSwapped: Boolean,
) {
    /**
     * Whether [other] describes the same crop, to within half a pixel and a rounding of the values.
     *
     * Quarter turns compare modulo a full turn, so four left turns match none.
     *
     * @param other Snapshot to compare with.
     * @return `true` when nothing the user can see differs.
     */
    fun matches(other: CropSnapshot): Boolean {
        val mine = transform
        val theirs = other.transform
        return aspect == other.aspect &&
            isOrientationSwapped == other.isOrientationSwapped &&
            mine.isMirrored == theirs.isMirrored &&
            normalizeQuarterTurns(mine.rotation) == normalizeQuarterTurns(theirs.rotation) &&
            abs(mine.straighten - theirs.straighten) <= ValueTolerance &&
            abs(mine.zoom - theirs.zoom) <= ValueTolerance &&
            (mine.focus - theirs.focus).getDistance() <= PixelTolerance &&
            mine.frame.approximately(theirs.frame)
    }

    /**
     * The same crop with its frame refitted to [bounds], keeping the frame's proportions, as the
     * state refits the crop on screen.
     *
     * @param bounds Area the frame may now occupy, in viewport pixels.
     * @param image Size of the photo, in pixels.
     * @return The refitted snapshot.
     */
    fun refitted(bounds: Rect, image: Size): CropSnapshot = copy(
        transform = transform.copy(frame = fitFrame(bounds, transform.frame.width / transform.frame.height))
            .clamped(image),
    )

    /**
     * Appends this snapshot to [out] as [SnapshotFloats] numbers, for saved instance state.
     *
     * @param out Numbers to append to.
     */
    fun writeTo(out: MutableList<Float>) {
        val frame = transform.frame
        out += listOf(frame.left, frame.top, frame.right, frame.bottom)
        out += listOf(transform.focus.x, transform.focus.y, transform.zoom, transform.rotation, transform.straighten)
        out += if (transform.isMirrored) 1f else 0f
        out += when (aspect) {
            CropAspect.Free -> listOf(AspectFree, 0f, 0f)
            CropAspect.Original -> listOf(AspectOriginal, 0f, 0f)
            is CropAspect.Ratio -> listOf(AspectRatio, aspect.width.toFloat(), aspect.height.toFloat())
        }
        out += if (isOrientationSwapped) 1f else 0f
    }

    /** Reading snapshots back from saved instance state. */
    companion object {
        /**
         * Reads the snapshot [writeTo] wrote at [offset] of [saved].
         *
         * @param saved Saved numbers.
         * @param offset Index of the snapshot's first number.
         * @return The snapshot, or `null` when [saved] holds no valid snapshot there.
         */
        fun readFrom(saved: FloatArray, offset: Int): CropSnapshot? {
            if (offset < 0 || offset + SnapshotFloats > saved.size) return null
            val values = saved.copyOfRange(offset, offset + SnapshotFloats)
            val frame = Rect(values[0], values[1], values[2], values[3])
            if (frame.width <= 0f || frame.height <= 0f) return null
            val aspect = when (values[10]) {
                AspectFree -> CropAspect.Free
                AspectOriginal -> CropAspect.Original
                AspectRatio -> {
                    val width = values[11].toInt()
                    val height = values[12].toInt()
                    if (width <= 0 || height <= 0) return null
                    CropAspect.Ratio(width, height)
                }
                else -> return null
            }
            return CropSnapshot(
                transform = CropTransform(
                    frame = frame,
                    focus = Offset(values[4], values[5]),
                    zoom = values[6],
                    rotation = values[7],
                    straighten = values[8],
                    isMirrored = values[9] != 0f,
                ),
                aspect = aspect,
                isOrientationSwapped = values[13] != 0f,
            )
        }
    }
}

/**
 * Whether two rectangles match to within half a pixel on every side.
 *
 * @param other Rectangle to compare with.
 * @return `true` when they match.
 */
internal fun Rect.approximately(other: Rect): Boolean =
    abs(left - other.left) <= PixelTolerance && abs(top - other.top) <= PixelTolerance &&
        abs(right - other.right) <= PixelTolerance && abs(bottom - other.bottom) <= PixelTolerance

/** Tolerance for comparing zoom and angle values. */
internal const val ValueTolerance = 0.005f

/** Tolerance for comparing positions, in pixels. */
internal const val PixelTolerance = 0.5f

/** Numbers one [CropSnapshot] takes in saved instance state. */
internal const val SnapshotFloats = 14

/** Saved code of [CropAspect.Free]. */
private const val AspectFree = 0f

/** Saved code of [CropAspect.Original]. */
private const val AspectOriginal = 1f

/** Saved code of a [CropAspect.Ratio]. */
private const val AspectRatio = 2f
