package dev.voir.areca.datetime.calendar.agenda

import kotlinx.datetime.LocalDate
import kotlinx.datetime.YearMonth
import kotlin.test.Test
import kotlin.test.assertEquals

/** The agenda's list items and the group each belongs to. */
class AgendaModelTest {

    private val nov25 = LocalDate(2026, 11, 25)
    private val nov27 = LocalDate(2026, 11, 27)
    private val dec1 = LocalDate(2026, 12, 1)
    private val days = listOf(nov25, nov27, dec1)

    @Test
    fun `each day is a heading and its rows with a separator per month`() {
        val entries = agendaEntries(days, placeholder = null, monthSeparators = true)
        assertEquals(
            listOf(
                AgendaMonth(YearMonth(2026, 11)),
                AgendaHeading(nov25, false), AgendaBody(nov25, false),
                AgendaHeading(nov27, false), AgendaBody(nov27, false),
                AgendaMonth(YearMonth(2026, 12)),
                AgendaHeading(dec1, false), AgendaBody(dec1, false),
            ),
            entries,
        )
    }

    @Test
    fun `a placeholder joins in date order and only when the day has no entries`() {
        val nov26 = LocalDate(2026, 11, 26)
        val entries = agendaEntries(days, placeholder = nov26, monthSeparators = false)
        assertEquals(AgendaHeading(nov26, true), entries[2])
        assertEquals(AgendaBody(nov26, true), entries[3])
        assertEquals(6 + 2, entries.size)
        assertEquals(6, agendaEntries(days, placeholder = nov27, monthSeparators = false).size)
    }

    @Test
    fun `a scroll lands on the day or the next day or the last`() {
        val entries = agendaEntries(days, placeholder = null, monthSeparators = true)
        assertEquals(3, entries.scrollTargetFor(nov27))
        assertEquals(3, entries.scrollTargetFor(LocalDate(2026, 11, 26)))
        assertEquals(6, entries.scrollTargetFor(LocalDate(2027, 1, 5)))
        assertEquals(1, entries.firstHeadingIndex())
        assertEquals(-1, entries.headingIndexOf(LocalDate(2026, 11, 26)))
    }

    @Test
    fun `every item belongs to the group whose heading is above it`() {
        val entries = agendaEntries(days, placeholder = null, monthSeparators = true)
        // A separator before a month's first day stays in the group above it, whose heading is pinned
        // over it; the first separator belongs to no group.
        assertEquals(listOf(null, nov25, nov25, nov27, nov27, nov27, dec1, dec1), entries.groupDays().toList())
    }
}
