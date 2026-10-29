package dev.voir.areca.media.zoom

import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculateCentroid
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.geometry.isSpecified
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerType
import androidx.compose.ui.input.pointer.isCtrlPressed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChanged
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import dev.voir.areca.indication.FadeIndication
import dev.voir.areca.indication.FocusOutline
import dev.voir.areca.theme.ArecaTheme
import kotlinx.coroutines.launch
import kotlin.math.abs

/** What pinching a picture does. */
public enum class ZoomBehavior {
    /** Nothing: a thumbnail, or a picture that only opens. */
    None,

    /**
     * Zooms and stays — the viewer. One finger pans once zoomed; at the fitted size it belongs to
     * whatever scrolls around the picture, so a pager still turns.
     */
    Sticky,

    /**
     * Zooms while the fingers are down and springs back when they lift — a picture in a list or a
     * carousel, looked at closely without leaving the screen.
     */
    Peek,

    /**
     * Brings the picture closer under a mouse pointer and follows it across, letting go as the
     * pointer leaves — a desktop product page. Touch and pens only tap; a pinch does nothing.
     */
    Hover,
}

/**
 * Makes content zoomable: pinch, double tap, pan once zoomed, and the same without gestures.
 *
 * **One finger belongs to the pager until the content is zoomed.** At its fitted size a swipe moves
 * whatever scrolls around it; once zoomed, the same swipe pans instead, and two fingers always zoom.
 * A pinch zooms around the fingers and a double tap around the point tapped. Panning stops at the
 * content's edges, measured from [contentSize]; a swipe that keeps pushing past an edge is reported
 * to [onEdgeSwipe], so a viewer can turn the page from a zoomed picture.
 *
 * **Zoom does not need gestures.** A screen reader offers the actions named in [labels]; with keyboard focus, shown by a ring inside the frame, `+`, `−` and
 * `0` do the same, and Ctrl with the mouse wheel zooms around the pointer.
 *
 * With [ZoomBehavior.Sticky] the modifier clips and moves what follows it. With
 * [ZoomBehavior.Peek] it only drives [state] and springs it back on release: the caller draws the
 * zoomed content where it can rise above its clip, as
 * [MediaImage][dev.voir.areca.media.image.MediaImage] does. A double tap does nothing in a peek, so
 * a tap is reported at once. [ZoomBehavior.Hover] magnifies under a mouse pointer and needs no
 * gesture at all. [ZoomBehavior.None] leaves only [onTap].
 *
 * @param state The zoom to drive.
 * @param contentSize The content's own size before it is fitted into the frame — a painter's
 *   intrinsic size — or [Size.Unspecified] for content that fills the frame.
 * @param behavior What pinching does.
 * @param onTap Called on a single tap, or `null`. With a double tap available it waits out the
 *   double-tap timeout first.
 * @param onEdgeSwipe Called with `1` or `-1` when a one-finger swipe on zoomed content keeps going
 *   past its right or left edge — next or previous — or `null`.
 * @param labels Names of the zoom actions offered to a screen reader.
 * @return The modifier.
 */
@Composable
public fun Modifier.zoomable(
    state: ZoomState,
    contentSize: Size = Size.Unspecified,
    behavior: ZoomBehavior = ZoomBehavior.Sticky,
    onTap: (() -> Unit)? = null,
    onEdgeSwipe: ((direction: Int) -> Unit)? = null,
    labels: ZoomLabels = ZoomLabels(),
): Modifier {
    SideEffect { state.content = contentSize }
    val scope = rememberCoroutineScope()
    val latestTap by rememberUpdatedState(onTap)
    val latestEdge by rememberUpdatedState(onEdgeSwipe)
    val taps = if (latestTap == null && behavior != ZoomBehavior.Sticky) {
        Modifier
    } else {
        Modifier.pointerInput(state, behavior) {
            detectTapGestures(
                onTap = { latestTap?.invoke() },
                onDoubleTap = if (behavior == ZoomBehavior.Sticky) {
                    { focus -> scope.launch { state.animateToggle(focus) } }
                } else {
                    null
                },
            )
        }
    }

    if (behavior == ZoomBehavior.None) return this.then(taps)
    if (behavior == ZoomBehavior.Hover) return this.hoverZoom(state).then(taps)

    val sticky = behavior == ZoomBehavior.Sticky
    val edgePx = with(LocalDensity.current) { EDGE_SWIPE.toPx() }

    return this
        .then(if (sticky) Modifier.clipToBounds() else Modifier)
        .zoomControls(state, labels)
        .then(taps)
        .pointerInput(state, behavior) {
            awaitEachGesture {
                awaitFirstDown(requireUnconsumed = false)
                var pushed = 0f

                do {
                    val event = awaitPointerEvent()
                    val fingers = event.changes.count { change -> change.pressed }

                    if (fingers > 1) {
                        val centroid = event.calculateCentroid()
                        state.zoom(
                            target = state.scale * event.calculateZoom(),
                            focus = if (centroid.isSpecified) centroid else state.frame.center,
                            pan = event.calculatePan(),
                        )
                        event.changes.forEach { change -> if (change.positionChanged()) change.consume() }
                    } else if (state.isZoomed) {
                        pushed += state.panBy(event.calculatePan()).x
                        event.changes.forEach { change -> if (change.positionChanged()) change.consume() }
                    }
                } while (event.changes.any { change -> change.pressed })

                if (sticky) {
                    state.settle()
                    if (abs(pushed) > edgePx) latestEdge?.invoke(if (pushed < 0f) 1 else -1)
                } else {
                    scope.launch { state.animateReset() }
                }
            }
        }
        .then(
            if (sticky) {
                Modifier.graphicsLayer {
                    scaleX = state.scale
                    scaleY = state.scale
                    translationX = state.offset.x
                    translationY = state.offset.y
                }
            } else {
                Modifier
            },
        )
}

/**
 * What every zoomable surface offers besides gestures: its frame measured into [state], zoom in,
 * zoom out and reset as actions for a screen reader, `+`, `−` and `0` on the keyboard, Ctrl with
 * the mouse wheel around the pointer, and a focus ring drawn inside its edges, where a pager cannot
 * cut it off.
 *
 * [zoomable] includes it. Use it alone on content that reads its own gestures and drives [state]
 * through [ZoomState.zoom] and [ZoomState.panBy] — a column of pages whose pan past an edge scrolls
 * it — and draw the content with the state's scale and offset.
 *
 * The wheel is read before the content under it, so a scrolling column does not take a Ctrl-scroll
 * as a scroll.
 *
 * @param state The zoom to drive.
 * @param labels Names of the zoom actions.
 * @return The modifier.
 */
@Composable
public fun Modifier.zoomControls(state: ZoomState, labels: ZoomLabels = ZoomLabels()): Modifier {
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
                if (canZoomIn) add(CustomAccessibilityAction(labels.zoomIn) { state.zoomIn(); true })
                if (isZoomed) {
                    add(CustomAccessibilityAction(labels.zoomOut) { state.zoomOut(); true })
                    add(CustomAccessibilityAction(labels.reset) { state.reset(); true })
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

/**
 * Magnifies [state]'s content under a mouse pointer: closer as it enters, the point under it held
 * still as it moves, back to the fitted size as it leaves.
 *
 * @param state The zoom to drive.
 * @return The modifier.
 */
@Composable
private fun Modifier.hoverZoom(state: ZoomState): Modifier {
    val scope = rememberCoroutineScope()
    return this
        .clipToBounds()
        .onSizeChanged { size -> state.frame = size.toSize() }
        .pointerInput(state) {
            awaitPointerEventScope {
                while (true) {
                    val event = awaitPointerEvent()
                    val change = event.changes.firstOrNull() ?: continue
                    if (change.type != PointerType.Mouse) continue
                    when (event.type) {
                        PointerEventType.Enter, PointerEventType.Move -> {
                            state.reset()
                            state.zoom(HOVER_SCALE.coerceAtMost(state.maxScale), change.position)
                        }
                        PointerEventType.Exit -> scope.launch { state.animateReset() }
                    }
                }
            }
        }
        .graphicsLayer {
            scaleX = state.scale
            scaleY = state.scale
            translationX = state.offset.x
            translationY = state.offset.y
        }
}

/** How close a hovered picture is brought. */
private const val HOVER_SCALE = 2.2f

/** How far one notch of the mouse wheel, with Ctrl held, zooms. */
private const val WHEEL_SCALE = 1.25f

/** How far a swipe must push past a zoomed picture's edge before it turns the page. */
private val EDGE_SWIPE = 72.dp
