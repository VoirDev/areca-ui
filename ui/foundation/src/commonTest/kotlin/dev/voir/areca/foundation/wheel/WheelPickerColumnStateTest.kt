package dev.voir.areca.foundation.wheel

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** Tests for [WheelPickerColumnState] and the position a column reports. */
class WheelPickerColumnStateTest {
    private fun state(initialIndex: Int, itemCount: Int, infinite: Boolean = false) =
        WheelPickerColumnState(initialIndex = initialIndex, infinite = infinite).apply {
            updateItemCount(itemCount)
        }

    @Test
    fun `a wheel starts on its initial item`() {
        val wheel = state(initialIndex = 3, itemCount = 10)

        assertEquals(3, wheel.currentIndex)
        assertEquals(3, wheel.settledIndex)
    }

    @Test
    fun `a list that shrinks past the wheel pulls it back to the last item`() {
        val wheel = state(initialIndex = 8, itemCount = 10)

        wheel.updateItemCount(5)

        assertEquals(4, wheel.currentIndex)
        assertEquals(4, wheel.settledIndex)
    }

    @Test
    fun `a finite wheel cannot scroll past its ends`() {
        val first = state(initialIndex = 0, itemCount = 3)
        val last = state(initialIndex = 2, itemCount = 3)

        assertFalse(first.canScrollBackward)
        assertTrue(first.canScrollForward)
        assertTrue(last.canScrollBackward)
        assertFalse(last.canScrollForward)
    }

    @Test
    fun `an endless wheel can always scroll both ways`() {
        val wheel = state(initialIndex = 0, itemCount = 3, infinite = true)

        assertTrue(wheel.canScrollBackward)
        assertTrue(wheel.canScrollForward)
    }

    @Test
    fun `an empty wheel cannot scroll`() {
        val wheel = state(initialIndex = 0, itemCount = 0)

        assertFalse(wheel.canScrollBackward)
        assertFalse(wheel.canScrollForward)
    }

    @Test
    fun `a jump settles on the item asked for`() = runTest {
        val wheel = state(initialIndex = 0, itemCount = 10)

        wheel.scrollTo(7)

        assertEquals(7, wheel.currentIndex)
        assertEquals(7, wheel.settledIndex)
    }

    @Test
    fun `a jump past the end of a finite wheel stops on the last item`() = runTest {
        val wheel = state(initialIndex = 0, itemCount = 10)

        wheel.scrollTo(25)

        assertEquals(9, wheel.settledIndex)
    }

    @Test
    fun `a jump on an endless wheel lands on the item without leaving the first cycle`() = runTest {
        val wheel = state(initialIndex = 1, itemCount = 12, infinite = true)

        wheel.scrollTo(11)

        assertEquals(11, wheel.settledIndex)
        assertEquals(11f, wheel.offset)
    }

    @Test
    fun `a wheel reports one step between every pair of items`() {
        val info = wheelRangeInfo(settledIndex = 4, itemCount = 12)

        assertEquals(4f, info.current)
        assertEquals(0f..11f, info.range)
        assertEquals(10, info.steps)
    }

    @Test
    fun `a one-item wheel reports no steps`() {
        val info = wheelRangeInfo(settledIndex = 0, itemCount = 1)

        assertEquals(0f..0f, info.range)
        assertEquals(0, info.steps)
    }
}
