package dev.voir.areca.foundation.status

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import dev.voir.areca.foundation.icon.IconSize
import dev.voir.areca.theme.ArecaTheme

/**
 * Metrics of a [StatusMark].
 *
 * The mark's footprint is the halo's, [disc] × [haloScale], and it is the same in every state, so
 * nothing below it moves when the work ends.
 *
 * @property disc Diameter of the disc.
 * @property haloScale How much wider than the disc the halo reaches: far enough that the rings are
 * clearly leaving something, close enough that they fade before they reach what is underneath.
 * @property spinner Diameter of the arc turning inside the disc while working.
 * @property icon Size of the glyph inside the disc.
 */
@Immutable
public data class StatusMarkSize(
    public val disc: Dp,
    public val haloScale: Float,
    public val spinner: Dp,
    public val icon: Dp,
) {
    /** The theme's default [StatusMarkSize], and [default] for building a variation on it. */
    public companion object {
        /** Theme default: a mark that carries a screen of its own. */
        public val Default: StatusMarkSize
            @Composable
            get() = default()

        /**
         * Builds a size from the current theme.
         *
         * @param disc Diameter of the disc.
         * @param haloScale How much wider than the disc the halo reaches.
         * @param spinner Diameter of the arc turning inside the disc.
         * @param icon Size of the glyph inside the disc.
         * @return Size for a mark.
         */
        @Composable
        public fun default(
            disc: Dp = ArecaTheme.spacing.xl8,
            haloScale: Float = 1.7f,
            // Leaves a ring of the disc between the arc and the glyph it turns around.
            spinner: Dp = ArecaTheme.spacing.xl7,
            icon: Dp = IconSize.Large,
        ): StatusMarkSize = StatusMarkSize(
            disc = disc,
            haloScale = haloScale,
            spinner = spinner,
            icon = icon,
        )
    }
}

/** Default [StatusMarkSize] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalStatusMarkSize: ProvidableCompositionLocal<StatusMarkSize?> =
    compositionLocalOf { null }
