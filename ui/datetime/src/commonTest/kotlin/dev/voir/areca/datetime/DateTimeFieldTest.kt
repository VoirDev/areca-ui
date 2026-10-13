package dev.voir.areca.datetime

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/** What a date-and-time field reports for each pick, and how it renders what it holds. */
class DateTimeFieldTest {

    private val date = LocalDate(2026, 9, 13)
    private val defaultTime = LocalTime(hour = 9, minute = 0)

    @Test
    fun `a time picked before any date is held rather than reported`() {
        assertNull(valueAfterTimePick(time = LocalTime(14, 30), value = null))
    }

    @Test
    fun `a time picked on a held value keeps its date`() {
        val held = LocalDateTime(date, LocalTime(8, 0))

        assertEquals(
            LocalDateTime(date, LocalTime(14, 30)),
            valueAfterTimePick(time = LocalTime(14, 30), value = held),
        )
    }

    @Test
    fun `the first date picked takes the drafted time`() {
        val value = valueAfterDatePick(
            date = date,
            value = null,
            draftTime = LocalTime(14, 30),
            defaultTime = defaultTime,
        )

        assertEquals(LocalDateTime(date, LocalTime(14, 30)), value)
    }

    @Test
    fun `a date picked with no time anywhere takes the default time`() {
        val value = valueAfterDatePick(
            date = date,
            value = null,
            draftTime = null,
            defaultTime = defaultTime,
        )

        assertEquals(LocalDateTime(date, defaultTime), value)
    }

    @Test
    fun `a date picked on a held value keeps its time`() {
        val held = LocalDateTime(LocalDate(2026, 1, 1), LocalTime(18, 45))

        val value = valueAfterDatePick(
            date = date,
            value = held,
            draftTime = null,
            defaultTime = defaultTime,
        )

        assertEquals(LocalDateTime(date, LocalTime(18, 45)), value)
    }

    @Test
    fun `the default format counts hours the way a twelve-hour wheel does`() {
        val format = DateTimeFieldFormat.numeric(timeFormat = TimeFormat.Hours12)

        assertEquals("2:30 PM", format.time(LocalTime(14, 30)))
        assertEquals("12:05 AM", format.time(LocalTime(0, 5)))
        assertEquals("12:00 PM", format.time(LocalTime(12, 0)))
        assertEquals("11:59 AM", format.time(LocalTime(11, 59)))
    }

    @Test
    fun `the default format counts hours the way a twenty-four-hour wheel does`() {
        val format = DateTimeFieldFormat.numeric(timeFormat = TimeFormat.Hours24)

        assertEquals("14:30", format.time(LocalTime(14, 30)))
        assertEquals("00:05", format.time(LocalTime(0, 5)))
    }

    @Test
    fun `a twelve-hour format names the periods from the labels`() {
        val labels = TimePickerLabels.English.copy(am = "a.m.", pm = "p.m.")
        val format = DateTimeFieldFormat.numeric(
            timeFormat = TimeFormat.Hours12,
            timeLabels = labels,
        )

        assertEquals("9:15 a.m.", format.time(LocalTime(9, 15)))
        assertEquals("9:15 p.m.", format.time(LocalTime(21, 15)))
    }

    @Test
    fun `the date is ISO-ordered and zero-padded`() {
        val format = DateTimeFieldFormat.numeric(timeFormat = TimeFormat.Hours12)

        assertEquals("2026-09-13", format.date(date))
        assertEquals("0987-03-04", format.date(LocalDate(987, 3, 4)))
    }
}
