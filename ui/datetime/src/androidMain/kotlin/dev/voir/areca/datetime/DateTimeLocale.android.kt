package dev.voir.areca.datetime

import android.os.Build
import android.text.format.DateFormat
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import java.util.Calendar

/**
 * Reads the week start from `java.util.Calendar` for the configuration's locale and the hour cycle
 * from `DateFormat.is24HourFormat`, which honours the user's 24-hour setting.
 *
 * Keyed on the configuration, so a locale change while the app runs is followed.
 *
 * @return The device's conventions.
 */
@Composable
internal actual fun rememberPlatformDateTimeLocale(): DateTimeLocale {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    return remember(context, configuration) {
        val locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.locales.get(0)
        } else {
            // `Configuration.locales` arrived in API 24; the single locale is all API 23 has.
            @Suppress("DEPRECATION")
            configuration.locale
        }
        DateTimeLocale(
            firstDayOfWeek = dayOfWeekFromSundayBased(Calendar.getInstance(locale).firstDayOfWeek),
            timeFormat = if (DateFormat.is24HourFormat(context)) {
                TimeFormat.Hours24
            } else {
                TimeFormat.Hours12
            },
        )
    }
}
