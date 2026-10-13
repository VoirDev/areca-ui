package dev.voir.areca.datetime

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.wheel.LocalWheelPickerScroll
import dev.voir.areca.foundation.wheel.LocalWheelPickerSize
import dev.voir.areca.foundation.wheel.LocalWheelPickerStyle
import dev.voir.areca.foundation.wheel.WheelPicker
import dev.voir.areca.foundation.wheel.WheelPickerColumn
import dev.voir.areca.foundation.wheel.WheelPickerItem
import dev.voir.areca.foundation.wheel.WheelPickerItemScope
import dev.voir.areca.foundation.wheel.WheelPickerScroll
import dev.voir.areca.foundation.wheel.WheelPickerSize
import dev.voir.areca.foundation.wheel.WheelPickerStyle
import dev.voir.areca.foundation.wheel.rememberWheelPickerColumnState
import dev.voir.areca.theme.ArecaTheme
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.datetime.LocalTime

/** Whether a [TimePicker] counts hours to twenty-four or to twelve with a period column. */
public enum class TimeFormat {
    /** `00:00` to `23:59`. Two columns. */
    Hours24,

    /** `12:00 AM` to `11:59 PM`. Three columns. */
    Hours12,
}

/** Half of the day a twelve-hour time falls in. */
internal enum class DayPeriod {
    /** Midnight to noon. */
    AM,

    /** Noon to midnight. */
    PM,
    ;

    /** Conversion from twenty-four-hour hours. */
    companion object {
        /**
         * Period a twenty-four-hour hour falls in.
         *
         * @param hour24 Hour in `0..23`.
         * @return [AM] before noon, [PM] from noon.
         */
        fun fromHour24(hour24: Int): DayPeriod = if (hour24 < 12) AM else PM
    }
}

/**
 * The text a [TimePicker] shows on its period wheel and reads out for each of its wheels.
 *
 * Collected here for the same reason [dev.voir.areca.datetime.calendar.CalendarLabels] exists: the
 * library ships no resources, so an application passes text built from whatever localization it
 * already has.
 *
 * @property hour What assistive technology reads for the hour wheel.
 * @property minute What assistive technology reads for the minute wheel.
 * @property period What assistive technology reads for the AM/PM wheel.
 * @property am The row of the period wheel for the hours before noon, such as `AM` or `a.m.`.
 * @property pm The row of the period wheel for the hours from noon, such as `PM` or `p.m.`.
 * @property hourWithPeriod What assistive technology reads as the value of a twelve-hour hour
 *   wheel: the hour, `1..12`, joined with [am] or [pm] for the row the period wheel rests on, so the
 *   hour is never read without its half of the day. A function so the order can follow the
 *   language — `2 PM`, but `오후 2시`.
 */
@Immutable
public data class TimePickerLabels(
    public val hour: String,
    public val minute: String,
    public val period: String,
    public val am: String = "AM",
    public val pm: String = "PM",
    public val hourWithPeriod: (hour: Int, period: String) -> String =
        { hour, period -> "$hour $period" },
) {
    /** Ready-made label sets. */
    public companion object {
        /** Plain-English fallback. */
        public val English: TimePickerLabels = TimePickerLabels(
            hour = "Hour",
            minute = "Minute",
            period = "AM or PM",
            am = "AM",
            pm = "PM",
            hourWithPeriod = { hour, period -> "$hour $period" },
        )
    }
}

/**
 * A time picker built on [dev.voir.areca.foundation.wheel.WheelPicker].
 *
 * Uncontrolled: it manages its own wheels, seeded from [initialTime], and reports what they come to
 * rest on through [onTimeChange]. The callback fires on the user's doing and on nothing else —
 * neither on first composition nor for the values a wheel merely spins past — so it slots into a
 * form's `onValueChange` without spurious updates.
 *
 * The hour and minute wheels repeat, so 23 runs into 00 the way a clock does; the period wheel does
 * not, because AM has nothing beyond it. Changing [format] while the picker is showing rebuilds the
 * hour and period wheels on the time they last came to rest on, so the time survives the switch.
 *
 * To assistive technology each wheel reads its value in words: a twelve-hour hour together with its
 * period ([TimePickerLabels.hourWithPeriod]), and the period as [TimePickerLabels.am] or
 * [TimePickerLabels.pm].
 *
 * The result is minute precision: seconds and nanoseconds of [initialTime] are dropped.
 *
 * @param initialTime Time the wheels start on.
 * @param onTimeChange Called with the time the wheels came to rest on.
 * @param modifier Modifier applied to the picker.
 * @param format Whether to count hours to twenty-four or to twelve with a period column. Defaults
 *   to the hour cycle of [DateTimeLocale.Current].
 * @param style Colors of the wheels.
 * @param size Row height and visible row count.
 * @param scroll How hard the wheels answer scrolling. `null` lets each wheel pace itself from
 *   its own length, so the minutes and the AM/PM column are not held to one compromise.
 * @param labels What assistive technology reads for each wheel, and the period wheel's rows.
 * @param centerContent Whether the columns cluster in the middle of the picker or hug its start
 *   edge.
 * @param columnWidth Width of one column.
 * @param columnSpacing Gap between columns.
 * @param itemContent Renders one row. Receives the already-formatted text — zero-padded hours and
 *   minutes, or [TimePickerLabels.am] and [TimePickerLabels.pm] — and, through the receiver,
 *   where the row sits on the wheel.
 */
@Composable
public fun TimePicker(
    initialTime: LocalTime,
    onTimeChange: (LocalTime) -> Unit,
    modifier: Modifier = Modifier,
    format: TimeFormat = DateTimeLocale.Current.timeFormat,
    style: WheelPickerStyle = LocalWheelPickerStyle.current ?: WheelPickerStyle.Default,
    size: WheelPickerSize = LocalWheelPickerSize.current ?: WheelPickerSize.Default,
    scroll: WheelPickerScroll? = LocalWheelPickerScroll.current,
    labels: TimePickerLabels = TimePickerLabels.English,
    centerContent: Boolean = true,
    columnWidth: Dp = TimeColumnWidth,
    columnSpacing: Dp = ArecaTheme.spacing.sm,
    itemContent: @Composable WheelPickerItemScope.(text: String) -> Unit = { text ->
        WheelPickerItem(text)
    },
) {
    val hours = remember(format) { hoursOf(format) }
    val minutes = remember { (0..59).toList() }
    val periods = remember { DayPeriod.entries.toList() }

    // The time the wheels last came to rest on. A plain holder rather than state: it is read only
    // when a format change builds new hour and period wheels, never to draw anything.
    val settled = remember { SettledTime(LocalTime(initialTime.hour, initialTime.minute)) }

    // Keyed on the format: a twelve-row hour wheel cannot keep the index of a twenty-four-row one,
    // so a format change builds both wheels afresh on the last settled time.
    val hourState = key(format) {
        rememberWheelPickerColumnState(
            initialIndex = hourIndexOf(settled.time.hour, format),
            infinite = true,
        )
    }
    val minuteState = rememberWheelPickerColumnState(
        initialIndex = settled.time.minute,
        infinite = true,
    )
    val periodState = key(format) {
        rememberWheelPickerColumnState(
            initialIndex = DayPeriod.fromHour24(settled.time.hour).ordinal,
        )
    }

    val currentOnTimeChange by rememberUpdatedState(onTimeChange)

    // Settled indices only, so a spin reports the time it stopped on rather than every minute it
    // passed. drop(1) skips the emission that merely echoes the time the wheels were seeded with —
    // including after a format change, whose new wheels start on the time already reported.
    LaunchedEffect(hourState, minuteState, periodState, hours, format) {
        snapshotFlow {
            timeAt(
                hour = hours.getOrNull(hourState.settledIndex),
                minute = minutes.getOrNull(minuteState.settledIndex),
                period = periods.getOrNull(periodState.settledIndex),
                format = format,
            )
        }
            .filterNotNull()
            .distinctUntilChanged()
            .drop(1)
            .collect { time ->
                settled.time = time
                currentOnTimeChange(time)
            }
    }

    val periodLabel: (DayPeriod) -> String = { period ->
        when (period) {
            DayPeriod.AM -> labels.am
            DayPeriod.PM -> labels.pm
        }
    }

    WheelPicker(
        modifier = modifier.fillMaxWidth(),
        style = style,
        size = size,
        scroll = scroll,
        horizontalArrangement = Arrangement.spacedBy(
            space = columnSpacing,
            alignment = if (centerContent) Alignment.CenterHorizontally else Alignment.Start,
        ),
        columns = {
            WheelPickerColumn(
                items = hours,
                state = hourState,
                modifier = Modifier.width(columnWidth),
                contentDescription = labels.hour,
                valueDescription = { hour ->
                    when (format) {
                        TimeFormat.Hours24 -> hour.toString()
                        TimeFormat.Hours12 -> labels.hourWithPeriod(
                            hour,
                            periodLabel(
                                periods.getOrElse(periodState.settledIndex) { DayPeriod.AM },
                            ),
                        )
                    }
                },
            ) { hour ->
                itemContent(if (format == TimeFormat.Hours24) hour.pad2() else hour.toString())
            }
            WheelPickerColumn(
                items = minutes,
                state = minuteState,
                modifier = Modifier.width(columnWidth),
                contentDescription = labels.minute,
                valueDescription = { minute -> minute.toString() },
            ) { minute ->
                itemContent(minute.pad2())
            }
            if (format == TimeFormat.Hours12) {
                WheelPickerColumn(
                    items = periods,
                    state = periodState,
                    modifier = Modifier.width(columnWidth),
                    contentDescription = labels.period,
                    valueDescription = periodLabel,
                ) { period ->
                    itemContent(periodLabel(period))
                }
            }
        },
    )
}

/** Width of one wheel of a [TimePicker]: room for two digits or a period name in its type. */
private val TimeColumnWidth: Dp = 64.dp

/**
 * The last time a [TimePicker]'s wheels came to rest on.
 *
 * @property time Minute-precision time, written each time the wheels settle.
 */
private class SettledTime(var time: LocalTime)

/**
 * The rows of a [TimePicker]'s hour wheel.
 *
 * @param format Whether hours count to twenty-four or to twelve.
 * @return `0..23` for [TimeFormat.Hours24], `1..12` for [TimeFormat.Hours12].
 */
internal fun hoursOf(format: TimeFormat): List<Int> = when (format) {
    TimeFormat.Hours24 -> (0..23).toList()
    TimeFormat.Hours12 -> (1..12).toList()
}

/**
 * Where a twenty-four-hour hour sits on the hour wheel of a [format].
 *
 * @param hour24 Hour in `0..23`.
 * @param format Whether the wheel counts to twenty-four or to twelve.
 * @return Index into [hoursOf] for [format].
 */
internal fun hourIndexOf(hour24: Int, format: TimeFormat): Int = when (format) {
    TimeFormat.Hours24 -> hour24
    TimeFormat.Hours12 -> toHour12(hour24) - 1
}

/**
 * The time a [TimePicker]'s wheels show.
 *
 * @param hour The hour row, in `0..23` or `1..12` depending on [format], or `null` when the wheel
 *   has no row yet.
 * @param minute The minute row, or `null` when the wheel has no row yet.
 * @param period The period row. Ignored for [TimeFormat.Hours24]; `null` for a twelve-hour wheel
 *   leaves the time undefined.
 * @param format Whether [hour] counts to twenty-four or to twelve.
 * @return The time, or `null` when a wheel it needs has no row.
 */
internal fun timeAt(hour: Int?, minute: Int?, period: DayPeriod?, format: TimeFormat): LocalTime? {
    if (hour == null || minute == null) return null
    return when (format) {
        TimeFormat.Hours24 -> LocalTime(hour, minute)
        TimeFormat.Hours12 -> period?.let { LocalTime(toHour24(hour, it), minute) }
    }
}

/**
 * Twelve-hour reading of a twenty-four-hour hour.
 *
 * @param hour24 Hour in `0..23`.
 * @return Hour in `1..12`: midnight and noon are both 12.
 */
internal fun toHour12(hour24: Int): Int = when {
    hour24 == 0 -> 12
    hour24 <= 12 -> hour24
    else -> hour24 - 12
}

/**
 * Twenty-four-hour reading of a twelve-hour hour and its period.
 *
 * @param hour12 Hour in `1..12`.
 * @param period Half of the day the hour falls in.
 * @return Hour in `0..23`: 12 AM is 0 and 12 PM is 12.
 */
internal fun toHour24(hour12: Int, period: DayPeriod): Int {
    val base = if (hour12 == 12) 0 else hour12
    return if (period == DayPeriod.AM) base else base + 12
}

/**
 * Two-digit rendering of an hour or minute, such as `07`.
 *
 * @return The receiver, zero-padded to two digits.
 */
private fun Int.pad2(): String = toString().padStart(2, '0')
