package dev.voir.areca.foundation.slot

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.isSpecified

/**
 * Where the middle slot of a [SlotRowArrangement.Centered] row ends up.
 *
 * @property width Width the middle slot is given.
 * @property x Offset of the middle slot from the row's leading edge.
 */
internal data class CenterSlotPlacement(
    val width: Int,
    val x: Int,
)

/**
 * Decides whether the middle slot can keep the row's centre line, and where it goes either way.
 *
 * Centring reserves the wider of the two sides on *both* sides of the middle. That is what makes
 * the band symmetric about the row's centre without touching either side's own width — the
 * alternative, splitting the row into equal shares, is what squeezes a trailing control down to a
 * third of the row and clips its label on a narrow screen.
 *
 * When the middle needs more than the symmetric band leaves, centring is abandoned rather than
 * paid for by the sides: the middle takes everything between them and truncates there.
 *
 * @param rowWidth Width the row has to divide.
 * @param leadWidth Leading slot's width plus the gap after it, or `0` with no leading slot.
 * @param trailWidth Trailing slot's width plus the gap before it, or `0` with no trailing slot.
 * @param desiredWidth Width the middle slot's content asks for, already capped at the space
 *   between the two sides.
 * @return Width and offset for the middle slot. Never negative, even when the sides alone overflow
 *   the row.
 */
internal fun centerSlotPlacement(
    rowWidth: Int,
    leadWidth: Int,
    trailWidth: Int,
    desiredWidth: Int,
): CenterSlotPlacement {
    val betweenSides = (rowWidth - leadWidth - trailWidth).coerceAtLeast(0)
    val reserved = maxOf(leadWidth, trailWidth)
    val centeredWidth = rowWidth - reserved * 2

    return if (centeredWidth > 0 && centeredWidth >= desiredWidth) {
        CenterSlotPlacement(width = centeredWidth, x = reserved)
    } else {
        CenterSlotPlacement(width = betweenSides, x = leadWidth)
    }
}

/**
 * A [SlotRow] laid out by [SlotRowArrangement.Centered].
 *
 * A custom layout rather than a `Row`, because the middle slot's width has to depend on the
 * trailing slot's width — which a `Row` cannot express, since its weights are resolved from the
 * space left over rather than from the shape of what sits beside them.
 *
 * @param modifier Modifier applied to the row.
 * @param layout Slot rules, gaps and alignments to lay out by.
 * @param start Leading content, or `null` for a row without one.
 * @param center The row's subject.
 * @param end Trailing content, or `null` for a row without one.
 */
@Composable
internal fun CenteredSlotRow(
    modifier: Modifier,
    layout: SlotRowLayout,
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
        val startMeasurable = measurables[0]
        val centerMeasurable = measurables[1]
        val endMeasurable = measurables[2]

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

        val startPlaceable = startMeasurable.measureSide(
            width = fixedWidthPx(layout.start),
            available = constraints.maxWidth,
            maxHeight = constraints.maxHeight,
        )
        val endPlaceable = endMeasurable.measureSide(
            width = fixedWidthPx(layout.end),
            available = constraints.remainingWidth(startPlaceable.width),
            maxHeight = constraints.maxHeight,
        )

        val leadWidth = startPlaceable.width + startGap
        val trailWidth = endPlaceable.width + endGap
        val centerIntrinsicWidth = centerMeasurable.maxIntrinsicWidth(constraints.maxHeight)

        // An unbounded row has no centre to sit on, so it becomes as wide as its content and the
        // placement below degenerates into laying the three slots out in order.
        val rowWidth = if (constraints.hasBoundedWidth) {
            constraints.maxWidth
        } else {
            leadWidth + trailWidth + centerIntrinsicWidth
        }
        val betweenSides = (rowWidth - leadWidth - trailWidth).coerceAtLeast(0)

        val placement = centerSlotPlacement(
            rowWidth = rowWidth,
            leadWidth = leadWidth,
            trailWidth = trailWidth,
            desiredWidth = centerIntrinsicWidth.coerceIn(0, betweenSides),
        )
        val centerPlaceable = centerMeasurable.measure(
            Constraints(
                minWidth = placement.width,
                maxWidth = placement.width,
                minHeight = 0,
                maxHeight = constraints.maxHeight,
            )
        )

        val minHeight = if (layout.minHeight.isSpecified) layout.minHeight.roundToPx() else 0
        val rowHeight = maxOf(
            startPlaceable.height,
            centerPlaceable.height,
            endPlaceable.height,
            minHeight,
        ).coerceIn(constraints.minHeight, constraints.maxHeight)

        layout(rowWidth.coerceAtLeast(constraints.minWidth), rowHeight) {
            // placeRelative, so the leading slot sits on the right in a right-to-left layout and
            // the centred band stays symmetric either way.
            startPlaceable.placeRelative(
                x = 0,
                y = layout.verticalAlignment.align(startPlaceable.height, rowHeight),
            )
            centerPlaceable.placeRelative(
                x = placement.x,
                y = layout.verticalAlignment.align(centerPlaceable.height, rowHeight),
            )
            endPlaceable.placeRelative(
                x = rowWidth - endPlaceable.width,
                y = layout.verticalAlignment.align(endPlaceable.height, rowHeight),
            )
        }
    }
}

/**
 * Measures a side slot at the width its rule asks for.
 *
 * @param width Exact width the slot must take, or `null` to let it wrap its content.
 * @param available Largest width the slot may take.
 * @param maxHeight Largest height the slot may take.
 * @return The measured slot.
 */
private fun Measurable.measureSide(
    width: Int?,
    available: Int,
    maxHeight: Int,
): Placeable = measure(
    Constraints(
        minWidth = width ?: 0,
        maxWidth = width ?: available,
        minHeight = 0,
        maxHeight = maxHeight,
    )
)

/**
 * Exact width a side slot's rule demands, in pixels.
 *
 * [SlotSize.Weight] cannot mean anything without a leftover to share, so it wraps like
 * [SlotSize.Wrap] — see [SlotRowArrangement.Centered].
 *
 * @param size Width rule of the slot.
 * @return Width to pin the slot to, or `null` when the slot wraps its content.
 */
private fun Density.fixedWidthPx(size: SlotSize): Int? =
    (size as? SlotSize.Fixed)?.width?.roundToPx()

/**
 * Width still available for a slot once an earlier one has taken its share.
 *
 * @param used Width already claimed.
 * @return Remaining width, or [Constraints.Infinity] when the row is unbounded.
 */
private fun Constraints.remainingWidth(used: Int): Int =
    if (hasBoundedWidth) (maxWidth - used).coerceAtLeast(0) else maxWidth
