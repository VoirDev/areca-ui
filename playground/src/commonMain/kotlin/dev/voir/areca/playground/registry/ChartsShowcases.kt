package dev.voir.areca.playground.registry

import dev.voir.areca.playground.charts.BarChartShowcase

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
)
