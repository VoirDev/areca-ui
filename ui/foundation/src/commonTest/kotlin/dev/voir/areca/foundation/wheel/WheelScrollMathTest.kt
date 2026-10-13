package dev.voir.areca.foundation.wheel

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** Position arithmetic of a wheel: wrapping, clamping, shortest routes and where a throw lands. */
class WheelScrollMathTest {

    @Test
    fun `a row below the list wraps onto the end`() {
        assertEquals(11, wrapIndex(row = -1, itemCount = 12))
        assertEquals(0, wrapIndex(row = -12, itemCount = 12))
        assertEquals(1, wrapIndex(row = 13, itemCount = 12))
    }

    @Test
    fun `halves round up on every platform`() {
        assertEquals(1, nearestRow(0.5f))
        assertEquals(2, nearestRow(1.5f))
        assertEquals(0, nearestRow(-0.5f))
        assertEquals(-1, nearestRow(-0.51f))
    }

    @Test
    fun `a finite wheel stops at its ends`() {
        assertEquals(0f, coerceRowOffset(offset = -3f, itemCount = 12, infinite = false))
        assertEquals(11f, coerceRowOffset(offset = 40f, itemCount = 12, infinite = false))
    }

    @Test
    fun `an endless wheel has no ends to stop at`() {
        assertEquals(-3f, coerceRowOffset(offset = -3f, itemCount = 12, infinite = true))
        assertEquals(40f, coerceRowOffset(offset = 40f, itemCount = 12, infinite = true))
    }

    @Test
    fun `a single item cannot repeat`() {
        assertEquals(0f, coerceRowOffset(offset = 5f, itemCount = 1, infinite = true))
    }

    @Test
    fun `an endless offset folds back into the first cycle`() {
        assertEquals(1f, normalizeRowOffset(offset = 25f, itemCount = 12, infinite = true))
        assertEquals(11f, normalizeRowOffset(offset = -1f, itemCount = 12, infinite = true))
    }

    @Test
    fun `a finite offset is left where it is`() {
        assertEquals(7f, normalizeRowOffset(offset = 7f, itemCount = 12, infinite = false))
    }

    @Test
    fun `an endless wheel takes the short way round`() {
        // December to January is one row forwards, not eleven backwards.
        assertEquals(
            1f,
            rowDeltaToIndex(offset = 11f, index = 0, itemCount = 12, infinite = true),
        )
        assertEquals(
            -1f,
            rowDeltaToIndex(offset = 0f, index = 11, itemCount = 12, infinite = true),
        )
    }

    @Test
    fun `an endless wheel keeps the short route from a far-off cycle`() {
        assertEquals(
            1f,
            rowDeltaToIndex(offset = 47f, index = 0, itemCount = 12, infinite = true),
        )
    }

    @Test
    fun `a finite wheel travels straight to the item`() {
        assertEquals(
            -11f,
            rowDeltaToIndex(offset = 11f, index = 0, itemCount = 12, infinite = false),
        )
    }

    @Test
    fun `travelling to an item outside a finite wheel stops at the last one`() {
        assertEquals(
            1f,
            rowDeltaToIndex(offset = 10f, index = 99, itemCount = 12, infinite = false),
        )
    }

    @Test
    fun `a throw always lands on a whole row`() {
        val landed = flingTargetOffset(
            offset = 3.4f,
            projectedRows = 2.3f,
            maxFlingRows = 12,
            itemCount = 60,
            infinite = true,
        )
        assertEquals(6f, landed)
    }

    @Test
    fun `an over-eager velocity cannot spin the wheel away`() {
        val landed = flingTargetOffset(
            offset = 0f,
            projectedRows = 400f,
            maxFlingRows = 12,
            itemCount = 60,
            infinite = true,
        )
        assertEquals(12f, landed)
    }

    @Test
    fun `a throw off the end of a finite wheel stops at its last row`() {
        val landed = flingTargetOffset(
            offset = 10f,
            projectedRows = 8f,
            maxFlingRows = 12,
            itemCount = 12,
            infinite = false,
        )
        assertEquals(11f, landed)
    }

    @Test
    fun `a step target stays within reach of the wheel`() {
        // The runaway this exists to stop: a trackpad's momentum phase keeps sending events after
        // the fingers lift, so an unbounded target ends up hundreds of rows from the wheel.
        var target: Float? = null
        repeat(200) {
            target = stepTargetOffset(
                current = 0f,
                pending = target,
                rows = 1f,
                maxRowsAhead = 2.5f,
                itemCount = 201,
                infinite = false,
            )
        }
        assertEquals(2.5f, target)
    }

    @Test
    fun `a step target follows the wheel as it moves`() {
        val target = stepTargetOffset(
            current = 40f,
            pending = 42f,
            rows = 1f,
            maxRowsAhead = 2.5f,
            itemCount = 201,
            infinite = false,
        )
        assertEquals(42.5f, target)
    }

    @Test
    fun `a step target still stops at the end of a finite wheel`() {
        val target = stepTargetOffset(
            current = 10f,
            pending = null,
            rows = 5f,
            maxRowsAhead = 8f,
            itemCount = 12,
            infinite = false,
        )
        assertEquals(11f, target)
    }

    @Test
    fun `a gap is never crossed faster than the top speed`() {
        val delta = followStepDelta(
            remaining = 200f,
            elapsedMillis = 16f,
            tauMillis = 45f,
            maxRowsPerSecond = 20f,
            pacedBeyondRows = 1f,
        )
        // 20 rows a second over one frame, rather than the 60-odd rows easing alone would take.
        assertEquals(20f * 16f / 1000f, delta, absoluteTolerance = 1e-4f)
    }

    @Test
    fun `a notch is not held to the travelling speed`() {
        // One row is a notch of a stepped mouse, not a journey, so it lands as fast as the easing
        // carries it rather than being drawn out to the pace a long scroll is held to. Checked at a
        // long wheel's gentle base speed, where the difference is the whole point: paced, a single
        // notch would take a fifth of a second to move one row.
        val rowsPerSecond = 5f
        val paced = rowsPerSecond * 16f / 1000f
        val delta = followStepDelta(
            remaining = 1f,
            elapsedMillis = 16f,
            tauMillis = 45f,
            maxRowsPerSecond = rowsPerSecond,
            pacedBeyondRows = 1f,
        )
        assertTrue(delta > paced, "expected a notch to outrun the travelling pace, was $delta")
        assertTrue(delta < 1f)
    }

    @Test
    fun `travelling backwards is paced the same as travelling forwards`() {
        val delta = followStepDelta(
            remaining = -200f,
            elapsedMillis = 16f,
            tauMillis = 45f,
            maxRowsPerSecond = 20f,
            pacedBeyondRows = 1f,
        )
        assertEquals(-20f * 16f / 1000f, delta, absoluteTolerance = 1e-4f)
    }

    @Test
    fun `a short scroll is not accelerated`() {
        assertEquals(1f, scrollGain(sustainedMillis = 0f, windUpMillis = 600f, maxGain = 3f))
    }

    @Test
    fun `a sustained scroll reaches the full gain`() {
        assertEquals(3f, scrollGain(sustainedMillis = 900f, windUpMillis = 600f, maxGain = 3f))
    }

    @Test
    fun `the first part of a gesture barely accelerates`() {
        // Halfway through the wind-up is a quarter of the way up the gain, which is what keeps a
        // wheel aimable until the gesture is plainly a long one.
        val gain = scrollGain(sustainedMillis = 300f, windUpMillis = 600f, maxGain = 3f)
        assertEquals(1.5f, gain, absoluteTolerance = 1e-4f)
    }

    @Test
    fun `acceleration can be turned off`() {
        assertEquals(1f, scrollGain(sustainedMillis = 5000f, windUpMillis = 600f, maxGain = 1f))
    }

    @Test
    fun `a new gesture starts from rest`() {
        val sustained = sustainedScrollMillis(previous = 900f, gapMillis = 400f, idleMillis = 150f)
        assertEquals(0f, sustained)
    }

    @Test
    fun `a continuing gesture accumulates`() {
        var sustained = 0f
        repeat(10) {
            sustained = sustainedScrollMillis(
                previous = sustained,
                gapMillis = 8f,
                idleMillis = 150f,
            )
        }
        assertEquals(80f, sustained, absoluteTolerance = 1e-4f)
    }

    @Test
    fun `a trackpad and a mouse wind up at the same rate`() {
        // The whole point of measuring duration rather than a rate: a trackpad reporting every 8ms
        // and a mouse reporting every 120ms reach the same gain after the same wall-clock time,
        // where a units-per-second measure would have the trackpad accelerating fifteen times sooner.
        var trackpad = 0f
        repeat(75) {
            trackpad = sustainedScrollMillis(previous = trackpad, gapMillis = 8f, idleMillis = 150f)
        }
        var mouse = 0f
        repeat(5) {
            mouse = sustainedScrollMillis(previous = mouse, gapMillis = 120f, idleMillis = 150f)
        }
        assertEquals(600f, trackpad, absoluteTolerance = 1e-4f)
        assertEquals(600f, mouse, absoluteTolerance = 1e-4f)
        assertEquals(
            scrollGain(trackpad, windUpMillis = 600f, maxGain = 3f),
            scrollGain(mouse, windUpMillis = 600f, maxGain = 3f),
            absoluteTolerance = 1e-4f,
        )
    }

    @Test
    fun `a wheel paces itself from how many rows it holds`() {
        assertEquals(WheelPickerScroll.Short, WheelPickerScroll.forItemCount(2))
        assertEquals(WheelPickerScroll.Short, WheelPickerScroll.forItemCount(12))
        assertEquals(WheelPickerScroll.Medium, WheelPickerScroll.forItemCount(24))
        assertEquals(WheelPickerScroll.Medium, WheelPickerScroll.forItemCount(60))
        assertEquals(WheelPickerScroll.Long, WheelPickerScroll.forItemCount(201))
    }

    @Test
    fun `a long wheel is gentler at rest but winds up further than a short one`() {
        val short = WheelPickerScroll.Short
        val long = WheelPickerScroll.Long
        assertTrue(
            long.maxRowsPerSecond < short.maxRowsPerSecond,
            "a careless nudge on a long wheel loses the place, so its base must be gentler",
        )
        assertTrue(
            long.maxRowsPerSecond * long.acceleration.maxGain >
                short.maxRowsPerSecond * short.acceleration.maxGain,
            "a long wheel still has to be crossable, so its sustained speed must be higher",
        )
    }

    @Test
    fun `an offset on a row needs no settling`() {
        assertTrue(isSettledOffset(4f))
        assertFalse(isSettledOffset(4.2f))
    }
}
