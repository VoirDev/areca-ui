package dev.voir.areca.playground.foundation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.voir.areca.foundation.avatar.Avatar
import dev.voir.areca.foundation.avatar.AvatarSize
import dev.voir.areca.foundation.badge.Badge
import dev.voir.areca.foundation.badge.BadgeStyle
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.icon.FeaturedIcon
import dev.voir.areca.foundation.icon.FeaturedIconSize
import dev.voir.areca.foundation.icon.FeaturedIconStyle
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.foundation.list.ListItemChevron
import dev.voir.areca.foundation.selection.NumberStepper
import dev.voir.areca.foundation.selection.Swatch
import dev.voir.areca.foundation.selection.SwatchSize
import dev.voir.areca.foundation.selection.SwatchStyle
import dev.voir.areca.foundation.selection.ToggleState
import dev.voir.areca.foundation.settings.Section
import dev.voir.areca.foundation.settings.Settings
import dev.voir.areca.foundation.settings.SettingsBlock
import dev.voir.areca.foundation.settings.SettingsOptionRow
import dev.voir.areca.foundation.settings.SettingsRow
import dev.voir.areca.foundation.settings.SettingsRowIcon
import dev.voir.areca.foundation.settings.SettingsRowSkeleton
import dev.voir.areca.foundation.settings.SettingsRowStatus
import dev.voir.areca.foundation.settings.SettingsRowTone
import dev.voir.areca.foundation.settings.SettingsSelectRow
import dev.voir.areca.foundation.settings.SettingsTextFieldRow
import dev.voir.areca.foundation.settings.SettingsToggleRow
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.appendTextLink
import dev.voir.areca.foundation.text.textLinkStyles
import dev.voir.areca.icons.Announcement
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.AtSign
import dev.voir.areca.icons.Bell
import dev.voir.areca.icons.Brush
import dev.voir.areca.icons.Gift
import dev.voir.areca.icons.Globe
import dev.voir.areca.icons.Hand
import dev.voir.areca.icons.Image
import dev.voir.areca.icons.Laptop
import dev.voir.areca.icons.Lock
import dev.voir.areca.icons.LogOut
import dev.voir.areca.icons.MagicWand01
import dev.voir.areca.icons.Monitor
import dev.voir.areca.icons.MoonAlt
import dev.voir.areca.icons.MoonStar
import dev.voir.areca.icons.Palette
import dev.voir.areca.icons.Phone
import dev.voir.areca.icons.Play
import dev.voir.areca.icons.QrCode
import dev.voir.areca.icons.Shield
import dev.voir.areca.icons.Tablet
import dev.voir.areca.icons.User
import dev.voir.areca.theme.ArecaTheme
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

/*
 * Whole settings screens, shared by the showcase recipes and the render test.
 *
 * Each one is modelled on a real screen of a messaging app, because a settings component is only
 * as good as the ugliest real screen it has to build: editable names, a bio with a counter, colored
 * tiles, inline values, split switches, device lists and a destructive row at the bottom.
 */

/** A session on another device, as the devices screen lists it. */
private data class SampleSession(
    /** Device name. */
    val device: String,
    /** Client and version. */
    val client: String,
    /** Where and when it was last active. */
    val seen: String,
    /** Glyph of the kind of device. */
    val icon: ImageVector,
)

/** How long a session may stay inactive before it is signed out. */
private val InactivityOptions = listOf("1 week", "1 month", "3 months", "6 months", "1 year")

/** Microphones a voice message can be recorded with. */
private val MicrophoneOptions = listOf("Built-in", "Headset", "Bluetooth")

/** Units distances are shown in. */
private val DistanceOptions = listOf("Kilometers", "Miles")

/**
 * The account screen: editable name and bio, contact details on colored tiles, and the accounts
 * signed in on this device.
 */
@Composable
internal fun AccountSettingsSample() {
    var firstName by remember { mutableStateOf("Alex") }
    var lastName by remember { mutableStateOf("Mercer") }
    var bio by remember { mutableStateOf("Product designer. Building calm software in Belgrade.") }

    Settings(modifier = Modifier.fillMaxWidth()) {
        Section(header = { Text("Your name") }) {
            SettingsTextFieldRow(
                value = firstName,
                onValueChange = { firstName = it },
                placeholder = "First name (required)",
            )
            SettingsTextFieldRow(
                value = lastName,
                onValueChange = { lastName = it },
                placeholder = "Last name (optional)",
            )
        }

        Section(footer = { Text("A few words about you.") }) {
            SettingsTextFieldRow(
                value = bio,
                onValueChange = { bio = it },
                placeholder = "Bio",
                maxLength = 70,
                singleLine = false,
            )
        }

        Section(
            header = { Text("Your info") },
            separated = false,
            footer = { BirthdayFooter() },
        ) {
            SettingsRow(
                title = { Text("+381 64 123 45 67") },
                subtitle = { Text("Tap to change phone number") },
                leading = { SettingsRowIcon(ArecaIcons.Phone, color = ActionColor.Success) },
                onClick = {},
            )
            SettingsRow(
                title = { Text("@alexmercer") },
                subtitle = { Text("Username") },
                leading = { SettingsRowIcon(ArecaIcons.AtSign, color = ActionColor.Warning) },
                onClick = {},
            )
            SettingsRow(
                title = { Text("Oct 04") },
                subtitle = { Text("Birthday") },
                leading = { SettingsRowIcon(ArecaIcons.Gift) },
                onClick = {},
            )
        }

        Section(
            separated = false,
            footer = { Text("Add a bot to answer messages on your behalf.") },
        ) {
            SettingsRow(
                title = { Text("Personal channel") },
                leading = { SettingsRowIcon(ArecaIcons.Announcement, color = ActionColor.Warning) },
                value = { Text("Add") },
                onClick = {},
            )
            SettingsRow(
                title = { Text("Chat automation") },
                leading = { SettingsRowIcon(ArecaIcons.MagicWand01) },
                // A feature being introduced is the accent this screen needs, so the badge names the
                // brand; every other badge in the list stays neutral.
                badge = { Badge(style = BadgeStyle.Brand) { Text("NEW") } },
                onClick = {},
            )
        }

        Section(header = { Text("Accounts") }) {
            listOf("Alex Mercer", "Studio Mercer", "Night shift").forEach { name ->
                SettingsRow(
                    title = { Text(name) },
                    leading = { Avatar(size = AvatarSize.ExtraSmall, letters = name) },
                    trailing = { ListItemChevron() },
                    onClick = {},
                )
            }
        }

        Section {
            SettingsRow(
                title = { Text("Log out") },
                leading = { Icon(ArecaIcons.LogOut, contentDescription = null) },
                tone = SettingsRowTone.Danger,
                onClick = {},
            )
        }
    }
}

/**
 * The chat settings screen: a live preview, brand actions, a split switch, switches with long
 * explanations and values picked from a menu.
 */
@Composable
internal fun ChatSettingsSample() {
    var textSize by remember { mutableIntStateOf(16) }
    var theme by remember { mutableIntStateOf(0) }
    var autoNight by remember { mutableStateOf<ToggleState>(ToggleState.On) }
    var browser by remember { mutableStateOf<ToggleState>(ToggleState.Off) }
    var animations by remember { mutableStateOf<ToggleState>(ToggleState.On) }
    var nextMedia by remember { mutableStateOf<ToggleState>(ToggleState.On) }
    var raiseToListen by remember { mutableStateOf<ToggleState>(ToggleState.On) }
    var raiseToSpeak by remember { mutableStateOf<ToggleState>(ToggleState.Off) }
    var microphone by remember { mutableStateOf(MicrophoneOptions.first()) }
    var directShare by remember { mutableStateOf(true) }
    var sendWithEnter by remember { mutableStateOf(false) }
    var distance by remember { mutableStateOf(DistanceOptions.first()) }

    Settings(modifier = Modifier.fillMaxWidth()) {
        Section(header = { Text("Message text size") }) {
            SettingsRow(
                title = { Text("Text size") },
                subtitle = { Text("Applies to every chat") },
                trailing = {
                    NumberStepper(
                        value = textSize,
                        onValueChange = { textSize = it },
                        // The stepper fills whatever width it is given; a row gives it the rest of the line.
                        modifier = Modifier.width(128.dp),
                        minValue = 12,
                        maxValue = 24,
                    )
                },
            )
            SettingsBlock { ChatPreview(textSize = textSize) }
            SettingsRow(
                title = { Text("Change chat wallpaper") },
                leading = { Icon(ArecaIcons.Image, contentDescription = null) },
                tone = SettingsRowTone.Brand,
                onClick = {},
            )
            SettingsRow(
                title = { Text("Change name color") },
                leading = { Icon(ArecaIcons.Palette, contentDescription = null) },
                trailing = { NameColorSwatch() },
                tone = SettingsRowTone.Brand,
                onClick = {},
            )
        }

        Section(header = { Text("Color theme") }) {
            SettingsBlock { ThemePicker(selected = theme, onSelect = { theme = it }) }
            SettingsRow(
                title = { Text("Switch to night mode") },
                leading = { Icon(ArecaIcons.MoonStar, contentDescription = null) },
                tone = SettingsRowTone.Brand,
                onClick = {},
            )
            SettingsRow(
                title = { Text("Browse themes") },
                leading = { Icon(ArecaIcons.Brush, contentDescription = null) },
                tone = SettingsRowTone.Brand,
                onClick = {},
            )
        }

        Section {
            SettingsToggleRow(
                state = autoNight,
                onToggle = { autoNight = it },
                title = { Text("Auto-night mode") },
                subtitle = { Text("Adaptive dark") },
                leading = { Icon(ArecaIcons.MoonAlt, contentDescription = null) },
                onClick = {},
                onClickLabel = "Open auto-night settings",
            )
            SettingsToggleRow(
                state = browser,
                onToggle = { browser = it },
                title = { Text("In-app browser") },
                subtitle = { Text("Open external links within the app") },
                leading = { Icon(ArecaIcons.Globe, contentDescription = null) },
            )
            SettingsToggleRow(
                state = animations,
                onToggle = { animations = it },
                title = { Text("Animations") },
                subtitle = { Text("Reduce motion effects to save power") },
                leading = { Icon(ArecaIcons.Play, contentDescription = null) },
            )
        }

        Section(header = { Text("Media and sound") }) {
            SettingsToggleRow(
                state = nextMedia,
                onToggle = { nextMedia = it },
                title = { Text("Tap to show next media") },
                subtitle = { Text("Tap near the edge of the screen while viewing media to navigate") },
            )
            SettingsToggleRow(
                state = raiseToListen,
                onToggle = { raiseToListen = it },
                title = { Text("Raise to listen") },
                subtitle = { Text("Switch sound to the earpiece by raising the phone to your ear") },
            )
            SettingsToggleRow(
                state = raiseToSpeak,
                onToggle = { raiseToSpeak = it },
                title = { Text("Raise to speak") },
                subtitle = { Text("Record voice messages by raising the phone to your ear") },
            )
            SettingsSelectRow(
                title = { Text("Microphone for voice messages") },
                options = MicrophoneOptions,
                selected = microphone,
                onSelect = { microphone = it },
                optionLabel = { it },
            )
        }

        Section(header = { Text("Other settings") }) {
            SettingsToggleRow(
                checked = directShare,
                onCheckedChange = { directShare = it },
                title = { Text("Direct share") },
                subtitle = { Text("Show recent chats in the share menu") },
            )
            SettingsToggleRow(
                checked = sendWithEnter,
                onCheckedChange = { sendWithEnter = it },
                title = { Text("Send with Enter") },
            )
            SettingsSelectRow(
                title = { Text("Distance units") },
                options = DistanceOptions,
                selected = distance,
                onSelect = { distance = it },
                optionLabel = { it },
            )
        }
    }
}

/**
 * The devices screen: a promotional block with its own button, this device on its own card with the
 * destructive action beside it, the other sessions, and a timeout picked from a menu.
 */
@Composable
internal fun DevicesSettingsSample() {
    var inactivity by remember { mutableStateOf(InactivityOptions.first()) }
    var sessions by remember {
        mutableStateOf(
            listOf(
                SampleSession("MacBook Pro", "Desktop 7.2.9 arm64", "Belgrade, Serbia · 10:18", ArecaIcons.Laptop),
                SampleSession("Studio PC", "Desktop 7.2.9 x64", "Belgrade, Serbia · Thu", ArecaIcons.Monitor),
                SampleSession("iPad Air", "iPadOS 12.4", "Novi Sad, Serbia · Mon", ArecaIcons.Tablet),
            )
        )
    }

    Settings(modifier = Modifier.fillMaxWidth()) {
        Section {
            SettingsBlock(
                contentPadding = PaddingValues(ArecaTheme.spacing.xl),
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg),
                ) {
                    FeaturedIcon(
                        imageVector = ArecaIcons.Laptop,
                        contentDescription = null,
                        size = FeaturedIconSize.ExtraLarge,
                        style = FeaturedIconStyle.soft(ActionColor.Brand),
                    )
                    Text(
                        text = "Sign in on desktop or web by scanning a QR code.",
                        textAlign = TextAlign.Center,
                    )
                    Button(
                        onClick = {},
                        modifier = Modifier.fillMaxWidth(),
                        leading = { Icon(ArecaIcons.QrCode, contentDescription = null) },
                    ) {
                        Text("Add device")
                    }
                }
            }
        }

        Section(
            header = { Text("This device") },
            footer = { Text("Signs out every device except this one.") },
        ) {
            SessionRow(
                session = SampleSession("Pixel 9 Pro", "Android 12.10.3", "Belgrade, Serbia · online", ArecaIcons.Phone),
                current = true,
            )
            SettingsRow(
                title = { Text("Terminate all other sessions") },
                leading = { Icon(ArecaIcons.Hand, contentDescription = null) },
                tone = SettingsRowTone.Danger,
                enabled = sessions.isNotEmpty(),
                onClick = { sessions = emptyList() },
            )
        }

        Section(
            header = { Text("Active sessions") },
            footer = { Text("The app is available on Android, iPhone, iPad, Windows, macOS and Linux.") },
        ) {
            if (sessions.isEmpty()) {
                SettingsRow(title = { Text("No other sessions") }, subtitle = { Text("Only this device is signed in") })
            }
            sessions.forEach { session ->
                SessionRow(session = session, current = false)
            }
        }

        Section(header = { Text("Automatically terminate old sessions") }) {
            SettingsSelectRow(
                title = { Text("If inactive for") },
                options = InactivityOptions,
                selected = inactivity,
                onSelect = { inactivity = it },
                optionLabel = { it },
            )
        }
    }
}

/**
 * A privacy screen that saves each change to a server, showing the row statuses in turn.
 *
 * Every other change fails, so both endings of the flow can be seen without a backend.
 */
@Composable
internal fun PrivacySettingsSample() {
    var twoStep by remember { mutableStateOf<ToggleState>(ToggleState.Off) }
    var twoStepStatus by remember { mutableStateOf<SettingsRowStatus>(SettingsRowStatus.Idle) }
    var requested by remember { mutableStateOf<ToggleState?>(null) }
    var attempts by remember { mutableIntStateOf(0) }
    var lastSeen by remember { mutableStateOf("My contacts") }
    var lastSeenStatus by remember { mutableStateOf<SettingsRowStatus>(SettingsRowStatus.Idle) }
    var pendingLastSeen by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(requested) {
        val next = requested ?: return@LaunchedEffect
        twoStepStatus = SettingsRowStatus.Loading
        delay(1200.milliseconds)
        attempts++
        if (attempts % 2 == 0) {
            twoStepStatus = SettingsRowStatus.Error("Couldn't reach the server. Try again.")
        } else {
            twoStep = next
            twoStepStatus = SettingsRowStatus.Idle
        }
        requested = null
    }

    LaunchedEffect(pendingLastSeen) {
        val next = pendingLastSeen ?: return@LaunchedEffect
        lastSeenStatus = SettingsRowStatus.Loading
        delay(900.milliseconds)
        lastSeen = next
        lastSeenStatus = SettingsRowStatus.Idle
        pendingLastSeen = null
    }

    Settings(modifier = Modifier.fillMaxWidth()) {
        Section(
            header = { Text("Security") },
            footer = { Text("Changes are saved to your account and apply on every device.") },
        ) {
            SettingsToggleRow(
                state = twoStep,
                onToggle = { requested = it },
                title = { Text("Two-step verification") },
                subtitle = { Text("Ask for a password on new devices") },
                leading = { SettingsRowIcon(ArecaIcons.Shield, color = ActionColor.Success) },
                status = twoStepStatus,
            )
            SettingsSelectRow(
                title = { Text("Last seen") },
                options = listOf("Everybody", "My contacts", "Nobody"),
                selected = lastSeen,
                onSelect = { pendingLastSeen = it },
                optionLabel = { it },
                leading = { SettingsRowIcon(ArecaIcons.User) },
                status = lastSeenStatus,
            )
            SettingsRow(
                title = { Text("Passcode lock") },
                leading = { SettingsRowIcon(ArecaIcons.Lock, color = ActionColor.Neutral) },
                value = { Text("Off") },
                trailing = { ListItemChevron() },
                onClick = {},
            )
        }
    }
}

/** Settings of a screen that has not loaded yet, holding the height of the rows to come. */
@Composable
internal fun LoadingSettingsSample() {
    Settings(modifier = Modifier.fillMaxWidth()) {
        Section(header = { Text("Notifications") }) {
            SettingsRowSkeleton(showLeading = true, showTrailing = true, titleWidthFraction = 0.5f)
            SettingsRowSkeleton(showLeading = true, showSubtitle = true, showTrailing = true)
            SettingsRowSkeleton(showLeading = true, showTrailing = true, titleWidthFraction = 0.35f)
        }
    }
}

/** A choice laid out as a section of options rather than a menu. */
@Composable
internal fun AutoLockSample() {
    val options = listOf("Away for 1 minute", "Away for 5 minutes", "Away for 1 hour", "Never")
    var selected by remember { mutableStateOf(options[1]) }

    Settings(modifier = Modifier.fillMaxWidth()) {
        Section(
            header = { Text("Auto-lock") },
            footer = { Text("The app asks for your passcode after this long in the background.") },
        ) {
            options.forEach { option ->
                SettingsOptionRow(
                    selected = option == selected,
                    onClick = { selected = option },
                    title = { Text(option) },
                    badge = if (option == options[1]) {
                        { Badge { Text("Recommended") } }
                    } else {
                        null
                    },
                )
            }
        }
    }
}

/** Settings that only mean something while the one above them is on, indented beneath it. */
@Composable
internal fun DependentSettingsSample() {
    var notifications by remember { mutableStateOf<ToggleState>(ToggleState.On) }
    var preview by remember { mutableStateOf<ToggleState>(ToggleState.On) }
    var sound by remember { mutableStateOf("Chime") }

    Settings(modifier = Modifier.fillMaxWidth()) {
        Section(header = { Text("Private chats") }) {
            SettingsToggleRow(
                state = notifications,
                onToggle = { notifications = it },
                title = { Text("Show notifications") },
            )
            SettingsToggleRow(
                state = preview,
                onToggle = { preview = it },
                title = { Text("Message preview") },
                enabled = notifications == ToggleState.On,
                nestingLevel = 1,
            )
            SettingsSelectRow(
                title = { Text("Sound") },
                options = listOf("Chime", "Note", "Pulse", "None"),
                selected = sound,
                onSelect = { sound = it },
                optionLabel = { it },
                enabled = notifications == ToggleState.On,
                nestingLevel = 1,
            )
        }
    }
}

/** A two-pane settings navigator: the open pane keeps its row selected. */
@Composable
internal fun SettingsNavigatorSample() {
    val panes = listOf(
        Triple("Account", ArecaIcons.User, ActionColor.Neutral),
        Triple("Privacy and security", ArecaIcons.Lock, ActionColor.Neutral),
        Triple("Notifications", ArecaIcons.Bell, ActionColor.Danger),
        Triple("Appearance", ArecaIcons.Palette, ActionColor.Warning),
    )
    var open by remember { mutableStateOf(panes[1].first) }

    Settings(modifier = Modifier.fillMaxWidth()) {
        Section(separated = false) {
            panes.forEach { (name, icon, color) ->
                SettingsRow(
                    title = { Text(name) },
                    leading = { SettingsRowIcon(icon, color = color) },
                    selected = name == open,
                    onClick = { open = name },
                )
            }
        }
    }
}

/**
 * Footer of the contact details section, ending in an inline link to the setting it describes.
 */
@Composable
private fun BirthdayFooter() {
    val linkStyles = textLinkStyles()
    Text(
        text = buildAnnotatedString {
            append("Only your contacts can see your birthday. ")
            appendTextLink(text = "Change ›", styles = linkStyles, onClick = {})
        },
    )
}

/**
 * One session on the devices screen: a device tile and three lines of detail.
 *
 * @param session Session to show.
 * @param current Whether it is this device, which cannot be terminated from here.
 */
@Composable
private fun SessionRow(session: SampleSession, current: Boolean) {
    SettingsRow(
        title = { Text(session.device, fontWeight = FontWeight.SemiBold) },
        subtitle = {
            Column {
                Text(session.client, color = ArecaTheme.colors.fgBase)
                Text(session.seen)
            }
        },
        leading = {
            FeaturedIcon(
                imageVector = session.icon,
                contentDescription = null,
                size = FeaturedIconSize.Small,
                style = FeaturedIconStyle.solid(
                    if (current) ActionColor.Success else ActionColor.Neutral,
                ),
                shape = CircleShape,
            )
        },
        onClick = if (current) null else ({}),
    )
}

/**
 * A pair of message bubbles previewing the text size being chosen.
 *
 * @param textSize Text size of the preview, in scale-independent pixels.
 */
@Composable
private fun ChatPreview(textSize: Int) {
    val bubble = RoundedCornerShape(ArecaTheme.radius.lg)
    val style = ArecaTheme.typography.bodyMedium.merge(fontSize = textSize.sp)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(ArecaTheme.radius.md))
            .background(ArecaTheme.colors.bgMuted)
            .padding(ArecaTheme.spacing.lg),
        verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
    ) {
        Text(
            text = "Do you know what time it is?",
            style = style,
            modifier = Modifier
                .clip(bubble)
                .background(ArecaTheme.colors.bgSurface)
                .padding(horizontal = ArecaTheme.spacing.lg, vertical = ArecaTheme.spacing.md),
        )
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
            Text(
                text = "It's morning in Tokyo",
                style = style,
                color = ArecaTheme.colors.fgOnFill,
                modifier = Modifier
                    .clip(bubble)
                    .background(ArecaTheme.colors.accentBase)
                    .padding(horizontal = ArecaTheme.spacing.lg, vertical = ArecaTheme.spacing.md),
            )
        }
    }
}

/** Trailing swatch of the name-color row: the color currently chosen. */
@Composable
private fun NameColorSwatch() {
    Swatch(size = SwatchSize.Small, contentDescription = "Current name color")
}

/**
 * A row of theme swatches, one of them chosen.
 *
 * @param selected Index of the chosen theme.
 * @param onSelect Called with the index of the theme pressed.
 */
@Composable
private fun ThemePicker(selected: Int, onSelect: (Int) -> Unit) {
    val colors = ArecaTheme.colors
    val themes: List<Color> = listOf(
        colors.accentBase,
        colors.fgSuccess,
        colors.fgInfo,
        colors.fgWarn,
        colors.fgDanger,
    )

    Row(horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg)) {
        themes.forEachIndexed { index, color ->
            Swatch(
                size = SwatchSize.Large,
                style = SwatchStyle.default(
                    bg = color,
                    border = if (index == selected) colors.fgBase else Color.Transparent,
                ),
                onClick = { onSelect(index) },
                contentDescription = "Theme ${index + 1}",
                modifier = Modifier.size(40.dp),
            )
        }
    }
}
