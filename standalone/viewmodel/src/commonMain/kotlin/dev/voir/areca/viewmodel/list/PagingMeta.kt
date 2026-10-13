package dev.voir.areca.viewmodel.list

/**
 * Pagination metadata for the current list state.
 *
 * Tracks position within a paginated data set. All fields are optional or defaulted so callers can
 * omit values that the source does not expose (for example a total page count).
 *
 * @property currentPage Most recently loaded page number (one-based). Zero means no page has been
 * fetched yet.
 * @property totalPages Total pages available, or `null` if the source does not expose this.
 * @property totalItems Total item count across all pages, or `null` if unknown.
 * @property hasNextPage True when at least one more page exists beyond [currentPage].
 * @property pageSize Number of items requested per page.
 */
public data class PagingMeta(
    public val currentPage: Int = 0,
    public val totalPages: Int? = null,
    public val totalItems: Int? = null,
    public val hasNextPage: Boolean = false,
    public val pageSize: Int = BaseListViewModel.DEFAULT_PAGE_SIZE,
)
