package dev.voir.areca.playground.flow.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dev.voir.areca.flow.profile.ProfileSummary
import dev.voir.areca.flow.profile.part.ProfileGreeting
import dev.voir.areca.flow.profile.part.ProfileGreetingPlaceholder
import dev.voir.areca.flow.profile.part.ProfileSummaryCard
import dev.voir.areca.flow.profile.part.ProfileSummaryCardAction
import dev.voir.areca.flow.profile.part.ProfileSummaryCardPlaceholder
import dev.voir.areca.flow.profile.row.AccountRow
import dev.voir.areca.flow.profile.row.AddAccountRow
import dev.voir.areca.flow.profile.row.DataExportRow
import dev.voir.areca.flow.profile.row.DataExportState
import dev.voir.areca.flow.profile.row.DeleteAccountRow
import dev.voir.areca.flow.profile.row.EmailRow
import dev.voir.areca.flow.profile.row.LinkedAccountRow
import dev.voir.areca.flow.profile.row.SignOutRow
import dev.voir.areca.flow.profile.sheet.DeleteAccountSheet
import dev.voir.areca.flow.profile.sheet.SignOutSheet
import dev.voir.areca.foundation.list.ListItemChevron
import dev.voir.areca.foundation.settings.Section
import dev.voir.areca.foundation.settings.Settings
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.playground.showcase.FlowShowcase
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.canvas.DeviceFrame
import dev.voir.areca.playground.showcase.canvas.DeviceSize
import dev.voir.areca.playground.showcase.canvas.windowInsets
import dev.voir.areca.playground.showcase.chrome.ChromeText
import dev.voir.areca.playground.showcase.chrome.ChromeTextRole
import dev.voir.areca.playground.showcase.chrome.ChromeTone
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.horizontalContentInsets

/** Which summary surface leads the menu. */
private enum class SummaryOption(
    /** Label of the option in the control panel. */
    val label: String,
) {
    /** The card that leads into the account. */
    Card("card"),

    /** The greeting that tops a home screen. */
    Greeting("greeting"),
}

/** How the sample provider is linked. */
private enum class LinkOption(
    /** Label of the option in the control panel. */
    val label: String,
) {
    /** Not linked. */
    Unlinked("not linked"),

    /** Linked beside other methods. */
    Linked("linked"),

    /** Linked, and the account's only way in. */
    OnlyMethod("only method"),
}

/** States of the data export. */
private enum class ExportOption(
    /** Label of the option in the control panel. */
    val label: String,
    /** The state it stands for. */
    val state: DataExportState,
) {
    /** Nothing asked for. */
    Idle("idle", DataExportState.Idle),

    /** Being put together. */
    Preparing("preparing", DataExportState.Preparing),

    /** Ready to download. */
    Ready("ready", DataExportState.Ready),

    /** Could not be put together. */
    Failed("failed", DataExportState.Failed("We couldn't prepare your copy. Try again later.")),
}

/**
 * An account menu: who is signed in, how they sign in, their data, the other accounts on the device,
 * and the ways out.
 */
@Composable
fun AccountShowcase() = FlowShowcase(
    title = "Account",
    summary = "An account menu: sign-in methods, data export, the account switcher and the ways out.",
    module = "flow-profile",
    status = ShowcaseStatus.Beta,
    notes = {
        ChromeText(text = "The recipe", role = ChromeTextRole.Label)
        ChromeText(
            text = "AccountRecipe in this file is the whole assembly: a summary card from the module, " +
                "then a foundation Settings list of the module's rows. Sign out and Delete each sit " +
                "in a section of their own at the foot, so neither is pressed on the way to " +
                "something else.",
            role = ChromeTextRole.SectionDescription,
            tone = ChromeTone.Muted,
        )
        ChromeText(
            text = "The provider mark is the application's to supply. Set linking to the only method " +
                "and the row stops offering its own removal.",
            role = ChromeTextRole.SectionDescription,
            tone = ChromeTone.Muted,
        )
    },
) {
    val summaryStyle by enumControl(
        key = "summary",
        default = SummaryOption.Card,
        options = SummaryOption.entries,
        optionLabel = { it.label },
    )
    val loading by booleanControl("summary loading", default = false)
    val avatar by enumControl(
        key = "avatar",
        default = SampleAvatar.Picture,
        options = SampleAvatar.entries,
        optionLabel = { it.label },
    )
    val verified by booleanControl("email verified", default = false)
    val linking by enumControl(
        key = "google",
        default = LinkOption.Linked,
        options = LinkOption.entries,
        optionLabel = { it.label },
    )
    val export by enumControl(
        key = "export",
        default = ExportOption.Idle,
        options = ExportOption.entries,
        optionLabel = { it.label },
    )

    preview {
        var signingOut by remember { mutableStateOf(false) }
        var deleting by remember { mutableStateOf(false) }

        DeviceFrame(size = DeviceSize.Regular) {
            AccountRecipe(
                summary = if (loading) {
                    null
                } else {
                    ProfileSamples.summary(avatar = avatar, isVerified = verified)
                },
                greeting = summaryStyle == SummaryOption.Greeting,
                googleLinked = linking != LinkOption.Unlinked,
                googleIsOnlyMethod = linking == LinkOption.OnlyMethod,
                export = export.state,
                otherAccounts = ProfileSamples.OtherAccounts,
                confirmingSignOut = signingOut,
                confirmingDelete = deleting,
                onSignOut = { signingOut = true },
                onSignOutDismissed = { signingOut = false },
                onDelete = { deleting = true },
                onDeleteDismissed = { deleting = false },
                windowInsets = DeviceSize.Regular.windowInsets,
            )
        }
    }
}

/**
 * The recipe: an account menu built from `flow-profile` parts on the foundation settings list.
 *
 * @param summary Who is signed in, or `null` while that is loading.
 * @param greeting Whether to lead with the greeting rather than the card.
 * @param googleLinked Whether the sample provider is linked.
 * @param googleIsOnlyMethod Whether the sample provider is the account's only way in.
 * @param export Where the data export has got to.
 * @param otherAccounts Other accounts signed in on this device.
 * @param confirmingSignOut Whether the sign-out sheet is up.
 * @param confirmingDelete Whether the delete sheet is up.
 * @param onSignOut Called when signing out is asked for.
 * @param onSignOutDismissed Called when the sign-out sheet is dismissed or confirmed.
 * @param onDelete Called when deletion is asked for.
 * @param onDeleteDismissed Called when the delete sheet is dismissed or confirmed.
 * @param windowInsets Regions the system occupies.
 */
@Composable
private fun AccountRecipe(
    summary: ProfileSummary?,
    greeting: Boolean,
    googleLinked: Boolean,
    googleIsOnlyMethod: Boolean,
    export: DataExportState,
    otherAccounts: List<ProfileSummary>,
    confirmingSignOut: Boolean,
    confirmingDelete: Boolean,
    onSignOut: () -> Unit,
    onSignOutDismissed: () -> Unit,
    onDelete: () -> Unit,
    onDeleteDismissed: () -> Unit,
    windowInsets: WindowInsets,
) {
    val insets = Modifier.horizontalContentInsets()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ArecaTheme.colors.bgCanvas)
            .verticalScroll(rememberScrollState())
            .windowInsetsPadding(windowInsets),
    ) {
        Spacer(Modifier.height(ArecaTheme.spacing.lg))

        when {
            summary == null && greeting -> ProfileGreetingPlaceholder(modifier = insets)
            summary == null -> ProfileSummaryCardPlaceholder(modifier = insets)
            greeting -> ProfileGreeting(
                summary = summary,
                onClick = {},
                modifier = insets,
                greeting = "Welcome back",
                onClickLabel = "Open your account",
            )
            else -> ProfileSummaryCard(
                summary = summary,
                onClick = {},
                modifier = insets,
                trailing = { ProfileSummaryCardAction(label = "Edit") },
                onClickLabel = "Open your account",
            )
        }

        Spacer(Modifier.height(ArecaTheme.spacing.lg))

        Settings {
            Section(header = { Text("Sign-in methods") }) {
                EmailRow(
                    email = summary?.email,
                    isVerified = summary?.isVerified == true,
                    onVerify = {},
                    onClick = {},
                    unverifiedBadge = "Unverified",
                )
                LinkedAccountRow(
                    name = "Google",
                    icon = { ProfileSamples.ProviderMark() },
                    isLinked = googleLinked,
                    account = summary?.email,
                    canUnlink = !googleIsOnlyMethod,
                    lockedReason = "Your only way to sign in",
                    notLinkedLabel = "Not linked",
                    onLink = {},
                    onUnlink = {},
                )
            }

            Section(header = { Text("Your data") }) {
                DataExportRow(
                    state = export,
                    onRequest = {},
                    onDownload = {},
                    idleSubtitle = "A copy of everything in your account",
                    preparingSubtitle = "Preparing your copy. This can take a while.",
                    readySubtitle = "Your copy is ready",
                )
            }

            if (summary != null) {
                Section(header = { Text("Accounts") }) {
                    AccountRow(
                        account = summary,
                        isCurrent = true,
                        onClick = null,
                        currentDescription = "Current account",
                    )
                    otherAccounts.forEach { account ->
                        key(account.email ?: account.name) {
                            AccountRow(
                                account = account,
                                onClick = {},
                                onClickLabel = "Switch to this account",
                                trailing = { ListItemChevron() },
                            )
                        }
                    }
                    AddAccountRow(onClick = {})
                }
            }

            Section { SignOutRow(onClick = onSignOut) }

            Section {
                DeleteAccountRow(onClick = onDelete, subtitle = "Permanent, and cannot be undone")
            }
        }

        Spacer(Modifier.height(ArecaTheme.spacing.xl3))
    }

    SignOutSheet(
        visible = confirmingSignOut,
        description = "You'll need to sign in again to use your account on this device.",
        note = "2 changes on this device haven't synced yet and will be lost.",
        onConfirm = onSignOutDismissed,
        onDismiss = onSignOutDismissed,
    )

    DeleteAccountSheet(
        visible = confirmingDelete,
        confirmationPhrase = "DELETE",
        description = "Your profile and everything in it will be removed from every device you use.",
        note = "This cannot be undone.",
        onConfirm = onDeleteDismissed,
        onDismiss = onDeleteDismissed,
    )
}
