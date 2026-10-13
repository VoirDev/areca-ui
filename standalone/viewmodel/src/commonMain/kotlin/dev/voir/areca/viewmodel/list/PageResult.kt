package dev.voir.areca.viewmodel.list

/**
 * The result of a single page fetch, returned by [BaseListViewModel.fetchPage].
 *
 * Implementations of [BaseListViewModel.fetchPage] must map their use-case result to this type so
 * the base class can manage state transitions uniformly.
 *
 * @param ITEM Type of each list item.
 * @property items Items belonging to the fetched page only (not cumulative).
 * @property currentPage The one-based page that was fetched. It must equal the page that
 * [BaseListViewModel.fetchPage] was asked for; the base class treats any other value as a failure
 * of that request.
 * @property totalPages Total pages available, or `null` if the source does not expose this.
 * @property totalItems Total item count across all pages, or `null` if unknown.
 * @property hasNextPage True when at least one additional page exists after this one.
 */
public data class PageResult<out ITEM>(
    public val items: List<ITEM>,
    public val currentPage: Int,
    public val totalPages: Int? = null,
    public val totalItems: Int? = null,
    public val hasNextPage: Boolean,
)

/**
 * Converts a [PageResult] into a [PagingMeta] record, carrying over all fields.
 *
 * @param pageSize The page size that was used for this request. Stored in metadata so consumers
 * know the granularity of each page.
 * @return Paging metadata describing this page.
 */
internal fun <ITEM> PageResult<ITEM>.toPagingMeta(pageSize: Int): PagingMeta = PagingMeta(
    currentPage = currentPage,
    totalPages = totalPages,
    totalItems = totalItems,
    hasNextPage = hasNextPage,
    pageSize = pageSize,
)
