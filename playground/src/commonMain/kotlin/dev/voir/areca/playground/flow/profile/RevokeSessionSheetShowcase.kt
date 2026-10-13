package dev.voir.areca.playground.flow.profile

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.voir.areca.flow.profile.session.SessionItem
import dev.voir.areca.flow.profile.session.SessionRow
import dev.voir.areca.flow.profile.sheet.RevokeSessionSheet
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.button.ButtonSize
import dev.voir.areca.foundation.button.ButtonStyle
import dev.voir.areca.foundation.settings.Section
import dev.voir.areca.foundation.settings.Settings
import dev.voir.areca.foundation.settings.SettingsRow
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.Variants
import kotlinx.coroutines.delay

/** Showcase for [RevokeSessionSheet]. */
@Composable
fun RevokeSessionSheetShowcase() = ShowcaseScaffold(
    title = "Sign out device sheet",
    summary = "Asks once before signing out one other device, naming it so the right one goes.",
    module = "flow-profile",
    status = ShowcaseStatus.Beta,
) {
    Playground(description = "Pick a device, open the sheet and confirm: it spins, then closes.") {
        val session by enumControl(
            key = "session",
            default = ProfileSamples.OtherSession,
            options = ProfileSamples.Sessions.filterNot { it.isCurrent },
            optionLabel = { it.name },
        )
        val details by booleanControl(
            key = "client and seen",
            default = true,
            description = "The description names the device, then what it runs and where it was seen.",
        )
        var confirming by booleanControl(
            key = "confirming",
            default = false,
            description = "Signing the device out is in flight: the sheet refuses to close. The preview " +
                "finishes after a moment and closes it.",
        )

        preview {
            var target by remember { mutableStateOf<SessionItem?>(null) }

            LaunchedEffect(target, confirming) {
                if (target != null && confirming) {
                    delay(WorkDelayMillis)
                    confirming = false
                    target = null
                }
            }

            Button(
                text = "Open sheet",
                onClick = {
                    target = if (details) session else session.copy(client = null, seen = null)
                },
                style = ButtonStyle.soft(ActionColor.Neutral),
                size = ButtonSize.Small,
            )

            RevokeSessionSheet(
                session = target,
                onConfirm = { confirming = true },
                onDismiss = { target = null },
                confirming = confirming,
            )
        }
    }

    Variants(
        title = "Description",
        description = "Whatever the session carries is joined with a dot; missing details leave no gap.",
    ) {
        variant(name = "Name · client · seen", detail = ProfileSamples.OtherSession.name) {
            var target by remember { mutableStateOf<SessionItem?>(null) }

            Button(
                text = "Open sheet",
                onClick = { target = ProfileSamples.OtherSession },
                style = ButtonStyle.soft(ActionColor.Neutral),
                size = ButtonSize.Small,
            )

            RevokeSessionSheet(
                session = target,
                onConfirm = { target = null },
                onDismiss = { target = null },
            )
        }
        variant(name = "Name only", detail = "client and seen null") {
            var target by remember { mutableStateOf<SessionItem?>(null) }

            Button(
                text = "Open sheet",
                onClick = { target = ProfileSamples.OtherSession.copy(client = null, seen = null) },
                style = ButtonStyle.soft(ActionColor.Neutral),
                size = ButtonSize.Small,
            )

            RevokeSessionSheet(
                session = target,
                onConfirm = { target = null },
                onDismiss = { target = null },
            )
        }
    }

    States {
        variant(name = "Default", detail = "Both buttons take presses") {
            var target by remember { mutableStateOf<SessionItem?>(null) }

            Button(
                text = "Open sheet",
                onClick = { target = ProfileSamples.OtherSession },
                style = ButtonStyle.soft(ActionColor.Neutral),
                size = ButtonSize.Small,
            )

            RevokeSessionSheet(
                session = target,
                onConfirm = { target = null },
                onDismiss = { target = null },
            )
        }
        variant(name = "Confirming", detail = "Spinner, no way out; closes after a moment") {
            var target by remember { mutableStateOf<SessionItem?>(null) }

            LaunchedEffect(target) {
                if (target != null) {
                    delay(WorkDelayMillis)
                    target = null
                }
            }

            Button(
                text = "Open sheet",
                onClick = { target = ProfileSamples.OtherSession },
                style = ButtonStyle.soft(ActionColor.Neutral),
                size = ButtonSize.Small,
            )

            RevokeSessionSheet(
                session = target,
                onConfirm = {},
                onDismiss = { target = null },
                confirming = true,
            )
        }
    }

    Recipes {
        recipe(
            name = "Active sessions list",
            description = "A pressed device opens the sheet; confirmed, the sheet spins and the device leaves.",
        ) {
            var sessions by remember { mutableStateOf(ProfileSamples.Sessions.filterNot { it.isCurrent }) }
            var target by remember { mutableStateOf<SessionItem?>(null) }
            var revoking by remember { mutableStateOf(false) }

            LaunchedEffect(revoking) {
                val session = target
                if (revoking && session != null) {
                    delay(WorkDelayMillis)
                    sessions = sessions.filterNot { it.id == session.id }
                    revoking = false
                    target = null
                }
            }

            Settings(modifier = Modifier.fillMaxWidth()) {
                Section(header = { Text("Active sessions") }) {
                    if (sessions.isEmpty()) {
                        SettingsRow(
                            title = { Text("No other devices") },
                            subtitle = { Text("Only this device is signed in") },
                        )
                    }
                    sessions.forEach { session ->
                        key(session.id) {
                            SessionRow(session = session, onClick = { target = session })
                        }
                    }
                }
            }

            RevokeSessionSheet(
                session = target,
                onConfirm = { revoking = true },
                onDismiss = { target = null },
                confirming = revoking,
            )
        }
    }

    EdgeCases {
        case(
            name = "Very long device name",
            expectation = "The description wraps, centred, and keeps every detail; nothing is cut.",
        ) {
            var target by remember { mutableStateOf<SessionItem?>(null) }

            Button(
                text = "Open sheet",
                onClick = {
                    target = ProfileSamples.OtherSession.copy(
                        name = "Ada's MacBook Pro (16-inch, Nov 2023) — Analytical Engine office",
                        client = "Areca for macOS 7.2.9 arm64, Developer ID build, beta channel",
                        seen = "Kragujevac, Šumadija District, Serbia · 10:18",
                    )
                },
                style = ButtonStyle.soft(ActionColor.Neutral),
                size = ButtonSize.Small,
            )

            RevokeSessionSheet(
                session = target,
                onConfirm = { target = null },
                onDismiss = { target = null },
            )
        }
        case(
            name = "Opened from a narrow container",
            expectation = "The button sits in 200dp; the sheet still spans the window, not the container.",
        ) {
            var target by remember { mutableStateOf<SessionItem?>(null) }

            Button(
                text = "Open sheet",
                onClick = { target = ProfileSamples.OtherSession },
                modifier = Modifier.width(200.dp),
                style = ButtonStyle.soft(ActionColor.Neutral),
                size = ButtonSize.Small,
            )

            RevokeSessionSheet(
                session = target,
                onConfirm = { target = null },
                onDismiss = { target = null },
            )
        }
    }
}

/** How long the pretend sign-out takes, so the in-flight sheet can be seen. */
private const val WorkDelayMillis: Long = 2_000
