package dev.voir.areca.media.viewer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned

/**
 * Where each picture a [MediaViewer] can open from sits on the screen beneath it, so the viewer can
 * grow the picture out of its frame on open and shrink it back into the frame of the picture on
 * screen when it closes.
 *
 * The viewer is a window of its own, so the transition cannot share one layout with the screen.
 * Both windows run edge to edge, so the positions agree: each source records its bounds in the
 * window with [mediaTransitionSource], and the viewer moves its page between those bounds and its
 * own. A source that is off screen, or never recorded, opens and closes with a fade.
 *
 * Create one with [rememberMediaTransitionState] on the screen that opens the viewer, and pass it to
 * both the sources and the viewer.
 */
@Stable
public class MediaTransitionState internal constructor() {
    /** Each source's bounds in the window and its picture's shape, by key. */
    private val sources = mutableStateMapOf<Any, MediaTransitionSource>()

    /** The key of the source hidden while its picture is in flight, or `null`. */
    internal var hidden: Any? by mutableStateOf(null)

    /**
     * The source recorded for [key].
     *
     * @param key The source's key.
     * @return Its bounds and picture shape, or `null` when it is not on screen.
     */
    internal fun source(key: Any): MediaTransitionSource? = sources[key]?.takeIf { it.bounds.width > 0f && it.bounds.height > 0f }

    /**
     * Records where the source for [key] is.
     *
     * @param key The source's key.
     * @param source Its bounds and picture shape.
     */
    internal fun record(key: Any, source: MediaTransitionSource) {
        sources[key] = source
    }

    /**
     * Forgets the source for [key], once it leaves the screen.
     *
     * @param key The source's key.
     */
    internal fun forget(key: Any) {
        sources.remove(key)
    }
}

/**
 * A source's place in the window.
 *
 * @property bounds Its bounds, in the window's pixels.
 * @property aspectRatio Width over height of the picture drawn in it, or `null` when the picture
 *   fills the bounds.
 */
internal data class MediaTransitionSource(val bounds: Rect, val aspectRatio: Float?)

/**
 * Remembers a [MediaTransitionState].
 *
 * @return The state, for the screen's sources and the viewer they open.
 */
@Composable
public fun rememberMediaTransitionState(): MediaTransitionState = remember { MediaTransitionState() }

/**
 * Marks this frame as the place the picture for [key] opens from and closes into, and hides it
 * while that picture is in flight.
 *
 * Put it on the frame the picture is drawn in — a carousel's item, a grid's tile — with the same key
 * the viewer gives that picture.
 *
 * @param state The transition, shared with the viewer.
 * @param key The picture's key in the viewer.
 * @param pictureSize The picture's own size when it is drawn whole in the frame, so the viewer grows
 *   the picture rather than the frame; [Size.Unspecified] for a picture that fills it.
 * @return The modifier.
 */
public fun Modifier.mediaTransitionSource(
    state: MediaTransitionState,
    key: Any,
    pictureSize: Size = Size.Unspecified,
): Modifier = this.recordTransitionSource(state, key, pictureSize).hideTransitionSource(state, key)

/**
 * Records where the picture for [key] is drawn — the part of [mediaTransitionSource] a frame puts on
 * the picture itself, inside its inset.
 *
 * @param state The transition.
 * @param key The picture's key.
 * @param pictureSize The picture's own size when drawn whole, or [Size.Unspecified].
 * @return The modifier.
 */
internal fun Modifier.recordTransitionSource(state: MediaTransitionState, key: Any, pictureSize: Size): Modifier =
    onGloballyPositioned { coordinates ->
        val bounds = coordinates.boundsInWindow()
        val aspect = if (pictureSize.isSpecified()) pictureSize.width / pictureSize.height else null
        if (bounds.isEmpty) state.forget(key) else state.record(key, MediaTransitionSource(bounds, aspect))
    }

/**
 * Hides what follows while the picture for [key] is in flight — the part of
 * [mediaTransitionSource] a frame puts on itself.
 *
 * @param state The transition.
 * @param key The picture's key.
 * @return The modifier.
 */
internal fun Modifier.hideTransitionSource(state: MediaTransitionState, key: Any): Modifier =
    graphicsLayer { alpha = if (state.hidden == key) 0f else 1f }

/** @return Whether both sides are known and positive. */
private fun Size.isSpecified(): Boolean = this != Size.Unspecified && width > 0f && height > 0f
