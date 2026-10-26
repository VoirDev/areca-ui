package dev.voir.areca.playground.foundation.field

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.button.ButtonSize
import dev.voir.areca.foundation.button.ButtonStyle
import dev.voir.areca.foundation.field.select.SelectFlowModal
import dev.voir.areca.foundation.field.select.SelectOption
import dev.voir.areca.foundation.field.select.SelectSelectionMode
import dev.voir.areca.foundation.overlay.FlowModalController
import dev.voir.areca.foundation.overlay.FlowModalHeaderInsets
import dev.voir.areca.foundation.overlay.rememberFlowModal
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.foundation.topbar.CloseAction
import dev.voir.areca.foundation.topbar.TopBar
import dev.voir.areca.foundation.topbar.TopBarSize
import dev.voir.areca.foundation.topbar.TopBarStyle
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.theme.ArecaTheme

private val Cities = listOf(
    "Abu Dhabi", "Amsterdam", "Berlin", "Copenhagen", "Dubai", "Edinburgh",
    "Helsinki", "Lisbon", "London", "Madrid", "Oslo", "Paris", "Prague",
    "Reykjavik", "Stockholm", "Tokyo", "Vienna", "Zurich",
)

private fun cityOptions(): List<SelectOption<String>> =
    Cities.map { SelectOption(key = it, value = it, label = it) }

/** Showcase for [SelectFlowModal]. */
@Composable
fun SelectFlowModalShowcase() = ShowcaseScaffold(
    title = "Select flow modal",
    summary = "Full-surface option picker with search, for lists too long for a dropdown.",
    module = "foundation",
) {
    Playground(description = "Open the picker, then search and select inside it.") {
        val mode by enumControl(
            key = "selectionMode",
            default = SelectSelectionMode.Single,
            options = SelectSelectionMode.entries,
            optionLabel = { it.name },
        )
        val searchable by booleanControl(
            key = "searchable",
            default = true,
            description = "Passing onQueryChange turns the search field on.",
        )

        val controller = rememberFlowModal(maxHeightFraction = 0.9f)
        var values by remember { mutableStateOf(emptySet<String>()) }

        actionControl(
            key = "open",
            buttonLabel = "Open picker",
            onInvoke = { controller.show(partial = false) },
        )

        preview {
            SelectionSummary(values)

            SelectFlowModal(
                controller = controller,
                values = values,
                options = cityOptions(),
                onChange = { values = it },
                selectionMode = mode,
                onQueryChange = if (searchable) ({ }) else null,
                header = { PickerHeader("Choose a city", controller) },
            )
        }
    }

    Recipes {
        recipe(
            name = "Single selection",
            description = "Choosing an option applies it and closes the picker.",
        ) {
            PickerExample(
                label = "Choose a city",
                mode = SelectSelectionMode.Single,
            )
        }
        recipe(
            name = "Multiple selection",
            description = "Options toggle and the picker stays open until it is dismissed.",
        ) {
            PickerExample(
                label = "Choose cities",
                mode = SelectSelectionMode.Multiple,
            )
        }
    }

    EdgeCases {
        case(
            name = "Search with no matches",
            expectation = "Empty content names the query rather than showing a blank list.",
        ) {
            PickerExample(label = "Search for something absent", mode = SelectSelectionMode.Single)
        }
        case(
            name = "Search disabled",
            expectation = "No search field; the list is scrolled instead.",
        ) {
            PickerExample(
                label = "Open without search",
                mode = SelectSelectionMode.Single,
                searchable = false,
            )
        }
    }
}

@Composable
private fun PickerExample(
    label: String,
    mode: SelectSelectionMode,
    searchable: Boolean = true,
) {
    val controller = rememberFlowModal(maxHeightFraction = 0.9f)
    var values by remember { mutableStateOf(emptySet<String>()) }

    Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md)) {
        Row(horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md)) {
            Button(
                style = ButtonStyle.solid(ActionColor.Brand),
                size = ButtonSize.Small,
                onClick = { controller.show(partial = false) },
            ) {
                Text(label)
            }
            Button(
                style = ButtonStyle.outlined(ActionColor.Neutral),
                size = ButtonSize.Small,
                onClick = { values = emptySet() },
            ) {
                Text("Clear")
            }
        }

        SelectionSummary(values)
    }

    SelectFlowModal(
        controller = controller,
        values = values,
        options = cityOptions(),
        onChange = { values = it },
        selectionMode = mode,
        onQueryChange = if (searchable) ({ }) else null,
        header = { PickerHeader(label, controller) },
    )
}

/**
 * A picker sheet's header: its title and the way out, as a modal [TopBar].
 *
 * @param title What is being picked.
 * @param controller Controller of the sheet, hidden by the close control.
 */
@Composable
private fun PickerHeader(title: String, controller: FlowModalController) {
    TopBar(
        title = title,
        modifier = Modifier.fillMaxWidth(),
        size = TopBarSize.Modal,
        style = TopBarStyle.Modal,
        windowInsets = FlowModalHeaderInsets,
        trailing = { CloseAction(onClick = controller::hide) },
    )
}

@Composable
private fun SelectionSummary(values: Set<String>) {
    Text(
        text = if (values.isEmpty()) "Nothing selected" else values.sorted().joinToString(", "),
        variant = TextVariant.BodySmall,
        color = TextColor.Muted,
    )
}
