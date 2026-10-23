package dev.voir.areca.playground.charts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import dev.voir.areca.charts.ChartLegend
import dev.voir.areca.charts.ChartLegendItem
import dev.voir.areca.charts.ChartSwatch
import dev.voir.areca.charts.MeterBar
import dev.voir.areca.charts.MeterBarSize
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.Sizes
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme

/** A step of [MeterBarSize], nameable outside composition so a control can pick it. */
private enum class MeterStep { Slim, Medium, Large }

/** The [MeterBarSize] this step names. */
private val MeterStep.meterBarSize: MeterBarSize
    get() = when (this) {
        MeterStep.Slim -> MeterBarSize.Slim
        MeterStep.Medium -> MeterBarSize.Medium
        MeterStep.Large -> MeterBarSize.Large
    }

/** Showcase for [MeterBar]. */
@Composable
fun MeterBarShowcase() = ShowcaseScaffold(
    title = "Meter bar",
    summary = "One amount against a track, with an optional marker for a usual amount or a target.",
    module = "charts",
) {
    Playground {
        val fraction by numberControl("fraction", default = 0.62f, range = 0f..1f, step = 0.01f)
        val showMarker by booleanControl("marker", default = true, description = "The reference tick.")
        val marker by numberControl("marker at", default = 0.55f, range = 0f..1f, step = 0.01f)
        val size by enumControl(
            key = "size",
            default = MeterStep.Medium,
            options = MeterStep.entries,
            optionLabel = { it.name },
        )

        preview {
            MeterBar(
                fraction = fraction,
                marker = marker.takeIf { showMarker },
                size = size.meterBarSize,
                modifier = Modifier.fillMaxWidth(),
                contentDescription = "Food and dining, 62% of the largest, usual 55%",
            )
        }
    }

    Variants(
        title = "Colours",
        description = "The fill takes the row's own colour; track and marker stay the style's.",
        layout = VariantLayout.Vertical,
    ) {
        variant("Default", detail = "MeterBarStyle.fill") {
            MeterBar(fraction = 0.7f, marker = 0.6f, modifier = Modifier.fillMaxWidth())
        }
        variant("Info") {
            MeterBar(
                fraction = 0.4f,
                marker = 0.45f,
                color = ArecaTheme.colors.bgInfoBase,
                modifier = Modifier.fillMaxWidth(),
            )
        }
        variant("Warning") {
            MeterBar(
                fraction = 0.85f,
                marker = 0.5f,
                color = ArecaTheme.colors.bgWarnIntense,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }

    Sizes(layout = VariantLayout.Vertical) {
        variant(name = "Slim", detail = "4dp, for a nested row") {
            MeterBar(fraction = 0.5f, marker = 0.4f, size = MeterBarSize.Slim, modifier = Modifier.fillMaxWidth())
        }
        variant(name = "Medium", detail = "6dp, the default") {
            MeterBar(fraction = 0.5f, marker = 0.4f, size = MeterBarSize.Medium, modifier = Modifier.fillMaxWidth())
        }
        variant(name = "Large", detail = "10dp, for two totals compared") {
            MeterBar(fraction = 0.5f, size = MeterBarSize.Large, modifier = Modifier.fillMaxWidth())
        }
    }

    Recipes {
        recipe(
            name = "Category against its usual",
            description = "Name and amount over the bar, what it is and the difference under it.",
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Food and dining", variant = TextVariant.LabelMedium)
                    Text("€612", variant = TextVariant.LabelMedium)
                }
                MeterBar(
                    fraction = 0.66f,
                    marker = 0.6f,
                    color = ArecaTheme.colors.bgDangerBase,
                    modifier = Modifier.fillMaxWidth(),
                )
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("26% of spending · 39 payments", variant = TextVariant.BodyExtraSmall, color = TextColor.Subtle)
                    Text("+11% on usual", variant = TextVariant.BodyExtraSmall)
                }
                ChartLegend(
                    items = listOf(
                        ChartLegendItem("October", ChartSwatch.Line(ArecaTheme.colors.bgDangerBase)),
                        ChartLegendItem("usual", ChartSwatch.Tick(ArecaTheme.colors.fgBase)),
                    ),
                )
            }
        }
        recipe(
            name = "Two totals compared",
            description = "Large bars on one scale: this period against the one it is compared with.",
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg)) {
                MeterBar(fraction = 1f, size = MeterBarSize.Large, modifier = Modifier.fillMaxWidth())
                MeterBar(
                    fraction = 0.8f,
                    size = MeterBarSize.Large,
                    color = ArecaTheme.colors.bgNeutralIntense,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }

    EdgeCases {
        case(
            name = "Empty and full",
            expectation = "Zero draws only the track; one fills it; a marker at either end stays inside.",
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg)) {
                MeterBar(fraction = 0f, marker = 0f, modifier = Modifier.fillMaxWidth())
                MeterBar(fraction = 1f, marker = 1f, modifier = Modifier.fillMaxWidth())
            }
        }
        case(
            name = "Out of range and not a number",
            expectation = "1.4 clamps to full; NaN draws no fill and no marker.",
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg)) {
                MeterBar(fraction = 1.4f, marker = -0.2f, modifier = Modifier.fillMaxWidth())
                MeterBar(fraction = Float.NaN, marker = Float.NaN, modifier = Modifier.fillMaxWidth())
            }
        }
        case(name = "Tiny amount", expectation = "A sliver still shows as a round dot, not nothing.") {
            MeterBar(fraction = 0.002f, marker = 0.3f, modifier = Modifier.fillMaxWidth())
        }
        case(name = "Narrow container", expectation = "Constrained to 80dp; the marker keeps its place.") {
            MeterBar(fraction = 0.5f, marker = 0.75f, modifier = Modifier.width(80.dp))
        }
        case(name = "Right to left", expectation = "The fill grows from the right and the marker mirrors.") {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                MeterBar(fraction = 0.3f, marker = 0.6f, modifier = Modifier.fillMaxWidth())
            }
        }
    }
}
