package dev.voir.areca.datetime

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.Foundation.NSCalendar
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSLocale
import platform.Foundation.currentLocale

/**
 * Reads the week start from `NSCalendar.currentCalendar` and the hour cycle from the `"j"` skeleton
 * of `NSDateFormatter`, which resolves to the user's preferred hour format, 24-hour setting
 * included.
 *
 * @return The device's conventions.
 */
@Composable
internal actual fun rememberPlatformDateTimeLocale(): DateTimeLocale = remember {
    val pattern = NSDateFormatter.dateFormatFromTemplate(
        tmplate = "j",
        options = 0u,
        locale = NSLocale.currentLocale,
    ).orEmpty()
    DateTimeLocale(
        firstDayOfWeek = dayOfWeekFromSundayBased(NSCalendar.currentCalendar.firstWeekday.toInt()),
        timeFormat = timeFormatOfPattern(pattern),
    )
}
