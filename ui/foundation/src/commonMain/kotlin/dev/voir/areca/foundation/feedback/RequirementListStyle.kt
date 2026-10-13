package dev.voir.areca.foundation.feedback

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import dev.voir.areca.theme.ArecaTheme

/** Default [RequirementListStyle] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalRequirementListStyle: ProvidableCompositionLocal<RequirementListStyle?> =
    compositionLocalOf { null }

/**
 * Colors for each state a [Requirement] can be in.
 *
 * Every state names a mark and a text color separately, because the two do different jobs: the
 * mark is what a glance reads, and the text is what somebody reads when the glance was not enough.
 * A met rule keeps its text at full strength with only the mark turning green — the rule is still
 * true and still worth reading — while an unmet one colors both, because that is the line the
 * reader has to act on.
 *
 * @property pendingMark Mark of a rule nothing has been checked against yet.
 * @property pendingText Text of a rule nothing has been checked against yet.
 * @property metMark Mark of a rule the value satisfies.
 * @property metText Text of a rule the value satisfies.
 * @property unmetMark Mark of a rule the value was checked against and fails.
 * @property unmetText Text of a rule the value was checked against and fails.
 */
@Immutable
public data class RequirementListStyle(
    public val pendingMark: Color,
    public val pendingText: Color,
    public val metMark: Color,
    public val metText: Color,
    public val unmetMark: Color,
    public val unmetText: Color,
) {
    /**
     * The mark color for a state.
     *
     * @param state State of the rule.
     * @return Color to draw its mark in.
     */
    internal fun markFor(state: RequirementState): Color = when (state) {
        RequirementState.Pending -> pendingMark
        RequirementState.Met -> metMark
        RequirementState.Unmet -> unmetMark
    }

    /**
     * The text color for a state.
     *
     * @param state State of the rule.
     * @return Color to draw its text in.
     */
    internal fun textFor(state: RequirementState): Color = when (state) {
        RequirementState.Pending -> pendingText
        RequirementState.Met -> metText
        RequirementState.Unmet -> unmetText
    }

    /** The theme's default [RequirementListStyle], and [default] for building a variation on it. */
    public companion object {
        /** Theme default: quiet while pending, a green mark once met, danger throughout once unmet. */
        public val Default: RequirementListStyle
            @Composable
            get() = default()

        /**
         * Builds a style from the current theme.
         *
         * @param pendingMark Mark of a rule nothing has been checked against yet.
         * @param pendingText Text of a rule nothing has been checked against yet.
         * @param metMark Mark of a rule the value satisfies.
         * @param metText Text of a rule the value satisfies.
         * @param unmetMark Mark of a rule the value was checked against and fails.
         * @param unmetText Text of a rule the value was checked against and fails.
         * @return Style for a requirement list.
         */
        @Composable
        public fun default(
            pendingMark: Color = ArecaTheme.colors.fgMuted,
            pendingText: Color = ArecaTheme.colors.fgSubtle,
            metMark: Color = ArecaTheme.colors.fgSuccessText,
            metText: Color = ArecaTheme.colors.fgBase,
            unmetMark: Color = ArecaTheme.colors.fgDangerText,
            unmetText: Color = ArecaTheme.colors.fgDangerText,
        ): RequirementListStyle = RequirementListStyle(
            pendingMark = pendingMark,
            pendingText = pendingText,
            metMark = metMark,
            metText = metText,
            unmetMark = unmetMark,
            unmetText = unmetText,
        )
    }
}
