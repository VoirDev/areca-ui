package dev.voir.areca.datetime.calendar

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.button.ButtonSize
import dev.voir.areca.foundation.button.ButtonStyle

/**
 * A control that returns the calendar to today.
 *
 * Not part of the default header — most calendars open on today and never leave it far enough to
 * need one. Add it beside a header, or into a custom one, for a calendar people navigate a long way:
 * a schedule, a history, a report.
 *
 * Disabled once the calendar is already showing today's month, and whenever today lies outside the
 * calendar's bounds, so it never looks like a control that does nothing.
 *
 * @param modifier Modifier applied to the button.
 * @param style Colors. Defaults to a plain button.
 * @param size Dimensions. Defaults to small.
 * @param interactionSource Source the button reports presses, hover and focus to, or `null` for
 *   one it creates.
 */
@Composable
public fun CalendarScope.TodayButton(
    modifier: Modifier = Modifier,
    style: ButtonStyle = ButtonStyle.plain(),
    size: ButtonSize = ButtonSize.Small,
    interactionSource: MutableInteractionSource? = null,
) {
    Button(
        text = labels.todayAction,
        onClick = { goToToday() },
        modifier = modifier,
        style = style,
        size = size,
        enabled = canGoToToday(visibleMonth = visibleMonth, today = state.today, bounds = bounds),
        interactionSource = interactionSource,
    )
}
