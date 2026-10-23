package dev.voir.areca.playground.datetime

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import dev.voir.areca.datetime.DateTile
import dev.voir.areca.datetime.calendar.CalendarLabels
import dev.voir.areca.foundation.list.ListItem
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.theme.ArecaTheme
import kotlinx.datetime.LocalDate

/** Showcase for [DateTile]. */
@Composable
fun DateTileShowcase() = ShowcaseScaffold(
    title = "Date tile",
    summary = "A date as a small tile — month over day — leading a row about something on that date.",
    module = "datetime",
) {
    Playground {
        val day by numberControl("day", default = 22f, range = 1f..28f, step = 1f, format = { it.toInt().toString() })
        val month by numberControl("month", default = 11f, range = 1f..12f, step = 1f, format = { it.toInt().toString() })

        preview {
            DateTile(date = LocalDate(2026, month.toInt(), day.toInt()), contentDescription = "Expected on this date")
        }
    }

    Recipes {
        recipe(name = "Expected next", description = "The tile leads a row naming what arrives and how much.") {
            ListItem(
                title = { Text("Salary") },
                subtitle = { Text("Usually on the 25th · last 6 months") },
                value = { Text("€3,650") },
                leading = { DateTile(date = LocalDate(2026, 10, 25), contentDescription = "25 October") },
            )
        }
    }

    EdgeCases {
        case(name = "Own labels", expectation = "The month line is the app's CalendarLabels.shortMonth.") {
            val french = CalendarLabels.English.copy(shortMonth = { if (it.ordinal == 2) "mars" else "déc." })
            Row(
                horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                DateTile(date = LocalDate(2026, 3, 1), labels = french)
                DateTile(date = LocalDate(2026, 12, 31), labels = french)
            }
        }
        case(name = "Long month name", expectation = "A name wider than the tile is cut short, not wrapped.") {
            DateTile(date = LocalDate(2026, 9, 9), labels = CalendarLabels.English.copy(shortMonth = { "September" }))
        }
    }
}
