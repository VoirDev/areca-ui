package dev.voir.areca.foundation.selection

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

/**
 * Where one option sits along a selector's main axis.
 *
 * Along the axis rather than as a rectangle, because that is all the thumb and the scroll-into-view
 * need, and it is what lets one type serve both orientations.
 *
 * @property start Lower edge, in pixels from the left or top of the selector's content.
 * @property end Higher edge, in pixels from the left or top of the selector's content.
 */
@Immutable
internal data class SelectorBounds(
    val start: Float,
    val end: Float,
)

/**
 * Where a selector's thumb should go.
 *
 * @property index Position of the chosen option among the selector's options.
 * @property bounds The chosen option's measured bounds.
 * @property color Thumb colour for the chosen option.
 */
@Immutable
internal data class SelectorThumbTarget(
    val index: Int,
    val bounds: SelectorBounds,
    val color: Color,
)

/**
 * Where a [SlidingSelector] draws its thumb, how present it is and in what colour, moved from option
 * to option as the selection changes.
 *
 * The first decision is taken at once, so a selector never opens with its thumb sweeping in. After
 * that:
 * - a move to another option slides both edges together while the colour cross-fades;
 * - a choice after none grows the thumb in at the chosen option, never from a stale place;
 * - clearing the selection shrinks and fades the thumb where it was;
 * - a new measurement of the same option (a layout change) follows without animating;
 * - with reduced motion, a move fades out and back in at the new place.
 */
@Stable
internal class SelectorThumbState {
    /** Animated lower edge. */
    private val start = Animatable(0f)

    /** Animated higher edge. */
    private val end = Animatable(0f)

    /** How present the thumb is, from 0 (gone) to 1 (fully shown). Drives opacity and scale. */
    private val presence = Animatable(0f)

    /** Progress of the colour cross-fade from [colorFrom] to [colorTo]. */
    private val colorProgress = Animatable(1f)

    /** Colour the cross-fade starts from. */
    private var colorFrom by mutableStateOf(Color.Transparent)

    /** Colour the cross-fade ends at. */
    private var colorTo by mutableStateOf(Color.Transparent)

    /** Whether the first decision, which is never animated, has been taken. */
    private var settled = false

    /** Option the thumb is at or travelling to, or `-1` while it is gone or going. */
    private var index = -1

    /**
     * Where to draw the thumb right now.
     *
     * Reads animated state, so call it in the layout or draw phase to update without recomposing.
     *
     * @return Current bounds, or `null` while the thumb is gone.
     */
    fun bounds(): SelectorBounds? =
        if (presence.value > 0f) SelectorBounds(start.value, end.value) else null

    /** Opacity to draw the thumb with. */
    val alpha: Float get() = presence.value

    /** Scale to draw the thumb at, from [GrowFrom] as it appears to 1 when fully shown. */
    val scale: Float get() = GrowFrom + (1f - GrowFrom) * presence.value

    /** Colour to draw the thumb in right now. */
    val color: Color get() = lerp(colorFrom, colorTo, colorProgress.value)

    /**
     * Moves the thumb to [target].
     *
     * Suspends until the move finishes; a newer call cancels it by cancelling the caller, and starts
     * from wherever this one left the thumb.
     *
     * @param target Where to go, or `null` to clear the thumb.
     * @param animationSpec Motion used for every change after the first.
     * @param reduceMotion Whether to fade between places instead of travelling.
     */
    suspend fun moveTo(
        target: SelectorThumbTarget?,
        animationSpec: FiniteAnimationSpec<Float>,
        reduceMotion: Boolean,
    ) {
        if (!settled) {
            settled = true
            if (target != null) {
                place(target)
                presence.snapTo(1f)
            }
            return
        }

        if (target == null) {
            index = -1
            presence.animateTo(0f, animationSpec)
            return
        }

        if (target.index == index) {
            // The same option, measured again: follow the layout rather than animating.
            start.snapTo(target.bounds.start)
            end.snapTo(target.bounds.end)
            if (target.color != colorTo) {
                colorFrom = color
                colorTo = target.color
                colorProgress.snapTo(0f)
            }
            // Finishes whatever a cancelled move left undone; nothing runs when nothing is.
            coroutineScope {
                if (colorProgress.value != 1f) launch { colorProgress.animateTo(1f, animationSpec) }
                if (presence.value != 1f) launch { presence.animateTo(1f, animationSpec) }
            }
            return
        }

        if (index == -1) {
            // From nothing: appear at the chosen option, whatever was left of a thumb going away.
            place(target)
            presence.animateTo(1f, animationSpec)
            return
        }

        index = target.index
        colorFrom = color
        colorTo = target.color
        colorProgress.snapTo(0f)

        if (reduceMotion) {
            presence.animateTo(0f, animationSpec)
            start.snapTo(target.bounds.start)
            end.snapTo(target.bounds.end)
            colorProgress.snapTo(1f)
            presence.animateTo(1f, animationSpec)
            return
        }

        coroutineScope {
            launch { start.animateTo(target.bounds.start, animationSpec) }
            launch { end.animateTo(target.bounds.end, animationSpec) }
            launch { colorProgress.animateTo(1f, animationSpec) }
            launch { presence.animateTo(1f, animationSpec) }
        }
    }

    /**
     * Puts the thumb at [target] at once, in its colour.
     *
     * @param target Where the thumb goes.
     */
    private suspend fun place(target: SelectorThumbTarget) {
        index = target.index
        start.snapTo(target.bounds.start)
        end.snapTo(target.bounds.end)
        colorFrom = target.color
        colorTo = target.color
        colorProgress.snapTo(1f)
    }

    private companion object {
        /** Scale a thumb grows from as it appears, and shrinks to as it goes. */
        const val GrowFrom = 0.6f
    }
}
