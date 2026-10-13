package dev.voir.areca.datetime

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import java.time.chrono.IsoChronology
import java.time.format.DateTimeFormatterBuilder
import java.time.format.FormatStyle
import java.util.Calendar
import java.util.Locale

/**
 * Reads the week start from `java.util.Calendar` and the hour cycle from the short localized time
 * pattern, both for the default formatting locale.
 *
 * @return The JVM's conventions.
 */
@Composable
internal actual fun rememberPlatformDateTimeLocale(): DateTimeLocale = remember {
    val locale = Locale.getDefault(Locale.Category.FORMAT)
    val pattern = DateTimeFormatterBuilder.getLocalizedDateTimePattern(
        null,
        FormatStyle.SHORT,
        IsoChronology.INSTANCE,
        locale,
    )
    DateTimeLocale(
        firstDayOfWeek = dayOfWeekFromSundayBased(Calendar.getInstance(locale).firstDayOfWeek),
        timeFormat = timeFormatOfPattern(pattern),
    )
}
