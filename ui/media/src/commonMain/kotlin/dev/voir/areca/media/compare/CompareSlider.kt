package dev.voir.areca.media.compare

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.setProgress
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.media.icon.BundledIcons
import dev.voir.areca.media.icon.SwitchHorizontal
import dev.voir.areca.theme.ArecaTheme
import kotlin.math.roundToInt

/**
 * Two pictures of the same thing laid one over the other, with a divider to drag between them —
 * a room before and after, a photo raw and retouched.
 *
 * Drag the divider, or anywhere on the pictures across it, to show more of one or the other; a tap
 * moves it there. To assistive technology it is an adjustable value from all [after] to all
 * [before], named by [contentDescription]; with focus, the arrow keys move it.
 *
 * Draw both pictures to fill the frame the same way — a
 * [MediaImage][dev.voir.areca.media.image.MediaImage] with `fit = MediaFit.Fill` — so their parts
 * line up across the divider.
 *
 * @param before The picture uncovered towards the start or top.
 * @param after The picture uncovered towards the end or bottom.
 * @param modifier Modifier applied to the frame.
 * @param aspectRatio Width over height of the frame.
 * @param orientation [Orientation.Horizontal] for a divider moving across, [Orientation.Vertical]
 *   for one moving down.
 * @param initialPosition Where the divider starts, from `0f` to `1f`.
 * @param labels Words over each picture, fading as the divider covers them, or `null` for none.
 * @param contentDescription What the comparison is, read out with how much of [before] shows.
 * @param style Shape and colours.
 */
@Composable
public fun CompareSlider(
    before: @Composable BoxScope.() -> Unit,
    after: @Composable BoxScope.() -> Unit,
    modifier: Modifier = Modifier,
    aspectRatio: Float = 3f / 2f,
    orientation: Orientation = Orientation.Horizontal,
    initialPosition: Float = 0.5f,
    labels: CompareLabels? = CompareLabels(),
    contentDescription: String = "Before and after",
    style: CompareStyle = CompareStyle.default(),
) {
    var position by remember { mutableFloatStateOf(initialPosition.coerceIn(0f, 1f)) }
    val across = orientation == Orientation.Horizontal

    BoxWithConstraints(
        modifier = modifier
            .aspectRatio(aspectRatio)
            .clip(style.shape)
            .background(style.bg)
            .semantics {
                this.contentDescription = contentDescription
                progressBarRangeInfo = ProgressBarRangeInfo(position, 0f..1f)
                setProgress { target -> position = target.coerceIn(0f, 1f); true }
            }
            .onKeyEvent { event ->
                if (event.type != KeyEventType.KeyDown) return@onKeyEvent false
                val step = when (event.key) {
                    Key.DirectionLeft, Key.DirectionUp -> -KEY_STEP
                    Key.DirectionRight, Key.DirectionDown -> KEY_STEP
                    Key.MoveHome -> -1f
                    Key.MoveEnd -> 1f
                    else -> return@onKeyEvent false
                }
                position = (position + step).coerceIn(0f, 1f)
                true
            }
            .focusable()
            .pointerInput(across) {
                detectTapGestures { at -> position = fraction(at.x, at.y, size.width, size.height, across) }
            }
            .pointerInput(across) {
                detectDragGestures { change, _ ->
                    position = fraction(change.position.x, change.position.y, size.width, size.height, across)
                }
            },
    ) {
        val width = constraints.maxWidth.toFloat()
        val height = constraints.maxHeight.toFloat()

        Box(Modifier.fillMaxSize(), content = after)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawWithContent {
                    if (across) {
                        clipRect(right = size.width * position) { this@drawWithContent.drawContent() }
                    } else {
                        clipRect(bottom = size.height * position) { this@drawWithContent.drawContent() }
                    }
                },
            content = before,
        )

        // The divider and its knob, centred on the position.
        Box(
            Modifier
                .then(if (across) Modifier.fillMaxHeight().width(DIVIDER) else Modifier.fillMaxWidth().height(DIVIDER))
                .offset {
                    if (across) {
                        IntOffset((width * position - DIVIDER.toPx() / 2f).roundToInt(), 0)
                    } else {
                        IntOffset(0, (height * position - DIVIDER.toPx() / 2f).roundToInt())
                    }
                }
                .background(style.divider),
        )
        Box(
            Modifier
                .offset {
                    val knob = KNOB.toPx()
                    if (across) {
                        IntOffset((width * position - knob / 2f).roundToInt(), ((height - knob) / 2f).roundToInt())
                    } else {
                        IntOffset(((width - knob) / 2f).roundToInt(), (height * position - knob / 2f).roundToInt())
                    }
                }
                .size(KNOB)
                .shadow(4.dp, CircleShape)
                .background(style.knob, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = BundledIcons.SwitchHorizontal,
                contentDescription = null,
                tint = style.knobContent,
                modifier = Modifier.graphicsLayer { rotationZ = if (across) 0f else 90f },
            )
        }

        if (labels != null) {
            CompareLabel(
                text = labels.first,
                style = style,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(ArecaTheme.spacing.md)
                    .graphicsLayer { alpha = if (position < LABEL_HIDE) 0f else 1f },
            )
            CompareLabel(
                text = labels.second,
                style = style,
                modifier = Modifier
                    .align(if (across) Alignment.TopEnd else Alignment.BottomStart)
                    .padding(ArecaTheme.spacing.md)
                    .graphicsLayer { alpha = if (position > 1f - LABEL_HIDE) 0f else 1f },
            )
        }
    }
}

/**
 * Two pictures of the same thing laid one over the other, cross-faded along a track under them.
 *
 * Drag along the track, or tap it, to fade from [after] to [before]. To assistive technology the
 * track is an adjustable value named by [contentDescription]; with focus, the arrow keys move it.
 *
 * @param before The picture shown at the track's start.
 * @param after The picture shown at its end.
 * @param modifier Modifier applied to the pictures and track together.
 * @param aspectRatio Width over height of the pictures' frame.
 * @param initialFraction How far towards [after] the fade starts, from `0f` to `1f`.
 * @param labels Words at either end of the track, or `null` for none.
 * @param contentDescription What the comparison is.
 * @param style Shape and colours.
 */
@Composable
public fun CompareFade(
    before: @Composable BoxScope.() -> Unit,
    after: @Composable BoxScope.() -> Unit,
    modifier: Modifier = Modifier,
    aspectRatio: Float = 3f / 2f,
    initialFraction: Float = 0.5f,
    labels: CompareLabels? = CompareLabels(),
    contentDescription: String = "Fade between before and after",
    style: CompareStyle = CompareStyle.default(),
) {
    var fraction by remember { mutableFloatStateOf(initialFraction.coerceIn(0f, 1f)) }

    Column(modifier, verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg)) {
        Box(Modifier.fillMaxWidth().aspectRatio(aspectRatio).clip(style.shape).background(style.bg)) {
            Box(Modifier.fillMaxSize(), content = after)
            Box(Modifier.fillMaxSize().graphicsLayer { alpha = 1f - fraction }, content = before)
        }
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md)) {
            if (labels != null) Text(labels.first, style = ArecaTheme.typography.bodySmall, color = ArecaTheme.colors.fgMuted)
            BoxWithConstraints(
                modifier = Modifier
                    .weight(1f)
                    .height(TRACK_TOUCH)
                    .semantics {
                        this.contentDescription = contentDescription
                        progressBarRangeInfo = ProgressBarRangeInfo(fraction, 0f..1f)
                        setProgress { target -> fraction = target.coerceIn(0f, 1f); true }
                    }
                    .onKeyEvent { event ->
                        if (event.type != KeyEventType.KeyDown) return@onKeyEvent false
                        val step = when (event.key) {
                            Key.DirectionLeft -> -KEY_STEP
                            Key.DirectionRight -> KEY_STEP
                            else -> return@onKeyEvent false
                        }
                        fraction = (fraction + step).coerceIn(0f, 1f)
                        true
                    }
                    .focusable()
                    .pointerInput(Unit) { detectTapGestures { at -> fraction = (at.x / size.width).coerceIn(0f, 1f) } }
                    .pointerInput(Unit) {
                        detectDragGestures { change, _ -> fraction = (change.position.x / size.width).coerceIn(0f, 1f) }
                    },
                contentAlignment = Alignment.CenterStart,
            ) {
                val track = constraints.maxWidth.toFloat()
                Box(Modifier.fillMaxWidth().height(TRACK).background(ArecaTheme.colors.borderStrong, CircleShape))
                Box(
                    Modifier
                        .offset { IntOffset((track * fraction - THUMB.toPx() / 2f).roundToInt(), 0) }
                        .size(THUMB)
                        .shadow(2.dp, CircleShape)
                        .background(ArecaTheme.colors.bgSurface, CircleShape),
                )
            }
            if (labels != null) Text(labels.second, style = ArecaTheme.typography.bodySmall, color = ArecaTheme.colors.fgMuted)
        }
    }
}

/**
 * Where a point falls along a frame, from `0f` at its start to `1f` at its end.
 *
 * @param x The point across.
 * @param y The point down.
 * @param width The frame's width.
 * @param height The frame's height.
 * @param across Whether the divider moves across rather than down.
 * @return The fraction, kept inside the frame.
 */
internal fun fraction(x: Float, y: Float, width: Int, height: Int, across: Boolean): Float =
    (if (across) x / width else y / height).coerceIn(0f, 1f)

/** How far one arrow key moves a divider or a fade. */
private const val KEY_STEP = 0.05f

/** Where a label hides as the divider covers it. */
private const val LABEL_HIDE = 0.18f

/** Thickness of the divider line. */
private val DIVIDER = 2.dp

/** Side of the divider's knob, a touch target. */
private val KNOB = 44.dp

/** Height of the fade's touchable track. */
private val TRACK_TOUCH = 44.dp

/** Thickness of the fade's drawn track. */
private val TRACK = 4.dp

/** Side of the fade's thumb. */
private val THUMB = 22.dp
