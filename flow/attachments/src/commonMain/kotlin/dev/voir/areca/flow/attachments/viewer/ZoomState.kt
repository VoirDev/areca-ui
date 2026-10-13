package dev.voir.areca.flow.attachments.viewer

import androidx.compose.foundation.focusable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.isCtrlPressed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.toSize
import dev.voir.areca.indication.FadeIndication
import dev.voir.areca.indication.FocusOutline
import dev.voir.areca.theme.ArecaTheme

/**
 * How far zoomable content in the viewer is zoomed and panned: a picture, or a column of PDF pages.
 *
 * The content scales about the frame's centre and is then translated by [offset]. Every change goes
 * through [zoom] or [panBy], which keep the zoom between 1 and [maxScale] and the content's edges
 * outside the frame.
 *
 * @property maxScale How close the content may be brought.
 * @property content Size of the content before it is fitted into the frame, read at each change, or
 *   [Size.Unspecified] for content that fills the frame.
 */
@Stable
internal class ZoomState(
    private val maxScale: Float,
    private val content: () -> Size,
) {
    /** Current zoom: 1 at the fitted size. */
    var scale: Float by mutableFloatStateOf(1f)
        private set

    /** Translation from the centred position, in pixels. */
    var offset: Offset by mutableStateOf(Offset.Zero)
        private set

    /** Size of the frame the content is fitted into, in pixels. */
    var frame: Size by mutableStateOf(Size.Zero)

    /** Whether the content is closer than its fitted size. */
    val isZoomed: Boolean by derivedStateOf { scale > 1f }

    /** Whether the content can be brought closer. */
    val canZoomIn: Boolean by derivedStateOf { scale < maxScale }

    /** The frame's centre, which the zoom actions zoom around. */
    private val centre: Offset
        get() = Offset(frame.width / 2f, frame.height / 2f)

    /**
     * Zooms to [target] around [focus], within the limits, panned by [pan], and kept inside the
     * content's edges.
     *
     * @param target Zoom to reach, before the limits.
     * @param focus Point that stays where it is, in the frame's coordinates.
     * @param pan Translation added after the zoom.
     */
    fun zoom(target: Float, focus: Offset, pan: Offset = Offset.Zero) {
        val zoomed = target.coerceIn(1f, maxScale)
        val moved = zoomOffset(offset, focus, frame, zoomChange = zoomed / scale) + pan

        scale = zoomed
        offset = clampOffset(moved, frame, fittedSize(content(), frame), zoomed)
    }

    /**
     * Pans by [pan] as far as the content's edges allow.
     *
     * @param pan How far the fingers moved.
     * @return The part of [pan] the edges stopped.
     */
    fun panBy(pan: Offset): Offset {
        val result = panWithin(offset, pan, frame, fittedSize(content(), frame), scale)
        offset = result.offset
        return result.remainder
    }

    /** Brings the content a step closer, around the frame's centre. */
    fun zoomIn() = zoom(scale * STEP_SCALE, centre)

    /** Takes the content a step back, around the frame's centre. */
    fun zoomOut() = zoom(scale / STEP_SCALE, centre)

    /** Returns the content to its fitted size. */
    fun reset() = zoom(1f, centre)

    /**
     * Zooms around [focus] by a double tap: closer from the fitted size, back to it from any zoom.
     *
     * @param focus Point tapped, in the frame's coordinates.
     */
    fun toggle(focus: Offset) {
        if (isZoomed) reset() else zoom(DOUBLE_TAP_SCALE, focus)
    }

    /** Centres content that a gesture left at its fitted size. */
    fun settle() {
        if (scale <= 1f) offset = Offset.Zero
    }
}

/**
 * What every zoomable surface in the viewer offers besides gestures: its frame measured into
 * [state], zoom in, zoom out and reset as actions for a screen reader, `+`, `−` and `0` on the
 * keyboard, Ctrl with the mouse wheel around the pointer, and a focus ring drawn inside its edges,
 * where the pager cannot cut it off.
 *
 * The wheel is read before the content under it, so a scrolling column does not take a Ctrl-scroll
 * as a scroll.
 *
 * @param state The zoom to drive.
 * @param zoomInLabel Name of the action that brings the content closer.
 * @param zoomOutLabel Name of the action that takes it back a step.
 * @param resetZoomLabel Name of the action that returns it to its fitted size.
 * @return The modifier.
 */
@Composable
internal fun Modifier.zoomControls(
    state: ZoomState,
    zoomInLabel: String,
    zoomOutLabel: String,
    resetZoomLabel: String,
): Modifier {
    val focusSource = remember { MutableInteractionSource() }
    val ring = FadeIndication.tint(
        shape = RectangleShape,
        focusOutline = FocusOutline(offset = -ArecaTheme.spacing.xs),
    )
    // Read here rather than in the semantics block, so a pinch recomposes only as a limit is crossed.
    val canZoomIn = state.canZoomIn
    val isZoomed = state.isZoomed

    return this
        .onSizeChanged { size -> state.frame = size.toSize() }
        .semantics {
            customActions = buildList {
                if (canZoomIn) add(CustomAccessibilityAction(zoomInLabel) { state.zoomIn(); true })
                if (isZoomed) {
                    add(CustomAccessibilityAction(zoomOutLabel) { state.zoomOut(); true })
                    add(CustomAccessibilityAction(resetZoomLabel) { state.reset(); true })
                }
            }
        }
        .onKeyEvent { event ->
            if (event.type != KeyEventType.KeyDown) return@onKeyEvent false

            when (event.key) {
                Key.Plus, Key.Equals, Key.NumPadAdd -> state.zoomIn()
                Key.Minus, Key.NumPadSubtract -> state.zoomOut()
                Key.Zero, Key.NumPad0 -> state.reset()
                else -> return@onKeyEvent false
            }
            true
        }
        .indication(focusSource, ring)
        .focusable(interactionSource = focusSource)
        .pointerInput(state) {
            awaitPointerEventScope {
                while (true) {
                    val event = awaitPointerEvent(PointerEventPass.Initial)
                    val change = event.changes.firstOrNull()

                    if (event.type == PointerEventType.Scroll && event.keyboardModifiers.isCtrlPressed &&
                        change != null && change.scrollDelta.y != 0f
                    ) {
                        val step = if (change.scrollDelta.y < 0f) WHEEL_SCALE else 1f / WHEEL_SCALE
                        state.zoom(state.scale * step, change.position)
                        change.consume()
                    }
                }
            }
        }
}

/** How close a double tap brings the content. */
private const val DOUBLE_TAP_SCALE = 2.5f

/** How far one zoom action, or one key press, brings the content closer or takes it back. */
private const val STEP_SCALE = 2f

/** How far one notch of the mouse wheel, with Ctrl held, zooms. */
private const val WHEEL_SCALE = 1.25f
