package dev.voir.areca.media.image

/**
 * What content drawn in a [MediaFrame]'s slot is doing, for the frame to draw the matching face.
 *
 * [MediaImage] and the model overload of [MediaFrame] work this out from the loader themselves; pass
 * it only when the slot draws the picture with something else.
 */
public enum class MediaState {
    /** Not ready yet: the frame draws its loading face over the slot. */
    Loading,

    /** Ready: the frame draws the slot alone. */
    Ready,

    /** Could not be drawn: the frame draws its failed face, with a retry when it has one. */
    Failed,
}
