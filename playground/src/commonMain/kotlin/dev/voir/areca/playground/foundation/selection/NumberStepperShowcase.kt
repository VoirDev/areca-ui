package dev.voir.areca.playground.foundation.selection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.voir.areca.foundation.selection.NumberStepper
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.theme.ArecaTheme

/** Showcase for [NumberStepper]. */
@Composable
fun NumberStepperShowcase() = ShowcaseScaffold(
    title = "Number stepper",
    summary = "Increment and decrement a bounded integer without a keyboard.",
    module = "foundation",
) {
    Playground {
        val minimum by numberControl(
            key = "minValue",
            default = 0f,
            range = -10f..10f,
            step = 1f,
            format = { it.toInt().toString() },
        )
        val maximum by numberControl(
            key = "maxValue",
            default = 10f,
            range = 1f..50f,
            step = 1f,
            format = { it.toInt().toString() },
        )
        val enabled by booleanControl("enabled", default = true)

        preview {
            var value by remember { mutableStateOf(1) }

            NumberStepper(
                value = value,
                onValueChange = { value = it },
                enabled = enabled,
                minValue = minimum.toInt(),
                maxValue = maximum.toInt(),
            )
        }
    }

    States(layout = VariantLayout.Grid) {
        variant("Enabled") {
            var value by remember { mutableStateOf(3) }
            NumberStepper(value = value, onValueChange = { value = it })
        }
        variant("Disabled") {
            NumberStepper(value = 3, onValueChange = {}, enabled = false)
        }
        variant("At the minimum", detail = "decrement is refused") {
            var value by remember { mutableStateOf(0) }
            NumberStepper(value = value, onValueChange = { value = it }, minValue = 0, maxValue = 5)
        }
        variant("At the maximum", detail = "increment is refused") {
            var value by remember { mutableStateOf(5) }
            NumberStepper(value = value, onValueChange = { value = it }, minValue = 0, maxValue = 5)
        }
    }

    Recipes {
        recipe(
            name = "Quantity row",
            description = "A stepper beside the thing it counts.",
        ) {
            var quantity by remember { mutableStateOf(1) }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = "Standard licence", variant = TextVariant.BodyMedium)
                NumberStepper(
                    value = quantity,
                    onValueChange = { quantity = it },
                    minValue = 1,
                    maxValue = 20,
                )
            }
        }
        recipe(
            name = "With a computed total",
            description = "The stepper owns nothing but the number; the caller derives the rest.",
        ) {
            var seats by remember { mutableStateOf(3) }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xl),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                NumberStepper(
                    value = seats,
                    onValueChange = { seats = it },
                    minValue = 1,
                    maxValue = 50,
                )
                Text(
                    text = "$seats seats — $${seats * 12}/month",
                    variant = TextVariant.BodySmall,
                    color = TextColor.Subtle,
                )
            }
        }
    }

    EdgeCases {
        case(
            name = "Negative range",
            expectation = "Bounds can straddle zero; the value is clamped, not sign-checked.",
        ) {
            var value by remember { mutableStateOf(0) }
            NumberStepper(
                value = value,
                onValueChange = { value = it },
                minValue = -5,
                maxValue = 5,
            )
        }
        case(
            name = "Single-value range",
            expectation = "Both buttons refuse; the stepper is effectively read-only.",
        ) {
            NumberStepper(value = 7, onValueChange = {}, minValue = 7, maxValue = 7)
        }
        case(
            name = "Large number",
            expectation = "The value area grows with the digits rather than clipping.",
        ) {
            var value by remember { mutableStateOf(99999) }
            NumberStepper(value = value, onValueChange = { value = it })
        }
    }
}
