package dev.voir.areca.foundation.adaptive

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.KeyInputModifierNode
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.AwaitPointerEventScope
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.PointerType
import androidx.compose.ui.input.pointer.SuspendingPointerInputModifierNode
import androidx.compose.ui.input.pointer.isSecondaryPressed
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.node.DelegatingNode
import androidx.compose.ui.node.GlobalPositionAwareModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.SemanticsModifierNode
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.semantics.onLongClick
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.roundToIntRect
import dev.voir.areca.foundation.overlay.DropdownPopupState
import kotlin.math.roundToInt

/**
 * Opens [state]'s [AdaptiveMenu] from this element: a right-click under a pointer, a long press
 * under touch, the Menu key or Shift+F10 from the keyboard, and the long-click action of assistive
 * technology.
 *
 * Put it on a row or a card that has actions of its own, ahead of its `clickable`, and pass the same
 * state to the [AdaptiveMenu]. It anchors the menu itself: a pointer or a finger opens it where it
 * pressed, the keyboard under the element. A long press that opens the menu does not also click
 * the element.
 *
 * ```kotlin
 * val menu = rememberDropdownPopup()
 * ListItem(
 *     title = { Text("Lidl") },
 *     onClick = { open(transaction) },
 *     modifier = Modifier.contextMenu(menu),
 * )
 * AdaptiveMenu(state = menu, title = "Lidl", actions = actions)
 * ```
 *
 * @param state The menu the element opens.
 * @param enabled Whether the gestures open it.
 * @param label What assistive technology calls the long-click action. English by default; pass a
 *   localized string.
 * @return The receiver, opening the menu.
 */
public fun Modifier.contextMenu(
    state: DropdownPopupState,
    enabled: Boolean = true,
    label: String = "Show actions",
): Modifier = if (enabled) this then ContextMenuElement(state, label) else this

private class ContextMenuElement(
    private val state: DropdownPopupState,
    private val label: String,
) : ModifierNodeElement<ContextMenuNode>() {
    override fun create(): ContextMenuNode = ContextMenuNode(state, label)

    override fun update(node: ContextMenuNode) {
        node.state = state
        node.label = label
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "contextMenu"
        properties["state"] = state
        properties["label"] = label
    }

    override fun equals(other: Any?): Boolean =
        other is ContextMenuElement && other.state == state && other.label == label

    override fun hashCode(): Int = 31 * state.hashCode() + label.hashCode()
}

/**
 * Watches the element's gestures and keys, and anchors and opens the menu.
 *
 * Gestures are read on the initial pass, before the element's own `clickable` sees them, so a
 * right-click or a long press that opens the menu can consume the rest of the gesture and the
 * click never fires.
 */
private class ContextMenuNode(
    var state: DropdownPopupState,
    var label: String,
) : DelegatingNode(), GlobalPositionAwareModifierNode, SemanticsModifierNode, KeyInputModifierNode {
    /** Where the element is, so a press inside it can be turned into a point in the window. */
    private var coordinates: LayoutCoordinates? = null

    init {
        delegate(SuspendingPointerInputModifierNode { detectMenuGestures() })
    }

    override fun onGloballyPositioned(coordinates: LayoutCoordinates) {
        this.coordinates = coordinates
    }

    override fun SemanticsPropertyReceiver.applySemantics() {
        onLongClick(label = label) {
            open(at = null)
            true
        }
    }

    override fun onKeyEvent(event: KeyEvent): Boolean {
        if (event.type != KeyEventType.KeyDown) return false
        val opens = event.key == Key.Menu || (event.key == Key.F10 && event.isShiftPressed)
        if (opens) open(at = null)
        return opens
    }

    override fun onPreKeyEvent(event: KeyEvent): Boolean = false

    /**
     * Anchors the menu at [at], a point inside the element, or along the element's bottom when
     * `null`, and opens it.
     */
    private fun open(at: Offset?) {
        val layout = coordinates?.takeIf { it.isAttached } ?: return
        val anchor = state.anchorState
        anchor.anchorBoundsInWindow = if (at != null) {
            val point = layout.localToWindow(at)
            IntRect(IntOffset(point.x.roundToInt(), point.y.roundToInt()), IntSize.Zero)
        } else {
            layout.boundsInWindow().roundToIntRect()
        }
        anchor.anchorSize = layout.size
        state.show()
    }

    private suspend fun PointerInputScope.detectMenuGestures() {
        awaitEachGesture {
            var event = awaitPointerEvent(PointerEventPass.Initial)
            while (event.type != PointerEventType.Press) event = awaitPointerEvent(PointerEventPass.Initial)
            val down = event.changes.firstOrNull() ?: return@awaitEachGesture
            when {
                event.buttons.isSecondaryPressed -> {
                    open(at = down.position)
                    consumeUntilUp()
                }
                // A mouse's primary button clicks; only a finger or a pen holds for the menu.
                down.type == PointerType.Mouse -> Unit
                else -> {
                    val endedEarly = withTimeoutOrNull(viewConfiguration.longPressTimeoutMillis) {
                        endsBeforeLongPress(down, viewConfiguration.touchSlop)
                    }
                    if (endedEarly == null) {
                        open(at = down.position)
                        consumeUntilUp()
                    }
                }
            }
        }
    }
}

/**
 * Waits for [down] to lift or wander past [slop]; `true` when it does. Never returns `false` — it
 * suspends until the caller's timeout calls the hold a long press.
 */
private suspend fun AwaitPointerEventScope.endsBeforeLongPress(down: PointerInputChange, slop: Float): Boolean {
    while (true) {
        val event = awaitPointerEvent(PointerEventPass.Initial)
        val change = event.changes.firstOrNull { it.id == down.id } ?: return true
        if (!change.pressed || (change.position - down.position).getDistance() > slop) return true
    }
}

/** Consumes the rest of the gesture, so the element's own `clickable` cancels its press. */
private suspend fun AwaitPointerEventScope.consumeUntilUp() {
    var event = currentEvent
    while (true) {
        event.changes.forEach { it.consume() }
        if (event.changes.none { it.pressed }) return
        event = awaitPointerEvent(PointerEventPass.Initial)
    }
}
