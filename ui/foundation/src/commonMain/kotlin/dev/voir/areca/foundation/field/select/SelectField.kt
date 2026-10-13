package dev.voir.areca.foundation.field.select

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import dev.voir.areca.foundation.field.Field
import dev.voir.areca.foundation.field.FieldDefaults
import dev.voir.areca.foundation.field.FieldScope
import dev.voir.areca.foundation.field.FieldSize
import dev.voir.areca.foundation.field.FieldStatus
import dev.voir.areca.foundation.field.FieldStyle
import dev.voir.areca.foundation.field.fieldChevronSpec
import dev.voir.areca.foundation.icon.Chevron
import dev.voir.areca.foundation.icon.ChevronDirection
import dev.voir.areca.foundation.overlay.AnchoredPlacement
import dev.voir.areca.foundation.overlay.DropdownPopup
import dev.voir.areca.foundation.overlay.DropdownPopupSize
import dev.voir.areca.foundation.overlay.DropdownPopupStyle
import dev.voir.areca.foundation.overlay.LocalDropdownPopupSize
import dev.voir.areca.foundation.overlay.LocalDropdownPopupStyle
import dev.voir.areca.foundation.overlay.anchored
import dev.voir.areca.foundation.overlay.rememberDropdownPopup
import dev.voir.areca.foundation.text.Text

/**
 * A [Field] whose value is chosen from a list in a dropdown anchored to it.
 *
 * The whole container is the trigger — there is nothing to type here, so a small chevron target
 * would only be a smaller version of the same tap. Under [SelectSelectionMode.Single] the
 * dropdown closes as soon as a choice is made; under [SelectSelectionMode.Multiple] it stays
 * open, because the next tap is usually another option.
 *
 * Selection is controlled: [onChange] is called with the values that should be selected next and
 * nothing moves until your state comes back. Use [SelectFlowModal] instead when the list is long
 * enough to want a sheet, a search box, or an apply button.
 *
 * @param Value Type of the values being selected.
 * @param values Currently selected values.
 * @param onChange Called with the full new selection whenever an option is acted on.
 * @param options Options to offer, in display order.
 * @param modifier Modifier applied to the field container.
 * @param selectionMode Whether one value or many can be held at once.
 * @param style Color palette and container shape. See [FieldStyle].
 * @param size Typography, paddings and vertical metrics.
 * @param field Scope from an enclosing [FieldGroup]: the status to report, and a label to
 *   draw inside the container when the group placed it there. Forward it and an internal
 *   label placement takes effect; leave it out and the group's label has nowhere to go.
 * @param status Semantic state — Default, Error, Success, Warning. Comes from [field]
 *   unless it is given explicitly.
 * @param enabled When false, non-interactive and rendered as disabled. Comes from [field] unless
 *   it is given explicitly.
 * @param readOnly Shows the selection but does not open the dropdown.
 * @param loading Shows a loading indicator in place of the chevron and refuses to open, for a
 *   list that is still being fetched. Turning on [loading] or [readOnly], or turning off
 *   [enabled], closes a dropdown that is already open.
 * @param empty Whether the field should show its [placeholder]. Defaults to having no selection.
 * @param placement Where the dropdown sits relative to the field.
 * @param dropdownStyle Colors and elevation for the dropdown surface. Defaults to the theme's.
 * @param dropdownSize Sizing constraints for the dropdown surface. Defaults to the theme's.
 * @param labels What assistive technology reads for the dropdown's open and closed states. The
 *   field announces itself as a dropdown list either way.
 * @param placeholder Shown when the field is [empty].
 * @param leading Optional leading slot pinned to the start edge.
 * @param trailing Replaces the chevron. Receives whether the dropdown is currently open.
 * @param valueContent Renders the current selection inside the field. Defaults to the selected
 *   labels, joined by commas under [SelectSelectionMode.Multiple].
 * @param content Renders the option list inside the dropdown. Receives the options, the current
 *   selection, and a callback to invoke with the option the user acted on — the callback works
 *   out the resulting selection, reports it and closes the dropdown when appropriate.
 */
@Composable
public fun <Value> SelectField(
    values: Set<Value>,
    onChange: (Set<Value>) -> Unit,
    options: List<SelectOption<Value>>,
    modifier: Modifier = Modifier,
    selectionMode: SelectSelectionMode = SelectSelectionMode.Single,
    style: FieldStyle = FieldDefaults.style,
    size: FieldSize = FieldDefaults.size,
    field: FieldScope? = null,
    status: FieldStatus = field?.status ?: FieldStatus.Default,
    enabled: Boolean = field?.enabled ?: true,
    readOnly: Boolean = false,
    loading: Boolean = false,
    empty: Boolean = values.isEmpty(),
    placement: AnchoredPlacement = AnchoredPlacement.BelowStart,
    dropdownStyle: DropdownPopupStyle = LocalDropdownPopupStyle.current ?: DropdownPopupStyle.Default,
    dropdownSize: DropdownPopupSize = LocalDropdownPopupSize.current ?: DropdownPopupSize.Default,
    labels: SelectFieldLabels = SelectFieldLabels.English,
    placeholder: (@Composable RowScope.() -> Unit)? = null,
    leading: (@Composable RowScope.() -> Unit)? = null,
    trailing: (@Composable RowScope.(expanded: Boolean) -> Unit)? = null,
    valueContent: (@Composable RowScope.(selectedEntries: List<SelectOption<Value>>) -> Unit)? = null,
    content: @Composable (
        options: List<SelectOption<Value>>,
        values: Set<Value>,
        onSelect: (SelectOption<Value>) -> Unit,
    ) -> Unit = { dropdownOptions, selectedValues, onSelect ->
        SelectFlowModalDefaults.List(
            options = dropdownOptions,
            values = selectedValues,
            onSelect = onSelect,
        )
    },
) {
    val dropdownPopup = rememberDropdownPopup(placement = placement)

    val canInteract = enabled && !readOnly && !loading

    // A list that starts loading, or a field that turns read-only or disabled, takes the dropdown
    // away with it rather than leaving an open menu over a control that will no longer answer it.
    LaunchedEffect(canInteract) {
        if (!canInteract) dropdownPopup.dismiss()
    }

    val selectedOptions = remember(options, values) {
        options.filter { values.contains(it.value) }
    }

    Box(modifier = modifier) {
        Field(
            modifier = Modifier
                .fillMaxWidth()
                .anchored(dropdownPopup.anchorState)
                .selectTriggerSemantics(
                    expanded = dropdownPopup.isOpen,
                    canOpen = canInteract,
                    labels = labels,
                    onExpand = { dropdownPopup.show() },
                    onCollapse = { dropdownPopup.dismiss() },
                ),
            style = style,
            size = size,
            field = field,
            status = status,
            enabled = enabled,
            readOnly = readOnly,
            loading = loading,
            empty = empty,
            onClick = if (canInteract) ({ dropdownPopup.show() }) else null,
            // The container is the control in every state, so a select that cannot open right now
            // still carries the group's name and error.
            ownsControl = true,
            leading = leading,
            trailing = {
                if (trailing != null) {
                    trailing(dropdownPopup.isOpen)
                } else {
                    Chevron(
                        direction = if (dropdownPopup.isOpen) {
                            ChevronDirection.Up
                        } else {
                            ChevronDirection.Down
                        },
                        size = size.chevron,
                        animationSpec = fieldChevronSpec(),
                    )
                }
            },
            placeholder = placeholder,
        ) {
            if (selectedOptions.isNotEmpty()) {
                if (valueContent != null) {
                    valueContent(selectedOptions)
                } else {
                    DefaultSelectFieldValueContent(
                        selectedOptions = selectedOptions,
                        selectionMode = selectionMode,
                    )
                }
            }
        }

        DropdownPopup(
            state = dropdownPopup,
            onDismissRequest = {
                dropdownPopup.dismiss()
            },
            modifier = Modifier,
            size = dropdownSize,
            style = dropdownStyle,
        ) {
            content(options, values) { option ->
                onChange(selectionMode.applyTo(values, option))

                if (selectionMode == SelectSelectionMode.Single) {
                    dropdownPopup.dismiss()
                }
            }
        }
    }
}

/**
 * Renders the current selection as text inside the field.
 *
 * @param T Type of the values being selected.
 * @param selectedOptions Options matching the current selection, in option order.
 * @param selectionMode Whether one value or many can be held at once.
 */
@Composable
private fun <T> RowScope.DefaultSelectFieldValueContent(
    selectedOptions: List<SelectOption<T>>,
    selectionMode: SelectSelectionMode,
) {
    val text = when (selectionMode) {
        SelectSelectionMode.Single -> selectedOptions.firstOrNull()?.label.orEmpty()
        SelectSelectionMode.Multiple -> selectedOptions.joinToString { it.label }
    }

    if (text.isNotBlank()) {
        // One line, clipped. A selection long enough to wrap would grow the field past the height
        // its size promises, which in a column of fields reads as one of them having broken.
        Text(text, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}
