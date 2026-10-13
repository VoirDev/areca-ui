package dev.voir.areca.foundation.field.select

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * The selection a [SelectFlowModal] is editing, kept apart from the one its caller holds.
 *
 * One instance belongs to one committed selection: when the caller's values change, the modal
 * builds a new draft rather than patching this one, so a draft can never outlive the selection it
 * was started from.
 *
 * @param Value Type of the values being selected.
 * @property committed Selection the caller held when the draft was started, and what [discard]
 *   returns to.
 */
@Stable
internal class SelectFlowModalDraft<Value>(
    private val committed: Set<Value>,
) {
    /** Selection as edited so far. Starts at [committed] and is observable by composition. */
    var values: Set<Value> by mutableStateOf(committed)
        private set

    /**
     * Applies the option the user acted on to the draft.
     *
     * @param option Option the user acted on.
     * @param selectionMode Whether one value or many can be held at once.
     * @return The draft afterwards, which is what the modal reports through its `onChange`.
     */
    fun select(option: SelectOption<Value>, selectionMode: SelectSelectionMode): Set<Value> {
        values = selectionMode.applyTo(values, option)
        return values
    }

    /** Abandons every edit and returns the draft to [committed], as a closed sheet must. */
    fun discard() {
        values = committed
    }
}
