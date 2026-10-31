package dev.voir.areca.foundation.adaptive

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.coerceAtLeast
import dev.voir.areca.foundation.layout.VerticalScrollbarOverlay
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.window.WidthClass

/**
 * A scrolling grid whose column count follows the width it is given.
 *
 * As many columns as fit cells of at least [minCellWidth], up to [maxColumns]; at least one. The
 * grid itself scrolls the full width, but its cells stop growing at [maxWidth] and sit centred, with
 * the screen gutter for the width class at the sides — two columns of products on a phone, four on a
 * tablet, six on a desktop, never a row stretched across a monitor.
 *
 * ```kotlin
 * AdaptiveGrid(modifier = Modifier.fillMaxSize(), minCellWidth = 150.dp) {
 *     items(perfumes, key = { it.id }) { PerfumeCard(it) }
 * }
 * ```
 *
 * @param modifier Modifier applied to the grid.
 * @param minCellWidth Narrowest a cell may be before a column is dropped.
 * @param maxColumns Most columns the grid lays out.
 * @param maxWidth Widest the cells grow together, gutters excluded.
 * @param state Scroll state of the grid.
 * @param contentPadding Padding around the cells, added to the gutters at the sides.
 * @param horizontalSpacing Space between columns.
 * @param verticalSpacing Space between rows.
 * @param content The grid's items.
 */
@Composable
public fun AdaptiveGrid(
    modifier: Modifier = Modifier,
    minCellWidth: Dp = AdaptiveDefaults.GridMinCellWidth,
    maxColumns: Int = Int.MAX_VALUE,
    maxWidth: Dp = AdaptiveDefaults.ContentWidth,
    state: LazyGridState = rememberLazyGridState(),
    contentPadding: PaddingValues = PaddingValues(),
    horizontalSpacing: Dp = ArecaTheme.spacing.lg,
    verticalSpacing: Dp = ArecaTheme.spacing.xl,
    content: LazyGridScope.() -> Unit,
) {
    val spacing = ArecaTheme.spacing
    val layoutDirection = LocalLayoutDirection.current
    BoxWithConstraints(modifier = modifier) {
        val gutter = WidthClass.of(this.maxWidth).select(
            compact = spacing.horizontalContentInsets,
            medium = spacing.mediumHorizontalContentInsets,
            expanded = spacing.expandedHorizontalContentInsets,
        )
        // Whatever the cells cannot use past maxWidth goes to the sides, so they stay centred.
        val side = maxOf(gutter, (this.maxWidth - maxWidth) / 2)
        val available = (this.maxWidth - side * 2).coerceAtLeast(minCellWidth)
        val columns = ((available + horizontalSpacing) / (minCellWidth + horizontalSpacing)).toInt()
            .coerceIn(1, maxColumns.coerceAtLeast(1))

        LazyVerticalGrid(
            columns = GridCells.Fixed(columns),
            state = state,
            contentPadding = PaddingValues(
                start = side + contentPadding.calculateStartPadding(layoutDirection),
                end = side + contentPadding.calculateEndPadding(layoutDirection),
                top = contentPadding.calculateTopPadding(),
                bottom = contentPadding.calculateBottomPadding(),
            ),
            horizontalArrangement = Arrangement.spacedBy(horizontalSpacing),
            verticalArrangement = Arrangement.spacedBy(verticalSpacing),
            content = content,
        )
        VerticalScrollbarOverlay(state)
    }
}
