package dev.voir.areca.foundation.selection

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/**
 * The three colors a selection control draws in one of its states.
 *
 * A checkbox, a radio, and a toggle are the same thing in three shapes: a container that fills, an
 * edge that outlines it, and a mark inside that says which way it is set. Naming the triple once is
 * what keeps the three controls from drifting apart state by state.
 *
 * @property bg Container fill. For a toggle, the track.
 * @property border Container edge. [Color.Transparent] draws no visible border.
 * @property mark The check, the dot, or the thumb.
 */
@Immutable
public data class SelectionColors(
    public val bg: Color,
    public val border: Color,
    public val mark: Color,
)

/**
 * Colors a two-state selection control uses across its six combinations of state and editability.
 *
 * Six rather than three because a disabled checkbox and a disabled *checked* checkbox are different
 * — one is an empty grey box, the other a filled grey box — and a design that only greys the
 * container loses the distinction entirely.
 *
 * Sealed, and implemented only by [CheckboxStyle] and [RadioStyle], which differ in their defaults
 * and in what they draw, never in which state maps to which colors. Build one of those rather than
 * implementing this. Resolve a state with [resolve].
 */
public sealed interface SelectionStyle {
    /** Unchecked and editable. */
    public val unchecked: SelectionColors

    /** Checked and editable. */
    public val checked: SelectionColors

    /** Unchecked and unavailable. */
    public val disabledUnchecked: SelectionColors

    /** Checked and unavailable. */
    public val disabledChecked: SelectionColors

    /** Unchecked, shown but not editable. */
    public val readOnlyUnchecked: SelectionColors

    /** Checked, shown but not editable. */
    public val readOnlyChecked: SelectionColors
}

/**
 * Picks the colors for one combination of state and editability.
 *
 * Disabled wins over read-only: a control that is both unavailable and uneditable is simply
 * unavailable, and saying so twice would be a distinction the user cannot act on.
 *
 * @param selected Whether the control is checked, or showing an indeterminate mark.
 * @param enabled Whether the control is available at all.
 * @param readOnly Whether the control shows its state but rejects changes.
 * @return Colors to draw with.
 */
public fun SelectionStyle.resolve(
    selected: Boolean,
    enabled: Boolean,
    readOnly: Boolean,
): SelectionColors = when {
    !enabled && selected -> disabledChecked
    !enabled -> disabledUnchecked
    readOnly && selected -> readOnlyChecked
    readOnly -> readOnlyUnchecked
    selected -> checked
    else -> unchecked
}
