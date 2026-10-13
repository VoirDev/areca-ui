package dev.voir.areca.foundation.feedback

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import dev.voir.areca.theme.ArecaTheme

/**
 * Colors of a [StepIndicator].
 *
 * @property reached Fill of the segments for the steps done and the step on screen.
 * @property ahead Fill of the segments for the steps still to come.
 */
@Immutable
public data class StepIndicatorStyle(
    public val reached: Color,
    public val ahead: Color,
) {
    /** The theme's default [StepIndicatorStyle], and [default] for building a variation on it. */
    public companion object {
        /** Theme default. */
        public val Default: StepIndicatorStyle
            @Composable
            get() = default()

        /**
         * Builds a style from the current theme.
         *
         * The steps ahead take the strong border grey: a four-dp segment at the muted step
         * disappears on a grey page.
         *
         * @param reached Fill of the segments for the steps done and the step on screen.
         * @param ahead Fill of the segments for the steps still to come.
         * @return Style for an indicator.
         */
        @Composable
        public fun default(
            reached: Color = ArecaTheme.colors.accentBase,
            ahead: Color = ArecaTheme.colors.borderStrong,
        ): StepIndicatorStyle = StepIndicatorStyle(reached = reached, ahead = ahead)
    }
}

/** Default [StepIndicatorStyle] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalStepIndicatorStyle: ProvidableCompositionLocal<StepIndicatorStyle?> =
    compositionLocalOf { null }
