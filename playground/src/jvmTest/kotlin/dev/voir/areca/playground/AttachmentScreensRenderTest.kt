package dev.voir.areca.playground

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Density
import dev.voir.areca.flow.attachments.LocalAttachmentImageLoader
import dev.voir.areca.flow.attachments.LocalAttachmentPdfRenderer
import dev.voir.areca.playground.flow.attachments.AttachmentDetailsShowcase
import dev.voir.areca.playground.flow.attachments.AttachmentDocumentsShowcase
import dev.voir.areca.playground.flow.attachments.AttachmentFormShowcase
import dev.voir.areca.playground.flow.attachments.SampleAttachmentImageLoader
import dev.voir.areca.playground.flow.attachments.SampleAttachmentPdfRenderer
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.ArecaThemeProvider
import org.jetbrains.skia.EncodedImageFormat
import java.io.File
import kotlin.test.Test

/** Width of the playground window the pages are rendered in. */
private const val WIDTH = 1440

/** Height of that window. */
private const val HEIGHT = 960

/** Frames advanced before the picture is taken, so the device frame has laid out. */
private const val SETTLE_FRAMES = 10

/** Time between those frames, in nanoseconds. */
private const val FRAME_NANOS = 50_000_000L

/** Where the rendered pages are written. */
private val OUTPUT = File(System.getProperty("render.out") ?: "build/screens")

/**
 * Renders one screen page, controls and device frame included, as the playground window shows it.
 *
 * @param name File name, without the extension.
 * @param page The page.
 */
@OptIn(ExperimentalComposeUiApi::class)
private fun renderPage(name: String, page: @Composable () -> Unit) {
    val scene = ImageComposeScene(width = WIDTH, height = HEIGHT, density = Density(1f)) {
        ArecaThemeProvider(darkTheme = false) {
            CompositionLocalProvider(
                LocalAttachmentImageLoader provides SampleAttachmentImageLoader,
                LocalAttachmentPdfRenderer provides SampleAttachmentPdfRenderer,
            ) {
                Box(Modifier.fillMaxSize().background(ArecaTheme.colors.bgCanvas)) { page() }
            }
        }
    }

    try {
        var time = 0L
        repeat(SETTLE_FRAMES) {
            scene.render(time)
            time += FRAME_NANOS
        }
        val data = scene.render(time).encodeToData(EncodedImageFormat.PNG)!!
        OUTPUT.mkdirs()
        File(OUTPUT, "$name.png").writeBytes(data.bytes)
    } finally {
        scene.close()
    }
}

/**
 * Renders the attachments screen pages to `build/screens`, as the playground shows them.
 *
 * Not an assertion — see [AuthScreenRenderTest] for why. It fails when a recipe throws during
 * composition or layout, and it is the only way to look at them on a machine with no display.
 *
 * ```bash
 * ./gradlew :playground:jvmTest --tests '*AttachmentScreensRenderTest*'
 * ```
 */
class AttachmentScreensRenderTest {

    @Test
    fun form() = renderPage("attachment-screen-form") { AttachmentFormShowcase() }

    @Test
    fun details() = renderPage("attachment-screen-details") { AttachmentDetailsShowcase() }

    @Test
    fun documents() = renderPage("attachment-screen-documents") { AttachmentDocumentsShowcase() }
}
