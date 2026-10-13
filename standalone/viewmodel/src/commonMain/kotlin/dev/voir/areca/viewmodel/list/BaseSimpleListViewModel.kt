package dev.voir.areca.viewmodel.list

/**
 * Convenience base for lists that require neither filtering nor sorting.
 *
 * Removes the filter and sort type parameters from [BaseListViewModel] and simplifies [fetchPage]
 * to a single `page` argument.
 *
 * Example:
 * ```kotlin
 * class NotificationsViewModel(
 *     private val getNotifications: GetNotificationsPageUseCase,
 * ) : BaseSimpleListViewModel<NotificationsAction, Nothing, Notification>() {
 *
 *     init { load() }
 *
 *     fun load() = dispatch(NotificationsAction.Load)
 *     fun refresh() = dispatch(NotificationsAction.Refresh)
 *     fun loadNextPage() = dispatch(NotificationsAction.LoadNextPage)
 *
 *     override suspend fun reduce(action: NotificationsAction) = when (action) {
 *         NotificationsAction.Load -> handleLoad()
 *         NotificationsAction.Refresh -> handleRefresh()
 *         NotificationsAction.LoadNextPage -> handleLoadNextPage()
 *     }
 *
 *     override suspend fun fetchPage(page: Int): PageResult<Notification> =
 *         getNotifications(page = page, pageSize = pageSize)
 * }
 * ```
 *
 * @param A Action type dispatched from the UI.
 * @param E Best-effort, one-off effect type.
 * @param ITEM Type of each list item.
 * @param pageSize Number of items per page; at least 1. Defaults to
 * [BaseListViewModel.DEFAULT_PAGE_SIZE].
 * @throws IllegalArgumentException when [pageSize] is zero or negative.
 */
public abstract class BaseSimpleListViewModel<A : Any, E : Any, ITEM : Any>(
    pageSize: Int = DEFAULT_PAGE_SIZE,
) : BaseListViewModel<A, E, ITEM, Unit, Unit>(
    initialFilters = null,
    initialSorting = null,
    pageSize = pageSize,
) {

    /**
     * Fetches a single page from a domain use case.
     *
     * Simpler overload of [BaseListViewModel.fetchPage] that omits the unused filter and sort
     * parameters, with the same contract.
     *
     * @param page One-based page number to fetch.
     * @return Exactly the requested page.
     */
    protected abstract suspend fun fetchPage(page: Int): PageResult<ITEM>

    /**
     * Bridges the base class signature to the simplified [fetchPage] overload.
     *
     * Do not override this in subclasses; override [fetchPage] instead.
     *
     * @param page One-based page number to fetch.
     * @param filters Always `null`; this list has no filters.
     * @param sorting Always `null`; this list has no sorting.
     * @return Exactly the requested page.
     */
    final override suspend fun fetchPage(
        page: Int,
        filters: Unit?,
        sorting: Unit?,
    ): PageResult<ITEM> = fetchPage(page)
}
