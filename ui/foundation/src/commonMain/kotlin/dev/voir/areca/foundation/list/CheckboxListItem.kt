package dev.voir.areca.foundation.list

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import dev.voir.areca.foundation.selection.Checkbox
import dev.voir.areca.foundation.selection.CheckboxSize
import dev.voir.areca.foundation.selection.CheckboxStyle
import dev.voir.areca.foundation.selection.LocalCheckboxSize
import dev.voir.areca.foundation.selection.LocalCheckboxStyle
import dev.voir.areca.foundation.selection.checkboxToggleableState
import dev.voir.areca.foundation.slot.SlotRowLayout

/**
 * A row of a multi-select list, whose whole surface toggles its checkbox.
 *
 * A checkbox alone is a small target, and a row that only reacts where the checkbox sits reads as
 * inert everywhere else. This row takes the press itself — so it hovers, presses and takes keyboard
 * focus as one object, wherever it is pressed — and is announced as one checkbox carrying the row's
 * copy as its label. The checkbox inside is a picture of the state, not a second target.
 *
 * By default the row stays visually neutral while checked: the checkbox already states the choice,
 * and filling every checked row turns a long list into a wall of color. Set [highlightChecked] for
 * a short list where the selection should be readable at a glance.
 *
 * @param checked Whether the row is checked.
 * @param onCheckedChange Called with the next value when the row is activated.
 * @param title Main copy of the row.
 * @param modifier Modifier applied to the row container.
 * @param subtitle Supporting copy below [title].
 * @param overline Small copy above [title].
 * @param value Inline value rendered at the end of the row.
 * @param enabled Whether the row accepts input and uses its enabled colors.
 * @param indeterminate Whether the checkbox shows the indeterminate mark instead of the check, and
 * reports the mixed state. Only meaningful while [checked].
 * @param highlightChecked Whether a checked row also wears the selected background and rail.
 * @param nestingLevel Indentation steps of [ListItemSize.nestedLevelOffset] to add.
 * @param placement Edge of the row the checkbox sits on.
 * @param style Colors of the row across its states.
 * @param size Metrics and typography of the row.
 * @param layout Slot layout of the row.
 * @param checkboxStyle Colors of the checkbox.
 * @param checkboxSize Metrics of the checkbox.
 * @param interactionSource Source of the row interactions; pass one to observe them from outside.
 * @param leading Slot before the text block, beside the checkbox when it leads.
 * @param trailing Slot after the text block, beside the checkbox when it trails.
 * @param bottom Content below the main row line, inside the row hit area.
 */
@Composable
public fun CheckboxListItem(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: (@Composable () -> Unit)? = null,
    overline: (@Composable () -> Unit)? = null,
    value: (@Composable () -> Unit)? = null,
    enabled: Boolean = true,
    indeterminate: Boolean = false,
    highlightChecked: Boolean = false,
    nestingLevel: Int = 0,
    placement: ListItemControlPlacement = ListItemControlPlacement.Leading,
    style: ListItemStyle = LocalListItemStyle.current ?: ListItemStyle.Default,
    size: ListItemSize = LocalListItemSize.current ?: ListItemSize.Default,
    layout: SlotRowLayout = LocalListItemLayout.current,
    checkboxStyle: CheckboxStyle = LocalCheckboxStyle.current ?: CheckboxStyle.Default,
    checkboxSize: CheckboxSize = LocalCheckboxSize.current ?: CheckboxSize.Default,
    interactionSource: MutableInteractionSource? = null,
    leading: (@Composable BoxScope.() -> Unit)? = null,
    trailing: (@Composable BoxScope.() -> Unit)? = null,
    bottom: (@Composable ColumnScope.() -> Unit)? = null,
) {
    val control: @Composable () -> Unit = {
        Checkbox(
            checked = checked,
            // The row handles every press; the checkbox only draws the state.
            onCheckedChange = null,
            indeterminate = indeterminate,
            // The row resolves its own enabled state, enclosing list included; follow it.
            enabled = LocalListItemState.current.enabled,
            size = checkboxSize,
            style = checkboxStyle,
        )
    }
    ControlListItem(
        control = control,
        selected = checked,
        action = ListItemAction.Toggle(
            state = checkboxToggleableState(checked = checked, indeterminate = indeterminate),
            role = Role.Checkbox,
            onToggle = { onCheckedChange(!checked) },
        ),
        title = title,
        modifier = modifier,
        subtitle = subtitle,
        overline = overline,
        value = value,
        enabled = enabled,
        highlightSelected = highlightChecked,
        nestingLevel = nestingLevel,
        placement = placement,
        style = style,
        size = size,
        layout = layout,
        interactionSource = interactionSource,
        leading = leading,
        trailing = trailing,
        bottom = bottom,
    )
}
