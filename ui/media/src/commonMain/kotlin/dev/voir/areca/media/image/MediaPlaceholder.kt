package dev.voir.areca.media.image

import androidx.compose.runtime.Immutable

/** What stands in for a picture while it loads. */
@Immutable
public sealed interface MediaPlaceholder {
    /** A shimmering block the frame's shape and size, so nothing moves when the picture arrives. */
    public data object Skeleton : MediaPlaceholder

    /**
     * A small, already-loaded version of the picture — its thumbnail — drawn blurred, then cleared
     * by the full picture.
     *
     * @property model The small picture's model, loaded as a thumbnail through the same loader.
     */
    public data class Blurred(public val model: Any) : MediaPlaceholder

    /** The frame's fill alone. */
    public data object None : MediaPlaceholder
}
