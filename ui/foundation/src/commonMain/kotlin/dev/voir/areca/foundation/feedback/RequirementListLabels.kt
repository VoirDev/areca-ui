package dev.voir.areca.foundation.feedback

import androidx.compose.runtime.Immutable

/**
 * What assistive technology reads after each rule of a [RequirementList].
 *
 * The library ships no localization, so the words are parameters: pass your own translations, or
 * [English] for an English-only product. Pending and unmet rules read the same — the difference
 * between them is emphasis, which a screen reader does not need to be told twice.
 *
 * @property met Read after a rule the value satisfies.
 * @property notMet Read after a rule that is pending or unmet.
 */
@Immutable
public data class RequirementListLabels(
    public val met: String,
    public val notMet: String,
) {
    /**
     * The words read for a state.
     *
     * @param state State of the rule.
     * @return [met] for [RequirementState.Met], [notMet] otherwise.
     */
    internal fun describe(state: RequirementState): String = when (state) {
        RequirementState.Met -> met
        RequirementState.Pending, RequirementState.Unmet -> notMet
    }

    /** Ready-made label sets. */
    public companion object {
        /** English wording: "met" and "not met". */
        public val English: RequirementListLabels = RequirementListLabels(
            met = "met",
            notMet = "not met",
        )
    }
}
