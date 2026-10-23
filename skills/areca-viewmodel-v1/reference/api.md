# Public API

Open this when you need the exact type, parameter or default of a `dev.voir.areca:viewmodel`
declaration, at the latest release. This is the whole public API; the SKILL says how to use it.
Members marked `protected` are for subclasses only.

## `dev.voir.areca.viewmodel`

```kotlin
abstract class BaseViewModel<A : Any, E : Any, S : Any>(initial: S) : ViewModel
// A action, E effect, S display state. Starts at UiState.Display(initial).

    val state: StateFlow<UiState<S>>
    val effects: Flow<E>                  // lossless, single consumer; collect once per instance
    fun dispatch(action: A)               // never suspends, never drops; one ordered reducer

    protected abstract suspend fun reduce(action: A)
    protected open fun onReduceFailure(action: A, throwable: Throwable) // default: setError(throwable)
    protected open fun errorMessage(throwable: Throwable): String       // default: ""

    protected val display: S              // latest known data, even while Loading or Error
    protected fun currentDisplayOrNull(): S?                 // data only while Display is shown
    protected fun setDisplay(data: S)
    protected fun mutateDisplay(transform: (S) -> S)         // always publishes Display
    protected fun setInitial()                               // keeps `display`
    protected fun setLoading()                               // keeps `display`
    protected fun setError(message: String, cause: Throwable? = null)
    protected fun setError(throwable: Throwable)             // message from errorMessage(throwable)
    protected fun emitEffect(effect: E)
    protected suspend fun <T> runAction(
        block: suspend () -> T,
        onSuccess: suspend (T) -> Unit,
        onFailure: suspend (Throwable) -> Unit = { throwable -> setError(throwable) },
    )                                     // publishes Loading, runs inline, launches nothing
```

```kotlin
sealed interface UiState<out T> {
    data object Initial : UiState<Nothing>
    data object Loading : UiState<Nothing>
    data class Display<out T>(val data: T) : UiState<T>
    data class Error(val message: String, val cause: Throwable? = null) : UiState<Nothing>
}

fun <T> UiState<T>.displayOrNull(): T?   // data when Display, else null
```

```kotlin
class RefreshGate(
    cooldown: Duration = DEFAULT_COOLDOWN,             // 1.seconds
    timeSource: TimeSource = TimeSource.Monotonic,     // TestTimeSource in tests
) {
    companion object { val DEFAULT_COOLDOWN: Duration }
    fun tryReserve(onAccepted: () -> Unit): Boolean    // false while one runs or cools down
    suspend fun <T> runReserved(block: suspend () -> T): T
    // Throws IllegalStateException without a held reservation. Every successful tryReserve must
    // reach exactly one runReserved; if onAccepted throws, the reservation is released.
}
```

## `dev.voir.areca.viewmodel.list`

```kotlin
abstract class BaseListViewModel<A : Any, E : Any, ITEM : Any, FILTER : Any, SORT : Any>(
    initialFilters: FILTER? = null,
    initialSorting: SORT? = null,
    protected val pageSize: Int = DEFAULT_PAGE_SIZE,   // > 0, else IllegalArgumentException
) : BaseViewModel<A, E, ListState<ITEM, FILTER, SORT>> {
    companion object { const val DEFAULT_PAGE_SIZE: Int = 20 }

    protected abstract suspend fun fetchPage(page: Int, filters: FILTER?, sorting: SORT?): PageResult<ITEM>
    protected open suspend fun onRefreshError(throwable: Throwable)    // default: nothing

    protected suspend fun handleLoad()                 // full-screen load of page 1
    protected suspend fun handleRetry()                // the same, for a "Try again" button
    protected suspend fun handleRefresh()              // keeps items; full-screen without content
    protected fun handleLoadNextPage()                 // appends in an owned job; returns at once
    protected fun handleRetryLoadNextPage()            // clears nextPageError, loads the page again
    protected suspend fun handleUpdateFilters(filters: FILTER,
        strategy: ReloadStrategy = ReloadStrategy.FullScreen)
    protected suspend fun handleClearFilters(strategy: ReloadStrategy = ReloadStrategy.FullScreen)
    protected suspend fun handleUpdateSorting(sorting: SORT,
        strategy: ReloadStrategy = ReloadStrategy.FullScreen)
}

abstract class BaseSimpleListViewModel<A : Any, E : Any, ITEM : Any>(
    pageSize: Int = DEFAULT_PAGE_SIZE,
) : BaseListViewModel<A, E, ITEM, Unit, Unit> {
    protected abstract suspend fun fetchPage(page: Int): PageResult<ITEM>
}
```

```kotlin
data class ListState<ITEM, FILTER, SORT>(
    val items: List<ITEM> = emptyList(),
    val pagingMeta: PagingMeta = PagingMeta(),
    val filters: FILTER? = null,
    val sorting: SORT? = null,
    val isRefreshing: Boolean = false,      // pull-to-refresh with items kept
    val isReloading: Boolean = false,       // ReloadStrategy.Inline reload with items kept
    val isLoadingNextPage: Boolean = false,
    val nextPageError: Throwable? = null,
    val refreshError: Throwable? = null,    // failed refresh or inline reload; items kept
)

// Extensions on ListState<ITEM, FILTER, SORT>:
val isEmpty: Boolean                    // loaded (currentPage > 0) and no items: the empty state
val isAnyOperationInProgress: Boolean   // refreshing, reloading or loading the next page
val hasContent: Boolean                 // has items and no inline reload is replacing them

data class PageResult<out ITEM>(
    val items: List<ITEM>,
    val currentPage: Int,                   // must equal the requested page (one-based)
    val totalPages: Int? = null,
    val totalItems: Int? = null,
    val hasNextPage: Boolean,               // authoritative
)

data class PagingMeta(
    val currentPage: Int = 0,               // 0 before the first page loads
    val totalPages: Int? = null,
    val totalItems: Int? = null,
    val hasNextPage: Boolean = false,
    val pageSize: Int = BaseListViewModel.DEFAULT_PAGE_SIZE,
)

enum class ReloadStrategy {
    FullScreen, // Loading over the list; on failure Error, and retry uses the new query
    Inline,     // isReloading over the kept items; on failure the previous query is restored,
                // refreshError is set and onRefreshError called
}
```
