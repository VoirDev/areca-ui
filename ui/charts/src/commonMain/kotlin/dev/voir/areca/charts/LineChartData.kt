package dev.voir.areca.charts

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/** How a [LineChartSeries] line is stroked. */
public enum class LineChartStroke {
    /** A continuous line: the series the chart is about. */
    Solid,

    /** A dashed line: what it is compared with. */
    Dashed,
}

/** The dot on a [LineChartSeries]'s last point. */
public enum class LineChartMarker {
    /** A filled dot ringed in the chart's ground: where the series stands now. */
    Filled,

    /** A hollow dot: a reference point on a comparison series. */
    Hollow,
}

/**
 * One line of a [LineChart].
 *
 * @property points One value per step along the x axis, from the start edge. `null` is "no value
 *   yet" — the days still to come — and breaks the line; a series may end before the others.
 *   Non-finite values count as `null`.
 * @property label What the series is, read aloud: "October", "September".
 * @property color Colour of the line, its area and its marker, or `null` for the style's palette
 *   colour at the series' index.
 * @property stroke Solid or dashed.
 * @property area Whether the space under the line is shaded, fading to nothing at the baseline.
 * @property endMarker The dot on the last point with a value, or `null` for none.
 */
@Immutable
public data class LineChartSeries(
    public val points: List<Float?>,
    public val label: String,
    public val color: Color? = null,
    public val stroke: LineChartStroke = LineChartStroke.Solid,
    public val area: Boolean = false,
    public val endMarker: LineChartMarker? = LineChartMarker.Filled,
)

/**
 * A label under a [LineChart] at one step: "1 Oct", "15", "31".
 *
 * @property index Step it marks, counted from `0`.
 * @property label Text of the label.
 */
@Immutable
public data class LineChartXLabel(
    public val index: Int,
    public val label: String,
)

/**
 * A horizontal grid line across a [LineChart] with its label at the end edge: "€1k".
 *
 * @property value Where it sits, in the units of the chart's scale. Hidden above the scale's top.
 * @property label Text of the label.
 */
@Immutable
public data class LineChartYLabel(
    public val value: Float,
    public val label: String,
)

/**
 * The bubble a [LineChart] shows over one point: "€2,340 by the 23rd".
 *
 * @property series Index of the series the point belongs to.
 * @property index Step of the point. A point without a value shows no bubble.
 * @property text Text of the bubble.
 */
@Immutable
public data class LineChartTooltip(
    public val series: Int,
    public val index: Int,
    public val text: String,
)
