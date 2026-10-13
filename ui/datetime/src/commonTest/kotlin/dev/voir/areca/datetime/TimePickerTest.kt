package dev.voir.areca.datetime

import kotlinx.datetime.LocalTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/** How a time picker converts between twelve- and twenty-four-hour clocks. */
class TimePickerTest {

    @Test
    fun `midnight reads as twelve on a twelve-hour clock`() {
        assertEquals(12, toHour12(0))
        assertEquals(DayPeriod.AM, DayPeriod.fromHour24(0))
    }

    @Test
    fun `noon reads as twelve in the afternoon`() {
        assertEquals(12, toHour12(12))
        assertEquals(DayPeriod.PM, DayPeriod.fromHour24(12))
    }

    @Test
    fun `the hours either side of noon stay in their own half`() {
        assertEquals(11, toHour12(11))
        assertEquals(DayPeriod.AM, DayPeriod.fromHour24(11))
        assertEquals(1, toHour12(13))
        assertEquals(DayPeriod.PM, DayPeriod.fromHour24(13))
        assertEquals(11, toHour12(23))
        assertEquals(DayPeriod.PM, DayPeriod.fromHour24(23))
    }

    @Test
    fun `twelve in the morning is midnight and twelve in the afternoon is noon`() {
        assertEquals(0, toHour24(12, DayPeriod.AM))
        assertEquals(12, toHour24(12, DayPeriod.PM))
    }

    @Test
    fun `every hour of the day survives a round trip through the twelve-hour clock`() {
        (0..23).forEach { hour ->
            assertEquals(hour, toHour24(toHour12(hour), DayPeriod.fromHour24(hour)), "hour $hour")
        }
    }

    @Test
    fun `an hour lands on the row that shows it in either format`() {
        (0..23).forEach { hour ->
            assertEquals(hour, hoursOf(TimeFormat.Hours24)[hourIndexOf(hour, TimeFormat.Hours24)])
            assertEquals(
                toHour12(hour),
                hoursOf(TimeFormat.Hours12)[hourIndexOf(hour, TimeFormat.Hours12)],
                "hour $hour",
            )
        }
    }

    @Test
    fun `the wheels read back the time they were seeded with after the format changes`() {
        val time = LocalTime(0, 45)
        val period = DayPeriod.fromHour24(time.hour)

        val twelve = hoursOf(TimeFormat.Hours12)[hourIndexOf(time.hour, TimeFormat.Hours12)]
        val twentyFour = hoursOf(TimeFormat.Hours24)[hourIndexOf(time.hour, TimeFormat.Hours24)]

        assertEquals(time, timeAt(twelve, time.minute, period, TimeFormat.Hours12))
        assertEquals(time, timeAt(twentyFour, time.minute, period, TimeFormat.Hours24))
    }

    @Test
    fun `a twenty-four-hour time ignores the period wheel`() {
        assertEquals(LocalTime(14, 5), timeAt(14, 5, null, TimeFormat.Hours24))
    }

    @Test
    fun `a twelve-hour time without a period is undefined`() {
        assertNull(timeAt(2, 5, null, TimeFormat.Hours12))
    }

    @Test
    fun `a wheel without a row yet gives no time`() {
        assertNull(timeAt(null, 5, DayPeriod.AM, TimeFormat.Hours12))
        assertNull(timeAt(2, null, DayPeriod.AM, TimeFormat.Hours12))
    }
}
