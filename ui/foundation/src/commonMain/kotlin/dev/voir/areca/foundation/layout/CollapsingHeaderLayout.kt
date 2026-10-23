package dev.voir.areca.foundation.layout

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.animate
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.zIndex
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.tween
import kotlin.math.roundToInt
import kotlin.time.Duration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * A header that scrolls away, a strip that stays, and scrolling content under both.
 *
 * Three bands top to bottom: [header] collapses as the content scrolls up, [pinned] never moves, and
 * [content] scrolls. The common shape is a title and artwork that give way to a tab strip.
 *
 * [content] must bring its own scrolling container — a `LazyColumn`, a `LazyVerticalGrid`, a pager.
 * The layout drives the collapse through nested scrolling rather than by scrolling anything itself,
 * which is what keeps the inner list lazy: its items are still composed on demand, and its own
 * scroll position is still its own.
 *
 * The collapse follows the finger exactly: each scroll delta moves the header before the content
 * sees what is left, so several deltas delivered within one frame never claim the same distance
 * twice.
 *
 * @param header The band that collapses. Measured once; its height is how far it can collapse.
 * @param pinned The band that stays, between the header and the content.
 * @param modifier Modifier applied to the whole layout.
 * @param state How far the header has collapsed. Hoist it to read
 *   [CollapsingHeaderState.collapsedFraction] — to fade a title into a bar, say.
 * @param collapsed A compact band the header collapses into rather than out of sight: the header
 *   stops collapsing at the band's height, and the band fades in over it as it goes, carrying what
 *   must stay readable — a title and a figure. Give it an opaque ground. Once the header has
 *   collapsed fully, assistive technology reads the band instead of the header, never both. `null`
 *   lets the header collapse away entirely.
 * @param expandOnlyWhenScrolledToTop Whether scrolling back up leaves the header collapsed until the
 *   inner content reaches its own top. `true` is the familiar behaviour — the header returns as part
 *   of arriving back at the start, rather than reappearing the instant a drag reverses.
 * @param expandDuration How long the header takes to spring back once the content reaches its top.
 * @param content The scrolling content, which must supply its own scrollable container.
 */
@Composable
public fun CollapsingHeaderLayout(
    header: @Composable () -> Unit,
    pinned: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    state: CollapsingHeaderState = rememberCollapsingHeaderState(),
    collapsed: (@Composable () -> Unit)? = null,
    expandOnlyWhenScrolledToTop: Boolean = true,
    expandDuration: Duration = ArecaTheme.motion.medium,
    content: @Composable (Modifier) -> Unit,
) {
    val expandEasing = ArecaTheme.motion.standard
    val coroutineScope = rememberCoroutineScope()
    val collapse = state.offset
    var headerFullPx by remember { mutableIntStateOf(0) }
    var collapsedPx by remember { mutableIntStateOf(0) }
    // Derived, so a scroll frame recomposes the layout only when the band takes over or hands back,
    // not on every pixel of the collapse.
    val bandTakesOver by remember(state) { derivedStateOf { state.collapsedFraction >= 1f } }
    val hasBand = collapsed != null
    if (!hasBand && collapsedPx != 0) {
        // A band removed at run time no longer holds the header open at its height.
        SideEffect {
            collapsedPx = 0
            collapse.onHeaderMeasured(headerFullPx)
        }
    }
    val currentExpandDuration by rememberUpdatedState(expandDuration)
    val currentExpandEasing by rememberUpdatedState(expandEasing)

    val nestedScrollConnection = remember(collapse, expandOnlyWhenScrolledToTop) {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                // Positive is a downward pull; with the gate on, the header waits for the content
                // to reach its top and returns in onPostScroll instead.
                if (expandOnlyWhenScrolledToTop && delta > 0f) return Offset.Zero

                collapse.cancelExpand()
                val previous = collapse.offsetPx
                val next = collapsingHeaderOffsetAfter(
                    offsetPx = previous,
                    deltaY = delta,
                    headerHeightPx = collapse.headerHeightPx,
                )
                collapse.offsetPx = next
                return Offset(x = 0f, y = next - previous)
            }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource,
            ): Offset {
                if (!expandOnlyWhenScrolledToTop) return Offset.Zero

                // Downward scroll the content could not use means it has reached its own top.
                val claimed = collapsingHeaderPostScrollConsumption(
                    availableY = available.y,
                    collapseOffsetPx = collapse.offsetPx,
                )
                if (claimed > 0f) {
                    collapse.expand(
                        scope = coroutineScope,
                        duration = currentExpandDuration,
                        easing = currentExpandEasing,
                    )
                }
                return Offset(x = 0f, y = claimed)
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
    ) {
        // The header's slot shrinks rather than translating, so no gap opens above the pinned band.
        Box(
            modifier = Modifier
                .clipToBounds()
                .layout { measurable, constraints ->
                    val placeable = measurable.measure(constraints)
                    // The header collapses down to the compact band's height, not past it.
                    val visibleHeight = (collapsedPx + collapse.headerHeightPx + collapse.offsetPx)
                        .roundToInt()
                        .coerceAtLeast(0)

                    layout(width = placeable.width, height = visibleHeight) {
                        placeable.placeRelative(0, 0)
                    }
                }
        ) {
            Box(
                modifier = Modifier
                    .onSizeChanged { size ->
                        headerFullPx = size.height
                        collapse.onHeaderMeasured((size.height - collapsedPx).coerceAtLeast(0))
                    }
                    // Once the band has taken over, the header under it is not read a second time.
                    .then(if (hasBand && bandTakesOver) Modifier.clearAndSetSemantics {} else Modifier),
            ) {
                header()
            }
            if (collapsed != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onSizeChanged { size ->
                            collapsedPx = size.height
                            collapse.onHeaderMeasured((headerFullPx - size.height).coerceAtLeast(0))
                        }
                        .graphicsLayer { alpha = state.collapsedFraction }
                        .then(if (bandTakesOver) Modifier else Modifier.clearAndSetSemantics {}),
                ) {
                    collapsed()
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .zIndex(1f)
        ) {
            pinned()
        }

        Box(
            modifier = Modifier.weight(1f)
        ) {
            content(
                Modifier.fillMaxSize()
            )
        }
    }
}

/**
 * How far a [CollapsingHeaderLayout]'s header has collapsed.
 *
 * Read [collapsedFraction] to drive something of your own from the collapse; the layout writes it.
 */
@Stable
public class CollapsingHeaderState internal constructor() {
    /** The collapse itself, written by the layout's nested scrolling. */
    internal val offset: CollapseOffset = CollapseOffset()

    /**
     * How far the header has collapsed: `0f` fully shown, `1f` collapsed as far as it goes — out of
     * sight, or down to the compact band when the layout has one.
     */
    public val collapsedFraction: Float
        get() {
            val distance = offset.headerHeightPx
            return if (distance <= 0) 0f else (-offset.offsetPx / distance).coerceIn(0f, 1f)
        }
}

/**
 * Remembers a [CollapsingHeaderLayout]'s collapse, starting fully expanded.
 *
 * @return A state for one layout.
 */
@Composable
public fun rememberCollapsingHeaderState(): CollapsingHeaderState = remember { CollapsingHeaderState() }

/**
 * How far a [CollapsingHeaderLayout]'s header has collapsed, and the animation returning it.
 *
 * The offset is plain snapshot state written synchronously from the nested scroll callbacks, so the
 * next callback in the same frame already sees the distance the previous one claimed. Only the
 * spring back to fully expanded runs as a coroutine, and any new scroll cancels it.
 */
@Stable
internal class CollapseOffset {
    /** How far the header can collapse, in pixels: its height, less any compact band it stops at. */
    var headerHeightPx by mutableIntStateOf(0)
        private set

    /** Current collapse, in pixels: `0` fully expanded, `-headerHeightPx` fully collapsed. */
    var offsetPx by mutableFloatStateOf(0f)

    /** Running spring back to expanded, or `null` when none is running. */
    private var expandJob: Job? = null

    /**
     * Records a new header height and keeps the offset within it.
     *
     * @param heightPx Measured height of the header, in pixels.
     */
    fun onHeaderMeasured(heightPx: Int) {
        headerHeightPx = heightPx
        offsetPx = offsetPx.coerceIn(-heightPx.toFloat(), 0f)
    }

    /** Stops a running spring back, leaving the header where it is. */
    fun cancelExpand() {
        expandJob?.cancel()
        expandJob = null
    }

    /**
     * Springs the header back to fully expanded, unless a spring back is already running.
     *
     * @param scope Scope the animation runs in; the layout's own.
     * @param duration How long the spring back takes.
     * @param easing Easing of the spring back.
     */
    fun expand(
        scope: CoroutineScope,
        duration: Duration,
        easing: Easing,
    ) {
        if (expandJob?.isActive == true) return
        expandJob = scope.launch {
            animate(
                initialValue = offsetPx,
                targetValue = 0f,
                animationSpec = duration.tween(easing),
            ) { value, _ ->
                offsetPx = value
            }
        }
    }
}

/**
 * Header offset of a [CollapsingHeaderLayout] after a scroll delta reaches it before its content.
 *
 * The offset moves by the whole delta and is clamped to the header's range, so the distance the
 * header claims is the difference between the result and [offsetPx].
 *
 * @param offsetPx Current offset, `0` fully expanded and negative while collapsed.
 * @param deltaY Scroll delta in pixels. Negative pushes the content up and collapses the header.
 * @param headerHeightPx Measured header height in pixels; the offset never goes below its negative.
 * @return The new offset, between `-headerHeightPx` and `0`.
 */
internal fun collapsingHeaderOffsetAfter(offsetPx: Float, deltaY: Float, headerHeightPx: Int): Float =
    (offsetPx + deltaY).coerceIn(minimumValue = -headerHeightPx.toFloat(), maximumValue = 0f)

/**
 * Vertical scroll [CollapsingHeaderLayout] reports as consumed once its content reaches the top.
 *
 * The header springs back on its own animation rather than following the finger, so it claims only
 * the distance it still has to travel, and never more than the gesture offered. Whatever is left
 * stays available to a parent or to the content's overscroll effect, as the nested scroll contract
 * requires.
 *
 * @param availableY Scroll left over after the content consumed its share, in pixels. Positive
 *   pulls the content down.
 * @param collapseOffsetPx Current header offset, `0` fully expanded and negative while collapsed.
 * @return Distance consumed, between `0` and [availableY]; `0` when nothing is offered downward.
 */
internal fun collapsingHeaderPostScrollConsumption(availableY: Float, collapseOffsetPx: Float): Float {
    if (availableY <= 0f || collapseOffsetPx >= 0f) return 0f
    return (-collapseOffsetPx).coerceAtMost(availableY)
}
