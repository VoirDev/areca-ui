package dev.voir.areca.foundation.navigation

import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Moves a [SwipePager] between its pages, from anywhere on the screen.
 *
 * The pages can always be swiped; this is for everything else that moves them — the button in
 * [PagerControls], a back control in the top bar, a "Skip" that jumps to the end, a link inside a
 * page. Every one of them calls [next], [previous] or [goTo], so a swipe and a press land on the
 * same page and the indicator follows both.
 *
 * Moves are relative to where the pages are heading, not where they are: two quick presses of
 * "Next" advance two pages, even while the first move is still animating.
 *
 * Create one with [rememberPagerController]; it lives as long as the composition that remembered it.
 *
 * @property pagerState The underlying Compose pager state, for a pager or indicator built by hand.
 * @property scope Scope the page moves run in: the remembering composition's, so a move in flight
 *   is cancelled when it leaves.
 */
@Stable
public class PagerController internal constructor(
    public val pagerState: PagerState,
    private val scope: CoroutineScope,
) {
    /** Number of pages. */
    public val pageCount: Int
        get() = pagerState.pageCount

    /** Index of the page on screen; changes as a swipe passes the halfway point. */
    public val currentPage: Int
        get() = pagerState.currentPage

    /** Whether the page on screen is the first. */
    public val isFirstPage: Boolean
        get() = currentPage == 0

    /** Whether the page on screen is the last — where a "Next" usually becomes "Done". */
    public val isLastPage: Boolean
        get() = currentPage == pageCount - 1

    /** Moves to the page after the one the pages are heading to; does nothing past the last. */
    public fun next(): Unit = goTo(pagerState.targetPage + 1)

    /** Moves to the page before the one the pages are heading to; does nothing before the first. */
    public fun previous(): Unit = goTo(pagerState.targetPage - 1)

    /**
     * Moves to [page].
     *
     * @param page Index of the page to show; one outside the pages is ignored rather than clamped,
     * so a "Next" pressed on the last page does not appear to do something.
     * @param animated Whether the pages slide there or jump.
     */
    public fun goTo(page: Int, animated: Boolean = true) {
        val target = pagerMoveTarget(page, pageCount) ?: return
        scope.launch {
            if (animated) pagerState.animateScrollToPage(target) else pagerState.scrollToPage(target)
        }
    }
}

/**
 * The page a [PagerController] move lands on.
 *
 * @param page Page asked for.
 * @param pageCount Number of pages.
 * @return [page] when it is one of the pages, or `null` when the move should be ignored.
 */
internal fun pagerMoveTarget(page: Int, pageCount: Int): Int? =
    page.takeIf { it in 0 until pageCount }

/**
 * Remembers a [PagerController] for [pageCount] pages.
 *
 * @param pageCount Number of pages. May change; the pager follows it.
 * @param initialPage Page shown first.
 * @return A controller for a [SwipePager] and its [PagerControls].
 */
@Composable
public fun rememberPagerController(pageCount: Int, initialPage: Int = 0): PagerController {
    val count by rememberUpdatedState(pageCount)
    val pagerState = rememberPagerState(initialPage = initialPage) { count }
    val scope = rememberCoroutineScope()
    return remember(pagerState, scope) { PagerController(pagerState = pagerState, scope = scope) }
}
