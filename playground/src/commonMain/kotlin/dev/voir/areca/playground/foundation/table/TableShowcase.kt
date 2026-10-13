package dev.voir.areca.playground.foundation.table

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.badge.Badge
import dev.voir.areca.foundation.badge.BadgeSize
import dev.voir.areca.foundation.badge.BadgeStyle
import dev.voir.areca.foundation.table.LazyTable
import dev.voir.areca.foundation.table.Table
import dev.voir.areca.foundation.table.TableColumn
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.Anatomy
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants

/** A row in the examples. */
private data class Invoice(
    val number: String,
    val client: String,
    val amount: String,
    val status: String,
)

private val Invoices = listOf(
    Invoice("INV-4821", "Northwind Trading", "$4,200.00", "Paid"),
    Invoice("INV-4822", "Contoso Ltd", "$1,180.50", "Pending"),
    Invoice("INV-4823", "Fabrikam Inc", "$9,640.00", "Overdue"),
    Invoice("INV-4824", "Adventure Works", "$620.00", "Paid"),
    Invoice("INV-4825", "Tailspin Toys", "$3,015.75", "Pending"),
)

/**
 * Showcase for [Table] and [LazyTable].
 */
@Composable
fun TableShowcase() = ShowcaseScaffold(
    title = "Table",
    summary = "Column-driven data table, eagerly laid out or lazily scrolled.",
    module = "foundation",
) {
    Playground(description = "Columns are data. Each one decides its own width strategy and renderer.") {
        val lazyRows by booleanControl(
            key = "lazy",
            default = false,
            description = "LazyTable scrolls its body; Table lays every row out at once.",
        )
        val withStatusBadge by booleanControl(
            key = "custom cell",
            default = true,
            description = "Render the status column as a badge instead of text.",
        )
        val withDividers by booleanControl("dividers", default = true)

        preview {
            val columns = invoiceColumns(statusAsBadge = withStatusBadge)

            if (lazyRows) {
                LazyTable(
                    items = Invoices,
                    columns = columns,
                    modifier = Modifier.fillMaxWidth().height(260.dp),
                    separated = withDividers,
                )
            } else {
                Table(
                    items = Invoices,
                    columns = columns,
                    modifier = Modifier.fillMaxWidth(),
                    separated = withDividers,
                )
            }
        }
    }

    Anatomy(description = "A table is its column list. Everything else follows from that.") {
        slot(
            name = "value",
            description = "The default renderer: read a field, draw it as text.",
        ) {
            Table(
                items = Invoices.take(3),
                columns = listOf(
                    TableColumn(id = "number", title = "Invoice", value = { it.number }),
                    TableColumn(id = "client", title = "Client", value = { it.client }),
                ),
                modifier = Modifier.fillMaxWidth(),
            )
        }
        slot(
            name = "cell",
            description = "A custom renderer, which replaces value entirely.",
        ) {
            Table(
                items = Invoices.take(3),
                columns = listOf(
                    TableColumn(id = "number", title = "Invoice", value = { it.number }),
                    TableColumn<Invoice>(
                        id = "status",
                        title = "Status",
                        cell = { invoice -> StatusBadge(invoice.status) },
                    ),
                ),
                modifier = Modifier.fillMaxWidth(),
            )
        }
        slot(
            name = "header",
            description = "A custom header renderer, for sort affordances and the like.",
        ) {
            Table(
                items = Invoices.take(3),
                columns = listOf(
                    TableColumn(id = "number", title = "Invoice", value = { it.number }),
                    TableColumn(
                        id = "amount",
                        title = "Amount",
                        value = { it.amount },
                        header = {
                            Text(
                                text = "Amount ↓",
                                variant = TextVariant.LabelSmall,
                                color = TextColor.Brand,
                            )
                        },
                    ),
                ),
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }

    Variants(
        title = "Column widths",
        description = "Fixed, weighted, or clamped between a minimum and maximum.",
        layout = VariantLayout.Vertical,
    ) {
        variant("Weighted", detail = "expandWeight shares the space") {
            Table(
                items = Invoices.take(3),
                columns = listOf(
                    TableColumn(id = "number", title = "Invoice", value = { it.number }),
                    TableColumn(id = "client", title = "Client", expandWeight = 2f, value = { it.client }),
                    TableColumn(id = "amount", title = "Amount", value = { it.amount }),
                ),
                modifier = Modifier.fillMaxWidth(),
            )
        }
        variant("Fixed", detail = "width = 120dp on the first column") {
            Table(
                items = Invoices.take(3),
                columns = listOf(
                    TableColumn(id = "number", title = "Invoice", width = 120.dp, value = { it.number }),
                    TableColumn(id = "client", title = "Client", value = { it.client }),
                    TableColumn(id = "amount", title = "Amount", value = { it.amount }),
                ),
                modifier = Modifier.fillMaxWidth(),
            )
        }
        variant("Clamped", detail = "maxWidth caps the client column") {
            Table(
                items = Invoices.take(3),
                columns = listOf(
                    TableColumn(id = "number", title = "Invoice", value = { it.number }),
                    TableColumn(
                        id = "client",
                        title = "Client",
                        maxWidth = 140.dp,
                        value = { it.client },
                    ),
                    TableColumn(id = "amount", title = "Amount", value = { it.amount }),
                ),
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }

    EdgeCases {
        case(
            name = "No rows",
            expectation = "Header still renders; the empty state is the caller's to supply.",
        ) {
            Table(
                items = emptyList<Invoice>(),
                columns = invoiceColumns(statusAsBadge = false),
                modifier = Modifier.fillMaxWidth(),
            )
        }
        case(
            name = "One column",
            expectation = "Takes the full width rather than collapsing to its minimum.",
        ) {
            Table(
                items = Invoices.take(3),
                columns = listOf(TableColumn(id = "client", title = "Client", value = { it.client })),
                modifier = Modifier.fillMaxWidth(),
            )
        }
        case(
            name = "Long cell content",
            expectation = "Cell content is clipped by the column, not allowed to widen it.",
        ) {
            Table(
                items = listOf(
                    Invoice(
                        number = "INV-4899",
                        client = "A client with an extremely long registered trading name",
                        amount = "$12,400.00",
                        status = "Pending",
                    ),
                ),
                columns = invoiceColumns(statusAsBadge = false),
                modifier = Modifier.fillMaxWidth(),
            )
        }
        case(
            name = "Many rows in a LazyTable",
            expectation = "Header stays put while the body scrolls beneath it.",
        ) {
            LazyTable(
                items = List(40) { Invoices[it % Invoices.size] },
                columns = invoiceColumns(statusAsBadge = true),
                modifier = Modifier.fillMaxWidth().height(280.dp),
                key = { index, _ -> index },
            )
        }
    }
}

private fun invoiceColumns(statusAsBadge: Boolean): List<TableColumn<Invoice>> = listOf(
    TableColumn(id = "number", title = "Invoice", value = { it.number }),
    TableColumn(id = "client", title = "Client", expandWeight = 2f, value = { it.client }),
    TableColumn(id = "amount", title = "Amount", value = { it.amount }),
    if (statusAsBadge) {
        TableColumn<Invoice>(
            id = "status",
            title = "Status",
            cell = { invoice -> StatusBadge(invoice.status) },
        )
    } else {
        TableColumn(id = "status", title = "Status", value = { it.status })
    },
)

@Composable
private fun StatusBadge(status: String) {
    val style = when (status) {
        "Paid" -> BadgeStyle.Success
        "Overdue" -> BadgeStyle.Danger
        else -> BadgeStyle.Warning
    }

    Badge(style = style, size = BadgeSize.ExtraSmall, soft = true) { Text(status) }
}
