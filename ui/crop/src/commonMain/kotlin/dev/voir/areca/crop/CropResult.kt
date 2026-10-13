package dev.voir.areca.crop

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.addOutline
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.roundToInt
import kotlin.math.sqrt

/**
 * A finished crop, described in the source image's own pixels.
 *
 * A straightened crop is not an axis-aligned rectangle of the source, so it is described as a
 * rectangle of [size] centred on [center], with a rotation and a mirror — enough to store, send to a
 * server that crops itself, or [render] here. To reproduce it, apply the steps in this order:
 *
 * 1. When [isMirrored], flip the source left to right about the vertical line through [center].
 * 2. Rotate the result clockwise by [rotation] degrees about [center].
 * 3. Cut the axis-aligned rectangle of [size] centred on [center].
 *
 * Mirroring comes first: rotating first and flipping the output gives a different picture whenever
 * [rotation] is not a multiple of 180°. As a mapping, the output point at offset `q` from the
 * output's centre shows the source point
 *
 * ```
 * center + M · R(−rotation) · q
 * ```
 *
 * where `R(a)` turns clockwise by `a` degrees with y pointing down (`(x, y) → (x·cos a − y·sin a,
 * x·sin a + y·cos a)`), `M` negates x when [isMirrored], and `q` is in source pixels: an offset in the
 * pixels of an output of another size is first scaled by `size / outputSize`.
 *
 * A crop made on a preview — the photo decoded to a display size — describes the preview's pixels.
 * [scaledTo] carries it over to the full-resolution photo before it is stored or rendered.
 *
 * @property imageSize Size of the source image, in pixels.
 * @property center Centre of the crop, in source pixels from the image's top-left corner.
 * @property size Width and height of the crop, in source pixels, measured along the crop's own
 *   (rotated) axes.
 * @property rotation Clockwise rotation in degrees applied to the source, after any mirror and before
 *   cutting, within `-180..180`. Quarter turns and straightening together.
 * @property isMirrored Whether the source is flipped left to right about [center] before it is
 *   rotated.
 */
@Immutable
public data class CropResult(
    public val imageSize: Size,
    public val center: Offset,
    public val size: Size,
    public val rotation: Float,
    public val isMirrored: Boolean,
) {
    /** Width over height of the crop. */
    val aspectRatio: Float get() = size.width / size.height

    /**
     * The same crop on a copy of the photo of another size: the full-resolution photo a preview was
     * decoded from, usually.
     *
     * The centre scales with each side of the photo, and the crop's size by the geometric mean of the
     * two scales, so a crop on a downsampled preview — whose sides were rounded separately — keeps its
     * proportions. The rotation and the mirror are unchanged.
     *
     * @param sourceSize Size of the other copy, in pixels. It must have the proportions of [imageSize]:
     *   the same picture in the same orientation, decoded at another size.
     * @return The crop in [sourceSize]'s pixels, with [imageSize] set to it.
     * @throws IllegalArgumentException When [sourceSize] is not positive, or its proportions differ
     *   from [imageSize]'s by more than rounding explains — a photo turned on its side, or another
     *   picture.
     */
    public fun scaledTo(sourceSize: Size): CropResult {
        require(sourceSize.width > 0f && sourceSize.height > 0f) { "Source size must be positive, was $sourceSize." }
        val scaleX = sourceSize.width / imageSize.width
        val scaleY = sourceSize.height / imageSize.height
        require(abs(scaleX / scaleY - 1f) <= ProportionTolerance) {
            "The source ($sourceSize) must have the proportions of the image the crop was made on ($imageSize)."
        }
        val scale = sqrt(scaleX * scaleY)
        return copy(
            imageSize = sourceSize,
            center = Offset(center.x * scaleX, center.y * scaleY),
            size = Size(size.width * scale, size.height * scale),
        )
    }

    /**
     * The pixel size to render at to keep the crop's full detail, optionally capped.
     *
     * @param maxDimension Longest side allowed, in pixels, or `null` for no cap. An avatar passes its
     *   upload size; a photo editor usually passes nothing.
     * @return Output size with the crop's proportions, at least one pixel each way.
     * @throws IllegalArgumentException When [maxDimension] is not positive.
     */
    public fun outputSize(maxDimension: Int? = null): IntSize {
        require(maxDimension == null || maxDimension > 0) { "maxDimension must be positive, was $maxDimension." }
        val longest = max(size.width, size.height)
        val factor = if (maxDimension != null && longest > maxDimension) maxDimension / longest else 1f
        return IntSize(
            width = (size.width * factor).roundToInt().coerceAtLeast(1),
            height = (size.height * factor).roundToInt().coerceAtLeast(1),
        )
    }

    /**
     * Draws the crop into a new bitmap, in the order the class describes.
     *
     * [outputSize] should have the crop's proportions; a different one stretches the crop to fill it.
     * Outside a [mask] that is not a rectangle the pixels are transparent, so encode the result in a
     * format with alpha. Many products store the square crop and mask it only when displaying it —
     * leave [mask] as a rectangle to do that.
     *
     * The [mask] is resolved at [outputSize] with [density], so a mask sized in dp comes out in other
     * proportions than on a stage of another size or density. [ImageCropState.render] resolves the mask
     * as the stage drew it; prefer it, or use a proportional mask such as [CropShapes.Oval] or
     * `RoundedCornerShape(percent = 12)` here.
     *
     * Runs on the calling thread and allocates the whole bitmap, `width × height × 4` bytes of
     * [outputSize]; for a large output, call it off the main thread. It draws into a software canvas,
     * so on Android [painter] must not draw a hardware bitmap (`Bitmap.Config.HARDWARE`, which Coil
     * decodes by default): decode the photo with hardware bitmaps turned off.
     *
     * @param painter The same image the crop was made on, at [imageSize]. To render a crop made on a
     *   preview from the full-resolution photo, [scaledTo] the photo's size first.
     * @param outputSize Pixel size of the bitmap.
     * @param mask Shape the crop is cut to.
     * @param density Density the [mask] and [painter] resolve dp against.
     * @param layoutDirection Direction the [mask] and [painter] resolve against.
     * @return The cropped image.
     * @throws IllegalArgumentException When [outputSize] is not positive, or when [painter] cannot be
     *   drawn into a software bitmap, as an Android hardware bitmap cannot.
     */
    public fun render(
        painter: Painter,
        outputSize: IntSize = outputSize(),
        mask: Shape = RectangleShape,
        density: Density = Density(1f),
        layoutDirection: LayoutDirection = LayoutDirection.Ltr,
    ): ImageBitmap {
        val target = Size(outputSize.width.toFloat(), outputSize.height.toFloat())
        val clip = if (mask == RectangleShape) {
            null
        } else {
            Path().apply { addOutline(mask.createOutline(target, layoutDirection, density)) }
        }
        return renderClipped(painter, outputSize, clip, density, layoutDirection)
    }

    /**
     * Draws the crop into a new bitmap, cut to an outline already resolved at [outputSize].
     *
     * @param painter The same image the crop was made on, at [imageSize].
     * @param outputSize Pixel size of the bitmap.
     * @param clip Outline the crop is cut to, in output pixels, or `null` for the whole rectangle.
     * @param density Density [painter] resolves dp against.
     * @param layoutDirection Direction [painter] resolves against.
     * @return The cropped image.
     * @throws IllegalArgumentException When [outputSize] is not positive, or when [painter] cannot be
     *   drawn into a software bitmap.
     */
    internal fun renderClipped(
        painter: Painter,
        outputSize: IntSize,
        clip: Path?,
        density: Density,
        layoutDirection: LayoutDirection,
    ): ImageBitmap {
        require(outputSize.width > 0 && outputSize.height > 0) { "Output size must be positive, was $outputSize." }

        val bitmap = ImageBitmap(outputSize.width, outputSize.height, hasAlpha = true)
        val target = Size(outputSize.width.toFloat(), outputSize.height.toFloat())
        try {
            drawCrop(painter, bitmap, target, clip, density, layoutDirection)
        } catch (error: IllegalArgumentException) {
            // Android's software canvas rejects a hardware bitmap with a message that names neither
            // the painter nor the remedy.
            throw IllegalArgumentException(
                "The painter could not be drawn into the crop's software bitmap. On Android, decode the " +
                    "photo with hardware bitmaps turned off (Bitmap.Config.HARDWARE is Coil's default).",
                error,
            )
        }
        return bitmap
    }

    /**
     * Draws the photo, transformed as the crop describes, into [bitmap].
     *
     * @param painter The photo, at [imageSize].
     * @param bitmap Bitmap to draw into.
     * @param target Size of [bitmap], in pixels.
     * @param clip Outline the crop is cut to, in output pixels, or `null` for the whole rectangle.
     * @param density Density [painter] resolves dp against.
     * @param layoutDirection Direction [painter] resolves against.
     */
    private fun drawCrop(
        painter: Painter,
        bitmap: ImageBitmap,
        target: Size,
        clip: Path?,
        density: Density,
        layoutDirection: LayoutDirection,
    ) {
        CanvasDrawScope().draw(density, layoutDirection, Canvas(bitmap), target) {
            val drawPhoto: DrawScope.() -> Unit = {
                val scaleX = target.width / this@CropResult.size.width
                val scaleY = target.height / this@CropResult.size.height
                // `this@CropResult` throughout: the transform scope has a `center` and a `size` of
                // its own, which would silently stand in for the crop's.
                withTransform(
                    transformBlock = {
                        translate(target.width / 2f, target.height / 2f)
                        rotate(rotation, pivot = Offset.Zero)
                        scale(if (isMirrored) -scaleX else scaleX, scaleY, pivot = Offset.Zero)
                        translate(-this@CropResult.center.x, -this@CropResult.center.y)
                    },
                ) {
                    with(painter) { draw(imageSize) }
                }
            }
            if (clip == null) {
                drawPhoto()
            } else {
                // Clipping rather than blending: a blend only touches the pixels the shape covers,
                // so it cannot clear what lies outside it.
                clipPath(clip) { drawPhoto() }
            }
        }
    }
}

/**
 * The outline of [mask] as a stage draws it on a frame of [frameSize], scaled to [target].
 *
 * Resolving the mask at the frame's own size, with the stage's density and direction, and then
 * scaling it keeps every proportion the user saw, including those of a mask sized in dp or pixels.
 *
 * @param mask Shape the crop is cut to.
 * @param frameSize Size of the crop frame on the stage, in pixels.
 * @param target Size of the output, in pixels.
 * @param density Density of the stage.
 * @param layoutDirection Layout direction of the stage.
 * @return The outline in output pixels.
 */
internal fun maskPath(
    mask: Shape,
    frameSize: Size,
    target: Size,
    density: Density,
    layoutDirection: LayoutDirection,
): Path = Path().apply {
    addOutline(mask.createOutline(frameSize, layoutDirection, density))
    transform(Matrix().apply { scale(x = target.width / frameSize.width, y = target.height / frameSize.height) })
}

/**
 * How far the proportions of the two copies [CropResult.scaledTo] relates may differ: a 4000 × 3000
 * photo decoded to 1365 × 1024 rounds its sides apart by a tenth of a percent, while a 4:3 photo
 * turned on its side is more than forty percent off.
 */
private const val ProportionTolerance = 0.02f
