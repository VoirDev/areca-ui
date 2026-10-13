package dev.voir.areca.foundation.overlay

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** Which presses on a confirmation sheet count as its answer. */
class ConfirmSheetAnswerGateTest {

    @Test
    fun `a press on an open sheet is the answer`() {
        val gate = ConfirmSheetAnswerGate(isArmed = true)

        assertTrue(gate.tryAnswer(FlowModalState.Expanded))
    }

    @Test
    fun `a second press after the answer is ignored`() {
        val gate = ConfirmSheetAnswerGate(isArmed = true)
        gate.tryAnswer(FlowModalState.Expanded)

        assertFalse(gate.tryAnswer(FlowModalState.Expanded))
    }

    @Test
    fun `a press while the sheet slides away is ignored`() {
        val gate = ConfirmSheetAnswerGate(isArmed = true)

        assertFalse(gate.tryAnswer(FlowModalState.Hiding))
    }

    @Test
    fun `a press while the sheet rises is ignored and leaves the answer open`() {
        val gate = ConfirmSheetAnswerGate(isArmed = true)

        assertFalse(gate.tryAnswer(FlowModalState.Opening))
        assertTrue(gate.tryAnswer(FlowModalState.PartiallyOpened))
    }

    @Test
    fun `a sheet that is not shown takes no answer`() {
        val gate = ConfirmSheetAnswerGate(isArmed = false)

        assertFalse(gate.tryAnswer(FlowModalState.Expanded))
    }
}
