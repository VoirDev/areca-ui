package dev.voir.areca.foundation.field.select

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import dev.voir.areca.foundation.field.FieldRow
import dev.voir.areca.foundation.field.FieldRowScope
import dev.voir.areca.foundation.field.fieldChevronSpec
import dev.voir.areca.foundation.field.segmentWidth
import dev.voir.areca.foundation.icon.Chevron
import dev.voir.areca.foundation.icon.ChevronDirection
import dev.voir.areca.foundation.overlay.AnchoredPlacement
import dev.voir.areca.foundation.overlay.DropdownPopup
import dev.voir.areca.foundation.overlay.DropdownPopupSize
import dev.voir.areca.foundation.overlay.DropdownPopupStyle
import dev.voir.areca.foundation.overlay.LocalDropdownPopupStyle
import dev.voir.areca.foundation.overlay.anchored
import dev.voir.areca.foundation.overlay.rememberDropdownPopup
import dev.voir.areca.foundation.text.Text

/**
 * A [FieldRowScope.Segment] whose value is chosen from a dropdown anchored to it.
 *
 * The half of a composite field that is not typed into: a currency beside an amount, a unit beside
 * a measurement, a dialling code beside a phone number. It is [SelectField] with the container
 * taken away — the [FieldRow] draws that — so the two behave the same way and differ only in
 * what is around them.
 *
 * Unweighted by default, because that is the point of it: the segment is as wide as the code or
 * unit it shows, and the editor beside it takes the rest of the row. Give [width] a value when a
 * column of these would otherwise come out ragged, since `USD` and `€` are not the same width.
 *
 * Selection is controlled, exactly as in [SelectField]: [onChange] is called with the selection
 * that should apply next and nothing moves until your state comes back.
 *
 * @param Value Type of the values being selected.
 * @param values Currently selected values.
 * @param onChange Called with the full new selection whenever an option is acted on.
 * @param options Options to offer, in display order.
 * @param modifier Modifier applied to the segment.
 * @param width Fixed segment width, or [Dp.Unspecified] to size to the current selection.
 * @param weight Share of the row's leftover width. Zero — the default — sizes to the content.
 * @param selectionMode Whether one value or many can be held at once.
 * @param enabled Whether this segment opens. Ignored while the row is disabled.
 * @param loading Shows a spinner in place of the chevron and refuses to open, for a list still
 *   being fetched.
 * @param padding Inset between the segment's edges and its content. Defaults to the row's
 *   [FieldRowScope.contentPadding].
 * @param empty Whether the segment should show its [placeholder]. Defaults to having no selection.
 * @param placement Where the dropdown sits relative to the segment.
 * @param dropdownStyle Colors and elevation for the dropdown surface. Defaults to the theme's.
 * @param dropdownSize Sizing constraints for the dropdown surface. [DropdownPopupSize.Wide] by
 *   default: it matches the segment but never goes below a floor wider than [SelectField]'s,
 *   because a segment is usually far narrower than the options it offers, and matching it alone
 *   would give a useless strip.
 * @param labels What assistive technology reads for the dropdown's open and closed states. The
 *   segment announces itself as a dropdown list either way.
 * @param placeholder Shown when the segment is [empty].
 * @param leading Slot pinned to the segment's start edge.
 * @param trailing Replaces the chevron. Receives whether the dropdown is currently open.
 * @param valueContent Renders the current selection. Defaults to the selected labels, joined by
 *   commas under [SelectSelectionMode.Multiple].
 * @param content Renders the option list inside the dropdown.
 */
@Composable
public fun <Value> FieldRowScope.SelectSegment(
    values: Set<Value>,
    onChange: (Set<Value>) -> Unit,
    options: List<SelectOption<Value>>,
    modifier: Modifier = Modifier,
    width: Dp = Dp.Unspecified,
    weight: Float = 0f,
    selectionMode: SelectSelectionMode = SelectSelectionMode.Single,
    enabled: Boolean = true,
    loading: Boolean = false,
    padding: PaddingValues = contentPadding,
    empty: Boolean = values.isEmpty(),
    placement: AnchoredPlacement = AnchoredPlacement.BelowStart,
    dropdownStyle: DropdownPopupStyle = LocalDropdownPopupStyle.current ?: DropdownPopupStyle.Default,
    dropdownSize: DropdownPopupSize = DropdownPopupSize.Wide,
    labels: SelectFieldLabels = SelectFieldLabels.English,
    placeholder: (@Composable RowScope.() -> Unit)? = null,
    leading: (@Composable RowScope.() -> Unit)? = null,
    trailing: (@Composable RowScope.(expanded: Boolean) -> Unit)? = null,
    valueContent: (@Composable RowScope.(selected: List<SelectOption<Value>>) -> Unit)? = null,
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
    val dropdown = rememberDropdownPopup(placement = placement)

    val canOpen = this.enabled && enabled && !readOnly && !loading

    // A list that starts loading, or a row that turns read-only, takes the dropdown away with it
    // rather than leaving an open menu over a control that will no longer answer it.
    LaunchedEffect(canOpen) {
        if (!canOpen) dropdown.dismiss()
    }

    // Bound here: inside the slots the row's own scope is out of reach by an implicit receiver.
    val chevronSize = size.chevron

    val selected = remember(options, values) {
        options.filter { values.contains(it.value) }
    }

    Segment(
        modifier = modifier
            .segmentWidth(width)
            .anchored(dropdown.anchorState)
            .selectTriggerSemantics(
                expanded = dropdown.isOpen,
                canOpen = canOpen,
                labels = labels,
                onExpand = { dropdown.show() },
                onCollapse = { dropdown.dismiss() },
            ),
        weight = weight,
        enabled = enabled,
        onClick = if (canOpen) ({ dropdown.show() }) else null,
        padding = padding,
        loading = loading,
        empty = empty,
        leading = leading,
        trailing = {
            if (trailing != null) {
                trailing(dropdown.isOpen)
            } else {
                Chevron(
                    direction = if (dropdown.isOpen) ChevronDirection.Up else ChevronDirection.Down,
                    size = chevronSize,
                    animationSpec = fieldChevronSpec(),
                )
            }
        },
        placeholder = placeholder,
    ) {
        if (selected.isNotEmpty()) {
            if (valueContent != null) {
                valueContent(selected)
            } else {
                val text = when (selectionMode) {
                    SelectSelectionMode.Single -> selected.firstOrNull()?.label.orEmpty()
                    SelectSelectionMode.Multiple -> selected.joinToString { it.label }
                }
                // One line, clipped: a segment given a fixed width narrower than its longest
                // label would otherwise wrap, growing the row and taking the divider with it.
                if (text.isNotBlank()) {
                    Text(text, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
        }

        // The popup lives in a window of its own and measures to nothing here, so emitting it
        // inside the segment costs the row no width and keeps the anchor beside its trigger.
        DropdownPopup(
            state = dropdown,
            onDismissRequest = { dropdown.dismiss() },
            size = dropdownSize,
            style = dropdownStyle,
        ) {
            content(options, values) { option ->
                onChange(selectionMode.applyTo(values, option))

                if (selectionMode == SelectSelectionMode.Single) {
                    dropdown.dismiss()
                }
            }
        }
    }
}
