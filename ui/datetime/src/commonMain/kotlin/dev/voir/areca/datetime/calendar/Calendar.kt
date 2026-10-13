package dev.voir.areca.datetime.calendar

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.CollectionInfo
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.collectionInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import dev.voir.areca.datetime.DateTimeLocale
import dev.voir.areca.foundation.AnnounceOnChange
import dev.voir.areca.modifier.thenIf
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.tween
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.YearMonth
import kotlinx.datetime.monthsUntil
import kotlinx.datetime.yearMonth
import kotlin.math.abs

/**
 * Everything a day cell needs to draw itself.
 *
 * One value rather than several positional parameters, so a custom day slot reads by name and so a
 * later addition — a dot for "has events", a price under the number — is not a breaking change to
 * every slot anyone has written.
 *
 * [month] is the crucial one. A cell is drawn for a *page*, and a pager composes pages either side
 * of the visible one, so a cell cannot ask the calendar which month is on screen to work out whether
 * it is a spill-over from a neighbour; it has to be told which page it belongs to.
 *
 * @property date The day.
 * @property month The page this cell is drawn on, which is not necessarily [date]'s own month.
 * @property isAdjacent Whether the day spills in from the month before or after [month].
 * @property isToday Whether the day is the calendar's today.
 * @property isSelected Whether the day is in the selection.
 * @property isEnabled Whether the day accepts a tap.
 */
@Immutable
public data class CalendarDayState(
    public val date: LocalDate,
    public val month: YearMonth,
    public val isAdjacent: Boolean,
    public val isToday: Boolean,
    public val isSelected: Boolean,
    public val isEnabled: Boolean,
)

/**
 * What a [Calendar]'s slots can see and do.
 *
 * A class held in `remember` rather than an object built inline, so the slot lambdas are not
 * invalidated on every recomposition of the calendar.
 *
 * The navigation functions here are the fire-and-forget counterparts of [CalendarState]'s suspending
 * ones: a header slot is composable code with no coroutine of its own, so it gets functions it can
 * call straight from an `onClick`.
 *
 * It also carries the grid's keyboard focus. The grid is one tab stop: exactly one day can take
 * focus from Tab, and the arrow, Home, End and page keys move focus from day to day, paging the
 * calendar when the move leaves the month.
 *
 * @property state The calendar's state. Everything the slots read comes from here.
 * @property labels Text the slots render.
 * @property startDayOfWeek Day each week begins on.
 * @property coroutineScope Scope the navigation functions launch in. Tied to the calendar's
 *   composition, so a scroll is cancelled when the calendar leaves.
 */
@Stable
public class CalendarScope internal constructor(
    public val state: CalendarState,
    public val labels: CalendarLabels,
    public val startDayOfWeek: DayOfWeek,
    private val coroutineScope: CoroutineScope,
) {
    /** Months the calendar can reach. */
    public val bounds: CalendarBounds get() = state.bounds

    /** Month currently named by the header. */
    public val visibleMonth: YearMonth get() = state.visibleMonth

    /** Whether the month picker is showing in place of the grid. */
    public val isMonthPickerExpanded: Boolean get() = state.isMonthPickerExpanded

    /** Whether an earlier month exists. */
    public val canGoBack: Boolean get() = state.canScrollBackward

    /** Whether a later month exists. */
    public val canGoForward: Boolean get() = state.canScrollForward

    /**
     * Days drawn as chosen. Written by the calendar after each composition and read only to place
     * the tab stop.
     */
    internal var selection: CalendarSelection by mutableStateOf(CalendarSelection.Empty)

    /** Day the keyboard last rested on, or `null` before it has rested anywhere. */
    internal var activeDate: LocalDate? by mutableStateOf(null)

    /**
     * Day whose cell should take focus as soon as it is composed, or `null` when none is waiting.
     *
     * A move that leaves the month pages the calendar first, so the target cell does not exist yet
     * when the key is pressed; the cell picks the request up once its page is composed.
     */
    internal var pendingFocus: LocalDate? by mutableStateOf(null)

    /**
     * Derivation behind [tabStop], recomputed when the visible month, the keyboard's resting day,
     * the selection, today or the bounds change.
     */
    private val tabStopState = derivedStateOf {
        tabStopIn(
            month = state.visibleMonth,
            active = activeDate,
            selection = selection,
            today = state.today,
            bounds = state.bounds,
        )
    }

    /** The one day of the visible month that Tab focuses. */
    internal val tabStop: LocalDate by tabStopState

    /** Animates to the next month. Does nothing at the end of the range. */
    public fun nextMonth() {
        coroutineScope.launch { state.animateToNextMonth() }
    }

    /** Animates to the previous month. Does nothing at the start of the range. */
    public fun previousMonth() {
        coroutineScope.launch { state.animateToPreviousMonth() }
    }

    /**
     * Animates to a month, clamped into [bounds].
     *
     * @param month Month to show.
     */
    public fun goTo(month: YearMonth) {
        coroutineScope.launch { state.animateTo(month) }
    }

    /**
     * Jumps to a month without animating, clamped into [bounds].
     *
     * What the month picker uses. Animating there would page the grid through every month in
     * between, and the grid is not on screen while the picker is open — so the animation is both
     * invisible and, because each month it passes through is published as the visible one, a stream
     * of changes the picker would read as somebody else moving the calendar.
     *
     * @param month Month to show.
     */
    public fun jumpTo(month: YearMonth) {
        coroutineScope.launch { state.scrollTo(month) }
    }

    /** Animates to the month containing today. */
    public fun goToToday() {
        coroutineScope.launch { state.animateToToday() }
    }

    /** Swaps between the day grid and the month picker. */
    public fun toggleMonthPicker() {
        state.toggleMonthPicker()
    }

    /** Shows the day grid again. */
    public fun collapseMonthPicker() {
        state.collapseMonthPicker()
    }

    /**
     * Moves keyboard focus from one day to the day a key asks for.
     *
     * The target becomes the tab stop at once. When it lies in another month the calendar pages
     * there — animated for a neighbouring month, a jump for anything further — and the target's
     * cell takes focus once its page is composed.
     *
     * @param from Day that holds focus.
     * @param move Step the key asks for.
     */
    internal fun moveFocus(from: LocalDate, move: CalendarMove) {
        val target = from.moved(move, startDayOfWeek, state.bounds)
        if (target == from) return
        activeDate = target
        pendingFocus = target
        val distance = state.visibleMonth.monthsUntil(target.yearMonth)
        if (distance == 0) return
        coroutineScope.launch {
            if (abs(distance) == 1) {
                state.animateTo(target.yearMonth)
            } else {
                state.scrollTo(target.yearMonth)
            }
        }
    }
}

/**
 * A swipeable month grid.
 *
 * Selection is the caller's: the calendar reports taps through [onDateClick] and draws the
 * [selection] it is handed back. That is what lets a screen apply rules the calendar knows nothing
 * about — a cap on how many days may be chosen, a minimum stay, refusing to clear the last one —
 * without racing internal state.
 *
 * Reaching a distant month is two gestures rather than several hundred: the month in the header
 * opens a [MonthYearPicker] in place of the grid, and the back and forward chevrons fade away while
 * it is showing. The picker is measured to the exact height of the grid it replaces, and the bands
 * that fade keep their space, so opening it does not resize the calendar or shift what sits below.
 *
 * Three parameters shape what exists, and they are not interchangeable.
 * [CalendarBounds][CalendarState.bounds] decides which months can be reached at all — days outside
 * it are never paged to, and the edge days of a partial first or last month are drawn unavailable.
 * [dateRule] decides which of the drawn days accept a tap; a day it refuses is still shown, greyed
 * and announced as unavailable, which is how the user learns it exists. [showAdjacentMonths]
 * decides only whether the cells around the edges carry their real dates or stay blank.
 *
 * To assistive technology the grid is a table of weeks and weekdays, and each day reads its full
 * date, weekday included. The grid is one tab stop: Tab lands on the chosen day, else today, else
 * the first day of the month; the arrow keys move by day and week, Home and End to the ends of the
 * week, Page Up and Page Down by month, and with Shift by year, paging the calendar when they leave
 * the month. Each month the calendar settles on is announced. The built-in cells, [CalendarDay] and
 * [dev.voir.areca.datetime.calendar.range.DateRangeDay], carry the grid position and the keyboard
 * behaviour, so a custom [day] slot keeps both by drawing through one of them.
 *
 * For a read-only calendar, pass `null` for [onDateClick]. The grid still pages and still announces
 * its days; nothing is selectable or focusable.
 *
 * @param modifier Modifier applied to the calendar.
 * @param state Which month is showing, and the range it may move in. Hoist it to drive the calendar
 *   from outside.
 * @param selection Days drawn as chosen.
 * @param onDateClick Called with the day the user tapped. `null` makes the calendar read-only.
 * @param dateRule Decides which days accept a tap. Combined with the state's bounds, which always
 *   apply.
 * @param startDayOfWeek Day each week begins on. Defaults to the week start of
 *   [DateTimeLocale.Current].
 * @param labels Text the calendar renders. Defaults to English.
 * @param showAdjacentMonths Whether the cells before the 1st and after the last carry the
 *   neighbouring months' dates rather than staying blank.
 * @param fixedWeekCount Whether every month is laid out over six rows, so paging between a five-row
 *   and a six-row month does not resize the calendar.
 * @param style Colors. Defaults to [LocalCalendarStyle], or [CalendarStyle.Default].
 * @param size Dimensions and type. Defaults to [LocalCalendarSize], or [CalendarSize.Default].
 * @param header The band above the grid. `null` removes it — for a calendar navigated entirely from
 *   elsewhere.
 * @param weekdayHeader The Mo/Tu/We row. `null` removes it. Fades out — rather than being removed —
 *   while the month picker is open, so the calendar keeps one height.
 * @param day One cell. Called for every drawn day of every composed page.
 */
@Composable
public fun Calendar(
    modifier: Modifier = Modifier,
    state: CalendarState = rememberCalendarState(),
    selection: CalendarSelection = CalendarSelection.Empty,
    onDateClick: ((LocalDate) -> Unit)? = null,
    dateRule: CalendarDateRule = CalendarDateRule.All,
    startDayOfWeek: DayOfWeek = DateTimeLocale.Current.firstDayOfWeek,
    labels: CalendarLabels = CalendarLabels.English,
    showAdjacentMonths: Boolean = false,
    fixedWeekCount: Boolean = true,
    style: CalendarStyle = LocalCalendarStyle.current ?: CalendarStyle.Default,
    size: CalendarSize = LocalCalendarSize.current ?: CalendarSize.Default,
    header: (@Composable CalendarScope.() -> Unit)? = { MonthHeader() },
    weekdayHeader: (@Composable CalendarScope.() -> Unit)? = { WeekdayHeader() },
    day: @Composable CalendarScope.(CalendarDayState) -> Unit = { dayState ->
        CalendarDay(
            day = dayState,
            onClick = onDateClick?.let { click -> { click(dayState.date) } },
        )
    },
) {
    val coroutineScope = rememberCoroutineScope()
    val scope = remember(state, labels, startDayOfWeek, coroutineScope) {
        CalendarScope(
            state = state,
            labels = labels,
            startDayOfWeek = startDayOfWeek,
            coroutineScope = coroutineScope,
        )
    }
    SideEffect { scope.selection = selection }
    val motion = ArecaTheme.motion

    MonthChangeAnnouncement(state = state, labels = labels)

    CompositionLocalProvider(
        LocalCalendarSize provides size,
        LocalCalendarStyle provides style,
    ) {
        Column(modifier = modifier) {
            header?.let { slot ->
                Box(modifier = Modifier.padding(bottom = size.headerSpacing)) { scope.slot() }
            }

            // The weekday row stays laid out while the picker is open and only fades, so the
            // calendar keeps one height. Removing it would shorten the calendar by a row the moment
            // the picker opens, which is the jump this whole measurement exists to avoid.
            val weekdayAlpha by animateFloatAsState(
                targetValue = if (state.isMonthPickerExpanded) 0f else 1f,
                animationSpec = motion.medium.tween(motion.standard),
                label = "calendarWeekdayAlpha",
            )
            weekdayHeader?.let { slot ->
                Box(
                    modifier = Modifier
                        .alpha(weekdayAlpha)
                        .padding(bottom = size.weekdaySpacing)
                        // Faded out it is decoration, not content; a screen reader should not read
                        // seven weekday names over a wheel of months.
                        .thenIf(state.isMonthPickerExpanded) { clearAndSetSemantics { } },
                ) {
                    scope.slot()
                }
            }

            BoxWithConstraints {
                // Day cells are square and split the width evenly, so the grid's height follows from
                // the calendar's width. Computing it here lets the picker be given exactly that
                // height, which is what stops the calendar resizing when it opens.
                val layoutDirection = LocalLayoutDirection.current
                val gutters = size.contentPadding.calculateStartPadding(layoutDirection) +
                    size.contentPadding.calculateEndPadding(layoutDirection)
                val weeks = if (fixedWeekCount) {
                    MAX_WEEKS_IN_MONTH
                } else {
                    state.visibleMonth.weekCount(startDayOfWeek)
                }
                val cellSize = ((maxWidth - gutters) / DAYS_IN_WEEK).coerceAtLeast(0.dp)
                val bodyHeight = cellSize * weeks + size.weekSpacing * (weeks - 1)

                AnimatedContent(
                    targetState = state.isMonthPickerExpanded,
                    transitionSpec = {
                        val spec = motion.medium.tween<Float>(motion.standard)
                        fadeIn(spec) togetherWith fadeOut(spec)
                    },
                    label = "calendarBody",
                ) { pickingMonth ->
                    if (pickingMonth) {
                        MonthYearPicker(
                            month = state.visibleMonth,
                            onMonthChange = scope::jumpTo,
                            bounds = state.bounds,
                            labels = labels,
                            style = style.monthPicker,
                            // Stretch the rows to fill the grid's height rather than keeping the
                            // token row height, which would leave the picker shorter or taller than
                            // what it replaced.
                            size = size.monthPicker.copy(
                                itemHeight = bodyHeight / size.monthPicker.visibleItemsCount,
                            ),
                            modifier = Modifier.padding(
                                start = size.contentPadding.calculateStartPadding(layoutDirection),
                                end = size.contentPadding.calculateEndPadding(layoutDirection),
                            ),
                        )
                    } else {
                        MonthPager(
                            scope = scope,
                            selection = selection,
                            dateRule = dateRule,
                            showAdjacentMonths = showAdjacentMonths,
                            fixedWeekCount = fixedWeekCount,
                            size = size,
                            day = day,
                        )
                    }
                }
            }
        }
    }
}

/**
 * Announces each month the grid settles on, the way a sighted user sees the header change.
 *
 * Silent for the month the calendar opens on, and while the month picker is open: the picker's
 * wheels already read out every month and year they land on.
 *
 * @param state Calendar whose settled month is announced.
 * @param labels Source of the month's wording.
 */
@Composable
private fun MonthChangeAnnouncement(state: CalendarState, labels: CalendarLabels) {
    var announcement by remember(state) { mutableStateOf<String?>(null) }
    LaunchedEffect(state, labels) {
        snapshotFlow { state.settledMonth }
            .drop(1)
            .collect { month ->
                if (!state.isMonthPickerExpanded) announcement = labels.monthHeader(month)
            }
    }
    AnnounceOnChange(announcement)
}

/**
 * The swipeable grid itself.
 *
 * Split out of [Calendar] so the body's two branches read as one line each, and so the per-page work
 * — laying a month out into weeks — sits next to the loop that consumes it.
 *
 * Each page computes enablement and adjacency against **its own** month rather than the calendar's
 * visible one. A pager composes the pages either side of the one on screen, so a cell that asked
 * "which month is showing?" would grey the wrong days on every neighbour and throughout every swipe.
 *
 * Each page is a table to assistive technology: its rows are weeks and its columns weekdays, and
 * every built-in cell states its row and column.
 *
 * @param scope Slot receiver, carrying the state the cells read.
 * @param selection Days drawn as chosen.
 * @param dateRule Decides which days accept a tap.
 * @param showAdjacentMonths Whether edge cells carry their real dates.
 * @param fixedWeekCount Whether every month is laid out over six rows.
 * @param size Dimensions of the grid.
 * @param day Cell slot.
 */
@Composable
private fun MonthPager(
    scope: CalendarScope,
    selection: CalendarSelection,
    dateRule: CalendarDateRule,
    showAdjacentMonths: Boolean,
    fixedWeekCount: Boolean,
    size: CalendarSize,
    day: @Composable CalendarScope.(CalendarDayState) -> Unit,
) {
    val state = scope.state
    HorizontalPager(
        state = state.pagerState,
        modifier = Modifier.fillMaxWidth(),
        contentPadding = size.contentPadding,
        pageSpacing = size.monthSpacing,
        beyondViewportPageCount = 1,
        verticalAlignment = Alignment.Top,
    ) { page ->
        val pageMonth = remember(page, state.bounds) { state.bounds.monthAt(page) }
        val weeks = remember(pageMonth, scope.startDayOfWeek, showAdjacentMonths, fixedWeekCount) {
            pageMonth.calendarWeeks(
                startDayOfWeek = scope.startDayOfWeek,
                showAdjacentMonths = showAdjacentMonths,
                fixedWeekCount = fixedWeekCount,
            )
        }

        Column(
            modifier = Modifier.semantics {
                collectionInfo = CollectionInfo(rowCount = weeks.size, columnCount = DAYS_IN_WEEK)
            },
            verticalArrangement = Arrangement.spacedBy(size.weekSpacing),
        ) {
            weeks.forEach { week ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    week.forEach { date ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f),
                        ) {
                            if (date != null) {
                                scope.day(
                                    CalendarDayState(
                                        date = date,
                                        month = pageMonth,
                                        isAdjacent = date.yearMonth != pageMonth,
                                        isToday = date == state.today,
                                        isSelected = date in selection,
                                        isEnabled = date in state.bounds &&
                                            dateRule.isEnabled(date),
                                    ),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
