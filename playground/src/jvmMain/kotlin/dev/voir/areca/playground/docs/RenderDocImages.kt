package dev.voir.areca.playground.docs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.datetime.DateTimeLocale
import dev.voir.areca.datetime.LocalDateTimeLocale
import dev.voir.areca.flow.attachments.LocalAttachmentImageLoader
import dev.voir.areca.flow.attachments.LocalAttachmentPdfRenderer
import dev.voir.areca.flow.profile.LocalAvatarImageLoader
import dev.voir.areca.playground.flow.attachments.SampleAttachmentImageLoader
import dev.voir.areca.playground.flow.attachments.SampleAttachmentPdfRenderer
import dev.voir.areca.playground.flow.profile.SampleAvatarLoader
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.ArecaThemeProvider
import org.jetbrains.skia.EncodedImageFormat
import org.jetbrains.skia.Image
import org.jetbrains.skia.Surface
import java.io.File
import kotlin.math.ceil

/** Pixels per dp in the written images, so they stay sharp on a high-density screen. */
private const val DENSITY = 2f

/** Height a panel that fits its content is laid out in before it is cropped to that content. */
private val MeasuringHeight = 2400.dp

/** Time between the frames stepped through before the kept one, in nanoseconds. */
private const val FRAME_NANOS = 50_000_000L

/** How long the clock runs before the kept frame: past every entrance and sheet slide. */
private const val SETTLE_NANOS = 2_000_000_000L

/**
 * Renders the documentation screenshots into a directory, in light and in dark.
 *
 * Not part of `check`: text rasterises differently from one machine to the next, so the images are
 * rendered on demand and committed. Run it through Gradle:
 *
 * ```shell
 * ./gradlew :playground:renderDocImages
 * ./gradlew :playground:renderDocImages -Pshots=charts,datetime
 * ```
 *
 * @param args The output directory, then optionally the names of the shots to render; all of them
 *   when none is named.
 */
fun main(args: Array<String>) {
    require(args.isNotEmpty()) { "Usage: RenderDocImages <output directory> [shot names…]" }
    val output = File(args[0]).apply { mkdirs() }
    val wanted = args.drop(1).flatMap { it.split(',') }.map(String::trim).filter(String::isNotEmpty).toSet()

    val unknown = wanted - DocShots.map(DocShot::name).toSet()
    require(unknown.isEmpty()) { "No such shot: ${unknown.joinToString()}" }

    DocShots.filter { wanted.isEmpty() || it.name in wanted }.forEach { shot ->
        listOf(false, true).forEach { dark ->
            val file = File(output, "${shot.name}-${if (dark) "dark" else "light"}.png")
            file.writeBytes(render(shot, dark))
            println("${file.path}  ${file.length() / 1024} KB")
        }
    }
}

/**
 * Renders [shot] in one theme and encodes it as a PNG.
 *
 * @param shot What to render.
 * @param dark Whether to render in the dark theme.
 * @return The PNG's bytes.
 */
private fun render(shot: DocShot, dark: Boolean): ByteArray {
    // The gap is a panel of bare canvas, so it takes the theme's colour like the rest.
    val gap = DocPanel(width = shot.gap) {}
    val columns = shot.panels.flatMapIndexed { i, panel -> if (i == 0) listOf(panel) else listOf(gap, panel) }
    val panels = columns.map { panel -> renderPanel(panel, shot.height, dark) }
    val height = shot.height?.let(::px) ?: panels.maxOf { it.contentHeight }
    val surface = Surface.makeRasterN32Premul(panels.sumOf { it.image.width }, height)
    var x = 0f
    panels.forEach { panel ->
        surface.canvas.drawImage(panel.image, x, 0f)
        x += panel.image.width
    }
    return requireNotNull(surface.makeImageSnapshot().encodeToData(EncodedImageFormat.PNG)).bytes
}

/**
 * A panel as rendered.
 *
 * @property image The panel's pixels, as tall as the scene it was rendered in.
 * @property contentHeight Height of its content and margin, in pixels.
 */
private class RenderedPanel(val image: Image, val contentHeight: Int)

/**
 * Renders one panel in a scene of its own, letting entrances and animations settle first.
 *
 * @param panel What to render.
 * @param height Fixed logical height, or `null` to lay the panel out tall and keep its content's height.
 * @param dark Whether to render in the dark theme.
 * @return The settled frame, copied out of the scene, and the height its content took.
 */
@OptIn(ExperimentalComposeUiApi::class)
private fun renderPanel(panel: DocPanel, height: Dp?, dark: Boolean): RenderedPanel {
    var contentHeight = 0
    val scene = ImageComposeScene(
        width = px(panel.width),
        height = px(height ?: MeasuringHeight),
        density = Density(DENSITY),
    ) {
        DocFrame(dark = dark) {
            val margin = if (panel.padded) {
                PaddingValues(horizontal = ArecaTheme.spacing.md, vertical = ArecaTheme.spacing.xl3)
            } else {
                PaddingValues(0.dp)
            }
            Box(
                modifier = (if (height != null) Modifier.fillMaxSize() else Modifier.fillMaxWidth())
                    .onSizeChanged { contentHeight = it.height }
                    .padding(margin),
            ) {
                panel.content()
            }
        }
    }
    try {
        var time = 0L
        while (time < SETTLE_NANOS) {
            scene.render(time)
            time += FRAME_NANOS
        }
        val frame = scene.render(SETTLE_NANOS)
        // Copied out, so the pixels outlive the scene's surface.
        val copy = Surface.makeRasterN32Premul(frame.width, frame.height)
        copy.canvas.drawImage(frame, 0f, 0f)
        return RenderedPanel(copy.makeImageSnapshot(), contentHeight)
    } finally {
        scene.close()
    }
}

/**
 * The root an application mounts, as the playground shell mounts it: the theme in the given
 * direction, the sample image loaders, an ISO week and clock so dates do not follow the machine,
 * and the canvas colour filling the frame.
 *
 * @param dark Whether to render in the dark theme.
 * @param content What the frame holds.
 */
@Composable
private fun DocFrame(dark: Boolean, content: @Composable () -> Unit) {
    ArecaThemeProvider(darkTheme = dark) {
        CompositionLocalProvider(
            LocalAvatarImageLoader provides SampleAvatarLoader,
            LocalAttachmentImageLoader provides SampleAttachmentImageLoader,
            LocalAttachmentPdfRenderer provides SampleAttachmentPdfRenderer,
            LocalDateTimeLocale provides DateTimeLocale.Iso,
        ) {
            Box(Modifier.fillMaxSize().background(ArecaTheme.colors.bgCanvas)) { content() }
        }
    }
}

/** [dp] in the images' pixels. */
private fun px(dp: Dp): Int = ceil(dp.value * DENSITY).toInt()
