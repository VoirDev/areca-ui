# List screen with refresh, empty and error states

A paged list of invoices: a top bar with an add control, rows that open an invoice, pull-to-refresh,
the next page loaded as the user nears the end, and a face for each state — loading, failed with a
retry, empty with a call to action, and the end of the list.

Needs `dev.voir.areca:foundation`, and `dev.voir.areca:icons` for the inbox glyph.

## Components

| Component | Role here |
| --- | --- |
| `BaseLayout` | The screen's frame; its `padding` becomes the list's `contentPadding` |
| `TopBar` with `AddAction` | The title, and the control that creates an invoice |
| `PagedContentList` | Decides which face to show from the state, composes rows lazily, requests the next page near the end, and adds pull-to-refresh when `onRefresh` is set |
| `PagedContentListState` | Plain data your view model produces: the items, whether there is a next page, and what is loading or failed |
| `ContentListLabels` | The words of the default faces |
| `Fallback` with `FeaturedIcon` | A custom empty face with an action |
| `NavigationListItem` | A row that opens something, with a chevron |

Every component is in the [catalogue](../components.md).

## Code

```kotlin
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.runtime.Composable
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.collection.ContentListLabels
import dev.voir.areca.foundation.collection.PagedContentList
import dev.voir.areca.foundation.collection.PagedContentListState
import dev.voir.areca.foundation.feedback.Fallback
import dev.voir.areca.foundation.icon.FeaturedIcon
import dev.voir.areca.foundation.layout.BaseLayout
import dev.voir.areca.foundation.list.NavigationListItem
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.topbar.AddAction
import dev.voir.areca.foundation.topbar.TopBar
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.Inbox
import dev.voir.areca.theme.ArecaTheme
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
        bg = ArecaTheme.colors.bgCanvas,
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
                    top = { FeaturedIcon(ArecaIcons.Inbox, contentDescription = null) },
                    heading = { Text("No invoices yet") },
                    body = { Text("Invoices you send appear here.") },
                    content = { Button(text = "New invoice", onClick = onCreate) },
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
  load-more error's calls `onRetryLoadMore`, which defaults to `onLoadNextPage`.

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
