package dev.voir.areca.foundation.settings

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.voir.areca.foundation.list.ListItem
import dev.voir.areca.foundation.list.ListItemSize
import dev.voir.areca.foundation.list.ListItemStyle
import dev.voir.areca.foundation.list.LocalListItemLayout
import dev.voir.areca.foundation.slot.SlotRowLayout

/**
 * A single row of a settings list.
 *
 * The row is a [ListItem] with the settings list's metrics, so it hovers, presses, takes keyboard
 * focus and shows selection exactly like every other row in the design system, and it reads as one
 * item to assistive technology. On top of that it adds what a settings screen asks of a row:
 *
 * - **Tone.** [SettingsRowTone.Brand] turns the row into an action inside the list — "Add device",
 *   "Change wallpaper" — and [SettingsRowTone.Danger] into a destructive one. The title and the
 *   leading glyph take the tone; a [SettingsRowIcon] tile keeps its own color.
 * - **Interactive value.** A [value] on a row with [onClick] brightens while the row is reached
 *   for, and by default takes [SettingsStyles.interactiveValue] when the list gives it one — the
 *   brand, on a screen that wants its changeable values called out.
 * - **Badge.** Content after the title, such as a "New" badge, kept visible when the title wraps
 *   and sized to the title's line.
 * - **Status.** A [SettingsRowStatus] shows a spinner in place of the value while a change is in
 *   flight, and a message under the text when it failed.
 *
 * The slots align by the list's [SlotRowLayout.Auto]: a row of one or two lines centres its tile,
 * its text and its trailing content, and a longer one starts the tile and the text at the top with
 * the trailing content level with the tile. Pass [layout] to align them otherwise. Inside a
 * [Section], the separator above and below the row starts at its text.
 *
 * ```kotlin
 * SettingsRow(
 *     title = { Text("Microphone") },
 *     leading = { Icon(ArecaIcons.Microphone, contentDescription = null) },
 *     value = { Text("Built-in") },
 *     trailing = { ListItemChevron() },
 *     onClick = { showMicrophones() },
 * )
 * ```
 *
 * @param title Main copy of the row.
 * @param modifier Modifier applied to the row.
 * @param subtitle Supporting copy below [title].
 * @param caption A line of detail below [subtitle] — when a device was last seen, how large a file
 * is — smaller than the subtitle and in its color.
 * @param value Current value, drawn at the end of the row before [trailing].
 * @param badge Content right after [title].
 * @param leading Slot before the text, tinted with the tone's glyph color.
 * @param trailing Slot after [value], tinted with the tone's glyph color — a
 * [dev.voir.areca.foundation.list.ListItemChevron] on a row that opens another screen.
 * @param bottom Content below the row's main line, inside its hit area.
 * @param tone What the row does, which decides its colors.
 * @param status Progress of the change the row last asked for.
 * @param enabled Whether the row accepts input and uses its enabled colors. A row in a disabled
 * [Section] is disabled regardless.
 * @param selected Whether the row is the selected one, as in a two-pane settings navigator.
 * @param nestingLevel Indentation steps of [ListItemSize.nestedLevelOffset], for a setting that
 * depends on the one above it.
 * @param onClick Click handler; omit for a row that only states something.
 * @param onClickLabel What pressing does, for assistive technology.
 * @param interactionSource Source of the row interactions; pass one to observe them from outside.
 * @param style Colors of the row; defaults to the list's style for [tone], with its interactive
 * value color when [onClick] is given.
 * @param size Metrics and typography of the row.
 * @param layout Slot alignment of the row; defaults to the list's [SlotRowLayout.Auto], centred for
 * one or two lines and from the top beyond.
 */
@Composable
public fun SettingsRow(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: (@Composable () -> Unit)? = null,
    caption: (@Composable () -> Unit)? = null,
    value: (@Composable () -> Unit)? = null,
    badge: (@Composable () -> Unit)? = null,
    leading: (@Composable BoxScope.() -> Unit)? = null,
    trailing: (@Composable BoxScope.() -> Unit)? = null,
    bottom: (@Composable ColumnScope.() -> Unit)? = null,
    tone: SettingsRowTone = SettingsRowTone.Default,
    status: SettingsRowStatus = SettingsRowStatus.Idle,
    enabled: Boolean = true,
    selected: Boolean = false,
    nestingLevel: Int = 0,
    onClick: (() -> Unit)? = null,
    onClickLabel: String? = null,
    interactionSource: MutableInteractionSource? = null,
    style: ListItemStyle = SettingsTheme.styles.rowStyle(tone, interactive = onClick != null),
    size: ListItemSize = SettingsTheme.sizes.row,
    layout: SlotRowLayout = LocalListItemLayout.current,
) {
    val anchor = rememberSeparatorAnchor()

    ListItem(
        modifier = Modifier.separatorAnchor(anchor).then(modifier),
        title = { SettingsRowTitle(anchor = anchor, title = title, badge = badge) },
        subtitle = subtitle,
        caption = caption,
        layout = layout,
        value = statusValue(status, value),
        enabled = enabled,
        selected = selected,
        nestingLevel = nestingLevel,
        style = style,
        size = size,
        interactionSource = interactionSource,
        onClick = onClick.unlessLoading(status),
        onClickLabel = onClickLabel,
        leading = leading,
        trailing = trailing,
        bottom = statusBottom(status, bottom),
    )
}
