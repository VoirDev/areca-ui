package dev.voir.areca.playground.charts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import dev.voir.areca.charts.LineChart
import dev.voir.areca.charts.LineChartMarker
import dev.voir.areca.charts.LineChartSeries
import dev.voir.areca.charts.LineChartStroke
import dev.voir.areca.charts.LineChartTooltip
import dev.voir.areca.charts.LineChartXLabel
import dev.voir.areca.charts.LineChartYLabel
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.theme.ArecaTheme

/** Running totals of a made-up month's spending, one per day. */
private fun runningTotal(days: Int, daily: List<Float>): List<Float> {
    var total = 0f
    return List(days) { day -> total += daily[day % daily.size]; total }
}

private val DailySpend = listOf(84f, 0f, 32f, 141f, 12f, 0f, 260f, 46f, 18f, 0f, 75f, 120f, 9f, 64f, 38f)
private val LastMonth = runningTotal(30, DailySpend.reversed())
private val ThisMonth = runningTotal(23, DailySpend)

private val XLabels = listOf(LineChartXLabel(0, "1 Oct"), LineChartXLabel(14, "15"), LineChartXLabel(29, "30"))
private val YLabels = listOf(LineChartYLabel(0f, "0"), LineChartYLabel(1000f, "€1k"), LineChartYLabel(2000f, "€2k"))

/** Showcase for [LineChart]. */
@Composable
fun LineChartShowcase() = ShowcaseScaffold(
    title = "Line chart",
    summary = "Lines across a run of steps — a running total against another — with a rule and a bubble.",
    module = "charts",
) {
    Playground {
        val today by numberControl("today", default = 22f, range = 0f..29f, step = 1f, format = { "day ${it.toInt() + 1}" })
        val area by booleanControl("area", default = true, description = "Shade under this month's line.")
        val comparison by booleanControl("comparison", default = true, description = "Last month, dashed.")
        val bubble by booleanControl("tooltip", default = true)

        preview {
            val day = today.toInt()
            val current = runningTotal(day + 1, DailySpend)
            LineChart(
                series = listOfNotNull(
                    LineChartSeries(
                        points = LastMonth,
                        label = "September",
                        stroke = LineChartStroke.Dashed,
                        endMarker = null,
                    ).takeIf { comparison },
                    LineChartSeries(points = current, label = "October", color = ArecaTheme.colors.bgDangerBase, area = area),
                ),
                modifier = Modifier.fillMaxWidth(),
                xLabels = XLabels,
                yLabels = YLabels,
                rule = day,
                tooltip = if (bubble) {
                    LineChartTooltip(series = if (comparison) 1 else 0, index = day, text = "€${current.last().toInt()} so far")
                } else {
                    null
                },
                maxValue = 2000f,
            )
        }
    }

    Recipes {
        recipe(
            name = "Through the month",
            description = "This month so far, shaded, against last month dashed with a hollow dot at today.",
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md)) {
                LineChart(
                    series = listOf(
                        LineChartSeries(LastMonth, "September", color = ArecaTheme.colors.fgMuted, stroke = LineChartStroke.Dashed, endMarker = null),
                        LineChartSeries(LastMonth.take(23), "September by today", color = ArecaTheme.colors.fgMuted, stroke = LineChartStroke.Dashed, endMarker = LineChartMarker.Hollow),
                        LineChartSeries(ThisMonth, "October", color = ArecaTheme.colors.bgDangerBase, area = true),
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    xLabels = XLabels,
                    yLabels = YLabels,
                    rule = 22,
                    tooltip = LineChartTooltip(series = 2, index = 22, text = "€${ThisMonth.last().toInt()} by the 23rd"),
                    maxValue = 2000f,
                )
                ChartLegend(
                    items = listOf(
                        ChartLegendItem("October", ChartSwatch.Line(ArecaTheme.colors.bgDangerBase)),
                        ChartLegendItem("September", ChartSwatch.DashedLine(ArecaTheme.colors.fgMuted)),
                    ),
                )
            }
        }
    }

    EdgeCases {
        case(name = "No data yet", expectation = "Grid and labels draw; no line, marker or bubble.") {
            LineChart(
                series = listOf(LineChartSeries(List(30) { null }, "October")),
                modifier = Modifier.fillMaxWidth(),
                xLabels = XLabels,
                yLabels = YLabels,
                tooltip = LineChartTooltip(0, 0, "Nothing yet"),
                maxValue = 2000f,
            )
        }
        case(name = "Bubble at the edge", expectation = "The bubble moves inward rather than leaving the plot.") {
            LineChart(
                series = listOf(LineChartSeries(LastMonth, "September")),
                modifier = Modifier.fillMaxWidth(),
                tooltip = LineChartTooltip(0, 29, "€${LastMonth.last().toInt()} in September"),
            )
        }
        case(name = "Long labels in a narrow chart", expectation = "Constrained to 200dp: labels stay on one line and the bubble stays inside.") {
            LineChart(
                series = listOf(LineChartSeries(ThisMonth, "October")),
                modifier = Modifier.width(200.dp),
                xLabels = listOf(LineChartXLabel(0, "1 October 2026"), LineChartXLabel(29, "30 October 2026")),
                yLabels = listOf(LineChartYLabel(0f, "€0"), LineChartYLabel(2000f, "€2,000.00")),
                tooltip = LineChartTooltip(0, 22, "€${ThisMonth.last().toInt()} spent by the 23rd of the month"),
                maxValue = 2000f,
            )
        }
        case(name = "A single point", expectation = "A run of one step draws a dot, not nothing.") {
            LineChart(
                series = listOf(LineChartSeries(listOf(4f, null, 6f, null, 5f), "Readings", endMarker = null)),
                modifier = Modifier.fillMaxWidth(),
            )
        }
        case(name = "Gap in the data", expectation = "The line breaks where a day has no value.") {
            LineChart(
                series = listOf(LineChartSeries(listOf(1f, 3f, 2f, null, null, 4f, 6f, 5f), "Readings")),
                modifier = Modifier.fillMaxWidth(),
            )
        }
        case(name = "Right to left", expectation = "Day one sits on the right and the y labels on the left.") {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                LineChart(
                    series = listOf(LineChartSeries(ThisMonth, "October", area = true)),
                    modifier = Modifier.fillMaxWidth(),
                    xLabels = XLabels,
                    yLabels = YLabels,
                    rule = 22,
                    maxValue = 2000f,
                )
            }
        }
    }
}
