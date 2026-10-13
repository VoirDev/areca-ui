package dev.voir.areca.foundation.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.ParentDataModifierNode
import androidx.compose.ui.node.invalidateDraw
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection

/**
 * Vertical stack of settings rows that draws a hairline at every boundary between two rows.
 *
 * Separators belong to the stack rather than to the rows, so a section can never end up with a
 * leading or trailing hairline and callers never interleave dividers by hand. Each line is drawn on
 * top of the rows, centered on the boundary, so a row's press tint runs underneath it without
 * pushing it around.
 *
 * **A line starts where the text of the rows beside it starts**, and runs to the card's end edge.
 * Rows built by this package report their text start through a [SeparatorAnchor]; the line takes
 * the deeper of the two rows it separates, so it never runs underneath a leading icon, an avatar or
 * a nesting indent. A child that reports nothing — a [SettingsBlock], a caller's own composable —
 * falls back to [fallbackInset].
 *
 * Children of no height — a dialog or a sheet composed by a row part — take no line.
 *
 * @param separatorColor Color of the lines; [Color.Unspecified] or a transparent color draws none.
 * @param separatorThickness Thickness of the lines, floored at one pixel so a hairline never
 * disappears on a low-density screen.
 * @param fallbackInset Start inset of a line next to a child that reports no text start.
 * @param modifier Modifier applied to the stack.
 * @param content Rows of the stack, one child per row.
 */
@Composable
internal fun SettingsRowColumn(
    separatorColor: Color,
    separatorThickness: Dp,
    fallbackInset: Dp,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val boundaries = remember { RowBoundaries() }

    Layout(
        modifier = modifier.then(
            RowSeparatorElement(
                boundaries = boundaries,
                color = separatorColor,
                thickness = separatorThickness,
                fallbackInset = fallbackInset,
            )
        ),
        content = content,
    ) { measurables, constraints ->
        val rowConstraints = constraints.copy(minHeight = 0, maxHeight = Constraints.Infinity)
        val placeables = measurables.map { it.measure(rowConstraints) }
        val anchors = measurables.map { it.parentData as? SeparatorAnchor }

        val width = (placeables.maxOfOrNull { it.width } ?: constraints.minWidth)
            .coerceIn(constraints.minWidth, constraints.maxWidth)
        val height = placeables.sumOf { it.height }
            .coerceIn(constraints.minHeight, constraints.maxHeight)

        layout(width, height) {
            val lines = mutableListOf<RowBoundary>()
            var previous: SeparatorAnchor? = null
            var hasPrevious = false
            var y = 0
            placeables.forEachIndexed { index, placeable ->
                if (placeable.height > 0) {
                    if (hasPrevious) {
                        lines += RowBoundary(y = y.toFloat(), above = previous, below = anchors[index])
                    }
                    previous = anchors[index]
                    hasPrevious = true
                }
                placeable.placeRelative(0, y)
                y += placeable.height
            }
            boundaries.update(lines)
        }
    }
}

/**
 * Where a settings row's text starts, reported to the [SettingsRowColumn] that separates it.
 *
 * A row attaches the anchor to its root with [separatorAnchor] and to its text block with
 * [separatorText]. The two placements give the text's offset from the row's start edge, which is
 * all a separator needs; nothing is snapshot state, because the value is written during placement
 * and read while drawing the same frame.
 */
internal class SeparatorAnchor {
    /** Coordinates of the row's root, from its last placement. */
    private var row: LayoutCoordinates? = null

    /** Coordinates of the row's text block, from its last placement. */
    private var text: LayoutCoordinates? = null

    /** Whether the row is laid out right to left, so its start edge is on the right. */
    var rtl: Boolean = false

    /** Distance from the row's start edge to its text, in pixels; `NaN` until both are placed. */
    var textStart: Float = Float.NaN
        private set

    /** Invalidates the separators drawn beside this row; `null` while nothing draws them. */
    var onMoved: (() -> Unit)? = null

    /**
     * Records the row's placement.
     *
     * @param coordinates Coordinates of the row's root.
     */
    fun placeRow(coordinates: LayoutCoordinates) {
        row = coordinates
        measure()
    }

    /**
     * Records the text block's placement.
     *
     * @param coordinates Coordinates of the text block.
     */
    fun placeText(coordinates: LayoutCoordinates) {
        text = coordinates
        measure()
    }

    /** Recomputes [textStart] from the latest placements, invalidating drawing when it moved. */
    private fun measure() {
        val row = row?.takeIf { it.isAttached } ?: return
        val text = text?.takeIf { it.isAttached } ?: return
        val left = row.localPositionOf(text, Offset.Zero).x
        val start = if (rtl) row.size.width - (left + text.size.width) else left
        if (start == textStart) return
        textStart = start
        onMoved?.invoke()
    }
}

/**
 * Remembers the anchor of one settings row, keeping its layout direction current.
 *
 * @return Anchor to attach with [separatorAnchor] and [separatorText].
 */
@Composable
internal fun rememberSeparatorAnchor(): SeparatorAnchor {
    val anchor = remember { SeparatorAnchor() }
    val rtl = LocalLayoutDirection.current == LayoutDirection.Rtl
    SideEffect { anchor.rtl = rtl }
    return anchor
}

/**
 * Marks this layout as the root of a row that reports its text start to its section.
 *
 * Apply it outermost on the row, so the parent data reaches the section's stack.
 *
 * @param anchor Anchor of the row.
 * @return Modifier carrying the anchor as parent data and recording the row's placement.
 */
internal fun Modifier.separatorAnchor(anchor: SeparatorAnchor): Modifier =
    this.then(SeparatorAnchorElement(anchor)).onPlaced(anchor::placeRow)

/**
 * Marks this layout as the start of a row's text.
 *
 * @param anchor Anchor of the row the text belongs to.
 * @return Modifier recording the text's placement.
 */
internal fun Modifier.separatorText(anchor: SeparatorAnchor): Modifier =
    this.onPlaced(anchor::placeText)

/**
 * Modifier element that hands a [SeparatorAnchor] to the enclosing [SettingsRowColumn].
 *
 * @property anchor Anchor of the row.
 */
private data class SeparatorAnchorElement(
    val anchor: SeparatorAnchor,
) : ModifierNodeElement<SeparatorAnchorNode>() {
    /**
     * Creates the node that carries [anchor].
     *
     * @return A node holding [anchor].
     */
    override fun create(): SeparatorAnchorNode = SeparatorAnchorNode(anchor)

    /**
     * Points an existing node at [anchor].
     *
     * @param node Node created by an earlier element.
     */
    override fun update(node: SeparatorAnchorNode) {
        node.anchor = anchor
    }
}

/**
 * Parent-data node carrying a row's [SeparatorAnchor].
 *
 * @property anchor Anchor of the row.
 */
private class SeparatorAnchorNode(
    var anchor: SeparatorAnchor,
) : Modifier.Node(), ParentDataModifierNode {
    /**
     * Replaces the parent data with [anchor], so the stack reads it off the row's measurable.
     *
     * @param parentData Parent data set by modifiers further in; discarded.
     * @return [anchor].
     */
    override fun Density.modifyParentData(parentData: Any?): Any = anchor
}

/**
 * One boundary between two rows of a [SettingsRowColumn].
 *
 * @property y Vertical offset of the boundary, in pixels.
 * @property above Anchor of the row above, or `null` when it reports none.
 * @property below Anchor of the row below, or `null` when it reports none.
 */
private class RowBoundary(
    val y: Float,
    val above: SeparatorAnchor?,
    val below: SeparatorAnchor?,
)

/**
 * Boundaries between the rows of a [SettingsRowColumn], shared between its layout and its drawing.
 *
 * Written during placement and read during the draw pass of the same frame, which is why they are
 * plain fields rather than snapshot state: a change only has to invalidate drawing.
 */
private class RowBoundaries {
    /** Boundaries between adjacent rows, top to bottom. */
    var lines: List<RowBoundary> = emptyList()
        private set

    /** Invalidates the drawing that reads [lines]; `null` while nothing draws them. */
    private var onChanged: (() -> Unit)? = null

    /**
     * Registers the callback invoked when a boundary or an anchor beside one moves.
     *
     * @param listener Invalidation callback, or `null` to stop observing.
     */
    fun observe(listener: (() -> Unit)?) {
        onChanged = listener
        lines.forEach { line ->
            line.above?.onMoved = listener
            line.below?.onMoved = listener
        }
    }

    /**
     * Replaces the recorded boundaries and subscribes to the anchors beside them.
     *
     * @param values New boundaries.
     */
    fun update(values: List<RowBoundary>) {
        lines = values
        values.forEach { line ->
            line.above?.onMoved = onChanged
            line.below?.onMoved = onChanged
        }
        onChanged?.invoke()
    }
}

/**
 * Modifier element that draws the separators of a [SettingsRowColumn].
 *
 * @property boundaries Boundaries recorded by the stack layout.
 * @property color Color of the separators.
 * @property thickness Thickness of the separators.
 * @property fallbackInset Start inset beside a row that reports no text start.
 */
private data class RowSeparatorElement(
    val boundaries: RowBoundaries,
    val color: Color,
    val thickness: Dp,
    val fallbackInset: Dp,
) : ModifierNodeElement<RowSeparatorNode>() {
    /**
     * Creates the node that draws the separators.
     *
     * @return A node drawing with this element's parameters.
     */
    override fun create(): RowSeparatorNode = RowSeparatorNode(
        boundaries = boundaries,
        color = color,
        thickness = thickness,
        fallbackInset = fallbackInset,
    )

    /**
     * Applies this element's parameters to an existing node.
     *
     * @param node Node created by an earlier element.
     */
    override fun update(node: RowSeparatorNode) {
        node.update(
            boundaries = boundaries,
            color = color,
            thickness = thickness,
            fallbackInset = fallbackInset,
        )
    }
}

/**
 * Draws one hairline per recorded boundary, from the text start of the rows beside it to the end
 * edge of the stack.
 *
 * @property boundaries Boundaries recorded by the stack layout.
 * @property color Color of the separators.
 * @property thickness Thickness of the separators.
 * @property fallbackInset Start inset beside a row that reports no text start.
 */
private class RowSeparatorNode(
    private var boundaries: RowBoundaries,
    private var color: Color,
    private var thickness: Dp,
    private var fallbackInset: Dp,
) : Modifier.Node(), DrawModifierNode {

    /** Starts redrawing whenever a boundary or an anchor beside one moves. */
    override fun onAttach() {
        boundaries.observe { invalidateDraw() }
    }

    /** Stops observing, so the anchors no longer hold this node. */
    override fun onDetach() {
        boundaries.observe(null)
    }

    /**
     * Applies new parameters to an attached node.
     *
     * @param boundaries Boundaries recorded by the stack layout.
     * @param color Color of the separators.
     * @param thickness Thickness of the separators.
     * @param fallbackInset Start inset beside a row that reports no text start.
     */
    fun update(boundaries: RowBoundaries, color: Color, thickness: Dp, fallbackInset: Dp) {
        if (this.boundaries !== boundaries) {
            this.boundaries.observe(null)
            this.boundaries = boundaries
            boundaries.observe { invalidateDraw() }
        }
        this.color = color
        this.thickness = thickness
        this.fallbackInset = fallbackInset
        invalidateDraw()
    }

    /** Draws the rows, then one hairline over each boundary between them. */
    override fun ContentDrawScope.draw() {
        drawContent()

        if (color == Color.Unspecified || color.alpha <= 0.001f) return

        val lineThickness = thickness.toPx().coerceAtLeast(1f)
        val fallback = fallbackInset.toPx()

        boundaries.lines.forEach { line ->
            val start = maxOf(line.above.startOr(fallback), line.below.startOr(fallback))
            val width = size.width - start
            if (width <= 0f) return@forEach

            drawRect(
                color = color,
                topLeft = Offset(
                    x = if (layoutDirection == LayoutDirection.Ltr) start else 0f,
                    y = line.y - lineThickness / 2f,
                ),
                size = Size(width, lineThickness),
            )
        }
    }
}

/**
 * Text start reported by this anchor, or [fallback] when there is no anchor or it is not placed.
 *
 * @param fallback Inset to use when nothing was reported.
 * @return Distance from the row's start edge, in pixels.
 */
private fun SeparatorAnchor?.startOr(fallback: Float): Float =
    this?.textStart?.takeUnless { it.isNaN() } ?: fallback
