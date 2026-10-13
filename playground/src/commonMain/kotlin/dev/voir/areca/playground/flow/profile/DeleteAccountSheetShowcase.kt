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
import dev.voir.areca.flow.profile.row.DeleteAccountRow
import dev.voir.areca.flow.profile.row.SignOutRow
import dev.voir.areca.flow.profile.sheet.DeleteAccountSheet
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.button.ButtonSize
import dev.voir.areca.foundation.button.ButtonStyle
import dev.voir.areca.foundation.settings.Section
import dev.voir.areca.foundation.settings.Settings
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.Variants
import kotlinx.coroutines.delay

/** Showcase for [DeleteAccountSheet]. */
@Composable
fun DeleteAccountSheetShowcase() = ShowcaseScaffold(
    title = "Delete account sheet",
    summary = "Asks before deleting the account, optionally behind a phrase the person has to type.",
    module = "flow-profile",
    status = ShowcaseStatus.Beta,
) {
    Playground(description = "Open the sheet; with a phrase, Delete account enables only once it is typed.") {
        val withPhrase by booleanControl(
            key = "confirmationPhrase",
            default = true,
            description = "Matched ignoring case and surrounding spaces.",
        )
        val phrase by textControl("phrase", default = DeletePhrase)
        val customDescription by booleanControl(
            key = "description",
            default = false,
            description = "Off: the module's default. On: the application says when deletion happens.",
        )
        var confirming by booleanControl(
            key = "confirming",
            default = false,
            description = "Deletion is in flight: the box and both buttons refuse input. The preview " +
                "finishes after a moment and closes the sheet.",
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

            DeleteAccountSheet(
                visible = visible,
                onConfirm = { confirming = true },
                onDismiss = { visible = false },
                confirming = confirming,
                confirmationPhrase = phrase.takeIf { withPhrase },
                description = if (customDescription) {
                    ScheduledDescription
                } else {
                    "Your profile and everything in it will be removed from every device you use."
                },
            )
        }
    }

    Variants(
        title = "Confirmation",
        description = "A plain question, or one that needs a typed phrase before the button enables.",
    ) {
        variant(name = "Plain", detail = "confirmationPhrase = null") {
            var visible by remember { mutableStateOf(false) }

            Button(
                text = "Open sheet",
                onClick = { visible = true },
                style = ButtonStyle.soft(ActionColor.Neutral),
                size = ButtonSize.Small,
            )

            DeleteAccountSheet(
                visible = visible,
                onConfirm = { visible = false },
                onDismiss = { visible = false },
            )
        }
        variant(name = "Typed phrase", detail = "confirmationPhrase = \"$DeletePhrase\"") {
            var visible by remember { mutableStateOf(false) }

            Button(
                text = "Open sheet",
                onClick = { visible = true },
                style = ButtonStyle.soft(ActionColor.Neutral),
                size = ButtonSize.Small,
            )

            DeleteAccountSheet(
                visible = visible,
                onConfirm = { visible = false },
                onDismiss = { visible = false },
                confirmationPhrase = DeletePhrase,
            )
        }
        variant(name = "Scheduled deletion", detail = "description set by the application") {
            var visible by remember { mutableStateOf(false) }

            Button(
                text = "Open sheet",
                onClick = { visible = true },
                style = ButtonStyle.soft(ActionColor.Neutral),
                size = ButtonSize.Small,
            )

            DeleteAccountSheet(
                visible = visible,
                onConfirm = { visible = false },
                onDismiss = { visible = false },
                description = ScheduledDescription,
            )
        }
    }

    States {
        variant(name = "Phrase not typed", detail = "Delete account disabled; box empty on every open") {
            var visible by remember { mutableStateOf(false) }

            Button(
                text = "Open sheet",
                onClick = { visible = true },
                style = ButtonStyle.soft(ActionColor.Neutral),
                size = ButtonSize.Small,
            )

            DeleteAccountSheet(
                visible = visible,
                onConfirm = { visible = false },
                onDismiss = { visible = false },
                confirmationPhrase = DeletePhrase,
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

            DeleteAccountSheet(
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
            description = "The delete row at the foot of the account screen opens the sheet behind a phrase.",
        ) {
            var visible by remember { mutableStateOf(false) }
            var deleting by remember { mutableStateOf(false) }
            var deleted by remember { mutableStateOf(false) }

            LaunchedEffect(deleting) {
                if (deleting) {
                    delay(WorkDelayMillis)
                    deleting = false
                    visible = false
                    deleted = true
                }
            }

            if (deleted) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Account scheduled for deletion",
                        variant = TextVariant.BodySmall,
                        color = TextColor.Muted,
                    )

                    Button(
                        text = "Start over",
                        onClick = { deleted = false },
                        style = ButtonStyle.soft(ActionColor.Neutral),
                        size = ButtonSize.Small,
                    )
                }
            } else {
                Settings(modifier = Modifier.fillMaxWidth()) {
                    Section {
                        SignOutRow(onClick = {})
                        DeleteAccountRow(onClick = { visible = true })
                    }
                }
            }

            DeleteAccountSheet(
                visible = visible,
                onConfirm = { deleting = true },
                onDismiss = { visible = false },
                confirming = deleting,
                confirmationPhrase = DeletePhrase,
                description = ScheduledDescription,
            )
        }
    }

    EdgeCases {
        case(
            name = "Long phrase and description",
            expectation = "The prompt and description wrap, centred; the box stays one line and scrolls.",
        ) {
            var visible by remember { mutableStateOf(false) }

            Button(
                text = "Open sheet",
                onClick = { visible = true },
                style = ButtonStyle.soft(ActionColor.Neutral),
                size = ButtonSize.Small,
            )

            DeleteAccountSheet(
                visible = visible,
                onConfirm = { visible = false },
                onDismiss = { visible = false },
                confirmationPhrase = "delete ada@analytical.engine and everything in it",
                description = "Your profile, your messages, the files you uploaded and every workspace " +
                    "you own will be removed from every device you use. Workspaces you share are " +
                    "handed to their next oldest member.",
            )
        }
        case(
            name = "Lower-case phrase",
            expectation = "The keyboard is not forced to capitals; \"Delete\" and \" DELETE \" still match.",
        ) {
            var visible by remember { mutableStateOf(false) }

            Button(
                text = "Open sheet",
                onClick = { visible = true },
                style = ButtonStyle.soft(ActionColor.Neutral),
                size = ButtonSize.Small,
            )

            DeleteAccountSheet(
                visible = visible,
                onConfirm = { visible = false },
                onDismiss = { visible = false },
                confirmationPhrase = "delete",
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

            DeleteAccountSheet(
                visible = visible,
                onConfirm = { visible = false },
                onDismiss = { visible = false },
                confirmationPhrase = DeletePhrase,
            )
        }
    }
}

/** The phrase the sample asks for, in capitals as most products ask for it. */
private const val DeletePhrase: String = "DELETE"

/** A description an application with a grace period would pass instead of the default. */
private const val ScheduledDescription: String = "Your account will be deleted in 30 days. Sign in " +
    "before then to keep it."

/** How long the pretend deletion takes, so the in-flight sheet can be seen. */
private const val WorkDelayMillis: Long = 2_000
