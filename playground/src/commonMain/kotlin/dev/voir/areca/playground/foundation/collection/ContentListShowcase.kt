package dev.voir.areca.playground.foundation.collection

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.collection.ContentList
import dev.voir.areca.foundation.collection.LimitedContentListState
import dev.voir.areca.foundation.collection.PagedContentList
import dev.voir.areca.foundation.collection.PagedContentListState
import dev.voir.areca.foundation.list.ListItem
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout

private val SampleItems = List(8) { "Invoice #${4800 + it}" }

/** The states a content list resolves between. */
private enum class ListStateOption {
    Loading,
    Empty,
    Error,
    Loaded,
    Refreshing,
}

/** Showcase for [ContentList] and [PagedContentList]. */
@Composable
fun ContentListShowcase() = ShowcaseScaffold(
    title = "Content list",
    summary = "List wrapper that resolves loading, empty, error and loaded into one component.",
    module = "foundation",
) {
    Playground(description = "Switch between the states a real list moves through.") {
        val state by enumControl(
            key = "state",
            default = ListStateOption.Loaded,
            options = ListStateOption.entries,
            optionLabel = { it.name },
        )

        preview {
            ContentList(
                state = state.resolve(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp),
                itemContent = { InvoiceRow(it) },
            )
        }
    }

    States(
        description = "Each state is reached from the same component and the same data shape.",
        layout = VariantLayout.Vertical,
    ) {
        for (option in ListStateOption.entries) {
            variant(name = option.name, detail = option.detail) {
                ContentList(
                    state = option.resolve(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp),
                    itemContent = { InvoiceRow(it) },
                )
            }
        }
    }

    EdgeCases {
        case(
            name = "Paged list with more to load",
            expectation = "Shows a loading row at the end while hasNextPage is true.",
        ) {
            PagedContentList(
                state = PagedContentListState(
                    items = SampleItems,
                    hasNextPage = true,
                    loadingNextPage = true,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp),
                onLoadNextPage = {},
                itemContent = { InvoiceRow(it) },
            )
        }
        case(
            name = "Paged list at the end",
            expectation = "No loading row once hasNextPage is false.",
        ) {
            PagedContentList(
                state = PagedContentListState(items = SampleItems, hasNextPage = false),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp),
                onLoadNextPage = {},
                itemContent = { InvoiceRow(it) },
            )
        }
        case(
            name = "Error after content loaded",
            expectation = "Existing items stay visible; the error does not blank the list.",
        ) {
            PagedContentList(
                state = PagedContentListState(
                    items = SampleItems.take(3),
                    hasNextPage = true,
                    loadMoreError = "Could not load more invoices.",
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp),
                onLoadNextPage = {},
                itemContent = { InvoiceRow(it) },
            )
        }
        case(
            name = "Retry from the error state",
            expectation = "Retry calls back into the caller, which owns the reload.",
        ) {
            var attempts by remember { mutableStateOf(0) }

            ContentList(
                state = LimitedContentListState(
                    items = emptyList<String>(),
                    initialError = "Could not load invoices. Attempts: $attempts",
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp),
                onRetryInitial = { attempts++ },
                itemContent = { InvoiceRow(it) },
            )
        }
    }
}

@Composable
private fun InvoiceRow(label: String) {
    ListItem(title = { Text(label) })
}

private val ListStateOption.detail: String
    get() = when (this) {
        ListStateOption.Loading -> "loadingInitial = true"
        ListStateOption.Empty -> "items is empty, nothing loading"
        ListStateOption.Error -> "initialError is set"
        ListStateOption.Loaded -> "items present"
        ListStateOption.Refreshing -> "items present, refreshing = true"
    }

private fun ListStateOption.resolve(): LimitedContentListState<String> = when (this) {
    ListStateOption.Loading -> LimitedContentListState(emptyList(), loadingInitial = true)
    ListStateOption.Empty -> LimitedContentListState(emptyList())
    ListStateOption.Error -> LimitedContentListState(
        items = emptyList(),
        initialError = "Could not load invoices.",
    )

    ListStateOption.Loaded -> LimitedContentListState(SampleItems)
    ListStateOption.Refreshing -> LimitedContentListState(SampleItems, refreshing = true)
}
