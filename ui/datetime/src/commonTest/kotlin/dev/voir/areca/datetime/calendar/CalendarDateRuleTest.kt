package dev.voir.areca.datetime.calendar

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** Which days a calendar lets the user choose. */
class CalendarDateRuleTest {

    private val first = LocalDate(2026, 9, 1)
    private val second = LocalDate(2026, 9, 2)
    private val third = LocalDate(2026, 9, 3)

    @Test
    fun `an allow-list refuses everything it does not list`() {
        val rule = CalendarDateRule.allowing(setOf(first, third))

        assertTrue(rule.isEnabled(first))
        assertFalse(rule.isEnabled(second))
        assertTrue(rule.isEnabled(third))
    }

    @Test
    fun `an empty allow-list refuses everything`() {
        assertFalse(CalendarDateRule.allowing(emptySet()).isEnabled(first))
    }

    @Test
    fun `a block-list refuses only what it lists`() {
        val rule = CalendarDateRule.blocking(setOf(second))

        assertTrue(rule.isEnabled(first))
        assertFalse(rule.isEnabled(second))
    }

    @Test
    fun `a range refuses days outside it and allows both of its ends`() {
        val rule = CalendarDateRule.inRange(first..third)

        assertTrue(rule.isEnabled(first))
        assertTrue(rule.isEnabled(third))
        assertFalse(rule.isEnabled(LocalDate(2026, 8, 31)))
        assertFalse(rule.isEnabled(LocalDate(2026, 9, 4)))
    }

    @Test
    fun `a day-of-week rule follows the weekday rather than the date`() {
        val weekdaysOnly = CalendarDateRule.byDayOfWeek {
            it != DayOfWeek.SATURDAY && it != DayOfWeek.SUNDAY
        }

        // 5 September 2026 is a Saturday, the 7th a Monday.
        assertFalse(weekdaysOnly.isEnabled(LocalDate(2026, 9, 5)))
        assertTrue(weekdaysOnly.isEnabled(LocalDate(2026, 9, 7)))
    }

    @Test
    fun `combining rules requires every one of them to allow the day`() {
        val rule = CalendarDateRule.inRange(first..third) + CalendarDateRule.blocking(setOf(second))

        assertTrue(rule.isEnabled(first))
        assertFalse(rule.isEnabled(second), "blocked, though inside the range")
        assertTrue(rule.isEnabled(third))
        assertFalse(rule.isEnabled(LocalDate(2026, 9, 4)), "outside the range, though not blocked")
    }

    @Test
    fun `combining with an always-refusing rule refuses everything`() {
        val rule = CalendarDateRule.All + CalendarDateRule.None

        assertFalse(rule.isEnabled(first))
    }

    @Test
    fun `combining three rules narrows through all of them`() {
        val rule = CalendarDateRule.inRange(LocalDate(2026, 9, 1)..LocalDate(2026, 9, 30)) +
            CalendarDateRule.byDayOfWeek { it != DayOfWeek.SUNDAY } +
            CalendarDateRule.blocking(setOf(LocalDate(2026, 9, 7)))

        assertTrue(rule.isEnabled(LocalDate(2026, 9, 8)))
        assertFalse(rule.isEnabled(LocalDate(2026, 9, 6)), "a Sunday")
        assertFalse(rule.isEnabled(LocalDate(2026, 9, 7)), "blocked")
        assertFalse(rule.isEnabled(LocalDate(2026, 10, 1)), "outside the range")
    }

    @Test
    fun `the default rule allows every day`() {
        assertTrue(CalendarDateRule.All.isEnabled(first))
        assertTrue(CalendarDateRule.All.isEnabled(LocalDate(1900, 1, 1)))
    }
}
