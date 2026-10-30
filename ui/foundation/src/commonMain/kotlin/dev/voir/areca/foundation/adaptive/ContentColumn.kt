package dev.voir.areca.foundation.adaptive

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import dev.voir.areca.theme.horizontalContentInsets
import dev.voir.areca.window.WindowClassBox

/**
 * A column of content centred in the space it is given and no wider than [maxWidth].
 *
 * On a phone it is the full width less the screen gutters, as every Areca screen already is; on a
 * tablet or a desktop it stops growing, so a sign-in form or a settings page keeps a readable line
 * length instead of stretching across the window. The column is a [WindowClassBox], so what is
 * inside lays itself out for the column's width.
 *
 * Give [modifier] the column's height and scrolling: `Modifier.fillMaxSize()` to centre a short form
 * with [verticalArrangement], or `Modifier.verticalScroll(…)` for a long page, so the scrollbar sits
 * at the window's edge rather than the column's.
 *
 * ```kotlin
 * ContentColumn(
 *     modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
 *     verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xl),
 * ) {
 *     Heading(title = { Text("Sign in") })
 *     EmailField(…)
 * }
 * ```
 *
 * @param modifier Modifier applied to the full-width area the column is centred in.
 * @param maxWidth Widest the column grows, gutters included. [AdaptiveDefaults.ReadableWidth] for
 *   forms and reading; [AdaptiveDefaults.ContentWidth] for browsing.
 * @param verticalArrangement Vertical arrangement of the children.
 * @param horizontalAlignment Horizontal alignment of the children within the column.
 * @param content The column's children.
 */
@Composable
public fun ContentColumn(
    modifier: Modifier = Modifier,
    maxWidth: Dp = AdaptiveDefaults.ReadableWidth,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable ColumnScope.() -> Unit,
) {
    // Minimum constraints pass through, so a caller's fillMaxSize gives the column the full height
    // to arrange its children in. The minimum width is dropped first, or it would outrank the cap.
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter,
        propagateMinConstraints = true,
    ) {
        WindowClassBox(
            modifier = Modifier.wrapContentWidth().widthIn(max = maxWidth).fillMaxWidth(),
            propagateMinConstraints = true,
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().horizontalContentInsets(),
                verticalArrangement = verticalArrangement,
                horizontalAlignment = horizontalAlignment,
                content = content,
            )
        }
    }
}
