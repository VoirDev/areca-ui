package dev.voir.areca.foundation.slot

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/*
 * The slot package holds one layout primitive, not a screen scaffold.
 *
 * It is deliberately separate from `foundation.layout`, which holds the things a whole screen is
 * built out of. This is the cell a row is built out of, one level down — and the reason it is a
 * shared primitive at all is that a button, a badge, a top bar and a list row are the same three
 * boxes with different rules about how the width is split.
 */

/**
 * A row of three slots — leading, middle, trailing — arranged by a [SlotRowLayout].
 *
 * Nothing here is specific to any one component. A [dev.voir.areca.foundation.button.Button] is
 * this with its slots wrapped tight, a [dev.voir.areca.foundation.list.ListItem] is this with a
 * weighted middle, and a [dev.voir.areca.foundation.topbar.TopBar] is this with a middle that holds
 * the row's centre. Keeping the arrangement in one place is what makes an icon sit the same
 * distance from a label in all of them.
 *
 * Under [SlotRowArrangement.Sequential], how much width a slot claims is decided by its [SlotSize]
 * rule alone, never by whether it has content. That is what lets a column of [SlotSize.Fixed]
 * leading slots line up when some rows have an icon and some do not. A [SlotSize.Wrap] slot with no
 * content is zero-wide anyway, so the rule costs nothing in the common case.
 *
 * Under [SlotRowArrangement.Centered] the middle slot is placed on the row's centre line instead,
 * and the side slots keep their content's width rather than being cut to a share of the row. Reach
 * for it whenever the middle is a title between two controls.
 *
 * The *gap* beside an absent slot is dropped, because a gap separates two things and there is only
 * one — except beside a [SlotSize.Fixed] slot, which keeps both its width and its gap so the
 * middle slots of a column of rows start at the same x whether or not each row fills it.
 *
 * With a [SlotRowLayout.alignment], the side slots line up with a middle slot of text the way the
 * scenario asks — centred for a row of one or two lines, from the top for a longer one, on the first
 * line for a marker — reading the first line the middle slot reports through [firstLineCenter].
 *
 * The middle slot is required. A row with nothing in the middle is two boxes and a gap, which is a
 * `Row`.
 *
 * @param modifier Modifier applied to the row.
 * @param layout How the width is divided and how the slots align.
 * @param start Leading content, normally an icon or an avatar.
 * @param center The row's subject.
 * @param end Trailing content, normally a control or a chevron.
 */
@Composable
public fun SlotRow(
    modifier: Modifier = Modifier,
    layout: SlotRowLayout = SlotRowLayout.Default,
    start: (@Composable BoxScope.() -> Unit)? = null,
    center: @Composable BoxScope.() -> Unit,
    end: (@Composable BoxScope.() -> Unit)? = null,
) {
    when (layout.arrangement) {
        // Aligning against the text needs the middle slot's height before the sides are placed,
        // which a Row cannot give, so it has a layout of its own.
        SlotRowArrangement.Sequential -> if (layout.alignment != null) {
            AlignedSlotRow(
                modifier = modifier,
                layout = layout,
                alignment = layout.alignment,
                start = start,
                center = center,
                end = end,
            )
        } else {
            SequentialSlotRow(
                modifier = modifier,
                layout = layout,
                start = start,
                center = center,
                end = end,
            )
        }

        // The centred arrangement carries its own minimum height, because it measures the row
        // rather than delegating to a Row.
        SlotRowArrangement.Centered -> CenteredSlotRow(
            modifier = modifier,
            layout = layout,
            start = start,
            center = center,
            end = end,
        )
    }
}

/**
 * A [SlotRow] laid out by [SlotRowArrangement.Sequential]: a `Row` of three width rules.
 *
 * @param modifier Modifier applied to the row.
 * @param layout How the width is divided and how the slots align.
 * @param start Leading content, or `null` for a row without one.
 * @param center The row's subject.
 * @param end Trailing content, or `null` for a row without one.
 */
@Composable
private fun SequentialSlotRow(
    modifier: Modifier,
    layout: SlotRowLayout,
    start: (@Composable BoxScope.() -> Unit)?,
    center: @Composable BoxScope.() -> Unit,
    end: (@Composable BoxScope.() -> Unit)?,
) {
    Row(
        modifier = modifier.then(
            if (layout.minHeight != Dp.Unspecified) {
                Modifier.heightIn(min = layout.minHeight)
            } else {
                Modifier
            }
        ),
        verticalAlignment = layout.verticalAlignment,
    ) {
        SlotBox(size = layout.start, alignment = layout.startAlignment) { start?.invoke(this) }

        if (drawsSideGap(layout.start, hasContent = start != null) && layout.resolvedStartGap > 0.dp) {
            Spacer(modifier = Modifier.width(layout.resolvedStartGap))
        }

        SlotBox(size = layout.center, alignment = layout.centerAlignment, content = center)

        if (drawsSideGap(layout.end, hasContent = end != null) && layout.resolvedEndGap > 0.dp) {
            Spacer(modifier = Modifier.width(layout.resolvedEndGap))
        }

        SlotBox(size = layout.end, alignment = layout.endAlignment) { end?.invoke(this) }
    }
}

/**
 * One slot, sized by its [SlotSize] rule and aligning its content within itself.
 *
 * @param size How the slot claims width.
 * @param alignment Where the content sits inside the slot.
 * @param content The slot's content.
 */
@Composable
private fun RowScope.SlotBox(
    size: SlotSize,
    alignment: Alignment,
    content: @Composable BoxScope.() -> Unit,
) {
    val sizeModifier = when (size) {
        SlotSize.Wrap -> Modifier
        is SlotSize.Fixed -> Modifier.width(size.width)
        is SlotSize.Weight -> Modifier.weight(size.weight, fill = size.fill)
    }

    Box(
        modifier = sizeModifier,
        contentAlignment = alignment,
        content = content,
    )
}
