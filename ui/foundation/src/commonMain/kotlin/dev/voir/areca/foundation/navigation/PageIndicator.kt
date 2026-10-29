package dev.voir.areca.foundation.navigation

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.lerp
import dev.voir.areca.modifier.clickCursor
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.roundToInt

/**
 * Marks saying which page is on screen — the dots under an onboarding carousel, over a product's
 * pictures, or the bars across a story.
 *
 * [variant] picks the marks. By default, [PageIndicatorVariant.Elongated], the mark for the current
 * page is longer and colored, so it reads without color too; [PageIndicatorVariant.Shrinking] and
 * [PageIndicatorVariant.Line] keep any number of pages to a fixed width. Pass [currentPageOffset]
 * while the pages are being dragged and the marks follow the finger; changed without an offset,
 * they glide to their new place. Prefer the [PagerState] overload when the pages are a pager, which
 * does both.
 *
 * To assistive technology the marks are decoration: it hears the row as one element, named by
 * [contentDescription] — "Page 2 of 4". A page change still needs its own announcement if it
 * matters. With [onPageClick] a pointer may also jump to a page by tapping near its mark; the pages
 * must stay reachable another way, since a mark is far below a touch target.
 *
 * Over a picture, pass [PageIndicatorStyle.onImage], which draws the marks on a capsule.
 *
 * @param pageCount Number of pages, at least one.
 * @param currentPage Index of the page on screen, coerced into `0 until pageCount`.
 * @param modifier Modifier applied to the row. [PageIndicatorVariant.Segments] fills the width this
 *   gives it.
 * @param currentPageOffset How far the pages have been dragged from [currentPage], in pages, from
 * `-0.5f` to `0.5f`. `0f` when they are at rest.
 * @param contentDescription What the row says to assistive technology.
 * @param variant Which marks are drawn.
 * @param progress How far the current page has run, from `0f` to `1f`, read while drawing. Only
 *   [PageIndicatorVariant.Segments] shows it, filling the current bar.
 * @param onPageClick Called with the page whose mark a pointer tapped, or `null` for marks that only
 *   show.
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
    variant: PageIndicatorVariant = PageIndicatorVariant.Elongated,
    progress: () -> Float = NoProgress,
    onPageClick: ((page: Int) -> Unit)? = null,
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
        variant = variant,
        progress = progress,
        onPageClick = onPageClick,
        style = style,
        size = size,
    )
}

/**
 * A [PageIndicator] following a pager.
 *
 * Reads the pager's own position, so the marks track a drag exactly and need no animation of their
 * own.
 *
 * @param pagerState State of the pager whose pages the marks stand for.
 * @param modifier Modifier applied to the row.
 * @param contentDescription What the row says to assistive technology.
 * @param variant Which marks are drawn.
 * @param progress How far the current page has run, from `0f` to `1f`; shown by
 *   [PageIndicatorVariant.Segments].
 * @param onPageClick Called with the page whose mark a pointer tapped, or `null` for marks that only
 *   show. Moving the pager is the caller's.
 * @param style Colors of the marks.
 * @param size Metrics of the marks.
 */
@Composable
public fun PageIndicator(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    variant: PageIndicatorVariant = PageIndicatorVariant.Elongated,
    progress: () -> Float = NoProgress,
    onPageClick: ((page: Int) -> Unit)? = null,
    style: PageIndicatorStyle = LocalPageIndicatorStyle.current ?: PageIndicatorStyle.Default,
    size: PageIndicatorSize = LocalPageIndicatorSize.current ?: PageIndicatorSize.Default,
) {
    if (pagerState.pageCount == 0) return

    PageIndicatorMarks(
        pageCount = pagerState.pageCount,
        position = pagerState.currentPage + pagerState.currentPageOffsetFraction,
        modifier = modifier,
        contentDescription = contentDescription,
        variant = variant,
        progress = progress,
        onPageClick = onPageClick,
        style = style,
        size = size,
    )
}

/**
 * The marks of [variant], drawn for a position that may fall between two pages, in the capsule of
 * [style] when it has one.
 *
 * @param pageCount Number of pages.
 * @param position Page on screen, fractional while the pages move.
 * @param modifier Modifier applied to the row.
 * @param contentDescription What the row says to assistive technology.
 * @param variant Which marks are drawn.
 * @param progress How far the current page has run, for segments.
 * @param onPageClick Called with a tapped page, or `null`.
 * @param style Colors of the marks.
 * @param size Metrics of the marks.
 */
@Composable
private fun PageIndicatorMarks(
    pageCount: Int,
    position: Float,
    modifier: Modifier,
    contentDescription: String?,
    variant: PageIndicatorVariant,
    progress: () -> Float,
    onPageClick: ((Int) -> Unit)?,
    style: PageIndicatorStyle,
    size: PageIndicatorSize,
) {
    val current = position.roundToInt().coerceIn(0, pageCount - 1)
    val shrinking = (variant as? PageIndicatorVariant.Shrinking)?.takeIf { pageCount > it.window + 2 }
    // Remembered rather than derived: the window moves only when the current page leaves it, so
    // where it sits depends on where it was.
    // The first frame centres the window on the page; later ones move it only as far as they must.
    val windowStart = remember(pageCount, shrinking) { intArrayOf(NO_WINDOW) }
    if (shrinking != null) {
        val previous = if (windowStart[0] == NO_WINDOW) current - shrinking.window / 2 else windowStart[0]
        windowStart[0] = shrinkingWindowStart(current, pageCount, shrinking.window, previous)
    }

    val frame = modifier
        .clearAndSetSemantics {
            if (contentDescription != null) this.contentDescription = contentDescription
        }
        .then(
            if (onPageClick == null) {
                Modifier
            } else {
                Modifier
                    .clickCursor()
                    .pointerInput(pageCount, shrinking, onPageClick) {
                        detectTapGestures { offset ->
                            val fraction = (offset.x / this.size.width).coerceIn(0f, 0.999f)
                            val page = if (shrinking == null) {
                                floor(fraction * pageCount).toInt()
                            } else {
                                windowStart[0] - 1 + floor(fraction * (shrinking.window + 2)).toInt()
                            }
                            onPageClick(page.coerceIn(0, pageCount - 1))
                        }
                    }
            },
        )
        .then(
            style.capsule?.let { capsule ->
                Modifier
                    .background(capsule, size.shape)
                    .padding(size.spacing)
            } ?: Modifier,
        )

    when {
        shrinking != null -> ShrinkingDots(pageCount, current, windowStart[0], shrinking.window, frame, style, size)
        variant == PageIndicatorVariant.Line -> LineTrack(pageCount, position, frame, style, size)
        variant == PageIndicatorVariant.Segments -> SegmentBars(pageCount, current, progress, frame, style, size)
        else -> DotRow(pageCount, position, stretch = variant == PageIndicatorVariant.Elongated, frame, style, size)
    }
}

/**
 * A dot per page. Each is as coloured — and, when [stretch], as long — as it is close to
 * [position]: the one at it is fully the current mark, its neighbours share the difference while
 * the pages move, and the rest are dots.
 *
 * @param pageCount Number of pages.
 * @param position Page on screen, fractional while the pages move.
 * @param stretch Whether the current mark grows to [PageIndicatorSize.currentLength].
 * @param modifier Modifier applied to the row.
 * @param style Colors of the marks.
 * @param size Metrics of the marks.
 */
@Composable
private fun DotRow(
    pageCount: Int,
    position: Float,
    stretch: Boolean,
    modifier: Modifier,
    style: PageIndicatorStyle,
    size: PageIndicatorSize,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(size.spacing),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(pageCount) { page ->
            val closeness = (1f - abs(page - position)).coerceIn(0f, 1f)

            Box(
                Modifier
                    .width(if (stretch) lerp(size.dot, size.currentLength, closeness) else size.dot)
                    .height(size.dot)
                    .clip(size.shape)
                    .background(lerp(style.other, style.current, closeness))
            )
        }
    }
}

/**
 * Dots seen through a window of [window] pages starting at [windowStart], with a smaller dot either
 * side while more pages lie beyond, sliding as the window moves.
 *
 * @param pageCount Number of pages, more than fit in the window and its edge dots.
 * @param current Page on screen.
 * @param windowStart First page drawn full size.
 * @param window How many pages are drawn full size.
 * @param modifier Modifier applied to the window.
 * @param style Colors of the marks.
 * @param size Metrics of the marks.
 */
@Composable
private fun ShrinkingDots(
    pageCount: Int,
    current: Int,
    windowStart: Int,
    window: Int,
    modifier: Modifier,
    style: PageIndicatorStyle,
    size: PageIndicatorSize,
) {
    val step = size.dot + size.spacing
    val stepPx = with(LocalDensity.current) { step.toPx() }
    // One slot before the window is kept for the leading small dot.
    val shift by animateFloatAsState(
        targetValue = -(windowStart - 1) * stepPx,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "pageIndicatorWindow",
    )

    Box(modifier = modifier.width(step * (window + 2) - size.spacing).height(size.dot).clipToBounds()) {
        Row(
            modifier = Modifier
                .wrapContentWidth(Alignment.Start, unbounded = true)
                .offset { IntOffset(shift.roundToInt(), 0) },
            horizontalArrangement = Arrangement.spacedBy(size.spacing),
        ) {
            repeat(pageCount) { page ->
                val scale by animateFloatAsState(
                    targetValue = shrinkingScale(page, current, windowStart, window, pageCount),
                    animationSpec = spring(stiffness = Spring.StiffnessMedium),
                    label = "pageIndicatorDotScale",
                )

                Box(
                    Modifier
                        .size(size.dot)
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                        }
                        .clip(size.shape)
                        .background(if (page == current) style.current else style.other)
                )
            }
        }
    }
}

/**
 * A track [PageIndicatorSize.lineLength] long with a thumb one page long, at [position].
 *
 * @param pageCount Number of pages.
 * @param position Page on screen, fractional while the pages move.
 * @param modifier Modifier applied to the track.
 * @param style Colors of the track and thumb.
 * @param size Metrics of the track.
 */
@Composable
private fun LineTrack(
    pageCount: Int,
    position: Float,
    modifier: Modifier,
    style: PageIndicatorStyle,
    size: PageIndicatorSize,
) {
    val thumb = size.lineLength / pageCount
    val thumbPx = with(LocalDensity.current) { thumb.toPx() }

    Box(
        modifier = modifier
            .width(size.lineLength)
            .height(size.lineThickness)
            .clip(size.shape)
            .background(style.other),
    ) {
        Box(
            Modifier
                .width(thumb)
                .fillMaxHeight()
                .offset { IntOffset((thumbPx * position).roundToInt(), 0) }
                .clip(size.shape)
                .background(style.current)
        )
    }
}

/**
 * A bar per page across the width given, full before [current], empty after it, and the current
 * one filled to [progress].
 *
 * @param pageCount Number of pages.
 * @param current Page on screen.
 * @param progress How far the current page has run, read while drawing.
 * @param modifier Modifier applied to the row.
 * @param style Colors of the bars.
 * @param size Metrics of the bars.
 */
@Composable
private fun SegmentBars(
    pageCount: Int,
    current: Int,
    progress: () -> Float,
    modifier: Modifier,
    style: PageIndicatorStyle,
    size: PageIndicatorSize,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(size.spacing),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(pageCount) { page ->
            Box(
                Modifier
                    .weight(1f)
                    .height(size.lineThickness)
                    .clip(size.shape)
                    .background(style.other)
                    .drawBehind {
                        val fill = segmentFill(page, current, progress())
                        if (fill > 0f) drawRect(style.current, size = Size(this.size.width * fill, this.size.height))
                    }
            )
        }
    }
}

/**
 * Where a shrinking window starts once [page] is on screen: where it was, unless the page has left
 * it, in which case just far enough to take the page back in at its edge.
 *
 * @param page Page on screen.
 * @param pageCount Number of pages.
 * @param window How many pages the window holds.
 * @param previous Where the window started before.
 * @return The first page of the window, between `0` and `pageCount - window`.
 */
internal fun shrinkingWindowStart(page: Int, pageCount: Int, window: Int, previous: Int): Int {
    val last = (pageCount - window).coerceAtLeast(0)
    var start = previous.coerceIn(0, last)
    if (page < start) start = page
    if (page > start + window - 1) start = page - window + 1
    return start.coerceIn(0, last)
}

/**
 * How large a page's dot is drawn in a shrinking window: full inside it and for the page on screen,
 * smaller at an edge with more pages beyond, small just outside it, and not at all further away.
 *
 * @param page The dot's page.
 * @param current Page on screen, always drawn full size.
 * @param windowStart First page of the window.
 * @param window How many pages the window holds.
 * @param pageCount Number of pages.
 * @return The dot's scale, from `0f` to `1f`.
 */
internal fun shrinkingScale(page: Int, current: Int, windowStart: Int, window: Int, pageCount: Int): Float {
    val windowEnd = windowStart + window - 1
    return when {
        page == current -> 1f
        page == windowStart && windowStart > 0 -> EDGE_SCALE
        page == windowEnd && windowEnd < pageCount - 1 -> EDGE_SCALE
        page in windowStart..windowEnd -> 1f
        page == windowStart - 1 || page == windowEnd + 1 -> OUTER_SCALE
        else -> 0f
    }
}

/**
 * How much of a segment is filled.
 *
 * @param page The segment's page.
 * @param current Page on screen.
 * @param progress How far the current page has run.
 * @return `1f` before the current page, [progress] at it, `0f` after it.
 */
internal fun segmentFill(page: Int, current: Int, progress: Float): Float = when {
    page < current -> 1f
    page == current -> progress.coerceIn(0f, 1f)
    else -> 0f
}

/** The `progress` of an indicator with nothing running: an empty current segment. */
private val NoProgress: () -> Float = { 0f }

/** Where a shrinking window starts before it has been placed. */
private const val NO_WINDOW = Int.MIN_VALUE

/** Scale of a dot at a shrinking window's edge while more pages lie beyond it. */
private const val EDGE_SCALE = 0.66f

/** Scale of the dot just outside a shrinking window. */
private const val OUTER_SCALE = 0.4f
