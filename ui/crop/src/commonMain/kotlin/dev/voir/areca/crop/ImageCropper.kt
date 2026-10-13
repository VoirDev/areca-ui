package dev.voir.areca.crop

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.focusable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.isSpecified
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.addOutline
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import dev.voir.areca.indication.FocusOutline
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos

/**
 * The stage of a crop: the photo under a fixed frame, with everything outside the frame veiled.
 *
 * **The frame holds still and the photo moves.** Dragging, pinching and scrolling act on the photo;
 * only the frame's grips (when [CropSpec.isResizable]) move the frame, and when a resize ends
 * the frame re-centres and grows, carrying the photo with it — a zoom into what was picked. The photo
 * always covers the frame: straightening zooms in just enough, and a drag past an edge stretches and
 * springs back.
 *
 * The stage fills what it is given and takes keyboard focus — a mouse click on it focuses it too: the
 * arrow keys move the photo (with Shift, further), `+` and `−` zoom, and `[` and `]` straighten, and a
 * key held down is one step for undo. While focused it shows foundation's focus ring just inside its
 * edge, in [ImageCropperStyle.focusRing]. Assistive technology gets the same as
 * actions — move the photo left, right, up and down, zoom in and out, and, on a resizable
 * [CropAspect.Free] frame, make the frame wider or taller — named by [labels]. Rotation, mirroring and
 * the aspect are commands on [state], which [CropToolbar] puts on buttons.
 *
 * @param painter The photo. Its intrinsic size must be specified, and is taken as the photo's size in
 *   pixels; a change of size starts a fresh crop. For a large bitmap, a `BitmapPainter` with
 *   `FilterQuality.High` keeps the zoomed-out photo free of shimmer. On Android the stage draws it on
 *   the hardware canvas, which refuses a bitmap over 100 MB (`width × height × 4` bytes for ARGB, about
 *   26 megapixels), so decode a camera photo to no more than about 4096 pixels on its longest side.
 * @param state Crop the stage shows and changes.
 * @param contentDescription What the photo shows, read in its place, or `null` when a heading nearby
 *   already says it.
 * @param modifier Modifier applied to the stage.
 * @param shape Mask the frame cuts. Defaults to the state's [CropSpec.mask], which
 *   [ImageCropState.render] also cuts the result to; override it only to preview another.
 * @param style Colors of the stage. Defaults to [LocalImageCropperStyle], or
 *   [ImageCropperStyle.Default].
 * @param size Metrics of the stage. Defaults to [LocalImageCropperSize], or [ImageCropperSize.Default].
 * @param labels Names of the stage's assistive-technology actions; the same [CropToolbarLabels] the
 *   toolbar takes.
 * @param interactionSource Source the stage's focus is reported to, or `null` for one of its own.
 */
@Composable
public fun ImageCropper(
    painter: Painter,
    state: ImageCropState,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    shape: Shape = state.spec.mask,
    style: ImageCropperStyle = LocalImageCropperStyle.current ?: ImageCropperStyle.Default,
    size: ImageCropperSize = LocalImageCropperSize.current ?: ImageCropperSize.Default,
    labels: CropToolbarLabels = CropToolbarLabels(),
    interactionSource: MutableInteractionSource? = null,
) {
    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current
    var viewport by remember { mutableStateOf(IntSize.Zero) }
    val inset = with(density) { size.inset.toPx() }
    val imageSize = painter.intrinsicSize
    val source = interactionSource ?: remember { MutableInteractionSource() }
    val focusRequester = remember { FocusRequester() }
    val guides by animateFloatAsState(
        targetValue = if (state.isAdjusting) 1f else 0f,
        animationSpec = tween(GuidesFadeMillis),
        label = "cropGuides",
    )

    // Reported before the frame is laid out and drawn, rather than from an effect that runs after it,
    // so the very first frame already shows the photo under its frame.
    SideEffect {
        state.updateStage(density, layoutDirection)
        if (imageSize.isSpecified) state.updateImage(imageSize)
        if (viewport.width > 0 && viewport.height > 0) state.updateBounds(stageBounds(viewport, inset))
    }

    val touchRadius = with(density) { size.handleTouchRadius.toPx() }
    val minFrame = with(density) { size.minFrame.toPx() }
    val keyStep = with(density) { KeyStep.toPx() }
    val actions = stageActions(state, labels, step = keyStep * ShiftMultiplier, minFrame = minFrame)
    val ringWidth = size.focusRingWidth

    Box(
        modifier = modifier
            .clipToBounds()
            .onSizeChanged {
                viewport = it
                // During layout, so the frame is in place before this frame draws.
                if (it.width > 0 && it.height > 0) state.updateBounds(stageBounds(it, inset))
            }
            .indication(
                source,
                // Inside the edge: the stage fills its slot, and its clip would cut a ring drawn outside.
                focusRingIndication(
                    shape = RectangleShape,
                    focusOutline = FocusOutline(color = style.focusRing, width = ringWidth, offset = -ringWidth),
                ),
            )
            .semantics {
                if (contentDescription != null) this.contentDescription = contentDescription
                customActions = actions
            }
            .onFocusChanged { if (!it.isFocused) state.releaseKey(null) }
            .onKeyEvent { event -> state.onKey(event, keyStep) }
            .focusRequester(focusRequester)
            .focusable(interactionSource = source)
            .cropGestures(state, touchRadius, minFrame, onMousePress = focusRequester::requestFocus)
            .cropScrollZoom(state)
            .drawBehind { drawStage(painter, state, shape, style, size, guides) },
    )
}

/**
 * Area of a stage of [viewport] that the frame may occupy.
 *
 * @param viewport Size of the stage, in pixels.
 * @param inset Space kept at every edge, in pixels.
 * @return The area, in viewport pixels.
 */
private fun stageBounds(viewport: IntSize, inset: Float): Rect =
    Rect(inset, inset, viewport.width - inset, viewport.height - inset)

/**
 * Draws the stage: ground, photo, veil, guides, outline and grips.
 *
 * @param painter The photo.
 * @param state Crop to draw.
 * @param shape Mask of the frame.
 * @param style Colors.
 * @param metrics Stroke widths and grip lengths.
 * @param guides Visibility of the guides, `0..1`; also lifts the veil.
 */
private fun DrawScope.drawStage(
    painter: Painter,
    state: ImageCropState,
    shape: Shape,
    style: ImageCropperStyle,
    metrics: ImageCropperSize,
    guides: Float,
) {
    drawRect(style.stage)
    val transform = state.drawnTransform ?: return
    val image = state.imageSize
    val projection = transform.project(image)
    val frame = transform.frame

    // The mirror animates as a squeeze through zero, like a card turning over; a squeeze of exactly
    // zero would make the matrix singular.
    val squeeze = cos(PI.toFloat() * state.mirrorProgress).let { if (abs(it) < MinSqueeze) MinSqueeze else it }
    withTransform(
        transformBlock = {
            translate(frame.center.x, frame.center.y)
            scale(squeeze, 1f, pivot = Offset.Zero)
            rotate(transform.angle, pivot = Offset.Zero)
            scale(transform.mirrorSign * projection.scale, projection.scale, pivot = Offset.Zero)
            translate(-transform.focus.x - image.width / 2f, -transform.focus.y - image.height / 2f)
        },
    ) {
        with(painter) { draw(image) }
    }

    val mask = Path().apply {
        addOutline(shape.createOutline(frame.size, layoutDirection, this@drawStage))
        translate(frame.topLeft)
    }
    clipPath(mask, clipOp = ClipOp.Difference) {
        drawRect(lerp(style.veil, style.veilActive, guides))
    }

    if (guides > 0f) {
        val lines = if (state.isStraightening) FineGridDivisions else GridDivisions
        val stroke = metrics.gridStroke.toPx()
        val color = style.grid.copy(alpha = style.grid.alpha * guides * if (state.isStraightening) FineGridAlpha else 1f)
        clipPath(mask) {
            for (index in 1 until lines) {
                val x = frame.left + frame.width * index / lines
                val y = frame.top + frame.height * index / lines
                drawLine(color, Offset(x, frame.top), Offset(x, frame.bottom), stroke)
                drawLine(color, Offset(frame.left, y), Offset(frame.right, y), stroke)
            }
        }
    }

    if (state.spec.isResizable && shape != RectangleShape) {
        drawRect(
            color = style.frame.copy(alpha = style.frame.alpha * BoundingBoxAlpha),
            topLeft = frame.topLeft,
            size = frame.size,
            style = Stroke(metrics.gridStroke.toPx()),
        )
    }
    drawPath(mask, style.frame, style = Stroke(metrics.frameStroke.toPx()))
    if (state.spec.isResizable) drawGrips(frame, style, metrics, edges = state.aspect == CropAspect.Free)
}

/**
 * Draws the frame's corner grips, and its edge grips when [edges] and the frame is large enough to
 * fit them between the corners.
 *
 * Grips sit just outside the frame line, so they never cover the photo being framed.
 *
 * @param frame Crop frame in pixels.
 * @param style Colors.
 * @param metrics Grip length and thickness.
 * @param edges Whether to draw the edge grips.
 */
private fun DrawScope.drawGrips(frame: Rect, style: ImageCropperStyle, metrics: ImageCropperSize, edges: Boolean) {
    val length = metrics.handleLength.toPx()
    val thickness = metrics.handleThickness.toPx()
    val out = thickness / 2f
    val path = Path()
    listOf(
        Triple(frame.topLeft, 1f, 1f),
        Triple(frame.topRight, -1f, 1f),
        Triple(frame.bottomRight, -1f, -1f),
        Triple(frame.bottomLeft, 1f, -1f),
    ).forEach { (corner, inX, inY) ->
        val x = corner.x - inX * out
        val y = corner.y - inY * out
        path.moveTo(x + inX * length, y)
        path.lineTo(x, y)
        path.lineTo(x, y + inY * length)
    }
    if (edges && frame.width > length * EdgeGripRoom && frame.height > length * EdgeGripRoom) {
        val half = length / 2f
        path.moveTo(frame.center.x - half, frame.top - out)
        path.lineTo(frame.center.x + half, frame.top - out)
        path.moveTo(frame.center.x - half, frame.bottom + out)
        path.lineTo(frame.center.x + half, frame.bottom + out)
        path.moveTo(frame.left - out, frame.center.y - half)
        path.lineTo(frame.left - out, frame.center.y + half)
        path.moveTo(frame.right + out, frame.center.y - half)
        path.lineTo(frame.right + out, frame.center.y + half)
    }
    drawPath(path, style.handle, style = Stroke(thickness, cap = StrokeCap.Butt, join = StrokeJoin.Miter))
}

/**
 * Applies the stage's keyboard commands. A key held down is one step for undo, ended by its release.
 *
 * @param event Key event received by the focused stage.
 * @param step Distance an arrow key moves the photo, in pixels; four times as far with Shift.
 * @return Whether the key was a command.
 */
private fun ImageCropState.onKey(event: KeyEvent, step: Float): Boolean {
    val distance = if (event.isShiftPressed) step * ShiftMultiplier else step
    val command: ImageCropState.() -> Unit = when (event.key) {
        Key.DirectionLeft -> { { moveBy(Offset(-distance, 0f)) } }
        Key.DirectionRight -> { { moveBy(Offset(distance, 0f)) } }
        Key.DirectionUp -> { { moveBy(Offset(0f, -distance)) } }
        Key.DirectionDown -> { { moveBy(Offset(0f, distance)) } }
        Key.Plus, Key.Equals, Key.NumPadAdd -> { { zoomAround(KeyZoomFactor, anchor = null) } }
        Key.Minus, Key.NumPadSubtract -> { { zoomAround(1f / KeyZoomFactor, anchor = null) } }
        Key.LeftBracket -> { { straightenBy(-KeyStraighten) } }
        Key.RightBracket -> { { straightenBy(KeyStraighten) } }
        else -> return false
    }
    when (event.type) {
        KeyEventType.KeyDown -> pressKey(event.key, command)
        KeyEventType.KeyUp -> releaseKey(event.key)
        else -> return false
    }
    return true
}

/**
 * The stage's actions for assistive technology: everything a drag, a pinch or a grip does, as a
 * command of its own.
 *
 * @param state Crop the actions change.
 * @param labels Names of the actions.
 * @param step Distance a move action moves the photo, in pixels.
 * @param minFrame Smallest side a frame action may leave the frame with, in pixels.
 * @return The actions; the frame's only while it is resizable and [CropAspect.Free].
 */
private fun stageActions(
    state: ImageCropState,
    labels: CropToolbarLabels,
    step: Float,
    minFrame: Float,
): List<CustomAccessibilityAction> {
    val photo = listOf(
        CustomAccessibilityAction(labels.moveLeft) { state.perform { nudge(Offset(-step, 0f)) } },
        CustomAccessibilityAction(labels.moveRight) { state.perform { nudge(Offset(step, 0f)) } },
        CustomAccessibilityAction(labels.moveUp) { state.perform { nudge(Offset(0f, -step)) } },
        CustomAccessibilityAction(labels.moveDown) { state.perform { nudge(Offset(0f, step)) } },
        CustomAccessibilityAction(labels.zoomIn) { state.perform { zoomBy(ActionZoomFactor) } },
        CustomAccessibilityAction(labels.zoomOut) { state.perform { zoomBy(1f / ActionZoomFactor) } },
    )
    if (!state.spec.isResizable || state.aspect != CropAspect.Free) return photo
    return photo + listOf(
        CustomAccessibilityAction(labels.widenFrame) { state.stretchFrame(ActionStretchFactor, minFrame) },
        CustomAccessibilityAction(labels.heightenFrame) { state.stretchFrame(1f / ActionStretchFactor, minFrame) },
    )
}

/**
 * Runs [command] when the crop can be changed.
 *
 * @param command Command to run on the state.
 * @return Whether it ran: `false` until [ImageCropState.isReady].
 */
private inline fun ImageCropState.perform(command: ImageCropState.() -> Unit): Boolean {
    if (!isReady) return false
    command()
    return true
}

/** Distance an arrow key moves the photo. */
private val KeyStep = 10.dp

/** How much further an arrow key moves the photo with Shift held. */
private const val ShiftMultiplier = 4f

/** Zoom change per `+` or `−` press. */
private const val KeyZoomFactor = 1.1f

/** Straightening per `[` or `]` press, in degrees. */
private const val KeyStraighten = 0.5f

/** Zoom change per assistive-technology zoom action; larger than a key's, since each is announced. */
private const val ActionZoomFactor = 1.25f

/** Change of a free frame's width over height per assistive-technology frame action. */
private const val ActionStretchFactor = 1.15f

/** Rule-of-thirds divisions of the frame. */
private const val GridDivisions = 3

/** Divisions of the finer grid shown while straightening. */
private const val FineGridDivisions = 9

/** Strength of the finer grid relative to [ImageCropperStyle.grid], so nine lines don't shout. */
private const val FineGridAlpha = 0.55f

/** Strength of a non-rectangular frame's bounding box relative to [ImageCropperStyle.frame]. */
private const val BoundingBoxAlpha = 0.35f

/** Frame sides, in grip lengths, below which edge grips would crowd the corners and are left out. */
private const val EdgeGripRoom = 5f

/** Smallest magnitude of the mirror squeeze. */
private const val MinSqueeze = 0.001f

/** Length of the guides fading in and out. */
private const val GuidesFadeMillis = 200
