package dev.voir.areca.foundation.wheel

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.spring
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.setProgress
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.Velocity
import dev.voir.areca.indication.TouchIndication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlin.math.floor
import kotlin.math.roundToInt
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.TimeMark
import kotlin.time.TimeSource

/**
 * Quiet time after a scroll stops before the wheel corrects itself onto a row.
 *
 * Long enough that the gap between two notches of a mouse wheel does not read as the end of the
 * gesture, short enough that the correction still feels like part of it.
 */
private const val SettleDelayMillis: Long = 80L

/**
 * Shortest gap between two ticks.
 *
 * A throw crosses rows faster than any device can answer, and asking for feedback the hardware
 * cannot keep up with queues it: the ticks arrive after the rows they belong to, which reads as the
 * wheel lagging. Rate-limiting keeps the tick attached to the motion.
 */
private val MinHapticInterval: Duration = 40.milliseconds

/** Gap between scroll events above which the next one begins a fresh, unaccelerated gesture. */
private const val ScrollGestureIdleMillis: Float = 150f

/**
 * One wheel of a [WheelPicker].
 *
 * Scrolls by drag, by throw, by mouse wheel or trackpad, by arrow and page keys, and by clicking a
 * row to bring it to the centre. Whichever of those the user reaches for, the wheel comes to rest on
 * a whole row: a throw's travel is capped and rounded before it is animated, and anything that still
 * ends between two rows is corrected once the wheel goes quiet.
 *
 * How far a scroll carries depends on how long it is sustained, not only on how far it asks to go:
 * [WheelPickerScroll.acceleration] winds the wheel up over the course of a gesture that keeps going,
 * while leaving a short one exactly aimable. Each gesture starts from rest.
 *
 * Scroll-wheel travel is handled here rather than left to `Modifier.scrollable`, and measured in
 * scroll units rather than pixels. The platform's own pixels-per-notch differs per operating system
 * and, on Windows, scales with the height of the element being scrolled, so a wheel left to the
 * platform would turn by a different amount on every desktop and turn faster inside a taller picker.
 * One unit is one notch everywhere, and [WheelPickerScroll.rowsPerUnit] says what a notch means.
 *
 * Only the rows on screen plus a row of margin either side are composed, so a wheel of sixty minutes
 * costs the same as a wheel of two.
 *
 * The column is one tab stop and draws the theme's focus ring while it holds keyboard focus. To
 * assistive technology it is one adjustable value: it reports the settled item through
 * [valueDescription] and its position among the items, so the platform's own adjust gestures —
 * swipe up and down, the rotor — turn it a row at a time.
 *
 * @param items Values the wheel shows, in order. An empty list draws an empty column of the right
 *   height.
 * @param state Position of this wheel. Create it with [rememberWheelPickerColumnState].
 * @param modifier Modifier applied to the column. Its height is fixed by [size].
 * @param style Colours and the falloff handed to [WheelPickerItem].
 * @param size Row height, visible row count and how far a scroll or throw travels.
 * @param scroll How hard the wheel answers scrolling. `null`, the default, lets the wheel choose
 *   from how many rows it holds — see [WheelPickerScroll.forItemCount] — which is right often enough
 *   that passing one is the exception.
 * @param enabled Whether the wheel responds to scrolling, keys and clicks.
 * @param scrollPropagation What becomes of scrolling past the ends of a finite wheel.
 * @param hapticFeedback Whether to tick as each row crosses the centre, as a physical wheel does.
 * @param contentDescription What this wheel selects, for assistive technology. For example
 *   `"Hour"`.
 * @param valueDescription How the settled item is read aloud. The item's `toString()` by default;
 *   pass a formatter when the rows draw something else, such as a month name for a month number.
 * @param onSettledIndexChange Called with [WheelPickerColumnState.settledIndex] once the wheel comes
 *   to rest somewhere new. Not called for the position the wheel started on, nor for the rows it
 *   merely passed over, so it can drive a form value without reporting choices the user did not
 *   make.
 * @param itemContent Renders one row. Receives the item and, through the receiver, where the row
 *   sits on the wheel.
 */
@Composable
public fun <T> WheelPickerColumn(
    items: List<T>,
    state: WheelPickerColumnState,
    modifier: Modifier = Modifier,
    style: WheelPickerStyle = LocalWheelPickerStyle.current ?: WheelPickerStyle.Default,
    size: WheelPickerSize = LocalWheelPickerSize.current ?: WheelPickerSize.Default,
    scroll: WheelPickerScroll? = LocalWheelPickerScroll.current,
    enabled: Boolean = true,
    scrollPropagation: WheelPickerScrollPropagation = WheelPickerScrollPropagation.Contain,
    hapticFeedback: Boolean = true,
    contentDescription: String? = null,
    valueDescription: (item: T) -> String = { item -> item.toString() },
    onSettledIndexChange: ((index: Int) -> Unit)? = null,
    itemContent: @Composable WheelPickerItemScope.(item: T) -> Unit = { item ->
        WheelPickerItem(item.toString())
    },
) {
    val resolvedScroll = scroll ?: remember(items.size) {
        WheelPickerScroll.forItemCount(items.size)
    }
    val density = LocalDensity.current
    val rowHeightPx = with(density) { size.itemHeight.toPx() }
    // A spring rather than one of the duration tokens: a settle that follows a throw has to start at
    // the speed the throw ended, which a fixed duration cannot express.
    val snapSpec = remember {
        spring<Float>(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMediumLow,
        )
    }

    SideEffect {
        state.rowHeightPx = rowHeightPx
        state.snapSpec = snapSpec
        state.scroll = resolvedScroll
        state.updateItemCount(items.size)
    }

    WheelPickerColumnEffects(
        state = state,
        hapticFeedback = hapticFeedback,
        onSettledIndexChange = onSettledIndexChange,
    )

    val scope = rememberCoroutineScope()
    val decay = remember { exponentialDecay<Float>() }
    val flingBehavior = remember(state, snapSpec, resolvedScroll.maxFlingRows) {
        WheelPickerFlingBehavior(
            state = state,
            decay = decay,
            snapSpec = snapSpec,
            maxFlingRows = resolvedScroll.maxFlingRows,
        )
    }

    val anchorRow by remember(state) { derivedStateOf { floor(state.offset).toInt() } }
    val isEndless = state.infinite && items.size > 1
    val focusSource = remember { MutableInteractionSource() }
    // Read in composition so the semantics follow each settle; the wheel only settles at the end of
    // a gesture, so this recomposes the column once per choice rather than once per row crossed.
    val settledIndex = state.settledIndex
    val settledValue = items.getOrNull(settledIndex)?.let(valueDescription)

    CompositionLocalProvider(
        LocalWheelPickerSize provides size,
        LocalWheelPickerStyle provides style,
        LocalWheelPickerScroll provides scroll,
    ) {
        Box(
            modifier = modifier
                .height(size.height)
                // Before the clip, so the ring drawn outside the column is not cut off.
                .indication(focusSource, TouchIndication(shape = style.highlightShape))
                .clipToBounds()
                .semantics {
                    if (contentDescription != null) this.contentDescription = contentDescription
                    if (settledValue != null) {
                        stateDescription = settledValue
                        progressBarRangeInfo = wheelRangeInfo(settledIndex, items.size)
                        if (enabled) {
                            setProgress { target ->
                                scope.launch { state.animateTo(target.roundToInt()) }
                                true
                            }
                        }
                    }
                    if (!enabled) disabled()
                }
                .selectableGroup()
                .wheelKeys(state = state, size = size, enabled = enabled, scope = scope)
                .focusable(enabled = enabled, interactionSource = focusSource)
                .wheelSteps(
                    state = state,
                    scroll = resolvedScroll,
                    enabled = enabled,
                    scrollPropagation = scrollPropagation,
                    scope = scope,
                )
                .then(
                    if (scrollPropagation == WheelPickerScrollPropagation.Contain) {
                        Modifier.nestedScroll(ContainVerticalScroll)
                    } else {
                        Modifier
                    },
                )
                .scrollable(
                    state = state.scrollableState,
                    orientation = Orientation.Vertical,
                    enabled = enabled && items.isNotEmpty(),
                    reverseDirection = true,
                    flingBehavior = flingBehavior,
                ),
        ) {
            if (items.isEmpty()) return@Box
            val side = size.sideItemsCount
            for (row in (anchorRow - side - 1)..(anchorRow + side + 1)) {
                if (!isEndless && row !in items.indices) continue
                val index = wrapIndex(row, items.size)
                key(row) {
                    // Remembered so the row sees the same instance on every recomposition of the
                    // column. A lambda rebuilt here, or the coroutine scope passed down raw, would
                    // make the row unskippable and undo the point of keying the rows at all.
                    val onClick: () -> Unit = remember(state, index, scope) {
                        { scope.launch { state.animateTo(index) } }
                    }
                    WheelPickerRow(
                        state = state,
                        row = row,
                        index = index,
                        item = items[index],
                        rowHeightPx = rowHeightPx,
                        side = side,
                        itemHeight = size.itemHeight,
                        enabled = enabled,
                        onClick = onClick,
                        itemContent = itemContent,
                    )
                }
            }
        }
    }
}

/**
 * One drawn row of a wheel.
 *
 * Placed by a graphics layer rather than by layout, so the wheel turning re-draws the rows without
 * re-measuring them.
 *
 * Every parameter is of a stable type, and the item is passed in rather than read out of the list
 * through a lambda, so a row that stays on screen while the wheel turns past it is skipped rather
 * than recomposed. An unstable parameter here — the caller's list, a coroutine scope — costs a text
 * layout per visible row per row crossing, which on an endless wheel under a throw is most of the
 * frame budget.
 *
 * @param state Column this row belongs to.
 * @param row Row position, which on an endless wheel runs beyond the item indices.
 * @param index Item this row draws.
 * @param item Value this row draws.
 * @param rowHeightPx Height of a row in pixels.
 * @param side Rows shown above the centre.
 * @param itemHeight Height of a row.
 * @param enabled Whether clicking the row brings it to the centre.
 * @param onClick Brings this row to the centre.
 * @param itemContent Renders the item.
 */
@Composable
private fun <T> WheelPickerRow(
    state: WheelPickerColumnState,
    row: Int,
    index: Int,
    item: T,
    rowHeightPx: Float,
    side: Int,
    itemHeight: Dp,
    enabled: Boolean,
    onClick: () -> Unit,
    itemContent: @Composable WheelPickerItemScope.(T) -> Unit,
) {
    val itemScope = remember(state, row, index) {
        WheelPickerItemScopeImpl(state = state, row = row, index = index)
    }
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(itemHeight)
            .graphicsLayer { translationY = (row - state.offset + side) * rowHeightPx }
            // Selectable for assistive technology and for the tap that brings a row to the centre,
            // but not a tab stop: a five-row wheel would otherwise put six stops in the tab order
            // where one belongs. The column itself is the focus target, and the arrow keys turn it.
            .focusProperties { canFocus = false }
            .selectable(
                selected = itemScope.isSelected,
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        with(itemScope) { itemContent(item) }
    }
}

/**
 * Keeps a wheel on a row, ticks as it turns, and reports where it stopped.
 *
 * @param state Column to watch.
 * @param hapticFeedback Whether to tick as each row crosses the centre.
 * @param onSettledIndexChange Called once the wheel comes to rest somewhere new.
 */
@Composable
private fun WheelPickerColumnEffects(
    state: WheelPickerColumnState,
    hapticFeedback: Boolean,
    onSettledIndexChange: ((index: Int) -> Unit)?,
) {
    // Every scroll ends here, whatever started it. `collectLatest` restarts the wait each time
    // scrolling resumes, so a run of mouse-wheel notches settles once at the end rather than
    // fighting the user between notches.
    LaunchedEffect(state) {
        snapshotFlow { state.isScrollInProgress }
            .filter { inProgress -> !inProgress }
            .collectLatest {
                delay(SettleDelayMillis)
                state.settle()
            }
    }

    if (hapticFeedback) {
        val haptics = LocalHapticFeedback.current
        LaunchedEffect(state, haptics) {
            var lastTick: TimeMark? = null
            snapshotFlow { state.currentIndex }
                .drop(1)
                .collect {
                    val previous = lastTick
                    if (previous == null || previous.elapsedNow() >= MinHapticInterval) {
                        lastTick = TimeSource.Monotonic.markNow()
                        haptics.performHapticFeedback(HapticFeedbackType.SegmentTick)
                    }
                }
        }
    }

    if (onSettledIndexChange != null) {
        val callback by rememberUpdatedState(onSettledIndexChange)
        LaunchedEffect(state) {
            snapshotFlow { state.settledIndex }
                .drop(1)
                .collect { index -> callback(index) }
        }
    }
}

/**
 * Turns scroll-wheel and trackpad events into rows of travel.
 *
 * Handled on the initial pass and consumed there, so the event is spent on the wheel the pointer is
 * over before any ancestor can take it. That is what stops scrolling one picker from scrolling the
 * page — and with it every other picker on the page.
 *
 * @param state Column to turn.
 * @param scroll Pacing giving the rows each scroll unit is worth, and the ceiling on one event.
 * @param enabled Whether the wheel responds at all.
 * @param scrollPropagation Whether travel the wheel cannot use is kept or let through.
 * @param scope Scope the stepping animation runs in.
 * @return Modifier handling scroll events.
 */
private fun Modifier.wheelSteps(
    state: WheelPickerColumnState,
    scroll: WheelPickerScroll,
    enabled: Boolean,
    scrollPropagation: WheelPickerScrollPropagation,
    scope: CoroutineScope,
): Modifier = pointerInput(state, scroll, enabled, scrollPropagation) {
    awaitPointerEventScope {
        // Carried across events for as long as this handler lives, which is exactly the span over
        // which "how long has this gesture been running" is a meaningful question.
        var sustainedMillis = 0f
        var lastEventMillis: Long? = null

        while (true) {
            val event = awaitPointerEvent(PointerEventPass.Initial)
            if (event.type != PointerEventType.Scroll || !enabled) continue
            val units = event.changes.fold(0f) { total, change -> total + change.scrollDelta.y }
            if (units == 0f) continue

            val eventMillis = event.changes.first().uptimeMillis
            val gapMillis = lastEventMillis
                ?.let { previous -> (eventMillis - previous).toFloat() }
                ?: Float.MAX_VALUE
            lastEventMillis = eventMillis
            sustainedMillis = sustainedScrollMillis(
                previous = sustainedMillis,
                gapMillis = gapMillis,
                idleMillis = ScrollGestureIdleMillis,
            )
            val gain = scrollGain(
                sustainedMillis = sustainedMillis,
                windUpMillis = scroll.acceleration.windUp.inWholeMilliseconds.toFloat(),
                maxGain = scroll.acceleration.maxGain,
            )

            // Capped per event rather than per gesture: the driver's acceleration lands in the size
            // of each event, so this is where it can be removed without also shortening a long,
            // deliberate scroll. The gain then puts back a curve measured in rows instead of pixels.
            val rows = (units * scroll.rowsPerUnit)
                .coerceIn(-scroll.maxRowsPerEvent, scroll.maxRowsPerEvent) * gain
            val canTravel = if (rows > 0f) state.canScrollForward else state.canScrollBackward
            if (canTravel || scrollPropagation == WheelPickerScrollPropagation.Contain) {
                event.changes.forEach { change -> change.consume() }
            }
            if (canTravel && state.requestStepBy(rows, gain)) {
                scope.launch { state.followSteps() }
            }
        }
    }
}

/**
 * Moves a focused wheel with the arrow, page, home and end keys.
 *
 * @param state Column to turn.
 * @param size Metrics giving what a page is worth.
 * @param enabled Whether the wheel responds at all.
 * @param scope Scope the animation runs in.
 * @return Modifier handling key events.
 */
private fun Modifier.wheelKeys(
    state: WheelPickerColumnState,
    size: WheelPickerSize,
    enabled: Boolean,
    scope: CoroutineScope,
): Modifier = onKeyEvent { event ->
    if (!enabled || event.type != KeyEventType.KeyDown || state.itemCount == 0) {
        return@onKeyEvent false
    }
    val rows = when (event.key) {
        Key.DirectionUp -> -1
        Key.DirectionDown -> 1
        Key.PageUp -> -size.visibleItemsCount
        Key.PageDown -> size.visibleItemsCount
        Key.MoveHome -> -state.settledIndex
        Key.MoveEnd -> state.itemCount - 1 - state.settledIndex
        else -> return@onKeyEvent false
    }
    if (rows != 0) {
        scope.launch { state.animateByRows(rows) }
    }
    true
}

/**
 * Keeps vertical scrolling inside the picker.
 *
 * A wheel that has run out of rows would otherwise hand the rest of the gesture to whatever
 * scrolling container the picker sits in, which moves the page under the pointer and drags every
 * other wheel on screen with it.
 */
private object ContainVerticalScroll : NestedScrollConnection {
    /**
     * Swallows vertical travel the wheel did not use.
     *
     * @param consumed Travel already used.
     * @param available Travel left over.
     * @param source What produced the scroll.
     * @return The whole vertical remainder, marking it as used.
     */
    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource,
    ): Offset = Offset(x = 0f, y = available.y)

    /**
     * Swallows vertical velocity the wheel did not use.
     *
     * @param consumed Velocity already used.
     * @param available Velocity left over.
     * @return The whole vertical remainder, marking it as used.
     */
    override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity =
        Velocity(x = 0f, y = available.y)
}

/**
 * The position a wheel reports to assistive technology.
 *
 * One step per item, so an adjustment moves the wheel exactly one row.
 *
 * @param settledIndex Item the wheel rests on.
 * @param itemCount Number of items on the wheel; at least one.
 * @return [settledIndex] within `0` to the last index, with a step between every pair of items.
 */
internal fun wheelRangeInfo(settledIndex: Int, itemCount: Int): ProgressBarRangeInfo {
    val last = (itemCount - 1).coerceAtLeast(0)
    return ProgressBarRangeInfo(
        current = settledIndex.coerceIn(0, last).toFloat(),
        range = 0f..last.toFloat(),
        steps = (itemCount - 2).coerceAtLeast(0),
    )
}
