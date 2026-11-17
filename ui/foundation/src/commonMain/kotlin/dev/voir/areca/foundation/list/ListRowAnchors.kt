package dev.voir.areca.foundation.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onPlaced
import kotlin.math.abs

/**
 * Where one list row and its text block were last placed, so the list's hairline beside the row
 * can start where the row's text starts.
 *
 * Plain fields rather than snapshot state: both are written while the row is placed and read while
 * the list draws the same frame.
 */
internal class ListRowAnchor {
    /** Coordinates of the row's root. */
    var row: LayoutCoordinates? = null

    /** Coordinates of the row's text block — its title, subtitle and their siblings. */
    var text: LayoutCoordinates? = null

    /**
     * Distance from the start edge of [container] to the start of this row's text, in pixels.
     *
     * @param container Coordinates the distance is measured in: the list's, or the row's own.
     * @param rtl Whether the layout runs right to left, so the start edge is on the right.
     * @return The distance, or `null` while the row or its text is not placed.
     */
    fun textStartIn(container: LayoutCoordinates, rtl: Boolean): Float? {
        val text = text?.takeIf { it.isAttached } ?: return null
        if (!container.isAttached) return null
        val left = container.localPositionOf(text, Offset.Zero).x
        return if (rtl) container.size.width - (left + text.size.width) else left
    }
}

/**
 * The rows of one list, which report where their text starts so the list draws each hairline from
 * the text of the row under it to the far edge — as settings sections and pickers do — rather than
 * across the leading icon.
 *
 * A list provides it through [LocalListRowAnchors]; every [ListItem] in that list registers its
 * [ListRowAnchor]. Nothing here is snapshot state: placements are recorded during layout and read
 * while drawing the same frame, and a move calls the observer to redraw.
 */
internal class ListRowAnchors {
    private val anchors = mutableListOf<ListRowAnchor>()

    /** Redraws the hairlines that read these rows; `null` while nothing draws them. */
    private var onChanged: (() -> Unit)? = null

    /**
     * Registers the callback invoked when a row or its text moves.
     *
     * @param listener Invalidation callback, or `null` to stop observing.
     */
    fun observe(listener: (() -> Unit)?) {
        onChanged = listener
    }

    /**
     * Adds a row of this list.
     *
     * @param anchor The row's anchor.
     */
    fun add(anchor: ListRowAnchor) {
        anchors += anchor
    }

    /**
     * Removes a row that left the list.
     *
     * @param anchor The row's anchor.
     */
    fun remove(anchor: ListRowAnchor) {
        anchors -= anchor
        onChanged?.invoke()
    }

    /** Tells the observer that a row or its text moved. */
    fun moved() {
        onChanged?.invoke()
    }

    /**
     * Start of the text of the row whose top edge is at [top] in [container].
     *
     * @param container Coordinates of the list.
     * @param top Vertical offset of a boundary between two rows, in pixels.
     * @param rtl Whether the layout runs right to left.
     * @return Distance from the start edge to the text, or `null` when no placed row starts there.
     */
    fun textStartAt(container: LayoutCoordinates, top: Float, rtl: Boolean): Float? {
        if (!container.isAttached) return null
        for (anchor in anchors) {
            val row = anchor.row?.takeIf { it.isAttached } ?: continue
            val rowTop = container.localPositionOf(row, Offset.Zero).y
            if (abs(rowTop - top) > 0.5f) continue
            return anchor.textStartIn(container, rtl)
        }
        return null
    }

    /**
     * Start of the text of the only row registered, for a lazy list whose items each hold their
     * own anchors.
     *
     * @param rtl Whether the layout runs right to left.
     * @return Distance from the row's start edge to its text, or `null` while it is not placed.
     */
    fun textStartOfOnlyRow(rtl: Boolean): Float? {
        val anchor = anchors.singleOrNull() ?: return null
        val row = anchor.row?.takeIf { it.isAttached } ?: return null
        return anchor.textStartIn(row, rtl)
    }
}

/** The rows of the list being composed, or `null` outside a list and inside a row's own slots. */
internal val LocalListRowAnchors: ProvidableCompositionLocal<ListRowAnchors?> =
    staticCompositionLocalOf { null }

/**
 * The anchor of one row in the list being composed, registered for as long as the row is.
 *
 * @param anchors The list's rows, or `null` outside a list.
 * @return The row's anchor, or `null` outside a list.
 */
@Composable
internal fun rememberListRowAnchor(anchors: ListRowAnchors?): ListRowAnchor? {
    if (anchors == null) return null
    val anchor = remember(anchors) { ListRowAnchor() }
    DisposableEffect(anchors, anchor) {
        anchors.add(anchor)
        onDispose { anchors.remove(anchor) }
    }
    return anchor
}

/**
 * Records where a row is placed.
 *
 * @param anchor The row's anchor, or `null` outside a list.
 * @param anchors The list's rows, told when the row moves.
 * @return Modifier for the row's root.
 */
internal fun Modifier.listRow(anchor: ListRowAnchor?, anchors: ListRowAnchors?): Modifier =
    if (anchor == null || anchors == null) {
        this
    } else {
        onPlaced { coordinates ->
            anchor.row = coordinates
            anchors.moved()
        }
    }

/**
 * Records where a row's text block is placed.
 *
 * @param anchor The row's anchor, or `null` outside a list.
 * @param anchors The list's rows, told when the text moves.
 * @return Modifier for the row's text block.
 */
internal fun Modifier.listRowText(anchor: ListRowAnchor?, anchors: ListRowAnchors?): Modifier =
    if (anchor == null || anchors == null) {
        this
    } else {
        onPlaced { coordinates ->
            anchor.text = coordinates
            anchors.moved()
        }
    }
