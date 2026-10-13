package dev.voir.areca.foundation.topbar

import dev.voir.areca.foundation.slot.SlotRow
import dev.voir.areca.foundation.slot.SlotRowLayout
import androidx.compose.ui.unit.Dp

/**
 * Where a [TopBar]'s title sits between the bar's controls.
 *
 * The choice is not cosmetic: it decides what happens when the title and the controls cannot both
 * have the room they want. Neither option ever shrinks a control — see [SlotRowLayout.Centered].
 */
public enum class TopBarTitleAlignment {
    /**
     * Title begins after the leading control and runs to the trailing one.
     *
     * Also the alignment to use when the title slot holds a control rather than a title — a search
     * field, a segmented picker — because the slot is given every pixel the controls leave.
     */
    Start,

    /**
     * Title holds the bar's centre line while it fits there, and spreads into the space between the
     * controls when it does not.
     */
    Center,
}

/**
 * Slot layout this alignment produces.
 *
 * @param gap Space between the title and each control row.
 * @return Layout to hand to [SlotRow].
 */
internal fun TopBarTitleAlignment.slotRowLayout(gap: Dp): SlotRowLayout = when (this) {
    TopBarTitleAlignment.Start -> SlotRowLayout(startGap = gap, endGap = gap)
    TopBarTitleAlignment.Center -> SlotRowLayout.Centered.copy(startGap = gap, endGap = gap)
}
