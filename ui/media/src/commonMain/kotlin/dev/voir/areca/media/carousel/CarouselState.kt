package dev.voir.areca.media.carousel

import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Where a [Carousel] is, and the way to move it from outside: an indicator, a thumbnail strip, the
 * viewer it opened, arrows.
 *
 * A carousel turns by **page**. A page is one item, or — with [CarouselSnap.Page] and
 * [CarouselPageSize.PerPage] — a group of items side by side, so indicators count [pageCount] marks
 * and the viewer reports items: use [goToItem] for an item and [currentItem] to read one.
 *
 * With [loop] the pages run on past the last into the first; the indices it reports stay inside
 * the real pages and items.
 *
 * Create one with [rememberCarouselState].
 *
 * @property itemCount Number of items.
 * @property loop Whether the pages wrap round.
 */
@Stable
public class CarouselState internal constructor(
    itemCount: () -> Int,
    public val loop: Boolean,
    initialItem: Int,
    private val scope: CoroutineScope,
) {
    private val count = itemCount

    /** The item shown first, kept so a carousel that groups items can land on its page. */
    internal val initialItem: Int = initialItem

    /** Number of items. */
    public val itemCount: Int
        get() = count()

    /** Items to a page, set by the carousel drawing this state. */
    internal var group: Int = 1

    /** The pager underneath: virtual pages when looping. */
    internal val pagerState: PagerState = PagerState(currentPage = startPage(initialItem)) { pagerPageCount() }

    /** Number of pages: items, or groups of them. */
    public val pageCount: Int
        get() = carouselPageCount(itemCount, group)

    /** Index of the page on screen, among [pageCount]. */
    public val currentPage: Int
        get() = realPage(pagerState.currentPage)

    /** How far the pages have been dragged from [currentPage], in pages, from `-0.5f` to `0.5f`. */
    public val currentPageOffset: Float
        get() = pagerState.currentPageOffsetFraction

    /** Index of the first item on the page on screen. */
    public val currentItem: Int
        get() = (currentPage * group).coerceAtMost((itemCount - 1).coerceAtLeast(0))

    /** Whether a finger or an animation is moving the pages. */
    public val isScrollInProgress: Boolean
        get() = pagerState.isScrollInProgress

    /** How far the page on screen has run while autoplaying, from `0f` to `1f`; `0f` otherwise. */
    public var autoplayProgress: Float by mutableFloatStateOf(0f)
        internal set

    /** Moves to the next page; past the last, to the first when looping, else nowhere. */
    public fun next(): Unit = moveBy(1)

    /** Moves to the previous page; before the first, to the last when looping, else nowhere. */
    public fun previous(): Unit = moveBy(-1)

    /**
     * Moves to the page holding [item].
     *
     * @param item Index of the item; one outside the items is ignored.
     * @param animated Whether the pages slide there or jump — jump when following a viewer.
     */
    public fun goToItem(item: Int, animated: Boolean = true) {
        if (item !in 0 until itemCount) return
        goToPage(item / group, animated)
    }

    /**
     * Moves to [page].
     *
     * @param page Index of the page; one outside the pages is ignored.
     * @param animated Whether the pages slide there or jump.
     */
    public fun goToPage(page: Int, animated: Boolean = true) {
        if (page !in 0 until pageCount) return
        val target = pagerState.currentPage + (page - currentPage)
        scope.launch {
            if (animated) pagerState.animateScrollToPage(target) else pagerState.scrollToPage(target)
        }
    }

    /**
     * Moves [step] pages from where the pages are heading, wrapping when looping.
     *
     * @param step Pages to move, either way.
     */
    private fun moveBy(step: Int) {
        val pages = pageCount
        if (pages <= 1) return
        val from = realPage(pagerState.targetPage)
        val to = from + step
        when {
            loop -> scope.launch { pagerState.animateScrollToPage(pagerState.targetPage + step) }
            to in 0 until pages -> goToPage(to)
        }
    }

    /** Wraps from the last page to the first, for autoplay: the one move that wraps without [loop]. */
    internal suspend fun advance() {
        val pages = pageCount
        if (pages <= 1) return
        if (loop || currentPage < pages - 1) {
            pagerState.animateScrollToPage(pagerState.currentPage + 1)
        } else {
            pagerState.animateScrollToPage(pagerState.currentPage - currentPage)
        }
    }

    /** The page index among the real pages for a pager page. */
    internal fun realPage(pagerPage: Int): Int {
        val pages = pageCount
        return if (pages == 0) 0 else pagerPage.mod(pages)
    }

    /** Pages the pager holds: the real ones, or many rounds of them when looping. */
    private fun pagerPageCount(): Int {
        val pages = pageCount
        return if (loop && pages > 1) pages * LOOP_ROUNDS else pages
    }

    /** The pager page an item starts on: in the middle round when looping, so both ways run long. */
    private fun startPage(item: Int): Int {
        val pages = carouselPageCount(count(), 1)
        val page = item.coerceIn(0, (pages - 1).coerceAtLeast(0))
        return if (loop && pages > 1) pages * (LOOP_ROUNDS / 2) + page else page
    }
}

/**
 * Remembers a [CarouselState] for [itemCount] items.
 *
 * @param itemCount Number of items. May change; the carousel follows it.
 * @param initialItem Item shown first.
 * @param loop Whether the pages wrap round, past the last into the first.
 * @return The state.
 */
@Composable
public fun rememberCarouselState(itemCount: Int, initialItem: Int = 0, loop: Boolean = false): CarouselState {
    val count by rememberUpdatedState(itemCount)
    val scope = rememberCoroutineScope()
    return remember(loop, scope) {
        CarouselState(itemCount = { count }, loop = loop, initialItem = initialItem, scope = scope)
    }
}

/**
 * Pages needed for [itemCount] items, [group] to a page.
 *
 * @param itemCount Number of items.
 * @param group Items to a page, at least one.
 * @return The number of pages, the last one possibly short.
 */
internal fun carouselPageCount(itemCount: Int, group: Int): Int {
    if (itemCount <= 0) return 0
    val size = group.coerceAtLeast(1)
    return (itemCount + size - 1) / size
}

/** Rounds of pages a looping carousel holds: far more than anyone swipes through. */
private const val LOOP_ROUNDS = 1000
