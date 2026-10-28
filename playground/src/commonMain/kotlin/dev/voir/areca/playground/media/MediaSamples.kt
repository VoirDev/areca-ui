package dev.voir.areca.playground.media

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import dev.voir.areca.media.MediaImageLoader
import dev.voir.areca.media.MediaImageState
import kotlinx.coroutines.delay

/** What a sample product is: the thing drawn standing on its ground. */
internal enum class SampleShape { Sneaker, Bottle, Bag, Chair }

/** What a sample picture is: a product alone, or a scene a photograph would show. */
internal enum class SampleScene { Product, Landscape, Studio }

/** How a sample picture behaves when loaded, so every face a frame has can be seen on demand. */
internal enum class SampleLoad {
    /** Loads after a short wait. */
    Loads,

    /** Loads after a long wait, past the point a spinner joins the skeleton. */
    Slow,

    /** Never loads. */
    Fails,

    /** Fails the first time and loads on retry. */
    Flaky,

    /** Never finishes loading. */
    Hangs,
}

/**
 * A picture model the sample loader understands, standing in for an application's URL.
 *
 * @property id Distinguishes pictures of the same look.
 * @property title What it shows, for captions and descriptions.
 * @property shape The product in it.
 * @property hue The product's colour, in degrees.
 * @property scene Product alone or a scene.
 * @property width Its width in pixels; with [height] the picture's own shape.
 * @property height Its height in pixels.
 * @property zoom How close the product is drawn, for a detail shot.
 * @property flip Whether the product faces the other way.
 * @property load How it behaves when loaded.
 */
internal data class SamplePicture(
    val id: Int,
    val title: String,
    val shape: SampleShape = SampleShape.Sneaker,
    val hue: Float = 12f,
    val scene: SampleScene = SampleScene.Product,
    val width: Int = 800,
    val height: Int = 800,
    val zoom: Float = 1f,
    val flip: Boolean = false,
    val load: SampleLoad = SampleLoad.Loads,
)

/** A product page's pictures: the product from its sides, a detail, a campaign shot and a reader's photo. */
internal val SampleProductPictures: List<SamplePicture> = listOf(
    SamplePicture(1, "Runner 2, side"),
    SamplePicture(2, "Runner 2, other side", flip = true),
    SamplePicture(3, "Laces, close up", zoom = 2.2f),
    SamplePicture(4, "Autumn campaign", scene = SampleScene.Studio, width = 900, height = 1100),
    SamplePicture(5, "On the trail", scene = SampleScene.Landscape, hue = 150f, width = 1200, height = 800),
    SamplePicture(6, "Colourway: Lake", hue = 205f),
    SamplePicture(7, "Field Tote", shape = SampleShape.Bag, hue = 30f),
)

/** Twenty-four products, more than plain dots can count. */
internal val SampleManyPictures: List<SamplePicture> = List(24) { index ->
    SamplePicture(
        id = 100 + index,
        title = "Picture ${index + 1}",
        shape = SampleShape.entries[index % SampleShape.entries.size],
        hue = (index * 37 % 360).toFloat(),
    )
}

/**
 * Stands in for an application's image loader: paints each [SamplePicture] rather than fetching it,
 * after the wait its [SamplePicture.load] asks for, so the playground runs offline and shows every
 * face on demand.
 *
 * @property waitMillis How long a picture that loads takes; [SampleLoad.Slow] takes four times as
 *   long. Zero in render tests, whose clock does not run delays.
 * @property fallback Loads every other model — the attachment samples — as one application loader
 *   serves every picture.
 */
internal class SampleMediaImageLoader(
    private val waitMillis: Long = 450,
    private val fallback: MediaImageLoader? = null,
) : MediaImageLoader {
    /** @return The sample's state, moving to ready or failed after its wait. */
    @Composable
    override fun load(model: Any, maxSize: Int): MediaImageState {
        val picture = model as? SamplePicture ?: return fallback?.load(model, maxSize) ?: MediaImageState.Failed
        // Counted per composition: a retry composes the call afresh, so a flaky picture's second
        // attempt is a new count.
        val attempt = remember(picture) { ((attempts[picture.id] ?: 0) + 1).also { attempts[picture.id] = it } }
        var state by remember(picture) { mutableStateOf<MediaImageState>(MediaImageState.Loading) }

        LaunchedEffect(picture) {
            val fails = picture.load == SampleLoad.Fails || (picture.load == SampleLoad.Flaky && attempt == 1)
            if (picture.load == SampleLoad.Hangs) return@LaunchedEffect
            val wait = if (picture.load == SampleLoad.Slow) waitMillis * SLOW_FACTOR else waitMillis
            if (wait > 0) delay(wait)
            state = if (fails) MediaImageState.Failed else MediaImageState.Ready(samplePainter(picture))
        }
        return state
    }

    private companion object {
        /** How many times each picture has been asked for, to fail a flaky one only the first time. */
        val attempts = mutableMapOf<Int, Int>()

        /** How much longer a slow picture takes. */
        const val SLOW_FACTOR = 4L
    }
}

/** Painted pictures, kept so each is painted once. */
private val painted = mutableMapOf<SamplePicture, Painter>()

/**
 * The painted picture for [picture], whatever its load behaviour.
 *
 * @param picture The sample.
 * @return Its painter.
 */
internal fun samplePainter(picture: SamplePicture): Painter = painted.getOrPut(picture.copy(load = SampleLoad.Loads)) {
    val bitmap = ImageBitmap(picture.width, picture.height)
    val size = Size(picture.width.toFloat(), picture.height.toFloat())
    CanvasDrawScope().draw(Density(1f), LayoutDirection.Ltr, Canvas(bitmap), size) { paint(picture) }
    BitmapPainter(bitmap, filterQuality = FilterQuality.High)
}

/** Paints a sample's scene, then its product. */
private fun DrawScope.paint(picture: SamplePicture) {
    val side = minOf(size.width, size.height)
    when (picture.scene) {
        SampleScene.Product -> Unit
        SampleScene.Studio -> drawRect(
            Brush.radialGradient(
                0f to hsl(picture.hue, 0.45f, 0.82f),
                1f to hsl(picture.hue, 0.4f, 0.52f),
                center = Offset(size.width / 2f, size.height * 0.42f),
                radius = maxOf(size.width, size.height) * 0.75f,
            ),
        )
        SampleScene.Landscape -> paintLandscape(picture.hue)
    }
    if (picture.scene == SampleScene.Landscape) return

    val product = if (picture.scene == SampleScene.Studio) side * 0.8f else side
    translate(left = (size.width - product) / 2f, top = (size.height - product) / 2f) {
        scale(product / UNIT, pivot = Offset.Zero) {
            scale(picture.zoom, pivot = Offset(UNIT / 2f, UNIT / 2f)) {
                scale(if (picture.flip) -1f else 1f, 1f, pivot = Offset(UNIT / 2f, UNIT / 2f)) {
                    paintProduct(picture.shape, picture.hue)
                }
            }
        }
    }
}

/** Paints a sky, a sun and three hills. */
private fun DrawScope.paintLandscape(hue: Float) {
    drawRect(Brush.verticalGradient(0f to hsl(hue + 190f, 0.6f, 0.7f), 1f to hsl(hue + 20f, 0.6f, 0.9f)))
    drawCircle(hsl(40f, 0.95f, 0.72f), radius = size.minDimension * 0.08f, center = Offset(size.width * 0.72f, size.height * 0.3f))
    listOf(0.6f to 0.58f, 0.74f to 0.42f, 0.86f to 0.28f).forEachIndexed { index, (top, light) ->
        val path = Path().apply {
            moveTo(0f, size.height * top)
            cubicTo(
                size.width * 0.3f, size.height * (top - 0.1f),
                size.width * 0.6f, size.height * (top + 0.1f),
                size.width, size.height * (top - 0.04f),
            )
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            close()
        }
        drawPath(path, hsl(hue + 90f + index * 20f, 0.42f, light))
    }
}

/** Paints a product on a [UNIT]-sided square, with its shadow. */
private fun DrawScope.paintProduct(shape: SampleShape, hue: Float) {
    val base = hsl(hue, 0.6f, 0.55f)
    val dark = hsl(hue, 0.6f, 0.34f)
    val light = hsl(hue + 30f, 0.6f, 0.86f)
    drawOval(Color.Black.copy(alpha = 0.1f), topLeft = Offset(210f, 810f), size = Size(580f, 52f))
    when (shape) {
        SampleShape.Sneaker -> {
            val upper = Path().apply {
                moveTo(170f, 610f)
                cubicTo(190f, 520f, 260f, 470f, 330f, 455f)
                lineTo(430f, 440f)
                cubicTo(470f, 400f, 520f, 380f, 560f, 400f)
                lineTo(640f, 470f)
                cubicTo(720f, 500f, 800f, 520f, 840f, 560f)
                cubicTo(870f, 590f, 860f, 640f, 830f, 650f)
                lineTo(200f, 660f)
                close()
            }
            drawPath(upper, base)
            drawRoundRect(Color(0xFFF4F3EF), topLeft = Offset(160f, 650f), size = Size(690f, 55f), cornerRadius = CornerRadius(26f))
            listOf(0f, 40f, 80f).forEach { step ->
                drawLine(Color.White, Offset(430f + step, 440f - step / 2f), Offset(520f + step, 520f - step / 2f), strokeWidth = 14f)
            }
        }
        SampleShape.Bottle -> {
            drawRoundRect(dark, topLeft = Offset(430f, 170f), size = Size(140f, 100f), cornerRadius = CornerRadius(14f))
            drawRect(light, topLeft = Offset(465f, 265f), size = Size(70f, 60f))
            drawRoundRect(base, topLeft = Offset(320f, 320f), size = Size(360f, 450f), cornerRadius = CornerRadius(64f))
            drawRoundRect(Color.White.copy(alpha = 0.9f), topLeft = Offset(390f, 470f), size = Size(220f, 150f), cornerRadius = CornerRadius(8f))
        }
        SampleShape.Bag -> {
            drawArc(dark, 180f, 180f, useCenter = false, topLeft = Offset(380f, 230f), size = Size(240f, 340f), style = Stroke(28f))
            val body = Path().apply {
                moveTo(270f, 380f)
                lineTo(730f, 380f)
                lineTo(770f, 800f)
                lineTo(230f, 800f)
                close()
            }
            drawPath(body, base)
            drawRect(light, topLeft = Offset(250f, 560f), size = Size(500f, 44f))
        }
        SampleShape.Chair -> {
            drawRoundRect(base, topLeft = Offset(330f, 200f), size = Size(340f, 320f), cornerRadius = CornerRadius(44f))
            drawRoundRect(dark, topLeft = Offset(290f, 500f), size = Size(420f, 115f), cornerRadius = CornerRadius(32f))
            listOf(345f to 322f, 655f to 678f).forEach { (from, to) ->
                drawLine(dark, Offset(from, 612f), Offset(to, 810f), strokeWidth = 20f)
            }
        }
    }
}

/**
 * A colour from hue, saturation and lightness, the hue taken round the circle.
 *
 * @param hue Hue in degrees, any value.
 * @param saturation Saturation, `0..1`.
 * @param lightness Lightness, `0..1`.
 * @return The colour.
 */
private fun hsl(hue: Float, saturation: Float, lightness: Float): Color =
    Color.hsl(((hue % 360f) + 360f) % 360f, saturation, lightness)

/** Side of the square a product is drawn on before it is scaled to its picture. */
private const val UNIT = 1000f
