package dev.voir.areca.foundation.selection

import kotlin.test.Test
import kotlin.test.assertEquals

/** Tests for [ToggleState.next], the rule that decides what a press on a [Toggle] asks for. */
class ToggleStateTest {
    @Test
    fun `a binary toggle flips between off and on`() {
        assertEquals(ToggleState.On, ToggleState.Off.next(BinaryToggleCycle))
        assertEquals(ToggleState.Off, ToggleState.On.next(BinaryToggleCycle))
    }

    @Test
    fun `a tri-state toggle walks through the partial state and wraps round`() {
        assertEquals(ToggleState.Indeterminate, ToggleState.Off.next(TriStateToggleCycle))
        assertEquals(ToggleState.On, ToggleState.Indeterminate.next(TriStateToggleCycle))
        assertEquals(ToggleState.Off, ToggleState.On.next(TriStateToggleCycle))
    }

    @Test
    fun `a state outside the cycle starts the cycle from its beginning`() {
        assertEquals(ToggleState.Off, ToggleState.Indeterminate.next(BinaryToggleCycle))
    }

    @Test
    fun `an empty cycle leaves the state alone`() {
        assertEquals(ToggleState.On, ToggleState.On.next(emptyList()))
    }
}
