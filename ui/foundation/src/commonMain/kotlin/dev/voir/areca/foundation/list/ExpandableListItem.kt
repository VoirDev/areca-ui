package dev.voir.areca.foundation.list

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.collapse
import androidx.compose.ui.semantics.expand
import androidx.compose.ui.semantics.semantics
import dev.voir.areca.foundation.icon.ChevronDirection
import dev.voir.areca.foundation.layout.Expandable
import dev.voir.areca.foundation.layout.ExpandableTransition
import dev.voir.areca.foundation.slot.SlotRowLayout

/**
 * A row that reveals content underneath it, with a chevron that turns to point at the state it is
 * in.
 *
 * The revealed content sits outside the row's hit area, so a control inside it is clickable without
 * collapsing what it belongs to — unlike the row's own `bottom` slot, which is part of the row. Pass
 * a [nestingLevel] to the rows inside [content] to indent them under their parent.
 *
 * Expansion is hoisted: the caller owns [expanded] so it can be restored, driven from a view model,
 * or made exclusive across a group of rows.
 *
 * The row offers assistive technology an expand or a collapse action alongside its click, which is
 * how a screen reader announces whether it is expanded.
 *
 * @param expanded Whether the content is revealed.
 * @param onExpandedChange Called with the next value when the row is activated.
 * @param title Main copy of the row.
 * @param modifier Modifier applied to the row and its revealed content.
 * @param subtitle Supporting copy below [title].
 * @param overline Small copy above [title].
 * @param value Inline value rendered before the chevron.
 * @param enabled Whether the row accepts input and uses its enabled colors.
 * @param selected Whether the row is the selected one in its list.
 * @param nestingLevel Indentation steps of [ListItemSize.nestedLevelOffset] to add to the row
 * itself.
 * @param style Colors of the row across its states.
 * @param size Metrics and typography of the row.
 * @param layout Slot layout of the row.
 * @param interactionSource Source of the row interactions; pass one to observe them from outside.
 * @param onClickLabel Accessibility description of what the click does.
 * @param transition How the content opens and closes.
 * @param leading Slot before the text block, tinted with the `extra` colors.
 * @param content Content revealed below the row.
 */
@Composable
public fun ExpandableListItem(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: (@Composable () -> Unit)? = null,
    overline: (@Composable () -> Unit)? = null,
    value: (@Composable () -> Unit)? = null,
    enabled: Boolean = true,
    selected: Boolean = false,
    nestingLevel: Int = 0,
    style: ListItemStyle = LocalListItemStyle.current ?: ListItemStyle.Default,
    size: ListItemSize = LocalListItemSize.current ?: ListItemSize.Default,
    layout: SlotRowLayout = LocalListItemLayout.current,
    interactionSource: MutableInteractionSource? = null,
    onClickLabel: String? = null,
    transition: ExpandableTransition = ExpandableTransition.Default,
    leading: (@Composable BoxScope.() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val rowEnabled = enabled && LocalListEnabled.current

    Column(modifier = modifier) {
        ListItem(
            // The expand or collapse action is what assistive technology reads the state from:
            // a row offering "collapse" is expanded, one offering "expand" is collapsed.
            modifier = Modifier.semantics {
                if (!rowEnabled) return@semantics
                if (expanded) {
                    collapse {
                        onExpandedChange(false)
                        true
                    }
                } else {
                    expand {
                        onExpandedChange(true)
                        true
                    }
                }
            },
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
            onClick = { onExpandedChange(!expanded) },
            onClickLabel = onClickLabel,
            // The row governs whether its content is shown rather than navigating anywhere.
            role = Role.Button,
            leading = leading,
            trailing = {
                ListItemChevron(
                    direction = if (expanded) ChevronDirection.Up else ChevronDirection.Down,
                )
            },
        )

        Expandable(expanded = expanded, transition = transition) {
            Column(content = content)
        }
    }
}
