package dev.voir.areca.playground.charts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import dev.voir.areca.charts.BarChart
import dev.voir.areca.charts.BarChartBar
import dev.voir.areca.charts.BarChartBarWidth
import dev.voir.areca.charts.BarChartColor
import dev.voir.areca.charts.BarChartFill
import dev.voir.areca.charts.BarChartGroup
import dev.voir.areca.charts.BarChartHorizontalAlignment
import dev.voir.areca.charts.BarChartOptions
import dev.voir.areca.charts.BarChartShape
import dev.voir.areca.charts.BarChartSize
import dev.voir.areca.charts.BarChartStyle
import dev.voir.areca.charts.BarChartYAxisLabel
import dev.voir.areca.charts.StackedBarChart
import dev.voir.areca.charts.StackedBarChartSize
import dev.voir.areca.charts.StackedBarSegment
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme

private val Quarters = listOf(
    BarChartGroup(label = "Q1", bars = listOf(BarChartBar(value = 42f), BarChartBar(value = 28f))),
    BarChartGroup(label = "Q2", bars = listOf(BarChartBar(value = 58f), BarChartBar(value = 36f))),
    BarChartGroup(label = "Q3", bars = listOf(BarChartBar(value = 71f), BarChartBar(value = 44f))),
    BarChartGroup(label = "Q4", bars = listOf(BarChartBar(value = 64f), BarChartBar(value = 52f))),
)

private val YLabels = listOf(
    BarChartYAxisLabel(value = 0f, label = "0"),
    BarChartYAxisLabel(value = 40f, label = "40"),
    BarChartYAxisLabel(value = 80f, label = "80"),
)

/** A step of [BarChartSize], nameable outside composition so a control can pick it. */
private enum class SizeStep { Small, Medium, Large }

/** The [BarChartSize] this step names. */
private val SizeStep.barChartSize: BarChartSize
    @Composable
    get() = when (this) {
        SizeStep.Small -> BarChartSize.Small
        SizeStep.Medium -> BarChartSize.Medium
        SizeStep.Large -> BarChartSize.Large
    }

/** Showcase for [BarChart] and [StackedBarChart]. */
@Composable
fun BarChartShowcase() = ShowcaseScaffold(
    title = "Bar chart",
    summary = "Grouped vertical bars, and the single horizontal stacked bar for part-of-whole.",
    module = "charts",
) {
    Playground {
        val size by enumControl(
            key = "size",
            default = SizeStep.Medium,
            options = SizeStep.entries,
            optionLabel = { it.name },
        )
        val color by enumControl(
            key = "color",
            default = BarChartColor.Brand,
            options = BarChartColor.entries,
            optionLabel = { it.name },
        )
        val shape by enumControl(
            key = "shape",
            default = BarChartShape.Rounded,
            options = BarChartShape.entries,
            optionLabel = { it.name },
        )
        val showYAxis by booleanControl("showYAxis", default = true)
        val showGuides by booleanControl("showYGuides", default = true)
        val showTrack by booleanControl(
            key = "showBarTrack",
            default = true,
            description = "The faint full-height track behind each bar.",
        )

        preview {
            BarChart(
                groups = Quarters,
                modifier = Modifier.fillMaxWidth(),
                style = BarChartStyle.of(color),
                size = size.barChartSize,
                yAxisLabels = if (showYAxis) YLabels else emptyList(),
                options = BarChartOptions(
                    showYAxis = showYAxis,
                    showYGuides = showGuides,
                    showBarTrack = showTrack,
                    shape = shape,
                ),
            )
        }
    }

    Variants(
        title = "Colors",
        description = "Each family draws its bars from its solid fill and its tracks from its tint.",
        layout = VariantLayout.Vertical,
    ) {
        for (color in BarChartColor.entries) {
            variant(name = color.name) {
                BarChart(
                    groups = Quarters,
                    modifier = Modifier.fillMaxWidth(),
                    style = BarChartStyle.of(color),
                    yAxisLabels = YLabels,
                )
            }
        }
    }

    Variants(
        title = "Sizes",
        description = "Size drives bar width, spacing and the chart's own height.",
        layout = VariantLayout.Vertical,
    ) {
        for (size in SizeStep.entries) {
            variant(name = size.name) {
                BarChart(
                    groups = Quarters,
                    modifier = Modifier.fillMaxWidth(),
                    size = size.barChartSize,
                    yAxisLabels = YLabels,
                )
            }
        }
    }

    Variants(
        title = "Stacked bar",
        description = "A single bar split into parts. Fractions should sum to at most one.",
        layout = VariantLayout.Vertical,
    ) {
        variant("Three parts") {
            StackedBarChart(
                segments = listOf(
                    StackedBarSegment(fraction = 0.5f),
                    StackedBarSegment(fraction = 0.3f),
                    StackedBarSegment(fraction = 0.1f),
                ),
                modifier = Modifier.fillMaxWidth(),
            )
        }
        variant("Custom colors") {
            StackedBarChart(
                segments = listOf(
                    StackedBarSegment(fraction = 0.6f, color = ArecaTheme.colors.bgSuccessBase),
                    StackedBarSegment(fraction = 0.25f, color = ArecaTheme.colors.bgWarnBase),
                    StackedBarSegment(fraction = 0.15f, color = ArecaTheme.colors.bgDangerBase),
                ),
                modifier = Modifier.fillMaxWidth(),
            )
        }
        variant("Large", detail = "StackedBarChartSize.Large") {
            StackedBarChart(
                segments = listOf(
                    StackedBarSegment(fraction = 0.7f),
                    StackedBarSegment(fraction = 0.2f),
                ),
                modifier = Modifier.fillMaxWidth(),
                size = StackedBarChartSize.Large,
            )
        }
    }

    Recipes {
        recipe(
            name = "Revenue by quarter",
            description = "Two bars per group — this year against last — with a labelled axis.",
        ) {
            BarChart(
                groups = Quarters,
                modifier = Modifier.fillMaxWidth(),
                style = BarChartStyle.of(BarChartColor.Brand),
                yAxisLabels = YLabels,
            )
        }
        recipe(
            name = "Storage breakdown",
            description = "Stacked bar as a part-of-whole summary above a legend.",
        ) {
            StackedBarChart(
                segments = listOf(
                    StackedBarSegment(fraction = 0.42f, color = ArecaTheme.colors.bgBrandBase),
                    StackedBarSegment(fraction = 0.28f, color = ArecaTheme.colors.bgInfoBase),
                    StackedBarSegment(fraction = 0.12f, color = ArecaTheme.colors.bgWarnBase),
                ),
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }

    EdgeCases {
        case(
            name = "Single group, single bar",
            expectation = "Bar respects the alignment option rather than stretching the width.",
        ) {
            BarChart(
                groups = listOf(BarChartGroup(label = "Total", bars = listOf(BarChartBar(60f)))),
                modifier = Modifier.fillMaxWidth(),
                yAxisLabels = YLabels,
            )
        }
        case(
            name = "All zero values",
            expectation = "Track and axis still render; no bar has height.",
        ) {
            BarChart(
                groups = listOf(
                    BarChartGroup(label = "Q1", bars = listOf(BarChartBar(0f))),
                    BarChartGroup(label = "Q2", bars = listOf(BarChartBar(0f))),
                ),
                modifier = Modifier.fillMaxWidth(),
                yAxisLabels = YLabels,
            )
        }
        case(
            name = "Fractions summing above one",
            expectation = "Segments are normalised down to fit rather than overflowing.",
        ) {
            StackedBarChart(
                segments = listOf(
                    StackedBarSegment(fraction = 0.8f),
                    StackedBarSegment(fraction = 0.6f),
                ),
                modifier = Modifier.fillMaxWidth(),
            )
        }
        case(
            name = "Negative fractions",
            expectation = "A negative segment draws nothing and does not count toward the total, " +
                "so the other two still fill the bar half and half.",
        ) {
            StackedBarChart(
                segments = listOf(
                    StackedBarSegment(fraction = 0.8f),
                    StackedBarSegment(fraction = -0.4f),
                    StackedBarSegment(fraction = 0.8f),
                ),
                modifier = Modifier.fillMaxWidth(),
            )
        }
        case(
            name = "Long and missing labels",
            expectation = "Bar labels stop at two lines and group labels at one, with an " +
                "ellipsis. With no bar label anywhere the bar-label row is left out.",
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xl)) {
                BarChart(
                    groups = listOf(
                        BarChartGroup(
                            label = "A group label far too long for its two bars",
                            bars = listOf(
                                BarChartBar(30f, label = "An unusually long bar label that wraps"),
                                BarChartBar(50f),
                            ),
                        ),
                        BarChartGroup(label = "Q2", bars = listOf(BarChartBar(40f), BarChartBar(20f))),
                    ),
                    modifier = Modifier.fillMaxWidth(),
                )
                BarChart(
                    groups = Quarters,
                    modifier = Modifier.fillMaxWidth(),
                    size = BarChartSize.Small,
                )
            }
        }
        case(
            name = "Unbounded width",
            expectation = "Inside a horizontal scroll the chart takes its bars' natural width " +
                "instead of failing to measure.",
        ) {
            BarChart(
                groups = Quarters + Quarters,
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                yAxisLabels = YLabels,
            )
        }
        case(
            name = "Right to left",
            expectation = "Bars start beside the y axis on the right, the gradient's first colour " +
                "sits at that edge, and the stacked bar's first segment starts on the right.",
        ) {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xl)) {
                    BarChart(
                        groups = BarChartGroup.singleBars(
                            listOf(42f, 58f, 71f, 64f).mapIndexed { index, value ->
                                BarChartBar(
                                    value = value,
                                    label = "Q${index + 1}",
                                    fill = BarChartFill.Gradient(
                                        Brush.horizontalGradient(
                                            listOf(
                                                ArecaTheme.colors.bgBrandBase,
                                                ArecaTheme.colors.bgInfoBase,
                                            ),
                                        ),
                                    ),
                                )
                            },
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        yAxisLabels = YLabels,
                        options = BarChartOptions(
                            horizontalAlignment = BarChartHorizontalAlignment.Start,
                            barWidth = BarChartBarWidth.Fixed(32.dp),
                        ),
                    )
                    StackedBarChart(
                        segments = listOf(
                            StackedBarSegment(fraction = 0.5f),
                            StackedBarSegment(fraction = 0.3f),
                            StackedBarSegment(fraction = 0.1f),
                        ),
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
        case(
            name = "No axis or guides",
            expectation = "A bare sparkline-style chart; every chrome element is optional.",
        ) {
            BarChart(
                groups = Quarters,
                modifier = Modifier.fillMaxWidth().height(120.dp),
                options = BarChartOptions(
                    showXAxis = false,
                    showYAxis = false,
                    showYGuides = false,
                    showBarTrack = false,
                    contentPadding = PaddingValues(0.dp),
                ),
            )
        }
    }
}
