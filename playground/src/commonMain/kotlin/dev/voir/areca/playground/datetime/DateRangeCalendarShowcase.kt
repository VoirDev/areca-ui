package dev.voir.areca.playground.datetime

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.datetime.calendar.CalendarBounds
import dev.voir.areca.datetime.calendar.CalendarDateRange
import dev.voir.areca.datetime.calendar.CalendarDateRule
import dev.voir.areca.datetime.calendar.MonthHeader
import dev.voir.areca.datetime.calendar.WeekdayHeader
import dev.voir.areca.datetime.calendar.range.BlockedDayPolicy
import dev.voir.areca.datetime.calendar.range.DateRangeCalendar
import dev.voir.areca.datetime.calendar.range.DateRangeConstraints
import dev.voir.areca.datetime.calendar.range.DateRangeLabels
import dev.voir.areca.datetime.calendar.range.DateRangeSelection
import dev.voir.areca.datetime.calendar.rememberCalendarState
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.Anatomy
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlinx.datetime.yearMonth
import kotlin.time.Clock

/** Showcase for [DateRangeCalendar]. */
@Composable
fun DateRangeCalendarShowcase() = ShowcaseScaffold(
    title = "Date range calendar",
    summary = "Two-tap range picking on the month grid, with minimum and maximum lengths, " +
        "excluded days that a range may stop at or step over, and a preview under the pointer.",
    module = "datetime",
) {
    val today = remember { Clock.System.todayIn(TimeZone.currentSystemDefault()) }

    Playground {
        val minDays by enumControl(
            key = "minDays",
            default = 1,
            options = listOf(1, 2, 3),
            optionLabel = { "$it day${if (it == 1) "" else "s"}" },
            description = "Shortest range that may be completed, counting both ends. Two and " +
                "above refuse a single-day range, which is why there is no separate flag for it.",
        )
        val maxDays by enumControl(
            key = "maxDays",
            default = 0,
            options = listOf(0, 7, 14),
            optionLabel = { if (it == 0) "none" else "$it days" },
            description = "Longest range that may be completed. Everything past it greys out as " +
                "soon as the first end is picked.",
        )
        val weekendsBlocked by booleanControl(
            key = "block weekends",
            default = false,
            description = "A CalendarDateRule refusing Saturdays and Sundays.",
        )
        val blockedPolicy by enumControl(
            key = "blockedDays",
            default = BlockedDayPolicy.StopAtBlocked,
            options = listOf(BlockedDayPolicy.StopAtBlocked, BlockedDayPolicy.AllowGaps),
            optionLabel = { if (it == BlockedDayPolicy.StopAtBlocked) "Stop at blocked" else "Allow gaps" },
            description = "Whether a range may step over a refused day, or has to stop at the " +
                "first one after its start.",
        )
        val showAdjacent by booleanControl(
            key = "showAdjacentMonths",
            default = true,
            description = "Fill the edge cells with the neighbouring months' dates, so a range " +
                "reads as continuous across a month boundary.",
        )
        val readOnly by booleanControl(
            key = "read-only",
            default = false,
            description = "Without onSelectionChange nothing is tappable and nothing previews.",
        )

        preview {
            var selection by remember {
                mutableStateOf<DateRangeSelection>(DateRangeSelection.Empty)
            }

            val constraints = DateRangeConstraints(
                dateRule = if (weekendsBlocked) WeekdaysOnly else CalendarDateRule.All,
                minDays = minDays,
                maxDays = maxDays.takeIf { it > 0 },
                blockedDays = blockedPolicy,
            )

            RangeFrame {
                Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg)) {
                    DateRangeCalendar(
                        selection = selection,
                        onSelectionChange = if (readOnly) null else { next -> selection = next },
                        constraints = constraints,
                        showAdjacentMonths = showAdjacent,
                    )

                    SelectionSummary(selection)
                }
            }
        }
    }

    Anatomy {
        slot(
            name = "day",
            description = "One cell, handed a DateRangeDayState: the calendar's own day plus " +
                "where it sits between the ends and whether it is only being previewed.",
        ) {
            RangeFrame {
                DateRangeCalendar(
                    selection = DateRangeSelection.Complete(
                        CalendarDateRange(today, today.plusDays(4)),
                    ),
                    onSelectionChange = null,
                )
            }
        }
        slot(
            name = "header",
            description = "The calendar's own month header, unchanged. Paging, the month and year " +
                "wheel and the bounds all behave exactly as they do without a range.",
        ) {
            RangeFrame {
                DateRangeCalendar(
                    selection = DateRangeSelection.Empty,
                    onSelectionChange = null,
                    weekdayHeader = null,
                    header = { MonthHeader() },
                )
            }
        }
        slot(
            name = "weekdayHeader",
            description = "The Mo/Tu/We row, also unchanged.",
            ) {
            RangeFrame {
                DateRangeCalendar(
                    selection = DateRangeSelection.Empty,
                    onSelectionChange = null,
                    header = null,
                    weekdayHeader = { WeekdayHeader() },
                )
            }
        }
    }

    Variants(
        title = "Picking a range",
        description = "The three states of a selection. Tap to move between them: a second range " +
            "always starts from scratch rather than editing the last one.",
        layout = VariantLayout.Vertical,
    ) {
        variant("Nothing picked", detail = "DateRangeSelection.Empty") {
            RangeFrame {
                DateRangeCalendar(
                    selection = DateRangeSelection.Empty,
                    onSelectionChange = null,
                )
            }
        }
        variant("One end picked", detail = "DateRangeSelection.Started") {
            RangeFrame {
                DateRangeCalendar(
                    selection = DateRangeSelection.Started(today.plusDays(2)),
                    onSelectionChange = null,
                )
            }
        }
        variant("Both ends picked", detail = "DateRangeSelection.Complete") {
            RangeFrame {
                DateRangeCalendar(
                    selection = DateRangeSelection.Complete(
                        CalendarDateRange(today.plusDays(2), today.plusDays(9)),
                    ),
                    onSelectionChange = null,
                )
            }
        }
    }

    States(layout = VariantLayout.Vertical) {
        variant(
            name = "Half-picked, with a maximum",
            detail = "maxDays = 7 — everything past the seventh day is greyed",
        ) {
            var selection by remember {
                mutableStateOf<DateRangeSelection>(DateRangeSelection.Started(today))
            }
            RangeFrame {
                DateRangeCalendar(
                    selection = selection,
                    onSelectionChange = { selection = it },
                    constraints = DateRangeConstraints(maxDays = 7),
                )
            }
        }
        variant(name = "Single day", detail = "minDays = 1 — tapping one day twice") {
            RangeFrame {
                DateRangeCalendar(
                    selection = DateRangeSelection.Complete(CalendarDateRange(today, today)),
                    onSelectionChange = null,
                )
            }
        }
        variant(name = "Ends that became unavailable", detail = "A range whose days were taken") {
            RangeFrame {
                DateRangeCalendar(
                    selection = DateRangeSelection.Complete(
                        CalendarDateRange(today.plusDays(1), today.plusDays(5)),
                    ),
                    onSelectionChange = null,
                    constraints = DateRangeConstraints(
                        dateRule = CalendarDateRule.blocking(
                            setOf(today.plusDays(1), today.plusDays(5)),
                        ),
                    ),
                )
            }
        }
        variant(name = "Read-only", detail = "onSelectionChange = null") {
            RangeFrame {
                DateRangeCalendar(
                    selection = DateRangeSelection.Complete(
                        CalendarDateRange(today, today.plusDays(3)),
                    ),
                    onSelectionChange = null,
                )
            }
        }
    }

    EdgeCases {
        case(
            name = "A range crossing a month boundary",
            expectation = "With showAdjacentMonths the band runs into the spill-over cells and " +
                "the range reads as one. Its number is muted; the band is not.",
        ) {
            val monthEnd = remember(today) {
                today.yearMonth.firstDay.plusDays(today.yearMonth.numberOfDays - 1)
            }
            RangeFrame {
                DateRangeCalendar(
                    selection = DateRangeSelection.Complete(
                        CalendarDateRange(monthEnd.plusDays(-2), monthEnd.plusDays(3)),
                    ),
                    onSelectionChange = null,
                    showAdjacentMonths = true,
                )
            }
        }
        case(
            name = "A minimum stay",
            expectation = "Pick a start: the two days after it grey out, because a three-day " +
                "range cannot end there. The days before it stay available — tapping one starts " +
                "again rather than filling backwards.",
        ) {
            var selection by remember {
                mutableStateOf<DateRangeSelection>(DateRangeSelection.Started(today.plusDays(8)))
            }
            RangeFrame {
                DateRangeCalendar(
                    selection = selection,
                    onSelectionChange = { selection = it },
                    constraints = DateRangeConstraints(minDays = 3),
                )
            }
        }
        case(
            name = "A blocked day interrupts the stay",
            expectation = "One day is taken. Pick a start before it and everything beyond it " +
                "greys out too — the days past a blockage are not merely unavailable, they are " +
                "unreachable.",
        ) {
            var selection by remember {
                mutableStateOf<DateRangeSelection>(DateRangeSelection.Started(today.plusDays(1)))
            }
            RangeFrame {
                DateRangeCalendar(
                    selection = selection,
                    onSelectionChange = { selection = it },
                    constraints = DateRangeConstraints(
                        dateRule = CalendarDateRule.blocking(setOf(today.plusDays(5))),
                        blockedDays = BlockedDayPolicy.StopAtBlocked,
                    ),
                )
            }
        }
        case(
            name = "A blocked day the range steps over",
            expectation = "The same blocked day under AllowGaps: the range spans it, the day " +
                "stays greyed inside the band, and it still cannot be an end.",
        ) {
            RangeFrame {
                DateRangeCalendar(
                    selection = DateRangeSelection.Complete(
                        CalendarDateRange(today.plusDays(1), today.plusDays(9)),
                    ),
                    onSelectionChange = null,
                    constraints = DateRangeConstraints(
                        dateRule = CalendarDateRule.blocking(setOf(today.plusDays(5))),
                        blockedDays = BlockedDayPolicy.AllowGaps,
                    ),
                )
            }
        }
        case(
            name = "Weekdays only",
            expectation = "Under StopAtBlocked a stay cannot cross a weekend, so a range picked " +
                "on a Thursday reaches Friday and no further.",
        ) {
            var selection by remember {
                mutableStateOf<DateRangeSelection>(DateRangeSelection.Empty)
            }
            RangeFrame {
                DateRangeCalendar(
                    selection = selection,
                    onSelectionChange = { selection = it },
                    constraints = DateRangeConstraints(dateRule = WeekdaysOnly),
                )
            }
        }
        case(
            name = "Bounded paging",
            expectation = "Sixty days of calendar and nothing either side. A range cannot run " +
                "past the last page, and the chevrons disable at both ends.",
        ) {
            var selection by remember {
                mutableStateOf<DateRangeSelection>(DateRangeSelection.Empty)
            }
            RangeFrame {
                DateRangeCalendar(
                    selection = selection,
                    onSelectionChange = { selection = it },
                    state = rememberCalendarState(
                        bounds = CalendarBounds(today, today.plusDays(60)),
                    ),
                )
            }
        }
    }
}

/**
 * The one line under a preview saying where the user is.
 *
 * Spells out the state machine in words, because the whole component is three states and a grid
 * cannot show which of them it is in when nothing is picked.
 *
 * @param selection Selection to describe.
 */
@Composable
private fun SelectionSummary(selection: DateRangeSelection) {
    val text = when (selection) {
        DateRangeSelection.Empty -> "Pick a start date"
        is DateRangeSelection.Started -> "Pick an end date, or tap an earlier day to start again"
        is DateRangeSelection.Complete ->
            "${selection.range.start} to ${selection.range.endInclusive} " +
                "· ${DateRangeLabels.English.length(selection.range)}"
    }

    Text(text = text, variant = TextVariant.BodySmall, color = TextColor.Muted)
}

/**
 * Holds a preview to the width a calendar is actually built for.
 *
 * Day cells are square and split the width evenly, so a calendar stretched across a desktop
 * showcase page draws enormous cells — and a range band wide enough to lose the number inside it.
 *
 * @param width Width to hold the preview to.
 * @param content The calendar to frame.
 */
@Composable
private fun RangeFrame(
    width: Dp = PreviewWidth,
    content: @Composable () -> Unit,
) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Box(modifier = Modifier.width(width)) { content() }
    }
}

/** Width previews are drawn at — a typical phone sheet or desktop dialog. */
private val PreviewWidth = 340.dp

/** Refuses Saturdays and Sundays, for the blocked-day examples. */
private val WeekdaysOnly = CalendarDateRule.byDayOfWeek {
    it != DayOfWeek.SATURDAY && it != DayOfWeek.SUNDAY
}

/**
 * Adds whole days.
 *
 * @param days Days to add, negative to go back.
 * @return The receiver shifted by [days].
 */
private fun LocalDate.plusDays(days: Int): LocalDate =
    LocalDate.fromEpochDays(toEpochDays() + days)
