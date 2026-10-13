package dev.voir.areca.flow.profile.row

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import dev.voir.areca.flow.profile.icon.BundledIcons
import dev.voir.areca.flow.profile.icon.Mail
import dev.voir.areca.foundation.badge.Badge
import dev.voir.areca.foundation.settings.SettingsRow
import dev.voir.areca.foundation.settings.SettingsRowIcon
import dev.voir.areca.foundation.settings.SettingsRowStatus
import dev.voir.areca.foundation.settings.SettingsTheme
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.indication.FadeIndication
import dev.voir.areca.modifier.MinimumTouchTarget
import dev.voir.areca.modifier.clickCursor
import dev.voir.areca.theme.ArecaTheme

/**
 * The address the account signs in with, and whether it has been confirmed.
 *
 * A fact like any other on the profile — the address over its [label] — with its state after it.
 * An unconfirmed address carries the [unverifiedBadge] on its line and, when [onVerify] is given, a
 * [verifyLabel] link at the row's end, a target of its own. A confirmed one carries nothing extra:
 * the absence of a warning is the good news. Pass [trailing] to put something else at the row's
 * end — a `ListItemChevron` when pressing opens another screen.
 *
 * @param email Address, or `null` when the account has none — the row then invites one, so pass
 *   [onClick] too: an invitation that cannot be pressed is a dead end.
 * @param isVerified Whether [email] has been confirmed.
 * @param modifier Modifier applied to the row.
 * @param onVerify Called when the person asks for a confirmation to be sent, or `null` to offer
 *   none — an application that cannot send one from here should not draw a control that says it can.
 * @param onClick Called when the row is pressed — to change the address — or `null` for a row that
 *   only states.
 * @param onClickLabel What pressing the row does, for assistive technology, or `null` for none.
 * @param status Progress of a change the row started: a spinner while saving, a message on failure.
 * @param label What the fact is, under the address.
 * @param placeholder Title while there is no address — an invitation to add one.
 * @param unverifiedBadge Badge on an unconfirmed address, or `null` for none. It is the only sign of
 *   that state when [onVerify] is not given, so drop it only where something else says so.
 * @param verifyLabel Label of the link [onVerify] is offered through.
 * @param enabled Whether the row and its link accept input.
 * @param trailing Content at the row's end; defaults to the [verifyLabel] link on an unconfirmed
 *   address when [onVerify] is given, and to nothing otherwise.
 */
@Composable
public fun EmailRow(
    email: String?,
    isVerified: Boolean,
    modifier: Modifier = Modifier,
    onVerify: (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    onClickLabel: String? = null,
    status: SettingsRowStatus = SettingsRowStatus.Idle,
    label: String = "Email",
    placeholder: String = "Add email",
    unverifiedBadge: String? = "Unverified",
    verifyLabel: String = "Verify",
    enabled: Boolean = true,
    trailing:(@Composable BoxScope.() -> Unit)? = if (email != null && !isVerified && onVerify != null) {
        { VerifyLink(text = verifyLabel, onClick = onVerify, enabled = enabled) }
    } else {
        null
    },
) {
    val unverified = email != null && !isVerified

    SettingsRow(
        title = { Text(email ?: placeholder, maxLines = 1, overflow = TextOverflow.Ellipsis) },
        subtitle = { Text(label) },
        badge = if (unverified && unverifiedBadge != null) ({ Badge { Text(unverifiedBadge) } }) else null,
        modifier = modifier,
        leading = { SettingsRowIcon(BundledIcons.Mail) },
        trailing = trailing,
        status = status,
        enabled = enabled,
        onClick = onClick,
        onClickLabel = onClickLabel,
    )
}

/**
 * The "Verify" link at the end of an [EmailRow]: underlined, so it reads as a link rather than as
 * the row's value, and a target of at least 48 dp however short its word is — the row around it is
 * pressable too, so a near miss would otherwise land on the row.
 *
 * @param text What the link says.
 * @param onClick Called when it is pressed.
 * @param enabled Whether it accepts a press; drawn in the disabled ink when not.
 */
@Composable
private fun VerifyLink(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean,
) {
    val shape = ArecaTheme.shapes.md

    Box(
        modifier = Modifier
            .sizeIn(minWidth = MinimumTouchTarget, minHeight = MinimumTouchTarget)
            .clickCursor(enabled = enabled)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = FadeIndication.tint(shape),
                enabled = enabled,
                role = Role.Button,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            variant = TextVariant.BodyMedium,
            // Neutral like every other value in the list; the underline is what says it is a link.
            color = if (enabled) TextColor.Base else TextColor.Disabled,
            textDecoration = TextDecoration.Underline,
        )
    }
}

/**
 * One way of signing in through another provider, and whether the account uses it.
 *
 * The provider's mark is a slot, sized to the row's icon tile. Brand marks have fixed colours and
 * usage terms of their own; the application that agreed to those terms supplies the artwork.
 *
 * The row is the control: pressing it links an unlinked provider and unlinks a linked one, and its
 * value says which — [linkLabel] or [unlinkLabel]. While that is in flight, pass
 * [SettingsRowStatus.Loading]; if it fails, [SettingsRowStatus.Error] with the reason.
 *
 * [canUnlink] is the application's answer to whether unlinking is allowed — usually "is this the last
 * way in?", which only the application can count. The row only refuses what it is told to refuse,
 * and says why in [lockedReason] instead of offering the control.
 *
 * @param name Provider's name, such as "Google".
 * @param isLinked Whether the account can sign in through this provider.
 * @param onLink Called when the person asks to link the provider.
 * @param onUnlink Called when the person asks to unlink it.
 * @param modifier Modifier applied to the row.
 * @param account Identity at the provider — usually an address — shown under the name while linked,
 *   or `null` to show none.
 * @param canUnlink Whether unlinking is allowed.
 * @param lockedReason Why a linked provider cannot be unlinked — "Your only way to sign in" — shown
 *   in place of the account while [canUnlink] is `false`, or `null` to say nothing.
 * @param status Progress of the last link or unlink.
 * @param notLinkedLabel Line under the name while the provider is not linked — "Not linked" — or
 *   `null` for none.
 * @param linkLabel Value of an unlinked row, naming what pressing does.
 * @param unlinkLabel Value of a linked row, naming what pressing does.
 * @param onClickLabel What pressing the row does, for assistive technology, or `null` for none —
 *   the value already names it.
 * @param enabled Whether the row accepts input.
 * @param icon Provider's mark.
 */
@Composable
public fun LinkedAccountRow(
    name: String,
    isLinked: Boolean,
    onLink: () -> Unit,
    onUnlink: () -> Unit,
    modifier: Modifier = Modifier,
    account: String? = null,
    canUnlink: Boolean = true,
    lockedReason: String? = null,
    status: SettingsRowStatus = SettingsRowStatus.Idle,
    notLinkedLabel: String? = null,
    linkLabel: String = "Link",
    unlinkLabel: String = "Unlink",
    onClickLabel: String? = null,
    enabled: Boolean = true,
    icon: @Composable BoxScope.() -> Unit,
) {
    val presentation = LinkedAccountPresentation.of(
        isLinked = isLinked,
        canUnlink = canUnlink,
        account = account,
        lockedReason = lockedReason,
        notLinkedLabel = notLinkedLabel,
    )

    SettingsRow(
        title = { Text(name) },
        subtitle = presentation.subtitle?.let { line ->
            { Text(line, maxLines = 1, overflow = TextOverflow.Ellipsis) }
        },
        value = when (presentation.action) {
            LinkedAccountAction.Link -> ({ Text(linkLabel) })
            LinkedAccountAction.Unlink -> ({ Text(unlinkLabel) })
            LinkedAccountAction.None -> null
        },
        modifier = modifier,
        leading = {
            Box(
                modifier = Modifier.size(SettingsTheme.sizes.iconTile.container),
                contentAlignment = Alignment.Center,
                content = icon,
            )
        },
        status = status,
        enabled = enabled,
        onClick = when (presentation.action) {
            LinkedAccountAction.Link -> onLink
            LinkedAccountAction.Unlink -> onUnlink
            LinkedAccountAction.None -> null
        },
        onClickLabel = onClickLabel,
    )
}

/** What pressing a [LinkedAccountRow] does. */
internal enum class LinkedAccountAction {
    /** Links the provider. */
    Link,

    /** Unlinks the provider. */
    Unlink,

    /** Nothing: the provider is linked and may not be unlinked, so the row only states. */
    None,
}

/**
 * How a [LinkedAccountRow] reads in its current state.
 *
 * @property subtitle Line under the provider's name, or `null` for none.
 * @property action What pressing the row does.
 */
internal data class LinkedAccountPresentation(
    val subtitle: String?,
    val action: LinkedAccountAction,
) {
    /** Construction from the row's inputs. */
    companion object {
        /**
         * Works out the row's line and action.
         *
         * An unlinked provider shows [notLinkedLabel] and links; a linked one shows [account] and
         * unlinks; a linked one that may not be unlinked shows [lockedReason] — else [account] — and
         * does nothing.
         *
         * @param isLinked Whether the account can sign in through the provider.
         * @param canUnlink Whether unlinking is allowed.
         * @param account Identity at the provider, or `null`.
         * @param lockedReason Why the provider cannot be unlinked, or `null`.
         * @param notLinkedLabel Line while the provider is not linked, or `null`.
         * @return The row's line and action.
         */
        fun of(
            isLinked: Boolean,
            canUnlink: Boolean,
            account: String?,
            lockedReason: String?,
            notLinkedLabel: String?,
        ): LinkedAccountPresentation = when {
            !isLinked -> LinkedAccountPresentation(subtitle = notLinkedLabel, action = LinkedAccountAction.Link)
            !canUnlink -> LinkedAccountPresentation(subtitle = lockedReason ?: account, action = LinkedAccountAction.None)
            else -> LinkedAccountPresentation(subtitle = account, action = LinkedAccountAction.Unlink)
        }
    }
}
