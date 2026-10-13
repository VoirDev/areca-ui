package dev.voir.areca.playground.foundation.selection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import dev.voir.areca.foundation.selection.Radio
import dev.voir.areca.foundation.selection.RadioSize
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.Sizes
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.theme.ArecaTheme

/** Sizes offered by [RadioSize], as a selectable list. */
private enum class RadioSizeOption(val control: String) {
    Small("16dp control"),
    Medium("20dp control"),
    Large("24dp control"),
}

/** Showcase for [Radio]. */
@Composable
fun RadioShowcase() = ShowcaseScaffold(
    title = "Radio",
    summary = "Single-select control: choosing one option clears the others in its group.",
    module = "foundation",
) {
    Playground {
        val checked by booleanControl("checked", default = false)
        val enabled by booleanControl("enabled", default = true)
        val readOnly by booleanControl(
            key = "readOnly",
            default = false,
            description = "Shows its value but refuses input.",
        )
        val size by enumControl(
            key = "size",
            default = RadioSizeOption.Medium,
            options = RadioSizeOption.entries,
            optionLabel = { it.name },
        )

        preview {
            var value by remember { mutableStateOf(checked) }

            LabelledRadio(label = describeRadioState(value, enabled, readOnly)) {
                Radio(
                    checked = value,
                    enabled = enabled,
                    readOnly = readOnly,
                    size = size.resolve(),
                    onClick = { value = true },
                )
            }
        }
    }

    Sizes(description = "Shown selected so the inner circle can be compared, not just the ring.") {
        for (size in RadioSizeOption.entries) {
            variant(name = size.name, detail = size.control) {
                LabelledRadio(label = "Selected") {
                    Radio(checked = true, size = size.resolve(), onClick = {})
                }
            }
        }
    }

    States(
        description = "Radio has no indeterminate state — that is the difference from Checkbox.",
        layout = VariantLayout.Grid,
    ) {
        for (mode in listOf(RadioMode.Interactive, RadioMode.Disabled, RadioMode.ReadOnly)) {
            variant(name = "Unselected${mode.suffix}") {
                LabelledRadio(label = "Unselected") {
                    Radio(
                        checked = false,
                        enabled = mode != RadioMode.Disabled,
                        readOnly = mode == RadioMode.ReadOnly,
                        onClick = {},
                    )
                }
            }
            variant(name = "Selected${mode.suffix}") {
                LabelledRadio(label = "Selected") {
                    Radio(
                        checked = true,
                        enabled = mode != RadioMode.Disabled,
                        readOnly = mode == RadioMode.ReadOnly,
                        onClick = {},
                    )
                }
            }
        }
    }

    Recipes {
        recipe(
            name = "Option group",
            description = "The shape radio is actually used in — selection is owned by the group.",
        ) {
            var selected by remember { mutableStateOf("Standard") }
            val options = listOf("Standard", "Express", "Same day")

            Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg)) {
                options.forEach { option ->
                    LabelledRadio(label = option) {
                        Radio(
                            checked = selected == option,
                            onClick = { selected = option },
                        )
                    }
                }
            }
        }
    }

    EdgeCases {
        case(
            name = "Clicking a selected radio",
            expectation = "Stays selected. A radio never clears itself — only the group can.",
        ) {
            var selected by remember { mutableStateOf(true) }

            LabelledRadio(label = if (selected) "Still selected" else "Cleared") {
                Radio(checked = selected, onClick = { selected = true })
            }
        }
    }
}

/** How a state row refuses or accepts input. */
private enum class RadioMode(val suffix: String) {
    Interactive(""),
    Disabled(" / disabled"),
    ReadOnly(" / read-only"),
}

@Composable
private fun LabelledRadio(label: String, control: @Composable () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        control()
        Text(text = label, variant = TextVariant.BodyMedium)
    }
}

private fun describeRadioState(checked: Boolean, enabled: Boolean, readOnly: Boolean): String {
    val state = if (checked) "Selected" else "Not selected"
    val mode = when {
        !enabled -> "disabled"
        readOnly -> "read-only"
        else -> null
    }

    return listOfNotNull(state, mode).joinToString(", ")
}

@Composable
private fun RadioSizeOption.resolve(): RadioSize = when (this) {
    RadioSizeOption.Small -> RadioSize.Small
    RadioSizeOption.Medium -> RadioSize.Medium
    RadioSizeOption.Large -> RadioSize.Large
}
