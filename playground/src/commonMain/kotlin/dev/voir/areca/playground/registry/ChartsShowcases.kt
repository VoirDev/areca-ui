package dev.voir.areca.playground.registry

import dev.voir.areca.playground.charts.BarChartShowcase
import dev.voir.areca.playground.charts.ColumnChartShowcase
import dev.voir.areca.playground.charts.LineChartShowcase
import dev.voir.areca.playground.charts.MeterBarShowcase

/**
 * Component pages for `:ui:charts`, in the [ShowcaseGroup.Charts] group.
 *
 * Source order is for reading only: the sidebar sorts each section by title.
 */
internal val ChartsShowcases: List<ShowcaseEntry> = listOf(
    ShowcaseEntry(
        id = "charts/bar-chart",
        group = ShowcaseGroup.Charts,
        title = "Bar chart",
        summary = "Grouped vertical bars, and the single horizontal stacked bar for part-of-whole.",
        keywords = listOf("chart", "graph", "bar", "stacked", "analytics", "visualisation"),
        content = { BarChartShowcase() },
    ),
    ShowcaseEntry(
        id = "charts/column-chart",
        group = ShowcaseGroup.Charts,
        title = "Column chart",
        summary = "Periods as columns of bars that scroll, open on the latest and select like tabs.",
        keywords = listOf("chart", "period", "month", "selector", "scrolling", "stacked", "history", "analytics"),
        content = { ColumnChartShowcase() },
    ),
    ShowcaseEntry(
        id = "charts/line-chart",
        group = ShowcaseGroup.Charts,
        title = "Line chart",
        summary = "Lines across a run of steps — a running total against another — with a rule and a bubble.",
        keywords = listOf("chart", "line", "running total", "trend", "pace", "tooltip", "area", "compare"),
        content = { LineChartShowcase() },
    ),
    ShowcaseEntry(
        id = "charts/meter-bar",
        group = ShowcaseGroup.Charts,
        title = "Meter bar",
        summary = "One amount against a track, with an optional marker for a usual amount or a target.",
        keywords = listOf("meter", "bar", "target", "usual", "budget", "progress", "tick", "legend"),
        content = { MeterBarShowcase() },
    ),
)
