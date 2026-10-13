package dev.voir.areca.playground.foundation.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.navigation.NavData
import dev.voir.areca.foundation.navigation.NavNode
import dev.voir.areca.foundation.badge.Badge
import dev.voir.areca.foundation.badge.BadgeSize
import dev.voir.areca.foundation.badge.BadgeStyle
import dev.voir.areca.foundation.icon.Chevron
import dev.voir.areca.foundation.icon.ChevronDirection
import dev.voir.areca.foundation.navigation.NavItem
import dev.voir.areca.foundation.navigation.NavList
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.Anatomy
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme

/**
 * Showcase for [NavList] and [NavItem].
 *
 * One page: the list is built from the item, and a change to either shows up in the other.
 */
@Composable
fun NavListShowcase() = ShowcaseScaffold(
    title = "Nav list",
    summary = "Sidebar navigation: a collapsible tree of sections built from selectable rows.",
    module = "foundation",
) {
    Playground(description = "Select rows and collapse sections; selection is owned by the caller.") {
        val expandable by booleanControl(
            key = "expandable",
            default = true,
            description = "Whether section headers can collapse their children.",
        )
        val initiallyExpanded by booleanControl("initiallyExpanded", default = true)
        val disableReports by booleanControl(
            key = "disable Reports",
            default = false,
            description = "A disabled row cannot take selection.",
        )

        preview {
            var selected by remember { mutableStateOf<String?>("overview") }

            NavList(
                nodes = sampleNodes(
                    expandable = expandable,
                    initiallyExpanded = initiallyExpanded,
                    disabled = if (disableReports) setOf("reports") else emptySet(),
                ),
                selectedId = selected,
                onItemClick = { selected = it.id },
                modifier = Modifier.width(260.dp),
            )
        }
    }

    Anatomy(description = "A list is made of two node kinds and the row they both render.") {
        slot(
            name = "NavNode.Item",
            description = "A single destination with no children.",
        ) {
            var selected by remember { mutableStateOf<String?>("inbox") }
            NavList(
                nodes = listOf(
                    NavNode.Item(NavData("inbox", "Inbox")),
                    NavNode.Item(NavData("drafts", "Drafts")),
                ),
                selectedId = selected,
                onItemClick = { selected = it.id },
                modifier = Modifier.width(260.dp),
            )
        }
        slot(
            name = "NavNode.Section",
            description = "A header with children that can collapse.",
        ) {
            var selected by remember { mutableStateOf<String?>("overview") }
            NavList(
                nodes = listOf(sampleSection(expandable = true, initiallyExpanded = true)),
                selectedId = selected,
                onItemClick = { selected = it.id },
                modifier = Modifier.width(260.dp),
            )
        }
        slot(
            name = "NavItem",
            description = "The row itself, usable outside a list with its own slots.",
        ) {
            Column(
                modifier = Modifier.width(260.dp),
                verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xs),
            ) {
                NavItem(selected = true, onClick = {}) { Text("Selected row") }
                NavItem(onClick = {}) { Text("Resting row") }
            }
        }
    }

    States(layout = VariantLayout.Grid) {
        variant("Resting") {
            NavItem(modifier = Modifier.width(220.dp), onClick = {}) { Text("Overview") }
        }
        variant("Selected") {
            NavItem(modifier = Modifier.width(220.dp), selected = true, onClick = {}) {
                Text("Overview")
            }
        }
        variant("Disabled") {
            NavItem(modifier = Modifier.width(220.dp), enabled = false, onClick = {}) {
                Text("Overview")
            }
        }
        variant("Static", detail = "onClick = null") {
            NavItem(modifier = Modifier.width(220.dp)) { Text("Overview") }
        }
    }

    Variants(
        title = "Row slots",
        description = "NavItem takes leading and trailing content and an indent.",
        layout = VariantLayout.Vertical,
    ) {
        variant("Leading") {
            NavItem(
                modifier = Modifier.width(260.dp),
                onClick = {},
                leading = { Chevron(direction = ChevronDirection.Right) },
            ) {
                Text("With leading")
            }
        }
        variant("Trailing", detail = "a count badge") {
            NavItem(
                modifier = Modifier.width(260.dp),
                onClick = {},
                trailing = {
                    Badge(style = BadgeStyle.Brand, size = BadgeSize.ExtraSmall) { Text("12") }
                },
            ) {
                Text("With trailing")
            }
        }
        variant("Indented", detail = "indent = 24dp") {
            NavItem(modifier = Modifier.width(260.dp), onClick = {}, indent = 24.dp) {
                Text("Nested row")
            }
        }
    }

    EdgeCases {
        case(
            name = "Nothing selected",
            expectation = "No row is highlighted; the list is still usable.",
        ) {
            NavList(
                nodes = sampleNodes(),
                selectedId = null,
                onItemClick = {},
                modifier = Modifier.width(260.dp),
            )
        }
        case(
            name = "Non-collapsible section",
            expectation = "Header renders without a chevron and its children always show.",
        ) {
            var selected by remember { mutableStateOf<String?>("overview") }
            NavList(
                nodes = listOf(sampleSection(expandable = false, initiallyExpanded = true)),
                selectedId = selected,
                onItemClick = { selected = it.id },
                modifier = Modifier.width(260.dp),
            )
        }
        case(
            name = "Long row label",
            expectation = "Truncates within the sidebar width rather than widening it.",
        ) {
            NavItem(modifier = Modifier.width(220.dp), onClick = {}) {
                Text(
                    text = "Quarterly revenue reconciliation",
                    variant = TextVariant.BodyMedium,
                    color = TextColor.Base,
                    maxLines = 1,
                )
            }
        }
    }
}

private fun sampleSection(
    expandable: Boolean = true,
    initiallyExpanded: Boolean = true,
    disabled: Set<String> = emptySet(),
) = NavNode.Section(
    trigger = NavData("analytics", "Analytics"),
    children = listOf(
        NavData("overview", "Overview"),
        NavData("reports", "Reports", enabled = "reports" !in disabled),
        NavData("exports", "Exports"),
    ),
    expandable = expandable,
    initiallyExpanded = initiallyExpanded,
)

private fun sampleNodes(
    expandable: Boolean = true,
    initiallyExpanded: Boolean = true,
    disabled: Set<String> = emptySet(),
): List<NavNode> = listOf(
    NavNode.Item(NavData("home", "Home")),
    sampleSection(expandable, initiallyExpanded, disabled),
    NavNode.Section(
        trigger = NavData("settings", "Settings"),
        children = listOf(
            NavData("members", "Members"),
            NavData("billing", "Billing"),
        ),
        expandable = expandable,
        initiallyExpanded = false,
    ),
)
