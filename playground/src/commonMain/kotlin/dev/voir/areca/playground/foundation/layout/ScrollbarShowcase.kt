package dev.voir.areca.playground.foundation.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.layout.VerticalScrollbar
import dev.voir.areca.foundation.list.ListItem
import dev.voir.areca.foundation.table.Table
import dev.voir.areca.foundation.table.TableColumn
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.canvas.WindowFrame
import dev.voir.areca.playground.showcase.canvas.WindowPreset
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.window.InputMode

/**
 * Showcase for [VerticalScrollbar] and its horizontal sibling.
 *
 * The sample is a month of a finance app's transactions: long enough to scroll in a laptop window.
 */
@Composable
fun ScrollbarShowcase() = ShowcaseScaffold(
    title = "Scrollbar",
    summary = "A draggable scrollbar beside a scrolling page, drawn under a pointer and never under touch.",
    module = "foundation",
    status = ShowcaseStatus.Beta,
) {
    Playground(
        description = "Scroll, drag the thumb or click the track. Switch to touch and the scrollbar is gone.",
    ) {
        val input by enumControl(
            key = "input",
            default = InputMode.Pointer,
            options = InputMode.entries,
            optionLabel = { it.name },
        )
        val rows by numberControl(
            key = "rows",
            default = 30f,
            range = 1f..60f,
            step = 1f,
            format = { it.toInt().toString() },
            description = "How long the page is.",
        )

        preview {
            WindowFrame(preset = WindowPreset.Laptop, input = input, maxHeight = 320.dp) {
                ScrollingPage(rows = rows.toInt())
            }
        }
    }

    States(layout = VariantLayout.Vertical) {
        variant("Pointer", detail = "thumb at 22% of the copy's colour, 45% under the pointer") {
            WindowFrame(preset = WindowPreset.Laptop, maxHeight = 200.dp) { ScrollingPage(rows = 30) }
        }
        variant("Touch", detail = "nothing drawn, no space taken") {
            WindowFrame(preset = WindowPreset.Tablet, input = InputMode.Touch, maxHeight = 200.dp) {
                ScrollingPage(rows = 30)
            }
        }
    }

    Recipes {
        recipe(
            name = "Built into Areca's lists",
            description = "List, ContentList, AdaptiveGrid, GroupedGrid, NavList and LazyTable draw their own; " +
                "Table draws a horizontal one when its columns are wider than it.",
        ) {
            WindowFrame(preset = WindowPreset.Laptop, maxHeight = 220.dp) {
                Table(
                    items = MonthOfTransactions.take(4),
                    columns = listOf(
                        TableColumn(id = "date", title = "Date", width = 160.dp, value = { it.first }),
                        TableColumn(id = "merchant", title = "Merchant", width = 360.dp, value = { it.second }),
                        TableColumn(id = "category", title = "Category", width = 360.dp, value = { "Groceries" }),
                        TableColumn(id = "wallet", title = "Wallet", width = 360.dp, value = { "Main account" }),
                        TableColumn(id = "amount", title = "Amount", width = 200.dp, value = { it.third }),
                    ),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }

    EdgeCases {
        case(
            name = "Content that fits",
            expectation = "Nothing to scroll, so no thumb.",
        ) {
            WindowFrame(preset = WindowPreset.Laptop, maxHeight = 200.dp) { ScrollingPage(rows = 2) }
        }
    }
}

/** A month of transactions as date, merchant and amount. */
private val MonthOfTransactions: List<Triple<String, String, String>> = List(60) { index ->
    val merchants = listOf("Lidl", "Café Central", "Netflix", "Uber", "Rewe", "Deutsche Bahn")
    Triple("${31 - index / 2} Oct", merchants[index % merchants.size], "−€${(index * 7 % 90) + 3}.40")
}

/** A scrolling column of [rows] transactions with a scrollbar at its end. */
@Composable
private fun ScrollingPage(rows: Int) {
    val scroll = rememberScrollState()
    Box(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize().verticalScroll(scroll).padding(vertical = 8.dp)) {
            for ((date, merchant, amount) in MonthOfTransactions.take(rows)) {
                ListItem(
                    title = { Text(merchant) },
                    subtitle = { Text(date) },
                    trailing = { Text(amount) },
                )
            }
        }
        VerticalScrollbar(scroll, Modifier.align(Alignment.CenterEnd).fillMaxHeight().padding(2.dp))
    }
}
