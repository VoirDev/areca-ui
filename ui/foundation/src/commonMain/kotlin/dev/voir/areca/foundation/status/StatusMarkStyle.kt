package dev.voir.areca.foundation.status

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import dev.voir.areca.theme.ArecaTheme
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

/**
 * Colors and pace of a [StatusMark].
 *
 * @property disc Fill of the disc while working or stopped.
 * @property working Color of the arc, the glyph and the halo while working.
 * @property success Fill of the disc and color of the halo once the work succeeded.
 * @property onSuccess Color of the tick drawn on [success].
 * @property stoppedRing Color of the halo once the work stopped.
 * @property stoppedGlyph Color of the glyph once the work stopped.
 * @property haloPeriod How long one ring takes to travel from the disc's edge to nothing. Slow
 * enough to read as breathing rather than as an alarm.
 */
@Immutable
public data class StatusMarkStyle(
    public val disc: Color,
    public val working: Color,
    public val success: Color,
    public val onSuccess: Color,
    public val stoppedRing: Color,
    public val stoppedGlyph: Color,
    public val haloPeriod: Duration,
) {
    /** The theme's default [StatusMarkStyle], and [default] for building a variation on it. */
    public companion object {
        /** Theme default. */
        public val Default: StatusMarkStyle
            @Composable
            get() = default()

        /**
         * Builds a style from the current theme.
         *
         * Success is filled rather than tinted: it is the one moment on its screen allowed to be
         * loud. A stopped mark is grey with a danger glyph rather than red throughout, because the
         * sentence under it is what says what happened, and nothing may have been lost.
         *
         * @param disc Fill of the disc while working or stopped.
         * @param working Color of the arc, the glyph and the halo while working.
         * @param success Fill of the disc and color of the halo on success.
         * @param onSuccess Color of the tick.
         * @param stoppedRing Color of the halo once stopped.
         * @param stoppedGlyph Color of the glyph once stopped.
         * @param haloPeriod How long one ring takes to cross the halo.
         * @return Style for a mark.
         */
        @Composable
        public fun default(
            disc: Color = ArecaTheme.colors.bgSurface,
            working: Color = ArecaTheme.colors.fgBrand,
            success: Color = ArecaTheme.colors.bgSuccessBase,
            onSuccess: Color = ArecaTheme.colors.fgOnFill,
            stoppedRing: Color = ArecaTheme.colors.fgMuted,
            stoppedGlyph: Color = ArecaTheme.colors.fgDanger,
            haloPeriod: Duration = 2200.milliseconds,
        ): StatusMarkStyle = StatusMarkStyle(
            disc = disc,
            working = working,
            success = success,
            onSuccess = onSuccess,
            stoppedRing = stoppedRing,
            stoppedGlyph = stoppedGlyph,
            haloPeriod = haloPeriod,
        )
    }
}

/** Default [StatusMarkStyle] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalStatusMarkStyle: ProvidableCompositionLocal<StatusMarkStyle?> =
    compositionLocalOf { null }
