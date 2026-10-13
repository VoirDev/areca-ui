package dev.voir.areca.playground.flow.profile

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.voir.areca.flow.profile.ProfileSummary
import dev.voir.areca.flow.profile.row.AccountRow
import dev.voir.areca.flow.profile.row.AddAccountRow
import dev.voir.areca.flow.profile.row.SignOutRow
import dev.voir.areca.foundation.list.ListItemChevron
import dev.voir.areca.foundation.settings.Section
import dev.voir.areca.foundation.settings.Settings
import dev.voir.areca.foundation.settings.SettingsRowStatus
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

/** How a switch the reader starts in the playground ends. */
private enum class AccountSwitchOutcome(
    /** Label of the option in the control panel. */
    val label: String,
) {
    /** The switch goes through and the pressed account becomes the one in use. */
    Succeeds("Succeeds"),

    /** The switch fails and the pressed account reports why. */
    Fails("Fails"),
}

/** Why the sample switch failed, worded the way a product would word it. */
private const val SWITCH_ERROR = "Couldn't switch accounts. Check your connection and try again."

/** Showcase for [AccountRow] and [AddAccountRow]. */
@Composable
fun AccountRowShowcase() = ShowcaseScaffold(
    title = "Account row",
    summary = "Lists the accounts signed in on this device so the person can switch between them or " +
        "add another.",
    module = "flow-profile",
    status = ShowcaseStatus.Beta,
) {
    Playground(description = "Press another account: it switches after a moment, or reports why it could not.") {
        val avatar by enumControl(
            key = "avatar",
            default = SampleAvatar.Picture,
            options = SampleAvatar.entries,
            optionLabel = { it.label },
            description = "The picture of the account in use.",
        )
        val outcome by enumControl(
            key = "switch",
            default = AccountSwitchOutcome.Succeeds,
            options = AccountSwitchOutcome.entries,
            optionLabel = { it.label },
        )
        val canAdd by booleanControl(
            key = "add account enabled",
            default = true,
            description = "Off at the product's account limit.",
        )

        preview {
            val accounts = remember(avatar) {
                listOf(ProfileSamples.summary(avatar = avatar)) + ProfileSamples.OtherAccounts
            }
            var current by remember(avatar) { mutableIntStateOf(0) }
            var target by remember(avatar) { mutableStateOf<Int?>(null) }
            var failed by remember(avatar) { mutableStateOf<Int?>(null) }

            // A stand-in for the service: a switch takes a moment, then lands or fails.
            LaunchedEffect(target) {
                val switchingTo = target ?: return@LaunchedEffect
                delay(1.5.seconds)
                when (outcome) {
                    AccountSwitchOutcome.Succeeds -> current = switchingTo
                    AccountSwitchOutcome.Fails -> failed = switchingTo
                }
                target = null
            }

            Settings(modifier = Modifier.fillMaxWidth()) {
                Section(
                    header = { Text("Accounts") },
                    footer = { Text("You stay signed in to every account on this device.") },
                ) {
                    accounts.forEachIndexed { index, account ->
                        key(account.email ?: account.name) {
                            AccountRow(
                                account = account,
                                onClick = {
                                    failed = null
                                    target = index
                                },
                                isCurrent = index == current,
                                status = when (index) {
                                    target -> SettingsRowStatus.Loading
                                    failed -> SettingsRowStatus.Error(SWITCH_ERROR)
                                    else -> SettingsRowStatus.Idle
                                },
                            )
                        }
                    }
                    AddAccountRow(onClick = {}, enabled = canAdd)
                }
            }
        }
    }

    Variants(
        title = "What identifies the account",
        description = "The line under the name: the handle when there is one, the address otherwise.",
        layout = VariantLayout.Vertical,
    ) {
        variant(name = "Current", detail = "isCurrent = true: a tick, nothing to press") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    AccountRow(account = ProfileSamples.summary(), onClick = {}, isCurrent = true)
                }
            }
        }
        variant(name = "Other / handle", detail = "handle = \"@analytical\"") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    AccountRow(
                        account = ProfileSamples.OtherAccounts[0],
                        onClick = {},
                        trailing = { ListItemChevron() },
                    )
                }
            }
        }
        variant(name = "Other / address", detail = "handle = null, no picture") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    AccountRow(
                        account = ProfileSamples.OtherAccounts[1],
                        onClick = {},
                        trailing = { ListItemChevron() },
                    )
                }
            }
        }
        variant(name = "Other / name only", detail = "handle and email null") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    AccountRow(
                        account = ProfileSummary(name = "Charles Babbage"),
                        onClick = {},
                        trailing = { ListItemChevron() },
                    )
                }
            }
        }
    }

    States(layout = VariantLayout.Vertical) {
        variant(name = "Idle") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    AccountRow(
                        account = ProfileSamples.OtherAccounts[0],
                        onClick = {},
                        trailing = { ListItemChevron() },
                    )
                }
            }
        }
        variant(name = "Switching", detail = "status = Loading on the account being switched to") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    AccountRow(
                        account = ProfileSamples.OtherAccounts[0],
                        onClick = {},
                        trailing = { ListItemChevron() },
                        status = SettingsRowStatus.Loading,
                    )
                }
            }
        }
        variant(name = "Switch failed", detail = "status = Error: the reason under the account") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    AccountRow(
                        account = ProfileSamples.OtherAccounts[0],
                        onClick = {},
                        trailing = { ListItemChevron() },
                        status = SettingsRowStatus.Error(SWITCH_ERROR),
                    )
                }
            }
        }
        variant(name = "Picture loading", detail = "Spinner over the initials") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    AccountRow(
                        account = ProfileSamples.summary(avatar = SampleAvatar.Loading),
                        onClick = {},
                        isCurrent = true,
                    )
                }
            }
        }
        variant(name = "Add account", detail = "AddAccountRow") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    AddAccountRow(onClick = {})
                }
            }
        }
        variant(name = "Add account disabled", detail = "enabled = false: at the account limit") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    AddAccountRow(onClick = {}, enabled = false)
                }
            }
        }
    }

    Recipes {
        recipe(name = "Account switcher", description = "Every account on the device, the one in use first.") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section(header = { Text("Accounts") }) {
                    AccountRow(account = ProfileSamples.summary(), onClick = {}, isCurrent = true)
                    ProfileSamples.OtherAccounts.forEach { account ->
                        AccountRow(
                            account = account,
                            onClick = {},
                            trailing = { ListItemChevron() },
                        )
                    }
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
            name = "Very long name and address",
            expectation = "Both lines stay on one line and end in an ellipsis; the picture and chevron never move.",
        ) {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    AccountRow(
                        account = ProfileSummary(
                            name = "Augusta Ada King, Countess of Lovelace and Baroness Wentworth",
                            email = "augusta.ada.king.countess.of.lovelace@analytical-engine-society.org.uk",
                        ),
                        onClick = {},
                        trailing = { ListItemChevron() },
                    )
                }
            }
        }
        case(
            name = "Narrow container",
            expectation = "Constrained to 260dp: names ellipsise; the tick and chevrons keep their size.",
        ) {
            Settings(modifier = Modifier.width(260.dp)) {
                Section(header = { Text("Accounts") }) {
                    AccountRow(account = ProfileSamples.summary(), onClick = {}, isCurrent = true)
                    ProfileSamples.OtherAccounts.forEach { account ->
                        AccountRow(
                            account = account,
                            onClick = {},
                            trailing = { ListItemChevron() },
                        )
                    }
                    AddAccountRow(onClick = {})
                }
            }
        }
    }
}
