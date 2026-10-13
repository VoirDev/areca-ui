package dev.voir.areca.foundation.list

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.findRootCoordinates
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.toSize
import dev.voir.areca.indication.buildShapePath

/**
 * Where a row group, or one of its rows, sits on screen, written when it is placed.
 *
 * A plain field rather than snapshot state: placement always precedes the draw pass that reads it
 * in the same frame, and re-placing a row redraws it, so no recomposition is involved.
 */
internal class PlacedCoordinates {
    /** Coordinates written at the last placement, or `null` before the first one. */
    var coordinates: LayoutCoordinates? = null
}

/**
 * The rounded surface of a row group that its rows draw their states inside.
 *
 * A group does not clip its rows, because clipping would also crop a row's focus ring where the
 * row meets the group's edge. Each row instead clips only its own state drawing — background, hover
 * and press tints, the selected rail — to this shape, so the first and last row of a rounded group
 * still follow its corners.
 *
 * @property shape Shape of the group's surface.
 * @property placement Where the group was last placed.
 */
@Immutable
internal data class ListGroupClip(
    val shape: Shape,
    val placement: PlacedCoordinates,
)

/** The group the rows below this point sit in, or `null` outside one. */
internal val LocalListGroupClip = compositionLocalOf<ListGroupClip?> { null }

/**
 * Records where this point of the modifier chain is placed.
 *
 * A group applies it where the chain's bounds are its surface; a row applies it where they are the
 * area its states are drawn in.
 *
 * @param placement Holder the coordinates are written to.
 * @return Modifier that records the placement.
 */
internal fun Modifier.recordPlacement(placement: PlacedCoordinates): Modifier =
    onPlaced { placement.coordinates = it }

/**
 * Builds this group's outline in the coordinate space of one of its rows.
 *
 * @param row Coordinates of the row's drawing, or `null` before the row is placed.
 * @param layoutDirection Direction the outline is resolved in.
 * @param density Density the outline is resolved at.
 * @param target Path that receives the outline; reset first.
 * @return `true` when [target] holds the outline; `false` when either side is not placed, or the
 *   row is drawn in another window — a popup opened from inside the group — where the group's
 *   outline does not apply.
 */
internal fun ListGroupClip.outlineIn(
    row: LayoutCoordinates?,
    layoutDirection: LayoutDirection,
    density: Density,
    target: Path,
): Boolean {
    val group = placement.coordinates
    if (row == null || group == null || !row.isAttached || !group.isAttached) return false
    if (row.findRootCoordinates() !== group.findRootCoordinates()) return false

    val groupSize: Size = group.size.toSize()
    buildShapePath(
        shape = shape,
        size = groupSize,
        layoutDirection = layoutDirection,
        density = density,
        offsetPx = 0f,
        target = target,
    )
    target.translate(row.localPositionOf(group, Offset.Zero))
    return true
}
