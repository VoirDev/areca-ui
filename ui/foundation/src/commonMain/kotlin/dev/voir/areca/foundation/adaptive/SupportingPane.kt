package dev.voir.areca.foundation.adaptive

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.layout.VerticalDivider
import dev.voir.areca.window.FoldOrientation
import dev.voir.areca.window.WindowClassBox
import dev.voir.areca.window.rememberFoldView
import dev.voir.areca.window.thickness

/** What the slots of a [SupportingPane] know about how the supporting content is shown. */
@Stable
public interface SupportingPaneScope {
    /**
     * Whether the supporting content is on screen beside the main content. When `false` it opens in
     * a sheet, and the main content should offer the button that opens it.
     */
    public val isSupportingInline: Boolean
}

/**
 * Main content with supporting content beside it when there is room, in a sheet when there is not:
 * filters beside results, reviews beside a product, upcoming payments beside a dashboard.
 *
 * In a container of [AdaptiveDefaults.SupportingPaneMinWidth] or more the supporting content is a
 * pane [AdaptiveDefaults.supportingPaneWidth] wide at the end, always shown, under
 * [supportingHeader]. In a narrower container it opens in an [AdaptiveSheet] while
 * [supportingVisible] — offer the button that sets it when
 * [SupportingPaneScope.isSupportingInline] is `false`. Each part is a [WindowClassBox] and keeps
 * its saved state across the switch. Neither part scrolls on its own: give long supporting content
 * its own scroll, as you would the main content. On a foldable opened along a vertical fold, the
 * pane starts at the fold whenever that leaves the main content and the pane their minimum widths.
 *
 * ```kotlin
 * SupportingPane(
 *     supportingVisible = filtersOpen,
 *     onSupportingDismissRequest = { filtersOpen = false },
 *     supportingHeader = { TopBar("Filters", size = TopBarSize.Compact) },
 *     main = {
 *         ExploreResults(
 *             onOpenFilters = if (isSupportingInline) null else { { filtersOpen = true } },
 *         )
 *     },
 *     supporting = { FilterPanel(…) },
 * )
 * ```
 *
 * @param supportingVisible Whether the sheet is open, when the supporting content is not inline.
 *   Ignored while it is.
 * @param onSupportingDismissRequest Called when the person dismisses the sheet; set
 *   [supportingVisible] to `false` there.
 * @param modifier Modifier applied to the layout.
 * @param supportingHeader Content above the supporting content, in the pane and in the sheet.
 * @param main The main content.
 * @param supporting The supporting content.
 */
@Composable
public fun SupportingPane(
    supportingVisible: Boolean,
    onSupportingDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    supportingHeader: (@Composable () -> Unit)? = null,
    main: @Composable SupportingPaneScope.() -> Unit,
    supporting: @Composable SupportingPaneScope.() -> Unit,
) {
    val holder = rememberSaveableStateHolder()
    val foldView = rememberFoldView()
    BoxWithConstraints(modifier = modifier.then(foldView.modifier)) {
        val containerWidth = maxWidth
        val inline = containerWidth >= AdaptiveDefaults.SupportingPaneMinWidth
        val scope = remember(inline) { SupportingPaneScopeImpl(inline) }
        if (inline) {
            // A vertical fold that leaves the main content and the pane their minimum widths is
            // where they meet.
            val fold = foldView.foldIn(DpSize(maxWidth, maxHeight))
                ?.takeIf { it.orientation == FoldOrientation.Vertical }
                ?.takeIf {
                    it.bounds.left >= AdaptiveDefaults.DetailPaneMinWidth &&
                        containerWidth - it.bounds.right >= AdaptiveDefaults.ListPaneMinWidth
                }
            val paneWidth = if (fold != null) containerWidth - fold.bounds.right else AdaptiveDefaults.supportingPaneWidth(containerWidth)
            Row(modifier = Modifier.fillMaxSize()) {
                WindowClassBox(modifier = Modifier.weight(1f).fillMaxHeight()) {
                    holder.SaveableStateProvider(MainKey) { scope.main() }
                }
                if (fold != null && fold.thickness > 0.dp) Spacer(Modifier.width(fold.thickness)) else VerticalDivider()
                WindowClassBox(
                    modifier = Modifier.width(paneWidth).fillMaxHeight(),
                ) {
                    // The supporting pane keeps its toolbar over itself: the main content's takes the
                    // edge column.
                    CompositionLocalProvider(LocalToolbarInline provides true) {
                        Column {
                            supportingHeader?.invoke()
                            holder.SaveableStateProvider(SupportingKey) { scope.supporting() }
                        }
                    }
                }
            }
        } else {
            WindowClassBox(modifier = Modifier.fillMaxSize()) {
                holder.SaveableStateProvider(MainKey) { scope.main() }
            }
            AdaptiveSheet(
                visible = supportingVisible,
                onDismissRequest = onSupportingDismissRequest,
                header = supportingHeader,
            ) {
                holder.SaveableStateProvider(SupportingKey) { scope.supporting() }
            }
        }
    }
}

/** Keys the two parts' saved state is held under. */
private const val MainKey = "areca.supportingPane.main"
private const val SupportingKey = "areca.supportingPane.supporting"

private class SupportingPaneScopeImpl(override val isSupportingInline: Boolean) : SupportingPaneScope
