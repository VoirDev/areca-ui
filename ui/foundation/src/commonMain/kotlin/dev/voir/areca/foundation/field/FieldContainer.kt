package dev.voir.areca.foundation.field

import androidx.compose.foundation.Indication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.indication.FadeIndication
import dev.voir.areca.indication.FocusOutline
import dev.voir.areca.indication.TINT_PRESSED_ALPHA

/**
 * Draws the box a field lives in: the focus ring, the fill, the clip and the border.
 *
 * Shared by [Field] and [FieldRow] rather than written twice, because the two must be
 * indistinguishable from the outside — a composite field is one field as far as a form is
 * concerned, and a border that resolved differently from a single-part one would give that away.
 *
 * Colors arrive already resolved and already animated. This is the drawing, not the decision:
 * which color applies to which state belongs to [FieldStyle], and the caller has done that.
 *
 * @param style Palette, for the shape and where the border goes.
 * @param metrics Border and ring thickness.
 * @param bg Resolved container fill.
 * @param border Resolved border color.
 * @param focusRing Resolved ring color, or [Color.Transparent] when no ring applies.
 * @return The modifier with the container drawn behind whatever follows it.
 */
internal fun Modifier.fieldContainer(
    style: FieldStyle,
    metrics: FieldSize,
    bg: Color,
    border: Color,
    focusRing: Color,
): Modifier = fieldContainer(
    style = style,
    borderWidth = metrics.borderWidth,
    focusRingWidth = metrics.focusRing,
    bg = bg,
    border = border,
    focusRing = focusRing,
)

/**
 * Draws a field's box from loose thicknesses rather than a whole [FieldSize].
 *
 * For a control that is several boxes rather than one — each cell of an [OtpField] is a field
 * container of its own, and has no use for the paddings and type a [FieldSize] carries.
 *
 * @param style Palette, for the shape and where the border goes.
 * @param borderWidth Border thickness.
 * @param focusRingWidth Ring thickness.
 * @param bg Resolved container fill.
 * @param border Resolved border color.
 * @param focusRing Resolved ring color, or [Color.Transparent] when no ring applies.
 * @return The modifier with the container drawn behind whatever follows it.
 */
internal fun Modifier.fieldContainer(
    style: FieldStyle,
    borderWidth: Dp,
    focusRingWidth: Dp,
    bg: Color,
    border: Color,
    focusRing: Color,
): Modifier = this
    .fieldFocusRing(shape = style.shape, width = focusRingWidth, color = focusRing)
    .background(bg, shape = style.shape)
    .clip(style.shape)
    .then(
        when (style.borderPlacement) {
            FieldBorderPlacement.All -> Modifier.border(borderWidth, border, style.shape)
            FieldBorderPlacement.Bottom -> Modifier.fieldBottomRule(
                width = borderWidth,
                color = border,
            )
        },
    )

/**
 * Strokes a halo just outside the container.
 *
 * Drawn outside the field's own bounds rather than in reserved space, so turning the ring on does
 * not move anything: a field keeps the height its size promises, and a form does not reflow the
 * first time somebody tabs into it. The cost is that a ring can overlap a neighbour sitting tight
 * against the field, and that an ancestor which clips will cut it off.
 *
 * The path is the shape re-resolved at the inflated size rather than a true offset of it, which is
 * exact for a rectangle and off by the corner-radius difference for a rounded one — invisible at
 * the few pixels a ring is thick, and far cheaper than offsetting a path every frame.
 *
 * @param shape Container shape the ring follows.
 * @param width Ring thickness.
 * @param color Ring color. Fully transparent draws nothing, which is how an unfocused field and a
 *   style with no ring at all both arrive here.
 * @return The modifier with the ring drawn behind whatever follows it.
 */
private fun Modifier.fieldFocusRing(
    shape: Shape,
    width: Dp,
    color: Color,
): Modifier = drawBehind {
    if (color.alpha <= 0f) return@drawBehind

    val ring = width.toPx()
    if (ring <= 0f || size.width <= 0f || size.height <= 0f) return@drawBehind

    // Inflate by half the ring on every side and stroke centred on that path, which puts the
    // stroke's inner edge flush against the container and its outer edge `ring` away from it.
    val outline = shape.createOutline(
        size = Size(size.width + ring, size.height + ring),
        layoutDirection = layoutDirection,
        density = this,
    )
    translate(left = -ring / 2f, top = -ring / 2f) {
        drawOutline(outline = outline, color = color, style = Stroke(width = ring))
    }
}

/**
 * Draws a rule along the bottom edge, for [FieldBorderPlacement.Bottom].
 *
 * Inside the container's bounds rather than under them, so an underlined field occupies exactly
 * the same box as an outlined one and the two can be swapped without re-spacing a form.
 *
 * @param width Rule thickness.
 * @param color Rule color.
 * @return The modifier with the rule drawn behind whatever follows it.
 */
private fun Modifier.fieldBottomRule(
    width: Dp,
    color: Color,
): Modifier = drawBehind {
    val stroke = width.toPx()
    if (stroke <= 0f) return@drawBehind

    drawRect(
        color = color,
        topLeft = Offset(x = 0f, y = size.height - stroke),
        size = Size(width = size.width, height = stroke),
    )
}

/**
 * Press feedback for a field, or a part of one, that is itself the trigger — a select, a date
 * segment.
 *
 * The [FadeIndication.tint] family that cards and list rows answer a press with, narrowed to the
 * press alone. A field already shows hover through its state colors and focus through its own ring
 * outside the container, so a hover tint or an indication ring here would draw each state twice.
 *
 * @param shape Shape the tint fills: the container's for a [Field], a rectangle for a [FieldRow]
 *   segment, which the row's own clip rounds.
 * @param ink Color the tint is taken from — the field's resting content color, so a pressed field
 *   darkens towards its own text the way a pressed card does.
 * @return Indication to hand to the trigger's `clickable`.
 */
internal fun fieldPressIndication(shape: Shape, ink: Color): Indication = FadeIndication(
    shape = shape,
    hoverColor = Color.Transparent,
    pressedColor = ink.copy(alpha = TINT_PRESSED_ALPHA),
    focusOutline = FocusOutline(color = Color.Transparent, width = 0.dp),
)
