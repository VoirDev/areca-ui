package dev.voir.areca.crop

import androidx.compose.ui.input.key.Key
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** How held keys are told from separate presses. */
class CropKeyHoldTest {

    @Test
    fun `repeats of a held key continue its adjustment`() {
        val hold = CropKeyHold()

        assertTrue(hold.press(Key.DirectionRight))
        assertFalse(hold.press(Key.DirectionRight))
        assertFalse(hold.press(Key.DirectionRight))
    }

    @Test
    fun `a released key starts a new adjustment when pressed again`() {
        val hold = CropKeyHold()
        hold.press(Key.DirectionRight)

        assertTrue(hold.release(Key.DirectionRight))
        assertTrue(hold.press(Key.DirectionRight))
    }

    @Test
    fun `another key starts a new adjustment`() {
        val hold = CropKeyHold()
        hold.press(Key.DirectionRight)

        assertTrue(hold.press(Key.DirectionLeft))
        assertFalse(hold.release(Key.DirectionRight))
        assertTrue(hold.isHolding)
    }

    @Test
    fun `clearing ends the hold`() {
        val hold = CropKeyHold()
        hold.press(Key.DirectionRight)

        assertTrue(hold.clear())
        assertFalse(hold.isHolding)
        assertTrue(hold.press(Key.DirectionRight))
    }
}
