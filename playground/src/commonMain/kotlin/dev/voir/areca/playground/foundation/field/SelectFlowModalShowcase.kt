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
import dev.voir.areca.foundation.field.select.SelectOptionSection
import dev.voir.areca.foundation.field.select.rememberSelectFlowModalState
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

private val PopularBrands = listOf("Apple", "Bosch", "IKEA", "Samsung")

private val AllBrands = listOf(
    "3M", "Acer", "Apple", "Asus", "Bosch", "Braun", "Brother", "Canon", "Dell", "DeLonghi",
    "Dyson", "Epson", "HP", "IKEA", "Karcher", "Lenovo", "LG", "Makita", "Miele", "Nikon",
    "Philips", "Samsung", "Sony", "Xiaomi",
)

private fun brandOption(label: String) = SelectOption(key = label, value = label, label = label)

/**
 * Brand sections as a caller builds them for a query: the user's own, popular, similar (only while
 * searching) and all of them under a letter index. The modal filters nothing itself.
 *
 * @param query Current search text.
 * @param yours Brands the user added.
 * @return The sections, empty ones included — the modal skips those.
 */
private fun brandSections(query: String, yours: List<String>): List<SelectOptionSection<String>> {
    val needle = query.trim()
    fun matches(label: String) = needle.isEmpty() || label.contains(needle, ignoreCase = true)
    val similar = if (needle.isEmpty()) {
        emptyList()
    } else {
        AllBrands.filter { !matches(it) && it.first().equals(needle.first(), ignoreCase = true) }
    }
    return listOf(
        SelectOptionSection(key = "yours", title = "Yours", options = yours.filter(::matches).map(::brandOption)),
        SelectOptionSection(key = "popular", title = "Popular", options = PopularBrands.filter(::matches).map(::brandOption)),
        SelectOptionSection(key = "similar", title = "Similar", options = similar.map(::brandOption)),
        SelectOptionSection(
            key = "all",
            title = "All",
            options = (AllBrands + yours).distinct().sortedBy { it.lowercase() }.filter(::matches).map(::brandOption),
            indexed = true,
        ),
    )
}

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

        val sectioned by booleanControl(
            key = "sections",
            default = false,
            description = "Pass sections instead of options: Yours, Popular, Similar, All by letter.",
        )
        val creatable by booleanControl(
            key = "onCreateOption",
            default = true,
            description = "With sections and search, offers to add the query as the user's own.",
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

            if (sectioned) {
                BrandPickerModal(
                    controller = controller,
                    values = values,
                    onValuesChange = { values = it },
                    mode = mode,
                    searchable = searchable,
                    creatable = creatable,
                )
            } else {
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

    Recipes(
        title = "Sections",
        description = "Options grouped under titles, the full list indexed by letter, and a way to add one's own.",
    ) {
        recipe(
            name = "Brand, single",
            description = "Yours, Popular and All by letter; Similar appears while searching. Type a brand that is " +
                "not listed — \"Gorenje\" — to be offered to add it.",
        ) {
            BrandPickerExample(label = "Choose a brand", mode = SelectSelectionMode.Single)
        }
        recipe(
            name = "Brands, multiple",
            description = "The same sections; an option listed in Popular and All is ticked in both.",
        ) {
            BrandPickerExample(label = "Choose brands", mode = SelectSelectionMode.Multiple)
        }
        recipe(
            name = "Without search",
            description = "No search field, so no query and no row to add one: sections and the index alone.",
        ) {
            BrandPickerExample(label = "Browse brands", mode = SelectSelectionMode.Single, searchable = false)
        }
    }

    EdgeCases {
        case(
            name = "Sections with no matches",
            expectation = "Every section empties and the add-your-own row takes the empty body's place.",
        ) {
            BrandPickerExample(label = "Search \"Gorenje\"", mode = SelectSelectionMode.Single, initialQuery = "Gorenje")
        }
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

@Composable
private fun BrandPickerExample(
    label: String,
    mode: SelectSelectionMode,
    searchable: Boolean = true,
    initialQuery: String = "",
) {
    val controller = rememberFlowModal(maxHeightFraction = 0.9f)
    var values by remember { mutableStateOf(emptySet<String>()) }

    Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md)) {
        Button(
            style = ButtonStyle.solid(ActionColor.Brand),
            size = ButtonSize.Small,
            onClick = { controller.show(partial = false) },
        ) {
            Text(label)
        }
        SelectionSummary(values)
    }

    BrandPickerModal(
        controller = controller,
        values = values,
        onValuesChange = { values = it },
        mode = mode,
        searchable = searchable,
        creatable = true,
        initialQuery = initialQuery,
        title = label,
    )
}

/**
 * A sectioned brand picker: filters its sections by the query and adds a typed brand to "Yours",
 * selecting it — closing the sheet under single selection.
 */
@Composable
private fun BrandPickerModal(
    controller: FlowModalController,
    values: Set<String>,
    onValuesChange: (Set<String>) -> Unit,
    mode: SelectSelectionMode,
    searchable: Boolean,
    creatable: Boolean,
    initialQuery: String = "",
    title: String = "Choose a brand",
) {
    val state = rememberSelectFlowModalState(initialQuery = initialQuery)
    var yours by remember { mutableStateOf(listOf("Gaggia", "Moccamaster")) }

    SelectFlowModal(
        controller = controller,
        values = values,
        sections = brandSections(if (searchable) state.query else "", yours),
        onChange = { draft ->
            onValuesChange(draft)
            if (mode == SelectSelectionMode.Single && draft.isNotEmpty()) controller.hide()
        },
        state = state,
        selectionMode = mode,
        onQueryChange = if (searchable) ({ }) else null,
        onCreateOption = if (creatable) {
            { brand ->
                if (brand !in yours) yours = yours + brand
                onValuesChange(if (mode == SelectSelectionMode.Single) setOf(brand) else values + brand)
                state.clearQuery()
                if (mode == SelectSelectionMode.Single) controller.hide()
            }
        } else {
            null
        },
        header = { PickerHeader(title, controller) },
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
