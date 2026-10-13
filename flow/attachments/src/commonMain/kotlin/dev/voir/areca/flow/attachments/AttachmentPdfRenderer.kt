package dev.voir.areca.flow.attachments

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.ImageBitmap

/**
 * An open PDF, drawn one page at a time.
 *
 * Pages are drawn on demand as they scroll into view rather than all at once: a long statement
 * drawn whole at a phone's width is over a hundred megabytes of bitmaps. The viewer closes the
 * document when its page leaves the screen.
 *
 * **[close] can arrive while a page is being drawn.** It is called on the main thread as the page
 * leaves composition, whether or not a [render] is still running, so an implementation must not
 * release what a running render uses until that render is done — with a renderer that throws when
 * closed under an open page, drawing and closing share one lock. A [render] after [close] returns
 * `null`.
 */
public interface AttachmentPdfDocument : AutoCloseable {
    /** How many pages it has. */
    public val pageCount: Int

    /**
     * Draws one page on white, the colour of paper.
     *
     * Called from composition-scoped coroutines, possibly for several pages at once; an
     * implementation over a renderer that opens one page at a time serialises the calls itself.
     *
     * @param index Zero-based page.
     * @param width Width to draw it at, in pixels; the height follows the page's proportions.
     * @return The page, or `null` when it could not be drawn.
     */
    public suspend fun render(index: Int, width: Int): ImageBitmap?
}

/**
 * Opens an application's PDF for the viewer to draw.
 *
 * The module draws no PDF itself. Every platform has a renderer — `PdfRenderer` on Android, PDFKit
 * on iOS — and each needs work this module does not do: Android's reads a seekable file, so the
 * bytes are written to storage first. The application implements this once and provides it through
 * [LocalAttachmentPdfRenderer]; the module README carries implementations for both platforms to
 * copy.
 *
 * With none provided, or where the platform has no renderer, a PDF is offered to another
 * application instead, like any other document.
 */
@Stable
public interface AttachmentPdfRenderer {
    /**
     * Opens [model] as a PDF.
     *
     * Cancelled when the page leaves the screen before the document is open — a swipe past a PDF.
     * A cancelled open must release whatever it had already opened before rethrowing, since nothing
     * else will ever hold it to close.
     *
     * @param model File model from [AttachmentItem.model], never `null`.
     * @return The document, which the caller closes, or `null` when it cannot be drawn here — not a
     *   PDF, or a PDF locked with a password.
     */
    public suspend fun open(model: Any): AttachmentPdfDocument?

    /** The renderer in scope when the application provides none: nothing can be opened. */
    public data object None : AttachmentPdfRenderer {
        /**
         * Opens nothing.
         *
         * @param model File model, ignored.
         * @return `null`, whatever [model] is.
         */
        override suspend fun open(model: Any): AttachmentPdfDocument? = null
    }
}

/**
 * PDF renderer in scope for the viewer.
 *
 * Static because an application provides it once, above everything, and never changes it.
 * Unprovided, it resolves to [AttachmentPdfRenderer.None].
 */
public val LocalAttachmentPdfRenderer: ProvidableCompositionLocal<AttachmentPdfRenderer> =
    staticCompositionLocalOf { AttachmentPdfRenderer.None }
