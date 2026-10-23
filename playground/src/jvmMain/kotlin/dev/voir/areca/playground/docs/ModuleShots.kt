package dev.voir.areca.playground.docs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.voir.areca.charts.BarChart
import dev.voir.areca.charts.BarChartBar
import dev.voir.areca.charts.BarChartFill
import dev.voir.areca.charts.BarChartGroup
import dev.voir.areca.charts.BarChartOptions
import dev.voir.areca.charts.BarChartSize
import dev.voir.areca.charts.BarChartYAxisLabel
import dev.voir.areca.charts.ChartLegend
import dev.voir.areca.charts.ChartLegendItem
import dev.voir.areca.charts.ChartSwatch
import dev.voir.areca.charts.ColumnChart
import dev.voir.areca.charts.ColumnChartBar
import dev.voir.areca.charts.ColumnChartColumn
import dev.voir.areca.charts.ColumnChartLayout
import dev.voir.areca.charts.ColumnChartReferenceLine
import dev.voir.areca.charts.LineChart
import dev.voir.areca.charts.LineChartMarker
import dev.voir.areca.charts.LineChartSeries
import dev.voir.areca.charts.LineChartStroke
import dev.voir.areca.charts.LineChartTooltip
import dev.voir.areca.charts.LineChartXLabel
import dev.voir.areca.charts.LineChartYLabel
import dev.voir.areca.charts.MeterBar
import dev.voir.areca.charts.StackedBarChart
import dev.voir.areca.charts.StackedBarChartStyle
import dev.voir.areca.charts.StackedBarSegment
import dev.voir.areca.datetime.DateTimeField
import dev.voir.areca.datetime.TimeFormat
import dev.voir.areca.datetime.TimePicker
import dev.voir.areca.datetime.calendar.Calendar
import dev.voir.areca.datetime.calendar.CalendarDateRange
import dev.voir.areca.datetime.calendar.CalendarSelection
import dev.voir.areca.datetime.calendar.range.DateRangeCalendar
import dev.voir.areca.datetime.calendar.range.DateRangeSelection
import dev.voir.areca.datetime.calendar.rememberCalendarState
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.button.ButtonStyle
import dev.voir.areca.foundation.field.FieldGroup
import dev.voir.areca.foundation.field.TextField
import dev.voir.areca.foundation.layout.Card
import dev.voir.areca.foundation.layout.CardStyle
import dev.voir.areca.foundation.stat.Stat
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.keyboard.KeyboardController
import dev.voir.areca.keyboard.KeyboardHost
import dev.voir.areca.keyboard.key.KeyboardGrids
import dev.voir.areca.keyboard.keyboardTarget
import dev.voir.areca.keyboard.layout.KeyboardLayout
import dev.voir.areca.keyboard.rememberKeyboardTarget
import dev.voir.areca.playground.crop.CropPreset
import dev.voir.areca.playground.crop.CropScreen
import dev.voir.areca.playground.crop.SamplePhoto
import dev.voir.areca.playground.foundation.settings.PrivacySettingsSample
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.horizontalContentInsets
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.YearMonth
import kotlin.time.Clock
import kotlin.time.Instant

/** The day every dated image is drawn on, so "today" does not follow the machine. */
private val Today = LocalDate(2026, 10, 23)

/** A clock stopped on [Today]. */
private val DocClock = object : Clock {
    override fun now(): Instant = Instant.parse("2026-10-23T09:00:00Z")
}

/** Six months of spending, the latest still running. */
private val Months = listOf("May", "Jun", "Jul", "Aug", "Sep", "Oct")
private val MonthSpent = listOf(1840f, 2530f, 3050f, 2260f, 2090f, 1720f)

/** A whole euro amount with a thousands comma: `€2,340`. */
private fun euros(value: Float): String {
    val whole = value.toInt()
    return if (whole >= 1000) "€${whole / 1000},${(whole % 1000).toString().padStart(3, '0')}" else "€$whole"
}

/** Six months as columns, the latest selected and labelled "Now". */
@Composable
private fun spendingColumns(): List<ColumnChartColumn<Int>> = Months.indices.map { i ->
    val latest = i == Months.lastIndex
    ColumnChartColumn(
        key = i,
        bars = listOf(ColumnChartBar.of(MonthSpent[i], ArecaTheme.colors.bgBrandBase)),
        label = if (latest) "Now" else Months[i],
        sublabel = if (latest) "so far" else null,
        valueLabel = euros(MonthSpent[i]),
        contentDescription = "${Months[i]}: spent ${euros(MonthSpent[i])}",
    )
}

/** Spending by month as a column chart, against the usual month. */
@Composable
private fun SpendingColumns() {
    ColumnChart(
        columns = spendingColumns(),
        modifier = Modifier.fillMaxWidth(),
        selectedKey = Months.lastIndex,
        onSelect = {},
        referenceLines = listOf(ColumnChartReferenceLine(value = 2200f, label = "usual")),
        layout = ColumnChartLayout.Fill,
    )
}

/** `overview`: a settings list, a field and buttons beside a period chart and a tab bar. */
internal val OverviewShot = DocShot(name = "overview", width = 720.dp) {
    DocColumns(
        {
            PrivacySettingsSample()
            DocGroup {
                FieldGroup(label = "Display name", assistive = "Shown on invoices you send.") { field ->
                    TextField(
                        value = "Ada Lovelace",
                        onValueChange = {},
                        field = field,
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md)) {
                    Button(text = "Save changes", onClick = {}, modifier = Modifier.weight(1f))
                    Button(
                        text = "Cancel",
                        onClick = {},
                        style = ButtonStyle.plain(ActionColor.Neutral),
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        },
        {
            DocGroup {
                Card(modifier = Modifier.fillMaxWidth(), style = CardStyle.Outlined) { padding ->
                    Column(
                        modifier = Modifier.padding(padding),
                        verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xl),
                    ) {
                        Stat(label = "Spent in October", value = "€1,720", note = "€480 under your usual month")
                        SpendingColumns()
                    }
                }
            }
            SampleTabBar(selected = SampleDestination.Home, floating = true)
        },
    )
}

/** Running totals of a month's spending, one per day. */
private fun runningTotal(days: Int, daily: List<Float>): List<Float> {
    var total = 0f
    return List(days) { day ->
        total += daily[day % daily.size]
        total
    }
}

private val DailySpend = listOf(84f, 0f, 32f, 141f, 12f, 0f, 260f, 46f, 18f, 0f, 75f, 120f, 9f, 64f, 38f)
private val LastMonth = runningTotal(30, DailySpend.reversed())
private val ThisMonth = runningTotal(23, DailySpend)

/** Revenue by quarter, this year against last. */
private val QuarterRevenue = listOf(42f to 28f, 58f to 36f, 71f to 44f, 64f to 52f)

/** `charts`: column, line, bar and stacked bar charts, meter bars and legends. */
internal val ChartsShot = DocShot(name = "charts", width = 720.dp) {
    val c = ArecaTheme.colors
    DocColumns(
        {
            DocGroup("Column chart") {
                SpendingColumns()
            }
            DocGroup("Bar chart") {
                BarChart(
                    groups = QuarterRevenue.mapIndexed { i, (now, before) ->
                        BarChartGroup(
                            label = "Q${i + 1}",
                            bars = listOf(
                                BarChartBar(now, fill = BarChartFill.Solid(c.bgBrandBase)),
                                BarChartBar(before, fill = BarChartFill.Solid(c.bgInfoBase)),
                            ),
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    size = BarChartSize.Small,
                    options = BarChartOptions(maxValue = 80f),
                    yAxisLabels = listOf(
                        BarChartYAxisLabel(0f, "0"),
                        BarChartYAxisLabel(40f, "€40k"),
                        BarChartYAxisLabel(80f, "€80k"),
                    ),
                )
                ChartLegend(
                    items = listOf(
                        ChartLegendItem("2026", ChartSwatch.Square(c.bgBrandBase)),
                        ChartLegendItem("2025", ChartSwatch.Square(c.bgInfoBase)),
                    ),
                )
            }
        },
        {
            DocGroup("Line chart") {
                LineChart(
                    series = listOf(
                        LineChartSeries(
                            LastMonth,
                            "September",
                            color = c.fgMuted,
                            stroke = LineChartStroke.Dashed,
                            endMarker = null,
                        ),
                        LineChartSeries(
                            LastMonth.take(23),
                            "September by today",
                            color = c.fgMuted,
                            stroke = LineChartStroke.Dashed,
                            endMarker = LineChartMarker.Hollow,
                        ),
                        LineChartSeries(ThisMonth, "October", color = c.bgDangerBase, area = true),
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    xLabels = listOf(LineChartXLabel(0, "1 Oct"), LineChartXLabel(14, "15"), LineChartXLabel(29, "31")),
                    yLabels = listOf(LineChartYLabel(0f, "0"), LineChartYLabel(1000f, "€1k"), LineChartYLabel(2000f, "€2k")),
                    rule = 22,
                    tooltip = LineChartTooltip(series = 2, index = 22, text = "${euros(ThisMonth.last())} by the 23rd"),
                    maxValue = 2000f,
                )
                ChartLegend(
                    items = listOf(
                        ChartLegendItem("October", ChartSwatch.Line(c.bgDangerBase)),
                        ChartLegendItem("September", ChartSwatch.DashedLine(c.fgMuted)),
                    ),
                )
            }
            DocGroup("Stacked bar and meter bars") {
                val palette = StackedBarChartStyle.Default.palette
                val shares = listOf("Rent" to 0.42f, "Groceries" to 0.21f, "Transport" to 0.12f, "Other" to 0.25f)
                StackedBarChart(
                    segments = shares.map { (label, share) -> StackedBarSegment(fraction = share, label = label) },
                    modifier = Modifier.fillMaxWidth(),
                )
                ChartLegend(
                    items = shares.mapIndexed { i, (label, _) ->
                        ChartLegendItem(label, ChartSwatch.Square(palette[i % palette.size]))
                    },
                )
                MeterRow("Food and dining", "€612", fraction = 0.72f, usual = 0.55f)
                MeterRow("Transport", "€184", fraction = 0.31f, usual = 0.4f)
            }
        },
    )
}

/**
 * A category's spending against its usual: name and amount over a meter bar.
 *
 * @param name The category.
 * @param amount Spent this month.
 * @param fraction Spent, as a share of the largest category.
 * @param usual The usual month, on the same scale.
 */
@Composable
private fun MeterRow(name: String, amount: String, fraction: Float, usual: Float) {
    Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.sm)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(name, variant = TextVariant.LabelMedium)
            Text(amount, variant = TextVariant.LabelMedium)
        }
        MeterBar(
            fraction = fraction,
            marker = usual,
            modifier = Modifier.fillMaxWidth(),
            contentDescription = "$name, $amount",
        )
    }
}

/** `datetime`: a calendar with a day picked, a date range and a time picker. */
internal val DateTimeShot = DocShot(name = "datetime", width = 720.dp) {
    DocColumns(
        {
            DocGroup("Calendar") {
                Calendar(
                    state = rememberCalendarState(
                        initialMonth = YearMonth(2026, 10),
                        clock = DocClock,
                        timeZone = TimeZone.UTC,
                    ),
                    selection = CalendarSelection.Single(LocalDate(2026, 10, 27)),
                    onDateClick = {},
                    fixedWeekCount = false,
                )
            }
            DocGroup("Time picker") {
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    TimePicker(initialTime = LocalTime(9, 30), onTimeChange = {}, format = TimeFormat.Hours24)
                }
            }
        },
        {
            DocGroup("Date range") {
                DateRangeCalendar(
                    selection = DateRangeSelection.Complete(
                        CalendarDateRange(LocalDate(2026, 10, 12), LocalDate(2026, 10, 18)),
                    ),
                    onSelectionChange = {},
                    fixedWeekCount = false,
                    state = rememberCalendarState(
                        initialMonth = YearMonth(2026, 10),
                        clock = DocClock,
                        timeZone = TimeZone.UTC,
                    ),
                )
            }
            DocGroup("Date and time field") {
                FieldGroup(label = "Remind me") { field ->
                    DateTimeField(
                        value = LocalDateTime(2026, 10, 27, 9, 30),
                        onValueChange = {},
                        field = field,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        },
    )
}

/** `crop`: the crop screen on a photo, as the playground's crop recipe builds it. */
internal val CropShot = DocShot(name = "crop", width = 390.dp, height = 720.dp, padded = false) {
    CropScreen(
        photo = SamplePhoto,
        spec = CropPreset.Photo.spec,
        title = CropPreset.Photo.title,
        onCancel = {},
        onDone = {},
        windowInsets = WindowInsets(0, 0, 0, 0),
    )
}

/** `keyboard`: an amount screen with the calculator keyboard open under it. */
internal val KeyboardShot = DocShot(name = "keyboard", width = 390.dp, height = 560.dp, padded = false) {
    val target = rememberKeyboardTarget(key = "amount")
    val controller = remember(target) {
        KeyboardController().apply { open(target, scroll = false, stealFocus = false) }
    }
    KeyboardHost(
        keyboardContent = {
            KeyboardLayout(
                grid = KeyboardGrids.calculator(onAction = {}, decimalSeparator = '.'),
                windowInsets = WindowInsets(0, 0, 0, 0),
                header = {
                    Text(
                        text = "€1,234.56",
                        modifier = Modifier.fillMaxWidth(),
                        variant = TextVariant.HeadingSmall,
                        textAlign = TextAlign.End,
                    )
                },
            )
        },
        controller = controller,
        compressContent = true,
    ) {
        Column(
            modifier = Modifier.fillMaxSize().horizontalContentInsets().padding(top = ArecaTheme.spacing.xl3),
            verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xs),
        ) {
            Text("Amount", variant = TextVariant.BodySmall, color = TextColor.Subtle)
            Text(
                text = "€1,234.56",
                modifier = Modifier.fillMaxWidth().keyboardTarget(controller, target),
                variant = TextVariant.HeadingLarge,
            )
            Text("Corner Grocery · Groceries", variant = TextVariant.BodySmall, color = TextColor.Muted)
        }
    }
}
