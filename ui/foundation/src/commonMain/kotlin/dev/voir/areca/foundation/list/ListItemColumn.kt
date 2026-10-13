package dev.voir.areca.foundation.list

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.invalidateDraw
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.theme.ArecaTheme

/**
 * A group of rows that draws a separator at every boundary between two of them.
 *
 * Separators belong to the group rather than to the rows, which is what makes it impossible to end
 * up with a stray hairline above the first row or below the last one, and means call sites stop
 * interleaving dividers by hand. Each line is drawn on top of the rows and centered on the boundary,
 * so a row's hover tint runs underneath it without pushing the line around. A divider entry of a
 * [List] is already a line, so no separator is drawn beside one.
 *
 * The group also owns the surface. It does not clip its rows, so a row's focus ring is drawn whole
 * where the row meets the group's edge; each [ListItem] instead clips its own background and
 * interaction tints to the group's shape, so the first and last row of a rounded group follow its
 * corners — which is why [ListItemStyle.plain] rows are square: their container gives them their
 * corners. Other content placed in the group paints to its own bounds, so give it the group's
 * corners itself if it fills its edges.
 *
 * ```kotlin
 * ListItemColumn(
 *     listStyle = ListStyle.default(bg = ArecaTheme.colors.bgSurface),
 *     listSize = ListSize.default(shape = ArecaTheme.shapes.md),
 * ) {
 *     NavigationListItem(title = { Text("Account") }, onClick = ::openAccount)
 *     NavigationListItem(title = { Text("Privacy") }, onClick = ::openPrivacy)
 * }
 * ```
 *
 * @param modifier Modifier applied to the group.
 * @param listStyle Colors of the group's surface: background, border and separators.
 * @param listSize Metrics of the group's surface: shape, border width and separator geometry.
 * @param separated Whether to draw separators between the rows. On by default, like every list
 * container in this package.
 * @param enabled Whether the rows of the group accept input; `false` disables every row in it.
 * @param content Rows of the group, one child per row.
 */
@Composable
public fun ListItemColumn(
    modifier: Modifier = Modifier,
    listStyle: ListStyle = LocalListStyle.current ?: ListStyle.Default,
    listSize: ListSize = LocalListSize.current ?: ListSize.Default,
    separated: Boolean = true,
    enabled: Boolean = true,
    content: @Composable () -> Unit,
) {
    val boundaries = remember { RowBoundaries() }
    val groupPlacement = remember { PlacedCoordinates() }
    val groupClip = ListGroupClip(shape = listSize.shape, placement = groupPlacement)

    val groupModifier = modifier
        .listSurface(listStyle = listStyle, listSize = listSize)
        .recordPlacement(groupPlacement)
        .then(
            RowSeparatorElement(
                boundaries = boundaries,
                color = if (separated) listStyle.separator else Color.Unspecified,
                thickness = listSize.separatorThickness,
                inset = listSize.separatorInset,
            )
        )

    CompositionLocalProvider(
        LocalListEnabled provides (enabled && LocalListEnabled.current),
        LocalListGroupClip provides groupClip,
    ) {
        Layout(modifier = groupModifier, content = content) { measurables, constraints ->
            val rowConstraints = constraints.copy(minHeight = 0, maxHeight = Constraints.Infinity)
            val placeables = measurables.map { it.measure(rowConstraints) }

            val width = (placeables.maxOfOrNull { it.width } ?: constraints.minWidth)
                .coerceIn(constraints.minWidth, constraints.maxWidth)
            val height = placeables.sumOf { it.height }
                .coerceIn(constraints.minHeight, constraints.maxHeight)

            layout(width, height) {
                val offsets = ArrayList<Float>(maxOf(0, placeables.size - 1))
                var y = 0
                placeables.forEachIndexed { index, placeable ->
                    placeable.placeRelative(0, y)
                    y += placeable.height
                    // A divider entry is already a line; a hairline beside it would double it up.
                    val separates = index != placeables.lastIndex &&
                        measurables[index].layoutId != ListDividerLayoutId &&
                        measurables[index + 1].layoutId != ListDividerLayoutId
                    if (separates) offsets += y.toFloat()
                }
                boundaries.update(offsets.toFloatArray())
            }
        }
    }
}

/**
 * Layout id of a [ListEntry.Divider] rendered inside a [ListItemColumn], which draws no separator
 * on either side of it.
 */
internal object ListDividerLayoutId

/**
 * Draws a list's own surface: fills the background and strokes the border in its shape.
 *
 * Shared by [ListItemColumn] and [LazyList] so a lazy list honours the same surface fields as a
 * composed one. It does not clip: [ListItemColumn] leaves its rows unclipped so their focus rings
 * stay whole, and [LazyList] clips its scrolling viewport itself. An unspecified background color
 * draws no background, and a zero border width or an unspecified border color draws no border.
 *
 * @param listStyle Colors of the surface; only [ListStyle.bg] and [ListStyle.border] are read.
 * @param listSize Metrics of the surface; only [ListSize.shape] and [ListSize.borderWidth] are read.
 * @return Modifier that draws the surface: the background behind the content, the border over it.
 */
internal fun Modifier.listSurface(listStyle: ListStyle, listSize: ListSize): Modifier {
    val filled = if (listStyle.bg != Color.Unspecified) {
        background(listStyle.bg, listSize.shape)
    } else {
        this
    }
    return if (listSize.borderWidth > 0.dp && listStyle.border != Color.Unspecified) {
        filled.border(listSize.borderWidth, listStyle.border, listSize.shape)
    } else {
        filled
    }
}

/**
 * Boundaries between the rows of a [ListItemColumn], shared between its layout and its drawing.
 *
 * The values are written during layout and read during the draw pass of the same frame, which is why
 * they are plain fields rather than snapshot state: no recomposition is involved, and a change only
 * has to invalidate drawing.
 */
private class RowBoundaries {
    /** Vertical offsets, in pixels, of each boundary between two adjacent rows. */
    var offsets: FloatArray = FloatArray(0)
        private set

    /** Invalidates the drawing that reads [offsets]; `null` while nothing draws them. */
    private var onChanged: (() -> Unit)? = null

    /**
     * Registers the callback invoked when [offsets] change.
     *
     * @param listener Invalidation callback, or `null` to stop observing.
     */
    fun observe(listener: (() -> Unit)?) {
        onChanged = listener
    }

    /**
     * Replaces the recorded boundaries, invalidating drawing only when they moved.
     *
     * @param values New boundary offsets in pixels.
     */
    fun update(values: FloatArray) {
        if (values.contentEquals(offsets)) return
        offsets = values
        onChanged?.invoke()
    }
}

/**
 * Modifier element that draws the separators of a [ListItemColumn].
 *
 * @property boundaries Boundaries recorded by the group layout.
 * @property color Color of the separators; [Color.Unspecified] draws none.
 * @property thickness Thickness of the separators.
 * @property inset Distance between each separator and both group edges.
 */
private data class RowSeparatorElement(
    val boundaries: RowBoundaries,
    val color: Color,
    val thickness: Dp,
    val inset: Dp,
) : ModifierNodeElement<RowSeparatorNode>() {
    override fun create(): RowSeparatorNode = RowSeparatorNode(
        boundaries = boundaries,
        color = color,
        thickness = thickness,
        inset = inset,
    )

    override fun update(node: RowSeparatorNode) {
        node.update(
            boundaries = boundaries,
            color = color,
            thickness = thickness,
            inset = inset,
        )
    }
}

/**
 * Draws one hairline per recorded row boundary, over the rows and within the group insets.
 *
 * @property boundaries Boundaries recorded by the group layout, observed while the node is
 *   attached.
 * @property color Color of the separators; [Color.Unspecified] draws none.
 * @property thickness Thickness of the separators.
 * @property inset Distance between each separator and both group edges.
 */
private class RowSeparatorNode(
    private var boundaries: RowBoundaries,
    private var color: Color,
    private var thickness: Dp,
    private var inset: Dp,
) : Modifier.Node(), DrawModifierNode {

    override fun onAttach() {
        boundaries.observe { invalidateDraw() }
    }

    override fun onDetach() {
        boundaries.observe(null)
    }

    /**
     * Applies new parameters to an attached node.
     *
     * @param boundaries Boundaries recorded by the group layout.
     * @param color Color of the separators.
     * @param thickness Thickness of the separators.
     * @param inset Distance between each separator and both group edges.
     */
    fun update(boundaries: RowBoundaries, color: Color, thickness: Dp, inset: Dp) {
        if (this.boundaries !== boundaries) {
            this.boundaries.observe(null)
            this.boundaries = boundaries
            boundaries.observe { invalidateDraw() }
        }
        this.color = color
        this.thickness = thickness
        this.inset = inset
        invalidateDraw()
    }

    override fun ContentDrawScope.draw() {
        drawContent()

        if (color == Color.Unspecified || color.alpha <= 0.001f) return

        val lineThickness = thickness.toPx().coerceAtLeast(1f)
        val insetPx = inset.toPx()
        val lineWidth = size.width - insetPx * 2f
        if (lineWidth <= 0f) return

        boundaries.offsets.forEach { boundary ->
            drawRect(
                color = color,
                topLeft = Offset(insetPx, boundary - lineThickness / 2f),
                size = Size(lineWidth, lineThickness),
            )
        }
    }
}
