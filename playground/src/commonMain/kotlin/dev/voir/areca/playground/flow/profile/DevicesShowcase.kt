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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dev.voir.areca.flow.profile.session.ActiveSessionsSections
import dev.voir.areca.flow.profile.session.ActiveSessionsState
import dev.voir.areca.flow.profile.session.SessionItem
import dev.voir.areca.flow.profile.sheet.RevokeSessionSheet
import dev.voir.areca.flow.profile.sheet.SignOutOthersSheet
import dev.voir.areca.foundation.settings.Section
import dev.voir.areca.foundation.settings.Settings
import dev.voir.areca.foundation.settings.SettingsSelectRow
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

/** How the device list loaded. */
private enum class DevicesLoad(
    /** Label of the option in the control panel. */
    val label: String,
) {
    /** The list arrived. */
    Loaded("loaded"),

    /** Still arriving. */
    Loading("loading"),

    /** Could not be fetched. */
    Failed("failed"),
}

/**
 * The devices screen: this device, the others, and when old ones sign out on their own. Live —
 * signing a device out removes it.
 */
@Composable
fun DevicesShowcase() = FlowShowcase(
    title = "Devices",
    summary = "Where the account is signed in: this device, every other one, and signing them out.",
    module = "flow-profile",
    status = ShowcaseStatus.Beta,
    notes = {
        ChromeText(text = "The recipe", role = ChromeTextRole.Label)
        ChromeText(
            text = "DevicesRecipe emits ActiveSessionsSections — this device with the one destructive " +
                "action, then every other session — into a foundation Settings list, followed by the " +
                "product's own auto-sign-out choice.",
            role = ChromeTextRole.SectionDescription,
            tone = ChromeTone.Muted,
        )
        ChromeText(
            text = "Press a device for the revoke sheet. Loading and failure stay inside the sessions " +
                "card; the sections around it keep working.",
            role = ChromeTextRole.SectionDescription,
            tone = ChromeTone.Muted,
        )
    },
) {
    val load by enumControl(
        key = "list",
        default = DevicesLoad.Loaded,
        options = DevicesLoad.entries,
        optionLabel = { it.label },
    )

    preview {
        var sessions by remember { mutableStateOf(ProfileSamples.Sessions) }
        var revoking by remember { mutableStateOf<SessionItem?>(null) }
        var signingOutOthers by remember { mutableStateOf(false) }

        DeviceFrame(size = DeviceSize.Regular) {
            DevicesRecipe(
                state = when (load) {
                    DevicesLoad.Loaded -> ActiveSessionsState.Loaded(sessions)
                    DevicesLoad.Loading -> ActiveSessionsState.Loading
                    DevicesLoad.Failed -> ActiveSessionsState.Failed("The server didn't answer in time.")
                },
                revoking = revoking,
                confirmingSignOutOthers = signingOutOthers,
                onSessionClick = { revoking = it },
                onRevokeConfirmed = { session ->
                    sessions = sessions - session
                    revoking = null
                },
                onRevokeDismissed = { revoking = null },
                onSignOutOthers = { signingOutOthers = true },
                onSignOutOthersConfirmed = {
                    sessions = sessions.filter { it.isCurrent }
                    signingOutOthers = false
                },
                onSignOutOthersDismissed = { signingOutOthers = false },
                windowInsets = DeviceSize.Regular.windowInsets,
            )
        }
    }
}

/**
 * The recipe: a devices screen built from `flow-profile`'s session sections and sheets on the
 * foundation settings list.
 *
 * @param state The device list.
 * @param revoking The session whose revoke sheet is up, or `null`.
 * @param confirmingSignOutOthers Whether the sign-out-others sheet is up.
 * @param onSessionClick Called with a pressed session.
 * @param onRevokeConfirmed Called with the session whose revoke was confirmed.
 * @param onRevokeDismissed Called when the revoke sheet is dismissed.
 * @param onSignOutOthers Called when every other session is asked to end.
 * @param onSignOutOthersConfirmed Called when that is confirmed.
 * @param onSignOutOthersDismissed Called when that sheet is dismissed.
 * @param windowInsets Regions the system occupies.
 */
@Composable
private fun DevicesRecipe(
    state: ActiveSessionsState,
    revoking: SessionItem?,
    confirmingSignOutOthers: Boolean,
    onSessionClick: (SessionItem) -> Unit,
    onRevokeConfirmed: (SessionItem) -> Unit,
    onRevokeDismissed: () -> Unit,
    onSignOutOthers: () -> Unit,
    onSignOutOthersConfirmed: () -> Unit,
    onSignOutOthersDismissed: () -> Unit,
    windowInsets: WindowInsets,
) {
    var inactivity by remember { mutableStateOf(InactivityOptions[1]) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ArecaTheme.colors.bgCanvas)
            .verticalScroll(rememberScrollState())
            .windowInsetsPadding(windowInsets),
    ) {
        Spacer(Modifier.height(ArecaTheme.spacing.lg))

        Settings {
            ActiveSessionsSections(
                state = state,
                onSessionClick = onSessionClick,
                onSignOutOthers = onSignOutOthers,
                onRetry = {},
                footer = "Areca is available on Android, iPhone, iPad, Windows, macOS and the web.",
                signOutOthersFooter = "Signs out every device except this one.",
                sessionClickLabel = "Sign out device",
                emptySubtitle = "Only this device is signed in",
            )

            Section(header = { Text("Automatically sign out old devices") }) {
                SettingsSelectRow(
                    title = { Text("If inactive for") },
                    options = InactivityOptions,
                    selected = inactivity,
                    onSelect = { inactivity = it },
                    optionLabel = { it },
                )
            }
        }

        Spacer(Modifier.height(ArecaTheme.spacing.xl3))
    }

    RevokeSessionSheet(
        session = revoking,
        note = "It will need to sign in again to use your account.",
        onConfirm = onRevokeConfirmed,
        onDismiss = onRevokeDismissed,
    )

    SignOutOthersSheet(
        visible = confirmingSignOutOthers,
        description = "Every other device will need to sign in again.",
        note = "You'll stay signed in on this device.",
        onConfirm = onSignOutOthersConfirmed,
        onDismiss = onSignOutOthersDismissed,
    )
}

/** Choices for how long a device may be idle before it is signed out. */
private val InactivityOptions: List<String> = listOf("1 week", "1 month", "3 months", "6 months")
