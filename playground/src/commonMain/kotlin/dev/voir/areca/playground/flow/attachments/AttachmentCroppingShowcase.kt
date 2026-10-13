package dev.voir.areca.playground.flow.attachments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import dev.voir.areca.crop.CropAspect
import dev.voir.areca.crop.CropSpec
import dev.voir.areca.flow.attachments.AttachmentFormat
import dev.voir.areca.flow.attachments.AttachmentState
import dev.voir.areca.flow.attachments.part.AttachmentStrip
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.playground.crop.CropScreen
import dev.voir.areca.playground.showcase.FlowShowcase
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.canvas.DeviceFrame
import dev.voir.areca.playground.showcase.canvas.DeviceSize
import dev.voir.areca.playground.showcase.canvas.windowInsets
import dev.voir.areca.playground.showcase.chrome.ChromeText
import dev.voir.areca.playground.showcase.chrome.ChromeTextRole
import dev.voir.areca.playground.showcase.chrome.ChromeTone
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.horizontalContentInsets

/**
 * How the sample application crops a picked photo.
 *
 * @property label Name in the control.
 * @property spec What the crop screen is opened with.
 */
private enum class AttachmentCrop(val label: String, val spec: CropSpec) {
    /** Any proportions, with the common ratios offered, kept at up to 2048 px. */
    Free("free + ratios", CropSpec.Photo.copy(maxOutputDimension = 2048)),

    /** A fixed square, for a product whose files are shown in a grid. */
    Square(
        "square, locked",
        CropSpec(aspect = CropAspect.Square, isAspectLocked = true, maxOutputDimension = 1080),
    ),
}

/** Photos cropped before they are attached; other files attached as they are. */
@Composable
fun AttachmentCroppingShowcase() = FlowShowcase(
    title = "Cropping",
    summary = "A picked photo can be cropped before it is attached: the pending sheet offers Edit, the " +
        "application's crop screen opens, and the crop comes back as the file.",
    module = "flow-attachments",
    status = ShowcaseStatus.Beta,
    notes = {
        ChromeText(text = "The recipe", role = ChromeTextRole.Label)
        ChromeText(
            text = "ReceiptsRecipe in this file is the screen. The module only offers Edit — it passes " +
                "PendingAttachmentSheet an onEdit for photos and null for anything else, so a PDF has " +
                "no Edit. The crop screen is the application's own, from :ui:crop, opened with this " +
                "screen's CropSpec; the module never depends on it.",
            role = ChromeTextRole.SectionDescription,
            tone = ChromeTone.Muted,
        )
        ChromeText(
            text = "Add from the camera or library, wait for the copy, press Edit, crop, and press Done: " +
                "the sheet comes back with the crop as the file. Add from Files for a PDF without Edit.",
            role = ChromeTextRole.SectionDescription,
            tone = ChromeTone.Muted,
        )
    },
) {
    val crop by enumControl(
        key = "crop",
        default = AttachmentCrop.Free,
        options = AttachmentCrop.entries,
        optionLabel = { it.label },
    )
    val editable by booleanControl(
        key = "photos can be edited",
        default = true,
        description = "Off, the application passes no onEdit and the sheet shows no Edit.",
    )

    preview {
        val attachments = rememberSampleAttachments(SampleExpense.Empty.files)
        attachments.photographs = true

        DeviceFrame(size = DeviceSize.Regular) {
            ReceiptsRecipe(
                attachments = attachments,
                spec = crop.spec,
                photosEditable = editable,
                windowInsets = DeviceSize.Regular.windowInsets,
            )
        }
    }
}

/**
 * The recipe: a screen of receipts whose photos can be cropped before they are attached.
 *
 * @param attachments The screen's files and which sheet is up.
 * @param spec What the crop screen is opened with.
 * @param photosEditable Whether this product crops photos at all.
 * @param windowInsets Regions the system occupies.
 */
@Composable
private fun ReceiptsRecipe(
    attachments: SampleAttachments,
    spec: CropSpec,
    photosEditable: Boolean,
    windowInsets: WindowInsets,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ArecaTheme.colors.bgCanvas)
                .windowInsetsPadding(windowInsets),
        ) {
            Spacer(Modifier.height(ArecaTheme.spacing.xl3))

            Text(
                text = "Receipts",
                modifier = Modifier
                    .horizontalContentInsets()
                    .semantics { heading() },
                style = ArecaTheme.typography.headingSmall,
                color = ArecaTheme.colors.fgBase,
            )

            Spacer(Modifier.height(ArecaTheme.spacing.lg))

            AttachmentStrip(
                items = attachments.files,
                onOpen = attachments::open,
                onAdd = if (attachments.canAdd) attachments::add else null,
                onRemove = attachments::remove,
                title = "Attachments",
                counter = attachments.counter,
                emptyTitle = "Add a receipt",
                emptyDescription = "Photos can be cropped before they’re attached",
                failure = attachments.failure,
                caption = { item -> if (item.state == AttachmentState.Adding) "Adding…" else item.label },
                addCaption = "Add",
            )
        }

        // The application's crop screen, over the screen while it has the file. A product with
        // navigation opens it as a destination instead.
        val editing = attachments.editing
        val photo = editing?.let { decodeSamplePhoto(it.model) }
        if (photo != null) {
            CropScreen(
                photo = photo,
                spec = spec,
                title = "Crop photo",
                onCancel = attachments::cancelEdit,
                onDone = attachments::finishEdit,
                windowInsets = windowInsets,
            )
        }
    }

    AttachmentSheetsRecipe(
        attachments = attachments,
        confirmRemoval = false,
        // The application's rule, per file: photos can be cropped; PDFs and documents cannot.
        onEdit = if (photosEditable && attachments.pending?.format == AttachmentFormat.Image) {
            attachments::startEdit
        } else {
            null
        },
    )
}
