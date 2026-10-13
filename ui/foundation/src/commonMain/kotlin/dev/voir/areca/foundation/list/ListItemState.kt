package dev.voir.areca.foundation.list

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf

/**
 * Interaction state of the [ListItem] currently being composed.
 *
 * Row slots read this to stay in step with the row they live in — a trailing chevron can lean
 * toward the hovered row, a value can brighten, a check mark can fade in with the selection. Slots
 * composed outside a row see [Idle].
 *
 * @property enabled Whether the row accepts input.
 * @property selected Whether the row is one of the selected rows in its list.
 * @property hovered Whether a pointer is over the row.
 * @property pressed Whether the row is held down.
 * @property focused Whether the row holds keyboard focus.
 * @property dragging Whether the row is being dragged, in a reorderable list.
 */
@Immutable
public data class ListItemState(
    public val enabled: Boolean = true,
    public val selected: Boolean = false,
    public val hovered: Boolean = false,
    public val pressed: Boolean = false,
    public val focused: Boolean = false,
    public val dragging: Boolean = false,
) {
    /**
     * Whether the row is being reached for — hovered, pressed or keyboard-focused — while it can
     * still accept input. Focus is included because a row reached by keyboard should answer the
     * same way as one reached by pointer.
     */
    val isActive: Boolean get() = enabled && (hovered || pressed || focused)

    /** Shared states. */
    public companion object {
        /** State of a row that is enabled and untouched, used outside of a [ListItem]. */
        public val Idle: ListItemState = ListItemState()
    }
}

/**
 * Interaction state of the enclosing [ListItem], or [ListItemState.Idle] outside of one.
 *
 * Public so consumer-defined row slots can react to hover, press, selection and disabled the same
 * way the built-in ones do.
 */
public val LocalListItemState: ProvidableCompositionLocal<ListItemState> =
    compositionLocalOf { ListItemState.Idle }

/**
 * Whether the enclosing list accepts input.
 *
 * A row combines it with its own `enabled` flag, so disabling a whole list disables its rows
 * without each call site repeating the condition.
 */
public val LocalListEnabled: ProvidableCompositionLocal<Boolean> = compositionLocalOf { true }
