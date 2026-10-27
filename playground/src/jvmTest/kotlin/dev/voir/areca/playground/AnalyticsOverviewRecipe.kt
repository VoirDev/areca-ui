package dev.voir.areca.playground

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
import dev.voir.areca.foundation.selection.SelectorItem
import dev.voir.areca.foundation.selection.SelectorLayout
import dev.voir.areca.foundation.selection.SlidingSelector
import dev.voir.areca.foundation.stat.Stat
import dev.voir.areca.foundation.stat.StatGrid
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.RefreshCcw
import dev.voir.areca.theme.ArecaTheme

// The analytics overview recipe from `skills/areca-ui-v1/reference/recipes.md`, kept here so the
// snippet the skill shows is known to compile and is rendered by AnalyticsRenderTest.

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
            SlidingSelector(
                items = listOf("Week", "Month", "Year").map { SelectorItem(it) { Text(it) } },
                selected = step,
                onSelect = { if (it != null) step = it },
                layout = SelectorLayout.Hug,
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
            referenceLines = listOf(ColumnChartReferenceLine(usualSpend, "usual spend", ArecaTheme.colors.fgDangerText)),
            contentPadding = PaddingValues(horizontal = ArecaTheme.spacing.horizontalContentInsets),
        )

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
