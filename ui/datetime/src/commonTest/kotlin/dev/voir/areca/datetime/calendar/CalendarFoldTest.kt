package dev.voir.areca.datetime.calendar

import androidx.compose.animation.core.snap
import androidx.compose.runtime.MonotonicFrameClock
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.YearMonth
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** Folding a calendar's days to one week. */
class CalendarFoldTest {

    private val today = LocalDate(2026, 11, 25)
    private val window = CalendarBounds(minDate = today, maxDate = LocalDate(2027, 2, 22))

    private fun state(bounds: CalendarBounds = window) =
        CalendarState(bounds = bounds, today = today, initialMonth = YearMonth(2026, 11))

    @Test
    fun `the row of a day counts from the month's first row`() {
        val november = YearMonth(2026, 11) // 1 November 2026 is a Sunday.
        assertEquals(0, LocalDate(2026, 11, 1).rowIn(november, DayOfWeek.MONDAY))
        assertEquals(1, LocalDate(2026, 11, 2).rowIn(november, DayOfWeek.MONDAY))
        assertEquals(4, LocalDate(2026, 11, 25).rowIn(november, DayOfWeek.MONDAY))
        assertEquals(5, LocalDate(2026, 11, 30).rowIn(november, DayOfWeek.MONDAY))
        assertEquals(4, LocalDate(2026, 11, 30).rowIn(november, DayOfWeek.SUNDAY))
    }

    @Test
    fun `a day outside the month takes its nearer edge row`() {
        val november = YearMonth(2026, 11)
        assertEquals(0, LocalDate(2026, 10, 31).rowIn(november, DayOfWeek.MONDAY))
        assertEquals(5, LocalDate(2026, 12, 1).rowIn(november, DayOfWeek.MONDAY))
    }

    @Test
    fun `a week step keeps the weekday`() {
        val step = LocalDate(2026, 11, 25).foldStep(1, DayOfWeek.MONDAY, window)
        assertEquals(LocalDate(2026, 12, 2), step)
    }

    @Test
    fun `a step past the bounds stops at their edge or nowhere in the edge week`() {
        // Back from today's week: today is the first reachable day, in the same week.
        assertNull(today.foldStep(-1, DayOfWeek.MONDAY, window))
        // Forward from the week before the last: lands on the last day, a Monday.
        assertEquals(LocalDate(2027, 2, 22), LocalDate(2027, 2, 18).foldStep(1, DayOfWeek.MONDAY, window))
        assertNull(LocalDate(2027, 2, 22).foldStep(1, DayOfWeek.MONDAY, window))
    }

    @Test
    fun `back from the second week keeps the weekday or reaches the first reachable day`() {
        assertEquals(LocalDate(2026, 11, 27), LocalDate(2026, 12, 4).foldStep(-1, DayOfWeek.MONDAY, window))
        assertEquals(today, LocalDate(2026, 12, 1).foldStep(-1, DayOfWeek.MONDAY, window))
    }

    @Test
    fun `a folded height moves from the grid to one row`() {
        assertEquals(200, foldedHeight(fullHeight = 200, rowHeight = 40, fraction = 0f))
        assertEquals(120, foldedHeight(fullHeight = 200, rowHeight = 40, fraction = 0.5f))
        assertEquals(40, foldedHeight(fullHeight = 200, rowHeight = 40, fraction = 1f))
    }

    @Test
    fun `folding keeps the anchor inside the bounds and closes the levels`() = runTest {
        val state = state()
        state.zoomOut()
        withFrames { state.fold(LocalDate(2026, 1, 1), animationSpec = snap()) }
        assertTrue(state.isFolded)
        assertEquals(1f, state.foldFraction)
        assertEquals(CalendarLevel.Days, state.level)
        assertEquals(today, state.foldAnchor)
        assertFalse(state.canZoomOut)

        withFrames { state.unfold(snap()) }
        assertFalse(state.isFolded)
        assertEquals(0f, state.foldFraction)
        assertEquals(today, state.foldAnchor)
        assertTrue(state.canZoomOut)
    }

    @Test
    fun `a fold dragged past half way counts as folded and stops paging the levels`() {
        val state = state()
        state.snapFoldTo(0.3f)
        assertFalse(state.isFolded)
        assertFalse(state.canZoomOut, "part way folded, the days cannot step out to the months")
        state.snapFoldTo(0.6f)
        assertTrue(state.isFolded)
        state.snapFoldTo(2f)
        assertEquals(1f, state.foldFraction)
        state.snapFoldTo(0f)
        assertTrue(state.canZoomOut)
    }

    /** Runs [block] with a frame clock that hands out frames at once, as animations need. */
    private suspend fun withFrames(block: suspend () -> Unit) = withContext(
        object : MonotonicFrameClock {
            override suspend fun <R> withFrameNanos(onFrame: (frameTimeNanos: Long) -> R): R = onFrame(0L)
        },
    ) { block() }

    @Test
    fun `the anchor starts at today and inside the bounds`() {
        assertEquals(today, state().foldAnchor)
        val later = CalendarBounds(minDate = LocalDate(2027, 1, 1), maxDate = LocalDate(2027, 3, 1))
        assertEquals(LocalDate(2027, 1, 1), state(later).foldAnchor)
    }
}
