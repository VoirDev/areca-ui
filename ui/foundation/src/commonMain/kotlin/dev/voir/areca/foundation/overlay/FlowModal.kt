package dev.voir.areca.foundation.overlay

import androidx.annotation.FloatRange
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.constrainHeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isSpecified
import androidx.compose.ui.window.DialogProperties
import dev.voir.areca.modifier.thenIf
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.slide
import dev.voir.areca.theme.tween
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt
import androidx.compose.ui.window.Dialog as ComposeDialog
import dev.voir.areca.theme.SurfaceOf
import dev.voir.areca.theme.SurfaceRoot
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.layout.Layout
import androidx.compose.foundation.layout.consumeWindowInsets

/**
 * Colors and shape of a [FlowModal]: the scrim behind it, the sheet and its drag handle.
 *
 * @property overlay Scrim covering the screen the sheet interrupts.
 * @property bg Fill of the sheet.
 * @property drag Color of the drag handle at the top of a draggable sheet.
 * @property shape Shape of the sheet; round only the top corners, since the bottom edge runs off
 *   the screen.
 */
@Immutable
public data class FlowModalStyle(
    public val overlay: Color,
    public val bg: Color,
    public val drag: Color,
    public val shape: Shape,
) {
    /** The theme's default [FlowModalStyle], and [default] for building a variation on it. */
    public companion object {
        /** Theme default: the canvas, with the top corners rounded. */
        public val Default: FlowModalStyle
            @Composable
            get() = default()

        /**
         * Builds a style from the current theme.
         *
         * @param overlay Scrim covering the screen the sheet interrupts.
         * @param bg Fill of the sheet. Defaults to the sheet surface — white, lifted in the dark —
         *   whatever the screen's layout; a sheet given the grey of a card is laid out as one, so
         *   the cards inside it turn white.
         * @param drag Color of the drag handle.
         * @param shape Shape of the sheet.
         * @return Style built from the supplied colors and shape.
         */
        @Composable
        public fun default(
            overlay: Color = ArecaTheme.colors.bgOverlayDark,
            bg: Color = ArecaTheme.surfaces.sheet.page.ground,
            drag: Color = ArecaTheme.colors.bgNeutralBase,
            shape: Shape = RoundedCornerShape(
                topStart = ArecaTheme.radius.lg,
                topEnd = ArecaTheme.radius.lg,
            ),
        ): FlowModalStyle = FlowModalStyle(
            overlay = overlay,
            bg = bg,
            drag = drag,
            shape = shape,
        )
    }
}

/**
 * Geometry of a [FlowModal]: how wide the sheet may grow, and its drag handle.
 *
 * The sheet's own heights are behaviour rather than geometry, and live on [FlowModalController].
 *
 * @property dragPadding Space around the drag handle, inside the top of the sheet. The handle floats
 *   over the header, which clears this space with [FlowModalHeaderInsets].
 * @property dragWidth Width of the drag handle.
 * @property dragHeight Thickness of the drag handle.
 * @property maxWidth Widest the sheet grows, centred along the bottom edge on a wider window, or
 *   [Dp.Unspecified] to span the window. An app that keeps a phone layout on a tablet caps its
 *   sheets here: a sheet opens in its own window, so a width limit on the screens behind it does
 *   not reach it. The scrim still covers the whole window.
 */
@Immutable
public data class FlowModalSize(
    public val dragPadding: PaddingValues,
    public val dragWidth: Dp,
    public val dragHeight: Dp,
    public val maxWidth: Dp = Dp.Unspecified,
) {
    /** The theme's default [FlowModalSize], and [default] for building a variation on it. */
    public companion object {
        /** Theme default. */
        public val Default: FlowModalSize
            @Composable
            get() = default()

        /**
         * Builds a size from the current theme.
         *
         * @param dragPadding Space around the drag handle.
         * @param dragWidth Width of the drag handle.
         * @param dragHeight Thickness of the drag handle.
         * @param maxWidth Widest the sheet grows, or [Dp.Unspecified] to span the window.
         * @return Size built from the supplied metrics.
         */
        @Composable
        public fun default(
            dragPadding: PaddingValues = PaddingValues(
                top = ArecaTheme.spacing.sm,
                bottom = ArecaTheme.spacing.xs
            ),
            dragWidth: Dp = 44.dp,
            dragHeight: Dp = 4.dp,
            maxWidth: Dp = Dp.Unspecified,
        ): FlowModalSize = FlowModalSize(
            dragPadding = dragPadding,
            dragWidth = dragWidth,
            dragHeight = dragHeight,
            maxWidth = maxWidth
        )
    }
}

/**
 * Represents the discrete states the modal can be in.
 *
 * Notes:
 * - [Opening] and [Hiding] are transitional states used to run animations.
 * - The modal is considered "visible" in all states except [Hidden].
 */
public enum class FlowModalState {
    /** Fully hidden (not composed). */
    Hidden,

    /** Transitioning to [PartiallyOpened] or [Expanded]. */
    Opening,

    /** Visible at the configured partial height. */
    PartiallyOpened,

    /** Visible at the configured maximum height. */
    Expanded,

    /** Transitioning to [Hidden]. */
    Hiding
}

/**
 * State machine for a flow-style modal.
 *
 * The UI observes [state] and animates toward [targetState] while [state] is transitional. The
 * state changes only through [show], [expand], [collapse] and [hide], and through [FlowModal] as
 * its animations finish and drags come to rest; the transitions themselves are
 * [FlowModalStateMachine]'s.
 *
 * @param initialState State the modal starts in: [FlowModalState.Hidden],
 *   [FlowModalState.PartiallyOpened] or [FlowModalState.Expanded].
 * Every sheet is as tall as its content, up to [maxHeightFraction] of the window: a short list
 * opens at the height of its rows, a long one stops at the cap and scrolls. A sheet that should be
 * tall whatever it holds says so in its content — a lazy list fills the room it is given, and so
 * does a body with `Modifier.fillMaxHeight()`. Its heights are measured, not configured:
 * - expanded: the content's height, up to the cap;
 * - partial: [partialMultiplier] of the cap, which a sheet has only when [allowPartial] — off by
 *   default — and its content is taller than that. A partial request on a shorter sheet opens it fully, and [state]
 *   says so.
 *
 * @param allowPartial Whether the modal may rest at a partial height when its content is tall
 *   enough to have one. Off by default: a sheet opens at its full height, so a form or a calendar
 *   never opens cut off; turn it on for a long list that may peek first.
 * @param allowExpand Whether the sheet may grow past its partial height. Off with [allowPartial],
 *   the sheet is capped at the partial height and rests there as its only height.
 * @param allowDrag Whether the sheet can be dragged between its heights and dragged away.
 * @param partialMultiplier Partial height as a share of the sheet's cap.
 * @param maxHeightFraction Cap on the sheet's height as a share of the window height, in `0..1`;
 *   never past the status bar.
 * @param autoHeight Ignored: every sheet sizes to its content. Kept for source compatibility.
 * @property dialogProperties Properties of the dialog window the modal is drawn in. Its scrim colour
 *   is replaced by [FlowModalStyle.overlay].
 * @property animationSpec Animation of the sheet's height between states, or `null` for the theme's
 *   [dev.voir.areca.theme.ArecaMotion.slow] step with its standard easing. A controller can be
 *   built outside composition, where the theme cannot be read, so `null` is resolved by
 *   [FlowModal] each time it composes.
 * @throws IllegalArgumentException When [initialState] is [FlowModalState.Opening] or
 *   [FlowModalState.Hiding], which have no height of their own to start from, or when
 *   [maxHeightFraction] is outside `0..1`.
 */
public class FlowModalController(
    initialState: FlowModalState = FlowModalState.Hidden,
    allowPartial: Boolean = false,
    allowExpand: Boolean = true,
    allowDrag: Boolean = true,
    partialMultiplier: Float = 0.5f,
    @FloatRange(from = 0.0, to = 1.0) maxHeightFraction: Float = 0.9f,
    autoHeight: Boolean = false,
    public val dialogProperties: DialogProperties = flowModalDialogProperties(),
    public val animationSpec: AnimationSpec<Float>? = null,
) {
    init {
        requireHeightFraction(maxHeightFraction)
    }

    // The configuration below is observable and settable so [rememberFlowModal] can apply a
    // caller's changes to the live controller. Recreating the controller instead would reset
    // [state], closing an open modal whenever any of these changed.

    /** Whether the modal may rest at a partial height when its content is tall enough to have one. */
    public var allowPartial: Boolean by mutableStateOf(allowPartial)

    /** Whether the sheet may grow past its partial height; off with [allowPartial] caps it there. */
    public var allowExpand: Boolean by mutableStateOf(allowExpand)

    /** Whether the sheet can be dragged between its heights and dragged away. */
    public var allowDrag: Boolean by mutableStateOf(allowDrag)

    /** Partial height as a share of the sheet's cap. */
    public var partialMultiplier: Float by mutableStateOf(partialMultiplier)

    /** Backing state of [maxHeightFraction]. */
    private var maxHeightFractionState: Float by mutableStateOf(maxHeightFraction)

    /**
     * Expanded height as a share of the window height, `0..1`.
     *
     * @throws IllegalArgumentException When set outside `0..1`.
     */
    @setparam:FloatRange(from = 0.0, to = 1.0)
    public var maxHeightFraction: Float
        get() = maxHeightFractionState
        set(value) {
            requireHeightFraction(value)
            maxHeightFractionState = value
        }

    /** Ignored: every sheet sizes to its content, up to [maxHeightFraction] of the window. */
    @Deprecated(
        message = "Every FlowModal sizes to its content up to maxHeightFraction. A sheet that should " +
            "be tall whatever it holds fills its body: a lazy list, or Modifier.fillMaxHeight().",
    )
    public var autoHeight: Boolean by mutableStateOf(autoHeight)

    /** Current state observed by Compose (drives recomposition). */
    public var state: FlowModalState by
        mutableStateOf(FlowModalStateMachine.resting(initialState).state)
        private set

    /**
     * Resting state the modal is animating toward while [state] is transitional, and [state]
     * itself once it rests. Observable, so retargeting a running animation restarts it.
     */
    public var targetState: FlowModalState by mutableStateOf(initialState)
        private set

    /** Current state and target as one value, for [FlowModalStateMachine]. */
    internal val phase: FlowModalPhase
        get() = FlowModalPhase(state = state, targetState = targetState)

    /**
     * Opens a hidden or hiding modal. Does nothing while it is open or opening.
     *
     * @param partial Whether to open at the partial height, when the modal has one.
     */
    public fun show(partial: Boolean = true) {
        moveTo(FlowModalStateMachine.show(phase, partial, allowPartial))
    }

    /** Moves the modal to its full height, opening it if it is hidden. */
    public fun expand() {
        moveTo(FlowModalStateMachine.expand(phase))
    }

    /**
     * Moves the modal to its partial height, opening it if it is hidden. A sheet whose content is
     * no taller than the partial height goes to its full height instead.
     */
    public fun collapse() {
        moveTo(FlowModalStateMachine.collapse(phase, allowPartial))
    }

    /** Starts hiding a visible modal. Does nothing while it is hidden or hiding. */
    public fun hide() {
        moveTo(FlowModalStateMachine.hide(phase))
    }

    /** Whether the modal is on screen: `true` in every state but [FlowModalState.Hidden]. */
    public val isVisible: Boolean
        get() = state != FlowModalState.Hidden

    /**
     * Applies the end of a drag that released the sheet toward [anchor].
     *
     * @param anchor Resting state the drag settled on.
     */
    internal fun settle(anchor: FlowModalState) {
        moveTo(FlowModalStateMachine.settle(phase, anchor))
    }

    /**
     * Moves a partial request on to the full height, for a sheet that has measured its content and
     * found it no taller than the partial height.
     */
    internal fun dropPartial() {
        moveTo(FlowModalStateMachine.withoutPartial(phase))
    }

    /**
     * Records that the animation started for [started] ran to its end.
     *
     * Ignored when the phase has moved on since, so a late completion cannot overwrite a newer
     * request such as a [hide] made during the animation's last frame.
     *
     * @param started Phase the finished animation was started for.
     */
    internal fun finish(started: FlowModalPhase) {
        if (phase == started) moveTo(FlowModalStateMachine.completed(started))
    }

    /**
     * Makes [next] the current phase.
     *
     * @param next Phase to move to.
     */
    private fun moveTo(next: FlowModalPhase) {
        targetState = next.targetState
        state = next.state
    }
}

/**
 * Checks a flow modal's expanded height fraction.
 *
 * @param fraction Expanded height as a share of the window height.
 * @throws IllegalArgumentException When [fraction] is outside `0..1`.
 */
private fun requireHeightFraction(fraction: Float) {
    require(fraction in 0f..1f) { "maxHeightFraction must be in 0..1, was $fraction" }
}

/**
 * Remembers a [FlowModalController] and keeps its configuration in step with the arguments.
 *
 * The controller instance survives recomposition, so the modal's open state is preserved, while the
 * behavioural flags are re-applied each time. [dialogProperties] and [animationSpec] are captured
 * once, when the controller is created: a caller's instances are typically allocated per call, so
 * tracking them would recompose forever, and the controller holds them as read-only values. A flow
 * that needs them to vary builds a [FlowModalController] itself, remembered under a key that
 * changes with them; the new controller starts in its initial state.
 *
 * @param allowPartial Whether the modal may rest at a partial height when its content is tall
 *   enough to have one. Off by default, so every sheet opens at its full height.
 * @param allowExpand Whether the sheet may grow past its partial height; off with [allowPartial]
 *   caps it there.
 * @param allowDrag Whether the sheet can be dragged between its heights and dragged away.
 * @param partialMultiplier Partial height as a share of the sheet's cap.
 * @param maxHeightFraction Cap on the sheet's height as a share of the window height.
 * @param autoHeight Ignored: every sheet sizes to its content. Kept for source compatibility.
 * @param dialogProperties Properties of the dialog window, read once. Build them with
 *   [flowModalDialogProperties] so the window keeps drawing edge to edge.
 * @param animationSpec Animation of the sheet's height between states, read once, or `null` for
 *   the theme's motion, resolved each time the modal composes.
 * @return The remembered controller.
 * @throws IllegalArgumentException When [maxHeightFraction] is outside `0..1`.
 */
@Composable
public fun rememberFlowModal(
    allowPartial: Boolean = false,
    allowExpand: Boolean = true,
    allowDrag: Boolean = true,
    partialMultiplier: Float = 0.5f,
    @FloatRange(from = 0.0, to = 1.0) maxHeightFraction: Float = 0.9f,
    autoHeight: Boolean = false,
    dialogProperties: DialogProperties = flowModalDialogProperties(),
    animationSpec: AnimationSpec<Float>? = null,
): FlowModalController {
    val controller = remember {
        FlowModalController(
            allowPartial = allowPartial,
            allowExpand = allowExpand,
            allowDrag = allowDrag,
            partialMultiplier = partialMultiplier,
            maxHeightFraction = maxHeightFraction,
            dialogProperties = dialogProperties,
            animationSpec = animationSpec
        )
    }

    // Re-apply configuration on every composition. Without this the controller would keep whatever
    // it was first given and silently ignore later changes.
    SideEffect {
        controller.allowPartial = allowPartial
        controller.allowExpand = allowExpand
        controller.allowDrag = allowDrag
        controller.partialMultiplier = partialMultiplier
        controller.maxHeightFraction = maxHeightFraction
    }

    return controller
}

/**
 * Flow-style bottom modal:
 * - slides in from the bottom (implemented by animating height while anchored to bottom)
 * - supports partial and expanded heights
 * - optional drag to expand/collapse/dismiss
 *
 * The dialog window draws edge to edge (see [flowModalDialogProperties]), so the scrim covers the
 * whole screen and the sheet's background runs down behind the navigation bar. The sheet pads its
 * content by the bottom and side safe-drawing insets, and never grows past the top one.
 *
 * There is one scrim, in [FlowModalStyle.overlay]. On iOS and the desktop the platform's dialog
 * window draws it and fades it in and out with the window. On Android, whose window dim cannot take
 * a colour, the modal clears that dim and draws the scrim itself, deepening as the sheet rises and
 * lifting as it is dragged away.
 *
 * The sheet's animated height is read only while laying out and drawing, so the slide and the drag
 * do not recompose the modal frame by frame.
 *
 * The sheet is as tall as its content, up to the controller's cap; the body scrolls past it while
 * the header and the footer stay. Its content is composed once and measured before the sheet moves,
 * so it opens straight to its height. Opening, hiding and a drag toward dismissal slide the sheet
 * as a whole; between its partial and full heights the body is revealed under a header and a footer
 * that stay put. A change of content — a search that narrows a list — moves the sheet to its new
 * height. The keyboard counts against the cap: the sheet's content stays above it.
 *
 * @param controller State of the modal and its configuration.
 * @param modifier Modifier applied to the sheet.
 * @param style Colors and shape of the scrim, sheet and drag handle.
 * @param size Geometry of the drag handle.
 * @param onDismissRequest Called when the person asks to dismiss the modal — back, the scrim, or a
 *   drag down past the dismiss threshold. The modal hides only if this hides [controller]; a drag
 *   that is refused settles the sheet back to where it rested.
 * @param overlayContentDescription What pressing the scrim does, read by assistive technology.
 * @param header Content above the body. It starts at the sheet's top edge, under the drag handle,
 *   so its background runs behind the handle; it clears the handle with [FlowModalHeaderInsets].
 * @param footer Content below the body.
 * @param content Body of the sheet, below the drag handle when there is no header.
 */
@Composable
public fun FlowModal(
    controller: FlowModalController,
    modifier: Modifier = Modifier,
    style: FlowModalStyle = LocalFlowModalStyle.current ?: FlowModalStyle.Default,
    size: FlowModalSize = LocalFlowModalSize.current ?: FlowModalSize.Default,
    onDismissRequest: () -> Unit = { controller.hide() },
    overlayContentDescription: String = ArecaTheme.labels.dismiss,
    header: (@Composable () -> Unit)? = null,
    footer: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    // Don't compose the dialog when fully hidden.
    if (!controller.isVisible) return

    val motion = ArecaTheme.motion
    val animationSpec = controller.animationSpec ?: motion.slow.tween(motion.standard)

    ComposeDialog(
        onDismissRequest = onDismissRequest,
        properties = remember(controller.dialogProperties, style.overlay) {
            controller.dialogProperties.withScrim(style.overlay)
        },
    ) {
        ClearPlatformDim()

        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val density = LocalDensity.current
            val scope = rememberCoroutineScope()

            val screenHeightPx = with(density) { maxHeight.toPx() }
            // The window runs behind the status bar, so keep the sheet's top edge below it.
            val topInsetPx = WindowInsets.safeDrawing.getTop(density).toFloat()
            val maxSheetHeightPx = min(
                screenHeightPx * controller.maxHeightFraction,
                screenHeightPx - topInsetPx,
            ).coerceAtLeast(0f)
            val partialHeightPx = maxSheetHeightPx * controller.partialMultiplier
            // A sheet that may not grow past its partial height is capped there.
            val capPx = if (controller.allowPartial && !controller.allowExpand) partialHeightPx else maxSheetHeightPx

            // The content's own height, up to the cap, measured by [SheetContent] on every layout;
            // 0 until it has been measured once.
            var contentHeightPx by remember { mutableFloatStateOf(0f) }
            val measured = contentHeightPx > 0f
            val expandedHeightPx = contentHeightPx.coerceAtMost(capPx)
            val hasPartial = controller.allowPartial && controller.allowExpand && expandedHeightPx > partialHeightPx
            // The lowest height the sheet rests at: below it, the sheet slides rather than shrinks.
            val restHeightPx = if (hasPartial) partialHeightPx else expandedHeightPx

            // The heights as they are now, read by a running animation so that content which grows
            // or shrinks while the sheet moves redirects the motion instead of restarting it.
            val currentRestHeightPx by rememberUpdatedState(restHeightPx)
            val currentExpandedHeightPx by rememberUpdatedState(expandedHeightPx)

            /**
             * Height the sheet rests at, or animates toward, in [phase], at the current sizes.
             * Reads state, so a [snapshotFlow] of it follows the content.
             *
             * @param phase Current state and target of the modal.
             * @return Height in pixels.
             */
            fun heightOf(phase: FlowModalPhase): Float = FlowModalStateMachine.heightOf(
                phase = phase,
                partialHeight = currentRestHeightPx,
                expandedHeight = currentExpandedHeightPx,
            )

            // Carries the sheet to a height that changed while it moved or rested, from the speed it
            // has: content that arrives while the sheet opens (data replacing a placeholder) bends
            // one motion toward the new height rather than stopping and starting again.
            val followSpec = remember(motion) { motion.slide<Float>() }

            /**
             * How much of the sheet shows above the bottom edge: the single source of truth for its
             * position, animated between states and moved by the finger.
             */
            val sheetHeightPx = remember { Animatable(0f) }

            /**
             * While the user is dragging, we stop state-driven animations to avoid
             * fighting gestures. On drag end we settle to the nearest anchor.
             */
            var isDragging by remember { mutableStateOf(false) }

            // A sheet too short for its partial height opens, and rests, at its full height, and its
            // state says so.
            LaunchedEffect(measured, hasPartial, controller.state, controller.targetState) {
                if (measured && !hasPartial) controller.dropPartial()
            }

            // Drive the position from the controller. A drag cancels the running animation, so
            // `isDragging` is a key: releasing the sheet restarts the effect, which then animates from
            // wherever the finger left it even when the phase is unchanged. Nothing opens before the
            // content has been measured, so the sheet rises straight to its height. The heights are
            // not keys: a change of size redirects the running motion (see [followHeight]).
            LaunchedEffect(
                controller.state,
                controller.targetState,
                isDragging,
                measured,
            ) {
                if (isDragging) return@LaunchedEffect

                val started = controller.phase
                when (started.state) {
                    FlowModalState.Opening -> {
                        if (!measured) return@LaunchedEffect
                        sheetHeightPx.followHeight(
                            target = { heightOf(started) },
                            firstSpec = animationSpec,
                            followSpec = followSpec,
                            untilSettled = true,
                        )
                        controller.finish(started)
                    }

                    FlowModalState.Hiding -> {
                        sheetHeightPx.animateTo(targetValue = 0f, animationSpec = animationSpec)
                        controller.finish(started)
                    }

                    FlowModalState.PartiallyOpened,
                    FlowModalState.Expanded -> {
                        // The content changed (a search narrowed the list, data replaced a
                        // placeholder), or the window did: move to the new height.
                        if (!measured) return@LaunchedEffect
                        sheetHeightPx.followHeight(
                            target = { heightOf(started) },
                            firstSpec = followSpec,
                            followSpec = followSpec,
                            untilSettled = false,
                        )
                    }

                    FlowModalState.Hidden -> sheetHeightPx.snapTo(0f)
                }
            }

            // Scrim — full screen, dismisses on tap. Painted here only where the window cannot paint
            // it; its intensity follows the sheet's openness, read while drawing.
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .thenIf(!platformDrawsModalScrim) {
                        drawBehind {
                            val openness = (sheetHeightPx.value / max(1f, restHeightPx))
                                .coerceIn(0f, 1f)
                            drawRect(style.overlay.copy(alpha = style.overlay.alpha * openness))
                        }
                    }
                    // A semantics action rather than `clickable`: the scrim must be dismissable by
                    // touch without becoming an unlabelled full-screen button that a screen reader
                    // reads out and the keyboard focuses before the sheet's own content.
                    .pointerInput(onDismissRequest) {
                        detectTapGestures { onDismissRequest() }
                    }
                    .semantics {
                        contentDescription = overlayContentDescription
                        onClick(label = overlayContentDescription) {
                            onDismissRequest()
                            true
                        }
                    }
            )

            // Sheet — bottom-anchored, composed once.
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                val capInt = capPx.roundToInt()
                Box(
                    // The cap comes first so that nothing the caller passes can widen the sheet
                    // past it; the bottom-centre alignment above centres a capped sheet.
                    modifier = Modifier
                        .then(
                            if (size.maxWidth.isSpecified) {
                                Modifier.widthIn(max = size.maxWidth)
                            } else {
                                Modifier
                            },
                        )
                        .then(modifier)
                        .fillMaxWidth()
                        // The sheet takes as much of the window as shows: content past it is clipped
                        // from drawing and, through the reported bounds, from touch.
                        .layout { measurable, constraints ->
                            val placeable = measurable.measure(
                                constraints.copy(minHeight = 0, maxHeight = min(capInt, constraints.maxHeight)),
                            )
                            val shown = sheetHeightPx.value.roundToInt().coerceIn(0, placeable.height)
                            layout(placeable.width, shown) { placeable.place(0, 0) }
                        }
                        .clip(style.shape)
                        .background(style.bg, style.shape)
                        // Swallows presses so they never reach the scrim behind. A `clickable` here
                        // would do the same but would also announce the whole sheet as one button
                        // and put it in the focus order.
                        .pointerInput(Unit) { detectTapGestures { } }
                        .then(
                            if (!controller.allowDrag) {
                                Modifier
                            } else {
                                Modifier.flowModalDrag(
                                    scope = scope,
                                    sheetHeightPx = sheetHeightPx,
                                    expandedHeightPx = expandedHeightPx,
                                    partialHeightPx = restHeightPx,
                                    hasPartial = hasPartial,
                                    onDragStarted = { isDragging = true },
                                    onDragSettled = { anchor ->
                                        val hiding = controller.state == FlowModalState.Hiding
                                        if (anchor == FlowModalState.Hidden && !hiding) {
                                            // Dismissal is the caller's to grant. Head back to
                                            // where the sheet rested first, so a refusal settles
                                            // it there and a grant hides it from the finger.
                                            controller.settle(
                                                FlowModalStateMachine.anchorOf(controller.phase),
                                            )
                                            onDismissRequest()
                                        } else {
                                            controller.settle(anchor)
                                        }
                                        isDragging = false
                                    },
                                    onDragCancelled = {
                                        controller.settle(
                                            FlowModalStateMachine.anchorOf(controller.phase),
                                        )
                                        isDragging = false
                                    },
                                )
                            }
                        )
                        // Last in the chain, so the background and the drag area reach the screen
                        // edges while the content clears the cutouts at the sides; the bottom inset
                        // — the navigation bar and the keyboard — is the content's own.
                        .windowInsetsPadding(SheetSideInsets)
                ) {
                    SheetContent(
                        controller = controller,
                        style = style,
                        size = size,
                        layoutHeight = { max(sheetHeightPx.value, restHeightPx).roundToInt() },
                        onContentHeight = { height -> contentHeightPx = height.toFloat() },
                        header = header,
                        footer = footer,
                        content = content,
                    )
                }
            }
        }
    }
}

/**
 * Animates toward [target] and keeps following it: each new value redirects the running animation
 * from its current position and speed, so the motion bends instead of stopping and starting again.
 *
 * @param target Height to reach; reads state, and is followed through a [snapshotFlow].
 * @param firstSpec Animation toward the first value.
 * @param followSpec Animation toward every later value; a spring, so it carries the speed over.
 * @param untilSettled Whether to return once the animation toward the latest value has ended, or to
 *   follow until cancelled.
 */
private suspend fun Animatable<Float, AnimationVector1D>.followHeight(
    target: () -> Float,
    firstSpec: AnimationSpec<Float>,
    followSpec: AnimationSpec<Float>,
    untilSettled: Boolean,
) {
    val settled = CompletableDeferred<Unit>()
    coroutineScope {
        var spec = firstSpec
        val follower = launch {
            snapshotFlow(target).collect { height ->
                if (abs(value - height) <= 1f && !isRunning) {
                    settled.complete(Unit)
                    return@collect
                }
                val animationSpec = spec
                spec = followSpec
                // Undispatched, so the speed is read and the running animation is replaced at once;
                // a replaced animation ends with a cancellation and settles nothing.
                launch(start = CoroutineStart.UNDISPATCHED) {
                    animateTo(targetValue = height, animationSpec = animationSpec, initialVelocity = velocity)
                    settled.complete(Unit)
                }
            }
        }
        if (untilSettled) {
            settled.await()
            follower.cancel()
        }
    }
}

/** Insets the sheet's content clears at its sides: cutouts and system bars. */
private val SheetSideInsets: WindowInsets
    @Composable get() = WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal)

/** Insets the sheet's content clears at its bottom: the navigation bar, or the keyboard when open. */
private val SheetBottomInsets: WindowInsets
    @Composable get() = WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom)

/** Default [FlowModalSize] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalFlowModalSize: ProvidableCompositionLocal<FlowModalSize?> =
    compositionLocalOf { null }

/** Default [FlowModalStyle] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalFlowModalStyle: ProvidableCompositionLocal<FlowModalStyle?> =
    compositionLocalOf { null }

/**
 * The sheet's content: the header at its top, the footer above the navigation bar or the keyboard,
 * and the body between them, with the drag handle floating over the top.
 *
 * Measured once per layout, against the cap the sheet passes down: the header and the footer at
 * their own height, the body with what is left, so a long body stops at the cap and scrolls. The
 * resulting height — the content's own — goes to [onContentHeight]. The content is then laid out at
 * [layoutHeight], never taller than its own height: below that, the footer stays at the visible
 * bottom and the body is cut off above it, which is how a sheet shows between its partial and full
 * heights.
 *
 * @param controller State and configuration of the modal.
 * @param style Style passed to [FlowModal]; supplies the drag handle's color.
 * @param size Size passed to [FlowModal]; supplies the drag handle's geometry.
 * @param layoutHeight The height to lay the content out at, read during layout.
 * @param onContentHeight Called during layout with the content's own height, in pixels.
 * @param header Content above the body.
 * @param footer Content below the body.
 * @param content Body of the sheet.
 */
@Composable
private fun SheetContent(
    controller: FlowModalController,
    style: FlowModalStyle,
    size: FlowModalSize,
    layoutHeight: () -> Int,
    onContentHeight: (Int) -> Unit,
    header: (@Composable () -> Unit)?,
    footer: (@Composable () -> Unit)?,
    content: @Composable () -> Unit,
) {
    val dragAreaHeight = if (controller.allowDrag) size.dragAreaHeight else 0.dp
    val headerInsets = WindowInsets(top = dragAreaHeight)
    val bottomInset = SheetBottomInsets.getBottom(LocalDensity.current)

    Box(modifier = Modifier.fillMaxWidth()) {
        SurfaceOf(style.bg, root = SurfaceRoot.Sheet) {
            Layout(
                contents = listOf(
                    {
                        // A header runs up to the sheet's top edge so its fill shows under the drag
                        // handle, and clears the handle itself with [FlowModalHeaderInsets]. A body
                        // has no fill to show, so it starts below the handle.
                        if (header != null) {
                            CompositionLocalProvider(LocalFlowModalHeaderInsets provides headerInsets) {
                                header()
                            }
                        } else {
                            Spacer(Modifier.height(dragAreaHeight))
                        }
                    },
                    // The sheet clears the bottom inset — the navigation bar, or the keyboard — itself,
                    // below the footer; the body and the footer see it as used, so a padding of
                    // their own for it adds nothing.
                    {
                        Column(modifier = Modifier.fillMaxWidth().consumeWindowInsets(SheetBottomInsets)) {
                            content()
                        }
                    },
                    {
                        if (footer != null) {
                            Box(modifier = Modifier.consumeWindowInsets(SheetBottomInsets)) { footer() }
                        }
                    },
                ),
                modifier = Modifier.fillMaxWidth(),
            ) { (headers, bodies, footers), constraints ->
                val width = constraints.maxWidth
                val loose = Constraints(minWidth = width, maxWidth = width, maxHeight = constraints.maxHeight)
                val top = headers.map { it.measure(loose) }
                val bottom = footers.map { it.measure(loose) }
                val topHeight = top.sumOf { it.height }
                val bottomHeight = bottom.sumOf { it.height }
                val bodyRoom = (constraints.maxHeight - topHeight - bottomHeight - bottomInset).coerceAtLeast(0)
                val body = bodies.map { it.measure(loose.copy(maxHeight = bodyRoom)) }
                val bodyHeight = body.maxOfOrNull { it.height } ?: 0

                val own = (topHeight + bodyHeight + bottomHeight + bottomInset).coerceAtMost(constraints.maxHeight)
                onContentHeight(own)
                val height = layoutHeight().coerceIn(topHeight + bottomHeight + bottomInset, own)

                layout(width, height) {
                    var y = 0
                    top.forEach { placeable ->
                        placeable.place(0, y)
                        y += placeable.height
                    }
                    val footerTop = height - bottomInset - bottomHeight
                    val bodyShown = (footerTop - topHeight).coerceAtLeast(0)
                    body.forEach { placeable ->
                        if (bodyShown >= placeable.height) {
                            placeable.place(0, topHeight)
                        } else {
                            // Cut off above the footer, for drawing and for touch alike.
                            placeable.placeWithLayer(0, topHeight) {
                                clip = true
                                shape = TopPartShape(bodyShown.toFloat())
                            }
                        }
                    }
                    var footerY = footerTop
                    bottom.forEach { placeable ->
                        placeable.place(0, footerY)
                        footerY += placeable.height
                    }
                }
            }
        }

        // Drawn after the content, so it floats over whatever the top of the sheet shows.
        if (controller.allowDrag) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(size.dragPadding)
                    .size(width = size.dragWidth, height = size.dragHeight)
                    .clip(ArecaTheme.shapes.full)
                    .background(style.drag)
                    // The grabber says "drag me" by being there; a screen reader user drags the
                    // sheet with the dismiss action on the scrim instead.
                    .clearAndSetSemantics { },
            )
        }
    }
}

/**
 * The top [height] pixels of whatever it clips: the part of a sheet's body that shows above its
 * footer.
 *
 * @property height Height of the part that shows, in pixels.
 */
private class TopPartShape(private val height: Float) : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline =
        Outline.Rectangle(Rect(0f, 0f, size.width, height.coerceIn(0f, size.height)))
}

/**
 * Space a [FlowModal]'s drag handle takes at the top of the sheet: the handle and the padding
 * around it.
 */
private val FlowModalSize.dragAreaHeight: Dp
    get() = dragPadding.calculateTopPadding() + dragHeight + dragPadding.calculateBottomPadding()

/** Insets that keep a [FlowModal] header clear of the drag handle; none outside a header. */
private val LocalFlowModalHeaderInsets: ProvidableCompositionLocal<WindowInsets> =
    compositionLocalOf { WindowInsets(0, 0, 0, 0) }

/**
 * Insets a [FlowModal] header pads its content by to clear the drag handle floating over it.
 *
 * The header runs up to the sheet's top edge, so its background shows behind the handle. A header
 * with a fill passes these to it, as `TopBar(windowInsets = FlowModalHeaderInsets)`, so its fill
 * runs under the handle while its title sits below it. They are zero when the sheet cannot be
 * dragged and outside a [FlowModal] header.
 */
public val FlowModalHeaderInsets: WindowInsets
    @Composable get() = LocalFlowModalHeaderInsets.current

/**
 * Drag modifier for [FlowModal].
 *
 * Moves [sheetHeightPx] with the finger, and on release picks the height the sheet settles on with
 * [FlowModalStateMachine.dragTarget], handing the state change back through the lambdas.
 *
 * @param scope Scope the finger-following updates run in.
 * @param sheetHeightPx How much of the sheet shows, in pixels.
 * @param expandedHeightPx Full height of the sheet, in pixels; the highest it can be dragged.
 * @param partialHeightPx Partial height of the sheet, or its full height when it has none, in pixels.
 * @param hasPartial Whether the sheet can rest at [partialHeightPx] as well as at [expandedHeightPx].
 * @param onDragStarted Called when the finger starts dragging the sheet.
 * @param onDragSettled Called with the resting state the released sheet settles on.
 * @param onDragCancelled Called when the drag is cancelled before the finger lifts.
 * @return Modifier that drags the sheet.
 */
private fun Modifier.flowModalDrag(
    scope: CoroutineScope,
    sheetHeightPx: Animatable<Float, *>,
    expandedHeightPx: Float,
    partialHeightPx: Float,
    hasPartial: Boolean,
    onDragStarted: () -> Unit,
    onDragSettled: (FlowModalState) -> Unit,
    onDragCancelled: () -> Unit,
): Modifier = pointerInput(expandedHeightPx, partialHeightPx, hasPartial) {
    detectVerticalDragGestures(
        onDragStart = { onDragStarted() },
        onVerticalDrag = { change, dragAmount ->
            change.consume()

            // dragAmount: +down, -up
            val newHeight = (sheetHeightPx.value - dragAmount).coerceIn(0f, expandedHeightPx)
            // Undispatched, so the snap lands before the release is handled; a snap that ran
            // after it would cancel the settling animation the release starts.
            scope.launch(start = CoroutineStart.UNDISPATCHED) {
                sheetHeightPx.snapTo(newHeight)
            }
        },
        onDragEnd = {
            onDragSettled(
                FlowModalStateMachine.dragTarget(
                    height = sheetHeightPx.value,
                    partialHeight = partialHeightPx,
                    expandedHeight = expandedHeightPx,
                    hasPartial = hasPartial,
                ),
            )
        },
        onDragCancel = { onDragCancelled() },
    )
}
