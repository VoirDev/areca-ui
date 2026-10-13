package dev.voir.areca.modifier

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Smallest touch target an interactive control reserves: the size a finger reliably hits.
 *
 * Every Areca control that draws smaller than this — a button, an icon button, a checkbox, a
 * swatch — reserves it through [minimumTouchTarget], so the controls cannot disagree about it.
 */
public val MinimumTouchTarget: Dp = 48.dp

/**
 * Reserves at least [minimum] in each direction around an element that draws smaller, centring it.
 *
 * For a control whose drawn size is below a finger's reach — a 32dp icon button, a 40dp button. The
 * element keeps its drawn size, so its fill, border and indication stay where the design puts them;
 * only the layout slot grows. Compose extends a pointer target's hit area to the platform's minimum
 * touch size when nothing else claims the touch, and reserving the space is what guarantees nothing
 * else does: a neighbour cannot be laid out inside the band.
 *
 * Apply it outside the element's own size and before its `clickable` (or `selectable`,
 * `toggleable`), so the interactive area stays at the drawn size. A non-positive or unspecified
 * [minimum] reserves nothing. The reservation never grows past the parent's constraints: a control
 * squeezed into a smaller slot keeps the slot rather than overflowing its neighbours.
 *
 * ```kotlin
 * Box(
 *     modifier = Modifier
 *         .minimumTouchTarget()
 *         .size(32.dp)
 *         .clickable(onClick = onClick),
 * )
 * ```
 *
 * @param minimum Smallest width and height the element occupies in layout. Defaults to
 *   [MinimumTouchTarget].
 * @return The receiver, with the reservation appended.
 */
public fun Modifier.minimumTouchTarget(minimum: Dp = MinimumTouchTarget): Modifier {
    if (minimum == Dp.Unspecified || minimum.value <= 0f) return this
    return layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)
        val minimumPx = minimum.roundToPx()
        val width = touchTargetExtent(placeable.width, minimumPx, constraints.maxWidth)
        val height = touchTargetExtent(placeable.height, minimumPx, constraints.maxHeight)
        layout(width, height) {
            placeable.place((width - placeable.width) / 2, (height - placeable.height) / 2)
        }
    }
}

/**
 * Size along one axis of a slot that holds content of [content] pixels and reserves [minimum].
 *
 * The reservation never pushes past [max], the parent's limit: a control squeezed into a smaller
 * slot keeps the slot rather than overflowing its neighbours.
 *
 * @param content Measured size of the content, in pixels.
 * @param minimum Size to reserve, in pixels.
 * @param max Largest size the parent allows, in pixels.
 * @return The slot size, in pixels: at least [content], and at least [minimum] where [max] allows.
 */
internal fun touchTargetExtent(content: Int, minimum: Int, max: Int): Int =
    maxOf(content, minOf(minimum, max))
