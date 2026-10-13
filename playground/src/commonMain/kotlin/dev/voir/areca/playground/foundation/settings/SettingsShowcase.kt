package dev.voir.areca.playground.foundation.settings

import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import dev.voir.areca.foundation.avatar.Avatar
import dev.voir.areca.foundation.avatar.AvatarSize
import dev.voir.areca.foundation.badge.Badge
import dev.voir.areca.foundation.badge.BadgeStyle
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.icon.FeaturedIconStyle
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.foundation.list.ListItemChevron
import dev.voir.areca.foundation.selection.ToggleState
import dev.voir.areca.foundation.settings.Section
import dev.voir.areca.foundation.settings.Settings
import dev.voir.areca.foundation.settings.SettingsBlock
import dev.voir.areca.foundation.settings.SettingsDefaults
import dev.voir.areca.foundation.settings.SettingsHeaderPlacement
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
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.Bell
import dev.voir.areca.icons.Globe
import dev.voir.areca.icons.Image
import dev.voir.areca.icons.Lock
import dev.voir.areca.icons.LogOut
import dev.voir.areca.icons.MoonAlt
import dev.voir.areca.icons.Translate
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.Anatomy
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme

/** What the playground row is. */
private enum class RowKind(
    /** Label of the option in the control panel. */
    val label: String,
) {
    /** A row that states a value and opens a screen to change it. */
    Navigation("Navigation"),

    /** A row that is a switch. */
    Toggle("Toggle"),

    /** A row that opens a screen, with a switch of its own. */
    SplitToggle("Split toggle"),

    /** A row whose value is picked from a menu. */
    Select("Select"),

    /** A row that performs an action. */
    Action("Action"),

    /** A row that destroys something. */
    Danger("Danger"),
}

/** What the playground row carries in its leading slot. */
private enum class LeadingKind(
    /** Label of the option in the control panel. */
    val label: String,
) {
    /** Nothing. */
    None("None"),

    /** A bare glyph, tinted by the row. */
    Glyph("Glyph"),

    /** A glyph on a colored tile. */
    Tile("Tile"),

    /** A glyph on a tile of the caller's own colours, outside the theme's color families. */
    CustomTile("Custom tile"),

    /** A person. */
    Avatar("Avatar"),
}

/** The status the playground row is forced into. */
private enum class StatusKind(
    /** Label of the option in the control panel. */
    val label: String,
) {
    /** Nothing in flight. */
    Idle("Idle"),

    /** A change is saving. */
    Loading("Loading"),

    /** The last change failed. */
    Error("Error"),
}

/** Showcase for [Settings]. */
@Composable
fun SettingsShowcase() = ShowcaseScaffold(
    title = "Settings",
    summary = "Grouped settings screens: sections on cards, rows that state, switch, pick, " +
            "edit and act, with text-aligned separators and per-row save status.",
    module = "foundation",
) {
    Playground(description = "Build one row and see it among its neighbours.") {
        val kind by enumControl(
            key = "row kind",
            default = RowKind.Navigation,
            options = RowKind.entries,
            optionLabel = { it.label },
        )
        val leadingKind by enumControl(
            key = "leading",
            default = LeadingKind.Tile,
            options = LeadingKind.entries,
            optionLabel = { it.label },
        )
        val withSubtitle by booleanControl("subtitle", default = true)
        val withBadge by booleanControl("badge", default = false)
        val withChevron by booleanControl(
            key = "chevron",
            default = false,
            description = "Navigation rows only: a chevron after the value.",
        )
        val statusKind by enumControl(
            key = "status",
            default = StatusKind.Idle,
            options = StatusKind.entries,
            optionLabel = { it.label },
            description = "Spinner in place of the value, or a message under the text.",
        )
        val placement by enumControl(
            key = "header",
            default = SettingsHeaderPlacement.Inside,
            options = SettingsHeaderPlacement.entries,
            optionLabel = { it.name },
        )
        val withFooter by booleanControl("footer", default = true)
        val separated by booleanControl("separators", default = true)
        val enabled by booleanControl("section enabled", default = true)
        val rtl by booleanControl("right to left", default = false)
        val brandAccents by booleanControl(
            key = "brand accents",
            default = false,
            description = "Opt in: header, pressable values and badge in the brand. Neutral otherwise.",
        )

        preview {
            var toggle by remember { mutableStateOf<ToggleState>(ToggleState.On) }
            var select by remember { mutableStateOf("Every 15 minutes") }
            val status = when (statusKind) {
                StatusKind.Idle -> SettingsRowStatus.Idle
                StatusKind.Loading -> SettingsRowStatus.Loading
                StatusKind.Error -> SettingsRowStatus.Error("Couldn't save. Check your connection.")
            }

            val title: @Composable () -> Unit = {
                Text(
                    when (kind) {
                        RowKind.Navigation -> "Language"
                        RowKind.Toggle -> "Background sync"
                        RowKind.SplitToggle -> "Auto-night mode"
                        RowKind.Select -> "Sync interval"
                        RowKind.Action -> "Change wallpaper"
                        RowKind.Danger -> "Delete account"
                    }
                )
            }
            val subtitle: (@Composable () -> Unit)? = if (withSubtitle) {
                {
                    Text(
                        when (kind) {
                            RowKind.Navigation -> "Used across the app"
                            RowKind.Toggle -> "Keep chats up to date in the background"
                            RowKind.SplitToggle -> "Adaptive dark"
                            RowKind.Select -> "How often to check for changes"
                            RowKind.Action -> "Pick a photo or a pattern"
                            RowKind.Danger -> "This cannot be undone"
                        }
                    )
                }
            } else {
                null
            }
            val badge: (@Composable () -> Unit)? = if (withBadge) {
                { Badge { Text("NEW") } }
            } else {
                null
            }
            val leading: (@Composable BoxScope.() -> Unit)? =
                when (leadingKind) {
                    LeadingKind.None -> null
                    LeadingKind.Glyph -> {
                        { Icon(ArecaIcons.Translate, contentDescription = null) }
                    }

                    LeadingKind.Tile -> {
                        { SettingsRowIcon(ArecaIcons.Translate) }
                    }

                    LeadingKind.CustomTile -> {
                        {
                            SettingsRowIcon(
                                imageVector = ArecaIcons.Translate,
                                style = FeaturedIconStyle(
                                    bg = ArecaTheme.baseColors.blue60,
                                    content = ArecaTheme.colors.fgOnFill,
                                ),
                            )
                        }
                    }

                    LeadingKind.Avatar -> {
                        { Avatar(size = AvatarSize.ExtraSmall, letters = "Alex Mercer") }
                    }
                }

            CompositionLocalProvider(
                LocalLayoutDirection provides if (rtl) LayoutDirection.Rtl else LayoutDirection.Ltr,
            ) {
                val styles = if (brandAccents) {
                    SettingsDefaults.styles(
                        section = SettingsDefaults.sectionStyle(header = ArecaTheme.colors.fgBrandText),
                        interactiveValue = ArecaTheme.colors.fgBrandText,
                        badge = BadgeStyle.Brand,
                    )
                } else {
                    SettingsDefaults.styles()
                }

                Settings(
                    modifier = Modifier.fillMaxWidth(),
                    styles = styles,
                    headerPlacement = placement,
                ) {
                    Section(
                        header = { Text("Preferences") },
                        footer = if (withFooter) {
                            { Text("Changes apply to every device signed in to this account.") }
                        } else {
                            null
                        },
                        enabled = enabled,
                        separated = separated,
                    ) {
                        NeighbourRow(title = "Notifications", icon = ArecaIcons.Bell, color = ActionColor.Danger)

                        when (kind) {
                            RowKind.Navigation -> SettingsRow(
                                title = title,
                                subtitle = subtitle,
                                badge = badge,
                                leading = leading,
                                value = { Text("English") },
                                trailing = if (withChevron) ({ ListItemChevron() }) else null,
                                status = status,
                                onClick = {},
                            )

                            RowKind.Toggle, RowKind.SplitToggle -> SettingsToggleRow(
                                state = toggle,
                                onToggle = { toggle = it },
                                title = title,
                                subtitle = subtitle,
                                badge = badge,
                                leading = leading,
                                status = status,
                                onClick = if (kind == RowKind.SplitToggle) ({}) else null,
                            )

                            RowKind.Select -> SettingsSelectRow(
                                title = title,
                                subtitle = subtitle,
                                badge = badge,
                                leading = leading,
                                options = listOf("Every 5 minutes", "Every 15 minutes", "Hourly", "Manually"),
                                selected = select,
                                onSelect = { select = it },
                                optionLabel = { it },
                                status = status,
                            )

                            RowKind.Action, RowKind.Danger -> SettingsRow(
                                title = title,
                                subtitle = subtitle,
                                badge = badge,
                                leading = leading,
                                tone = if (kind == RowKind.Action) {
                                    SettingsRowTone.Brand
                                } else {
                                    SettingsRowTone.Danger
                                },
                                status = status,
                                onClick = {},
                            )
                        }

                        NeighbourRow(title = "Privacy", icon = ArecaIcons.Lock, color = ActionColor.Neutral)
                    }
                }
            }
        }
    }

    Anatomy(description = "A settings screen is sections on cards; a row has six optional slots.") {
        slot(
            name = "Header",
            description = "Names the group. Inside the card by default, or above it.",
        ) {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section(header = { Text("Your info") }) {
                    SettingsRow(title = { Text("+381 64 123 45 67") }, subtitle = { Text("Mobile") })
                }
            }
        }
        slot(
            name = "Footer",
            description = "Below the card: what the group means, or what changing it costs.",
        ) {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section(footer = { Text("Only your contacts can see your birthday.") }) {
                    SettingsRow(title = { Text("Oct 04") }, subtitle = { Text("Birthday") })
                }
            }
        }
        slot(
            name = "Row slots",
            description = "leading, title with badge, subtitle, value, trailing and bottom.",
        ) {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    SettingsRow(
                        title = { Text("Display name") },
                        badge = { Badge { Text("NEW") } },
                        subtitle = { Text("Shown to your team") },
                        leading = { SettingsRowIcon(ArecaIcons.Globe) },
                        value = { Text("Alex") },
                        trailing = { ListItemChevron() },
                        bottom = { Text("Two to thirty-two characters.") },
                        onClick = {},
                    )
                }
            }
        }
        slot(
            name = "Separators",
            description = "Drawn by the section, starting at the text of the rows on either side.",
        ) {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    SettingsRow(title = { Text("No leading slot") }, onClick = {})
                    SettingsRow(
                        title = { Text("Glyph") },
                        leading = { Icon(ArecaIcons.Image, contentDescription = null) },
                        onClick = {},
                    )
                    SettingsRow(
                        title = { Text("Tile") },
                        leading = { SettingsRowIcon(ArecaIcons.Bell, color = ActionColor.Danger) },
                        onClick = {},
                    )
                }
            }
        }
    }

    Variants(
        title = "Row kinds",
        description = "One row for each question a settings screen asks.",
        layout = VariantLayout.Vertical,
    ) {
        variant(name = "SettingsRow", detail = "States a value; a pressable value brightens when reached for") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    SettingsRow(title = { Text("Version") }, value = { Text("4.2.0") })
                    SettingsRow(title = { Text("Language") }, value = { Text("English") }, onClick = {})
                    SettingsRow(
                        title = { Text("Storage") },
                        value = { Text("2.4 GB") },
                        trailing = { ListItemChevron() },
                        onClick = {},
                    )
                }
            }
        }
        variant(name = "SettingsToggleRow", detail = "Whole row is the switch; checked for a plain yes or no") {
            var inAppBrowser by remember { mutableStateOf(true) }
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    SettingsToggleRow(
                        checked = inAppBrowser,
                        onCheckedChange = { inAppBrowser = it },
                        title = { Text("In-app browser") },
                        subtitle = { Text("Open external links within the app") },
                        leading = { Icon(ArecaIcons.Globe, contentDescription = null) },
                    )
                }
            }
        }
        variant(name = "SettingsToggleRow + onClick", detail = "Split, on the ToggleState overload") {
            var state by remember { mutableStateOf<ToggleState>(ToggleState.On) }
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    SettingsToggleRow(
                        state = state,
                        onToggle = { state = it },
                        title = { Text("Auto-night mode") },
                        subtitle = { Text("Adaptive dark") },
                        leading = { Icon(ArecaIcons.MoonAlt, contentDescription = null) },
                        onClick = {},
                    )
                }
            }
        }
        variant(name = "SettingsSelectRow", detail = "Picks one option from a menu off the value") {
            var units by remember { mutableStateOf("Kilometers") }
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    SettingsSelectRow(
                        title = { Text("Distance units") },
                        options = listOf("Kilometers", "Miles"),
                        selected = units,
                        onSelect = { units = it },
                        optionLabel = { it },
                    )
                }
            }
        }
        variant(name = "SettingsOptionRow", detail = "A choice laid out as a section") {
            var theme by remember { mutableStateOf("System") }
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section(header = { Text("Theme") }) {
                    listOf("Light", "Dark", "System").forEach { option ->
                        SettingsOptionRow(
                            selected = option == theme,
                            onClick = { theme = option },
                            title = { Text(option) },
                        )
                    }
                }
            }
        }
        variant(name = "SettingsTextFieldRow", detail = "Typed into in place; counts down with maxLength") {
            var name by remember { mutableStateOf("") }
            var bio by remember { mutableStateOf("Building calm software.") }
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section(header = { Text("Profile") }) {
                    SettingsTextFieldRow(value = name, onValueChange = { name = it }, placeholder = "Display name")
                    SettingsTextFieldRow(
                        value = bio,
                        onValueChange = { bio = it },
                        placeholder = "Bio",
                        maxLength = 70,
                        singleLine = false,
                    )
                }
            }
        }
        variant(name = "Tones", detail = "SettingsRowTone.Brand and .Danger: the only colored copy by default") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    SettingsRow(
                        title = { Text("Change wallpaper") },
                        leading = { Icon(ArecaIcons.Image, contentDescription = null) },
                        tone = SettingsRowTone.Brand,
                        onClick = {},
                    )
                    SettingsRow(
                        title = { Text("Log out") },
                        leading = { Icon(ArecaIcons.LogOut, contentDescription = null) },
                        tone = SettingsRowTone.Danger,
                        onClick = {},
                    )
                }
            }
        }
        variant(name = "SettingsRowIcon(style)", detail = "A tile in the caller's own colours") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    SettingsRow(
                        title = { Text("Language") },
                        leading = {
                            SettingsRowIcon(
                                imageVector = ArecaIcons.Translate,
                                style = FeaturedIconStyle(
                                    bg = ArecaTheme.baseColors.blue60,
                                    content = ArecaTheme.colors.fgOnFill,
                                ),
                            )
                        },
                        value = { Text("English") },
                        onClick = {},
                    )
                    SettingsRow(
                        title = { Text("Appearance") },
                        leading = {
                            SettingsRowIcon(
                                imageVector = ArecaIcons.MoonAlt,
                                style = FeaturedIconStyle(
                                    bg = ArecaTheme.baseColors.brand80,
                                    content = ArecaTheme.colors.fgOnFill,
                                ),
                            )
                        },
                        value = { Text("System") },
                        onClick = {},
                    )
                }
            }
        }
        variant(name = "SettingsBlock", detail = "Free-form content inset like a row") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section(header = { Text("Storage") }) {
                    SettingsBlock {
                        Text("2.4 GB of 16 GB used")
                        Text(
                            text = "Media is cleared after a month unless it is saved.",
                            style = ArecaTheme.typography.bodySmall,
                            color = ArecaTheme.colors.fgMuted,
                        )
                    }
                    SettingsRow(title = { Text("Clear cache") }, tone = SettingsRowTone.Brand, onClick = {})
                }
            }
        }
        variant(name = "SettingsRowSkeleton", detail = "Holds the height of rows that have not loaded") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    SettingsRowSkeleton(showLeading = true, showTrailing = true)
                    SettingsRowSkeleton(showLeading = true, showSubtitle = true)
                }
            }
        }
    }

    States(
        description = "Hover, press and focus are forced here; point at the playground row to see " +
                "them arrive and settle. Focus shares the hover treatment.",
        layout = VariantLayout.Vertical,
    ) {
        variant(name = "Rest", detail = "Neutral throughout") { StateRow() }
        variant(name = "Hovered", detail = "Tinted row, chevron leans in") {
            StateRow(forced = listOf(HoverInteraction.Enter()))
        }
        variant(name = "Pressed", detail = "Deeper tint, landed in 90ms") {
            StateRow(forced = listOf(HoverInteraction.Enter(), PressInteraction.Press(Offset.Zero)))
        }
        variant(name = "Focused", detail = "Wears the hover treatment, reached by keyboard") {
            StateRow(forced = listOf(FocusInteraction.Focus()))
        }
        variant(name = "Selected", detail = "Filled, with a rail on the leading edge") { StateRow(selected = true) }
        variant(name = "Disabled", detail = "Shaded copy and tile, no hit area") { StateRow(enabled = false) }
        variant(name = "Loading", detail = "Spinner in place of the value; presses ignored") {
            StateRow(status = SettingsRowStatus.Loading)
        }
        variant(name = "Error", detail = "Message under the text; the row accepts input again") {
            StateRow(status = SettingsRowStatus.Error("Couldn't save. Check your connection."))
        }
    }

    Recipes {
        recipe(
            name = "Account",
            description = "Fields typed into in place, a bio with a counter, contact details on " +
                    "colored tiles, a footer with an inline link, and a destructive row alone at the end.",
        ) { AccountSettingsSample() }

        recipe(
            name = "Chat settings",
            description = "A live preview in a block, brand actions, a split auto-night switch, " +
                    "switches with long explanations and values picked from a menu.",
        ) { ChatSettingsSample() }

        recipe(
            name = "Devices",
            description = "A promotional block with its own button, this device beside the action " +
                    "that signs out the rest, the other sessions, and a timeout picked from a menu.",
        ) { DevicesSettingsSample() }

        recipe(
            name = "Saving to a server",
            description = "Each change shows a spinner until it settles. Every other switch of " +
                    "two-step verification fails, to show the error and the retry.",
        ) { PrivacySettingsSample() }

        recipe(
            name = "Choice as a section",
            description = "When the options need room — or a recommendation — lay them out as rows.",
        ) { AutoLockSample() }

        recipe(
            name = "Dependent settings",
            description = "Rows that only mean something while the switch above is on: indented, " +
                    "and disabled with it. Separators follow the indent.",
        ) { DependentSettingsSample() }

        recipe(
            name = "Two-pane navigator",
            description = "On a wide screen the list is navigation: the open pane stays selected.",
        ) { SettingsNavigatorSample() }

        recipe(
            name = "Loading",
            description = "Skeleton rows hold the section's height until its content arrives.",
        ) { LoadingSettingsSample() }
    }

    EdgeCases {
        case(
            name = "Long title, value and badge",
            expectation = "The title wraps; the badge and the value keep their room.",
        ) {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    SettingsRow(
                        title = { Text("Automatic background synchronisation of media over cellular") },
                        badge = { Badge { Text("BETA") } },
                        value = { Text("Wi-Fi only") },
                        trailing = { ListItemChevron() },
                        onClick = {},
                    )
                }
            }
        }
        case(
            name = "Mixed leading slots",
            expectation = "Each separator starts at the deeper text of the two rows it separates.",
        ) {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    SettingsRow(title = { Text("Plain row") }, onClick = {})
                    SettingsRow(
                        title = { Text("Row with a tile") },
                        leading = { SettingsRowIcon(ArecaIcons.Bell, color = ActionColor.Danger) },
                        onClick = {},
                    )
                    SettingsRow(title = { Text("Nested row") }, nestingLevel = 1, onClick = {})
                    SettingsBlock { Text("A block reports no text start and falls back to the inset.") }
                    SettingsRow(title = { Text("Plain row") }, onClick = {})
                }
            }
        }
        case(
            name = "Disabled section",
            expectation = "Every row refuses input; tiles, header and footer shade with them.",
        ) {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section(
                    header = { Text("Billing") },
                    footer = { Text("Only workspace owners can change billing.") },
                    enabled = false,
                ) {
                    SettingsRow(
                        title = { Text("Plan") },
                        leading = { SettingsRowIcon(ArecaIcons.Lock, color = ActionColor.Warning) },
                        value = { Text("Team") },
                        trailing = { ListItemChevron() },
                        onClick = {},
                    )
                    SettingsToggleRow(state = ToggleState.On, onToggle = {}, title = { Text("Annual billing") })
                    SettingsTextFieldRow(value = "Mercer Studio", onValueChange = {}, placeholder = "Company")
                }
            }
        }
        case(
            name = "Single row, no header",
            expectation = "No separator, and the pressed row keeps the card's corners.",
        ) {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    SettingsRow(
                        title = { Text("Log out") },
                        tone = SettingsRowTone.Danger,
                        interactionSource = forcedInteractions(
                            listOf(HoverInteraction.Enter(), PressInteraction.Press(Offset.Zero)),
                        ),
                        onClick = {},
                    )
                }
            }
        }
        case(
            name = "Text field at its limit",
            expectation = "The counter reaches zero in the warning color and input stops there.",
        ) {
            var text by remember { mutableStateOf("Exactly twenty chars") }
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    SettingsTextFieldRow(value = text, onValueChange = { text = it }, placeholder = "Label", maxLength = 20)
                }
            }
        }
        case(
            name = "Right to left",
            expectation = "Slots mirror, and separators start at the text on the right.",
        ) {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                Settings(modifier = Modifier.fillMaxWidth()) {
                    Section(header = { Text("Language") }) {
                        SettingsRow(
                            title = { Text("App language") },
                            leading = { SettingsRowIcon(ArecaIcons.Translate) },
                            value = { Text("English") },
                            trailing = { ListItemChevron() },
                            onClick = {},
                        )
                        SettingsToggleRow(
                            state = ToggleState.On,
                            onToggle = {},
                            title = { Text("Translate messages") },
                            leading = { SettingsRowIcon(ArecaIcons.Globe, color = ActionColor.Success) },
                        )
                    }
                }
            }
        }
    }
}

/**
 * A plain neighbour of the playground row, so separators and alignment are seen in context.
 *
 * @param title Title of the row.
 * @param icon Glyph of its tile.
 * @param color Color of its tile.
 */
@Composable
private fun NeighbourRow(
    title: String,
    icon: ImageVector,
    color: ActionColor,
) {
    SettingsRow(
        title = { Text(title) },
        leading = { SettingsRowIcon(icon, color = color) },
        trailing = { ListItemChevron() },
        onClick = {},
    )
}

/**
 * One row in a one-row section, used by the state cells.
 *
 * @param enabled Whether the row accepts input.
 * @param selected Whether the row is the selected one.
 * @param status Status the row is in.
 * @param forced Interactions emitted into the row so a state holds still for inspection.
 */
@Composable
private fun StateRow(
    enabled: Boolean = true,
    selected: Boolean = false,
    status: SettingsRowStatus = SettingsRowStatus.Idle,
    forced: List<Interaction> = emptyList(),
) {
    Settings(modifier = Modifier.fillMaxWidth()) {
        Section {
            SettingsRow(
                title = { Text("Appearance") },
                subtitle = { Text("Theme, colors and density") },
                leading = { SettingsRowIcon(ArecaIcons.MoonAlt) },
                value = { Text("System") },
                trailing = { ListItemChevron() },
                enabled = enabled,
                selected = selected,
                status = status,
                interactionSource = forcedInteractions(forced),
                onClick = {},
            )
        }
    }
}

/**
 * An interaction source holding the given interactions open.
 *
 * Lets a documentation cell show hover, press or focus without a pointer, which is the only way to
 * put those states side by side on a page.
 *
 * @param interactions Interactions emitted once the source is composed.
 * @return Source to hand to the component under inspection.
 */
@Composable
private fun forcedInteractions(interactions: List<Interaction>): MutableInteractionSource {
    val source = remember { MutableInteractionSource() }

    LaunchedEffect(source, interactions.size) {
        // The row subscribes to the source in its own effect; emitting before that drops the
        // interaction, so hand the frame over first.
        withFrameNanos { }
        interactions.forEach { source.emit(it) }
    }

    return source
}
