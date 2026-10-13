package dev.voir.areca.foundation.list

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.semantics.Role
import dev.voir.areca.foundation.LocalContentColor
import dev.voir.areca.foundation.slot.SlotRowLayout

/**
 * A row that marks itself with a check when chosen — the shape of a select sheet, a filter list or a
 * menu that remembers its current entry.
 *
 * The check mark keeps its footprint whether or not it is showing, so the rows stay aligned as the
 * choice moves between them, and it draws itself in rather than appearing all at once. The row is
 * not filled while chosen by default: the mark already says which one it is, and a filled row inside
 * a floating menu competes with the hover state.
 *
 * Unlike [RadioListItem] this row reports every activation, chosen or not, so a caller can implement
 * either single or multiple selection — which is why it takes `onClick` rather than `onSelect`.
 *
 * @param selected Whether the row is chosen.
 * @param onClick Called when the row is activated, including when it is already chosen.
 * @param title Main copy of the row.
 * @param modifier Modifier applied to the row container.
 * @param subtitle Supporting copy below [title].
 * @param overline Small copy above [title].
 * @param value Inline value rendered before the check mark.
 * @param enabled Whether the row accepts input and uses its enabled colors.
 * @param highlightSelected Whether a chosen row also wears the selected background and rail.
 * @param checkmarkColor Colour of the check mark — the brand's, say, so the choice reads as the
 *   product's answer. [Color.Unspecified] takes the row's trailing-slot colour.
 * @param nestingLevel Indentation steps of [ListItemSize.nestedLevelOffset] to add.
 * @param style Colors of the row across its states.
 * @param size Metrics and typography of the row.
 * @param layout Slot layout of the row.
 * @param interactionSource Source of the row interactions; pass one to observe them from outside.
 * @param onClickLabel Accessibility description of what the click does.
 * @param leading Slot before the text block, tinted with the `extra` colors.
 * @param bottom Content below the main row line, inside the row hit area.
 */
@Composable
public fun CheckmarkListItem(
    selected: Boolean,
    onClick: () -> Unit,
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: (@Composable () -> Unit)? = null,
    overline: (@Composable () -> Unit)? = null,
    value: (@Composable () -> Unit)? = null,
    enabled: Boolean = true,
    highlightSelected: Boolean = false,
    checkmarkColor: Color = Color.Unspecified,
    nestingLevel: Int = 0,
    style: ListItemStyle = LocalListItemStyle.current ?: ListItemStyle.Default,
    size: ListItemSize = LocalListItemSize.current ?: ListItemSize.Default,
    layout: SlotRowLayout = LocalListItemLayout.current,
    interactionSource: MutableInteractionSource? = null,
    onClickLabel: String? = null,
    leading: (@Composable BoxScope.() -> Unit)? = null,
    bottom: (@Composable ColumnScope.() -> Unit)? = null,
) {
    ListItem(
        modifier = modifier,
        title = title,
        subtitle = subtitle,
        overline = overline,
        value = value,
        enabled = enabled,
        selected = selected,
        highlightSelected = highlightSelected,
        nestingLevel = nestingLevel,
        style = style,
        size = size,
        layout = layout,
        interactionSource = interactionSource,
        onClick = onClick,
        onClickLabel = onClickLabel,
        // A button that reports its selected state, rather than a checkbox or a radio button: the
        // row cannot know whether the caller allows one choice or several.
        role = Role.Button,
        leading = leading,
        bottom = bottom,
        // The mark reads the row's own selection, which is what animates it in and out.
        trailing = {
            ListItemCheckmark(color = checkmarkColor.takeOrElse { LocalContentColor.current })
        },
    )
}
