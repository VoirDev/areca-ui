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
import dev.voir.areca.flow.profile.row.UsernameCheck
import dev.voir.areca.flow.profile.row.UsernameCheckFooter
import dev.voir.areca.foundation.settings.Section
import dev.voir.areca.foundation.settings.Settings
import dev.voir.areca.foundation.settings.SettingsTextFieldRow
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout

/** Where the playground's client-side complaint comes from. */
private enum class UsernameComplaintSource(
    /** Label of the option in the control panel. */
    val label: String,
) {
    /** No complaint is passed: the service's verdict always shows. */
    None("None"),

    /** The product's own rule against spaces, run on the typed name. */
    Spaces("No spaces"),

    /** A product rule of its own, which always complains. */
    Custom("Custom"),
}

/** The complaint the product's rule against spaces raises. */
private const val SPACES_COMPLAINT = "A username can't contain spaces."

/** A complaint from a product's own rule, worded the way a product would word it. */
private const val CUSTOM_COMPLAINT = "A username can't start with a number."

/** The username the sample account asks for. */
private const val SAMPLE_USERNAME = "ada_lovelace"

/** Showcase for [UsernameCheckFooter]. */
@Composable
fun UsernameCheckFooterShowcase() = ShowcaseScaffold(
    title = "Username check footer",
    summary = "Says under a username field whether the name can be had, as the service answers.",
    module = "flow-profile",
    status = ShowcaseStatus.Beta,
) {
    Playground(
        description = "Type in the field and pick the service's verdict; a complaint outranks the verdict.",
    ) {
        val check by enumControl(
            key = "verdict",
            default = UsernameCheck.Available,
            options = UsernameCheck.entries,
            optionLabel = { it.name },
            description = "The service's answer about the typed name.",
        )
        val complaintSource by enumControl(
            key = "complaint",
            default = UsernameComplaintSource.Spaces,
            options = UsernameComplaintSource.entries,
            optionLabel = { it.label },
            description = "The application's own rule, passed in as a complaint. The footer checks nothing.",
        )
        val hint by textControl(
            key = "hint",
            default = "People can find you by this name.",
            description = "Shown before anything is known — where a product states its rules.",
        )
        val enabled by booleanControl(
            key = "section enabled",
            default = true,
            description = "A disabled section shades its footer with the field.",
        )

        preview {
            var username by remember { mutableStateOf(SAMPLE_USERNAME) }
            val complaint = when (complaintSource) {
                UsernameComplaintSource.None -> null
                UsernameComplaintSource.Spaces ->
                    if (username.any { it.isWhitespace() }) SPACES_COMPLAINT else null
                UsernameComplaintSource.Custom -> CUSTOM_COMPLAINT
            }

            Settings(modifier = Modifier.fillMaxWidth()) {
                Section(
                    header = { Text("Username") },
                    footer = {
                        UsernameCheckFooter(
                            username = username,
                            check = check,
                            complaint = complaint,
                            hint = hint,
                        )
                    },
                    enabled = enabled,
                ) {
                    SettingsTextFieldRow(
                        value = username,
                        onValueChange = { username = it },
                        placeholder = "Username",
                        maxLength = 32,
                    )
                }
            }
        }
    }

    States(
        description = "Neutral while nothing is known; coloured only for the two answers and a complaint.",
        layout = VariantLayout.Vertical,
    ) {
        variant(name = "Idle", detail = "check = Idle: the rules a username follows") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section(
                    header = { Text("Username") },
                    footer = { UsernameCheckFooter(username = SAMPLE_USERNAME, check = UsernameCheck.Idle) },
                ) {
                    SettingsTextFieldRow(value = SAMPLE_USERNAME, onValueChange = {}, placeholder = "Username")
                }
            }
        }
        variant(name = "Checking", detail = "check = Checking") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section(
                    header = { Text("Username") },
                    footer = { UsernameCheckFooter(username = SAMPLE_USERNAME, check = UsernameCheck.Checking) },
                ) {
                    SettingsTextFieldRow(value = SAMPLE_USERNAME, onValueChange = {}, placeholder = "Username")
                }
            }
        }
        variant(name = "Available", detail = "check = Available: success colour") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section(
                    header = { Text("Username") },
                    footer = { UsernameCheckFooter(username = SAMPLE_USERNAME, check = UsernameCheck.Available) },
                ) {
                    SettingsTextFieldRow(value = SAMPLE_USERNAME, onValueChange = {}, placeholder = "Username")
                }
            }
        }
        variant(name = "Taken", detail = "check = Taken: danger colour") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section(
                    header = { Text("Username") },
                    footer = { UsernameCheckFooter(username = "ada", check = UsernameCheck.Taken) },
                ) {
                    SettingsTextFieldRow(value = "ada", onValueChange = {}, placeholder = "Username")
                }
            }
        }
        variant(name = "Complaint", detail = "complaint outranks check = Available") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section(
                    header = { Text("Username") },
                    footer = {
                        UsernameCheckFooter(
                            username = "ada lovelace",
                            check = UsernameCheck.Available,
                            complaint = "A username can't contain spaces.",
                        )
                    },
                ) {
                    SettingsTextFieldRow(value = "ada lovelace", onValueChange = {}, placeholder = "Username")
                }
            }
        }
        variant(name = "Disabled", detail = "Section(enabled = false) shades the footer") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section(
                    header = { Text("Username") },
                    footer = { UsernameCheckFooter(username = SAMPLE_USERNAME, check = UsernameCheck.Available) },
                    enabled = false,
                ) {
                    SettingsTextFieldRow(value = SAMPLE_USERNAME, onValueChange = {}, placeholder = "Username")
                }
            }
        }
    }

    Recipes {
        recipe(name = "Username screen", description = "The field and its verdict, alone on the screen they own.") {
            var username by remember { mutableStateOf("ada") }
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section(
                    header = { Text("Username") },
                    footer = {
                        UsernameCheckFooter(
                            username = username,
                            check = if (username == "ada") UsernameCheck.Taken else UsernameCheck.Available,
                        )
                    },
                ) {
                    SettingsTextFieldRow(
                        value = username,
                        onValueChange = { username = it },
                        placeholder = "Username",
                        maxLength = 32,
                    )
                }
            }
        }
    }

    EdgeCases {
        case(
            name = "Very long username",
            expectation = "The verdict wraps onto as many lines as it needs; the field above never moves.",
        ) {
            val username = "augusta_ada_king_countess_of_lovelace"
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section(
                    header = { Text("Username") },
                    footer = { UsernameCheckFooter(username = username, check = UsernameCheck.Available) },
                ) {
                    SettingsTextFieldRow(value = username, onValueChange = {}, placeholder = "Username")
                }
            }
        }
        case(
            name = "Empty field",
            expectation = "With nothing asked yet, the hint stands in for a verdict on nothing.",
        ) {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section(
                    header = { Text("Username") },
                    footer = {
                        UsernameCheckFooter(
                            username = "",
                            check = UsernameCheck.Idle,
                        )
                    },
                ) {
                    SettingsTextFieldRow(value = "", onValueChange = {}, placeholder = "Username")
                }
            }
        }
        case(
            name = "Narrow container",
            expectation = "Constrained to 260dp: the rules wrap under the card rather than being cut.",
        ) {
            Settings(modifier = Modifier.width(260.dp)) {
                Section(
                    header = { Text("Username") },
                    footer = { UsernameCheckFooter(username = SAMPLE_USERNAME, check = UsernameCheck.Idle) },
                ) {
                    SettingsTextFieldRow(value = SAMPLE_USERNAME, onValueChange = {}, placeholder = "Username")
                }
            }
        }
    }
}
