package dev.voir.areca.foundation.overlay

import androidx.annotation.FloatRange
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
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
import androidx.compose.ui.window.DialogProperties
import dev.voir.areca.modifier.thenIf
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.tween
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt
import androidx.compose.ui.window.Dialog as ComposeDialog

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
         * @param bg Fill of the sheet.
         * @param drag Color of the drag handle.
         * @param shape Shape of the sheet.
         * @return Style built from the supplied colors and shape.
         */
        @Composable
        public fun default(
            overlay: Color = ArecaTheme.colors.bgOverlayDark,
            bg: Color = ArecaTheme.colors.bgCanvas,
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
 * Geometry of a [FlowModal]'s drag handle.
 *
 * The sheet's own heights are behaviour rather than geometry, and live on [FlowModalController].
 *
 * @property dragPadding Space around the drag handle, inside the top of the sheet. The handle floats
 *   over the header, which clears this space with [FlowModalHeaderInsets].
 * @property dragWidth Width of the drag handle.
 * @property dragHeight Thickness of the drag handle.
 */
@Immutable
public data class FlowModalSize(
    public val dragPadding: PaddingValues,
    public val dragWidth: Dp,
    public val dragHeight: Dp
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
         * @return Size built from the supplied metrics.
         */
        @Composable
        public fun default(
            dragPadding: PaddingValues = PaddingValues(
                top = ArecaTheme.spacing.sm,
                bottom = ArecaTheme.spacing.xs
            ),
            dragWidth: Dp = 44.dp,
            dragHeight: Dp = 4.dp
        ): FlowModalSize = FlowModalSize(
            dragPadding = dragPadding,
            dragWidth = dragWidth,
            dragHeight = dragHeight
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
 * @param allowPartial Whether the modal has a partial height.
 * @param allowExpand Whether the modal has an expanded height.
 * @param allowDrag Whether the sheet can be dragged between its heights and dragged away.
 * @param partialMultiplier Partial height as a share of the expanded height.
 * @param maxHeightFraction Expanded height as a share of the window height, in `0..1`.
 * @param autoHeight Whether the sheet sizes to its content instead of a fixed height.
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
    allowPartial: Boolean = true,
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

    /** Whether the modal has a partial height. */
    public var allowPartial: Boolean by mutableStateOf(allowPartial)

    /** Whether the modal has an expanded height. */
    public var allowExpand: Boolean by mutableStateOf(allowExpand)

    /** Whether the sheet can be dragged between its heights and dragged away. */
    public var allowDrag: Boolean by mutableStateOf(allowDrag)

    /** Partial height as a share of the expanded height. */
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

    /** Whether the sheet sizes to its content instead of a fixed height. */
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
        moveTo(FlowModalStateMachine.show(phase, partial, allowPartial, autoHeight))
    }

    /** Moves the modal to its expanded height, opening it if it is hidden. */
    public fun expand() {
        moveTo(FlowModalStateMachine.expand(phase, allowExpand, autoHeight))
    }

    /** Moves the modal to its partial height, opening it if it is hidden. */
    public fun collapse() {
        moveTo(FlowModalStateMachine.collapse(phase, allowPartial, autoHeight))
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
 * @param allowPartial Whether the modal has a partial height.
 * @param allowExpand Whether the modal has an expanded height.
 * @param allowDrag Whether the sheet can be dragged between its heights and dragged away.
 * @param partialMultiplier Partial height as a share of the expanded height.
 * @param maxHeightFraction Expanded height as a share of the window height.
 * @param autoHeight Whether the sheet sizes to its content instead of a fixed height.
 * @param dialogProperties Properties of the dialog window, read once. Build them with
 *   [flowModalDialogProperties] so the window keeps drawing edge to edge.
 * @param animationSpec Animation of the sheet's height between states, read once, or `null` for
 *   the theme's motion, resolved each time the modal composes.
 * @return The remembered controller.
 * @throws IllegalArgumentException When [maxHeightFraction] is outside `0..1`.
 */
@Composable
public fun rememberFlowModal(
    allowPartial: Boolean = true,
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
            autoHeight = autoHeight,
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
        controller.autoHeight = autoHeight
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
 * In [FlowModalController.autoHeight] mode the content is composed and measured exactly once.
 * The natural content height is captured via a [Modifier.layout] interceptor and used as the
 * animation target, while the layout's reported size is constrained to the current animated
 * value — this means content beyond the visible portion is properly clipped from both
 * rendering and pointer input (so partially-revealed items can't be clicked).
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
    overlayContentDescription: String = "Dismiss",
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
            val maxSheetHeightDp = with(density) { maxSheetHeightPx.toDp() }

            /**
             * Natural measured height of the sheet content (drag handle + header + body + footer),
             * captured by the [Modifier.layout] interceptor below. Used as the animation target
             * in autoHeight mode. In fixed-height mode this stays at 0 and is unused.
             */
            var measuredContentHeightPx by remember { mutableStateOf(0f) }

            val expandedHeightPx = if (controller.autoHeight) {
                measuredContentHeightPx.coerceIn(0f, maxSheetHeightPx)
            } else {
                maxSheetHeightPx
            }

            val partialHeightPx =
                (expandedHeightPx * controller.partialMultiplier).coerceIn(0f, expandedHeightPx)

            /**
             * Height the sheet rests at, or animates toward, in [phase], at the current sizes.
             *
             * @param phase Current state and target of the modal.
             * @return Height in pixels.
             */
            fun heightOf(phase: FlowModalPhase): Float = FlowModalStateMachine.heightOf(
                phase = phase,
                partialHeight = partialHeightPx,
                expandedHeight = expandedHeightPx,
                autoHeight = controller.autoHeight,
            )

            /**
             * Single source of truth for the sheet's currently-visible height.
             * Because the sheet is bottom-aligned, animating this value creates the
             * natural "slide up/down" effect.
             */
            val sheetHeightPx = remember { Animatable(0f) }

            /**
             * While the user is dragging, we stop state-driven animations to avoid
             * fighting gestures. On drag end we settle to the nearest anchor.
             */
            var isDragging by remember { mutableStateOf(false) }

            // Drive height animation from controller state machine. A drag cancels the running
            // animation, so `isDragging` is a key: releasing the sheet restarts the effect, which
            // then animates from wherever the finger left it even when the phase is unchanged.
            LaunchedEffect(
                controller.state,
                controller.targetState,
                isDragging,
                maxSheetHeightPx,
                partialHeightPx,
                expandedHeightPx
            ) {
                if (isDragging) return@LaunchedEffect

                val started = controller.phase
                when (started.state) {
                    FlowModalState.Opening -> {
                        val target = heightOf(started)
                        // In autoHeight mode wait for the first measurement before animating in.
                        if (target > 0f || !controller.autoHeight) {
                            sheetHeightPx.animateTo(
                                targetValue = target,
                                animationSpec = animationSpec
                            )

                            controller.finish(started)
                        }
                    }

                    FlowModalState.Hiding -> {
                        sheetHeightPx.animateTo(
                            targetValue = 0f,
                            animationSpec = animationSpec
                        )
                        controller.finish(started)
                    }

                    FlowModalState.PartiallyOpened,
                    FlowModalState.Expanded -> {
                        // If constraints changed (rotation/resize/content reflow),
                        // keep the visual height consistent.
                        val target = heightOf(started)
                        if (abs(sheetHeightPx.value - target) > 1f) sheetHeightPx.snapTo(target)
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
                            val openness = (sheetHeightPx.value / max(1f, expandedHeightPx))
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

            // Sheet — bottom-anchored. Composed exactly once regardless of mode.
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                // Build the size/layout strategy:
                //  - fixed-height mode  -> lay out at the animated height
                //  - auto-height mode   -> measure children naturally up to max,
                //                          report the *animated* height as our size,
                //                          and remember the natural height for animation
                val sizingModifier = if (controller.autoHeight) {
                    Modifier
                        .heightIn(max = maxSheetHeightDp)
                        .layout { measurable, constraints ->
                            // Measure the content at its natural height (capped by heightIn above).
                            val placeable = measurable.measure(
                                constraints.copy(minHeight = 0)
                            )
                            val natural = placeable.height

                            // Publish the natural height so the controller can target it.
                            // Only write if it actually changed to avoid extra invalidations.
                            if (measuredContentHeightPx != natural.toFloat()) {
                                measuredContentHeightPx = natural.toFloat()
                            }

                            // Report our size as the animated visible height. Anything beyond
                            // this is clipped (visually by .clip below, and from hit testing
                            // because Compose hit-tests against reported layout bounds).
                            val visible = sheetHeightPx.value
                                .roundToInt()
                                .coerceIn(0, natural)

                            layout(placeable.width, visible) {
                                placeable.place(0, 0)
                            }
                        }
                } else {
                    Modifier.layout { measurable, constraints ->
                        val height = constraints.constrainHeight(sheetHeightPx.value.roundToInt())
                        val placeable = measurable.measure(
                            constraints.copy(minHeight = height, maxHeight = height)
                        )
                        layout(placeable.width, height) {
                            placeable.place(0, 0)
                        }
                    }
                }

                Box(
                    modifier = modifier
                        .fillMaxWidth()
                        .then(sizingModifier)
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
                                    maxSheetHeightPx = expandedHeightPx,
                                    partialHeightPx = partialHeightPx,
                                    allowPartial = controller.allowPartial && !controller.autoHeight,
                                    allowExpand = controller.allowExpand || controller.autoHeight,
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
                        // edge while the content clears the navigation bar, cutouts and keyboard.
                        .windowInsetsPadding(SheetContentInsets)
                ) {
                    SheetContent(
                        controller = controller,
                        style = style,
                        size = size,
                        header = header,
                        footer = footer,
                        content = content,
                    )
                }
            }
        }
    }
}

/** Insets the sheet's content clears: everything on the sides and bottom it is drawn against. */
private val SheetContentInsets: WindowInsets
    @Composable get() = WindowInsets.safeDrawing.only(
        WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom,
    )

/** Default [FlowModalSize] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalFlowModalSize: ProvidableCompositionLocal<FlowModalSize?> =
    compositionLocalOf { null }

/** Default [FlowModalStyle] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalFlowModalStyle: ProvidableCompositionLocal<FlowModalStyle?> =
    compositionLocalOf { null }

/**
 * Single, deduplicated sheet body. Used in both fixed-height and auto-height modes —
 * the parent is responsible for sizing/clipping it.
 *
 * In auto-height mode this Column is allowed to wrap its content (the parent's
 * [Modifier.layout] interceptor measures it once). In fixed-height mode the body
 * uses [ColumnScope.weight] so the content area absorbs the remaining space below
 * the (optional) drag handle / header and above the (optional) footer.
 *
 * @param controller State and configuration of the modal.
 * @param style Style passed to [FlowModal]; supplies the drag handle's color.
 * @param size Size passed to [FlowModal]; supplies the drag handle's geometry.
 * @param header Content above the body.
 * @param footer Content below the body.
 * @param content Body of the sheet.
 */
@Composable
private fun SheetContent(
    controller: FlowModalController,
    style: FlowModalStyle,
    size: FlowModalSize,
    header: (@Composable () -> Unit)?,
    footer: (@Composable () -> Unit)?,
    content: @Composable () -> Unit,
) {
    val dragAreaHeight = if (controller.allowDrag) size.dragAreaHeight else 0.dp
    val headerInsets = WindowInsets(top = dragAreaHeight)

    Box(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // A header runs up to the sheet's top edge so its fill shows under the drag handle, and
            // clears the handle itself with [FlowModalHeaderInsets]. A body has no fill to show,
            // so it starts below the handle.
            if (header != null) {
                CompositionLocalProvider(LocalFlowModalHeaderInsets provides headerInsets) {
                    header()
                }
            } else {
                Spacer(Modifier.height(dragAreaHeight))
            }

            Column(
                modifier = Modifier
                    .weight(1f, fill = !controller.autoHeight)
                    .fillMaxWidth()
            ) {
                content()
            }

            footer?.invoke()
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
 * Responsibilities:
 * - Update [sheetHeightPx] directly during drag for a "finger-following" interaction.
 * - On release, pick the anchor the sheet settles on with [FlowModalStateMachine.dragTarget].
 * - Delegate the actual state transitions back to the caller via lambdas.
 *
 * This keeps the main composable smaller and makes the drag rules easier to evolve.
 *
 * @param scope Scope the finger-following height updates run in.
 * @param sheetHeightPx Animated height of the sheet, in pixels.
 * @param maxSheetHeightPx Expanded height of the sheet, in pixels.
 * @param partialHeightPx Partial height of the sheet, in pixels.
 * @param allowPartial Whether the sheet can rest at [partialHeightPx].
 * @param allowExpand Whether the sheet can rest at [maxSheetHeightPx].
 * @param onDragStarted Called when the finger starts dragging the sheet.
 * @param onDragSettled Called with the resting state the released sheet settles on.
 * @param onDragCancelled Called when the drag is cancelled before the finger lifts.
 * @return Modifier that drags the sheet.
 */
private fun Modifier.flowModalDrag(
    scope: CoroutineScope,
    sheetHeightPx: Animatable<Float, *>,
    maxSheetHeightPx: Float,
    partialHeightPx: Float,
    allowPartial: Boolean,
    allowExpand: Boolean,
    onDragStarted: () -> Unit,
    onDragSettled: (FlowModalState) -> Unit,
    onDragCancelled: () -> Unit,
): Modifier = pointerInput(
    maxSheetHeightPx,
    partialHeightPx,
    allowPartial,
    allowExpand
) {
    detectVerticalDragGestures(
        onDragStart = { onDragStarted() },
        onVerticalDrag = { change, dragAmount ->
            change.consume()

            // dragAmount: +down, -up
            val newHeight = (sheetHeightPx.value - dragAmount).coerceIn(0f, maxSheetHeightPx)
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
                    expandedHeight = maxSheetHeightPx,
                    allowPartial = allowPartial,
                    allowExpand = allowExpand,
                ),
            )
        },
        onDragCancel = { onDragCancelled() },
    )
}
