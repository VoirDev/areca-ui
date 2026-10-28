package dev.voir.areca.media.viewer

import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import dev.voir.areca.media.zoom.ZoomState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * What a [MediaViewer]'s bars need: where the viewer is, whether its bars show, and how to move or
 * close it. Handed to its `topBar` and `footer`.
 */
@Stable
public class MediaViewerState internal constructor(
    internal val pagerState: PagerState,
    private val scope: CoroutineScope,
    private val close: () -> Unit,
) {
    /** Each page's zoom, by index, kept while the viewer is open. */
    internal val zooms = mutableMapOf<Int, ZoomState>()

    /** Number of items. */
    public val itemCount: Int
        get() = pagerState.pageCount

    /** Index of the item on screen. */
    public val currentIndex: Int
        get() = pagerState.currentPage

    /** How far the pages have been dragged from [currentIndex], in pages. */
    public val currentIndexOffset: Float
        get() = pagerState.currentPageOffsetFraction

    /** Whether the bars show; a tap on the picture hides and shows them. */
    public var isChromeVisible: Boolean by mutableStateOf(true)
        internal set

    /** Whether the picture on screen is zoomed in. */
    public val isZoomed: Boolean
        get() = zooms[currentIndex]?.isZoomed == true

    /** Shows the bars if hidden, hides them if shown. */
    public fun toggleChrome() {
        isChromeVisible = !isChromeVisible
    }

    /** Shows the bars. */
    public fun showChrome() {
        isChromeVisible = true
    }

    /**
     * Moves to the item at [index].
     *
     * @param index Index of the item; one outside the items is ignored.
     * @param animated Whether the pages slide there or jump.
     */
    public fun goTo(index: Int, animated: Boolean = true) {
        if (index !in 0 until itemCount) return
        scope.launch { if (animated) pagerState.animateScrollToPage(index) else pagerState.scrollToPage(index) }
    }

    /** Closes the viewer, shrinking the picture back into its source when it has one. */
    public fun dismiss(): Unit = close()
}

/**
 * What a [MediaViewer] page draws with: its zoom, and the gestures that reach past the picture.
 * Pass them to [MediaImage][dev.voir.areca.media.image.MediaImage], or draw the page with
 * [MediaViewerImage], which does.
 */
@Stable
public interface MediaViewerPageScope {
    /** The page's zoom, reset as the page leaves. */
    public val zoomState: ZoomState

    /** Whether this page is the one on screen. */
    public val isCurrent: Boolean

    /** A single tap on the page: hides or shows the bars, as the viewer's chrome says. */
    public fun onTap()

    /**
     * A swipe past the zoomed picture's edge: turns to the next or previous page.
     *
     * @param direction `1` for the next page, `-1` for the previous.
     */
    public fun onEdgeSwipe(direction: Int)
}
