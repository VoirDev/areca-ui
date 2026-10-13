package dev.voir.areca.playground.flow.profile

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.voir.areca.flow.profile.row.AccountRow
import dev.voir.areca.flow.profile.row.AddAccountRow
import dev.voir.areca.flow.profile.row.SignOutRow
import dev.voir.areca.foundation.list.ListItemChevron
import dev.voir.areca.foundation.settings.Section
import dev.voir.areca.foundation.settings.Settings
import dev.voir.areca.foundation.settings.SettingsRow
import dev.voir.areca.foundation.settings.SettingsRowIcon
import dev.voir.areca.foundation.settings.SettingsRowStatus
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.Lock
import dev.voir.areca.icons.Phone
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout

/** The status the playground row is forced into. */
private enum class SignOutRowStatus(
    /** Label of the option in the control panel. */
    val label: String,
    /** Status handed to the row. */
    val status: SettingsRowStatus,
) {
    /** Nothing in flight. */
    Idle("Idle", SettingsRowStatus.Idle),

    /** Signing out, without a confirmation sheet. */
    SigningOut("Signing out", SettingsRowStatus.Loading),

    /** Signing out failed. */
    Failed("Failed", SettingsRowStatus.Error(SIGN_OUT_ERROR)),
}

/** Why the sample sign-out failed, worded the way a product would word it. */
private const val SIGN_OUT_ERROR = "Couldn't sign out. Check your connection and try again."

/** Showcase for [SignOutRow]. */
@Composable
fun SignOutRowShowcase() = ShowcaseScaffold(
    title = "Sign out row",
    summary = "Signs the person out of this device from a section of its own at the foot of the account menu.",
    module = "flow-profile",
    status = ShowcaseStatus.Beta,
) {
    Playground(description = "The row in its own section, under the rest of the account menu.") {
        val status by enumControl(
            key = "status",
            default = SignOutRowStatus.Idle,
            options = SignOutRowStatus.entries,
            optionLabel = { it.label },
            description = "For signing out without a sheet: a spinner, or the reason it failed.",
        )
        val enabled by booleanControl("enabled", default = true)

        preview {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section(header = { Text("Security") }) {
                    SettingsRow(
                        title = { Text("Password") },
                        leading = { SettingsRowIcon(ArecaIcons.Lock) },
                        trailing = { ListItemChevron() },
                        onClick = {},
                    )
                    SettingsRow(
                        title = { Text("Devices") },
                        leading = { SettingsRowIcon(ArecaIcons.Phone) },
                        value = { Text("3") },
                        trailing = { ListItemChevron() },
                        onClick = {},
                    )
                }
                Section {
                    SignOutRow(onClick = {}, status = status.status, enabled = enabled)
                }
            }
        }
    }

    States(layout = VariantLayout.Vertical) {
        variant(name = "Idle") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    SignOutRow(onClick = {})
                }
            }
        }
        variant(name = "Signing out", detail = "status = Loading: spinner, presses ignored") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    SignOutRow(onClick = {}, status = SettingsRowStatus.Loading)
                }
            }
        }
        variant(name = "Failed", detail = "status = Error: the reason under the row") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    SignOutRow(onClick = {}, status = SettingsRowStatus.Error(SIGN_OUT_ERROR))
                }
            }
        }
        variant(name = "Disabled", detail = "enabled = false") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    SignOutRow(onClick = {}, enabled = false)
                }
            }
        }
    }

    Recipes {
        recipe(name = "Account menu", description = "The accounts on the device, then signing out on its own.") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section(header = { Text("Accounts") }) {
                    AccountRow(account = ProfileSamples.summary(), onClick = {}, isCurrent = true)
                    AccountRow(
                        account = ProfileSamples.OtherAccounts[0],
                        onClick = {},
                        trailing = { ListItemChevron() },
                    )
                    AddAccountRow(onClick = {})
                }
                Section {
                    SignOutRow(onClick = {})
                }
            }
        }
    }

    EdgeCases {
        case(
            name = "Very long title",
            expectation = "Worded by the app through `title`: the title wraps; the glyph stays on the " +
                "first line.",
        ) {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    SignOutRow(
                    onClick = {},
                    title = "Sign out of this device and forget the passwords saved on it",
                )
                }
            }
        }
        case(
            name = "Narrow container",
            expectation = "Constrained to 260dp: the failure message wraps under the title.",
        ) {
            Settings(modifier = Modifier.width(260.dp)) {
                Section {
                    SignOutRow(onClick = {}, status = SettingsRowStatus.Error(SIGN_OUT_ERROR))
                }
            }
        }
    }
}
