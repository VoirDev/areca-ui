package dev.voir.areca.flow.attachments

import androidx.compose.runtime.Immutable

/**
 * One attached file, worded and ready to draw.
 *
 * Everything here is what the application already knows, put in the shape every part of the module
 * reads: the strip's square, the viewer's page, the options sheet's heading. The module never opens
 * the file itself — it hands [model] to the application's [AttachmentImageLoader] or
 * [AttachmentPdfRenderer] and draws what comes back.
 *
 * @property key Stable identity of the file, used — as its `toString` — to key its square in a lazy
 *   row and its page in the viewer, since Android can only save a lazy key that is a string or a
 *   number. Anything whose `toString` is stable and unique among the files: an id, a URI, a string.
 * @property fileName Name the file goes by, extension included. Stated in the viewer and on the
 *   options sheet; its extension is printed on a square that cannot show the file itself.
 * @property label What the file is called under its square and over the viewer. A word its owner
 *   chose ("Receipt") reads better in a strip than `IMG_2041.jpg`, which is why this is separate
 *   from [fileName]; it defaults to the name for an application that has no such word.
 * @property size Its length, already worded by the application ("1.2 MB"), or `null` to state none.
 * @property format How the file can be drawn; see [AttachmentFormat.of].
 * @property state Where the file's bytes are, and whether something is happening to them.
 * @property model What the application's loader and renderer read the file from — a URI, a path,
 *   bytes, a key of its own — passed through untouched. `null` while the bytes are not on this
 *   device, when the file is drawn as its kind of file rather than as itself.
 */
@Immutable
public data class AttachmentItem(
    public val key: Any,
    public val fileName: String,
    public val label: String = fileName,
    public val size: String? = null,
    public val format: AttachmentFormat = AttachmentFormat.Document,
    public val state: AttachmentState = AttachmentState.Ready,
    public val model: Any? = null,
) {
    /** The file's extension in capitals ("PDF"), or `null` when its name carries none. */
    public val extension: String?
        get() = fileExtension(fileName)?.uppercase()
}

/**
 * The extension of [fileName], as written, without its dot.
 *
 * A name that only starts with a dot (`.env`) has no extension, and a trailing part longer than
 * [MAX_EXTENSION_LENGTH] is a name with a dot in it (`Invoice 12.03.2026 corrected`), not an
 * extension worth printing on a square.
 *
 * @param fileName Name the file goes by.
 * @return The extension, or `null` when there is none.
 */
internal fun fileExtension(fileName: String): String? {
    val dot = fileName.lastIndexOf('.')
    if (dot <= 0) return null

    return fileName.substring(dot + 1)
        .takeIf { extension -> extension.isNotEmpty() && extension.length <= MAX_EXTENSION_LENGTH }
}

/** Longest trailing part of a name that is still read as an extension. */
private const val MAX_EXTENSION_LENGTH = 5
