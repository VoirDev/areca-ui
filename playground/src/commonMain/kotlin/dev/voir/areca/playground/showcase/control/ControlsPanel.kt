package dev.voir.areca.playground.showcase.control

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.voir.areca.playground.showcase.chrome.ChromeButton
import dev.voir.areca.playground.showcase.chrome.ChromeButtonTone
import dev.voir.areca.playground.showcase.chrome.ChromeControlColumn
import dev.voir.areca.playground.showcase.chrome.ChromeControlRow
import dev.voir.areca.playground.showcase.chrome.ChromeInput
import dev.voir.areca.playground.showcase.chrome.ChromeLevel
import dev.voir.areca.playground.showcase.chrome.ChromeSegmented
import dev.voir.areca.playground.showcase.chrome.ChromeSurface
import dev.voir.areca.playground.showcase.chrome.ChromeSwitch
import dev.voir.areca.playground.showcase.chrome.ChromeText
import dev.voir.areca.playground.showcase.chrome.ChromeTextRole
import dev.voir.areca.playground.showcase.chrome.ChromeTone
import dev.voir.areca.theme.ArecaTheme

/**
 * Past this many options a segmented picker stops fitting the control column.
 *
 * Three short labels fit; four rarely do, and a segmented picker does not shrink — it clips, which
 * hides the option somebody was looking for. Past this the cycler takes over, which is narrower and
 * always shows the current value in full.
 */
private const val MaxSegmentedOptions = 3

/**
 * The editable prop list under a playground preview.
 *
 * Rendered from the specs a playground body registered, in declaration order.
 *
 * @param specs Controls to render.
 * @param onReset Restores every control to its declared value.
 * @param modifier Modifier applied to the panel.
 */
@Composable
internal fun ControlsPanel(
    specs: List<ControlSpec>,
    onReset: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (specs.isEmpty()) return

    ChromeSurface(
        modifier = modifier.fillMaxWidth(),
        level = ChromeLevel.Inset,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(ArecaTheme.spacing.xl),
            verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                ChromeText(
                    text = "Controls",
                    role = ChromeTextRole.Detail,
                    tone = ChromeTone.Muted,
                )
                ChromeButton(
                    label = "Reset",
                    onClick = onReset,
                    tone = ChromeButtonTone.Ghost,
                )
            }

            specs.forEach { spec ->
                ControlRow(spec)
            }
        }
    }
}

@Composable
private fun ControlRow(spec: ControlSpec) {
    when (spec) {
        is BooleanControlSpec -> {
            var value by spec.state
            ChromeControlRow(label = spec.label, description = spec.description) {
                ChromeSwitch(checked = value, onCheckedChange = { value = it })
            }
        }

        is TextControlSpec -> {
            var value by spec.state
            ChromeControlRow(label = spec.label, description = spec.description) {
                ChromeInput(
                    value = value,
                    onValueChange = { value = it },
                    placeholder = spec.placeholder,
                )
            }
        }

        is NumberControlSpec -> {
            val value by spec.state
            ChromeControlRow(label = spec.label, description = spec.description) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    ChromeButton(label = "‹", onClick = { spec.nudge(-1) })
                    ChromeText(
                        text = spec.format(value),
                        role = ChromeTextRole.Label,
                        maxLines = 1,
                    )
                    ChromeButton(label = "›", onClick = { spec.nudge(1) })
                }
            }
        }

        is ActionControlSpec -> {
            ChromeControlRow(label = spec.label, description = spec.description) {
                ChromeButton(
                    label = spec.buttonLabel,
                    onClick = spec.onInvoke,
                    tone = ChromeButtonTone.Accent,
                )
            }
        }

        is EnumControlSpec<*> -> EnumControlRow(spec)
    }
}

@Composable
private fun <T> EnumControlRow(spec: EnumControlSpec<T>) {
    var value by spec.state

    // Two options fit beside their label. More do not: a segmented picker does not shrink, so it
    // would clip, and a clipped picker hides the option somebody was looking for.
    if (spec.options.size <= 2) {
        ChromeControlRow(label = spec.label, description = spec.description) {
            ChromeSegmented(
                options = spec.options,
                selected = value,
                onSelect = { value = it },
                label = spec.optionLabel,
            )
        }
        return
    }

    ChromeControlColumn(label = spec.label, description = spec.description) {
        if (spec.options.size <= MaxSegmentedOptions) {
            ChromeSegmented(
                options = spec.options,
                selected = value,
                onSelect = { value = it },
                label = spec.optionLabel,
                modifier = Modifier.fillMaxWidth(),
            )
        } else {
            ChromeOptionCycler(
                options = spec.options,
                selected = value,
                onSelect = { value = it },
                label = spec.optionLabel,
            )
        }
    }
}

/**
 * Fallback for enums with too many options to lay out as segments: the current value plus step
 * buttons. Cheaper than a dropdown and keeps the panel one row tall per control.
 */
@Composable
private fun <T> ChromeOptionCycler(
    options: List<T>,
    selected: T,
    onSelect: (T) -> Unit,
    label: (T) -> String,
) {
    val index = options.indexOf(selected).coerceAtLeast(0)

    Row(
        horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ChromeButton(
            label = "‹",
            onClick = { onSelect(options[(index - 1 + options.size) % options.size]) },
            tone = ChromeButtonTone.Outline,
        )
        ChromeText(text = label(selected), role = ChromeTextRole.Label, maxLines = 1)
        ChromeButton(
            label = "›",
            onClick = { onSelect(options[(index + 1) % options.size]) },
            tone = ChromeButtonTone.Outline,
        )
    }
}
