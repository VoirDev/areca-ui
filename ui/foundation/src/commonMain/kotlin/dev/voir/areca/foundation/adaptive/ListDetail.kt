package dev.voir.areca.foundation.adaptive

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isSpecified
import dev.voir.areca.foundation.layout.VerticalDivider
import dev.voir.areca.modifier.resizeCursor
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.window.FoldOrientation
import dev.voir.areca.window.InputMode
import dev.voir.areca.window.WindowClassBox
import dev.voir.areca.window.rememberFoldView
import dev.voir.areca.window.thickness

/** What the panes of a [ListDetail] know about how they are shown. */
@Stable
public interface ListDetailScope {
    /**
     * Whether the list and the detail are side by side. When `false` only one shows at a time, and
     * the detail should offer a way back to the list — a back action in its top bar.
     */
    public val isTwoPane: Boolean
}

/**
 * A list of things and the one that is open: side by side when there is room, one at a time when
 * there is not.
 *
 * In a container of [AdaptiveDefaults.TwoPaneMinWidth] or more, the list takes
 * [AdaptiveDefaults.listPaneWidth] at the start, a divider follows, and the detail fills the rest —
 * or [detailPlaceholder], while nothing is open. Under a pointer the divider is a handle while
 * [resizable]: dragging it moves the list's edge, within [AdaptiveDefaults.ListPaneMinWidth] and
 * leaving the detail [AdaptiveDefaults.DetailPaneMinWidth], and a double-click puts it back. The
 * dragged width is kept across recreation. In a narrower container the detail replaces the
 * list while [showDetail] is `true`, as a phone pushes a screen; take [showDetail] back to `false`
 * from the detail's back action. Each pane is a [WindowClassBox], so it lays itself out for its own
 * width, and each keeps its saved state — the list's scroll position — when the other is shown and
 * when a resize moves between one pane and two.
 *
 * On a foldable opened along a vertical fold, the panes meet at the fold whenever that leaves each
 * its minimum width — the list on one half, the detail on the other — the divider does not drag, and
 * a hinge's strip is left empty.
 *
 * The open item is the app's state: keep the selection and [showDetail] together, and set
 * [showDetail] when an item is chosen.
 *
 * ```kotlin
 * ListDetail(
 *     showDetail = selected != null,
 *     list = { TransactionList(selected = selected, onOpen = { selected = it }) },
 *     detail = {
 *         TransactionDetail(
 *             id = selected!!,
 *             onBack = if (isTwoPane) null else { { selected = null } },
 *         )
 *     },
 *     detailPlaceholder = { Fallback(title = "Pick a transaction") },
 * )
 * ```
 *
 * @param showDetail Whether something is open. One pane: the detail shows instead of the list. Two
 *   panes: the detail shows instead of [detailPlaceholder].
 * @param modifier Modifier applied to the layout.
 * @param listWidth Width of the list pane beside the detail, or [Dp.Unspecified] for
 *   [AdaptiveDefaults.listPaneWidth] of the container. A drag of the divider overrides it.
 * @param resizable Whether a pointer can drag the divider.
 * @param detailPlaceholder Content of the detail pane while nothing is open, two panes only. `null`
 *   leaves the pane empty.
 * @param list The list.
 * @param detail The open item.
 */
@Composable
public fun ListDetail(
    showDetail: Boolean,
    modifier: Modifier = Modifier,
    listWidth: Dp = Dp.Unspecified,
    resizable: Boolean = true,
    detailPlaceholder: (@Composable ListDetailScope.() -> Unit)? = null,
    list: @Composable ListDetailScope.() -> Unit,
    detail: @Composable ListDetailScope.() -> Unit,
) {
    val holder = rememberSaveableStateHolder()
    // In dp; null until the person drags the divider.
    var draggedWidth by rememberSaveable { mutableStateOf<Float?>(null) }
    val foldView = rememberFoldView()
    BoxWithConstraints(modifier = modifier.then(foldView.modifier)) {
        val containerWidth = maxWidth
        val twoPane = containerWidth >= AdaptiveDefaults.TwoPaneMinWidth
        val scope = remember(twoPane) { ListDetailScopeImpl(twoPane) }
        if (twoPane) {
            // A vertical fold that leaves both panes their minimum widths is where they meet.
            val fold = foldView.foldIn(DpSize(maxWidth, maxHeight))
                ?.takeIf { it.orientation == FoldOrientation.Vertical }
                ?.takeIf {
                    it.bounds.left >= AdaptiveDefaults.ListPaneMinWidth &&
                        containerWidth - it.bounds.right >= AdaptiveDefaults.DetailPaneMinWidth
                }
            val baseWidth = if (listWidth.isSpecified) listWidth else AdaptiveDefaults.listPaneWidth(containerWidth)
            val paneWidth = fold?.bounds?.left ?: (draggedWidth?.dp ?: baseWidth).coerceIn(
                AdaptiveDefaults.ListPaneMinWidth,
                (containerWidth - AdaptiveDefaults.DetailPaneMinWidth).coerceAtLeast(AdaptiveDefaults.ListPaneMinWidth),
            )
            Row(modifier = Modifier.fillMaxSize()) {
                WindowClassBox(
                    modifier = Modifier
                        .width(paneWidth)
                        .fillMaxHeight(),
                ) {
                    // Beside the detail, the list keeps its toolbar over its own pane: the detail's
                    // takes the edge column.
                    CompositionLocalProvider(LocalToolbarInline provides true) {
                        holder.SaveableStateProvider(ListKey) { scope.list() }
                    }
                }
                // A hinge hides a strip of the screen: leave it empty rather than draw a divider in it.
                if (fold != null && fold.thickness > 0.dp) Spacer(Modifier.width(fold.thickness)) else VerticalDivider()
                WindowClassBox(modifier = Modifier.weight(1f).fillMaxHeight()) {
                    if (showDetail) {
                        holder.SaveableStateProvider(DetailKey) { scope.detail() }
                    } else if (detailPlaceholder != null) {
                        scope.detailPlaceholder()
                    }
                }
            }
            if (fold == null && resizable && ArecaTheme.window.input == InputMode.Pointer) {
                PaneResizeHandle(
                    paneWidth = paneWidth,
                    onResize = { draggedWidth = it.value },
                    onReset = { draggedWidth = null },
                )
            }
        } else {
            WindowClassBox(modifier = Modifier.fillMaxSize()) {
                if (showDetail) {
                    holder.SaveableStateProvider(DetailKey) { scope.detail() }
                } else {
                    holder.SaveableStateProvider(ListKey) { scope.list() }
                }
            }
        }
    }
}

/** Keys the panes' saved state is held under. */
private const val ListKey = "areca.listDetail.list"
private const val DetailKey = "areca.listDetail.detail"

/**
 * The draggable strip over the divider between the panes, centred on the list's edge.
 *
 * @param paneWidth Current width of the list pane.
 * @param onResize Called with the list's new width as the strip is dragged.
 * @param onReset Called on a double-click, to put the width back.
 */
@Composable
private fun PaneResizeHandle(paneWidth: Dp, onResize: (Dp) -> Unit, onReset: () -> Unit) {
    val source = remember { MutableInteractionSource() }
    val hovered by source.collectIsHoveredAsState()
    val dragged by source.collectIsDraggedAsState()
    val density = LocalDensity.current
    // The drag works in pixels on screen; the width grows towards the end edge, which is the left
    // one right to left.
    val direction = if (LocalLayoutDirection.current == LayoutDirection.Rtl) -1f else 1f
    val currentWidth by rememberUpdatedState(paneWidth)
    val currentOnResize by rememberUpdatedState(onResize)
    val currentOnReset by rememberUpdatedState(onReset)
    val dragState = rememberDraggableState { delta ->
        currentOnResize(currentWidth + with(density) { (delta * direction).toDp() })
    }
    Box(
        modifier = Modifier
            .offset(x = paneWidth - ResizeHandleWidth / 2)
            .width(ResizeHandleWidth)
            .fillMaxHeight()
            .resizeCursor()
            .hoverable(source)
            .draggable(dragState, Orientation.Horizontal, interactionSource = source)
            .pointerInput(Unit) { detectTapGestures(onDoubleTap = { currentOnReset() }) },
        contentAlignment = Alignment.Center,
    ) {
        if (hovered || dragged) {
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .fillMaxHeight()
                    .background(ArecaTheme.colors.accentBase),
            )
        }
    }
}

/** Width of the strip a pointer grabs to resize the panes, centred on the divider. */
private val ResizeHandleWidth = 8.dp

private class ListDetailScopeImpl(override val isTwoPane: Boolean) : ListDetailScope
