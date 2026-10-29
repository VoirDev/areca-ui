package dev.voir.areca.media.compare

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import dev.voir.areca.media.zoom.ZoomLabels
import dev.voir.areca.media.zoom.rememberZoomState
import dev.voir.areca.media.zoom.zoomable
import dev.voir.areca.theme.ArecaTheme

/**
 * Two pictures side by side — or one above the other — whose zooms move together: pinch or double
 * tap either and both bring the same part closer, so a detail can be compared in each.
 *
 * Draw both pictures the same way, filling their frames — a
 * [MediaImage][dev.voir.areca.media.image.MediaImage] with `fit = MediaFit.Fill` — so the same
 * point sits in the same place in each.
 *
 * @param before The first picture.
 * @param after The second picture.
 * @param modifier Modifier applied to the pair.
 * @param arrangement Side by side, or one above the other.
 * @param aspectRatio Width over height of each frame.
 * @param labels Words over each picture, or `null` for none.
 * @param maxScale How close the pictures may be brought.
 * @param zoomLabels Names of the zoom actions offered to a screen reader.
 * @param style Shape and colours.
 */
@Composable
public fun CompareSideBySide(
    before: @Composable BoxScope.() -> Unit,
    after: @Composable BoxScope.() -> Unit,
    modifier: Modifier = Modifier,
    arrangement: CompareArrangement = CompareArrangement.Row,
    aspectRatio: Float = if (arrangement == CompareArrangement.Row) 3f / 4f else 3f / 2f,
    labels: CompareLabels? = CompareLabels(),
    maxScale: Float = 5f,
    zoomLabels: ZoomLabels = ZoomLabels(),
    style: CompareStyle = CompareStyle.default(),
) {
    // One zoom for both: the frames are the same size, so the same zoom shows the same part.
    val zoom = rememberZoomState(maxScale)
    val pane: @Composable (Modifier, Int) -> Unit = { paneModifier, index ->
        Box(paneModifier.aspectRatio(aspectRatio).clip(style.shape).background(style.bg)) {
            Box(Modifier.fillMaxSize().zoomable(zoom, labels = zoomLabels), content = if (index == 0) before else after)
            val label = labels?.let { if (index == 0) it.first else it.second }
            if (label != null) {
                CompareLabel(
                    text = label,
                    style = style,
                    modifier = Modifier.align(Alignment.TopStart).padding(ArecaTheme.spacing.md),
                )
            }
        }
    }
    val spacing = Arrangement.spacedBy(ArecaTheme.spacing.sm)
    if (arrangement == CompareArrangement.Row) {
        Row(modifier, horizontalArrangement = spacing) {
            pane(Modifier.weight(1f), 0)
            pane(Modifier.weight(1f), 1)
        }
    } else {
        Column(modifier, verticalArrangement = spacing) {
            pane(Modifier.fillMaxWidth(), 0)
            pane(Modifier.fillMaxWidth(), 1)
        }
    }
}

/**
 * One picture that shows the other while it is held — the after, and the before under a press.
 *
 * Press and hold, or hold Space with focus, to see [before]; let go to return to [after]. To
 * assistive technology it is a button named [holdLabel] that switches between the two.
 *
 * @param before The picture shown while held.
 * @param after The picture shown otherwise.
 * @param modifier Modifier applied to the frame.
 * @param aspectRatio Width over height of the frame.
 * @param labels Words for the picture showing, or `null` for none.
 * @param holdLabel What holding does, read out.
 * @param style Shape and colours.
 */
@Composable
public fun CompareHold(
    before: @Composable BoxScope.() -> Unit,
    after: @Composable BoxScope.() -> Unit,
    modifier: Modifier = Modifier,
    aspectRatio: Float = 3f / 2f,
    labels: CompareLabels? = CompareLabels(),
    holdLabel: String = "Hold to see before",
    style: CompareStyle = CompareStyle.default(),
) {
    var holding by remember { mutableStateOf(false) }
    val shown by animateFloatAsState(if (holding) 1f else 0f, label = "compareHold")

    Box(
        modifier = modifier
            .aspectRatio(aspectRatio)
            .clip(style.shape)
            .background(style.bg)
            .semantics {
                role = Role.Button
                contentDescription = holdLabel
                labels?.let { stateDescription = if (holding) it.first else it.second }
                onClick { holding = !holding; true }
            }
            .onKeyEvent { event ->
                if (event.key != Key.Spacebar) return@onKeyEvent false
                holding = event.type == KeyEventType.KeyDown
                true
            }
            .focusable()
            .pointerInput(Unit) {
                awaitEachGesture {
                    awaitFirstDown(requireUnconsumed = false)
                    holding = true
                    waitForUpOrCancellation()
                    holding = false
                }
            },
    ) {
        Box(Modifier.fillMaxSize(), content = after)
        Box(Modifier.fillMaxSize().graphicsLayer { alpha = shown }, content = before)
        if (labels != null) {
            CompareLabel(
                text = if (holding) labels.first else labels.second,
                style = style,
                modifier = Modifier.align(Alignment.TopStart).padding(ArecaTheme.spacing.md),
            )
        }
    }
}
