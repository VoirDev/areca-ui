package dev.voir.areca.media.zoom

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.lerp
import androidx.compose.ui.util.lerp

/**
 * How far zoomable content is zoomed and panned: a picture in a viewer, a page of a document.
 *
 * The content scales about its frame's centre and is then translated by [offset]. Every change keeps
 * the zoom between 1 and [maxScale] and the content's edges outside the frame, so a picture can
 * never be dragged off into empty ground. [Modifier.zoomable][zoomable] drives it from gestures, the
 * keyboard and a screen reader; hold one to read or reset the zoom from elsewhere — a viewer resets
 * a page as it leaves.
 *
 * Create one with [rememberZoomState].
 *
 * @property maxScale How close the content may be brought, as a multiple of its fitted size.
 */
@Stable
public class ZoomState internal constructor(public val maxScale: Float) {
    /** Current zoom: 1 at the fitted size. */
    public var scale: Float by mutableFloatStateOf(1f)
        private set

    /** Translation from the centred position, in pixels. */
    public var offset: Offset by mutableStateOf(Offset.Zero)
        private set

    /** Whether the content is closer than its fitted size. */
    public val isZoomed: Boolean by derivedStateOf { scale > ZOOMED_THRESHOLD }

    /** Whether the content can be brought closer. */
    public val canZoomIn: Boolean by derivedStateOf { scale < maxScale }

    /** Size of the frame the content is fitted into, in pixels, measured by [zoomable] or [zoomControls]. */
    public var frame: Size by mutableStateOf(Size.Zero)
        internal set

    /**
     * The content's own size before it is fitted into the frame, or [Size.Unspecified] for content
     * that fills it. [zoomable] sets it from its `contentSize`; set it yourself when driving the zoom
     * from gestures of your own.
     */
    public var content: Size = Size.Unspecified

    /** The frame's centre, which the zoom actions zoom around. */
    private val centre: Offset
        get() = Offset(frame.width / 2f, frame.height / 2f)

    /** Size the content is drawn at before zoom. */
    private val fitted: Size
        get() = fittedSize(content, frame)

    /** Brings the content a step closer, around the frame's centre. */
    public fun zoomIn(): Unit = zoom(scale * STEP_SCALE, centre)

    /** Takes the content a step back, around the frame's centre. */
    public fun zoomOut(): Unit = zoom(scale / STEP_SCALE, centre)

    /** Returns the content to its fitted size at once. */
    public fun reset() {
        scale = 1f
        offset = Offset.Zero
    }

    /** Returns the content to its fitted size on a spring. */
    public suspend fun animateReset(): Unit = animateTo(1f, Offset.Zero)

    /**
     * Zooms to [target] around [focus], within the limits, panned by [pan], and kept inside the
     * content's edges — what a pinch does, for content that reads its own gestures.
     *
     * @param target Zoom to reach, before the limits.
     * @param focus Point that stays where it is, in the frame's coordinates.
     * @param pan Translation added after the zoom.
     */
    public fun zoom(target: Float, focus: Offset, pan: Offset = Offset.Zero) {
        val zoomed = target.coerceIn(1f, maxScale)
        val moved = zoomOffset(offset, focus, frame, zoomChange = zoomed / scale) + pan

        scale = zoomed
        offset = clampOffset(moved, frame, fitted, zoomed)
    }

    /**
     * Pans by [pan] as far as the content's edges allow — what one finger does on zoomed content.
     *
     * @param pan How far the fingers moved.
     * @return The part of [pan] the edges stopped: for a scrolling column, the part that scrolls it.
     */
    public fun panBy(pan: Offset): Offset {
        val result = panWithin(offset, pan, frame, fitted, scale)
        offset = result.offset
        return result.remainder
    }

    /**
     * Zooms around [focus] by a double tap, on a spring: closer from the fitted size, back to it
     * from any zoom.
     *
     * @param focus Point tapped, in the frame's coordinates.
     */
    public suspend fun animateToggle(focus: Offset) {
        if (isZoomed) {
            animateReset()
        } else {
            val target = DOUBLE_TAP_SCALE.coerceAtMost(maxScale)
            val moved = zoomOffset(offset, focus, frame, zoomChange = target / scale)
            animateTo(target, clampOffset(moved, frame, fitted, target))
        }
    }

    /** Centres content that a gesture left at its fitted size; call it as a gesture of your own ends. */
    public fun settle() {
        if (scale <= 1f) offset = Offset.Zero
    }

    /**
     * Moves to [targetScale] and [targetOffset] together on one spring.
     *
     * @param targetScale Zoom to reach.
     * @param targetOffset Translation to reach.
     */
    private suspend fun animateTo(targetScale: Float, targetOffset: Offset) {
        val fromScale = scale
        val fromOffset = offset
        animate(0f, 1f, animationSpec = spring(stiffness = Spring.StiffnessMediumLow)) { fraction, _ ->
            scale = lerp(fromScale, targetScale, fraction)
            offset = lerp(fromOffset, targetOffset, fraction)
        }
    }
}

/**
 * Remembers a [ZoomState].
 *
 * @param maxScale How close the content may be brought, as a multiple of its fitted size; at least 1.
 * @return The state, kept for as long as the composition that remembered it.
 */
@Composable
public fun rememberZoomState(maxScale: Float = DEFAULT_MAX_SCALE): ZoomState =
    remember(maxScale) { ZoomState(maxScale = maxScale.coerceAtLeast(1f)) }

/** How close a picture may be brought by default: far enough to read small print on a label. */
private const val DEFAULT_MAX_SCALE = 5f

/** How close a double tap brings the content. */
private const val DOUBLE_TAP_SCALE = 2.5f

/** How far one zoom action, or one key press, brings the content closer or takes it back. */
internal const val STEP_SCALE = 2f

/** Zoom past which content counts as zoomed: above 1 by more than a spring's rounding. */
private const val ZOOMED_THRESHOLD = 1.001f
