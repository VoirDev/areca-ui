package dev.voir.areca.playground

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Density
import dev.voir.areca.flow.profile.LocalAvatarImageLoader
import dev.voir.areca.flow.profile.part.ProfileSummaryCardAction
import dev.voir.areca.flow.profile.row.UsernameCheck
import dev.voir.areca.flow.profile.part.AvatarUploadState
import dev.voir.areca.flow.profile.part.AvatarUploadStatus
import dev.voir.areca.flow.profile.part.EditableAvatar
import dev.voir.areca.flow.profile.part.ProfileCardAction
import dev.voir.areca.flow.profile.part.ProfileCoverCard
import dev.voir.areca.flow.profile.part.ProfileGreeting
import dev.voir.areca.flow.profile.part.ProfileHeaderCard
import dev.voir.areca.flow.profile.part.ProfileSummaryCard
import dev.voir.areca.flow.profile.part.ProfileSummaryCardPlaceholder
import dev.voir.areca.flow.profile.part.StaleDetailsNotice
import dev.voir.areca.flow.profile.row.AccountRow
import dev.voir.areca.flow.profile.row.AddAccountRow
import dev.voir.areca.flow.profile.row.BirthdayRow
import dev.voir.areca.flow.profile.row.DataExportRow
import dev.voir.areca.flow.profile.row.DataExportState
import dev.voir.areca.flow.profile.row.DeleteAccountRow
import dev.voir.areca.flow.profile.row.EmailRow
import dev.voir.areca.flow.profile.row.LinkedAccountRow
import dev.voir.areca.flow.profile.row.ProfileInfoRow
import dev.voir.areca.flow.profile.row.SignOutRow
import dev.voir.areca.flow.profile.row.UsernameCheckFooter
import dev.voir.areca.flow.profile.session.ActiveSessionsSections
import dev.voir.areca.flow.profile.session.ActiveSessionsState
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.button.ButtonSize
import dev.voir.areca.foundation.button.ButtonStyle
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.list.ListItemChevron
import dev.voir.areca.foundation.settings.Section
import dev.voir.areca.foundation.settings.Settings
import dev.voir.areca.foundation.settings.SettingsRowIcon
import dev.voir.areca.foundation.settings.SettingsTextFieldRow
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.AtSign
import dev.voir.areca.icons.Edit
import dev.voir.areca.icons.Gift
import dev.voir.areca.icons.Phone
import dev.voir.areca.icons.QrCode
import dev.voir.areca.icons.Share
import dev.voir.areca.playground.flow.profile.ProfileSamples
import dev.voir.areca.playground.flow.profile.SampleAvatar
import dev.voir.areca.playground.flow.profile.SampleAvatarLoader
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.ArecaThemeProvider
import dev.voir.areca.theme.horizontalContentInsets
import kotlinx.datetime.LocalDate
import org.jetbrains.skia.EncodedImageFormat
import java.io.File
import kotlin.test.Test

/** Logical width of the device the screens are rendered at. */
private const val WIDTH = 390

/** Logical height of the device the screens are rendered at. */
private const val HEIGHT = 1150

/** Pixel density the scenes are rasterised at, so the PNGs are legible when opened. */
private const val DENSITY = 2f

/** Where the rendered screens are written. */
private val OUTPUT = File(System.getProperty("render.out") ?: "build/screens")

/**
 * Renders one screen to a PNG, on the canvas the profile recipes draw on.
 *
 * @param name File name, without the extension.
 * @param dark Whether to render in the dark theme.
 * @param content Screen to render.
 */
@OptIn(ExperimentalComposeUiApi::class)
private fun render(name: String, dark: Boolean = false, content: @Composable () -> Unit) {
    ImageComposeScene(
        width = (WIDTH * DENSITY).toInt(),
        height = (HEIGHT * DENSITY).toInt(),
        density = Density(DENSITY),
    ) {
        ArecaThemeProvider(darkTheme = dark) {
            CompositionLocalProvider(LocalAvatarImageLoader provides SampleAvatarLoader) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .background(ArecaTheme.colors.bgCanvas)
                        .verticalScroll(rememberScrollState()),
                ) {
                    Spacer(Modifier.height(ArecaTheme.spacing.lg))
                    content()
                }
            }
        }
    }.let { scene ->
        try {
            val data = scene.render().encodeToData(EncodedImageFormat.PNG)!!
            OUTPUT.mkdirs()
            File(OUTPUT, "$name.png").writeBytes(data.bytes)
        } finally {
            scene.close()
        }
    }
}

/**
 * Renders the profile recipes to `build/screens`, in both themes.
 *
 * Not an assertion — see [AuthScreenRenderTest] for why. It fails when any assembly throws during
 * composition or layout, and it is the only way to look at these screens on a machine with no
 * display. Sheets are dialogs, which an image scene does not draw; the playground shows those.
 *
 * ```bash
 * ./gradlew :playground:jvmTest --tests '*ProfileScreenRenderTest*'
 * ```
 */
class ProfileScreenRenderTest {

    @Test
    fun profile() = bothThemes("profile") { ProfileScreen() }

    @Test
    fun `profile with a cover`() = bothThemes("profile-cover") { CoverScreen() }

    @Test
    fun `edit profile`() = bothThemes("edit-profile") { EditScreen() }

    @Test
    fun account() = bothThemes("account") { AccountScreen() }

    @Test
    fun devices() = bothThemes("devices") { DevicesScreen(ActiveSessionsState.Loaded(ProfileSamples.Sessions)) }

    @Test
    fun `devices loading and failed`() = render("devices-states") {
        DevicesScreen(ActiveSessionsState.Loading)
        DevicesScreen(ActiveSessionsState.Failed("The server didn't answer in time."))
    }

    @Test
    fun username() = bothThemes("username") { UsernameScreen() }

    /**
     * Renders [content] once per theme.
     *
     * @param name File name stem; the dark render takes a `-dark` suffix.
     * @param content Screen to render.
     */
    private fun bothThemes(name: String, content: @Composable () -> Unit) {
        render(name, dark = false, content = content)
        render("$name-dark", dark = true, content = content)
    }
}

/** The profile screen, as the showcase recipe assembles it. */
@Composable
private fun ProfileScreen() {

    StaleDetailsNotice(
        message = "We couldn't reach the server, so these are the details saved on this device.",
        modifier = Modifier.horizontalContentInsets(),
    )
    Spacer(Modifier.height(ArecaTheme.spacing.lg))
    ProfileHeaderCard(
        summary = ProfileSamples.summary(),
        modifier = Modifier.horizontalContentInsets(),
        actions = {
            ProfileCardAction(ArecaIcons.Edit, "Edit", onClick = {}, modifier = Modifier.weight(1f))
            ProfileCardAction(ArecaIcons.Share, "Share", onClick = {}, modifier = Modifier.weight(1f))
            ProfileCardAction(ArecaIcons.QrCode, "QR code", onClick = {}, modifier = Modifier.weight(1f))
        },
    )
    Spacer(Modifier.height(ArecaTheme.spacing.lg))
    Settings {
        Section(header = { Text("Your info") }, separated = false) {
            ProfileInfoRow(
                value = "+381 64 123 45 67",
                label = "Mobile",
                leading = { SettingsRowIcon(ArecaIcons.Phone) },
            )
            ProfileInfoRow(
                value = "@ada",
                label = "Username",
                leading = { SettingsRowIcon(ArecaIcons.AtSign) },
            )
            EmailRow(email = "ada@analytical.engine", isVerified = true)
            ProfileInfoRow(
                value = "10 December 1815",
                label = "Birthday",
                leading = { SettingsRowIcon(ArecaIcons.Gift) },
            )
        }
    }
}

/** The profile screen opening with the cover card. */
@Composable
private fun CoverScreen() {
    ProfileCoverCard(
        summary = ProfileSamples.summary(),
        bio = ProfileSamples.Bio,
        cover = { ProfileSamples.Cover() },
        modifier = Modifier.horizontalContentInsets(),
        actions = {
            Button(style = ButtonStyle.soft(ActionColor.Neutral), size = ButtonSize.Small, onClick = {}) {
                Text("Edit profile")
            }
        },
    )
    Spacer(Modifier.height(ArecaTheme.spacing.lg))
    ProfileCoverCard(
        summary = ProfileSamples.summary(avatar = SampleAvatar.None, isVerified = false),
        modifier = Modifier.horizontalContentInsets(),
    )
    Spacer(Modifier.height(ArecaTheme.spacing.lg))
    ProfileGreeting(
        summary = ProfileSamples.summary(),
        onClick = {},
        modifier = Modifier.horizontalContentInsets(),
        greeting = "Welcome back",
    )
}

/** The edit screen, as the showcase recipe assembles it, with a failed upload. */
@Composable
private fun EditScreen() {

    EditableAvatar(
        summary = ProfileSamples.summary(),
        onChangePhoto = {},
        modifier = Modifier.horizontalContentInsets(),
    )
    AvatarUploadStatus(
        state = AvatarUploadState.Failed("The picture is larger than 10 MB."),
        onRetry = {},
        onDismiss = {},
        modifier = Modifier.horizontalContentInsets(),
    )
    Spacer(Modifier.height(ArecaTheme.spacing.xl2))
    Settings {
        Section(
            header = { Text("Your name") },
            footer = { Text("Enter your first name.", color = ArecaTheme.colors.fgDangerText) },
        ) {
            SettingsTextFieldRow(value = "", onValueChange = {}, placeholder = "First name (required)")
            SettingsTextFieldRow(value = "Lovelace", onValueChange = {}, placeholder = "Last name (optional)")
        }
        Section(footer = { Text("A few words about you.") }) {
            SettingsTextFieldRow(
                value = ProfileSamples.Bio,
                onValueChange = {},
                placeholder = "Bio",
                maxLength = 140,
                singleLine = false,
            )
        }
        Section(header = { Text("Your info") }, separated = false) {
            ProfileInfoRow(
                value = "+381 64 123 45 67",
                label = "Tap to change phone number",
                leading = { SettingsRowIcon(ArecaIcons.Phone) },
                onClick = {},
            )
            ProfileInfoRow(
                value = "@ada",
                label = "Username",
                leading = { SettingsRowIcon(ArecaIcons.AtSign) },
                trailing = { ListItemChevron() },
                onClick = {},
            )
            BirthdayRow(value = LocalDate(1815, 12, 10), onValueChange = {})
        }
        Section { DeleteAccountRow(onClick = {}, subtitle = "Permanent, and cannot be undone") }
    }
}

/** The account menu, as the showcase recipe assembles it. */
@Composable
private fun AccountScreen() {
    val summary = ProfileSamples.summary(isVerified = false)

    ProfileSummaryCard(
        summary = summary,
        onClick = {},
        modifier = Modifier.horizontalContentInsets(),
        trailing = { ProfileSummaryCardAction(label = "Edit") },
    )
    Spacer(Modifier.height(ArecaTheme.spacing.lg))
    ProfileSummaryCardPlaceholder(modifier = Modifier.horizontalContentInsets())
    Spacer(Modifier.height(ArecaTheme.spacing.lg))
    Settings {
        Section(header = { Text("Sign-in methods") }) {
            EmailRow(email = summary.email, isVerified = false, onVerify = {}, onClick = {}, unverifiedBadge = "Unverified")
            LinkedAccountRow(
                name = "Google",
                icon = { ProfileSamples.ProviderMark() },
                isLinked = true,
                account = summary.email,
                canUnlink = false,
                lockedReason = "Your only way to sign in",
                onLink = {},
                onUnlink = {},
            )
            LinkedAccountRow(
                name = "Apple",
                icon = { ProfileSamples.ProviderMark() },
                isLinked = false,
                onLink = {},
                onUnlink = {},
            )
        }
        Section(header = { Text("Your data") }) {
            DataExportRow(
                state = DataExportState.Ready,
                onRequest = {},
                onDownload = {},
                readySubtitle = "Your copy is ready",
            )
            DataExportRow(state = DataExportState.Failed("We couldn't prepare your copy."), onRequest = {}, onDownload = {})
        }
        Section(header = { Text("Accounts") }) {
            AccountRow(account = summary, isCurrent = true, onClick = null, currentDescription = "Current account")
            ProfileSamples.OtherAccounts.forEach {
                AccountRow(account = it, onClick = {}, trailing = { ListItemChevron() })
            }
            AddAccountRow(onClick = {})
        }
        Section { SignOutRow(onClick = {}) }
        Section { DeleteAccountRow(onClick = {}, subtitle = "Permanent, and cannot be undone") }
    }
}

/**
 * The devices screen, as the showcase recipe assembles it.
 *
 * @param state Device list to draw.
 */
@Composable
private fun DevicesScreen(state: ActiveSessionsState) {
    Settings {
        ActiveSessionsSections(
            state = state,
            onSessionClick = {},
            onSignOutOthers = {},
            onRetry = {},
            footer = "Areca is available on Android, iPhone, iPad, Windows, macOS and the web.",
            signOutOthersFooter = "Signs out every device except this one.",
        )
    }
    Spacer(Modifier.height(ArecaTheme.spacing.lg))
}

/** The username screen in each verdict. */
@Composable
private fun UsernameScreen() {

    Settings {
        listOf(UsernameCheck.Idle, UsernameCheck.Checking, UsernameCheck.Available, UsernameCheck.Taken)
            .forEach { check ->
                Section(
                    header = { Text("Username") },
                    footer = {
                        UsernameCheckFooter(
                            username = "ada_lovelace",
                            check = check,
                            hint = "People can find you by this name.",
                        )
                    },
                ) {
                    SettingsTextFieldRow(
                        value = "ada_lovelace",
                        onValueChange = {},
                        placeholder = "Username",
                        leading = { Text("@") },
                    )
                }
            }
    }
}
