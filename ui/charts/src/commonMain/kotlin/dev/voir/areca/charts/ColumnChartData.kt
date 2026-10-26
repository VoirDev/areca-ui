package dev.voir.areca.charts

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * One part of a [ColumnChartBar]: the whole bar, or one layer of a stacked bar.
 *
 * @property value Amount the part stands for, in the units of the chart's scale. Zero, negative and
 *   non-finite values draw nothing and are left out of the scale.
 * @property color Fill of the part, or `null` for the style's palette colour — see
 *   [ColumnChartStyle.palette].
 * @property brush Fill of the part as a brush — a gradient — drawn in place of [color]. A brush is
 *   laid over the whole plot rather than the bar, so a vertical gradient shades every bar alike by
 *   height: a short bar shows only the gradient's lower end. `null` fills with [color].
 */
@Immutable
public data class ColumnChartSegment(
    public val value: Float,
    public val color: Color? = null,
    public val brush: Brush? = null,
)

/**
 * One bar of a [ColumnChartColumn].
 *
 * A bar of one segment is a plain bar; a bar of several stacks them from the baseline up, in order,
 * with [ColumnChartSize.stackGap] between them, and only the top one rounded.
 *
 * @property segments Parts of the bar, from the baseline up.
 * @property companion Whether the bar is a slim companion — the same period a year earlier beside
 *   this year's bar — drawn [ColumnChartSize.companionWidth] wide in [ColumnChartStyle.companion]
 *   unless a segment carries its own colour.
 */
@Immutable
public data class ColumnChartBar(
    public val segments: List<ColumnChartSegment>,
    public val companion: Boolean = false,
) {
    /** Builders of the common bars. */
    public companion object {
        /**
         * A plain bar of one value.
         *
         * @param value Amount the bar stands for.
         * @param color Fill, or `null` for the style's palette colour.
         * @return A bar of one segment.
         */
        public fun of(value: Float, color: Color? = null): ColumnChartBar =
            ColumnChartBar(segments = listOf(ColumnChartSegment(value, color)))

        /**
         * A plain bar of one value, filled with a brush.
         *
         * @param value Amount the bar stands for.
         * @param brush Fill — a gradient, usually vertical.
         * @return A bar of one segment.
         */
        public fun of(value: Float, brush: Brush): ColumnChartBar =
            ColumnChartBar(segments = listOf(ColumnChartSegment(value, brush = brush)))

        /**
         * A slim companion bar of one value, such as the same period a year earlier.
         *
         * @param value Amount the bar stands for.
         * @param color Fill, or `null` for [ColumnChartStyle.companion].
         * @return A companion bar of one segment.
         */
        public fun companion(value: Float, color: Color? = null): ColumnChartBar =
            ColumnChartBar(segments = listOf(ColumnChartSegment(value, color)), companion = true)
    }
}

/**
 * One column of a [ColumnChart]: a period, its bars, and its labels.
 *
 * @param K Type of the column's identity: a month, a week's first day, a weekday.
 * @property key Stable identity of the column, unique within the chart. What `onSelect` reports and
 *   `selectedKey` is matched against.
 * @property bars Bars drawn side by side, centred in the column. Empty draws an empty column.
 * @property label Text under the column: "Oct", "Now", "Mon".
 * @property sublabel Second line under [label]: the year under January, "so far" under the current
 *   period. The line is reserved for every column once any column has one, so labels stay level.
 * @property valueLabel Text above the column's tallest bar — "€2,340" — shown according to the
 *   chart's [ColumnChartValueLabels]. `null` for none.
 * @property enabled Whether the column can be selected. A disabled column still draws its bars.
 * @property placeholder Whether the column stands in for missing history: its bars are drawn as
 *   dashed outlines, it cannot be selected, and assistive technology skips it.
 * @property contentDescription What assistive technology reads for the column — "October 2026,
 *   income €4,250, expenses €2,340" — or `null` to read [label], [sublabel] and [valueLabel].
 */
@Immutable
public data class ColumnChartColumn<K>(
    public val key: K,
    public val bars: List<ColumnChartBar>,
    public val label: String,
    public val sublabel: String? = null,
    public val valueLabel: String? = null,
    public val enabled: Boolean = true,
    public val placeholder: Boolean = false,
    public val contentDescription: String? = null,
)

/**
 * A dashed line across a [ColumnChart] at one value, with an optional label at its end — the usual
 * amount, an average, a budget.
 *
 * The line and its label stay put while the columns scroll under them.
 *
 * @property value Where the line sits, in the units of the chart's scale. A line above the scale's
 *   top is not drawn.
 * @property label Text at the line's end edge, or `null` for none.
 * @property color Colour of the line and its label, or `null` for
 *   [ColumnChartStyle.referenceLine] and [ColumnChartStyle.referenceLabel].
 */
@Immutable
public data class ColumnChartReferenceLine(
    public val value: Float,
    public val label: String? = null,
    public val color: Color? = null,
)

/** Which columns of a [ColumnChart] show their [ColumnChartColumn.valueLabel]. */
public enum class ColumnChartValueLabels {
    /** None. */
    None,

    /** Only the selected column. */
    Selected,

    /** Every column that has one. */
    All,
}

/** How a [ColumnChart]'s columns take the width they are given. */
public enum class ColumnChartLayout {
    /**
     * Every column is [ColumnChartSize.columnWidth] wide and the chart scrolls sideways, opening on
     * its last column. For a long history read one period at a time.
     */
    Scroll,

    /** The columns share the width equally and nothing scrolls. For a handful of columns. */
    Fill,
}
