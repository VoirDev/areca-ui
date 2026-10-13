package dev.voir.areca.datetime

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.datetime.calendar.Calendar
import dev.voir.areca.datetime.calendar.CalendarDateRule
import dev.voir.areca.datetime.calendar.CalendarLabels
import dev.voir.areca.datetime.calendar.CalendarSelection
import dev.voir.areca.datetime.calendar.CalendarSize
import dev.voir.areca.datetime.calendar.CalendarState
import dev.voir.areca.datetime.calendar.LocalCalendarSize
import dev.voir.areca.datetime.calendar.rememberCalendarState
import dev.voir.areca.datetime.icon.BundledIcons
import dev.voir.areca.datetime.icon.Calendar
import dev.voir.areca.datetime.icon.Clock
import dev.voir.areca.foundation.field.FieldDefaults
import dev.voir.areca.foundation.field.FieldRow
import dev.voir.areca.foundation.field.FieldScope
import dev.voir.areca.foundation.field.FieldSize
import dev.voir.areca.foundation.field.FieldStatus
import dev.voir.areca.foundation.field.FieldStyle
import dev.voir.areca.foundation.field.segmentWidth
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.foundation.overlay.AnchoredPlacement
import dev.voir.areca.foundation.overlay.DropdownPopup
import dev.voir.areca.foundation.overlay.DropdownPopupSize
import dev.voir.areca.foundation.overlay.DropdownPopupStyle
import dev.voir.areca.foundation.overlay.DropdownWidth
import dev.voir.areca.foundation.overlay.anchored
import dev.voir.areca.foundation.overlay.rememberDropdownPopup
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.theme.ArecaTheme
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.yearMonth

/**
 * How a [DateTimeField] turns the value it holds into the two strings it shows.
 *
 * A pair of functions rather than a pattern string, for the reason the rest of this module takes
 * [CalendarLabels] and [TimePickerLabels]: the library ships no localization, so an application
 * formats with whatever it already has and passes the result in.
 *
 * @property date Renders the date half. Called only when there is a date to render.
 * @property time Renders the time half.
 */
@Immutable
public data class DateTimeFieldFormat(
    public val date: (LocalDate) -> String,
    public val time: (LocalTime) -> String,
) {
    /** Ready-made formats that render ISO-ordered digits. */
    public companion object {
        /**
         * ISO-ordered digits — `2026-09-13` and `14:30` — which read the same in every locale
         * and are wrong in most of them. A placeholder for an application's own formatter.
         *
         * Always twenty-four-hour; [numeric] gives the same digits for either [TimeFormat].
         */
        public val Iso: DateTimeFieldFormat = numeric(timeFormat = TimeFormat.Hours24)

        /**
         * ISO-ordered digits whose time counts hours the way the wheels do: `2026-09-13` with
         * `14:30`, or with `2:30 PM`.
         *
         * What [DateTimeField] shows unless told otherwise, so the field never reads `14:30` over a
         * wheel that says `2 PM`. Like [Iso], a placeholder for an application's own formatter.
         *
         * @param timeFormat Whether the time counts hours to twenty-four or to twelve.
         * @param timeLabels Source of the period names for a twelve-hour time.
         * @return A format pairing ISO-ordered dates with times in [timeFormat].
         */
        public fun numeric(
            timeFormat: TimeFormat,
            timeLabels: TimePickerLabels = TimePickerLabels.English,
        ): DateTimeFieldFormat = DateTimeFieldFormat(
            date = { date ->
                "${date.year.toString().padStart(4, '0')}-" +
                    "${(date.month.ordinal + 1).toString().padStart(2, '0')}-" +
                    date.day.toString().padStart(2, '0')
            },
            time = { time ->
                val minute = time.minute.toString().padStart(2, '0')
                when (timeFormat) {
                    TimeFormat.Hours24 -> "${time.hour.toString().padStart(2, '0')}:$minute"
                    TimeFormat.Hours12 -> {
                        val period = when (DayPeriod.fromHour24(time.hour)) {
                            DayPeriod.AM -> timeLabels.am
                            DayPeriod.PM -> timeLabels.pm
                        }
                        "${toHour12(time.hour)}:$minute $period"
                    }
                }
            },
        )
    }
}

/**
 * What [DateTimeField] reads out for its two halves, and what it shows while they are empty.
 *
 * Collected here for the reason [CalendarLabels] and [TimePickerLabels] exist: the library ships no
 * resources, so an application passes text built from whatever localization it already has.
 *
 * @property date Name of the date half, read before what it shows.
 * @property time Name of the time half, read before what it shows.
 * @property expanded State of a half while its picker is open.
 * @property collapsed State of a half while its picker is closed.
 * @property datePlaceholder Shown in the date half while nothing is chosen.
 * @property timePlaceholder Shown in the time half while no time is chosen.
 */
@Immutable
public data class DateTimeFieldLabels(
    public val date: String,
    public val time: String,
    public val expanded: String,
    public val collapsed: String,
    public val datePlaceholder: String = "Pick a date",
    public val timePlaceholder: String = "--:--",
) {
    /** Ready-made label sets. */
    public companion object {
        /** Plain-English fallback. */
        public val English: DateTimeFieldLabels = DateTimeFieldLabels(
            date = "Date",
            time = "Time",
            expanded = "Expanded",
            collapsed = "Collapsed",
            datePlaceholder = "Pick a date",
            timePlaceholder = "--:--",
        )
    }
}

/**
 * One field holding a date and a time, each chosen from its own picker.
 *
 * The composite this module exists to make possible: a [FieldRow] whose left half opens a
 * [Calendar] and whose right half opens a [TimePicker], inside a single border that reports one
 * focus and one status. Two separate fields would ask a form to align them, label them and
 * validate them twice, and would let a user submit a date with no time.
 *
 * Controlled, and deliberately non-null in the callback: picking a date on a field that held
 * nothing produces a value at [defaultTime] rather than a half-filled state the caller has to
 * model. [value] itself stays nullable, because "nothing chosen yet" is a real state and an
 * arbitrary default masquerading as a choice is not.
 *
 * For the same reason a time picked before any date is not reported. There is no date to pair it
 * with, and inventing one could hand the caller a day [dateRule] or the calendar's bounds refuse.
 * The field holds the time as a draft instead: the time half shows it, and the first date picked
 * takes it in place of [defaultTime]. The draft is dropped whenever [value] changes, so a form
 * that resets the field to null resets the draft with it.
 *
 * The two pickers are exclusive — opening one closes the other, since they sit side by side and
 * two popups over one field is a menu covering the thing it belongs to. The calendar is drawn at
 * [CalendarSize.Compact] unless a [LocalCalendarSize] is provided, and each time it opens it shows
 * its grid on the month of the value the field holds, wherever it was left the last time.
 *
 * Unless told otherwise, the calendar's week starts and the clock counts hours the way the user's
 * locale does ([DateTimeLocale.Current]).
 *
 * @param value Current date and time, or null while nothing has been chosen.
 * @param onValueChange Called with the complete value after either half is changed. Never called
 *   for a time picked while there is no date; see above.
 * @param modifier Modifier applied to the field container.
 * @param style Color palette and container shape.
 * @param size Typography, paddings and vertical metrics.
 * @param field Scope from an enclosing [dev.voir.areca.foundation.field.FieldGroup].
 * @param status Validation outcome to report. Comes from [field] unless given explicitly.
 * @param enabled When false, neither half opens and the field is drawn as disabled.
 *   Comes from [field] unless it is given explicitly.
 * @param readOnly Shows the value but opens neither picker.
 * @param interactionSource Source the field reports hover to, or `null` for one it creates.
 * @param timeFormat Whether the wheels count hours to twenty-four or to twelve. Defaults to the
 *   hour cycle of [DateTimeLocale.Current].
 * @param timeLabels What assistive technology reads for the wheels, and the period names.
 * @param format How the value is rendered into each half. Defaults to digits whose time counts
 *   hours the way [timeFormat] does.
 * @param labels What assistive technology reads for each half, and the placeholders.
 * @param defaultTime Time applied when a date is chosen on a field that held no value and no
 *   drafted time.
 * @param datePlaceholder Shown in the date half while nothing is chosen. Defaults to
 *   [DateTimeFieldLabels.datePlaceholder].
 * @param timePlaceholder Shown in the time half while nothing is chosen. Defaults to
 *   [DateTimeFieldLabels.timePlaceholder].
 * @param timeWidth Fixed width for the time half, so a column of these lines up. Unspecified
 *   sizes it to the time it happens to show, which is what you want unless the format produces
 *   strings of different lengths. Leave room for the clock glyph and the padding either side of
 *   it, or the time is clipped.
 * @param calendarState Month the calendar opens on and the bounds it can move between.
 *   Opens on the month [value] falls in, or on the current one while there is no value.
 * @param dateRule Which days accept selection.
 * @param startDayOfWeek First column of the calendar. Defaults to the week start of
 *   [DateTimeLocale.Current].
 * @param calendarLabels What assistive technology reads for the calendar.
 * @param placement Where each picker sits relative to the half that opened it.
 * @param dropdownStyle Colors and elevation for both picker surfaces.
 * @param leading Slot before the date, in the style's decoration color. Defaults to a calendar
 *   glyph; pass null for a field that is already labelled.
 */
@Composable
public fun DateTimeField(
    value: LocalDateTime?,
    onValueChange: (LocalDateTime) -> Unit,
    modifier: Modifier = Modifier,
    style: FieldStyle = FieldDefaults.style,
    size: FieldSize = FieldDefaults.size,
    field: FieldScope? = null,
    status: FieldStatus = field?.status ?: FieldStatus.Default,
    enabled: Boolean = field?.enabled ?: true,
    readOnly: Boolean = false,
    interactionSource: MutableInteractionSource? = null,
    timeFormat: TimeFormat = DateTimeLocale.Current.timeFormat,
    timeLabels: TimePickerLabels = TimePickerLabels.English,
    format: DateTimeFieldFormat = remember(timeFormat, timeLabels) {
        DateTimeFieldFormat.numeric(timeFormat = timeFormat, timeLabels = timeLabels)
    },
    labels: DateTimeFieldLabels = DateTimeFieldLabels.English,
    defaultTime: LocalTime = LocalTime(hour = 9, minute = 0),
    datePlaceholder: String = labels.datePlaceholder,
    timePlaceholder: String = labels.timePlaceholder,
    timeWidth: Dp = Dp.Unspecified,
    calendarState: CalendarState = rememberCalendarState(
        initialMonth = value?.date?.yearMonth,
    ),
    dateRule: CalendarDateRule = CalendarDateRule.All,
    startDayOfWeek: DayOfWeek = DateTimeLocale.Current.firstDayOfWeek,
    calendarLabels: CalendarLabels = CalendarLabels.English,
    placement: AnchoredPlacement = AnchoredPlacement.BelowStart,
    dropdownStyle: DropdownPopupStyle = DropdownPopupStyle.Default,
    leading: (@Composable RowScope.() -> Unit)? = {
        Icon(imageVector = BundledIcons.Calendar, contentDescription = null)
    },
) {
    val datePopup = rememberDropdownPopup(placement = placement)
    val timePopup = rememberDropdownPopup(placement = placement)

    val canOpen = enabled && !readOnly

    // A time picked while there is no date to pair it with; see the KDoc for why it is held here
    // rather than reported.
    var draftTime by remember(value) { mutableStateOf<LocalTime?>(null) }
    val shownTime = value?.time ?: draftTime

    LaunchedEffect(canOpen) {
        if (!canOpen) {
            datePopup.dismiss()
            timePopup.dismiss()
        }
    }

    // The calendar's state outlives its popup, so it would reopen wherever it was left. Each time
    // it opens it goes back to the grid and to the month of the value now held, which may have
    // changed from outside while the popup was closed.
    val currentValue by rememberUpdatedState(value)
    val isDateOpen = datePopup.isOpen
    LaunchedEffect(isDateOpen, calendarState) {
        if (!isDateOpen) return@LaunchedEffect
        calendarState.collapseMonthPicker()
        val month = currentValue?.date?.yearMonth ?: return@LaunchedEffect
        if (calendarState.visibleMonth != month) calendarState.scrollTo(month)
    }

    // Each half opens its own picker and closes the other: two popups over one field is a menu
    // covering the thing it belongs to.
    val openDate: () -> Unit = {
        timePopup.dismiss()
        datePopup.show()
    }
    val openTime: () -> Unit = {
        datePopup.dismiss()
        timePopup.show()
    }

    FieldRow(
        modifier = modifier,
        style = style,
        size = size,
        field = field,
        status = status,
        enabled = enabled,
        readOnly = readOnly,
        interactionSource = interactionSource,
    ) {
        val dateText = value?.let { format.date(it.date) }
        Segment(
            modifier = Modifier
                .anchored(datePopup.anchorState)
                .semantics {
                    contentDescription = "${labels.date}, ${dateText ?: datePlaceholder}"
                    if (canOpen) {
                        role = Role.Button
                        stateDescription = if (isDateOpen) labels.expanded else labels.collapsed
                    }
                },
            weight = 1f,
            onClick = if (canOpen) openDate else null,
            empty = dateText == null,
            leading = leading,
            placeholder = { Text(datePlaceholder, maxLines = 1, overflow = TextOverflow.Ellipsis) },
        ) {
            // Single-line and clipped: a half that wrapped would grow the row and take the
            // divider with it, which reads as the field having broken rather than as a long value.
            dateText?.let { Text(it, maxLines = 1, overflow = TextOverflow.Ellipsis) }
        }

        SegmentDivider()

        val timeText = shownTime?.let(format.time)
        val isTimeOpen = timePopup.isOpen
        Segment(
            modifier = Modifier
                .segmentWidth(timeWidth)
                .anchored(timePopup.anchorState)
                .semantics {
                    contentDescription = "${labels.time}, ${timeText ?: timePlaceholder}"
                    if (canOpen) {
                        role = Role.Button
                        stateDescription = if (isTimeOpen) labels.expanded else labels.collapsed
                    }
                },
            onClick = if (canOpen) openTime else null,
            empty = timeText == null,
            leading = { Icon(imageVector = BundledIcons.Clock, contentDescription = null) },
            placeholder = { Text(timePlaceholder, maxLines = 1, overflow = TextOverflow.Ellipsis) },
        ) {
            timeText?.let { Text(it, maxLines = 1, overflow = TextOverflow.Ellipsis) }
        }
    }

    val selection = remember(value) { CalendarSelection.Single(value?.date) }

    // Both popups hang off the row rather than off the segment that opened them: a segment
    // is measured at the row's intrinsic height, and a picker composed inside one would be
    // asked for an intrinsic height it has no business answering.
    DropdownPopup(
        state = datePopup,
        onDismissRequest = { datePopup.dismiss() },
        style = dropdownStyle,
        size = CalendarPopupSize,
    ) {
        Calendar(
            modifier = Modifier.padding(ArecaTheme.spacing.md),
            state = calendarState,
            selection = selection,
            onDateClick = { date ->
                onValueChange(
                    valueAfterDatePick(
                        date = date,
                        value = value,
                        draftTime = draftTime,
                        defaultTime = defaultTime,
                    ),
                )
                datePopup.dismiss()
            },
            dateRule = dateRule,
            startDayOfWeek = startDayOfWeek,
            labels = calendarLabels,
            size = LocalCalendarSize.current ?: CalendarSize.Compact,
        )
    }

    DropdownPopup(
        state = timePopup,
        onDismissRequest = { timePopup.dismiss() },
        style = dropdownStyle,
        size = TimePopupSize,
    ) {
        Box(modifier = Modifier.padding(ArecaTheme.spacing.md)) {
            TimePicker(
                initialTime = shownTime ?: defaultTime,
                onTimeChange = { time ->
                    val next = valueAfterTimePick(time = time, value = value)
                    if (next != null) onValueChange(next) else draftTime = time
                },
                format = timeFormat,
                labels = timeLabels,
            )
        }
    }
}

/** Width of the calendar's popup: seven compact day cells and the calendar's own insets. */
private val CalendarPopupWidth: Dp = 340.dp

/** Tallest the calendar's popup grows: the compact header, the weekday row and six weeks. */
private val CalendarPopupMaxHeight: Dp = 420.dp

/** Width of the time picker's popup: three wheels and the gaps between them. */
private val TimePopupWidth: Dp = 240.dp

/** Tallest the time picker's popup grows: the wheels' visible rows and the popup's inset. */
private val TimePopupMaxHeight: Dp = 320.dp

/**
 * Room for a month grid, which is wider than the field half that opens it.
 *
 * Fixed rather than [DropdownWidth.Min]: a month grid fills the width it is offered, so a mode
 * with no upper bound would give it the whole window.
 */
private val CalendarPopupSize: DropdownPopupSize
    @Composable get() = DropdownPopupSize.default(
        width = DropdownWidth.Fixed(width = CalendarPopupWidth),
        maxHeight = CalendarPopupMaxHeight,
    )

/** Room for the wheels, which are taller than a dropdown's usual allowance. */
private val TimePopupSize: DropdownPopupSize
    @Composable get() = DropdownPopupSize.default(
        width = DropdownWidth.Fixed(width = TimePopupWidth),
        maxHeight = TimePopupMaxHeight,
    )

/**
 * The value a [DateTimeField] reports when a date is picked.
 *
 * @param date Day the user picked.
 * @param value What the field held before the pick, or `null` while it held nothing.
 * @param draftTime Time picked while the field held no date, or `null` when none was.
 * @param defaultTime Time for a field that has neither a value nor a drafted time.
 * @return [date] at the held time, else the drafted time, else [defaultTime].
 */
internal fun valueAfterDatePick(
    date: LocalDate,
    value: LocalDateTime?,
    draftTime: LocalTime?,
    defaultTime: LocalTime,
): LocalDateTime = LocalDateTime(date, value?.time ?: draftTime ?: defaultTime)

/**
 * The value a [DateTimeField] reports when a time is picked.
 *
 * @param time Time the wheels came to rest on.
 * @param value What the field held before the pick, or `null` while it held nothing.
 * @return The held date at [time], or `null` when there is no date yet and the time has to be held
 *   as a draft instead of reported.
 */
internal fun valueAfterTimePick(time: LocalTime, value: LocalDateTime?): LocalDateTime? =
    value?.let { LocalDateTime(date = it.date, time = time) }
