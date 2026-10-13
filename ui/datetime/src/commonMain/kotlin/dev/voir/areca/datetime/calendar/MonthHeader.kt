package dev.voir.areca.datetime.calendar

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import dev.voir.areca.foundation.LocalContentColor
import dev.voir.areca.foundation.LocalTextStyle
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.button.IconButton
import dev.voir.areca.foundation.icon.Chevron
import dev.voir.areca.foundation.icon.ChevronDirection
import dev.voir.areca.foundation.icon.LocalChevronSize
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.modifier.thenIf
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.tween

/**
 * The band above the grid: the month on the left, back and forward on the right.
 *
 * The arrangement is the one iOS uses for an inline date picker, and it is the right one. A month
 * name is a label — it reads first, so it goes where reading starts — while back and forward are a
 * pair of related controls that belong together rather than at opposite ends of the header with the
 * label marooned between them.
 *
 * The month is a control, not a caption. Tapping it swaps the grid for a [MonthYearPicker], which is
 * the difference between a calendar you can only step through and one you can navigate. Its tap
 * target wraps the label rather than spanning the header, so what is pressable is what looks
 * pressable.
 *
 * It is a [Button] rather than a tappable label, and that is the point: the header holds three
 * controls, and a hand-rolled one answers a hover and a press in its own way however carefully its
 * colours are matched. Sharing the component means sharing the indication, the focus ring, the
 * cursor and the disabled treatment, so the month and the chevrons beside it cannot drift apart.
 *
 * The chevrons disable at the ends of the range rather than silently doing nothing, so the limits of
 * a bounded calendar are visible before the user pushes against them. While the picker is open they
 * fade away entirely: stepping one month at a time says nothing over a wheel that moves in years.
 *
 * @param modifier Modifier applied to the header row.
 * @param showControls Whether to draw the back and forward buttons.
 * @param showMonthPicker Whether tapping the month opens the picker. `false` leaves it as plain text
 *   — for a calendar whose range is a handful of months, where a wheel would be overkill.
 * @param style Colors. Defaults to the calendar's.
 * @param size Dimensions and type. Defaults to the calendar's.
 */
@Composable
public fun CalendarScope.MonthHeader(
    modifier: Modifier = Modifier,
    showControls: Boolean = true,
    showMonthPicker: Boolean = true,
    style: CalendarStyle = LocalCalendarStyle.current ?: CalendarStyle.Default,
    size: CalendarSize = LocalCalendarSize.current ?: CalendarSize.Default,
) {
    CompositionLocalProvider(
        LocalTextStyle provides size.headerTextStyle,
        LocalContentColor provides style.monthLabel,
        // Governs all three chevrons — the disclosure beside the month and the two nav buttons —
        // so they cannot drift apart. IconButton pushes down LocalIconSize but not this, so without
        // it the nav chevrons fall back to the theme's default and come out larger than the
        // disclosure. Provided as a local rather than passed per call site so a custom header slot
        // inherits the same size for free.
        LocalChevronSize provides size.headerChevron,
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(size.headerContentPadding),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (showMonthPicker) {
                Button(
                    onClick = { toggleMonthPicker() },
                    style = style.monthPickerToggle,
                    size = size.monthPickerToggle,
                    trailing = {
                        // Drawn in the stepping buttons' own content colour rather than the label's,
                        // so the three chevrons in this header cannot drift apart: whatever
                        // monthControl is restyled to, the disclosure follows.
                        CompositionLocalProvider(
                            LocalContentColor provides style.monthControl.content,
                        ) {
                            Chevron(
                                direction = if (isMonthPickerExpanded) {
                                    ChevronDirection.Up
                                } else {
                                    ChevronDirection.Down
                                },
                            )
                        }
                    },
                    modifier = Modifier.semantics {
                        // Names the control with the month it is showing, because a screen reader
                        // reaches the button without the surrounding header for context.
                        contentDescription =
                            "${labels.monthHeader(visibleMonth)}, ${labels.monthPickerAction}"
                        stateDescription = if (isMonthPickerExpanded) {
                            labels.monthPickerExpanded
                        } else {
                            labels.monthPickerCollapsed
                        }
                    },
                ) {
                    Text(text = labels.monthHeader(visibleMonth), maxLines = 1)
                }
            } else {
                Text(text = labels.monthHeader(visibleMonth), maxLines = 1)
            }

            Spacer(modifier = Modifier.weight(1f))

            if (showControls) {
                // Stepping one month at a time is meaningless over a wheel, so the controls go away
                // while it is open — but they fade rather than leave. They are the tallest thing in
                // the header, and removing them would shrink the header and pull the whole calendar
                // up, which is exactly the jump the body measurement exists to prevent.
                val controlsAlpha by animateFloatAsState(
                    targetValue = if (isMonthPickerExpanded) 0f else 1f,
                    animationSpec = ArecaTheme.motion.medium.tween(ArecaTheme.motion.standard),
                    label = "calendarMonthControlsAlpha",
                )
                Row(
                    modifier = Modifier
                        .alpha(controlsAlpha)
                        // Invisible, so not a control: no tap target, nothing to announce.
                        .thenIf(isMonthPickerExpanded) { clearAndSetSemantics { } },
                    horizontalArrangement = Arrangement.spacedBy(size.monthControlSpacing),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(
                        onClick = { previousMonth() },
                        enabled = canGoBack && !isMonthPickerExpanded,
                        style = style.monthControl.copy(shape = CircleShape),
                        size = size.monthControl,
                        modifier = Modifier.semantics {
                            contentDescription = labels.previousMonthAction
                        },
                    ) {
                        Chevron(direction = ChevronDirection.Left)
                    }
                    IconButton(
                        onClick = { nextMonth() },
                        enabled = canGoForward && !isMonthPickerExpanded,
                        style = style.monthControl.copy(shape = CircleShape),
                        size = size.monthControl,
                        modifier = Modifier.semantics {
                            contentDescription = labels.nextMonthAction
                        },
                    ) {
                        Chevron(direction = ChevronDirection.Right)
                    }
                }
            }
        }
    }
}
