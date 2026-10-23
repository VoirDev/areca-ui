package dev.voir.areca.playground.charts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import dev.voir.areca.charts.ChartLegend
import dev.voir.areca.charts.ChartLegendItem
import dev.voir.areca.charts.ChartSwatch
import dev.voir.areca.charts.ColumnChart
import dev.voir.areca.charts.ColumnChartBar
import dev.voir.areca.charts.ColumnChartColumn
import dev.voir.areca.charts.ColumnChartLayout
import dev.voir.areca.charts.ColumnChartReferenceLine
import dev.voir.areca.charts.ColumnChartSegment
import dev.voir.areca.charts.ColumnChartSize
import dev.voir.areca.charts.ColumnChartStyle
import dev.voir.areca.charts.ColumnChartValueLabels
import dev.voir.areca.charts.rememberColumnChartState
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.IconButton
import dev.voir.areca.foundation.button.IconButtonSize
import dev.voir.areca.foundation.button.IconButtonStyle
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.RefreshCcw
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme
import kotlinx.coroutines.launch

private val MonthNames = listOf("Nov", "Dec", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct")
private val MonthIncome = listOf(3900f, 4100f, 3800f, 3950f, 4400f, 3900f, 4000f, 4300f, 3850f, 4600f, 4100f, 4250f)
private val MonthSpent = listOf(2100f, 3050f, 1690f, 1780f, 2170f, 2010f, 1840f, 2530f, 3050f, 2260f, 2090f, 2340f)

/** A whole euro amount with a thousands comma: `€2,340`. */
private fun euros(value: Float): String {
    val whole = value.toInt()
    return if (whole >= 1000) "€${whole / 1000},${(whole % 1000).toString().padStart(3, '0')}" else "€$whole"
}

/** What each column of the showcase charts holds. */
private enum class BarsOption { Single, Paired, Stacked, WithCompanion }

/** A step of [ColumnChartSize], nameable outside composition so a control can pick it. */
private enum class ColumnStep { Small, Medium, Large }

/** The [ColumnChartSize] this step names. */
private val ColumnStep.columnChartSize: ColumnChartSize
    @Composable
    get() = when (this) {
        ColumnStep.Small -> ColumnChartSize.Small
        ColumnStep.Medium -> ColumnChartSize.Medium
        ColumnStep.Large -> ColumnChartSize.Large
    }

/** Twelve months of columns holding [bars], the latest labelled "Now · so far". */
@Composable
private fun monthColumns(bars: BarsOption): List<ColumnChartColumn<Int>> {
    val income = ArecaTheme.colors.bgBrandBase
    val spent = ArecaTheme.colors.bgDangerBase
    return MonthNames.indices.map { i ->
        val latest = i == MonthNames.lastIndex
        ColumnChartColumn(
            key = i,
            bars = when (bars) {
                BarsOption.Single -> listOf(ColumnChartBar.of(MonthSpent[i], spent))
                BarsOption.Paired -> listOf(ColumnChartBar.of(MonthIncome[i], income), ColumnChartBar.of(MonthSpent[i], spent))
                BarsOption.Stacked -> listOf(
                    ColumnChartBar(
                        listOf(
                            ColumnChartSegment(MonthIncome[i] * 0.84f),
                            ColumnChartSegment(MonthIncome[i] * 0.11f),
                            ColumnChartSegment(MonthIncome[i] * 0.05f),
                        ),
                    ),
                )
                BarsOption.WithCompanion -> listOf(
                    ColumnChartBar.of(MonthIncome[i], income),
                    ColumnChartBar.companion(MonthIncome[i] * 0.92f),
                )
            },
            label = if (latest) "Now" else MonthNames[i],
            sublabel = when {
                latest -> "so far"
                MonthNames[i] == "Jan" -> "2026"
                else -> null
            },
            valueLabel = euros(MonthSpent[i]),
            contentDescription = "${MonthNames[i]}: income €${MonthIncome[i].toInt()}, spent €${MonthSpent[i].toInt()}",
        )
    }
}

/** Showcase for [ColumnChart]. */
@Composable
fun ColumnChartShowcase() = ShowcaseScaffold(
    title = "Column chart",
    summary = "Periods as columns of bars that scroll, open on the latest and select like tabs.",
    module = "charts",
) {
    Playground {
        val bars by enumControl("bars", default = BarsOption.Paired, options = BarsOption.entries, optionLabel = { it.name })
        val layout by enumControl(
            key = "layout",
            default = ColumnChartLayout.Scroll,
            options = ColumnChartLayout.entries,
            optionLabel = { it.name },
        )
        val valueLabels by enumControl(
            key = "valueLabels",
            default = ColumnChartValueLabels.Selected,
            options = ColumnChartValueLabels.entries,
            optionLabel = { it.name },
        )
        val size by enumControl("size", default = ColumnStep.Medium, options = ColumnStep.entries, optionLabel = { it.name })
        val reference by booleanControl("referenceLine", default = true, description = "A dashed usual amount.")
        val selectable by booleanControl("onSelect", default = true, description = "Columns pick a period.")

        preview {
            var selected by remember { mutableStateOf(MonthNames.lastIndex) }
            ColumnChart(
                columns = monthColumns(bars),
                selectedKey = selected,
                onSelect = if (selectable) ({ selected = it }) else null,
                referenceLines = if (reference) {
                    listOf(ColumnChartReferenceLine(2240f, "usual spend", ArecaTheme.colors.fgDangerText))
                } else {
                    emptyList()
                },
                valueLabels = valueLabels,
                layout = layout,
                size = size.columnChartSize,
            )
        }
    }

    Variants(
        title = "Bars per column",
        description = "One bar, a pair to compare, a stack of parts, or a bar beside a slim companion.",
        layout = VariantLayout.Vertical,
    ) {
        for (option in BarsOption.entries) {
            variant(option.name) {
                var selected by remember { mutableStateOf(MonthNames.lastIndex) }
                ColumnChart(columns = monthColumns(option), selectedKey = selected, onSelect = { selected = it })
            }
        }
    }

    Variants(
        title = "Sizes",
        description = "Size drives the plot height, column width and bar width.",
        layout = VariantLayout.Vertical,
    ) {
        for (step in ColumnStep.entries) {
            variant(step.name) {
                ColumnChart(
                    columns = monthColumns(BarsOption.Paired),
                    selectedKey = MonthNames.lastIndex,
                    onSelect = {},
                    size = step.columnChartSize,
                )
            }
        }
    }

    Recipes {
        recipe(
            name = "Period picker with back to latest",
            description = "A hoisted state; the button shows once an older period is selected.",
        ) {
            var selected by remember { mutableStateOf(MonthNames.lastIndex) }
            val state = rememberColumnChartState()
            val scope = rememberCoroutineScope()
            Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("${MonthNames[selected]} 2026", variant = TextVariant.HeadingSmall)
                        Text(
                            if (selected == MonthNames.lastIndex) "So far" else "Closed",
                            variant = TextVariant.BodyExtraSmall,
                            color = TextColor.Subtle,
                        )
                    }
                    if (selected != MonthNames.lastIndex) {
                        IconButton(
                            imageVector = ArecaIcons.RefreshCcw,
                            contentDescription = "Back to the latest month",
                            onClick = {
                                selected = MonthNames.lastIndex
                                scope.launch { state.scrollToEnd() }
                            },
                            style = IconButtonStyle.soft(ActionColor.Brand),
                            size = IconButtonSize.Small,
                        )
                    }
                }
                ColumnChart(
                    columns = monthColumns(BarsOption.Paired),
                    selectedKey = selected,
                    onSelect = { selected = it },
                    state = state,
                    referenceLines = listOf(ColumnChartReferenceLine(2240f, "usual spend")),
                )
                ChartLegend(
                    items = listOf(
                        ChartLegendItem("Income", ChartSwatch.Dot(ArecaTheme.colors.bgBrandBase)),
                        ChartLegendItem("Expenses", ChartSwatch.Dot(ArecaTheme.colors.bgDangerBase)),
                        ChartLegendItem("usual spend", ChartSwatch.DashedLine(ArecaTheme.colors.fgMuted)),
                    ),
                )
            }
        }
        recipe(
            name = "Kept each month",
            description = "A static year that fills its width: values over every column, this month highlighted.",
        ) {
            ColumnChart(
                columns = MonthNames.indices.map { i ->
                    val kept = (MonthIncome[i] - MonthSpent[i]) / MonthIncome[i]
                    ColumnChartColumn(
                        key = i,
                        bars = listOf(
                            ColumnChartBar.of(
                                kept,
                                if (i == MonthNames.lastIndex) ArecaTheme.colors.bgBrandBase else ArecaTheme.colors.bgMuted,
                            ),
                        ),
                        label = MonthNames[i].take(1),
                        valueLabel = "${(kept * 100).toInt()}%",
                    )
                },
                selectedKey = MonthNames.lastIndex,
                valueLabels = ColumnChartValueLabels.All,
                layout = ColumnChartLayout.Fill,
                referenceLines = listOf(ColumnChartReferenceLine(0.41f)),
                style = ColumnChartStyle.default(halo = Color.Transparent, dimmedAlpha = 1f),
                size = ColumnChartSize.Small,
            )
        }
        recipe(
            name = "Average day by weekday",
            description = "Seven columns sharing the width, the busiest one highlighted.",
        ) {
            val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
            val values = listOf(48f, 52f, 41f, 60f, 74f, 112f, 88f)
            ColumnChart(
                columns = days.mapIndexed { i, day ->
                    ColumnChartColumn(key = day, bars = listOf(ColumnChartBar.of(values[i])), label = day, valueLabel = "€${values[i].toInt()}")
                },
                selectedKey = "Sat",
                valueLabels = ColumnChartValueLabels.All,
                layout = ColumnChartLayout.Fill,
                size = ColumnChartSize.Small,
            )
        }
    }

    EdgeCases {
        case(
            name = "First month of history",
            expectation = "Placeholder columns draw dashed outlines, cannot be selected and are not read aloud.",
        ) {
            ColumnChart(
                columns = (0 until 6).map { i ->
                    ColumnChartColumn(
                        key = i,
                        bars = listOf(ColumnChartBar.of(listOf(60f, 85f, 50f, 95f, 70f, 40f)[i])),
                        label = if (i == 5) "Now" else "",
                        placeholder = i < 5,
                    )
                },
                selectedKey = 5,
                onSelect = {},
                layout = ColumnChartLayout.Fill,
                size = ColumnChartSize.Small,
            )
        }
        case(name = "All zero", expectation = "Columns and labels stay; no bar is drawn.") {
            ColumnChart(
                columns = MonthNames.take(4).mapIndexed { i, name ->
                    ColumnChartColumn(key = i, bars = listOf(ColumnChartBar.of(0f)), label = name)
                },
                layout = ColumnChartLayout.Fill,
            )
        }
        case(name = "Disabled columns", expectation = "Months still to come draw no bar and cannot be selected.") {
            ColumnChart(
                columns = MonthNames.mapIndexed { i, name ->
                    ColumnChartColumn(
                        key = i,
                        bars = if (i < 9) listOf(ColumnChartBar.of(MonthSpent[i])) else emptyList(),
                        label = name,
                        enabled = i < 9,
                    )
                },
                selectedKey = 8,
                onSelect = {},
            )
        }
        case(name = "Narrow container", expectation = "Constrained to 160dp: scrolls, still opening on the end.") {
            ColumnChart(
                columns = monthColumns(BarsOption.Single),
                modifier = Modifier.width(160.dp),
                selectedKey = MonthNames.lastIndex,
                onSelect = {},
            )
        }
        case(name = "Long labels", expectation = "Labels stop at one line with an ellipsis.") {
            ColumnChart(
                columns = listOf("Groceries and household", "Restaurants", "Transport").mapIndexed { i, name ->
                    ColumnChartColumn(key = i, bars = listOf(ColumnChartBar.of(30f + i * 20f)), label = name)
                },
                layout = ColumnChartLayout.Fill,
                selectedKey = 0,
            )
        }
        case(name = "Right to left", expectation = "The latest column sits at the left end and the chart opens there.") {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                ColumnChart(
                    columns = monthColumns(BarsOption.Paired),
                    modifier = Modifier.fillMaxWidth(),
                    selectedKey = MonthNames.lastIndex,
                    onSelect = {},
                )
            }
        }
    }
}
