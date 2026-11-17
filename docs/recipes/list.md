# List screen with refresh, empty and error states

A paged list of invoices: a top bar with an add control, rows that open an invoice, pull-to-refresh,
the next page loaded as the user nears the end, and a face for each state — loading, failed with a
retry, empty, and the end of the list. The rows never lie straight on the grey page: they come by
month, each month on a card of its own with its name inside.

Needs `dev.voir.areca:foundation`.

## Components

| Component | Role here |
| --- | --- |
| `BaseLayout` | The screen's frame; its `padding` becomes the list's `contentPadding` |
| `TopBar` with `AddAction` | The title, and the control that creates an invoice |
| `GroupedPagedContentList` with `ContentListGroupLayout.Inset` | Decides which face to show from the state, composes rows lazily on one card per group, requests the next page near the end, and adds pull-to-refresh when `onRefresh` is set |
| `PagedContentListState` of `ContentListGroup`s | Plain data your view model produces: the months and their invoices, whether there is a next page, and what is loading or failed |
| `ContentListLabels` | The words of the default faces |
| `Fallback(title, …, tone)` | The empty face and the failed face, in words, with the stock tray and alert marks |
| `NavigationListItem` | A row that opens something, with a chevron |
| `AmountText` | An invoice's amount, in the row's type with tabular figures |

Every component is in the [catalogue](../components.md).

## Code

```kotlin
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.runtime.Composable
import dev.voir.areca.foundation.collection.ContentListGroup
import dev.voir.areca.foundation.collection.ContentListGroupLayout
import dev.voir.areca.foundation.collection.ContentListLabels
import dev.voir.areca.foundation.collection.GroupedPagedContentList
import dev.voir.areca.foundation.collection.PagedContentListState
import dev.voir.areca.foundation.feedback.Fallback
import dev.voir.areca.foundation.feedback.FallbackTone
import dev.voir.areca.foundation.layout.BaseLayout
import dev.voir.areca.foundation.list.NavigationListItem
import dev.voir.areca.foundation.text.Amount
import dev.voir.areca.foundation.text.AmountText
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.topbar.AddAction
import dev.voir.areca.foundation.topbar.TopBar
```

```kotlin
data class Invoice(
    val id: String,
    val customer: String,
    val month: String,
    val due: String,
    val amount: Amount,
)

@Composable
fun InvoicesScreen(
    state: PagedContentListState<ContentListGroup<String, Invoice>>,
    onRefresh: () -> Unit,
    onLoadNextPage: () -> Unit,
    onOpen: (Invoice) -> Unit,
    onCreate: () -> Unit,
) {
    BaseLayout(
        contentWindowInsets = WindowInsets.safeDrawing.only(
            WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal,
        ),
        topBar = {
            TopBar(
                title = "Invoices",
                windowInsets = WindowInsets.safeDrawing.only(
                    WindowInsetsSides.Top + WindowInsetsSides.Horizontal,
                ),
                trailing = { AddAction(onClick = onCreate, contentDescription = "New invoice") },
            )
        },
    ) { padding ->
        GroupedPagedContentList(
            state = state,
            contentPadding = padding,
            groupLayout = ContentListGroupLayout.Inset,
            labels = ContentListLabels(
                emptyTitle = "No invoices yet",
                errorTitle = "Couldn’t load invoices",
                retry = "Try again",
                loadMoreErrorTitle = "Couldn’t load more invoices",
                endReached = "That’s every invoice",
            ),
            onRefresh = onRefresh,
            onLoadNextPage = onLoadNextPage,
            itemKey = { it.id },
            emptyContent = {
                Fallback(
                    title = "No invoices yet",
                    body = "Invoices you send appear here. Create one with +.",
                )
            },
            initialErrorContent = { message, retry ->
                Fallback(
                    title = "Couldn’t load invoices",
                    body = message,
                    tone = FallbackTone.Failure,
                    actionText = "Try again",
                    onAction = retry,
                )
            },
            groupHeaderContent = { month -> Text(month) },
        ) { invoice ->
            NavigationListItem(
                title = { Text(invoice.customer) },
                onClick = { onOpen(invoice) },
                subtitle = { Text(invoice.due) },
                value = { AmountText(invoice.amount) },
                onClickLabel = "Open invoice",
            )
        }
    }
}
```

- **Cards.** `ContentListGroupLayout.Inset` draws each month on a card at the screen's gutter, its
  name inside in line with the rows' text. The rows bring their own 16dp start padding and their
  hairlines start at the text, so add no gutter or `separatorInset` of your own. The faces lie on
  the page.
- **Padding.** `contentPadding = padding` lets the rows scroll under the bars and rest clear of
  them; the faces and the pull-to-refresh indicator are inset by it too.
- **Keys.** `itemKey` keeps each row's state with its item when the list changes.
- **Retry.** The error face's retry calls `onRetryInitial`, which defaults to `onRefresh`; the
  load-more error's calls `onRetryLoadMore`, which defaults to `onLoadNextPage`. A custom
  `initialErrorContent` is handed the message and that retry.
- **Faces.** `Fallback(title, …)` with `FallbackTone.Empty` draws a tray and, with an action, a
  neutral way back such as "Clear filters"; `FallbackTone.Failure` draws an alert and a "Try again".
  The create action stays in the top bar. For an empty face that is a call to action, use the slot
  form of `Fallback` with a solid `Button`.

## The state

The list shows whatever the state says. Your view model produces it:

```kotlin
typealias InvoiceMonths = PagedContentListState<ContentListGroup<String, Invoice>>

val loading = InvoiceMonths(items = emptyList(), hasNextPage = false, loadingInitial = true)

val failed = InvoiceMonths(
    items = emptyList(),
    hasNextPage = false,
    initialError = "Check your connection and try again.",
)

// The loaded invoices, by month, newest first.
val firstPage = InvoiceMonths(
    items = invoices.groupBy { it.month }.map { (month, inMonth) -> ContentListGroup(month, month, inMonth) },
    hasNextPage = true,
)
```

| Field | Shows |
| --- | --- |
| `loadingInitial = true`, no items | The loading face |
| `initialError` set, no items | The error face, with the message and a retry |
| No items, nothing loading or failed | The empty face |
| `loadingNextPage = true` | A spinner after the last row |
| `loadMoreError` set | An inline error after the last row, with a retry |
| `hasNextPage = false` | The end-of-list line after the last row |
| `refreshing = true` | The pull-to-refresh indicator |

Items already shown stay on screen while a refresh runs or a later load fails.

`dev.voir.areca:viewmodel` keeps paged-list state in a view model (`ListState`), which you map to this shape;
see [standalone/viewmodel](../../standalone/viewmodel/README.md).

## Variations

**A short set loaded in one go.** `GroupedContentList` takes a `LimitedContentListState` and
composes every row, on the same inset cards; put it inside a scrolling parent:

```kotlin
GroupedContentList(
    state = recent, // LimitedContentListState(listOf(ContentListGroup("recent", "Recent", invoices)))
    labels = ContentListLabels.English.copy(emptyTitle = "No recent invoices"),
    groupLayout = ContentListGroupLayout.Inset,
    onRetryInitial = onRetry,
    groupHeaderContent = { title -> Text(title) },
) { invoice ->
    ListItem(title = { Text(invoice.customer) }, value = { AmountText(invoice.amount) })
}
```

**No groups.** `ContentList` and `PagedContentList` take the items alone. Their rows lie straight
on whatever is under them, so keep them for a sheet, whose rows lie flat; on the grey page, give the
rows a card through the grouped lists above.

**Your own scroller.** For a list that is not a `ContentList`, wrap it in `PullToRefreshLayout` and
show a `Fallback` yourself when the state says the content is missing or failed. Put the rows on a
card at the screen's gutter; they bring their own start padding, so the card's padding is not
applied. `PullToRefreshLayout` is experimental, so opt in:

```kotlin
@OptIn(ExperimentalArecaApi::class)
@Composable
fun Feed(invoices: List<Invoice>, refreshing: Boolean, onRefresh: () -> Unit) {
    PullToRefreshLayout(isRefreshing = refreshing, onRefresh = onRefresh) {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(vertical = ArecaTheme.spacing.xl),
        ) {
            Card(modifier = Modifier.fillMaxWidth().horizontalContentInsets()) { _ ->
                List {
                    invoices.forEach { invoice ->
                        item(key = invoice.id, title = { Text(invoice.customer) })
                    }
                }
            }
        }
    }
}
```

A list long enough to need composing lazily is a grouped list instead, so it keeps its cards.

**Columns.** A set read across columns is a `Table` or a `LazyTable`.

**Placeholders.** `ListItemSkeleton` is a row-shaped placeholder, for a custom `loadingContent`.
`SkeletonBlock` holds the place of something that is not text — an avatar, a thumbnail, a card — at
the size its modifier gives it. Wrap a region of placeholders in `SkeletonGroup`, which reads its
`contentDescription` once.

**A section that loads on its own.** Outside a `ContentList`, `rememberSettledLoading(loading)`
keeps a fast load from flashing a placeholder: no face for a load under 150ms, and a face that
shows stays at least 400ms.

The section sits on its own card; the card's padding goes to the faces that are not rows:

```kotlin
val showSkeleton = rememberSettledLoading(state.loading)
// Under the delay: draw nothing, not the empty face.
if (state.loading && !showSkeleton) return

Card(modifier = Modifier.fillMaxWidth().horizontalContentInsets()) { padding ->
    when {
        showSkeleton -> SkeletonGroup(contentDescription = "Loading invoices") {
            repeat(3) { ListItemSkeleton() }
        }
        state.error != null -> Fallback(
            title = "Couldn’t load invoices",
            modifier = Modifier.padding(padding),
            body = state.error,
            tone = FallbackTone.Failure,
            actionText = "Try again",
            onAction = onRetry,
        )
        state.invoices.isEmpty() -> Fallback(
            title = "No invoices yet",
            modifier = Modifier.padding(padding),
            horizontalAlignment = Alignment.Start,
        )
        else -> RecentInvoiceRows(state.invoices) // a ListItemColumn of rows, no padding of its own
    }
}
```

**Back to the top.** In a tab destination, hoist the list's state and call
`listState.ScrollToTopOnTabReselect()`; pass `listState` to `GroupedPagedContentList`. A tap on the tab
already selected scrolls the list to its first row.
