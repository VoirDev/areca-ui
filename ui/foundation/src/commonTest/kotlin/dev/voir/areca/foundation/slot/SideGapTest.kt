package dev.voir.areca.foundation.slot

import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** When a slot row keeps the gap beside a side slot, which is what lines fixed columns up. */
class SideGapTest {

    @Test
    fun `a side slot with content keeps its gap whatever its rule`() {
        assertTrue(drawsSideGap(SlotSize.Wrap, hasContent = true))
        assertTrue(drawsSideGap(SlotSize.Fixed(40.dp), hasContent = true))
        assertTrue(drawsSideGap(SlotSize.Weight(1f), hasContent = true))
    }

    @Test
    fun `an empty wrapping slot drops its gap so the row closes up`() {
        assertFalse(drawsSideGap(SlotSize.Wrap, hasContent = false))
    }

    @Test
    fun `an empty weighted slot drops its gap`() {
        assertFalse(drawsSideGap(SlotSize.Weight(1f), hasContent = false))
    }

    @Test
    fun `an empty fixed slot keeps its gap so the middle lines up with filled rows`() {
        assertTrue(drawsSideGap(SlotSize.Fixed(40.dp), hasContent = false))
    }
}
