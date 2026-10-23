package dev.voir.areca.charts

import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

/**
 * Scroll position of a [ColumnChart] under [ColumnChartLayout.Scroll].
 *
 * A new state is pinned to the end: it shows the chart's last column — the latest period — and
 * keeps showing it as columns arrive, a month is appended or the chart is resized. Scrolling away
 * unpins it, and from then on the position is the user's: new data, a rotation or a font change do
 * not move it. Scrolling back to the end, or [scrollToEnd], pins it again.
 *
 * The chart scrolls the selected column into view by itself whenever the selected key changes, so
 * "back to the latest period" is selecting the last column; [scrollToEnd] and [scrollToColumn]
 * are for moving without changing the selection.
 *
 * @param initialScroll Scroll position to open at.
 * @param initiallyAtEnd Whether the state starts pinned to the end.
 */
@Stable
public class ColumnChartState internal constructor(initialScroll: Int, initiallyAtEnd: Boolean) {
    /** The scroll the chart's columns run in. */
    internal val scroll: ScrollState = ScrollState(initialScroll)

    /** Whether the position follows the end, as columns arrive and the chart resizes. */
    internal var pinnedToEnd: Boolean by mutableStateOf(initiallyAtEnd)

    /** Distance from one column's start edge to the next one's, in pixels, as last laid out. */
    internal var columnStride: Int by mutableIntStateOf(0)

    /** Width of one column, in pixels, as last laid out. */
    internal var columnWidth: Int by mutableIntStateOf(0)

    /** Space before the first column inside the scroll, in pixels, as last laid out. */
    internal var leadingPadding: Int by mutableIntStateOf(0)

    /** Space after the last column inside the scroll, in pixels, as last laid out. */
    internal var trailingPadding: Int by mutableIntStateOf(0)

    /** How many columns the chart has, as last laid out. */
    internal var columnCount: Int by mutableIntStateOf(0)

    /** Whether the chart is scrolled to its end, where its last column sits. */
    public val isAtEnd: Boolean
        get() = scroll.value >= scroll.maxValue

    /** Whether the chart can scroll at all — its columns are wider than the space it has. */
    public val canScroll: Boolean
        get() = scroll.maxValue > 0

    /**
     * Index of the first column at least partly in view, counted in the chart's `columns`, or `-1`
     * before the chart has been laid out or while it has no columns.
     */
    public val firstVisibleIndex: Int
        get() = visibleRange()?.first ?: -1

    /**
     * Index of the last column at least partly in view, counted in the chart's `columns`, or `-1`
     * before the chart has been laid out or while it has no columns. For a caption of the span in
     * view, or for loading older periods as the reader nears the start.
     */
    public val lastVisibleIndex: Int
        get() = visibleRange()?.last ?: -1

    /**
     * Scrolls to the end, where the last column sits, and pins the position there.
     *
     * @param animate Whether to animate the move rather than jump.
     */
    public suspend fun scrollToEnd(animate: Boolean = true) {
        pinnedToEnd = true
        if (animate) scroll.animateScrollTo(scroll.maxValue) else scroll.scrollTo(scroll.maxValue)
    }

    /**
     * Scrolls the column at [index] fully into view, gutter included at either end, by as little as
     * it takes.
     *
     * Does nothing before the chart has been laid out.
     *
     * @param index Position of the column in the chart's `columns`.
     * @param animate Whether to animate the move rather than jump.
     */
    public suspend fun scrollToColumn(index: Int, animate: Boolean = true) {
        val viewport = scroll.viewportSize
        if (columnStride <= 0 || viewport <= 0 || index !in 0 until columnCount) return
        if (index == columnCount - 1) {
            scrollToEnd(animate)
            return
        }
        val start = leadingPadding + index * columnStride
        val target = ColumnChartMath.reveal(
            start = if (index == 0) 0 else start,
            end = start + columnWidth,
            scroll = scroll.value,
            viewport = viewport,
            max = scroll.maxValue,
        ) ?: return
        pinnedToEnd = target >= scroll.maxValue
        if (animate) scroll.animateScrollTo(target) else scroll.scrollTo(target)
    }

    /** Columns at least partly in view, or `null` before layout. */
    private fun visibleRange(): IntRange? {
        val viewport = scroll.viewportSize
        if (columnStride <= 0 || viewport <= 0 || columnCount <= 0) return null
        return ColumnChartMath.visibleColumns(
            scroll = scroll.value,
            viewport = viewport,
            leadingPadding = leadingPadding,
            stride = columnStride,
            columnWidth = columnWidth,
            count = columnCount,
        )
    }

    internal companion object {
        /** Keeps the scroll position and whether it is pinned across configuration change. */
        val Saver: Saver<ColumnChartState, Any> = listSaver(
            save = { listOf(it.scroll.value, if (it.pinnedToEnd) 1 else 0) },
            restore = { ColumnChartState(initialScroll = it[0], initiallyAtEnd = it[1] == 1) },
        )
    }
}

/**
 * Remembers a [ColumnChart]'s scroll position, pinned to its last column until the user scrolls
 * away.
 *
 * @return A state that survives configuration change.
 */
@Composable
public fun rememberColumnChartState(): ColumnChartState =
    rememberSaveable(saver = ColumnChartState.Saver) {
        ColumnChartState(initialScroll = Int.MAX_VALUE, initiallyAtEnd = true)
    }
