package dev.voir.areca.foundation.adaptive

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.LocalContentColor
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.foundation.layout.HorizontalDivider
import dev.voir.areca.foundation.list.ListItem
import dev.voir.areca.foundation.overlay.DropdownPopup
import dev.voir.areca.foundation.overlay.DropdownPopupSize
import dev.voir.areca.foundation.overlay.DropdownPopupState
import dev.voir.areca.foundation.overlay.DropdownPopupStyle
import dev.voir.areca.foundation.overlay.DropdownWidth
import dev.voir.areca.foundation.overlay.FlowModal
import dev.voir.areca.foundation.overlay.rememberFlowModal
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.theme.ArecaTheme

/**
 * One action in an [AdaptiveMenu].
 *
 * @property label What the action does, as the person reads it: "Duplicate", "Move to wallet".
 * @property icon Glyph drawn before the label, or `null` for none.
 * @property destructive Whether the action deletes or discards something; drawn in the danger colour.
 * @property enabled Whether the action can be chosen.
 * @property onClick Called when the action is chosen, after the menu closes.
 */
@Immutable
public class AdaptiveMenuItem(
    public val label: String,
    public val icon: ImageVector? = null,
    public val destructive: Boolean = false,
    public val enabled: Boolean = true,
    public val onClick: () -> Unit,
)

/**
 * A list of actions for one thing — a row, a card — in the form the app chose.
 *
 * [LocalAdaptiveMenuPresentation] picks the form, the same on every window:
 * [AdaptiveMenuPresentation.Dropdown], the default, is a [DropdownPopup] beside the trigger;
 * [AdaptiveMenuPresentation.Sheet] is a sheet from the bottom edge with [title] at its head.
 *
 * Anchor it as a dropdown is anchored: put `Modifier.anchored(state.anchorState)` on the trigger and
 * call `state.show()` from it. Choosing an action closes the menu, then runs the action.
 *
 * ```kotlin
 * val menu = rememberDropdownPopup(placement = AnchoredPlacement.BelowEnd)
 * IconButton(
 *     imageVector = ArecaIcons.DotsVertical,
 *     contentDescription = "Actions for Corner Market",
 *     onClick = menu::show,
 *     modifier = Modifier.anchored(menu.anchorState),
 * )
 * AdaptiveMenu(
 *     state = menu,
 *     title = "Corner Market",
 *     actions = listOf(
 *         AdaptiveMenuItem("Duplicate", onClick = duplicate),
 *         AdaptiveMenuItem("Delete", destructive = true, onClick = delete),
 *     ),
 * )
 * ```
 *
 * @param state Whether the menu is open, and the trigger it is anchored to.
 * @param actions The actions, in the order they are listed.
 * @param modifier Modifier applied to the list of actions.
 * This form lists the actions as given, each glyph before its label, at the dropdown's own width.
 * The overload that takes `groups` adds groups, a destructive group drawn last, a choice of form,
 * and [AdaptiveMenuStyle] and [AdaptiveMenuSize].
 *
 * @param title What the actions act on, shown at the head of the sheet. The dropdown sits beside its
 *   trigger and shows none.
 */
@Composable
public fun AdaptiveMenu(
    state: DropdownPopupState,
    actions: List<AdaptiveMenuItem>,
    modifier: Modifier = Modifier,
    title: String? = null,
) {
    if (LocalAdaptiveMenuPresentation.current == AdaptiveMenuPresentation.Dropdown) {
        DropdownPopup(state = state, onDismissRequest = state::dismiss) {
            MenuActions(
                actions = actions,
                onChosen = state::dismiss,
                modifier = modifier.widthIn(min = 200.dp, max = 320.dp),
            )
        }
    } else {
        val controller = rememberFlowModal()
        LaunchedEffect(state.isOpen) {
            if (state.isOpen) controller.show(partial = false) else controller.hide()
        }
        FlowModal(
            controller = controller,
            onDismissRequest = state::dismiss,
            header = title?.let { text ->
                {
                    Text(
                        text = text,
                        variant = TextVariant.BodySmall,
                        color = TextColor.Muted,
                        maxLines = 1,
                        modifier = Modifier.padding(
                            start = ArecaTheme.spacing.xl,
                            end = ArecaTheme.spacing.xl,
                            top = ArecaTheme.spacing.xl3,
                        ),
                    )
                }
            },
        ) {
            MenuActions(
                actions = actions,
                onChosen = state::dismiss,
                modifier = modifier.fillMaxWidth().padding(bottom = ArecaTheme.spacing.xl),
            )
        }
    }
}

/**
 * The actions for one thing — a row, a card, a details sheet — in groups, as a card hanging from
 * its trigger or a sheet from the bottom edge, as the app chose.
 *
 * [presentation] picks the form, the same on every window and under every input.
 * [AdaptiveMenuPresentation.Dropdown] — the default unless [LocalAdaptiveMenuPresentation] says
 * otherwise — is a card under the trigger: the screen stays in view around it, and it grows in from
 * the corner that touches the trigger. [AdaptiveMenuPresentation.Sheet] is a sheet from the bottom
 * edge with [title] at its head.
 *
 * Groups are drawn in order with one full-width hairline between them and none inside a group.
 * Destructive actions are taken out of every group and drawn last, as a group of their own in the
 * danger ink, so the action that cannot be taken back stands apart; a group left empty is dropped.
 * Each row is its label and its glyph, at the end of the row or before the label as
 * [AdaptiveMenuSize.iconPosition] says, a control's height to press. Choosing an action closes the
 * menu, then runs the action.
 *
 * Anchor it as a dropdown is anchored: `Modifier.anchored(state.anchorState)` on the trigger and
 * `state.show()` from it, or `Modifier.contextMenu(state)` on a row. Compose it unconditionally; it
 * animates out.
 *
 * ```kotlin
 * val menu = rememberDropdownPopup(placement = AnchoredPlacement.BelowEnd)
 * IconButton(
 *     imageVector = ArecaIcons.DotsHorizontal,
 *     contentDescription = "Actions for Rent",
 *     onClick = menu::show,
 *     modifier = Modifier.anchored(menu.anchorState),
 * )
 * AdaptiveMenu(
 *     state = menu,
 *     presentation = AdaptiveMenuPresentation.Dropdown,
 *     groups = listOf(
 *         listOf(
 *             AdaptiveMenuItem("Edit", icon = ArecaIcons.Edit, onClick = edit),
 *             AdaptiveMenuItem("End", icon = ArecaIcons.StopCircle, onClick = end),
 *         ),
 *         listOf(AdaptiveMenuItem("Delete", icon = ArecaIcons.Trash, destructive = true, onClick = delete)),
 *     ),
 * )
 * ```
 *
 * @param state Whether the menu is open, and the trigger it is anchored to.
 * @param groups The actions in groups, each group and each action in the order drawn; a menu of one
 *   group is `listOf(actions)`. Destructive actions move to a last group whatever group they are in.
 * @param modifier Modifier applied to the list of actions.
 * @param title What the actions act on, shown at the head of the sheet. The card shows none.
 * @param presentation The form: a card at the trigger or a sheet. Defaults to
 *   [LocalAdaptiveMenuPresentation].
 * @param style Card colours, shape and lift, and row looks. Defaults to [LocalAdaptiveMenuStyle],
 *   then [AdaptiveMenuStyle.Default].
 * @param size Card width and gap, glyph position and row metrics. Defaults to
 *   [LocalAdaptiveMenuSize], then [AdaptiveMenuSize.Default].
 */
@Composable
public fun AdaptiveMenu(
    state: DropdownPopupState,
    groups: List<List<AdaptiveMenuItem>>,
    modifier: Modifier = Modifier,
    title: String? = null,
    presentation: AdaptiveMenuPresentation = LocalAdaptiveMenuPresentation.current,
    style: AdaptiveMenuStyle = LocalAdaptiveMenuStyle.current ?: AdaptiveMenuStyle.Default,
    size: AdaptiveMenuSize = LocalAdaptiveMenuSize.current ?: AdaptiveMenuSize.Default,
) {
    val drawn = menuGroups(groups)
    if (presentation == AdaptiveMenuPresentation.Dropdown) {
        DropdownPopup(
            state = state,
            onDismissRequest = state::dismiss,
            style = DropdownPopupStyle(
                bg = style.bg,
                border = style.border,
                shape = style.shape,
                shadowElevation = style.shadowElevation,
            ),
            size = DropdownPopupSize.default(
                offsetY = size.offsetY,
                width = DropdownWidth.Fixed(size.width),
                maxHeight = size.maxHeight,
                borderWidth = size.borderWidth,
            ),
        ) {
            GroupedMenuActions(
                groups = drawn,
                onChosen = state::dismiss,
                style = style,
                size = size,
                modifier = modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
            )
        }
    } else {
        val controller = rememberFlowModal()
        LaunchedEffect(state.isOpen) {
            if (state.isOpen) controller.show(partial = false) else controller.hide()
        }
        FlowModal(
            controller = controller,
            onDismissRequest = state::dismiss,
            header = title?.let { text ->
                {
                    Text(
                        text = text,
                        variant = TextVariant.BodySmall,
                        color = TextColor.Muted,
                        maxLines = 1,
                        modifier = Modifier.padding(
                            start = ArecaTheme.spacing.xl,
                            end = ArecaTheme.spacing.xl,
                            top = ArecaTheme.spacing.xl3,
                        ),
                    )
                }
            },
        ) {
            GroupedMenuActions(
                groups = drawn,
                onChosen = state::dismiss,
                style = style,
                size = size,
                modifier = modifier.fillMaxWidth().padding(bottom = ArecaTheme.spacing.xl),
            )
        }
    }
}

/**
 * The groups an [AdaptiveMenu] draws: [groups] without their destructive actions, then those
 * actions as one last group. Groups left empty are dropped, so no hairline stands alone.
 *
 * @param groups The groups as the caller gave them.
 * @return The groups in the order drawn, none empty.
 */
internal fun menuGroups(groups: List<List<AdaptiveMenuItem>>): List<List<AdaptiveMenuItem>> {
    val destructive = groups.flatten().filter(AdaptiveMenuItem::destructive)
    val others = groups.map { group -> group.filterNot(AdaptiveMenuItem::destructive) }
    return (others + listOf(destructive)).filter(List<AdaptiveMenuItem>::isNotEmpty)
}

/**
 * The groups as rows, a full-width hairline between groups, closing the menu before each action
 * runs.
 *
 * @param groups The groups in the order drawn, none empty.
 * @param onChosen Closes the menu.
 * @param style Row looks.
 * @param size Row metrics and glyph position.
 * @param modifier Modifier applied to the column of rows.
 */
@Composable
private fun GroupedMenuActions(
    groups: List<List<AdaptiveMenuItem>>,
    onChosen: () -> Unit,
    style: AdaptiveMenuStyle,
    size: AdaptiveMenuSize,
    modifier: Modifier,
) {
    Column(modifier = modifier) {
        groups.forEachIndexed { index, group ->
            if (index > 0) HorizontalDivider()
            for (action in group) {
                MenuRow(action = action, onChosen = onChosen, style = style, size = size)
            }
        }
    }
}

/**
 * One action: its label, and its glyph at the end the size names; inked by its row style.
 *
 * @param action The action.
 * @param onChosen Closes the menu; called before the action runs.
 * @param style Row looks; a destructive action takes [AdaptiveMenuStyle.destructiveItem].
 * @param size Row metrics and glyph position.
 */
@Composable
private fun MenuRow(
    action: AdaptiveMenuItem,
    onChosen: () -> Unit,
    style: AdaptiveMenuStyle,
    size: AdaptiveMenuSize,
) {
    val glyph: (@Composable BoxScope.() -> Unit)? =
        action.icon?.let { icon -> { Icon(imageVector = icon, contentDescription = null) } }
    val trailing = size.iconPosition == AdaptiveMenuIconPosition.Trailing
    ListItem(
        title = { Text(text = action.label) },
        enabled = action.enabled,
        style = if (action.destructive) style.destructiveItem else style.item,
        size = size.item,
        onClick = {
            onChosen()
            action.onClick()
        },
        leading = if (trailing) null else glyph,
        trailing = if (trailing) glyph else null,
    )
}

/**
 * The actions as rows, closing the menu before each runs.
 *
 * @param actions The actions.
 * @param onChosen Closes the menu.
 * @param modifier Modifier applied to the column of rows.
 */
@Composable
private fun MenuActions(
    actions: List<AdaptiveMenuItem>,
    onChosen: () -> Unit,
    modifier: Modifier,
) {
    Column(modifier = modifier) {
        for (action in actions) {
            // A destructive action takes the danger ink; the others keep the row's own.
            val danger = ArecaTheme.colors.fgDanger
            ListItem(
                title = { Text(text = action.label, color = if (action.destructive) danger else LocalContentColor.current) },
                enabled = action.enabled,
                onClick = {
                    onChosen()
                    action.onClick()
                },
                leading = action.icon?.let { icon ->
                    {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = if (action.destructive) danger else LocalContentColor.current,
                        )
                    }
                },
            )
        }
    }
}
