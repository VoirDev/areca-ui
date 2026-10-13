package dev.voir.areca.foundation.status

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.ButtonStyle
import dev.voir.areca.theme.ArecaTheme

/**
 * Button styles of [StatusSceneActions].
 *
 * @property primary Style of the main button.
 * @property secondary Style of the quieter button under it.
 */
@Immutable
public data class StatusSceneActionsStyle(
    public val primary: ButtonStyle,
    public val secondary: ButtonStyle,
) {
    /** The theme's default [StatusSceneActionsStyle], and [default] for building a variation on it. */
    public companion object {
        /** Theme default: a dark neutral pill over a plain neutral one. */
        public val Default: StatusSceneActionsStyle
            @Composable
            get() = default()

        /**
         * Builds a style from the current theme.
         *
         * @param primaryColor Colour of the main button — the brand for "done", the dark neutral for
         *   a way back from a problem. Only read by the default [primary].
         * @param primary Style of the main button: a solid pill in [primaryColor].
         * @param secondary Style of the quieter button: a plain neutral pill.
         * @return Style for the actions.
         */
        @Composable
        public fun default(
            primaryColor: ActionColor = ActionColor.NeutralDark,
            primary: ButtonStyle = ButtonStyle.solid(primaryColor, shape = ArecaTheme.shapes.full),
            secondary: ButtonStyle = ButtonStyle.plain(ActionColor.Neutral, shape = ArecaTheme.shapes.full),
        ): StatusSceneActionsStyle = StatusSceneActionsStyle(
            primary = primary,
            secondary = secondary,
        )
    }
}

/** Default [StatusSceneActionsStyle] in scope, or `null` to use [StatusSceneActionsStyle.Default]. */
public val LocalStatusSceneActionsStyle: ProvidableCompositionLocal<StatusSceneActionsStyle?> =
    compositionLocalOf { null }
