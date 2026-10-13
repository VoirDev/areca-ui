package dev.voir.areca.crop

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.isSpecified
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.TimeMark
import kotlin.time.TimeSource

/**
 * Creates and remembers the state of one crop, saved across configuration changes and process death.
 *
 * The state, with its crop and its undo history, lives as long as [spec] and [key] stay equal. A
 * photo of a different size starts a fresh crop on its own, but a different photo of the same size —
 * most photos from one camera — does not, so pass the photo, or its identifier, as [key] wherever one
 * state may be shown more than one photo.
 *
 * The crop and its history are saved as instance state; the photo is not. After the process is
 * recreated the crop is restored onto whatever photo [ImageCropper] is given, so the application
 * restores the photo itself — from a URI it keeps in its own saved state — and shows the same one. A
 * photo of another size starts a fresh crop, as always.
 *
 * @param spec What this use of the cropper decides. A spec that compares unequal starts a fresh crop.
 *   It is not saved: the composition passes it again after a restore.
 * @param key Identity of the photo being cropped. A key that compares unequal starts a fresh crop;
 *   `null` leaves it to [spec] and the photo's size. It is not saved either.
 * @return The state, to pass to [ImageCropper] and [CropToolbar].
 */
@Composable
public fun rememberImageCropState(spec: CropSpec = CropSpec(), key: Any? = null): ImageCropState {
    val scope = rememberCoroutineScope()
    return rememberSaveable(spec, key, scope, saver = imageCropStateSaver(spec, scope)) {
        ImageCropState(spec = spec, scope = scope)
    }
}

/**
 * Saves an [ImageCropState] as numbers — the photo's size, the stage's bounds, the crop and its
 * history — and restores it with [spec] and [scope].
 *
 * @param spec Spec the restored state is made with.
 * @param scope Scope the restored state animates in.
 * @return The saver.
 */
internal fun imageCropStateSaver(spec: CropSpec, scope: CoroutineScope): Saver<ImageCropState, FloatArray> = Saver(
    save = { state -> state.toSaved() },
    restore = { saved -> ImageCropState(spec = spec, scope = scope).apply { restoreSaved(saved) } },
)

/**
 * Where a photo sits under a crop frame, and the commands that move it.
 *
 * [ImageCropper] reports the photo's size and the stage's bounds to it and turns gestures into
 * calls on it; [CropToolbar] reads its values and calls its commands, [undo] among them. An application uses it to read
 * the [result] when the user is done, and may drive it from controls of its own.
 *
 * Commands that change the frame or rotation animate; commands driven continuously by a control
 * ([setStraighten], [setZoom]) apply at once. Every command first completes any running animation, so
 * the state is never left between two targets. Each discrete command — a button, a key press, an
 * assistive-technology action — records a step [undo] can return to; a continuous one is recorded
 * once, by the [startAdjusting] that begins it, and a burst of scroll-wheel ticks, or the repeats of
 * a held key, are recorded once.
 *
 * [straighten], [zoom], [rotation] and [isMirrored] report where the crop is going: during an
 * animation they already hold its target, which is also what [result] and [render] use. The stage
 * draws the animation in between.
 *
 * @property spec What this use of the cropper decides; it holds the starting aspect, the lock, the
 *   grips, the mask, the aspect choices, the zoom limit and the output size.
 * @param scope Scope the state's animations run in, tied to the composition that remembers it.
 * @param timeSource Clock that tells one scroll-wheel burst from the next.
 */
@Stable
public class ImageCropState internal constructor(
    public val spec: CropSpec,
    private val scope: CoroutineScope,
    private val timeSource: TimeSource = TimeSource.Monotonic,
) {
    /** Proportions the frame keeps now. Change it with [selectAspect]. */
    public var aspect: CropAspect by mutableStateOf(spec.aspect)
        private set

    /** Whether a [CropAspect.Ratio] is applied the other way round — 3:4 for a 4:3 ratio. */
    public var isOrientationSwapped: Boolean by mutableStateOf(false)
        private set

    /**
     * Whether the user is working the photo or a control right now, and a short while after.
     *
     * [ImageCropper] shows its grid and lifts its veil while this is true.
     */
    public var isAdjusting: Boolean by mutableStateOf(false)
        private set

    /** Whether the current adjustment is straightening, which draws a finer grid. */
    public var isStraightening: Boolean by mutableStateOf(false)
        private set

    /** The transform as stored: may run past its limits mid-drag or mid-animation. */
    internal var transform: CropTransform? by mutableStateOf(null)
        private set

    /** Size of the photo in pixels, or [Size.Unspecified] until [ImageCropper] reports it. */
    internal var imageSize: Size by mutableStateOf(Size.Unspecified)
        private set

    /** Progress of a running mirror animation, `0..1`; drawn as a horizontal squeeze through zero. */
    internal var mirrorProgress: Float by mutableFloatStateOf(0f)
        private set

    /** Area the frame may occupy, in viewport pixels. */
    private var bounds: Rect = Rect.Zero

    /** Density [ImageCropper] resolves the mask with; [render] resolves it the same way. */
    private var stageDensity: Density = Density(1f)

    /** Layout direction [ImageCropper] resolves the mask with; [render] resolves it the same way. */
    private var stageLayoutDirection: LayoutDirection = LayoutDirection.Ltr

    /**
     * When the running burst of scroll-wheel ticks ends, or `null` when none is running. Every other
     * step ends it early.
     */
    private var wheelBurstEnd: TimeMark? = null

    /** The key whose repeats are one step for [undo]. Every other step ends the hold. */
    private val keyHold = CropKeyHold()

    /** How [drawnTransform] treats a focus past its limits. */
    private var focusMode: FocusMode by mutableStateOf(FocusMode.Clamped)

    /** The running animation, if any. */
    private var animationJob: Job? = null

    /**
     * Where the running animation ends; applied at once if the animation is cut short. Snapshot state,
     * so what reads [settled] sees an animation start and end.
     */
    private var animationTarget: CropTransform? by mutableStateOf(null)

    /** The pending re-centring of the frame after a resize. */
    private var settleJob: Job? = null

    /** The pending end of [isAdjusting]. */
    private var guidesJob: Job? = null

    /**
     * How the crop stood before each change, oldest first, for [undo]. Neighbours never match, and
     * the oldest are dropped past [MaxHistory].
     */
    private val history = mutableStateListOf<CropSnapshot>()

    /** Whether the photo's size and the stage's bounds are both known, so the crop can be read. */
    public val isReady: Boolean get() = transform != null

    /** Fine rotation in degrees, clockwise, within ±45; the target while an animation runs. */
    public val straighten: Float get() = settled?.straighten ?: 0f

    /**
     * Magnification over the size at which the photo just covers the frame, `1..`[CropSpec.maxZoom];
     * the target while an animation runs.
     */
    public val zoom: Float get() = settled?.zoom ?: 1f

    /** Fine rotation as drawn this frame, passing through the values between while an animation runs. */
    internal val liveStraighten: Float get() = transform?.straighten ?: 0f

    /** Magnification as drawn this frame, passing through the values between while an animation runs. */
    internal val liveZoom: Float get() = transform?.zoom ?: 1f

    /** Quarter-turn rotation in degrees, clockwise: 0, 90, 180 or 270; the target while an animation runs. */
    public val rotation: Int get() = normalizeQuarterTurns(settled?.rotation ?: 0f).toInt()

    /** Whether the photo is mirrored left to right; the target while an animation runs. */
    public val isMirrored: Boolean get() = settled?.isMirrored ?: false

    /** Whether [swapOrientation] would change anything. */
    public val canSwapOrientation: Boolean
        get() = !spec.isAspectLocked && (aspect as? CropAspect.Ratio)?.isSquare == false

    /** Whether anything differs from the starting crop, so [reset] would change something. */
    public val canReset: Boolean
        get() {
            val current = snapshot() ?: return false
            val fresh = freshTransform() ?: return false
            return !current.matches(CropSnapshot(fresh, spec.aspect, isOrientationSwapped = false))
        }

    /**
     * Whether [undo] would change anything: some earlier crop in this session differs from the
     * current one.
     */
    public val canUndo: Boolean
        get() {
            val current = snapshot() ?: return false
            return history.any { !it.matches(current) }
        }

    /**
     * The transform as it will be once any running animation ends.
     *
     * Reads both states every time, so a reader stays subscribed to [transform] while an animation
     * runs and sees the changes that follow it.
     */
    private val settled: CropTransform?
        get() {
            val target = animationTarget
            val current = transform
            return target ?: current
        }

    /**
     * The crop as it stands, or as it will stand once any running animation ends.
     *
     * @return The crop, or `null` until [isReady].
     */
    public fun result(): CropResult? {
        val current = settled?.clamped(imageSize) ?: return null
        val projection = current.project(imageSize)
        return CropResult(
            imageSize = imageSize,
            center = current.focus + Offset(imageSize.width / 2f, imageSize.height / 2f),
            size = Size(current.frame.width / projection.scale, current.frame.height / projection.scale),
            rotation = normalizeHalfTurn(current.angle),
            isMirrored = current.isMirrored,
        )
    }

    /**
     * Renders the crop as [spec] asks: cut to [CropSpec.mask] and no larger than
     * [CropSpec.maxOutputDimension].
     *
     * [painter] may be a larger copy of the photo the stage showed: the stage can show a preview
     * decoded to a display size while the render cuts the full-resolution photo. When the painter's
     * intrinsic size differs from the stage's photo, the crop is scaled to it first
     * ([CropResult.scaledTo]), so the output keeps the larger photo's detail. Both must be the same
     * picture in the same orientation.
     *
     * The mask is the one the stage drew: it is resolved at the frame's size on screen, with the
     * density and layout direction [ImageCropper] draws with, then scaled to the output. A mask sized
     * in dp, such as `RoundedCornerShape(12.dp)`, keeps the proportions the user saw, and a mask that
     * differs between start and end keeps the side it was drawn on.
     *
     * Runs on the calling thread and allocates the whole bitmap; see [CropResult.render] for the
     * memory it takes and what [painter] must be on Android.
     *
     * @param painter The photo the crop was made on: the one [ImageCropper] shows, or the same photo
     *   at a larger size.
     * @param applyMask Whether to cut the result to the mask. Pass `false` to store the rectangular
     *   crop and mask it only where it is shown — an avatar drawn by a component that clips it anyway.
     * @return The cropped image, or `null` until [isReady].
     * @throws IllegalArgumentException When [painter] cannot be drawn into a software bitmap, as an
     *   Android hardware bitmap cannot, or when its proportions differ from the stage's photo.
     */
    public fun render(painter: Painter, applyMask: Boolean = true): ImageBitmap? {
        val preview = result() ?: return null
        val source = painter.intrinsicSize
        val result = if (source.isSpecified && source != imageSize) preview.scaledTo(source) else preview
        val frame = settled?.frame ?: return null
        val outputSize = result.outputSize(spec.maxOutputDimension)
        val target = Size(outputSize.width.toFloat(), outputSize.height.toFloat())
        val clip = if (applyMask && spec.mask != RectangleShape) {
            maskPath(spec.mask, frame.size, target, stageDensity, stageLayoutDirection)
        } else {
            null
        }
        return result.renderClipped(
            painter = painter,
            outputSize = outputSize,
            clip = clip,
            density = Density(stageDensity.density * target.width / frame.width, stageDensity.fontScale),
            layoutDirection = stageLayoutDirection,
        )
    }

    /**
     * Changes the frame's proportions, animating it to the largest frame of the new aspect.
     *
     * @param aspect New proportions.
     */
    public fun selectAspect(aspect: CropAspect) {
        checkpoint()
        applyAspect(aspect)
    }

    /** Applies the current ratio the other way round: 4:3 becomes 3:4. Does nothing unless [canSwapOrientation]. */
    public fun swapOrientation() {
        if (!canSwapOrientation) return
        checkpoint()
        isOrientationSwapped = !isOrientationSwapped
        applyAspect(aspect)
    }

    /**
     * Turns the photo a quarter turn anticlockwise.
     *
     * Unless the aspect is locked, the frame turns with it, so a landscape crop stays the same crop
     * in portrait.
     */
    public fun rotateLeft() {
        checkpoint()
        finishAnimation()
        settleJob?.cancel()
        val current = transform ?: return
        val frame = if (spec.isAspectLocked) {
            current.frame
        } else {
            if ((aspect as? CropAspect.Ratio)?.isSquare == false) isOrientationSwapped = !isOrientationSwapped
            fitFrame(bounds, current.frame.height / current.frame.width)
        }
        animateTo(
            target = current.copy(rotation = normalizeQuarterTurns(current.rotation - 90f), frame = frame)
                .clamped(imageSize),
            durationMillis = TurnMillis,
        )
        pulseGuides(straightening = false)
    }

    /** Mirrors the photo left to right, as it appears on screen. */
    public fun flipHorizontally() {
        checkpoint()
        finishAnimation()
        val current = transform ?: return
        animateTo(current.mirrored(), MirrorMillis, mirror = true)
    }

    /**
     * Sets the fine rotation at once. Values within a third of a degree of level snap to level.
     *
     * Driven continuously, so it records no step for [undo] itself: a control calls [startAdjusting]
     * when a drag begins, which does.
     *
     * @param degrees Clockwise rotation, coerced into ±45.
     */
    public fun setStraighten(degrees: Float) {
        finishAnimation()
        val current = transform ?: return
        val value = degrees.coerceIn(-MaxStraighten, MaxStraighten).let { if (abs(it) < StraightenSnap) 0f else it }
        transform = current.copy(straighten = value).clamped(imageSize)
    }

    /**
     * Sets the magnification at once, holding the frame's centre still. Like [setStraighten], it
     * leaves recording a step for [undo] to [startAdjusting].
     *
     * @param zoom Magnification over the cover size, coerced into `1..`[CropSpec.maxZoom].
     */
    public fun setZoom(zoom: Float) {
        finishAnimation()
        val current = transform ?: return
        transform = current.zoomed(zoom, anchor = null, image = imageSize, maxZoom = spec.maxZoom)
    }

    /** Animates the fine rotation back to level. */
    public fun resetStraighten() {
        checkpoint()
        finishAnimation()
        val current = transform ?: return
        animateTo(current.copy(straighten = 0f).clamped(imageSize), ValueMillis)
    }

    /** Animates the magnification back to the cover size. */
    public fun resetZoom() {
        checkpoint()
        finishAnimation()
        val current = transform ?: return
        animateTo(current.zoomed(1f, anchor = null, image = imageSize, maxZoom = spec.maxZoom), ValueMillis)
    }

    /**
     * Animates everything back to the starting crop: aspect, rotation, mirror, zoom and position. A
     * step of its own, so [undo] brings the changes back.
     */
    public fun reset() {
        checkpoint()
        finishAnimation()
        settleJob?.cancel()
        aspect = spec.aspect
        isOrientationSwapped = false
        val fresh = freshTransform() ?: return
        animateTo(fresh, ResetMillis)
    }

    /**
     * Records a step for [undo] and shows the guides while a control drags a value. Pair every call
     * with [stopAdjusting].
     *
     * [CropToolbar] calls it for its dial; a custom control that drives [setStraighten] or [setZoom]
     * calls it too, so the grid appears and the drag undoes the same way.
     *
     * @param straightening Whether the control straightens, which draws a finer grid.
     */
    public fun startAdjusting(straightening: Boolean = false) {
        checkpoint()
        showGuides(straightening)
    }

    /**
     * Steps back to how the crop stood before the latest change that is still in effect, animating
     * there. Repeated, it walks back through every change of the session, across tools, to the
     * starting crop. Does nothing unless [canUndo].
     */
    public fun undo() {
        val current = snapshot() ?: return
        while (history.lastOrNull()?.matches(current) == true) history.removeAt(history.lastIndex)
        val previous = history.removeLastOrNull() ?: return
        restore(previous)
    }

    /** Hides the guides a short while after the adjustment ends, so they fade rather than blink. */
    public fun stopAdjusting() {
        guidesJob?.cancel()
        guidesJob = scope.launch {
            delay(GuidesLingerMillis)
            isAdjusting = false
            isStraightening = false
        }
    }

    /**
     * Captures the crop as it will stand once any running animation ends.
     *
     * @return The snapshot, or `null` until [isReady].
     */
    internal fun snapshot(): CropSnapshot? {
        val current = settled ?: return null
        return CropSnapshot(current.clamped(imageSize), aspect, isOrientationSwapped)
    }

    /**
     * Records how the crop stands now as a step [undo] can return to, unless it is the step already on
     * top. Called before every change, and ends any running scroll-wheel burst or key hold.
     */
    internal fun checkpoint() {
        wheelBurstEnd = null
        keyHold.clear()
        val current = snapshot() ?: return
        if (history.lastOrNull()?.matches(current) == true) return
        history.add(current)
        if (history.size > MaxHistory) history.removeAt(0)
    }

    /**
     * Animates back to [snapshot]: aspect, rotation, mirror, straightening, zoom and position.
     *
     * @param snapshot Crop to return to, in the current bounds.
     */
    internal fun restore(snapshot: CropSnapshot) {
        finishAnimation()
        settleJob?.cancel()
        wheelBurstEnd = null
        keyHold.clear()
        if (transform == null) return
        aspect = snapshot.aspect
        isOrientationSwapped = snapshot.isOrientationSwapped
        animateTo(snapshot.transform, ResetMillis)
        showGuides(straightening = false)
        stopAdjusting()
    }

    /** Ends any running animation at its target at once. */
    internal fun finishAnimation() {
        animationJob?.cancel()
        animationJob = null
        completeAnimation()
    }

    /**
     * The transform as [ImageCropper] draws it: rubber-banded during a drag, otherwise kept inside
     * the photo.
     */
    internal val drawnTransform: CropTransform?
        get() {
            val current = transform ?: return null
            return when (focusMode) {
                FocusMode.Clamped -> current.clamped(imageSize)
                FocusMode.Rubber -> current.rubberBanded(imageSize)
                FocusMode.Free -> current
            }
        }

    /**
     * Records the photo's size. A new size is a new photo, and starts a fresh crop.
     *
     * @param size Intrinsic size of the photo, in pixels.
     */
    internal fun updateImage(size: Size) {
        if (size == imageSize) return
        finishAnimation()
        settleJob?.cancel()
        imageSize = size
        aspect = spec.aspect
        isOrientationSwapped = false
        history.clear()
        transform = freshTransform()
    }

    /**
     * Records how the stage resolves the mask, so [render] can resolve it the same way.
     *
     * @param density Density of the stage.
     * @param layoutDirection Layout direction of the stage.
     */
    internal fun updateStage(density: Density, layoutDirection: LayoutDirection) {
        stageDensity = density
        stageLayoutDirection = layoutDirection
    }

    /**
     * Records the area the frame may occupy. The frame is refitted to it, keeping its proportions,
     * and so is every step [undo] keeps, so the history still compares with the crop on screen.
     *
     * Empty bounds, from a stage no larger than its padding, are ignored: no frame fits them.
     *
     * @param bounds Area inside the stage's padding, in viewport pixels.
     */
    internal fun updateBounds(bounds: Rect) {
        if (bounds == this.bounds || bounds.width <= 0f || bounds.height <= 0f) return
        finishAnimation()
        settleJob?.cancel()
        this.bounds = bounds
        val current = transform
        transform = if (current == null) {
            freshTransform()
        } else {
            current.copy(frame = fitFrame(bounds, current.frame.aspectRatio)).clamped(imageSize)
        }
        if (imageSize.isSpecified) {
            for (index in history.indices) history[index] = history[index].refitted(bounds, imageSize)
        }
    }

    /**
     * The state as numbers for saved instance state: the photo's size, the stage's bounds, the crop as
     * it will stand once any running animation ends, and the history.
     *
     * @return The numbers, empty until [isReady].
     */
    internal fun toSaved(): FloatArray {
        val current = snapshot() ?: return FloatArray(0)
        val out = mutableListOf(imageSize.width, imageSize.height, bounds.left, bounds.top, bounds.right, bounds.bottom)
        current.writeTo(out)
        history.forEach { it.writeTo(out) }
        return out.toFloatArray()
    }

    /**
     * Restores what [toSaved] saved. Numbers that do not describe a crop leave the state fresh.
     *
     * The stage reports its own photo size and bounds afterwards: the same size keeps the crop, and new
     * bounds refit it.
     *
     * @param saved Numbers from [toSaved].
     */
    internal fun restoreSaved(saved: FloatArray) {
        if (saved.size < SavedHeaderFloats + SnapshotFloats) return
        val image = Size(saved[0], saved[1])
        val savedBounds = Rect(saved[2], saved[3], saved[4], saved[5])
        if (image.width <= 0f || image.height <= 0f || savedBounds.width <= 0f || savedBounds.height <= 0f) return
        val current = CropSnapshot.readFrom(saved, SavedHeaderFloats) ?: return
        val steps = (SavedHeaderFloats + SnapshotFloats until saved.size step SnapshotFloats)
            .map { offset -> CropSnapshot.readFrom(saved, offset) ?: return }
        imageSize = image
        bounds = savedBounds
        aspect = current.aspect
        isOrientationSwapped = current.isOrientationSwapped
        transform = current.transform
        history.clear()
        history.addAll(steps)
    }

    /**
     * The frame grip under a press, if any.
     *
     * @param point Press position in viewport pixels.
     * @param touchRadius Reach of a grip in pixels.
     * @return The grip, or `null` when the press should move the photo.
     */
    internal fun handleAt(point: Offset, touchRadius: Float): CropHandle? {
        if (!spec.isResizable) return null
        val frame = transform?.frame ?: return null
        return handleAt(frame, point, touchRadius, includeEdges = aspect == CropAspect.Free)
    }

    /**
     * Prepares for a touch: completes animations, cancels re-centring, records a step for [undo], and
     * shows the guides.
     */
    internal fun beginGesture() {
        finishAnimation()
        settleJob?.cancel()
        transform = transform?.clamped(imageSize)
        startAdjusting()
    }

    /** Starts moving the photo with a finger, letting it stretch past the edges. */
    internal fun startPan() {
        focusMode = FocusMode.Rubber
    }

    /**
     * Moves the photo with a finger.
     *
     * @param delta Distance moved, in viewport pixels.
     */
    internal fun panBy(delta: Offset) {
        val current = transform ?: return
        transform = current.panned(delta, imageSize)
    }

    /**
     * Moves and zooms the photo with two fingers.
     *
     * @param pan Distance the fingers' centroid moved, in viewport pixels.
     * @param zoomChange Ratio of the fingers' spread to the previous event's.
     * @param centroid Fingers' centroid, which holds still under the zoom.
     */
    internal fun pinch(pan: Offset, zoomChange: Float, centroid: Offset) {
        val current = transform ?: return
        focusMode = FocusMode.Clamped
        val moved = current.clamped(imageSize).panned(pan, imageSize).clamped(imageSize)
        transform = moved.zoomed(moved.zoom * zoomChange, centroid, imageSize, spec.maxZoom)
    }

    /**
     * Resizes the frame from a grip, leaving the photo where it is on screen.
     *
     * @param handle Grip being dragged.
     * @param start Transform when the drag began.
     * @param delta Total drag distance since the press, in viewport pixels.
     * @param minSize Smallest side of the frame, in pixels.
     */
    internal fun resize(handle: CropHandle, start: CropTransform, delta: Offset, minSize: Float) {
        val ratio = if (aspectRatioOf(aspect, start.rotation) == null) null else start.frame.aspectRatio
        val frame = resizeFrame(start.frame, handle, delta, bounds, minSize, ratio)
        transform = start.withFrame(frame, imageSize, spec.maxZoom)
    }

    /**
     * Ends a touch: springs a stretched photo back, re-centres a resized frame after a pause, and lets
     * the guides fade.
     */
    internal fun endGesture() {
        val current = transform
        if (focusMode == FocusMode.Rubber && current != null) {
            val released = current.rubberBanded(imageSize)
            transform = released
            animateTo(released.clamped(imageSize), SpringBackMillis, mode = FocusMode.Free)
        } else {
            focusMode = FocusMode.Clamped
        }
        scheduleSettle()
        stopAdjusting()
    }

    /**
     * Double-tap: zooms in on the tapped point, or back out when already zoomed.
     *
     * @param anchor Tap position in viewport pixels.
     */
    internal fun toggleZoom(anchor: Offset) {
        checkpoint()
        finishAnimation()
        val current = transform ?: return
        val target = if (current.zoom < DoubleTapThreshold) DoubleTapZoom else 1f
        animateTo(current.zoomed(target, anchor, imageSize, spec.maxZoom), ValueMillis)
    }

    /**
     * Zooms by a factor around the frame's centre at once, for a key or an assistive-technology
     * action. A step of its own for [undo].
     *
     * @param factor Multiplier applied to the current zoom.
     */
    internal fun zoomBy(factor: Float) {
        checkpoint()
        zoomAround(factor, anchor = null)
    }

    /**
     * Zooms by a factor around the pointer at once, for a scroll wheel or a trackpad scroll.
     *
     * Ticks that follow each other within [WheelBurstMillis], with no other step between them, are one
     * step for [undo], so one turn of the wheel undoes at once.
     *
     * @param factor Multiplier applied to the current zoom.
     * @param anchor Pointer position, which holds still.
     */
    internal fun scrollZoom(factor: Float, anchor: Offset) {
        if (wheelBurstEnd?.hasPassedNow() != false) checkpoint()
        zoomAround(factor, anchor)
        wheelBurstEnd = timeSource.markNow() + WheelBurstMillis.milliseconds
    }

    /**
     * Moves the photo by a fixed step, for an assistive-technology action. A step of its own for
     * [undo].
     *
     * @param delta Distance in viewport pixels.
     */
    internal fun nudge(delta: Offset) {
        checkpoint()
        moveBy(delta)
    }

    /**
     * Applies a key's command. The first key-down of a key is a step for [undo]; the repeats of a key
     * held down are part of the same step, until [releaseKey] or any other step.
     *
     * @param key Key pressed.
     * @param command Change the key makes, without recording a step itself: [moveBy],
     *   [zoomAround] or [straightenBy].
     */
    internal fun pressKey(key: Key, command: ImageCropState.() -> Unit) {
        if (keyHold.press(key)) {
            // A checkpoint ends every hold, so the new one starts after it.
            checkpoint()
            keyHold.press(key)
        }
        command()
    }

    /**
     * Ends the hold of [key], so its next press is a step of its own.
     *
     * @param key Key released, or `null` to end any hold, as when the stage loses focus.
     */
    internal fun releaseKey(key: Key?) {
        if (key == null) keyHold.clear() else keyHold.release(key)
    }

    /**
     * Moves the photo by a fixed step at once, without recording a step.
     *
     * @param delta Distance in viewport pixels.
     */
    internal fun moveBy(delta: Offset) {
        finishAnimation()
        val current = transform ?: return
        transform = current.panned(delta, imageSize).clamped(imageSize)
        pulseGuides(straightening = false)
    }

    /**
     * Steps the fine rotation at once, without recording a step.
     *
     * @param degrees Change in degrees, clockwise.
     */
    internal fun straightenBy(degrees: Float) {
        setStraighten(straighten + degrees)
        pulseGuides(straightening = true)
    }

    /**
     * Changes a free frame's proportions by a factor, for an assistive-technology action, as dragging
     * its grips would: the frame refits the stage while the photo stays where it is on screen, growing
     * only where it would no longer cover the frame. A step of its own for [undo].
     *
     * @param factor Multiplier applied to the frame's width over height; above 1 makes it wider.
     * @param minSize Smallest side the frame may have, in pixels.
     * @return Whether the frame changed: `false` unless [CropSpec.isResizable] and the aspect is
     *   [CropAspect.Free], and when either side would fall below [minSize].
     */
    internal fun stretchFrame(factor: Float, minSize: Float): Boolean {
        if (!spec.isResizable || aspect != CropAspect.Free) return false
        finishAnimation()
        val current = transform ?: return false
        val frame = fitFrame(bounds, current.frame.aspectRatio * factor)
        if (frame.width < minSize || frame.height < minSize) return false
        checkpoint()
        settleJob?.cancel()
        animateTo(current.withFrame(frame, imageSize, spec.maxZoom), FrameMillis)
        pulseGuides(straightening = false)
        return true
    }

    /**
     * Zooms by a factor around a point at once, without recording a step.
     *
     * @param factor Multiplier applied to the current zoom.
     * @param anchor Point that holds still, or `null` for the frame's centre.
     */
    internal fun zoomAround(factor: Float, anchor: Offset?) {
        finishAnimation()
        val current = transform ?: return
        transform = current.zoomed(current.zoom * factor, anchor, imageSize, spec.maxZoom)
        pulseGuides(straightening = false)
    }

    /**
     * Width over height of the frame for [aspect], or `null` when it is free.
     *
     * @param aspect Proportions to resolve.
     * @param rotation Rotation in degrees, which turns [CropAspect.Original] on its side.
     * @return The ratio.
     */
    private fun aspectRatioOf(aspect: CropAspect, rotation: Float): Float? = when (aspect) {
        CropAspect.Free -> null
        CropAspect.Original -> {
            val ratio = imageSize.width / imageSize.height
            if (normalizeQuarterTurns(rotation) % 180f == 90f) 1f / ratio else ratio
        }
        is CropAspect.Ratio -> if (isOrientationSwapped && !aspect.isSquare) 1f / aspect.value else aspect.value
    }

    /**
     * The starting crop for the current photo and bounds.
     *
     * @return The crop [reset] returns to, or `null` while the photo's size or the stage's bounds are
     *   unknown.
     */
    private fun freshTransform(): CropTransform? {
        if (!imageSize.isSpecified || imageSize.width <= 0f || imageSize.height <= 0f) return null
        if (bounds.width <= 0f || bounds.height <= 0f) return null
        val ratio = when (val aspect = spec.aspect) {
            CropAspect.Free, CropAspect.Original -> imageSize.width / imageSize.height
            is CropAspect.Ratio -> aspect.value
        }
        return CropTransform(frame = fitFrame(bounds, ratio))
    }

    /**
     * Shows the guides until [stopAdjusting], without recording a step.
     *
     * @param straightening Whether to draw the finer straightening grid.
     */
    private fun showGuides(straightening: Boolean) {
        guidesJob?.cancel()
        isAdjusting = true
        isStraightening = straightening
    }

    /**
     * Changes the frame's proportions without recording a step, animating it to the largest frame of
     * the new aspect.
     *
     * @param aspect New proportions.
     */
    private fun applyAspect(aspect: CropAspect) {
        finishAnimation()
        settleJob?.cancel()
        this.aspect = aspect
        val current = transform ?: return
        val frame = fitFrame(bounds, aspectRatioOf(aspect, current.rotation) ?: current.frame.aspectRatio)
        animateTo(current.copy(frame = frame).clamped(imageSize), FrameMillis)
    }

    /** Re-centres a resized frame after a pause, so a second drag of the same handle can follow. */
    private fun scheduleSettle() {
        settleJob?.cancel()
        settleJob = scope.launch {
            delay(SettleDelayMillis)
            val current = settled ?: return@launch
            val fitted = fitFrame(bounds, current.frame.aspectRatio)
            if (!current.frame.approximately(fitted)) animateTo(current.copy(frame = fitted), SettleMillis)
        }
    }

    /**
     * Shows the guides briefly, for a one-off change such as a key press.
     *
     * @param straightening Whether the change straightened the photo.
     */
    private fun pulseGuides(straightening: Boolean) {
        showGuides(straightening)
        stopAdjusting()
    }

    /**
     * Animates from the current transform to [target], completing any running animation first.
     *
     * The rotation takes the short way round, and a change of mirroring is drawn as a squeeze through
     * zero alongside the rest; see [animationPath].
     *
     * @param target Transform to end at.
     * @param durationMillis Length of the animation.
     * @param mode How a focus past its limits is drawn meanwhile.
     * @param mirror Whether this is a mirror: the transform holds still and [mirrorProgress] runs
     *   instead, then [target] is applied.
     */
    private fun animateTo(
        target: CropTransform,
        durationMillis: Int,
        mode: FocusMode = FocusMode.Clamped,
        mirror: Boolean = false,
    ) {
        finishAnimation()
        val start = transform ?: return
        val path = animationPath(start, target)
        val flips = !mirror && start.isMirrored != target.isMirrored
        animationTarget = target
        focusMode = mode
        animationJob = scope.launch {
            animate(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = tween(durationMillis, easing = if (mirror) FastOutSlowInEasing else CropEasing),
            ) { value, _ ->
                if (mirror) {
                    mirrorProgress = value
                } else {
                    transform = lerp(start, path, value)
                    if (flips) mirrorProgress = value
                }
            }
            animationJob = null
            completeAnimation()
        }
    }

    /** Applies the running animation's target, if an animation was running. */
    private fun completeAnimation() {
        val target = animationTarget ?: return
        animationTarget = null
        transform = target
        mirrorProgress = 0f
        focusMode = FocusMode.Clamped
    }
}

/** How a focus past its limits is drawn. */
private enum class FocusMode {
    /** Pulled back inside the photo. The normal case. */
    Clamped,

    /** Stretched with resistance, while a finger drags the photo past an edge. */
    Rubber,

    /** Drawn as stored, while a stretched photo springs back. */
    Free,
}

/** Width over height. */
private val Rect.aspectRatio: Float get() = width / height

/**
 * Brings an angle into `-180..180`.
 *
 * @param degrees Angle in degrees.
 * @return The equivalent angle.
 */
private fun normalizeHalfTurn(degrees: Float): Float = ((degrees + 180f) % 360f + 360f) % 360f - 180f

/** Settling curve shared by every frame and rotation animation: a quick start and a long, soft landing. */
private val CropEasing = CubicBezierEasing(0.2f, 0.8f, 0.2f, 1f)

/** Largest fine rotation either way, in degrees. */
private const val MaxStraighten = 45f

/** Fine rotation closer to level than this, in degrees, snaps to level. */
private const val StraightenSnap = 0.3f

/** Zoom a double-tap goes to from the cover size. */
private const val DoubleTapZoom = 2.5f

/** Zoom below which a double-tap zooms in rather than out. */
private const val DoubleTapThreshold = 1.8f

/** Length of an aspect change. */
private const val FrameMillis = 340

/** Length of a quarter turn. */
private const val TurnMillis = 300

/** Length of a mirror. */
private const val MirrorMillis = 380

/** Length of a single value returning to its origin, or a double-tap zoom. */
private const val ValueMillis = 300

/** Length of a full reset. */
private const val ResetMillis = 360

/** Length of a stretched photo springing back. */
private const val SpringBackMillis = 280

/** Pause after a resize before the frame re-centres. */
private const val SettleDelayMillis = 380L

/** Length of the frame re-centring. */
private const val SettleMillis = 420

/** Most steps [ImageCropState.undo] can walk back. */
private const val MaxHistory = 100

/** How long the guides stay after an adjustment ends. */
private const val GuidesLingerMillis = 450L

/** Longest pause between scroll-wheel ticks that still belong to one step for [ImageCropState.undo]. */
private const val WheelBurstMillis = 450

/** Numbers [ImageCropState.toSaved] writes before the crop: the photo's size and the stage's bounds. */
private const val SavedHeaderFloats = 6
