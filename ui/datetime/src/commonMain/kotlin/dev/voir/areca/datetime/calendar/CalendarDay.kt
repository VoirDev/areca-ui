package dev.voir.areca.datetime.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.Snapshot
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.CollectionItemInfo
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.collectionItemInfo
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.LayoutDirection
import dev.voir.areca.foundation.LocalContentColor
import dev.voir.areca.foundation.LocalTextStyle
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.indication.TouchIndication
import dev.voir.areca.modifier.clickCursor
import dev.voir.areca.modifier.thenIf
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.tween
import kotlinx.coroutines.flow.filter

/**
 * One day cell.
 *
 * The circle is drawn on a square that the grid sizes, so this composable never states a size: it
 * fills the cell it is given, insets the circle by [CalendarSize.dayPadding], and lets the number
 * sit in the middle. The whole cell takes the tap, padding included, while the press and the focus
 * ring are drawn on the circle. Colors come from [CalendarStyle.resolveDay], which is where the
 * precedence between chosen, today, unavailable and adjacent is decided.
 *
 * All three colors animate, including the border, so today's ring fades in rather than appearing the
 * instant a page settles.
 *
 * The cell is one node to assistive technology: a selectable day reading
 * [CalendarLabels.dayDescription], with its row and column in the grid. In a calendar that takes
 * taps, the days of the page's own month take part in the grid's keyboard navigation — unavailable
 * ones included, so the arrows can cross them — while the spill-over days of neighbouring months
 * stay out of the tab order.
 *
 * @param day Everything about the day this cell draws.
 * @param modifier Modifier applied to the cell.
 * @param onClick Called when the day is tapped. Ignored when the day is unavailable, and `null` for
 *   a read-only calendar.
 * @param style Colors. Defaults to the calendar's.
 * @param size Dimensions and type. Defaults to the calendar's.
 * @param interactionSource Source the press and focus indication is driven from, or `null` for one
 *   the cell creates. Shared by the cell that takes the tap and the circle that draws it.
 */
@Composable
public fun CalendarScope.CalendarDay(
    day: CalendarDayState,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    style: CalendarStyle = LocalCalendarStyle.current ?: CalendarStyle.Default,
    size: CalendarSize = LocalCalendarSize.current ?: CalendarSize.Default,
    interactionSource: MutableInteractionSource? = null,
) {
    val source = interactionSource ?: remember { MutableInteractionSource() }
    val colors = style.resolveDay(
        selected = day.isSelected,
        isToday = day.isToday,
        enabled = day.isEnabled,
        adjacent = day.isAdjacent,
    )
    val motion = ArecaTheme.motion
    val focusable = onClick != null && !day.isAdjacent

    val backgroundColor by animateDayColorAsState(
        targetValue = colors.bg,
        animationSpec = motion.fast.tween(motion.standard),
        label = "calendarDayBackground",
    )
    val contentColor by animateDayColorAsState(
        targetValue = colors.content,
        animationSpec = motion.fast.tween(motion.standard),
        label = "calendarDayContent",
    )
    val borderColor by animateDayColorAsState(
        targetValue = colors.border,
        animationSpec = motion.fast.tween(motion.standard),
        label = "calendarDayBorder",
    )

    CompositionLocalProvider(
        LocalTextStyle provides size.dayTextStyle,
        LocalContentColor provides contentColor,
    ) {
        // Tapped on the whole cell so the gaps between circles are not dead space; the press is
        // drawn on the circle from the same interaction source.
        Box(
            modifier = modifier
                .fillMaxSize()
                .then(dayCell(day = day, focusable = focusable))
                .semantics { contentDescription = labels.dayDescription(day.date) }
                .dayInteraction(
                    day = day,
                    onClick = onClick,
                    focusable = focusable,
                    interactionSource = source,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .padding(size.dayPadding)
                    .fillMaxSize()
                    .background(color = backgroundColor, shape = CircleShape)
                    .border(width = size.dayBorderWidth, color = borderColor, shape = CircleShape)
                    .dayIndication(
                        visible = onClick != null && (day.isEnabled || focusable),
                        interactionSource = source,
                        shape = CircleShape,
                    )
                    // The cell already reads the whole date; the drawn number is not a second node.
                    .clearAndSetSemantics { },
                contentAlignment = Alignment.Center,
            ) {
                Text(text = labels.dayNumber(day.date.day))
            }
        }
    }
}

/**
 * Grid position and keyboard navigation for a built-in day cell.
 *
 * States the cell's row and column, so assistive technology reads the page as a table. When
 * [focusable], the cell takes part in the grid's roving focus: it is focusable only while it holds
 * the calendar's one tab stop or a move is waiting on it, records itself as the keyboard's resting
 * day when focused, turns the arrow, Home, End and page keys into [CalendarScope.moveFocus], and
 * takes focus once a move lands on it and its page is composed. Otherwise the cell is kept out of
 * the tab order entirely.
 *
 * Apply it before the modifier that makes the cell clickable, so its focus properties govern that
 * modifier's focus target.
 *
 * @param day The day the cell draws.
 * @param focusable Whether the cell takes part in keyboard navigation.
 * @return Modifier carrying the cell's grid semantics and focus behaviour.
 */
@Composable
internal fun CalendarScope.dayCell(day: CalendarDayState, focusable: Boolean): Modifier {
    val scope = this
    val cell = remember(day.month, day.date, scope.startDayOfWeek) {
        day.month.cellOf(day.date, scope.startDayOfWeek)
    }
    val positioned = Modifier.semantics {
        collectionItemInfo = CollectionItemInfo(
            rowIndex = cell.row,
            rowSpan = 1,
            columnIndex = cell.column,
            columnSpan = 1,
        )
    }
    if (!focusable) return positioned.focusProperties { canFocus = false }

    val requester = remember { FocusRequester() }
    val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl
    LaunchedEffect(scope, day.date) {
        snapshotFlow { scope.pendingFocus == day.date }
            .filter { pending -> pending }
            .collect {
                if (requester.requestFocus(FocusDirection.Enter)) scope.pendingFocus = null
            }
    }
    return positioned
        // Read without observation: the answer is only needed when the focus system asks, and an
        // observed read would make the focused cell drop focus the moment a key moves the tab stop,
        // before the next cell has taken it. A cell a move is waiting on may take focus before the
        // calendar has paged to its month.
        .focusProperties {
            canFocus = Snapshot.withoutReadObservation {
                scope.pendingFocus == day.date || scope.tabStop == day.date
            }
        }
        .focusRequester(requester)
        .onFocusChanged { state -> if (state.isFocused) scope.activeDate = day.date }
        .onKeyEvent { event ->
            if (event.type != KeyEventType.KeyDown) return@onKeyEvent false
            val move = calendarMoveFor(event.key, event.isShiftPressed, isRtl)
                ?: return@onKeyEvent false
            scope.moveFocus(day.date, move)
            true
        }
}

/**
 * The tap and selection behaviour of a built-in day cell.
 *
 * A calendar that takes taps makes each day a selectable button, refusing the tap on an unavailable
 * day; an unavailable day of the page's own month stays focusable so the arrows can cross it. A
 * read-only calendar only states whether each day is chosen or unavailable.
 *
 * @param day The day the cell draws.
 * @param onClick Called when the day is tapped, or `null` for a read-only calendar.
 * @param focusable Whether the cell takes part in keyboard navigation.
 * @param interactionSource Source the cell reports presses and focus to.
 * @return Modifier making the cell tappable or describing it.
 */
@Composable
internal fun Modifier.dayInteraction(
    day: CalendarDayState,
    onClick: (() -> Unit)?,
    focusable: Boolean,
    interactionSource: MutableInteractionSource,
): Modifier {
    if (onClick == null) {
        return semantics {
            selected = day.isSelected
            if (!day.isEnabled) disabled()
        }
    }
    return selectable(
        selected = day.isSelected,
        interactionSource = interactionSource,
        indication = null,
        enabled = day.isEnabled,
        role = Role.Button,
        onClick = onClick,
    )
        .clickCursor(enabled = day.isEnabled)
        .thenIf(focusable && !day.isEnabled) { focusable(interactionSource = interactionSource) }
}

/**
 * The press and focus indication of a day's circle.
 *
 * @param visible Whether the cell reacts at all: a tappable day, or an unavailable one that can
 *   still hold keyboard focus.
 * @param interactionSource Source shared with the cell that takes the tap.
 * @param shape Shape of the circle, which the indication follows.
 * @return Modifier drawing the indication, or the receiver unchanged when not [visible].
 */
@Composable
internal fun Modifier.dayIndication(
    visible: Boolean,
    interactionSource: MutableInteractionSource,
    shape: Shape,
): Modifier = thenIf(visible) {
    indication(
        interactionSource = interactionSource,
        indication = TouchIndication(shape = shape),
    )
}
