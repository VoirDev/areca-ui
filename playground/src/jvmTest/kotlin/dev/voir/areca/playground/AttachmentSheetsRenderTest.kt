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
import dev.voir.areca.flow.attachments.AttachmentFormat
import dev.voir.areca.media.LocalMediaImageLoader
import dev.voir.areca.flow.attachments.sheet.AttachmentOptions
import dev.voir.areca.flow.attachments.sheet.AttachmentOptionsSheet
import dev.voir.areca.flow.attachments.sheet.AttachmentRenameSheet
import dev.voir.areca.flow.attachments.sheet.AttachmentSourceOptions
import dev.voir.areca.flow.attachments.sheet.AttachmentSourceSheet
import dev.voir.areca.flow.attachments.sheet.AttachmentTagChips
import dev.voir.areca.flow.attachments.sheet.PendingAttachment
import dev.voir.areca.flow.attachments.sheet.PendingAttachmentSheet
import dev.voir.areca.playground.flow.attachments.AttachmentSamples
import dev.voir.areca.playground.flow.attachments.SampleAttachmentImageLoader
import dev.voir.areca.playground.flow.attachments.SampleFile
import dev.voir.areca.playground.flow.attachments.SampleTag
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.ArecaThemeProvider
import org.jetbrains.skia.EncodedImageFormat
import java.io.File
import kotlin.test.Test

/** Logical width of the device the sheets are rendered at. */
private const val WIDTH = 390

/** Logical height of the device the sheets are rendered at. */
private const val HEIGHT = 700

/** Pixel density the scenes are rasterised at, so the PNGs are legible when opened. */
private const val DENSITY = 2f

/** Frames advanced before the picture is taken, so a sheet has finished sliding up. */
private const val SETTLE_FRAMES = 60

/** Time between those frames, in nanoseconds. */
private const val FRAME_NANOS = 50_000_000L

/** Where the rendered sheets are written. */
private val OUTPUT = File(System.getProperty("render.out") ?: "build/screens")

/**
 * Renders one sheet, open over an empty screen, to a PNG.
 *
 * A sheet is a dialog that slides up once composed, so the scene's clock is advanced until the
 * slide has finished before the picture is taken.
 *
 * @param name File name, without the extension.
 * @param dark Whether to render in the dark theme.
 * @param sheet The sheet, already up.
 */
@OptIn(ExperimentalComposeUiApi::class)
private fun renderSheet(name: String, dark: Boolean, sheet: @Composable () -> Unit) {
    val scene = ImageComposeScene(
        width = (WIDTH * DENSITY).toInt(),
        height = (HEIGHT * DENSITY).toInt(),
        density = Density(DENSITY),
    ) {
        ArecaThemeProvider(darkTheme = dark) {
            CompositionLocalProvider(LocalMediaImageLoader provides SampleAttachmentImageLoader) {
                Box(Modifier.fillMaxSize().background(ArecaTheme.colors.bgCanvas))
                sheet()
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
 * Renders every attachment sheet, open, to `build/screens`, in both themes.
 *
 * Not an assertion — see [AuthScreenRenderTest] for why. It fails when a sheet throws during
 * composition or layout, and it is the only way to look at them on a machine with no display.
 *
 * ```bash
 * ./gradlew :playground:jvmTest --tests '*AttachmentSheetsRenderTest*'
 * ```
 */
class AttachmentSheetsRenderTest {

    @Test
    fun source() = bothThemes("attachment-sheet-source") {
        AttachmentSourceSheet(
            visible = true,
            options = listOf(
                AttachmentSourceOptions.camera {},
                AttachmentSourceOptions.library {},
                AttachmentSourceOptions.files {},
            ),
            onDismiss = {},
            subtitle = "Up to 25 MB each. Photos are resized before they’re saved.",
        )
    }

    @Test
    fun pending() = bothThemes("attachment-sheet-pending") {
        PendingAttachmentSheet(
            file = PendingAttachment(
                fileName = AttachmentSamples.receipt.fileName,
                format = AttachmentSamples.receipt.format,
                size = AttachmentSamples.receipt.size,
                model = AttachmentSamples.receipt.model,
            ),
            onAttach = {},
            onDismiss = {},
            title = "What is this?",
            attachEnabled = false,
            question = {
                AttachmentTagChips(tags = SampleTag.entries, selected = null, onSelect = {}, label = { it.label })
            },
        )
    }

    /** A photo the application lets its owner crop: the row carries Edit. */
    @Test
    fun `pending with edit`() = bothThemes("attachment-sheet-pending-edit") {
        PendingAttachmentSheet(
            file = PendingAttachment(
                fileName = "IMG_1874.jpg",
                format = AttachmentFormat.Image,
                size = "2.2 MB",
                model = SampleFile.LandscapePhoto,
            ),
            onAttach = {},
            onDismiss = {},
            title = "What is this?",
            attachEnabled = false,
            onEdit = {},
            question = {
                AttachmentTagChips(tags = SampleTag.entries, selected = null, onSelect = {}, label = { it.label })
            },
        )
    }

    @Test
    fun `pending while copying`() = bothThemes("attachment-sheet-pending-copying") {
        PendingAttachmentSheet(
            file = PendingAttachment(fileName = "Statement September 2026.pdf", isCopying = true),
            onAttach = {},
            onDismiss = {},
            copyingText = "Adding…",
        )
    }

    @Test
    fun `pending refused`() = bothThemes("attachment-sheet-pending-refused") {
        PendingAttachmentSheet(
            file = PendingAttachment(
                fileName = AttachmentSamples.statement.fileName,
                format = AttachmentSamples.statement.format,
                size = "31.4 MB",
                problem = "This file is larger than 25 MB.",
            ),
            onAttach = {},
            onDismiss = {},
        )
    }

    @Test
    fun options() = bothThemes("attachment-sheet-options") {
        AttachmentOptionsSheet(
            item = AttachmentSamples.invoice,
            choices = listOf(
                AttachmentOptions.rename {},
                AttachmentOptions.share {},
                AttachmentOptions.save {},
                AttachmentOptions.remove {},
            ),
            onDismiss = {},
            tagsTitle = "Type",
            tags = {
                AttachmentTagChips(
                    tags = SampleTag.entries,
                    selected = SampleTag.Invoice,
                    onSelect = {},
                    label = { it.label },
                )
            },
        )
    }

    @Test
    fun rename() = bothThemes("attachment-sheet-rename") {
        AttachmentRenameSheet(
            item = AttachmentSamples.invoice,
            onRename = {},
            onDismiss = {},
            error = "Another file on this expense already has this name.",
        )
    }

    /**
     * Renders [sheet] once in each theme.
     *
     * @param name File name, without the extension or the theme suffix.
     * @param sheet The sheet, already up.
     */
    private fun bothThemes(name: String, sheet: @Composable () -> Unit) {
        renderSheet(name, dark = false, sheet)
        renderSheet(name, dark = true, sheet)
    }
}
