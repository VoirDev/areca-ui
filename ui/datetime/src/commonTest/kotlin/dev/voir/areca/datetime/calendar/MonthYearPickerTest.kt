package dev.voir.areca.datetime.calendar

import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals

/** Which month the month wheel lands on when turning the year changes the months on offer. */
class MonthYearPickerTest {

    private val bounds = CalendarBounds(
        minDate = LocalDate(2020, 6, 1),
        maxDate = LocalDate(2022, 3, 31),
    )

    private fun monthsIn(year: Int): List<Int> = bounds.monthNumbersIn(year).toList()

    @Test
    fun `the first month of a partial year stays put when the next year offers every month`() {
        val june2020 = monthsIn(2020).indexOf(6)

        val index = monthIndexInNewList(monthsIn(2020), june2020, monthsIn(2021))

        assertEquals(6, monthsIn(2021)[index])
    }

    @Test
    fun `a month stays put when turning back into a year that still offers it`() {
        val september2021 = monthsIn(2021).indexOf(9)

        val index = monthIndexInNewList(monthsIn(2021), september2021, monthsIn(2020))

        assertEquals(9, monthsIn(2020)[index])
    }

    @Test
    fun `a month before the earliest one on offer lands on the earliest`() {
        val january2021 = monthsIn(2021).indexOf(1)

        val index = monthIndexInNewList(monthsIn(2021), january2021, monthsIn(2020))

        assertEquals(6, monthsIn(2020)[index])
    }

    @Test
    fun `a month after the latest one on offer lands on the latest`() {
        val november2021 = monthsIn(2021).indexOf(11)

        val index = monthIndexInNewList(monthsIn(2021), november2021, monthsIn(2022))

        assertEquals(3, monthsIn(2022)[index])
    }

    @Test
    fun `a wheel built while following a month from outside starts on that month`() {
        val january2021 = monthsIn(2021).indexOf(1)

        val index = seedMonthIndex(
            months = monthsIn(2022),
            previousMonths = monthsIn(2021),
            previousIndex = january2021,
            wanted = 3,
        )

        assertEquals(3, monthsIn(2022)[index])
    }

    @Test
    fun `a followed month the year does not offer lands on the nearest one it does`() {
        val index = seedMonthIndex(
            months = monthsIn(2020),
            previousMonths = monthsIn(2021),
            previousIndex = 0,
            wanted = 2,
        )

        assertEquals(6, monthsIn(2020)[index])
    }

    @Test
    fun `a wheel built by turning the year keeps the month the old wheel rested on`() {
        val september2021 = monthsIn(2021).indexOf(9)

        val index = seedMonthIndex(
            months = monthsIn(2020),
            previousMonths = monthsIn(2021),
            previousIndex = september2021,
            wanted = null,
        )

        assertEquals(9, monthsIn(2020)[index])
    }

    @Test
    fun `a first wheel with nothing to go on starts on the first row`() {
        val index = seedMonthIndex(
            months = monthsIn(2021),
            previousMonths = emptyList(),
            previousIndex = null,
            wanted = null,
        )

        assertEquals(0, index)
    }

    @Test
    fun `an empty list of months lands on the first row`() {
        assertEquals(0, monthIndexInNewList(monthsIn(2021), 4, emptyList()))
        assertEquals(0, monthIndexInNewList(emptyList(), 4, monthsIn(2021)))
    }
}
