package dev.voir.areca.foundation.overlay

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.millis
import dev.voir.areca.window.InputMode
import kotlinx.coroutines.delay

/**
 * A short label that names [content] when the pointer rests on it or keyboard focus reaches it.
 *
 * For a control whose meaning a pointer user cannot read off it — an icon alone, a truncated title.
 * The label shows after the pointer has rested for half a second, hides when it leaves or presses,
 * and shows at once when focus arrives by keyboard. Under touch it never shows: a long press there
 * belongs to the element. [dev.voir.areca.foundation.button.IconButton] shows its content
 * description this way already.
 *
 * The label is for the eye alone. Say the same thing to assistive technology through the content's
 * own description.
 *
 * ```kotlin
 * TooltipBox(text = "Synced 2 minutes ago") {
 *     Icon(ArecaIcons.CloudCheck, contentDescription = "Synced 2 minutes ago")
 * }
 * ```
 *
 * @param text The label: a few words, one line where it can be.
 * @param modifier Modifier applied to the box around [content].
 * @param placement Where the label sits relative to [content]; it flips to the other side when it
 *   would leave the window.
 * @param enabled Whether the label shows at all.
 * @param content The element the label names.
 */
@Composable
public fun TooltipBox(
    text: String,
    modifier: Modifier = Modifier,
    placement: AnchoredPlacement = AnchoredPlacement.BelowCenter,
    enabled: Boolean = true,
    content: @Composable BoxScope.() -> Unit,
) {
    val tooltip = rememberPointerTooltip(enabled)
    Box(modifier = modifier.tooltipTrigger(tooltip)) {
        content()
        if (tooltip != null) TooltipPopup(tooltip, text, placement)
    }
}

/** How long the pointer rests on an element before its tooltip shows. */
private const val TooltipDelayMillis = 500L

/**
 * Hover, press and focus of one element with a tooltip, and so whether the tooltip shows.
 */
@Stable
internal class PointerTooltipState {
    val anchor: AnchoredState = AnchoredState()

    /** Whether the pointer is over the element. */
    var hovered: Boolean by mutableStateOf(false)

    /** Whether the pointer has rested long enough for the tooltip. */
    var rested: Boolean by mutableStateOf(false)

    /** Whether keyboard focus, not a click, brought focus to the element. */
    var focusedByKey: Boolean by mutableStateOf(false)

    /** Set by a press, cleared when the pointer leaves: a pressed element keeps its label hidden. */
    var pressed: Boolean by mutableStateOf(false)

    val visible: Boolean get() = !pressed && (rested || focusedByKey)
}

/**
 * Remembers an element's tooltip state, or returns `null` under touch or when [enabled] is `false`,
 * where no tooltip shows.
 */
@Composable
internal fun rememberPointerTooltip(enabled: Boolean): PointerTooltipState? {
    if (!enabled || ArecaTheme.window.input != InputMode.Pointer) return null
    val state = remember { PointerTooltipState() }
    LaunchedEffect(state.hovered) {
        if (state.hovered) {
            delay(TooltipDelayMillis)
            state.rested = true
        } else {
            state.rested = false
        }
    }
    return state
}

/**
 * Tracks the hover, press and focus [state] reads, and anchors its tooltip to this element. The
 * receiver unchanged when [state] is `null`.
 */
internal fun Modifier.tooltipTrigger(state: PointerTooltipState?): Modifier {
    if (state == null) return this
    return this
        .anchored(state.anchor)
        .onFocusChanged { focus ->
            // A click focuses the element while the pointer is on it; only focus that arrives with
            // the pointer elsewhere came from the keyboard.
            state.focusedByKey = focus.hasFocus && !state.hovered
        }
        .pointerInput(state) {
            awaitPointerEventScope {
                while (true) {
                    // The initial pass sees the events before the element's own clickable consumes
                    // them; nothing here consumes, so the element behaves as it would without.
                    val event = awaitPointerEvent(PointerEventPass.Initial)
                    when (event.type) {
                        PointerEventType.Enter -> state.hovered = true
                        PointerEventType.Exit -> {
                            state.hovered = false
                            state.pressed = false
                        }
                        PointerEventType.Press -> {
                            state.pressed = true
                            state.focusedByKey = false
                        }
                    }
                }
            }
        }
}

/**
 * Draws [state]'s tooltip while it is visible.
 *
 * @param state State of the element the tooltip names.
 * @param text The label.
 * @param placement Where the label sits relative to the element.
 */
@Composable
internal fun TooltipPopup(state: PointerTooltipState, text: String, placement: AnchoredPlacement) {
    if (!state.visible) return
    val anchorBounds = state.anchor.anchorBoundsInWindow ?: return
    val provider = remember(anchorBounds, placement) {
        AnchoredPositionProvider(anchorBounds = anchorBounds, placement = placement, offsetX = 0, offsetY = 0)
    }
    Popup(popupPositionProvider = provider, properties = PopupProperties(focusable = false)) {
        val alpha = remember { Animatable(0f) }
        val duration = ArecaTheme.motion.fast.millis
        LaunchedEffect(Unit) { alpha.animateTo(1f, tween(duration)) }
        val shape = RoundedCornerShape(ArecaTheme.radius.sm)
        Box(
            modifier = Modifier
                .alpha(alpha.value)
                // Transparent, so the gap from the element holds on whichever side the label lands.
                .padding(vertical = ArecaTheme.spacing.xs)
                .widthIn(max = 280.dp)
                .shadow(ArecaTheme.elevation.md, shape, clip = false)
                .background(ArecaTheme.colors.bgContrast, shape)
                .padding(horizontal = ArecaTheme.spacing.md, vertical = ArecaTheme.spacing.xs)
                // The element says this itself; a second reading of the label would be noise.
                .clearAndSetSemantics {},
        ) {
            Text(text = text, variant = TextVariant.BodySmall, color = TextColor.OnContrast)
        }
    }
}
