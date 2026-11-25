package dev.voir.areca.datetime.calendar.agenda

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import dev.voir.areca.datetime.calendar.CalendarBounds
import dev.voir.areca.datetime.calendar.CalendarState
import dev.voir.areca.datetime.calendar.rememberCalendarState
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.YearMonth
import kotlin.time.Clock

/**
 * The scroll and fold positions of a [CalendarAgenda], hoisted so a screen can drive them.
 *
 * The selected day is not here: it is the caller's, passed to the agenda as `selected` and changed
 * through `onSelect`. This holds where the calendar and the list are. Fold or open the calendar
 * through [calendar] ([CalendarState.fold], [CalendarState.unfold]); the agenda folds it as the
 * list scrolls.
 *
 * Create one with [rememberCalendarAgendaState].
 *
 * @property calendar The calendar's state: its month, bounds, today and fold.
 * @property list The list's scroll position.
 */
@Stable
public class CalendarAgendaState internal constructor(
    public val calendar: CalendarState,
    public val list: LazyListState,
) {
    /**
     * Scrolls the list to [date]'s group — or, when [date] has none, to the next day that has one —
     * and briefly highlights its heading, as a tap on the day in the calendar does. Picking the day
     * stays the caller's: call `onSelect` yourself if the day should also be selected.
     *
     * Days do not follow the scroll while it runs; it ends when it settles or a drag takes over.
     *
     * @param date Day to show.
     */
    public fun requestScrollToDay(date: LocalDate) {
        scrollRequest = ScrollRequest(date, ++requestCount)
    }

    /** The scroll a [CalendarAgenda] is asked to make, or `null` when none waits. */
    internal var scrollRequest: ScrollRequest? by mutableStateOf(null)

    /** Counts requests, so asking for the same day twice scrolls twice. */
    private var requestCount = 0

    /** Whether a requested scroll is running, during which days do not follow the scroll. */
    internal var scrollingToDay: Boolean by mutableStateOf(false)

    /** Day whose heading flashes, and a count that restarts the flash for the same day. */
    internal var highlight: ScrollRequest? by mutableStateOf(null)

    /** The day the calendar was last moved to, so a week step by the user tells itself apart. */
    internal var syncedAnchor: LocalDate? = null
}

/**
 * A request to scroll to a day.
 *
 * @property date Day to show.
 * @property id Number of the request, unique per state.
 */
internal data class ScrollRequest(val date: LocalDate, val id: Int)

/**
 * Creates and remembers a [CalendarAgendaState].
 *
 * @param bounds Days the agenda covers: the calendar reaches only these months, days outside them are
 *   dimmed and cannot be picked. Pass the window the list covers, such as the next 90 days.
 * @param initialMonth Month the calendar opens on, clamped into [bounds]. Defaults to today's.
 * @param clock Source of today. Inject a fixed clock in tests.
 * @param timeZone Zone today is read in.
 * @return State remembered across recomposition; the calendar's month and fold are saved across
 *   configuration change.
 */
@Composable
public fun rememberCalendarAgendaState(
    bounds: CalendarBounds = CalendarBounds.Default,
    initialMonth: YearMonth? = null,
    clock: Clock = Clock.System,
    timeZone: TimeZone = TimeZone.currentSystemDefault(),
): CalendarAgendaState {
    val calendar = rememberCalendarState(initialMonth = initialMonth, bounds = bounds, clock = clock, timeZone = timeZone)
    val list = rememberLazyListState()
    return remember(calendar, list) { CalendarAgendaState(calendar = calendar, list = list) }
}
