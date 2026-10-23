package dev.voir.areca.charts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import dev.voir.areca.theme.ArecaTheme

/**
 * Colours of a [ColumnChart].
 *
 * @property palette Fills of segments without a colour of their own. A stacked bar takes
 *   `palette[segmentIndex % size]`; a column's plain bars take `palette[barIndex % size]`, counting
 *   regular bars only. Never empty.
 * @property companion Fill of a companion bar's segments without a colour of their own.
 * @property halo Rounded ground behind the selected column, running behind its plot and labels.
 * @property baseline Line the bars stand on, across the whole chart.
 * @property referenceLine Dashed reference line, unless the line carries its own colour.
 * @property referenceLabel Reference line label, unless the line carries its own colour.
 * @property referenceLabelBg Ground behind a reference label, so the line does not run through it.
 *   Match it to what the chart sits on.
 * @property label Column label of an unselected column.
 * @property labelSelected Column label of the selected column.
 * @property sublabel Second line under a column label.
 * @property valueLabel Text above a column's tallest bar.
 * @property placeholder Dashed outline of a placeholder column's bars.
 * @property dimmedAlpha Opacity of the bars of every column but the selected one, while a column is
 *   selected, so the selection reads at a glance. `1f` dims nothing.
 * @throws IllegalArgumentException When [palette] is empty or [dimmedAlpha] is outside `0f..1f`.
 */
@Immutable
public data class ColumnChartStyle(
    public val palette: List<Color>,
    public val companion: Color,
    public val halo: Color,
    public val baseline: Color,
    public val referenceLine: Color,
    public val referenceLabel: Color,
    public val referenceLabelBg: Color,
    public val label: Color,
    public val labelSelected: Color,
    public val sublabel: Color,
    public val valueLabel: Color,
    public val placeholder: Color,
    public val dimmedAlpha: Float,
) {
    init {
        require(palette.isNotEmpty()) { "A column chart palette needs at least one colour." }
        require(dimmedAlpha in 0f..1f) { "dimmedAlpha must be within 0..1, was $dimmedAlpha." }
    }

    /** Theme-backed [ColumnChartStyle] presets and their builder. */
    public companion object {
        /** Theme default: brand bars, a neutral halo and baseline, on the canvas. */
        public val Default: ColumnChartStyle
            @Composable
            get() = default()

        /**
         * Builds a style from the current theme.
         *
         * @param palette Fills of segments without a colour of their own.
         * @param companion Fill of companion bars.
         * @param halo Ground behind the selected column.
         * @param baseline Line the bars stand on.
         * @param referenceLine Dashed reference lines.
         * @param referenceLabel Reference line labels.
         * @param referenceLabelBg Ground behind a reference label.
         * @param label Unselected column labels.
         * @param labelSelected The selected column's label.
         * @param sublabel Second lines under labels.
         * @param valueLabel Text above a column's tallest bar.
         * @param placeholder Outline of placeholder bars.
         * @param dimmedAlpha Opacity of unselected columns' bars while one is selected.
         * @return Style built from the supplied colours.
         */
        @Composable
        public fun default(
            palette: List<Color> = listOf(
                ArecaTheme.colors.bgBrandBase,
                ArecaTheme.colors.bgInfoBase,
                ArecaTheme.colors.bgWarnIntense,
            ),
            companion: Color = ArecaTheme.colors.bgMuted,
            halo: Color = ArecaTheme.colors.bgNeutralMuted,
            baseline: Color = ArecaTheme.colors.bgNeutralMuted,
            referenceLine: Color = ArecaTheme.colors.fgMuted,
            referenceLabel: Color = ArecaTheme.colors.fgSubtle,
            referenceLabelBg: Color = ArecaTheme.colors.bgCanvas,
            label: Color = ArecaTheme.colors.fgSubtle,
            labelSelected: Color = ArecaTheme.colors.fgBase,
            sublabel: Color = ArecaTheme.colors.fgSubtle,
            valueLabel: Color = ArecaTheme.colors.fgBase,
            placeholder: Color = ArecaTheme.colors.bgNeutralIntense,
            dimmedAlpha: Float = 0.45f,
        ): ColumnChartStyle = ColumnChartStyle(
            palette = palette,
            companion = companion,
            halo = halo,
            baseline = baseline,
            referenceLine = referenceLine,
            referenceLabel = referenceLabel,
            referenceLabelBg = referenceLabelBg,
            label = label,
            labelSelected = labelSelected,
            sublabel = sublabel,
            valueLabel = valueLabel,
            placeholder = placeholder,
            dimmedAlpha = dimmedAlpha,
        )
    }
}

/**
 * Default [ColumnChartStyle] in scope, or `null` to use [ColumnChartStyle.Default].
 *
 * Not seeded by [dev.voir.areca.theme.ArecaThemeProvider] — see [LocalBarChartStyle] for why.
 */
public val LocalColumnChartStyle: ProvidableCompositionLocal<ColumnChartStyle?> =
    compositionLocalOf { null }
