package dev.voir.areca.flow.profile.row

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import dev.voir.areca.flow.profile.ProfileSummary
import dev.voir.areca.flow.profile.icon.BundledIcons
import dev.voir.areca.flow.profile.icon.Check
import dev.voir.areca.flow.profile.icon.UserPlus
import dev.voir.areca.flow.profile.part.ProfileAvatar
import dev.voir.areca.flow.profile.part.identityLine
import dev.voir.areca.foundation.avatar.AvatarSize
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.foundation.settings.SettingsRow
import dev.voir.areca.foundation.settings.SettingsRowStatus
import dev.voir.areca.foundation.text.Text

/**
 * One account signed in on this device, in a list to switch between them.
 *
 * The picture, the name and — under it — the handle or address, so two accounts with one name can
 * be told apart. The account in use is marked with a tick by default; any account given [onClick] is
 * pressable. Usually that is every account but the one in use — switching to it would change
 * nothing — but a product that opens the current account's settings from here gives it one too.
 * Pass a `ListItemChevron` as [trailing] when pressing takes the person to another screen.
 *
 * While a switch is in flight, pass [SettingsRowStatus.Loading] to the account being switched to.
 *
 * In a list built with `forEach`, wrap each row in `key` with the account's identifier, so a row's
 * press and focus stay with its account when the list changes.
 *
 * @param account The account.
 * @param modifier Modifier applied to the row.
 * @param onClick Called when the row is pressed — to switch to the account — or `null` for a row
 *   that only states.
 * @param onClickLabel What pressing does, for assistive technology, or `null` for none.
 * @param isCurrent Whether this is the account in use.
 * @param currentDescription What the tick means, for assistive technology. The tick is the only sign
 *   of the account in use, so it is always announced.
 * @param status Progress of a switch to this account.
 * @param enabled Whether the row accepts input. Pass `false` to every other account while a switch
 *   is in flight, so a second press cannot start a competing one.
 * @param trailing Content at the end of the row; defaults to the tick when [isCurrent] and to
 *   nothing otherwise.
 */
@Composable
public fun AccountRow(
    account: ProfileSummary,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    onClickLabel: String? = null,
    isCurrent: Boolean = false,
    currentDescription: String = "Current account",
    status: SettingsRowStatus = SettingsRowStatus.Idle,
    enabled: Boolean = true,
    trailing: (@Composable BoxScope.() -> Unit)? = if (isCurrent) {
        { Icon(BundledIcons.Check, contentDescription = currentDescription) }
    } else {
        null
    },
) {
    val subtitle = account.identityLine

    SettingsRow(
        title = { Text(account.name, maxLines = 1, overflow = TextOverflow.Ellipsis) },
        subtitle = subtitle?.let { line ->
            { Text(line, maxLines = 1, overflow = TextOverflow.Ellipsis) }
        },
        modifier = modifier,
        leading = {
            ProfileAvatar(name = account.name, image = account.avatar, size = AvatarSize.ExtraSmall)
        },
        trailing = trailing,
        status = status,
        enabled = enabled,
        onClick = onClick,
        onClickLabel = onClickLabel,
    )
}

/**
 * The row at the end of an account list that signs in to another account.
 *
 * @param onClick Called when the person asks to add an account.
 * @param modifier Modifier applied to the row.
 * @param title Title of the row.
 * @param onClickLabel What pressing does, for assistive technology, or `null` for none.
 * @param status Progress of adding the account, when it starts from here: a spinner while it runs,
 *   a message on failure.
 * @param enabled Whether the row accepts input — `false` at the product's account limit.
 */
@Composable
public fun AddAccountRow(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    title: String = "Add account",
    onClickLabel: String? = null,
    status: SettingsRowStatus = SettingsRowStatus.Idle,
    enabled: Boolean = true,
) {
    SettingsRow(
        title = { Text(title) },
        modifier = modifier,
        leading = { Icon(BundledIcons.UserPlus, contentDescription = null) },
        status = status,
        enabled = enabled,
        onClick = onClick,
        onClickLabel = onClickLabel,
    )
}
