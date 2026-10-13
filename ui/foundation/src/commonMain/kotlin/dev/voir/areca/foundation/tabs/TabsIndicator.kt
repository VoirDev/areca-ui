package dev.voir.areca.foundation.tabs

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

/**
 * Where one tab sits along the strip's scrolling axis.
 *
 * Along the axis rather than as a rectangle, because that is all the indicator and the
 * scroll-into-view need — and it is what lets one type serve both orientations.
 *
 * @property start Lower edge, in pixels from the left or top of the strip's content.
 * @property end Higher edge, in pixels from the left or top of the strip's content.
 */
@Immutable
internal data class TabBounds(
    val start: Float,
    val end: Float,
)

/**
 * Where a [Tabs] strip draws its indicator, moved from tab to tab as the selection changes.
 *
 * The first position is taken at once rather than animated to, so a strip does not open with its
 * indicator sweeping in from the edge to a tab that was selected all along. Every later move
 * animates. Hiding the indicator — nothing selected — resets that, so the next selection appears in
 * place too.
 */
@Stable
internal class TabsIndicator {
    /** Animated lower edge. */
    private val start = Animatable(0f)

    /** Animated higher edge. */
    private val end = Animatable(0f)

    /** Whether the indicator has a position to be drawn at. */
    private var isPlaced by mutableStateOf(false)

    /**
     * Where to draw the indicator right now.
     *
     * Reads animated state, so call it in the draw phase to redraw without recomposing.
     *
     * @return Current bounds, or `null` while nothing is selected.
     */
    fun bounds(): TabBounds? = if (isPlaced) TabBounds(start.value, end.value) else null

    /**
     * Moves the indicator to the selected tab's [bounds].
     *
     * Suspends until the move finishes; a newer call cancels it by cancelling the caller.
     *
     * @param bounds Selected tab's bounds, or `null` to hide the indicator.
     * @param animationSpec Motion used for every move after the first.
     */
    suspend fun moveTo(bounds: TabBounds?, animationSpec: AnimationSpec<Float>) {
        if (bounds == null) {
            isPlaced = false
            return
        }

        if (!isPlaced) {
            start.snapTo(bounds.start)
            end.snapTo(bounds.end)
            isPlaced = true
            return
        }

        coroutineScope {
            launch { start.animateTo(bounds.start, animationSpec) }
            launch { end.animateTo(bounds.end, animationSpec) }
        }
    }
}
