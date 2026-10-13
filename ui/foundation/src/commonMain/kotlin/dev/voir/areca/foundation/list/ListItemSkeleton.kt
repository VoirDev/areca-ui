package dev.voir.areca.foundation.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.feedback.Skeleton
import dev.voir.areca.foundation.feedback.SkeletonSize

/**
 * Placeholder shaped like the row that is loading.
 *
 * It borrows the real row's [size] tokens, so a loading list has the same height and rhythm as the
 * list that replaces it and nothing jumps when the data lands — the bars even take their height from
 * the text styles the real row would use. Widths are shares of the row rather than fixed values,
 * which keeps the placeholder plausible at any column width.
 *
 * Rows in a placeholder list should differ slightly from each other; identical bars read as a broken
 * layout rather than as loading. Vary [titleWidthFraction] per row.
 *
 * @param modifier Modifier applied to the row container.
 * @param size Metrics the placeholder takes its height, padding and text sizes from.
 * @param showLeading Whether to draw a leading placeholder — use it when the real rows carry an
 * avatar or an icon.
 * @param showSubtitle Whether to draw a second, shorter bar for the subtitle line.
 * @param showTrailing Whether to draw a trailing placeholder.
 * @param valueWidth Width of a bar in the value slot, as tall as the value's text — use it when the
 * real rows state a figure before their trailing slot. `null` draws none.
 * @param leadingSize Width and height of the leading placeholder.
 * @param leadingShape Shape of the leading placeholder; circular for an avatar, rounded for a
 * thumbnail.
 * @param titleWidthFraction Share of the text block width the title bar takes.
 * @param subtitleWidthFraction Share of the text block width the subtitle bar takes.
 * @param style Colors of the row, so the placeholder sits on the same surface as the real rows.
 */
@Composable
public fun ListItemSkeleton(
    modifier: Modifier = Modifier,
    size: ListItemSize = LocalListItemSize.current ?: ListItemSize.Default,
    showLeading: Boolean = false,
    showSubtitle: Boolean = true,
    showTrailing: Boolean = false,
    leadingSize: Dp = size.icon + 12.dp,
    leadingShape: Shape = CircleShape,
    titleWidthFraction: Float = 0.45f,
    subtitleWidthFraction: Float = 0.7f,
    valueWidth: Dp? = null,
    style: ListItemStyle = LocalListItemStyle.current ?: ListItemStyle.Default,
) {
    // No click handler: a placeholder is not a target, so it never reports a hover or a press.
    ListItem(
        modifier = modifier,
        size = size,
        style = style,
        leading = if (showLeading) {
            {
                Skeleton(
                    width = leadingSize,
                    height = leadingSize,
                    size = SkeletonSize.Default.copy(shape = leadingShape),
                )
            }
        } else {
            null
        },
        value = valueWidth?.let { width ->
            { Skeleton(width = width, textStyle = size.valueTextStyle) }
        },
        trailing = if (showTrailing) {
            { Skeleton(width = size.icon, height = size.icon) }
        } else {
            null
        },
        content = {
            Column(verticalArrangement = Arrangement.spacedBy(size.textGap)) {
                // Sized by the text styles the real row would use, which is what keeps the
                // placeholder the same height as the row.
                Skeleton(
                    modifier = Modifier.fillMaxWidth(titleWidthFraction),
                    textStyle = size.titleTextStyle,
                )

                if (showSubtitle) {
                    Skeleton(
                        modifier = Modifier.fillMaxWidth(subtitleWidthFraction),
                        textStyle = size.subtitleTextStyle,
                    )
                }
            }
        },
    )
}
