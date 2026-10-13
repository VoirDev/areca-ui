package dev.voir.areca.datetime.calendar.range

import dev.voir.areca.datetime.calendar.CalendarBounds
import dev.voir.areca.datetime.calendar.CalendarDateRange
import dev.voir.areca.datetime.calendar.CalendarDateRule
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** How far through picking a range the user is, and what each tap does to it. */
class DateRangeSelectionTest {

    private val bounds = CalendarBounds(
        minDate = LocalDate(2026, 1, 1),
        maxDate = LocalDate(2026, 12, 31),
    )
    private val anchor = LocalDate(2026, 6, 10)
    private val unlimited = DateRangeConstraints()

    private fun day(dayOfMonth: Int) = LocalDate(2026, 6, dayOfMonth)

    @Test
    fun `nothing is picked to begin with`() {
        assertEquals(null, DateRangeSelection.Empty.range)
        assertFalse(anchor in DateRangeSelection.Empty)
    }

    @Test
    fun `an unfinished range holds its anchor and no range`() {
        val selection = DateRangeSelection.Started(anchor)

        assertEquals(null, selection.range)
        assertTrue(anchor in selection)
        assertFalse(day(11) in selection)
    }

    @Test
    fun `a finished range holds every one of its days`() {
        val selection = DateRangeSelection.Complete(CalendarDateRange(day(10), day(12)))

        assertEquals(CalendarDateRange(day(10), day(12)), selection.range)
        assertTrue(day(11) in selection)
        assertFalse(day(13) in selection)
    }

    @Test
    fun `the first tap sets the anchor`() {
        val selection = DateRangeSelection.Empty.select(anchor, unlimited, bounds)

        assertEquals(DateRangeSelection.Started(anchor), selection)
    }

    @Test
    fun `a later day finishes the range`() {
        val selection = DateRangeSelection.Started(anchor).select(day(14), unlimited, bounds)

        assertEquals(DateRangeSelection.Complete(CalendarDateRange(anchor, day(14))), selection)
    }

    @Test
    fun `tapping the anchor again finishes a single-day range`() {
        val selection = DateRangeSelection.Started(anchor).select(anchor, unlimited, bounds)

        assertEquals(DateRangeSelection.Complete(CalendarDateRange(anchor, anchor)), selection)
    }

    @Test
    fun `tapping the anchor does nothing when a single day is too short`() {
        val constraints = DateRangeConstraints(minDays = 2)
        val started = DateRangeSelection.Started(anchor)

        assertEquals(started, started.select(anchor, constraints, bounds))
    }

    @Test
    fun `an earlier day starts the range again rather than filling backwards`() {
        val selection = DateRangeSelection.Started(anchor).select(day(3), unlimited, bounds)

        assertEquals(DateRangeSelection.Started(day(3)), selection)
    }

    @Test
    fun `a finished range gives way to a new anchor`() {
        val complete = DateRangeSelection.Complete(CalendarDateRange(day(10), day(12)))

        assertEquals(
            DateRangeSelection.Started(day(20)),
            complete.select(day(20), unlimited, bounds),
        )
    }

    @Test
    fun `a day beyond the longest allowed range does nothing`() {
        val constraints = DateRangeConstraints(maxDays = 5)
        val started = DateRangeSelection.Started(anchor)

        assertEquals(
            DateRangeSelection.Complete(CalendarDateRange(anchor, day(14))),
            started.select(day(14), constraints, bounds),
        )
        assertEquals(started, started.select(day(15), constraints, bounds))
    }

    @Test
    fun `a day short of the shortest allowed range does nothing`() {
        val constraints = DateRangeConstraints(minDays = 3)
        val started = DateRangeSelection.Started(anchor)

        assertEquals(started, started.select(day(11), constraints, bounds))
        assertEquals(
            DateRangeSelection.Complete(CalendarDateRange(anchor, day(12))),
            started.select(day(12), constraints, bounds),
        )
    }

    @Test
    fun `an unavailable day never changes the selection`() {
        val constraints = DateRangeConstraints(dateRule = CalendarDateRule.blocking(setOf(day(14))))

        assertEquals(
            DateRangeSelection.Empty,
            DateRangeSelection.Empty.select(day(14), constraints, bounds),
        )
    }

    @Test
    fun `a day outside the calendar never changes the selection`() {
        assertEquals(
            DateRangeSelection.Empty,
            DateRangeSelection.Empty.select(LocalDate(2027, 1, 1), unlimited, bounds),
        )
    }

    @Test
    fun `a day past a blocked one cannot finish the range`() {
        val constraints = DateRangeConstraints(
            dateRule = CalendarDateRule.blocking(setOf(day(12))),
            blockedDays = BlockedDayPolicy.StopAtBlocked,
        )
        val started = DateRangeSelection.Started(anchor)

        assertEquals(
            DateRangeSelection.Complete(CalendarDateRange(anchor, day(11))),
            started.select(day(11), constraints, bounds),
        )
        assertEquals(started, started.select(day(13), constraints, bounds))
    }

    @Test
    fun `a day past a blocked one finishes the range when gaps are allowed`() {
        val constraints = DateRangeConstraints(
            dateRule = CalendarDateRule.blocking(setOf(day(12))),
            blockedDays = BlockedDayPolicy.AllowGaps,
        )

        assertEquals(
            DateRangeSelection.Complete(CalendarDateRange(anchor, day(13))),
            DateRangeSelection.Started(anchor).select(day(13), constraints, bounds),
        )
    }

    @Test
    fun `nothing is in range while nothing is picked`() {
        assertEquals(DateRangeDayPosition.None, DateRangeSelection.Empty.positionOf(anchor))
    }

    @Test
    fun `a lone anchor draws as a single day`() {
        val selection = DateRangeSelection.Started(anchor)

        assertEquals(DateRangeDayPosition.Single, selection.positionOf(anchor))
        assertEquals(DateRangeDayPosition.None, selection.positionOf(day(11)))
    }

    @Test
    fun `a finished range caps its ends and fills between them`() {
        val selection = DateRangeSelection.Complete(CalendarDateRange(day(10), day(13)))

        assertEquals(DateRangeDayPosition.None, selection.positionOf(day(9)))
        assertEquals(DateRangeDayPosition.Start, selection.positionOf(day(10)))
        assertEquals(DateRangeDayPosition.InRange, selection.positionOf(day(11)))
        assertEquals(DateRangeDayPosition.InRange, selection.positionOf(day(12)))
        assertEquals(DateRangeDayPosition.End, selection.positionOf(day(13)))
        assertEquals(DateRangeDayPosition.None, selection.positionOf(day(14)))
    }

    @Test
    fun `a finished single-day range draws as a single day`() {
        val selection = DateRangeSelection.Complete(CalendarDateRange(anchor, anchor))

        assertEquals(DateRangeDayPosition.Single, selection.positionOf(anchor))
    }

    @Test
    fun `a preview paints the range the next tap would finish`() {
        val preview = DateRangeSelection.Started(anchor).withPreviewEnd(day(13), unlimited, bounds)

        assertEquals(DateRangeSelection.Complete(CalendarDateRange(anchor, day(13))), preview)
    }

    @Test
    fun `a preview before the anchor paints nothing because the tap would start again`() {
        val started = DateRangeSelection.Started(anchor)

        assertEquals(started, started.withPreviewEnd(day(3), unlimited, bounds))
    }

    @Test
    fun `nothing hovered previews nothing`() {
        val started = DateRangeSelection.Started(anchor)

        assertEquals(started, started.withPreviewEnd(null, unlimited, bounds))
    }

    @Test
    fun `only a half-picked range previews`() {
        val complete = DateRangeSelection.Complete(CalendarDateRange(day(10), day(12)))

        assertEquals(complete, complete.withPreviewEnd(day(20), unlimited, bounds))
        assertEquals(
            DateRangeSelection.Empty,
            DateRangeSelection.Empty.withPreviewEnd(day(20), unlimited, bounds),
        )
    }

    @Test
    fun `a day past the longest range previews nothing because the tap would do nothing`() {
        val started = DateRangeSelection.Started(anchor)
        val fortnight = DateRangeConstraints(maxDays = 14)

        assertEquals(started, started.withPreviewEnd(day(24), fortnight, bounds))
    }

    @Test
    fun `a day beyond a blocked day previews nothing when a range stops at blocked days`() {
        val started = DateRangeSelection.Started(anchor)
        val booked = DateRangeConstraints(
            dateRule = CalendarDateRule.blocking(setOf(day(12))),
            blockedDays = BlockedDayPolicy.StopAtBlocked,
        )

        assertEquals(started, started.withPreviewEnd(day(14), booked, bounds))
    }

    @Test
    fun `a day the rule refuses previews nothing even when the range may span it`() {
        val started = DateRangeSelection.Started(anchor)
        val holiday = DateRangeConstraints(
            dateRule = CalendarDateRule.blocking(setOf(day(12))),
            blockedDays = BlockedDayPolicy.AllowGaps,
        )

        assertEquals(started, started.withPreviewEnd(day(12), holiday, bounds))
        assertEquals(
            DateRangeSelection.Complete(CalendarDateRange(anchor, day(14))),
            started.withPreviewEnd(day(14), holiday, bounds),
        )
    }
}
