package dev.voir.areca.keyboard

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.IntSize
import kotlin.test.Test
import kotlin.test.assertEquals

/** The area [KeyboardHost] brings into view for a target the keyboard is drawn over. */
class KeyboardHostTest {
    @Test
    fun `clearance spans the target and a keyboard-high band below it`() {
        val clearance = keyboardClearance(targetSize = IntSize(width = 300, height = 40), keyboardHeightPx = 500)

        assertEquals(Rect(left = 0f, top = 0f, right = 300f, bottom = 540f), clearance)
    }

    @Test
    fun `clearance before the keyboard is measured is the target alone`() {
        val clearance = keyboardClearance(targetSize = IntSize(width = 300, height = 40), keyboardHeightPx = 0)

        assertEquals(Rect(left = 0f, top = 0f, right = 300f, bottom = 40f), clearance)
    }
}
