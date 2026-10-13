package dev.voir.areca.datetime.calendar

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.YearMonth
import kotlinx.datetime.daysUntil
import kotlinx.datetime.number
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** Layout of a month into rows of seven cells. */
class CalendarMonthTest {

    @Test
    fun `a Monday-start week puts Monday in the first column`() {
        val weeks = YearMonth(2026, 9).calendarWeeks(
            startDayOfWeek = DayOfWeek.MONDAY,
            showAdjacentMonths = true,
            fixedWeekCount = false,
        )

        assertEquals(DayOfWeek.MONDAY, weeks.first().first()!!.dayOfWeek)
        assertEquals(DayOfWeek.SUNDAY, weeks.first().last()!!.dayOfWeek)
    }

    @Test
    fun `a Sunday-start week puts Sunday in the first column`() {
        val weeks = YearMonth(2026, 9).calendarWeeks(
            startDayOfWeek = DayOfWeek.SUNDAY,
            showAdjacentMonths = true,
            fixedWeekCount = false,
        )

        assertEquals(DayOfWeek.SUNDAY, weeks.first().first()!!.dayOfWeek)
        assertEquals(DayOfWeek.SATURDAY, weeks.first().last()!!.dayOfWeek)
    }

    @Test
    fun `every start day of the week puts its own day in the first column`() {
        DayOfWeek.entries.forEach { start ->
            val weeks = YearMonth(2026, 9).calendarWeeks(
                startDayOfWeek = start,
                showAdjacentMonths = true,
                fixedWeekCount = false,
            )

            assertEquals(
                expected = start,
                actual = weeks.first().first()!!.dayOfWeek,
                message = "first column of a week starting on $start",
            )
        }
    }

    @Test
    fun `the first of the month lands in the column its day of the week calls for`() {
        // 1 September 2026 is a Tuesday, so with a Monday-start week it sits in column 1.
        val weeks = YearMonth(2026, 9).calendarWeeks(
            startDayOfWeek = DayOfWeek.MONDAY,
            showAdjacentMonths = false,
            fixedWeekCount = false,
        )

        assertNull(weeks[0][0], "the Monday before the 1st belongs to August")
        assertEquals(LocalDate(2026, 9, 1), weeks[0][1])
    }

    @Test
    fun `a month that fills exactly five rows is not given a sixth`() {
        // 1 May 2026 is a Friday: four leading cells plus 31 days is exactly 35.
        val weeks = YearMonth(2026, 5).calendarWeeks(
            startDayOfWeek = DayOfWeek.MONDAY,
            showAdjacentMonths = false,
            fixedWeekCount = false,
        )

        assertEquals(5, weeks.size)
    }

    @Test
    fun `a month that spills past five rows is given six`() {
        // 1 August 2026 is a Saturday: five leading cells plus 31 days is 36.
        val weeks = YearMonth(2026, 8).calendarWeeks(
            startDayOfWeek = DayOfWeek.MONDAY,
            showAdjacentMonths = false,
            fixedWeekCount = false,
        )

        assertEquals(6, weeks.size)
    }

    @Test
    fun `a February starting on the first column needs four rows`() {
        // 1 February 2021 is a Monday and the month has 28 days: exactly four weeks.
        val weeks = YearMonth(2021, 2).calendarWeeks(
            startDayOfWeek = DayOfWeek.MONDAY,
            showAdjacentMonths = false,
            fixedWeekCount = false,
        )

        assertEquals(4, weeks.size)
    }

    @Test
    fun `a leap February carries its 29th`() {
        val days = YearMonth(2024, 2).calendarWeeks(
            startDayOfWeek = DayOfWeek.MONDAY,
            showAdjacentMonths = false,
            fixedWeekCount = false,
        ).flatten().filterNotNull()

        assertEquals(29, days.size)
        assertEquals(LocalDate(2024, 2, 29), days.last())
    }

    @Test
    fun `a fixed week count gives every month six rows`() {
        listOf(YearMonth(2021, 2), YearMonth(2026, 5), YearMonth(2026, 8)).forEach { month ->
            val weeks = month.calendarWeeks(
                startDayOfWeek = DayOfWeek.MONDAY,
                showAdjacentMonths = true,
                fixedWeekCount = true,
            )

            assertEquals(MAX_WEEKS_IN_MONTH, weeks.size, "rows for $month")
            assertEquals(MAX_WEEKS_IN_MONTH * DAYS_IN_WEEK, weeks.flatten().size, "cells for $month")
        }
    }

    @Test
    fun `hiding adjacent months blanks their cells but keeps their slots`() {
        val weeks = YearMonth(2026, 9).calendarWeeks(
            startDayOfWeek = DayOfWeek.MONDAY,
            showAdjacentMonths = false,
            fixedWeekCount = false,
        )

        assertTrue(weeks.all { it.size == DAYS_IN_WEEK })
        assertTrue(
            weeks.flatten().filterNotNull().all { it.month.number == 9 },
            "only September's own days carry a date",
        )
    }

    @Test
    fun `showing adjacent months fills every cell with a consecutive date`() {
        val days = YearMonth(2026, 9).calendarWeeks(
            startDayOfWeek = DayOfWeek.MONDAY,
            showAdjacentMonths = true,
            fixedWeekCount = true,
        ).flatten()

        assertTrue(days.all { it != null })
        days.filterNotNull().zipWithNext { earlier, later ->
            assertEquals(1, earlier.daysUntil(later), "$earlier to $later")
        }
    }

    @Test
    fun `every month of a year lays out into whole weeks`() {
        (1..12).forEach { monthNumber ->
            val weeks = YearMonth(2026, monthNumber).calendarWeeks(
                startDayOfWeek = DayOfWeek.MONDAY,
                showAdjacentMonths = false,
                fixedWeekCount = false,
            )

            assertTrue(weeks.all { it.size == DAYS_IN_WEEK }, "month $monthNumber")
            assertEquals(
                expected = YearMonth(2026, monthNumber).numberOfDays,
                actual = weeks.flatten().filterNotNull().size,
                message = "days rendered for month $monthNumber",
            )
        }
    }

    @Test
    fun `the weekday order starts on the requested day and wraps`() {
        assertEquals(
            listOf(
                DayOfWeek.SUNDAY,
                DayOfWeek.MONDAY,
                DayOfWeek.TUESDAY,
                DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY,
                DayOfWeek.FRIDAY,
                DayOfWeek.SATURDAY,
            ),
            daysOfWeekFrom(DayOfWeek.SUNDAY),
        )
        assertEquals(
            listOf(
                DayOfWeek.SATURDAY,
                DayOfWeek.SUNDAY,
                DayOfWeek.MONDAY,
                DayOfWeek.TUESDAY,
                DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY,
                DayOfWeek.FRIDAY,
            ),
            daysOfWeekFrom(DayOfWeek.SATURDAY),
        )
    }

    @Test
    fun `a day is in the column its distance from the start of the week gives`() {
        assertEquals(0, DayOfWeek.MONDAY.columnIn(DayOfWeek.MONDAY))
        assertEquals(6, DayOfWeek.SUNDAY.columnIn(DayOfWeek.MONDAY))
        assertEquals(0, DayOfWeek.SUNDAY.columnIn(DayOfWeek.SUNDAY))
        assertEquals(1, DayOfWeek.MONDAY.columnIn(DayOfWeek.SUNDAY))
        assertEquals(6, DayOfWeek.FRIDAY.columnIn(DayOfWeek.SATURDAY))
    }
}
