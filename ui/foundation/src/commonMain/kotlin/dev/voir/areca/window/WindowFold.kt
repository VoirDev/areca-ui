package dev.voir.areca.window

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpRect
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.height
import androidx.compose.ui.unit.width

/** Which way a [WindowFold] runs across the window. */
public enum class FoldOrientation {
    /** Top to bottom: a book-style foldable held open, its halves left and right. */
    Vertical,

    /** Side to side: a foldable propped half open on a table, its halves above and below. */
    Horizontal,
}

/** How far a foldable is opened along its [WindowFold]. */
public enum class FoldState {
    /** Opened out flat: the fold is a line across one continuous screen. */
    Flat,

    /** Partly folded, like a book half open or a laptop: each half faces its own way. */
    HalfOpened,
}

/**
 * A fold or hinge across the window, in the window's — or, under a [WindowClassBox], the box's — own
 * coordinates.
 *
 * Read it from [WindowClass.fold]. Areca's two-pane layouts put their split on a vertical fold and
 * the [dev.voir.areca.foundation.adaptive.EdgeColumn] keeps its clusters either side of a horizontal
 * one; a layout of your own keeps controls and text off [bounds], and, while the device is
 * [FoldState.HalfOpened], lays out each half for itself.
 *
 * Reported on Android, from the platform's folding feature. iOS and the desktop report none; build
 * one to preview or test a layout at a fold with [ProvideWindowClass].
 *
 * @property orientation Which way the fold runs.
 * @property bounds Where it is: a line for a fold with no gap, the occluded strip for a hinge.
 * @property state How far the device is opened.
 * @property isSeparating Whether it splits the window into two parts content should not run across:
 *   a hinge, or any fold while half opened.
 */
@Immutable
public class WindowFold(
    public val orientation: FoldOrientation,
    public val bounds: DpRect,
    public val state: FoldState = FoldState.Flat,
    public val isSeparating: Boolean = state == FoldState.HalfOpened,
) {
    /**
     * The same fold seen from a container at [origin] of [size], or `null` when it does not cross
     * the container.
     *
     * @param origin The container's top-left corner, in this fold's coordinates.
     * @param size The container's size.
     * @return The fold in the container's coordinates, or `null`.
     */
    internal fun within(origin: DpOffset, size: DpSize): WindowFold? {
        val moved = DpRect(
            left = bounds.left - origin.x,
            top = bounds.top - origin.y,
            right = bounds.right - origin.x,
            bottom = bounds.bottom - origin.y,
        )
        // A fold without a gap is a line, zero wide: it crosses when that line falls strictly inside.
        val crosses = when (orientation) {
            FoldOrientation.Vertical -> moved.left > 0.dp && moved.right < size.width && moved.top < size.height && moved.bottom > 0.dp
            FoldOrientation.Horizontal -> moved.top > 0.dp && moved.bottom < size.height && moved.left < size.width && moved.right > 0.dp
        }
        return if (crosses) WindowFold(orientation, moved, state, isSeparating) else null
    }

    override fun equals(other: Any?): Boolean =
        this === other ||
            (other is WindowFold && orientation == other.orientation && bounds == other.bounds &&
                state == other.state && isSeparating == other.isSeparating)

    override fun hashCode(): Int {
        var result = orientation.hashCode()
        result = 31 * result + bounds.hashCode()
        result = 31 * result + state.hashCode()
        return 31 * result + isSeparating.hashCode()
    }

    override fun toString(): String =
        "WindowFold(orientation=$orientation, bounds=$bounds, state=$state, isSeparating=$isSeparating)"

    /** Builders for the common folds, for previews and tests. */
    public companion object {
        /**
         * A vertical fold with no gap, [at] from the start edge, across a window [height] tall.
         *
         * @param at Distance from the window's left edge.
         * @param height Height of the window.
         * @param state How far the device is opened.
         * @return The fold.
         */
        public fun vertical(at: Dp, height: Dp, state: FoldState = FoldState.Flat): WindowFold =
            WindowFold(FoldOrientation.Vertical, DpRect(at, 0.dp, at, height), state)

        /**
         * A horizontal fold with no gap, [at] from the top edge, across a window [width] wide.
         *
         * @param at Distance from the window's top edge.
         * @param width Width of the window.
         * @param state How far the device is opened.
         * @return The fold.
         */
        public fun horizontal(at: Dp, width: Dp, state: FoldState = FoldState.HalfOpened): WindowFold =
            WindowFold(FoldOrientation.Horizontal, DpRect(0.dp, at, width, at), state)
    }
}

/** The fold's thickness across its orientation: zero for a fold, the hinge's width for a hinge. */
internal val WindowFold.thickness: Dp
    get() = if (orientation == FoldOrientation.Vertical) bounds.width else bounds.height
