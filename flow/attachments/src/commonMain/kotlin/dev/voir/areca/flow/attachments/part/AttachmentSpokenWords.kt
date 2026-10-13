package dev.voir.areca.flow.attachments.part

import dev.voir.areca.flow.attachments.AttachmentItem
import dev.voir.areca.flow.attachments.AttachmentState

/**
 * What a file's square is called for assistive technology when the application words nothing.
 *
 * The label and the name, so a strip captioned with tags still says which file is which; the name
 * alone when the label is the name, rather than reading it twice.
 *
 * @param item The file.
 * @return The English name of the square.
 */
internal fun defaultDescription(item: AttachmentItem): String =
    if (item.label == item.fileName) item.label else "${item.label}, ${item.fileName}"

/**
 * Where a file's bytes are, in English, for assistive technology — what the corner disc, the ring
 * or the veil only draws.
 *
 * @param state Where the bytes are.
 * @return The words, or `null` for [AttachmentState.Ready], which is the ordinary case and needs none.
 */
internal fun defaultStateDescription(state: AttachmentState): String? = when (state) {
    AttachmentState.Adding -> "Adding"
    is AttachmentState.Uploading -> withPercent("Uploading", state.progress)
    AttachmentState.Ready -> null
    AttachmentState.Remote -> "Not downloaded"
    is AttachmentState.Downloading -> withPercent("Downloading", state.progress)
    AttachmentState.Waiting -> "Waiting to download"
    AttachmentState.Unavailable -> "Unavailable"
}

/**
 * [words] followed by how far a transfer has come, when that is known.
 *
 * @param words What is happening.
 * @param progress Share done, `0..1`, or `null` when not known.
 * @return The words, with the percentage after a comma when there is one.
 */
private fun withPercent(words: String, progress: Float?): String =
    if (progress == null) words else "$words, ${(progress.coerceIn(0f, 1f) * PERCENT).toInt()}%"

/** Multiplier from a share to a percentage. */
private const val PERCENT = 100
