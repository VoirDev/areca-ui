package dev.voir.areca.foundation.list

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import dev.voir.areca.foundation.selection.LocalRadioSize
import dev.voir.areca.foundation.selection.LocalRadioStyle
import dev.voir.areca.foundation.selection.Radio
import dev.voir.areca.foundation.selection.RadioSize
import dev.voir.areca.foundation.selection.RadioStyle
import dev.voir.areca.foundation.slot.SlotRowLayout

/**
 * A row of a single-choice list, whose whole surface picks it.
 *
 * Like [CheckboxListItem], the row is the one target and the radio button inside it is a picture of
 * the state; the row is announced as a radio button carrying its copy as the label. Unlike a
 * checkbox row it cannot be un-picked by pressing it again: in a single-choice group, pressing the
 * chosen row is a no-op rather than a way to end up with nothing selected. Clearing the choice is
 * the group's business, not the row's.
 *
 * Put the rows of one choice in a container with `Modifier.selectableGroup()`, so assistive
 * technology announces them as one group.
 *
 * @param selected Whether this row is the chosen one.
 * @param onSelect Called when the row is activated while not already selected.
 * @param title Main copy of the row.
 * @param modifier Modifier applied to the row container.
 * @param subtitle Supporting copy below [title].
 * @param overline Small copy above [title].
 * @param value Inline value rendered at the end of the row.
 * @param enabled Whether the row accepts input and uses its enabled colors.
 * @param highlightSelected Whether the chosen row also wears the selected background and rail.
 * @param nestingLevel Indentation steps of [ListItemSize.nestedLevelOffset] to add.
 * @param placement Edge of the row the control sits on.
 * @param style Colors of the row across its states.
 * @param size Metrics and typography of the row.
 * @param layout Slot layout of the row.
 * @param radioStyle Colors of the control.
 * @param radioSize Metrics of the control.
 * @param interactionSource Source of the row interactions; pass one to observe them from outside.
 * @param leading Slot before the text block, beside the control when it leads.
 * @param trailing Slot after the text block, beside the control when it trails.
 * @param bottom Content below the main row line, inside the row hit area.
 */
@Composable
public fun RadioListItem(
    selected: Boolean,
    onSelect: () -> Unit,
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: (@Composable () -> Unit)? = null,
    overline: (@Composable () -> Unit)? = null,
    value: (@Composable () -> Unit)? = null,
    enabled: Boolean = true,
    highlightSelected: Boolean = false,
    nestingLevel: Int = 0,
    placement: ListItemControlPlacement = ListItemControlPlacement.Leading,
    style: ListItemStyle = LocalListItemStyle.current ?: ListItemStyle.Default,
    size: ListItemSize = LocalListItemSize.current ?: ListItemSize.Default,
    layout: SlotRowLayout = LocalListItemLayout.current,
    radioStyle: RadioStyle = LocalRadioStyle.current ?: RadioStyle.Default,
    radioSize: RadioSize = LocalRadioSize.current ?: RadioSize.Default,
    interactionSource: MutableInteractionSource? = null,
    leading: (@Composable BoxScope.() -> Unit)? = null,
    trailing: (@Composable BoxScope.() -> Unit)? = null,
    bottom: (@Composable ColumnScope.() -> Unit)? = null,
) {
    val control: @Composable () -> Unit = {
        Radio(
            checked = selected,
            // The row handles every press; the radio button only draws the state.
            onClick = null,
            // The row resolves its own enabled state, enclosing list included; follow it.
            enabled = LocalListItemState.current.enabled,
            size = radioSize,
            style = radioStyle,
        )
    }
    ControlListItem(
        control = control,
        selected = selected,
        action = ListItemAction.Select(
            selected = selected,
            role = Role.RadioButton,
            onSelect = { if (!selected) onSelect() },
        ),
        title = title,
        modifier = modifier,
        subtitle = subtitle,
        overline = overline,
        value = value,
        enabled = enabled,
        highlightSelected = highlightSelected,
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
