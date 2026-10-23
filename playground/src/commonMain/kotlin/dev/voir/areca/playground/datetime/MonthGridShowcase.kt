package dev.voir.areca.playground.datetime

import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.voir.areca.datetime.calendar.CalendarLabels
import dev.voir.areca.datetime.calendar.MonthGrid
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import kotlinx.datetime.Month
import kotlinx.datetime.YearMonth

private val Current = YearMonth(2026, Month.OCTOBER)

/** A made-up month's total, as the tile's second line. */
private fun total(month: YearMonth): String {
    val value = 1.6f + ((month.year * 12 + month.month.ordinal) * 37 % 15) / 10f
    return "€${(value * 10).toInt() / 10f}k"
}

/** Showcase for [MonthGrid]. */
@Composable
fun MonthGridShowcase() = ShowcaseScaffold(
    title = "Month grid",
    summary = "Months as tiles, a year to a block, each with a figure, for picking a whole month.",
    module = "datetime",
) {
    Playground {
        val columns by numberControl("columns", default = 4f, range = 2f..6f, step = 1f, format = { it.toInt().toString() })
        val figures by booleanControl("content", default = true, description = "A total under each month.")

        preview {
            var selected by remember { mutableStateOf<YearMonth?>(YearMonth(2026, Month.SEPTEMBER)) }
            MonthGrid(
                years = listOf(2026, 2025),
                selected = selected,
                onSelect = { selected = it },
                enabled = { it < Current },
                columns = columns.toInt(),
                content = if (figures) {
                    { month -> Text(if (month == Current) "this" else if (month > Current) "" else total(month)) }
                } else {
                    null
                },
            )
        }
    }

    Recipes {
        recipe(
            name = "Compare with any month",
            description = "The current and future months refuse; every other month shows its total.",
        ) {
            var selected by remember { mutableStateOf<YearMonth?>(null) }
            MonthGrid(
                years = listOf(2026),
                selected = selected,
                onSelect = { selected = it },
                enabled = { it < Current },
            ) { month -> Text(if (month < Current) total(month) else if (month == Current) "this" else "") }
        }
    }

    EdgeCases {
        case(name = "Narrow container", expectation = "Constrained to 220dp: tiles shrink, names stay on one line.") {
            MonthGrid(
                years = listOf(2025),
                selected = YearMonth(2025, Month.MAY),
                onSelect = {},
                modifier = Modifier.width(220.dp),
            )
        }
        case(name = "Long month names", expectation = "Full names cut short with an ellipsis on narrow tiles.") {
            MonthGrid(
                years = listOf(2025),
                selected = null,
                onSelect = {},
                labels = CalendarLabels.English.copy(shortMonth = { it.name.lowercase().replaceFirstChar(Char::uppercase) }),
            )
        }
        case(name = "Five columns", expectation = "The last row's tiles keep the full rows' width.") {
            MonthGrid(years = listOf(2025), selected = null, onSelect = {}, columns = 5)
        }
    }
}
