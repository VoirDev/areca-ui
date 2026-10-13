package dev.voir.areca.datetime.calendar.range

import androidx.compose.foundation.shape.CornerSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import dev.voir.areca.datetime.calendar.CalendarDayColors
import dev.voir.areca.datetime.calendar.CalendarStyle
import dev.voir.areca.theme.ArecaTheme

/**
 * What one cell of a range draws: the band behind it, and the circle on top.
 *
 * Two parts because a range cell is two things at once. The band is what joins a day to the ones
 * either side of it — it runs the full width of the cell so that adjacent cells meet — while the
 * circle is what marks an end, and only the ends have one.
 *
 * @property track Band behind the day, joining it to its neighbours. [Color.Transparent] draws none,
 *   which is what a single day and a day outside the range get.
 * @property cap The circle and its number, in the same three colors an ordinary day cell uses.
 */
@Immutable
public data class DateRangeDayColors(
    public val track: Color,
    public val cap: CalendarDayColors,
)

/**
 * Colors and shapes of the cells a range runs through.
 *
 * A layer over [CalendarStyle] rather than a replacement for it: days outside the range are drawn by
 * the calendar's own style, and only the days the range touches are resolved here. That is why
 * [resolveDay] returns `null` for [DateRangeDayPosition.None] instead of restating the nine states
 * [CalendarStyle.resolveDay] already covers — a range calendar is a calendar, and a day it does not
 * reach should not look different because a range exists somewhere else in the month.
 *
 * Shapes live here rather than in a size, following [dev.voir.areca.foundation.button.ButtonStyle] and
 * [dev.voir.areca.foundation.button.IconButtonStyle]: in this library a corner is a matter of style.
 *
 * @property track Band behind the days between the ends.
 * @property previewTrack Band behind a range that is only being previewed under the pointer. Quieter
 *   than [track], because it is not a choice yet.
 * @property cap Circle marking an end of the range, and the lone anchor of a half-picked one.
 * @property previewCap Circle marking where the pointer would put the far end.
 * @property disabledCap An end that has since become unavailable — a booking whose day was taken.
 *   Distinct from an ordinary disabled day for the reason [CalendarStyle.selectedDisabledDay] is:
 *   the user has to see that their choice is still there and no longer valid.
 * @property inRange A day between the ends.
 * @property inRangeToday Today, between the ends. Keeps its ring, so it is still findable inside a
 *   long range.
 * @property inRangeDisabled A day between the ends that the date rule refuses — reachable only under
 *   [BlockedDayPolicy.AllowGaps], where a range may step over one.
 * @property inRangeAdjacent A day between the ends that spills in from a neighbouring month.
 * @property capShape Shape of the circle at an end. Also the shape the press indication takes.
 * @property trackCorner Corner the band is capped with where it meets an end. Half the band's height
 *   — the default — makes that cap exactly the circle it sits under, so the two are continuous.
 */
@Immutable
public data class DateRangeStyle(
    public val track: Color,
    public val previewTrack: Color,
    public val cap: CalendarDayColors,
    public val previewCap: CalendarDayColors,
    public val disabledCap: CalendarDayColors,
    public val inRange: CalendarDayColors,
    public val inRangeToday: CalendarDayColors,
    public val inRangeDisabled: CalendarDayColors,
    public val inRangeAdjacent: CalendarDayColors,
    public val capShape: Shape,
    public val trackCorner: CornerSize,
) {
    /**
     * Picks the colors for one cell of a range.
     *
     * Precedence, and where it deliberately parts company with [CalendarStyle.resolveDay]:
     *
     * 1. **Adjacency does not win here.** A day spilling in from a neighbouring month keeps its
     *    band, and only its number is muted. Suppressing the band would break the range in half at
     *    every month boundary, which reads as two ranges rather than as one crossing a month.
     * 2. **Previewed** wins over everything else it touches. A previewed day is not a choice, and
     *    saying so is worth more than saying it is also today.
     * 3. **An end stays an end.** A cap is never restyled for being today, for the reason a chosen
     *    day is not: today is a fact about the calendar, an end is the user's answer.
     * 4. **Unavailable** modifies rather than replaces, so an end that has gone stale is still
     *    visibly an end.
     *
     * @param position Where the day sits in the range.
     * @param isToday Whether the day is the calendar's today.
     * @param enabled Whether the day accepts a tap.
     * @param adjacent Whether the day belongs to a neighbouring month.
     * @param isPreview Whether the day is in the range only because of where the pointer is.
     * @return Colors to draw the cell with, or `null` when the range does not reach this day and the
     *   calendar's own [CalendarStyle] should draw it.
     */
    public fun resolveDay(
        position: DateRangeDayPosition,
        isToday: Boolean,
        enabled: Boolean,
        adjacent: Boolean,
        isPreview: Boolean,
    ): DateRangeDayColors? {
        if (position == DateRangeDayPosition.None) return null

        val isEnd = position != DateRangeDayPosition.InRange
        return DateRangeDayColors(
            track = when {
                // Nothing on either side to join to.
                position == DateRangeDayPosition.Single -> Color.Transparent
                isPreview -> previewTrack
                else -> track
            },
            cap = when {
                isEnd && isPreview -> previewCap
                isEnd && !enabled -> disabledCap
                isEnd -> cap
                !enabled -> inRangeDisabled
                adjacent -> inRangeAdjacent
                isToday -> inRangeToday
                else -> inRange
            },
        )
    }

    /** Theme-derived styles and the builder they share. */
    public companion object {
        /** Theme default: a brand-tinted band between filled brand ends. */
        public val Default: DateRangeStyle
            @Composable
            get() = default()

        /**
         * Builds a style from the current theme.
         *
         * Parameters are flat colors rather than [DateRangeDayColors] pairs, so overriding one stays
         * a one-line change.
         *
         * @param track Band behind the days between the ends.
         * @param previewTrack Band behind a previewed range.
         * @param capBg Fill of the circle at an end.
         * @param capContent Number at an end.
         * @param previewCapBg Fill of the circle where the pointer would put the far end.
         * @param previewCapContent Number at a previewed end.
         * @param disabledCapBg Fill of an end that became unavailable.
         * @param disabledCapContent Number of an end that became unavailable.
         * @param inRangeContent Number of a day between the ends.
         * @param inRangeDisabledContent Number of a refused day a range stepped over.
         * @param inRangeAdjacentContent Number of a day between the ends from a neighbouring month.
         * @param capShape Shape of the circle at an end.
         * @param trackCorner Corner the band is capped with where it meets an end.
         * @return Style built from the given colors.
         */
        @Composable
        public fun default(
            track: Color = ArecaTheme.colors.bgBrandMuted,
            previewTrack: Color = ArecaTheme.colors.bgSubtle,
            capBg: Color = ArecaTheme.colors.bgBrandBase,
            capContent: Color = ArecaTheme.colors.fgOnFill,
            previewCapBg: Color = ArecaTheme.colors.bgBrandMuted,
            previewCapContent: Color = ArecaTheme.colors.fgBrandText,
            disabledCapBg: Color = ArecaTheme.colors.bgDisabled,
            disabledCapContent: Color = ArecaTheme.colors.fgDisabled,
            inRangeContent: Color = ArecaTheme.colors.fgBrandText,
            inRangeDisabledContent: Color = ArecaTheme.colors.fgDisabled,
            inRangeAdjacentContent: Color = ArecaTheme.colors.fgMuted,
            capShape: Shape = ArecaTheme.shapes.full,
            trackCorner: CornerSize = CornerSize(percent = 50),
        ): DateRangeStyle = DateRangeStyle(
            track = track,
            previewTrack = previewTrack,
            cap = CalendarDayColors(bg = capBg, content = capContent),
            previewCap = CalendarDayColors(bg = previewCapBg, content = previewCapContent),
            disabledCap = CalendarDayColors(bg = disabledCapBg, content = disabledCapContent),
            inRange = CalendarDayColors(bg = Color.Transparent, content = inRangeContent),
            inRangeToday = CalendarDayColors(
                bg = Color.Transparent,
                content = ArecaTheme.colors.accentBase,
                border = ArecaTheme.colors.accentBase,
            ),
            inRangeDisabled = CalendarDayColors(
                bg = Color.Transparent,
                content = inRangeDisabledContent,
            ),
            inRangeAdjacent = CalendarDayColors(
                bg = Color.Transparent,
                content = inRangeAdjacentContent,
            ),
            capShape = capShape,
            trackCorner = trackCorner,
        )
    }
}

/**
 * Default [DateRangeStyle] in scope, or `null` to use [DateRangeStyle.Default].
 *
 * Not seeded by [dev.voir.areca.theme.ArecaThemeProvider] — see
 * [dev.voir.areca.datetime.calendar.LocalCalendarSize] for why.
 */
public val LocalDateRangeStyle: ProvidableCompositionLocal<DateRangeStyle?> =
    compositionLocalOf { null }
