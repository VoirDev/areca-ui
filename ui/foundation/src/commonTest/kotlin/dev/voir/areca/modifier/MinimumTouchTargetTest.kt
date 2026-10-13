package dev.voir.areca.modifier

import kotlin.test.Test
import kotlin.test.assertEquals

/** Slot sizing behind [minimumTouchTarget]. */
class MinimumTouchTargetTest {

    @Test
    fun `content smaller than the target grows to the target`() {
        assertEquals(48, touchTargetExtent(content = 32, minimum = 48, max = 400))
    }

    @Test
    fun `content larger than the target keeps its own size`() {
        assertEquals(56, touchTargetExtent(content = 56, minimum = 48, max = 400))
    }

    @Test
    fun `the reservation stops at the parent limit`() {
        assertEquals(40, touchTargetExtent(content = 32, minimum = 48, max = 40))
    }

    @Test
    fun `an unbounded parent allows the full reservation`() {
        assertEquals(48, touchTargetExtent(content = 20, minimum = 48, max = Int.MAX_VALUE))
    }
}
