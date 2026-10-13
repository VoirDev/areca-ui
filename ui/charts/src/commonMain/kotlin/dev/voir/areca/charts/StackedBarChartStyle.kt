package dev.voir.areca.charts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import dev.voir.areca.theme.ArecaTheme

/**
 * Colors of a [StackedBarChart].
 *
 * @property track Color of the part of the bar no segment covers.
 * @property palette Fills given, in order, to segments that carry no colour of their own: the
 *   segment at index `i` takes `palette[i % palette.size]`. Not empty.
 * @throws IllegalArgumentException When [palette] is empty.
 */
@Immutable
public data class StackedBarChartStyle(
    public val track: Color,
    public val palette: List<Color>,
) {
    init {
        require(palette.isNotEmpty()) { "A stacked bar palette needs at least one colour." }
    }

    /** Theme-backed [StackedBarChartStyle] preset and its builder. */
    public companion object {
        /** Theme default: the theme's solid semantic fills on a muted track. */
        public val Default: StackedBarChartStyle
            @Composable
            get() = default()

        /**
         * Builds a style from the current theme.
         *
         * The default palette is the theme's solid fills in an order that never puts the danger red
         * beside the success green, wrapping around included, since that pair is the one most
         * readers with a colour-vision deficiency cannot tell apart.
         *
         * @param track Color of the uncovered part of the bar.
         * @param palette Fills given in order to segments without a colour of their own.
         * @return Style built from the given values.
         */
        @Composable
        public fun default(
            track: Color = ArecaTheme.colors.bgMuted,
            palette: List<Color> = listOf(
                ArecaTheme.colors.bgBrandBase,
                ArecaTheme.colors.bgSuccessBase,
                ArecaTheme.colors.bgWarnBase,
                ArecaTheme.colors.bgInfoBase,
                ArecaTheme.colors.bgDangerBase,
            ),
        ): StackedBarChartStyle = StackedBarChartStyle(track = track, palette = palette)
    }
}

/**
 * Default [StackedBarChartStyle] in scope, or `null` to use [StackedBarChartStyle.Default].
 *
 * Not seeded by [dev.voir.areca.theme.ArecaThemeProvider] — see [LocalBarChartStyle] for why.
 */
public val LocalStackedBarChartStyle: ProvidableCompositionLocal<StackedBarChartStyle?> =
    compositionLocalOf { null }
