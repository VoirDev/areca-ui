package dev.voir.areca.playground.flow.profile

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.voir.areca.flow.profile.row.DataExportRow
import dev.voir.areca.flow.profile.row.DataExportState
import dev.voir.areca.flow.profile.row.DeleteAccountRow
import dev.voir.areca.foundation.settings.Section
import dev.voir.areca.foundation.settings.Settings
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants

/** What deleting costs, as a product with its own content would say it. */
private const val PRODUCT_DELETE_COST = "Removes your profile, your 214 notes and your shared folders"

/** Showcase for [DeleteAccountRow]. */
@Composable
fun DeleteAccountRowShowcase() = ShowcaseScaffold(
    title = "Delete account row",
    summary = "Asks to delete the account, saying what it costs, from a section of its own at the end " +
        "of the screen.",
    module = "flow-profile",
    status = ShowcaseStatus.Beta,
) {
    Playground(description = "The row in its own headerless section, under the account's data.") {
        val subtitle by textControl(
            key = "subtitle",
            default = "Permanent, and cannot be undone",
            description = "What deleting costs, worded by the app. Empty draws no subtitle.",
        )
        val enabled by booleanControl(
            key = "enabled",
            default = true,
            description = "Off while a save is in flight, so the account being written to is not removed.",
        )

        preview {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section(header = { Text("Your data") }) {
                    DataExportRow(state = DataExportState.Idle, onRequest = {}, onDownload = {})
                }
                Section {
                    DeleteAccountRow(onClick = {}, subtitle = subtitle.ifEmpty { null }, enabled = enabled)
                }
            }
        }
    }

    Variants(
        title = "What deleting costs",
        description = "The default wording, or the product's own account of what goes.",
        layout = VariantLayout.Vertical,
    ) {
        variant(name = "Default subtitle", detail = "subtitle from ProfileStrings") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    DeleteAccountRow(onClick = {})
                }
            }
        }
        variant(name = "Product subtitle", detail = "subtitle = \"Removes your profile, …\"") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    DeleteAccountRow(onClick = {}, subtitle = PRODUCT_DELETE_COST)
                }
            }
        }
    }

    States(layout = VariantLayout.Vertical) {
        variant(name = "Enabled") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    DeleteAccountRow(onClick = {})
                }
            }
        }
        variant(name = "Disabled", detail = "enabled = false: a save is in flight") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    DeleteAccountRow(onClick = {}, enabled = false)
                }
            }
        }
    }

    Recipes {
        recipe(name = "End of the account screen", description = "The data copy, then deletion alone and unnamed.") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section(
                    header = { Text("Your data") },
                    footer = { Text("Download a copy before deleting your account.") },
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
            name = "Very long subtitle",
            expectation = "The subtitle wraps; the glyph stays with the title.",
        ) {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    DeleteAccountRow(
                        onClick = {},
                        subtitle = "Removes your profile, your 214 notes, your shared folders and every " +
                            "device signed in to this account. Shared folders stay with their other members.",
                    )
                }
            }
        }
        case(
            name = "Narrow container",
            expectation = "Constrained to 260dp: the subtitle wraps under the title.",
        ) {
            Settings(modifier = Modifier.width(260.dp)) {
                Section {
                    DeleteAccountRow(onClick = {}, subtitle = PRODUCT_DELETE_COST)
                }
            }
        }
    }
}
