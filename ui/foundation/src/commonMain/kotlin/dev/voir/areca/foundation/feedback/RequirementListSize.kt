package dev.voir.areca.foundation.feedback

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.theme.ArecaTheme

/** Default [RequirementListSize] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalRequirementListSize: ProvidableCompositionLocal<RequirementListSize?> =
    compositionLocalOf { null }

/**
 * Metrics for a [RequirementList].
 *
 * @property textStyle Type for each rule. One step under body text: the list explains a field and
 *   should not compete with it.
 * @property mark Size of the mark beside each rule.
 * @property markGap Space between a mark and its text.
 * @property rowGap Space between two rows of rules.
 * @property columnGap Space between two columns, when the list has more than one.
 */
@Immutable
public data class RequirementListSize(
    public val textStyle: TextStyle,
    public val mark: Dp,
    public val markGap: Dp,
    public val rowGap: Dp,
    public val columnGap: Dp,
) {
    /** The theme's default [RequirementListSize], and [default] for building a variation on it. */
    public companion object {
        /** Theme default, sized to sit under a field's message line. */
        public val Default: RequirementListSize
            @Composable
            get() = default()

        /**
         * Builds a size from the current theme.
         *
         * @param textStyle Type for each rule.
         * @param mark Size of the mark beside each rule.
         * @param markGap Space between a mark and its text.
         * @param rowGap Space between two rows of rules.
         * @param columnGap Space between two columns.
         * @return Size for a requirement list.
         */
        @Composable
        public fun default(
            textStyle: TextStyle = ArecaTheme.typography.bodySmall,
            mark: Dp = 14.dp,
            markGap: Dp = ArecaTheme.spacing.sm,
            rowGap: Dp = ArecaTheme.spacing.sm,
            columnGap: Dp = ArecaTheme.spacing.lg,
        ): RequirementListSize = RequirementListSize(
            textStyle = textStyle,
            mark = mark,
            markGap = markGap,
            rowGap = rowGap,
            columnGap = columnGap,
        )
    }
}
