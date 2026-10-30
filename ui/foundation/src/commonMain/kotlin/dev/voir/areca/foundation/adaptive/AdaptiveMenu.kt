package dev.voir.areca.foundation.adaptive

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.LocalContentColor
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.foundation.list.ListItem
import dev.voir.areca.foundation.overlay.DropdownPopup
import dev.voir.areca.foundation.overlay.DropdownPopupState
import dev.voir.areca.foundation.overlay.FlowModal
import dev.voir.areca.foundation.overlay.rememberFlowModal
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.window.InputMode
import dev.voir.areca.window.WidthClass

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
 * A list of actions for one thing — a row, a card — in the form the window calls for.
 *
 * Under a pointer, or on any window from expanded up, it is a [DropdownPopup] beside the trigger.
 * On a phone or a tablet in portrait under touch it is a sheet from the bottom edge with [title] at
 * its head, where a finger reaches it. The form follows the window, not the pane that opened it.
 *
 * Anchor it as a dropdown is anchored: put `Modifier.anchored(state.anchorState)` on the trigger and
 * call `state.show()` from it. Choosing an action closes the menu, then runs the action.
 *
 * ```kotlin
 * val menu = rememberDropdownPopup(placement = AnchoredPlacement.BelowEnd)
 * IconButton(
 *     imageVector = ArecaIcons.DotsVertical,
 *     contentDescription = "Actions for Lidl",
 *     onClick = menu::show,
 *     modifier = Modifier.anchored(menu.anchorState),
 * )
 * AdaptiveMenu(
 *     state = menu,
 *     title = "Lidl",
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
    val window = ArecaTheme.hostWindow
    if (window.input == InputMode.Pointer || window.width >= WidthClass.Expanded) {
        DropdownPopup(state = state, onDismissRequest = state::dismiss) {
            MenuActions(
                actions = actions,
                onChosen = state::dismiss,
                modifier = modifier.widthIn(min = 200.dp, max = 320.dp),
            )
        }
    } else {
        val controller = rememberFlowModal(allowPartial = false, autoHeight = true)
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
