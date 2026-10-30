package dev.voir.areca.foundation.layout

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.window.InputMode

/**
 * A draggable scrollbar for a vertically scrolling column, drawn only under a pointer.
 *
 * A pointer has no swipe to fling with and no edge to feel, so a desktop window shows where a long
 * page is and lets the person drag or click the track to move through it. Under touch it draws
 * nothing and takes no space: a phone scrolls with the finger. Areca's own scrolling lists and grids
 * draw one already; place one beside a scrolling element of your own, sharing its state, and give it
 * its height.
 *
 * ```kotlin
 * val scroll = rememberScrollState()
 * Box(Modifier.fillMaxSize()) {
 *     Column(Modifier.verticalScroll(scroll)) { … }
 *     VerticalScrollbar(scroll, Modifier.align(Alignment.CenterEnd).fillMaxHeight())
 * }
 * ```
 *
 * @param state Scroll state of the column it follows.
 * @param modifier Modifier applied to the scrollbar; it supplies the height, the width is the
 *   scrollbar's own.
 */
@Composable
public fun VerticalScrollbar(state: ScrollState, modifier: Modifier = Modifier) {
    PointerScrollbar(ScrollbarTarget.Scroll(state), vertical = true, modifier = modifier)
}

/**
 * A draggable scrollbar for a [androidx.compose.foundation.lazy.LazyColumn], drawn only under a
 * pointer. See the [ScrollState] overload.
 *
 * @param state Scroll state of the list it follows.
 * @param modifier Modifier applied to the scrollbar; it supplies the height.
 */
@Composable
public fun VerticalScrollbar(state: LazyListState, modifier: Modifier = Modifier) {
    PointerScrollbar(ScrollbarTarget.LazyList(state), vertical = true, modifier = modifier)
}

/**
 * A draggable scrollbar for a [androidx.compose.foundation.lazy.grid.LazyVerticalGrid], drawn only
 * under a pointer. See the [ScrollState] overload.
 *
 * @param state Scroll state of the grid it follows.
 * @param modifier Modifier applied to the scrollbar; it supplies the height.
 */
@Composable
public fun VerticalScrollbar(state: LazyGridState, modifier: Modifier = Modifier) {
    PointerScrollbar(ScrollbarTarget.LazyGrid(state), vertical = true, modifier = modifier)
}

/**
 * A draggable scrollbar for a horizontally scrolling row, drawn only under a pointer. A wheel
 * scrolls a row only with Shift held, so a wide table or strip needs one on a desktop.
 *
 * @param state Scroll state of the row it follows.
 * @param modifier Modifier applied to the scrollbar; it supplies the width, the height is the
 *   scrollbar's own.
 */
@Composable
public fun HorizontalScrollbar(state: ScrollState, modifier: Modifier = Modifier) {
    PointerScrollbar(ScrollbarTarget.Scroll(state), vertical = false, modifier = modifier)
}

/**
 * A draggable scrollbar for a [androidx.compose.foundation.lazy.LazyRow], drawn only under a
 * pointer. See the [ScrollState] overload.
 *
 * @param state Scroll state of the row it follows.
 * @param modifier Modifier applied to the scrollbar; it supplies the width.
 */
@Composable
public fun HorizontalScrollbar(state: LazyListState, modifier: Modifier = Modifier) {
    PointerScrollbar(ScrollbarTarget.LazyList(state), vertical = false, modifier = modifier)
}

/**
 * Lays a vertical scrollbar over the end edge of the box, inset from it, without changing the box's
 * size. For Areca's own scrolling containers: wrap the lazy list in a `Box` with
 * `propagateMinConstraints = true` and call this after it.
 */
@Composable
internal fun BoxScope.VerticalScrollbarOverlay(state: LazyListState) {
    VerticalScrollbar(state, Modifier.matchParentSize().wrapContentWidth(Alignment.End).padding(ScrollbarInset))
}

/** [VerticalScrollbarOverlay] for a lazy grid. */
@Composable
internal fun BoxScope.VerticalScrollbarOverlay(state: LazyGridState) {
    VerticalScrollbar(state, Modifier.matchParentSize().wrapContentWidth(Alignment.End).padding(ScrollbarInset))
}

/** Lays a horizontal scrollbar over the bottom edge of the box, inset from it. */
@Composable
internal fun BoxScope.HorizontalScrollbarOverlay(state: ScrollState) {
    HorizontalScrollbar(state, Modifier.matchParentSize().wrapContentHeight(Alignment.Bottom).padding(ScrollbarInset))
}

/** Gap between an overlaid scrollbar and the edge it runs along. */
private val ScrollbarInset = 2.dp

/** The scrolling state a scrollbar follows. */
internal sealed interface ScrollbarTarget {
    class Scroll(val state: ScrollState) : ScrollbarTarget
    class LazyList(val state: LazyListState) : ScrollbarTarget
    class LazyGrid(val state: LazyGridState) : ScrollbarTarget
}

/** Draws [PlatformScrollbar] under a pointer and nothing under touch. */
@Composable
private fun PointerScrollbar(target: ScrollbarTarget, vertical: Boolean, modifier: Modifier) {
    if (ArecaTheme.window.input == InputMode.Pointer) {
        PlatformScrollbar(target = target, vertical = vertical, modifier = modifier)
    }
}

/**
 * The platform's scrollbar, styled from the theme. Platforms without a pointer scrollbar draw
 * nothing.
 *
 * @param target State the scrollbar follows.
 * @param vertical Whether it runs top to bottom rather than start to end.
 * @param modifier Modifier applied to the scrollbar.
 */
@Composable
internal expect fun PlatformScrollbar(target: ScrollbarTarget, vertical: Boolean, modifier: Modifier)
