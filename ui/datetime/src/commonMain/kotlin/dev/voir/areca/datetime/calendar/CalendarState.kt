package dev.voir.areca.datetime.calendar

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.spring
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import kotlinx.coroutines.Job
import kotlinx.coroutines.currentCoroutineContext
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
 * @param initiallyFolded Whether the days open folded to the week of [initialFoldAnchor].
 * @param initialFoldAnchor Day whose week a fold shows, or `null` for [today]; clamped into [bounds].
 */
@Stable
public class CalendarState internal constructor(
    public val bounds: CalendarBounds,
    public val today: LocalDate,
    initialMonth: YearMonth,
    initiallyFolded: Boolean = false,
    initialFoldAnchor: LocalDate? = null,
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

    /**
     * What the calendar body shows: the days of [visibleMonth], the months of [browsedYear], or the
     * page of years [browsedYears].
     *
     * Not saved: a calendar restored after a configuration change opens on its days.
     */
    public var level: CalendarLevel by mutableStateOf(CalendarLevel.Days)
        private set

    /**
     * Year the [CalendarLevel.Months] level lists, and the year [browsedYears] is built around.
     *
     * Set to the year of [visibleMonth] each time the calendar steps out of its days, so the months
     * open on the year being looked at; stepped by [browseBackward] and [browseForward] after that.
     */
    public var browsedYear: Int by mutableStateOf(bounds.coerce(initialMonth).year)
        private set

    /** Derivation behind [browsedYears]: the page of years holding [browsedYear]. */
    private val browsedYearsState = derivedStateOf { bounds.yearPageOf(browsedYear) }

    /**
     * Years the [CalendarLevel.Years] level lists: [YEARS_PER_PAGE] consecutive years holding
     * [browsedYear], counted from the first year of [bounds] so the first page never starts before
     * it. Years past the end of [bounds] are listed and refused.
     */
    public val browsedYears: IntRange by browsedYearsState

    /** Derivation behind [canBrowseBackward]. */
    private val canBrowseBackwardState = derivedStateOf {
        when (level) {
            CalendarLevel.Days -> false
            CalendarLevel.Months -> browsedYear > bounds.yearRange.first
            CalendarLevel.Years -> browsedYears.first > bounds.yearRange.first
        }
    }

    /**
     * Whether [browseBackward] has an earlier year (at [CalendarLevel.Months]) or page of years (at
     * [CalendarLevel.Years]) to show. Always `false` at [CalendarLevel.Days], where
     * [canScrollBackward] applies instead.
     */
    public val canBrowseBackward: Boolean by canBrowseBackwardState

    /** Derivation behind [canBrowseForward]. */
    private val canBrowseForwardState = derivedStateOf {
        when (level) {
            CalendarLevel.Days -> false
            CalendarLevel.Months -> browsedYear < bounds.yearRange.last
            CalendarLevel.Years -> browsedYears.last < bounds.yearRange.last
        }
    }

    /**
     * Whether [browseForward] has a later year or page of years to show. Always `false` at
     * [CalendarLevel.Days], where [canScrollForward] applies instead.
     */
    public val canBrowseForward: Boolean by canBrowseForwardState

    /** Backing value of [foldFraction]. */
    private var foldFractionState by mutableFloatStateOf(if (initiallyFolded) 1f else 0f)

    /**
     * How far the days are folded to the one week holding [foldAnchor]: `0` shows the month, `1`
     * the one week, and anything between part of the way, as a fold being dragged or animated.
     *
     * Changed by [fold], [unfold] and [snapFoldTo]. The calendar reads it while laying out, so
     * changing it every frame moves the grid without recomposing it; read it the same way —
     * in a layout or drawing block — to move something with the fold.
     */
    public val foldFraction: Float get() = foldFractionState

    /** Derivation behind [isFolded]. */
    private val isFoldedState = derivedStateOf { foldFractionState >= FOLDED_PAST }

    /**
     * Whether the days are folded, or more than half way to it.
     *
     * Folded, the header names the week and its back and forward step a week rather than a month.
     * Changes only as the fold crosses half way, so reading it in composition is cheap. Saved across
     * configuration change with [foldAnchor].
     */
    public val isFolded: Boolean by isFoldedState

    /** The coroutine animating the fold, cancelled by the next fold change. */
    private var foldMotion: Job? = null

    /**
     * The day whose week a folded calendar shows, always inside [bounds]. Kept while the calendar is
     * open, so folding again returns to the same week unless [fold] is given another day.
     */
    public var foldAnchor: LocalDate by mutableStateOf((initialFoldAnchor ?: today).coerceIn(bounds.minDate, bounds.maxDate))
        private set

    /** Derivation behind [canZoomOut]. */
    private val canZoomOutState = derivedStateOf {
        when (level) {
            CalendarLevel.Days -> bounds.monthCount > 1 && foldFractionState == 0f
            CalendarLevel.Months -> bounds.yearRange.first < bounds.yearRange.last
            CalendarLevel.Years -> false
        }
    }

    /**
     * Whether [zoomOut] has a wider level to step to. `false` on the years, while the days are
     * folded even part way ([foldFraction] above `0`), and on any level whose wider one would hold a single choice: the months of
     * a calendar bounded to one month, the years of one bounded to one year.
     */
    public val canZoomOut: Boolean by canZoomOutState

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

    /**
     * Folds the days to the week holding [anchor], returning to the days from the months or years
     * and showing that week's month at once if another is showing.
     *
     * Suspends until the fold has finished. A later [fold], [unfold] or [snapFoldTo] interrupts it,
     * cancelling the caller.
     *
     * @param anchor Day whose week to show, clamped into [bounds]. Defaults to the last one folded to.
     * @param animationSpec How the fold moves. Pass the theme's motion, such as
     *   `ArecaTheme.motion.slow.tween(ArecaTheme.motion.standard)`, and `snap()` under reduced motion.
     */
    public suspend fun fold(
        anchor: LocalDate = foldAnchor,
        animationSpec: AnimationSpec<Float> = spring(stiffness = Spring.StiffnessMediumLow),
    ) {
        foldAnchor = anchor.coerceIn(bounds.minDate, bounds.maxDate)
        level = CalendarLevel.Days
        if (!isOnPage(foldAnchor.yearMonth)) pagerState.scrollToPage(bounds.pageOf(foldAnchor.yearMonth))
        animateFoldFraction(1f, animationSpec)
    }

    /**
     * Opens the folded week back out to the whole month. Keeps [foldAnchor].
     *
     * Suspends until the month is open. A later [fold], [unfold] or [snapFoldTo] interrupts it,
     * cancelling the caller.
     *
     * @param animationSpec How the fold moves; see [fold].
     */
    public suspend fun unfold(animationSpec: AnimationSpec<Float> = spring(stiffness = Spring.StiffnessMediumLow)) {
        animateFoldFraction(0f, animationSpec)
    }

    /**
     * Sets how far the days are folded at once, stopping a [fold] or [unfold] in flight: what a
     * screen calls frame by frame to fold the calendar under a finger, as a collapsing header does.
     *
     * @param fraction `0` for the month to `1` for the week; clamped.
     */
    public fun snapFoldTo(fraction: Float) {
        foldMotion?.cancel()
        foldMotion = null
        if (fraction > 0f && foldFractionState == 0f) level = CalendarLevel.Days
        foldFractionState = fraction.coerceIn(0f, 1f)
    }

    /** Whether a [fold] or [unfold] is animating. */
    internal val isFoldAnimating: Boolean get() = foldMotion?.isActive == true

    /**
     * Animates [foldFraction] to [target], interrupting any fold animation already running.
     *
     * @param target `0` or `1`.
     * @param animationSpec How it moves.
     */
    private suspend fun animateFoldFraction(target: Float, animationSpec: AnimationSpec<Float>) {
        val job = currentCoroutineContext()[Job]
        foldMotion?.takeIf { it != job }?.cancel()
        foldMotion = job
        try {
            animate(initialValue = foldFractionState, targetValue = target, animationSpec = animationSpec) { value, _ ->
                foldFractionState = value
            }
        } finally {
            if (foldMotion == job) foldMotion = null
        }
    }

    /**
     * Moves the folded week to the one holding [anchor], animating to its month when it lies in
     * another. Open, it only sets the week a later [fold] shows, and pages to its month.
     *
     * Always comes to rest on a whole page: a move interrupted part way between two months — by the
     * next day picked while the pager is still sliding — is finished by the next call.
     *
     * @param anchor Day whose week to show, clamped into [bounds].
     */
    public suspend fun animateFoldTo(anchor: LocalDate) {
        foldAnchor = anchor.coerceIn(bounds.minDate, bounds.maxDate)
        if (!isOnPage(foldAnchor.yearMonth)) pagerState.animateScrollToPage(bounds.pageOf(foldAnchor.yearMonth))
    }

    /**
     * Whether the pager rests exactly on [month]'s page — not on another, and not part way between two.
     *
     * @param month Month to check.
     */
    private fun isOnPage(month: YearMonth): Boolean =
        pagerState.currentPage == bounds.pageOf(month) && pagerState.currentPageOffsetFraction == 0f

    /**
     * Steps out one level: from the days to the months of the visible month's year, from the months
     * to the page of years around it. Does nothing when [canZoomOut] is `false`.
     */
    public fun zoomOut() {
        if (!canZoomOut) return
        when (level) {
            CalendarLevel.Days -> {
                browsedYear = visibleMonth.year
                level = CalendarLevel.Months
            }
            CalendarLevel.Months -> level = CalendarLevel.Years
            CalendarLevel.Years -> Unit
        }
    }

    /** Returns to the days of [visibleMonth] without changing it — the way out of a level unchosen. */
    public fun showDays() {
        level = CalendarLevel.Days
    }

    /**
     * Chooses a year on the [CalendarLevel.Years] level: shows that year's months.
     *
     * @param year Year chosen, clamped into [bounds].
     */
    public fun selectYear(year: Int) {
        browsedYear = year.coerceIn(bounds.yearRange)
        level = CalendarLevel.Months
    }

    /**
     * Chooses a month on the [CalendarLevel.Months] level: shows its days.
     *
     * Jumps rather than animates. The days are not on screen while a month is being chosen, so an
     * animation would page invisibly through every month in between.
     *
     * @param month Month chosen, clamped into [bounds].
     */
    public fun selectMonth(month: YearMonth) {
        pagerState.requestScrollToPage(bounds.pageOf(month))
        level = CalendarLevel.Days
    }

    /**
     * Shows the previous year of months, or the previous page of years. Does nothing at
     * [CalendarLevel.Days] or when [canBrowseBackward] is `false`.
     */
    public fun browseBackward() {
        if (!canBrowseBackward) return
        browsedYear = when (level) {
            CalendarLevel.Years -> browsedYears.first - YEARS_PER_PAGE
            else -> browsedYear - 1
        }.coerceIn(bounds.yearRange)
    }

    /**
     * Shows the next year of months, or the next page of years. Does nothing at
     * [CalendarLevel.Days] or when [canBrowseForward] is `false`.
     */
    public fun browseForward() {
        if (!canBrowseForward) return
        browsedYear = when (level) {
            CalendarLevel.Years -> browsedYears.first + YEARS_PER_PAGE
            else -> browsedYear + 1
        }.coerceIn(bounds.yearRange)
    }

    /** Construction helpers that stay inside the module. */
    internal companion object {
        /**
         * Saves the visible month and the fold across configuration change and process death.
         *
         * Only the month and the fold are saved. [bounds] and [today] are restored from the call site, because
         * both may legitimately differ on the way back — a booking window moves on, and so does
         * today — and a saved copy would silently resurrect yesterday's limits.
         *
         * @param bounds Window to rebuild the state with.
         * @param today Day to rebuild the state with.
         * @return Saver holding `[year, monthNumber, folded, anchorEpochDay]`, with `folded` `1` or `0`.
         */
        fun saver(bounds: CalendarBounds, today: LocalDate): Saver<CalendarState, List<Long>> =
            Saver(
                save = {
                    listOf(
                        it.visibleMonth.year.toLong(),
                        it.visibleMonth.month.number.toLong(),
                        if (it.isFolded) 1L else 0L,
                        it.foldAnchor.toEpochDays(),
                    )
                },
                restore = { saved ->
                    CalendarState(
                        bounds = bounds,
                        today = today,
                        initialMonth = YearMonth(saved[0].toInt(), saved[1].toInt()),
                        initiallyFolded = saved[2] == 1L,
                        initialFoldAnchor = LocalDate.fromEpochDays(saved[3]),
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

/** What a [Calendar]'s body shows. Stepped through by [CalendarState.zoomOut] and the choices on each level. */
public enum class CalendarLevel {
    /** The days of the visible month. */
    Days,

    /** The twelve months of [CalendarState.browsedYear]. */
    Months,

    /** The page of years [CalendarState.browsedYears]. */
    Years,
}

/** Fold past which a calendar counts as [folded][CalendarState.isFolded]. */
private const val FOLDED_PAST: Float = 0.5f

/** Years on one page of the [CalendarLevel.Years] level: three columns of four rows. */
public const val YEARS_PER_PAGE: Int = 12

/**
 * The page of [YEARS_PER_PAGE] years holding [year], counted from the first year of the bounds.
 *
 * @param year Year the page must hold. Clamped into the bounds first.
 * @return Consecutive years, starting at the bounds' first year or a multiple of [YEARS_PER_PAGE]
 *   after it.
 */
internal fun CalendarBounds.yearPageOf(year: Int): IntRange {
    val first = yearRange.first
    val start = first + (year.coerceIn(yearRange) - first) / YEARS_PER_PAGE * YEARS_PER_PAGE
    return start until start + YEARS_PER_PAGE
}
