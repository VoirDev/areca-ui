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
import dev.voir.areca.flow.profile.row.ProfileInfoRow
import dev.voir.areca.foundation.list.ListItemChevron
import dev.voir.areca.foundation.settings.Section
import dev.voir.areca.foundation.settings.Settings
import dev.voir.areca.foundation.settings.SettingsRowIcon
import dev.voir.areca.foundation.settings.SettingsRowStatus
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.AtSign
import dev.voir.areca.icons.Phone
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout
import kotlinx.datetime.LocalDate

/** The status the playground row is forced into. */
private enum class BirthdayRowStatus(
    /** Label of the option in the control panel. */
    val label: String,
    /** Status handed to the row. */
    val status: SettingsRowStatus,
) {
    /** Nothing in flight. */
    Idle("Idle", SettingsRowStatus.Idle),

    /** The last date chosen is saving. */
    Saving("Saving", SettingsRowStatus.Loading),

    /** The last date chosen could not be saved. */
    Failed("Failed", SettingsRowStatus.Error(BIRTHDAY_SAVE_ERROR)),
}

/** Why the sample save failed, worded the way a product would word it. */
private const val BIRTHDAY_SAVE_ERROR = "Couldn't save your birthday. Check your connection and try again."

/** The date of birth the sample account starts with. */
private val SampleBirthday: LocalDate = LocalDate(1990, 12, 10)

/** Showcase for [BirthdayRow]. */
@Composable
fun BirthdayRowShowcase() = ShowcaseScaffold(
    title = "Birthday row",
    summary = "States the account's date of birth and opens a calendar sheet to choose or remove it.",
    module = "flow-profile",
    status = ShowcaseStatus.Beta,
) {
    Playground(
        description = "Press the row: the sheet really sets and removes the date the row shows.",
    ) {
        val startsSet by booleanControl(
            key = "birthday set",
            default = true,
            description = "What the row starts with. Reset or flip it to start again.",
        )
        val status by enumControl(
            key = "status",
            default = BirthdayRowStatus.Idle,
            options = BirthdayRowStatus.entries,
            optionLabel = { it.label },
            description = "Spinner while the chosen date saves, or a message under the text when it fails.",
        )
        val enabled by booleanControl("enabled", default = true)

        preview {
            var birthday by remember(startsSet) { mutableStateOf(if (startsSet) SampleBirthday else null) }

            Settings(modifier = Modifier.fillMaxWidth()) {
                Section(
                    header = { Text("Your info") },
                    footer = { Text("Only your contacts can see your birthday.") },
                ) {
                    ProfileInfoRow(
                        value = "+381 64 123 45 67",
                        label = "Mobile",
                        leading = { SettingsRowIcon(ArecaIcons.Phone) },
                        onClick = {},
                    )
                    ProfileInfoRow(
                        value = "@ada",
                        label = "Username",
                        leading = { SettingsRowIcon(ArecaIcons.AtSign) },
                        onClick = {},
                        trailing = { ListItemChevron() },
                    )
                    BirthdayRow(
                        value = birthday,
                        onValueChange = { birthday = it },
                        status = status.status,
                        enabled = enabled,
                    )
                }
            }
        }
    }

    States(layout = VariantLayout.Vertical) {
        variant(name = "Set", detail = "value = 10 December 1990") {
            var birthday by remember { mutableStateOf<LocalDate?>(SampleBirthday) }
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    BirthdayRow(value = birthday, onValueChange = { birthday = it })
                }
            }
        }
        variant(name = "Not set", detail = "value = null: the row invites a date") {
            var birthday by remember { mutableStateOf<LocalDate?>(null) }
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    BirthdayRow(value = birthday, onValueChange = { birthday = it })
                }
            }
        }
        variant(name = "Saving", detail = "status = Loading: spinner, presses ignored") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    BirthdayRow(
                        value = SampleBirthday,
                        onValueChange = {},
                        status = SettingsRowStatus.Loading,
                    )
                }
            }
        }
        variant(name = "Failed", detail = "status = Error: the reason under the text") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    BirthdayRow(
                        value = SampleBirthday,
                        onValueChange = {},
                        status = SettingsRowStatus.Error(BIRTHDAY_SAVE_ERROR),
                    )
                }
            }
        }
        variant(name = "Disabled", detail = "enabled = false: the sheet cannot be opened") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    BirthdayRow(value = SampleBirthday, onValueChange = {}, enabled = false)
                }
            }
        }
    }

    Recipes {
        recipe(name = "Your info", description = "The birthday among the other facts on one's own profile.") {
            var birthday by remember { mutableStateOf<LocalDate?>(SampleBirthday) }
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section(
                    header = { Text("Your info") },
                    footer = { Text("Only your contacts can see your birthday.") },
                ) {
                    ProfileInfoRow(
                        value = "+381 64 123 45 67",
                        label = "Mobile",
                        leading = { SettingsRowIcon(ArecaIcons.Phone) },
                        onClick = {},
                    )
                    BirthdayRow(value = birthday, onValueChange = { birthday = it })
                }
            }
        }
    }

    EdgeCases {
        case(
            name = "Leap day",
            expectation = "Shown as written; the sheet opens on February of that leap year with the 29th marked.",
        ) {
            var birthday by remember { mutableStateOf<LocalDate?>(LocalDate(1996, 2, 29)) }
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    BirthdayRow(value = birthday, onValueChange = { birthday = it })
                }
            }
        }
        case(
            name = "Long failure message",
            expectation = "The message wraps under the text; the date and the tile stay where they are.",
        ) {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    BirthdayRow(
                        value = SampleBirthday,
                        onValueChange = {},
                        status = SettingsRowStatus.Error(
                            "Couldn't save your birthday because the profile service is down for " +
                                "scheduled maintenance. It will be back within the hour.",
                        ),
                    )
                }
            }
        }
        case(
            name = "Narrow container",
            expectation = "Constrained to 260dp: the date wraps rather than ellipsising; the tile keeps its size.",
        ) {
            var birthday by remember { mutableStateOf<LocalDate?>(LocalDate(1990, 9, 30)) }
            Settings(modifier = Modifier.width(260.dp)) {
                Section(header = { Text("Your info") }) {
                    BirthdayRow(value = birthday, onValueChange = { birthday = it })
                }
            }
        }
    }
}
