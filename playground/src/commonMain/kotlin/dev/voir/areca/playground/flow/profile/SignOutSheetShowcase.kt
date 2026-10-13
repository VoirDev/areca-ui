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
import dev.voir.areca.flow.profile.row.SignOutRow
import dev.voir.areca.flow.profile.sheet.SignOutSheet
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.button.ButtonSize
import dev.voir.areca.foundation.button.ButtonStyle
import dev.voir.areca.foundation.settings.Section
import dev.voir.areca.foundation.settings.Settings
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.Variants
import kotlinx.coroutines.delay

/** Showcase for [SignOutSheet]. */
@Composable
fun SignOutSheetShowcase() = ShowcaseScaffold(
    title = "Sign-out sheet",
    summary = "Asks once before signing out of this device, and says what that costs when anything is " +
        "at stake.",
    module = "flow-profile",
    status = ShowcaseStatus.Beta,
) {
    Playground(description = "Open the sheet and confirm: it spins while signing out, then closes.") {
        var confirming by booleanControl(
            key = "confirming",
            default = false,
            description = "Signing out is in flight: the sheet refuses to close. The preview finishes " +
                "after a moment and closes it.",
        )
        val withNote by booleanControl("note", default = true)
        val note by textControl(
            key = "note text",
            default = UnsyncedNote,
            description = "Drawn in the warning colour.",
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

            SignOutSheet(
                visible = visible,
                onConfirm = { confirming = true },
                onDismiss = { visible = false },
                confirming = confirming,
                note = note.takeIf { withNote },
            )
        }
    }

    Variants(
        title = "Note",
        description = "A note only when signing out loses something; otherwise the sheet is a plain question.",
    ) {
        variant(name = "No note", detail = "note = null") {
            var visible by remember { mutableStateOf(false) }

            Button(
                text = "Open sheet",
                onClick = { visible = true },
                style = ButtonStyle.soft(ActionColor.Neutral),
                size = ButtonSize.Small,
            )

            SignOutSheet(
                visible = visible,
                onConfirm = { visible = false },
                onDismiss = { visible = false },
            )
        }
        variant(name = "Note", detail = "Unsynchronised changes") {
            var visible by remember { mutableStateOf(false) }

            Button(
                text = "Open sheet",
                onClick = { visible = true },
                style = ButtonStyle.soft(ActionColor.Neutral),
                size = ButtonSize.Small,
            )

            SignOutSheet(
                visible = visible,
                onConfirm = { visible = false },
                onDismiss = { visible = false },
                note = UnsyncedNote,
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

            SignOutSheet(
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

            SignOutSheet(
                visible = visible,
                onConfirm = {},
                onDismiss = { visible = false },
                confirming = true,
            )
        }
    }

    Recipes {
        recipe(
            name = "Account screen",
            description = "The sign-out row opens the sheet; the sheet spins until signing out is done.",
        ) {
            var visible by remember { mutableStateOf(false) }
            var signingOut by remember { mutableStateOf(false) }

            LaunchedEffect(signingOut) {
                if (signingOut) {
                    delay(WorkDelayMillis)
                    signingOut = false
                    visible = false
                }
            }

            Settings(modifier = Modifier.fillMaxWidth()) {
                Section(footer = { Text("Signed in as ada@analytical.engine") }) {
                    SignOutRow(onClick = { visible = true })
                }
            }

            SignOutSheet(
                visible = visible,
                onConfirm = { signingOut = true },
                onDismiss = { visible = false },
                confirming = signingOut,
                note = UnsyncedNote,
            )
        }
    }

    EdgeCases {
        case(
            name = "Long translated copy",
            expectation = "Title, description and note wrap, centred; the buttons stay full width below.",
        ) {
            var visible by remember { mutableStateOf(false) }

            Button(
                text = "Open sheet",
                onClick = { visible = true },
                style = ButtonStyle.soft(ActionColor.Neutral),
                size = ButtonSize.Small,
            )

            SignOutSheet(
                title = "Sign out of your account on this computer?",
                description = "You will need your address and password, or a sign-in link, to use " +
                    "your account on this computer again. Other devices stay signed in.",
                confirmText = "Sign out of this computer",
                visible = visible,
                onConfirm = { visible = false },
                onDismiss = { visible = false },
                note = "3 changes made offline in the last hour have not been uploaded yet and " +
                    "will be lost if you sign out now.",
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

            SignOutSheet(
                visible = visible,
                onConfirm = { visible = false },
                onDismiss = { visible = false },
                note = UnsyncedNote,
            )
        }
    }
}

/** What signing out costs on the sample device. */
private const val UnsyncedNote: String = "3 changes on this device haven't been uploaded yet."

/** How long the pretend sign-out takes, so the in-flight sheet can be seen. */
private const val WorkDelayMillis: Long = 2_000
