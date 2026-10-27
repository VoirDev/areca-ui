package dev.voir.areca.foundation.selection

import kotlin.math.roundToInt

/**
 * The arithmetic that scrolls an overflowing [SlidingSelector] so its chosen option is visible.
 *
 * Kept apart from the composable so it can be stated and tested as a plain function. Everything is
 * in pixels.
 */
internal object SelectorScrollMath {
    /**
     * Scroll position that brings an option fully into view, moving as little as possible.
     *
     * Option bounds are measured from the content's left (or top) edge, while a horizontal scroll in a
     * right-to-left layout counts from the right edge. [reversed] converts one to the other.
     *
     * @param itemStart Option's lower edge, from the content's left or top.
     * @param itemEnd Option's higher edge, from the content's left or top.
     * @param viewportExtent Visible length of the selector.
     * @param contentExtent Full length of the selector's content.
     * @param scroll Current scroll position, counted from the start edge.
     * @param reversed Whether the scroll counts from the right edge: a horizontal selector in a
     *   right-to-left layout.
     * @return Scroll position to move to, or `null` when the option is already fully visible or the
     *   selector has not been measured yet. An option longer than the viewport is aligned by its start.
     */
    fun scrollToReveal(
        itemStart: Float,
        itemEnd: Float,
        viewportExtent: Int,
        contentExtent: Int,
        scroll: Int,
        reversed: Boolean,
    ): Int? {
        if (viewportExtent <= 0 || contentExtent <= 0) return null

        val start = if (reversed) contentExtent - itemEnd else itemStart
        val end = if (reversed) contentExtent - itemStart else itemEnd
        val visibleEnd = scroll + viewportExtent

        val target = when {
            start < scroll -> start
            end > visibleEnd -> end - viewportExtent
            else -> return null
        }
        val maxScroll = (contentExtent - viewportExtent).coerceAtLeast(0)

        return target.roundToInt().coerceIn(0, maxScroll).takeIf { it != scroll }
    }
}
