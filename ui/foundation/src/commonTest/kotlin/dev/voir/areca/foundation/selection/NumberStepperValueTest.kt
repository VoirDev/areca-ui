package dev.voir.areca.foundation.selection

import kotlin.test.Test
import kotlin.test.assertEquals

/** Tests for the value rules of [NumberStepper]. */
class NumberStepperValueTest {
    @Test
    fun `a step moves the value by one inside the bounds`() {
        assertEquals(6, steppedValue(value = 5, delta = 1, minValue = 0, maxValue = 10))
        assertEquals(4, steppedValue(value = 5, delta = -1, minValue = 0, maxValue = 10))
    }

    @Test
    fun `a step stops at the bound`() {
        assertEquals(10, steppedValue(value = 10, delta = 1, minValue = 0, maxValue = 10))
        assertEquals(0, steppedValue(value = 0, delta = -1, minValue = 0, maxValue = 10))
    }

    @Test
    fun `a step at the edge of Int does not wrap round`() {
        assertEquals(
            Int.MAX_VALUE,
            steppedValue(
                value = Int.MAX_VALUE,
                delta = 1,
                minValue = Int.MIN_VALUE,
                maxValue = Int.MAX_VALUE,
            ),
        )
        assertEquals(
            Int.MIN_VALUE,
            steppedValue(
                value = Int.MIN_VALUE,
                delta = -1,
                minValue = Int.MIN_VALUE,
                maxValue = Int.MAX_VALUE,
            ),
        )
    }

    @Test
    fun `a small range reports one step per whole number`() {
        val info = numberStepperRangeInfo(value = 14, minValue = 12, maxValue = 24)

        assertEquals(14f, info.current)
        assertEquals(12f..24f, info.range)
        assertEquals(11, info.steps)
    }

    @Test
    fun `an unbounded range reports no steps`() {
        val info = numberStepperRangeInfo(
            value = 0,
            minValue = Int.MIN_VALUE,
            maxValue = Int.MAX_VALUE,
        )

        assertEquals(0, info.steps)
    }

    @Test
    fun `an adjustment is rounded to a whole number and held within the bounds`() {
        assertEquals(15, numberStepperProgressValue(target = 14.6f, minValue = 12, maxValue = 24))
        assertEquals(24, numberStepperProgressValue(target = 30f, minValue = 12, maxValue = 24))
        assertEquals(12, numberStepperProgressValue(target = -3f, minValue = 12, maxValue = 24))
    }
}
