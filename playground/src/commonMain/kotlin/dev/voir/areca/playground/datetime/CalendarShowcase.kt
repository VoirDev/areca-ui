package dev.voir.areca.playground.datetime

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import dev.voir.areca.datetime.calendar.Calendar
import dev.voir.areca.datetime.calendar.CalendarBounds
import dev.voir.areca.datetime.calendar.CalendarDateRule
import dev.voir.areca.datetime.calendar.CalendarHeatDay
import dev.voir.areca.datetime.calendar.CalendarHeatLegend
import dev.voir.areca.datetime.calendar.CalendarLabels
import dev.voir.areca.datetime.calendar.CalendarSelection
import dev.voir.areca.datetime.calendar.CalendarSize
import dev.voir.areca.datetime.calendar.MonthHeader
import dev.voir.areca.datetime.calendar.MonthYearPicker
import dev.voir.areca.datetime.calendar.TodayButton
import dev.voir.areca.datetime.calendar.WeekdayHeader
import dev.voir.areca.datetime.calendar.calendarHeatLevel
import dev.voir.areca.datetime.calendar.plus
import dev.voir.areca.datetime.calendar.rememberCalendarState
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.Anatomy
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme
import kotlin.time.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import kotlinx.datetime.yearMonth

/** A month of made-up daily spending, some days empty, for the heat map. */
private val DemoSpend = listOf(
    84f, 0f, 32f, 141f, 12f, 0f, 260f, 46f, 18f, 0f, 75f, 120f, 9f, 64f, 0f, 38f,
    210f, 27f, 0f, 91f, 55f, 14f, 0f, 180f, 42f, 7f, 66f, 0f, 104f, 23f, 58f,
)

/** Showcase for [Calendar]. */
@Composable
fun CalendarShowcase() = ShowcaseScaffold(
    title = "Calendar",
    summary = "Month grid with an inline month and year wheel, single or multiple selection, " +
        "selection rules and a paging range.",
    module = "datetime",
) {
    val today = remember { Clock.System.todayIn(TimeZone.currentSystemDefault()) }

    Playground {
        val multiSelect by booleanControl(
            key = "multiple selection",
            default = false,
            description = "CalendarSelection.Multiple instead of Single.",
        )
        val weekStart by enumControl(
            key = "startDayOfWeek",
            default = DayOfWeek.MONDAY,
            options = listOf(DayOfWeek.MONDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY),
            optionLabel = { it.name.lowercase().replaceFirstChar(Char::uppercase) },
            description = "Which day each row begins on.",
        )
        val showAdjacent by booleanControl(
            key = "showAdjacentMonths",
            default = false,
            description = "Fill the edge cells with the neighbouring months' dates.",
        )
        val fixedWeeks by booleanControl(
            key = "fixedWeekCount",
            default = true,
            description = "Six rows for every month, so paging never resizes the grid.",
        )
        val bounded by booleanControl(
            key = "narrow bounds",
            default = false,
            description = "Limit paging to the six months around today. The chevrons disable at " +
                "the ends and the wheel offers only those months.",
        )
        val weekendsBlocked by booleanControl(
            key = "weekdays only",
            default = false,
            description = "A CalendarDateRule refusing Saturdays and Sundays.",
        )

        preview {
            var single by remember { mutableStateOf<LocalDate?>(today) }
            var multiple by remember { mutableStateOf(setOf(today)) }

            val bounds = if (bounded) {
                CalendarBounds(today.minusMonths(3), today.plusMonths(3))
            } else {
                CalendarBounds.Default
            }

            CalendarFrame {
                Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg)) {
                    Calendar(
                        state = rememberCalendarState(bounds = bounds),
                        selection = if (multiSelect) {
                            CalendarSelection.Multiple(multiple)
                        } else {
                            CalendarSelection.Single(single)
                        },
                        dateRule = if (weekendsBlocked) WeekdaysOnly else CalendarDateRule.All,
                        startDayOfWeek = weekStart,
                        showAdjacentMonths = showAdjacent,
                        fixedWeekCount = fixedWeeks,
                        onDateClick = { date ->
                            if (multiSelect) {
                                multiple = if (date in multiple) multiple - date else multiple + date
                            } else {
                                single = date
                            }
                        },
                    )

                    Text(
                        text = if (multiSelect) {
                            "Selected: ${multiple.size} day(s)"
                        } else {
                            "Selected: ${single ?: "none"}"
                        },
                        variant = TextVariant.BodySmall,
                        color = TextColor.Muted,
                    )
                }
            }
        }
    }

    Anatomy {
        slot(
            name = "header",
            description = "The month on the left, back and forward on the right. Tapping the month " +
                "opens the wheel; its tap target wraps the label rather than spanning the header.",
        ) {
            CalendarFrame {
                Calendar(weekdayHeader = null, header = { MonthHeader() })
            }
        }
        slot(
            name = "weekdayHeader",
            description = "The Mo/Tu/We row. Fades out rather than being removed while the month " +
                "picker is open, so the calendar keeps one height.",
        ) {
            CalendarFrame {
                Calendar(header = null, weekdayHeader = { WeekdayHeader() })
            }
        }
        slot(
            name = "day",
            description = "One cell, handed a CalendarDayState. Replace it to draw event dots, " +
                "prices, or anything else under the number.",
        ) {
            CalendarFrame {
                Calendar(selection = CalendarSelection.Single(today))
            }
        }
    }

    Variants(
        title = "Selection",
        description = "The calendar draws the selection it is given; the caller decides what a tap " +
            "does to it.",
        layout = VariantLayout.Vertical,
    ) {
        variant("Single", detail = "CalendarSelection.Single") {
            var selected by remember { mutableStateOf<LocalDate?>(today) }
            CalendarFrame {
                Calendar(
                    selection = CalendarSelection.Single(selected),
                    onDateClick = { selected = it },
                )
            }
        }
        variant("Multiple", detail = "CalendarSelection.Multiple") {
            var selected by remember { mutableStateOf(setOf(today)) }
            CalendarFrame {
                Calendar(
                    selection = CalendarSelection.Multiple(selected),
                    onDateClick = {
                        selected = if (it in selected) selected - it else selected + it
                    },
                )
            }
        }
    }

    Variants(
        title = "Sizes",
        description = "Compact tightens the insets and the gaps; the day numbers stay the same size.",
        layout = VariantLayout.Vertical,
    ) {
        variant("Default", detail = "CalendarSize.Default, 340dp wide") {
            CalendarFrame {
                Calendar(size = CalendarSize.Default, selection = CalendarSelection.Single(today))
            }
        }
        variant("Compact", detail = "CalendarSize.Compact, 280dp wide") {
            CalendarFrame(width = CompactPreviewWidth) {
                Calendar(size = CalendarSize.Compact, selection = CalendarSelection.Single(today))
            }
        }
    }

    Variants(
        title = "Month and year picker",
        description = "The wheel the header opens, and the same wheel used on its own.",
        layout = VariantLayout.Vertical,
    ) {
        variant("Standalone", detail = "MonthYearPicker") {
            var month by remember { mutableStateOf(today.yearMonth) }
            CalendarFrame {
                Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md)) {
                    MonthYearPicker(
                        month = month,
                        onMonthChange = { month = it },
                        bounds = CalendarBounds.around(today, years = 60),
                    )
                    Text(
                        text = CalendarLabels.English.monthHeader(month),
                        variant = TextVariant.BodySmall,
                        color = TextColor.Muted,
                    )
                }
            }
        }
        variant("With a Today action", detail = "CalendarScope.TodayButton") {
            CalendarFrame {
                Calendar(
                    header = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xs),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            MonthHeader(modifier = Modifier.weight(1f), showControls = false)
                            TodayButton()
                        }
                    },
                    selection = CalendarSelection.Single(today),
                )
            }
        }
    }

    Variants(
        title = "Heat map",
        description = "CalendarHeatDay in the day slot: shaded by the day's figure, empty days " +
            "ringed, days to come flat, today and the pick ringed.",
        layout = VariantLayout.Vertical,
    ) {
        variant("Spending by day", detail = "CalendarHeatDay + CalendarHeatLegend + calendarHeatLevel") {
            var picked by remember { mutableStateOf(today) }
            val steps = ArecaTheme.colors.accentScale.size
            CalendarFrame {
                Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg)) {
                    Calendar(
                        selection = CalendarSelection.Single(picked),
                        onDateClick = { picked = it },
                        dateRule = CalendarDateRule { it <= today },
                        day = { day ->
                            val spent = DemoSpend[day.date.day % DemoSpend.size]
                            CalendarHeatDay(
                                day = day,
                                level = calendarHeatLevel(spent, max = 280f, steps = steps),
                                amount = if (spent > 0f) "€${spent.toInt()}" else null,
                                onClick = { picked = day.date },
                            )
                        },
                    )
                    CalendarHeatLegend(lowLabel = "€0", highLabel = "€280", emptyLabel = "nothing spent")
                }
            }
        }
    }

    EdgeCases {
        case(
            name = "Opening the picker keeps the height",
            expectation = "Tap the month: the wheel is exactly as tall as the grid it replaces, so " +
                "the line below it does not move.",
        ) {
            CalendarFrame {
                Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md)) {
                    Calendar(selection = CalendarSelection.Single(today))
                    Text(
                        text = "This line must not move when the picker opens.",
                        variant = TextVariant.BodySmall,
                        color = TextColor.Muted,
                    )
                }
            }
        }
        case(
            name = "Bounded paging",
            expectation = "Paging stops three months either side of today and the chevrons disable " +
                "there. The wheel offers only those months.",
        ) {
            CalendarFrame {
                Calendar(
                    state = rememberCalendarState(
                        bounds = CalendarBounds(today.minusMonths(3), today.plusMonths(3)),
                    ),
                    selection = CalendarSelection.Single(today),
                )
            }
        }
        case(
            name = "Week starting on Sunday",
            expectation = "Sunday is the first column and Saturday the last, with the 1st under " +
                "its own day of the week.",
        ) {
            CalendarFrame {
                Calendar(
                    startDayOfWeek = DayOfWeek.SUNDAY,
                    showAdjacentMonths = true,
                    selection = CalendarSelection.Single(today),
                )
            }
        }
        case(
            name = "Keyboard",
            expectation = "Tab lands on the chosen day only. Arrows move by day and week, Home " +
                "and End to the ends of the week, Page Up and Page Down by month (with Shift by " +
                "year), paging the grid when focus leaves the month. Weekends take focus but " +
                "refuse Enter and Space, which choose any other day.",
        ) {
            var chosen by remember { mutableStateOf<LocalDate?>(today) }
            CalendarFrame {
                Calendar(
                    selection = CalendarSelection.Single(chosen),
                    onDateClick = { chosen = it },
                    dateRule = WeekdaysOnly,
                )
            }
        }
        case(
            name = "Allow-list of days",
            expectation = "Only three days accept a tap; every other day is greyed and announced " +
                "as unavailable.",
        ) {
            val allowed = remember { setOf(today, today.plusDays(2), today.plusDays(4)) }
            var selected by remember { mutableStateOf<LocalDate?>(null) }

            CalendarFrame {
                Calendar(
                    selection = CalendarSelection.Single(selected),
                    dateRule = CalendarDateRule.allowing(allowed),
                    onDateClick = { selected = it },
                )
            }
        }
        case(
            name = "Combined rules",
            expectation = "Weekdays inside the next fortnight only — the two rules narrow each " +
                "other rather than replacing each other.",
        ) {
            var selected by remember { mutableStateOf<LocalDate?>(null) }
            val rule = remember {
                CalendarDateRule.inRange(today..today.plusDays(14)) + WeekdaysOnly
            }

            CalendarFrame {
                Calendar(
                    selection = CalendarSelection.Single(selected),
                    dateRule = rule,
                    onDateClick = { selected = it },
                )
            }
        }
        case(
            name = "Read-only",
            expectation = "Without onDateClick the grid still pages and still announces its days, " +
                "but nothing is selectable.",
        ) {
            CalendarFrame {
                Calendar(selection = CalendarSelection.Single(today))
            }
        }
        case(
            name = "No header at all",
            expectation = "A bare grid, for a calendar navigated entirely from elsewhere.",
        ) {
            CalendarFrame {
                Calendar(header = null, selection = CalendarSelection.Single(today))
            }
        }
    }
}

/**
 * Holds a preview to the width a calendar is actually built for.
 *
 * Day cells are square and split the width evenly, so a calendar stretched across a desktop showcase
 * page draws enormous cells with a two-digit number lost in the middle of each — which says nothing
 * true about how the component looks in a sheet, a dialog, or on a phone.
 *
 * @param width Width to hold the preview to.
 * @param content The calendar to frame.
 */
@Composable
private fun CalendarFrame(
    width: Dp = DefaultPreviewWidth,
    content: @Composable () -> Unit,
) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Box(modifier = Modifier.width(width)) { content() }
    }
}

/** Width most previews are drawn at — a typical phone sheet or desktop dialog. */
private val DefaultPreviewWidth = 340.dp

/** Width the Compact preview is drawn at, so the step down is visible rather than implied. */
private val CompactPreviewWidth = 280.dp

/** Refuses Saturdays and Sundays, for the rule examples. */
private val WeekdaysOnly = CalendarDateRule.byDayOfWeek {
    it != DayOfWeek.SATURDAY && it != DayOfWeek.SUNDAY
}

/**
 * Adds whole days.
 *
 * @param days Days to add.
 * @return The receiver shifted by [days].
 */
private fun LocalDate.plusDays(days: Int): LocalDate =
    LocalDate.fromEpochDays(toEpochDays() + days)

/**
 * Adds whole months.
 *
 * @param months Months to add, negative to go back.
 * @return First day of the resulting month, which is all the bounds examples need.
 */
private fun LocalDate.plusMonths(months: Int): LocalDate =
    yearMonth.plus(months, DateTimeUnit.MONTH).firstDay

/**
 * Subtracts whole months.
 *
 * @param months Months to subtract.
 * @return First day of the resulting month.
 */
private fun LocalDate.minusMonths(months: Int): LocalDate = plusMonths(-months)
