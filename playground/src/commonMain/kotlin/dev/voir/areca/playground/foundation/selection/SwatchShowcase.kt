package dev.voir.areca.playground.foundation.selection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dev.voir.areca.foundation.selection.ColorItem
import dev.voir.areca.foundation.selection.ColorShade
import dev.voir.areca.foundation.selection.Swatch
import dev.voir.areca.foundation.selection.SwatchPicker
import dev.voir.areca.foundation.selection.SwatchPickerLayout
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme

/**
 * Showcase for [Swatch] and [SwatchPicker].
 *
 * One page: the picker is a grid of swatches, and neither is useful to look at without the other.
 */
@Composable
fun SwatchShowcase() = ShowcaseScaffold(
    title = "Swatch",
    summary = "A single colour chip, and the grouped picker built from them.",
    module = "foundation",
) {
    Playground {
        val layout by enumControl(
            key = "layout",
            default = SwatchPickerLayout.GroupedColumns,
            options = SwatchPickerLayout.entries,
            optionLabel = { it.name },
        )
        val enabled by booleanControl("enabled", default = true)

        preview {
            var selected by remember { mutableStateOf<ColorItem?>(null) }

            SwatchPicker(
                items = paletteShades(),
                selected = selected,
                onChange = { selected = it },
                modifier = Modifier.fillMaxWidth(),
                layout = layout,
                enabled = enabled,
            )

            Text(
                text = selected?.name ?: "Nothing selected",
                variant = TextVariant.BodySmall,
                color = TextColor.Muted,
            )
        }
    }

    Variants(
        title = "Swatch shapes",
        description = "The chip itself takes any shape and any content.",
        layout = VariantLayout.Grid,
    ) {
        variant("Circle") {
            Swatch(shape = CircleShape) {
                Box(Modifier.fillMaxSize().background(ArecaTheme.colors.bgBrandBase, CircleShape))
            }
        }
        variant("Rounded square") {
            val shape = RoundedCornerShape(ArecaTheme.radius.sm)
            Swatch(shape = shape) {
                Box(Modifier.fillMaxSize().background(ArecaTheme.colors.bgSuccessBase, shape))
            }
        }
        variant("Clickable") {
            Swatch(shape = CircleShape, onClick = {}) {
                Box(Modifier.fillMaxSize().background(ArecaTheme.colors.bgInfoBase, CircleShape))
            }
        }
        variant("Disabled") {
            Swatch(shape = CircleShape, enabled = false, onClick = {}) {
                Box(Modifier.fillMaxSize().background(ArecaTheme.colors.bgDisabled, CircleShape))
            }
        }
    }

    Variants(
        title = "Picker layouts",
        description = "How the shades are arranged. Same data either way.",
        layout = VariantLayout.Vertical,
    ) {
        for (layout in SwatchPickerLayout.entries) {
            variant(name = layout.name) {
                var selected by remember { mutableStateOf<ColorItem?>(null) }
                SwatchPicker(
                    items = paletteShades(),
                    selected = selected,
                    onChange = { selected = it },
                    modifier = Modifier.fillMaxWidth(),
                    layout = layout,
                )
            }
        }
    }

    EdgeCases {
        case(
            name = "Nothing selected",
            expectation = "No chip shows a selection ring; the picker is still usable.",
        ) {
            SwatchPicker(
                items = paletteShades(),
                selected = null,
                onChange = {},
                modifier = Modifier.fillMaxWidth(),
            )
        }
        case(
            name = "Disabled picker",
            expectation = "Every chip refuses input; the current selection stays visible.",
        ) {
            SwatchPicker(
                items = paletteShades(),
                selected = paletteShades().first().items.first(),
                onChange = {},
                modifier = Modifier.fillMaxWidth(),
                enabled = false,
            )
        }
        case(
            name = "Single shade",
            expectation = "One group renders without the grouping affordance looking broken.",
        ) {
            var selected by remember { mutableStateOf<ColorItem?>(null) }
            SwatchPicker(
                items = paletteShades().take(1),
                selected = selected,
                onChange = { selected = it },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun paletteShades(): List<ColorShade> {
    val colors = ArecaTheme.baseColors

    return listOf(
        ColorShade(
            id = "brand",
            name = "Brand",
            items = listOf(
                ColorItem("brand40", "Brand 40", colors.brand50, colors.brand40),
                ColorItem("brand60", "Brand 60", colors.brand70, colors.brand60),
                ColorItem("brand80", "Brand 80", colors.brand90, colors.brand80),
            ),
        ),
        ColorShade(
            id = "green",
            name = "Green",
            items = listOf(
                ColorItem("green40", "Green 40", colors.green50, colors.green40),
                ColorItem("green60", "Green 60", colors.green70, colors.green60),
                ColorItem("green80", "Green 80", colors.green90, colors.green80),
            ),
        ),
        ColorShade(
            id = "red",
            name = "Red",
            items = listOf(
                ColorItem("red40", "Red 40", colors.red50, colors.red40),
                ColorItem("red60", "Red 60", colors.red70, colors.red60),
                ColorItem("red80", "Red 80", colors.red90, colors.red80),
            ),
        ),
    )
}
