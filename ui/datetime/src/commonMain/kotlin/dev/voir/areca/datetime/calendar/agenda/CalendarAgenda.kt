package dev.voir.areca.datetime.calendar.agenda

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.constrainHeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import dev.voir.areca.datetime.DateTimeLocale
import dev.voir.areca.datetime.calendar.Calendar
import dev.voir.areca.datetime.calendar.CalendarDateRule
import dev.voir.areca.datetime.calendar.CalendarLabels
import dev.voir.areca.datetime.calendar.CalendarMarker
import dev.voir.areca.datetime.calendar.CalendarSelection
import dev.voir.areca.datetime.calendar.CalendarSize
import dev.voir.areca.datetime.calendar.CalendarState
import dev.voir.areca.datetime.calendar.CalendarStyle
import dev.voir.areca.datetime.calendar.LocalCalendarSize
import dev.voir.areca.datetime.calendar.LocalCalendarStyle
import dev.voir.areca.datetime.calendar.MonthHeader
import dev.voir.areca.datetime.calendar.monthGridHeight
import dev.voir.areca.datetime.calendar.weekCount
import dev.voir.areca.datetime.calendar.weekRowHeight
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.button.ButtonSize
import dev.voir.areca.foundation.button.ButtonStyle
import dev.voir.areca.indication.TouchIndication
import dev.voir.areca.modifier.clickCursor
import dev.voir.areca.modifier.thenIf
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.RaisedSurface
import dev.voir.areca.theme.tween
import dev.voir.areca.window.WidthClass
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.YearMonth
import kotlinx.datetime.yearMonth
import kotlin.math.roundToInt

/**
 * What a day's heading and rows in a [CalendarAgenda] can see.
 *
 * @property date The day.
 * @property isSelected Whether the day is the agenda's selected day: draw its heading in the brand's
 *   ink so the list shows which day the calendar is on.
 * @property isToday Whether the day is the calendar's today.
 * @property isEmpty Whether this is the placeholder group of a selected day with no entries, drawn
 *   by the agenda's `emptyDay` slot.
 */
@Stable
public class CalendarAgendaDayScope internal constructor(
    public val date: LocalDate,
    public val isSelected: Boolean,
    public val isToday: Boolean,
    public val isEmpty: Boolean,
)

/**
 * A month calendar joined to a long list of days — what is planned on each day, under the month it
 * falls in — kept in step both ways.
 *
 * The list is the caller's: [days] says which days have entries, and [dayHeading] and [dayContent]
 * draw each day's heading and rows. The agenda draws no rows of its own. It lays the days out in date
 * order, pins the heading of the day at the top while its rows scroll, and keeps the calendar and the
 * list together:
 *
 * - **Tapping a day** calls [onSelect] and scrolls its group to the top of the list, flashing its
 *   heading; the calendar stays as it is. While that scroll runs, scrolling does not pick days. A day without entries scrolls to the next day that
 *   has some, or, with [emptyDay], gets a group of its own while it is selected.
 * - **Scrolling the list** picks the day whose heading is pinned at the top through [onSelect], and
 *   the calendar follows it to its week and month.
 * - **On a compact window** the calendar sits above the list and collapses with it, the way a
 *   collapsing top bar does: scrolling the list up first folds the month to the selected week, pixel
 *   for pixel under the finger, and only then scrolls the rows; pulling down at the top of the list
 *   opens the month again. Let go part way and it finishes the way the finger was going. The
 *   [legend] folds away with the month, a shadow lifts the calendar off the rows scrolled under it,
 *   and the chevrons step a week. The handle under the calendar folds or opens it with a tap.
 * - **From a medium window** (`ArecaTheme.window.width >= WidthClass.Medium`, the pane's class
 *   inside a `WindowClassBox`) the calendar is a card of [CalendarAgendaSize.calendarPaneWidth] beside
 *   a card holding the list; it never folds, has no handle, and the headings pin at the list's top.
 * - **A "Today" pill** appears beside the title once the selection or the month leaves today, and
 *   brings both back.
 *
 * The calendar is a [Calendar] laid out over only the weeks each month has, at [calendarSize]
 * (compact by default); [CalendarState.bounds][dev.voir.areca.datetime.calendar.CalendarState.bounds]
 * on [state] decide which days exist, so pass the window the list covers. Selection is hoisted:
 * [selected] is drawn, and every change — a tap, a scroll, a week step, Today — arrives through
 * [onSelect]. Motion follows `ArecaTheme.motion`; under reduced motion the fold snaps and scrolls
 * jump.
 *
 * Folding and scrolling recompose nothing: the fold moves in layout, and a new selection recomposes
 * only the two days whose [CalendarAgendaDayScope.isSelected] changed. That holds while [dayHeading],
 * [dayContent] and [emptyDay] stay the same lambdas between recompositions — define them in the
 * screen's composable, where Compose remembers them, and read changing data through each day's
 * scope or the caller's own state rather than capturing it.
 *
 * @param days Days that have entries. Sorted and made distinct by the agenda; keep it to the days in
 *   the calendar's bounds.
 * @param selected The selected day, drawn in the calendar and passed to each day's scope.
 * @param onSelect Called with the day the user picked, by any means.
 * @param dayHeading A day's heading: its date, and a total if the app shows one. The agenda draws the
 *   opaque band behind it, pins it, and flashes it; draw the words in the brand's ink when
 *   [CalendarAgendaDayScope.isSelected].
 * @param modifier Modifier applied to the agenda, which fills the space it is given.
 * @param state Scroll and fold positions, and the calendar's bounds. Hoist it to drive the agenda.
 * @param markers What each day of the calendar shows under its number, such as a
 *   [CalendarMarker.Dots] per kind of entry. Keep it a map lookup.
 * @param dateRule Days of the calendar that accept a tap, on top of the bounds.
 * @param startDayOfWeek Day each week begins on.
 * @param labels Words of the calendar, the "Today" pill included.
 * @param agendaLabels Words the agenda adds: the handle's descriptions.
 * @param calendarStyle Colours of the calendar.
 * @param calendarSize Dimensions of the calendar. Defaults to [LocalCalendarSize], or
 *   [CalendarSize.Compact].
 * @param style Grounds, heading highlight and handle. Defaults to [LocalCalendarAgendaStyle], or
 *   [CalendarAgendaStyle.Default].
 * @param size Card corners, heading inset, handle and panes. Defaults to [LocalCalendarAgendaSize],
 *   or [CalendarAgendaSize.Default].
 * @param contentPadding Inset around the list's content. Headings pin at the list's top edge.
 * @param legend Drawn under the calendar's grid — usually a
 *   [dev.voir.areca.datetime.calendar.CalendarLegend] — and folded away with the month.
 * @param monthSeparator Drawn before the first day of each month in the list, such as the month's
 *   name. `null` for none.
 * @param emptyDay Rows of the placeholder group a selected day with no entries gets, such as
 *   "Nothing planned". `null` gives such a day no group; a tap on it scrolls to the next day.
 * @param dayContent A day's rows.
 */
@Composable
public fun CalendarAgenda(
    days: List<LocalDate>,
    selected: LocalDate,
    onSelect: (LocalDate) -> Unit,
    dayHeading: @Composable CalendarAgendaDayScope.() -> Unit,
    modifier: Modifier = Modifier,
    state: CalendarAgendaState = rememberCalendarAgendaState(),
    markers: ((LocalDate) -> CalendarMarker?)? = null,
    dateRule: CalendarDateRule = CalendarDateRule.All,
    startDayOfWeek: DayOfWeek = DateTimeLocale.Current.firstDayOfWeek,
    labels: CalendarLabels = CalendarLabels.English,
    agendaLabels: CalendarAgendaLabels = CalendarAgendaLabels.English,
    calendarStyle: CalendarStyle = LocalCalendarStyle.current ?: CalendarStyle.Default,
    calendarSize: CalendarSize = LocalCalendarSize.current ?: CalendarSize.Compact,
    style: CalendarAgendaStyle = LocalCalendarAgendaStyle.current ?: CalendarAgendaStyle.Default,
    size: CalendarAgendaSize = LocalCalendarAgendaSize.current ?: CalendarAgendaSize.Default,
    contentPadding: PaddingValues = PaddingValues(),
    legend: (@Composable () -> Unit)? = null,
    monthSeparator: (@Composable (YearMonth) -> Unit)? = null,
    emptyDay: (@Composable CalendarAgendaDayScope.() -> Unit)? = null,
    dayContent: @Composable CalendarAgendaDayScope.() -> Unit,
) {
    val calendar = state.calendar
    val list = state.list
    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current
    val motion = ArecaTheme.motion
    val reduceMotion = motion.reduceMotion
    val coroutineScope = rememberCoroutineScope()

    val listBg = style.listBg.takeOrElse { ArecaTheme.surface.raised }
    val calendarBg = style.calendarBg.takeOrElse { ArecaTheme.surface.raised }
    val headingBg = style.headingBg.takeOrElse { listBg }
    // From a medium window the calendar is a card beside the list and never folds; on a compact one
    // it sits above the list and collapses as the list scrolls.
    val wide = ArecaTheme.window.width >= WidthClass.Medium
    val panelShape = if (wide) size.shape else size.shape.topOnly()

    val sortedDays = remember(days) { days.distinct().sorted() }
    val placeholder = selected.takeIf { emptyDay != null && it in calendar.bounds }
    val entries = remember(sortedDays, placeholder, monthSeparator != null) {
        agendaEntries(sortedDays, placeholder, monthSeparators = monthSeparator != null)
    }
    val groupDays = remember(entries) { entries.groupDays() }
    // Items read the selection through this state, each deriving its own flag from it, so a new
    // selection recomposes the two headings whose flag changed rather than every visible item.
    val selectedState = rememberUpdatedState(selected)
    val currentOnSelect by rememberUpdatedState(onSelect)

    // The calendar follows the selected day to its week and month.
    LaunchedEffect(state, selected) {
        state.syncedAnchor = selected.coerceIn(calendar.bounds.minDate, calendar.bounds.maxDate)
        calendar.animateFoldTo(selected)
    }

    // A week stepped with the chevrons, or today's week, picks that day and scrolls to it.
    LaunchedEffect(state) {
        snapshotFlow { calendar.foldAnchor }.collect { anchor ->
            val synced = state.syncedAnchor
            if (synced != null && anchor != synced) {
                state.syncedAnchor = anchor
                currentOnSelect(anchor)
                state.requestScrollToDay(anchor)
            }
        }
    }

    // A requested scroll: the day's heading to the top of the list, or the list's top for the first
    // group. One scroll, by index, so it lands exactly wherever the calendar's edge is.
    val request = state.scrollRequest
    LaunchedEffect(request, entries, selected) {
        val asked = request ?: return@LaunchedEffect
        var index = entries.headingIndexOf(asked.date)
        // The placeholder of a newly picked day arrives once the caller has selected it.
        val awaitingPlaceholder = index < 0 && emptyDay != null && asked.date in calendar.bounds && selected != asked.date
        if (awaitingPlaceholder) return@LaunchedEffect
        if (index < 0) index = entries.scrollTargetFor(asked.date)
        if (index < 0) {
            state.scrollRequest = null
            return@LaunchedEffect
        }
        state.scrollingToDay = true
        state.highlight = ScrollRequest(entries[index].date ?: asked.date, asked.id)
        try {
            val target = if (index <= entries.firstHeadingIndex()) 0 else index
            if (reduceMotion) list.scrollToItem(target) else list.animateScrollToItem(target)
        } finally {
            state.scrollingToDay = false
            if (state.scrollRequest == asked) state.scrollRequest = null
        }
    }

    // Scrolling picks the day whose heading is pinned at the top. The first visible index changes
    // only as an item leaves the top, so this runs once per item, not once per frame.
    LaunchedEffect(state, entries, groupDays) {
        val firstDay = entries.firstNotNullOfOrNull { it.date }
        snapshotFlow { groupDays.getOrNull(list.firstVisibleItemIndex) ?: firstDay }.collect { day ->
            val userScrolling = list.isScrollInProgress && !state.scrollingToDay
            if (userScrolling && day != null && day != selectedState.value) currentOnSelect(day)
        }
    }

    // The fold follows the list on a compact window, as a collapsing header does.
    val metrics = remember { FoldMetrics() }
    val laneHeight = if (markers != null) calendarSize.markerLaneHeight else 0.dp
    val gutters = calendarSize.contentPadding.calculateStartPadding(layoutDirection) +
        calendarSize.contentPadding.calculateEndPadding(layoutDirection)
    val weekSpacingPx = with(density) { calendarSize.weekSpacing.roundToPx() }
    val collapse = remember(calendar, density, gutters, calendarSize, laneHeight, startDayOfWeek) {
        CalendarCollapse(
            calendar = calendar,
            foldRange = {
                // How far the calendar block shrinks from the month to the week: the grid's rows but
                // one, and the legend.
                val row = density.weekRowHeight(metrics.width, gutters, calendarSize, laneHeight)
                val weeks = calendar.visibleMonth.weekCount(startDayOfWeek)
                val month = monthGridHeight(weeks, weeks, 0f, row, weekSpacingPx)
                (month - row + metrics.legendHeight).toFloat()
            },
            onFoldStart = {
                // The fold shows the selected day's week, so the pager must be on that day's month.
                coroutineScope.launch { calendar.animateFoldTo(calendar.foldAnchor) }
            },
        )
    }
    val settleSpec: AnimationSpec<Float> = if (reduceMotion) snap() else motion.medium.tween(motion.standard)
    LaunchedEffect(state, wide, settleSpec) {
        if (wide) {
            calendar.snapFoldTo(0f)
            return@LaunchedEffect
        }
        // Let go part way, the fold finishes the way the finger was going.
        snapshotFlow { list.isScrollInProgress }.collect { scrolling ->
            val fraction = calendar.foldFraction
            if (!scrolling && fraction > 0f && fraction < 1f && !calendar.isFoldAnimating) {
                launch { if (collapse.folding) calendar.fold(animationSpec = settleSpec) else calendar.unfold(settleSpec) }
            }
        }
    }
    val toggleSpec: AnimationSpec<Float> = if (reduceMotion) snap() else motion.slow.tween(motion.standard)
    val onToggleFold: () -> Unit = remember(calendar, coroutineScope, toggleSpec) {
        {
            coroutineScope.launch {
                if (calendar.isFolded) calendar.unfold(toggleSpec) else calendar.fold(animationSpec = toggleSpec)
            }
        }
    }

    // The flash on a heading a tap scrolled to.
    val flash = remember(state) { Animatable(0f) }
    val highlight = state.highlight
    LaunchedEffect(highlight, reduceMotion) {
        if (highlight == null) return@LaunchedEffect
        flash.snapTo(1f)
        delay(motion.slow)
        if (reduceMotion) flash.snapTo(0f) else flash.animateTo(0f, motion.deliberate.tween(motion.fade))
    }
    val flashFor: (LocalDate) -> Float = remember(state, flash) {
        { date -> if (state.highlight?.date == date) flash.value else 0f }
    }

    val onDateClick: (LocalDate) -> Unit = remember(state) {
        { date ->
            currentOnSelect(date)
            state.requestScrollToDay(date)
        }
    }
    val itemFade = remember(motion) { if (motion.reduceMotion) null else motion.slow.tween<Float>(motion.fade) }
    val today = calendar.today

    val agendaList: @Composable (Modifier) -> Unit = { listModifier ->
        LazyColumn(state = list, modifier = listModifier, contentPadding = contentPadding) {
            entries.forEach { entry ->
                when (entry) {
                    is AgendaMonth -> item(key = entry.key, contentType = "month") {
                        Box(modifier = Modifier.fillMaxWidth().agendaItemMotion(this, itemFade)) {
                            monthSeparator?.invoke(entry.month)
                        }
                    }
                    is AgendaHeading -> stickyHeader(key = entry.key, contentType = "heading") {
                        DayHeadingBand(
                            ground = headingBg,
                            highlight = style.highlight,
                            flash = { flashFor(entry.date) },
                            padding = size.headingPadding,
                            modifier = Modifier.agendaItemMotion(this, itemFade),
                        ) {
                            AgendaDay(entry.date, entry.isEmpty, selectedState, today, dayHeading)
                        }
                    }
                    is AgendaBody -> item(key = entry.key, contentType = "body") {
                        Box(modifier = Modifier.fillMaxWidth().agendaItemMotion(this, itemFade)) {
                            if (entry.isEmpty) {
                                emptyDay?.let { AgendaDay(entry.date, true, selectedState, today, it) }
                            } else {
                                AgendaDay(entry.date, false, selectedState, today, dayContent)
                            }
                        }
                    }
                }
            }
        }
    }

    val calendarBlock: @Composable (Modifier) -> Unit = { blockModifier ->
        CalendarBlock(
            state = state,
            selected = selected,
            onDateClick = onDateClick,
            onToggleFold = if (wide) null else onToggleFold,
            metrics = metrics,
            lifted = !wide,
            panelShape = panelShape,
            ground = calendarBg,
            markers = markers,
            dateRule = dateRule,
            startDayOfWeek = startDayOfWeek,
            labels = labels,
            agendaLabels = agendaLabels,
            calendarStyle = calendarStyle,
            calendarSize = calendarSize,
            style = style,
            size = size,
            legend = legend,
            modifier = blockModifier,
        )
    }

    if (wide) {
        // A calendar card beside a list card. The calendar scrolls on its own when the window is too
        // short for it; the list's rows scroll under its rounded corners.
        Row(
            modifier = modifier.fillMaxSize().padding(size.paneSpacing),
            horizontalArrangement = Arrangement.spacedBy(size.paneSpacing),
            verticalAlignment = Alignment.Top,
        ) {
            Box(modifier = Modifier.width(size.calendarPaneWidth).verticalScroll(rememberScrollState())) {
                RaisedSurface {
                    calendarBlock(Modifier.fillMaxWidth().padding(bottom = size.paneSpacing / 2))
                }
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(panelShape)
                    .background(listBg, panelShape),
            ) {
                RaisedSurface { agendaList(Modifier.fillMaxSize()) }
            }
        }
    } else {
        // The calendar above the list on one card running to the bottom edge. The list starts at the
        // calendar's edge, so the two cannot drift apart as the calendar folds.
        Box(modifier = modifier.fillMaxSize().background(listBg, panelShape)) {
            RaisedSurface {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Above the list, so its shadow falls on the rows.
                    calendarBlock(Modifier.fillMaxWidth().zIndex(1f))
                    agendaList(Modifier.fillMaxWidth().weight(1f).nestedScroll(collapse))
                }
            }
        }
    }
}

/**
 * Draws [content] for one day of the list, with a scope whose [CalendarAgendaDayScope.isSelected]
 * is derived here, so only the days whose flag changes recompose when the selection moves.
 *
 * @param date The day.
 * @param isEmpty Whether it is the placeholder group.
 * @param selected The agenda's selected day.
 * @param today The calendar's today.
 * @param content The caller's heading or rows.
 */
@Composable
private fun AgendaDay(
    date: LocalDate,
    isEmpty: Boolean,
    selected: State<LocalDate>,
    today: LocalDate,
    content: @Composable CalendarAgendaDayScope.() -> Unit,
) {
    val isSelected by remember(date, selected) { derivedStateOf { selected.value == date } }
    val scope = remember(date, isSelected, isEmpty, today) {
        CalendarAgendaDayScope(date = date, isSelected = isSelected, isToday = date == today, isEmpty = isEmpty)
    }
    scope.content()
}

/**
 * The calendar card: the month, its legend, and on a phone the fold handle and the shadow over the
 * rows scrolled under it.
 *
 * Only the ends of a fold recompose it; while the fold moves, the calendar, the legend and the
 * shadow change in layout and drawing.
 *
 * @param state The agenda's state.
 * @param selected The selected day.
 * @param onDateClick Called with a tapped day.
 * @param onToggleFold Folds or opens the calendar from the handle; `null` for no handle.
 * @param metrics Where the block reports its width and its legend's height.
 * @param lifted Whether the block casts a shadow once the list scrolls under it.
 * @param panelShape The card's shape.
 * @param ground The card's fill.
 * @param markers What each day shows under its number.
 * @param dateRule Days that accept a tap.
 * @param startDayOfWeek Day each week begins on.
 * @param labels Words of the calendar.
 * @param agendaLabels Words of the handle.
 * @param calendarStyle Colours of the calendar.
 * @param calendarSize Dimensions of the calendar.
 * @param style The agenda's colours.
 * @param size The agenda's dimensions.
 * @param legend Drawn under the grid and folded away with the month.
 * @param modifier Modifier applied to the block.
 */
@Composable
private fun CalendarBlock(
    state: CalendarAgendaState,
    selected: LocalDate,
    onDateClick: (LocalDate) -> Unit,
    onToggleFold: (() -> Unit)?,
    metrics: FoldMetrics,
    lifted: Boolean,
    panelShape: Shape,
    ground: Color,
    markers: ((LocalDate) -> CalendarMarker?)?,
    dateRule: CalendarDateRule,
    startDayOfWeek: DayOfWeek,
    labels: CalendarLabels,
    agendaLabels: CalendarAgendaLabels,
    calendarStyle: CalendarStyle,
    calendarSize: CalendarSize,
    style: CalendarAgendaStyle,
    size: CalendarAgendaSize,
    legend: (@Composable () -> Unit)?,
    modifier: Modifier,
) {
    val calendar = state.calendar
    val motion = ArecaTheme.motion
    val reduceMotion = motion.reduceMotion
    val lift by animateFloatAsState(
        targetValue = if (lifted && state.list.canScrollBackward) 1f else 0f,
        animationSpec = motion.fast.tween(motion.standard),
        label = "calendarAgendaLift",
    )
    val liftPx = with(LocalDensity.current) { size.lift.toPx() }
    Column(
        modifier = modifier
            .onSizeChanged { metrics.width = it.width }
            .graphicsLayer {
                shadowElevation = liftPx * lift
                shape = panelShape
                ambientShadowColor = style.lift
                spotShadowColor = style.lift
            }
            .background(ground, panelShape),
    ) {
        Calendar(
            state = calendar,
            selection = CalendarSelection.Single(selected),
            onDateClick = onDateClick,
            dateRule = dateRule,
            startDayOfWeek = startDayOfWeek,
            labels = labels,
            fixedWeekCount = false,
            style = calendarStyle,
            size = calendarSize,
            markers = markers,
            header = {
                val today = calendar.today
                val showToday = today in calendar.bounds &&
                    (selected != today || calendar.visibleMonth != today.yearMonth)
                MonthHeader(
                    actions = {
                        AnimatedVisibility(
                            visible = showToday,
                            enter = if (reduceMotion) fadeIn() else fadeIn() + scaleIn(initialScale = PillScale),
                            exit = if (reduceMotion) fadeOut() else fadeOut() + scaleOut(targetScale = PillScale),
                        ) {
                            Button(
                                text = labels.todayAction,
                                onClick = { onDateClick(today) },
                                style = ButtonStyle.soft(ActionColor.Brand, shape = ArecaTheme.shapes.full),
                                size = ButtonSize.ExtraSmall,
                            )
                        }
                    },
                )
            },
        )
        legend?.let { slot ->
            Box(
                modifier = Modifier
                    .foldAway(onOpenHeight = { metrics.legendHeight = it }) { calendar.foldFraction }
                    .thenIf(calendar.isFolded) { clearAndSetSemantics { } },
            ) { slot() }
        }
        onToggleFold?.let { toggle ->
            FoldHandle(
                modifier = Modifier,
                folded = calendar.isFolded,
                onToggle = toggle,
                labels = agendaLabels,
                color = style.handle,
                size = size,
            )
        }
    }
}

/**
 * Sizes of the calendar block the fold's range follows from, written as they are measured. Plain
 * fields: only the scroll connection reads them, between frames, so nothing recomposes for them.
 */
private class FoldMetrics {
    /** Width of the calendar block, in pixels. */
    var width: Int = 0

    /** Height of the legend open, in pixels. */
    var legendHeight: Int = 0
}

/**
 * Folds the calendar with the list's scroll, as a collapsing header does: scrolling up folds it before
 * the list moves, and scrolling down past the list's top opens it.
 *
 * @param calendar Calendar to fold.
 * @param foldRange How far the calendar block shrinks from the month to the week, in pixels.
 * @param onFoldStart Called as a fold starts from the open month.
 */
private class CalendarCollapse(
    private val calendar: CalendarState,
    private val foldRange: () -> Float,
    private val onFoldStart: () -> Unit,
) : NestedScrollConnection {
    /** Whether the last scroll that moved the fold folded rather than opened it. */
    var folding: Boolean = false
        private set

    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        if (available.y >= 0f) return Offset.Zero
        return Offset(0f, -moveFold(-available.y))
    }

    override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
        if (available.y <= 0f) return Offset.Zero
        return Offset(0f, -moveFold(-available.y))
    }

    /**
     * Moves the fold by [distance] pixels — folding for a positive one, opening for a negative one —
     * as far as it can go.
     *
     * @return The distance used, in pixels, with the sign of [distance].
     */
    private fun moveFold(distance: Float): Float {
        val range = foldRange()
        if (range <= 0f) return 0f
        val from = calendar.foldFraction
        val to = (from + distance / range).coerceIn(0f, 1f)
        if (to == from) return 0f
        if (from == 0f) onFoldStart()
        folding = distance > 0f
        calendar.snapFoldTo(to)
        return (to - from) * range
    }
}

/**
 * The opaque band behind a day's heading, which flashes the [highlight] tint while [flash] is above
 * zero.
 *
 * @param ground The band's ground.
 * @param highlight Tint of the flash.
 * @param flash Strength of the flash, `0` to `1`. Read in drawing.
 * @param padding Inset around the content.
 * @param modifier Modifier applied to the band.
 * @param content The caller's heading.
 */
@Composable
private fun DayHeadingBand(
    ground: Color,
    highlight: Color,
    flash: () -> Float,
    padding: PaddingValues,
    modifier: Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .drawBehind { drawRect(lerp(ground, highlight, flash().coerceIn(0f, 1f))) }
            .padding(padding)
            .semantics { heading() },
    ) { content() }
}

/**
 * The bar under a phone's calendar that folds it to a week and opens it again.
 *
 * @param modifier Modifier applied to the handle's band.
 * @param folded Whether the calendar is folded.
 * @param onToggle Folds or opens it.
 * @param labels What assistive technology reads for it.
 * @param color Colour of the bar.
 * @param size The bar's size and its band's height.
 */
@Composable
private fun FoldHandle(
    modifier: Modifier,
    folded: Boolean,
    onToggle: () -> Unit,
    labels: CalendarAgendaLabels,
    color: Color,
    size: CalendarAgendaSize,
) {
    val shape = ArecaTheme.shapes.full
    Box(modifier = modifier.fillMaxWidth().height(size.handleHeight), contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .width(size.handleSize.width * 2)
                .height(size.handleHeight)
                .clickCursor()
                .clickable(
                    interactionSource = null,
                    indication = TouchIndication(shape = shape),
                    role = Role.Button,
                    onClick = onToggle,
                )
                .semantics { contentDescription = if (folded) labels.showMonthAction else labels.showWeekAction },
            contentAlignment = Alignment.Center,
        ) {
            Box(modifier = Modifier.size(size.handleSize).background(color, CircleShape))
        }
    }
}

/**
 * Shrinks the content to nothing and fades it out as [fraction] goes from `0` to `1`.
 *
 * @param onOpenHeight Called with the content's full height each time it is measured.
 * @param fraction How far it is folded away. Read in layout and drawing.
 * @return Modifier laying the content out folded.
 */
private fun Modifier.foldAway(onOpenHeight: (Int) -> Unit, fraction: () -> Float): Modifier = clipToBounds().layout { measurable, constraints ->
    val placeable = measurable.measure(constraints.copy(minHeight = 0, maxHeight = Constraints.Infinity))
    onOpenHeight(placeable.height)
    val open = 1f - fraction().coerceIn(0f, 1f)
    layout(placeable.width, constraints.constrainHeight((placeable.height * open).roundToInt())) {
        placeable.placeWithLayer(0, 0) { alpha = open }
    }
}

/**
 * Fades an item in as it joins the list, such as a placeholder group.
 *
 * @param scope The item's scope.
 * @param fade The fade, or `null` for none (reduced motion).
 * @return Modifier animating the item.
 */
private fun Modifier.agendaItemMotion(scope: LazyItemScope, fade: FiniteAnimationSpec<Float>?): Modifier =
    if (fade == null) this else with(scope) { animateItem(fadeInSpec = fade, placementSpec = null, fadeOutSpec = null) }

/** Scale the "Today" pill grows from and shrinks to. */
private const val PillScale: Float = 0.8f
