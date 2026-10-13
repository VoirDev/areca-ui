package dev.voir.areca.playground.crop

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
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sin
import kotlin.random.Random

/**
 * The photo every crop page edits: an alpine lake at dusk, painted rather than shipped, so the
 * playground carries no binary assets.
 *
 * Its horizon leans [SampleTilt] degrees, so there is something to straighten, and it is wider than
 * tall, so quarter turns and the Original aspect both visibly change the frame. Painted once on first
 * use and shared: a bitmap painter holds no per-use state, and a copy per cell would cost megabytes
 * each.
 */
internal val SamplePhoto: Painter by lazy { BitmapPainter(paintSamplePhoto(), filterQuality = FilterQuality.High) }

/** How far the sample's horizon leans, in degrees anticlockwise. */
internal const val SampleTilt = 3.2f

/** Width of the sample photo in pixels. */
private const val SampleWidth = 1800

/** Height of the sample photo in pixels. */
private const val SampleHeight = 1200

/** Paints the sample photo into a new bitmap. */
private fun paintSamplePhoto(): ImageBitmap {
    val bitmap = ImageBitmap(SampleWidth, SampleHeight)
    val size = Size(SampleWidth.toFloat(), SampleHeight.toFloat())
    CanvasDrawScope().draw(Density(1f), LayoutDirection.Ltr, Canvas(bitmap), size) {
        rotate(-SampleTilt, pivot = center) { paintScene(Random(seed = 11)) }
        drawRect(
            brush = Brush.radialGradient(
                0f to Color.Transparent,
                1f to Color(0x73080614),
                center = center,
                radius = 1150f,
            ),
        )
    }
    return bitmap
}

/**
 * Paints the tilted scene. Everything is drawn past the photo's edges so the tilt leaves no gaps.
 *
 * @param random Source of the scatter, seeded so the photo is the same on every run.
 */
private fun DrawScope.paintScene(random: Random) {
    val horizon = 700f
    val left = -320f
    val right = 2120f
    val width = right - left

    drawRect(
        brush = Brush.verticalGradient(
            0f to Color(0xFF141A3D),
            0.42f to Color(0xFF4A3A78),
            0.72f to Color(0xFFC86F74),
            0.9f to Color(0xFFF2A873),
            1f to Color(0xFFFBD193),
            startY = -220f,
            endY = horizon,
        ),
        topLeft = Offset(left, -320f),
        size = Size(width, horizon + 320f),
    )
    repeat(160) {
        val y = -220f + random.nextFloat() * 420f
        drawCircle(
            color = Color.White.copy(alpha = max(0f, 0.2f + random.nextFloat() * 0.6f * (1f - (y + 220f) / 440f))),
            radius = random.nextFloat() * 1.4f + 0.3f,
            center = Offset(left + random.nextFloat() * width, y),
        )
    }

    val sun = Offset(1220f, horizon - 84f)
    drawRect(
        brush = Brush.radialGradient(
            0f to Color(0xD9FFE2AA),
            0.2f to Color(0x66FFBE8C),
            1f to Color(0x00FFAA82),
            center = sun,
            radius = 560f,
        ),
        topLeft = Offset(left, -320f),
        size = Size(width, horizon + 320f),
    )
    drawCircle(Color(0xFFFFF1D6), radius = 46f, center = sun)

    val ridges = listOf(
        Ridge(amplitude = 310f, color = Color(0xFF8A6A9C), alpha = 0.78f, frequency = 0.0021f),
        Ridge(amplitude = 210f, color = Color(0xFF5B4479), alpha = 0.94f, frequency = 0.0034f),
        Ridge(amplitude = 120f, color = Color(0xFF33284F), alpha = 1f, frequency = 0.0052f),
    )
    val outlines = ridges.map { ridge -> ridge.outline(left, right, horizon, random) }
    ridges.forEachIndexed { index, ridge ->
        drawPath(outlines[index], ridge.color, alpha = ridge.alpha)
        if (index < 2) {
            drawRect(
                brush = Brush.verticalGradient(
                    0f to Color(0x00FABE96),
                    1f to Color(0x52FABE96),
                    startY = horizon - 200f,
                    endY = horizon,
                ),
                topLeft = Offset(left, horizon - 200f),
                size = Size(width, 202f),
            )
        }
    }

    drawRect(
        brush = Brush.verticalGradient(
            0f to Color(0xFFE7A47A),
            0.12f to Color(0xFF9A6A86),
            0.45f to Color(0xFF3B2F5F),
            1f to Color(0xFF120F24),
            startY = horizon,
            endY = 1420f,
        ),
        topLeft = Offset(left, horizon),
        size = Size(width, 760f),
    )
    clipRect(left, horizon, right, horizon + 760f) {
        scale(1f, -1f, pivot = Offset(0f, horizon)) {
            ridges.forEachIndexed { index, ridge -> drawPath(outlines[index], ridge.color, alpha = 0.3f) }
        }
    }
    repeat(80) {
        val y = horizon + 6f + random.nextFloat().pow(1.6f) * 430f
        val streak = (30f + random.nextFloat() * 170f) * max(0.1f, 1f - (y - horizon) / 600f)
        drawRect(
            color = Color(0xFFFFD9A8).copy(alpha = max(0f, 0.15f + random.nextFloat() * 0.5f * (1f - (y - horizon) / 450f))),
            topLeft = Offset(sun.x - streak / 2f + (random.nextFloat() - 0.5f) * 40f, y),
            size = Size(streak, 1.5f + random.nextFloat() * 2f),
        )
    }
    repeat(260) {
        drawRect(
            color = Color.White.copy(alpha = 0.04f + random.nextFloat() * 0.08f),
            topLeft = Offset(left + random.nextFloat() * width, horizon + 10f + random.nextFloat() * 560f),
            size = Size(20f + random.nextFloat() * 120f, 1f),
        )
    }

    val shore = Path().apply {
        moveTo(left, horizon - 12f)
        cubicTo(200f, horizon - 30f, 420f, horizon + 20f, 660f, horizon + 36f)
        lineTo(660f, horizon + 62f)
        cubicTo(400f, horizon + 96f, 100f, horizon + 126f, left, horizon + 150f)
        close()
    }
    drawPath(shore, Color(0xFF0F0C1C))
    repeat(18) {
        val x = -140f + random.nextFloat() * 700f
        drawPine(x = x, base = horizon + 24f + x / 660f * 18f, height = (110f + random.nextFloat() * 230f) * (1f - x / 1100f))
    }
    drawOval(Color(0xFF0D0A18), topLeft = Offset(1326f, horizon + 204f), size = Size(68f, 12f))
    drawRect(Color(0xFF0D0A18), topLeft = Offset(1356f, horizon + 186f), size = Size(6f, 20f))
}

/**
 * Draws a pine silhouette standing on [base].
 *
 * @param x Horizontal position of the trunk.
 * @param base Vertical position of the ground.
 * @param height Height of the tree.
 */
private fun DrawScope.drawPine(x: Float, base: Float, height: Float) {
    val ink = Color(0xFF0D0A18)
    drawRect(ink, topLeft = Offset(x - 2f, base - height * 0.15f), size = Size(4f, height * 0.2f))
    for (tier in 0 until 5) {
        val bottom = base - height * 0.1f - tier * height * 0.18f
        val span = height * 0.3f * (1f - tier / 5f * 0.75f)
        val needles = Path().apply {
            moveTo(x - span / 2f, bottom)
            lineTo(x, bottom - height * 0.32f)
            lineTo(x + span / 2f, bottom)
            close()
        }
        drawPath(needles, ink)
    }
}

/**
 * One layer of mountains.
 *
 * @property amplitude Height of the tallest peaks above the horizon.
 * @property color Fill of the layer.
 * @property alpha Opacity of the layer; distant ranges are hazier.
 * @property frequency How tightly the peaks are packed.
 */
private class Ridge(val amplitude: Float, val color: Color, val alpha: Float, val frequency: Float) {

    /**
     * Outlines the layer from [left] to [right], closed along the horizon.
     *
     * @param left Leftmost x.
     * @param right Rightmost x.
     * @param horizon Height of the horizon.
     * @param random Source of the phase and the jitter.
     * @return The closed outline.
     */
    fun outline(left: Float, right: Float, horizon: Float, random: Random): Path {
        val phase = random.nextFloat() * 10f
        val secondPhase = random.nextFloat() * 10f
        return Path().apply {
            moveTo(left, horizon + 2f)
            var x = left
            while (x <= right) {
                val wave = 0.55f * sin(x * frequency + phase) +
                    0.3f * sin(x * frequency * 2.7f + secondPhase) +
                    0.15f * sin(x * frequency * 6.1f + phase * 2f)
                lineTo(x, horizon - amplitude * max(0.06f, 0.45f + 0.55f * wave) - random.nextFloat() * 5f)
                x += 12f
            }
            lineTo(right, horizon + 2f)
            close()
        }
    }
}
