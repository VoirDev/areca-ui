package dev.voir.areca.flow.profile.session

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import dev.voir.areca.flow.profile.icon.BundledIcons
import dev.voir.areca.flow.profile.icon.Hand
import dev.voir.areca.flow.profile.rememberLastNonNull
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.button.ButtonSize
import dev.voir.areca.foundation.button.ButtonStyle
import dev.voir.areca.foundation.feedback.Fallback
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.foundation.settings.Section
import dev.voir.areca.foundation.settings.SettingsBlock
import dev.voir.areca.foundation.settings.SettingsRow
import dev.voir.areca.foundation.settings.SettingsRowSkeleton
import dev.voir.areca.foundation.settings.SettingsRowStatus
import dev.voir.areca.foundation.settings.SettingsRowTone
import dev.voir.areca.foundation.settings.SettingsScope
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.theme.ArecaTheme

/**
 * The devices the account is signed in on, as the two sections a devices screen is built from.
 *
 * **This device** — the session this application is signed in with, on its own card, and under it
 * the one destructive action the screen has: signing every other session out. The footer says what
 * that does. The section is drawn once the list holds a session marked [SessionItem.isCurrent] — a
 * placeholder stands in while it first loads — and the action only when, on top of that,
 * [onSignOutOthers] is given and there is another session. Once this device has been shown it stays
 * while the list reloads or fails to, since a failed fetch does not stop it being this device; the
 * action waits for a list it can act on.
 *
 * **Active sessions** — every other session, each pressed to act on it (`RevokeSessionSheet`). While
 * the list loads, both cards hold placeholder rows at their final height, announced as
 * [loadingDescription]; if it could not be fetched, the card says so and offers to try again, and the
 * sections around it keep working. With no other session, the card says that in a line instead of
 * disappearing.
 *
 * Emitted into a `Settings` list, between whatever sections the screen puts around it — a promotional
 * block above, an auto-terminate choice below.
 *
 * @param state What to draw.
 * @param onSessionClick Called with a session other than this one when it is pressed, or `null` for
 *   a list that only states. This device is always inert here: ending it is signing out.
 * @param onSignOutOthers Called when every other session is asked to end — pair it with
 *   `SignOutOthersSheet` — or `null` to offer no such action.
 * @param onRetry Called when a failed load offers to try again.
 * @param footer Line under the active sessions, such as where else the application is available, or
 *   `null` for none.
 * @param thisDeviceHeader Header of the current session's section.
 * @param activeSessionsHeader Header of the other sessions' section.
 * @param signOutOthersLabel Title of the row that ends every other session.
 * @param signOutOthersFooter Line under the current session saying what that row does, or `null`
 *   for none.
 * @param signOutOthersClickLabel What pressing the row that ends every other session does, for
 *   assistive technology — "Review and confirm" when it opens a sheet — or `null` for none.
 * @param sessionClickLabel What pressing a session does, for assistive technology, or `null`.
 * @param emptyTitle Title of the row standing in for an empty list of other sessions.
 * @param emptySubtitle Line under [emptyTitle], or `null` for none.
 * @param failedTitle Heading of the other sessions' card when the list could not be fetched.
 * @param retryLabel Label of the retry button in that card.
 * @param loadingDescription What the placeholder rows stand for, for assistive technology.
 */
@Composable
public fun SettingsScope.ActiveSessionsSections(
    state: ActiveSessionsState,
    onSessionClick: ((SessionItem) -> Unit)?,
    onSignOutOthers: (() -> Unit)?,
    onRetry: () -> Unit,
    footer: String? = null,
    thisDeviceHeader: String = "This device",
    activeSessionsHeader: String = "Active sessions",
    signOutOthersLabel: String = "Sign out all other devices",
    signOutOthersFooter: String? = null,
    signOutOthersClickLabel: String? = null,
    sessionClickLabel: String? = null,
    emptyTitle: String = "No other devices",
    emptySubtitle: String? = null,
    failedTitle: String = "Couldn't load your devices",
    retryLabel: String = "Try again",
    loadingDescription: String = "Loading devices",
) {
    val loaded = state as? ActiveSessionsState.Loaded
    val groups = SessionGroups.of(loaded?.sessions.orEmpty())
    val others = groups.others
    val signingOut = loaded?.isSigningOutOthers == true
    val showSignOutOthers = onSignOutOthers != null && groups.canSignOutOthers
    // A loaded list is the authority on which session is this one; between lists, the last one is.
    val heldCurrent = rememberLastNonNull(groups.current)
    val current = if (loaded != null) groups.current else heldCurrent

    if (current != null) {
        Section(
            header = { Text(thisDeviceHeader) },
            footer = if (showSignOutOthers && signOutOthersFooter != null) {
                { Text(signOutOthersFooter) }
            } else {
                null
            },
            // The way out of every other session closes this card on the danger band rather than
            // as one more row behind a hairline: it acts on the list below, not on this device.
            bandColor = ArecaTheme.colors.bgDangerMuted,
            band = if (showSignOutOthers) {
                {
                    SettingsRow(
                        title = { Text(signOutOthersLabel) },
                        leading = { Icon(BundledIcons.Hand, contentDescription = null) },
                        tone = SettingsRowTone.Danger,
                        status = if (signingOut) SettingsRowStatus.Loading else SettingsRowStatus.Idle,
                        onClick = onSignOutOthers,
                        onClickLabel = signOutOthersClickLabel,
                    )
                }
            } else {
                null
            },
        ) {
            SessionRow(session = current)
        }
    } else if (state == ActiveSessionsState.Loading) {
        // Held at its final height, so the card does not appear above the list when it arrives.
        Section(header = { Text(thisDeviceHeader) }) {
            LoadingRow(description = loadingDescription)
        }
    }

    Section(
        header = { Text(activeSessionsHeader) },
        footer = footer?.let { text -> { Text(text) } },
    ) {
        when (state) {
            ActiveSessionsState.Loading -> repeat(PlaceholderRowCount) { index ->
                // The card is announced once, by its first row; the rest only hold the height.
                LoadingRow(description = loadingDescription.takeIf { index == 0 })
            }

            is ActiveSessionsState.Failed -> SessionsFailure(
                title = failedTitle,
                message = state.message,
                retryLabel = retryLabel,
                onRetry = onRetry,
            )

            is ActiveSessionsState.Loaded -> if (others.isEmpty()) {
                SettingsRow(
                    title = { Text(emptyTitle) },
                    subtitle = emptySubtitle?.let { line -> { Text(line) } },
                )
            } else {
                others.forEach { session ->
                    key(session.id) {
                        SessionRow(
                            session = session,
                            enabled = !signingOut,
                            onClick = onSessionClick?.let { click -> { click(session) } },
                            onClickLabel = sessionClickLabel,
                        )
                    }
                }
            }
        }
    }
}

/**
 * One placeholder row, at a session row's height.
 *
 * @param description What the placeholder stands for, read by assistive technology as one item, or
 *   `null` for a row that only holds the height.
 */
@Composable
private fun LoadingRow(description: String?) {
    SettingsRowSkeleton(
        modifier = if (description != null) {
            Modifier.clearAndSetSemantics { contentDescription = description }
        } else {
            Modifier
        },
        showLeading = true,
        showSubtitle = true,
    )
}

/**
 * The section's own failure: what went wrong, and one way to try again, inside the card.
 *
 * A foundation [Fallback], since it stands in for part of the screen; the card's own padding is
 * dropped for the fallback's.
 *
 * @param title What happened.
 * @param message Why the list could not be fetched.
 * @param retryLabel Label of the retry button.
 * @param onRetry Called to try again.
 */
@Composable
private fun SessionsFailure(
    title: String,
    message: String,
    retryLabel: String,
    onRetry: () -> Unit,
) {
    SettingsBlock(contentPadding = PaddingValues(ArecaTheme.spacing.none)) {
        Fallback(
            heading = { Text(title) },
            body = { Text(message) },
            content = {
                Button(
                    onClick = onRetry,
                    style = ButtonStyle.soft(ActionColor.Neutral),
                    size = ButtonSize.Small,
                ) {
                    Text(retryLabel)
                }
            },
        )
    }
}

/**
 * A loaded list of sessions, split the way [ActiveSessionsSections] draws it.
 *
 * @property current The session this application is signed in with, or `null` when the list marks
 *   none — the first one marked, should the application mark several.
 * @property others Every session not marked current, in the list's order.
 */
internal data class SessionGroups(
    val current: SessionItem?,
    val others: List<SessionItem>,
) {
    /**
     * Whether "sign out all other devices" has a place and a purpose: a current session to sit
     * under, and another session to end.
     */
    val canSignOutOthers: Boolean
        get() = current != null && others.isNotEmpty()

    /** Construction from a list of sessions. */
    companion object {
        /**
         * Splits a list into this device and the rest.
         *
         * @param sessions Every session, in the order to draw them.
         * @return The split.
         */
        fun of(sessions: List<SessionItem>): SessionGroups = SessionGroups(
            current = sessions.firstOrNull { it.isCurrent },
            others = sessions.filterNot { it.isCurrent },
        )
    }
}

/**
 * How many placeholder rows a loading section draws.
 *
 * Two: enough to read as a list, few enough that the card does not shrink much when an account with
 * one other session answers.
 */
private const val PlaceholderRowCount = 2
