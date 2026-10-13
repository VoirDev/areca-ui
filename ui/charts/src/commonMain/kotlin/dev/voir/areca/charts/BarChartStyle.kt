package dev.voir.areca.charts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import dev.voir.areca.theme.ArecaTheme

/**
 * Semantic family a [BarChart]'s bars and tracks are drawn from.
 *
 * Picks the theme's solid fill for the bars and the same family's tint for the tracks behind them,
 * so a chart reads as one colour rather than a bar colour on an unrelated grey.
 */
public enum class BarChartColor {
    /**
     * Bars without sentiment: a mid grey that keeps 3:1 against the page in both themes, on a
     * muted neutral track.
     */
    Neutral,

    /** Bars in the product's own colour. The default. */
    Brand,

    /** Bars that read as a good outcome. */
    Success,

    /** Bars that call for attention. */
    Warning,

    /** Bars that read as a bad outcome. */
    Danger,

    /** Bars that carry information without sentiment, in the informational colour. */
    Info,
}

/**
 * Colors of a [BarChart].
 *
 * Build one with [of] for a semantic family, or take [Default]; change a single colour with `copy`.
 *
 * @property bg Fill of the chart's container, behind the plot and the labels.
 * @property plotBg Fill of the plot area the bars are drawn in.
 * @property axis Color of the x and y axis lines.
 * @property guide Color of the dashed guides drawn at the y-tick labels.
 * @property bar Fill of a bar that carries no [BarChartBar.fill] of its own.
 * @property track Color of the full-height track behind a bar that carries no
 *   [BarChartBar.trackColor] of its own.
 * @property xLabel Color of the per-bar labels under the plot.
 * @property groupLabel Color of the per-group labels under the bar labels.
 * @property yLabel Color of the y-tick labels beside the plot.
 */
@Immutable
public data class BarChartStyle(
    public val bg: Color,
    public val plotBg: Color,
    public val axis: Color,
    public val guide: Color,
    public val bar: Color,
    public val track: Color,
    public val xLabel: Color,
    public val groupLabel: Color,
    public val yLabel: Color,
) {
    /** Theme-backed [BarChartStyle] presets and their builder. */
    public companion object {
        /** Theme default: brand bars on brand-tinted tracks, on the surface colour. */
        public val Default: BarChartStyle
            @Composable
            get() = of(BarChartColor.Brand)

        /**
         * Builds a style from the current theme for one semantic family.
         *
         * [color] decides [bar] and [track]: the family's solid fill for the bars and its tint for
         * the tracks. Everything else is shared by every family and can be overridden here.
         *
         * @param color Semantic family of the bars and tracks.
         * @param bg Fill of the chart's container.
         * @param plotBg Fill of the plot area.
         * @param axis Color of the axis lines.
         * @param guide Color of the dashed y guides.
         * @param xLabel Color of the per-bar labels.
         * @param groupLabel Color of the per-group labels.
         * @param yLabel Color of the y-tick labels.
         * @return Style for a chart drawn in [color].
         */
        @Composable
        public fun of(
            color: BarChartColor,
            bg: Color = ArecaTheme.colors.bgSurface,
            plotBg: Color = ArecaTheme.colors.bgSurface,
            axis: Color = ArecaTheme.colors.borderBase,
            guide: Color = ArecaTheme.colors.borderBase,
            xLabel: Color = ArecaTheme.colors.fgSubtle,
            groupLabel: Color = ArecaTheme.colors.fgSubtle,
            yLabel: Color = ArecaTheme.colors.fgSubtle,
        ): BarChartStyle {
            val colors = ArecaTheme.colors
            val (bar, track) = when (color) {
                BarChartColor.Neutral -> colors.bgContrastMuted to colors.bgNeutralMuted
                BarChartColor.Brand -> colors.bgBrandBase to colors.bgBrandMuted
                BarChartColor.Success -> colors.bgSuccessBase to colors.bgSuccessMuted
                BarChartColor.Warning -> colors.bgWarnBase to colors.bgWarnMuted
                BarChartColor.Danger -> colors.bgDangerBase to colors.bgDangerMuted
                BarChartColor.Info -> colors.bgInfoBase to colors.bgInfoMuted
            }
            return BarChartStyle(
                bg = bg,
                plotBg = plotBg,
                axis = axis,
                guide = guide,
                bar = bar,
                track = track,
                xLabel = xLabel,
                groupLabel = groupLabel,
                yLabel = yLabel,
            )
        }
    }
}

/**
 * Default [BarChartStyle] in scope, or `null` to use [BarChartStyle.Default].
 *
 * Unlike the foundation's component defaults this is **not** seeded by
 * [dev.voir.areca.theme.ArecaThemeProvider]: that provider lives in `:foundation`, and `:foundation`
 * does not depend on `:charts`. Wire it yourself around a subtree that wants a different default:
 *
 * ```kotlin
 * CompositionLocalProvider(LocalBarChartStyle provides BarChartStyle.of(BarChartColor.Info)) { … }
 * ```
 */
public val LocalBarChartStyle: ProvidableCompositionLocal<BarChartStyle?> =
    compositionLocalOf { null }
