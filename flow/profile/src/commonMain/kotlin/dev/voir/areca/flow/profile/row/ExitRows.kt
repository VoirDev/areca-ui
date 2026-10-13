package dev.voir.areca.flow.profile.row

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.voir.areca.flow.profile.icon.BundledIcons
import dev.voir.areca.flow.profile.icon.LogOut
import dev.voir.areca.flow.profile.icon.Trash
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.foundation.settings.SettingsRow
import dev.voir.areca.foundation.settings.SettingsRowStatus
import dev.voir.areca.foundation.settings.SettingsRowTone
import dev.voir.areca.foundation.text.Text

/**
 * Signing out of this device, in the danger tone with a bare glyph.
 *
 * Give it a `Section` of its own at the foot of the account menu, so it cannot be pressed on the way
 * to something else. Pair it with `SignOutSheet` when signing out costs something — changes that have
 * not synced, a long way back in — and call straight through when it does not.
 *
 * @param onClick Called when the person asks to sign out.
 * @param modifier Modifier applied to the row.
 * @param title Title of the row.
 * @param onClickLabel What pressing does, for assistive technology, or `null` for none.
 * @param status Progress of signing out, when it is not confirmed in a sheet.
 * @param enabled Whether the row accepts input.
 */
@Composable
public fun SignOutRow(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    title: String = "Sign out",
    onClickLabel: String? = null,
    status: SettingsRowStatus = SettingsRowStatus.Idle,
    enabled: Boolean = true,
) {
    SettingsRow(
        title = { Text(title) },
        modifier = modifier,
        leading = { Icon(BundledIcons.LogOut, contentDescription = null) },
        tone = SettingsRowTone.Danger,
        status = status,
        enabled = enabled,
        onClick = onClick,
        onClickLabel = onClickLabel,
    )
}

/**
 * Deleting the account, in the danger tone, with what it costs under its title when [subtitle] is
 * given.
 *
 * Put it in a `Section` of its own at the end of the screen, with no header: "Danger zone" names a
 * group of one, and the row already says what it does. Always pair it with `DeleteAccountSheet`.
 *
 * @param onClick Called when the person asks to delete the account.
 * @param modifier Modifier applied to the row.
 * @param title Title of the row.
 * @param subtitle What deleting costs — "Permanent, and cannot be undone" — or `null` for none.
 * @param onClickLabel What pressing does, for assistive technology, or `null` for none.
 * @param status Progress of deleting, when it is not confirmed in a sheet: a spinner while it runs,
 *   a message on failure.
 * @param enabled Whether the row accepts input. Disable it while a save is in flight — asking to
 *   remove the account a form is writing to invites a race nothing here resolves.
 */
@Composable
public fun DeleteAccountRow(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    title: String = "Delete account",
    subtitle: String? = null,
    onClickLabel: String? = null,
    status: SettingsRowStatus = SettingsRowStatus.Idle,
    enabled: Boolean = true,
) {
    SettingsRow(
        title = { Text(title) },
        subtitle = subtitle?.let { line -> { Text(line) } },
        modifier = modifier,
        leading = { Icon(BundledIcons.Trash, contentDescription = null) },
        tone = SettingsRowTone.Danger,
        status = status,
        enabled = enabled,
        onClick = onClick,
        onClickLabel = onClickLabel,
    )
}
