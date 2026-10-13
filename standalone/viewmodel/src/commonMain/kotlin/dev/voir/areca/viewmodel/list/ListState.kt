package dev.voir.areca.viewmodel.list

/**
 * Screen state owned by [BaseListViewModel] and exposed via
 * [dev.voir.areca.viewmodel.UiState.Display].
 *
 * Captures both the visible content (items, pagination) and all transient operation states
 * (refresh, next-page loading, inline errors). Keeping these flags here, rather than at the
 * [dev.voir.areca.viewmodel.UiState] level, keeps the list visible while background operations run.
 *
 * Fine-grained concerns that do not belong here:
 * - Selection state: put it in the item model or a separate set in a wrapping screen state
 * - Expand/collapse state: same
 * - Per-item loading: annotate the item model
 *
 * @param ITEM Type of each list item.
 * @param FILTER Type describing the active filter criteria. Use [NoFilter] ([Unit]) when filtering
 * is not required.
 * @param SORT Type describing the active sort order. Use [NoSort] ([Unit]) when sorting is not
 * required.
 * @property items All items accumulated so far (all loaded pages combined, in order).
 * @property pagingMeta Current pagination metadata.
 * @property filters Filters currently applied to the list, or `null` if none are active.
 * @property sorting Sort order currently applied, or `null` for the default service order.
 * @property isRefreshing True while a pull-to-refresh reload is in flight. The list remains visible
 * so the user can see existing content during the wait.
 * @property isReloading True while a filter or sort change reloads the list without a full-screen
 * [dev.voir.areca.viewmodel.UiState.Loading]. The current items stay in place, so the UI can show
 * an inline loading indicator over them.
 * @property isLoadingNextPage True while the next page is being fetched.
 * @property nextPageError The failure of the most recent next-page request, or `null`. Cleared when
 * a new next-page request starts or the list is replaced.
 * @property refreshError The failure of the most recent pull-to-refresh or inline reload, or `null`.
 * The items it failed to replace are still shown. Cleared when the next refresh or reload starts or
 * the list is replaced.
 */
public data class ListState<ITEM, FILTER, SORT>(
    public val items: List<ITEM> = emptyList(),
    public val pagingMeta: PagingMeta = PagingMeta(),
    public val filters: FILTER? = null,
    public val sorting: SORT? = null,
    public val isRefreshing: Boolean = false,
    public val isReloading: Boolean = false,
    public val isLoadingNextPage: Boolean = false,
    public val nextPageError: Throwable? = null,
    public val refreshError: Throwable? = null,
)

/**
 * Type alias indicating that a list has no filter concept.
 *
 * Use as the `FILTER` type argument of [BaseListViewModel] or [ListState] when the list does not
 * support user-defined filtering. Equivalent to passing [Unit].
 */
public typealias NoFilter = Unit

/**
 * Type alias indicating that a list has no sort concept.
 *
 * Use as the `SORT` type argument of [BaseListViewModel] or [ListState] when the list does not
 * support user-defined sorting. Equivalent to passing [Unit].
 */
public typealias NoSort = Unit
