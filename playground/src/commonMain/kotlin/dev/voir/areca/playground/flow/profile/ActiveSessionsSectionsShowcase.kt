package dev.voir.areca.playground.flow.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.voir.areca.flow.profile.session.ActiveSessionsSections
import dev.voir.areca.flow.profile.session.ActiveSessionsState
import dev.voir.areca.flow.profile.session.SessionItem
import dev.voir.areca.flow.profile.sheet.RevokeSessionSheet
import dev.voir.areca.flow.profile.sheet.SignOutOthersSheet
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.button.ButtonSize
import dev.voir.areca.foundation.button.ButtonStyle
import dev.voir.areca.foundation.settings.Settings
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.section.Anatomy
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import kotlinx.coroutines.delay

/** Which [ActiveSessionsState] the playground sections draw. */
private enum class StateKind(
    /** Label of the option in the control panel. */
    val label: String,
) {
    /** Loaded, with this device and two others. */
    Loaded("Loaded"),

    /** Loaded, with this device alone. */
    OnlyCurrent("Only this device"),

    /** The list has not arrived. */
    Loading("Loading"),

    /** The list could not be fetched. */
    Failed("Failed"),
}

/** Showcase for [ActiveSessionsSections] and the [ActiveSessionsState] it draws. */
@Composable
fun ActiveSessionsSectionsShowcase() = ShowcaseScaffold(
    title = "Active sessions",
    summary = "The two sections a devices screen is built from: this device with the way to sign " +
        "out every other, and the other devices, each pressed to sign it out.",
    module = "flow-profile",
    status = ShowcaseStatus.Beta,
) {
    Playground(description = "Switch the state and press rows; the last event is written under the list.") {
        val kind by enumControl(
            key = "state",
            default = StateKind.Loaded,
            options = StateKind.entries,
            optionLabel = { it.label },
        )
        val signingOut by booleanControl(
            key = "isSigningOutOthers",
            default = false,
            description = "Loaded only: the sign-out row spins and other devices stop taking presses.",
        )
        val revoking by booleanControl(
            key = "one revoking",
            default = false,
            description = "Loaded only: the MacBook Pro is being signed out.",
        )
        val message by textControl(
            key = "failure message",
            default = FailureMessage,
            description = "Failed only: why the list could not be fetched.",
        )
        val footer by booleanControl("footer", default = true)

        preview {
            var event by remember { mutableStateOf("Nothing pressed yet") }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Settings(modifier = Modifier.fillMaxWidth()) {
                    ActiveSessionsSections(
                        state = when (kind) {
                            StateKind.Loaded -> ActiveSessionsState.Loaded(
                                sessions = ProfileSamples.Sessions.map { session ->
                                    session.copy(
                                        isRevoking = revoking && session.id == ProfileSamples.OtherSession.id,
                                    )
                                },
                                isSigningOutOthers = signingOut,
                            )

                            StateKind.OnlyCurrent -> ActiveSessionsState.Loaded(
                                sessions = ProfileSamples.Sessions.take(1),
                            )

                            StateKind.Loading -> ActiveSessionsState.Loading
                            StateKind.Failed -> ActiveSessionsState.Failed(message)
                        },
                        onSessionClick = { session -> event = "Pressed ${session.name}" },
                        onSignOutOthers = { event = "Asked to sign out every other device" },
                        onRetry = { event = "Asked to try again" },
                        footer = FooterText.takeIf { footer },
                    )
                }

                Text(text = event, variant = TextVariant.BodySmall, color = TextColor.Muted)
            }
        }
    }

    Anatomy(description = "Two sections emitted into a Settings list, and the footer under the second.") {
        slot(
            name = "This device",
            description = "The current session on its own card, with the sign-out row and its footer " +
                "when there is anything else to sign out.",
        ) {
            Settings(modifier = Modifier.fillMaxWidth()) {
                ActiveSessionsSections(
                    state = ActiveSessionsState.Loaded(ProfileSamples.Sessions),
                    onSessionClick = {},
                    onSignOutOthers = {},
                    onRetry = {},
                )
            }
        }
        slot(
            name = "Active sessions",
            description = "Every other session, or placeholders, a failure, or a line saying there are none.",
        ) {
            Settings(modifier = Modifier.fillMaxWidth()) {
                ActiveSessionsSections(
                    state = ActiveSessionsState.Loading,
                    onSessionClick = {},
                    onSignOutOthers = {},
                    onRetry = {},
                )
            }
        }
        slot(
            name = "footer",
            description = "The application's own line under the active sessions.",
        ) {
            Settings(modifier = Modifier.fillMaxWidth()) {
                ActiveSessionsSections(
                    state = ActiveSessionsState.Loaded(ProfileSamples.Sessions.take(1)),
                    onSessionClick = {},
                    onSignOutOthers = {},
                    onRetry = {},
                    footer = FooterText,
                )
            }
        }
    }

    Variants(
        title = "What the list holds",
        description = "With nothing else to sign out, the sign-out row and its footer go and the second " +
            "card says so in a line.",
        layout = VariantLayout.Vertical,
    ) {
        variant(name = "Other devices", detail = "Loaded, three sessions") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                ActiveSessionsSections(
                    state = ActiveSessionsState.Loaded(ProfileSamples.Sessions),
                    onSessionClick = {},
                    onSignOutOthers = {},
                    onRetry = {},
                )
            }
        }
        variant(name = "Only this device", detail = "Loaded, the current session alone") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                ActiveSessionsSections(
                    state = ActiveSessionsState.Loaded(ProfileSamples.Sessions.take(1)),
                    onSessionClick = {},
                    onSignOutOthers = {},
                    onRetry = {},
                )
            }
        }
        variant(name = "With footer", detail = "footer = \"$FooterText\"") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                ActiveSessionsSections(
                    state = ActiveSessionsState.Loaded(ProfileSamples.Sessions),
                    onSessionClick = {},
                    onSignOutOthers = {},
                    onRetry = {},
                    footer = FooterText,
                )
            }
        }
    }

    States(layout = VariantLayout.Vertical) {
        variant(name = "Loading", detail = "Placeholder rows at their final height") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                ActiveSessionsSections(
                    state = ActiveSessionsState.Loading,
                    onSessionClick = {},
                    onSignOutOthers = {},
                    onRetry = {},
                )
            }
        }
        variant(name = "Failed", detail = "The card says so and offers to try again") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                ActiveSessionsSections(
                    state = ActiveSessionsState.Failed(FailureMessage),
                    onSessionClick = {},
                    onSignOutOthers = {},
                    onRetry = {},
                )
            }
        }
        variant(name = "Signing out others", detail = "isSigningOutOthers: rows disabled") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                ActiveSessionsSections(
                    state = ActiveSessionsState.Loaded(ProfileSamples.Sessions, isSigningOutOthers = true),
                    onSessionClick = {},
                    onSignOutOthers = {},
                    onRetry = {},
                )
            }
        }
        variant(name = "One device signing out", detail = "isRevoking on the MacBook Pro") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                ActiveSessionsSections(
                    state = ActiveSessionsState.Loaded(
                        ProfileSamples.Sessions.map { session ->
                            session.copy(isRevoking = session.id == ProfileSamples.OtherSession.id)
                        },
                    ),
                    onSessionClick = {},
                    onSignOutOthers = {},
                    onRetry = {},
                )
            }
        }
    }

    Recipes {
        recipe(
            name = "Devices screen",
            description = "Loads, then signs out one device through its sheet or every other one at once.",
        ) {
            var loading by remember { mutableStateOf(true) }
            var sessions by remember { mutableStateOf(ProfileSamples.Sessions) }
            var pending by remember { mutableStateOf<SessionItem?>(null) }
            var revokingId by remember { mutableStateOf<String?>(null) }
            var askingSignOutOthers by remember { mutableStateOf(false) }
            var signingOutOthers by remember { mutableStateOf(false) }

            LaunchedEffect(loading) {
                if (loading) {
                    delay(WorkDelayMillis)
                    loading = false
                }
            }
            LaunchedEffect(revokingId) {
                val id = revokingId ?: return@LaunchedEffect
                delay(WorkDelayMillis)
                sessions = sessions.filterNot { it.id == id }
                revokingId = null
            }
            LaunchedEffect(signingOutOthers) {
                if (signingOutOthers) {
                    delay(WorkDelayMillis)
                    sessions = sessions.filter { it.isCurrent }
                    signingOutOthers = false
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Settings(modifier = Modifier.fillMaxWidth()) {
                    ActiveSessionsSections(
                        state = if (loading) {
                            ActiveSessionsState.Loading
                        } else {
                            ActiveSessionsState.Loaded(
                                sessions = sessions.map { it.copy(isRevoking = it.id == revokingId) },
                                isSigningOutOthers = signingOutOthers,
                            )
                        },
                        onSessionClick = { session -> pending = session },
                        onSignOutOthers = { askingSignOutOthers = true },
                        onRetry = { loading = true },
                        footer = FooterText,
                    )
                }

                Button(
                    text = "Start over",
                    onClick = {
                        sessions = ProfileSamples.Sessions
                        loading = true
                    },
                    style = ButtonStyle.soft(ActionColor.Neutral),
                    size = ButtonSize.Small,
                )
            }

            RevokeSessionSheet(
                session = pending,
                onConfirm = { session ->
                    pending = null
                    revokingId = session.id
                },
                onDismiss = { pending = null },
            )

            SignOutOthersSheet(
                visible = askingSignOutOthers,
                onConfirm = {
                    askingSignOutOthers = false
                    signingOutOthers = true
                },
                onDismiss = { askingSignOutOthers = false },
            )
        }
        recipe(
            name = "Failed load, then retry",
            description = "The failure stays inside its card; trying again shows placeholders, then the list.",
        ) {
            var state by remember {
                mutableStateOf<ActiveSessionsState>(ActiveSessionsState.Failed(FailureMessage))
            }

            LaunchedEffect(state) {
                if (state == ActiveSessionsState.Loading) {
                    delay(WorkDelayMillis)
                    state = ActiveSessionsState.Loaded(ProfileSamples.Sessions)
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Settings(modifier = Modifier.fillMaxWidth()) {
                    ActiveSessionsSections(
                        state = state,
                        onSessionClick = {},
                        onSignOutOthers = {},
                        onRetry = { state = ActiveSessionsState.Loading },
                    )
                }

                Button(
                    text = "Fail again",
                    onClick = { state = ActiveSessionsState.Failed(FailureMessage) },
                    style = ButtonStyle.soft(ActionColor.Neutral),
                    size = ButtonSize.Small,
                )
            }
        }
    }

    EdgeCases {
        case(
            name = "Very long device name",
            expectation = "Each line of a session ellipsises on its own line; the cards keep their width.",
        ) {
            Settings(modifier = Modifier.fillMaxWidth()) {
                ActiveSessionsSections(
                    state = ActiveSessionsState.Loaded(
                        ProfileSamples.Sessions + ProfileSamples.OtherSession.copy(
                            id = "long",
                            name = "Ada's MacBook Pro (16-inch, Nov 2023) — Analytical Engine office",
                            client = "Areca for macOS 7.2.9 arm64, Developer ID build, beta channel",
                        ),
                    ),
                    onSessionClick = {},
                    onSignOutOthers = {},
                    onRetry = {},
                )
            }
        }
        case(
            name = "Long failure message",
            expectation = "The message wraps, centred, and Try again stays under it inside the card.",
        ) {
            Settings(modifier = Modifier.fillMaxWidth()) {
                ActiveSessionsSections(
                    state = ActiveSessionsState.Failed(
                        "The device list is temporarily unavailable while we move your account to a " +
                            "new region. Your devices are still signed in, and nothing needs doing.",
                    ),
                    onSessionClick = {},
                    onSignOutOthers = {},
                    onRetry = {},
                )
            }
        }
        case(
            name = "No current session in the list",
            expectation = "The This device section is left out; the others are still listed.",
        ) {
            Settings(modifier = Modifier.fillMaxWidth()) {
                ActiveSessionsSections(
                    state = ActiveSessionsState.Loaded(ProfileSamples.Sessions.drop(1)),
                    onSessionClick = {},
                    onSignOutOthers = {},
                    onRetry = {},
                )
            }
        }
        case(
            name = "Narrow container",
            expectation = "Constrained to 260dp: the footers wrap and the session lines ellipsise.",
        ) {
            Settings(modifier = Modifier.width(260.dp)) {
                ActiveSessionsSections(
                    state = ActiveSessionsState.Loaded(ProfileSamples.Sessions),
                    onSessionClick = {},
                    onSignOutOthers = {},
                    onRetry = {},
                    footer = FooterText,
                )
            }
        }
    }
}

/** A footer an application would put under its active sessions. */
private const val FooterText: String = "Areca is also available for Windows, macOS and Linux."

/** Why the sample list could not be fetched. */
private const val FailureMessage: String = "Check your connection and try again."

/** How long the recipes' pretend network work takes, so the in-between states can be seen. */
private const val WorkDelayMillis: Long = 1_500
