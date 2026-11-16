# List screen with refresh, empty and error states

A paged list of invoices: a top bar with an add control, rows that open an invoice, pull-to-refresh,
the next page loaded as the user nears the end, and a face for each state — loading, failed with a
retry, empty, and the end of the list.

Needs `dev.voir.areca:foundation`.

## Components

| Component | Role here |
| --- | --- |
| `BaseLayout` | The screen's frame; its `padding` becomes the list's `contentPadding` |
| `TopBar` with `AddAction` | The title, and the control that creates an invoice |
| `PagedContentList` | Decides which face to show from the state, composes rows lazily, requests the next page near the end, and adds pull-to-refresh when `onRefresh` is set |
| `PagedContentListState` | Plain data your view model produces: the items, whether there is a next page, and what is loading or failed |
| `ContentListLabels` | The words of the default faces |
| `Fallback(title, …, tone)` | The empty face and the failed face, in words, with the stock tray and alert marks |
| `NavigationListItem` | A row that opens something, with a chevron |

Every component is in the [catalogue](../components.md).

## Code

```kotlin
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.runtime.Composable
import dev.voir.areca.foundation.collection.ContentListLabels
import dev.voir.areca.foundation.collection.PagedContentList
import dev.voir.areca.foundation.collection.PagedContentListState
import dev.voir.areca.foundation.feedback.Fallback
import dev.voir.areca.foundation.feedback.FallbackTone
import dev.voir.areca.foundation.layout.BaseLayout
import dev.voir.areca.foundation.list.NavigationListItem
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.topbar.AddAction
import dev.voir.areca.foundation.topbar.TopBar
```

```kotlin
data class Invoice(val id: String, val customer: String, val due: String, val amount: String)

@Composable
fun InvoicesScreen(
    state: PagedContentListState<Invoice>,
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
        PagedContentList(
            state = state,
            contentPadding = padding,
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
        ) { invoice ->
            NavigationListItem(
                title = { Text(invoice.customer) },
                onClick = { onOpen(invoice) },
                subtitle = { Text(invoice.due) },
                value = { Text(invoice.amount) },
                onClickLabel = "Open invoice",
            )
        }
    }
}
```

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
val loading = PagedContentListState<Invoice>(items = emptyList(), hasNextPage = false, loadingInitial = true)

val failed = PagedContentListState<Invoice>(
    items = emptyList(),
    hasNextPage = false,
    initialError = "Check your connection and try again.",
)

val firstPage = PagedContentListState(items = invoices, hasNextPage = true)
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

**A short set loaded in one go.** `ContentList` takes a `LimitedContentListState` and composes every
row; put it inside a scrolling parent:

```kotlin
ContentList(
    state = recent,
    labels = ContentListLabels.English.copy(emptyTitle = "No recent invoices"),
    onRetryInitial = onRetry,
) { invoice ->
    ListItem(title = { Text(invoice.customer) }, value = { Text(invoice.amount) })
}
```

**Groups.** `GroupedContentList` and `GroupedPagedContentList` take `ContentListGroup`s and a
`groupHeaderContent` slot — invoices by month.

**Your own scroller.** For a list that is not a `ContentList`, wrap it in `PullToRefreshLayout` and
show a `Fallback` yourself when the state says the content is missing or failed.
`PullToRefreshLayout` is experimental, so opt in:

```kotlin
@OptIn(ExperimentalArecaApi::class)
@Composable
fun Feed(invoices: List<Invoice>, refreshing: Boolean, onRefresh: () -> Unit) {
    PullToRefreshLayout(isRefreshing = refreshing, onRefresh = onRefresh) {
        LazyColumn(Modifier.fillMaxSize()) {
            items(invoices, key = { it.id }) { invoice ->
                ListItem(title = { Text(invoice.customer) })
            }
        }
    }
}
```

**Columns.** A set read across columns is a `Table` or a `LazyTable`.

**Placeholders.** `ListItemSkeleton` is a row-shaped placeholder, for a custom `loadingContent`.
`SkeletonBlock` holds the place of something that is not text — an avatar, a thumbnail, a card — at
the size its modifier gives it. Wrap a region of placeholders in `SkeletonGroup`, which reads its
`contentDescription` once.

**A section that loads on its own.** Outside a `ContentList`, `rememberSettledLoading(loading)`
keeps a fast load from flashing a placeholder: no face for a load under 150ms, and a face that
shows stays at least 400ms.

```kotlin
val showSkeleton = rememberSettledLoading(state.loading)
when {
    showSkeleton -> SkeletonGroup(contentDescription = "Loading invoices") {
        repeat(3) { ListItemSkeleton() }
    }
    state.loading -> Unit // under the delay: draw nothing, not the empty face
    state.error != null -> Fallback(
        title = "Couldn’t load invoices",
        body = state.error,
        tone = FallbackTone.Failure,
        actionText = "Try again",
        onAction = onRetry,
    )
    state.invoices.isEmpty() -> Fallback(title = "No invoices yet")
    else -> RecentInvoices(state.invoices)
}
```

**Back to the top.** In a tab destination, hoist the list's state and call
`listState.ScrollToTopOnTabReselect()`; pass `listState` to `PagedContentList`. A tap on the tab
already selected scrolls the list to its first row.
