package dev.voir.areca.keyboard

import androidx.compose.foundation.Indication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.ObserverModifierNode
import androidx.compose.ui.node.SemanticsModifierNode
import androidx.compose.ui.node.invalidateSemantics
import androidx.compose.ui.node.observeReads
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.semantics.collapse
import androidx.compose.ui.semantics.expand
import androidx.compose.ui.unit.IntSize

/**
 * A place on screen the keyboard is opened for: what to focus, and what to scroll into view.
 *
 * Created with [rememberKeyboardTarget] and attached with [Modifier.keyboardTarget], or with
 * [Modifier.keyboardAnchor] for an element that opens the keyboard itself, such as a read-only text
 * field. One per input, never shared — two inputs sharing a target would fight over focus.
 *
 * @property key Caller-supplied identity, carried so a host can decide *which* keyboard to draw for
 *   this target. `null` when a screen has only one kind of keyboard and does not need to tell its
 *   targets apart. Use a key that can be saved — a string, a number, an enum — so an open keyboard
 *   survives a configuration change; see [KeyboardController.Saver].
 * @property focusRequester Moves focus to the target, so it can receive physical key presses while
 *   the on-screen keyboard is up.
 * @property bringIntoViewRequester Scrolls the target clear of the keyboard once it is laid out.
 */
@Stable
public data class KeyboardTarget(
    public val key: Any? = null,
    public val focusRequester: FocusRequester,
    public val bringIntoViewRequester: BringIntoViewRequester,
) {
    /**
     * Size of the element [Modifier.keyboardAnchor] is attached to, in pixels, as of its last
     * layout. [IntSize.Zero] until then.
     *
     * Read only when a host scrolls the target, never during composition, so it is a plain field
     * rather than snapshot state. Not part of the target's equality.
     */
    internal var size: IntSize = IntSize.Zero
}

/**
 * Creates a [KeyboardTarget] that survives recomposition.
 *
 * @param key Identity carried on the target so a host can pick a keyboard for it. See
 *   [KeyboardTarget.key].
 * @return A target scoped to the calling composition.
 */
@Composable
public fun rememberKeyboardTarget(key: Any? = null): KeyboardTarget {
    val focusRequester = remember { FocusRequester() }
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    return remember(key, focusRequester, bringIntoViewRequester) {
        KeyboardTarget(key, focusRequester, bringIntoViewRequester)
    }
}

/**
 * Makes this element [target]'s place on screen, without making it open the keyboard.
 *
 * Attaches the target's focus and scroll requesters, measures the element so a host can scroll it
 * clear of a keyboard drawn over the content, and tells assistive technology whether the keyboard
 * is open for it — collapsed while it is not, expanded while it is — with actions to open and close
 * it.
 *
 * [Modifier.keyboardTarget] applies this and adds the press that opens the keyboard. Use this alone
 * for an element that already handles its own presses and focus, such as a read-only text field
 * that opens the keyboard when it gains focus.
 *
 * @param controller Controller whose keyboard serves the target. Normally [LocalKeyboardController].
 * @param target Target to anchor, from [rememberKeyboardTarget].
 * @param scroll Whether the accessibility action that opens the keyboard scrolls the target clear
 *   of it. See [KeyboardController.open].
 * @param stealFocus Whether the accessibility action that opens the keyboard moves focus to the
 *   target. See [KeyboardController.open].
 * @return This modifier, with the target anchored.
 */
public fun Modifier.keyboardAnchor(
    controller: KeyboardController,
    target: KeyboardTarget,
    scroll: Boolean = true,
    stealFocus: Boolean = true,
): Modifier = keyboardAnchor(controller, target, enabled = true, scroll, stealFocus)

/**
 * [Modifier.keyboardAnchor], with the open and close actions withheld while not [enabled].
 *
 * @param controller Controller whose keyboard serves the target.
 * @param target Target to anchor.
 * @param enabled Whether assistive technology is offered the actions that open and close the
 *   keyboard.
 * @param scroll Whether opening scrolls the target clear of the keyboard.
 * @param stealFocus Whether opening moves focus to the target.
 * @return This modifier, with the target anchored.
 */
private fun Modifier.keyboardAnchor(
    controller: KeyboardController,
    target: KeyboardTarget,
    enabled: Boolean,
    scroll: Boolean,
    stealFocus: Boolean,
): Modifier = this
    .focusRequester(target.focusRequester)
    .bringIntoViewRequester(target.bringIntoViewRequester)
    .onSizeChanged { target.size = it }
    .then(KeyboardExpandedElement(controller, target, enabled, scroll, stealFocus))

/**
 * Makes this element open [controller]'s keyboard for [target] when it is pressed.
 *
 * Draws no press feedback by default. A keyboard target is normally a large custom display — an
 * amount, a date — where a ripple across the whole area reads as a mistake; pass [indication] when
 * the element is small enough to want one.
 *
 * The element is announced as a button, collapsed or expanded as the keyboard is closed or open for
 * it, and is focusable in every input mode, touch included, so [stealFocus] lands focus on it and a
 * physical keyboard attached to a phone or tablet reaches it. `clickable` already treats Enter and
 * Space as a press.
 *
 * @param controller Controller to open. Normally [LocalKeyboardController].
 * @param target Target to open for, from [rememberKeyboardTarget].
 * @param enabled Whether pressing opens the keyboard. A disabled target is not focusable.
 * @param scroll Whether to scroll the target clear of the keyboard. See [KeyboardController.open].
 * @param stealFocus Whether to move focus to the target. See [KeyboardController.open].
 * @param onClickLabel Description of the press for assistive technology, such as `"Edit amount"`.
 * @param interactionSource Hoisted interaction source. `null` lets the modifier own one internally.
 * @param indication Press, hover, and focus feedback. `null` draws none.
 * @return This modifier, with keyboard targeting applied.
 */
public fun Modifier.keyboardTarget(
    controller: KeyboardController,
    target: KeyboardTarget,
    enabled: Boolean = true,
    scroll: Boolean = true,
    stealFocus: Boolean = true,
    onClickLabel: String? = null,
    interactionSource: MutableInteractionSource? = null,
    indication: Indication? = null,
): Modifier = this
    .keyboardAnchor(controller, target, enabled, scroll, stealFocus)
    // `clickable`'s focus target is focusable only outside touch mode on Android and iOS, so after a
    // tap `requestFocus()` would be refused; this keeps it focusable in every input mode. Adding
    // `focusable()` instead would put two focus targets on one element and stop at the wrong one
    // when tabbing.
    .focusProperties { canFocus = enabled }
    .clickable(
        interactionSource = interactionSource,
        indication = indication,
        enabled = enabled,
        onClickLabel = onClickLabel,
        role = Role.Button,
    ) {
        controller.open(target, scroll, stealFocus)
    }

/**
 * Element of the expanded-state semantics [Modifier.keyboardAnchor] applies.
 *
 * @property controller Controller whose state is reported.
 * @property target Target the state is reported for.
 * @property enabled Whether the open and close actions are offered.
 * @property scroll Whether the open action scrolls the target into view.
 * @property stealFocus Whether the open action moves focus to the target.
 */
private data class KeyboardExpandedElement(
    val controller: KeyboardController,
    val target: KeyboardTarget,
    val enabled: Boolean,
    val scroll: Boolean,
    val stealFocus: Boolean,
) : ModifierNodeElement<KeyboardExpandedNode>() {
    /**
     * Creates the node.
     *
     * @return A node reporting this element's state.
     */
    override fun create(): KeyboardExpandedNode =
        KeyboardExpandedNode(controller, target, enabled, scroll, stealFocus)

    /**
     * Points an existing node at this element's values.
     *
     * @param node Node to update.
     */
    override fun update(node: KeyboardExpandedNode) {
        node.update(controller, target, enabled, scroll, stealFocus)
    }
}

/**
 * Reports whether the keyboard is open for a target, and offers actions to open and close it.
 *
 * Observes the controller's snapshot state outside composition, so the element announcing the
 * target need not recompose when the keyboard opens.
 *
 * @param controller Controller whose state is reported.
 * @param target Target the state is reported for.
 * @param enabled Whether the open and close actions are offered.
 * @param scroll Whether the open action scrolls the target into view.
 * @param stealFocus Whether the open action moves focus to the target.
 */
private class KeyboardExpandedNode(
    private var controller: KeyboardController,
    private var target: KeyboardTarget,
    private var enabled: Boolean,
    private var scroll: Boolean,
    private var stealFocus: Boolean,
) : Modifier.Node(), SemanticsModifierNode, ObserverModifierNode {
    /** Whether the keyboard was open for [target] when last observed. */
    private var expanded: Boolean = false

    /** Starts observing the controller once the node is attached. */
    override fun onAttach() {
        observeExpanded()
    }

    /** Re-reads the controller after a state it was read from changed. */
    override fun onObservedReadsChanged() {
        observeExpanded()
    }

    /**
     * Replaces the node's values.
     *
     * @param controller Controller whose state is reported.
     * @param target Target the state is reported for.
     * @param enabled Whether the open and close actions are offered.
     * @param scroll Whether the open action scrolls the target into view.
     * @param stealFocus Whether the open action moves focus to the target.
     */
    fun update(
        controller: KeyboardController,
        target: KeyboardTarget,
        enabled: Boolean,
        scroll: Boolean,
        stealFocus: Boolean,
    ) {
        this.controller = controller
        this.target = target
        this.enabled = enabled
        this.scroll = scroll
        this.stealFocus = stealFocus
        if (isAttached) {
            observeExpanded()
            invalidateSemantics()
        }
    }

    /** Reads whether the keyboard is open for [target], invalidating semantics when it changes. */
    private fun observeExpanded() {
        var open = false
        observeReads { open = controller.isOpenFor(target) }
        if (open != expanded) {
            expanded = open
            invalidateSemantics()
        }
    }

    /**
     * Offers the action that closes the keyboard while it is open for [target], and the one that
     * opens it otherwise. Offers neither while not [enabled].
     */
    override fun SemanticsPropertyReceiver.applySemantics() {
        if (!enabled) return
        if (expanded) {
            collapse {
                controller.close()
                true
            }
        } else {
            expand {
                controller.open(target, scroll, stealFocus)
                true
            }
        }
    }
}
