package dev.voir.areca.flow.attachments

import androidx.compose.runtime.Immutable

/**
 * Where an attachment's bytes are, and what is happening to them, as a square or the viewer draws
 * it.
 *
 * Which of these an application reports is its own decision. One whose files never leave the
 * device uses only [Ready]; one that does not want uploads on screen never reports [Uploading] —
 * a file waiting to reach the server is already here and opens like any other.
 */
@Immutable
public sealed interface AttachmentState {
    /**
     * Being saved. Drawn under a veil with a spinner; it cannot be opened yet, because there is
     * nothing to show.
     */
    public data object Adding : AttachmentState

    /**
     * On this device and on its way to the server. Opens like [Ready]; a ring in the square's corner
     * fills as it goes.
     *
     * @property progress Share that has been sent, `0..1`, or `null` when not known.
     */
    public data class Uploading(public val progress: Float? = null) : AttachmentState

    /** On this device, ready to open. */
    public data object Ready : AttachmentState

    /**
     * On the server only. Opening it is asking for it; nothing can be drawn of it until it arrives.
     */
    public data object Remote : AttachmentState

    /**
     * Arriving.
     *
     * @property progress Share that has arrived, `0..1`, or `null` when not known.
     */
    public data class Downloading(public val progress: Float? = null) : AttachmentState

    /**
     * Wanted, but the server cannot be reached right now. Ordinary, not an error: it arrives once it
     * can, and the viewer offers to try again.
     */
    public data object Waiting : AttachmentState

    /** The server no longer has it, and nothing further will be tried. */
    public data object Unavailable : AttachmentState
}
