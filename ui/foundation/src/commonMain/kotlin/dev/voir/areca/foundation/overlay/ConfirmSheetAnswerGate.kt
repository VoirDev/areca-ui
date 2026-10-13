package dev.voir.areca.foundation.overlay

/**
 * Decides whether a press on a [ConfirmSheet] counts as its answer.
 *
 * A sheet is answered once. A press counts only while the gate is armed and the sheet rests open —
 * [FlowModalState.PartiallyOpened] or [FlowModalState.Expanded] — and the first one that counts
 * disarms the gate. While the sheet is rising, sliding away or gone, nothing counts: the buttons move
 * under the finger then, and a second tap on Cancel would otherwise land on the confirming button.
 *
 * Not thread-safe; it is read and written on the main thread, from click handlers.
 *
 * @param isArmed Whether the sheet can be answered yet — `false` for a sheet that is not shown.
 */
internal class ConfirmSheetAnswerGate(isArmed: Boolean) {

    /** Whether a press can still count as the answer. */
    private var isArmed: Boolean = isArmed

    /**
     * Records a press, if it counts.
     *
     * @param modalState State of the sheet's modal when it was pressed.
     * @return `true` when the press is the answer and its action should run; `false` when it is
     *   ignored.
     */
    fun tryAnswer(modalState: FlowModalState): Boolean {
        val resting = modalState == FlowModalState.PartiallyOpened || modalState == FlowModalState.Expanded
        if (!isArmed || !resting) return false
        isArmed = false
        return true
    }
}
