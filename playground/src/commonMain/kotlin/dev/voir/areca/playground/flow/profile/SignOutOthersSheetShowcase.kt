package dev.voir.areca.playground.flow.profile

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
import dev.voir.areca.flow.profile.sheet.SignOutOthersSheet
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.button.ButtonSize
import dev.voir.areca.foundation.button.ButtonStyle
import dev.voir.areca.foundation.settings.Settings
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.States
import kotlinx.coroutines.delay

/** Showcase for [SignOutOthersSheet]. */
@Composable
fun SignOutOthersSheetShowcase() = ShowcaseScaffold(
    title = "Sign out other devices sheet",
    summary = "Asks once before ending every session but this one, and reassures that this device " +
        "stays signed in.",
    module = "flow-profile",
    status = ShowcaseStatus.Beta,
) {
    Playground(description = "Open the sheet and confirm: it spins while the devices sign out, then closes.") {
        var confirming by booleanControl(
            key = "confirming",
            default = false,
            description = "Ending the sessions is in flight: the sheet refuses to close. The preview " +
                "finishes after a moment and closes it.",
        )

        preview {
            var visible by remember { mutableStateOf(false) }

            LaunchedEffect(visible, confirming) {
                if (visible && confirming) {
                    delay(WorkDelayMillis)
                    confirming = false
                    visible = false
                }
            }

            Button(
                text = "Open sheet",
                onClick = { visible = true },
                style = ButtonStyle.soft(ActionColor.Neutral),
                size = ButtonSize.Small,
            )

            SignOutOthersSheet(
                visible = visible,
                onConfirm = { confirming = true },
                onDismiss = { visible = false },
                confirming = confirming,
            )
        }
    }

    States {
        variant(name = "Default", detail = "Both buttons take presses") {
            var visible by remember { mutableStateOf(false) }

            Button(
                text = "Open sheet",
                onClick = { visible = true },
                style = ButtonStyle.soft(ActionColor.Neutral),
                size = ButtonSize.Small,
            )

            SignOutOthersSheet(
                visible = visible,
                onConfirm = { visible = false },
                onDismiss = { visible = false },
            )
        }
        variant(name = "Confirming", detail = "Spinner, no way out; closes after a moment") {
            var visible by remember { mutableStateOf(false) }

            LaunchedEffect(visible) {
                if (visible) {
                    delay(WorkDelayMillis)
                    visible = false
                }
            }

            Button(
                text = "Open sheet",
                onClick = { visible = true },
                style = ButtonStyle.soft(ActionColor.Neutral),
                size = ButtonSize.Small,
            )

            SignOutOthersSheet(
                visible = visible,
                onConfirm = {},
                onDismiss = { visible = false },
                confirming = true,
            )
        }
    }

    Recipes {
        recipe(
            name = "Devices screen",
            description = "The sign-out row opens the sheet; once confirmed only this device is left.",
        ) {
            var sessions by remember { mutableStateOf(ProfileSamples.Sessions) }
            var visible by remember { mutableStateOf(false) }
            var signingOut by remember { mutableStateOf(false) }

            LaunchedEffect(signingOut) {
                if (signingOut) {
                    delay(WorkDelayMillis)
                    sessions = sessions.filter { it.isCurrent }
                    signingOut = false
                    visible = false
                }
            }

            Settings(modifier = Modifier.fillMaxWidth()) {
                ActiveSessionsSections(
                    state = ActiveSessionsState.Loaded(sessions, isSigningOutOthers = signingOut),
                    onSessionClick = {},
                    onSignOutOthers = { visible = true },
                    onRetry = {},
                )
            }

            SignOutOthersSheet(
                visible = visible,
                onConfirm = { signingOut = true },
                onDismiss = { visible = false },
                confirming = signingOut,
            )
        }
    }

    EdgeCases {
        case(
            name = "Long translated copy",
            expectation = "Title, description and note wrap, centred; the confirm label stays on one line.",
        ) {
            var visible by remember { mutableStateOf(false) }

            Button(
                text = "Open sheet",
                onClick = { visible = true },
                style = ButtonStyle.soft(ActionColor.Neutral),
                size = ButtonSize.Small,
            )

            SignOutOthersSheet(
                title = "Sign out of every phone, tablet, computer and browser except this one?",
                description = "Each of them will need your address and password, or a sign-in " +
                    "link, before it can use your account again.",
                note = "You'll stay signed in on this device, and nothing on it will change.",
                confirmText = "Sign out every other device now",
                visible = visible,
                onConfirm = { visible = false },
                onDismiss = { visible = false },
            )
        }
        case(
            name = "Opened from a narrow container",
            expectation = "The button sits in 200dp; the sheet still spans the window, not the container.",
        ) {
            var visible by remember { mutableStateOf(false) }

            Button(
                text = "Open sheet",
                onClick = { visible = true },
                modifier = Modifier.width(200.dp),
                style = ButtonStyle.soft(ActionColor.Neutral),
                size = ButtonSize.Small,
            )

            SignOutOthersSheet(
                visible = visible,
                onConfirm = { visible = false },
                onDismiss = { visible = false },
            )
        }
    }
}

/** How long the pretend sign-out takes, so the in-flight sheet can be seen. */
private const val WorkDelayMillis: Long = 2_000
