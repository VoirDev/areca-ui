package dev.voir.areca.datetime.calendar

import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.YearMonth
import kotlinx.datetime.number
import kotlinx.datetime.todayIn
import kotlinx.datetime.yearMonth
import kotlin.time.Clock

/**
 * Which month a [Calendar] is showing, and how to move it.
 *
 * Hoisted out of the composable so navigation is something the caller owns. Everything a screen
 * might want to do to a calendar from the outside — jump to the month of a value the user just
 * typed, snap back to today when a filter resets, disable a "next" button of its own at the end of
 * the range — needs this object rather than anything inside `Calendar`.
 *
 * The scrolling functions suspend, matching `LazyListState` and `PagerState`: a caller in a
 * composable launches them from `rememberCoroutineScope`, and a caller already in a coroutine simply
 * calls them. [CalendarScope] wraps the common ones in fire-and-forget form for use inside header
 * slots.
 *
 * Create one with [rememberCalendarState] rather than directly; the constructor is internal because
 * [today] must come from a clock the caller can control in tests.
 *
 * @property bounds Months this calendar can reach. Fixes the page count, so it cannot be changed
 *   after construction — pass different bounds to [rememberCalendarState] and a new state is built.
 * @property today The day drawn as today. Captured once when the state is created rather than read
 *   per cell, so every cell in a composition agrees and tests can pin it.
 * @param initialMonth Month the calendar opens on, clamped into [bounds].
 */
@Stable
public class CalendarState internal constructor(
    public val bounds: CalendarBounds,
    public val today: LocalDate,
    initialMonth: YearMonth,
) {
    /**
     * Pager backing the month grid.
     *
     * Internal: the page index is an implementation detail of [bounds], and a caller that reached
     * for it would be doing month arithmetic the state already does correctly.
     */
    internal val pagerState: PagerState = PagerState(
        currentPage = bounds.pageOf(initialMonth),
        currentPageOffsetFraction = 0f,
        pageCount = { bounds.monthCount },
    )

    /** Derivation behind [visibleMonth]: the month on the pager's current page. */
    private val visibleMonthState = derivedStateOf { bounds.monthAt(pagerState.currentPage) }

    /**
     * Month the header names.
     *
     * Follows [PagerState.currentPage], so it flips as a swipe crosses the halfway point rather than
     * waiting for the gesture to settle — which is what makes the header feel attached to the
     * gesture. Use [settledMonth] when a decision must not be taken on a swipe the user may reverse.
     */
    public val visibleMonth: YearMonth by visibleMonthState

    /** Derivation behind [settledMonth]: the month on the pager's settled page. */
    private val settledMonthState = derivedStateOf { bounds.monthAt(pagerState.settledPage) }

    /** Month the pager has come to rest on. Unchanged while a swipe is in flight. */
    public val settledMonth: YearMonth by settledMonthState

    /** Whether the month picker is showing in place of the day grid. */
    public var isMonthPickerExpanded: Boolean by mutableStateOf(false)
        private set

    /** Derivation behind [canScrollBackward]: whether the current page is past the first. */
    private val canScrollBackwardState = derivedStateOf { pagerState.currentPage > 0 }

    /** Whether an earlier month exists. `false` on [CalendarBounds.minMonth]. */
    public val canScrollBackward: Boolean by canScrollBackwardState

    /** Derivation behind [canScrollForward]: whether the current page is before the last. */
    private val canScrollForwardState =
        derivedStateOf { pagerState.currentPage < bounds.monthCount - 1 }

    /** Whether a later month exists. `false` on [CalendarBounds.maxMonth]. */
    public val canScrollForward: Boolean by canScrollForwardState

    /**
     * Animates to a month, clamped into [bounds].
     *
     * @param month Month to show.
     */
    public suspend fun animateTo(month: YearMonth) {
        pagerState.animateScrollToPage(bounds.pageOf(month))
    }

    /**
     * Jumps to a month without animating, clamped into [bounds].
     *
     * Prefer this when the move is not the user's doing — restoring a screen, following a value
     * typed into a field — because animating a jump of several years is a long, meaningless blur.
     *
     * @param month Month to show.
     */
    public suspend fun scrollTo(month: YearMonth) {
        pagerState.scrollToPage(bounds.pageOf(month))
    }

    /** Animates one month later. Does nothing on the last page. */
    public suspend fun animateToNextMonth() {
        if (canScrollForward) pagerState.animateScrollToPage(pagerState.currentPage + 1)
    }

    /** Animates one month earlier. Does nothing on the first page. */
    public suspend fun animateToPreviousMonth() {
        if (canScrollBackward) pagerState.animateScrollToPage(pagerState.currentPage - 1)
    }

    /**
     * Animates to the month containing [today] when it is reachable, otherwise to the nearest
     * reachable month.
     */
    public suspend fun animateToToday() {
        animateTo(today.yearMonth)
    }

    /** Shows the month picker in place of the day grid. */
    public fun expandMonthPicker() {
        isMonthPickerExpanded = true
    }

    /** Shows the day grid again. */
    public fun collapseMonthPicker() {
        isMonthPickerExpanded = false
    }

    /** Swaps between the day grid and the month picker. */
    public fun toggleMonthPicker() {
        isMonthPickerExpanded = !isMonthPickerExpanded
    }

    /** Construction helpers that stay inside the module. */
    internal companion object {
        /**
         * Saves the visible month across configuration change and process death.
         *
         * Only the month is saved. [bounds] and [today] are restored from the call site, because
         * both may legitimately differ on the way back — a booking window moves on, and so does
         * today — and a saved copy would silently resurrect yesterday's limits.
         *
         * @param bounds Window to rebuild the state with.
         * @param today Day to rebuild the state with.
         * @return Saver holding the visible month as `[year, monthNumber]`.
         */
        fun saver(bounds: CalendarBounds, today: LocalDate): Saver<CalendarState, List<Int>> =
            Saver(
                save = { listOf(it.visibleMonth.year, it.visibleMonth.month.number) },
                restore = { saved ->
                    CalendarState(
                        bounds = bounds,
                        today = today,
                        initialMonth = YearMonth(saved[0], saved[1]),
                    )
                },
            )
    }
}

/**
 * Creates and remembers a [CalendarState].
 *
 * [clock] and [timeZone] are parameters rather than reads of `Clock.System` inside the calendar for
 * two reasons: today is then computed once for the whole grid instead of once per cell per
 * recomposition, and a test or a screenshot can pin a date without touching the machine's clock.
 *
 * Changing [bounds] rebuilds the state, since the page count is fixed at construction. Changing
 * [initialMonth] does not — it is the month the calendar *opens* on, and moving the user's view
 * because a caller recomputed a default would be surprising. Call [CalendarState.animateTo] to move
 * an existing calendar.
 *
 * @param initialMonth Month to open on, clamped into [bounds]. Defaults to the month containing
 *   today.
 * @param bounds Months the calendar can reach.
 * @param clock Source of today. Inject a fixed clock in tests.
 * @param timeZone Zone today is read in.
 * @return State remembered across recomposition and saved across configuration change.
 */
@Composable
public fun rememberCalendarState(
    initialMonth: YearMonth? = null,
    bounds: CalendarBounds = CalendarBounds.Default,
    clock: Clock = Clock.System,
    timeZone: TimeZone = TimeZone.currentSystemDefault(),
): CalendarState {
    val today = remember(clock, timeZone) { clock.todayIn(timeZone) }
    val openOn = initialMonth ?: today.yearMonth
    return rememberSaveable(bounds, today, saver = CalendarState.saver(bounds, today)) {
        CalendarState(bounds = bounds, today = today, initialMonth = openOn)
    }
}
