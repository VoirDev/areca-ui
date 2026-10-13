package dev.voir.areca.foundation.list

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.voir.areca.foundation.icon.ChevronDirection
import dev.voir.areca.foundation.slot.SlotRowLayout

/**
 * A row that leads somewhere else — a detail screen, a submenu, a nested list.
 *
 * The trailing chevron leans toward the pointer while the row is reached for, so the row as a whole
 * answers the hover rather than only its background. [value] states the row's current setting before
 * the chevron, which is the shape of most drill-in rows: what it is now, then the way to change it.
 *
 * @param onClick Called when the row is activated.
 * @param title Main copy of the row.
 * @param modifier Modifier applied to the row container.
 * @param subtitle Supporting copy below [title].
 * @param overline Small copy above [title].
 * @param value Inline value rendered before the chevron.
 * @param enabled Whether the row accepts input and uses its enabled colors.
 * @param selected Whether the row is the selected one in its list.
 * @param nestingLevel Indentation steps of [ListItemSize.nestedLevelOffset] to add.
 * @param direction Direction the chevron points toward, in reading order: [ChevronDirection.Right]
 * points at the end edge and mirrors with the chevron in a right-to-left layout, lean included. Use
 * [ChevronDirection.Left] for a row that goes back.
 * @param style Colors of the row across its states.
 * @param size Metrics and typography of the row.
 * @param layout Slot layout of the row.
 * @param interactionSource Source of the row interactions; pass one to observe them from outside.
 * @param onClickLabel Accessibility description of what the click does.
 * @param leading Slot before the text block, tinted with the `extra` colors.
 * @param bottom Content below the main row line, inside the row hit area.
 */
@Composable
public fun NavigationListItem(
    onClick: () -> Unit,
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: (@Composable () -> Unit)? = null,
    overline: (@Composable () -> Unit)? = null,
    value: (@Composable () -> Unit)? = null,
    enabled: Boolean = true,
    selected: Boolean = false,
    nestingLevel: Int = 0,
    direction: ChevronDirection = ChevronDirection.Right,
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
        nestingLevel = nestingLevel,
        style = style,
        size = size,
        layout = layout,
        interactionSource = interactionSource,
        onClick = onClick,
        onClickLabel = onClickLabel,
        leading = leading,
        bottom = bottom,
        trailing = { ListItemChevron(direction = direction) },
    )
}
