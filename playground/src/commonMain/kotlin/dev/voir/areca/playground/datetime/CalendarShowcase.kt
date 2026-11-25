package dev.voir.areca.playground.datetime

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import dev.voir.areca.datetime.calendar.CalendarDayShape
import dev.voir.areca.datetime.calendar.CalendarHeatDay
import dev.voir.areca.datetime.calendar.CalendarHeatLegend
import dev.voir.areca.datetime.calendar.CalendarLabels
import dev.voir.areca.datetime.calendar.CalendarMarker
import dev.voir.areca.datetime.calendar.CalendarSelection
import dev.voir.areca.datetime.calendar.CalendarSize
import dev.voir.areca.datetime.calendar.CalendarStyle
import dev.voir.areca.datetime.calendar.MonthHeader
import dev.voir.areca.datetime.calendar.MonthYearPicker
import dev.voir.areca.datetime.calendar.TodayButton
import dev.voir.areca.datetime.calendar.WeekdayHeader
import dev.voir.areca.datetime.calendar.calendarHeatLevel
import dev.voir.areca.datetime.calendar.plus
import dev.voir.areca.datetime.calendar.rememberCalendarState
import dev.voir.areca.foundation.list.ListItem
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.Gift
import dev.voir.areca.icons.Plane
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.Anatomy
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme
import kotlin.time.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.YearMonth
import kotlinx.datetime.number
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
    summary = "Month grid with day, month and year levels, markers under each day, single or " +
        "multiple selection, selection rules and a paging range.",
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
                "the ends and the month tiles refuse the months outside.",
        )
        val marker by enumControl(
            key = "markers",
            default = DemoMarker.Dots,
            options = DemoMarker.entries,
            description = "What each day shows under its number. Any value but None reserves the row.",
        )
        val shape by enumControl(
            key = "dayShape",
            default = CalendarDayShape.Circle,
            options = CalendarDayShape.entries,
            description = "Circle fills around the number; Tile fills the whole cell, marker included.",
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

            val markers = rememberDemoMarkers(marker)
            val monthMarkers = rememberDemoMonthMarkers(marker)
            CalendarFrame {
                Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg)) {
                    Calendar(
                        state = rememberCalendarState(bounds = bounds),
                        style = CalendarStyle.default(dayShape = shape),
                        markers = markers,
                        monthMarkers = monthMarkers,
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
            description = "The title on the left, back and forward on the right. Tapping the month " +
                "steps out to the months, and tapping the year to the years; the chevrons step by " +
                "the level shown.",
        ) {
            CalendarFrame {
                Calendar(weekdayHeader = null, header = { MonthHeader() })
            }
        }
        slot(
            name = "weekdayHeader",
            description = "The Mo/Tu/We row. Fades out rather than being removed while months or " +
                "years show, so the calendar keeps one height.",
        ) {
            CalendarFrame {
                Calendar(header = null, weekdayHeader = { WeekdayHeader() })
            }
        }
        slot(
            name = "day",
            description = "One cell, handed a CalendarDayState. Draw through CalendarDay; what goes " +
                "under the number comes from markers, or CalendarDay(marker = …) for one day.",
        ) {
            CalendarFrame {
                Calendar(
                    selection = CalendarSelection.Single(today),
                    markers = rememberDemoMarkers(DemoMarker.Dots),
                )
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
        title = "Months and years",
        description = "The levels the title steps out to. A tap on a tile is the choice; the tile " +
            "already shown, or Escape, returns unchanged.",
        layout = VariantLayout.Vertical,
    ) {
        variant("Months", detail = "CalendarLevel.Months, with monthMarkers") {
            val state = rememberCalendarState()
            LaunchedEffect(state) { state.zoomOut() }
            CalendarFrame {
                Calendar(
                    state = state,
                    selection = CalendarSelection.Single(today),
                    markers = rememberDemoMarkers(DemoMarker.Dots),
                    monthMarkers = rememberDemoMonthMarkers(DemoMarker.Dots),
                )
            }
        }
        variant("Years", detail = "CalendarLevel.Years") {
            val state = rememberCalendarState()
            LaunchedEffect(state) {
                state.zoomOut()
                state.zoomOut()
            }
            CalendarFrame {
                Calendar(state = state, selection = CalendarSelection.Single(today))
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
        variant("Wheels on their own", detail = "MonthYearPicker, for a far jump in the app's own sheet") {
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
    }

    Variants(
        title = "Markers",
        description = "One row under every number, kept whether or not a day has a marker. Under " +
            "a Circle the marker keeps its colours below the fill; inside a filled Tile it takes " +
            "the day's ink.",
        layout = VariantLayout.Vertical,
    ) {
        DemoMarker.entries.filter { it != DemoMarker.None }.forEach { kind ->
            val shape = if (kind == DemoMarker.Label || kind == DemoMarker.Count) {
                CalendarDayShape.Tile
            } else {
                CalendarDayShape.Circle
            }
            variant(kind.name, detail = "CalendarMarker.${kind.name}, dayShape = $shape") {
                var picked by remember { mutableStateOf(today) }
                CalendarFrame {
                    Calendar(
                        selection = CalendarSelection.Single(picked),
                        onDateClick = { picked = it },
                        style = CalendarStyle.default(dayShape = shape),
                        markers = rememberDemoMarkers(kind),
                    )
                }
            }
        }
    }

    Recipes {
        recipe("Event calendar", description = "A dot per category under each day, the day's events below.") {
            var picked by remember { mutableStateOf(today) }
            CalendarFrame {
                Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md)) {
                    Calendar(
                        selection = CalendarSelection.Single(picked),
                        onDateClick = { picked = it },
                        markers = rememberDemoMarkers(DemoMarker.Dots),
                        monthMarkers = rememberDemoMonthMarkers(DemoMarker.Dots),
                    )
                    val events = demoEvents(picked)
                    if (events.isEmpty()) {
                        Text(text = "Nothing planned", variant = TextVariant.BodySmall, color = TextColor.Muted)
                    }
                    events.forEachIndexed { index, category ->
                        ListItem(
                            title = { Text(category.title) },
                            value = { Text("${9 + index * 2}:00") },
                        )
                    }
                }
            }
        }
        recipe("Fare picker", description = "The price inside a tile, the cheapest in green, from today.") {
            var checkIn by remember { mutableStateOf<LocalDate?>(null) }
            CalendarFrame {
                Calendar(
                    selection = CalendarSelection.Single(checkIn),
                    onDateClick = { checkIn = it },
                    dateRule = CalendarDateRule { it >= today },
                    style = CalendarStyle.default(dayShape = CalendarDayShape.Tile),
                    markers = rememberDemoMarkers(DemoMarker.Label),
                    monthMarkers = rememberDemoMonthMarkers(DemoMarker.Label),
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
            name = "Stepping out keeps the height",
            expectation = "Tap the month, then the year: the tiles are exactly as tall as the grid " +
                "they replace, so the line below does not move.",
        ) {
            CalendarFrame {
                Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md)) {
                    Calendar(selection = CalendarSelection.Single(today))
                    Text(
                        text = "This line must not move between days, months and years.",
                        variant = TextVariant.BodySmall,
                        color = TextColor.Muted,
                    )
                }
            }
        }
        case(
            name = "Bounded paging",
            expectation = "Paging stops three months either side of today and the chevrons disable " +
                "there. The month tiles refuse the months outside, and there is no years level.",
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

/** What the demo calendars draw under each day. */
private enum class DemoMarker { None, Dots, Count, Icon, Label, Load }

/** Kinds of made-up event, each with its own colour. */
private enum class DemoCategory(val title: String) {
    Work("Design review"),
    Personal("Dinner with Sam"),
    Travel("Flight to Lisbon"),
    Health("Dentist"),
}

/**
 * Made-up events for a day: none on most days, up to five on a few, the same every run.
 *
 * @param date Day to make events for.
 * @return The categories of that day's events, in time order.
 */
private fun demoEvents(date: LocalDate): List<DemoCategory> =
    when ((date.day * 7 + date.month.number * 3) % 11) {
        4, 5 -> listOf(DemoCategory.Work)
        6 -> listOf(DemoCategory.Personal)
        7 -> listOf(DemoCategory.Work, DemoCategory.Travel)
        8 -> listOf(DemoCategory.Work, DemoCategory.Personal, DemoCategory.Health)
        9 -> listOf(DemoCategory.Work, DemoCategory.Work, DemoCategory.Personal, DemoCategory.Travel, DemoCategory.Health)
        10 -> listOf(DemoCategory.Travel)
        else -> emptyList()
    }

/** A made-up nightly fare in euros, the same every run. */
private fun demoFare(date: LocalDate): Int = 70 + (date.day * 37 + date.month.number * 11) % 110

/** Fares at or under this are the cheap ones, drawn emphasised. */
private const val CheapFare = 85

/**
 * The markers of one [DemoMarker] kind, built from the theme's colours.
 *
 * @param kind What to draw.
 * @return The calendar's `markers`, or `null` for [DemoMarker.None].
 */
@Composable
private fun rememberDemoMarkers(kind: DemoMarker): ((LocalDate) -> CalendarMarker?)? {
    val colors = ArecaTheme.colors
    val palette = mapOf(
        DemoCategory.Work to colors.bgBrandBase,
        DemoCategory.Personal to colors.bgSuccessBase,
        DemoCategory.Travel to colors.bgWarnBase,
        DemoCategory.Health to colors.bgDangerBase,
    )
    return remember(kind, palette) {
        when (kind) {
            DemoMarker.None -> null
            DemoMarker.Dots -> { date ->
                demoEvents(date).takeIf { it.isNotEmpty() }?.let { events ->
                    CalendarMarker.Dots(
                        colors = events.distinct().map { palette.getValue(it) },
                        contentDescription = "${events.size} events",
                    )
                }
            }
            DemoMarker.Count -> { date ->
                demoEvents(date).size.takeIf { it > 0 }?.let { CalendarMarker.Count(it, "$it events") }
            }
            DemoMarker.Icon -> { date ->
                val events = demoEvents(date)
                when {
                    DemoCategory.Travel in events -> CalendarMarker.Icon(ArecaIcons.Plane, palette[DemoCategory.Travel], "Flight")
                    DemoCategory.Personal in events -> CalendarMarker.Icon(ArecaIcons.Gift, palette[DemoCategory.Personal], "Party")
                    else -> null
                }
            }
            DemoMarker.Label -> { date ->
                val fare = demoFare(date)
                CalendarMarker.Label("€$fare", emphasis = fare <= CheapFare, contentDescription = "€$fare a night")
            }
            DemoMarker.Load -> { date ->
                demoEvents(date).size.takeIf { it > 0 }?.let { count ->
                    CalendarMarker.Load(
                        fraction = count / 5f,
                        color = if (count >= 4) colors.bgDangerBase else null,
                        contentDescription = "$count events",
                    )
                }
            }
        }
    }
}

/**
 * Month-tile markers matching [rememberDemoMarkers]: a dot for a month with events, or the
 * month's lowest fare.
 *
 * @param kind What the days draw.
 * @return The calendar's `monthMarkers`, or `null` when the kind has none.
 */
@Composable
private fun rememberDemoMonthMarkers(kind: DemoMarker): ((YearMonth) -> CalendarMarker?)? {
    val brand = ArecaTheme.colors.bgBrandBase
    return remember(kind, brand) {
        when (kind) {
            DemoMarker.None -> null
            DemoMarker.Label -> { month ->
                val lowest = (1..28).minOf { demoFare(LocalDate(month.year, month.month, it)) }
                CalendarMarker.Label("from €$lowest", contentDescription = "from €$lowest")
            }
            else -> { month ->
                val count = (1..28).sumOf { demoEvents(LocalDate(month.year, month.month, it)).size }
                if (count > 0) CalendarMarker.Dots(listOf(brand), contentDescription = "$count events") else null
            }
        }
    }
}
