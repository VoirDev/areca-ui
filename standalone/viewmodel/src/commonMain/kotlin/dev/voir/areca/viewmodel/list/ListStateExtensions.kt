package dev.voir.areca.viewmodel.list

/**
 * True when the list has been loaded but contains no items.
 *
 * Distinguishes the "empty result" case from the pre-load case where [ListState.items] is also
 * empty but [PagingMeta.currentPage] is zero. Use it to decide when to show the empty state.
 */
public val <ITEM, FILTER, SORT> ListState<ITEM, FILTER, SORT>.isEmpty: Boolean
    get() = pagingMeta.currentPage > 0 && items.isEmpty()

/**
 * True while a pull-to-refresh, an inline reload, or a next-page load is in progress.
 *
 * Full-screen loads are represented by [dev.voir.areca.viewmodel.UiState.Loading] instead.
 */
public val <ITEM, FILTER, SORT> ListState<ITEM, FILTER, SORT>.isAnyOperationInProgress: Boolean
    get() = isRefreshing || isReloading || isLoadingNextPage

/**
 * True when the list has items and no inline reload is about to replace them.
 */
public val <ITEM, FILTER, SORT> ListState<ITEM, FILTER, SORT>.hasContent: Boolean
    get() = items.isNotEmpty() && !isReloading
