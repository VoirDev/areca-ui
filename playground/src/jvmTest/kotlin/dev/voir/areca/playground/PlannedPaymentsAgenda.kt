package dev.voir.areca.playground

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
import dev.voir.areca.theme.ArecaTheme
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.YearMonth
import kotlinx.datetime.daysUntil
import kotlinx.datetime.number
import kotlinx.datetime.plus
import kotlin.time.Clock

/** The kinds of planned payment, in the order their dots are drawn. */
internal enum class PaymentKind { Income, Payment, Subscription, Transfer }

/**
 * One planned payment.
 *
 * @property kind What sort it is.
 * @property title What it is.
 * @property account Where it moves money.
 * @property cents Amount in cents.
 */
internal data class PlannedPayment(val kind: PaymentKind, val title: String, val account: String, val cents: Long)

/** First day of the plan. */
internal val PlanStart = LocalDate(2026, 11, 25)

/** Last day of the plan, 90 days on. */
internal val PlanEnd = PlanStart.plus(89, DateTimeUnit.DAY)

/** Monthly items by day of the month. */
private val Monthly = listOf(
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

/** The plan: payments by day over [PlanStart]..[PlanEnd]. */
internal val Plan: Map<LocalDate, List<PlannedPayment>> = buildMap<LocalDate, MutableList<PlannedPayment>> {
    fun add(date: LocalDate, payment: PlannedPayment) {
        if (date in PlanStart..PlanEnd) getOrPut(date) { mutableListOf() } += payment
    }
    var month = YearMonth(2026, 11)
    while (month.firstDay <= PlanEnd) {
        Monthly.forEach { (day, payment) -> add(LocalDate(month.year, month.month.number, day), payment) }
        month = month.plus(1, DateTimeUnit.MONTH)
    }
    var groceries = LocalDate(2026, 11, 28)
    while (groceries <= PlanEnd) {
        add(groceries, PlannedPayment(PaymentKind.Payment, "Groceries", "Visa", 6_800L + groceries.day % 5 * 750))
        groceries = groceries.plus(7, DateTimeUnit.DAY)
    }
    add(LocalDate(2026, 11, 25), PlannedPayment(PaymentKind.Payment, "Car insurance", "Main account", 31_250))
    add(LocalDate(2026, 11, 25), PlannedPayment(PaymentKind.Subscription, "Cloud storage", "Visa", 299))
    add(LocalDate(2026, 11, 27), PlannedPayment(PaymentKind.Income, "Tax refund", "Main account", 42_000))
    add(LocalDate(2026, 12, 31), PlannedPayment(PaymentKind.Income, "Year-end bonus", "Main account", 150_000))
}

/** The colour of each kind's dot and tile. */
@Composable
internal fun PaymentKind.hue(): Color = when (this) {
    PaymentKind.Income -> ArecaTheme.hues.green
    PaymentKind.Payment -> ArecaTheme.hues.red
    PaymentKind.Subscription -> ArecaTheme.hues.purple
    PaymentKind.Transfer -> ArecaTheme.hues.blue
}

/** The glyph on each kind's tile. */
private val PaymentKind.glyph: ImageVector
    get() = when (this) {
        PaymentKind.Income -> ArecaIcons.ArrowDown
        PaymentKind.Payment -> ArecaIcons.ArrowUp
        PaymentKind.Subscription -> ArecaIcons.Repeat
        PaymentKind.Transfer -> ArecaIcons.SwitchHorizontal
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

/** English day heading: `Today, 25 November`. */
private fun headingFor(date: LocalDate): String {
    val month = date.month.name.lowercase().replaceFirstChar { it.uppercase() }
    val base = "${date.day} $month"
    return when (PlanStart.daysUntil(date)) {
        0 -> "Today, $base"
        1 -> "Tomorrow, $base"
        else -> "${date.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }}, $base"
    }
}

/**
 * A state for the plan's 90 days.
 *
 * @param clock Source of today.
 * @return The state.
 */
@Composable
internal fun rememberPlannedPaymentsState(clock: Clock): CalendarAgendaState = rememberCalendarAgendaState(
    bounds = CalendarBounds(minDate = PlanStart, maxDate = PlanEnd),
    clock = clock,
    timeZone = TimeZone.UTC,
)

/**
 * The plan as an agenda: dots per kind, a legend, month separators and an empty-day placeholder.
 *
 * @param state The agenda's state.
 * @param selected The selected day.
 * @param onSelect Picks a day.
 * @param modifier Modifier applied to the agenda.
 */
@Composable
internal fun PlannedPaymentsAgenda(
    state: CalendarAgendaState,
    selected: LocalDate,
    onSelect: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    val hues = PaymentKind.entries.associateWith { it.hue() }
    CalendarAgenda(
        days = Plan.keys.toList(),
        selected = selected,
        onSelect = onSelect,
        modifier = modifier,
        state = state,
        startDayOfWeek = DayOfWeek.MONDAY,
        markers = { date ->
            Plan[date]?.let { payments ->
                CalendarMarker.Dots(
                    colors = PaymentKind.entries.filter { kind -> payments.any { it.kind == kind } }.map { hues.getValue(it) },
                    max = 4,
                    contentDescription = "${payments.size} planned",
                )
            }
        },
        contentPadding = PaddingValues(bottom = ArecaTheme.spacing.xl4),
        legend = {
            CalendarLegend(items = PaymentKind.entries.map { CalendarLegendItem(hues.getValue(it), it.name) })
        },
        monthSeparator = { month ->
            Text(
                text = "${month.month.name} ${month.year}",
                variant = TextVariant.Overline,
                color = TextColor.Muted,
                modifier = Modifier.padding(
                    start = ArecaTheme.spacing.xl,
                    top = ArecaTheme.spacing.xl2,
                    bottom = ArecaTheme.spacing.xs,
                ),
            )
        },
        emptyDay = {
            Text(
                text = "Nothing planned on this day.",
                variant = TextVariant.BodyMedium,
                color = TextColor.Muted,
                modifier = Modifier.padding(horizontal = ArecaTheme.spacing.xl, vertical = ArecaTheme.spacing.lg),
            )
        },
        dayHeading = {
            Text(
                text = headingFor(date),
                variant = TextVariant.LabelMedium,
                color = if (isSelected) TextColor.Brand else TextColor.Subtle,
            )
        },
    ) {
        Column {
            Plan[date].orEmpty().forEach { payment ->
                ListItem(
                    title = { Text(payment.title) },
                    subtitle = { Text(payment.account) },
                    leading = { SettingsRowIcon(payment.kind.glyph, hue = hues.getValue(payment.kind), soft = true) },
                    trailing = { AmountText(payment.amount()) },
                    onClick = {},
                )
            }
        }
    }
}
