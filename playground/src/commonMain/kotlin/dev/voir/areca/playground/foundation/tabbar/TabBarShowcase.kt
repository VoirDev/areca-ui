package dev.voir.areca.playground.foundation.tabbar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.voir.areca.foundation.tabbar.TabBar
import dev.voir.areca.foundation.tabbar.TabBarItem
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants

/** Destinations used by the examples. */
private enum class Section { Overview, Activity, Members, Settings }

/**
 * Showcase for [TabBar].
 *
 * The docked sibling of `FloatingTabBar`: same item model, but it sits inline in a layout rather
 * than floating above it.
 */
@Composable
fun TabBarShowcase() = ShowcaseScaffold(
    title = "Tab bar",
    summary = "Docked navigation bar that spreads its destinations across the full width.",
    module = "foundation",
) {
    Playground {
        val disableMembers by booleanControl(
            key = "disable Members",
            default = false,
            description = "A disabled destination cannot take selection.",
        )
        val fewer by booleanControl(
            key = "two destinations",
            default = false,
            description = "Items always share the width evenly, however many there are.",
        )

        preview {
            var selected by remember { mutableStateOf(Section.Overview) }
            val values = if (fewer) listOf(Section.Overview, Section.Settings) else Section.entries

            TabBar(
                items = sections(values, disabled = if (disableMembers) setOf(Section.Members) else emptySet()),
                selectedValue = selected,
                onValueSelected = { selected = it },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }

    Variants(
        title = "Destination counts",
        description = "Items divide the width evenly; the bar never scrolls.",
        layout = VariantLayout.Vertical,
    ) {
        for (count in 2..4) {
            variant(name = "$count destinations") {
                var selected by remember { mutableStateOf(Section.Overview) }
                TabBar(
                    items = sections(Section.entries.take(count)),
                    selectedValue = selected,
                    onValueSelected = { selected = it },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }

    EdgeCases {
        case(
            name = "Disabled destination",
            expectation = "Reads as unavailable and refuses selection.",
        ) {
            var selected by remember { mutableStateOf(Section.Overview) }
            TabBar(
                items = sections(Section.entries, disabled = setOf(Section.Members)),
                selectedValue = selected,
                onValueSelected = { selected = it },
                modifier = Modifier.fillMaxWidth(),
            )
        }
        case(
            name = "Nothing selected",
            expectation = "No destination is highlighted; the bar is still usable.",
        ) {
            var selected by remember { mutableStateOf<Section?>(null) }
            TabBar(
                items = sections(Section.entries),
                selectedValue = selected,
                onValueSelected = { selected = it },
                modifier = Modifier.fillMaxWidth(),
            )
        }
        case(
            name = "Long destination names",
            expectation = "Labels truncate within their share rather than widening the bar.",
        ) {
            var selected by remember { mutableStateOf("Reconciliation") }
            val long = listOf("Reconciliation", "Notifications", "Integrations")

            TabBar(
                items = long.map { name ->
                    TabBarItem(value = name) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(text = name, variant = TextVariant.LabelSmall, maxLines = 1)
                        }
                    }
                },
                selectedValue = selected,
                onValueSelected = { selected = it },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

private fun sections(
    values: List<Section>,
    disabled: Set<Section> = emptySet(),
): List<TabBarItem<Section>> = values.map { section ->
    TabBarItem(value = section, enabled = section !in disabled) {
        Box(contentAlignment = Alignment.Center) {
            Text(text = section.name, variant = TextVariant.LabelMedium)
        }
    }
}
