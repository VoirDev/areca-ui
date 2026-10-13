package dev.voir.areca.foundation.wheel

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollScope

/**
 * Ends a throw on a whole row.
 *
 * Written rather than taken from `rememberSnapFlingBehavior` because the velocity a throw arrives
 * with cannot be trusted. A finger's is real; a scroll wheel's is synthesised from a velocity
 * tracker that is never reset and, for a single notch, from a fixed fallback — either of which a
 * decay turns into a jump of several rows for one notch of a mouse. Capping the projected travel in
 * rows makes every input device land somewhere the user could have aimed at.
 *
 * @property state Column being thrown.
 * @property decay Decay used only to ask how far the throw would go; the travel itself is animated
 *   by [snapSpec] so that it always ends on a row.
 * @property snapSpec Animation that carries the wheel to the row it landed on.
 * @property maxFlingRows Furthest a single throw may travel.
 */
internal class WheelPickerFlingBehavior(
    private val state: WheelPickerColumnState,
    private val decay: DecayAnimationSpec<Float>,
    private val snapSpec: AnimationSpec<Float>,
    private val maxFlingRows: Int,
) : FlingBehavior {
    /**
     * Carries the wheel from where the gesture left it to the row it should rest on.
     *
     * @param initialVelocity Velocity the gesture ended with, in pixels per second.
     * @return Velocity left over. Always `0f`: the wheel absorbs the whole throw, since handing the
     *   remainder to an ancestor would scroll the page the instant a wheel reached its last item.
     */
    override suspend fun ScrollScope.performFling(initialVelocity: Float): Float {
        val rowPx = state.rowHeightPx
        if (rowPx <= 0f || state.itemCount <= 0) return 0f

        val projectedRows = decay.calculateTargetValue(
            initialValue = 0f,
            initialVelocity = initialVelocity,
        ) / rowPx
        val from = state.offset
        val target = flingTargetOffset(
            offset = from,
            projectedRows = projectedRows,
            maxFlingRows = maxFlingRows,
            itemCount = state.itemCount,
            infinite = state.infinite,
        )

        var last = from
        animate(
            initialValue = from,
            targetValue = target,
            initialVelocity = initialVelocity / rowPx,
            animationSpec = snapSpec,
        ) { value, _ ->
            scrollBy((value - last) * rowPx)
            last = value
        }
        return 0f
    }
}
