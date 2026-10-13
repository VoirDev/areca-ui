package dev.voir.areca.foundation.navigation

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.lerp
import kotlin.math.abs

/**
 * A row of marks saying which of a few pages is on screen — the dots under an onboarding carousel.
 *
 * The mark for the current page is longer and colored, so it reads without color too. Pass
 * [currentPageOffset] while the pages are being dragged and the long mark slides between its
 * neighbours with the finger; changed without an offset, it glides to its new place. Prefer the
 * [PagerState] overload when the pages are a pager, which does both.
 *
 * The marks are decoration, not controls: they cannot be tapped, and assistive technology hears the
 * row as one element, named by [contentDescription] — "Page 2 of 4". A page change still needs its
 * own announcement if it matters.
 *
 * @param pageCount Number of pages, at least one.
 * @param currentPage Index of the page on screen, coerced into `0 until pageCount`.
 * @param modifier Modifier applied to the row.
 * @param currentPageOffset How far the pages have been dragged from [currentPage], in pages, from
 * `-0.5f` to `0.5f`. `0f` when they are at rest.
 * @param contentDescription What the row says to assistive technology.
 * @param style Colors of the marks.
 * @param size Metrics of the marks.
 * @throws IllegalArgumentException When [pageCount] is less than one.
 */
@Composable
public fun PageIndicator(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier,
    currentPageOffset: Float = 0f,
    contentDescription: String? = null,
    style: PageIndicatorStyle = LocalPageIndicatorStyle.current ?: PageIndicatorStyle.Default,
    size: PageIndicatorSize = LocalPageIndicatorSize.current ?: PageIndicatorSize.Default,
) {
    require(pageCount > 0) { "A page indicator needs at least one page, got $pageCount." }

    val target = currentPage.coerceIn(0, pageCount - 1) + currentPageOffset
    // A spring rather than a tween: while a finger drags the pages the target moves every frame,
    // and a spring follows a moving target where a tween would restart on each one.
    val position by animateFloatAsState(
        targetValue = target,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "pageIndicatorPosition",
    )

    PageIndicatorMarks(
        pageCount = pageCount,
        position = position,
        modifier = modifier,
        contentDescription = contentDescription,
        style = style,
        size = size,
    )
}

/**
 * A [PageIndicator] following a pager.
 *
 * Reads the pager's own position, so the long mark tracks a drag exactly and needs no animation of
 * its own.
 *
 * @param pagerState State of the pager whose pages the marks stand for.
 * @param modifier Modifier applied to the row.
 * @param contentDescription What the row says to assistive technology.
 * @param style Colors of the marks.
 * @param size Metrics of the marks.
 */
@Composable
public fun PageIndicator(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    style: PageIndicatorStyle = LocalPageIndicatorStyle.current ?: PageIndicatorStyle.Default,
    size: PageIndicatorSize = LocalPageIndicatorSize.current ?: PageIndicatorSize.Default,
) {
    if (pagerState.pageCount == 0) return

    PageIndicatorMarks(
        pageCount = pagerState.pageCount,
        position = pagerState.currentPage + pagerState.currentPageOffsetFraction,
        modifier = modifier,
        contentDescription = contentDescription,
        style = style,
        size = size,
    )
}

/**
 * The marks themselves, drawn for a position that may fall between two pages.
 *
 * Each mark is as long and as colored as it is close to [position]: the one at it is fully the
 * current mark, its neighbours share the difference while the pages move, and the rest are dots.
 *
 * @param pageCount Number of pages.
 * @param position Page on screen, fractional while the pages move.
 * @param modifier Modifier applied to the row.
 * @param contentDescription What the row says to assistive technology.
 * @param style Colors of the marks.
 * @param size Metrics of the marks.
 */
@Composable
private fun PageIndicatorMarks(
    pageCount: Int,
    position: Float,
    modifier: Modifier,
    contentDescription: String?,
    style: PageIndicatorStyle,
    size: PageIndicatorSize,
) {
    Row(
        modifier = modifier.clearAndSetSemantics {
            if (contentDescription != null) this.contentDescription = contentDescription
        },
        horizontalArrangement = Arrangement.spacedBy(size.spacing),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(pageCount) { page ->
            val closeness = (1f - abs(page - position)).coerceIn(0f, 1f)

            Box(
                Modifier
                    .width(lerp(size.dot, size.currentLength, closeness))
                    .height(size.dot)
                    .clip(size.shape)
                    .background(lerp(style.other, style.current, closeness))
            )
        }
    }
}
