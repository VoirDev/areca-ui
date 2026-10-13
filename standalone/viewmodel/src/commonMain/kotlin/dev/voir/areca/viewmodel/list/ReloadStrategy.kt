package dev.voir.areca.viewmodel.list

/**
 * Controls how a list screen behaves when filters or sorting change and the list must be reloaded
 * from page 1 by a [BaseListViewModel].
 *
 * Choose based on the UX you want:
 *
 * - [FullScreen]: simpler; the user always knows something is loading.
 * - [Inline]: smoother; the old list stays visible beneath a loading indicator.
 *
 * The `handle*` functions that take a strategy default to [FullScreen].
 */
public enum class ReloadStrategy {

    /**
     * Hides the list behind [dev.voir.areca.viewmodel.UiState.Loading] while the fresh page is
     * fetched. On success the screen returns to [dev.voir.areca.viewmodel.UiState.Display] with the
     * new page; on failure it moves to [dev.voir.areca.viewmodel.UiState.Error], and a retry reloads
     * with the new criteria.
     *
     * Avoids showing stale items under new filter or sort criteria, which can be confusing.
     */
    FullScreen,

    /**
     * Keeps the current items and paging visible with [ListState.isReloading] set while the fresh
     * page is fetched. On success the items are replaced. On failure the previous items, paging,
     * filters, and sorting are restored, [ListState.refreshError] is set, and
     * [BaseListViewModel.onRefreshError] is invoked, the same as for a failed pull-to-refresh.
     *
     * Applies only while the screen shows content; from a loading or error screen the reload is
     * full-screen.
     */
    Inline,
}
