package dev.voir.areca.playground.crop

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.voir.areca.crop.CropDial
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme

/**
 * A dial configuration.
 *
 * @property label Name in the control and the cell.
 * @property range Values the dial spans.
 * @property step Value between ticks.
 * @property majorEvery Ticks between major ticks.
 * @property origin Value the dot marks.
 * @property start Value the dial starts at.
 * @property format How the value is written above the dial.
 */
private enum class DialPreset(
    val label: String,
    val range: ClosedFloatingPointRange<Float>,
    val step: Float,
    val majorEvery: Int,
    val origin: Float,
    val start: Float,
    val format: (Float) -> String,
) {
    /** Straightening: ±45°, a tick per degree. */
    Angle("angle", -45f..45f, 1f, 5, 0f, -3.2f, { "${(it * 10).toInt() / 10f}°" }),

    /** Zoom: 1–10×, a tick per tenth. */
    Zoom("zoom", 1f..10f, 0.1f, 10, 1f, 1.8f, { "${(it * 10).toInt() / 10f}×" }),
}

/** Showcase for [CropDial]. */
@Composable
fun CropDialShowcase() = ShowcaseScaffold(
    title = "Crop dial",
    summary = "A ruler that slides under a fixed mark, for straightening and zooming a photo with one " +
        "finger, the keyboard, or assistive technology.",
    module = "crop",
    status = ShowcaseStatus.Beta,
) {
    Playground(description = "Drag the ticks, or focus the dial and use the arrow keys; Home returns to the dot.") {
        val preset by enumControl(
            key = "preset",
            default = DialPreset.Angle,
            options = DialPreset.entries,
            optionLabel = { it.label },
        )

        preview { DialWithValue(preset) }
    }

    Variants(
        title = "Configurations",
        description = "The two dials the crop toolbar switches between.",
        layout = VariantLayout.Vertical,
    ) {
        DialPreset.entries.forEach { preset ->
            variant(name = preset.label, detail = "${preset.range.start}…${preset.range.endInclusive}, tick ${preset.step}") {
                DialWithValue(preset)
            }
        }
    }

    EdgeCases {
        case("At the end of the range", "Ticks stop at the last value; the ruler does not wrap.") {
            DialWithValue(DialPreset.Angle, start = 45f)
        }
        case("Narrow", "Fewer ticks show, fading sooner towards the ends.") {
            DialWithValue(DialPreset.Zoom, width = 180)
        }
    }
}

/**
 * A dial with its value written above it, as the crop toolbar shows one.
 *
 * @param preset Configuration.
 * @param start Starting value, or the preset's own.
 * @param width Width of the dial, in dp.
 */
@Composable
private fun DialWithValue(preset: DialPreset, start: Float = preset.start, width: Int = 340) {
    var value by remember(preset, start) { mutableFloatStateOf(start) }
    Column(
        modifier = Modifier.width(width.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xs),
    ) {
        Text(
            text = preset.format(value),
            style = ArecaTheme.typography.labelMedium,
            color = ArecaTheme.colors.fgSubtle,
        )
        CropDial(
            value = value,
            onValueChange = { value = it },
            valueRange = preset.range,
            contentDescription = preset.label,
            tickStep = preset.step,
            majorTickEvery = preset.majorEvery,
            origin = preset.origin,
        )
    }
}
