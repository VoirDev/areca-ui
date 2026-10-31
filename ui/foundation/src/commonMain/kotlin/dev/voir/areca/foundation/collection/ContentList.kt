package dev.voir.areca.foundation.collection

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import dev.voir.areca.ExperimentalArecaApi
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.button.ButtonSize
import dev.voir.areca.foundation.feedback.Fallback
import dev.voir.areca.foundation.feedback.Spinner
import dev.voir.areca.foundation.feedback.SpinnerStyle
import dev.voir.areca.foundation.layout.PullToRefreshIndicator
import dev.voir.areca.foundation.layout.PullToRefreshLayout
import dev.voir.areca.foundation.layout.VerticalScrollbarOverlay
import dev.voir.areca.foundation.layout.rememberPullToRefreshState
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.theme.ArecaTheme
import kotlinx.coroutines.flow.distinctUntilChanged

/**
 * What a content list shows: its items and where loading them stands.
 *
 * A list resolves these into one of four faces — loading, error, empty, or the items — so a screen
 * hands over its data and the list picks what to draw. Build one of the two implementations:
 * [LimitedContentListState] for a set loaded in one go, [PagedContentListState] for a set loaded a
 * page at a time.
 *
 * @param T Type of the items.
 */
public sealed interface BaseContentListState<T> {
    /** Items loaded so far, in display order. */
    public val items: List<T>

    /** Whether the first load is in flight. Shows the loading face while [items] is empty. */
    public val loadingInitial: Boolean

    /** Whether a refresh of already loaded items is in flight. Drives a paged list's pull indicator. */
    public val refreshing: Boolean

    /** Why the first load failed, or `null` when it has not. Shows the error face while [items] is empty. */
    public val initialError: String?
}

/**
 * State of a list whose items load in one go.
 *
 * @param T Type of the items.
 * @property items Items to show, in display order.
 * @property loadingInitial Whether the first load is in flight.
 * @property refreshing Whether a refresh of already loaded items is in flight.
 * @property initialError Why the first load failed, or `null` when it has not.
 */
public data class LimitedContentListState<T>(
    override val items: List<T>,
    override val loadingInitial: Boolean = false,
    override val refreshing: Boolean = false,
    override val initialError: String? = null,
) : BaseContentListState<T>

/**
 * State of a list whose items load a page at a time.
 *
 * @param T Type of the items.
 * @property items Items of every page loaded so far, in display order.
 * @property hasNextPage Whether another page exists.
 * @property loadingNextPage Whether the next page is in flight.
 * @property loadMoreError Why the last next-page load failed, or `null` when it has not. While set,
 *   the list stops requesting pages until the user retries.
 * @property loadingInitial Whether the first page is in flight.
 * @property refreshing Whether a refresh of already loaded items is in flight.
 * @property initialError Why the first page failed, or `null` when it has not.
 */
public data class PagedContentListState<T>(
    override val items: List<T>,
    public val hasNextPage: Boolean,
    public val loadingNextPage: Boolean = false,
    public val loadMoreError: String? = null,
    override val loadingInitial: Boolean = false,
    override val refreshing: Boolean = false,
    override val initialError: String? = null,
) : BaseContentListState<T>

/**
 * One group of a grouped content list: a header value and the items under it.
 *
 * @param G Type of the header value.
 * @param T Type of the items.
 * @property key Stable identity of the group, unique within the list.
 * @property group Value the group header renders.
 * @property items Items of the group, in display order.
 */
public data class ContentListGroup<G, T>(
    public val key: Any,
    public val group: G,
    public val items: List<T>,
)

/**
 * Colors of a content list's own pieces: its header and its loading and end-of-list rows.
 *
 * The empty and error faces are [Fallback]s and take their colors from the fallback style.
 *
 * @property headerTitle Color of the header title.
 * @property headerSubtitle Color of the header subtitle.
 * @property spinner Color of the loading spinners.
 * @property endReached Color of the end-of-list line.
 */
@Immutable
public data class ContentListStyle(
    public val headerTitle: Color,
    public val headerSubtitle: Color,
    public val spinner: Color,
    public val endReached: Color,
) {
    /** Theme-backed presets and builders. */
    public companion object {
        /** Theme default: base copy with a muted subtitle and accent spinners. */
        public val Default: ContentListStyle
            @Composable
            get() = default()

        /**
         * Builds a style from the current theme.
         *
         * @param headerTitle Color of the header title.
         * @param headerSubtitle Color of the header subtitle.
         * @param spinner Color of the loading spinners.
         * @param endReached Color of the end-of-list line.
         * @return The style.
         */
        @Composable
        public fun default(
            headerTitle: Color = ArecaTheme.colors.fgBase,
            headerSubtitle: Color = ArecaTheme.colors.fgMuted,
            spinner: Color = ArecaTheme.colors.accentBase,
            endReached: Color = ArecaTheme.colors.fgBase,
        ): ContentListStyle = ContentListStyle(
            headerTitle = headerTitle,
            headerSubtitle = headerSubtitle,
            spinner = spinner,
            endReached = endReached,
        )
    }
}

/**
 * Spacing and type of a content list.
 *
 * @property contentPadding Inset around the items, and around the loading, empty, and error faces.
 *   In a paged list it scrolls with the items.
 * @property itemSpacing Space between consecutive items, group headers included.
 * @property topSpacing Space above the first item.
 * @property bottomSpacing Space below the last item.
 * @property headerPadding Padding inside [ContentListDefaults.Header].
 * @property headerTextGap Space between the header title and its subtitle.
 * @property stateHorizontalPadding Horizontal inset of the loading, empty, and error faces.
 * @property inlineVerticalPadding Vertical padding of the rows a paged list appends: loading more,
 *   load-more error, and end of list.
 * @property headerTitleTextStyle Type of the header title.
 * @property headerSubtitleTextStyle Type of the header subtitle.
 * @property endReachedTextStyle Type of the end-of-list line.
 */
@Immutable
public data class ContentListSize(
    public val contentPadding: PaddingValues,
    public val itemSpacing: Dp,
    public val topSpacing: Dp,
    public val bottomSpacing: Dp,
    public val headerPadding: PaddingValues,
    public val headerTextGap: Dp,
    public val stateHorizontalPadding: Dp,
    public val inlineVerticalPadding: Dp,
    public val headerTitleTextStyle: TextStyle,
    public val headerSubtitleTextStyle: TextStyle,
    public val endReachedTextStyle: TextStyle,
) {
    /** Theme-backed presets and builders. */
    public companion object {
        /** Theme default: tight item spacing and screen-gutter insets on the full-list faces. */
        public val Default: ContentListSize
            @Composable
            get() = default()

        /**
         * Builds a size from the current theme.
         *
         * @param contentPadding Inset around the items.
         * @param itemSpacing Space between consecutive items.
         * @param topSpacing Space above the first item.
         * @param bottomSpacing Space below the last item.
         * @param headerPadding Padding inside the header.
         * @param headerTextGap Space between the header title and its subtitle.
         * @param stateHorizontalPadding Horizontal inset of the full-list faces.
         * @param inlineVerticalPadding Vertical padding of the appended paging rows.
         * @param headerTitleTextStyle Type of the header title.
         * @param headerSubtitleTextStyle Type of the header subtitle.
         * @param endReachedTextStyle Type of the end-of-list line.
         * @return The size.
         */
        @Composable
        public fun default(
            contentPadding: PaddingValues = PaddingValues(ArecaTheme.spacing.none),
            itemSpacing: Dp = ArecaTheme.spacing.xs,
            topSpacing: Dp = ArecaTheme.spacing.xs2,
            bottomSpacing: Dp = ArecaTheme.spacing.md,
            headerPadding: PaddingValues = PaddingValues(ArecaTheme.spacing.none),
            headerTextGap: Dp = ArecaTheme.spacing.xs2,
            stateHorizontalPadding: Dp = ArecaTheme.spacing.horizontalContentInsets,
            inlineVerticalPadding: Dp = ArecaTheme.spacing.lg,
            headerTitleTextStyle: TextStyle = ArecaTheme.typography.headingExtraSmall,
            headerSubtitleTextStyle: TextStyle = ArecaTheme.typography.bodySmall,
            endReachedTextStyle: TextStyle = ArecaTheme.typography.caption,
        ): ContentListSize = ContentListSize(
            contentPadding = contentPadding,
            itemSpacing = itemSpacing,
            topSpacing = topSpacing,
            bottomSpacing = bottomSpacing,
            headerPadding = headerPadding,
            headerTextGap = headerTextGap,
            stateHorizontalPadding = stateHorizontalPadding,
            inlineVerticalPadding = inlineVerticalPadding,
            headerTitleTextStyle = headerTitleTextStyle,
            headerSubtitleTextStyle = headerSubtitleTextStyle,
            endReachedTextStyle = endReachedTextStyle,
        )
    }
}

/**
 * Copy a content list shows in its default faces.
 *
 * The library ships no localization, so the words are parameters: pass your own translations, or
 * [English] for an English-only product.
 *
 * @property emptyTitle Heading of the empty face.
 * @property errorTitle Heading of the first-load error face.
 * @property retry Label of the retry buttons.
 * @property loadMoreErrorTitle Heading of the row shown when a next page fails to load.
 * @property endReached Line shown after the last page.
 */
@Immutable
public data class ContentListLabels(
    public val emptyTitle: String,
    public val errorTitle: String,
    public val retry: String,
    public val loadMoreErrorTitle: String,
    public val endReached: String,
) {
    /** Built-in wordings. */
    public companion object {
        /** English wording. */
        public val English: ContentListLabels = ContentListLabels(
            emptyTitle = "Nothing here yet",
            errorTitle = "Something went wrong",
            retry = "Try again",
            loadMoreErrorTitle = "Couldn’t load more items",
            endReached = "You reached the end",
        )
    }
}

/**
 * Default [ContentListStyle] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider].
 *
 * A content list provides its own style to its subtree, so the [ContentListDefaults] faces in its
 * slots match it.
 */
public val LocalContentListStyle: ProvidableCompositionLocal<ContentListStyle?> =
    compositionLocalOf { null }

/**
 * Default [ContentListSize] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider].
 *
 * A content list provides its own size to its subtree, so the [ContentListDefaults] faces in its
 * slots match it.
 */
public val LocalContentListSize: ProvidableCompositionLocal<ContentListSize?> =
    compositionLocalOf { null }

/**
 * Building blocks for a content list's slots: a header and the default loading, empty, error, and
 * paging faces.
 *
 * Each reads the [ContentListStyle] and [ContentListSize] in scope, so inside a list it matches
 * that list, and standalone it uses the theme's.
 */
public object ContentListDefaults {

    /**
     * Title row for the top of a list, with an optional subtitle and trailing actions.
     *
     * @param title Heading of the list.
     * @param modifier Modifier applied to the row.
     * @param subtitle Supporting line below [title], or `null` for none.
     * @param actions Trailing content, such as a "See all" button, or `null` for none.
     */
    @Composable
    public fun Header(
        title: String,
        modifier: Modifier = Modifier,
        subtitle: String? = null,
        actions: (@Composable RowScope.() -> Unit)? = null,
    ) {
        val style = currentContentListStyle()
        val size = currentContentListSize()

        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(size.headerPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = size.headerTitleTextStyle,
                    color = style.headerTitle,
                )

                if (subtitle != null) {
                    Spacer(Modifier.height(size.headerTextGap))
                    Text(
                        text = subtitle,
                        style = size.headerSubtitleTextStyle,
                        color = style.headerSubtitle,
                    )
                }
            }

            if (actions != null) {
                actions()
            }
        }
    }

    /**
     * Face shown while the first load is in flight: a centered spinner.
     *
     * @param modifier Modifier applied to the face, which fills the space it is given.
     * @param color Color of the spinner. Defaults to the style in scope.
     */
    @Composable
    public fun LoadingState(
        modifier: Modifier = Modifier,
        color: Color = currentContentListStyle().spinner,
    ) {
        val size = currentContentListSize()

        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = size.stateHorizontalPadding),
            contentAlignment = Alignment.Center,
        ) {
            Spinner(style = SpinnerStyle(color = color))
        }
    }

    /**
     * Face shown when a list has finished loading and holds nothing.
     *
     * @param modifier Modifier applied to the face, which fills the space it is given.
     * @param title Heading of the face.
     * @param subtitle Supporting copy below [title], or `null` for none.
     */
    @Composable
    public fun EmptyState(
        modifier: Modifier = Modifier,
        title: String = ContentListLabels.English.emptyTitle,
        subtitle: String? = null,
    ) {
        val size = currentContentListSize()

        Fallback(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = size.stateHorizontalPadding),
            heading = { Text(title) },
            body = subtitle?.let { { Text(it) } },
        )
    }

    /**
     * Face shown when the first load failed, with a retry button.
     *
     * @param message Why it failed, shown below the heading, or `null` for no detail.
     * @param onRetry Called when the retry button is pressed.
     * @param modifier Modifier applied to the face, which fills the space it is given.
     * @param title Heading of the face.
     * @param actionText Label of the retry button.
     */
    @Composable
    public fun ErrorState(
        message: String?,
        onRetry: () -> Unit,
        modifier: Modifier = Modifier,
        title: String = ContentListLabels.English.errorTitle,
        actionText: String = ContentListLabels.English.retry,
    ) {
        val size = currentContentListSize()

        Fallback(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = size.stateHorizontalPadding),
            heading = { Text(title) },
            body = message?.let { { Text(it) } },
            content = {
                Button(onClick = onRetry, size = ButtonSize.Small) {
                    Text(actionText)
                }
            }
        )
    }

    /**
     * Row a paged list appends while its next page is in flight.
     *
     * @param modifier Modifier applied to the row.
     * @param color Color of the spinner. Defaults to the style in scope.
     */
    @Composable
    public fun LoadMoreState(
        modifier: Modifier = Modifier,
        color: Color = currentContentListStyle().spinner,
    ) {
        val size = currentContentListSize()

        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = size.inlineVerticalPadding),
            contentAlignment = Alignment.Center,
        ) {
            Spinner(style = SpinnerStyle(color = color))
        }
    }

    /**
     * Row a paged list appends when its next page failed, with a retry button.
     *
     * @param message Why it failed, shown below the heading.
     * @param onRetry Called when the retry button is pressed.
     * @param modifier Modifier applied to the row.
     * @param title Heading of the row.
     * @param actionText Label of the retry button.
     */
    @Composable
    public fun LoadMoreErrorState(
        message: String,
        onRetry: () -> Unit,
        modifier: Modifier = Modifier,
        title: String = ContentListLabels.English.loadMoreErrorTitle,
        actionText: String = ContentListLabels.English.retry,
    ) {
        val size = currentContentListSize()

        Fallback(
            modifier = modifier
                .fillMaxSize()
                .padding(
                    vertical = size.inlineVerticalPadding,
                    horizontal = size.stateHorizontalPadding
                ),
            heading = { Text(title) },
            body = { Text(message) },
            content = {
                Button(onClick = onRetry) {
                    Text(actionText)
                }
            }
        )
    }

    /**
     * Row a paged list appends after its last page.
     *
     * @param modifier Modifier applied to the row.
     * @param text Line shown in the row.
     */
    @Composable
    public fun EndReachedState(
        modifier: Modifier = Modifier,
        text: String = ContentListLabels.English.endReached,
    ) {
        val style = currentContentListStyle()
        val size = currentContentListSize()

        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = size.inlineVerticalPadding),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = text,
                style = size.endReachedTextStyle,
                color = style.endReached,
            )
        }
    }
}

/**
 * Style in scope, or the theme default outside any list and theme.
 *
 * @return Style to draw the calling face with.
 */
@Composable
private fun currentContentListStyle(): ContentListStyle =
    LocalContentListStyle.current ?: ContentListStyle.Default

/**
 * Size in scope, or the theme default outside any list and theme.
 *
 * @return Size to lay the calling face out with.
 */
@Composable
private fun currentContentListSize(): ContentListSize =
    LocalContentListSize.current ?: ContentListSize.Default

/**
 * A list that resolves loading, error, empty, and loaded into one component, composing every item
 * eagerly.
 *
 * Items are laid out in a plain column, so the list is meant to sit inside a scrolling parent — a
 * section of a screen rather than the whole of one. For pull-to-refresh, wrap that scrolling parent
 * in a [PullToRefreshLayout]: the gesture belongs to whatever scrolls. Use [PagedContentList] for a
 * set long enough to need lazy composition.
 *
 * @param T Type of the items.
 * @param state Items and where loading them stands.
 * @param modifier Modifier applied to the list.
 * @param style Colors of the header and faces. Defaults to the theme's.
 * @param size Spacing and type. Defaults to the theme's.
 * @param labels Copy of the default faces.
 * @param fillAvailableHeight Whether the list, and its loading, empty, and error faces, fill the
 *   height they are given rather than wrapping their content.
 * @param onRetryInitial Called by the default error face's retry button.
 * @param loadingContent Face shown during the first load.
 * @param emptyContent Face shown when there is nothing to show.
 * @param initialErrorContent Face shown when the first load failed, given the message and a retry.
 * @param header Content above the items and faces, such as a [ContentListDefaults.Header].
 * @param itemContent Renders one item.
 */
@Composable
public fun <T> ContentList(
    state: LimitedContentListState<T>,
    modifier: Modifier = Modifier,
    style: ContentListStyle = LocalContentListStyle.current ?: ContentListStyle.Default,
    size: ContentListSize = LocalContentListSize.current ?: ContentListSize.Default,
    labels: ContentListLabels = ContentListLabels.English,
    fillAvailableHeight: Boolean = false,
    onRetryInitial: () -> Unit = {},
    loadingContent: @Composable () -> Unit = { ContentListDefaults.LoadingState() },
    emptyContent: @Composable () -> Unit = {
        ContentListDefaults.EmptyState(title = labels.emptyTitle)
    },
    initialErrorContent: @Composable (String?, () -> Unit) -> Unit = { message, retry ->
        ContentListDefaults.ErrorState(
            message = message,
            onRetry = retry,
            title = labels.errorTitle,
            actionText = labels.retry,
        )
    },
    header: (@Composable ColumnScope.() -> Unit)? = null,
    itemContent: @Composable (T) -> Unit,
) {
    ProvideContentListConfig(style = style, size = size) {
        BaseListScaffold(
            state = state,
            modifier = modifier,
            facePadding = size.contentPadding,
            fillAvailableHeight = fillAvailableHeight,
            onRetryInitial = onRetryInitial,
            loadingContent = loadingContent,
            emptyContent = emptyContent,
            initialErrorContent = initialErrorContent,
            header = header,
        ) {
            EagerItemsColumn(size = size) {
                state.items.forEach { item ->
                    itemContent(item)
                }
            }
        }
    }
}

/**
 * A [ContentList] whose items are broken into groups, each under its own header.
 *
 * @param G Type of the group header values.
 * @param T Type of the items.
 * @param state Groups and where loading them stands.
 * @param modifier Modifier applied to the list.
 * @param style Colors of the header and faces. Defaults to the theme's.
 * @param size Spacing and type. Defaults to the theme's.
 * @param labels Copy of the default faces.
 * @param fillAvailableHeight Whether the list, and its loading, empty, and error faces, fill the
 *   height they are given rather than wrapping their content.
 * @param onRetryInitial Called by the default error face's retry button.
 * @param loadingContent Face shown during the first load.
 * @param emptyContent Face shown when there is nothing to show.
 * @param initialErrorContent Face shown when the first load failed, given the message and a retry.
 * @param header Content above the groups and faces, such as a [ContentListDefaults.Header].
 * @param groupHeaderContent Renders a group's header.
 * @param itemContent Renders one item.
 */
@Composable
public fun <G, T> GroupedContentList(
    state: LimitedContentListState<ContentListGroup<G, T>>,
    modifier: Modifier = Modifier,
    style: ContentListStyle = LocalContentListStyle.current ?: ContentListStyle.Default,
    size: ContentListSize = LocalContentListSize.current ?: ContentListSize.Default,
    labels: ContentListLabels = ContentListLabels.English,
    fillAvailableHeight: Boolean = false,
    onRetryInitial: () -> Unit = {},
    loadingContent: @Composable () -> Unit = { ContentListDefaults.LoadingState() },
    emptyContent: @Composable () -> Unit = {
        ContentListDefaults.EmptyState(title = labels.emptyTitle)
    },
    initialErrorContent: @Composable (String?, () -> Unit) -> Unit = { message, retry ->
        ContentListDefaults.ErrorState(
            message = message,
            onRetry = retry,
            title = labels.errorTitle,
            actionText = labels.retry,
        )
    },
    header: (@Composable ColumnScope.() -> Unit)? = null,
    groupHeaderContent: @Composable (G) -> Unit,
    itemContent: @Composable (T) -> Unit,
) {
    ProvideContentListConfig(style = style, size = size) {
        BaseListScaffold(
            state = state,
            modifier = modifier,
            facePadding = size.contentPadding,
            fillAvailableHeight = fillAvailableHeight,
            onRetryInitial = onRetryInitial,
            loadingContent = loadingContent,
            emptyContent = emptyContent,
            initialErrorContent = initialErrorContent,
            header = header,
        ) {
            EagerItemsColumn(size = size) {
                state.items.forEach { group ->
                    groupHeaderContent(group.group)
                    group.items.forEach { item ->
                        itemContent(item)
                    }
                }
            }
        }
    }
}

/**
 * A list that loads a page at a time, resolving loading, error, empty, and loaded into one
 * component and composing items lazily.
 *
 * The next page is requested through [onLoadNextPage] as the user nears the end, while the state
 * says another page exists and nothing is loading or failed. With [onRefresh] set, pulling down at
 * the top of the items requests a refresh, and the indicator shows while
 * [PagedContentListState.refreshing] is `true`.
 *
 * @param T Type of the items.
 * @param state Items and where loading them stands.
 * @param modifier Modifier applied to the list.
 * @param style Colors of the header and faces. Defaults to the theme's.
 * @param size Spacing and type. Defaults to the theme's.
 * @param labels Copy of the default faces.
 * @param contentPadding Inset the caller adds around the items, on top of
 *   [ContentListSize.contentPadding] — typically the height of a translucent top bar and the
 *   system bars, so the items scroll under them yet rest clear of them. The items scroll with it;
 *   the loading, empty, and error faces are inset by it. [header] is not: it sits above the
 *   scrolling items, so a list with a header under a bar insets the header itself.
 * @param fillAvailableHeight Whether the list, and its loading, empty, and error faces, fill the
 *   height they are given rather than wrapping their content.
 * @param listState Scroll state, hoisted so a caller can scroll to an item.
 * @param onRefresh Called when the user pulls down to refresh, or `null` for no pull-to-refresh.
 * @param onLoadNextPage Called when the next page is due.
 * @param onRetryInitial Called by the default error face's retry button.
 * @param onRetryLoadMore Called by the default load-more error row's retry button.
 * @param itemKey Stable identity of an item, unique across the list, or `null` to key by position.
 * @param loadingContent Face shown during the first load.
 * @param emptyContent Face shown when there is nothing to show.
 * @param initialErrorContent Face shown when the first load failed, given the message and a retry.
 * @param loadMoreContent Row appended while the next page loads.
 * @param loadMoreErrorContent Row appended when the next page failed, given the message and a retry.
 * @param endReachedContent Row appended after the last page.
 * @param header Content above the items and faces, such as a [ContentListDefaults.Header]. It does
 *   not scroll with the items.
 * @param itemContent Renders one item.
 */
@Composable
public fun <T> PagedContentList(
    state: PagedContentListState<T>,
    modifier: Modifier = Modifier,
    style: ContentListStyle = LocalContentListStyle.current ?: ContentListStyle.Default,
    size: ContentListSize = LocalContentListSize.current ?: ContentListSize.Default,
    labels: ContentListLabels = ContentListLabels.English,
    contentPadding: PaddingValues = PaddingValues(ArecaTheme.spacing.none),
    fillAvailableHeight: Boolean = true,
    listState: LazyListState = rememberLazyListState(),
    onRefresh: (() -> Unit)? = null,
    onLoadNextPage: () -> Unit = {},
    onRetryInitial: () -> Unit = onRefresh ?: {},
    onRetryLoadMore: () -> Unit = onLoadNextPage,
    itemKey: ((T) -> Any)? = null,
    loadingContent: @Composable () -> Unit = { ContentListDefaults.LoadingState() },
    emptyContent: @Composable () -> Unit = {
        ContentListDefaults.EmptyState(title = labels.emptyTitle)
    },
    initialErrorContent: @Composable (String?, () -> Unit) -> Unit = { message, retry ->
        ContentListDefaults.ErrorState(
            message = message,
            onRetry = retry,
            title = labels.errorTitle,
            actionText = labels.retry,
        )
    },
    loadMoreContent: @Composable () -> Unit = { ContentListDefaults.LoadMoreState() },
    loadMoreErrorContent: @Composable (String, () -> Unit) -> Unit = { message, retry ->
        ContentListDefaults.LoadMoreErrorState(
            message = message,
            onRetry = retry,
            title = labels.loadMoreErrorTitle,
            actionText = labels.retry,
        )
    },
    endReachedContent: @Composable () -> Unit = {
        ContentListDefaults.EndReachedState(text = labels.endReached)
    },
    header: (@Composable ColumnScope.() -> Unit)? = null,
    itemContent: @Composable (T) -> Unit,
) {
    ProvideContentListConfig(style = style, size = size) {
        BindLoadNextPageEffect(
            state = state,
            listState = listState,
            onLoadNextPage = onLoadNextPage,
        )

        val padding = remember(size.contentPadding, contentPadding) {
            size.contentPadding.plusPadding(contentPadding)
        }

        BaseListScaffold(
            state = state,
            modifier = modifier,
            facePadding = padding,
            fillAvailableHeight = fillAvailableHeight,
            onRetryInitial = onRetryInitial,
            loadingContent = loadingContent,
            emptyContent = emptyContent,
            initialErrorContent = initialErrorContent,
            header = header,
        ) {
            Refreshable(
                refreshing = state.refreshing,
                onRefresh = onRefresh,
                indicatorTopInset = contentPadding.calculateTopPadding(),
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = padding,
                        verticalArrangement = Arrangement.spacedBy(size.itemSpacing),
                    ) {
                        item(key = TopSpacingKey) {
                            Spacer(Modifier.height(size.topSpacing))
                        }

                        items(
                            items = state.items,
                            key = itemKey,
                        ) { item ->
                            itemContent(item)
                        }

                        appendPagingItems(
                            state = state,
                            onRetryLoadMore = onRetryLoadMore,
                            loadMoreContent = loadMoreContent,
                            loadMoreErrorContent = loadMoreErrorContent,
                            endReachedContent = endReachedContent,
                        )

                        item(key = BottomSpacingKey) {
                            Spacer(Modifier.height(size.bottomSpacing))
                        }
                    }
                    VerticalScrollbarOverlay(listState)
                }
            }
        }
    }
}

/**
 * A [PagedContentList] whose items are broken into groups, each under a header that sticks to the
 * top while its group scrolls past.
 *
 * @param G Type of the group header values.
 * @param T Type of the items.
 * @param state Groups and where loading them stands.
 * @param modifier Modifier applied to the list.
 * @param style Colors of the header and faces. Defaults to the theme's.
 * @param size Spacing and type. Defaults to the theme's.
 * @param labels Copy of the default faces.
 * @param contentPadding Inset the caller adds around the items, on top of
 *   [ContentListSize.contentPadding] — typically the height of a translucent top bar and the
 *   system bars, so the items scroll under them yet rest clear of them. The items scroll with it;
 *   the loading, empty, and error faces are inset by it. [header] is not: it sits above the
 *   scrolling items, so a list with a header under a bar insets the header itself.
 * @param fillAvailableHeight Whether the list, and its loading, empty, and error faces, fill the
 *   height they are given rather than wrapping their content.
 * @param listState Scroll state, hoisted so a caller can scroll to an item.
 * @param onRefresh Called when the user pulls down to refresh, or `null` for no pull-to-refresh.
 * @param onLoadNextPage Called when the next page is due.
 * @param onRetryInitial Called by the default error face's retry button.
 * @param onRetryLoadMore Called by the default load-more error row's retry button.
 * @param itemKey Stable identity of an item, unique across the list, or `null` to key by position.
 * @param loadingContent Face shown during the first load.
 * @param emptyContent Face shown when there is nothing to show.
 * @param initialErrorContent Face shown when the first load failed, given the message and a retry.
 * @param loadMoreContent Row appended while the next page loads.
 * @param loadMoreErrorContent Row appended when the next page failed, given the message and a retry.
 * @param endReachedContent Row appended after the last page.
 * @param header Content above the groups and faces, such as a [ContentListDefaults.Header]. It does
 *   not scroll with the items.
 * @param groupHeaderContent Renders a group's sticky header.
 * @param itemContent Renders one item.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
public fun <G, T> GroupedPagedContentList(
    state: PagedContentListState<ContentListGroup<G, T>>,
    modifier: Modifier = Modifier,
    style: ContentListStyle = LocalContentListStyle.current ?: ContentListStyle.Default,
    size: ContentListSize = LocalContentListSize.current ?: ContentListSize.Default,
    labels: ContentListLabels = ContentListLabels.English,
    contentPadding: PaddingValues = PaddingValues(ArecaTheme.spacing.none),
    fillAvailableHeight: Boolean = true,
    listState: LazyListState = rememberLazyListState(),
    onRefresh: (() -> Unit)? = null,
    onLoadNextPage: () -> Unit = {},
    onRetryInitial: () -> Unit = onRefresh ?: {},
    onRetryLoadMore: () -> Unit = onLoadNextPage,
    itemKey: ((T) -> Any)? = null,
    loadingContent: @Composable () -> Unit = { ContentListDefaults.LoadingState() },
    emptyContent: @Composable () -> Unit = {
        ContentListDefaults.EmptyState(title = labels.emptyTitle)
    },
    initialErrorContent: @Composable (String?, () -> Unit) -> Unit = { message, retry ->
        ContentListDefaults.ErrorState(
            message = message,
            onRetry = retry,
            title = labels.errorTitle,
            actionText = labels.retry,
        )
    },
    loadMoreContent: @Composable () -> Unit = { ContentListDefaults.LoadMoreState() },
    loadMoreErrorContent: @Composable (String, () -> Unit) -> Unit = { message, retry ->
        ContentListDefaults.LoadMoreErrorState(
            message = message,
            onRetry = retry,
            title = labels.loadMoreErrorTitle,
            actionText = labels.retry,
        )
    },
    endReachedContent: @Composable () -> Unit = {
        ContentListDefaults.EndReachedState(text = labels.endReached)
    },
    header: (@Composable ColumnScope.() -> Unit)? = null,
    groupHeaderContent: @Composable (G) -> Unit,
    itemContent: @Composable (T) -> Unit,
) {
    ProvideContentListConfig(style = style, size = size) {
        BindLoadNextPageEffect(
            state = state,
            listState = listState,
            onLoadNextPage = onLoadNextPage,
        )

        val padding = remember(size.contentPadding, contentPadding) {
            size.contentPadding.plusPadding(contentPadding)
        }

        BaseListScaffold(
            state = state,
            modifier = modifier,
            facePadding = padding,
            fillAvailableHeight = fillAvailableHeight,
            onRetryInitial = onRetryInitial,
            loadingContent = loadingContent,
            emptyContent = emptyContent,
            initialErrorContent = initialErrorContent,
            header = header,
        ) {
            Refreshable(
                refreshing = state.refreshing,
                onRefresh = onRefresh,
                indicatorTopInset = contentPadding.calculateTopPadding(),
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = padding,
                        verticalArrangement = Arrangement.spacedBy(size.itemSpacing),
                    ) {
                        item(key = TopSpacingKey) {
                            Spacer(Modifier.height(size.topSpacing))
                        }

                        state.items.forEach { group ->
                            stickyHeader(key = "group-header:${group.key}") {
                                groupHeaderContent(group.group)
                            }

                            items(
                                items = group.items,
                                key = itemKey,
                            ) { item ->
                                itemContent(item)
                            }
                        }

                        appendPagingItems(
                            state = state,
                            onRetryLoadMore = onRetryLoadMore,
                            loadMoreContent = loadMoreContent,
                            loadMoreErrorContent = loadMoreErrorContent,
                            endReachedContent = endReachedContent,
                        )

                        item(key = BottomSpacingKey) {
                            Spacer(Modifier.height(size.bottomSpacing))
                        }
                    }
                    VerticalScrollbarOverlay(listState)
                }
            }
        }
    }
}

/**
 * Provides a list's style and size to its subtree, so the default faces in its slots match it.
 *
 * @param style Style of the list.
 * @param size Size of the list.
 * @param content The list.
 */
@Composable
private fun ProvideContentListConfig(
    style: ContentListStyle,
    size: ContentListSize,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalContentListStyle provides style,
        LocalContentListSize provides size,
        content = content,
    )
}

/**
 * Column of eagerly composed items, padded and spaced by [size].
 *
 * @param size Spacing of the list.
 * @param content Items, and group headers for a grouped list.
 */
@Composable
private fun EagerItemsColumn(
    size: ContentListSize,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(size.contentPadding),
        verticalArrangement = Arrangement.spacedBy(size.itemSpacing),
    ) {
        Spacer(Modifier.height(size.topSpacing))
        content()
        Spacer(Modifier.height(size.bottomSpacing))
    }
}

/**
 * Wraps the scrolling items of a paged list in a [PullToRefreshLayout] when [onRefresh] is set.
 *
 * The indicator emerges [indicatorTopInset] below the top, clipped above that line, so a list whose
 * items scroll under a translucent bar pulls its indicator out from under the bar rather than
 * behind it.
 *
 * @param refreshing Whether a refresh is in flight.
 * @param onRefresh Refresh request, or `null` to render [content] as it is.
 * @param indicatorTopInset Distance from the top at which the indicator appears.
 * @param content The scrolling items.
 */
@OptIn(ExperimentalArecaApi::class)
@Composable
private fun Refreshable(
    refreshing: Boolean,
    onRefresh: (() -> Unit)?,
    indicatorTopInset: Dp,
    content: @Composable () -> Unit,
) {
    if (onRefresh == null) {
        content()
    } else {
        val pullState = rememberPullToRefreshState()

        PullToRefreshLayout(
            isRefreshing = refreshing,
            onRefresh = onRefresh,
            modifier = Modifier.fillMaxSize(),
            state = pullState,
            indicator = {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .padding(top = indicatorTopInset)
                        .clipToBounds(),
                ) {
                    PullToRefreshIndicator(
                        state = pullState,
                        isRefreshing = refreshing,
                        modifier = Modifier.align(Alignment.TopCenter),
                    )
                }
            },
        ) {
            content()
        }
    }
}

/**
 * Header followed by whichever face [state] calls for: loading, error, empty, or [content].
 *
 * The full-list faces show only while there are no items, so a refresh or a later failure never
 * blanks a list that already has something to show.
 *
 * @param T Type of the items.
 * @param state Items and where loading them stands.
 * @param modifier Modifier applied to the scaffold.
 * @param facePadding Inset around the loading, empty, and error faces, matching the items'.
 * @param fillAvailableHeight Whether the scaffold and its faces fill the height they are given.
 * @param onRetryInitial Retry handed to [initialErrorContent].
 * @param loadingContent Face shown during the first load.
 * @param emptyContent Face shown when there is nothing to show.
 * @param initialErrorContent Face shown when the first load failed.
 * @param header Content above the faces and items.
 * @param content The items.
 */
@Composable
private fun <T> BaseListScaffold(
    state: BaseContentListState<T>,
    modifier: Modifier,
    facePadding: PaddingValues,
    fillAvailableHeight: Boolean,
    onRetryInitial: () -> Unit,
    loadingContent: @Composable () -> Unit,
    emptyContent: @Composable () -> Unit,
    initialErrorContent: @Composable (String?, () -> Unit) -> Unit,
    header: (@Composable ColumnScope.() -> Unit)?,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = if (fillAvailableHeight) {
            modifier.fillMaxSize()
        } else {
            modifier.fillMaxWidth()
        }
    ) {
        if (header != null) {
            header()
        }

        val faceModifier = if (fillAvailableHeight) {
            Modifier.weight(1f)
        } else {
            Modifier.fillMaxWidth()
        }.padding(facePadding)
        val initialError = state.initialError

        when {
            state.loadingInitial && state.items.isEmpty() -> {
                Box(modifier = faceModifier) {
                    loadingContent()
                }
            }

            initialError != null && state.items.isEmpty() -> {
                Box(modifier = faceModifier) {
                    initialErrorContent(initialError, onRetryInitial)
                }
            }

            state.items.isEmpty() -> {
                Box(modifier = faceModifier) {
                    emptyContent()
                }
            }

            else -> content()
        }
    }
}

/**
 * Requests the next page whenever the list nears its end with the paging gate open.
 *
 * @param T Type of the items.
 * @param state Paging state, read inside a snapshot so a finished load re-evaluates the gate.
 * @param listState Scroll state of the list.
 * @param onLoadNextPage Called when the next page is due.
 */
@Composable
private fun <T> BindLoadNextPageEffect(
    state: PagedContentListState<T>,
    listState: LazyListState,
    onLoadNextPage: () -> Unit,
) {
    val currentState by rememberUpdatedState(state)
    val currentOnLoadNextPage by rememberUpdatedState(onLoadNextPage)

    LaunchedEffect(listState) {
        // The paging gate is read inside the snapshot too, so a load finishing while the list is
        // still near its end (or a first page too short to scroll) flips the result and fires again.
        // The emitted key is the item count while a load is due, so an appended page re-arms the
        // trigger even for a caller that never toggles `loadingNextPage`.
        snapshotFlow {
            val layoutInfo = listState.layoutInfo
            val pagingState = currentState
            val isDue = shouldLoadNextPage(
                totalItemsCount = layoutInfo.totalItemsCount,
                lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1,
                hasNextPage = pagingState.hasNextPage,
                isLoading = pagingState.loadingInitial || pagingState.loadingNextPage || pagingState.refreshing,
                hasLoadMoreError = pagingState.loadMoreError != null,
            )
            if (isDue) layoutInfo.totalItemsCount else null
        }
            .distinctUntilChanged()
            .collect { dueAtCount ->
                if (dueAtCount != null) currentOnLoadNextPage()
            }
    }
}

/**
 * Number of trailing items that count as "near the end" of a paged list.
 */
private const val LoadNextPageThreshold = 4

/** Lazy key of the spacer above the first item. */
private const val TopSpacingKey = "top-spacing"

/** Lazy key of the spacer below the last item. */
private const val BottomSpacingKey = "bottom-spacing"

/**
 * Decides whether a paged list should request its next page.
 *
 * A request is due when the last visible item is within [LoadNextPageThreshold] items of the end
 * and the paging gate is open: more pages exist, nothing is loading, and no load-more error is
 * waiting for a retry.
 *
 * @param totalItemsCount Items in the lazy list, including its spacer and paging items.
 * @param lastVisibleItemIndex Index of the last visible item, or `-1` when none is visible.
 * @param hasNextPage Whether another page exists.
 * @param isLoading Whether the first page, a next page, or a refresh is in flight.
 * @param hasLoadMoreError Whether the last load-more attempt failed and awaits a retry.
 * @return `true` when the next page should be requested.
 */
internal fun shouldLoadNextPage(
    totalItemsCount: Int,
    lastVisibleItemIndex: Int,
    hasNextPage: Boolean,
    isLoading: Boolean,
    hasLoadMoreError: Boolean,
): Boolean {
    val isNearEnd = totalItemsCount > 0 &&
        lastVisibleItemIndex >= totalItemsCount - LoadNextPageThreshold
    return isNearEnd && hasNextPage && !isLoading && !hasLoadMoreError
}

/**
 * Appends the loading-more, load-more error, and end-of-list rows that [state] calls for.
 *
 * @param T Type of the items.
 * @param state Paging state.
 * @param onRetryLoadMore Retry handed to [loadMoreErrorContent].
 * @param loadMoreContent Row shown while the next page loads.
 * @param loadMoreErrorContent Row shown when the next page failed.
 * @param endReachedContent Row shown after the last page.
 */
private fun <T> LazyListScope.appendPagingItems(
    state: PagedContentListState<T>,
    onRetryLoadMore: () -> Unit,
    loadMoreContent: @Composable () -> Unit,
    loadMoreErrorContent: @Composable (String, () -> Unit) -> Unit,
    endReachedContent: @Composable () -> Unit,
) {
    if (state.loadingNextPage) {
        item(key = "paging-loader") {
            loadMoreContent()
        }
    }

    val loadMoreError = state.loadMoreError
    if (loadMoreError != null && state.items.isNotEmpty()) {
        item(key = "paging-error") {
            loadMoreErrorContent(loadMoreError, onRetryLoadMore)
        }
    }

    if (!state.hasNextPage && !state.loadingNextPage && loadMoreError == null && state.items.isNotEmpty()) {
        item(key = "paging-end") {
            endReachedContent()
        }
    }
}
