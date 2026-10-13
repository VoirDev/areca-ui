package dev.voir.areca.flow.attachments

/**
 * How a file can be drawn inside the application, decided by what its bytes are rather than by
 * what its owner called it.
 *
 * Only two formats are drawn in place, because only those two have a renderer on every platform: a
 * picture, drawn zoomable, and a PDF, drawn page by page. Everything else is named, sized and
 * handed to another application.
 */
public enum class AttachmentFormat {
    /** A photograph or a picture, drawn through [AttachmentImageLoader]. */
    Image,

    /** A PDF, drawn page by page through [AttachmentPdfRenderer]. */
    Pdf,

    /** Anything else: named, sized, and opened elsewhere. */
    Document,
    ;

    /** Recognises a file's format. */
    public companion object {
        /**
         * How a file can be drawn, from its media type — and from its extension when the type is
         * missing or is the generic one a file picker falls back to.
         *
         * @param contentType Media type of the bytes, or `null` when not known.
         * @param fileName Name the file goes by.
         * @return The format.
         */
        public fun of(contentType: String?, fileName: String): AttachmentFormat {
            val type = contentType?.lowercase()?.substringBefore(';')?.trim().orEmpty()
            val extension = fileExtension(fileName)?.lowercase()

            return when {
                type.startsWith("image/") -> Image
                type == "application/pdf" -> Pdf
                type.isNotEmpty() && type != GENERIC_TYPE -> Document
                extension in IMAGE_EXTENSIONS -> Image
                extension == "pdf" -> Pdf
                else -> Document
            }
        }

        /** The type a picker reports when it does not know what the bytes are. */
        private const val GENERIC_TYPE = "application/octet-stream"

        /** Extensions read as a picture when the media type says nothing. */
        private val IMAGE_EXTENSIONS = setOf("jpg", "jpeg", "png", "gif", "webp", "heic", "heif", "bmp")
    }
}
