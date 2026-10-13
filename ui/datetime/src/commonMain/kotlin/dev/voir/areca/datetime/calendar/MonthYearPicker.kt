package dev.voir.areca.datetime.calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.voir.areca.foundation.wheel.LocalWheelPickerScroll
import dev.voir.areca.foundation.wheel.LocalWheelPickerStyle
import dev.voir.areca.foundation.wheel.WheelPicker
import dev.voir.areca.foundation.wheel.WheelPickerColumn
import dev.voir.areca.foundation.wheel.WheelPickerColumnState
import dev.voir.areca.foundation.wheel.WheelPickerItem
import dev.voir.areca.foundation.wheel.WheelPickerScroll
import dev.voir.areca.foundation.wheel.WheelPickerSize
import dev.voir.areca.foundation.wheel.WheelPickerStyle
import dev.voir.areca.foundation.wheel.rememberWheelPickerColumnState
import dev.voir.areca.theme.ArecaTheme
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Month
import kotlinx.datetime.YearMonth
import kotlinx.datetime.number

/**
 * Two wheels — month and year — for landing on any month in a range.
 *
 * The problem it solves is that a month grid can only be stepped. Reaching a date of birth by
 * swiping is several hundred gestures; here it is two. Wheels rather than a grid of years because
 * the library already has one ([dev.voir.areca.foundation.wheel.WheelPicker], which
 * [dev.voir.areca.datetime.TimePicker] is built on), so the two date-time pickers behave
 * identically — the same snapping, the same falloff, the same feel under a trackpad, a mouse wheel
 * and a thumb.
 *
 * The month column carries only the months [bounds] actually allows in the year the year wheel has
 * come to rest on, so at the edges of a range the unreachable ones are absent rather than
 * selectable-then-silently-corrected. It narrows on the *settled* year rather than the live one:
 * rebuilding the month list while the year wheel is still turning would pull the month wheel about
 * under a gesture aimed at its neighbour.
 *
 * Controlled: it does not hold the month, it reports the one the wheels have come to rest on. Drive
 * it from the same state the calendar uses and the two can never disagree. Changes arriving from
 * outside are followed only while the wheels are idle and only once the caller has caught up with
 * whatever the picker last reported — so a chevron tap lands, while a caller travelling towards the
 * month the user just picked cannot drag the wheels back over it on the way. Following turns the
 * year first and then the month, and reports nothing on the way: the wheels pass through other
 * months, but none of them is a choice.
 *
 * To assistive technology the month wheel reads month names ([CalendarLabels.pickerMonth]) and the
 * year wheel its years as [CalendarLabels.year] renders them.
 *
 * @param month Month the wheels show.
 * @param onMonthChange Called with the month the user scrolled to. Not called for changes to [month]
 *   that came from outside.
 * @param modifier Modifier applied to the picker.
 * @param bounds Range the wheels may reach.
 * @param labels Month names and year digits. Defaults to English.
 * @param style Colors of the wheels, including the fill behind the centred row and the color its
 *   edges fade to.
 * @param size Row height and visible row count.
 * @param monthScroll How hard the month wheel answers scrolling. `null` lets it pace itself from
 *   its own length, which for twelve months means all precision and no hurry.
 * @param yearScroll How hard the year wheel answers scrolling. `null` lets it pace itself, which for
 *   a range of two centuries means a gentle base with a lot of wind-up — the two wheels end up
 *   deliberately unalike, because one runs out under any gesture and the other does not.
 */
@Composable
public fun MonthYearPicker(
    month: YearMonth,
    onMonthChange: (YearMonth) -> Unit,
    modifier: Modifier = Modifier,
    bounds: CalendarBounds = CalendarBounds.Default,
    labels: CalendarLabels = CalendarLabels.English,
    style: WheelPickerStyle = LocalWheelPickerStyle.current ?: WheelPickerStyle.Default,
    size: WheelPickerSize = LocalCalendarSize.current?.monthPicker ?: WheelPickerSize.Default,
    monthScroll: WheelPickerScroll? = LocalWheelPickerScroll.current,
    yearScroll: WheelPickerScroll? = LocalWheelPickerScroll.current,
) {
    val years = remember(bounds) { bounds.yearRange.toList() }
    val yearState = rememberWheelPickerColumnState(
        initialIndex = years.indexOf(month.year).coerceAtLeast(0),
    )

    val restingYear = years.getOrElse(yearState.settledIndex) { month.year }
    val monthNumbers = remember(bounds, restingYear) { bounds.monthNumbersIn(restingYear).toList() }

    // A month arriving from outside that the wheels are on their way to, or `null` while they are
    // not following anything. While it is set nothing is reported: the wheels pass through other
    // months on the way, and none of them is the user's choice.
    var followTarget by remember { mutableStateOf<YearMonth?>(null) }

    // The month wheel's index means nothing without the list it indexes, and that list changes
    // with the resting year at the edges of the bounds. A new list therefore gets a new wheel,
    // seeded with the month the old one rested on, so turning the year keeps the month rather than
    // its position: June stays June when the list for the next year starts at January. A wheel
    // built while following an outside change starts on the month being followed instead.
    val lastMonthWheel = remember { MonthWheel() }
    val monthState = key(monthNumbers) {
        val initialIndex = remember {
            val previousState = lastMonthWheel.state
            val followed = followTarget?.takeIf { it.year == restingYear }
            seedMonthIndex(
                months = monthNumbers,
                previousMonths = lastMonthWheel.months,
                previousIndex = previousState?.settledIndex,
                wanted = when {
                    followed != null -> followed.month.number
                    previousState == null -> month.month.number
                    else -> null
                },
            )
        }
        rememberWheelPickerColumnState(initialIndex = initialIndex)
    }
    SideEffect {
        lastMonthWheel.months = monthNumbers
        lastMonthWheel.state = monthState
    }

    // Month reported to the caller and not yet seen coming back. While one is outstanding, an
    // inbound month is the caller on its way to it — a pager animating through the months in
    // between, a screen updating in stages — and following those would drag the wheels back over the
    // choice the user has just made, which in turn reports that month, which moves the caller again.
    // That loop would keep a month several steps away from ever being chosen. Keyed on the year
    // wheel alone, because the month wheel is replaced whenever the year changes its list.
    var reportedMonth by remember(yearState) { mutableStateOf<YearMonth?>(null) }

    val currentMonth by rememberUpdatedState(month)
    val currentYears by rememberUpdatedState(years)
    val currentMonthNumbers by rememberUpdatedState(monthNumbers)
    val currentMonthState by rememberUpdatedState(monthState)
    val currentBounds by rememberUpdatedState(bounds)
    val currentOnMonthChange by rememberUpdatedState(onMonthChange)

    // Report what the wheels came to rest on. Reading the settled indices, rather than the live
    // ones, is what keeps a scroll from reporting every month it passes over; drop(1) skips the
    // emission that merely echoes the current value on first composition, and the equality check
    // skips the one that echoes an inbound change we have just scrolled to ourselves. Not keyed on
    // the month wheel: a replaced wheel is read through currentMonthState, and restarting here
    // would drop the year change that replaced it.
    LaunchedEffect(yearState) {
        snapshotFlow {
            val year = currentYears.getOrNull(yearState.settledIndex)
            val monthNumber = currentMonthNumbers.getOrNull(currentMonthState.settledIndex)
            if (year != null && monthNumber != null) YearMonth(year, monthNumber) else null
        }
            .filterNotNull()
            .distinctUntilChanged()
            .drop(1)
            .collect { picked ->
                if (followTarget == null && picked != currentMonth) {
                    val reachable = currentBounds.coerce(picked)
                    reportedMonth = reachable
                    currentOnMonthChange(reachable)
                }
            }
    }

    // Turning a wheel again withdraws whatever was reported before it, so a caller that never
    // confirms a report cannot leave the picker permanently deaf to changes from elsewhere.
    LaunchedEffect(yearState) {
        snapshotFlow { yearState.isScrollInProgress || currentMonthState.isScrollInProgress }
            .filter { scrolling -> scrolling }
            .collect { reportedMonth = null }
    }

    // Follow changes that came from elsewhere — a chevron tap, a caller jumping to today. Skipped
    // while either wheel is moving: a picker that scrolls itself mid-gesture is the bug where
    // turning one wheel appears to turn its neighbour. The year turns first and the month after it,
    // in one coroutine, because the month wheel for the new year exists only once the year wheel
    // has settled; whichever wheel would have finished first, the result is the same month.
    LaunchedEffect(month, years, yearState) {
        if (yearState.isScrollInProgress || currentMonthState.isScrollInProgress) {
            return@LaunchedEffect
        }
        val pending = reportedMonth
        if (pending != null) {
            if (month == pending) reportedMonth = null
            return@LaunchedEffect
        }
        val yearIndex = years.indexOf(month.year)
        if (yearIndex < 0) return@LaunchedEffect

        val targetMonths = currentBounds.monthNumbersIn(month.year).toList()
        followTarget = month
        try {
            if (yearIndex != yearState.settledIndex) yearState.animateTo(yearIndex)
            val wheel = snapshotFlow {
                currentMonthState.takeIf { currentMonthNumbers == targetMonths }
            }.filterNotNull().first()
            val monthIndex = targetMonths.indexOf(month.month.number)
            if (monthIndex >= 0 && monthIndex != wheel.settledIndex) wheel.animateTo(monthIndex)
        } finally {
            if (followTarget == month) followTarget = null
        }
    }

    WheelPicker(
        modifier = modifier.fillMaxWidth(),
        style = style,
        size = size,
        horizontalArrangement = Arrangement.spacedBy(
            space = ArecaTheme.spacing.md,
            alignment = Alignment.CenterHorizontally,
        ),
        columns = {
            WheelPickerColumn(
                items = monthNumbers,
                state = monthState,
                modifier = Modifier.weight(1f),
                scroll = monthScroll,
                contentDescription = labels.monthPickerMonth,
                valueDescription = { monthNumber -> labels.pickerMonth(Month(monthNumber)) },
            ) { monthNumber ->
                WheelPickerItem(labels.pickerMonth(Month(monthNumber)))
            }
            WheelPickerColumn(
                items = years,
                state = yearState,
                modifier = Modifier.weight(1f),
                scroll = yearScroll,
                contentDescription = labels.monthPickerYear,
                valueDescription = { year -> labels.year(year) },
            ) { year ->
                WheelPickerItem(labels.year(year))
            }
        },
    )
}

/**
 * The month wheel a [MonthYearPicker] last committed, and the list its index points into.
 *
 * Plain fields written from a `SideEffect`: read once when the month list changes, to seed the next
 * wheel, and never a reason to recompose.
 */
private class MonthWheel {
    /** Month numbers the wheel in [state] was showing. */
    var months: List<Int> = emptyList()

    /** The committed month wheel, or `null` before the first composition has been applied. */
    var state: WheelPickerColumnState? = null
}

/**
 * Where a new month wheel of a [MonthYearPicker] starts.
 *
 * @param months Month numbers the new wheel shows. Ascending and contiguous, as
 *   [CalendarBounds.monthNumbersIn] produces.
 * @param previousMonths Month numbers the wheel it replaces was showing.
 * @param previousIndex Index the replaced wheel rested on, or `null` when there was none.
 * @param wanted Month number the wheel must start on — the month being followed from outside, or
 *   the picker's first month — or `null` to keep the replaced wheel's month.
 * @return Index into [months] of [wanted], else of the replaced wheel's month, each pulled onto the
 *   nearest month [months] holds. `0` when there is nothing to go on.
 */
internal fun seedMonthIndex(
    months: List<Int>,
    previousMonths: List<Int>,
    previousIndex: Int?,
    wanted: Int?,
): Int = when {
    wanted != null -> monthIndexInNewList(listOf(wanted), 0, months)
    previousIndex != null -> monthIndexInNewList(previousMonths, previousIndex, months)
    else -> 0
}

/**
 * Where the month a wheel rested on sits in the list that replaces the wheel's own.
 *
 * Keyed by month number rather than by position, because the lists differ in where they start: at
 * the edges of [CalendarBounds] one year's list may begin at June and the next at January. A month
 * the new list does not offer lands on the nearest one it does.
 *
 * @param previousMonths Month numbers, `1..12`, the wheel was showing. Ascending.
 * @param previousIndex Index the wheel rested on in [previousMonths].
 * @param months Month numbers the replacing wheel shows. Ascending and contiguous, as
 *   [CalendarBounds.monthNumbersIn] produces.
 * @return Index into [months] of the same month, or of the nearest one it holds. `0` when [months]
 *   is empty or [previousMonths] has no month at [previousIndex].
 */
internal fun monthIndexInNewList(
    previousMonths: List<Int>,
    previousIndex: Int,
    months: List<Int>,
): Int {
    val monthNumber = previousMonths.getOrNull(previousIndex) ?: return 0
    if (months.isEmpty()) return 0
    return months.indexOf(monthNumber.coerceIn(months.first(), months.last())).coerceAtLeast(0)
}
