package dev.voir.areca.playground.datetime

import androidx.compose.animation.core.snap
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import dev.voir.areca.datetime.calendar.CalendarBounds
import dev.voir.areca.datetime.calendar.CalendarLegend
import dev.voir.areca.datetime.calendar.CalendarLegendItem
import dev.voir.areca.datetime.calendar.CalendarMarker
import dev.voir.areca.datetime.calendar.agenda.CalendarAgenda
import dev.voir.areca.datetime.calendar.agenda.CalendarAgendaState
import dev.voir.areca.datetime.calendar.agenda.rememberCalendarAgendaState
import dev.voir.areca.foundation.list.ListItem
import dev.voir.areca.foundation.settings.SettingsRowIcon
import dev.voir.areca.foundation.text.Amount
import dev.voir.areca.foundation.text.AmountText
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.ArrowDown
import dev.voir.areca.icons.ArrowUp
import dev.voir.areca.icons.Repeat
import dev.voir.areca.icons.SwitchHorizontal
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.tween
import dev.voir.areca.window.InputMode
import dev.voir.areca.window.ProvideWindowClass
import dev.voir.areca.window.WindowClass
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.YearMonth
import kotlinx.datetime.daysUntil
import kotlinx.datetime.number
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import kotlinx.datetime.yearMonth
import kotlin.time.Clock

/** Showcase for [CalendarAgenda]. */
@Composable
fun CalendarAgendaShowcase() = ShowcaseScaffold(
    title = "Calendar agenda",
    summary = "A compact month joined to a long day-by-day list the app draws, kept in step by taps, " +
        "scrolling and a fold to the selected week.",
    module = "datetime",
) {
    val today = remember { Clock.System.todayIn(TimeZone.currentSystemDefault()) }
    val plan = remember(today) { demoPlan(today) }

    Playground(
        description = "Tap a day to scroll to it, scroll the list to move the calendar. On the phone " +
            "frame the month folds to the selected week; the handle holds it open.",
    ) {
        val frame by enumControl(
            key = "window",
            default = AgendaFrame.Phone,
            options = AgendaFrame.entries,
            description = "Compact width folds the calendar over the list; Medium puts it in a card beside it.",
        )
        val markers by booleanControl(key = "markers", default = true, description = "A dot per kind of payment on each day.")
        val legend by booleanControl(key = "legend", default = true, description = "The key to the dots, folded away with the month.")
        val separators by booleanControl(
            key = "monthSeparator",
            default = true,
            description = "The month's name before its first day in the list.",
        )
        val emptyDay by booleanControl(
            key = "emptyDay",
            default = true,
            description = "A placeholder group for a picked day with nothing planned.",
        )
        val weekStart by enumControl(
            key = "startDayOfWeek",
            default = DayOfWeek.MONDAY,
            options = listOf(DayOfWeek.MONDAY, DayOfWeek.SUNDAY),
            optionLabel = { it.name.lowercase().replaceFirstChar(Char::uppercase) },
            description = "Which day each row begins on.",
        )
        val state = rememberCalendarAgendaState(bounds = CalendarBounds(today, today.plus(PlanDays - 1, DateTimeUnit.DAY)))
        var selected by remember { mutableStateOf(today) }
        val scope = rememberCoroutineScope()
        val motion = ArecaTheme.motion
        val foldSpec = if (motion.reduceMotion) snap() else motion.slow.tween<Float>(motion.standard)
        actionControl(key = "fold", buttonLabel = "Fold") { scope.launch { state.calendar.fold(animationSpec = foldSpec) } }
        actionControl(key = "unfold", buttonLabel = "Unfold") { scope.launch { state.calendar.unfold(foldSpec) } }
        actionControl(key = "requestScrollToDay", buttonLabel = "Scroll to today") {
            selected = today
            state.requestScrollToDay(today)
        }

        preview {
            InFrame(frame) {
                DemoAgenda(
                    state = state,
                    plan = plan,
                    today = today,
                    selected = selected,
                    onSelect = { selected = it },
                    markers = markers,
                    legend = legend,
                    separators = separators,
                    emptyDay = emptyDay,
                    startDayOfWeek = weekStart,
                )
            }
        }
    }

    Variants(
        title = "Window",
        description = "The layout follows the window class, never the device: one card on a compact " +
            "width, a calendar card beside a list card from medium.",
        layout = VariantLayout.Vertical,
    ) {
        AgendaFrame.entries.forEach { frame ->
            variant(frame.title, detail = frame.detail) {
                val state = rememberCalendarAgendaState(bounds = CalendarBounds(today, today.plus(PlanDays - 1, DateTimeUnit.DAY)))
                var selected by remember { mutableStateOf(today) }
                InFrame(frame) {
                    DemoAgenda(state = state, plan = plan, today = today, selected = selected, onSelect = { selected = it })
                }
            }
        }
    }

    EdgeCases {
        case(
            name = "Nothing planned",
            expectation = "With no days the list is empty and the calendar still pages and picks; with " +
                "emptyDay the picked day gets its placeholder group.",
        ) {
            val state = rememberCalendarAgendaState(bounds = CalendarBounds(today, today.plus(PlanDays - 1, DateTimeUnit.DAY)))
            var selected by remember { mutableStateOf(today) }
            InFrame(AgendaFrame.Phone, height = 520.dp) {
                DemoAgenda(state = state, plan = emptyMap(), today = today, selected = selected, onSelect = { selected = it })
            }
        }
        case(
            name = "A one-week window",
            expectation = "Bounds of seven days: the chevrons disable at once, days outside are dimmed, " +
                "and the list holds only that week.",
        ) {
            val state = rememberCalendarAgendaState(bounds = CalendarBounds(today, today.plus(6, DateTimeUnit.DAY)))
            var selected by remember { mutableStateOf(today) }
            InFrame(AgendaFrame.Phone, height = 560.dp) {
                DemoAgenda(
                    state = state,
                    plan = plan.filterKeys { today.daysUntil(it) < 7 },
                    today = today,
                    selected = selected,
                    onSelect = { selected = it },
                )
            }
        }
    }
}

/** Days the demo plan covers. */
private const val PlanDays = 90

/** Frames the previews are drawn in. */
private enum class AgendaFrame(val title: String, val detail: String, val width: Dp, val height: Dp) {
    Phone("Phone", "Compact width, 390dp", 390.dp, 720.dp),
    Tablet("Tablet", "Medium width and up, 820dp", 820.dp, 640.dp),
}

/**
 * A frame of [frame]'s size — narrower when the page is — that tells the agenda its window class.
 *
 * @param frame Size to draw at.
 * @param height Height, when it differs from the frame's.
 * @param content The agenda.
 */
@Composable
private fun InFrame(frame: AgendaFrame, height: Dp = frame.height, content: @Composable () -> Unit) {
    BoxWithConstraints(modifier = Modifier.widthIn(max = frame.width).fillMaxWidth().height(height)) {
        ProvideWindowClass(WindowClass(DpSize(maxWidth, maxHeight), InputMode.Touch)) { content() }
    }
}

/** Kinds of planned payment, in the order their dots are drawn. */
private enum class PaymentKind(val label: String, val glyph: ImageVector) {
    Income("Income", ArecaIcons.ArrowDown),
    Payment("Payment", ArecaIcons.ArrowUp),
    Subscription("Subscription", ArecaIcons.Repeat),
    Transfer("Transfer", ArecaIcons.SwitchHorizontal),
}

/**
 * One planned payment.
 *
 * @property kind What sort it is.
 * @property title What it is.
 * @property account Where it moves money.
 * @property cents Amount in cents.
 */
private data class PlannedPayment(val kind: PaymentKind, val title: String, val account: String, val cents: Long)

/** The colour of each kind's dot and tile. */
@Composable
private fun PaymentKind.hue(): Color = when (this) {
    PaymentKind.Income -> ArecaTheme.hues.green
    PaymentKind.Payment -> ArecaTheme.hues.red
    PaymentKind.Subscription -> ArecaTheme.hues.purple
    PaymentKind.Transfer -> ArecaTheme.hues.blue
}

/**
 * A plan of monthly bills, weekly groceries and a few one-offs over the [PlanDays] from [today].
 *
 * @param today First day of the plan.
 * @return Payments by day.
 */
private fun demoPlan(today: LocalDate): Map<LocalDate, List<PlannedPayment>> {
    val last = today.plus(PlanDays - 1, DateTimeUnit.DAY)
    val plan = mutableMapOf<LocalDate, MutableList<PlannedPayment>>()
    fun add(date: LocalDate, payment: PlannedPayment) {
        if (date in today..last) plan.getOrPut(date) { mutableListOf() } += payment
    }
    val monthly = listOf(
        1 to PlannedPayment(PaymentKind.Income, "Salary", "Main account", 320_000),
        1 to PlannedPayment(PaymentKind.Payment, "Rent", "Main account", 115_000),
        2 to PlannedPayment(PaymentKind.Transfer, "To savings", "Main → Savings", 40_000),
        5 to PlannedPayment(PaymentKind.Subscription, "Streaming", "Visa", 1_399),
        12 to PlannedPayment(PaymentKind.Subscription, "Music", "Visa", 1_099),
        15 to PlannedPayment(PaymentKind.Payment, "Electricity", "Main account", 8_640),
        22 to PlannedPayment(PaymentKind.Payment, "Loan repayment", "Main account", 21_000),
        28 to PlannedPayment(PaymentKind.Subscription, "Gym", "Visa", 3_900),
        28 to PlannedPayment(PaymentKind.Transfer, "Card top-up", "Main → Travel card", 15_000),
    )
    var month: YearMonth = today.yearMonth
    while (month.firstDay <= last) {
        monthly.forEach { (day, payment) -> add(LocalDate(month.year, month.month.number, day), payment) }
        month = month.plus(1, DateTimeUnit.MONTH)
    }
    var groceries = today.plus(3, DateTimeUnit.DAY)
    while (groceries <= last) {
        add(groceries, PlannedPayment(PaymentKind.Payment, "Groceries", "Visa", 6_800L + groceries.day % 5 * 750))
        groceries = groceries.plus(7, DateTimeUnit.DAY)
    }
    add(today, PlannedPayment(PaymentKind.Payment, "Car insurance", "Main account", 31_250))
    add(today, PlannedPayment(PaymentKind.Subscription, "Cloud storage", "Visa", 299))
    add(today.plus(2, DateTimeUnit.DAY), PlannedPayment(PaymentKind.Income, "Tax refund", "Main account", 42_000))
    return plan
}

/** An amount in euros, signed by kind. */
private fun PlannedPayment.amount(): Amount = Amount(
    whole = (cents / 100).toString(),
    fraction = (cents % 100).toString().padStart(2, '0'),
    decimalMark = ",",
    symbol = "€",
    sign = when (kind) {
        PaymentKind.Income -> "+"
        PaymentKind.Transfer -> null
        else -> "−"
    },
)

/** Title-cases an enum name: `NOVEMBER` → `November`. */
private fun String.titled(): String = lowercase().replaceFirstChar(Char::uppercase)

/**
 * The plan as an agenda.
 *
 * @param state Positions.
 * @param plan Payments by day.
 * @param today The calendar's today, for the day headings.
 * @param selected The selected day.
 * @param onSelect Picks a day.
 * @param markers Whether days carry dots.
 * @param legend Whether the dots have a key.
 * @param separators Whether months are named in the list.
 * @param emptyDay Whether a picked empty day gets a group.
 * @param startDayOfWeek Day each row begins on.
 */
@Composable
private fun DemoAgenda(
    state: CalendarAgendaState,
    plan: Map<LocalDate, List<PlannedPayment>>,
    today: LocalDate,
    selected: LocalDate,
    onSelect: (LocalDate) -> Unit,
    markers: Boolean = true,
    legend: Boolean = true,
    separators: Boolean = true,
    emptyDay: Boolean = true,
    startDayOfWeek: DayOfWeek = DayOfWeek.MONDAY,
) {
    val hues = PaymentKind.entries.associateWith { it.hue() }
    CalendarAgenda(
        days = plan.keys.toList(),
        selected = selected,
        onSelect = onSelect,
        state = state,
        startDayOfWeek = startDayOfWeek,
        markers = if (markers) {
            { date ->
                plan[date]?.let { payments ->
                    CalendarMarker.Dots(
                        colors = PaymentKind.entries.filter { kind -> payments.any { it.kind == kind } }.map(hues::getValue),
                        max = PaymentKind.entries.size,
                        contentDescription = "${payments.size} planned",
                    )
                }
            }
        } else {
            null
        },
        contentPadding = PaddingValues(bottom = ArecaTheme.spacing.xl4),
        legend = if (legend) {
            { CalendarLegend(items = PaymentKind.entries.map { CalendarLegendItem(hues.getValue(it), it.label) }) }
        } else {
            null
        },
        monthSeparator = if (separators) {
            { month ->
                Text(
                    text = "${month.month.name.titled()} ${month.year}",
                    variant = TextVariant.Overline,
                    color = TextColor.Muted,
                    modifier = Modifier.padding(
                        start = ArecaTheme.spacing.xl,
                        top = ArecaTheme.spacing.xl2,
                        bottom = ArecaTheme.spacing.xs,
                    ),
                )
            }
        } else {
            null
        },
        emptyDay = if (emptyDay) {
            {
                Text(
                    text = "Nothing planned on this day.",
                    variant = TextVariant.BodyMedium,
                    color = TextColor.Muted,
                    modifier = Modifier.padding(horizontal = ArecaTheme.spacing.xl, vertical = ArecaTheme.spacing.lg),
                )
            }
        } else {
            null
        },
        dayHeading = {
            val base = "${date.day} ${date.month.name.titled()}"
            val title = when (today.daysUntil(date)) {
                0 -> "Today, $base"
                1 -> "Tomorrow, $base"
                else -> "${date.dayOfWeek.name.titled()}, $base"
            }
            Text(
                text = title,
                variant = TextVariant.LabelMedium,
                color = if (isSelected) TextColor.Brand else TextColor.Subtle,
            )
        },
    ) {
        Column {
            plan[date].orEmpty().forEach { payment ->
                ListItem(
                    title = { Text(payment.title) },
                    subtitle = { Text(payment.account) },
                    leading = { SettingsRowIcon(payment.kind.glyph, hue = hues.getValue(payment.kind), soft = true) },
                    trailing = { AmountText(payment.amount()) },
                    onClick = {},
                    onClickLabel = "Open ${payment.title}",
                )
            }
        }
    }
}
