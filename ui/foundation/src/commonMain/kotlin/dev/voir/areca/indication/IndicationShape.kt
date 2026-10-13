package dev.voir.areca.indication

import androidx.compose.foundation.Indication
import androidx.compose.ui.graphics.Shape

/**
 * This indication drawn in [shape] instead of the shape it was built with.
 *
 * Lets a component whose style carries both a shape and an indication — a button, an icon button —
 * treat the style's shape as the one source of truth, so `style.copy(shape = …)` moves the hover
 * tint, the press, and the focus ring along with the container. Every Areca indication takes the
 * new shape; any other [Indication] has no shape to change and is returned as it is.
 *
 * @param shape Shape the feedback follows.
 * @return An indication equal to this one but for its shape, or this one when it already uses
 *   [shape] or is not an Areca indication.
 */
internal fun Indication.reshapedTo(shape: Shape): Indication = when (this) {
    is TouchIndication -> if (this.shape == shape) this else copy(shape = shape)
    is StrokeIndication -> if (this.shape == shape) this else copy(shape = shape)
    is FadeIndication -> if (this.shape == shape) this else copy(shape = shape)
    is LiquidSweepIndication -> if (this.shape == shape) this else copy(shape = shape)
    else -> this
}
