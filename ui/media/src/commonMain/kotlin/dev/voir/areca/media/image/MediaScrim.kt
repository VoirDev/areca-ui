package dev.voir.areca.media.image

/** A gradient laid over a picture so words and controls on it read on any photograph. */
public enum class MediaScrim {
    /** No gradient. */
    None,

    /** Darkening the top edge, under a bar or a corner control. */
    Top,

    /** Darkening the bottom edge, under a caption or an indicator. */
    Bottom,

    /** Darkening both edges. */
    Both,
}
