package dev.voir.areca.foundation.topbar

/**
 * The arithmetic behind [TopBarScrollState], kept apart from the Compose plumbing that drives it.
 *
 * Separated so the rules a bar collapses by can be stated and tested as plain functions — the same
 * reason [dev.voir.areca.foundation.slot.centerSlotPlacement] and the wheel picker's row arithmetic
 * ([dev.voir.areca.foundation.wheel.nearestRow] and its neighbours) sit beside their components
 * rather than inside them. Everything here is in pixels, because nested scrolling is.
 */
internal object TopBarScrollMath {
    /**
     * Distance the bar must have moved before its separator is considered to be over content.
     *
     * A guard against a bar whose rule flickers on and off around zero — a list settling after a
     * fling, or a rubber-band overscroll, can leave the content a fraction of a pixel off its top.
     */
    private const val SCROLLED_THRESHOLD_PX = 0.5f

    /**
     * Bar offset after the gesture has been offered to the bar, before the content sees it.
     *
     * This is where a bar gets *out of the way*. [TopBarScrollBehavior.EnterAlways] also comes back
     * here, which is what makes it return on the first downward pixel rather than on reaching the
     * top; [TopBarScrollBehavior.ExitUntilCollapsed] deliberately does not, and expands through
     * [offsetAfterPostScroll] instead.
     *
     * @param behavior How the bar answers a scroll.
     * @param offsetPx Current offset, `0` fully shown and `-collapseRangePx` fully gone.
     * @param availablePx Vertical scroll offered by the gesture. Negative scrolls content up.
     * @param collapseRangePx Distance the bar may travel. Never negative.
     * @return New offset, clamped to the bar's travel.
     */
    fun offsetAfterPreScroll(
        behavior: TopBarScrollBehavior,
        offsetPx: Float,
        availablePx: Float,
        collapseRangePx: Float,
    ): Float = when (behavior) {
        TopBarScrollBehavior.Pinned -> 0f
        TopBarScrollBehavior.EnterAlways -> clampedOffset(offsetPx, availablePx, collapseRangePx)
        // Collapsing only. Letting it expand here would bring the headline back in the middle of
        // the list, which is the behavior of the enum entry above rather than of this one.
        TopBarScrollBehavior.ExitUntilCollapsed ->
            if (availablePx < 0f) {
                clampedOffset(offsetPx, availablePx, collapseRangePx)
            } else {
                offsetPx
            }
    }

    /**
     * Bar offset after the content has taken what it wanted and left the rest.
     *
     * Scroll still available here means the content could not use it — which, scrolling downward,
     * means the content has reached its own top. That is the moment
     * [TopBarScrollBehavior.ExitUntilCollapsed] brings its headline back, and the reason it stays
     * collapsed while there is still list above the user.
     *
     * @param behavior How the bar answers a scroll.
     * @param offsetPx Current offset, `0` fully shown and `-collapseRangePx` fully gone.
     * @param availablePx Vertical scroll the content did not take.
     * @param collapseRangePx Distance the bar may travel. Never negative.
     * @return New offset, clamped to the bar's travel.
     */
    fun offsetAfterPostScroll(
        behavior: TopBarScrollBehavior,
        offsetPx: Float,
        availablePx: Float,
        collapseRangePx: Float,
    ): Float = when {
        behavior != TopBarScrollBehavior.ExitUntilCollapsed -> offsetPx
        availablePx <= 0f -> offsetPx
        else -> clampedOffset(offsetPx, availablePx, collapseRangePx)
    }

    /**
     * The offset moved by a scroll and held inside the bar's travel.
     *
     * @param offsetPx Current offset.
     * @param availablePx Vertical scroll to apply.
     * @param collapseRangePx Distance the bar may travel.
     * @return New offset, never past either end of the travel.
     */
    private fun clampedOffset(
        offsetPx: Float,
        availablePx: Float,
        collapseRangePx: Float,
    ): Float {
        // Returned before the clamp rather than through it: coercing into an empty range yields
        // negative zero, which reads as a bar that has moved when every caller is asking whether
        // it has.
        val travel = collapseRangePx.coerceAtLeast(0f)
        if (travel == 0f) return 0f

        return (offsetPx + availablePx).coerceIn(-travel, 0f)
    }

    /**
     * The offset held inside a travel that may just have changed.
     *
     * @param offsetPx Current offset, `0` or negative.
     * @param collapseRangePx Distance the bar may now travel.
     * @return The offset unchanged when it still fits, the end of the travel when it went past it,
     *   and exactly `0` when there is no travel at all.
     */
    fun offsetWithinRange(offsetPx: Float, collapseRangePx: Float): Float =
        clampedOffset(offsetPx = offsetPx, availablePx = 0f, collapseRangePx = collapseRangePx)

    /**
     * How far through its travel the bar is.
     *
     * @param offsetPx Current offset, `0` fully shown and `-collapseRangePx` fully gone.
     * @param collapseRangePx Distance the bar may travel.
     * @return `0` fully expanded through `1` fully collapsed. A bar with nowhere to travel is
     *   reported as fully expanded rather than fully collapsed, so a bar whose height has not been
     *   measured yet does not flash collapsed on its first frame.
     */
    fun collapsedFraction(offsetPx: Float, collapseRangePx: Float): Float =
        if (collapseRangePx <= 0f) 0f else (-offsetPx / collapseRangePx).coerceIn(0f, 1f)

    /**
     * Running total of how far the content under the bar has scrolled.
     *
     * Capped at `0` rather than accumulated in both directions, so overscrolling past the top does
     * not build up a debt the user has to scroll back through before the separator fades out again.
     *
     * @param contentOffsetPx Total so far, never positive.
     * @param consumedPx Vertical scroll the content just took.
     * @return New total, never positive.
     */
    fun contentOffsetAfterScroll(contentOffsetPx: Float, consumedPx: Float): Float =
        (contentOffsetPx + consumedPx).coerceAtMost(0f)

    /**
     * Whether content sits under the bar rather than below it.
     *
     * @param contentOffsetPx Total the content has scrolled, never positive.
     * @return Whether the bar should show a separator.
     */
    fun isScrolled(contentOffsetPx: Float): Boolean = contentOffsetPx < -SCROLLED_THRESHOLD_PX
}
