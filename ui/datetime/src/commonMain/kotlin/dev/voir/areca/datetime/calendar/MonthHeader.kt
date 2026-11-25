package dev.voir.areca.datetime.calendar

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.IntOffset
import dev.voir.areca.foundation.LocalContentColor
import dev.voir.areca.foundation.LocalTextStyle
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.button.IconButton
import dev.voir.areca.foundation.icon.Chevron
import dev.voir.areca.foundation.icon.ChevronDirection
import dev.voir.areca.foundation.icon.LocalChevronSize
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.tween
import kotlinx.datetime.number

/**
 * The band above the grid: the title on the left, back and forward on the right.
 *
 * The arrangement is the one iOS uses for an inline date picker. The title is a label — it reads
 * first, so it goes where reading starts — while back and forward are a pair of related controls
 * that belong together rather than at opposite ends of the header with the label marooned between
 * them.
 *
 * The header follows the calendar's [CalendarLevel]. Over the days the title is the month; over the
 * months it is the year, with a "Pick a month" hint beside it; over the years it is the span of the
 * page, with "Pick a year". The chevrons step by the level — a month (a week while the days are
 * [folded][CalendarState.isFolded]), a year, a page of years — and
 * disable at the ends of the bounds rather than silently doing nothing, so the limits of a bounded
 * calendar are visible before the user pushes against them.
 *
 * The title is a control while there is a wider level to step out to ([CalendarState.canZoomOut]):
 * tapping the month shows the months, tapping the year shows the years. It is a [Button] rather than
 * a tappable label, so the title and the chevrons beside it share one indication, focus ring, cursor
 * and disabled treatment. On the years, and whenever [zoomable] is `false`, it is plain text marked
 * as a heading.
 *
 * @param modifier Modifier applied to the header row.
 * @param showControls Whether to draw the back and forward buttons.
 * @param zoomable Whether tapping the title steps out to the months and years. `false` leaves it as
 *   plain text — for a calendar whose range is a handful of months.
 * @param style Colors. Defaults to the calendar's.
 * @param size Dimensions and type. Defaults to the calendar's.
 * @param actions Controls drawn before the chevrons, such as a "Today" pill. `null` draws none.
 */
@Composable
public fun CalendarScope.MonthHeader(
    modifier: Modifier = Modifier,
    showControls: Boolean = true,
    zoomable: Boolean = true,
    style: CalendarStyle = LocalCalendarStyle.current ?: CalendarStyle.Default,
    size: CalendarSize = LocalCalendarSize.current ?: CalendarSize.Default,
    actions: (@Composable RowScope.() -> Unit)? = null,
) {
    val title = when (level) {
        CalendarLevel.Days -> labels.monthHeader(visibleMonth)
        CalendarLevel.Months -> labels.year(state.browsedYear)
        CalendarLevel.Years -> labels.yearSpan(state.browsedYears)
    }
    // Where the title sits in time, so a later title slides in from below and an earlier one from
    // above, the way the grid pages under it.
    val titleOrder = when (level) {
        CalendarLevel.Days -> visibleMonth.year * MONTHS_IN_YEAR + visibleMonth.month.number
        CalendarLevel.Months -> state.browsedYear * MONTHS_IN_YEAR
        CalendarLevel.Years -> state.browsedYears.first * MONTHS_IN_YEAR
    }
    val hint = when (level) {
        CalendarLevel.Days -> null
        CalendarLevel.Months -> labels.pickMonthHint
        CalendarLevel.Years -> labels.pickYearHint
    }
    val (backAction, forwardAction) = when (level) {
        CalendarLevel.Days if state.isFolded -> labels.previousWeekAction to labels.nextWeekAction
        CalendarLevel.Days -> labels.previousMonthAction to labels.nextMonthAction
        CalendarLevel.Months -> labels.previousYearAction to labels.nextYearAction
        CalendarLevel.Years -> labels.previousYearsAction to labels.nextYearsAction
    }
    CompositionLocalProvider(
        LocalTextStyle provides size.headerTextStyle,
        LocalContentColor provides style.monthLabel,
        // Governs all three chevrons — the disclosure beside the title and the two nav buttons —
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
            if (zoomable && state.canZoomOut) {
                Button(
                    onClick = { zoomOut() },
                    style = style.levelToggle,
                    size = size.levelToggle,
                    trailing = {
                        // Drawn in the stepping buttons' own content colour rather than the label's,
                        // so the three chevrons in this header cannot drift apart: whatever
                        // monthControl is restyled to, the disclosure follows.
                        CompositionLocalProvider(
                            LocalContentColor provides style.monthControl.content,
                        ) {
                            Chevron(direction = ChevronDirection.Down)
                        }
                    },
                    modifier = Modifier.semantics {
                        // Names the control with what it is showing, because a screen reader reaches
                        // the button without the surrounding header for context.
                        contentDescription = when (level) {
                            CalendarLevel.Days -> "$title, ${labels.chooseMonthAction}"
                            else -> "$title, ${labels.chooseYearAction}"
                        }
                    },
                ) {
                    HeaderTitle(title = title, order = titleOrder)
                }
            } else {
                HeaderTitle(
                    title = title,
                    order = titleOrder,
                    // The toggle's own inset, so the title starts where it does over the days.
                    modifier = Modifier
                        .padding(size.levelToggle.padding)
                        .semantics { heading() },
                )
            }

            if (hint != null) {
                Text(
                    text = hint,
                    style = size.levelHintTextStyle,
                    color = style.levelHint,
                    maxLines = 1,
                    modifier = Modifier.padding(start = size.monthControlSpacing),
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            actions?.let { slot ->
                Row(
                    modifier = Modifier.padding(end = size.monthControlSpacing),
                    horizontalArrangement = Arrangement.spacedBy(size.monthControlSpacing),
                    verticalAlignment = Alignment.CenterVertically,
                    content = slot,
                )
            }

            if (showControls) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(size.monthControlSpacing),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(
                        onClick = { goBack() },
                        enabled = canGoBack,
                        style = style.monthControl.copy(shape = CircleShape),
                        size = size.monthControl,
                        modifier = Modifier.semantics { contentDescription = backAction },
                    ) {
                        Chevron(direction = ChevronDirection.Left)
                    }
                    IconButton(
                        onClick = { goForward() },
                        enabled = canGoForward,
                        style = style.monthControl.copy(shape = CircleShape),
                        size = size.monthControl,
                        modifier = Modifier.semantics { contentDescription = forwardAction },
                    ) {
                        Chevron(direction = ChevronDirection.Right)
                    }
                }
            }
        }
    }
}

/** Months in a year, for ordering header titles in time. */
private const val MONTHS_IN_YEAR: Int = 12

/** A header title and where it sits in time. */
private data class TitleKey(val title: String, val order: Int)

/**
 * The header's title, which slides up to a later title and down to an earlier one, and fades in
 * place when it does not move in time (stepping between levels). Under
 * [reduced motion][dev.voir.areca.theme.ArecaMotion.reduceMotion] every change fades.
 *
 * @param title Text to show.
 * @param order Where [title] sits in time; compared with the previous title to pick a direction.
 * @param modifier Modifier applied to the title.
 */
@Composable
private fun HeaderTitle(title: String, order: Int, modifier: Modifier = Modifier) {
    val motion = ArecaTheme.motion
    AnimatedContent(
        targetState = TitleKey(title, order),
        modifier = modifier,
        contentAlignment = Alignment.CenterStart,
        transitionSpec = {
            val fade = motion.medium.tween<Float>(motion.standard)
            val direction = targetState.order.compareTo(initialState.order)
            val transform = if (motion.reduceMotion || direction == 0) {
                fadeIn(fade) togetherWith fadeOut(fade)
            } else {
                val slide = motion.slow.tween<IntOffset>(motion.standard)
                (slideInVertically(slide) { it / 2 * direction } + fadeIn(fade)) togetherWith
                    (slideOutVertically(slide) { -it / 2 * direction } + fadeOut(fade))
            }
            transform using SizeTransform(clip = false)
        },
        label = "calendarHeaderTitle",
    ) { key ->
        Text(text = key.title, maxLines = 1)
    }
}
