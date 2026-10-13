package dev.voir.areca.playground.foundation.field

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dev.voir.areca.foundation.field.FieldGroup
import dev.voir.areca.foundation.field.FieldSize
import dev.voir.areca.foundation.field.FieldStatus
import dev.voir.areca.foundation.field.FieldStyle
import dev.voir.areca.foundation.field.select.SelectField
import dev.voir.areca.foundation.field.select.SelectOption
import dev.voir.areca.foundation.field.select.SelectSelectionMode
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.Sizes
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants

private val Countries = listOf(
    "United Kingdom",
    "United Arab Emirates",
    "Germany",
    "France",
    "Japan",
    "Australia",
)

private fun countryOptions(disabled: Set<String> = emptySet()): List<SelectOption<String>> =
    Countries.map { SelectOption(key = it, value = it, label = it, enabled = it !in disabled) }

/** Showcase for [SelectField]. */
@Composable
fun SelectFieldShowcase() = ShowcaseScaffold(
    title = "Select field",
    summary = "Field that opens a dropdown of options, choosing one value or several.",
    module = "foundation",
) {
    Playground {
        val mode by enumControl(
            key = "selectionMode",
            default = SelectSelectionMode.Single,
            options = SelectSelectionMode.entries,
            optionLabel = { it.name },
        )
        val status by enumControl(
            key = "status",
            default = FieldStatus.Default,
            options = FieldStatus.entries,
            optionLabel = { it.name },
        )
        val enabled by booleanControl("enabled", default = true)
        val readOnly by booleanControl("readOnly", default = false)
        val loading by booleanControl("loading", default = false)

        preview {
            var values by remember { mutableStateOf(emptySet<String>()) }

            SelectField(
                values = values,
                onChange = { values = it },
                options = countryOptions(),
                modifier = Modifier.fillMaxWidth(),
                selectionMode = mode,
                status = status,
                enabled = enabled,
                readOnly = readOnly,
                loading = loading,
                placeholder = { Text("Choose a country") },
            )
        }
    }

    Variants(
        title = "Selection modes",
        description = "Single replaces the value and closes; Multiple toggles and stays open.",
        layout = VariantLayout.Vertical,
    ) {
        for (mode in SelectSelectionMode.entries) {
            variant(name = mode.name) {
                SampleSelect(mode = mode)
            }
        }
    }

    Variants(
        title = "Styles",
        description = "Same style presets as the other fields.",
        layout = VariantLayout.Vertical,
    ) {
        variant("Outlined") { SampleSelect(style = FieldStyle.outlined()) }
        variant("Soft") { SampleSelect(style = FieldStyle.soft()) }
        variant("Filled") { SampleSelect(style = FieldStyle.filled()) }
        variant("Underlined") { SampleSelect(style = FieldStyle.underlined()) }
        variant("Inline") { SampleSelect(style = FieldStyle.inline()) }
    }

    Sizes(layout = VariantLayout.Vertical) {
        variant("ExtraSmall") { SampleSelect(size = FieldSize.ExtraSmall) }
        variant("Small") { SampleSelect(size = FieldSize.Small) }
        variant("Medium") { SampleSelect(size = FieldSize.Medium) }
        variant("Large") { SampleSelect(size = FieldSize.Large) }
        variant("ExtraLarge") { SampleSelect(size = FieldSize.ExtraLarge) }
    }

    States(layout = VariantLayout.Vertical) {
        variant("Empty") { SampleSelect() }
        variant("Selected") { SampleSelect(initial = setOf("Germany")) }
        variant("Disabled") { SampleSelect(initial = setOf("Germany"), enabled = false) }
        variant("Read-only") { SampleSelect(initial = setOf("Germany"), readOnly = true) }
        variant("Loading") { SampleSelect(loading = true) }
        for (status in FieldStatus.entries.filter { it != FieldStatus.Default }) {
            variant(status.name) { SampleSelect(initial = setOf("Germany"), status = status) }
        }
    }

    Recipes {
        recipe(
            name = "Inside a field group",
            description = "The usual shape: label, select, validation message.",
        ) {
            var values by remember { mutableStateOf(emptySet<String>()) }

            FieldGroup(
                modifier = Modifier.fillMaxWidth(),
                label = "Billing country",
                required = true,
                error = if (values.isEmpty()) "Choose a country to continue." else null,
            ) { field ->
                SelectField(
                    values = values,
                    onChange = { values = it },
                    options = countryOptions(),
                    field = field,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Choose a country") },
                )
            }
        }
    }

    EdgeCases {
        case(
            name = "Disabled options",
            expectation = "Unavailable options are visible but cannot be chosen.",
        ) {
            var values by remember { mutableStateOf(emptySet<String>()) }

            SelectField(
                values = values,
                onChange = { values = it },
                options = countryOptions(disabled = setOf("Japan", "Australia")),
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Two options are disabled") },
            )
        }
        case(
            name = "Many values selected",
            expectation = "Summary truncates rather than growing the field.",
        ) {
            SampleSelect(
                mode = SelectSelectionMode.Multiple,
                initial = Countries.toSet(),
            )
        }
        case(
            name = "No options",
            expectation = "Opens to an empty list rather than refusing to open.",
        ) {
            var values by remember { mutableStateOf(emptySet<String>()) }

            SelectField(
                values = values,
                onChange = { values = it },
                options = emptyList(),
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Nothing to choose") },
            )
        }
    }
}

@Composable
private fun SampleSelect(
    mode: SelectSelectionMode = SelectSelectionMode.Single,
    style: FieldStyle = FieldStyle.outlined(),
    size: FieldSize = FieldSize.Medium,
    status: FieldStatus = FieldStatus.Default,
    initial: Set<String> = emptySet(),
    enabled: Boolean = true,
    readOnly: Boolean = false,
    loading: Boolean = false,
) {
    var values by remember { mutableStateOf(initial) }

    SelectField(
        values = values,
        onChange = { values = it },
        options = countryOptions(),
        modifier = Modifier.fillMaxWidth(),
        selectionMode = mode,
        style = style,
        size = size,
        status = status,
        enabled = enabled,
        readOnly = readOnly,
        loading = loading,
        placeholder = { Text("Choose a country") },
    )
}
