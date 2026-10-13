package dev.voir.areca.flow.attachments.viewer

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculateCentroid
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.center
import androidx.compose.ui.geometry.isSpecified
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChanged
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.semantics

/**
 * A picture that can be pinched, panned and double-tapped closer.
 *
 * **One finger belongs to the pager until the picture is zoomed.** At its fitted size a swipe moves
 * to the next file; once zoomed, the same swipe moves around the picture instead, and two fingers
 * always zoom. Without that split the first file would be the only one anybody could reach.
 *
 * A pinch zooms around the fingers and a double tap around the point tapped, so what is under them
 * stays there. Panning stops at the picture's edges, so it can never be dragged off into empty
 * ground; on an axis where the zoomed picture is still smaller than the frame, it stays centred.
 * Leaving the page resets it, because the viewer keys each page by its file.
 *
 * **Zoom does not need gestures.** A screen reader offers [zoomInLabel], [zoomOutLabel] and
 * [resetZoomLabel] as actions on the picture, since a double tap there means "activate"; with
 * keyboard focus, shown by a ring inside the frame, `+`, `−` and `0` do the same, and Ctrl with the
 * mouse wheel zooms around the pointer. See [zoomControls].
 *
 * @param painter The picture, decoded.
 * @param contentDescription What it shows, read out in its place.
 * @param zoomInLabel Name of the action that brings the picture closer.
 * @param zoomOutLabel Name of the action that takes it back a step.
 * @param resetZoomLabel Name of the action that returns it to its fitted size.
 * @param modifier Modifier applied to the frame.
 */
@Composable
internal fun ZoomableImage(
    painter: Painter,
    contentDescription: String?,
    zoomInLabel: String,
    zoomOutLabel: String,
    resetZoomLabel: String,
    modifier: Modifier = Modifier,
) {
    // Read by the zoom, which outlives a recomposition that hands in a new picture.
    val picture by rememberUpdatedState(painter)
    val state = remember { ZoomState(maxScale = MAX_SCALE) { picture.intrinsicSize } }

    Box(
        modifier = modifier
            .fillMaxSize()
            .clipToBounds()
            .semantics(mergeDescendants = true) {}
            .zoomControls(
                state = state,
                zoomInLabel = zoomInLabel,
                zoomOutLabel = zoomOutLabel,
                resetZoomLabel = resetZoomLabel,
            )
            .pointerInput(state) {
                detectTapGestures(onDoubleTap = state::toggle)
            }
            .pointerInput(state) {
                awaitEachGesture {
                    awaitFirstDown(requireUnconsumed = false)

                    do {
                        val event = awaitPointerEvent()
                        val fingers = event.changes.count { change -> change.pressed }

                        if (fingers > 1 || state.isZoomed) {
                            val centroid = event.calculateCentroid()
                            state.zoom(
                                target = state.scale * event.calculateZoom(),
                                focus = if (centroid.isSpecified) centroid else state.frame.center,
                                pan = event.calculatePan(),
                            )
                            event.changes.forEach { change ->
                                if (change.positionChanged()) change.consume()
                            }
                        }
                    } while (event.changes.any { change -> change.pressed })

                    state.settle()
                }
            },
    ) {
        Image(
            painter = painter,
            contentDescription = contentDescription,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = state.scale
                    scaleY = state.scale
                    translationX = state.offset.x
                    translationY = state.offset.y
                },
        )
    }
}

/** How close a pinch may bring a picture: far enough to read the small print on a receipt. */
private const val MAX_SCALE = 5f
