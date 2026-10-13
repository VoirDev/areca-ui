package dev.voir.areca.playground.flow.profile

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.voir.areca.flow.profile.row.DataExportRow
import dev.voir.areca.flow.profile.row.DataExportState
import dev.voir.areca.flow.profile.row.DeleteAccountRow
import dev.voir.areca.foundation.list.ListItemChevron
import dev.voir.areca.foundation.settings.Section
import dev.voir.areca.foundation.settings.Settings
import dev.voir.areca.foundation.settings.SettingsRow
import dev.voir.areca.foundation.settings.SettingsRowIcon
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.Shield
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

/** The request state the playground row starts in. */
private enum class DataExportOption(
    /** Label of the option in the control panel. */
    val label: String,
    /** State handed to the row. */
    val state: DataExportState,
) {
    /** Nothing asked for yet. */
    Idle("Idle", DataExportState.Idle),

    /** Asked for and being put together. */
    Preparing("Preparing", DataExportState.Preparing),

    /** Put together and waiting to be downloaded. */
    Ready("Ready", DataExportState.Ready),

    /** Could not be put together. */
    Failed("Failed", DataExportState.Failed(EXPORT_ERROR)),
}

/** Why the sample copy could not be put together, worded the way a product would word it. */
private const val EXPORT_ERROR = "Couldn't prepare your copy. Try again later."

/** Showcase for [DataExportRow] and its [DataExportState]. */
@Composable
fun DataExportRowShowcase() = ShowcaseScaffold(
    title = "Data export row",
    summary = "Lets the person ask for a copy of their account's data and collect it when it is ready.",
    module = "flow-profile",
    status = ShowcaseStatus.Beta,
) {
    Playground(
        description = "Press Request: the copy is prepared for a few seconds, then offered for download.",
    ) {
        val start by enumControl(
            key = "state",
            default = DataExportOption.Idle,
            options = DataExportOption.entries,
            optionLabel = { it.label },
            description = "Where the request starts; pressing the row moves it on as a service would.",
        )
        val enabled by booleanControl("enabled", default = true)

        preview {
            var state by remember(start) { mutableStateOf(start.state) }
            var requests by remember(start) { mutableIntStateOf(0) }

            // A request the reader made here is prepared by a stand-in service; a state picked from
            // the control holds still, so it can be inspected.
            LaunchedEffect(requests) {
                if (requests == 0) return@LaunchedEffect
                delay(3.seconds)
                state = DataExportState.Ready
            }

            Settings(modifier = Modifier.fillMaxWidth()) {
                Section(
                    header = { Text("Your data") },
                    footer = { Text("A copy stays available for 7 days, then it is deleted.") },
                ) {
                    DataExportRow(
                        state = state,
                        onRequest = {
                            state = DataExportState.Preparing
                            requests++
                        },
                        onDownload = {},
                        enabled = enabled,
                    )
                    SettingsRow(
                        title = { Text("Privacy policy") },
                        leading = { SettingsRowIcon(ArecaIcons.Shield) },
                        trailing = { ListItemChevron() },
                        onClick = {},
                    )
                }
            }
        }
    }

    States(
        description = "Every step of a request; the value always names what pressing does.",
        layout = VariantLayout.Vertical,
    ) {
        variant(name = "Idle", detail = "Pressing requests a copy") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    DataExportRow(state = DataExportState.Idle, onRequest = {}, onDownload = {})
                }
            }
        }
        variant(name = "Preparing", detail = "Spinner, presses ignored") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    DataExportRow(state = DataExportState.Preparing, onRequest = {}, onDownload = {})
                }
            }
        }
        variant(name = "Ready", detail = "Pressing downloads the copy") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    DataExportRow(state = DataExportState.Ready, onRequest = {}, onDownload = {})
                }
            }
        }
        variant(name = "Failed", detail = "The reason under the row; pressing asks again") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    DataExportRow(
                        state = DataExportState.Failed(EXPORT_ERROR),
                        onRequest = {},
                        onDownload = {},
                    )
                }
            }
        }
        variant(name = "Disabled", detail = "enabled = false") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    DataExportRow(
                        state = DataExportState.Ready,
                        onRequest = {},
                        onDownload = {},
                        enabled = false,
                    )
                }
            }
        }
    }

    Recipes {
        recipe(name = "Your data", description = "The foot of an account screen: the copy, then deletion on its own.") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section(
                    header = { Text("Your data") },
                    footer = { Text("A copy stays available for 7 days, then it is deleted.") },
                ) {
                    DataExportRow(state = DataExportState.Ready, onRequest = {}, onDownload = {})
                }
                Section {
                    DeleteAccountRow(onClick = {})
                }
            }
        }
    }

    EdgeCases {
        case(
            name = "Long failure message",
            expectation = "The message wraps under the row; Try again stays at the end.",
        ) {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    DataExportRow(
                        state = DataExportState.Failed(
                            "Couldn't prepare your copy because your account holds more than 50 GB. " +
                                "Contact support to receive it in parts.",
                        ),
                        onRequest = {},
                        onDownload = {},
                    )
                }
            }
        }
        case(
            name = "Narrow container",
            expectation = "Constrained to 260dp: the subtitle and the message wrap; the tile and Try again " +
                "keep their size.",
        ) {
            Settings(modifier = Modifier.width(260.dp)) {
                Section(header = { Text("Your data") }) {
                    DataExportRow(
                        state = DataExportState.Failed(EXPORT_ERROR),
                        onRequest = {},
                        onDownload = {},
                    )
                }
            }
        }
    }
}
