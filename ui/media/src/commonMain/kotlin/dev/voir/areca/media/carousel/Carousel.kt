package dev.voir.areca.media.carousel

import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.time.Duration

/** How wide each item of a [Carousel] is. */
@Immutable
public sealed interface CarouselPageSize {
    /** One item across the whole width — a product page's hero. */
    public data object Fill : CarouselPageSize

    /**
     * A share of the width, so the next item peeks in at the edge.
     *
     * @property fraction Share of the width, from just above `0f` to `1f`.
     */
    public data class Fraction(public val fraction: Float) : CarouselPageSize {
        init {
            require(fraction > 0f && fraction <= 1f) { "A carousel item takes a share of the width in (0, 1], got $fraction." }
        }
    }

    /**
     * The same width whatever the screen — a row of tiles.
     *
     * @property width Width of each item.
     */
    public data class Fixed(public val width: Dp) : CarouselPageSize

    /**
     * [count] items side by side across the width, sharing it after the spacing.
     *
     * @property count Items across, at least one.
     */
    public data class PerPage(public val count: Int) : CarouselPageSize {
        init {
            require(count >= 1) { "A carousel shows at least one item across, got $count." }
        }
    }
}

/** How far a swipe moves a [Carousel]. */
public enum class CarouselSnap {
    /** One item at a time, even when several are on screen. */
    Item,

    /**
     * A whole page at a time: with [CarouselPageSize.PerPage] the items move as a group of that
     * many; with other sizes a fling may carry several items.
     */
    Page,
}

/**
 * Pictures — or any items — swiped sideways inside a screen: a product page's hero, a card in a
 * list, a row of looks.
 *
 * [pageSize] decides how much of each item shows and [snap] how far a swipe moves; together they
 * give one picture at a time, the next one peeking in, or two or three side by side moving one by
 * one or as a group. [state] says where the carousel is and moves it from elsewhere — indicators,
 * thumbnails and a viewer all read and drive the same state.
 *
 * With [autoplay] the pages advance on their own, from the last back to the first, and stop while a
 * finger is on them or a pointer hovers; the time the page on screen has run is
 * [CarouselState.autoplayProgress], for [PageIndicator][dev.voir.areca.foundation.navigation.PageIndicator]'s
 * segments. Leave autoplay off where the platform asks for reduced motion.
 *
 * Pressing an item is the item's: draw it as a pressable
 * [MediaFrame][dev.voir.areca.media.image.MediaFrame] that opens the viewer.
 *
 * @param state Where the carousel is. From [rememberCarouselState].
 * @param modifier Modifier applied to the carousel.
 * @param pageSize How wide each item is.
 * @param snap How far a swipe moves.
 * @param itemSpacing Gap between neighbouring items.
 * @param contentPadding Space before the first item and after the last, inside the swipe — the
 *   screen's gutter, so items scroll under the edges.
 * @param autoplay Time each page stays before the next, or `null` for none.
 * @param userScrollEnabled Whether a finger can swipe the items.
 * @param contentDescription What the carousel is, read out — "Runner 2 pictures" — or `null`.
 * @param key Stable key for the item at an index, or `null` for the index.
 * @param item Draws the item at an index.
 */
@Composable
public fun Carousel(
    state: CarouselState,
    modifier: Modifier = Modifier,
    pageSize: CarouselPageSize = CarouselPageSize.Fill,
    snap: CarouselSnap = CarouselSnap.Item,
    itemSpacing: Dp = 0.dp,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    autoplay: Duration? = null,
    userScrollEnabled: Boolean = true,
    contentDescription: String? = null,
    key: ((index: Int) -> Any)? = null,
    item: @Composable (index: Int) -> Unit,
) {
    val grouped = snap == CarouselSnap.Page && pageSize is CarouselPageSize.PerPage
    val group = if (grouped) (pageSize as CarouselPageSize.PerPage).count else 1
    if (state.group != group) {
        state.group = group
    }
    LaunchedEffect(state, group) {
        if (group > 1 && state.initialItem > 0) state.goToItem(state.initialItem, animated = false)
    }

    val hover = remember { MutableInteractionSource() }
    Autoplay(state = state, period = autoplay, hover = hover)

    val pages = state.pagerState
    HorizontalPager(
        state = pages,
        modifier = modifier
            .hoverable(hover)
            .semantics { if (contentDescription != null) this.contentDescription = contentDescription },
        contentPadding = contentPadding,
        pageSize = if (grouped) PageSize.Fill else pageSize.toPagerSize(),
        pageSpacing = itemSpacing,
        userScrollEnabled = userScrollEnabled,
        snapPosition = SnapPosition.Start,
        flingBehavior = PagerDefaults.flingBehavior(
            state = pages,
            pagerSnapDistance = PagerSnapDistance.atMost(if (snap == CarouselSnap.Page && !grouped) PAGE_FLING_ITEMS else 1),
        ),
        key = { page ->
            val real = state.realPage(page)
            key?.invoke(real * group) ?: real
        },
    ) { page ->
        val first = state.realPage(page) * group
        if (grouped) {
            Row(horizontalArrangement = Arrangement.spacedBy(itemSpacing)) {
                repeat(group) { slot ->
                    Box(Modifier.weight(1f)) {
                        if (first + slot < state.itemCount) item(first + slot) else Spacer(Modifier)
                    }
                }
            }
        } else {
            item(first)
        }
    }
}

/**
 * Advances [state] every [period], filling [CarouselState.autoplayProgress] as it goes and holding
 * still while the pages are dragged or hovered.
 *
 * @param state The carousel to advance.
 * @param period Time each page stays, or `null` for none.
 * @param hover Reports a pointer over the carousel.
 */
@Composable
private fun Autoplay(state: CarouselState, period: Duration?, hover: MutableInteractionSource) {
    val dragged by state.pagerState.interactionSource.collectIsDraggedAsState()
    val hovered by hover.collectIsHoveredAsState()
    val paused by rememberUpdatedState(dragged || hovered || state.isScrollInProgress)

    LaunchedEffect(state, period) {
        val total = period?.inWholeMilliseconds?.takeIf { it > 0 } ?: run {
            state.autoplayProgress = 0f
            return@LaunchedEffect
        }
        var elapsed = 0L
        var last = withFrameMillis { it }
        var page = state.currentPage
        while (true) {
            val now = withFrameMillis { it }
            if (state.currentPage != page) {
                page = state.currentPage
                elapsed = 0L
            }
            if (!paused) elapsed += now - last
            last = now
            state.autoplayProgress = (elapsed.toFloat() / total).coerceAtMost(1f)
            if (elapsed >= total) {
                state.advance()
                page = state.currentPage
                elapsed = 0L
                last = withFrameMillis { it }
            }
        }
    }
}

/** @return The pager's page size for this carousel size. */
private fun CarouselPageSize.toPagerSize(): PageSize = when (this) {
    CarouselPageSize.Fill -> PageSize.Fill
    is CarouselPageSize.Fixed -> PageSize.Fixed(width)
    is CarouselPageSize.Fraction -> object : PageSize {
        override fun Density.calculateMainAxisPageSize(availableSpace: Int, pageSpacing: Int): Int =
            (availableSpace * fraction).toInt()
    }
    is CarouselPageSize.PerPage -> object : PageSize {
        override fun Density.calculateMainAxisPageSize(availableSpace: Int, pageSpacing: Int): Int =
            perPageWidth(availableSpace, pageSpacing, count)
    }
}

/**
 * Width of each of [count] items sharing [availableSpace] with [spacing] between them.
 *
 * @param availableSpace Width to share, in pixels.
 * @param spacing Gap between neighbours, in pixels.
 * @param count Items across.
 * @return Each item's width, in pixels.
 */
internal fun perPageWidth(availableSpace: Int, spacing: Int, count: Int): Int =
    ((availableSpace - spacing * (count - 1)) / count).coerceAtLeast(0)

/** Most items a fling carries with [CarouselSnap.Page] on sizes that do not group. */
private const val PAGE_FLING_ITEMS = 3
