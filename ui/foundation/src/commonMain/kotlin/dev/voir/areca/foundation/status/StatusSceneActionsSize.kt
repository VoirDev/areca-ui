package dev.voir.areca.foundation.status

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import dev.voir.areca.foundation.button.ButtonSize
import dev.voir.areca.theme.ArecaTheme

/**
 * Button sizes and spacing of [StatusSceneActions].
 *
 * @property primary Size of the main button.
 * @property secondary Size of the quieter button under it.
 * @property gap Space between the two buttons.
 * @property primaryMinWidth Least width of the main button. Wide enough to read as the screen's one
 *   action rather than as a chip, short of a bar run edge to edge.
 */
@Immutable
public data class StatusSceneActionsSize(
    public val primary: ButtonSize,
    public val secondary: ButtonSize,
    public val gap: Dp,
    public val primaryMinWidth: Dp,
) {
    /** The theme's default [StatusSceneActionsSize], and [default] for building a variation on it. */
    public companion object {
        /** Theme default: a large main button over a medium one. */
        public val Default: StatusSceneActionsSize
            @Composable
            get() = default()

        /**
         * Builds a size from the current theme.
         *
         * @param primary Size of the main button.
         * @param secondary Size of the quieter button.
         * @param gap Space between the two buttons.
         * @param primaryMinWidth Least width of the main button.
         * @return Size for the actions.
         */
        @Composable
        public fun default(
            primary: ButtonSize = ButtonSize.Large,
            secondary: ButtonSize = ButtonSize.Medium,
            gap: Dp = ArecaTheme.spacing.md,
            primaryMinWidth: Dp = ArecaTheme.spacing.xl9 * 2,
        ): StatusSceneActionsSize = StatusSceneActionsSize(
            primary = primary,
            secondary = secondary,
            gap = gap,
            primaryMinWidth = primaryMinWidth,
        )
    }
}

/** Default [StatusSceneActionsSize] in scope, or `null` to use [StatusSceneActionsSize.Default]. */
public val LocalStatusSceneActionsSize: ProvidableCompositionLocal<StatusSceneActionsSize?> =
    compositionLocalOf { null }
