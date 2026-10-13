package dev.voir.areca.datetime

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import kotlinx.datetime.DayOfWeek

/**
 * The conventions of a locale that this module's pickers follow: the day a week starts on and
 * whether a clock counts to twelve or to twenty-four.
 *
 * `kotlinx-datetime` has no locale API, so these two facts are read from the platform instead —
 * `java.util.Calendar` and `DateFormat.is24HourFormat` on Android, `NSCalendar` and the `"j"`
 * skeleton of `NSDateFormatter` on iOS, `java.util.Calendar` and `java.time`'s localized short time
 * pattern on the JVM. On Android and iOS the hour cycle includes the user's own 24-hour setting.
 *
 * [Current] is what [dev.voir.areca.datetime.calendar.Calendar], [TimePicker] and [DateTimeField]
 * default to. Provide [LocalDateTimeLocale] to pin it for a subtree — a screenshot test, an app with
 * its own locale picker — or pass `startDayOfWeek` / `format` to one component.
 *
 * Words and digits are not here: they belong to the labels objects
 * ([dev.voir.areca.datetime.calendar.CalendarLabels], [TimePickerLabels], [DateTimeFieldLabels]),
 * which an application builds from its own localization.
 *
 * @property firstDayOfWeek Day each calendar week begins on.
 * @property timeFormat Whether times count hours to twenty-four or to twelve.
 */
@Immutable
public data class DateTimeLocale(
    public val firstDayOfWeek: DayOfWeek,
    public val timeFormat: TimeFormat,
) {
    /** The fixed ISO conventions and the conventions in scope. */
    public companion object {
        /** ISO 8601: weeks start on Monday and clocks count to twenty-four. */
        public val Iso: DateTimeLocale = DateTimeLocale(
            firstDayOfWeek = DayOfWeek.MONDAY,
            timeFormat = TimeFormat.Hours24,
        )

        /**
         * The locale in scope: [LocalDateTimeLocale] when one is provided, otherwise the platform's
         * current one.
         */
        public val Current: DateTimeLocale
            @Composable
            get() = LocalDateTimeLocale.current ?: rememberPlatformDateTimeLocale()
    }
}

/**
 * [DateTimeLocale] in scope, or `null` to follow the platform's current locale.
 *
 * Not seeded by [dev.voir.areca.theme.ArecaThemeProvider]: that provider lives in `:foundation`,
 * which does not depend on this module.
 */
public val LocalDateTimeLocale: ProvidableCompositionLocal<DateTimeLocale?> =
    compositionLocalOf { null }

/**
 * Reads the platform's current week start and hour cycle.
 *
 * Re-read when the configuration changes where the platform reports one, so a locale or 24-hour
 * setting changed while the app runs is followed.
 *
 * @return The platform's conventions.
 */
@Composable
internal expect fun rememberPlatformDateTimeLocale(): DateTimeLocale

/**
 * Maps a Sunday-based weekday number to a [DayOfWeek].
 *
 * `java.util.Calendar.firstDayOfWeek` and `NSCalendar.firstWeekday` both count `1` for Sunday
 * through `7` for Saturday.
 *
 * @param day Weekday number, `1` (Sunday) to `7` (Saturday). Anything else falls back to Monday.
 * @return The matching day of the week.
 */
internal fun dayOfWeekFromSundayBased(day: Int): DayOfWeek = when (day) {
    1 -> DayOfWeek.SUNDAY
    in 2..7 -> DayOfWeek(day - 1)
    else -> DayOfWeek.MONDAY
}

/**
 * Whether a localized time pattern counts hours to twelve.
 *
 * Unicode (and `java.text`) patterns write a twelve-hour hour as `h` or `K` and the period as `a`;
 * `H` and `k` are twenty-four-hour. Quoted literals are skipped, so a pattern such as `HH 'h' mm`
 * (French) stays twenty-four-hour.
 *
 * @param pattern A localized time pattern or skeleton result, such as `h:mm a` or `HH:mm`.
 * @return [TimeFormat.Hours12] when the pattern has a twelve-hour hour or a period field, otherwise
 *   [TimeFormat.Hours24].
 */
internal fun timeFormatOfPattern(pattern: String): TimeFormat {
    var quoted = false
    for (char in pattern) {
        when {
            char == '\'' -> quoted = !quoted
            !quoted && (char == 'h' || char == 'K' || char == 'a') -> return TimeFormat.Hours12
        }
    }
    return TimeFormat.Hours24
}
