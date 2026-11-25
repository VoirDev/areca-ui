package dev.voir.areca.datetime.calendar

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.CoroutineScope
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.YearMonth
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** Which marker a day gets, and how it is read. */
class CalendarMarkerTest {

    private val eventDay = LocalDate(2026, 11, 25)
    private val marker = CalendarMarker.Count(count = 3, contentDescription = "3 events")

    private fun scope(markers: ((LocalDate) -> CalendarMarker?)?) = CalendarScope(
        state = CalendarState(CalendarBounds.Default, today = eventDay, initialMonth = YearMonth(2026, 11)),
        labels = CalendarLabels.English,
        startDayOfWeek = DayOfWeek.MONDAY,
        coroutineScope = CoroutineScope(EmptyCoroutineContext),
        markersState = mutableStateOf(markers),
        monthMarkersState = mutableStateOf(null),
    )

    private fun day(date: LocalDate, month: YearMonth = YearMonth(2026, 11)) = CalendarDayState(
        date = date,
        month = month,
        isAdjacent = date.year != month.year || date.month != month.month,
        isToday = false,
        isSelected = false,
        isEnabled = true,
    )

    @Test
    fun `a day of the page's month gets its marker`() {
        val scope = scope { if (it == eventDay) marker else null }

        assertTrue(scope.hasMarkers)
        assertEquals(marker, scope.markerFor(day(eventDay)))
        assertNull(scope.markerFor(day(LocalDate(2026, 11, 24))))
    }

    @Test
    fun `a day spilling in from a neighbouring month gets none`() {
        val scope = scope { marker }

        assertNull(scope.markerFor(day(LocalDate(2026, 12, 1))))
    }

    @Test
    fun `a calendar without markers keeps no marker row`() {
        assertFalse(scope(null).hasMarkers)
    }

    @Test
    fun `a marker's description is read after the day`() {
        assertEquals("Wednesday 25 November 2026, 3 events", "Wednesday 25 November 2026".withMarker(marker))
        assertEquals("Wednesday", "Wednesday".withMarker(CalendarMarker.Dots(listOf(Color.Red))))
        assertEquals("Wednesday", "Wednesday".withMarker(null))
    }

    @Test
    fun `dots need room for at least one`() {
        assertFailsWith<IllegalArgumentException> { CalendarMarker.Dots(listOf(Color.Red), max = 0) }
    }
}
