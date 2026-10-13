package dev.voir.areca.playground.foundation.tabbar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.voir.areca.foundation.tabbar.FloatingTabBar
import dev.voir.areca.foundation.tabbar.FloatingTabBarIndicatorStyle
import dev.voir.areca.foundation.tabbar.TabBarItem
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants

/** Destinations used by the examples. */
private enum class Destination { Home, Search, Saved, Profile }

/** Indicator treatments offered by [FloatingTabBarIndicatorStyle]. */
private enum class IndicatorOption { Underline, Fill }

/** Showcase for [FloatingTabBar]. */
@Composable
fun FloatingTabBarShowcase() = ShowcaseScaffold(
    title = "Floating tab bar",
    summary = "Floating navigation bar whose indicator animates between destinations.",
    module = "foundation",
) {
    Playground(description = "Select a destination and watch the indicator travel to it.") {
        val indicator by enumControl(
            key = "indicatorStyle",
            default = IndicatorOption.Underline,
            options = IndicatorOption.entries,
            optionLabel = { it.name },
        )
        val disableSaved by booleanControl(
            key = "disable Saved",
            default = false,
            description = "A disabled item cannot take selection.",
        )

        preview {
            var selected by remember { mutableStateOf(Destination.Home) }

            FloatingTabBar(
                items = destinations(disabled = if (disableSaved) setOf(Destination.Saved) else emptySet()),
                selectedValue = selected,
                onValueSelected = { selected = it },
                modifier = Modifier.fillMaxWidth(),
                indicatorStyle = indicator.resolve(),
                windowInsets = WindowInsets(0, 0, 0, 0),
            )
        }
    }

    Variants(
        title = "Indicator styles",
        description = "Both animate between items; they differ in what moves.",
        layout = VariantLayout.Vertical,
    ) {
        for (option in IndicatorOption.entries) {
            variant(name = option.name) {
                var selected by remember { mutableStateOf(Destination.Home) }

                FloatingTabBar(
                    items = destinations(),
                    selectedValue = selected,
                    onValueSelected = { selected = it },
                    modifier = Modifier.fillMaxWidth(),
                    indicatorStyle = option.resolve(),
                    windowInsets = WindowInsets(0, 0, 0, 0),
                )
            }
        }
    }

    EdgeCases {
        case(
            name = "Disabled destination",
            expectation = "Saved reads as unavailable and the indicator refuses to move to it.",
        ) {
            var selected by remember { mutableStateOf(Destination.Home) }

            FloatingTabBar(
                items = destinations(disabled = setOf(Destination.Saved)),
                selectedValue = selected,
                onValueSelected = { selected = it },
                modifier = Modifier.fillMaxWidth(),
                windowInsets = WindowInsets(0, 0, 0, 0),
            )
        }
        case(
            name = "Nothing selected",
            expectation = "No indicator is drawn; the bar is still usable.",
        ) {
            var selected by remember { mutableStateOf<Destination?>(null) }

            FloatingTabBar(
                items = destinations(),
                selectedValue = selected,
                onValueSelected = { selected = it },
                modifier = Modifier.fillMaxWidth(),
                windowInsets = WindowInsets(0, 0, 0, 0),
            )
        }
        case(
            name = "Two destinations",
            expectation = "Items share the width evenly rather than clustering at one end.",
        ) {
            var selected by remember { mutableStateOf(Destination.Home) }

            FloatingTabBar(
                items = destinations(only = listOf(Destination.Home, Destination.Profile)),
                selectedValue = selected,
                onValueSelected = { selected = it },
                modifier = Modifier.fillMaxWidth(),
                windowInsets = WindowInsets(0, 0, 0, 0),
            )
        }
    }
}

@Composable
private fun IndicatorOption.resolve(): FloatingTabBarIndicatorStyle = when (this) {
    IndicatorOption.Underline -> FloatingTabBarIndicatorStyle.underline()
    IndicatorOption.Fill -> FloatingTabBarIndicatorStyle.fill()
}

private fun destinations(
    only: List<Destination> = Destination.entries,
    disabled: Set<Destination> = emptySet(),
): List<TabBarItem<Destination>> = only.map { destination ->
    TabBarItem(
        value = destination,
        enabled = destination !in disabled,
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(text = destination.name, variant = TextVariant.LabelMedium)
        }
    }
}
