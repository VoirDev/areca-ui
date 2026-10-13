package dev.voir.areca.crop

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import dev.voir.areca.indication.FadeIndication
import dev.voir.areca.indication.FocusOutline

/**
 * An Areca indication that draws only foundation's focus ring, for a control that answers presses and
 * drags with its own drawing — the stage and the dial.
 *
 * @param shape Shape of the control, which the ring follows.
 * @param focusOutline The ring: its color, width and offset from [shape].
 * @return The indication.
 */
internal fun focusRingIndication(shape: Shape, focusOutline: FocusOutline): FadeIndication = FadeIndication(
    shape = shape,
    color = Color.Transparent,
    hoverColor = Color.Transparent,
    pressedColor = Color.Transparent,
    focusOutline = focusOutline,
)
