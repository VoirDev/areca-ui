package dev.voir.areca.datetime.calendar

import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** A run of consecutive days. */
class CalendarDateRangeTest {

    @Test
    fun `a range ending before it starts is rejected`() {
        assertFailsWith<IllegalArgumentException> {
            CalendarDateRange(LocalDate(2026, 9, 10), LocalDate(2026, 9, 9))
        }
    }

    @Test
    fun `a range counts both of its ends`() {
        val range = CalendarDateRange(LocalDate(2026, 9, 10), LocalDate(2026, 9, 12))

        assertEquals(3, range.dayCount)
        assertEquals(2, range.nights)
        assertFalse(range.isSingleDay)
    }

    @Test
    fun `a single day is one day and no nights`() {
        val range = CalendarDateRange(LocalDate(2026, 9, 10), LocalDate(2026, 9, 10))

        assertEquals(1, range.dayCount)
        assertEquals(0, range.nights)
        assertTrue(range.isSingleDay)
    }

    @Test
    fun `a range counts across a leap February`() {
        val range = CalendarDateRange(LocalDate(2028, 2, 27), LocalDate(2028, 3, 1))

        // 27th, 28th, 29th, 1st.
        assertEquals(4, range.dayCount)
    }

    @Test
    fun `a range holds its own days and nothing either side`() {
        val range = CalendarDateRange(LocalDate(2026, 9, 10), LocalDate(2026, 9, 12))

        assertFalse(LocalDate(2026, 9, 9) in range)
        assertTrue(LocalDate(2026, 9, 10) in range)
        assertTrue(LocalDate(2026, 9, 11) in range)
        assertTrue(LocalDate(2026, 9, 12) in range)
        assertFalse(LocalDate(2026, 9, 13) in range)
    }

    @Test
    fun `the days come out in order with both ends included`() {
        val range = CalendarDateRange(LocalDate(2026, 12, 30), LocalDate(2027, 1, 2))

        assertEquals(
            listOf(
                LocalDate(2026, 12, 30),
                LocalDate(2026, 12, 31),
                LocalDate(2027, 1, 1),
                LocalDate(2027, 1, 2),
            ),
            range.asSequence().toList(),
        )
    }

    @Test
    fun `a single day yields itself once`() {
        val date = LocalDate(2026, 9, 10)

        assertEquals(listOf(date), CalendarDateRange(date, date).asSequence().toList())
    }
}
