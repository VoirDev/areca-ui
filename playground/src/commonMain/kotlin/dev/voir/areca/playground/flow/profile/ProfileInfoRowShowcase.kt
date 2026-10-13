package dev.voir.areca.playground.flow.profile

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.voir.areca.flow.profile.row.ProfileInfoRow
import dev.voir.areca.foundation.list.ListItemChevron
import dev.voir.areca.foundation.settings.Section
import dev.voir.areca.foundation.settings.Settings
import dev.voir.areca.foundation.settings.SettingsRowIcon
import dev.voir.areca.foundation.settings.SettingsRowStatus
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.AtSign
import dev.voir.areca.icons.MarkerPin
import dev.voir.areca.icons.Phone
import dev.voir.areca.icons.User
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.section.Anatomy
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants

/** What the playground row carries before its text. */
private enum class InfoRowLeading(
    /** Label of the option in the control panel. */
    val label: String,
) {
    /** Nothing: the text starts at the row's edge. */
    None("None"),

    /** A neutral phone tile. */
    Tile("Tile"),
}

/** The status the playground row is forced into. */
private enum class InfoRowStatus(
    /** Label of the option in the control panel. */
    val label: String,
    /** Status handed to the row. */
    val status: SettingsRowStatus,
) {
    /** Nothing in flight. */
    Idle("Idle", SettingsRowStatus.Idle),

    /** A change to the fact is saving. */
    Saving("Saving", SettingsRowStatus.Loading),

    /** The last change to the fact failed. */
    Failed("Failed", SettingsRowStatus.Error(INFO_ROW_SAVE_ERROR)),
}

/** Why the sample change failed, worded the way a product would word it. */
private const val INFO_ROW_SAVE_ERROR = "Couldn't save your number. Check your connection and try again."

/** Showcase for [ProfileInfoRow]. */
@Composable
fun ProfileInfoRowShowcase() = ShowcaseScaffold(
    title = "Profile info row",
    summary = "States one fact about the account — a number, a handle — with what kind of fact it " +
        "is underneath, and optionally opens whatever changes it.",
    module = "flow-profile",
    status = ShowcaseStatus.Beta,
) {
    Playground(description = "Change the fact and how the row behaves, between two neighbouring facts.") {
        val value by textControl("value", default = "+381 64 123 45 67")
        val label by textControl("label", default = "Mobile")
        val leadingKind by enumControl(
            key = "leading",
            default = InfoRowLeading.Tile,
            options = InfoRowLeading.entries,
            optionLabel = { it.label },
        )
        val pressable by booleanControl(
            key = "pressable",
            default = true,
            description = "With onClick the row opens an editor; without it the row only states.",
        )
        val chevron by booleanControl(
            key = "chevron",
            default = false,
            description = "For a row that opens another screen rather than a sheet.",
        )
        val status by enumControl(
            key = "status",
            default = InfoRowStatus.Idle,
            options = InfoRowStatus.entries,
            optionLabel = { it.label },
            description = "Spinner while a change saves, or a message under the text when it fails.",
        )
        val enabled by booleanControl("enabled", default = true)

        preview {
            val leading: (@Composable BoxScope.() -> Unit)? = when (leadingKind) {
                InfoRowLeading.None -> null
                InfoRowLeading.Tile -> {
                    { SettingsRowIcon(ArecaIcons.Phone) }
                }
            }

            Settings(modifier = Modifier.fillMaxWidth()) {
                Section(
                    header = { Text("Your info") },
                    footer = { Text("Only your contacts can see your mobile number.") },
                ) {
                    ProfileInfoRow(
                        value = "Ada Lovelace",
                        label = "Name",
                        leading = { SettingsRowIcon(ArecaIcons.User) },
                        onClick = {},
                        onClickLabel = "Change name",
                    )
                    ProfileInfoRow(
                        value = value,
                        label = label,
                        leading = leading,
                        onClick = if (pressable) ({}) else null,
                        onClickLabel = if (pressable) "Change mobile number" else null,
                        trailing = if (chevron) ({ ListItemChevron() }) else null,
                        status = status.status,
                        enabled = enabled,
                    )
                    ProfileInfoRow(
                        value = "@ada",
                        label = "Username",
                        leading = { SettingsRowIcon(ArecaIcons.AtSign) },
                        onClick = {},
                        onClickLabel = "Change username",
                        trailing = { ListItemChevron() },
                    )
                }
            }
        }
    }

    Anatomy(description = "The value leads with the title's weight; the label names it underneath.") {
        slot(
            name = "value and label",
            description = "The fact over what kind of fact it is — read as \"@ada, Username\".",
        ) {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    ProfileInfoRow(value = "@ada", label = "Username")
                }
            }
        }
        slot(
            name = "leading",
            description = "A SettingsRowIcon tile, usually; neutral unless the colour carries meaning.",
        ) {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    ProfileInfoRow(
                        value = "@ada",
                        label = "Username",
                        leading = { SettingsRowIcon(ArecaIcons.AtSign) },
                    )
                }
            }
        }
        slot(
            name = "chevron",
            description = "Ends a row that opens another screen, after the text.",
        ) {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    ProfileInfoRow(
                        value = "@ada",
                        label = "Username",
                        leading = { SettingsRowIcon(ArecaIcons.AtSign) },
                        onClick = {},
                        trailing = { ListItemChevron() },
                    )
                }
            }
        }
    }

    Variants(
        title = "What pressing does",
        description = "A row that only states, one that opens a sheet, and one that opens a screen.",
        layout = VariantLayout.Vertical,
    ) {
        variant(name = "Stating", detail = "onClick = null") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    ProfileInfoRow(
                        value = "+381 64 123 45 67",
                        label = "Mobile",
                        leading = { SettingsRowIcon(ArecaIcons.Phone) },
                    )
                }
            }
        }
        variant(name = "Pressable", detail = "onClick = { … }") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    ProfileInfoRow(
                        value = "+381 64 123 45 67",
                        label = "Mobile",
                        leading = { SettingsRowIcon(ArecaIcons.Phone) },
                        onClick = {},
                        onClickLabel = "Change mobile number",
                    )
                }
            }
        }
        variant(
            name = "Pressable / chevron",
            detail = "onClick = { … }, trailing = { ListItemChevron() }",
        ) {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    ProfileInfoRow(
                        value = "@ada",
                        label = "Username",
                        leading = { SettingsRowIcon(ArecaIcons.AtSign) },
                        onClick = {},
                        onClickLabel = "Change username",
                        trailing = { ListItemChevron() },
                    )
                }
            }
        }
        variant(name = "No leading", detail = "leading = null") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    ProfileInfoRow(value = "+381 64 123 45 67", label = "Mobile", onClick = {})
                }
            }
        }
    }

    States(layout = VariantLayout.Vertical) {
        variant(name = "Idle") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    ProfileInfoRow(
                        value = "+381 64 123 45 67",
                        label = "Mobile",
                        leading = { SettingsRowIcon(ArecaIcons.Phone) },
                        onClick = {},
                    )
                }
            }
        }
        variant(name = "Saving", detail = "status = Loading: spinner, presses ignored") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    ProfileInfoRow(
                        value = "+381 64 123 45 67",
                        label = "Mobile",
                        leading = { SettingsRowIcon(ArecaIcons.Phone) },
                        onClick = {},
                        status = SettingsRowStatus.Loading,
                    )
                }
            }
        }
        variant(name = "Failed", detail = "status = Error: the reason under the text") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    ProfileInfoRow(
                        value = "+381 64 123 45 67",
                        label = "Mobile",
                        leading = { SettingsRowIcon(ArecaIcons.Phone) },
                        onClick = {},
                        status = SettingsRowStatus.Error(INFO_ROW_SAVE_ERROR),
                    )
                }
            }
        }
        variant(name = "Disabled", detail = "enabled = false: the tile fades with the text") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    ProfileInfoRow(
                        value = "+381 64 123 45 67",
                        label = "Mobile",
                        leading = { SettingsRowIcon(ArecaIcons.Phone) },
                        onClick = {},
                        enabled = false,
                    )
                }
            }
        }
    }

    Recipes {
        recipe(name = "Your info", description = "The facts on one's own profile, each opening its editor.") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section(
                    header = { Text("Your info") },
                    footer = { Text("Only your contacts can see your mobile number.") },
                ) {
                    ProfileInfoRow(
                        value = "+381 64 123 45 67",
                        label = "Mobile",
                        leading = { SettingsRowIcon(ArecaIcons.Phone) },
                        onClick = {},
                        onClickLabel = "Change mobile number",
                    )
                    ProfileInfoRow(
                        value = "@ada",
                        label = "Username",
                        leading = { SettingsRowIcon(ArecaIcons.AtSign) },
                        onClick = {},
                        onClickLabel = "Change username",
                        trailing = { ListItemChevron() },
                    )
                    ProfileInfoRow(
                        value = "London, United Kingdom",
                        label = "Location",
                        leading = { SettingsRowIcon(ArecaIcons.MarkerPin) },
                        onClick = {},
                        onClickLabel = "Change location",
                    )
                }
            }
        }
        recipe(name = "Someone else's profile", description = "The same facts, stated and not editable.") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section(header = { Text("Contact") }) {
                    ProfileInfoRow(value = "+381 64 123 45 67", label = "Mobile")
                    ProfileInfoRow(value = "@ada", label = "Username")
                }
            }
        }
    }

    EdgeCases {
        case(
            name = "Very long value",
            expectation = "Wraps onto as many lines as it needs; the fact is never cut.",
        ) {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    ProfileInfoRow(
                        value = "Flat 4, 12 Knez Mihailova, Stari Grad, Belgrade 11000, Serbia",
                        label = "Home address",
                        leading = { SettingsRowIcon(ArecaIcons.MarkerPin) },
                        onClick = {},
                    )
                }
            }
        }
        case(
            name = "Very long label",
            expectation = "The label stays on one line and ends in an ellipsis.",
        ) {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    ProfileInfoRow(
                        value = "+44 20 7946 0958",
                        label = "Work landline, reception desk, weekdays between nine and five only",
                        leading = { SettingsRowIcon(ArecaIcons.Phone) },
                    )
                }
            }
        }
        case(
            name = "Narrow container",
            expectation = "Constrained to 260dp: the value wraps, the tile and chevron keep their size.",
        ) {
            Settings(modifier = Modifier.width(260.dp)) {
                Section(header = { Text("Your info") }) {
                    ProfileInfoRow(
                        value = "+381 64 123 45 67",
                        label = "Mobile",
                        leading = { SettingsRowIcon(ArecaIcons.Phone) },
                        onClick = {},
                        trailing = { ListItemChevron() },
                    )
                    ProfileInfoRow(
                        value = "@augusta_ada_king_lovelace",
                        label = "Username",
                        leading = { SettingsRowIcon(ArecaIcons.AtSign) },
                        onClick = {},
                        trailing = { ListItemChevron() },
                        status = SettingsRowStatus.Error(INFO_ROW_SAVE_ERROR),
                    )
                }
            }
        }
    }
}
