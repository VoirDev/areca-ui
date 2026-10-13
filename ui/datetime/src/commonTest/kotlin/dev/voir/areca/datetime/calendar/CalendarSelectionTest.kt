package dev.voir.areca.datetime.calendar

import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** What a calendar shows as chosen. */
class CalendarSelectionTest {

    private val first = LocalDate(2026, 9, 1)
    private val second = LocalDate(2026, 9, 2)
    private val third = LocalDate(2026, 9, 3)

    @Test
    fun `a single selection holds at most the one day`() {
        val selection = CalendarSelection.Single(first)

        assertTrue(first in selection)
        assertFalse(second in selection)
        assertEquals(setOf(first), selection.dates)
    }

    @Test
    fun `an empty single selection holds nothing`() {
        val selection = CalendarSelection.Single(null)

        assertFalse(first in selection)
        assertTrue(selection.dates.isEmpty())
    }

    @Test
    fun `the default selection is empty`() {
        assertTrue(CalendarSelection.Empty.dates.isEmpty())
    }

    @Test
    fun `a multiple selection holds every day it was given`() {
        val selection = CalendarSelection.Multiple(setOf(first, third))

        assertTrue(first in selection)
        assertFalse(second in selection)
        assertTrue(third in selection)
    }

    @Test
    fun `a range selection holds every day between its ends`() {
        val selection = CalendarSelection.Range(CalendarDateRange(first, third))

        assertTrue(first in selection)
        assertTrue(second in selection)
        assertTrue(third in selection)
        assertFalse(LocalDate(2026, 9, 4) in selection)
        assertEquals(setOf(first, second, third), selection.dates)
    }

    @Test
    fun `an empty range selection holds nothing`() {
        val selection = CalendarSelection.Range()

        assertFalse(first in selection)
        assertTrue(selection.dates.isEmpty())
    }
}
