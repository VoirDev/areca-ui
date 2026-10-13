package dev.voir.areca.foundation.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp

/**
 * Which edge of a row a selection control sits on.
 *
 * Leading is the convention for a list you are picking from, where the controls form a column the
 * eye can run down. Trailing suits a row whose subject matters more than its state — a settings
 * row, or a list whose leading slot is already spoken for by an avatar.
 */
public enum class ListItemControlPlacement {
    /** The control sits before the text block, in the leading slot. */
    Leading,

    /** The control sits after the text block, in the trailing slot. */
    Trailing,
}

/**
 * Builds a row slot that holds a selection control alongside whatever the caller put in that slot.
 *
 * Combining rather than replacing is what lets a checkbox row still carry an avatar: the control
 * takes the outer position and the caller's content sits beside it.
 *
 * @param control The selection control.
 * @param extra Content the caller supplied for the same slot, if any.
 * @param gap Spacing between the control and [extra].
 * @param controlFirst Whether the control comes before [extra] in reading order.
 * @return Slot content holding both, or the control alone when [extra] is `null`.
 */
internal fun combinedControlSlot(
    control: @Composable () -> Unit,
    extra: (@Composable BoxScope.() -> Unit)?,
    gap: Dp,
    controlFirst: Boolean,
): @Composable BoxScope.() -> Unit = {
    if (extra == null) {
        control()
    } else {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(gap),
        ) {
            if (controlFirst) {
                control()
                SlotContent(extra)
            } else {
                SlotContent(extra)
                control()
            }
        }
    }
}

/**
 * Renders slot content that expects a [BoxScope] receiver inside a row.
 *
 * @param content Slot content to render.
 */
@Composable
private fun SlotContent(content: @Composable BoxScope.() -> Unit) {
    Box(contentAlignment = Alignment.Center, content = content)
}
