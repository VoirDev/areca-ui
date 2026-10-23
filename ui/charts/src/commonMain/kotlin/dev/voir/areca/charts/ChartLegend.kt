package dev.voir.areca.charts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.theme.ArecaTheme

/** The mark beside a [ChartLegendItem]'s label, matching how its series is drawn. */
@Immutable
public sealed interface ChartSwatch {
    /** Colour of the mark. */
    public val color: Color

    /**
     * A rounded square, for a bar or a stacked segment.
     *
     * @property color Colour of the mark.
     */
    public data class Square(override val color: Color) : ChartSwatch

    /**
     * A dot, for a figure's colour key.
     *
     * @property color Colour of the mark.
     */
    public data class Dot(override val color: Color) : ChartSwatch

    /**
     * A short solid line, for a line series.
     *
     * @property color Colour of the mark.
     */
    public data class Line(override val color: Color) : ChartSwatch

    /**
     * A short dashed line, for a comparison series or a reference line.
     *
     * @property color Colour of the mark.
     */
    public data class DashedLine(override val color: Color) : ChartSwatch

    /**
     * An upright tick, for a [MeterBar]'s reference marker.
     *
     * @property color Colour of the mark.
     */
    public data class Tick(override val color: Color) : ChartSwatch
}

/**
 * One entry of a [ChartLegend].
 *
 * @property label What the series is: "Income", "October", "usual".
 * @property swatch The mark drawn before the label.
 */
@Immutable
public data class ChartLegendItem(
    public val label: String,
    public val swatch: ChartSwatch,
)

/**
 * The key to a chart's colours: a mark and a label per series, wrapping onto as many lines as it
 * needs.
 *
 * Assistive technology reads the legend as one sentence of its labels, separated by commas. Charts
 * describe their own data, so a legend beside one only repeats what each colour stands for.
 *
 * Requires `ArecaThemeProvider` above it.
 *
 * @param items Entries in the order shown.
 * @param modifier Modifier applied to the legend.
 * @param style Label colour. Defaults to [LocalChartLegendStyle], or [ChartLegendStyle.Default].
 * @param size Mark metrics, gaps and type. Defaults to [LocalChartLegendSize], or
 *   [ChartLegendSize.Default].
 */
@Composable
public fun ChartLegend(
    items: List<ChartLegendItem>,
    modifier: Modifier = Modifier,
    style: ChartLegendStyle = LocalChartLegendStyle.current ?: ChartLegendStyle.Default,
    size: ChartLegendSize = LocalChartLegendSize.current ?: ChartLegendSize.Default,
) {
    val description = items.joinToString(", ") { it.label }
    FlowRow(
        modifier = modifier.clearAndSetSemantics { contentDescription = description },
        horizontalArrangement = Arrangement.spacedBy(size.itemGap),
        verticalArrangement = Arrangement.spacedBy(size.rowGap),
    ) {
        items.forEach { item ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(size.swatchGap),
            ) {
                ChartSwatchMark(swatch = item.swatch, size = size)
                Text(text = item.label, style = size.textStyle, color = style.label, maxLines = 1)
            }
        }
    }
}

/**
 * Draws one [ChartSwatch].
 *
 * @param swatch The mark.
 * @param size Mark metrics.
 */
@Composable
private fun ChartSwatchMark(swatch: ChartSwatch, size: ChartLegendSize) {
    val box = when (swatch) {
        is ChartSwatch.Square -> DpSize(size.square, size.square)
        is ChartSwatch.Dot -> DpSize(size.dot, size.dot)
        is ChartSwatch.Line, is ChartSwatch.DashedLine -> DpSize(size.lineLength, size.square)
        is ChartSwatch.Tick -> DpSize(size.lineStroke, size.tickHeight)
    }
    Canvas(modifier = Modifier.size(box)) {
        val w = this.size.width
        val h = this.size.height
        val stroke = size.lineStroke.toPx()
        when (swatch) {
            is ChartSwatch.Square -> drawRoundRect(
                color = swatch.color,
                cornerRadius = CornerRadius(size.squareRadius.toPx(), size.squareRadius.toPx()),
            )

            is ChartSwatch.Dot -> drawCircle(color = swatch.color)

            is ChartSwatch.Line -> drawLine(
                color = swatch.color,
                start = Offset(stroke / 2f, h / 2f),
                end = Offset(w - stroke / 2f, h / 2f),
                strokeWidth = stroke,
                cap = StrokeCap.Round,
            )

            is ChartSwatch.DashedLine -> {
                val dash = size.dash.toPx()
                drawLine(
                    color = swatch.color,
                    start = Offset(0f, h / 2f),
                    end = Offset(w, h / 2f),
                    strokeWidth = stroke,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(dash, dash)),
                )
            }

            is ChartSwatch.Tick -> drawRoundRect(
                color = swatch.color,
                size = Size(w, h),
                cornerRadius = CornerRadius(w / 2f, w / 2f),
            )
        }
    }
}

/**
 * Colours of a [ChartLegend].
 *
 * @property label Ink of the labels.
 */
@Immutable
public data class ChartLegendStyle(public val label: Color) {
    /** Theme-backed [ChartLegendStyle] presets and their builder. */
    public companion object {
        /** Theme default: labels in the secondary ink. */
        public val Default: ChartLegendStyle
            @Composable
            get() = default()

        /**
         * Builds a style from the current theme.
         *
         * @param label Ink of the labels.
         * @return Style built from the supplied colour.
         */
        @Composable
        public fun default(label: Color = ArecaTheme.colors.fgSubtle): ChartLegendStyle =
            ChartLegendStyle(label = label)
    }
}

/**
 * Mark metrics, gaps and type of a [ChartLegend].
 *
 * @property square Side of a [ChartSwatch.Square].
 * @property squareRadius Corner radius of a [ChartSwatch.Square].
 * @property dot Diameter of a [ChartSwatch.Dot].
 * @property lineLength Length of a [ChartSwatch.Line] or [ChartSwatch.DashedLine].
 * @property lineStroke Thickness of a line, a dashed line and a tick.
 * @property dash Length of each dash of a [ChartSwatch.DashedLine], and of the gap after it.
 * @property tickHeight Height of a [ChartSwatch.Tick].
 * @property swatchGap Space between a mark and its label.
 * @property itemGap Space between entries on one line.
 * @property rowGap Space between wrapped lines.
 * @property textStyle Type of the labels.
 */
@Immutable
public data class ChartLegendSize(
    public val square: Dp,
    public val squareRadius: Dp,
    public val dot: Dp,
    public val lineLength: Dp,
    public val lineStroke: Dp,
    public val dash: Dp,
    public val tickHeight: Dp,
    public val swatchGap: Dp,
    public val itemGap: Dp,
    public val rowGap: Dp,
    public val textStyle: TextStyle,
) {
    /** The size steps of a [ChartLegend]. */
    public companion object {
        /** The legend under a chart or beside a figure. */
        public val Default: ChartLegendSize
            @Composable
            get() = default()

        /**
         * Builds a size from the current theme.
         *
         * @param square Side of a square mark.
         * @param squareRadius Corner radius of a square mark.
         * @param dot Diameter of a dot mark.
         * @param lineLength Length of a line or dashed-line mark.
         * @param lineStroke Thickness of a line, a dashed line and a tick.
         * @param dash Dash length of a dashed-line mark.
         * @param tickHeight Height of a tick mark.
         * @param swatchGap Space between a mark and its label.
         * @param itemGap Space between entries on one line.
         * @param rowGap Space between wrapped lines.
         * @param textStyle Type of the labels.
         * @return Size built from the supplied metrics.
         */
        @Composable
        public fun default(
            square: Dp = 10.dp,
            squareRadius: Dp = 3.dp,
            dot: Dp = 8.dp,
            lineLength: Dp = 14.dp,
            lineStroke: Dp = 2.dp,
            dash: Dp = 3.dp,
            tickHeight: Dp = 12.dp,
            swatchGap: Dp = ArecaTheme.spacing.sm,
            itemGap: Dp = ArecaTheme.spacing.xl,
            rowGap: Dp = ArecaTheme.spacing.xs,
            textStyle: TextStyle = ArecaTheme.typography.bodyExtraSmall,
        ): ChartLegendSize = ChartLegendSize(
            square = square,
            squareRadius = squareRadius,
            dot = dot,
            lineLength = lineLength,
            lineStroke = lineStroke,
            dash = dash,
            tickHeight = tickHeight,
            swatchGap = swatchGap,
            itemGap = itemGap,
            rowGap = rowGap,
            textStyle = textStyle,
        )
    }
}

/**
 * Default [ChartLegendStyle] in scope, or `null` to use [ChartLegendStyle.Default].
 *
 * Not seeded by [dev.voir.areca.theme.ArecaThemeProvider] — see [LocalBarChartStyle] for why.
 */
public val LocalChartLegendStyle: ProvidableCompositionLocal<ChartLegendStyle?> =
    compositionLocalOf { null }

/**
 * Default [ChartLegendSize] in scope, or `null` to use [ChartLegendSize.Default].
 *
 * Not seeded by [dev.voir.areca.theme.ArecaThemeProvider] — see [LocalBarChartStyle] for why.
 */
public val LocalChartLegendSize: ProvidableCompositionLocal<ChartLegendSize?> =
    compositionLocalOf { null }
