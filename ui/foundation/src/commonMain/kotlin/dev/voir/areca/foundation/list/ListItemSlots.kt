package dev.voir.areca.foundation.list

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.lerp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.LocalContentColor
import dev.voir.areca.foundation.icon.Chevron
import dev.voir.areca.foundation.icon.ChevronDirection
import dev.voir.areca.foundation.icon.ChevronSize
import dev.voir.areca.theme.tween

/**
 * Trailing chevron of a row that opens something else.
 *
 * It leans toward the pointer while its row is reached for and sits still otherwise, so the whole
 * row answers a hover rather than only its background. Color, size and timing come from the row it
 * sits in — its own `size` when it was given one — which means the chevron brightens with the rest
 * of the trailing content, fades when the row is disabled, and moves at the row's pace.
 *
 * [ChevronDirection.Right] and [ChevronDirection.Left] are reading-order directions: the lean is a
 * layout-relative offset, so it follows the chevron when a right-to-left layout mirrors it.
 *
 * @param modifier Modifier applied to the chevron.
 * @param direction Direction the chevron points toward.
 * @param size Size and stroke tokens of the chevron; defaults to the row's.
 * @param hoverOffset Distance the chevron travels while its row is reached for.
 */
@Composable
public fun ListItemChevron(
    modifier: Modifier = Modifier,
    direction: ChevronDirection = ChevronDirection.Right,
    size: ChevronSize = currentListItemSize().chevron,
    hoverOffset: Dp = 2.dp,
) {
    val state = LocalListItemState.current
    val animation = currentListItemSize().stateAnimation

    val travel = if (state.isActive) hoverOffset else 0.dp
    // The lean follows where the chevron points, so it reads as the row's destination.
    val horizontal by animateDpAsState(
        targetValue = when (direction) {
            ChevronDirection.Right -> travel
            ChevronDirection.Left -> -travel
            ChevronDirection.Up, ChevronDirection.Down -> 0.dp
        },
        animationSpec = animation.release.tween(animation.easing),
        label = "listItemChevronOffsetX",
    )
    val vertical by animateDpAsState(
        targetValue = when (direction) {
            ChevronDirection.Down -> travel
            ChevronDirection.Up -> -travel
            ChevronDirection.Left, ChevronDirection.Right -> 0.dp
        },
        animationSpec = animation.release.tween(animation.easing),
        label = "listItemChevronOffsetY",
    )

    Chevron(
        direction = direction,
        size = size,
        modifier = modifier.offset(x = horizontal, y = vertical),
    )
}

/**
 * Check mark that draws itself in when its row becomes selected.
 *
 * The stroke is drawn rather than faded in from an icon so the mark arrives the way it would be
 * written — the short leg first, then the long one. It keeps its square footprint whether or not it
 * is showing, so the rows of a list stay aligned as the selection moves between them. Its size and
 * timing come from the row it sits in — its own `size` when it was given one.
 *
 * @param modifier Modifier applied to the check mark.
 * @param selected Whether the mark is showing; defaults to the selection of the enclosing row.
 * @param size Width and height of the mark; defaults to the row's icon size.
 * @param color Stroke color; defaults to the row's trailing slot color.
 * @param strokeWidth Width of the stroke.
 */
@Composable
public fun ListItemCheckmark(
    modifier: Modifier = Modifier,
    selected: Boolean = LocalListItemState.current.selected,
    size: Dp = currentListItemSize().icon,
    color: Color = LocalContentColor.current,
    strokeWidth: Dp = 2.dp,
) {
    val animation = currentListItemSize().stateAnimation
    val progress by animateFloatAsState(
        targetValue = if (selected) 1f else 0f,
        animationSpec = animation.selection.tween(animation.easing),
        label = "listItemCheckmark",
    )

    Canvas(
        modifier = modifier
            .size(size)
            // Opaque after the first sliver of the stroke, so the mark reads as being written
            // rather than as fading in; the fade only covers the moment it appears and leaves.
            .alpha((progress * 4f).coerceIn(0f, 1f)),
    ) {
        if (progress <= 0.001f) return@Canvas

        val stroke = strokeWidth.toPx()
        val start = Offset(this.size.width * 0.20f, this.size.height * 0.52f)
        val corner = Offset(this.size.width * 0.42f, this.size.height * 0.74f)
        val end = Offset(this.size.width * 0.80f, this.size.height * 0.28f)

        // The short leg is drawn over the first 40% of the progress, the long leg over the rest.
        val shortLeg = (progress / 0.4f).coerceIn(0f, 1f)
        val longLeg = ((progress - 0.4f) / 0.6f).coerceIn(0f, 1f)

        drawLine(
            color = color,
            start = start,
            end = lerp(start, corner, shortLeg),
            strokeWidth = stroke,
            cap = StrokeCap.Round,
        )

        if (longLeg > 0f) {
            drawLine(
                color = color,
                start = corner,
                end = lerp(corner, end, longLeg),
                strokeWidth = stroke,
                cap = StrokeCap.Round,
            )
        }
    }
}
