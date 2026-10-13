package dev.voir.areca.foundation.list

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import dev.voir.areca.foundation.selection.LocalSelectionControlInert
import dev.voir.areca.foundation.slot.SlotRowLayout

/**
 * The row shared by [CheckboxListItem], [RadioListItem] and [ToggleListItem].
 *
 * The three differ only in which control they put in the slot and in what the row reports — a
 * toggleable state or a selectable one. Everything else — combining the control with whatever the
 * caller put in the same slot, choosing which edge it sits on, and handing the row's own enabled
 * state down to it — is the same in all three, so it lives here rather than three times over.
 *
 * The row is the only interactive target. The control is drawn inside it in its display-only mode
 * (see [InertControl]): a press anywhere — the control included — reaches the row once, and
 * assistive technology meets one checkbox, radio button or switch rather than a row and a control
 * that each claim the same state.
 *
 * Internal because the three public wrappers are the API: each names its own control's parameters
 * — `checked`/`onCheckedChange`, `selected`/`onSelect`, `state`/`onToggle` — which is what makes
 * them read correctly at a call site. A single public row taking a generic control would not.
 *
 * @param control The selection control itself, composed inside the chosen slot and drawn
 * display-only.
 * @param selected Whether the row is in its selected state; drives the row's highlight and
 * [LocalListItemState].
 * @param action How the row answers activation; a [ListItemAction.Toggle] or a
 * [ListItemAction.Select].
 * @param title Main copy of the row.
 * @param modifier Modifier applied to the row container.
 * @param subtitle Supporting copy below [title].
 * @param overline Small copy above [title].
 * @param value Inline value rendered at the end of the row.
 * @param enabled Whether the row accepts input and uses its enabled colors.
 * @param highlightSelected Whether a selected row also wears the selected background and rail.
 * @param nestingLevel Indentation steps of [ListItemSize.nestedLevelOffset] to add.
 * @param placement Edge of the row the control sits on.
 * @param style Colors of the row across its states.
 * @param size Metrics and typography of the row.
 * @param layout Slot layout of the row.
 * @param interactionSource Source of the row interactions.
 * @param leading Slot before the text block, beside the control when it leads.
 * @param trailing Slot after the text block, beside the control when it trails.
 * @param bottom Content below the main row line, inside the row hit area.
 */
@Composable
internal fun ControlListItem(
    control: @Composable () -> Unit,
    selected: Boolean,
    action: ListItemAction,
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
    interactionSource: MutableInteractionSource? = null,
    leading: (@Composable BoxScope.() -> Unit)? = null,
    trailing: (@Composable BoxScope.() -> Unit)? = null,
    bottom: (@Composable ColumnScope.() -> Unit)? = null,
) {
    val leads = placement == ListItemControlPlacement.Leading
    val inertControl: @Composable () -> Unit = { InertControl(control) }

    ListItemLayout(
        modifier = modifier,
        title = title,
        subtitle = subtitle,
        overline = overline,
        value = value,
        enabled = enabled,
        selected = selected,
        highlightSelected = highlightSelected,
        dragging = false,
        nestingLevel = nestingLevel,
        style = style,
        size = size,
        layout = layout,
        interactionSource = interactionSource,
        action = action,
        leading = if (leads) {
            combinedControlSlot(inertControl, leading, size.leadingGap, controlFirst = true)
        } else {
            leading
        },
        trailing = if (leads) {
            trailing
        } else {
            combinedControlSlot(inertControl, trailing, size.valueGap, controlFirst = false)
        },
        bottom = bottom,
        content = null,
    )
}

/**
 * Draws a selection control as a picture of its state rather than as a second target.
 *
 * Puts the bare selection controls — [dev.voir.areca.foundation.selection.Checkbox],
 * [dev.voir.areca.foundation.selection.Radio] and [dev.voir.areca.foundation.selection.Toggle] — in
 * their display-only mode, whatever handler they were given: no click, no keyboard focus, no
 * semantics of their own and no reserved touch band. The row reports the state, and a press on the
 * control reaches the row like a press anywhere else. Rows that build the control themselves pass it
 * a `null` handler as well; this is what holds for a control handed in with one.
 *
 * @param control The selection control to draw.
 */
@Composable
internal fun InertControl(control: @Composable () -> Unit) {
    CompositionLocalProvider(LocalSelectionControlInert provides true, content = control)
}
