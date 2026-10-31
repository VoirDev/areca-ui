package dev.voir.areca.playground.flow.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.voir.areca.flow.profile.ProfileSummary
import dev.voir.areca.flow.profile.part.AvatarUploadState
import dev.voir.areca.flow.profile.part.ProfileSummaryCard
import dev.voir.areca.flow.profile.part.ProfileSummaryCardAction
import dev.voir.areca.flow.profile.row.SignOutRow
import dev.voir.areca.flow.profile.session.ActiveSessionsState
import dev.voir.areca.flow.profile.session.SessionItem
import dev.voir.areca.flow.profile.sheet.AvatarSourceOptions
import dev.voir.areca.flow.profile.sheet.SignOutSheet
import dev.voir.areca.foundation.adaptive.ListDetail
import dev.voir.areca.foundation.feedback.Fallback
import dev.voir.areca.foundation.list.ListItemChevron
import dev.voir.areca.foundation.settings.Section
import dev.voir.areca.foundation.settings.Settings
import dev.voir.areca.foundation.settings.SettingsRow
import dev.voir.areca.foundation.settings.SettingsRowIcon
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.topbar.BackAction
import dev.voir.areca.foundation.topbar.TopBar
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.Laptop
import dev.voir.areca.icons.UserEdit
import dev.voir.areca.playground.showcase.FlowShowcase
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.canvas.WindowFrame
import dev.voir.areca.playground.showcase.canvas.WindowPreset
import dev.voir.areca.playground.showcase.canvas.windowInsets
import dev.voir.areca.playground.showcase.chrome.ChromeText
import dev.voir.areca.playground.showcase.chrome.ChromeTextRole
import dev.voir.areca.playground.showcase.chrome.ChromeTone
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.horizontalContentInsets
import kotlinx.datetime.LocalDate

/**
 * A page of the account settings, opened from the list.
 *
 * @property title Heading of the page, in the detail pane's top bar.
 */
private enum class AccountPage(val title: String) {
    /** The edit-profile screen. */
    EditProfile("Edit profile"),

    /** The devices screen. */
    Devices("Devices"),
}

/**
 * The account settings on a tablet or a desktop: the account menu as a list, and the page it opens
 * beside it — one at a time on a phone, as before.
 */
@Composable
fun AccountSettingsShowcase() = FlowShowcase(
    title = "Account settings",
    summary = "The account menu and the page it opens, side by side on a tablet or a desktop and one at a time on a phone.",
    module = "flow-profile",
    status = ShowcaseStatus.Beta,
    notes = {
        ChromeText(text = "The recipe", role = ChromeTextRole.Label)
        ChromeText(
            text = "AccountSettingsRecipe in this file is the whole assembly: a foundation ListDetail " +
                "with the summary card and the section rows in the list pane, and the open page — the " +
                "Edit profile and Devices recipes, unchanged — in the detail pane. The open page is the " +
                "screen's own state; ListDetail decides how much of it shows.",
            role = ChromeTextRole.SectionDescription,
            tone = ChromeTone.Muted,
        )
        ChromeText(
            text = "Beside the list the open row is marked and the page has no back action; alone, the " +
                "rows carry a chevron and the page a back action. From a tablet on its side up, the " +
                "photo, birthday and confirmation sheets open as centred dialogs; on a phone, a folded " +
                "or unfolded foldable and a tablet held upright they rise from the bottom edge.",
            role = ChromeTextRole.SectionDescription,
            tone = ChromeTone.Muted,
        )
    },
) {
    val preset by enumControl(
        key = "window",
        default = WindowPreset.Laptop,
        options = WindowPreset.entries,
        optionLabel = { it.label },
    )
    val avatar by enumControl(
        key = "avatar",
        default = SampleAvatar.Picture,
        options = SampleAvatar.entries,
        optionLabel = { it.label },
    )

    preview {
        var sessions by remember { mutableStateOf(ProfileSamples.Sessions) }
        var signingOut by remember { mutableStateOf(false) }
        val summary = ProfileSamples.summary(avatar = avatar)

        BoxWithConstraints {
            WindowFrame(preset = preset, maxHeight = maxHeight) {
                AccountSettingsRecipe(
                    summary = summary,
                    deviceCount = sessions.size,
                    confirmingSignOut = signingOut,
                    onSignOut = { signingOut = true },
                    onSignOutDismissed = { signingOut = false },
                    windowInsets = preset.windowInsets,
                    editProfile = { EditProfileScreen(summary = summary) },
                    devices = { DevicesScreen(sessions = sessions, onSessionsChange = { sessions = it }) },
                )
            }
        }
    }
}

/**
 * The recipe: the account menu as a [ListDetail] — the summary card and the section rows in the list
 * pane, the open page in the detail pane.
 *
 * @param summary Who is signed in.
 * @param deviceCount How many devices are signed in, shown on the Devices row.
 * @param confirmingSignOut Whether the sign-out sheet is up.
 * @param onSignOut Called when signing out is asked for.
 * @param onSignOutDismissed Called when the sign-out sheet is dismissed or confirmed.
 * @param windowInsets Regions the system occupies.
 * @param editProfile The edit-profile screen, from its own recipe.
 * @param devices The devices screen, from its own recipe.
 */
@Composable
private fun AccountSettingsRecipe(
    summary: ProfileSummary,
    deviceCount: Int,
    confirmingSignOut: Boolean,
    onSignOut: () -> Unit,
    onSignOutDismissed: () -> Unit,
    windowInsets: WindowInsets,
    editProfile: @Composable () -> Unit,
    devices: @Composable () -> Unit,
) {
    // The open page is the screen's state; ListDetail decides whether it shows beside the list or
    // instead of it.
    var open by rememberSaveable { mutableStateOf<AccountPage?>(null) }

    ListDetail(
        showDetail = open != null,
        modifier = Modifier
            .fillMaxSize()
            .background(ArecaTheme.colors.bgCanvas)
            .windowInsetsPadding(windowInsets),
        detailPlaceholder = {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Fallback(
                    heading = { Text("Choose a setting") },
                    body = { Text("Your profile and your devices open here.") },
                )
            }
        },
        list = {
            // Only a list beside the page marks the open row; alone, it has nothing to point at.
            val marked = open.takeIf { isTwoPane }
            // A chevron says the row pushes a page; beside the page it would promise nothing.
            val chevron: (@Composable BoxScope.() -> Unit)? =
                if (isTwoPane) null else ({ ListItemChevron() })

            Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
                Spacer(Modifier.height(ArecaTheme.spacing.lg))

                ProfileSummaryCard(
                    summary = summary,
                    onClick = { open = AccountPage.EditProfile },
                    modifier = Modifier.horizontalContentInsets(),
                    trailing = { ProfileSummaryCardAction(label = "Edit") },
                    onClickLabel = "Edit your profile",
                )

                Spacer(Modifier.height(ArecaTheme.spacing.lg))

                Settings {
                    // Every row carries a tile, and the tiles already separate the rows.
                    Section(header = { Text("Account") }, separated = false) {
                        SettingsRow(
                            title = { Text(AccountPage.EditProfile.title) },
                            leading = { SettingsRowIcon(ArecaIcons.UserEdit) },
                            trailing = chevron,
                            selected = marked == AccountPage.EditProfile,
                            onClick = { open = AccountPage.EditProfile },
                        )
                        SettingsRow(
                            title = { Text(AccountPage.Devices.title) },
                            value = { Text(deviceCount.toString()) },
                            leading = { SettingsRowIcon(ArecaIcons.Laptop) },
                            trailing = chevron,
                            selected = marked == AccountPage.Devices,
                            onClick = { open = AccountPage.Devices },
                        )
                    }

                    Section { SignOutRow(onClick = onSignOut) }
                }

                Spacer(Modifier.height(ArecaTheme.spacing.xl3))
            }
        },
        detail = {
            // Composed only while a page is open.
            open?.let { page ->
                Column(modifier = Modifier.fillMaxSize()) {
                    TopBar(
                        title = page.title,
                        // A way back only when the list is not beside the page.
                        leading = if (isTwoPane) null else ({ BackAction(onClick = { open = null }) }),
                    )

                    Box(modifier = Modifier.weight(1f)) {
                        when (page) {
                            AccountPage.EditProfile -> editProfile()
                            AccountPage.Devices -> devices()
                        }
                    }
                }
            }
        },
    )

    SignOutSheet(
        visible = confirmingSignOut,
        description = "You'll need to sign in again to use your account on this device.",
        onConfirm = onSignOutDismissed,
        onDismiss = onSignOutDismissed,
    )
}

/**
 * The edit-profile screen with sample state of its own, as the detail pane opens it.
 *
 * @param summary Who is signed in.
 */
@Composable
private fun EditProfileScreen(summary: ProfileSummary) {
    var fields by remember {
        mutableStateOf(
            ProfileFields(
                firstName = "Ada",
                lastName = "Lovelace",
                bio = ProfileSamples.Bio,
                birthday = LocalDate(1815, 12, 10),
            ),
        )
    }
    var picking by remember { mutableStateOf(false) }
    var deleting by remember { mutableStateOf(false) }

    EditProfileRecipe(
        summary = summary,
        fields = fields,
        onFieldsChange = { fields = it },
        saving = false,
        upload = AvatarUploadState.Idle,
        avatarOptions = buildList {
            add(AvatarSourceOptions.camera(onClick = { picking = false }))
            add(AvatarSourceOptions.library(onClick = { picking = false }))
            if (summary.avatar != null) add(AvatarSourceOptions.remove(onClick = { picking = false }))
        },
        pickingAvatar = picking,
        confirmingDelete = deleting,
        onChangePhoto = { picking = true },
        onAvatarSheetDismissed = { picking = false },
        onSave = {
            fields = fields.copy(
                firstNameError = if (fields.firstName.isBlank()) "Enter your first name." else null,
            )
        },
        onDelete = { deleting = true },
        onDeleteConfirmed = { deleting = false },
        onDeleteDismissed = { deleting = false },
        // The settings layout has already kept clear of the system bars.
        windowInsets = WindowInsets(0, 0, 0, 0),
    )
}

/**
 * The devices screen with sample state of its own, as the detail pane opens it.
 *
 * @param sessions Devices signed in; held above, so the list's count follows a revoke.
 * @param onSessionsChange Called with the devices left after a revoke.
 */
@Composable
private fun DevicesScreen(
    sessions: List<SessionItem>,
    onSessionsChange: (List<SessionItem>) -> Unit,
) {
    var revoking by remember { mutableStateOf<SessionItem?>(null) }
    var signingOutOthers by remember { mutableStateOf(false) }

    DevicesRecipe(
        state = ActiveSessionsState.Loaded(sessions),
        revoking = revoking,
        confirmingSignOutOthers = signingOutOthers,
        onSessionClick = { revoking = it },
        onRevokeConfirmed = { session ->
            onSessionsChange(sessions - session)
            revoking = null
        },
        onRevokeDismissed = { revoking = null },
        onSignOutOthers = { signingOutOthers = true },
        onSignOutOthersConfirmed = {
            onSessionsChange(sessions.filter { it.isCurrent })
            signingOutOthers = false
        },
        onSignOutOthersDismissed = { signingOutOthers = false },
        // The settings layout has already kept clear of the system bars.
        windowInsets = WindowInsets(0, 0, 0, 0),
    )
}
