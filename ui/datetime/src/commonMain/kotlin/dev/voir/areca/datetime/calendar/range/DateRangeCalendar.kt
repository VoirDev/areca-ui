package dev.voir.areca.datetime.calendar.range

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerType
import androidx.compose.ui.input.pointer.pointerInput
import dev.voir.areca.datetime.DateTimeLocale
import dev.voir.areca.datetime.calendar.Calendar
import dev.voir.areca.datetime.calendar.CalendarDateRange
import dev.voir.areca.datetime.calendar.CalendarLabels
import dev.voir.areca.datetime.calendar.CalendarScope
import dev.voir.areca.datetime.calendar.CalendarSelection
import dev.voir.areca.datetime.calendar.CalendarSize
import dev.voir.areca.datetime.calendar.CalendarState
import dev.voir.areca.datetime.calendar.CalendarStyle
import dev.voir.areca.datetime.calendar.LocalCalendarSize
import dev.voir.areca.datetime.calendar.LocalCalendarStyle
import dev.voir.areca.datetime.calendar.MonthHeader
import dev.voir.areca.datetime.calendar.WeekdayHeader
import dev.voir.areca.datetime.calendar.rememberCalendarState
import dev.voir.areca.modifier.thenIf
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate

/**
 * A month grid for picking a run of days.
 *
 * [Calendar] with a selection that has a shape. Everything the month grid does — paging, the month
 * and year wheel, the bounds, the slots, the keyboard navigation and the grid semantics — is the
 * same component underneath and behaves identically; what this adds is the two-tap state machine,
 * the limits a range has to satisfy, and cells that know which end of the range they are.
 *
 * Controlled in the same way, and for the same reason: it reports the selection a tap would produce
 * and draws the one it is handed back. A screen that has to save a range, confirm it, or refuse it
 * can do so between the two without racing anything.
 *
 * The limits are the part worth reading twice. [constraints] does not merely refuse an invalid tap —
 * the moment the first end is picked, every day the range could no longer reach is drawn as
 * unavailable, whether it is beyond a maximum length, short of a minimum one, or on the far side of
 * a day somebody else has taken. Days at or before the anchor stay available throughout, because
 * tapping one starts the range again rather than completing it.
 *
 * Under a pointer, hovering paints the range the next tap would produce. It is mouse-only by
 * design: a touch that "entered" a cell would paint a preview of the very tap that is about to
 * replace it. Only the cells whose look the preview changes are redrawn as the pointer moves.
 *
 * For a read-only range — a stay already booked, a reporting period on a dashboard — pass `null` for
 * [onSelectionChange]. The grid still pages and still announces itself; nothing is tappable and
 * nothing previews.
 *
 * @param selection How far through picking the user is. Hold it in state and hand back what
 *   [onSelectionChange] gives you.
 * @param onSelectionChange Called with the selection a tap produced. Never called for a tap that
 *   changes nothing. `null` makes the calendar read-only.
 * @param modifier Modifier applied to the calendar.
 * @param state Which month is showing, and the range it may move in.
 * @param constraints Limits the range must satisfy, and the per-day rule they are applied with.
 * @param onRangeComplete Called when a tap finishes a range, after [onSelectionChange]. For the
 *   screens whose next move is to close a popup or run a search.
 * @param startDayOfWeek Day each week begins on. Defaults to the week start of
 *   [DateTimeLocale.Current].
 * @param labels Text the calendar renders. Defaults to English.
 * @param rangeLabels What assistive technology reads for a day's place in the range.
 * @param showAdjacentMonths Whether the cells before the 1st and after the last carry the
 *   neighbouring months' dates rather than staying blank. Worth turning on for a range, which then
 *   reads as continuous across a month boundary instead of stopping at the edge of the grid.
 * @param fixedWeekCount Whether every month is laid out over six rows.
 * @param style Colors of the calendar. Defaults to [LocalCalendarStyle], or [CalendarStyle.Default].
 * @param rangeStyle Colors and shapes of the cells the range runs through. Defaults to
 *   [LocalDateRangeStyle], or [DateRangeStyle.Default].
 * @param size Dimensions and type. Defaults to [LocalCalendarSize], or [CalendarSize.Default].
 * @param header The band above the grid. `null` removes it.
 * @param weekdayHeader The Mo/Tu/We row. `null` removes it.
 * @param day One cell. Called for every drawn day of every composed page.
 */
@Composable
public fun DateRangeCalendar(
    selection: DateRangeSelection,
    onSelectionChange: ((DateRangeSelection) -> Unit)?,
    modifier: Modifier = Modifier,
    state: CalendarState = rememberCalendarState(),
    constraints: DateRangeConstraints = DateRangeConstraints(),
    onRangeComplete: ((CalendarDateRange) -> Unit)? = null,
    startDayOfWeek: DayOfWeek = DateTimeLocale.Current.firstDayOfWeek,
    labels: CalendarLabels = CalendarLabels.English,
    rangeLabels: DateRangeLabels = DateRangeLabels.English,
    showAdjacentMonths: Boolean = false,
    fixedWeekCount: Boolean = true,
    style: CalendarStyle = LocalCalendarStyle.current ?: CalendarStyle.Default,
    rangeStyle: DateRangeStyle = LocalDateRangeStyle.current ?: DateRangeStyle.Default,
    size: CalendarSize = LocalCalendarSize.current ?: CalendarSize.Default,
    header: (@Composable CalendarScope.() -> Unit)? = { MonthHeader() },
    weekdayHeader: (@Composable CalendarScope.() -> Unit)? = { WeekdayHeader() },
    day: @Composable CalendarScope.(DateRangeDayState) -> Unit = { dayState ->
        DateRangeDay(
            day = dayState,
            rangeLabels = rangeLabels,
            onClick = onSelectionChange?.let { emit ->
                {
                    val next = selection.select(dayState.date, constraints, state.bounds)
                    if (next != selection) {
                        emit(next)
                        (next as? DateRangeSelection.Complete)?.let { onRangeComplete?.invoke(it.range) }
                    }
                }
            },
        )
    },
) {
    val interactive = onSelectionChange != null
    // Day under the pointer. Tracked whenever the calendar takes taps, not only while a range is
    // half-picked, so a pointer that leaves the grid between two ranges is not remembered as still
    // hovering the day it left.
    val hovered = remember { mutableStateOf<LocalDate?>(null) }

    val dateRule = remember(constraints, selection, state.bounds) {
        constraints.dateRuleFor(selection, state.bounds)
    }

    // The selection as the pointer would leave it, resolved once per hover rather than once per
    // cell. A finished range previews nothing: the next tap starts again, and painting a range from
    // an anchor that does not exist yet would be a promise about a day the user has not chosen.
    val currentSelection by rememberUpdatedState(selection)
    val currentConstraints by rememberUpdatedState(constraints)
    val shownSelection = remember(state, interactive) {
        derivedStateOf {
            if (interactive) {
                currentSelection.withPreviewEnd(hovered.value, currentConstraints, state.bounds)
            } else {
                currentSelection
            }
        }
    }

    // What the grid reports as chosen, which is the committed selection and never the preview. A
    // previewed day is drawn as tentative but must not be announced as selected.
    val calendarSelection = remember(selection) {
        CalendarSelection.Range(
            when (selection) {
                DateRangeSelection.Empty -> null
                is DateRangeSelection.Started ->
                    CalendarDateRange(selection.anchor, selection.anchor)

                is DateRangeSelection.Complete -> selection.range
            },
        )
    }

    CompositionLocalProvider(LocalDateRangeStyle provides rangeStyle) {
        Calendar(
            modifier = modifier,
            state = state,
            selection = calendarSelection,
            // Cells carry their own clicks, because a range cell has to decide what its tap means
            // from the selection rather than merely report a date.
            onDateClick = null,
            dateRule = dateRule,
            startDayOfWeek = startDayOfWeek,
            labels = labels,
            showAdjacentMonths = showAdjacentMonths,
            fixedWeekCount = fixedWeekCount,
            style = style,
            size = size,
            header = header,
            weekdayHeader = weekdayHeader,
        ) { calendarDay ->
            // Each cell derives only its own position, so a hover recomposes the cells whose
            // position it changes instead of every cell of every composed page.
            val position by remember(calendarDay.date) {
                derivedStateOf { shownSelection.value.positionOf(calendarDay.date) }
            }
            val dayState = DateRangeDayState(
                day = calendarDay,
                position = position,
                // In the range only because of where the pointer is. False for the anchor, which is
                // a choice already made and keeps its solid circle while the rest stays tentative.
                isPreview = position != DateRangeDayPosition.None &&
                    calendarDay.date !in selection,
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .thenIf(interactive) { hoverPreview(calendarDay.date, hovered) },
            ) {
                day(dayState)
            }
        }
    }
}

/**
 * Tracks whether a pointer is over this cell.
 *
 * Mouse only. Compose reports a touch as entering a cell when it presses and leaving when it
 * releases, so following those would paint a preview of the tap that is in the middle of replacing
 * it — a flash on every selection, on exactly the devices with no pointer to preview with.
 *
 * Leaving is recorded only when this cell is the one currently hovered, so that the exit of the cell
 * being left cannot clear the entry of the cell being moved into, whichever order the two arrive in.
 *
 * @param date Day this cell draws.
 * @param hovered Day under the pointer, shared by every cell of the grid.
 * @return Modifier reporting this cell's hovering into [hovered].
 */
private fun Modifier.hoverPreview(
    date: LocalDate,
    hovered: MutableState<LocalDate?>,
): Modifier = pointerInput(date, hovered) {
    awaitPointerEventScope {
        while (true) {
            val event = awaitPointerEvent()
            if (event.changes.firstOrNull()?.type != PointerType.Mouse) continue
            when (event.type) {
                PointerEventType.Enter -> hovered.value = date
                PointerEventType.Exit -> if (hovered.value == date) hovered.value = null
            }
        }
    }
}
