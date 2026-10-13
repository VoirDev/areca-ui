package dev.voir.areca.foundation.wheel

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.spring
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import kotlin.math.abs

/** Settling animation used until a column tells the state what the theme asks for. */
private val DefaultSnapSpec: AnimationSpec<Float> = spring(
    dampingRatio = Spring.DampingRatioNoBouncy,
    stiffness = Spring.StiffnessMediumLow,
)

/**
 * How quickly the wheel closes the gap to where the scroll wheel has asked it to be.
 *
 * A time constant rather than a duration, because the target moves: a trackpad adds to it on every
 * frame. Following it exponentially means the wheel keeps whatever speed it already had when the
 * target moves, which a restarted spring cannot.
 */
private const val StepFollowTauMillis: Float = 45f

/** Nanoseconds in a millisecond, for turning frame times into the units the follow works in. */
private const val NanosPerMillis: Float = 1_000_000f

/** Longest frame the follow will believe. Caps the catch-up after a stall into something smooth. */
private const val MaxFrameMillis: Float = 64f

/**
 * Travel short enough to be left unpaced.
 *
 * One row, so a notch of a stepped mouse lands as promptly as it was asked for while a long,
 * sustained scroll is still held to its top speed.
 */
private const val PacedBeyondRows: Float = 1f

/**
 * Position of one wheel of a [WheelPicker].
 *
 * The wheel's entire position is one number: the row sitting in the centre slot, fractional while
 * the wheel is between rows. Selection, the per-row falloff and the snapping target are all read off
 * that one number, so nothing in the component can report a selection the wheel is not actually
 * showing.
 *
 * The state deliberately does **not** hold the items. It holds an index; [WheelPickerColumn] holds
 * the list and tells the state how long it is. A state that also kept a copy of the list had two
 * versions of it — one of them a frame stale — and no way to notice when the caller's list changed
 * under a selection that was already out of range.
 *
 * Create one with [rememberWheelPickerColumnState].
 *
 * @param initialIndex Item the wheel starts on. Negative values start on the first item.
 * @property infinite Whether the wheel repeats, so that scrolling past the last item continues at
 *   the first. Requested here but honoured only while the column has more than one item, since a
 *   one-item wheel has nothing to repeat.
 */
@Stable
public class WheelPickerColumnState internal constructor(
    initialIndex: Int,
    public val infinite: Boolean,
) {
    /** Snapshot state behind [offset], so anything reading the position follows the wheel. */
    private var offsetState by mutableFloatStateOf(initialIndex.coerceAtLeast(0).toFloat())

    /** Row at the centre slot, fractional between rows. */
    internal val offset: Float get() = offsetState

    /** Backing snapshot state for [itemCount], updated by the column on every composition. */
    private var itemCountState by mutableIntStateOf(0)

    /** Number of items the column is showing. `0` until the column has first composed. */
    public val itemCount: Int get() = itemCountState

    /** Height of one row in pixels, as measured by the column. `0f` before the first layout. */
    internal var rowHeightPx: Float = 0f

    /**
     * Absolute row a run of scroll-wheel steps is heading for, or `null` when none is pending.
     *
     * Kept so that a notch arriving mid-flight adds to where the wheel was going rather than to
     * where it happens to have reached, which is what makes three quick notches move three rows
     * instead of some smaller number nobody can predict.
     */
    private var stepTarget: Float? = null

    /** Whether a [followSteps] loop already owns the current run of steps. */
    private var isStepping: Boolean = false

    /**
     * How much the current run of steps is sped up by, from how fast it is being scrolled.
     *
     * Multiplies both ceilings rather than only the travel asked for: raising the distance a gesture
     * may queue without also raising the speed the wheel may close it at would leave the wheel
     * lagging further and further behind a fast scroll instead of keeping up with it.
     */
    private var stepGain: Float = 1f

    /** Settling animation, replaced by the column with the one the theme asks for. */
    internal var snapSpec: AnimationSpec<Float> = DefaultSnapSpec

    /** How hard the wheel answers scrolling, replaced by the column with what it was given. */
    internal var scroll: WheelPickerScroll = WheelPickerScroll.Medium

    /**
     * [currentIndex] derived from the offset, so readers recompose only when the centred item
     * changes rather than on every frame of a scroll.
     */
    private val currentIndexState = derivedStateOf {
        val count = itemCountState
        if (count <= 0) 0 else wrapIndex(nearestRow(offsetState), count)
    }

    /**
     * Item in the centre slot right now.
     *
     * Follows the wheel as it turns, flipping as each row crosses the centre. Drive anything that
     * should feel attached to the gesture from this, and anything that should not be taken back from
     * [settledIndex].
     */
    public val currentIndex: Int by currentIndexState

    /**
     * Item the wheel has come to rest on.
     *
     * Unchanged while a scroll is in flight, so a caller that reports or persists a choice never
     * sees the rows the wheel merely passed over. This is the one to feed a callback.
     */
    public var settledIndex: Int by mutableIntStateOf(initialIndex.coerceAtLeast(0))
        private set

    /** Scroll target the column's gestures and flings drive; hands each delta to [consumeScroll]. */
    internal val scrollableState: ScrollableState = ScrollableState(::consumeScroll)

    /** Whether the wheel is being dragged, thrown, stepped or settled. */
    public val isScrollInProgress: Boolean get() = scrollableState.isScrollInProgress

    /** Whether an earlier item is reachable. Always `true` on an endless wheel. */
    public val canScrollBackward: Boolean
        get() = itemCountState > 0 && (isEndless || offsetState > ROW_EPSILON)

    /** Whether a later item is reachable. Always `true` on an endless wheel. */
    public val canScrollForward: Boolean
        get() = itemCountState > 0 &&
            (isEndless || offsetState < itemCountState - 1 - ROW_EPSILON)

    /** Whether repeating was both asked for and makes sense for the current item count. */
    private val isEndless: Boolean get() = infinite && itemCountState > 1

    /**
     * Tells the state how many items the column is showing.
     *
     * Called by the column on every composition. A list that has grown changes nothing; a list that
     * has shrunk past the current position pulls the wheel back to the last row that still exists,
     * because the alternative is a wheel resting on a row it can no longer draw.
     *
     * @param count Number of items. Negative counts are treated as empty.
     */
    internal fun updateItemCount(count: Int) {
        val safe = count.coerceAtLeast(0)
        if (safe == itemCountState) return
        itemCountState = safe
        if (safe == 0) {
            offsetState = 0f
            settledIndex = 0
            return
        }
        offsetState = coerceRowOffset(offsetState, safe, infinite)
        settledIndex = settledIndex.coerceIn(0, safe - 1)
    }

    /**
     * Applies a drag or throw, in pixels, and reports what the wheel could take.
     *
     * Returning less than it was offered is what stops a finite wheel from being pulled past its
     * ends; the remainder is what [WheelPicker] then decides whether to let escape to a scrolling
     * ancestor.
     *
     * @param deltaPx Pixels of travel, positive towards later items.
     * @return Pixels actually travelled.
     */
    private fun consumeScroll(deltaPx: Float): Float {
        val rowPx = rowHeightPx
        if (rowPx <= 0f || itemCountState <= 0) return 0f
        // A drag or throw supersedes anything the scroll wheel had asked for, so a step target left
        // behind by a run the drag interrupted cannot later be resumed from a stale position.
        stepTarget = null
        val before = offsetState
        val after = coerceRowOffset(before + deltaPx / rowPx, itemCountState, infinite)
        offsetState = after
        return (after - before) * rowPx
    }

    /**
     * Moves the wheel while holding the scroll lock.
     *
     * Programmatic moves are expressed in rows and written straight to the offset rather than pushed
     * through pixels, so they work before the column has been measured and cannot accumulate
     * rounding over a long animation.
     *
     * @param target Row position to move to. Assumed already within reach.
     */
    private fun setOffsetInScroll(target: Float) {
        offsetState = target
    }

    /**
     * Animates to an item, taking the shorter way round an endless wheel.
     *
     * @param index Item to move to. Clamped to the items a finite wheel has.
     */
    public suspend fun animateTo(index: Int) {
        if (itemCountState <= 0) return
        scrollableState.scroll {
            val from = offsetState
            val to = coerceRowOffset(
                offset = from + rowDeltaToIndex(from, index, itemCountState, infinite),
                itemCount = itemCountState,
                infinite = infinite,
            )
            animate(initialValue = from, targetValue = to, animationSpec = snapSpec) { value, _ ->
                setOffsetInScroll(value)
            }
        }
        finish()
    }

    /**
     * Jumps to an item without animating.
     *
     * Prefer this when the move is not the user's doing — restoring a screen, following a value
     * typed into a field elsewhere — because animating a jump of many rows is a long, meaningless
     * blur.
     *
     * @param index Item to move to. Clamped to the items a finite wheel has.
     */
    public suspend fun scrollTo(index: Int) {
        if (itemCountState <= 0) return
        scrollableState.scroll {
            val from = offsetState
            setOffsetInScroll(
                coerceRowOffset(
                    offset = from + rowDeltaToIndex(from, index, itemCountState, infinite),
                    itemCount = itemCountState,
                    infinite = infinite,
                ),
            )
        }
        finish()
    }

    /**
     * Advances by whole rows, as an arrow key or a page key does.
     *
     * @param rows Rows to travel, negative to go backwards.
     */
    internal suspend fun animateByRows(rows: Int) {
        if (itemCountState <= 0 || rows == 0) return
        scrollableState.scroll {
            val from = offsetState
            val to = coerceRowOffset(from + rows, itemCountState, infinite)
            animate(initialValue = from, targetValue = to, animationSpec = snapSpec) { value, _ ->
                setOffsetInScroll(value)
            }
        }
        finish()
    }

    /**
     * Adds scroll-wheel or trackpad travel to where the wheel is heading.
     *
     * Does no work of its own beyond moving the target, so a precision device emitting a hundred
     * events a second costs a hundred field writes rather than a hundred coroutines, each cancelling
     * and restarting the last. It is the restarting that reads as lag: an animation begun afresh on
     * every event starts from a standstill each time, so the wheel never keeps up with the hand.
     *
     * The target it accumulates into is held close to the wheel by
     * [WheelPickerScroll.maxRowsAhead], which is what stops a trackpad's momentum phase — events
     * that go on arriving after the fingers lift — from leaving the wheel chasing a point hundreds
     * of rows away.
     *
     * @param rows Rows of travel to add, negative to go backwards.
     * @param gain How much to speed this run up by, from how hard it is being scrolled.
     * @return `true` when the caller must start [followSteps]; `false` when a loop is already running
     *   and will pick the new target up on its next frame.
     */
    internal fun requestStepBy(rows: Float, gain: Float): Boolean {
        if (itemCountState <= 0 || rows == 0f) return false
        stepGain = gain
        stepTarget = stepTargetOffset(
            current = offsetState,
            pending = stepTarget,
            rows = rows,
            maxRowsAhead = scroll.maxRowsAhead * gain,
            itemCount = itemCountState,
            infinite = infinite,
        )
        if (isStepping) return false
        isStepping = true
        return true
    }

    /**
     * Carries the wheel to [stepTarget], following it as further scrolling moves it.
     *
     * One loop serves a whole run of scrolling: it takes the scroll lock once, closes the remaining
     * gap by the same fraction each millisecond, and returns when it arrives. Because it never
     * restarts, travel added mid-flight is absorbed at whatever speed the wheel already had.
     *
     * Cancelled when a drag takes the scroll lock, which is the correct outcome — the drag is the
     * newer intent — and leaves [isStepping] clear so the next scroll event starts a fresh run.
     */
    internal suspend fun followSteps() {
        try {
            scrollableState.scroll(MutatePriority.UserInput) {
                var lastFrameNanos = withFrameNanos { it }
                while (true) {
                    val target = stepTarget ?: return@scroll
                    val remaining = target - offsetState
                    if (abs(remaining) < ROW_EPSILON) {
                        setOffsetInScroll(target)
                        stepTarget = null
                        return@scroll
                    }
                    val frameNanos = withFrameNanos { it }
                    val elapsedMillis = ((frameNanos - lastFrameNanos) / NanosPerMillis)
                        .coerceIn(0f, MaxFrameMillis)
                    lastFrameNanos = frameNanos
                    setOffsetInScroll(
                        offsetState + followStepDelta(
                            remaining = remaining,
                            elapsedMillis = elapsedMillis,
                            tauMillis = StepFollowTauMillis,
                            maxRowsPerSecond = scroll.maxRowsPerSecond * stepGain,
                            pacedBeyondRows = PacedBeyondRows,
                        ),
                    )
                }
            }
        } finally {
            isStepping = false
            stepGain = 1f
        }
    }

    /**
     * Brings the wheel onto the nearest row and publishes the result.
     *
     * The component's guarantee that the wheel never rests between two rows lives here: whatever the
     * platform did with a drag, a throw or a scroll wheel, this runs once the wheel stops and
     * corrects it. On an endless wheel it also folds the offset back into the first cycle, which is
     * invisible — rows are drawn from the offset modulo the item count — and keeps a long session
     * from scrolling the number out of single-row `Float` precision.
     */
    internal suspend fun settle() {
        if (itemCountState <= 0) return
        if (!isSettledOffset(offsetState)) {
            scrollableState.scroll {
                val from = offsetState
                val to = coerceRowOffset(
                    offset = nearestRow(from).toFloat(),
                    itemCount = itemCountState,
                    infinite = infinite,
                )
                animate(initialValue = from, targetValue = to, animationSpec = snapSpec) { value, _ ->
                    setOffsetInScroll(value)
                }
            }
        }
        finish()
    }

    /** Snaps the offset exactly onto its row, folds an endless offset back, and publishes it. */
    private fun finish() {
        stepTarget = null
        if (itemCountState <= 0) return
        offsetState = normalizeRowOffset(
            offset = coerceRowOffset(
                offset = nearestRow(offsetState).toFloat(),
                itemCount = itemCountState,
                infinite = infinite,
            ),
            itemCount = itemCountState,
            infinite = infinite,
        )
        settledIndex = currentIndex
    }

    /** Factory for the saver [rememberWheelPickerColumnState] restores the state with. */
    internal companion object {
        /**
         * Saver that restores the wheel onto the item it was resting on.
         *
         * Only the settled item is kept. A wheel restored mid-gesture to a fractional position would
         * be restoring a moment the user never chose.
         *
         * @param infinite Whether the restored wheel repeats.
         * @return Saver for [rememberSaveable].
         */
        internal fun saver(infinite: Boolean): Saver<WheelPickerColumnState, Int> = Saver(
            save = { it.settledIndex },
            restore = { WheelPickerColumnState(initialIndex = it, infinite = infinite) },
        )
    }
}

/**
 * Remembers the position of one wheel across recompositions and process death.
 *
 * [initialIndex] seeds the wheel on the first composition only; afterwards the wheel's position is
 * the user's, and a caller that needs to move it should call [WheelPickerColumnState.animateTo] or
 * [WheelPickerColumnState.scrollTo]. Changing [infinite] builds a new state, because a wheel cannot
 * grow or lose its ends underneath a scroll in flight.
 *
 * @param initialIndex Item to start on.
 * @param infinite Whether the wheel repeats.
 * @return State to hand to a [WheelPickerColumn].
 */
@Composable
public fun rememberWheelPickerColumnState(
    initialIndex: Int = 0,
    infinite: Boolean = false,
): WheelPickerColumnState = rememberSaveable(
    infinite,
    saver = WheelPickerColumnState.saver(infinite),
) {
    WheelPickerColumnState(initialIndex = initialIndex, infinite = infinite)
}
