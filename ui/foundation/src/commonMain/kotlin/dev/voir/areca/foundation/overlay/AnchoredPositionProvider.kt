package dev.voir.areca.foundation.overlay

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.roundToIntRect
import androidx.compose.ui.window.PopupPositionProvider
import kotlin.math.max

/**
 * Where an anchored surface sits relative to the element that opened it.
 *
 * The first word is the side, the second the alignment along the anchor's width. `BelowStart` — the
 * usual choice for a dropdown — hangs the surface under its anchor with their leading edges lined
 * up. Start and end follow the layout direction: the leading edge is the left one left to right and
 * the right one right to left.
 */
public enum class AnchoredPlacement {
    /** Under the anchor, leading edges aligned. */
    BelowStart,

    /** Under the anchor, centred on it. */
    BelowCenter,

    /** Under the anchor, trailing edges aligned. */
    BelowEnd,

    /** Above the anchor, leading edges aligned. */
    AboveStart,

    /** Above the anchor, centred on it. */
    AboveCenter,

    /** Above the anchor, trailing edges aligned. */
    AboveEnd;

    /** Whether the surface hangs below the anchor rather than sitting above it. */
    internal val isBelow: Boolean get() = this == BelowStart || this == BelowCenter || this == BelowEnd

    /** The same alignment on the opposite side, used when a placement would run off screen. */
    internal val flipped: AnchoredPlacement
        get() = when (this) {
            BelowStart -> AboveStart
            BelowCenter -> AboveCenter
            BelowEnd -> AboveEnd
            AboveStart -> BelowStart
            AboveCenter -> BelowCenter
            AboveEnd -> BelowEnd
        }
}

/**
 * Where an anchor is on screen, so a surface opened from it knows where to appear.
 *
 * Bounds are in *window* coordinates, because a `Popup` is positioned against the window rather than
 * against whatever laid the anchor out.
 */
@Stable
public class AnchoredState internal constructor() {
    /** The anchor's rectangle in window coordinates, or `null` before it has been laid out. */
    public var anchorBoundsInWindow: IntRect? by mutableStateOf(null)
        internal set

    /** The anchor's measured size, used to size a surface that matches its anchor's width. */
    public var anchorSize: IntSize by mutableStateOf(IntSize.Zero)
        internal set
}

/**
 * Remembers where an anchor is.
 *
 * @return State to pass to [Modifier.anchored] and to the surface that opens from it.
 */
@Composable
public fun rememberAnchoredState(): AnchoredState = remember { AnchoredState() }

/**
 * Reports this element's position and size into [state], making it an anchor.
 *
 * @param state State the position is written into.
 * @return The receiver, reporting its bounds.
 */
public fun Modifier.anchored(state: AnchoredState): Modifier = onGloballyPositioned { coordinates ->
    state.anchorBoundsInWindow = coordinates.boundsInWindow().roundToIntRect()
    state.anchorSize = coordinates.size
}

/**
 * Positions a popup against a remembered anchor.
 *
 * Compose hands `calculatePosition` its own idea of the anchor's bounds, but those are relative to
 * whatever composable owns the popup rather than to the window. This provider uses the bounds an
 * [AnchoredState] captured instead, which is what keeps a dropdown attached to its trigger when the
 * trigger is inside a scrolling or nested layout.
 *
 * Start and end alignments resolve against the layout direction the popup is laid out in, and
 * [offsetX] points toward the end edge, so a nudge mirrors with the rest of a right-to-left screen.
 *
 * @property anchorBounds The anchor's rectangle in window coordinates.
 * @property placement Preferred side and alignment.
 * @property offsetX Horizontal nudge applied after placement, in pixels; positive moves toward the
 *   end edge.
 * @property offsetY Vertical nudge applied after placement, in pixels.
 * @property flipWhenClipped Whether to try the opposite side when the preferred one does not fit.
 *   A dropdown near the bottom of the window then opens upward instead of being squashed against the
 *   edge, overlapping its own trigger. Pass `false` to pin the placement and clamp instead.
 */
internal class AnchoredPositionProvider(
    private val anchorBounds: IntRect,
    private val placement: AnchoredPlacement,
    private val offsetX: Int,
    private val offsetY: Int,
    private val flipWhenClipped: Boolean = true,
) : PopupPositionProvider {

    /**
     * Positions the popup against the bounds this provider was built with, not the ones Compose
     * passes in (see the class documentation).
     *
     * @param anchorBounds Compose's anchor bounds, relative to the popup's owner; unused.
     * @param windowSize Size of the window the popup must fit inside.
     * @param layoutDirection Direction start and end alignments, and [offsetX], resolve against.
     * @param popupContentSize Measured size of the popup.
     * @return Top-left corner of the popup in window coordinates, kept inside the window.
     */
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize,
    ): IntOffset {
        val anchor = this.anchorBounds
        val resolved = if (flipWhenClipped) {
            resolvePlacement(anchor, windowSize, popupContentSize)
        } else {
            placement
        }

        val x = horizontalPositionFor(
            placement = resolved,
            anchor = anchor,
            popupWidth = popupContentSize.width,
            offsetX = offsetX,
            layoutDirection = layoutDirection,
        )
        val y = verticalPositionFor(resolved, anchor, popupContentSize) + offsetY

        // Clamped even after flipping: a surface taller than the window fits nowhere, and pinning it
        // to the top edge at least keeps its first rows readable.
        return IntOffset(
            x = x.coerceIn(0, max(0, windowSize.width - popupContentSize.width)),
            y = y.coerceIn(0, max(0, windowSize.height - popupContentSize.height)),
        )
    }

    /**
     * Picks the side the surface actually opens on.
     *
     * Flips only when the preferred side does not fit *and* the opposite one does — a surface too
     * tall for either side stays where the caller asked, so it does not jump about as content grows.
     *
     * @param anchor The anchor's rectangle in window coordinates.
     * @param windowSize Size of the window the popup must fit inside.
     * @param popupContentSize Measured size of the popup.
     * @return The placement to use.
     */
    private fun resolvePlacement(
        anchor: IntRect,
        windowSize: IntSize,
        popupContentSize: IntSize,
    ): AnchoredPlacement {
        val preferredTop = verticalPositionFor(placement, anchor, popupContentSize) + offsetY
        val fits = preferredTop >= 0 && preferredTop + popupContentSize.height <= windowSize.height
        if (fits) return placement

        val flipped = placement.flipped
        val flippedTop = verticalPositionFor(flipped, anchor, popupContentSize) + offsetY
        val flippedFits =
            flippedTop >= 0 && flippedTop + popupContentSize.height <= windowSize.height

        return if (flippedFits) flipped else placement
    }

    /**
     * Top edge of the popup for one placement, before clamping.
     *
     * @param placement Side to measure for.
     * @param anchor The anchor's rectangle in window coordinates.
     * @param popupContentSize Measured size of the popup.
     * @return Top edge in window coordinates.
     */
    private fun verticalPositionFor(
        placement: AnchoredPlacement,
        anchor: IntRect,
        popupContentSize: IntSize,
    ): Int = if (placement.isBelow) anchor.bottom else anchor.top - popupContentSize.height
}

/**
 * Left edge of an anchored popup for one placement, before clamping.
 *
 * The start-aligned placements line up the leading edges — the left ones left to right, the right
 * ones right to left — and the end-aligned ones the trailing edges. [offsetX] points toward the end
 * edge, so it is subtracted right to left.
 *
 * @param placement Side and alignment to position for.
 * @param anchor The anchor's rectangle in window coordinates.
 * @param popupWidth Measured width of the popup, in pixels.
 * @param offsetX Nudge toward the end edge, in pixels.
 * @param layoutDirection Direction start and end resolve against.
 * @return Left edge in window coordinates.
 */
internal fun horizontalPositionFor(
    placement: AnchoredPlacement,
    anchor: IntRect,
    popupWidth: Int,
    offsetX: Int,
    layoutDirection: LayoutDirection,
): Int {
    val leftAligned = anchor.left
    val rightAligned = anchor.right - popupWidth
    val isRtl = layoutDirection == LayoutDirection.Rtl
    val x = when (placement) {
        AnchoredPlacement.BelowStart, AnchoredPlacement.AboveStart ->
            if (isRtl) rightAligned else leftAligned

        AnchoredPlacement.BelowCenter, AnchoredPlacement.AboveCenter ->
            anchor.left + (anchor.width - popupWidth) / 2

        AnchoredPlacement.BelowEnd, AnchoredPlacement.AboveEnd ->
            if (isRtl) leftAligned else rightAligned
    }
    return if (isRtl) x - offsetX else x + offsetX
}
