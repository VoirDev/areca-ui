package dev.voir.areca.datetime.calendar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import dev.voir.areca.foundation.LocalContentColor
import dev.voir.areca.foundation.LocalTextStyle
import dev.voir.areca.foundation.text.Text

/**
 * The Mo/Tu/We row.
 *
 * Each heading takes an equal share of the width, the same share a day cell takes, which is what
 * keeps the two bands in column however wide the calendar is drawn.
 *
 * The headings are the columns' names, so assistive technology reads each by its full name
 * ([CalendarLabels.weekdayName]) rather than spelling out the drawn abbreviation. Each day of the
 * grid also carries its weekday in its own description, because a screen reader visiting a cell
 * does not read the heading above it.
 *
 * @param modifier Modifier applied to the row.
 * @param style Colors. Defaults to the calendar's.
 * @param size Dimensions and type. Defaults to the calendar's.
 */
@Composable
public fun CalendarScope.WeekdayHeader(
    modifier: Modifier = Modifier,
    style: CalendarStyle = LocalCalendarStyle.current ?: CalendarStyle.Default,
    size: CalendarSize = LocalCalendarSize.current ?: CalendarSize.Default,
) {
    val weekdays = remember(startDayOfWeek) { daysOfWeekFrom(startDayOfWeek) }

    CompositionLocalProvider(
        LocalTextStyle provides size.weekdayTextStyle,
        LocalContentColor provides style.weekdayLabel,
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(size.weekdayContentPadding),
        ) {
            weekdays.forEach { dayOfWeek ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(size.weekdayPadding)
                        .clearAndSetSemantics { contentDescription = labels.weekdayName(dayOfWeek) },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = labels.weekday(dayOfWeek), maxLines = 1)
                }
            }
        }
    }
}
