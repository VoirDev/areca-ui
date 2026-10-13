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
import dev.voir.areca.foundation.selection.Toggle
import dev.voir.areca.foundation.selection.ToggleSize
import dev.voir.areca.foundation.selection.ToggleState
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.Sizes
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.theme.ArecaTheme

/** Sizes offered by [ToggleDefaults], as a selectable list. */
private enum class ToggleSizeOption(val track: String) {
    Small("36dp track"),
    Medium("44dp track"),
    Large("52dp track"),
}

/** Which states the toggle cycles through on tap. */
private enum class CycleOption(val states: List<ToggleState>) {
    OnOff(listOf(ToggleState.Off, ToggleState.On)),
    Tristate(listOf(ToggleState.Off, ToggleState.Indeterminate, ToggleState.On)),
}

/** Showcase for [Toggle]. */
@Composable
fun ToggleShowcase() = ShowcaseScaffold(
    title = "Toggle",
    summary = "Switch that cycles through on, off and optionally an indeterminate middle state.",
    module = "foundation",
) {
    Playground {
        val size by enumControl(
            key = "size",
            default = ToggleSizeOption.Medium,
            options = ToggleSizeOption.entries,
            optionLabel = { it.name },
        )
        val cycle by enumControl(
            key = "cycleOrder",
            default = CycleOption.Tristate,
            options = CycleOption.entries,
            optionLabel = { it.name },
            description = "Which states a tap moves between.",
        )
        val enabled by booleanControl("enabled", default = true)

        preview {
            var state by remember { mutableStateOf<ToggleState>(ToggleState.Off) }

            LabelledToggle(label = state.describe()) {
                Toggle(
                    state = state,
                    onToggle = { state = it },
                    size = size.resolve(),
                    enabled = enabled,
                    cycleOrder = cycle.states,
                )
            }
        }
    }

    Sizes(description = "Track and thumb scale together; the thumb inset stays proportional.") {
        for (size in ToggleSizeOption.entries) {
            variant(name = size.name, detail = size.track) {
                var state by remember { mutableStateOf<ToggleState>(ToggleState.On) }
                Toggle(state = state, onToggle = { state = it }, size = size.resolve())
            }
        }
    }

    States(
        description = "Disabled wraps the state it is disabled in, so it still shows its value.",
        layout = VariantLayout.Grid,
    ) {
        variant("Off") { Toggle(state = ToggleState.Off, onToggle = {}) }
        variant("On") { Toggle(state = ToggleState.On, onToggle = {}) }
        variant("Indeterminate") { Toggle(state = ToggleState.Indeterminate, onToggle = {}) }
        variant("Disabled off", detail = "enabled = false") {
            Toggle(state = ToggleState.Off, onToggle = {}, enabled = false)
        }
        variant("Read only on", detail = "readOnly = true") {
            Toggle(state = ToggleState.On, onToggle = {}, readOnly = true)
        }
        variant("enabled = false", detail = "the parameter, not the state") {
            Toggle(state = ToggleState.On, onToggle = {}, enabled = false)
        }
    }

    Recipes {
        recipe(
            name = "Settings rows",
            description = "The usual home for a toggle: one per row, label on the left.",
        ) {
            var push by remember { mutableStateOf<ToggleState>(ToggleState.On) }
            var digest by remember { mutableStateOf<ToggleState>(ToggleState.Off) }
            var sync by remember { mutableStateOf<ToggleState>(ToggleState.Indeterminate) }

            Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg)) {
                SettingRow("Push notifications", push) { push = it }
                SettingRow("Weekly digest", digest) { digest = it }
                SettingRow("Background sync", sync) { sync = it }
            }
        }
    }

    EdgeCases {
        case(
            name = "Two-state cycle",
            expectation = "Never reaches indeterminate, because it is not in the cycle order.",
        ) {
            var state by remember { mutableStateOf<ToggleState>(ToggleState.Off) }

            LabelledToggle(label = state.describe()) {
                Toggle(
                    state = state,
                    onToggle = { state = it },
                    cycleOrder = listOf(ToggleState.Off, ToggleState.On),
                )
            }
        }
        case(
            name = "Disabled state versus disabled parameter",
            expectation = "Disabled greys out; read-only keeps its color but rejects changes.",
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xl)) {
                LabelledToggle(label = "enabled = false") {
                    Toggle(state = ToggleState.On, onToggle = {}, enabled = false)
                }
                LabelledToggle(label = "readOnly = true") {
                    Toggle(state = ToggleState.On, onToggle = {}, readOnly = true)
                }
            }
        }
    }
}

@Composable
private fun SettingRow(
    label: String,
    state: ToggleState,
    onToggle: (ToggleState) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Toggle(state = state, onToggle = onToggle)
        Text(text = label, variant = TextVariant.BodyMedium)
    }
}

@Composable
private fun LabelledToggle(label: String, control: @Composable () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        control()
        Text(text = label, variant = TextVariant.BodySmall, color = TextColor.Subtle)
    }
}

private fun ToggleState.describe(): String = when (this) {
    ToggleState.Off -> "Off"
    ToggleState.On -> "On"
    ToggleState.Indeterminate -> "Indeterminate"
}

@Composable
private fun ToggleSizeOption.resolve(): ToggleSize = when (this) {
    ToggleSizeOption.Small -> ToggleSize.Small
    ToggleSizeOption.Medium -> ToggleSize.Medium
    ToggleSizeOption.Large -> ToggleSize.Large
}
