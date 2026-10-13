package dev.voir.areca.flow.profile.row

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import dev.voir.areca.foundation.settings.SettingsRow
import dev.voir.areca.foundation.settings.SettingsRowStatus
import dev.voir.areca.foundation.text.Text

/**
 * One fact about the account: the fact itself, and under it what kind of fact it is.
 *
 * The value leads and the label follows — "+381 64 123 45 67" over "Mobile", "@ada" over
 * "Username". On a profile the value is what is read, and the label only says which one it is, so
 * the value takes the title's weight. For a setting, where the question is what is read, use a
 * `SettingsRow` with a value instead.
 *
 * With [onClick] the row opens whatever changes the fact — a sheet, another screen — and is announced
 * as a button; pass a `ListItemChevron` as [trailing] when it opens another screen. Without it the
 * row only states the fact and reads as one item: "@ada, Username".
 *
 * Put it in a `Section` of a `Settings` list; the section draws the card and the hairlines.
 *
 * @param value The fact, already worded. Long values wrap rather than being cut, since the fact is
 *   the point of the row.
 * @param label What kind of fact it is.
 * @param modifier Modifier applied to the row.
 * @param onClick Called when the row is pressed, or `null` for a row that only states.
 * @param onClickLabel What pressing does, for assistive technology.
 * @param status Progress of a change the row started: a spinner while saving, a message on failure.
 * @param enabled Whether the row accepts input.
 * @param leading Glyph before the text — a `SettingsRowIcon` tile, usually — or `null` for none.
 * @param trailing Content at the end of the row — a `ListItemChevron`, usually — or `null` for none.
 */
@Composable
public fun ProfileInfoRow(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    onClickLabel: String? = null,
    status: SettingsRowStatus = SettingsRowStatus.Idle,
    enabled: Boolean = true,
    leading: (@Composable BoxScope.() -> Unit)? = null,
    trailing: (@Composable BoxScope.() -> Unit)? = null,
) {
    SettingsRow(
        title = { Text(value) },
        subtitle = { Text(label, maxLines = 1, overflow = TextOverflow.Ellipsis) },
        modifier = modifier,
        leading = leading,
        trailing = trailing,
        status = status,
        enabled = enabled,
        onClick = onClick,
        onClickLabel = onClickLabel,
    )
}
