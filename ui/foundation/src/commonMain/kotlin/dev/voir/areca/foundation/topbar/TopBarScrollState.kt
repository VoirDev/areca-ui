package dev.voir.areca.foundation.topbar

import androidx.compose.animation.core.animate
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource

/**
 * How a [TopBar] answers the content scrolling beneath it.
 *
 * Every behavior shows the bar's separator once content is under it — that part is not a choice,
 * because a bar with content sliding invisibly beneath its edge is the one thing a heading bar must
 * never look like. What differs is whether the bar itself moves.
 */
public enum class TopBarScrollBehavior {
    /**
     * The bar stays exactly where it is, and only its separator changes.
     *
     * The default, and right for nearly every screen: a heading is a place to look, and a heading
     * that leaves is one the user has to scroll back for.
     */
    Pinned,

    /**
     * The bar slides out of the way as content scrolls up, and returns as soon as it scrolls down.
     *
     * For a screen whose content is the point and whose bar is a way back — a reader, a feed. The
     * bar returns on the first downward pixel rather than on reaching the top, so it is always one
     * small gesture away.
     */
    EnterAlways,

    /**
     * A [LargeTopBar]'s headline collapses into the compact bar and stays collapsed until the
     * content returns to its own top.
     *
     * The large-title arrangement: a screen opens with its name spelled out, and trades it for
     * content as soon as the user starts reading.
     */
    ExitUntilCollapsed,
}

/**
 * What a [TopBar] knows about the content scrolling under it.
 *
 * Hand the same state to a bar and to the scrolling content — the bar reads it, the content feeds
 * it through [nestedScrollConnection]:
 *
 * ```kotlin
 * val scrollState = rememberTopBarScrollState(TopBarScrollBehavior.EnterAlways)
 *
 * BaseLayout(topBar = { TopBar(title = "Inbox", scrollState = scrollState) }) { padding ->
 *     LazyColumn(
 *         modifier = Modifier.nestedScroll(scrollState.nestedScrollConnection),
 *         contentPadding = padding,
 *     ) { … }
 * }
 * ```
 *
 * This describes one bar, not a whole page. Reach for
 * [dev.voir.areca.foundation.layout.CollapsingHeaderLayout] instead when what collapses is a band
 * of the screen — artwork, a hero, a tab strip that has to stay pinned beneath it.
 *
 * Only this module implements it; create one with [rememberTopBarScrollState].
 */
@Stable
public sealed interface TopBarScrollState {
    /** How the bar answers a scroll. */
    public val behavior: TopBarScrollBehavior

    /**
     * Whether content currently sits under the bar. Drives the separator under every behavior.
     *
     * Invalidates its readers only when the answer flips, so reading it in composition does not
     * recompose on every scrolled pixel.
     */
    public val isScrolled: Boolean

    /**
     * How far through its travel the bar is: `0` fully shown through `1` fully travelled.
     *
     * Follows [offsetPx], so it stays `0` under [TopBarScrollBehavior.Pinned] and while
     * [collapseRangePx] is still `0`. Changes on every scrolled pixel; read it in a layout or draw
     * lambda rather than in composition.
     */
    public val collapsedFraction: Float

    /**
     * Pixels the bar has travelled, from `0` (fully shown) down to `-collapseRangePx`.
     *
     * Moved by [TopBarScrollBehavior.EnterAlways], under which a bar slides by it, and by
     * [TopBarScrollBehavior.ExitUntilCollapsed], under which a [LargeTopBar]'s headline collapses
     * by it. Always `0` under [TopBarScrollBehavior.Pinned].
     */
    public val offsetPx: Float

    /**
     * How far the bar may travel, in pixels; `0` until a bar has been measured.
     *
     * Written by exactly one place per behavior: under [TopBarScrollBehavior.EnterAlways] the bar
     * reports its whole height, and under [TopBarScrollBehavior.ExitUntilCollapsed] a
     * [LargeTopBar] reports its headline's height. Read-only to callers.
     */
    public val collapseRangePx: Float

    /** Connection to attach to the scrolling content with `Modifier.nestedScroll`. */
    public val nestedScrollConnection: NestedScrollConnection

    /**
     * Animates the bar back to fully shown and clears [isScrolled], for a screen that scrolls
     * itself back to the top.
     *
     * Needs a `MonotonicFrameClock` in the calling context, which a composition's coroutine scope
     * provides.
     */
    public suspend fun expand()
}

/**
 * Creates and remembers the state a [TopBar] reads the scroll position from.
 *
 * The state is saved, so a bar keeps its travel and its separator across configuration change and
 * process death. A different [behavior] starts a fresh state.
 *
 * @param behavior How the bar should answer a scroll.
 * @return State to hand to both the bar and the scrolling content.
 */
@Composable
public fun rememberTopBarScrollState(
    behavior: TopBarScrollBehavior = TopBarScrollBehavior.Pinned,
): TopBarScrollState = rememberSaveable(behavior, saver = TopBarScrollStateImpl.Saver) {
    TopBarScrollStateImpl(behavior)
}

/**
 * Records how far the bar may travel, from the height the bar just measured.
 *
 * Kept off the public interface so only the bars in this module can move the range.
 *
 * @param px Collapse range in pixels. Negative values are treated as `0`.
 */
internal fun TopBarScrollState.updateCollapseRange(px: Float) {
    when (this) {
        is TopBarScrollStateImpl -> setCollapseRange(px)
    }
}

/**
 * The default [TopBarScrollState].
 *
 * The offset is plain observable state rather than an `Animatable`, because a dragged bar has to
 * move in the same frame as the content it travels with: anything that eased toward the gesture
 * would leave the bar lagging a few pixels behind the list. [expand] drives the same field through
 * an animation instead, which is the one time the bar moves on its own.
 *
 * @param behavior How the bar answers a scroll.
 * @param initialOffsetPx Offset to start from, `0` or negative. Non-zero only when restoring.
 * @param initialContentOffsetPx Content scroll total to start from, `0` or negative.
 * @param initialCollapseRangePx Collapse range to start from, until the bar measures itself again.
 */
internal class TopBarScrollStateImpl(
    override val behavior: TopBarScrollBehavior,
    initialOffsetPx: Float = 0f,
    initialContentOffsetPx: Float = 0f,
    initialCollapseRangePx: Float = 0f,
) : TopBarScrollState {
    /** Running total of how far the content has scrolled, never positive. */
    private var contentOffsetPx by mutableFloatStateOf(initialContentOffsetPx)

    /** [isScrolled], derived so that its readers are invalidated only when the answer flips. */
    private val scrolled = derivedStateOf { TopBarScrollMath.isScrolled(contentOffsetPx) }

    override var collapseRangePx: Float by mutableFloatStateOf(initialCollapseRangePx)
        private set

    override var offsetPx: Float by mutableFloatStateOf(initialOffsetPx)
        private set

    override val isScrolled: Boolean get() = scrolled.value

    override val collapsedFraction: Float
        get() = TopBarScrollMath.collapsedFraction(
            offsetPx = offsetPx,
            collapseRangePx = collapseRangePx,
        )

    override val nestedScrollConnection: NestedScrollConnection =
        object : NestedScrollConnection {
            /**
             * Offers the gesture to the bar before the content sees it.
             *
             * @param available Scroll the gesture offers.
             * @param source Kind of input producing the scroll.
             * @return Scroll the bar used up.
             */
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset =
                travel(
                    TopBarScrollMath.offsetAfterPreScroll(
                        behavior = behavior,
                        offsetPx = offsetPx,
                        availablePx = available.y,
                        collapseRangePx = collapseRangePx,
                    )
                )

            /**
             * Records what the content took, and offers the bar what the content left.
             *
             * @param consumed Scroll the content used.
             * @param available Scroll the content left over.
             * @param source Kind of input producing the scroll.
             * @return Scroll the bar used up out of [available].
             */
            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource,
            ): Offset {
                contentOffsetPx = TopBarScrollMath.contentOffsetAfterScroll(
                    contentOffsetPx = contentOffsetPx,
                    consumedPx = consumed.y,
                )

                return travel(
                    TopBarScrollMath.offsetAfterPostScroll(
                        behavior = behavior,
                        offsetPx = offsetPx,
                        availablePx = available.y,
                        collapseRangePx = collapseRangePx,
                    )
                )
            }

            /**
             * Moves the bar to [next] and reports how far it went.
             *
             * @param next Offset the bar should now be at.
             * @return Scroll the bar used up, so the content sees only the remainder rather than
             *   the whole gesture a second time.
             */
            private fun travel(next: Float): Offset {
                val previous = offsetPx
                if (next == previous) return Offset.Zero

                offsetPx = next

                return Offset(x = 0f, y = next - previous)
            }
        }

    /**
     * Sets the collapse range and pulls the offset back inside it.
     *
     * A bar re-measured shorter — after a rotation, say — is never left travelled past its own
     * height.
     *
     * @param px Collapse range in pixels. Negative values are treated as `0`.
     */
    fun setCollapseRange(px: Float) {
        val range = px.coerceAtLeast(0f)
        collapseRangePx = range
        offsetPx = TopBarScrollMath.offsetWithinRange(offsetPx = offsetPx, collapseRangePx = range)
    }

    override suspend fun expand() {
        contentOffsetPx = 0f

        val from = offsetPx
        if (from == 0f) return

        animate(initialValue = from, targetValue = 0f) { value, _ -> offsetPx = value }
    }

    /** Holder of the [TopBarScrollStateImpl] saver. */
    companion object {
        /**
         * Saves the behavior, the bar's offset, the content's scroll total and the measured range.
         *
         * The range is saved too, so a restored bar draws at its saved travel on its first frame
         * rather than snapping open until it has been measured again.
         */
        val Saver: Saver<TopBarScrollStateImpl, Any> = listSaver(
            save = { state ->
                listOf(
                    state.behavior.name,
                    state.offsetPx,
                    state.contentOffsetPx,
                    state.collapseRangePx,
                )
            },
            restore = { saved ->
                TopBarScrollStateImpl(
                    behavior = TopBarScrollBehavior.valueOf(saved[0] as String),
                    initialOffsetPx = saved[1] as Float,
                    initialContentOffsetPx = saved[2] as Float,
                    initialCollapseRangePx = saved[3] as Float,
                )
            },
        )
    }
}
