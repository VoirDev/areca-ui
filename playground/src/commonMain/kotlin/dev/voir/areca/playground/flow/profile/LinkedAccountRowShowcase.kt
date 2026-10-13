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
import dev.voir.areca.flow.profile.row.EmailRow
import dev.voir.areca.flow.profile.row.LinkedAccountRow
import dev.voir.areca.foundation.settings.Section
import dev.voir.areca.foundation.settings.Settings
import dev.voir.areca.foundation.settings.SettingsRowStatus
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.section.Anatomy
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants

/** The status the playground row is forced into. */
private enum class LinkedAccountRowStatus(
    /** Label of the option in the control panel. */
    val label: String,
    /** Status handed to the row. */
    val status: SettingsRowStatus,
) {
    /** Nothing in flight. */
    Idle("Idle", SettingsRowStatus.Idle),

    /** A link or unlink is in flight. */
    InFlight("In flight", SettingsRowStatus.Loading),

    /** The last link or unlink failed. */
    Failed("Failed", SettingsRowStatus.Error(LINK_ERROR)),
}

/** Why the sample link failed, worded the way a product would word it. */
private const val LINK_ERROR = "Google didn't respond. Try again in a moment."

/** The sample account's identity at the provider. */
private const val PROVIDER_ACCOUNT = "ada@analytical.engine"

/** Showcase for [LinkedAccountRow]. */
@Composable
fun LinkedAccountRowShowcase() = ShowcaseScaffold(
    title = "Linked account row",
    summary = "Shows whether the account can sign in through another provider and links or unlinks it.",
    module = "flow-profile",
    status = ShowcaseStatus.Beta,
) {
    Playground(description = "Press the row: it links and unlinks, unless it is the only way in.") {
        val name by textControl("name", default = "Google")
        val startsLinked by booleanControl(
            key = "linked",
            default = true,
            description = "What the row starts with; pressing it flips.",
        )
        val withAccount by booleanControl(
            key = "account",
            default = true,
            description = "The identity at the provider, shown under the name while linked.",
        )
        val canUnlink by booleanControl(
            key = "can unlink",
            default = true,
            description = "Off for the account's only sign-in method: the row says so instead of offering Unlink.",
        )
        val status by enumControl(
            key = "status",
            default = LinkedAccountRowStatus.Idle,
            options = LinkedAccountRowStatus.entries,
            optionLabel = { it.label },
            description = "Spinner while a link or unlink is in flight, or the reason it failed.",
        )
        val enabled by booleanControl("enabled", default = true)

        preview {
            var linked by remember(startsLinked) { mutableStateOf(startsLinked) }

            Settings(modifier = Modifier.fillMaxWidth()) {
                Section(
                    header = { Text("Sign-in methods") },
                    footer = { Text("You can sign in with any of these. Keep at least one.") },
                ) {
                    EmailRow(email = PROVIDER_ACCOUNT, isVerified = true, onClick = {})
                    LinkedAccountRow(
                        name = name,
                        icon = { ProfileSamples.ProviderMark() },
                        isLinked = linked,
                        onLink = { linked = true },
                        onUnlink = { linked = false },
                        account = if (withAccount) PROVIDER_ACCOUNT else null,
                        canUnlink = canUnlink,
                        status = status.status,
                        enabled = enabled,
                    )
                }
            }
        }
    }

    Anatomy(description = "A provider's mark, its name over the link state, and what pressing does.") {
        slot(
            name = "icon",
            description = "The provider's mark, supplied by the app under the provider's brand terms; " +
                "sized to the icon tile.",
        ) {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    LinkedAccountRow(
                        name = "Google",
                        icon = { ProfileSamples.ProviderMark() },
                        isLinked = true,
                        onLink = {},
                        onUnlink = {},
                        account = PROVIDER_ACCOUNT,
                    )
                }
            }
        }
    }

    Variants(
        title = "Link state",
        description = "What the line under the name says, and whether the row offers Link or Unlink.",
        layout = VariantLayout.Vertical,
    ) {
        variant(name = "Linked / account", detail = "isLinked = true, account = \"ada@…\"") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    LinkedAccountRow(
                        name = "Google",
                        icon = { ProfileSamples.ProviderMark() },
                        isLinked = true,
                        onLink = {},
                        onUnlink = {},
                        account = PROVIDER_ACCOUNT,
                    )
                }
            }
        }
        variant(name = "Linked / no account", detail = "isLinked = true, account = null") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    LinkedAccountRow(
                        name = "Google",
                        icon = { ProfileSamples.ProviderMark() },
                        isLinked = true,
                        onLink = {},
                        onUnlink = {},
                    )
                }
            }
        }
        variant(name = "Not linked", detail = "isLinked = false") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    LinkedAccountRow(
                        name = "Google",
                        icon = { ProfileSamples.ProviderMark() },
                        isLinked = false,
                        onLink = {},
                        onUnlink = {},
                    )
                }
            }
        }
        variant(name = "Only method", detail = "isLinked = true, canUnlink = false: no control, and why") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    LinkedAccountRow(
                        name = "Google",
                        icon = { ProfileSamples.ProviderMark() },
                        isLinked = true,
                        onLink = {},
                        onUnlink = {},
                        account = PROVIDER_ACCOUNT,
                        canUnlink = false,
                    )
                }
            }
        }
    }

    States(layout = VariantLayout.Vertical) {
        variant(name = "Idle") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    LinkedAccountRow(
                        name = "Google",
                        icon = { ProfileSamples.ProviderMark() },
                        isLinked = false,
                        onLink = {},
                        onUnlink = {},
                    )
                }
            }
        }
        variant(name = "Linking in flight", detail = "status = Loading: spinner in place of Link") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    LinkedAccountRow(
                        name = "Google",
                        icon = { ProfileSamples.ProviderMark() },
                        isLinked = false,
                        onLink = {},
                        onUnlink = {},
                        status = SettingsRowStatus.Loading,
                    )
                }
            }
        }
        variant(name = "Failed", detail = "status = Error: Link offered again, the reason underneath") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    LinkedAccountRow(
                        name = "Google",
                        icon = { ProfileSamples.ProviderMark() },
                        isLinked = false,
                        onLink = {},
                        onUnlink = {},
                        status = SettingsRowStatus.Error(LINK_ERROR),
                    )
                }
            }
        }
        variant(name = "Disabled", detail = "enabled = false") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    LinkedAccountRow(
                        name = "Google",
                        icon = { ProfileSamples.ProviderMark() },
                        isLinked = true,
                        onLink = {},
                        onUnlink = {},
                        account = PROVIDER_ACCOUNT,
                        enabled = false,
                    )
                }
            }
        }
    }

    Recipes {
        recipe(name = "Sign-in methods", description = "The providers beside the address, under one header.") {
            var linked by remember { mutableStateOf(false) }
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section(
                    header = { Text("Sign-in methods") },
                    footer = { Text("You can sign in with any of these. Keep at least one.") },
                ) {
                    EmailRow(email = PROVIDER_ACCOUNT, isVerified = true, onClick = {})
                    LinkedAccountRow(
                        name = "Google",
                        icon = { ProfileSamples.ProviderMark() },
                        isLinked = linked,
                        onLink = { linked = true },
                        onUnlink = { linked = false },
                        account = PROVIDER_ACCOUNT,
                    )
                }
            }
        }
        recipe(name = "Only way in", description = "An account created through a provider, with no password.") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section(
                    header = { Text("Sign-in methods") },
                    footer = { Text("Add an email address before unlinking Google.") },
                ) {
                    LinkedAccountRow(
                        name = "Google",
                        icon = { ProfileSamples.ProviderMark() },
                        isLinked = true,
                        onLink = {},
                        onUnlink = {},
                        account = PROVIDER_ACCOUNT,
                        canUnlink = false,
                    )
                }
            }
        }
    }

    EdgeCases {
        case(
            name = "Very long account",
            expectation = "The line under the name stays on one line and ends in an ellipsis; Unlink keeps its size.",
        ) {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    LinkedAccountRow(
                        name = "Google",
                        icon = { ProfileSamples.ProviderMark() },
                        isLinked = true,
                        onLink = {},
                        onUnlink = {},
                        account = "augusta.ada.king.countess.of.lovelace@analytical-engine-society.org.uk",
                    )
                }
            }
        }
        case(
            name = "Very long provider name",
            expectation = "The name wraps; the mark stays centred on the first line's tile.",
        ) {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    LinkedAccountRow(
                        name = "Google Workspace for the Analytical Engine Society",
                        icon = { ProfileSamples.ProviderMark() },
                        isLinked = false,
                        onLink = {},
                        onUnlink = {},
                    )
                }
            }
        }
        case(
            name = "Narrow container",
            expectation = "Constrained to 260dp: the text gives way first, never Link or Unlink.",
        ) {
            Settings(modifier = Modifier.width(260.dp)) {
                Section(header = { Text("Sign-in methods") }) {
                    EmailRow(email = PROVIDER_ACCOUNT, isVerified = true, onClick = {})
                    LinkedAccountRow(
                        name = "Google",
                        icon = { ProfileSamples.ProviderMark() },
                        isLinked = true,
                        onLink = {},
                        onUnlink = {},
                        account = PROVIDER_ACCOUNT,
                        status = SettingsRowStatus.Error(LINK_ERROR),
                    )
                }
            }
        }
    }
}
