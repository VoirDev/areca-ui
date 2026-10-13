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
import dev.voir.areca.flow.attachments.AttachmentItem
import dev.voir.areca.flow.attachments.AttachmentState
import dev.voir.areca.flow.attachments.LocalAttachmentImageLoader
import dev.voir.areca.flow.attachments.LocalAttachmentPdfRenderer
import dev.voir.areca.flow.attachments.viewer.AttachmentViewer
import dev.voir.areca.flow.attachments.viewer.AttachmentViewerPage
import dev.voir.areca.playground.flow.attachments.AttachmentSamples
import dev.voir.areca.playground.flow.attachments.SampleAttachmentImageLoader
import dev.voir.areca.playground.flow.attachments.SampleAttachmentPdfRenderer
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.ArecaThemeProvider
import org.jetbrains.skia.EncodedImageFormat
import java.io.File
import kotlin.test.Test

/** Logical width of the device the viewer is rendered at. */
private const val WIDTH = 390

/** Logical height of the device the viewer is rendered at. */
private const val HEIGHT = 780

/** Pixel density the scenes are rasterised at, so the PNGs are legible when opened. */
private const val DENSITY = 2f

/** Frames advanced before the picture is taken, so pages have opened and drawn. */
private const val SETTLE_FRAMES = 40

/** Time between those frames, in nanoseconds. */
private const val FRAME_NANOS = 50_000_000L

/** Where the rendered viewers are written. */
private val OUTPUT = File(System.getProperty("render.out") ?: "build/screens")

/**
 * Renders the viewer, open, to a PNG, with the sample loader and renderer in scope.
 *
 * @param name File name, without the extension.
 * @param dark Whether to render in the dark theme.
 * @param viewer The viewer.
 */
@OptIn(ExperimentalComposeUiApi::class)
private fun renderViewer(name: String, dark: Boolean, viewer: @Composable () -> Unit) {
    val scene = ImageComposeScene(
        width = (WIDTH * DENSITY).toInt(),
        height = (HEIGHT * DENSITY).toInt(),
        density = Density(DENSITY),
    ) {
        ArecaThemeProvider(darkTheme = dark) {
            CompositionLocalProvider(
                LocalAttachmentImageLoader provides SampleAttachmentImageLoader,
                LocalAttachmentPdfRenderer provides SampleAttachmentPdfRenderer,
            ) {
                Box(Modifier.fillMaxSize().background(ArecaTheme.colors.bgCanvas))
                viewer()
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
        File(OUTPUT, "$name${if (dark) "-dark" else ""}.png").writeBytes(data.bytes)
    } finally {
        scene.close()
    }
}

/**
 * Renders the viewer open on each kind of page to `build/screens`, in both themes.
 *
 * Not an assertion — see [AuthScreenRenderTest] for why. It fails when the viewer throws during
 * composition or layout, and it is the only way to look at it on a machine with no display.
 *
 * ```bash
 * ./gradlew :playground:jvmTest --tests '*AttachmentViewerRenderTest*'
 * ```
 */
class AttachmentViewerRenderTest {

    @Test
    fun picture() = bothThemes("attachment-viewer-picture") {
        Viewer(AttachmentSamples.expenseFiles, AttachmentSamples.receipt.key)
    }

    @Test
    fun pdf() = bothThemes("attachment-viewer-pdf") {
        Viewer(AttachmentSamples.expenseFiles + AttachmentSamples.statement, AttachmentSamples.statement.key)
    }

    @Test
    fun document() = bothThemes("attachment-viewer-document") {
        Viewer(AttachmentSamples.expenseFiles, AttachmentSamples.contract.key, notice = "Saved to Downloads.")
    }

    @Test
    fun downloading() = bothThemes("attachment-viewer-downloading") {
        Viewer(
            items = listOf(AttachmentSamples.warranty.copy(state = AttachmentState.Downloading(progress = 0.42f))),
            initialKey = AttachmentSamples.warranty.key,
        )
    }

    @Test
    fun waiting() = bothThemes("attachment-viewer-waiting") {
        Viewer(
            items = listOf(AttachmentSamples.warranty.copy(state = AttachmentState.Waiting)),
            initialKey = AttachmentSamples.warranty.key,
        )
    }

    /**
     * Renders [viewer] once in each theme.
     *
     * @param name File name, without the extension or the theme suffix.
     * @param viewer The viewer.
     */
    private fun bothThemes(name: String, viewer: @Composable () -> Unit) {
        renderViewer(name, dark = false, viewer)
        renderViewer(name, dark = true, viewer)
    }
}

/**
 * The viewer as an application wires it, with every action offered and its optional words given.
 *
 * @param items The files.
 * @param initialKey The file to open on.
 * @param notice Line over the file, or `null`.
 */
@Composable
private fun Viewer(items: List<AttachmentItem>, initialKey: Any, notice: String? = null) {
    AttachmentViewer(
        items = items,
        initialKey = initialKey,
        onDismiss = {},
        onShare = {},
        onMore = {},
        notice = notice,
        page = { item ->
            AttachmentViewerPage(
                item = item,
                onRetry = {},
                onOpenElsewhere = {},
                onSave = {},
                waitingText = "It’s saved to your account and will download once you’re back online.",
                elsewhereText = "This kind of file can’t be shown here. Open it in another app to see it.",
            )
        },
    )
}
