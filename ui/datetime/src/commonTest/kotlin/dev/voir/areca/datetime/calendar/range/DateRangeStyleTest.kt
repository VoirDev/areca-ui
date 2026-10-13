package dev.voir.areca.datetime.calendar.range

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.ui.graphics.Color
import dev.voir.areca.datetime.calendar.CalendarDayColors
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/** Which colors a cell of a range is drawn in. */
class DateRangeStyleTest {

    private fun colors(value: Long) = CalendarDayColors(bg = Color(value), content = Color(value))

    private val style = DateRangeStyle(
        track = Color(0xFF000001),
        previewTrack = Color(0xFF000002),
        cap = colors(0xFF000003),
        previewCap = colors(0xFF000004),
        disabledCap = colors(0xFF000005),
        inRange = colors(0xFF000006),
        inRangeToday = colors(0xFF000007),
        inRangeDisabled = colors(0xFF000008),
        inRangeAdjacent = colors(0xFF000009),
        capShape = CircleShape,
        trackCorner = CornerSize(percent = 50),
    )

    private fun resolve(
        position: DateRangeDayPosition,
        isToday: Boolean = false,
        enabled: Boolean = true,
        adjacent: Boolean = false,
        isPreview: Boolean = false,
    ) = style.resolveDay(position, isToday, enabled, adjacent, isPreview)

    @Test
    fun `a day the range does not reach is left to the calendar`() {
        assertNull(resolve(DateRangeDayPosition.None))
    }

    @Test
    fun `the ends are capped and joined to what lies between them`() {
        val start = resolve(DateRangeDayPosition.Start)
        val end = resolve(DateRangeDayPosition.End)

        assertEquals(style.cap, start?.cap)
        assertEquals(style.track, start?.track)
        assertEquals(style.cap, end?.cap)
        assertEquals(style.track, end?.track)
    }

    @Test
    fun `a single day is capped with nothing to join`() {
        val single = resolve(DateRangeDayPosition.Single)

        assertEquals(style.cap, single?.cap)
        assertEquals(Color.Transparent, single?.track)
    }

    @Test
    fun `a day between the ends carries the band and no circle`() {
        val inRange = resolve(DateRangeDayPosition.InRange)

        assertEquals(style.inRange, inRange?.cap)
        assertEquals(style.track, inRange?.track)
    }

    @Test
    fun `today keeps its ring between the ends`() {
        val inRange = resolve(DateRangeDayPosition.InRange, isToday = true)

        assertEquals(style.inRangeToday, inRange?.cap)
    }

    @Test
    fun `an end stays an end on today`() {
        assertEquals(style.cap, resolve(DateRangeDayPosition.Start, isToday = true)?.cap)
    }

    @Test
    fun `an end that became unavailable is still visibly an end`() {
        assertEquals(style.disabledCap, resolve(DateRangeDayPosition.End, enabled = false)?.cap)
    }

    @Test
    fun `a refused day a range stepped over is greyed`() {
        val inRange = resolve(DateRangeDayPosition.InRange, enabled = false)

        assertEquals(style.inRangeDisabled, inRange?.cap)
    }

    @Test
    fun `a previewed day says so before it says anything else`() {
        val end = resolve(DateRangeDayPosition.End, isToday = true, isPreview = true)
        val inRange = resolve(DateRangeDayPosition.InRange, isToday = true, isPreview = true)

        assertEquals(style.previewCap, end?.cap)
        assertEquals(style.previewTrack, end?.track)
        assertEquals(style.previewTrack, inRange?.track)
    }

    @Test
    fun `a day from a neighbouring month keeps the band and mutes only its number`() {
        val inRange = resolve(DateRangeDayPosition.InRange, adjacent = true)

        assertEquals(style.inRangeAdjacent, inRange?.cap)
        assertEquals(style.track, inRange?.track, "a range must not break at a month boundary")
    }

    @Test
    fun `unavailability outranks adjacency between the ends`() {
        val inRange = resolve(DateRangeDayPosition.InRange, enabled = false, adjacent = true)

        assertEquals(style.inRangeDisabled, inRange?.cap)
    }
}
