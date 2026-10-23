# Analytics overview

A tab that reads a spending history and lets the reader pick one period of it. A segmented switch
picks the step; the period's figures sit above a column chart that is also the picker — tapping a
column selects it, and every section below follows the selection, except "your usual month", which
stays fixed so it can be compared against. Each category opens to its subcategories.

Needs `dev.voir.areca:foundation`, `dev.voir.areca:charts`, and `dev.voir.areca:icons` for the
"back to latest" glyph.

## Components

| Component | Module | Role here |
| --- | --- | --- |
| `Tabs` with `TabsVariant.Segmented` | foundation | The week, month or year switch |
| `Stat` and `StatGrid` | foundation | Figures with their labels, in equal columns |
| `StackedBarChart` | charts | The period's spending against what was kept |
| `ColumnChart`, `ColumnChartColumn`, `ColumnChartBar`, `ColumnChartReferenceLine` | charts | Every period as a column of income and spending, with the usual spend as a line. Selecting a column is the picker |
| `ChartLegend` | charts | What the colours and the line stand for |
| `IconButton` | foundation | "Back to latest", shown only when an older period is selected |
| `ExpandableListItem` with `ExpandableListItemContainer.Soft` | foundation | A category that opens to its subcategories |
| `MeterBar` | charts | A category's share, with a marker at its usual share |
| `ListItem` | foundation | A subcategory |

Foundation components are in the [catalogue](../components.md); charts in
[ui/charts](../../ui/charts/README.md).

## Code

```kotlin
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.voir.areca.charts.ChartLegend
import dev.voir.areca.charts.ChartLegendItem
import dev.voir.areca.charts.ChartSwatch
import dev.voir.areca.charts.ColumnChart
import dev.voir.areca.charts.ColumnChartBar
import dev.voir.areca.charts.ColumnChartColumn
import dev.voir.areca.charts.ColumnChartReferenceLine
import dev.voir.areca.charts.MeterBar
import dev.voir.areca.charts.StackedBarChart
import dev.voir.areca.charts.StackedBarChartSize
import dev.voir.areca.charts.StackedBarSegment
import dev.voir.areca.charts.rememberColumnChartState
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.IconButton
import dev.voir.areca.foundation.button.IconButtonSize
import dev.voir.areca.foundation.button.IconButtonStyle
import dev.voir.areca.foundation.list.ExpandableListItem
import dev.voir.areca.foundation.list.ExpandableListItemContainer
import dev.voir.areca.foundation.list.ListItem
import dev.voir.areca.foundation.stat.Stat
import dev.voir.areca.foundation.stat.StatGrid
import dev.voir.areca.foundation.tabs.TabItem
import dev.voir.areca.foundation.tabs.Tabs
import dev.voir.areca.foundation.tabs.TabsSize
import dev.voir.areca.foundation.tabs.TabsVariant
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.RefreshCcw
import dev.voir.areca.theme.ArecaTheme
```

```kotlin
/** One period as the presentation layer hands it over: already summed, labelled and worded. */
@Immutable
data class PeriodSummary(
    val key: String,
    val label: String,
    val sublabel: String?,
    val title: String,
    val income: Float,
    val spent: Float,
    val description: String,
)

/** One category of the selected period, against its usual amount. */
@Immutable
data class CategoryShare(
    val name: String,
    val amount: String,
    val detail: String,
    val share: Float,
    val usualShare: Float,
    val usualAmount: String,
    val subcategories: List<Pair<String, String>>,
)

/**
 * The stateless overview: a period switch, the selected period's figures, the period chart that
 * picks it, the fixed usual figures, and the categories that follow the selection.
 */
@Composable
fun AnalyticsOverview(
    periods: List<PeriodSummary>,
    selectedKey: String,
    onSelect: (String) -> Unit,
    usualSpend: Float,
    usual: List<Triple<String, String, String>>,
    categories: List<CategoryShare>,
    modifier: Modifier = Modifier,
) {
    val chart = rememberColumnChartState()
    val latest = periods.last()
    val period = periods.first { it.key == selectedKey }
    val income = ArecaTheme.colors.bgBrandBase
    val spent = ArecaTheme.colors.bgDangerBase
    var step by remember { mutableStateOf("Month") }
    var open by remember { mutableStateOf<String?>(null) }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xl3)) {
        Column(
            modifier = Modifier.padding(horizontal = ArecaTheme.spacing.horizontalContentInsets),
            verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg),
        ) {
            Tabs(
                tabs = listOf("Week", "Month", "Year").map { TabItem(it) { Text(it) } },
                selectedValue = step,
                onValueSelected = { step = it },
                size = TabsSize.Segmented,
                variant = TabsVariant.Segmented,
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(period.title, variant = TextVariant.HeadingSmall, modifier = Modifier.weight(1f))
                if (selectedKey != latest.key) {
                    IconButton(
                        imageVector = ArecaIcons.RefreshCcw,
                        contentDescription = "Back to ${latest.title}",
                        onClick = { onSelect(latest.key) },
                        style = IconButtonStyle.soft(ActionColor.Brand),
                        size = IconButtonSize.Small,
                    )
                }
            }
            StatGrid {
                Stat(label = "Income", value = "€${period.income.toInt()}")
                Stat(label = "Expenses", value = "€${period.spent.toInt()}")
            }
            StackedBarChart(
                segments = listOf(
                    StackedBarSegment(period.spent, spent, "Spent"),
                    StackedBarSegment((period.income - period.spent).coerceAtLeast(0f), income, "Kept"),
                ),
                modifier = Modifier.fillMaxWidth(),
                size = StackedBarChartSize.Tiny,
            )
        }

        // Selecting a column is the whole interaction: the chart scrolls it into view, and the
        // sections below read the selection from the same state.
        Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md)) {
            ColumnChart(
                columns = periods.map { p ->
                    ColumnChartColumn(
                        key = p.key,
                        bars = listOf(ColumnChartBar.of(p.income, income), ColumnChartBar.of(p.spent, spent)),
                        label = p.label,
                        sublabel = p.sublabel,
                        contentDescription = p.description,
                    )
                },
                selectedKey = selectedKey,
                onSelect = onSelect,
                state = chart,
                referenceLines = listOf(
                    ColumnChartReferenceLine(usualSpend, "usual spend", ArecaTheme.colors.fgDangerText),
                ),
                contentPadding = PaddingValues(horizontal = ArecaTheme.spacing.horizontalContentInsets),
            )
            ChartLegend(
                items = listOf(
                    ChartLegendItem("Income", ChartSwatch.Square(income)),
                    ChartLegendItem("Spent", ChartSwatch.Square(spent)),
                    ChartLegendItem("Usual spend", ChartSwatch.DashedLine(ArecaTheme.colors.fgDangerText)),
                ),
                modifier = Modifier.padding(horizontal = ArecaTheme.spacing.horizontalContentInsets),
            )
        }

        Column(
            modifier = Modifier.padding(horizontal = ArecaTheme.spacing.horizontalContentInsets),
            verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg),
        ) {
            Text("Your usual month", variant = TextVariant.HeadingExtraSmall)
            StatGrid {
                usual.forEach { (label, value, note) -> Stat(label = label, value = value, note = note) }
            }
        }

        Column(modifier = Modifier.padding(horizontal = ArecaTheme.spacing.md)) {
            categories.forEach { category ->
                ExpandableListItem(
                    expanded = open == category.name,
                    onExpandedChange = { open = if (it) category.name else null },
                    title = { Text(category.name) },
                    value = { Text(category.amount) },
                    // The meter is part of the row, so a tap on it opens the category too.
                    bottom = {
                        Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md)) {
                            MeterBar(
                                fraction = category.share,
                                marker = category.usualShare,
                                color = spent,
                                contentDescription = "Usual ${category.usualAmount}",
                            )
                            Text(category.detail, variant = TextVariant.BodyExtraSmall, color = TextColor.Subtle)
                        }
                    },
                    showChevron = false,
                    container = ExpandableListItemContainer.Soft,
                    onClickLabel = "Show subcategories",
                ) {
                    category.subcategories.forEach { (name, amount) ->
                        ListItem(title = { Text(name) }, value = { Text(amount) }, nestingLevel = 1)
                    }
                }
            }
        }
    }
}
```

- **The data arrives finished.** The presentation layer hands over periods already summed, labelled
  and described; the charts draw numbers and strings, they do not aggregate or format. Put the
  latest period last.
- **Describe every column.** Give each column a `contentDescription` that says the whole period
  ("October: income €4,250, spent €2,340"); the drawn labels alone ("Now") do not.
- **Selection.** "Back to latest" selects the last key, and the chart scrolls to it. The chart stays
  on the latest column as periods arrive until the reader scrolls away, and moves only when the
  selected key changes, not on a refresh. Hoist `rememberColumnChartState()` only to scroll without
  selecting (`scrollToEnd()`), or to read `isAtEnd`, `firstVisibleIndex` and `lastVisibleIndex`.
- **A meter that belongs to its row** goes in `ExpandableListItem`'s `bottom` slot, inside the row's
  target, so tapping the meter opens the row; what the row reveals goes in its content.
- **Stacked shares.** A `StackedBarSegment`'s `fraction` is a share of the bar; segments that sum to
  more than `1f` are scaled to fill it, so amounts work as they are.

## Variations

**A comparison sheet.** A sheet whose foot holds two actions sharing the width — "Cancel" beside
"Compare with September" — uses `FlowModalActionBarLayout.Split()`. Filter chips that must look
tappable before any is chosen use `BadgeStyle.FilledChip`:

```kotlin
FlowModal(
    sheet,
    footer = {
        FlowModalActionBar(
            "Compare with September",
            onConfirm = onCompare,
            layout = FlowModalActionBarLayout.Split(),
            showDivider = false,
            leading = { FlowModalSecondaryAction("Cancel", onClick = { sheet.hide() }) },
        )
    },
) {
    FlowBadgeSelect(
        options = categories.map { BadgeOption(label = { Text(it) }, value = it) },
        onSelect = { onToggle(it.value) },
        selected = chosen,
        selectionMode = BadgeSelectionMode.Multiple,
        style = BadgeStyle.FilledChip,
        size = BadgeSize.Chip,
    )
}
```

The sheet's controller comes from `rememberFlowModal()`; open it with `show()`.

**A month day by day.** A `Calendar` from `dev.voir.areca:datetime` with `CalendarHeatDay` in its
`day` slot, and `calendarHeatLevel` mapping amounts to the theme's `accentScale` shades; see
[ui/datetime](../../ui/datetime/README.md).

**A running total against another period.** A `LineChart` with two series, the comparison drawn
dashed; see [ui/charts](../../ui/charts/README.md).

**Comparing groups.** A `BarChart` draws groups of bars on one scale — revenue by quarter, this year
against last.
