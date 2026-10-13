package dev.voir.areca.playground.flow.profile

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.voir.areca.flow.profile.row.BirthdayRow
import dev.voir.areca.flow.profile.row.EmailRow
import dev.voir.areca.flow.profile.row.LinkedAccountRow
import dev.voir.areca.flow.profile.row.ProfileInfoRow
import dev.voir.areca.foundation.settings.Section
import dev.voir.areca.foundation.settings.Settings
import dev.voir.areca.foundation.settings.SettingsRowIcon
import dev.voir.areca.foundation.settings.SettingsRowStatus
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.Phone
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import kotlinx.datetime.LocalDate

/** The status the playground row is forced into. */
private enum class EmailRowStatus(
    /** Label of the option in the control panel. */
    val label: String,
    /** Status handed to the row. */
    val status: SettingsRowStatus,
) {
    /** Nothing in flight. */
    Idle("Idle", SettingsRowStatus.Idle),

    /** A new address, or a confirmation request, is on its way. */
    Saving("Saving", SettingsRowStatus.Loading),

    /** The last change could not be made. */
    Failed("Failed", SettingsRowStatus.Error(EMAIL_SAVE_ERROR)),
}

/** Why the sample change failed, worded the way a product would word it. */
private const val EMAIL_SAVE_ERROR = "That address is already used by another account."

/** The sample account's address. */
private const val SAMPLE_EMAIL = "ada@analytical.engine"

/** Showcase for [EmailRow]. */
@Composable
fun EmailRowShowcase() = ShowcaseScaffold(
    title = "Email row",
    summary = "States the address the account signs in with and, until it is confirmed, offers to " +
        "send a confirmation.",
    module = "flow-profile",
    status = ShowcaseStatus.Beta,
) {
    Playground(description = "Change the address and whether it is confirmed, among the other facts.") {
        val hasEmail by booleanControl(
            key = "has address",
            default = true,
            description = "Without an address the row invites one.",
        )
        val email by textControl("email", default = SAMPLE_EMAIL)
        val verified by booleanControl("verified", default = false)
        val canVerify by booleanControl(
            key = "verify link",
            default = true,
            description = "Pass onVerify only when the app can send a confirmation from here.",
        )
        val pressable by booleanControl(
            key = "pressable",
            default = true,
            description = "With onClick the row opens whatever changes the address.",
        )
        val status by enumControl(
            key = "status",
            default = EmailRowStatus.Idle,
            options = EmailRowStatus.entries,
            optionLabel = { it.label },
            description = "Spinner while a change saves, or a message under the text when it fails.",
        )
        val enabled by booleanControl(
            key = "enabled",
            default = true,
            description = "Disables the row and its Verify link together.",
        )

        preview {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section(
                    header = { Text("Your info") },
                    footer = { Text("Your address is only used to sign in and to reach you about your account.") },
                ) {
                    ProfileInfoRow(
                        value = "+381 64 123 45 67",
                        label = "Mobile",
                        leading = { SettingsRowIcon(ArecaIcons.Phone) },
                        onClick = {},
                    )
                    EmailRow(
                        email = if (hasEmail) email else null,
                        isVerified = verified,
                        onVerify = if (canVerify) ({}) else null,
                        onClick = if (pressable) ({}) else null,
                        status = status.status,
                        enabled = enabled,
                    )
                    BirthdayRow(value = LocalDate(1990, 12, 10), onValueChange = {})
                }
            }
        }
    }

    Variants(
        title = "Confirmed or not",
        description = "Only an unconfirmed address carries anything extra; a confirmed one is quiet.",
        layout = VariantLayout.Vertical,
    ) {
        variant(name = "Verified", detail = "isVerified = true") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    EmailRow(email = SAMPLE_EMAIL, isVerified = true, onVerify = {}, onClick = {})
                }
            }
        }
        variant(name = "Unverified / onVerify", detail = "Badge on the line, Verify link at the end") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    EmailRow(email = SAMPLE_EMAIL, isVerified = false, onVerify = {}, onClick = {})
                }
            }
        }
        variant(name = "Unverified / no onVerify", detail = "Badge only: no control the app cannot honour") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    EmailRow(email = SAMPLE_EMAIL, isVerified = false, onClick = {})
                }
            }
        }
        variant(name = "No address", detail = "email = null: the row invites one") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    EmailRow(email = null, isVerified = false, onVerify = {}, onClick = {})
                }
            }
        }
    }

    States(layout = VariantLayout.Vertical) {
        variant(name = "Idle") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    EmailRow(email = SAMPLE_EMAIL, isVerified = false, onVerify = {}, onClick = {})
                }
            }
        }
        variant(name = "Saving", detail = "status = Loading: spinner, presses ignored") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    EmailRow(
                        email = SAMPLE_EMAIL,
                        isVerified = false,
                        onVerify = {},
                        onClick = {},
                        status = SettingsRowStatus.Loading,
                    )
                }
            }
        }
        variant(name = "Failed", detail = "status = Error: the reason under the text") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    EmailRow(
                        email = SAMPLE_EMAIL,
                        isVerified = false,
                        onVerify = {},
                        onClick = {},
                        status = SettingsRowStatus.Error(EMAIL_SAVE_ERROR),
                    )
                }
            }
        }
        variant(name = "Disabled", detail = "enabled = false: the Verify link is disabled with the row") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    EmailRow(
                        email = SAMPLE_EMAIL,
                        isVerified = false,
                        onVerify = {},
                        onClick = {},
                        enabled = false,
                    )
                }
            }
        }
    }

    Recipes {
        recipe(name = "Your info", description = "The address among the other facts on one's own profile.") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section(header = { Text("Your info") }) {
                    ProfileInfoRow(
                        value = "+381 64 123 45 67",
                        label = "Mobile",
                        leading = { SettingsRowIcon(ArecaIcons.Phone) },
                        onClick = {},
                    )
                    EmailRow(email = SAMPLE_EMAIL, isVerified = true, onClick = {})
                }
            }
        }
        recipe(name = "Sign-in methods", description = "The address beside the providers the account can use.") {
            var googleLinked by remember { mutableStateOf(true) }
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section(
                    header = { Text("Sign-in methods") },
                    footer = { Text("You can sign in with any of these. Keep at least one.") },
                ) {
                    EmailRow(email = SAMPLE_EMAIL, isVerified = false, onVerify = {}, onClick = {})
                    LinkedAccountRow(
                        name = "Google",
                        icon = { ProfileSamples.ProviderMark() },
                        isLinked = googleLinked,
                        onLink = { googleLinked = true },
                        onUnlink = { googleLinked = false },
                        account = SAMPLE_EMAIL,
                    )
                }
            }
        }
    }

    EdgeCases {
        case(
            name = "Very long address",
            expectation = "Stays on one line and ends in an ellipsis; the badge and the Verify link keep their size.",
        ) {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    EmailRow(
                        email = "augusta.ada.king.countess.of.lovelace@analytical-engine-society.org.uk",
                        isVerified = false,
                        onVerify = {},
                        onClick = {},
                    )
                }
            }
        }
        case(
            name = "Narrow container",
            expectation = "Constrained to 260dp: the address gives way first, never the Verify link.",
        ) {
            Settings(modifier = Modifier.width(260.dp)) {
                Section(header = { Text("Your info") }) {
                    ProfileInfoRow(
                        value = "+381 64 123 45 67",
                        label = "Mobile",
                        leading = { SettingsRowIcon(ArecaIcons.Phone) },
                        onClick = {},
                    )
                    EmailRow(email = SAMPLE_EMAIL, isVerified = false, onVerify = {}, onClick = {})
                }
            }
        }
    }
}
