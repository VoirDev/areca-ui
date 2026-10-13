package dev.voir.areca.foundation.wheel

import kotlin.math.abs
import kotlin.math.exp
import kotlin.math.floor

/**
 * Arithmetic of a wheel's position, kept apart from the composables that use it.
 *
 * A column's whole position is one `Float` — the row currently at the centre, fractional while the
 * wheel is between rows. Every question the component asks of that number (which item is showing,
 * where a throw should land, how far away a target is on a wheel with no ends) is answered here, in
 * functions that take no state and can be tested without a composition.
 */

/** Distance below which a fractional offset counts as resting exactly on a row. */
internal const val ROW_EPSILON: Float = 0.001f

/** Milliseconds in a second, for turning a top speed in rows per second into rows per frame. */
private const val MillisPerSecond: Float = 1000f

/**
 * Maps a row position, which may be negative or beyond the list on an endless wheel, onto an index
 * into the items.
 *
 * @param row Row position.
 * @param itemCount Number of items. Must be positive.
 * @return Index in `0 until itemCount`.
 */
internal fun wrapIndex(row: Int, itemCount: Int): Int {
    require(itemCount > 0) { "itemCount must be positive, was $itemCount" }
    return ((row % itemCount) + itemCount) % itemCount
}

/**
 * Rounds an offset to the row it would rest on.
 *
 * Rounds halves up rather than to even, so the result does not depend on the platform's `round`.
 *
 * @param offset Row position, possibly fractional.
 * @return Nearest row.
 */
internal fun nearestRow(offset: Float): Int = floor(offset + 0.5f).toInt()

/**
 * Holds an offset inside the range the wheel may reach.
 *
 * An endless wheel has no range, so the offset is returned untouched; a finite one stops at its
 * first and last row rather than rubber-banding, because a wheel that can be pulled past its last
 * value invites the user to aim at nothing.
 *
 * @param offset Row position to constrain.
 * @param itemCount Number of items.
 * @param infinite Whether the wheel repeats.
 * @return Offset within reach.
 */
internal fun coerceRowOffset(offset: Float, itemCount: Int, infinite: Boolean): Float = when {
    itemCount <= 0 -> 0f
    infinite && itemCount > 1 -> offset
    else -> offset.coerceIn(0f, (itemCount - 1).toFloat())
}

/**
 * Reduces an endless wheel's offset to its first cycle.
 *
 * Called only once the wheel is at rest, where it changes nothing on screen — rows are drawn from
 * the offset modulo the item count — but keeps the number small enough that a long session cannot
 * scroll it out of the range where a `Float` still resolves single rows.
 *
 * @param offset Row position to reduce.
 * @param itemCount Number of items.
 * @param infinite Whether the wheel repeats.
 * @return Equivalent offset, in `0 until itemCount` for an endless wheel.
 */
internal fun normalizeRowOffset(offset: Float, itemCount: Int, infinite: Boolean): Float = when {
    itemCount <= 0 -> 0f
    infinite && itemCount > 1 -> offset - itemCount * floor(offset / itemCount)
    else -> offset
}

/**
 * Shortest signed distance from the current offset to a given item.
 *
 * On an endless wheel the same item sits at infinitely many row positions, and the naive choice
 * takes the long way round: December to January is eleven rows backwards, not one forwards. This
 * picks the representative nearest the current position, so a programmatic jump travels the way the
 * user would expect.
 *
 * @param offset Current row position.
 * @param index Item to travel to.
 * @param itemCount Number of items.
 * @param infinite Whether the wheel repeats.
 * @return Rows to travel, negative to go backwards. `0f` when the wheel has no items.
 */
internal fun rowDeltaToIndex(
    offset: Float,
    index: Int,
    itemCount: Int,
    infinite: Boolean,
): Float {
    if (itemCount <= 0) return 0f
    if (!infinite || itemCount == 1) {
        return index.coerceIn(0, itemCount - 1) - offset
    }
    val target = wrapIndex(index, itemCount)
    val current = normalizeRowOffset(offset, itemCount, infinite = true)
    val raw = target - current
    val cycles = floor(raw / itemCount + 0.5f)
    return raw - cycles * itemCount
}

/**
 * Row a throw should come to rest on.
 *
 * The projected travel is capped before it is rounded, so an over-eager velocity — a mouse wheel's
 * synthetic one especially — cannot spin a long wheel past anything the user could have aimed at.
 *
 * @param offset Row position the throw starts from.
 * @param projectedRows Rows the throw would travel if left to decay.
 * @param maxFlingRows Furthest a single throw may travel.
 * @param itemCount Number of items.
 * @param infinite Whether the wheel repeats.
 * @return Row position to animate to. Always a whole row within reach.
 */
internal fun flingTargetOffset(
    offset: Float,
    projectedRows: Float,
    maxFlingRows: Int,
    itemCount: Int,
    infinite: Boolean,
): Float {
    if (itemCount <= 0) return 0f
    val capped = projectedRows.coerceIn(-maxFlingRows.toFloat(), maxFlingRows.toFloat())
    val landed = nearestRow(offset + capped).toFloat()
    return coerceRowOffset(landed, itemCount, infinite)
}

/**
 * Whether the wheel is resting on a row rather than between two.
 *
 * @param offset Row position.
 * @return `true` when no settling animation is needed.
 */
internal fun isSettledOffset(offset: Float): Boolean =
    abs(offset - nearestRow(offset)) < ROW_EPSILON

/**
 * Row a run of scroll steps should be heading for, once a new event is added to it.
 *
 * The result is held within [maxRowsAhead] of where the wheel actually is, and that bound is what
 * paces the wheel. Without it a target accumulates every event of a gesture — and a desktop
 * trackpad's momentum phase goes on emitting events long after the fingers have lifted — so the
 * wheel ends up chasing a point hundreds of rows away and crosses a long column in a blink. Bounded,
 * a gesture moves the wheel for as long as it lasts and then stops within [maxRowsAhead] rows.
 *
 * @param current Row the wheel is on.
 * @param pending Row the run was already heading for, or `null` when this event begins one.
 * @param rows Rows this event asks for, negative to go backwards.
 * @param maxRowsAhead Furthest ahead of [current] the target may sit. Must be positive.
 * @param itemCount Number of items.
 * @param infinite Whether the wheel repeats.
 * @return Row to head for, within reach and within [maxRowsAhead] of [current].
 */
internal fun stepTargetOffset(
    current: Float,
    pending: Float?,
    rows: Float,
    maxRowsAhead: Float,
    itemCount: Int,
    infinite: Boolean,
): Float {
    val requested = (pending ?: current) + rows
    val paced = requested.coerceIn(current - maxRowsAhead, current + maxRowsAhead)
    return coerceRowOffset(paced, itemCount, infinite)
}

/**
 * How far the wheel moves towards its step target in one frame.
 *
 * Eases towards the target so the motion has no hard edges, then holds the result to a top speed so
 * that no gap, however it arose, can be crossed faster than the eye can follow the rows.
 *
 * The speed limit applies only once the wheel is further than [pacedBeyondRows] from where it is
 * going. Within that, the easing is left alone: a gap of about a row is not travel, it is a notch of
 * a stepped mouse or the last of a settle, and pacing those makes a wheel that should answer at once
 * feel as though it is thinking about it. Pacing exists to stop a long journey becoming a blur, and
 * a single row is not a journey.
 *
 * @param remaining Rows between the wheel and its target, signed.
 * @param elapsedMillis Time since the previous frame.
 * @param tauMillis Time constant of the easing. Must be positive.
 * @param maxRowsPerSecond Top speed. Must be positive.
 * @param pacedBeyondRows Distance beyond which the top speed starts to apply.
 * @return Rows to move this frame, signed.
 */
internal fun followStepDelta(
    remaining: Float,
    elapsedMillis: Float,
    tauMillis: Float,
    maxRowsPerSecond: Float,
    pacedBeyondRows: Float,
): Float {
    val eased = remaining * (1f - exp(-elapsedMillis / tauMillis))
    if (abs(remaining) <= pacedBeyondRows) return eased
    val limit = maxRowsPerSecond * elapsedMillis / MillisPerSecond
    return eased.coerceIn(-limit, limit)
}

/**
 * How long the current run of scrolling has been going on.
 *
 * A gap longer than [idleMillis] is read as the start of a new gesture and returns to zero, so a
 * short, careful scroll is never accelerated by a long one that came before it.
 *
 * @param previous Elapsed time carried from the last event.
 * @param gapMillis Time since the previous event.
 * @param idleMillis Gap above which a new gesture is assumed.
 * @return Milliseconds this gesture has been running, never negative.
 */
internal fun sustainedScrollMillis(
    previous: Float,
    gapMillis: Float,
    idleMillis: Float,
): Float = if (gapMillis >= idleMillis) 0f else previous + gapMillis.coerceAtLeast(0f)

/**
 * How much to speed the wheel up for a gesture that has run this long.
 *
 * Rises with the square of the elapsed fraction, so the first part of a gesture barely accelerates:
 * a scroll that turns out to be short should have been a careful one all along, and only a gesture
 * that is plainly a long one should wind the wheel up.
 *
 * @param sustainedMillis How long the gesture has been running.
 * @param windUpMillis How long a gesture takes to reach [maxGain]. Must be positive.
 * @param maxGain Largest multiplier. `1f` disables acceleration.
 * @return Multiplier in `1f..maxGain`.
 */
internal fun scrollGain(
    sustainedMillis: Float,
    windUpMillis: Float,
    maxGain: Float,
): Float {
    if (maxGain <= 1f || windUpMillis <= 0f) return 1f
    val fraction = (sustainedMillis / windUpMillis).coerceIn(0f, 1f)
    return 1f + (maxGain - 1f) * fraction * fraction
}
