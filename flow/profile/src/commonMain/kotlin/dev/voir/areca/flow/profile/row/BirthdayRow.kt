package dev.voir.areca.flow.profile.row

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dev.voir.areca.foundation.overlay.FlowModalHeaderInsets
import kotlin.time.Clock
import dev.voir.areca.datetime.calendar.Calendar
import dev.voir.areca.datetime.calendar.CalendarBounds
import dev.voir.areca.datetime.calendar.CalendarLabels
import dev.voir.areca.datetime.calendar.CalendarSelection
import dev.voir.areca.datetime.calendar.rememberCalendarState
import dev.voir.areca.flow.profile.icon.BundledIcons
import dev.voir.areca.flow.profile.icon.Gift
import dev.voir.areca.foundation.overlay.FlowModal
import dev.voir.areca.foundation.overlay.FlowModalActionBar
import dev.voir.areca.foundation.overlay.FlowModalController
import dev.voir.areca.foundation.overlay.FlowModalSecondaryAction
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.overlay.rememberFlowModal
import dev.voir.areca.foundation.settings.SettingsRow
import dev.voir.areca.foundation.settings.SettingsRowIcon
import dev.voir.areca.foundation.settings.SettingsRowStatus
import dev.voir.areca.foundation.topbar.TopBar
import dev.voir.areca.foundation.topbar.TopBarSize
import dev.voir.areca.foundation.topbar.TopBarStyle
import dev.voir.areca.foundation.topbar.TopBarTitleAlignment
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.YearMonth
import kotlinx.datetime.todayIn
import kotlinx.datetime.yearMonth

/**
 * A date of birth, as a fact on the profile: the date, under it "Birthday", and a calendar to change
 * it.
 *
 * Chosen from a calendar rather than typed: typing a date means parsing one, and every format
 * somebody might reasonably type is ambiguous in some locale. Pressing the row opens the calendar
 * sheet, and the sheet is also where a birthday set by accident is removed.
 *
 * With no date set, the row invites one ("Add birthday") rather than stating an absence.
 *
 * Put it in a `Section` of a `Settings` list; the section draws the card and the hairlines.
 *
 * @param value Current date, or `null` when none is set.
 * @param onValueChange Called with the chosen date, or `null` when it is removed.
 * @param modifier Modifier applied to the row.
 * @param status Progress of saving the last change: a spinner while saving, a message on failure.
 * @param minDate Earliest day the calendar offers, or `null` for the calendar's own earliest,
 *   1 January 1900.
 * @param maxDate Latest day the calendar offers, or `null` for the calendar's own latest,
 *   31 December 2100. Today by default, since a birthday is in the past; a product with an age
 *   requirement moves it back.
 * @param removable Whether the calendar offers to remove a date in force. `false` for a product that
 *   requires one.
 * @param label What the fact is, under the date.
 * @param placeholder Title while no date is set — an invitation to set one.
 * @param formatDate Writes the date in force for the title. Defaults to "12 March 1990"; pass the
 *   application's locale-aware formatter.
 * @param sheetTitle Heading of the calendar, also the row's action label for assistive technology.
 * @param applyLabel Label of the calendar's committing button.
 * @param cancelLabel Label of the calendar's cancelling button, offered while there is no date to
 *   remove.
 * @param removeLabel Label of the button that removes the date in force.
 * @param calendarLabels Words the calendar draws and reads out — month and weekday names, each
 *   day's description and its navigation actions. English by default; pass the application's own.
 * @param startDayOfWeek Day each week of the calendar begins on. Monday by default; pass the
 *   locale's.
 * @param enabled Whether the row can be opened.
 */
@Composable
public fun BirthdayRow(
    value: LocalDate?,
    onValueChange: (LocalDate?) -> Unit,
    modifier: Modifier = Modifier,
    status: SettingsRowStatus = SettingsRowStatus.Idle,
    minDate: LocalDate? = null,
    maxDate: LocalDate? = Clock.System.todayIn(TimeZone.currentSystemDefault()),
    removable: Boolean = true,
    label: String = "Birthday",
    placeholder: String = "Add birthday",
    formatDate: (LocalDate) -> String = ::englishDate,
    sheetTitle: String = "Choose your birthday",
    applyLabel: String = "Save",
    cancelLabel: String = "Cancel",
    removeLabel: String = "Remove",
    calendarLabels: CalendarLabels = CalendarLabels.English,
    startDayOfWeek: DayOfWeek = DayOfWeek.MONDAY,
    enabled: Boolean = true,
) {
    val sheet = rememberFlowModal(autoHeight = true)

    SettingsRow(
        title = { Text(value?.let(formatDate) ?: placeholder) },
        subtitle = { Text(label) },
        modifier = modifier,
        leading = { SettingsRowIcon(BundledIcons.Gift) },
        status = status,
        enabled = enabled,
        onClick = { sheet.show() },
        onClickLabel = sheetTitle,
    )

    BirthdaySheet(
        controller = sheet,
        date = value,
        minDate = minDate,
        maxDate = maxDate,
        title = sheetTitle,
        applyLabel = applyLabel,
        cancelLabel = cancelLabel,
        removeLabel = removeLabel,
        calendarLabels = calendarLabels,
        startDayOfWeek = startDayOfWeek,
        onDateSelected = { date ->
            sheet.hide()
            onValueChange(date)
        },
        onRemove = if (removable) {
            {
                sheet.hide()
                onValueChange(null)
            }
        } else {
            null
        },
    )
}

/**
 * The sheet a birthday is chosen on.
 *
 * A tap marks a day and the commit commits it. A mis-tap on a grid of forty-two cells should not be
 * the answer, and the scrim, a drag or back discards the mark. The heading carries the title alone;
 * every action is in the [FlowModalActionBar] at the foot, as on every other sheet — the commit on
 * the right, and on the left the removal of the date in force, or Cancel while there is none to
 * remove. Every time the sheet opens, the mark is reset to the date in force and the calendar scrolls
 * to that date's month, so reopening after a cancel shows the date that is set rather than the one
 * abandoned. With no date in force nothing is marked and the commit waits for a tap: the calendar
 * opens on today's month, but a birthday is never today just because Save was pressed.
 *
 * Days outside [minDate]–[maxDate] do not exist on this calendar: bounds rather than a rule, so a
 * month past them cannot even be paged to. The month label opens the month-and-year picker, which is
 * how a birthday decades back is reached without paging through every month in between.
 *
 * @param controller Sheet's visibility.
 * @param date Date currently in force, which is what the sheet opens on.
 * @param minDate Earliest day offered, or `null` for the calendar's own earliest.
 * @param maxDate Latest day offered, or `null` for the calendar's own latest.
 * @param title Heading of the sheet.
 * @param applyLabel Label of the committing control.
 * @param cancelLabel Label of the cancelling control.
 * @param removeLabel Label of the removing control.
 * @param calendarLabels Words the calendar draws and reads out.
 * @param startDayOfWeek Day each week of the calendar begins on.
 * @param onDateSelected Called with the committed date. The caller closes the sheet.
 * @param onRemove Called when the date in force is removed, or `null` to offer no removal. Offered
 *   only when there is a date.
 */
@Composable
private fun BirthdaySheet(
    controller: FlowModalController,
    date: LocalDate?,
    minDate: LocalDate?,
    maxDate: LocalDate?,
    title: String,
    applyLabel: String,
    cancelLabel: String,
    removeLabel: String,
    calendarLabels: CalendarLabels,
    startDayOfWeek: DayOfWeek,
    onDateSelected: (LocalDate) -> Unit,
    onRemove: (() -> Unit)?,
) {
    val bounds = CalendarBounds.of(minDate = minDate, maxDate = maxDate)
    val calendar = rememberCalendarState(initialMonth = date?.yearMonth, bounds = bounds)

    var marked by remember { mutableStateOf(date) }

    // Keyed on visibility alone with the date read as a snapshot: visibility stays true through the
    // close animation, so keying on the date too would re-run this the instant a date is committed
    // and scroll the calendar on its way out.
    val inForce by rememberUpdatedState(date)
    val visible = controller.isVisible
    LaunchedEffect(visible) {
        if (!visible) return@LaunchedEffect
        val opening = BirthdaySheetOpening.of(date = inForce, today = calendar.today, bounds = bounds)
        marked = opening.mark
        calendar.scrollTo(opening.month)
    }

    val removable = date != null && onRemove != null

    FlowModal(
        controller = controller,
        header = {
            TopBar(
                title = title,
                modifier = Modifier.fillMaxWidth(),
                size = TopBarSize.Modal,
                style = TopBarStyle.Modal,
                windowInsets = FlowModalHeaderInsets,
                titleAlignment = TopBarTitleAlignment.Center,
            )
        },
        footer = {
            FlowModalActionBar(
                confirmText = applyLabel,
                onConfirm = { marked?.let(onDateSelected) },
                enabled = marked != null,
                leading = {
                    // Only a date in force can be removed; an unsaved mark is simply not saved.
                    if (removable) {
                        FlowModalSecondaryAction(text = removeLabel, onClick = { onRemove?.invoke() })
                    } else {
                        FlowModalSecondaryAction(text = cancelLabel, onClick = { controller.hide() })
                    }
                },
            )
        },
    ) {
        Calendar(
            state = calendar,
            selection = CalendarSelection.Single(marked),
            onDateClick = { clicked -> marked = clicked },
            startDayOfWeek = startDayOfWeek,
            labels = calendarLabels,
        )
    }
}

/**
 * What the birthday sheet shows as it opens.
 *
 * @property mark Day marked as chosen: the date in force, or `null` when there is none, so the commit
 *   waits for a tap rather than offering a day nobody picked.
 * @property month Month the calendar scrolls to: the date in force's, else the month of today pulled
 *   inside the bounds.
 */
internal data class BirthdaySheetOpening(
    val mark: LocalDate?,
    val month: YearMonth,
) {
    /** Construction from the sheet's inputs. */
    companion object {
        /**
         * Works out how the sheet opens.
         *
         * @param date Date in force, or `null` when none is set.
         * @param today Today, in the calendar's time zone.
         * @param bounds Days the calendar offers.
         * @return The opening mark and month.
         */
        fun of(date: LocalDate?, today: LocalDate, bounds: CalendarBounds): BirthdaySheetOpening =
            BirthdaySheetOpening(
                mark = date,
                month = date?.yearMonth ?: bounds.coerce(today.yearMonth),
            )
    }
}

/**
 * Writes a date as British English does, "12 March 1990" — the default for [BirthdayRow]'s
 * `formatDate`, meant to be replaced by the application's locale-aware formatter.
 *
 * @param date Date to write.
 * @return The date in words.
 */
internal fun englishDate(date: LocalDate): String =
    "${date.day} ${EnglishMonths[date.month.ordinal]} ${date.year}"

/** Month names in calendar order, indexed by `Month.ordinal`. */
private val EnglishMonths = listOf(
    "January", "February", "March", "April", "May", "June",
    "July", "August", "September", "October", "November", "December",
)
