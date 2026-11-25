package dev.voir.areca.foundation.overlay

import androidx.compose.ui.unit.Constraints
import kotlin.test.Test
import kotlin.test.assertEquals

/** A sheet's body takes what its header, footer and inset leave, and stays unlimited in an unlimited sheet. */
class SheetBodyRoomTest {

    @Test
    fun `a limited sheet leaves its body what the header footer and inset do not take`() {
        assertEquals(700, sheetBodyRoom(maxHeight = 1000, used = 300))
    }

    @Test
    fun `a body never gets a negative height`() {
        assertEquals(0, sheetBodyRoom(maxHeight = 200, used = 300))
    }

    @Test
    fun `an unlimited sheet leaves its body unlimited`() {
        assertEquals(Constraints.Infinity, sheetBodyRoom(maxHeight = Constraints.Infinity, used = 300))
        // The body's constraints are representable at any width, as the crash on iOS was not.
        Constraints(minWidth = 1280, maxWidth = 1280, maxHeight = sheetBodyRoom(Constraints.Infinity, used = 28))
    }
}
