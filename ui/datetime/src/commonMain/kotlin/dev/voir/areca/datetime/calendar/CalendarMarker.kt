package dev.voir.areca.datetime.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.Dp
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.foundation.text.Text

/**
 * What a day or a month tile shows under its number or name, in a row the calendar reserves for it.
 *
 * Markers are data: pass a function from a date to a marker as `markers` on [Calendar] or
 * [dev.voir.areca.datetime.calendar.range.DateRangeCalendar], and every cell draws its marker in the
 * same place. Once markers are given, every day keeps a row of [CalendarSize.markerLaneHeight] under
 * its number whether or not it has one, so the grid never shifts as data arrives. A marker never
 * touches the number or the selection fill: under [CalendarDayShape.Circle] it sits below the
 * circle in its own colours; under [CalendarDayShape.Tile] it sits inside the tile and turns to the
 * selected day's ink when that tile is filled. Days of a neighbouring month carry none, and an
 * unavailable day's marker is dimmed.
 *
 * Each marker can carry a [contentDescription], which assistive technology reads after the day.
 */
@Immutable
public sealed interface CalendarMarker {
    /** Read after the day's own description, such as `3 events`. `null` adds nothing. */
    public val contentDescription: String?

    /**
     * A dot per colour: one per kind of thing on the day, such as event categories.
     *
     * Past [max] colours the last dot gives way to a `+`, so a busy day says so without widening.
     *
     * @property colors One dot each, in order.
     * @property max Most dots drawn. At least 1.
     */
    public data class Dots(
        public val colors: List<Color>,
        public val max: Int = 3,
        override val contentDescription: String? = null,
    ) : CalendarMarker {
        init {
            require(max >= 1) { "max must be at least 1, was $max" }
        }
    }

    /**
     * A number in a small pill: how many things are on the day. Its digits come from
     * [CalendarLabels.dayNumber].
     *
     * @property count The number. Zero or less draws nothing.
     */
    public data class Count(
        public val count: Int,
        override val contentDescription: String? = null,
    ) : CalendarMarker

    /**
     * A small glyph: the one notable thing on the day, such as a flight or a birthday.
     *
     * @property icon Glyph to draw, sized by [CalendarSize.markerIconSize].
     * @property tint Colour of the glyph, or `null` for [CalendarStyle.markerContent].
     */
    public data class Icon(
        public val icon: ImageVector,
        public val tint: Color? = null,
        override val contentDescription: String? = null,
    ) : CalendarMarker

    /**
     * A short line of text, such as a price. Keep it to a few characters: it is clipped to the
     * cell's width.
     *
     * @property text Text to draw.
     * @property emphasis Whether to draw it in [CalendarStyle.markerEmphasis] — the cheapest fare, a
     *   free slot.
     */
    public data class Label(
        public val text: String,
        public val emphasis: Boolean = false,
        override val contentDescription: String? = null,
    ) : CalendarMarker

    /**
     * A short bar filled to how full the day is.
     *
     * @property fraction Filled share, clamped into `0..1`.
     * @property color Colour of the fill, or `null` for [CalendarStyle.markerFill].
     */
    public data class Load(
        public val fraction: Float,
        public val color: Color? = null,
        override val contentDescription: String? = null,
    ) : CalendarMarker

    /**
     * Anything else, drawn inside the reserved row. Read [CalendarMarkerScope.contentColor] for its
     * ink, so it follows the selected day's fill like the built-in markers do; it is clipped to the
     * row and never sizes the cell.
     *
     * @property content The marker.
     */
    public class Custom(
        override val contentDescription: String? = null,
        public val content: @Composable CalendarMarkerScope.() -> Unit,
    ) : CalendarMarker
}

/**
 * What a [CalendarMarker.Custom] can see.
 *
 * @property isOnFill Whether the marker sits on a filled selection, where it must use
 *   [contentColor] rather than colours of its own.
 * @property contentColor Ink to draw in: the selected day's ink on a fill, otherwise
 *   [CalendarStyle.markerContent].
 * @property laneHeight Height of the row the marker is drawn in.
 */
@Stable
public class CalendarMarkerScope internal constructor(
    public val isOnFill: Boolean,
    public val contentColor: Color,
    public val laneHeight: Dp,
)

/**
 * Draws one marker inside its reserved row.
 *
 * @param marker Marker to draw.
 * @param onFill Whether the row sits on a filled selection.
 * @param fillContent Ink of that fill.
 * @param enabled Whether the day or month accepts a tap; a refused one is dimmed.
 * @param labels Digits for [CalendarMarker.Count].
 * @param style Marker colours.
 * @param size Marker dimensions.
 * @param modifier Modifier giving the row its size.
 */
@Composable
internal fun MarkerLane(
    marker: CalendarMarker?,
    onFill: Boolean,
    fillContent: Color,
    enabled: Boolean,
    labels: CalendarLabels,
    style: CalendarStyle,
    size: CalendarSize,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(size.markerLaneHeight)
            .clip(RectangleShape)
            .alpha(if (enabled) 1f else DISABLED_MARKER_ALPHA)
            // The cell reads the marker's description; what is drawn here is not a second node.
            .clearAndSetSemantics { },
        contentAlignment = Alignment.Center,
    ) {
        if (marker == null) return@Box
        val ink = if (onFill) fillContent else style.markerContent
        when (marker) {
            is CalendarMarker.Dots -> Row(
                horizontalArrangement = Arrangement.spacedBy(size.markerSpacing),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                val overflow = marker.colors.size > marker.max
                val shown = if (overflow) marker.colors.take(marker.max - 1) else marker.colors
                shown.forEach { color ->
                    Box(
                        modifier = Modifier
                            .size(size.markerDotSize)
                            .background(if (onFill) fillContent else color, CircleShape),
                    )
                }
                if (overflow) Text(text = "+", style = size.markerTextStyle, color = ink, maxLines = 1)
            }

            is CalendarMarker.Count -> if (marker.count > 0) {
                Text(
                    text = labels.dayNumber(marker.count),
                    style = size.markerTextStyle,
                    color = if (onFill) fillContent else style.markerCountContent,
                    maxLines = 1,
                    modifier = Modifier
                        .background(
                            color = if (onFill) fillContent.copy(alpha = ON_FILL_GROUND_ALPHA) else style.markerCountBg,
                            shape = CircleShape,
                        )
                        .padding(horizontal = size.markerSpacing * 2),
                )
            }

            is CalendarMarker.Icon -> Icon(
                imageVector = marker.icon,
                contentDescription = null,
                tint = if (onFill) fillContent else marker.tint ?: style.markerContent,
                size = size.markerIconSize,
            )

            is CalendarMarker.Label -> Text(
                text = marker.text,
                style = size.markerTextStyle,
                color = when {
                    onFill -> fillContent
                    marker.emphasis -> style.markerEmphasis
                    else -> style.markerContent
                },
                maxLines = 1,
            )

            is CalendarMarker.Load -> BoxWithConstraints(
                modifier = Modifier.fillMaxWidth(LOAD_TRACK_WIDTH).height(size.markerBarHeight),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .background(
                            color = if (onFill) fillContent.copy(alpha = ON_FILL_GROUND_ALPHA) else style.markerTrack,
                            shape = CircleShape,
                        ),
                )
                Box(
                    modifier = Modifier
                        .width(maxWidth * marker.fraction.coerceIn(0f, 1f))
                        .fillMaxHeight()
                        .background(
                            color = if (onFill) fillContent else marker.color ?: style.markerFill,
                            shape = CircleShape,
                        ),
                )
            }

            is CalendarMarker.Custom -> CalendarMarkerScope(
                isOnFill = onFill,
                contentColor = ink,
                laneHeight = size.markerLaneHeight,
            ).(marker.content)()
        }
    }
}

/** Opacity of an unavailable day's or month's marker. */
private const val DISABLED_MARKER_ALPHA: Float = 0.5f

/** Opacity of a pill's or a track's ground drawn on a filled selection, in the fill's ink. */
private const val ON_FILL_GROUND_ALPHA: Float = 0.25f

/** Share of the lane's width a [CalendarMarker.Load] bar takes. */
private const val LOAD_TRACK_WIDTH: Float = 0.62f
