package dev.voir.areca.playground

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Density
import dev.voir.areca.media.LocalMediaImageLoader
import dev.voir.areca.flow.attachments.part.AttachmentIndicator
import dev.voir.areca.flow.attachments.part.AttachmentStrip
import dev.voir.areca.flow.attachments.part.AttachmentThumbnail
import dev.voir.areca.flow.attachments.part.AttachmentTileStyle
import dev.voir.areca.flow.attachments.row.AttachmentRow
import dev.voir.areca.foundation.settings.Section
import dev.voir.areca.foundation.settings.Settings
import dev.voir.areca.foundation.settings.SettingsRow
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.playground.flow.attachments.AttachmentSamples
import dev.voir.areca.playground.flow.attachments.SampleAttachmentImageLoader
import dev.voir.areca.playground.flow.attachments.SampleFileOption
import dev.voir.areca.playground.flow.attachments.SampleStateOption
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.ArecaThemeProvider
import dev.voir.areca.window.InputMode
import org.jetbrains.skia.EncodedImageFormat
import java.io.File
import kotlin.test.Test

/** Logical width of the device the parts are rendered at. */
private const val WIDTH = 390

/** Logical height of the device the parts are rendered at. */
private const val HEIGHT = 1100

/** Pixel density the scenes are rasterised at, so the PNGs are legible when opened. */
private const val DENSITY = 2f

/** Where the rendered screens are written. */
private val OUTPUT = File(System.getProperty("render.out") ?: "build/screens")

/**
 * Renders one screen of attachment parts to a PNG, with the sample image loader in scope.
 *
 * @param name File name, without the extension.
 * @param dark Whether to render in the dark theme.
 * @param content Screen to render.
 */
@OptIn(ExperimentalComposeUiApi::class)
private fun render(name: String, dark: Boolean, content: @Composable () -> Unit) {
    ImageComposeScene(
        width = (WIDTH * DENSITY).toInt(),
        height = (HEIGHT * DENSITY).toInt(),
        density = Density(DENSITY),
    ) {
        ArecaThemeProvider(darkTheme = dark, inputMode = InputMode.Touch) {
            CompositionLocalProvider(LocalMediaImageLoader provides SampleAttachmentImageLoader) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .background(ArecaTheme.colors.bgCanvas)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xl3),
                ) {
                    Spacer(Modifier.height(ArecaTheme.spacing.xs))
                    content()
                }
            }
        }
    }.let { scene ->
        try {
            val data = scene.render().encodeToData(EncodedImageFormat.PNG)!!
            OUTPUT.mkdirs()
            File(OUTPUT, "$name${if (dark) "-dark" else ""}.png").writeBytes(data.bytes)
        } finally {
            scene.close()
        }
    }
}

/**
 * Renders the attachment parts to `build/screens`, in both themes.
 *
 * Not an assertion — see [AuthScreenRenderTest] for why. It fails when any part throws during
 * composition or layout, and it is the only way to look at them on a machine with no display.
 *
 * ```bash
 * ./gradlew :playground:jvmTest --tests '*AttachmentsRenderTest*'
 * ```
 */
class AttachmentsRenderTest {

    @Test
    fun strips() = bothThemes("attachments-strips") { Strips() }

    @Test
    fun squares() = bothThemes("attachments-squares") { Squares() }

    @Test
    fun rows() = bothThemes("attachments-rows") { Rows() }

    /**
     * Renders [content] once in each theme.
     *
     * @param name File name, without the extension or the theme suffix.
     * @param content Screen to render.
     */
    private fun bothThemes(name: String, content: @Composable () -> Unit) {
        render(name, dark = false, content)
        render(name, dark = true, content)
    }
}

/** The strip empty, on a form, at every state, failing, and bled inside a padded card. */
@Composable
private fun Strips() {
    AttachmentStrip(
        items = emptyList(),
        onOpen = {},
        onAdd = {},
        title = "Attachments",
        emptyTitle = "Add a receipt or file",
        emptyDescription = "Photos, PDFs and documents, up to 25\u00A0MB each",
    )
    AttachmentStrip(
        items = AttachmentSamples.expenseFiles,
        onOpen = {},
        onAdd = {},
        onRemove = {},
        title = "Attachments",
        counter = "4 of 20",
        addCaption = "Add",
    )
    AttachmentStrip(
        items = SampleStateOption.entries.map { option ->
            option.applyTo(AttachmentSamples.receipt).copy(key = option.name, label = option.label)
        },
        onOpen = {},
        title = "Every state",
    )
    AttachmentStrip(
        items = AttachmentSamples.expenseFiles.take(2),
        onOpen = {},
        onAdd = {},
        onRemove = {},
        title = "Attachments",
        failure = "This file is larger than 25 MB.",
        addCaption = "Add",
    )
    Column(
        modifier = Modifier
            .padding(horizontal = ArecaTheme.spacing.horizontalContentInsets)
            .fillMaxWidth()
            .clip(ArecaTheme.shapes.lg)
            .background(ArecaTheme.colors.bgSubtle)
            .padding(ArecaTheme.spacing.horizontalContentInsets),
    ) {
        AttachmentStrip(
            items = AttachmentSamples.expenseFiles,
            onOpen = {},
            title = "Attachments",
            tileSize = ArecaTheme.spacing.xl9,
            detail = { item -> item.size },
            bleed = true,
            // White squares on the grey card, as the README advises.
            style = AttachmentTileStyle.default(bg = ArecaTheme.colors.bgSurface),
        )
    }
}

/** Every sample file as a square, and every state at the row's size with its marks off. */
@Composable
private fun Squares() {
    Row(
        modifier = Modifier.padding(horizontal = ArecaTheme.spacing.horizontalContentInsets),
        horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
    ) {
        SampleFileOption.entries.take(4).forEach { option ->
            AttachmentThumbnail(item = option.item, size = ArecaTheme.spacing.xl8)
        }
    }
    Row(
        modifier = Modifier.padding(horizontal = ArecaTheme.spacing.horizontalContentInsets),
        horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SampleStateOption.entries.forEach { option ->
            AttachmentThumbnail(
                item = option.applyTo(AttachmentSamples.receipt),
                size = ArecaTheme.spacing.xl5,
                marks = false,
            )
        }
    }
}

/** A section of rows in every state, and an activity row carrying the paperclip. */
@Composable
private fun Rows() {
    Settings(modifier = Modifier.fillMaxWidth()) {
        Section(header = { Text("Documents") }) {
            SampleStateOption.entries.forEach { option ->
                val item = option.applyTo(AttachmentSamples.invoice)
                AttachmentRow(
                    item = item,
                    onClick = {},
                    title = item.fileName,
                    subtitle = listOfNotNull(option.label, item.size).joinToString(" · "),
                )
            }
        }
        Section {
            SettingsRow(
                title = { Text("Grocery store") },
                subtitle = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xs),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        AttachmentIndicator(count = "2", contentDescription = "2 attachments")
                        Text("Food · 14:32")
                    }
                },
                value = { Text("−42.80 €") },
            )
        }
    }
}
