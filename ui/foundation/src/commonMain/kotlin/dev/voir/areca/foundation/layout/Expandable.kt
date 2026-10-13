package dev.voir.areca.foundation.layout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.millis
import androidx.compose.animation.core.tween as composeTween

/**
 * Whether one [Expandable] is open.
 *
 * Hoisted into a state object rather than left as a boolean so that a trigger far from the content
 * can flip it — a chevron in a row's trailing slot, a "Show more" link at the foot of a card — and so
 * several triggers can share one section.
 *
 * The value changes only through [expand], [collapse], and [toggle]. A caller that wants to drive
 * expansion from its own state should use the [Expandable] overload that takes a plain boolean
 * instead of assigning through this type.
 *
 * @param initialExpanded Whether the section starts open.
 */
@Stable
public class ExpandableState(initialExpanded: Boolean) {
    /** Whether the section is open. */
    public var expanded: Boolean by mutableStateOf(initialExpanded)
        private set

    /** Opens the section. Does nothing if it is already open. */
    public fun expand() {
        expanded = true
    }

    /** Closes the section. Does nothing if it is already closed. */
    public fun collapse() {
        expanded = false
    }

    /** Opens a closed section, or closes an open one. */
    public fun toggle() {
        expanded = !expanded
    }

    /** Saving support for [ExpandableState]. */
    public companion object {
        /** Saves and restores [expanded] across configuration change and process death. */
        public val Saver: Saver<ExpandableState, Boolean> = Saver(
            save = { it.expanded },
            restore = { ExpandableState(it) },
        )
    }
}

/**
 * Remembers an [ExpandableState] that survives configuration change.
 *
 * [initialExpanded] is read once. Changing it later does not reopen or close the section — the state
 * belongs to the user from the first composition onwards, and having it snap back when a parameter
 * changed would undo their action.
 *
 * @param initialExpanded Whether the section starts open.
 * @return State remembered for this position in the composition.
 */
@Composable
public fun rememberExpandableState(initialExpanded: Boolean = false): ExpandableState =
    rememberSaveable(saver = ExpandableState.Saver) { ExpandableState(initialExpanded) }

/**
 * Transitions an [Expandable] opens and closes with.
 *
 * The fade is deliberately shorter than the size change and runs at its start, so content appears
 * to arrive *into* space that is already opening rather than stretching as it fades. Reversed on the
 * way out.
 *
 * @property enter Transition used when the section opens.
 * @property exit Transition used when the section closes.
 */
@Stable
public data class ExpandableTransition(
    public val enter: EnterTransition,
    public val exit: ExitTransition,
) {
    /** Theme-backed presets and builders. */
    public companion object {
        /** Theme default: a quick fade over a slightly longer vertical expand. */
        public val Default: ExpandableTransition
            @Composable
            get() {
                val motion = ArecaTheme.motion
                return ExpandableTransition(
                    enter = fadeIn(composeTween(motion.fast.millis, easing = motion.enter)) +
                        expandVertically(
                            composeTween(motion.medium.millis, easing = motion.standard)
                        ),
                    exit = fadeOut(composeTween(motion.fast.millis, easing = motion.exit)) +
                        shrinkVertically(
                            composeTween(motion.medium.millis, easing = motion.standard)
                        ),
                )
            }
    }
}

/**
 * A section that opens and closes, with a caller-supplied trigger above it.
 *
 * The trigger is a slot rather than a fixed control, because what opens a section is never the same
 * twice — a whole list row, a chevron, a link, a heading. It is handed the current state and a
 * function to flip it, so the trigger never needs to know where the state lives.
 *
 * Content is composed only while open, so an expensive section costs nothing until it is used.
 *
 * @param state Whether the section is open.
 * @param modifier Modifier applied to the column that holds the trigger and the section.
 * @param transition How it opens and closes.
 * @param trigger Control shown above the content, given the open state and a toggle function.
 * @param content The section itself.
 */
@Composable
public fun Expandable(
    state: ExpandableState,
    modifier: Modifier = Modifier,
    transition: ExpandableTransition = ExpandableTransition.Default,
    trigger: @Composable (expanded: Boolean, onToggle: () -> Unit) -> Unit,
    content: @Composable () -> Unit,
) {
    Expandable(
        expanded = state.expanded,
        modifier = modifier,
        onExpandedChange = { state.toggle() },
        transition = transition,
        trigger = trigger,
        content = content,
    )
}

/**
 * A section that opens and closes, driven by the caller's own state.
 *
 * The overload to use when expansion is already part of a screen's state — one open row in a list,
 * a section opened by a search result — rather than owned by the component.
 *
 * The trigger and the section share one column, so the component is a single node wherever it is
 * placed: in a row, a box, or a lazy item, the section still opens below its trigger.
 *
 * @param expanded Whether the section is open.
 * @param modifier Modifier applied to the column that holds the trigger and the section.
 * @param onExpandedChange Called with the state the user is asking for. `null` makes [trigger]
 *   inert, for a section whose openness is not the user's to change.
 * @param transition How it opens and closes.
 * @param trigger Control shown above the content, given the open state and a toggle function.
 *   `null` renders the content alone, for a section controlled from somewhere else entirely.
 * @param content The section itself.
 */
@Composable
public fun Expandable(
    expanded: Boolean,
    modifier: Modifier = Modifier,
    onExpandedChange: ((Boolean) -> Unit)? = null,
    transition: ExpandableTransition = ExpandableTransition.Default,
    trigger: (@Composable (expanded: Boolean, onToggle: () -> Unit) -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    Column(modifier = modifier) {
        if (trigger != null) {
            trigger(expanded) { onExpandedChange?.invoke(!expanded) }
        }

        AnimatedVisibility(
            visible = expanded,
            enter = transition.enter,
            exit = transition.exit,
        ) {
            content()
        }
    }
}
