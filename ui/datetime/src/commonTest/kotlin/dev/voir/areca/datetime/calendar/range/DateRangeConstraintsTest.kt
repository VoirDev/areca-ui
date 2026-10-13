package dev.voir.areca.datetime.calendar.range

import dev.voir.areca.datetime.calendar.CalendarBounds
import dev.voir.areca.datetime.calendar.CalendarDateRange
import dev.voir.areca.datetime.calendar.CalendarDateRule
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** The limits a picked range has to satisfy, and the greying they produce. */
class DateRangeConstraintsTest {

    private val bounds = CalendarBounds(
        minDate = LocalDate(2026, 6, 1),
        maxDate = LocalDate(2026, 6, 30),
    )
    private val anchor = LocalDate(2026, 6, 10)

    private fun day(dayOfMonth: Int) = LocalDate(2026, 6, dayOfMonth)

    @Test
    fun `a range of no days is rejected`() {
        assertFailsWith<IllegalArgumentException> { DateRangeConstraints(minDays = 0) }
    }

    @Test
    fun `a maximum shorter than the minimum is rejected`() {
        assertFailsWith<IllegalArgumentException> {
            DateRangeConstraints(minDays = 3, maxDays = 2)
        }
    }

    @Test
    fun `with no limits the range may run to the end of the calendar`() {
        val window = DateRangeConstraints().endWindow(anchor, bounds)

        assertEquals(CalendarDateRange(anchor, bounds.maxDate), window)
    }

    @Test
    fun `a minimum length pushes the window past the days that are too close`() {
        val window = DateRangeConstraints(minDays = 3).endWindow(anchor, bounds)

        assertEquals(day(12), window?.start)
        assertFalse(day(11) in window!!)
    }

    @Test
    fun `a maximum length closes the window counting both ends`() {
        val window = DateRangeConstraints(maxDays = 5).endWindow(anchor, bounds)

        assertEquals(CalendarDateRange(anchor, day(14)), window)
    }

    @Test
    fun `the window never reaches past the calendar`() {
        val window = DateRangeConstraints(maxDays = 365).endWindow(anchor, bounds)

        assertEquals(bounds.maxDate, window?.endInclusive)
    }

    @Test
    fun `the window stops before the first blocked day`() {
        val constraints = DateRangeConstraints(
            dateRule = CalendarDateRule.blocking(setOf(day(15), day(20))),
        )

        assertEquals(CalendarDateRange(anchor, day(14)), constraints.endWindow(anchor, bounds))
    }

    @Test
    fun `a blockage the next day leaves only the single-day range`() {
        val constraints = DateRangeConstraints(dateRule = CalendarDateRule.blocking(setOf(day(11))))

        assertEquals(CalendarDateRange(anchor, anchor), constraints.endWindow(anchor, bounds))
    }

    @Test
    fun `an anchor with nothing long enough to reach offers no window at all`() {
        val constraints = DateRangeConstraints(
            dateRule = CalendarDateRule.blocking(setOf(day(11))),
            minDays = 2,
        )

        assertNull(constraints.endWindow(anchor, bounds))
    }

    @Test
    fun `allowing gaps lets the window span a blocked day`() {
        val constraints = DateRangeConstraints(
            dateRule = CalendarDateRule.blocking(setOf(day(15))),
            blockedDays = BlockedDayPolicy.AllowGaps,
        )

        assertEquals(CalendarDateRange(anchor, bounds.maxDate), constraints.endWindow(anchor, bounds))
    }

    @Test
    fun `an unavailable anchor offers no window`() {
        val constraints = DateRangeConstraints(dateRule = CalendarDateRule.blocking(setOf(anchor)))

        assertNull(constraints.endWindow(anchor, bounds))
    }

    @Test
    fun `an anchor outside the calendar offers no window`() {
        assertNull(DateRangeConstraints().endWindow(LocalDate(2026, 7, 1), bounds))
    }

    @Test
    fun `an anchor on the last day offers only itself`() {
        val window = DateRangeConstraints().endWindow(bounds.maxDate, bounds)

        assertEquals(CalendarDateRange(bounds.maxDate, bounds.maxDate), window)
    }

    @Test
    fun `an anchor on the last day offers nothing when a range needs two days`() {
        assertNull(DateRangeConstraints(minDays = 2).endWindow(bounds.maxDate, bounds))
    }

    @Test
    fun `before a range is started only the per-day rule applies`() {
        val constraints = DateRangeConstraints(
            dateRule = CalendarDateRule.blocking(setOf(day(15))),
            maxDays = 3,
        )
        val rule = constraints.dateRuleFor(DateRangeSelection.Empty, bounds)

        assertTrue(rule.isEnabled(day(1)))
        assertTrue(rule.isEnabled(day(30)))
        assertFalse(rule.isEnabled(day(15)))
    }

    @Test
    fun `a finished range puts the whole calendar back within reach`() {
        val constraints = DateRangeConstraints(maxDays = 3)
        val selection = DateRangeSelection.Complete(CalendarDateRange(anchor, day(12)))
        val rule = constraints.dateRuleFor(selection, bounds)

        assertTrue(rule.isEnabled(day(30)))
    }

    @Test
    fun `a half-picked range greys everything it can no longer reach`() {
        val constraints = DateRangeConstraints(maxDays = 5)
        val rule = constraints.dateRuleFor(DateRangeSelection.Started(anchor), bounds)

        assertTrue(rule.isEnabled(day(14)))
        assertFalse(rule.isEnabled(day(15)))
    }

    @Test
    fun `a half-picked range keeps the earlier days available to start again from`() {
        val constraints = DateRangeConstraints(minDays = 3, maxDays = 5)
        val rule = constraints.dateRuleFor(DateRangeSelection.Started(anchor), bounds)

        assertTrue(rule.isEnabled(day(1)))
        assertTrue(rule.isEnabled(anchor))
        // Too close to the anchor to finish a three-day range, and later than it, so not a restart.
        assertFalse(rule.isEnabled(day(11)))
    }

    @Test
    fun `a half-picked range still refuses the days the per-day rule refuses`() {
        val constraints = DateRangeConstraints(
            dateRule = CalendarDateRule.blocking(setOf(day(5))),
            blockedDays = BlockedDayPolicy.AllowGaps,
        )
        val rule = constraints.dateRuleFor(DateRangeSelection.Started(anchor), bounds)

        assertFalse(rule.isEnabled(day(5)))
    }

    @Test
    fun `an anchor with no window leaves only the earlier days available`() {
        val constraints = DateRangeConstraints(
            dateRule = CalendarDateRule.blocking(setOf(day(11))),
            minDays = 2,
        )
        val rule = constraints.dateRuleFor(DateRangeSelection.Started(anchor), bounds)

        assertTrue(rule.isEnabled(day(9)))
        assertFalse(rule.isEnabled(day(12)))
    }
}
