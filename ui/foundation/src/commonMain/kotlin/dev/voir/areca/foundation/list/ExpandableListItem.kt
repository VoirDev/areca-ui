package dev.voir.areca.foundation.list

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.collapse
import androidx.compose.ui.semantics.expand
import androidx.compose.ui.semantics.semantics
import dev.voir.areca.foundation.icon.ChevronDirection
import dev.voir.areca.foundation.layout.Expandable
import dev.voir.areca.foundation.layout.ExpandableTransition
import dev.voir.areca.foundation.slot.SlotRowLayout
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.tween
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment

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
 * @param trailing A control of the row's own before the chevron — a checkbox that selects the whole
 *   group while the row opens it. It takes its own input; a press elsewhere on the row expands it.
 * @param bottom Content below the row's text, inside the row and its hit area — a meter under a
 *   category's name — as opposed to [content], which is revealed under the row.
 * @param showChevron Whether the chevron is drawn at the row's end. Leave it on unless the row
 *   carries its own sign that it opens — a soft [container], a figure that is plainly a summary.
 * @param container Ground the row and its revealed content share while expanded.
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
    trailing: (@Composable BoxScope.() -> Unit)? = null,
    bottom: (@Composable ColumnScope.() -> Unit)? = null,
    showChevron: Boolean = true,
    container: ExpandableListItemContainer = ExpandableListItemContainer.None,
    content: @Composable ColumnScope.() -> Unit,
) {
    val rowEnabled = enabled && LocalListEnabled.current
    val containerShape = ArecaTheme.shapes.surface
    val containerColor by animateColorAsState(
        targetValue = if (expanded && container == ExpandableListItemContainer.Soft) {
            ArecaTheme.surface.raised
        } else {
            ArecaTheme.surface.raised.copy(alpha = 0f)
        },
        animationSpec = ArecaTheme.motion.medium.tween(ArecaTheme.motion.standard),
        label = "ExpandableListItemContainer",
    )

    Column(modifier = modifier.background(containerColor, containerShape)) {
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
            // On a soft block the row's own press and focus feedback takes the block's rounded shape,
            // rather than drawing square corners over it.
            style = if (container == ExpandableListItemContainer.Soft) style.copy(shape = containerShape) else style,
            size = size,
            layout = layout,
            interactionSource = interactionSource,
            onClick = { onExpandedChange(!expanded) },
            onClickLabel = onClickLabel,
            // The row governs whether its content is shown rather than navigating anywhere.
            role = Role.Button,
            leading = leading,
            bottom = bottom,
            trailing = if (showChevron || trailing != null) {
                {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(size.trailingGap),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        if (trailing != null) Box(contentAlignment = Alignment.Center, content = trailing)
                        if (showChevron) {
                            ListItemChevron(
                                direction = if (expanded) ChevronDirection.Up else ChevronDirection.Down,
                            )
                        }
                    }
                }
            } else {
                null
            },
        )

        Expandable(expanded = expanded, transition = transition) {
            Column(content = content)
        }
    }
}

/** The ground an [ExpandableListItem] and its revealed content share while expanded. */
public enum class ExpandableListItemContainer {
    /** None: the revealed content sits on whatever the row sits on. */
    None,

    /**
     * A block in the surface's [raised][dev.voir.areca.theme.ArecaSurface.raised] fill and the `surface` shape, behind the row and
     * its content, fading in as it opens, so the open group reads as one object. Give the row a
     * transparent style, such as the default plain one, or its own fill covers the block.
     */
    Soft,
}
