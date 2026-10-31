package dev.voir.areca.foundation.collection

import androidx.compose.foundation.OverscrollEffect
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberOverscrollEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import dev.voir.areca.foundation.layout.VerticalScrollbarOverlay
import dev.voir.areca.theme.ArecaTheme

/**
 * Items to show, keyed by the group they belong to.
 *
 * Groups appear in the map's iteration order, so pass an ordered map — a `LinkedHashMap`, which is
 * what `mapOf` and `associateBy` return — unless the order genuinely does not matter.
 */
public typealias GroupedGridData<Group, Item> = Map<Group, List<Item>>

/**
 * One row of a grouped grid: either a group's header or one of its items.
 *
 * The grid flattens a [GroupedGridData] into a single list of these so the underlying lazy grid sees
 * one homogeneous stream, with headers spanning the full width and items taking one cell each.
 *
 * Both type parameters are carried by both variants. Declaring the unused one as `Nothing` would
 * make neither variant a `GroupedGridItem<Group, Item>`, and every point of use would need an
 * unchecked cast to get one back.
 */
public sealed interface GroupedGridItem<out Group, out Item> {
    /**
     * A group's header, spanning the grid's full width.
     *
     * @property group The group being introduced.
     */
    public data class Header<out Group>(public val group: Group) : GroupedGridItem<Group, Nothing>

    /**
     * One item within a group, taking a single cell.
     *
     * @property item The item to show.
     */
    public data class Entry<out Item>(public val item: Item) : GroupedGridItem<Nothing, Item>
}

/**
 * A vertically scrolling grid whose items are broken into labelled groups.
 *
 * Headers span the grid's full width and items take one cell each, so a group reads as a band across
 * the grid rather than as a cell that happens to hold a title. Everything is flattened into a single
 * lazy list, so only what is on screen is composed however many groups there are.
 *
 * Give [key] whenever the data can change: without it the grid cannot tell an inserted group from a
 * reordered one, and loses scroll position and item state across updates.
 *
 * @param data Items to show, keyed by group. Groups appear in the map's iteration order.
 * @param columns How the grid divides its width.
 * @param modifier Modifier applied to the grid.
 * @param state Scroll position, hoisted so a caller can read or restore it.
 * @param flingBehavior How a fling decelerates.
 * @param reverseLayout Whether to lay out from the bottom up.
 * @param userScrollEnabled Whether the user can scroll. Programmatic scrolling still works.
 * @param overscrollEffect What happens at the ends of the content.
 * @param contentPadding Space inside the scrolling area, which scrolls with the content.
 * @param horizontalArrangement Space between columns.
 * @param verticalArrangement Space between rows.
 * @param header Content placed before the first group.
 * @param footer Content placed after the last group.
 * @param key Stable identity for a header or an item. Must be unique across the whole grid.
 * @param group Renders a group's header.
 * @param item Renders one item.
 */
@Composable
public fun <Group, Item> LazyVerticalGroupedGrid(
    data: GroupedGridData<Group, Item>,
    columns: GridCells,
    modifier: Modifier = Modifier,
    state: LazyGridState = rememberLazyGridState(),
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    reverseLayout: Boolean = false,
    userScrollEnabled: Boolean = true,
    overscrollEffect: OverscrollEffect? = rememberOverscrollEffect(),
    contentPadding: PaddingValues = PaddingValues(horizontal = ArecaTheme.spacing.horizontalContentInsets),
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(ArecaTheme.spacing.xl),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(ArecaTheme.spacing.xl),
    header: (LazyGridScope.() -> Unit)? = null,
    footer: (LazyGridScope.() -> Unit)? = null,
    key: ((GroupedGridItem<Group, Item>) -> Any)? = null,
    group: @Composable (Group) -> Unit,
    item: @Composable (Item) -> Unit,
) {
    val rows: List<GroupedGridItem<Group, Item>> = remember(data) { flattenGroupedGrid(data) }

    Box(modifier = modifier, propagateMinConstraints = true) {
        LazyVerticalGrid(
            columns = columns,
            contentPadding = contentPadding,
            horizontalArrangement = horizontalArrangement,
            verticalArrangement = verticalArrangement,
            state = state,
            flingBehavior = flingBehavior,
            reverseLayout = reverseLayout,
            overscrollEffect = overscrollEffect,
            userScrollEnabled = userScrollEnabled,
        ) {
            header?.let {
                it()
            }

            items(
                items = rows,
                span = { row ->
                    when (row) {
                        is GroupedGridItem.Header -> GridItemSpan(maxLineSpan)
                        is GroupedGridItem.Entry -> GridItemSpan(1)
                    }
                },
                key = key,
                itemContent = { row ->
                    when (row) {
                        is GroupedGridItem.Header -> group(row.group)
                        is GroupedGridItem.Entry -> item(row.item)
                    }
                },
            )

            footer?.let {
                it()
            }
        }
        // The scrollbar's thumb runs top to bottom; a reversed grid would drive it backwards.
        if (!reverseLayout) VerticalScrollbarOverlay(state)
    }
}

/**
 * Flattens grouped data into the single stream a [LazyVerticalGroupedGrid] lays out.
 *
 * Each group contributes its header followed by its items, in the map's iteration order. A group
 * with no items still contributes its header.
 *
 * @param data Items keyed by group.
 * @return Headers and items in display order.
 */
internal fun <Group, Item> flattenGroupedGrid(
    data: GroupedGridData<Group, Item>,
): List<GroupedGridItem<Group, Item>> =
    data.flatMap { (group, items) ->
        buildList {
            add(GroupedGridItem.Header(group))
            items.forEach { item -> add(GroupedGridItem.Entry(item)) }
        }
    }

/**
 * A horizontally scrolling grid whose items are broken into labelled groups.
 *
 * Each group is a column-block: its header sits above a fixed number of [rows], and its items fill
 * those rows top to bottom before starting a new column. Scrolling moves between groups, so this
 * suits a shelf of categories rather than a long flat list.
 *
 * Unlike its vertical counterpart, only the *groups* are lazy — every item of a visible group is
 * composed, because the group's width depends on how many items it holds. Keep groups small.
 *
 * @param data Items to show, keyed by group. Groups appear in the map's iteration order.
 * @param rows How many items stack vertically before a new column starts. Must be positive.
 * @param modifier Modifier applied to the grid.
 * @param state Scroll position, hoisted so a caller can read or restore it.
 * @param contentPadding Space inside the scrolling area, which scrolls with the content.
 * @param horizontalArrangement Space between groups.
 * @param verticalArrangement Space between a group's header and its items.
 * @param itemVerticalSpacing Space between items stacked in one column.
 * @param itemHorizontalSpacing Space between columns within one group.
 * @param userScrollEnabled Whether the user can scroll. Programmatic scrolling still works.
 * @param reverseLayout Whether to lay out from the end backwards.
 * @param flingBehavior How a fling decelerates.
 * @param overscrollEffect What happens at the ends of the content.
 * @param header Content placed before the first group.
 * @param footer Content placed after the last group.
 * @param key Stable identity for a group. Must be unique across the whole grid.
 * @param group Renders a group's header.
 * @param item Renders one item.
 * @throws IllegalArgumentException When [rows] is not positive.
 */
@Composable
public fun <Group, Item> LazyHorizontalGroupedGrid(
    data: GroupedGridData<Group, Item>,
    rows: Int = 5,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(
        horizontal = ArecaTheme.spacing.horizontalContentInsets,
    ),
    horizontalArrangement: Arrangement.Horizontal =
        Arrangement.spacedBy(ArecaTheme.spacing.xl),
    verticalArrangement: Arrangement.Vertical =
        Arrangement.spacedBy(ArecaTheme.spacing.xl),
    itemVerticalSpacing: Dp = ArecaTheme.spacing.xl,
    itemHorizontalSpacing: Dp = ArecaTheme.spacing.xl,
    userScrollEnabled: Boolean = true,
    reverseLayout: Boolean = false,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    overscrollEffect: OverscrollEffect? = rememberOverscrollEffect(),
    header: (LazyListScope.() -> Unit)? = null,
    footer: (LazyListScope.() -> Unit)? = null,
    key: ((Group) -> Any)? = null,
    group: @Composable (Group) -> Unit,
    item: @Composable (Item) -> Unit,
) {
    require(rows > 0) { "rows must be greater than 0" }

    LazyRow(
        modifier = modifier,
        state = state,
        contentPadding = contentPadding,
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = Alignment.Top,
        userScrollEnabled = userScrollEnabled,
        reverseLayout = reverseLayout,
        flingBehavior = flingBehavior,
        overscrollEffect = overscrollEffect,
    ) {
        header?.let { it() }

        items(
            items = data.entries.toList(),
            key = key?.let { keyFn ->
                { entry -> keyFn(entry.key) }
            },
        ) { (groupKey, groupItems) ->
            Column(
                verticalArrangement = verticalArrangement,
            ) {
                group(groupKey)

                Row(
                    horizontalArrangement = Arrangement.spacedBy(itemHorizontalSpacing),
                ) {
                    groupItems
                        .chunked(rows)
                        .forEach { columnItems ->
                            Column(
                                verticalArrangement = Arrangement.spacedBy(itemVerticalSpacing),
                            ) {
                                columnItems.forEach { gridItem ->
                                    item(gridItem)
                                }
                            }
                        }
                }
            }
        }

        footer?.let { it() }
    }
}
