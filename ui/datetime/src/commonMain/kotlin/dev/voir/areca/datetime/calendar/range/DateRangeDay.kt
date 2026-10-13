package dev.voir.areca.datetime.calendar.range

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import dev.voir.areca.datetime.calendar.CalendarDayState
import dev.voir.areca.datetime.calendar.CalendarScope
import dev.voir.areca.datetime.calendar.CalendarSize
import dev.voir.areca.datetime.calendar.CalendarStyle
import dev.voir.areca.datetime.calendar.LocalCalendarSize
import dev.voir.areca.datetime.calendar.LocalCalendarStyle
import dev.voir.areca.datetime.calendar.animateDayColorAsState
import dev.voir.areca.datetime.calendar.dayCell
import dev.voir.areca.datetime.calendar.dayIndication
import dev.voir.areca.datetime.calendar.dayInteraction
import dev.voir.areca.foundation.LocalContentColor
import dev.voir.areca.foundation.LocalTextStyle
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.tween
import kotlinx.datetime.LocalDate

/**
 * Everything a cell of a range calendar needs to draw itself.
 *
 * The calendar's own [CalendarDayState] plus the two facts only a range has: where the day sits
 * between the ends, and whether it is there at all or merely under the pointer.
 *
 * @property day What any calendar cell knows about the day. The date, its page, and whether it is
 *   today, adjacent, selected or available.
 * @property position Where the day sits in the range, which decides its band and its caps.
 * @property isPreview Whether the day is in the range only because of where the pointer is. False
 *   for the anchor of a half-picked range, which is a choice already made and keeps its solid
 *   circle while the tentative days beside it stay quiet.
 */
@Immutable
public data class DateRangeDayState(
    public val day: CalendarDayState,
    public val position: DateRangeDayPosition,
    public val isPreview: Boolean,
) {
    /** The day this cell draws. */
    public val date: LocalDate get() = day.date

    /** Whether the day is the calendar's today. */
    public val isToday: Boolean get() = day.isToday

    /** Whether the day accepts a tap. */
    public val isEnabled: Boolean get() = day.isEnabled

    /** Whether the day spills in from the month before or after the page it is drawn on. */
    public val isAdjacent: Boolean get() = day.isAdjacent
}

/**
 * One cell of a range: a band joining it to its neighbours, and a circle when it is an end.
 *
 * Not [dev.voir.areca.datetime.calendar.CalendarDay] with extra paint, and deliberately so. A range
 * cell is two layers with independent geometry — a band that must reach the full width of the cell
 * so that neighbouring cells meet, under a circle that must stay inset so it stays a circle — and
 * its colors are resolved from a position rather than from a boolean. Folding both into one
 * composable would mean a day cell that carries range machinery for every calendar that has no
 * range in it.
 *
 * The band's cap is the reason the two layers line up. On the day a range starts, the band begins at
 * the circle's leading edge rather than at the cell's, and is rounded there by
 * [DateRangeStyle.trackCorner] — half its height by default, which is exactly the circle above it.
 * Squaring it off at the cell edge instead would show two corners either side of the circle.
 *
 * Every color animates, the band included, so a range grows and clears rather than appearing.
 *
 * The whole cell takes the tap, padding included, while the press and the focus ring are drawn on
 * the circle. To assistive technology and the keyboard it behaves as
 * [dev.voir.areca.datetime.calendar.CalendarDay] does: one selectable node with its row and column,
 * taking part in the grid's roving focus.
 *
 * @param day Everything about the day this cell draws.
 * @param modifier Modifier applied to the cell.
 * @param onClick Called when the day is tapped. Ignored when the day is unavailable, and `null` for
 *   a read-only calendar.
 * @param rangeLabels What assistive technology reads for a day's place in the range.
 * @param style Colors of the days the range does not reach. Defaults to the calendar's.
 * @param rangeStyle Colors and shapes of the days it does. Defaults to [LocalDateRangeStyle], or
 *   [DateRangeStyle.Default].
 * @param size Dimensions and type. Defaults to the calendar's.
 * @param interactionSource Source the press and focus indication is driven from, or `null` for one
 *   the cell creates. Shared by the cell that takes the tap and the circle that draws it.
 */
@Composable
public fun CalendarScope.DateRangeDay(
    day: DateRangeDayState,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    rangeLabels: DateRangeLabels = DateRangeLabels.English,
    style: CalendarStyle = LocalCalendarStyle.current ?: CalendarStyle.Default,
    rangeStyle: DateRangeStyle = LocalDateRangeStyle.current ?: DateRangeStyle.Default,
    size: CalendarSize = LocalCalendarSize.current ?: CalendarSize.Default,
    interactionSource: MutableInteractionSource? = null,
) {
    val source = interactionSource ?: remember { MutableInteractionSource() }
    val colors = rangeStyle.resolveDay(
        position = day.position,
        isToday = day.isToday,
        enabled = day.isEnabled,
        adjacent = day.isAdjacent,
        isPreview = day.isPreview,
    ) ?: DateRangeDayColors(
        // Outside the range, so an ordinary day of an ordinary calendar.
        track = Color.Transparent,
        cap = style.resolveDay(
            selected = false,
            isToday = day.isToday,
            enabled = day.isEnabled,
            adjacent = day.isAdjacent,
        ),
    )

    val motion = ArecaTheme.motion
    val trackColor by animateDayColorAsState(
        targetValue = colors.track,
        animationSpec = motion.fast.tween(motion.standard),
        label = "dateRangeDayTrack",
    )
    val backgroundColor by animateDayColorAsState(
        targetValue = colors.cap.bg,
        animationSpec = motion.fast.tween(motion.standard),
        label = "dateRangeDayBackground",
    )
    val contentColor by animateDayColorAsState(
        targetValue = colors.cap.content,
        animationSpec = motion.fast.tween(motion.standard),
        label = "dateRangeDayContent",
    )
    val borderColor by animateDayColorAsState(
        targetValue = colors.cap.border,
        animationSpec = motion.fast.tween(motion.standard),
        label = "dateRangeDayBorder",
    )

    val layoutDirection = LocalLayoutDirection.current
    val trackPadding = remember(day.position, size.dayPadding, layoutDirection) {
        size.dayPadding.trackPadding(day.position, layoutDirection)
    }
    val trackShape = remember(day.position, rangeStyle.trackCorner) {
        trackShapeFor(day.position, rangeStyle.trackCorner)
    }

    val focusable = onClick != null && !day.isAdjacent
    val description = remember(day.date, day.position, labels, rangeLabels) {
        val place = rangeLabels.position(day.position)
        val described = labels.dayDescription(day.date)
        if (place == null) described else "$described, $place"
    }

    CompositionLocalProvider(
        LocalTextStyle provides size.dayTextStyle,
        LocalContentColor provides contentColor,
    ) {
        // Tapped on the whole cell so the gaps between circles are not dead space; the press is
        // drawn on the circle from the same interaction source.
        Box(
            modifier = modifier
                .fillMaxSize()
                .then(dayCell(day = day.day, focusable = focusable))
                .semantics { contentDescription = description }
                .dayInteraction(
                    day = day.day,
                    onClick = onClick,
                    focusable = focusable,
                    interactionSource = source,
                ),
        ) {
            Box(
                modifier = Modifier
                    .padding(trackPadding)
                    .fillMaxSize()
                    .background(color = trackColor, shape = trackShape),
            )

            Box(
                modifier = Modifier
                    .padding(size.dayPadding)
                    .fillMaxSize()
                    .background(color = backgroundColor, shape = rangeStyle.capShape)
                    .border(
                        width = size.dayBorderWidth,
                        color = borderColor,
                        shape = rangeStyle.capShape,
                    )
                    .dayIndication(
                        visible = onClick != null && (day.isEnabled || focusable),
                        interactionSource = source,
                        shape = rangeStyle.capShape,
                    )
                    // The cell already reads the whole date; the drawn number is not a second node.
                    .clearAndSetSemantics { },
                contentAlignment = Alignment.Center,
            ) {
                Text(text = labels.dayNumber(day.date.day))
            }
        }
    }
}

/**
 * Where the band sits inside the cell.
 *
 * It reaches the cell's edge wherever the range continues, so that the bands of neighbouring cells
 * meet with nothing between them, and stops at the circle's edge wherever the range ends.
 *
 * @param position Where the day sits in the range.
 * @param layoutDirection Direction the week runs in, so the leading edge of a range is the leading
 *   edge on screen.
 * @return Inset for the band, derived from the cell's own day padding.
 */
private fun PaddingValues.trackPadding(
    position: DateRangeDayPosition,
    layoutDirection: LayoutDirection,
): PaddingValues = PaddingValues(
    start = when (position) {
        DateRangeDayPosition.Start -> calculateStartPadding(layoutDirection)
        else -> 0.dp
    },
    top = calculateTopPadding(),
    end = when (position) {
        DateRangeDayPosition.End -> calculateEndPadding(layoutDirection)
        else -> 0.dp
    },
    bottom = calculateBottomPadding(),
)

/**
 * Which ends of the band are rounded.
 *
 * @param position Where the day sits in the range.
 * @param corner Corner to cap a closed end with.
 * @return Shape for the band. A rectangle wherever the range continues on both sides.
 */
private fun trackShapeFor(
    position: DateRangeDayPosition,
    corner: CornerSize,
): Shape = when (position) {
    DateRangeDayPosition.Start -> RoundedCornerShape(
        topStart = corner,
        topEnd = ZeroCornerSize,
        bottomEnd = ZeroCornerSize,
        bottomStart = corner,
    )

    DateRangeDayPosition.End -> RoundedCornerShape(
        topStart = ZeroCornerSize,
        topEnd = corner,
        bottomEnd = corner,
        bottomStart = ZeroCornerSize,
    )

    else -> RectangleShape
}
