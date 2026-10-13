package dev.voir.areca.playground.foundation.selection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import dev.voir.areca.foundation.selection.Checkbox
import dev.voir.areca.foundation.selection.CheckboxSize
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Sizes
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.theme.ArecaTheme

/** Sizes offered by [CheckboxSize], as a selectable list. */
private enum class CheckboxSizeOption(val control: String) {
    Small("16dp control"),
    Medium("20dp control"),
    Large("24dp control"),
}

/** Showcase for [Checkbox]. */
@Composable
fun CheckboxShowcase() = ShowcaseScaffold(
    title = "Checkbox",
    summary = "Multi-select control with checked, unchecked and indeterminate marks.",
    module = "foundation",
) {
    Playground {
        val checked by booleanControl("checked", default = false)
        val indeterminate by booleanControl(
            key = "indeterminate",
            default = false,
            description = "Draws a line instead of a tick. Only visible while checked.",
        )
        val enabled by booleanControl("enabled", default = true)
        val readOnly by booleanControl(
            key = "readOnly",
            default = false,
            description = "Shows its value but refuses input.",
        )
        val size by enumControl(
            key = "size",
            default = CheckboxSizeOption.Medium,
            options = CheckboxSizeOption.entries,
            optionLabel = { it.name },
        )

        preview {
            var value by remember { mutableStateOf(checked) }

            LabelledControl(label = describeCheckboxState(value, indeterminate, enabled, readOnly)) {
                Checkbox(
                    checked = value,
                    indeterminate = indeterminate,
                    enabled = enabled,
                    readOnly = readOnly,
                    size = size.resolve(),
                    onCheckedChange = { value = it },
                )
            }
        }
    }

    Sizes(description = "Shown checked so the drawn mark can be compared, not just the box.") {
        for (size in CheckboxSizeOption.entries) {
            variant(name = size.name, detail = size.control) {
                LabelledControl(label = "Selected") {
                    Checkbox(checked = true, size = size.resolve(), onCheckedChange = {})
                }
            }
        }
    }

    States(
        description = "Checked and unchecked are one boolean; indeterminate is a separate flag.",
        layout = VariantLayout.Grid,
    ) {
        for (mode in listOf(CheckboxMode.Interactive, CheckboxMode.Disabled, CheckboxMode.ReadOnly)) {
            variant(name = "Unchecked${mode.suffix}") {
                LabelledControl(label = "Unchecked") {
                    Checkbox(
                        checked = false,
                        enabled = mode != CheckboxMode.Disabled,
                        readOnly = mode == CheckboxMode.ReadOnly,
                        onCheckedChange = {},
                    )
                }
            }
            variant(name = "Checked${mode.suffix}") {
                LabelledControl(label = "Checked") {
                    Checkbox(
                        checked = true,
                        enabled = mode != CheckboxMode.Disabled,
                        readOnly = mode == CheckboxMode.ReadOnly,
                        onCheckedChange = {},
                    )
                }
            }
            variant(name = "Indeterminate${mode.suffix}") {
                LabelledControl(label = "Indeterminate") {
                    Checkbox(
                        checked = true,
                        indeterminate = true,
                        enabled = mode != CheckboxMode.Disabled,
                        readOnly = mode == CheckboxMode.ReadOnly,
                        onCheckedChange = {},
                    )
                }
            }
        }
    }

    EdgeCases {
        case(
            name = "Indeterminate while unchecked",
            expectation = "Renders as plain unchecked — the flag only applies to a checked box.",
        ) {
            LabelledControl(label = "checked = false, indeterminate = true") {
                Checkbox(checked = false, indeterminate = true, onCheckedChange = {})
            }
        }
        case(
            name = "Read-only and disabled together",
            expectation = "Reads as disabled; read-only adds nothing once input is already refused.",
        ) {
            LabelledControl(label = "Both flags set") {
                Checkbox(
                    checked = true,
                    enabled = false,
                    readOnly = true,
                    onCheckedChange = {},
                )
            }
        }
    }
}

/** How a state row refuses or accepts input. */
private enum class CheckboxMode(val suffix: String) {
    Interactive(""),
    Disabled(" / disabled"),
    ReadOnly(" / read-only"),
}

@Composable
private fun LabelledControl(label: String, control: @Composable () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        control()
        Text(text = label, variant = TextVariant.BodyMedium)
    }
}

private fun describeCheckboxState(
    checked: Boolean,
    indeterminate: Boolean,
    enabled: Boolean,
    readOnly: Boolean,
): String {
    val state = when {
        checked && indeterminate -> "Indeterminate"
        checked -> "Checked"
        else -> "Unchecked"
    }
    val mode = when {
        !enabled -> "disabled"
        readOnly -> "read-only"
        else -> null
    }

    return listOfNotNull(state, mode).joinToString(", ")
}

@Composable
private fun CheckboxSizeOption.resolve(): CheckboxSize = when (this) {
    CheckboxSizeOption.Small -> CheckboxSize.Small
    CheckboxSizeOption.Medium -> CheckboxSize.Medium
    CheckboxSizeOption.Large -> CheckboxSize.Large
}
