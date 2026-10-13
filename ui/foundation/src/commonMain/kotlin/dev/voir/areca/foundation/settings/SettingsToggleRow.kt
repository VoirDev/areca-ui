package dev.voir.areca.foundation.settings

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.selection.triStateToggleable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import dev.voir.areca.foundation.layout.VerticalDivider
import dev.voir.areca.foundation.list.ControlListItem
import dev.voir.areca.foundation.list.ListItem
import dev.voir.areca.foundation.list.ListItemAction
import dev.voir.areca.foundation.list.ListItemControlPlacement
import dev.voir.areca.foundation.list.ListItemSize
import dev.voir.areca.foundation.list.ListItemStyle
import dev.voir.areca.foundation.list.LocalListItemState
import dev.voir.areca.foundation.selection.BinaryToggleCycle
import dev.voir.areca.foundation.selection.Toggle
import dev.voir.areca.foundation.selection.ToggleState
import dev.voir.areca.foundation.selection.toToggleableState
import dev.voir.areca.foundation.slot.SlotRowLayout
import dev.voir.areca.indication.TouchIndication
import dev.voir.areca.modifier.MinimumTouchTarget
import dev.voir.areca.modifier.clickCursor
import dev.voir.areca.modifier.minimumTouchTarget
import dev.voir.areca.theme.ArecaTheme

/**
 * A settings row that ends in a switch.
 *
 * It comes in two shapes, chosen by whether [onClick] is given.
 *
 * **Whole row.** Without [onClick], the row *is* the switch: pressing anywhere on it — the switch
 * included — flips the state, and it is announced and focused as one switch. A switch alone is a
 * small target, and a row that only reacts where the switch sits reads as inert everywhere else.
 *
 * **Split.** With [onClick], the row opens more settings for the feature — a schedule, a list of
 * exceptions — and the switch turns the feature on or off without leaving the screen. A short
 * vertical rule stands between the two targets so the split is visible before it is discovered. The
 * row is then announced as a button and the switch as a switch of its own — one target with a
 * finger-sized hit area, focused and toggled as a whole.
 *
 * While [status] is [SettingsRowStatus.Loading], a spinner stands before the switch, the switch holds
 * still and presses are ignored; an error message appears under the text when the change failed.
 *
 * @param state Current state of the switch.
 * @param onToggle Called with the next state when the switch — or, without [onClick], the row — is
 * activated.
 * @param title Main copy of the row.
 * @param modifier Modifier applied to the row.
 * @param subtitle Supporting copy below [title].
 * @param badge Content right after [title].
 * @param leading Slot before the text.
 * @param bottom Content below the row's main line, inside its hit area.
 * @param status Progress of the change the row last asked for.
 * @param enabled Whether the row and its switch accept input.
 * @param nestingLevel Indentation steps of [ListItemSize.nestedLevelOffset].
 * @param onClick Opens the feature's own settings, splitting the row from its switch; `null` makes
 * the whole row the switch.
 * @param onClickLabel What pressing the row does, for assistive technology. Read only when
 * [onClick] splits the row; the whole-row switch is announced by its role and state.
 * @param cycleOrder States the switch walks through, in order.
 * @param interactionSource Source of the row interactions; pass one to observe them from outside.
 * @param style Colors of the row.
 * @param size Metrics and typography of the row.
 * @param layout Slot alignment of the row; defaults to the list's, top-aligned when there is a
 * [subtitle].
 */
@Composable
public fun SettingsToggleRow(
    state: ToggleState,
    onToggle: (ToggleState) -> Unit,
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: (@Composable () -> Unit)? = null,
    badge: (@Composable () -> Unit)? = null,
    leading: (@Composable BoxScope.() -> Unit)? = null,
    bottom: (@Composable ColumnScope.() -> Unit)? = null,
    status: SettingsRowStatus = SettingsRowStatus.Idle,
    enabled: Boolean = true,
    nestingLevel: Int = 0,
    onClick: (() -> Unit)? = null,
    onClickLabel: String? = null,
    cycleOrder: List<ToggleState> = BinaryToggleCycle,
    interactionSource: MutableInteractionSource? = null,
    style: ListItemStyle = SettingsTheme.styles.row,
    size: ListItemSize = SettingsTheme.sizes.row,
    layout: SlotRowLayout = settingsRowLayout(hasSubtitle = subtitle != null),
) {
    val anchor = rememberSeparatorAnchor()
    val loading = status == SettingsRowStatus.Loading
    val toggle: (ToggleState) -> Unit = { next -> if (!loading) onToggle(next) }

    val control: @Composable () -> Unit = {
        Toggle(
            state = state,
            onToggle = toggle,
            // The row resolves its own enabled state, section included; follow it.
            enabled = LocalListItemState.current.enabled,
            readOnly = loading,
            cycleOrder = cycleOrder,
        )
    }

    if (onClick == null) {
        ControlListItem(
            control = control,
            // A toggle's "selected" is its on state, which is what the row slots read.
            selected = state == ToggleState.On,
            action = ListItemAction.Toggle(
                state = state.toToggleableState(),
                role = Role.Switch,
                onToggle = { toggle(state.next(cycleOrder)) },
            ),
            title = { SettingsRowTitle(anchor = anchor, title = title, badge = badge) },
            modifier = Modifier.separatorAnchor(anchor).then(modifier),
            subtitle = subtitle,
            layout = layout,
            value = statusValue(status, value = null),
            enabled = enabled,
            // A switched-on row is not a selected row: the switch already says so, and highlighting
            // every enabled setting would turn the screen into a wall of color.
            highlightSelected = false,
            nestingLevel = nestingLevel,
            placement = ListItemControlPlacement.Trailing,
            style = style,
            size = size,
            interactionSource = interactionSource,
            leading = leading,
            bottom = statusBottom(status, bottom),
        )
    } else {
        val dividerHeight = SettingsTheme.sizes.splitDividerHeight

        ListItem(
            modifier = Modifier.separatorAnchor(anchor).then(modifier),
            title = { SettingsRowTitle(anchor = anchor, title = title, badge = badge) },
            subtitle = subtitle,
            layout = layout,
            value = statusValue(status, value = null),
            enabled = enabled,
            nestingLevel = nestingLevel,
            style = style,
            size = size,
            interactionSource = interactionSource,
            onClick = onClick.unlessLoading(status),
            onClickLabel = onClickLabel,
            leading = leading,
            trailing = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(size.trailingGap),
                ) {
                    VerticalDivider(modifier = Modifier.height(dividerHeight))
                    SplitSwitch(
                        state = state,
                        readOnly = loading,
                        onToggle = { toggle(state.next(cycleOrder)) },
                    )
                }
            },
            bottom = statusBottom(status, bottom),
        )
    }
}

/**
 * The switch half of a split [SettingsToggleRow]: one target that focuses, toggles and is announced
 * as a switch with its state.
 *
 * The [Toggle] inside is display-only — no press, focus or semantics of its own — so a press
 * lands on this target once and assistive technology meets one switch. The target follows the row's
 * enabled state, section included, and reserves a finger-sized slot around the drawn track.
 *
 * @param state Current state of the switch.
 * @param readOnly Whether the switch shows its state but rejects changes, as it does while a
 *   change is pending.
 * @param onToggle Called when the switch is activated.
 */
@Composable
private fun SplitSwitch(
    state: ToggleState,
    readOnly: Boolean,
    onToggle: () -> Unit,
) {
    val enabled = LocalListItemState.current.enabled

    Box(
        modifier = Modifier
            .minimumTouchTarget(MinimumTouchTarget)
            .triStateToggleable(
                state = state.toToggleableState(),
                enabled = enabled,
                role = Role.Switch,
                interactionSource = null,
                indication = TouchIndication(shape = ArecaTheme.shapes.full),
                onClick = onToggle,
            )
            .clickCursor(enabled),
        contentAlignment = Alignment.Center,
    ) {
        Toggle(state = state, onToggle = null, enabled = enabled, readOnly = readOnly)
    }
}

/**
 * A settings row that ends in an on/off switch, for a setting that is a plain yes or no.
 *
 * The same row as the [ToggleState] one above, for the common case where the setting is a
 * `Boolean` — so a caller does not convert to and from [ToggleState] at every row. A setting with an
 * indeterminate state takes the other overload.
 *
 * @param checked Whether the setting is on.
 * @param onCheckedChange Called with the next value when the switch — or, without [onClick], the
 * row — is activated.
 * @param title Main copy of the row.
 * @param modifier Modifier applied to the row.
 * @param subtitle Supporting copy below [title].
 * @param badge Content right after [title].
 * @param leading Slot before the text.
 * @param bottom Content below the row's main line, inside its hit area.
 * @param status Progress of the change the row last asked for.
 * @param enabled Whether the row and its switch accept input.
 * @param nestingLevel Indentation steps of [ListItemSize.nestedLevelOffset].
 * @param onClick Opens the feature's own settings, splitting the row from its switch; `null` makes
 * the whole row the switch.
 * @param onClickLabel What pressing the row does, for assistive technology. Read only when
 * [onClick] splits the row; the whole-row switch is announced by its role and state.
 * @param interactionSource Source of the row interactions; pass one to observe them from outside.
 * @param style Colors of the row.
 * @param size Metrics and typography of the row.
 * @param layout Slot alignment of the row; defaults to the list's, top-aligned when there is a
 * [subtitle].
 */
@Composable
public fun SettingsToggleRow(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: (@Composable () -> Unit)? = null,
    badge: (@Composable () -> Unit)? = null,
    leading: (@Composable BoxScope.() -> Unit)? = null,
    bottom: (@Composable ColumnScope.() -> Unit)? = null,
    status: SettingsRowStatus = SettingsRowStatus.Idle,
    enabled: Boolean = true,
    nestingLevel: Int = 0,
    onClick: (() -> Unit)? = null,
    onClickLabel: String? = null,
    interactionSource: MutableInteractionSource? = null,
    style: ListItemStyle = SettingsTheme.styles.row,
    size: ListItemSize = SettingsTheme.sizes.row,
    layout: SlotRowLayout = settingsRowLayout(hasSubtitle = subtitle != null),
) {
    SettingsToggleRow(
        state = if (checked) ToggleState.On else ToggleState.Off,
        onToggle = { next -> onCheckedChange(next == ToggleState.On) },
        title = title,
        modifier = modifier,
        subtitle = subtitle,
        badge = badge,
        leading = leading,
        bottom = bottom,
        status = status,
        enabled = enabled,
        nestingLevel = nestingLevel,
        onClick = onClick,
        onClickLabel = onClickLabel,
        interactionSource = interactionSource,
        style = style,
        size = size,
        layout = layout,
    )
}
