package dev.voir.areca.flow.profile.session

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import dev.voir.areca.flow.profile.icon.Browser
import dev.voir.areca.flow.profile.icon.BundledIcons
import dev.voir.areca.flow.profile.icon.HelpCircle
import dev.voir.areca.flow.profile.icon.Monitor
import dev.voir.areca.flow.profile.icon.Tablet
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.icon.FeaturedIcon
import dev.voir.areca.foundation.icon.FeaturedIconSize
import dev.voir.areca.foundation.icon.FeaturedIconStyle
import dev.voir.areca.foundation.settings.SettingsRow
import dev.voir.areca.foundation.settings.SettingsRowStatus
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.theme.ArecaTheme

/**
 * One device the account is signed in on: a round device tile and up to three lines — the device,
 * what it runs, and where and when it was last seen.
 *
 * Pressed to act on the session — usually to open `RevokeSessionSheet` — when [onClick] is given,
 * and inert when it is not; the list's spinner shows while [SessionItem.isRevoking]. Which sessions
 * can be acted on is the application's call: `ActiveSessionsSections` leaves the current one inert,
 * since ending it is signing out.
 *
 * The tile is neutral, with the current session's tile the one inverted disc on the list, so "this
 * device" is found at a glance without a colour that means something else.
 *
 * In a list built with `forEach`, wrap each row in `key` with [SessionItem.id], so a row's press and
 * focus stay with its session when the list changes.
 *
 * @param session The session.
 * @param modifier Modifier applied to the row.
 * @param onClick Called when the session is pressed, or `null` for a row that only states.
 * @param onClickLabel What pressing does, for assistive technology — "Sign out device" — or `null`
 *   for none.
 * @param enabled Whether the row accepts input.
 * @param trailing Content at the row's end — a `ListItemChevron` when pressing opens another
 *   screen — or `null` for none.
 */
@Composable
public fun SessionRow(
    session: SessionItem,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    onClickLabel: String? = null,
    enabled: Boolean = true,
    trailing: (@Composable BoxScope.() -> Unit)? = null,
) {
    SettingsRow(
        title = {
            Text(
                text = session.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.SemiBold,
            )
        },
        subtitle = if (session.client != null || session.seen != null) {
            {
                Column {
                    session.client?.let { client ->
                        Text(
                            text = client,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = ArecaTheme.colors.fgBase,
                        )
                    }
                    session.seen?.let { seen ->
                        Text(text = seen, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                }
            }
        } else {
            null
        },
        modifier = modifier,
        leading = {
            FeaturedIcon(
                imageVector = session.device.icon(),
                contentDescription = null,
                size = FeaturedIconSize.Small,
                style = if (session.isCurrent) {
                    FeaturedIconStyle.solid(ActionColor.NeutralDark)
                } else {
                    FeaturedIconStyle.soft(ActionColor.Neutral)
                },
                shape = ArecaTheme.shapes.full,
            )
        },
        status = if (session.isRevoking) SettingsRowStatus.Loading else SettingsRowStatus.Idle,
        enabled = enabled,
        onClick = onClick,
        onClickLabel = onClickLabel,
        trailing = trailing,
    )
}

/** @return The glyph that stands for this kind of device. */
private fun SessionDevice.icon(): ImageVector = when (this) {
    // Areca's `Phone` is a handset, which beside a device name reads as a call rather than as the
    // thing the session is on. A bezelled screen is the honest shape for a phone and a tablet both.
    SessionDevice.Phone -> BundledIcons.Tablet
    SessionDevice.Tablet -> BundledIcons.Tablet
    SessionDevice.Desktop -> BundledIcons.Monitor
    SessionDevice.Browser -> BundledIcons.Browser
    SessionDevice.Unknown -> BundledIcons.HelpCircle
}
