package dev.voir.areca.playground.foundation.tabs

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.tabs.TabItem
import dev.voir.areca.foundation.tabs.Tabs
import dev.voir.areca.foundation.tabs.TabsHorizontalPosition
import dev.voir.areca.foundation.tabs.TabsOrientation
import dev.voir.areca.foundation.tabs.TabsSize
import dev.voir.areca.foundation.tabs.TabsVariant
import dev.voir.areca.foundation.tabs.TabsVerticalPosition
import dev.voir.areca.foundation.tabs.rememberTabsState
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants

/** Sections used by the tab examples. */
private enum class ExampleTab {
    Overview,
    Analytics,
    Reports,
    Customers,
    Billing,
    Integrations,
    Security,
    Notifications,
    Settings,
    Disabled,
}

private val ShortTabs = listOf(
    ExampleTab.Overview,
    ExampleTab.Analytics,
    ExampleTab.Settings,
    ExampleTab.Disabled,
)

/** Showcase for [Tabs]. */
@Composable
fun TabsShowcase() = ShowcaseScaffold(
    title = "Tabs",
    summary = "Switches between sibling views, horizontally or vertically.",
    module = "foundation",
) {
    Playground {
        val variant by enumControl(
            key = "variant",
            default = TabsVariant.Underline,
            options = TabsVariant.entries,
            optionLabel = { it.name },
        )
        val orientation by enumControl(
            key = "orientation",
            default = TabsOrientation.Horizontal,
            options = TabsOrientation.entries,
            optionLabel = { it.name },
        )
        val horizontalPosition by enumControl(
            key = "horizontalPosition",
            default = TabsHorizontalPosition.Bottom,
            options = TabsHorizontalPosition.entries,
            optionLabel = { it.name },
            description = "Where the indicator sits when horizontal.",
        )
        val verticalPosition by enumControl(
            key = "verticalPosition",
            default = TabsVerticalPosition.Start,
            options = TabsVerticalPosition.entries,
            optionLabel = { it.name },
            description = "Which edge the indicator sits on when vertical.",
        )
        val fillWidth by booleanControl(
            key = "fillWidth",
            default = false,
            description = "Share the strip's width equally, horizontally. The strip stops scrolling.",
        )

        preview {
            val state = rememberTabsState(ExampleTab.Overview)

            Tabs(
                tabs = exampleTabs(ShortTabs),
                state = state,
                modifier = if (orientation == TabsOrientation.Vertical) {
                    Modifier.height(200.dp)
                } else {
                    Modifier
                },
                size = variant.size,
                variant = variant,
                orientation = orientation,
                horizontalPosition = horizontalPosition,
                verticalPosition = verticalPosition,
                fillWidth = fillWidth,
            )
        }
    }

    Variants(
        title = "Variant × orientation",
        description = "Each cell keeps its own selection, so they can be clicked independently.",
        layout = VariantLayout.Grid,
    ) {
        for (variant in TabsVariant.entries) {
            variant(name = "${variant.name} / Horizontal") {
                val state = rememberTabsState(ExampleTab.Overview)
                Tabs(tabs = exampleTabs(ShortTabs), state = state, size = variant.size, variant = variant)
            }
        }
        for (variant in TabsVariant.entries) {
            variant(name = "${variant.name} / Vertical") {
                val state = rememberTabsState(ExampleTab.Overview)
                Tabs(
                    tabs = exampleTabs(ShortTabs),
                    state = state,
                    modifier = Modifier.height(180.dp),
                    size = variant.size,
                    variant = variant,
                    orientation = TabsOrientation.Vertical,
                )
            }
        }
    }

    Variants(
        title = "Indicator position",
        description = "Which edge the active indicator is drawn on.",
        layout = VariantLayout.Grid,
    ) {
        for (position in TabsHorizontalPosition.entries) {
            variant(name = "Horizontal / ${position.name}") {
                val state = rememberTabsState(ExampleTab.Overview)
                Tabs(
                    tabs = exampleTabs(ShortTabs),
                    state = state,
                    variant = TabsVariant.Underline,
                    horizontalPosition = position,
                )
            }
        }
        for (position in TabsVerticalPosition.entries) {
            variant(name = "Vertical / ${position.name}") {
                val state = rememberTabsState(ExampleTab.Overview)
                Tabs(
                    tabs = exampleTabs(ShortTabs),
                    state = state,
                    modifier = Modifier.height(180.dp),
                    variant = TabsVariant.Underline,
                    orientation = TabsOrientation.Vertical,
                    verticalPosition = position,
                )
            }
        }
    }

    Recipes {
        recipe(
            name = "Period length",
            description = "A segmented switch for a setting: three short labels, sized to their text.",
        ) {
            val state = rememberTabsState("Month")
            Tabs(
                tabs = listOf("Week", "Month", "Year").map { TabItem(it) { Text(it) } },
                state = state,
                size = TabsSize.Segmented,
                variant = TabsVariant.Segmented,
            )
        }
        recipe(
            name = "Sheet filter",
            description = "Equal segments across a sheet, switching what the list below it shows.",
        ) {
            val state = rememberTabsState("Categories")
            Tabs(
                tabs = listOf("Categories", "Tags", "Wallets").map { TabItem(it) { Text(it) } },
                state = state,
                size = TabsSize.Segmented,
                variant = TabsVariant.Segmented,
                fillWidth = true,
            )
        }
    }

    EdgeCases {
        case(
            name = "More tabs than fit",
            expectation = "Scrolls along its axis rather than shrinking or wrapping the tabs.",
        ) {
            val state = rememberTabsState(ExampleTab.Security)
            Tabs(
                tabs = exampleTabs(ExampleTab.entries),
                state = state,
                modifier = Modifier.width(320.dp),
                variant = TabsVariant.Underline,
            )
        }
        case(
            name = "Vertical overflow",
            expectation = "Scrolls vertically, keeping the selected tab reachable.",
        ) {
            val state = rememberTabsState(ExampleTab.Security)
            Row(modifier = Modifier.height(180.dp)) {
                Tabs(
                    tabs = exampleTabs(ExampleTab.entries),
                    state = state,
                    modifier = Modifier.height(180.dp),
                    variant = TabsVariant.Underline,
                    orientation = TabsOrientation.Vertical,
                )
            }
        }
        case(
            name = "Disabled tab",
            expectation = "Reads as unavailable and cannot take selection.",
        ) {
            val state = rememberTabsState(ExampleTab.Overview)
            Tabs(tabs = exampleTabs(ShortTabs), state = state, variant = TabsVariant.Button)
        }
    }
}

/** The metrics a strip of this variant is drawn at: [TabsSize.Segmented] for the segmented one. */
private val TabsVariant.size: TabsSize
    @Composable
    get() = if (this == TabsVariant.Segmented) TabsSize.Segmented else TabsSize.Default

/** The `Disabled` entry is rendered disabled wherever it appears. */
private fun exampleTabs(values: List<ExampleTab>): List<TabItem<ExampleTab>> =
    values.map { tab ->
        TabItem(tab, enabled = tab != ExampleTab.Disabled) { Text(tab.name) }
    }
