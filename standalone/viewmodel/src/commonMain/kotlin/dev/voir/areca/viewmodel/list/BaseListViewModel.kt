package dev.voir.areca.viewmodel.list

import androidx.lifecycle.viewModelScope
import dev.voir.areca.viewmodel.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * Base ViewModel for paginated lists that may also support filtering and sorting.
 *
 * Adds list-specific behavior on top of [BaseViewModel]:
 * - Initial full-screen load and retry
 * - Pull-to-refresh while existing content stays visible
 * - Incremental page loading with inline error and retry
 * - Filter and sort updates with configurable reload strategies
 *
 * Concrete ViewModels keep ownership of their action sealed interface. They expose friendly public
 * methods, dispatch those actions, and route the actions to the protected `handle*` helpers from
 * `reduce`.
 *
 * Concurrency: load, retry, refresh, and filter or sort changes replace the list from page 1 and
 * run inline, so the reducer waits for them. A next-page load runs in a job owned by the ViewModel,
 * so the reducer stays free and further next-page requests are ignored until it finishes. Starting
 * a replacement cancels an in-flight next-page load, and a response that belongs to a replaced list
 * is discarded. The handlers may therefore also be called from jobs launched in `viewModelScope`,
 * for example to make filter changes latest-wins, without an old page landing in a new list.
 * Cancellation is never reported as a failure.
 *
 * @param A Action type dispatched from the UI.
 * @param E Best-effort, one-off effect type.
 * @param ITEM Type of a single list item.
 * @param FILTER Type capturing the active filter criteria. Use [NoFilter] when filtering is not
 * needed.
 * @param SORT Type describing the active sort order. Use [NoSort] when sorting is not needed.
 * @param initialFilters Filters to apply before the first [handleLoad] call, or `null` for none.
 * @param initialSorting Sort order to apply before the first [handleLoad] call, or `null` for the
 * default service order.
 * @property pageSize Number of items to request per page; at least 1.
 * @throws IllegalArgumentException when [pageSize] is zero or negative.
 */
public abstract class BaseListViewModel<A : Any, E : Any, ITEM : Any, FILTER : Any, SORT : Any>(
    initialFilters: FILTER? = null,
    initialSorting: SORT? = null,
    protected val pageSize: Int = DEFAULT_PAGE_SIZE,
) : BaseViewModel<A, E, ListState<ITEM, FILTER, SORT>>(
    initial = ListState(
        filters = initialFilters,
        sorting = initialSorting,
        pagingMeta = PagingMeta(pageSize = requirePositivePageSize(pageSize)),
    ),
) {

    /** Defaults shared by every list ViewModel. */
    public companion object {
        /** Page size used when none is provided to the constructor. */
        public const val DEFAULT_PAGE_SIZE: Int = 20
    }

    /**
     * Identifies the current list. Incremented whenever a replacement from page 1 starts, so a
     * response tagged with an older value belongs to a replaced list and is discarded.
     */
    private var generation: Int = 0

    /** The in-flight next-page load, or `null` when none was started since the last replacement. */
    private var nextPageJob: Job? = null

    /**
     * Fetches a single page from a domain use case.
     *
     * This is called by every load, refresh, and next-page operation. Implementations should only
     * fetch and map data; state transitions are handled by the base class.
     *
     * Throwing from this function is safe: the caller catches it and moves to the matching error
     * state. Returning a [PageResult.currentPage] other than [page] counts as a failure.
     *
     * @param page One-based page number to fetch.
     * @param filters Filters for this request, or `null` when no filter is applied.
     * @param sorting Sort order for this request, or `null` for the default service order.
     * @return Exactly the requested page.
     */
    protected abstract suspend fun fetchPage(
        page: Int,
        filters: FILTER?,
        sorting: SORT?,
    ): PageResult<ITEM>

    /**
     * Performs a full-screen load starting from page 1.
     *
     * The screen moves to [dev.voir.areca.viewmodel.UiState.Loading] while the request is in
     * flight. On success the first page is displayed; on failure the screen moves to
     * [dev.voir.areca.viewmodel.UiState.Error].
     *
     * Typically called when the screen opens, and by [handleRetry] after a full-screen error.
     */
    protected suspend fun handleLoad() {
        reloadFullScreen()
    }

    /**
     * Performs a pull-to-refresh reload starting from page 1.
     *
     * While content is shown, it stays visible with [ListState.isRefreshing] set. On success the
     * list is replaced with page 1; on failure the items stay, [ListState.refreshError] is set, and
     * [onRefreshError] is called. While no content is shown (initial, loading, or error screen),
     * this performs a full-screen load instead.
     *
     * Ignored while another refresh of the same list is in flight, which can happen only when the
     * handler is called from separately launched jobs.
     */
    protected suspend fun handleRefresh() {
        val current = currentDisplayOrNull()
        when {
            current == null -> reloadFullScreen()
            current.isRefreshing -> return
            else -> reloadInPlace(previous = current) { it.copy(isRefreshing = true) }
        }
    }

    /**
     * Retries the initial load after a [dev.voir.areca.viewmodel.UiState.Error].
     *
     * Semantically distinct from [handleLoad] but behaviorally identical. Wire this up to a "Try
     * again" button on the full-screen error view.
     */
    protected suspend fun handleRetry() {
        reloadFullScreen()
    }

    /**
     * Starts loading the next page; on success it is appended to [ListState.items].
     *
     * Returns without waiting: the page loads in a job owned by the ViewModel. Does nothing when:
     * - the screen does not show content
     * - [PagingMeta.hasNextPage] is false
     * - a next-page load, refresh, or inline reload is already in progress
     *
     * The UI should show an inline loading indicator at the bottom of the list while
     * [ListState.isLoadingNextPage] is true, and an inline error and retry row when
     * [ListState.nextPageError] is non-null.
     */
    protected fun handleLoadNextPage() {
        val current = currentDisplayOrNull() ?: return
        if (!current.pagingMeta.hasNextPage || current.isAnyOperationInProgress) return

        val operation = generation
        val page = current.pagingMeta.currentPage + 1
        // Clear an old page error as soon as a new page request starts.
        mutateDisplay { it.copy(isLoadingNextPage = true, nextPageError = null) }
        nextPageJob = viewModelScope.launch {
            loadNextPage(operation, page, current.filters, current.sorting)
        }
    }

    /**
     * Retries a failed [handleLoadNextPage] attempt.
     *
     * Clears [ListState.nextPageError] as the new attempt starts, so the UI can swap the error row
     * back to a loading indicator immediately on tap. Wire this to a "Retry" button rendered at the
     * bottom of the list.
     */
    protected fun handleRetryLoadNextPage() {
        handleLoadNextPage()
    }

    /**
     * Applies new filter criteria and reloads the list from page 1.
     *
     * @param filters New filter value to apply.
     * @param strategy How the screen should look while the reload is in flight. See
     * [ReloadStrategy].
     */
    protected suspend fun handleUpdateFilters(
        filters: FILTER,
        strategy: ReloadStrategy = ReloadStrategy.FullScreen,
    ) {
        reloadWithQuery(strategy) { it.copy(filters = filters) }
    }

    /**
     * Clears the active filter and reloads the list from page 1.
     *
     * Sets [ListState.filters] to `null`. [fetchPage] implementations should treat `null` filters
     * as "no filter applied". Use this when the user taps a "Clear filters" or "Show all" control.
     *
     * @param strategy How the screen should look while the reload is in flight. See
     * [ReloadStrategy].
     */
    protected suspend fun handleClearFilters(
        strategy: ReloadStrategy = ReloadStrategy.FullScreen,
    ) {
        reloadWithQuery(strategy) { it.copy(filters = null) }
    }

    /**
     * Applies a new sort order and reloads the list from page 1.
     *
     * @param sorting New sort order to apply.
     * @param strategy How the screen should look while the reload is in flight. See
     * [ReloadStrategy].
     */
    protected suspend fun handleUpdateSorting(
        sorting: SORT,
        strategy: ReloadStrategy = ReloadStrategy.FullScreen,
    ) {
        reloadWithQuery(strategy) { it.copy(sorting = sorting) }
    }

    /**
     * Called when [handleRefresh] or a [ReloadStrategy.Inline] reload fails.
     *
     * The failure is already recorded in [ListState.refreshError], which the UI should render. The
     * default implementation does nothing more. Override it to add a best-effort extra, such as a
     * snackbar effect. Not called for cancellation or for a list that was replaced meanwhile.
     *
     * Example:
     * ```kotlin
     * override suspend fun onRefreshError(throwable: Throwable) {
     *     emitEffect(MyEffect.ShowRefreshFailed)
     * }
     * ```
     *
     * @param throwable The error that caused the refresh to fail.
     */
    protected open suspend fun onRefreshError(throwable: Throwable): Unit = Unit

    /**
     * Applies a query change with [strategy], falling back to a full-screen reload when no content
     * is shown.
     *
     * @param strategy How the screen should look while the reload is in flight.
     * @param applyQuery Returns the state with the new filters or sorting applied.
     */
    private suspend fun reloadWithQuery(
        strategy: ReloadStrategy,
        applyQuery: (ListState<ITEM, FILTER, SORT>) -> ListState<ITEM, FILTER, SORT>,
    ) {
        val current = currentDisplayOrNull()
        if (strategy == ReloadStrategy.Inline && current != null) {
            reloadInPlace(previous = current) { applyQuery(it).copy(isReloading = true) }
        } else {
            reloadFullScreen(applyQuery)
        }
    }

    /**
     * Replaces the list with page 1 behind a full-screen loading state.
     *
     * The query is stored in [display] before the request, so a retry after a failure uses it.
     * Cancellation leaves the state as it is.
     *
     * @param applyQuery Returns the state with any new filters or sorting applied.
     */
    private suspend fun reloadFullScreen(
        applyQuery: (ListState<ITEM, FILTER, SORT>) -> ListState<ITEM, FILTER, SORT> = { it },
    ) {
        val operation = startReplacement()
        val query = applyQuery(display).withoutOperations()
        storeDisplay(query)
        setLoading()

        try {
            val result = fetchRequestedPage(page = 1, filters = query.filters, sorting = query.sorting)
            if (operation == generation) setDisplay(freshState(result, query))
        } catch (throwable: Throwable) {
            currentCoroutineContext().ensureActive()
            if (operation == generation) setError(throwable)
        }
    }

    /**
     * Replaces the list with page 1 while the current items and paging stay visible.
     *
     * On failure or cancellation the previous filters and sorting come back, so the state still
     * describes the items on screen; a failure is also recorded in [ListState.refreshError] and
     * passed to [onRefreshError].
     *
     * @param previous The displayed state before the reload.
     * @param start Returns the in-flight state: the query to load plus the operation flag to show.
     */
    private suspend fun reloadInPlace(
        previous: ListState<ITEM, FILTER, SORT>,
        start: (ListState<ITEM, FILTER, SORT>) -> ListState<ITEM, FILTER, SORT>,
    ) {
        val operation = startReplacement()
        val query = start(previous.withoutOperations())
        setDisplay(query)

        try {
            val result = fetchRequestedPage(page = 1, filters = query.filters, sorting = query.sorting)
            if (operation == generation) setDisplay(freshState(result, query))
        } catch (throwable: Throwable) {
            val isFailure = currentCoroutineContext().isActive
            if (operation == generation) {
                mutateDisplay {
                    it.copy(
                        filters = previous.filters,
                        sorting = previous.sorting,
                        isRefreshing = false,
                        isReloading = false,
                        refreshError = throwable.takeIf { isFailure },
                    )
                }
            }
            currentCoroutineContext().ensureActive()
            if (operation == generation) onRefreshError(throwable)
        }
    }

    /**
     * Loads [page] for the list identified by [operation] and appends it, unless the list was
     * replaced meanwhile.
     *
     * @param operation [generation] when the load started.
     * @param page One-based page to load.
     * @param filters Filters of the list the page belongs to.
     * @param sorting Sort order of the list the page belongs to.
     */
    private suspend fun loadNextPage(operation: Int, page: Int, filters: FILTER?, sorting: SORT?) {
        try {
            val result = fetchRequestedPage(page = page, filters = filters, sorting = sorting)
            if (operation != generation) return
            mutateDisplay { state ->
                state.copy(
                    items = state.items + result.items,
                    pagingMeta = result.toPagingMeta(pageSize),
                    isLoadingNextPage = false,
                    nextPageError = null,
                )
            }
        } catch (throwable: Throwable) {
            val isFailure = currentCoroutineContext().isActive
            if (operation == generation) {
                // Keep existing pages visible and expose a failure for an inline retry row.
                mutateDisplay {
                    it.copy(isLoadingNextPage = false, nextPageError = throwable.takeIf { isFailure })
                }
            }
            currentCoroutineContext().ensureActive()
        }
    }

    /**
     * Starts a replacement from page 1: cancels any in-flight next-page load and retires the
     * current list.
     *
     * @return The new [generation], identifying the list the replacement will produce.
     */
    private fun startReplacement(): Int {
        nextPageJob?.cancel()
        nextPageJob = null
        generation += 1
        return generation
    }

    /**
     * Fetches [page] and checks that the result is that page.
     *
     * @param page One-based page to fetch.
     * @param filters Filters for this request.
     * @param sorting Sort order for this request.
     * @return The fetched page.
     * @throws IllegalStateException when [fetchPage] returned a different page.
     */
    private suspend fun fetchRequestedPage(
        page: Int,
        filters: FILTER?,
        sorting: SORT?,
    ): PageResult<ITEM> {
        val result = fetchPage(page = page, filters = filters, sorting = sorting)
        check(result.currentPage == page) {
            "fetchPage returned page ${result.currentPage} when page $page was requested."
        }
        return result
    }

    /**
     * Builds a clean [ListState] from page 1 of [query].
     *
     * @param result First page of the new list.
     * @param query State holding the filters and sorting that produced [result].
     * @return A fresh state with no active loading or error flags.
     */
    private fun freshState(
        result: PageResult<ITEM>,
        query: ListState<ITEM, FILTER, SORT>,
    ): ListState<ITEM, FILTER, SORT> = ListState(
        items = result.items,
        pagingMeta = result.toPagingMeta(pageSize),
        filters = query.filters,
        sorting = query.sorting,
    )

    /**
     * Clears every operation flag and inline error, as a replacement from page 1 supersedes them.
     *
     * @return This state without in-flight operations or inline errors.
     */
    private fun ListState<ITEM, FILTER, SORT>.withoutOperations(): ListState<ITEM, FILTER, SORT> =
        copy(
            isRefreshing = false,
            isReloading = false,
            isLoadingNextPage = false,
            nextPageError = null,
            refreshError = null,
        )
}

/**
 * Checks a list ViewModel's page size before the ViewModel's scope starts its reducer, so an invalid
 * value fails the constructor without leaving a coroutine behind.
 *
 * @param pageSize Requested number of items per page.
 * @return [pageSize] unchanged.
 * @throws IllegalArgumentException when [pageSize] is zero or negative.
 */
private fun requirePositivePageSize(pageSize: Int): Int {
    require(pageSize > 0) { "pageSize must be positive, was $pageSize." }
    return pageSize
}
