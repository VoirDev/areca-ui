package dev.voir.areca.keyboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.paneTitle
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.constrainHeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first

/**
 * Hosts a custom keyboard docked to the bottom of the screen.
 *
 * Place one at the root of a screen. It puts a [KeyboardController] in scope for everything below,
 * draws [keyboardContent] over the bottom edge while the keyboard is open, and coordinates the
 * focus and scrolling that make a target usable while the keyboard covers part of the screen.
 *
 * [keyboardContent] receives the open [KeyboardTarget], so one host serves a whole screen: switch on
 * [KeyboardTarget.key] to give an amount field a numeric pad and a date field something else. While
 * the keyboard slides out it still receives the target it was last opened for.
 *
 * A target opened with `scroll = true` is scrolled once the keyboard has finished opening. With
 * [compressContent] the content's viewport has shrunk by then, so the target is brought into the
 * smaller viewport; without it, the keyboard covers the content, so the target is brought into view
 * together with a band the keyboard's height below it. Either way, only a scroll container between
 * the target and the host moves.
 *
 * With [compressContent] the content's bottom inset is also consumed as window insets, so content
 * that pads itself by the navigation bar or the safe area does not add that padding again above a
 * keyboard that already covers it. The inset is applied during layout, so the content is not
 * recomposed on every frame of the keyboard's slide.
 *
 * When the keyboard opens, assistive technology is told a pane titled [paneTitle] appeared.
 *
 * **Back press is deliberately not handled here.** An application already owns its back stack, so
 * wire it where that stack lives — with Compose Multiplatform's navigation events, for example:
 *
 * ```kotlin
 * NavigationBackHandler(
 *     state = rememberNavigationEventState(NavigationEventInfo.None),
 *     isBackEnabled = keyboard.isVisible,
 *     onBackCompleted = { keyboard.close() },
 * )
 * ```
 *
 * @param keyboardContent The keyboard itself, given the target it was opened for.
 * @param modifier Modifier applied to the host container, which fills its parent.
 * @param controller Controller to drive the keyboard. Hoist it to open the keyboard from outside.
 *   Two hosts may share one; the one that entered the composition last moves focus.
 * @param compressContent Whether the content shrinks by the keyboard's height. `true` keeps the
 *   whole screen reachable; `false` draws the keyboard over the content, which is what a screen with
 *   a docked action bar wants — compressing would lift that bar onto the keyboard's shoulder.
 * @param scrim Tinted layer between content and keyboard. `null` draws none, so the screen stays
 *   readable and usable while the keyboard is up.
 * @param animation How the keyboard, scrim, and content inset move.
 * @param paneTitle What assistive technology announces when the keyboard appears, or `null` to
 *   announce nothing. English by default; pass the product's own.
 * @param onVisibilityChange Called when the keyboard opens or closes, and not for its initial state.
 * @param content The screen the keyboard serves.
 */
@Composable
public fun KeyboardHost(
    keyboardContent: @Composable (KeyboardTarget?) -> Unit,
    modifier: Modifier = Modifier,
    controller: KeyboardController = rememberKeyboardController(),
    compressContent: Boolean = true,
    scrim: KeyboardScrim? = null,
    animation: KeyboardAnimation = KeyboardAnimation.Default,
    paneTitle: String? = "Keyboard",
    onVisibilityChange: ((Boolean) -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val density = LocalDensity.current

    var keyboardHeightPx by remember { mutableIntStateOf(0) }
    val keyboardHeight = remember(density, keyboardHeightPx) {
        with(density) { keyboardHeightPx.toDp() }
    }

    DisposableEffect(controller, focusManager) {
        controller.attachFocusManager(focusManager)
        onDispose { controller.detachFocusManager(focusManager) }
    }

    // One transition drives the surface, the scrim, and the inset. Separate animations would run on
    // separate clocks and the content could still be sliding after the keyboard had landed.
    val transition = updateTransition(controller.isVisible, label = "keyboard")

    // Held as a State and read only during layout, so the slide re-lays the content out rather than
    // recomposing it every frame.
    val bottomInset = transition.animateDp(
        transitionSpec = { if (targetState) animation.insetEnter else animation.insetExit },
        label = "keyboardInset",
    ) { visible ->
        if (visible && compressContent) keyboardHeight else 0.dp
    }
    val contentInsets = remember(bottomInset) { KeyboardContentInsets(bottomInset) }

    CompositionLocalProvider(LocalKeyboardController provides controller) {
        Box(modifier = modifier.fillMaxSize()) {
            Box(Modifier.fillMaxSize().padBottom(contentInsets).consumeWindowInsets(contentInsets)) {
                content()
            }

            if (scrim != null) {
                Scrim(
                    transition = transition,
                    scrim = scrim,
                    animation = animation,
                    onDismiss = controller::close,
                )
            }

            transition.AnimatedVisibility(
                visible = { it },
                enter = animation.enter,
                exit = animation.exit,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    // The keyboard must never take focus itself, or the target it types into loses
                    // focus on the first key press.
                    .focusProperties { canFocus = false },
            ) {
                Box(
                    Modifier
                        .onSizeChanged { keyboardHeightPx = it.height }
                        .then(
                            if (paneTitle != null) {
                                Modifier.semantics { this.paneTitle = paneTitle }
                            } else {
                                Modifier
                            },
                        ),
                ) {
                    // `shownTarget` rather than `target`: `close()` forgets the target at once, but
                    // the keyboard is still composed through its exit and must keep its keys.
                    keyboardContent(controller.shownTarget)
                }
            }
        }
    }

    val currentCompressContent by rememberUpdatedState(compressContent)
    LaunchedEffect(controller, transition) {
        controller.bringIntoViewRequests.collectLatest { target ->
            // Wait until the keyboard has finished opening: before then neither its height nor the
            // content inset is final, and a scroll measured early lands behind the keyboard.
            snapshotFlow {
                !controller.isVisible || (transition.currentState && !transition.isRunning)
            }.first { it }
            if (!controller.isVisible) return@collectLatest

            if (currentCompressContent) {
                target.bringIntoViewRequester.bringIntoView()
            } else {
                target.bringIntoViewRequester.bringIntoView(
                    keyboardClearance(targetSize = target.size, keyboardHeightPx = keyboardHeightPx),
                )
            }
        }
    }

    val currentOnVisibilityChange by rememberUpdatedState(onVisibilityChange)
    LaunchedEffect(controller) {
        snapshotFlow { controller.isVisible }
            // The first emission is the state on arrival, not a change.
            .drop(1)
            .collect { visible -> currentOnVisibilityChange?.invoke(visible) }
    }
}

/**
 * The area to bring into view so a target clears a keyboard drawn over the content.
 *
 * The target's own bounds extended downward by the keyboard's height: once a scroll container has
 * brought all of it into view, the target sits at least that far above the container's bottom edge,
 * which is where the keyboard covers it.
 *
 * @param targetSize Size of the target, in pixels.
 * @param keyboardHeightPx Height of the keyboard, in pixels.
 * @return The area in the target's own coordinates.
 */
internal fun keyboardClearance(targetSize: IntSize, keyboardHeightPx: Int): Rect = Rect(
    left = 0f,
    top = 0f,
    right = targetSize.width.toFloat(),
    bottom = (targetSize.height + keyboardHeightPx).toFloat(),
)

/**
 * The bottom inset a compressing [KeyboardHost] pads its content by, as window insets.
 *
 * Read during layout rather than composition — by [padBottom], and by any descendant that pads
 * itself by window insets once these are consumed — so the keyboard's slide re-lays the content
 * out without recomposing it.
 *
 * @param height Animated height of the inset.
 */
private class KeyboardContentInsets(private val height: State<Dp>) : WindowInsets {
    /** No inset on the left edge. */
    override fun getLeft(density: Density, layoutDirection: LayoutDirection): Int = 0

    /** No inset on the top edge. */
    override fun getTop(density: Density): Int = 0

    /** No inset on the right edge. */
    override fun getRight(density: Density, layoutDirection: LayoutDirection): Int = 0

    /** The keyboard's current height while it compresses the content, and zero otherwise. */
    override fun getBottom(density: Density): Int = with(density) { height.value.roundToPx() }
}

/**
 * Pads this element's bottom edge by [insets], read during layout.
 *
 * @param insets Insets whose bottom edge is left free.
 * @return This modifier, with the padding applied.
 */
private fun Modifier.padBottom(insets: WindowInsets): Modifier = layout { measurable, constraints ->
    val bottom = insets.getBottom(this).coerceAtLeast(0)
    val placeable = measurable.measure(constraints.offset(vertical = -bottom))
    layout(placeable.width, constraints.constrainHeight(placeable.height + bottom)) {
        placeable.place(0, 0)
    }
}

/**
 * The tinted layer between the host's content and its keyboard.
 *
 * Takes every press that lands on it, so the content beneath is out of reach while it shows: with
 * [KeyboardScrim.dismissOnTap] a tap closes the keyboard, and without it the press goes nowhere.
 *
 * @param transition Shared open/closed transition, so the fade cannot drift from the slide.
 * @param scrim Tint and dismiss behaviour.
 * @param animation Specs the fade runs on.
 * @param onDismiss Called when the tint is tapped, or activated by assistive technology, and
 *   [KeyboardScrim.dismissOnTap] is set.
 */
@Composable
private fun BoxScope.Scrim(
    transition: Transition<Boolean>,
    scrim: KeyboardScrim,
    animation: KeyboardAnimation,
    onDismiss: () -> Unit,
) {
    transition.AnimatedVisibility(
        visible = { it },
        enter = animation.scrimEnter,
        exit = animation.scrimExit,
        modifier = Modifier.matchParentSize(),
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .background(scrim.color)
                .then(
                    if (scrim.dismissOnTap) {
                        // A tap detector and a semantics action rather than `clickable`, as in
                        // foundation's Dialog: the scrim is labelled and can be activated by
                        // assistive technology, but is not a focus stop ahead of the screen's own
                        // controls.
                        Modifier
                            .pointerInput(onDismiss) { detectTapGestures { onDismiss() } }
                            .semantics {
                                contentDescription = scrim.dismissLabel
                                onClick(label = scrim.dismissLabel) {
                                    onDismiss()
                                    true
                                }
                            }
                    } else {
                        // The tint draws the screen as unavailable, so a press on it must not reach
                        // the content it covers even when it does not close the keyboard.
                        Modifier.pointerInput(Unit) {
                            awaitEachGesture { awaitFirstDown(requireUnconsumed = false).consume() }
                        }
                    },
                ),
        )
    }
}
