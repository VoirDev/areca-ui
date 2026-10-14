package dev.voir.areca.foundation.settings

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import dev.voir.areca.foundation.list.CheckmarkListItem
import dev.voir.areca.foundation.list.ListItemSize
import dev.voir.areca.foundation.list.ListItemStyle
import dev.voir.areca.foundation.list.LocalListItemLayout
import dev.voir.areca.foundation.list.LocalListItemState
import dev.voir.areca.foundation.overlay.AnchoredPlacement
import dev.voir.areca.foundation.overlay.DropdownPopup
import dev.voir.areca.foundation.overlay.anchored
import dev.voir.areca.foundation.overlay.rememberDropdownPopup
import dev.voir.areca.foundation.slot.SlotRowLayout
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.theme.ArecaTheme

/**
 * A settings row whose value is one of a few options, picked from a menu hanging off the value.
 *
 * The shape of most settings — units, a sound, a timeout, a default account. The current option is
 * the row's value, and pressing the row changes it: the menu opens below the value with a check mark
 * on the current option, and closes as soon as one is picked. Nothing navigates away, so the choice
 * is made where the question was asked.
 *
 * For a choice that needs more room — a long list, an explanation per option — lay the options out
 * as [SettingsOptionRow]s in a section of their own instead.
 *
 * While [status] is [SettingsRowStatus.Loading] the menu closes, the value turns into a spinner and
 * presses are ignored.
 *
 * @param T Type of an option.
 * @param title Main copy of the row.
 * @param options Options to pick from, in menu order.
 * @param selected The current option.
 * @param onSelect Called with the option picked from the menu.
 * @param optionLabel Label of an option, used for the value and for the menu.
 * @param modifier Modifier applied to the row.
 * @param subtitle Supporting copy below [title].
 * @param badge Content right after [title].
 * @param leading Slot before the text.
 * @param status Progress of the change the row last asked for.
 * @param enabled Whether the row accepts input.
 * @param nestingLevel Indentation steps of [ListItemSize.nestedLevelOffset].
 * @param onClickLabel What pressing the row does, for assistive technology.
 * @param interactionSource Source of the row interactions; pass one to observe them from outside.
 * @param style Colors of the row; defaults to the list's, with its interactive value color.
 * @param size Metrics and typography of the row.
 * @param layout Slot alignment of the row; defaults to the list's [SlotRowLayout.Auto], centred for
 * one or two lines and from the top beyond.
 */
@Composable
public fun <T> SettingsSelectRow(
    title: @Composable () -> Unit,
    options: List<T>,
    selected: T,
    onSelect: (T) -> Unit,
    optionLabel: (T) -> String,
    modifier: Modifier = Modifier,
    subtitle: (@Composable () -> Unit)? = null,
    badge: (@Composable () -> Unit)? = null,
    leading: (@Composable BoxScope.() -> Unit)? = null,
    status: SettingsRowStatus = SettingsRowStatus.Idle,
    enabled: Boolean = true,
    nestingLevel: Int = 0,
    onClickLabel: String? = null,
    interactionSource: MutableInteractionSource? = null,
    style: ListItemStyle = SettingsTheme.styles.rowStyle(SettingsRowTone.Default, interactive = true),
    size: ListItemSize = SettingsTheme.sizes.row,
    layout: SlotRowLayout = LocalListItemLayout.current,
) {
    val dropdown = rememberDropdownPopup(placement = AnchoredPlacement.BelowEnd)

    // A row that starts loading takes its menu with it rather than leaving it open over a value
    // that is about to change.
    LaunchedEffect(status) {
        if (status == SettingsRowStatus.Loading) dropdown.dismiss()
    }

    SettingsRow(
        title = title,
        modifier = modifier,
        subtitle = subtitle,
        badge = badge,
        leading = leading,
        value = {
            Text(
                text = optionLabel(selected),
                modifier = Modifier.anchored(dropdown.anchorState),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            // The popup lives in a window of its own and measures to nothing here, so emitting it
            // beside the value costs the row no width and keeps it anchored to what it changes.
            if (LocalListItemState.current.enabled) {
                DropdownPopup(
                    state = dropdown,
                    onDismissRequest = { dropdown.dismiss() },
                ) {
                    // The popup caps its height but leaves scrolling to its content, so a long
                    // option list scrolls here rather than running off the bottom of the menu.
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .padding(ArecaTheme.spacing.xs),
                    ) {
                        options.forEach { option ->
                            CheckmarkListItem(
                                selected = option == selected,
                                onClick = {
                                    dropdown.dismiss()
                                    if (option != selected) onSelect(option)
                                },
                                title = { Text(optionLabel(option)) },
                                style = ListItemStyle.ghost(),
                                size = ListItemSize.Compact,
                            )
                        }
                    }
                }
            }
        },
        status = status,
        enabled = enabled,
        nestingLevel = nestingLevel,
        onClick = { dropdown.show() },
        onClickLabel = onClickLabel,
        interactionSource = interactionSource,
        style = style,
        size = size,
        layout = layout,
    )
}
