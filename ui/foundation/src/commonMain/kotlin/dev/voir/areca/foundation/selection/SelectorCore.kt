package dev.voir.areca.foundation.selection

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.isSpecified
import dev.voir.areca.foundation.LocalContentColor
import dev.voir.areca.foundation.LocalTextStyle
import dev.voir.areca.foundation.icon.LocalIconSize
import dev.voir.areca.modifier.clickCursor
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.tween
import kotlin.math.roundToInt

/** How a selector's options are announced, and so which semantics they carry. */
internal enum class SelectorRole {
    /** One of a group that always has one chosen: [SelectorMode.Required]. */
    RadioButton,

    /** An option that can be turned off again: [SelectorMode.Optional]. */
    Switch,

    /** A tab of [dev.voir.areca.foundation.tabs.Tabs] or a tab bar. */
    Tab,
}

/**
 * The engine under [SlidingSelector], [dev.voir.areca.foundation.tabs.Tabs] and
 * [dev.voir.areca.foundation.tabbar.FloatingTabBar]: the options, their measured bounds, the thumb
 * and its motion, scrolling into view, semantics and keyboard focus.
 *
 * Works by index, so each caller keeps its own item type and its own selection rules.
 *
 * @param count Number of options.
 * @param selectedIndex Position of the chosen option, or `-1` for none.
 * @param isEnabled Whether the option at an index can be chosen.
 * @param onTap Called with an enabled option's index when it is tapped, chosen or not. The caller
 *   decides what the tap means.
 * @param role How the options are announced.
 * @param style Look, thumb and motion.
 * @param size Metrics of the options.
 * @param layout How the options share the selector's length.
 * @param orientation Which way the selector runs.
 * @param modifier Modifier applied to the selector.
 * @param thumbColor Thumb colour while the option at an index is chosen.
 * @param contentSelectedColor Content colour of the option at an index while it is chosen.
 * @param contentDescription What a screen reader announces for the option at an index, or `null`
 *   for its content.
 * @param itemBackground Fill of the option at an index, given whether it is chosen and enabled.
 *   Drawn under the option's content and over the thumb.
 * @param itemModifier Modifier applied to every option, after its share of the length.
 * @param itemContent Content of the option at an index, given whether it is chosen.
 */
@Composable
internal fun SelectorCore(
    count: Int,
    selectedIndex: Int,
    isEnabled: (Int) -> Boolean,
    onTap: (Int) -> Unit,
    role: SelectorRole,
    style: SlidingSelectorStyle,
    size: SlidingSelectorSize,
    layout: SelectorLayout,
    orientation: SelectorOrientation,
    modifier: Modifier = Modifier,
    thumbColor: (Int) -> Color = { style.thumb },
    contentSelectedColor: (Int) -> Color = { style.contentSelected },
    contentDescription: (Int) -> String? = { null },
    itemBackground: (index: Int, selected: Boolean, enabled: Boolean) -> Color = { _, _, _ -> Color.Transparent },
    itemModifier: Modifier = Modifier,
    itemContent: @Composable (index: Int, selected: Boolean) -> Unit,
) {
    val horizontal = orientation == SelectorOrientation.Horizontal
    val layoutDirection = LocalLayoutDirection.current
    val scrollState = rememberScrollState()
    // A horizontal scroll counts from the start edge, which is the right edge in a right-to-left
    // layout, while measured bounds count from the left.
    val reversed = horizontal && layoutDirection == LayoutDirection.Rtl

    // Measured rather than derived: options differ in length, and equal-length maths puts the thumb
    // under the wrong option as soon as one label is longer than the others.
    val itemBounds = remember { mutableStateMapOf<Int, SelectorBounds>() }
    var viewportExtent by remember { mutableIntStateOf(0) }
    var contentExtent by remember { mutableIntStateOf(0) }
    var focusedIndex by remember { mutableIntStateOf(-1) }
    val selectedBounds = itemBounds[selectedIndex]

    LaunchedEffect(count) {
        itemBounds.keys.retainAll { it < count }
    }

    val thumb = remember { SelectorThumbState() }
    val motion by rememberUpdatedState(style.motion)
    val reduceMotion by rememberUpdatedState(ArecaTheme.motion.reduceMotion)
    val target = selectedBounds?.let {
        SelectorThumbTarget(index = selectedIndex, bounds = it, color = thumbColor(selectedIndex))
    }
    // An option chosen but not measured yet is not "nothing chosen": wait for it, so the first
    // decision the thumb takes is the real one.
    val measuring = selectedIndex >= 0 && selectedBounds == null
    LaunchedEffect(target, measuring) {
        if (!measuring) {
            thumb.moveTo(target, motion, reduceMotion)
        }
    }

    // Keeps the chosen option visible, including when the selection changes from code rather than
    // from a tap.
    LaunchedEffect(selectedBounds, viewportExtent, contentExtent, reversed) {
        val bounds = selectedBounds ?: return@LaunchedEffect
        if (layout != SelectorLayout.Scroll) return@LaunchedEffect

        val scrollTarget = SelectorScrollMath.scrollToReveal(
            itemStart = bounds.start,
            itemEnd = bounds.end,
            viewportExtent = viewportExtent,
            contentExtent = contentExtent,
            scroll = scrollState.value,
            reversed = reversed,
        )
        if (scrollTarget != null) {
            scrollState.animateScrollTo(scrollTarget)
        }
    }

    val focusManager = LocalFocusManager.current
    val selectorModifier = modifier
        .onSizeChanged { viewportExtent = if (horizontal) it.width else it.height }
        .then(
            when {
                layout == SelectorLayout.Fill && horizontal -> Modifier.fillMaxWidth()
                layout == SelectorLayout.Fill -> Modifier.fillMaxHeight()
                layout == SelectorLayout.Hug -> Modifier
                horizontal -> Modifier.horizontalScroll(scrollState)
                else -> Modifier.verticalScroll(scrollState)
            }
        )
        // After the scroll, so this is the content's full length rather than the visible window.
        .onSizeChanged { contentExtent = if (horizontal) it.width else it.height }
        .selectableGroup()
        .onKeyEvent { event ->
            if (event.type != KeyEventType.KeyDown) return@onKeyEvent false
            val forward = arrowDirection(event.key, horizontal, layoutDirection)
                ?: return@onKeyEvent false
            val from = focusedIndex.takeIf { it >= 0 } ?: return@onKeyEvent false
            val ahead = if (forward) (from + 1 until count) else (from - 1 downTo 0)
            // At the last enabled option, focus stays rather than leaving the group.
            if (ahead.any(isEnabled)) {
                focusManager.moveFocus(if (forward) FocusDirection.Next else FocusDirection.Previous)
            }
            true
        }
        .then(
            if (style.divider.alpha > 0f) {
                Modifier.drawDivider(style, horizontal)
            } else {
                Modifier
            }
        )
        .background(style.track, style.trackShape)
        .then(
            if (style.trackBorder.alpha > 0f) {
                Modifier.border(style.trackBorderWidth, style.trackBorder, style.trackShape)
            } else {
                Modifier
            }
        )
        .padding(style.trackPadding)

    val items: @Composable (shareModifier: Modifier) -> Unit = { shareModifier ->
        for (index in 0 until count) {
            val selected = index == selectedIndex
            val enabled = isEnabled(index)
            SelectorOption(
                selected = selected,
                enabled = enabled,
                role = role,
                style = style,
                size = size,
                contentSelected = contentSelectedColor(index),
                background = itemBackground(index, selected, enabled),
                contentDescription = contentDescription(index),
                onTap = { onTap(index) },
                onFocusChanged = { focused ->
                    if (focused) {
                        focusedIndex = index
                    } else if (focusedIndex == index) {
                        focusedIndex = -1
                    }
                },
                horizontal = horizontal,
                modifier = Modifier
                    .onGloballyPositioned {
                        val position = it.positionInParent()
                        itemBounds[index] = if (horizontal) {
                            SelectorBounds(position.x, position.x + it.size.width)
                        } else {
                            SelectorBounds(position.y, position.y + it.size.height)
                        }
                    }
                    .then(shareModifier)
                    .then(itemModifier),
            ) {
                itemContent(index, selected)
            }
        }
    }

    // The thumb is a sibling of the options rather than a fill on the chosen one, so it can travel
    // between them. The options report bounds relative to their line, and the line sits at the
    // box's padded origin, which is where the thumb is placed from.
    Box(modifier = selectorModifier) {
        if (style.thumbKind == SelectorThumbKind.Fill) {
            SelectorThumb(thumb = thumb, style = style, horizontal = horizontal)
        }
        if (horizontal) {
            Row(
                modifier = if (layout == SelectorLayout.Fill) Modifier.fillMaxWidth() else Modifier,
                horizontalArrangement = Arrangement.spacedBy(size.gap),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                items(if (layout == SelectorLayout.Fill) Modifier.weight(1f) else Modifier)
            }
        } else {
            Column(
                modifier = if (layout == SelectorLayout.Fill) Modifier.fillMaxHeight() else Modifier,
                verticalArrangement = Arrangement.spacedBy(size.gap),
                horizontalAlignment = Alignment.Start,
            ) {
                items(if (layout == SelectorLayout.Fill) Modifier.weight(1f) else Modifier)
            }
        }
        // Over the options, so it stays visible on top of an option's own fill.
        if (style.thumbKind == SelectorThumbKind.Underline) {
            SelectorThumb(thumb = thumb, style = style, horizontal = horizontal)
        }
    }
}

/**
 * Which way an arrow key moves focus through a selector, if at all.
 *
 * @param key Key pressed.
 * @param horizontal Whether the selector runs across rather than down.
 * @param layoutDirection Reading direction, which mirrors left and right.
 * @return `true` for the next option, `false` for the previous one, or `null` for a key that does
 *   not move along this selector.
 */
private fun arrowDirection(key: Key, horizontal: Boolean, layoutDirection: LayoutDirection): Boolean? {
    val rtl = layoutDirection == LayoutDirection.Rtl
    return when {
        horizontal && key == Key.DirectionRight -> !rtl
        horizontal && key == Key.DirectionLeft -> rtl
        !horizontal && key == Key.DirectionDown -> true
        !horizontal && key == Key.DirectionUp -> false
        else -> null
    }
}

/**
 * The thumb, drawn from [thumb]'s animated state.
 *
 * Reads that state only in layout and drawing, so a moving thumb relayouts and redraws itself alone
 * rather than recomposing the selector.
 *
 * @param thumb Where the thumb is, how present, and in what colour.
 * @param style Kind, shape, elevation and geometry of the thumb.
 * @param horizontal Whether the selector runs across rather than down.
 */
@Composable
private fun BoxScope.SelectorThumb(
    thumb: SelectorThumbState,
    style: SlidingSelectorStyle,
    horizontal: Boolean,
) {
    val shape = style.thumbShape
    val elevation = if (style.thumbKind == SelectorThumbKind.Fill) style.thumbElevation else ArecaTheme.elevation.none
    Box(
        modifier = Modifier
            .matchParentSize()
            .thumbLayout(
                bounds = thumb::bounds,
                horizontal = horizontal,
                kind = style.thumbKind,
                thickness = style.thumbThickness,
                length = style.thumbLength,
                padding = style.thumbPadding,
                edge = style.thumbEdge,
            )
            .graphicsLayer {
                alpha = thumb.alpha
                scaleX = thumb.scale
                scaleY = thumb.scale
                shadowElevation = elevation.toPx()
                this.shape = shape
                clip = false
            }
            .drawThumb(shape, thumb::color),
    )
}

/**
 * Fills the thumb's shape with its current colour.
 *
 * @param shape Shape of the thumb.
 * @param color Reads the colour, only while drawing.
 * @return Modifier drawing the thumb.
 */
private fun Modifier.drawThumb(shape: Shape, color: () -> Color): Modifier = drawBehind {
    drawOutline(shape.createOutline(size, layoutDirection, this), color())
}

/**
 * Sizes and places the thumb over the chosen option.
 *
 * A [fill][SelectorThumbKind.Fill] thumb spans the option's length and the selector's full depth; an
 * [underline][SelectorThumbKind.Underline] is a bar [thickness] deep along [edge]. Either is inset by
 * [padding]. Draws nothing while the thumb is gone.
 *
 * @param bounds Reads where the thumb currently sits along the selector, or `null` while it is gone.
 * @param horizontal Whether the selector runs across rather than down.
 * @param kind What the thumb is.
 * @param thickness Depth of an underline.
 * @param length Length of an underline centred on the option, or [Dp.Unspecified] for the option's.
 * @param padding Space between the option's bounds and the thumb.
 * @param edge Edge an underline runs along.
 * @return Modifier laying the thumb out.
 */
private fun Modifier.thumbLayout(
    bounds: () -> SelectorBounds?,
    horizontal: Boolean,
    kind: SelectorThumbKind,
    thickness: Dp,
    length: Dp,
    padding: PaddingValues,
    edge: SelectorEdge,
): Modifier = layout { measurable, constraints ->
    val current = bounds()
    if (current == null || kind == SelectorThumbKind.None) {
        return@layout layout(0, 0) {}
    }

    val left = padding.calculateLeftPadding(layoutDirection).toPx()
    val right = padding.calculateRightPadding(layoutDirection).toPx()
    val top = padding.calculateTopPadding().toPx()
    val bottom = padding.calculateBottomPadding().toPx()
    val mainBefore = if (horizontal) left else top
    val mainAfter = if (horizontal) right else bottom
    val crossBefore = if (horizontal) top else left
    val crossAfter = if (horizontal) bottom else right
    val crossExtent = (if (horizontal) constraints.maxHeight else constraints.maxWidth).toFloat()

    val (mainStart, mainEnd) = if (kind == SelectorThumbKind.Underline && length.isSpecified) {
        val centre = (current.start + current.end) / 2f
        val half = length.toPx() / 2f
        centre - half to centre + half
    } else {
        current.start + mainBefore to current.end - mainAfter
    }

    val (crossStart, crossEnd) = if (kind == SelectorThumbKind.Fill) {
        crossBefore to crossExtent - crossAfter
    } else {
        // The start edge of a vertical selector is its right edge in a right-to-left layout.
        val atLeftOrTop = if (horizontal) {
            edge == SelectorEdge.Start
        } else {
            (edge == SelectorEdge.Start) == (layoutDirection == LayoutDirection.Ltr)
        }
        val depth = thickness.toPx()
        if (atLeftOrTop) crossBefore to crossBefore + depth else crossExtent - crossAfter - depth to crossExtent - crossAfter
    }

    val mainSize = (mainEnd - mainStart).roundToInt().coerceAtLeast(0)
    val crossSize = (crossEnd - crossStart).roundToInt().coerceAtLeast(0)
    val placeable = measurable.measure(
        if (horizontal) Constraints.fixed(mainSize, crossSize) else Constraints.fixed(crossSize, mainSize),
    )
    layout(constraints.maxWidth, constraints.maxHeight) {
        // Bounds are measured from the left, so the thumb is placed from the left too, in either
        // reading direction.
        if (horizontal) {
            placeable.place(mainStart.roundToInt(), crossStart.roundToInt())
        } else {
            placeable.place(crossStart.roundToInt(), mainStart.roundToInt())
        }
    }
}

/**
 * Draws the line running the selector's length along its thumb edge, behind the options.
 *
 * @param style Colour, thickness and edge of the line.
 * @param horizontal Whether the selector runs across rather than down.
 * @return Modifier drawing the divider.
 */
private fun Modifier.drawDivider(style: SlidingSelectorStyle, horizontal: Boolean): Modifier = drawBehind {
    val atStartEdge = style.thumbEdge == SelectorEdge.Start
    // The start edge of a vertical selector is its right edge in a right-to-left layout.
    val atLeftOrTop = horizontal && atStartEdge ||
        !horizontal && atStartEdge == (layoutDirection == LayoutDirection.Ltr)
    val thickness = style.dividerThickness.toPx()
    val crossExtent = if (horizontal) size.height else size.width
    // Inside the bounds, its outer side on the edge, as the underline thumb sits.
    val fixed = if (atLeftOrTop) thickness / 2f else crossExtent - thickness / 2f
    val length = if (horizontal) size.width else size.height

    fun offsetAt(along: Float): Offset = if (horizontal) Offset(along, fixed) else Offset(fixed, along)

    drawLine(
        color = style.divider,
        start = offsetAt(0f),
        end = offsetAt(length),
        strokeWidth = thickness,
    )
}

/**
 * One option of a selector.
 *
 * Uses `selectable` for a radio button or a tab and `toggleable` for a switch, so the option's state
 * is announced as part of the enclosing `selectableGroup`.
 *
 * @param selected Whether it is the chosen option.
 * @param enabled Whether it can be chosen.
 * @param role How it is announced.
 * @param style Colours, shape and feedback.
 * @param size Metrics, also provided to its content.
 * @param contentSelected Content colour while it is chosen.
 * @param background Fill under its content.
 * @param contentDescription What a screen reader announces, or `null` for its content.
 * @param onTap Called when the user taps it.
 * @param onFocusChanged Called when it gains or loses focus.
 * @param horizontal Whether the selector runs across rather than down.
 * @param modifier Modifier applied to the option, carrying its bounds reporting.
 * @param content The option's content.
 */
@Composable
private fun SelectorOption(
    selected: Boolean,
    enabled: Boolean,
    role: SelectorRole,
    style: SlidingSelectorStyle,
    size: SlidingSelectorSize,
    contentSelected: Color,
    background: Color,
    contentDescription: String?,
    onTap: () -> Unit,
    onFocusChanged: (Boolean) -> Unit,
    horizontal: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val contentColor by animateColorAsState(
        targetValue = when {
            !enabled -> style.contentDisabled
            selected -> contentSelected
            else -> style.content
        },
        animationSpec = ArecaTheme.motion.fast.tween(),
        label = "selectorOptionContent",
    )
    val interactionSource = remember { MutableInteractionSource() }

    CompositionLocalProvider(
        LocalTextStyle provides size.textStyle,
        LocalContentColor provides contentColor,
        LocalIconSize provides size.iconSize,
    ) {
        Box(
            modifier = modifier
                .defaultMinSize(minWidth = size.itemMinWidth, minHeight = size.itemMinHeight)
                // Shaped fill rather than a clip, which would crop the focus ring the indication
                // draws outside the option.
                .background(background, style.itemShape)
                .onFocusChanged { onFocusChanged(it.isFocused) }
                .then(
                    if (role == SelectorRole.Switch) {
                        Modifier.toggleable(
                            value = selected,
                            enabled = enabled,
                            interactionSource = interactionSource,
                            indication = style.indication,
                            role = Role.Switch,
                            onValueChange = { onTap() },
                        )
                    } else {
                        Modifier.selectable(
                            selected = selected,
                            enabled = enabled,
                            interactionSource = interactionSource,
                            indication = style.indication,
                            role = if (role == SelectorRole.Tab) Role.Tab else Role.RadioButton,
                            onClick = onTap,
                        )
                    }
                )
                .then(
                    if (contentDescription != null) {
                        Modifier.semantics { this.contentDescription = contentDescription }
                    } else {
                        Modifier
                    }
                )
                .clickCursor(enabled)
                .padding(size.itemPadding),
            contentAlignment = if (horizontal) Alignment.Center else Alignment.CenterStart,
        ) {
            content()
        }
    }
}
