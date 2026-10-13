package dev.voir.areca.crop

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculateCentroid
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.positionChanged
import kotlin.math.exp

/**
 * Turns touches on the stage into crop changes.
 *
 * One finger on a grip resizes the frame; one finger anywhere else moves the photo; two fingers move
 * and zoom it, and lifting one of them carries on as a move. A tap followed quickly by another in the
 * same place toggles the zoom. A mouse press also calls [onMousePress], which focuses the stage so the
 * keyboard works on it next; a touch does not, so a tap never brings up a focus ring.
 *
 * @param state Crop the touches change.
 * @param touchRadius Reach of a frame grip, in pixels.
 * @param minFrame Smallest side a frame can be dragged to, in pixels.
 * @param onMousePress Called when a mouse button goes down on the stage.
 * @return Modifier handling the gestures.
 */
internal fun Modifier.cropGestures(
    state: ImageCropState,
    touchRadius: Float,
    minFrame: Float,
    onMousePress: () -> Unit,
): Modifier =
    pointerInput(state, touchRadius, minFrame, onMousePress) {
        var lastTapMillis = 0L
        var lastTapPosition = Offset.Zero

        awaitEachGesture {
            val down = awaitFirstDown(requireUnconsumed = false)
            if (down.type == PointerType.Mouse) onMousePress()
            state.beginGesture()
            val start = state.transform
            val grip = if (start == null) null else state.handleAt(down.position, touchRadius)
            var mode = if (grip != null) GestureMode.Resize else GestureMode.Pan
            if (mode == GestureMode.Pan) state.startPan()
            var travelled = false

            while (true) {
                val event = awaitPointerEvent()
                val pressed = event.changes.filter { it.pressed }
                if (pressed.isEmpty()) break

                if (pressed.size >= 2) {
                    mode = GestureMode.Pinch
                    state.pinch(
                        pan = event.calculatePan(),
                        zoomChange = event.calculateZoom(),
                        centroid = event.calculateCentroid(useCurrent = true),
                    )
                    travelled = true
                } else {
                    val change = pressed.first()
                    if (mode == GestureMode.Pinch) {
                        mode = GestureMode.Pan
                        state.startPan()
                    }
                    if (mode == GestureMode.Resize && grip != null && start != null) {
                        state.resize(grip, start, change.position - down.position, minFrame)
                    } else {
                        state.panBy(change.positionChange())
                    }
                    if ((change.position - down.position).getDistance() > viewConfiguration.touchSlop) {
                        travelled = true
                    }
                }
                event.changes.forEach { if (it.positionChanged()) it.consume() }
            }

            state.endGesture()

            if (!travelled && mode == GestureMode.Pan) {
                val isSecondTap = down.uptimeMillis - lastTapMillis < viewConfiguration.doubleTapTimeoutMillis &&
                    (down.position - lastTapPosition).getDistance() < viewConfiguration.touchSlop * DoubleTapReach
                if (isSecondTap) {
                    state.toggleZoom(down.position)
                    lastTapMillis = 0L
                } else {
                    lastTapMillis = down.uptimeMillis
                    lastTapPosition = down.position
                }
            }
        }
    }

/**
 * Zooms the photo with a mouse wheel or a trackpad scroll, around the pointer.
 *
 * @param state Crop the scrolling changes.
 * @return Modifier handling scroll events.
 */
internal fun Modifier.cropScrollZoom(state: ImageCropState): Modifier = pointerInput(state) {
    awaitPointerEventScope {
        while (true) {
            val event = awaitPointerEvent()
            if (event.type != PointerEventType.Scroll) continue
            val change = event.changes.firstOrNull() ?: continue
            val delta = change.scrollDelta.y
            if (delta == 0f) continue
            state.scrollZoom(exp(-delta * WheelZoomRate), change.position)
            change.consume()
        }
    }
}

/** What a touch is doing to the crop. */
private enum class GestureMode {
    /** One finger moving the photo. */
    Pan,

    /** One finger dragging a frame grip. */
    Resize,

    /** Two fingers moving and zooming the photo. */
    Pinch,
}

/** How far apart, in touch slops, the two taps of a double-tap may land. */
private const val DoubleTapReach = 4f

/** Zoom change per unit of scroll; a mouse notch is one unit. */
private const val WheelZoomRate = 0.12f
