package dev.voir.areca.flow.profile.row

import dev.voir.areca.datetime.calendar.CalendarBounds
import kotlinx.datetime.LocalDate
import kotlinx.datetime.YearMonth
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/** How the birthday sheet opens, and the default way it writes a date. */
class BirthdaySheetOpeningTest {

    private val today = LocalDate(2026, 10, 13)

    @Test
    fun `a date in force is marked and opened on`() {
        val opening = BirthdaySheetOpening.of(
            date = LocalDate(1990, 3, 12),
            today = today,
            bounds = CalendarBounds.of(minDate = null, maxDate = today),
        )

        assertEquals(LocalDate(1990, 3, 12), opening.mark)
        assertEquals(YearMonth(1990, 3), opening.month)
    }

    @Test
    fun `with no date in force nothing is marked`() {
        val opening = BirthdaySheetOpening.of(
            date = null,
            today = today,
            bounds = CalendarBounds.of(minDate = null, maxDate = today),
        )

        assertNull(opening.mark)
        assertEquals(YearMonth(2026, 10), opening.month)
    }

    @Test
    fun `with no date in force the month is pulled back inside an age limit`() {
        val opening = BirthdaySheetOpening.of(
            date = null,
            today = today,
            bounds = CalendarBounds.of(minDate = null, maxDate = LocalDate(2010, 10, 13)),
        )

        assertNull(opening.mark)
        assertEquals(YearMonth(2010, 10), opening.month)
    }

    @Test
    fun `with no date in force the month is pushed forward to the earliest day`() {
        val opening = BirthdaySheetOpening.of(
            date = null,
            today = LocalDate(1890, 6, 1),
            bounds = CalendarBounds.of(minDate = LocalDate(1900, 1, 1), maxDate = null),
        )

        assertEquals(YearMonth(1900, 1), opening.month)
    }

    @Test
    fun `the default format writes the day and the month in words and the year`() {
        assertEquals("12 March 1990", englishDate(LocalDate(1990, 3, 12)))
        assertEquals("1 December 2001", englishDate(LocalDate(2001, 12, 1)))
    }
}
