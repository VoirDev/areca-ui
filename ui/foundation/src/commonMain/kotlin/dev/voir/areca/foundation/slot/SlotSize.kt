package dev.voir.areca.foundation.slot

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp

/**
 * How one slot of a [SlotRow] claims width.
 *
 * The three cases are the three answers a row can give when asked how wide a cell is: as wide as its
 * content, exactly this wide, or a share of whatever is left. Mixing them is the point — a list row
 * is a [Fixed] leading slot, a [Weight] centre, and a [Wrap] trailing one.
 */
@Immutable
public sealed interface SlotSize {
    /**
     * As wide as the slot's content needs.
     *
     * An empty slot takes no width at all, so a row with no leading content closes up rather than
     * leaving a gap.
     */
    public data object Wrap : SlotSize

    /**
     * Exactly [width], whatever the content.
     *
     * Use it to line up a column of rows whose leading content differs — a mix of avatars, icons and
     * nothing — so their labels start at the same x.
     *
     * @property width Width of the slot.
     */
    public data class Fixed(public val width: Dp) : SlotSize

    /**
     * A share of the width left after the [Wrap] and [Fixed] slots have taken theirs.
     *
     * @property weight This slot's share, relative to the other weighted slots in the same row.
     * @property fill Whether the slot is forced to occupy its whole share. `false` lets it be
     *   narrower than its share when its content is, which is what centres a short label inside a
     *   wide column instead of stretching it.
     */
    public data class Weight(
        public val weight: Float,
        public val fill: Boolean = true,
    ) : SlotSize
}

/**
 * Whether a [SlotRow] draws the gap between a side slot and the middle one.
 *
 * A gap separates two things, so it is dropped beside an empty slot — except a [SlotSize.Fixed]
 * one. A fixed slot keeps its width when empty so that a column of rows lines up, and that only
 * works if it keeps its gap too: otherwise the label of a row without leading content would start
 * a gap's width earlier than its neighbours'.
 *
 * @param size Width rule of the side slot.
 * @param hasContent Whether the side slot has content.
 * @return `true` when the gap beside the slot takes space.
 */
internal fun drawsSideGap(size: SlotSize, hasContent: Boolean): Boolean =
    hasContent || size is SlotSize.Fixed
