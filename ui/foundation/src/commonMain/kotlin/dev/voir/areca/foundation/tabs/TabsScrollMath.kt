package dev.voir.areca.foundation.tabs

import kotlin.math.roundToInt

/**
 * The arithmetic that scrolls an overflowing [Tabs] strip so its selected tab is visible.
 *
 * Kept apart from the composable so it can be stated and tested as a plain function. Everything is
 * in pixels.
 */
internal object TabsScrollMath {
    /**
     * Scroll position that brings a tab fully into view, moving as little as possible.
     *
     * Tab bounds are measured from the content's left (or top) edge, while a horizontal scroll in a
     * right-to-left layout counts from the right edge. [reversed] converts one to the other.
     *
     * @param tabStart Tab's lower edge, from the content's left or top.
     * @param tabEnd Tab's higher edge, from the content's left or top.
     * @param viewportExtent Visible length of the strip.
     * @param contentExtent Full length of the strip's content.
     * @param scroll Current scroll position, counted from the start edge.
     * @param reversed Whether the scroll counts from the right edge: a horizontal strip in a
     *   right-to-left layout.
     * @return Scroll position to move to, or `null` when the tab is already fully visible or the
     *   strip has not been measured yet. A tab longer than the viewport is aligned by its start.
     */
    fun scrollToReveal(
        tabStart: Float,
        tabEnd: Float,
        viewportExtent: Int,
        contentExtent: Int,
        scroll: Int,
        reversed: Boolean,
    ): Int? {
        if (viewportExtent <= 0 || contentExtent <= 0) return null

        val start = if (reversed) contentExtent - tabEnd else tabStart
        val end = if (reversed) contentExtent - tabStart else tabEnd
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
