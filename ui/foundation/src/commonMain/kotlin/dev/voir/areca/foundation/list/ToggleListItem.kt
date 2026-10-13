package dev.voir.areca.foundation.list

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import dev.voir.areca.foundation.selection.BinaryToggleCycle
import dev.voir.areca.foundation.selection.LocalToggleSize
import dev.voir.areca.foundation.selection.LocalToggleStyle
import dev.voir.areca.foundation.selection.Toggle
import dev.voir.areca.foundation.selection.ToggleSize
import dev.voir.areca.foundation.selection.ToggleState
import dev.voir.areca.foundation.selection.ToggleStyle
import dev.voir.areca.foundation.selection.toToggleableState
import dev.voir.areca.foundation.slot.SlotRowLayout

/**
 * A row whose whole surface flips a switch.
 *
 * The switch alone is a small target, and a row that only reacts where the switch sits reads as inert
 * everywhere else. This row takes the press itself — so it hovers, presses and takes keyboard focus
 * as one object, wherever it is pressed — and is announced as one switch carrying the row's copy as
 * its label. The switch inside is a picture of the state, not a second target.
 *
 * Each press moves the switch one step along [cycleOrder].
 *
 * @param state Current state of the switch.
 * @param onToggle Called with the next state when the row is activated.
 * @param title Main copy of the row.
 * @param modifier Modifier applied to the row container.
 * @param subtitle Supporting copy below [title].
 * @param overline Small copy above [title].
 * @param enabled Whether the row accepts input and uses its enabled colors.
 * @param nestingLevel Indentation steps of [ListItemSize.nestedLevelOffset] to add.
 * @param cycleOrder States the switch walks through, in order. Two states by default: a three-state
 * cycle is rarely what a row of settings means.
 * @param style Colors of the row across its states.
 * @param size Metrics and typography of the row.
 * @param layout Slot layout of the row.
 * @param toggleStyle Colors of the switch.
 * @param toggleSize Metrics of the switch.
 * @param interactionSource Source of the row interactions; pass one to observe them from outside.
 * @param leading Slot before the text block, tinted with the `extra` colors.
 * @param bottom Content below the main row line, inside the row hit area.
 */
@Composable
public fun ToggleListItem(
    state: ToggleState,
    onToggle: (ToggleState) -> Unit,
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: (@Composable () -> Unit)? = null,
    overline: (@Composable () -> Unit)? = null,
    enabled: Boolean = true,
    nestingLevel: Int = 0,
    cycleOrder: List<ToggleState> = BinaryToggleCycle,
    style: ListItemStyle = LocalListItemStyle.current ?: ListItemStyle.Default,
    size: ListItemSize = LocalListItemSize.current ?: ListItemSize.Default,
    layout: SlotRowLayout = LocalListItemLayout.current,
    toggleStyle: ToggleStyle = LocalToggleStyle.current ?: ToggleStyle.Default,
    toggleSize: ToggleSize = LocalToggleSize.current ?: ToggleSize.Default,
    interactionSource: MutableInteractionSource? = null,
    leading: (@Composable BoxScope.() -> Unit)? = null,
    bottom: (@Composable ColumnScope.() -> Unit)? = null,
) {
    val control: @Composable () -> Unit = {
        Toggle(
            state = state,
            // The row handles every press; the switch only draws the state.
            onToggle = null,
            // The row resolves its own enabled state, enclosing list included; follow it.
            enabled = LocalListItemState.current.enabled,
            cycleOrder = cycleOrder,
            size = toggleSize,
            style = toggleStyle,
        )
    }

    ControlListItem(
        control = control,
        // A toggle's "selected" is its on state, which is what row slots read as the selection.
        selected = state == ToggleState.On,
        action = ListItemAction.Toggle(
            state = state.toToggleableState(),
            role = Role.Switch,
            onToggle = { onToggle(state.next(cycleOrder)) },
        ),
        title = title,
        modifier = modifier,
        subtitle = subtitle,
        overline = overline,
        enabled = enabled,
        // A switched-on row is not a selected row: the toggle already states it, and highlighting
        // every enabled setting would turn a settings screen into a wall of color.
        highlightSelected = false,
        nestingLevel = nestingLevel,
        placement = ListItemControlPlacement.Trailing,
        style = style,
        size = size,
        layout = layout,
        interactionSource = interactionSource,
        leading = leading,
        bottom = bottom,
    )
}
