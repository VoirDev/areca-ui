package dev.voir.areca.playground

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import dev.voir.areca.charts.ChartLegend
import dev.voir.areca.charts.ChartLegendItem
import dev.voir.areca.charts.ChartSwatch
import dev.voir.areca.charts.ColumnChart
import dev.voir.areca.charts.ColumnChartBar
import dev.voir.areca.charts.ColumnChartColumn
import dev.voir.areca.charts.ColumnChartLayout
import dev.voir.areca.charts.ColumnChartReferenceLine
import dev.voir.areca.charts.ColumnChartSegment
import dev.voir.areca.charts.ColumnChartSize
import dev.voir.areca.charts.ColumnChartStyle
import dev.voir.areca.charts.ColumnChartValueLabels
import dev.voir.areca.charts.LineChart
import dev.voir.areca.charts.LineChartMarker
import dev.voir.areca.charts.LineChartSeries
import dev.voir.areca.charts.LineChartStroke
import dev.voir.areca.charts.LineChartTooltip
import dev.voir.areca.charts.LineChartXLabel
import dev.voir.areca.charts.LineChartYLabel
import dev.voir.areca.charts.MeterBar
import dev.voir.areca.charts.MeterBarSize
import dev.voir.areca.charts.StackedBarChart
import dev.voir.areca.charts.StackedBarChartSize
import dev.voir.areca.charts.StackedBarSegment
import dev.voir.areca.datetime.DateTile
import dev.voir.areca.datetime.calendar.Calendar
import dev.voir.areca.datetime.calendar.CalendarDateRule
import dev.voir.areca.datetime.calendar.CalendarHeatDay
import dev.voir.areca.datetime.calendar.CalendarHeatLegend
import dev.voir.areca.datetime.calendar.CalendarSelection
import dev.voir.areca.datetime.calendar.MonthGrid
import dev.voir.areca.datetime.calendar.calendarHeatLevel
import dev.voir.areca.datetime.calendar.rememberCalendarState
import dev.voir.areca.foundation.badge.Badge
import dev.voir.areca.foundation.badge.BadgeOption
import dev.voir.areca.foundation.badge.BadgeSelectionMode
import dev.voir.areca.foundation.badge.BadgeSize
import dev.voir.areca.foundation.badge.BadgeStyle
import dev.voir.areca.foundation.badge.FlowBadgeSelect
import dev.voir.areca.foundation.badge.IndicatorDot
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.ButtonStyle
import dev.voir.areca.foundation.field.FieldStyle
import dev.voir.areca.foundation.field.SearchField
import dev.voir.areca.foundation.icon.Chevron
import dev.voir.areca.foundation.icon.ChevronDirection
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.foundation.icon.IconSize
import dev.voir.areca.foundation.layout.Card
import dev.voir.areca.foundation.layout.CardSize
import dev.voir.areca.foundation.layout.CardStyle
import dev.voir.areca.foundation.list.CheckboxListItem
import dev.voir.areca.foundation.list.ExpandableListItem
import dev.voir.areca.foundation.list.ExpandableListItemContainer
import dev.voir.areca.foundation.list.ListItem
import dev.voir.areca.foundation.overlay.FlowModalActionBar
import dev.voir.areca.foundation.overlay.FlowModalActionBarLayout
import dev.voir.areca.foundation.overlay.FlowModalActionBarStyle
import dev.voir.areca.foundation.overlay.FlowModalSecondaryAction
import dev.voir.areca.foundation.stat.Stat
import dev.voir.areca.foundation.stat.StatGrid
import dev.voir.areca.foundation.stat.StatSize
import dev.voir.areca.foundation.tabs.TabItem
import dev.voir.areca.foundation.tabs.Tabs
import dev.voir.areca.foundation.tabs.TabsSize
import dev.voir.areca.foundation.tabs.TabsVariant
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.ArrowLeftRight
import dev.voir.areca.icons.X
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.ArecaThemeProvider
import java.io.File
import kotlin.test.Test
import kotlin.time.Clock
import kotlin.time.Duration.Companion.hours
import kotlin.time.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.YearMonth
import kotlinx.datetime.atStartOfDayIn
import org.jetbrains.skia.EncodedImageFormat

/** Logical width of the device the scenes are rendered at. */
private const val WIDTH = 390

/** Logical height of the device the scenes are rendered at. */
private const val HEIGHT = 844

/** Pixel density the scenes are rasterised at, so the PNGs are legible when opened. */
private const val DENSITY = 2f

/** Time between the frames stepped through before the final one, in nanoseconds. */
private const val FRAME_NANOS = 50_000_000L

/** How long the clock runs before the frame that is kept, in nanoseconds. */
private const val SETTLE_NANOS = 1_000_000_000L

/** Where the rendered images are written. */
private val OUTPUT = File(System.getProperty("render.out") ?: "build/screens")

/**
 * Renders one composition to a PNG once its animations have settled.
 *
 * @param name File name, without the extension.
 * @param dark Whether to render in the dark theme.
 * @param height Logical height of the image; defaults to a phone's.
 * @param content Composition to render.
 */
@OptIn(ExperimentalComposeUiApi::class)
private fun render(
    name: String,
    dark: Boolean = false,
    height: Int = HEIGHT,
    content: @Composable () -> Unit,
) {
    val scene = ImageComposeScene(
        width = (WIDTH * DENSITY).toInt(),
        height = (height * DENSITY).toInt(),
        density = Density(DENSITY),
    ) {
        ArecaThemeProvider(darkTheme = dark) {
            Box(Modifier.fillMaxSize().background(ArecaTheme.colors.bgCanvas)) { content() }
        }
    }

    try {
        var time = 0L
        while (time < SETTLE_NANOS) {
            scene.render(time)
            time += FRAME_NANOS
        }
        val data = requireNotNull(scene.render(SETTLE_NANOS).encodeToData(EncodedImageFormat.PNG))
        OUTPUT.mkdirs()
        File(OUTPUT, "$name.png").writeBytes(data.bytes)
    } finally {
        scene.close()
    }
}

/** Renders a scene in both themes, the dark one suffixed `-dark`. */
private fun renderBoth(name: String, height: Int = HEIGHT, content: @Composable () -> Unit) {
    render(name, height = height, content = content)
    render("$name-dark", dark = true, height = height, content = content)
}

/** A page column with the screen gutter and section rhythm. */
@Composable
private fun Page(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(ArecaTheme.spacing.xl),
        verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xl3),
        content = content,
    )
}

/**
 * Renders the components built for analytics screens — charts, controls and summaries — to
 * `build/screens`, in both themes.
 *
 * Not an assertion: there is no golden image. It fails when any configuration throws during
 * composition or layout, and it is how they are looked at on a machine with no display.
 *
 * ```bash
 * ./gradlew :playground:jvmTest --tests '*AnalyticsRenderTest*'
 * ```
 */
class AnalyticsRenderTest {

    /** Period charts: paired, stacked with a companion, a static year, a week, empty history. */
    @Test
    fun `column charts render`() {
        renderBoth("analytics-columns", height = 1100) { Columns() }
        render("analytics-columns-rtl", height = 1100) {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) { Columns() }
        }
    }

    /** Figures, a grid of them, and a category that opens in place on a soft block. */
    @Test
    fun `summaries render`() {
        renderBoth("analytics-summaries") { Summaries() }
    }

    /** A heat-map month with empty, future, today and picked days, its legend, and a date tile. */
    @Test
    fun `heat calendar renders`() {
        renderBoth("analytics-calendar") { HeatCalendar() }
    }

    /** A running total against last month, and a grid of months to compare with. */
    @Test
    fun `line chart and month grid render`() {
        renderBoth("analytics-compare") { Compare() }
        render("analytics-compare-rtl") {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) { Compare() }
        }
    }

    /** Existing components composed as the analytics screens use them: chips, filters, search, banner. */
    @Test
    fun `patterns render`() {
        renderBoth("analytics-patterns") { Patterns() }
    }

    /** The skill's analytics overview recipe, with one category open. */
    @Test
    fun `overview recipe renders`() {
        renderBoth("analytics-overview", height = 1500) { Overview() }
    }

    /**
     * Columns that arrive after the first frame, with nothing selected: the chart still opens on
     * the last one. A Fill chart of twelve paired months on a narrow card fits its bars.
     */
    @Test
    fun `late data and narrow fill render`() {
        render("analytics-columns-late", height = 500) { LateColumns() }
    }

    /** Meter bars, thin stacked bars, a legend, segmented tabs and a split sheet foot. */
    @Test
    fun `small charts and controls render`() {
        renderBoth("analytics-controls") { Controls() }
    }
}

private val Months = listOf("Nov", "Dec", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct")
private val Income = listOf(3900f, 4100f, 3800f, 3950f, 4400f, 3900f, 4000f, 4300f, 3850f, 4600f, 4100f, 4250f)
private val Spent = listOf(2100f, 3050f, 1690f, 1780f, 2170f, 2010f, 1840f, 2530f, 3050f, 2260f, 2090f, 2340f)

@Composable
private fun Columns() {
    Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xl3)) {
        var selected by remember { mutableStateOf(11) }
        ColumnChart(
            columns = Months.indices.map { i ->
                ColumnChartColumn(
                    key = i,
                    bars = listOf(
                        ColumnChartBar.of(Income[i], ArecaTheme.colors.bgBrandBase),
                        ColumnChartBar.of(Spent[i], ArecaTheme.colors.bgDangerBase),
                    ),
                    label = if (i == Months.lastIndex) "Now" else Months[i],
                    sublabel = when {
                        i == Months.lastIndex -> "so far"
                        Months[i] == "Jan" -> "2026"
                        else -> null
                    },
                )
            },
            selectedKey = selected,
            onSelect = { selected = it },
            referenceLines = listOf(ColumnChartReferenceLine(2240f, "usual spend", ArecaTheme.colors.fgDangerText)),
            contentPadding = PaddingValues(horizontal = ArecaTheme.spacing.xl),
        )

        ColumnChart(
            columns = Months.indices.map { i ->
                ColumnChartColumn(
                    key = i,
                    bars = listOf(
                        ColumnChartBar(
                            listOf(
                                ColumnChartSegment(Income[i] * 0.82f),
                                ColumnChartSegment(Income[i] * 0.12f),
                                ColumnChartSegment(Income[i] * 0.06f),
                            ),
                        ),
                        ColumnChartBar.companion(Income[i] * 0.9f),
                    ),
                    label = Months[i],
                    valueLabel = "€${(Income[i] / 100).toInt() / 10f}k",
                )
            },
            selectedKey = 9,
            onSelect = {},
            valueLabels = ColumnChartValueLabels.Selected,
            referenceLines = listOf(ColumnChartReferenceLine(4000f, "usual")),
            contentPadding = PaddingValues(horizontal = ArecaTheme.spacing.xl),
        )

        Column(
            modifier = Modifier.padding(horizontal = ArecaTheme.spacing.xl),
            verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xl3),
        ) {
            ColumnChart(
                columns = Months.indices.map { i ->
                    val kept = (Income[i] - Spent[i]) / Income[i]
                    ColumnChartColumn(
                        key = i,
                        bars = listOf(
                            ColumnChartBar.of(
                                kept,
                                if (i == 11) ArecaTheme.colors.bgBrandBase else ArecaTheme.colors.bgMuted,
                            ),
                        ),
                        label = Months[i].take(1),
                        valueLabel = "${(kept * 100).toInt()}%",
                    )
                },
                selectedKey = 11,
                valueLabels = ColumnChartValueLabels.All,
                layout = ColumnChartLayout.Fill,
                referenceLines = listOf(ColumnChartReferenceLine(0.41f)),
                style = ColumnChartStyle.default(halo = Color.Transparent, dimmedAlpha = 1f),
                size = ColumnChartSize.Small.copy(columnGap = ArecaTheme.spacing.sm),
            )
            ColumnChart(
                columns = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun").mapIndexed { i, day ->
                    val value = listOf(48f, 52f, 41f, 60f, 74f, 112f, 88f)[i]
                    ColumnChartColumn(key = day, bars = listOf(ColumnChartBar.of(value)), label = day, valueLabel = "€${value.toInt()}")
                },
                selectedKey = "Sat",
                valueLabels = ColumnChartValueLabels.All,
                layout = ColumnChartLayout.Fill,
                size = ColumnChartSize.Small,
            )
            ColumnChart(
                columns = (0 until 6).map { i ->
                    ColumnChartColumn(
                        key = i,
                        bars = listOf(ColumnChartBar.of(listOf(60f, 85f, 50f, 95f, 70f, 40f)[i])),
                        label = "",
                        placeholder = i < 5,
                    )
                },
                layout = ColumnChartLayout.Fill,
                size = ColumnChartSize.Small,
            )
        }
    }
}

@Composable
private fun Summaries() {
    Page {
        Stat(
            label = "October so far",
            value = "€2,340",
            note = "€218 under your usual pace",
            size = StatSize.Large,
            trailing = { Badge(style = BadgeStyle.Neutral) { Text("−9%") } },
        )
        StatGrid {
            Stat(label = "Spent", value = "€2,140", note = "a month", onClick = {})
            Stat(label = "Earned", value = "€4,180", note = "a month", onClick = {})
            Stat(label = "Kept", value = "49%", note = "of income")
            Stat(label = "A day", value = "€71.20", note = "weekends 1.8× weekdays")
        }
        Column {
            ExpandableListItem(
                expanded = true,
                onExpandedChange = {},
                title = { Text("Food and dining") },
                subtitle = { Text("€612 · 26% of spending") },
                showChevron = false,
                container = ExpandableListItemContainer.Soft,
            ) {
                ListItem(title = { Text("Groceries") }, value = { Text("€318") }, nestingLevel = 1)
                ListItem(title = { Text("Restaurants") }, value = { Text("€190") }, nestingLevel = 1)
            }
            ExpandableListItem(
                expanded = false,
                onExpandedChange = {},
                title = { Text("Transport") },
                subtitle = { Text("€318 · 14% of spending") },
                showChevron = false,
                container = ExpandableListItemContainer.Soft,
            ) {}
        }
    }
}

private val DaySpend = listOf(
    84f, 0f, 32f, 141f, 12f, 0f, 260f, 46f, 18f, 0f, 75f, 120f, 9f, 64f, 0f, 38f,
    210f, 27f, 0f, 91f, 55f, 14f, 0f, 180f, 42f, 7f, 66f, 0f, 104f, 23f, 58f,
)

@Composable
private fun HeatCalendar() {
    val today = LocalDate(2026, 10, 23)
    val steps = ArecaTheme.colors.accentScale.size
    Page {
        Calendar(
            state = rememberCalendarState(initialMonth = YearMonth(2026, 10), clock = FixedClock(today)),
            selection = CalendarSelection.Single(LocalDate(2026, 10, 17)),
            onDateClick = {},
            dateRule = CalendarDateRule { it <= today },
            day = { day ->
                val spent = DaySpend[day.date.day - 1]
                CalendarHeatDay(
                    day = day,
                    level = calendarHeatLevel(spent, max = 260f, steps = steps),
                    amount = if (spent > 0f) "€${spent.toInt()}" else null,
                    onClick = {},
                )
            },
        )
        CalendarHeatLegend(lowLabel = "€0", highLabel = "€260", emptyLabel = "nothing spent")
        ListItem(
            title = { Text("Salary") },
            subtitle = { Text("Usually on the 25th") },
            value = { Text("€3,650") },
            leading = { DateTile(date = LocalDate(2026, 10, 25)) },
        )
    }
}

/** A clock stopped at noon UTC on [date], so the render shows the same today every run. */
private class FixedClock(private val date: LocalDate) : Clock {
    override fun now(): Instant = date.atStartOfDayIn(TimeZone.UTC) + 12.hours
}

@Composable
private fun Compare() {
    var total = 0f
    val last = List(30) { day -> total += DaySpend[(29 - day)]; total }
    total = 0f
    val now = List(23) { day -> total += DaySpend[day]; total }
    Page {
        LineChart(
            series = listOf(
                LineChartSeries(last, "September", color = ArecaTheme.colors.fgMuted, stroke = LineChartStroke.Dashed, endMarker = null),
                LineChartSeries(last.take(23), "September by today", color = ArecaTheme.colors.fgMuted, stroke = LineChartStroke.Dashed, endMarker = LineChartMarker.Hollow),
                LineChartSeries(now, "October", color = ArecaTheme.colors.bgDangerBase, area = true),
            ),
            modifier = Modifier.fillMaxWidth(),
            xLabels = listOf(LineChartXLabel(0, "1 Oct"), LineChartXLabel(14, "15"), LineChartXLabel(29, "30")),
            yLabels = listOf(LineChartYLabel(0f, "0"), LineChartYLabel(1000f, "€1k"), LineChartYLabel(2000f, "€2k")),
            rule = 22,
            tooltip = LineChartTooltip(series = 2, index = 22, text = "€${now.last().toInt()} by the 23rd"),
            maxValue = 2000f,
        )
        MonthGrid(
            years = listOf(2026),
            selected = YearMonth(2026, 9),
            onSelect = {},
            enabled = { it < YearMonth(2026, 10) },
        ) { month -> Text(if (month < YearMonth(2026, 10)) "€2.${month.month.ordinal}k" else if (month == YearMonth(2026, 10)) "this" else "") }
    }
}

@Composable
private fun Patterns() {
    Page {
        SearchField(
            value = "gro",
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            style = FieldStyle.soft(shape = ArecaTheme.shapes.full),
        )
        FlowRow(horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.sm)) {
            listOf("Groceries", "Lisbon trip").forEach { name ->
                Badge(
                    style = BadgeStyle.Chip,
                    size = BadgeSize.Chip,
                    onClick = {},
                    leading = {
                        Box(Modifier.size(ArecaTheme.spacing.lg).background(ArecaTheme.colors.bgInfoBase, ArecaTheme.shapes.xs))
                    },
                    trailing = { Icon(ArecaIcons.X, contentDescription = "Remove") },
                ) { Text(name) }
            }
        }
        FlowBadgeSelect(
            options = listOf("Lisbon trip" to 9, "Work lunches" to 11, "Gifts" to 2).map { (name, count) ->
                BadgeOption(
                    label = { Text(name) },
                    value = name,
                    leading = { IndicatorDot(color = ArecaTheme.colors.bgBrandBase, contentDescription = null) },
                    trailing = { Text("$count") },
                )
            },
            onSelect = {},
            selected = listOf("Lisbon trip"),
            selectionMode = BadgeSelectionMode.Multiple,
            horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.sm),
            style = BadgeStyle.FilledChip,
            size = BadgeSize.Chip,
        )
        CheckboxListItem(
            checked = true,
            indeterminate = true,
            onCheckedChange = {},
            title = { Text("Food and dining") },
            subtitle = { Text("€612 in October") },
            bottom = {
                FlowBadgeSelect(
                    options = listOf("Groceries", "Restaurants", "Coffee").map { BadgeOption(label = { Text(it) }, value = it) },
                    onSelect = {},
                    selected = listOf("Groceries"),
                    selectionMode = BadgeSelectionMode.Multiple,
                    horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.sm),
                    style = BadgeStyle.FilledChip,
                    size = BadgeSize.Chip,
                )
            },
        )
        Card(
            style = CardStyle.softNeutral(),
            size = CardSize.default(padding = PaddingValues(ArecaTheme.spacing.lg)),
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
        ) { padding ->
            Row(
                modifier = Modifier.padding(padding),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
            ) {
                Icon(ArecaIcons.ArrowLeftRight, contentDescription = null, size = IconSize.Small)
                Text("€218 less than September by the 23rd", variant = TextVariant.BodySmall, modifier = Modifier.weight(1f))
                Chevron(direction = ChevronDirection.Right, color = ArecaTheme.colors.fgSubtle)
            }
        }
    }
}

@Composable
private fun Overview() {
    var selected by remember { mutableStateOf("2026-10") }
    AnalyticsOverview(
        periods = Months.indices.map { i ->
            val latest = i == Months.lastIndex
            PeriodSummary(
                key = if (latest) "2026-10" else "m$i",
                label = if (latest) "Now" else Months[i],
                sublabel = if (latest) "so far" else null,
                title = if (latest) "October so far" else "${Months[i]} 2026",
                income = Income[i],
                spent = Spent[i],
                description = "${Months[i]}: income €${Income[i].toInt()}, spent €${Spent[i].toInt()}",
            )
        },
        selectedKey = selected,
        onSelect = { selected = it },
        usualSpend = 2240f,
        usual = listOf(
            Triple("Spent", "€2,140", "a month"),
            Triple("Earned", "€4,180", "a month"),
            Triple("Kept", "49%", "of income"),
            Triple("A day", "€71.20", "weekends 1.8× weekdays"),
        ),
        categories = listOf(
            CategoryShare("Housing", "€920", "39% of spending · usual €980", 1f, 0.95f, "€980", listOf("Rent" to "€780", "Utilities" to "€110")),
            CategoryShare("Food and dining", "€612", "26% · €62 over usual", 0.66f, 0.6f, "€550", listOf("Groceries" to "€318", "Restaurants" to "€190")),
            CategoryShare("Transport", "€318", "14% · on usual", 0.35f, 0.36f, "€320", emptyList()),
        ),
        modifier = Modifier.padding(vertical = ArecaTheme.spacing.xl),
    )
}

@Composable
private fun LateColumns() {
    var columns by remember { mutableStateOf(emptyList<ColumnChartColumn<Int>>()) }
    val income = ArecaTheme.colors.bgBrandBase
    val spent = ArecaTheme.colors.bgDangerBase
    LaunchedEffect(Unit) {
        repeat(3) { withFrameNanos { } }
        columns = Months.indices.map { i ->
            ColumnChartColumn(
                key = i,
                bars = listOf(ColumnChartBar.of(Income[i], income), ColumnChartBar.of(Spent[i], spent)),
                label = Months[i],
            )
        }
    }
    Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xl3)) {
        ColumnChart(columns = columns, contentPadding = PaddingValues(horizontal = ArecaTheme.spacing.xl))
        ColumnChart(
            columns = columns,
            modifier = Modifier.padding(horizontal = ArecaTheme.spacing.xl),
            layout = ColumnChartLayout.Fill,
            size = ColumnChartSize.Small,
        )
    }
}

@Composable
private fun Controls() {
    Page {
        var period by remember { mutableStateOf("Month") }
        Row(modifier = Modifier.fillMaxWidth()) {
            Tabs(
                tabs = listOf("Week", "Month", "Year").map { TabItem(it) { Text(it) } },
                selectedValue = period,
                onValueSelected = { period = it },
                size = TabsSize.Segmented,
                variant = TabsVariant.Segmented,
            )
        }
        Tabs(
            tabs = listOf("Categories", "Tags", "Wallets").map { TabItem(it) { Text(it) } },
            selectedValue = "Tags",
            onValueSelected = {},
            size = TabsSize.Segmented,
            variant = TabsVariant.Segmented,
            fillWidth = true,
        )

        Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Food and dining", variant = TextVariant.LabelMedium)
                Text("€612", variant = TextVariant.LabelMedium)
            }
            MeterBar(fraction = 0.66f, marker = 0.6f, color = ArecaTheme.colors.bgDangerBase)
            Text("26% of spending", variant = TextVariant.BodyExtraSmall, color = TextColor.Subtle)
            MeterBar(fraction = 0.4f, marker = 0.5f, size = MeterBarSize.Slim)
            MeterBar(fraction = 0.8f, size = MeterBarSize.Large)
        }

        Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md)) {
            StackedBarChart(
                segments = listOf(
                    StackedBarSegment(0.55f, color = ArecaTheme.colors.bgDangerBase),
                    StackedBarSegment(0.45f, color = ArecaTheme.colors.bgBrandBase),
                ),
                modifier = Modifier.fillMaxWidth(),
                size = StackedBarChartSize.Tiny,
            )
            StackedBarChart(
                segments = listOf(
                    StackedBarSegment(0.7f, color = ArecaTheme.colors.fgBase),
                    StackedBarSegment(0.3f, color = ArecaTheme.colors.bgNeutralIntense),
                ),
                modifier = Modifier.fillMaxWidth(),
                size = StackedBarChartSize.ExtraSmall,
            )
            ChartLegend(
                items = listOf(
                    ChartLegendItem("Income", ChartSwatch.Square(ArecaTheme.colors.bgBrandBase)),
                    ChartLegendItem("Expenses", ChartSwatch.Dot(ArecaTheme.colors.bgDangerBase)),
                    ChartLegendItem("October", ChartSwatch.Line(ArecaTheme.colors.bgBrandBase)),
                    ChartLegendItem("September", ChartSwatch.DashedLine(ArecaTheme.colors.fgMuted)),
                    ChartLegendItem("usual", ChartSwatch.Tick(ArecaTheme.colors.fgBase)),
                ),
            )
        }

        FlowModalActionBar(
            confirmText = "Compare with September",
            onConfirm = {},
            leading = { FlowModalSecondaryAction(text = "Cancel", onClick = {}) },
            style = FlowModalActionBarStyle.default(
                secondary = ButtonStyle.soft(ActionColor.Neutral, shape = ArecaTheme.shapes.full),
            ),
            layout = FlowModalActionBarLayout.Split(),
            showDivider = false,
        )
    }
}
