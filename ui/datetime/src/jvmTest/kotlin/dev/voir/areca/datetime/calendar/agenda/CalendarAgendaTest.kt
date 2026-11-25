package dev.voir.areca.datetime.calendar.agenda

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.getBoundsInRoot
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.swipeDown
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.test.swipe
import androidx.compose.ui.test.swipeUp
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import dev.voir.areca.window.ProvideWindowClass
import dev.voir.areca.window.WindowClass
import dev.voir.areca.datetime.calendar.CalendarBounds
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.theme.ArecaThemeProvider
import dev.voir.areca.window.InputMode
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.yearMonth
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue
import kotlin.time.Clock
import kotlin.time.Instant

/** A [CalendarAgenda] keeps its calendar and its list in step. */
@OptIn(ExperimentalTestApi::class)
class CalendarAgendaTest {

    private val today = LocalDate(2026, 11, 25)
    private val clock = object : Clock {
        override fun now(): Instant = Instant.parse("2026-11-25T12:00:00Z")
    }
    private val bounds = CalendarBounds(minDate = today, maxDate = today.plus(89, DateTimeUnit.DAY))

    /** Every third day from today has two entries. */
    private val days = (0 until 90 step 3).map { today.plus(it, DateTimeUnit.DAY) }

    private class Harness {
        var selected by mutableStateOf(LocalDate(2026, 11, 25))
        lateinit var state: CalendarAgendaState

        /** How many times each day's heading and rows have composed. */
        val headingCompositions = mutableMapOf<LocalDate, Int>()
        val rowCompositions = mutableMapOf<LocalDate, Int>()
    }

    private fun ComposeUiTest.agenda(
        emptyDay: Boolean = false,
        openOn: LocalDate? = null,
        wide: Boolean = false,
        days: List<LocalDate> = this@CalendarAgendaTest.days,
        bounds: CalendarBounds = this@CalendarAgendaTest.bounds,
    ): Harness {
        val harness = Harness()
        val window = if (wide) DpSize(1024.dp, 800.dp) else DpSize(390.dp, 800.dp)
        setContent {
            ArecaThemeProvider(inputMode = InputMode.Touch) {
              ProvideWindowClass(WindowClass(window, InputMode.Touch)) {
                harness.state = rememberCalendarAgendaState(bounds = bounds, clock = clock, timeZone = TimeZone.UTC)
                LaunchedEffect(harness.state) {
                    if (openOn != null) {
                        harness.selected = openOn
                        harness.state.requestScrollToDay(openOn)
                    }
                }
                // Remembered, as a screen's would be, so the counts below measure the agenda alone.
                val heading: @Composable CalendarAgendaDayScope.() -> Unit = remember {
                    {
                        harness.headingCompositions[date] = (harness.headingCompositions[date] ?: 0) + 1
                        Text("Heading $date" + if (isSelected) " (selected)" else "")
                    }
                }
                val rows: @Composable CalendarAgendaDayScope.() -> Unit = remember {
                    {
                        harness.rowCompositions[date] = (harness.rowCompositions[date] ?: 0) + 1
                        Column {
                            Text("Bill A on $date", modifier = Modifier.height(56.dp))
                            Text("Bill B on $date", modifier = Modifier.height(56.dp))
                        }
                    }
                }
                val placeholder: @Composable CalendarAgendaDayScope.() -> Unit = remember { { Text("Nothing planned") } }
                CalendarAgenda(
                    days = days,
                    selected = harness.selected,
                    onSelect = { harness.selected = it },
                    state = harness.state,
                    startDayOfWeek = DayOfWeek.MONDAY,
                    modifier = Modifier.size(window).testTag("agenda"),
                    emptyDay = if (emptyDay) placeholder else null,
                    dayHeading = heading,
                    dayContent = rows,
                )
              }
            }
        }
        waitForIdle()
        return harness
    }

    /** Distance from the bottom of the calendar card (its handle) to the top of [text]'s first node. */
    private fun ComposeUiTest.gapUnderCalendar(text: String): Float {
        val handle = onNode(hasContentDescription("Show one week").or(hasContentDescription("Show the whole month")))
        return (onAllNodesWithText(text)[0].getBoundsInRoot().top - handle.getBoundsInRoot().bottom).value
    }

    @Test
    fun `tapping a day selects it and scrolls its heading to the top of the list`() = runComposeUiTest {
        val harness = agenda()
        onNodeWithContentDescription("Saturday 28 November 2026", substring = true).performClick()
        waitForIdle()

        assertEquals(LocalDate(2026, 11, 28), harness.selected)
        assertFalse(harness.state.calendar.isFolded, "a tap moves the list, not the calendar")
        // The heading's text sits inside its band's top padding (12dp), the band against the calendar.
        assertEquals(12f, gapUnderCalendar("Heading 2026-11-28 (selected)"), 1f)
    }

    @Test
    fun `scrolling the list picks the day at the top and folds the calendar`() = runComposeUiTest {
        val harness = agenda()
        onNodeWithTag("agenda").performTouchInput { swipeUp(startY = bottom - 10f, endY = bottom - 610f) }
        waitForIdle()

        assertNotEquals(today, harness.selected)
        assertEquals(1f, harness.state.calendar.foldFraction)
        assertEquals(harness.state.calendar.foldAnchor, harness.selected)
    }

    @Test
    fun `the list stays against the calendar's edge while a drag folds it`() = runComposeUiTest {
        val harness = agenda()
        onNodeWithTag("agenda").performTouchInput { down(Offset(centerX, bottom - 40f)) }
        repeat(8) { step ->
            onNodeWithTag("agenda").performTouchInput { moveBy(Offset(0f, -30f)) }
            waitForIdle()
            assertEquals(12f, gapUnderCalendar("Heading 2026-11-25 (selected)"), 1f, "after step $step")
        }
        val partWay = harness.state.calendar.foldFraction
        assertTrue(partWay > 0f && partWay < 1f, "the drag folds part of the way (was $partWay)")
        onNodeWithTag("agenda").performTouchInput { up() }
        waitForIdle()
        assertEquals(1f, harness.state.calendar.foldFraction, "let go part way up, the fold finishes")
    }

    @Test
    fun `pulling down at the top opens the month again`() = runComposeUiTest {
        val harness = agenda()
        onNodeWithTag("agenda").performTouchInput { swipeUp(startY = bottom - 10f, endY = bottom - 210f, durationMillis = 600) }
        waitForIdle()
        assertEquals(1f, harness.state.calendar.foldFraction)
        assertEquals(0, harness.state.list.firstVisibleItemIndex)

        onNodeWithTag("agenda").performTouchInput { swipeDown(startY = centerY, endY = centerY + 400f, durationMillis = 600) }
        waitForIdle()
        assertEquals(0f, harness.state.calendar.foldFraction)
    }

    @Test
    fun `a fold under the finger recomposes no rows and a new selection only two headings`() = runComposeUiTest {
        val harness = agenda()
        val shown = harness.rowCompositions.toMap()
        onNodeWithTag("agenda").performTouchInput { down(Offset(centerX, bottom - 40f)) }
        repeat(6) {
            onNodeWithTag("agenda").performTouchInput { moveBy(Offset(0f, -20f)) }
            waitForIdle()
        }
        assertTrue(harness.state.calendar.foldFraction > 0f)
        shown.forEach { (date, count) -> assertEquals(count, harness.rowCompositions[date], "rows of $date recomposed") }
        onNodeWithTag("agenda").performTouchInput { up() }
        waitForIdle()

        val headings = harness.headingCompositions.toMap()
        val rowsBefore = harness.rowCompositions.toMap()
        harness.selected = LocalDate(2026, 11, 28)
        waitForIdle()
        val changed = setOf(today, LocalDate(2026, 11, 28))
        assertEquals(changed, headings.filter { (date, count) -> harness.headingCompositions[date] != count }.keys)
        assertEquals(changed, rowsBefore.filter { (date, count) -> harness.rowCompositions[date] != count }.keys)
    }

    @Test
    fun `the folded week rests on a whole page after scrolling into the next month`() = runComposeUiTest {
        val harness = agenda()
        repeat(4) {
            onNodeWithTag("agenda").performTouchInput { swipeUp(startY = bottom - 10f, endY = top + 10f, durationMillis = 120) }
        }
        waitForIdle()

        val calendar = harness.state.calendar
        assertTrue(harness.selected >= LocalDate(2026, 12, 1), "the scroll reached December (at ${harness.selected})")
        assertTrue(calendar.isFolded)
        assertEquals(calendar.bounds.pageOf(harness.selected.yearMonth), calendar.pagerState.currentPage)
        assertEquals(0f, calendar.pagerState.currentPageOffsetFraction, "no page is left part way")
        onNodeWithContentDescription(harness.selected.descriptionPrefix(), substring = true).assertIsDisplayed()
    }

    @Test
    fun `the handle opens the folded month and a scroll folds it again`() = runComposeUiTest {
        val harness = agenda()
        onNodeWithTag("agenda").performTouchInput { swipeUp(startY = bottom - 10f, endY = bottom - 610f) }
        waitForIdle()
        assertTrue(harness.state.calendar.isFolded)

        onNodeWithContentDescription("Show the whole month").performClick()
        waitForIdle()
        assertFalse(harness.state.calendar.isFolded)
        onNodeWithContentDescription("Show one week").assertIsDisplayed()

        onNodeWithTag("agenda").performTouchInput { swipeUp(startY = bottom - 10f, endY = bottom - 610f) }
        waitForIdle()
        assertTrue(harness.state.calendar.isFolded)
    }

    @Test
    fun `a picked day with no entries gets a placeholder group`() = runComposeUiTest {
        val harness = agenda(emptyDay = true)
        onNodeWithContentDescription("Thursday 26 November 2026", substring = true).performClick()
        waitForIdle()

        assertEquals(LocalDate(2026, 11, 26), harness.selected)
        onNodeWithText("Nothing planned").assertIsDisplayed()
    }

    @Test
    fun `a scroll asked for before the first layout lands under the calendar`() = runComposeUiTest {
        val harness = agenda(emptyDay = true, openOn = LocalDate(2026, 12, 9))
        assertEquals(LocalDate(2026, 12, 9), harness.selected)
        assertEquals(12f, gapUnderCalendar("Heading 2026-12-09 (selected)"), 1f)
    }

    @Test
    fun `on a wide window the calendar never folds and has no handle`() = runComposeUiTest {
        val harness = agenda(wide = true)
        onNodeWithTag("agenda").performTouchInput { swipe(start = Offset(right - 100f, bottom - 60f), end = Offset(right - 100f, bottom - 660f)) }
        waitForIdle()

        assertNotEquals(today, harness.selected, "scrolling the list still picks days")
        assertEquals(0f, harness.state.calendar.foldFraction)
        onAllNodesWithContentDescription("Show the whole month").assertCountEquals(0)
        onAllNodesWithContentDescription("Show one week").assertCountEquals(0)
    }

    @Test
    fun `on a wide window a tapped day's heading goes to the top of the list`() = runComposeUiTest {
        val harness = agenda(wide = true)
        onNodeWithContentDescription("Saturday 28 November 2026", substring = true).performClick()
        waitForIdle()

        assertEquals(LocalDate(2026, 11, 28), harness.selected)
        val heading = onAllNodesWithText("Heading 2026-11-28 (selected)")[0].getBoundsInRoot()
        // The list card starts at the pane gap (16dp), and the text sits inside the band's 12dp.
        assertEquals(28f, heading.top.value, 1f)
    }

    @Test
    fun `an agenda with no days shows the selected day's placeholder`() = runComposeUiTest {
        agenda(emptyDay = true, days = emptyList())
        onNodeWithText("Nothing planned").assertIsDisplayed()
    }

    @Test
    fun `a one-week window lists only that week`() = runComposeUiTest {
        agenda(bounds = CalendarBounds(today, today.plus(6, DateTimeUnit.DAY)), days = days.filter { it <= today.plus(6, DateTimeUnit.DAY) })
        onAllNodesWithText("Bill A on 2026-11-28")[0].assertIsDisplayed()
    }

    @Test
    fun `today's pill brings the selection and the list back`() = runComposeUiTest {
        val harness = agenda()
        onNodeWithContentDescription("Saturday 28 November 2026", substring = true).performClick()
        waitForIdle()
        onNodeWithText("Today").performClick()
        waitForIdle()

        assertEquals(today, harness.selected)
        assertEquals(0, harness.state.list.firstVisibleItemIndex, "today's group is the first, so the list returns to the top")
    }

    /** The start of a day's description in the calendar, such as "Tuesday 1 December 2026". */
    private fun LocalDate.descriptionPrefix(): String {
        val weekday = dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }
        val month = this.month.name.lowercase().replaceFirstChar { it.uppercase() }
        return "$weekday $day $month $year"
    }
}
