package dev.voir.areca.foundation.slot

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.HorizontalAlignmentLine
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.isSpecified
import kotlin.math.min

/**
 * The centre of the first line of a row's text, measured from the top of the layout reporting it.
 *
 * A [SlotRow] aligned by a [SlotRowAlignment] reads it to find the row's first line:
 * [Top][SlotRowAlignment.Top] and [FirstLine][SlotRowAlignment.FirstLine] place against it, and [Auto][SlotRowAlignment.Auto] uses
 * it to tell two lines from more. Report it with [firstLineCenter] on whatever draws the first line;
 * when several layouts report it, the topmost wins.
 */
public val FirstLineCenter: HorizontalAlignmentLine = HorizontalAlignmentLine(::min)

/**
 * The top of the first line of a row's text, reported beside [FirstLineCenter].
 *
 * The two together give the first line's height wherever the line sits in the middle slot — under
 * an overline, say — which is how [SlotRowAlignment.Auto] measures the middle slot in lines.
 */
internal val FirstLineTop: HorizontalAlignmentLine = HorizontalAlignmentLine(::min)

/**
 * Reports [FirstLineCenter] for this layout, taking its first line to be one line of [textStyle].
 *
 * Put it on the title of a row of your own — a [SlotRow] whose middle slot is a name over a
 * caption, say — so the row aligns the way a list row does. A layout shorter than one line, or a
 * style with no resolvable line height, reports its own centre.
 *
 * @param textStyle Style the first line is set in; its line height decides where the line's centre
 * falls.
 * @return Modifier reporting the first line.
 */
public fun Modifier.firstLineCenter(textStyle: TextStyle): Modifier = layout { measurable, constraints ->
    val placeable = measurable.measure(constraints)
    val lineHeight = textStyle.lineHeight
    val linePx = when {
        lineHeight.isSp -> lineHeight.roundToPx()
        lineHeight.isEm && textStyle.fontSize.isSp ->
            (lineHeight.value * textStyle.fontSize.toPx()).toInt()
        else -> placeable.height
    }
    val center = min(linePx, placeable.height) / 2

    layout(
        width = placeable.width,
        height = placeable.height,
        alignmentLines = mapOf(FirstLineCenter to center, FirstLineTop to 0),
    ) {
        placeable.place(0, 0)
    }
}

/**
 * Where the middle slot's first line is, as the row measured it.
 *
 * @property center Centre of the first line, from the middle slot's top.
 * @property height Height of the first line.
 */
internal data class FirstLine(
    val center: Int,
    val height: Int,
)

/**
 * Vertical placement of the three slots of an aligned row.
 *
 * @property startY Offset of the leading slot from the row's top.
 * @property centerY Offset of the middle slot from the row's top.
 * @property endY Offset of the trailing slot from the row's top.
 * @property height Height of the row.
 */
internal data class AlignedPlacement(
    val startY: Int,
    val centerY: Int,
    val endY: Int,
    val height: Int,
)

/** How many first lines tall a middle slot may be and still count as two lines of text. */
private const val TWO_LINES = 2.5f

/**
 * The alignment [alignment] comes to for a middle slot [centerHeight] tall.
 *
 * [SlotRowAlignment.Auto] measures the middle slot in first lines: a title and one line under it
 * come to a little over two, and anything that wraps or adds a third line passes two and a half.
 *
 * @param alignment Alignment the row asked for.
 * @param centerHeight Height of the middle slot.
 * @param firstLine The middle slot's first line, or `null` when it reports none.
 * @return [alignment], with [SlotRowAlignment.Auto] resolved to what it means here.
 */
internal fun resolveAlignment(
    alignment: SlotRowAlignment,
    centerHeight: Int,
    firstLine: FirstLine?,
): SlotRowAlignment {
    if (alignment != SlotRowAlignment.Auto) return alignment
    if (firstLine == null || firstLine.height <= 0) return SlotRowAlignment.Center
    return if (centerHeight <= firstLine.height * TWO_LINES) {
        SlotRowAlignment.Center
    } else {
        SlotRowAlignment.Top
    }
}

/**
 * Lines the three slots of a row up according to [alignment].
 *
 * Offsets are worked out against the middle slot's top, then the whole group is shifted so nothing
 * sits above the row's top and centred within [minHeight] when it is shorter.
 *
 * @param alignment How to align; [SlotRowAlignment.Auto] is resolved first.
 * @param startHeight Height of the leading slot; `0` without one.
 * @param centerHeight Height of the middle slot.
 * @param firstLine The middle slot's first line, or `null` when it reports none.
 * @param endHeight Height of the trailing slot; `0` without one.
 * @param minHeight Smallest height the row takes.
 * @return Where each slot goes, and how tall the row is.
 */
internal fun alignedPlacement(
    alignment: SlotRowAlignment,
    startHeight: Int,
    centerHeight: Int,
    firstLine: FirstLine?,
    endHeight: Int,
    minHeight: Int,
): AlignedPlacement {
    // A middle slot that reports no line is one block, centred on as a whole.
    val line = firstLine ?: FirstLine(center = centerHeight / 2, height = centerHeight)

    val startTop: Int
    val endTop: Int
    when (resolveAlignment(alignment, centerHeight, firstLine)) {
        SlotRowAlignment.Center, SlotRowAlignment.Auto -> {
            val middle = maxOf(startHeight, centerHeight, endHeight) / 2
            // Against a centre line shared by all three, expressed from the middle slot's top.
            val centerTop = middle - centerHeight / 2
            startTop = middle - startHeight / 2 - centerTop
            endTop = middle - endHeight / 2 - centerTop
        }

        SlotRowAlignment.FirstLine -> {
            // A side as tall as the whole middle slot centres on it instead: a large visual pinned
            // to the first line leaves the text hanging below it.
            fun axis(sideHeight: Int): Int =
                if (sideHeight >= centerHeight) centerHeight / 2 else line.center
            startTop = axis(startHeight) - startHeight / 2
            endTop = axis(endHeight) - endHeight / 2
        }

        SlotRowAlignment.Top -> {
            // A visual shorter than the first line sits on it; a taller one starts with the text.
            startTop = if (startHeight in 1..line.height) line.center - startHeight / 2 else 0
            // The trailing slot keeps level with the visual, so a checkbox or an amount reads
            // with the icon it belongs to; with no visual, with the first line.
            val axis = if (startHeight > 0) startTop + startHeight / 2 else line.center
            endTop = axis - endHeight / 2
        }
    }

    val top = minOf(0, startTop, endTop)
    val bottom = maxOf(centerHeight, startTop + startHeight, endTop + endHeight)
    val contentHeight = bottom - top
    val height = maxOf(contentHeight, minHeight)
    val shift = (height - contentHeight) / 2 - top

    return AlignedPlacement(
        startY = startTop + shift,
        centerY = shift,
        endY = endTop + shift,
        height = height,
    )
}

/**
 * A [SlotRow] laid out by [SlotRowArrangement.Sequential] with a [SlotRowLayout.alignment].
 *
 * A custom layout rather than a `Row`, because where a side slot goes depends on how tall the middle
 * slot turned out and where its first line falls — and a `Row` aligns each child on its own. The
 * widths follow the same rules as the `Row` does: fixed and wrapping slots are measured first, in
 * order, and weighted slots share what is left.
 *
 * @param modifier Modifier applied to the row.
 * @param layout Slot rules, gaps and alignments to lay out by.
 * @param alignment How the slots line up vertically.
 * @param start Leading content, or `null` for a row without one.
 * @param center The row's subject.
 * @param end Trailing content, or `null` for a row without one.
 */
@Composable
internal fun AlignedSlotRow(
    modifier: Modifier,
    layout: SlotRowLayout,
    alignment: SlotRowAlignment,
    start: (@Composable BoxScope.() -> Unit)?,
    center: @Composable BoxScope.() -> Unit,
    end: (@Composable BoxScope.() -> Unit)?,
) {
    Layout(
        modifier = modifier,
        content = {
            Box(contentAlignment = layout.startAlignment) { start?.invoke(this) }
            Box(contentAlignment = layout.centerAlignment, content = center)
            Box(contentAlignment = layout.endAlignment) { end?.invoke(this) }
        },
    ) { measurables, constraints ->
        val startGap = if (drawsSideGap(layout.start, hasContent = start != null)) {
            layout.resolvedStartGap.roundToPx()
        } else {
            0
        }
        val endGap = if (drawsSideGap(layout.end, hasContent = end != null)) {
            layout.resolvedEndGap.roundToPx()
        } else {
            0
        }

        val sizes = listOf(layout.start, layout.center, layout.end)
        val placeables = arrayOfNulls<Placeable>(3)
        val bounded = constraints.hasBoundedWidth
        var remaining = if (bounded) (constraints.maxWidth - startGap - endGap).coerceAtLeast(0) else 0

        // Fixed and wrapping slots first, in order, exactly as a Row measures its unweighted children.
        sizes.forEachIndexed { index, size ->
            if (size is SlotSize.Weight && bounded) return@forEachIndexed
            val fixed = (size as? SlotSize.Fixed)?.width?.roundToPx()
            val placeable = measurables[index].measureSlot(
                minWidth = fixed ?: 0,
                maxWidth = fixed ?: if (bounded) remaining else Constraints.Infinity,
                maxHeight = constraints.maxHeight,
            )
            placeables[index] = placeable
            if (bounded) remaining = (remaining - placeable.width).coerceAtLeast(0)
        }

        // Weighted slots share the leftover; the last one takes the rounding remainder.
        if (bounded) {
            val weighted = sizes.withIndex().filter { it.value is SlotSize.Weight }
            val totalWeight = weighted.sumOf { (it.value as SlotSize.Weight).weight.toDouble() }
            var left = remaining
            weighted.forEachIndexed { order, (index, size) ->
                size as SlotSize.Weight
                val share = if (order == weighted.lastIndex) {
                    left
                } else {
                    (remaining * (size.weight / totalWeight)).toInt().coerceAtMost(left)
                }
                left -= share
                placeables[index] = measurables[index].measureSlot(
                    minWidth = if (size.fill) share else 0,
                    maxWidth = share,
                    maxHeight = constraints.maxHeight,
                )
            }
        }

        val startPlaceable = placeables[0]!!
        val centerPlaceable = placeables[1]!!
        val endPlaceable = placeables[2]!!

        val contentWidth = startPlaceable.width + startGap + centerPlaceable.width + endGap + endPlaceable.width
        val hasWeight = sizes.any { it is SlotSize.Weight }
        val rowWidth = (if (bounded && hasWeight) constraints.maxWidth else contentWidth)
            .coerceIn(constraints.minWidth, constraints.maxWidth)

        val lineCenter = centerPlaceable[FirstLineCenter]
        val lineTop = centerPlaceable[FirstLineTop]
        val firstLine = if (lineCenter == AlignmentLine.Unspecified) {
            null
        } else {
            val top = if (lineTop == AlignmentLine.Unspecified) 0 else lineTop
            FirstLine(center = lineCenter, height = (lineCenter - top) * 2)
        }
        val layoutMinHeight = if (layout.minHeight.isSpecified) layout.minHeight.roundToPx() else 0
        val placement = alignedPlacement(
            alignment = alignment,
            startHeight = startPlaceable.height,
            centerHeight = centerPlaceable.height,
            firstLine = firstLine,
            endHeight = endPlaceable.height,
            minHeight = maxOf(layoutMinHeight, constraints.minHeight),
        )
        val rowHeight = placement.height.coerceAtMost(constraints.maxHeight)

        layout(rowWidth, rowHeight) {
            // One after another, as the Row places them: a weighted middle that does not fill its
            // share leaves the leftover after the trailing slot, not before it. placeRelative, so
            // the leading slot sits on the right in a right-to-left layout.
            val centerX = startPlaceable.width + startGap
            startPlaceable.placeRelative(x = 0, y = placement.startY)
            centerPlaceable.placeRelative(x = centerX, y = placement.centerY)
            endPlaceable.placeRelative(x = centerX + centerPlaceable.width + endGap, y = placement.endY)
        }
    }
}

/**
 * Measures one slot between [minWidth] and [maxWidth], at any height up to [maxHeight].
 *
 * @param minWidth Smallest width the slot takes.
 * @param maxWidth Largest width the slot takes.
 * @param maxHeight Largest height the slot takes.
 * @return The measured slot.
 */
private fun Measurable.measureSlot(minWidth: Int, maxWidth: Int, maxHeight: Int): Placeable = measure(
    Constraints(minWidth = minWidth, maxWidth = maxWidth, minHeight = 0, maxHeight = maxHeight)
)
