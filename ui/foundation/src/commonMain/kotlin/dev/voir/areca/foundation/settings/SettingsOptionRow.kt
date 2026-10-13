package dev.voir.areca.foundation.settings

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.voir.areca.foundation.list.CheckmarkListItem
import dev.voir.areca.foundation.list.ListItemSize
import dev.voir.areca.foundation.list.ListItemStyle

/**
 * One option of a choice laid out in a section, marked with a check when it is the chosen one.
 *
 * Put the options of one question in a [Section] of their own, headed by the question — "Auto-lock",
 * "Theme", "Distance units" — so the chosen option is visible without opening anything. The check
 * draws itself in as the choice moves; the row does not also fill, so a section of options stays
 * calm. Reach for [SettingsSelectRow] instead when the question is one line among many.
 *
 * @param selected Whether this is the chosen option.
 * @param onClick Called when the option is picked.
 * @param title Name of the option.
 * @param modifier Modifier applied to the row.
 * @param subtitle What the option means, when its name does not say.
 * @param badge Content right after [title], such as a "Recommended" badge.
 * @param leading Slot before the text.
 * @param enabled Whether the option can be picked.
 * @param nestingLevel Indentation steps of [ListItemSize.nestedLevelOffset].
 * @param onClickLabel What picking does, for assistive technology.
 * @param interactionSource Source of the row interactions; pass one to observe them from outside.
 * @param style Colors of the row.
 * @param size Metrics and typography of the row.
 */
@Composable
public fun SettingsOptionRow(
    selected: Boolean,
    onClick: () -> Unit,
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: (@Composable () -> Unit)? = null,
    badge: (@Composable () -> Unit)? = null,
    leading: (@Composable BoxScope.() -> Unit)? = null,
    enabled: Boolean = true,
    nestingLevel: Int = 0,
    onClickLabel: String? = null,
    interactionSource: MutableInteractionSource? = null,
    style: ListItemStyle = SettingsTheme.styles.row,
    size: ListItemSize = SettingsTheme.sizes.row,
) {
    val anchor = rememberSeparatorAnchor()

    CheckmarkListItem(
        selected = selected,
        onClick = onClick,
        title = { SettingsRowTitle(anchor = anchor, title = title, badge = badge) },
        modifier = Modifier.separatorAnchor(anchor).then(modifier),
        subtitle = subtitle,
        enabled = enabled,
        nestingLevel = nestingLevel,
        style = style,
        size = size,
        interactionSource = interactionSource,
        onClickLabel = onClickLabel,
        leading = leading,
    )
}
