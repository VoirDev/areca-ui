package dev.voir.areca.foundation.selection

import androidx.compose.ui.state.ToggleableState
import kotlin.test.Test
import kotlin.test.assertEquals

/** What checkboxes, switches and the rows built on them report to accessibility as their state. */
class ToggleableStateTest {

    @Test
    fun `an unchecked checkbox row is off`() {
        assertEquals(ToggleableState.Off, checkboxToggleableState(checked = false, indeterminate = false))
    }

    @Test
    fun `a checked checkbox row is on`() {
        assertEquals(ToggleableState.On, checkboxToggleableState(checked = true, indeterminate = false))
    }

    @Test
    fun `a checked checkbox row showing the dash is indeterminate`() {
        assertEquals(
            ToggleableState.Indeterminate,
            checkboxToggleableState(checked = true, indeterminate = true),
        )
    }

    @Test
    fun `an unchecked checkbox row stays off even when marked indeterminate`() {
        assertEquals(ToggleableState.Off, checkboxToggleableState(checked = false, indeterminate = true))
    }

    @Test
    fun `each switch position maps to the matching toggleable state`() {
        assertEquals(ToggleableState.Off, ToggleState.Off.toToggleableState())
        assertEquals(ToggleableState.Indeterminate, ToggleState.Indeterminate.toToggleableState())
        assertEquals(ToggleableState.On, ToggleState.On.toToggleableState())
    }
}
