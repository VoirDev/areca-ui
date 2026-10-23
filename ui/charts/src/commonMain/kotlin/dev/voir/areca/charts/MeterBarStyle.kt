package dev.voir.areca.charts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import dev.voir.areca.theme.ArecaTheme

/**
 * Colours of a [MeterBar].
 *
 * @property track The full length behind the fill.
 * @property fill The amount, unless the bar is given a colour of its own.
 * @property marker The reference marker.
 */
@Immutable
public data class MeterBarStyle(
    public val track: Color,
    public val fill: Color,
    public val marker: Color,
) {
    /** Theme-backed [MeterBarStyle] presets and their builder. */
    public companion object {
        /** Theme default: a brand fill on a neutral track, with an ink marker. */
        public val Default: MeterBarStyle
            @Composable
            get() = default()

        /**
         * Builds a style from the current theme.
         *
         * @param track The full length behind the fill.
         * @param fill The amount.
         * @param marker The reference marker. The strongest ink, so it reads against any fill.
         * @return Style built from the supplied colours.
         */
        @Composable
        public fun default(
            track: Color = ArecaTheme.colors.bgNeutralMuted,
            fill: Color = ArecaTheme.colors.bgBrandBase,
            marker: Color = ArecaTheme.colors.fgBase,
        ): MeterBarStyle = MeterBarStyle(track = track, fill = fill, marker = marker)
    }
}

/**
 * Default [MeterBarStyle] in scope, or `null` to use [MeterBarStyle.Default].
 *
 * Not seeded by [dev.voir.areca.theme.ArecaThemeProvider] — see [LocalBarChartStyle] for why.
 */
public val LocalMeterBarStyle: ProvidableCompositionLocal<MeterBarStyle?> =
    compositionLocalOf { null }
