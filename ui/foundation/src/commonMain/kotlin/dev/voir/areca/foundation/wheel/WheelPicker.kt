package dev.voir.areca.foundation.wheel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * A row of wheels sharing one centre slot.
 *
 * The picker owns everything the wheels have in common: their height, the highlight marking the slot
 * they select into, and the fade that makes the top and bottom rows read as curving away rather than
 * as a list that has been cut off. Those two are drawn by default, because every wheel picker ever
 * built needed both and leaving them to callers is how two pickers in one product end up with
 * differently sized pills.
 *
 * Columns take their metrics and colours from the picker through composition locals, so a picker's
 * wheels line up without every call site repeating the same arguments.
 *
 * ```kotlin
 * WheelPicker(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
 *     WheelPickerColumn(
 *         items = hours,
 *         state = hourState,
 *         modifier = Modifier.weight(1f),
 *         contentDescription = "Hour",
 *     )
 *     WheelPickerColumn(
 *         items = minutes,
 *         state = minuteState,
 *         modifier = Modifier.weight(1f),
 *         contentDescription = "Minute",
 *     )
 * }
 * ```
 *
 * A column's rows fill its width, so every column needs a width of its own — a `weight`, as here, or
 * a fixed `width`. Left without one, the first column takes the whole row.
 *
 * @param modifier Modifier applied to the picker. Its height is fixed by [size].
 * @param style Colours and falloff, shared by every column.
 * @param size Row height and visible row count, shared by every column.
 * @param scroll How hard the wheels answer scrolling, shared by every column. `null`, the default,
 *   lets each column choose from its own length — which is what a picker mixing a wheel of twelve
 *   with a wheel of two hundred wants, since one pace cannot suit both.
 * @param horizontalArrangement How the columns are spread across the picker's width.
 * @param backgroundDecoration Drawn behind the columns, filling the picker. Defaults to the
 *   highlight behind the centre row. Pass `{}` for none.
 * @param foregroundDecoration Drawn over the columns, filling the picker. Defaults to the edge fade.
 *   Anything drawn here should stay non-interactive, or it will take the scrolling meant for the
 *   wheels underneath. Pass `{}` for none.
 * @param columns The wheels, in display order.
 */
@Composable
public fun WheelPicker(
    modifier: Modifier = Modifier,
    style: WheelPickerStyle = LocalWheelPickerStyle.current ?: WheelPickerStyle.Default,
    size: WheelPickerSize = LocalWheelPickerSize.current ?: WheelPickerSize.Default,
    scroll: WheelPickerScroll? = LocalWheelPickerScroll.current,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    backgroundDecoration: @Composable BoxScope.() -> Unit = { WheelPickerHighlight() },
    foregroundDecoration: @Composable BoxScope.() -> Unit = { WheelPickerEdgeFade() },
    columns: @Composable RowScope.() -> Unit,
) {
    CompositionLocalProvider(
        LocalWheelPickerSize provides size,
        LocalWheelPickerStyle provides style,
        LocalWheelPickerScroll provides scroll,
    ) {
        Box(modifier = modifier.height(size.height)) {
            backgroundDecoration()
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = horizontalArrangement,
                content = columns,
            )
            foregroundDecoration()
        }
    }
}

/**
 * The fill marking the slot a picker selects into.
 *
 * Drawn behind the columns rather than over them, which is the only order that works: an opaque fill
 * over the wheels hides the very row it is meant to pick out.
 *
 * @param style Style the fill and its shape are taken from. [Color.Unspecified] draws nothing.
 * @param size Metrics the band height is taken from.
 */
@Composable
public fun BoxScope.WheelPickerHighlight(
    style: WheelPickerStyle = LocalWheelPickerStyle.current ?: WheelPickerStyle.Default,
    size: WheelPickerSize = LocalWheelPickerSize.current ?: WheelPickerSize.Default,
) {
    if (style.highlight == Color.Unspecified) return
    Box(
        modifier = Modifier
            .align(Alignment.Center)
            .fillMaxWidth()
            .height(size.itemHeight)
            .background(style.highlight, style.highlightShape),
    )
}

/**
 * The fade that turns the top and bottom rows into the far side of a drum.
 *
 * Drawn over the columns as two gradients rather than as a mask on them, so it costs two fills
 * instead of a layer per row. Each band is as tall as the rows it covers, which is what makes the
 * wheel read as curving away.
 *
 * @param style Style the fade colour is taken from. [Color.Unspecified] draws nothing, which is what
 *   a picker over patterned or translucent backing wants.
 * @param size Metrics the band height is taken from.
 */
@Composable
public fun BoxScope.WheelPickerEdgeFade(
    style: WheelPickerStyle = LocalWheelPickerStyle.current ?: WheelPickerStyle.Default,
    size: WheelPickerSize = LocalWheelPickerSize.current ?: WheelPickerSize.Default,
) {
    if (style.surface == Color.Unspecified || size.sideItemsCount == 0) return
    val bandHeight = size.itemHeight * size.sideItemsCount
    Box(
        modifier = Modifier
            .align(Alignment.TopCenter)
            .fillMaxWidth()
            .height(bandHeight)
            .background(Brush.verticalGradient(listOf(style.surface, Color.Transparent))),
    )
    Box(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .height(bandHeight)
            .background(Brush.verticalGradient(listOf(Color.Transparent, style.surface))),
    )
}
