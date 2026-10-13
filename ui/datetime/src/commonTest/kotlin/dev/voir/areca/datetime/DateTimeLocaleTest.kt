package dev.voir.areca.datetime

import kotlinx.datetime.DayOfWeek
import kotlin.test.Test
import kotlin.test.assertEquals

/** How the platform's locale conventions are read. */
class DateTimeLocaleTest {

    @Test
    fun `a Sunday-based weekday number maps to its day`() {
        assertEquals(DayOfWeek.SUNDAY, dayOfWeekFromSundayBased(1))
        assertEquals(DayOfWeek.MONDAY, dayOfWeekFromSundayBased(2))
        assertEquals(DayOfWeek.SATURDAY, dayOfWeekFromSundayBased(7))
    }

    @Test
    fun `a weekday number out of range falls back to Monday`() {
        assertEquals(DayOfWeek.MONDAY, dayOfWeekFromSundayBased(0))
        assertEquals(DayOfWeek.MONDAY, dayOfWeekFromSundayBased(8))
    }

    @Test
    fun `a pattern with a twelve-hour hour or a period counts to twelve`() {
        assertEquals(TimeFormat.Hours12, timeFormatOfPattern("h:mm a"))
        assertEquals(TimeFormat.Hours12, timeFormatOfPattern("a h:mm"))
        assertEquals(TimeFormat.Hours12, timeFormatOfPattern("K:mm a"))
        assertEquals(TimeFormat.Hours12, timeFormatOfPattern("h a"))
    }

    @Test
    fun `a pattern with a twenty-four-hour hour counts to twenty-four`() {
        assertEquals(TimeFormat.Hours24, timeFormatOfPattern("HH:mm"))
        assertEquals(TimeFormat.Hours24, timeFormatOfPattern("H.mm"))
        assertEquals(TimeFormat.Hours24, timeFormatOfPattern("k:mm"))
    }

    @Test
    fun `quoted letters do not make a pattern twelve-hour`() {
        assertEquals(TimeFormat.Hours24, timeFormatOfPattern("HH 'h' mm"))
        assertEquals(TimeFormat.Hours24, timeFormatOfPattern("HH:mm 'at'"))
    }
}
