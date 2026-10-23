package dev.voir.areca.playground.foundation.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.badge.BadgeOption
import dev.voir.areca.foundation.badge.BadgeSelectionMode
import dev.voir.areca.foundation.badge.BadgeSize
import dev.voir.areca.foundation.badge.BadgeStyle
import dev.voir.areca.foundation.badge.FlowBadgeSelect
import dev.voir.areca.foundation.badge.IndicatorDot
import dev.voir.areca.foundation.list.CheckboxListItem
import dev.voir.areca.foundation.list.CheckmarkListItem
import dev.voir.areca.foundation.list.ExpandableListItem
import dev.voir.areca.foundation.list.ExpandableListItemContainer
import dev.voir.areca.foundation.list.ListItem
import dev.voir.areca.foundation.list.ListItemChevron
import dev.voir.areca.foundation.list.ListItemColor
import dev.voir.areca.foundation.list.ListItemColumn
import dev.voir.areca.foundation.list.ListItemSize
import dev.voir.areca.foundation.list.ListItemSkeleton
import dev.voir.areca.foundation.list.ListItemStyle
import dev.voir.areca.foundation.list.ListSize
import dev.voir.areca.foundation.list.ListStyle
import dev.voir.areca.foundation.list.NavigationListItem
import dev.voir.areca.foundation.list.RadioListItem
import dev.voir.areca.foundation.list.ToggleListItem
import dev.voir.areca.foundation.selection.ToggleState
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.Anatomy
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme

/** Showcase for [ListItem] and the rows built on it. */
@Composable
fun ListItemShowcase() = ShowcaseScaffold(
    title = "List item",
    summary = "A row with a text block and two slots, in a chosen appearance and tone.",
    module = "foundation",
) {
    Playground {
        val title by textControl("title", default = "Notifications")
        val subtitle by textControl("subtitle", default = "Email and push")
        val overline by textControl("overline", default = "")
        val value by textControl("value", default = "On")
        val enabled by booleanControl("enabled", default = true)
        val selected by booleanControl(
            key = "selected",
            default = false,
            description = "Fills the row and grows a rail along its leading edge.",
        )
        val clickable by booleanControl("clickable", default = true)
        val withLeading by booleanControl("leading", default = true, label = "leading slot")

        preview {
            ListItem(
                title = { Text(title) },
                subtitle = subtitle.takeIf { it.isNotEmpty() }?.let { { Text(it) } },
                overline = overline.takeIf { it.isNotEmpty() }?.let { { Text(it) } },
                value = value.takeIf { it.isNotEmpty() }?.let { { Text(it) } },
                enabled = enabled,
                selected = selected,
                onClick = if (clickable) ({}) else null,
                leading = if (withLeading) ({ Dot("N") }) else null,
                trailing = { ListItemChevron() },
            )
        }
    }

    Anatomy(
        description = "A text block between two slots. Only the title — or a content slot that " +
            "replaces the whole block — is required.",
    ) {
        slot(
            name = "leading",
            description = "Identity or status — an avatar, an icon, a selection control.",
        ) {
            ListItem(title = { Text("Leading slot") }, leading = { Dot("A") })
        }
        slot(
            name = "overline / title / subtitle",
            description = "The text block. Each line takes its style and color from the row.",
        ) {
            ListItem(
                overline = { Text("BILLING") },
                title = { Text("Invoice #1042") },
                subtitle = { Text("Paid on 4 March") },
            )
        }
        slot(
            name = "value",
            description = "The row's current setting, stated inline before the trailing slot.",
        ) {
            ListItem(
                title = { Text("Language") },
                value = { Text("English") },
                trailing = { ListItemChevron() },
                onClick = {},
            )
        }
        slot(
            name = "trailing",
            description = "The affordance — a chevron, a control, a badge.",
        ) {
            ListItem(title = { Text("Trailing slot") }, trailing = { ListItemChevron() }, onClick = {})
        }
        slot(
            name = "bottom",
            description = "Content below the row line, inside its hit area.",
        ) {
            ListItem(
                title = { Text("Storage") },
                onClick = {},
                bottom = {
                    Text("8.2 GB of 10 GB used", variant = TextVariant.BodySmall, color = TextColor.Subtle)
                },
            )
        }
    }

    States(layout = VariantLayout.Vertical) {
        variant("Resting", detail = "clickable") {
            ListItem(title = { Text("Resting") }, subtitle = { Text("Hover or focus it") }, onClick = {})
        }
        variant("Selected") {
            ListItem(
                title = { Text("Selected") },
                subtitle = { Text("Filled, with a rail that grew out of the centre") },
                selected = true,
                onClick = {},
            )
        }
        variant("Selected, not highlighted", detail = "highlightSelected = false") {
            ListItem(
                title = { Text("Selected") },
                subtitle = { Text("Reported to accessibility, but drawn as an ordinary row") },
                selected = true,
                highlightSelected = false,
                onClick = {},
            )
        }
        variant("Disabled") {
            ListItem(title = { Text("Disabled") }, subtitle = { Text("Refuses input") }, enabled = false, onClick = {})
        }
        variant("Static", detail = "onClick = null") {
            ListItem(title = { Text("Static") }, subtitle = { Text("No hover, no cursor change") })
        }
    }

    Variants(
        title = "Appearances",
        description = "How much surface the row carries. Plain is the default: square and " +
            "transparent, because the group it sits in owns the background and the corners.",
        layout = VariantLayout.Vertical,
    ) {
        variant("Plain", detail = "rows inside a card or a group") {
            ListItem(title = { Text("Plain") }, style = ListItemStyle.plain(), onClick = {})
        }
        variant("Surface", detail = "a row standing on the canvas") {
            ListItem(title = { Text("Surface") }, style = ListItemStyle.surface(), onClick = {})
        }
        variant("Outlined", detail = "a row that reads as its own object") {
            ListItem(title = { Text("Outlined") }, style = ListItemStyle.outlined(), onClick = {})
        }
        variant("Soft", detail = "a row called out among its neighbours") {
            ListItem(
                title = { Text("Soft") },
                subtitle = { Text("Recommended plan") },
                style = ListItemStyle.soft(ListItemColor.Brand),
                onClick = {},
            )
        }
        variant("Ghost", detail = "menus and popovers") {
            ListItem(
                title = { Text("Ghost") },
                style = ListItemStyle.ghost(),
                size = ListItemSize.Compact,
                onClick = {},
            )
        }
    }

    Variants(
        title = "Tones",
        description = "What the row means. Every tone works with every appearance, which is why " +
            "a row never needs a background passed by hand.",
        layout = VariantLayout.Vertical,
    ) {
        ListItemColor.entries.forEach { color ->
            variant(color.name) {
                ListItem(
                    title = { Text("${color.name} row") },
                    subtitle = { Text("Hover it: the tint carries the tone") },
                    leading = { IndicatorDot() },
                    style = ListItemStyle.plain(color),
                    onClick = {},
                )
            }
        }
    }

    Variants(
        title = "Densities",
        description = "One size rescales the row as a whole — height, text, icons and chevrons.",
        layout = VariantLayout.Vertical,
    ) {
        variant("Compact", detail = "menus, pickers, secondary lists") {
            ListItem(
                title = { Text("Compact") },
                subtitle = { Text("32dp minimum") },
                size = ListItemSize.Compact,
                trailing = { ListItemChevron() },
                onClick = {},
            )
        }
        variant("Medium", detail = "the default") {
            ListItem(
                title = { Text("Medium") },
                subtitle = { Text("44dp minimum") },
                size = ListItemSize.Medium,
                trailing = { ListItemChevron() },
                onClick = {},
            )
        }
        variant("Comfortable", detail = "contacts, conversations, media") {
            ListItem(
                title = { Text("Comfortable") },
                subtitle = { Text("56dp minimum") },
                size = ListItemSize.Comfortable,
                trailing = { ListItemChevron() },
                onClick = {},
            )
        }
    }

    Variants(
        title = "Row kinds",
        description = "Each of these is ListItem with its trailing slot and its click behaviour " +
            "already decided, so the whole row is the target rather than the small control on it.",
        layout = VariantLayout.Vertical,
    ) {
        variant("NavigationListItem", detail = "the chevron leans toward a hovered row") {
            NavigationListItem(
                title = { Text("Privacy") },
                value = { Text("Everyone") },
                onClick = {},
            )
        }
        variant("CheckboxListItem", detail = "multi-select") {
            var checked by remember { mutableStateOf(true) }
            CheckboxListItem(
                checked = checked,
                onCheckedChange = { checked = it },
                title = { Text("Weekly digest") },
                subtitle = { Text("A summary every Monday") },
            )
        }
        variant("RadioListItem", detail = "single choice") {
            var choice by remember { mutableStateOf("standard") }
            Column(modifier = Modifier.selectableGroup()) {
                RadioListItem(
                    selected = choice == "standard",
                    onSelect = { choice = "standard" },
                    title = { Text("Standard delivery") },
                )
                RadioListItem(
                    selected = choice == "express",
                    onSelect = { choice = "express" },
                    title = { Text("Express delivery") },
                )
            }
        }
        variant("CheckmarkListItem", detail = "select sheets and menus") {
            var chosen by remember { mutableStateOf("Berlin") }
            Column {
                listOf("Berlin", "Lisbon", "Tokyo").forEach { city ->
                    CheckmarkListItem(
                        selected = chosen == city,
                        onClick = { chosen = city },
                        title = { Text(city) },
                    )
                }
            }
        }
        variant("CheckmarkListItem + checkmarkColor", detail = "checkmarkColor = fgBrand") {
            var chosen by remember { mutableStateOf("Euro") }
            Column {
                listOf("Euro", "US dollar", "Japanese yen").forEach { currency ->
                    CheckmarkListItem(
                        selected = chosen == currency,
                        onClick = { chosen = currency },
                        title = { Text(currency) },
                        checkmarkColor = ArecaTheme.colors.fgBrand,
                    )
                }
            }
        }
        variant("ToggleListItem", detail = "the whole row flips the switch") {
            var state by remember { mutableStateOf<ToggleState>(ToggleState.On) }
            ToggleListItem(
                state = state,
                onToggle = { state = it },
                title = { Text("Push notifications") },
                subtitle = { Text("Alerts on this device") },
            )
        }
        variant("ExpandableListItem", detail = "reveals rows underneath it") {
            var expanded by remember { mutableStateOf(true) }
            ExpandableListItem(
                expanded = expanded,
                onExpandedChange = { expanded = it },
                title = { Text("Advanced") },
            ) {
                ListItem(title = { Text("Developer mode") }, nestingLevel = 1, onClick = {})
                ListItem(title = { Text("Reset caches") }, nestingLevel = 1, onClick = {})
            }
        }
        variant("ExpandableListItem + Soft", detail = "no chevron; the open group shares one soft block") {
            var expanded by remember { mutableStateOf(true) }
            ExpandableListItem(
                expanded = expanded,
                onExpandedChange = { expanded = it },
                title = { Text("Food and dining") },
                subtitle = { Text("€612 · 26% of spending") },
                showChevron = false,
                container = ExpandableListItemContainer.Soft,
            ) {
                ListItem(title = { Text("Groceries") }, value = { Text("€318") }, nestingLevel = 1)
                ListItem(title = { Text("Restaurants") }, value = { Text("€190") }, nestingLevel = 1)
            }
        }
        variant("ListItemSkeleton", detail = "the same row, still loading") {
            Column {
                ListItemSkeleton(showLeading = true, titleWidthFraction = 0.5f)
                ListItemSkeleton(showLeading = true, titleWidthFraction = 0.35f)
            }
        }
        variant("ListItemSkeleton + valueWidth", detail = "rows that state a figure") {
            Column {
                ListItemSkeleton(showLeading = true, showTrailing = true, valueWidth = 56.dp)
                ListItemSkeleton(
                    showLeading = true,
                    showTrailing = true,
                    valueWidth = 40.dp,
                    titleWidthFraction = 0.35f,
                )
            }
        }
    }

    Recipes {
        recipe(
            name = "Settings group",
            description = "A card that owns the surface, the corners and the hairlines. The rows " +
                "stay plain, which is what lets the first and last one take its corners.",
        ) {
            var notifications by remember { mutableStateOf<ToggleState>(ToggleState.On) }

            ListItemColumn(
                listStyle = ListStyle.default(
                    bg = ArecaTheme.colors.bgSurface,
                    border = ArecaTheme.colors.borderBase,
                ),
                listSize = ListSize.default(
                    shape = ArecaTheme.shapes.md,
                    borderWidth = 1.dp,
                    separatorInset = ArecaTheme.spacing.xl,
                ),
            ) {
                ToggleListItem(
                    state = notifications,
                    onToggle = { notifications = it },
                    title = { Text("Push notifications") },
                )
                NavigationListItem(
                    title = { Text("Email") },
                    value = { Text("Daily") },
                    onClick = {},
                )
                ListItem(
                    title = { Text("Delete account") },
                    style = ListItemStyle.plain(ListItemColor.Danger),
                    onClick = {},
                )
            }
        }
        recipe(
            name = "Inbox",
            description = "Overline for the timestamp, an unread dot in the leading slot, and a " +
                "soft row for the one that needs attention.",
        ) {
            ListItemColumn(listSize = ListSize.default(separatorInset = ArecaTheme.spacing.xl)) {
                ListItem(
                    overline = { Text("09:41") },
                    title = { Text("Deploy finished") },
                    subtitle = { Text("web-frontend · production") },
                    size = ListItemSize.Comfortable,
                    leading = { IndicatorDot(color = ArecaTheme.colors.accentBase) },
                    onClick = {},
                )
                ListItem(
                    overline = { Text("08:12") },
                    title = { Text("Build failed") },
                    subtitle = { Text("api-gateway · 3 tests failing") },
                    size = ListItemSize.Comfortable,
                    style = ListItemStyle.soft(ListItemColor.Danger),
                    leading = { IndicatorDot() },
                    onClick = {},
                )
            }
        }
        recipe(
            name = "Multi-select",
            description = "Checked rows stay neutral so a long list does not become a wall of " +
                "colour; the checkbox column already says which ones are chosen.",
        ) {
            var selected by remember { mutableStateOf(setOf("Design")) }

            ListItemColumn {
                listOf("Design", "Engineering", "Marketing").forEach { team ->
                    CheckboxListItem(
                        checked = team in selected,
                        onCheckedChange = { checked ->
                            selected = if (checked) selected + team else selected - team
                        },
                        title = { Text(team) },
                        subtitle = { Text("${team.length} members") },
                    )
                }
            }
        }
        recipe(
            name = "Category with subcategories",
            description = "A tri-state row: ticked when every subcategory is in, a dash when some " +
                "are. The subcategories toggle as chips in the row's bottom slot.",
        ) {
            val subcategories = listOf("Groceries", "Restaurants", "Coffee", "Delivery")
            var chosen by remember { mutableStateOf(listOf("Groceries", "Coffee")) }

            ListItemColumn {
                CheckboxListItem(
                    // Indeterminate is a checked box showing a dash, so some-but-not-all is checked.
                    checked = chosen.isNotEmpty(),
                    indeterminate = chosen.isNotEmpty() && chosen.size < subcategories.size,
                    onCheckedChange = {
                        chosen = if (chosen.size == subcategories.size) emptyList() else subcategories
                    },
                    title = { Text("Food and dining") },
                    subtitle = { Text("€612 in October") },
                    bottom = {
                        FlowBadgeSelect(
                            options = subcategories.map { BadgeOption(label = { Text(it) }, value = it) },
                            onSelect = { option ->
                                chosen = if (option.value in chosen) chosen - option.value else chosen + option.value
                            },
                            selected = chosen,
                            selectionMode = BadgeSelectionMode.Multiple,
                            horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.sm),
                            verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.sm),
                            style = BadgeStyle.FilledChip,
                            size = BadgeSize.Chip,
                        )
                    },
                )
            }
        }
        recipe(
            name = "Menu",
            description = "Ghost rows at compact density, marked rather than filled.",
        ) {
            var sort by remember { mutableStateOf("Newest") }

            ListItemColumn(
                listStyle = ListStyle.default(bg = ArecaTheme.colors.bgSurface),
                listSize = ListSize.default(shape = ArecaTheme.shapes.md),
                separated = false,
            ) {
                listOf("Newest", "Oldest", "Name").forEach { option ->
                    CheckmarkListItem(
                        selected = sort == option,
                        onClick = { sort = option },
                        title = { Text(option) },
                        style = ListItemStyle.ghost(),
                        size = ListItemSize.Compact,
                    )
                }
            }
        }
    }

    EdgeCases {
        case(
            name = "Long content",
            expectation = "The text block wraps; the slots stay vertically centred.",
        ) {
            ListItem(
                title = { Text("Retention policy for archived workspaces") },
                subtitle = {
                    Text(
                        "Deleted items are recoverable for thirty days, after which they are " +
                            "permanently removed and cannot be restored by support.",
                    )
                },
                leading = { Dot("R") },
                trailing = { ListItemChevron() },
                onClick = {},
            )
        }
        case(
            name = "Title only",
            expectation = "The row shrinks to one line rather than reserving a second.",
        ) {
            ListItem(title = { Text("Single line") }, trailing = { ListItemChevron() }, onClick = {})
        }
        case(
            name = "Nested rows",
            expectation = "Each level adds one step of indentation to the content, not the row.",
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                ListItem(title = { Text("Workspace") }, onClick = {})
                ListItem(title = { Text("Members") }, nestingLevel = 1, onClick = {})
                ListItem(title = { Text("Invitations") }, nestingLevel = 2, onClick = {})
            }
        }
        case(
            name = "Content slot",
            expectation = "Replaces the whole text block; the row still owns colour and state.",
        ) {
            ListItem(
                onClick = {},
                trailing = { ListItemChevron() },
                content = {
                    Text("Whatever you like here", variant = TextVariant.BodyMedium)
                },
            )
        }
    }
}

/**
 * A round monogram, standing in for an avatar in the leading slot.
 *
 * @param label Letter shown inside the circle.
 */
@Composable
private fun Dot(label: String) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(ArecaTheme.colors.bgBrandMuted),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = label, variant = TextVariant.LabelSmall, color = TextColor.Brand)
    }
}
