package dev.voir.areca.modifier

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp

/**
 * Draws a border of [width] filled with [brush], following [shape].
 *
 * `Modifier.border` takes a `Brush` too, but paints it across the element's full bounds, so a
 * gradient's stops land where the element's edges are rather than where the border is. This strokes
 * the outline itself, which keeps the gradient running *along* the border.
 *
 * The stroke is centred on the outline and inset by half its width, so the border sits entirely
 * inside the element — matching `Modifier.border` and leaving layout untouched. Draws over the
 * content, like a border rather than a background.
 *
 * Corner radii are reduced by half the stroke width so the border stays concentric with [shape]
 * instead of bulging at the corners.
 *
 * Two shapes are drawn approximately. A rounded shape with a different radius per corner is drawn
 * with its top-left radius on all four, because the underlying round-rect draw takes a single
 * radius. A shape with a generic outline (a custom path) cannot be inset at all and is stroked on
 * its own outline, so half the border falls outside the element. Pass a uniform rounded shape, or a
 * rectangle, to get an exact border.
 *
 * @param width Border thickness. A non-positive width draws nothing.
 * @param brush Brush the border is filled with.
 * @param shape Outline the border follows. Should match the element's background shape.
 * @return The receiver, with the border drawn over its content.
 */
internal fun Modifier.gradientBorder(
    width: Dp,
    brush: Brush,
    shape: Shape,
): Modifier = drawWithCache {
    val widthPx = width.toPx()
    val inset = widthPx / 2f
    val outline = shape.createOutline(size, layoutDirection, this)
    val strokedSize = Size(size.width - widthPx, size.height - widthPx)

    onDrawWithContent {
        drawContent()
        if (widthPx <= 0f || strokedSize.width <= 0f || strokedSize.height <= 0f) {
            return@onDrawWithContent
        }

        when (outline) {
            is Outline.Rounded -> {
                val radius = outline.roundRect.topLeftCornerRadius
                drawRoundRect(
                    brush = brush,
                    topLeft = Offset(inset, inset),
                    size = strokedSize,
                    cornerRadius = CornerRadius(
                        x = (radius.x - inset).coerceAtLeast(0f),
                        y = (radius.y - inset).coerceAtLeast(0f),
                    ),
                    style = Stroke(widthPx),
                )
            }

            is Outline.Rectangle -> {
                drawRect(
                    brush = brush,
                    topLeft = Offset(inset, inset),
                    size = strokedSize,
                    style = Stroke(widthPx),
                )
            }

            is Outline.Generic -> {
                drawPath(
                    path = outline.path,
                    brush = brush,
                    style = Stroke(widthPx),
                )
            }
        }
    }
}
