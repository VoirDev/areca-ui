package dev.voir.areca.playground.flow.attachments

import androidx.compose.runtime.Composable
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
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.painter.BrushPainter
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import dev.voir.areca.flow.attachments.AttachmentFormat
import dev.voir.areca.flow.attachments.AttachmentItem
import dev.voir.areca.media.MediaImageLoader
import dev.voir.areca.media.MediaImageState
import dev.voir.areca.flow.attachments.AttachmentPdfDocument
import dev.voir.areca.flow.attachments.AttachmentPdfRenderer
import dev.voir.areca.flow.attachments.AttachmentState
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.playground.crop.SamplePhoto
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.button.ButtonSize
import dev.voir.areca.foundation.button.ButtonStyle

/**
 * Demo content shared by every attachments page — the recipe screens and the part pages — so they
 * document one expense's files rather than many invented ones.
 */
internal object AttachmentSamples {

    /** A photographed receipt, on this device. */
    val receipt = AttachmentItem(
        key = "receipt",
        fileName = "IMG_2041.jpg",
        label = "Receipt",
        size = "1.8 MB",
        format = AttachmentFormat.Image,
        model = SampleFile.ReceiptPhoto,
    )

    /** A supplier's invoice, a two-page PDF on this device. */
    val invoice = AttachmentItem(
        key = "invoice",
        fileName = "Invoice INV-2026-0412.pdf",
        label = "Invoice",
        size = "214 KB",
        format = AttachmentFormat.Pdf,
        model = SampleFile.InvoicePdf,
    )

    /** A bank statement, a five-page PDF on this device. */
    val statement = AttachmentItem(
        key = "statement",
        fileName = "Statement September 2026.pdf",
        label = "Statement",
        size = "1.1 MB",
        format = AttachmentFormat.Pdf,
        model = SampleFile.StatementPdf,
    )

    /** A word-processor document: on this device, but only another application can show it. */
    val contract = AttachmentItem(
        key = "contract",
        fileName = "Lease agreement.docx",
        label = "Contract",
        size = "86 KB",
        format = AttachmentFormat.Document,
        model = SampleFile.Document,
    )

    /** A photographed warranty card that is on the server only. */
    val warranty = AttachmentItem(
        key = "warranty",
        fileName = "IMG_1988.jpg",
        label = "Warranty",
        size = "2.4 MB",
        format = AttachmentFormat.Image,
        state = AttachmentState.Remote,
    )

    /** The files of one expense, as a strip or the viewer shows them. */
    val expenseFiles: List<AttachmentItem> = listOf(receipt, invoice, contract, warranty)

    /** Files a page adds one at a time when its add square is pressed, in order. */
    val filesToAdd: List<AttachmentItem> = listOf(
        receipt.copy(key = "photo-2", fileName = "IMG_2057.jpg", label = "Photo", size = "2.1 MB"),
        statement,
        receipt.copy(key = "photo-3", fileName = "IMG_2058.jpg", label = "Receipt", size = "1.6 MB"),
        invoice.copy(key = "invoice-2", fileName = "Invoice INV-2026-0419.pdf", size = "198 KB"),
    )
}

/**
 * The files the part pages let a reader choose between, one per way a square can look.
 *
 * @property label Name shown in the control.
 * @property item The file.
 */
internal enum class SampleFileOption(val label: String, val item: AttachmentItem) {
    /** A photograph that decodes. */
    Photo("Photo", AttachmentSamples.receipt),

    /** A PDF: its glyph and extension, never a stand-in picture. */
    Pdf("PDF", AttachmentSamples.invoice),

    /** A document only another application opens. */
    Document("Document", AttachmentSamples.contract),

    /** A photograph whose picture has not decoded yet: its glyph shows until it does. */
    SlowPhoto("Slow photo", AttachmentSamples.receipt.copy(key = "slow", model = SampleFile.SlowPhoto)),

    /** A photograph that cannot be decoded: its glyph stays. */
    BrokenPhoto("Broken photo", AttachmentSamples.receipt.copy(key = "broken", model = SampleFile.BrokenPhoto)),
}

/**
 * Every [AttachmentState], as the part pages offer it.
 *
 * @property label Name shown in the control.
 */
internal enum class SampleStateOption(val label: String) {
    /** [AttachmentState.Adding]. */
    Adding("Adding"),

    /** [AttachmentState.Uploading]. */
    Uploading("Uploading"),

    /** [AttachmentState.Ready]. */
    Ready("Ready"),

    /** [AttachmentState.Remote]. */
    Remote("Remote"),

    /** [AttachmentState.Downloading]. */
    Downloading("Downloading"),

    /** [AttachmentState.Waiting]. */
    Waiting("Waiting"),

    /** [AttachmentState.Unavailable]. */
    Unavailable("Unavailable"),
    ;

    /**
     * [item] in this state, as an application would report it: a file whose bytes are elsewhere
     * carries no model.
     *
     * @param item The file.
     * @param progress Share of a transfer done, `0..1`, or `null` when not known.
     * @return The file in this state.
     */
    fun applyTo(item: AttachmentItem, progress: Float? = DefaultProgress): AttachmentItem = when (this) {
        Adding -> item.copy(state = AttachmentState.Adding)
        Uploading -> item.copy(state = AttachmentState.Uploading(progress))
        Ready -> item.copy(state = AttachmentState.Ready)
        Remote -> item.copy(state = AttachmentState.Remote, model = null)
        Downloading -> item.copy(state = AttachmentState.Downloading(progress), model = null)
        Waiting -> item.copy(state = AttachmentState.Waiting, model = null)
        Unavailable -> item.copy(state = AttachmentState.Unavailable, model = null)
    }

    private companion object {
        /** Progress a transfer is shown at when a page does not choose one. */
        const val DefaultProgress = 0.42f
    }
}

/**
 * File models the sample loader and renderer understand, standing in for an application's URIs or
 * keys.
 */
internal enum class SampleFile {
    /** A picture that loads. */
    ReceiptPhoto,

    /** A picture that is still decoding. */
    SlowPhoto,

    /** A picture that cannot be decoded. */
    BrokenPhoto,

    /** A two-page PDF. */
    InvoicePdf,

    /** A five-page PDF. */
    StatementPdf,

    /** A document neither the loader nor the renderer can draw. */
    Document,

    /** A full photograph, which [decodeSamplePhoto] can hand to the crop screen. */
    LandscapePhoto,
}

/**
 * Stands in for the application's decoder: the full picture behind a model, for the crop screen.
 *
 * @param model What a picked or cropped file is read from.
 * @return The picture, or `null` for a model that is not a photograph the sample can open.
 */
internal fun decodeSamplePhoto(model: Any?): Painter? = when (model) {
    SampleFile.LandscapePhoto -> SamplePhoto
    is ImageBitmap -> BitmapPainter(model, filterQuality = FilterQuality.High)
    else -> null
}

/**
 * Stands in for an application's image loader.
 *
 * Draws a gradient for [SampleFile.ReceiptPhoto] rather than decoding anything, so the playground
 * runs offline and shows every appearance a picture can take on demand. A cropped file's model is the
 * crop itself, an [ImageBitmap], and is drawn as it is.
 */
internal object SampleAttachmentImageLoader : MediaImageLoader {
    /** @return The state the sample model stands for. */
    @Composable
    override fun load(model: Any, maxSize: Int): MediaImageState {
        val picture = remember {
            MediaImageState.Ready(
                BrushPainter(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFFE9E4DA), Color(0xFFB9B1A2)),
                        start = Offset.Zero,
                        end = Offset.Infinite,
                    ),
                ),
            )
        }

        return when (model) {
            SampleFile.ReceiptPhoto -> picture
            SampleFile.LandscapePhoto, is ImageBitmap -> {
                val painter = remember(model) { decodeSamplePhoto(model) }
                if (painter == null) MediaImageState.Failed else MediaImageState.Ready(painter)
            }
            SampleFile.SlowPhoto -> MediaImageState.Loading
            else -> MediaImageState.Failed
        }
    }
}

/**
 * Stands in for an application's PDF renderer.
 *
 * Draws each page as grey bars of "print" on white, so the viewer's page column, placeholders and
 * page counter can be seen without a real PDF or a platform renderer.
 */
internal object SampleAttachmentPdfRenderer : AttachmentPdfRenderer {
    /** @return A sample document for the two PDF models, `null` for anything else. */
    override suspend fun open(model: Any): AttachmentPdfDocument? = when (model) {
        SampleFile.InvoicePdf -> SamplePdfDocument(pageCount = 2)
        SampleFile.StatementPdf -> SamplePdfDocument(pageCount = 5)
        else -> null
    }
}

/**
 * A document of blank A4 pages with bars where the text would be.
 *
 * @property pageCount How many pages it has.
 */
private class SamplePdfDocument(override val pageCount: Int) : AttachmentPdfDocument {
    /** @return The page drawn at [width], A4-proportioned, or `null` for a page it does not have. */
    override suspend fun render(index: Int, width: Int): ImageBitmap? {
        if (index !in 0 until pageCount || width <= 0) return null

        val height = (width * A4_HEIGHT_RATIO).toInt()
        val bitmap = ImageBitmap(width, height)
        val margin = width * 0.08f
        val line = width * 0.018f
        val gap = line * 1.8f

        CanvasDrawScope().draw(
            density = Density(1f),
            layoutDirection = LayoutDirection.Ltr,
            canvas = Canvas(bitmap),
            size = Size(width.toFloat(), height.toFloat()),
        ) {
            drawRect(Color.White)

            // A heading on the first page only, the way a statement or an invoice opens.
            var top = margin
            if (index == 0) {
                drawRoundRect(
                    color = HeadingInk,
                    topLeft = Offset(margin, top),
                    size = Size(size.width * 0.45f, line * 2f),
                    cornerRadius = CornerRadius(line / 2),
                )
                top += line * 5f
            }

            var row = 0
            while (top + line < size.height - margin) {
                // Ragged right, repeating every seven rows, so the page reads as prose.
                val share = LINE_SHARES[row % LINE_SHARES.size]
                drawRoundRect(
                    color = BodyInk,
                    topLeft = Offset(margin, top),
                    size = Size((size.width - margin * 2) * share, line),
                    cornerRadius = CornerRadius(line / 2),
                )
                top += line + gap
                row++
            }
        }

        return bitmap
    }

    /** Nothing to release. */
    override fun close() = Unit
}

/** Height over width of an A4 page. */
private const val A4_HEIGHT_RATIO = 1.414f

/** Share of the text width each row of "print" fills, in turn. */
private val LINE_SHARES = listOf(1f, 0.94f, 0.98f, 0.6f, 1f, 0.9f, 0.4f)

/** Colour of a sample page's heading. */
private val HeadingInk = Color(0xFF505457)

/** Colour of a sample page's body text. */
private val BodyInk = Color(0xFFCED1D3)

/**
 * What an expense's file can be, as a product would tag it — the playground's stand-in for an
 * application's own enum.
 *
 * @property label How the tag is worded on its chip.
 */
internal enum class SampleTag(val label: String) {
    /** A till receipt. */
    Receipt("Receipt"),

    /** A supplier's invoice. */
    Invoice("Invoice"),

    /** A warranty card or certificate. */
    Warranty("Warranty"),

    /** A contract or agreement. */
    Contract("Contract"),

    /** A bank or card statement. */
    Statement("Statement"),

    /** A photograph of the purchase itself. */
    Photo("Photo"),

    /** Anything else. */
    Other("Other"),
}

/**
 * A button that opens a sheet, and the sheet it opens — how every sheet page puts a sheet in a
 * variant or an edge case.
 *
 * @param label Caption of the button.
 * @param sheet The sheet, given whether it is up and how to close it.
 */
@Composable
internal fun SheetTrigger(
    label: String = "Open sheet",
    sheet: @Composable (visible: Boolean, close: () -> Unit) -> Unit,
) {
    var visible by remember { mutableStateOf(false) }

    Button(
        text = label,
        onClick = { visible = true },
        style = ButtonStyle.soft(ActionColor.Neutral),
        size = ButtonSize.Small,
    )

    sheet(visible) { visible = false }
}
