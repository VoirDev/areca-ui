package dev.voir.areca.foundation.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.LayoutDirection

/**
 * A screen's frame: a background, bars pinned to the top and bottom edges, and the screen itself.
 *
 * Five layers, drawn bottom to top: [background], [content], [topBar], [bottomBar], [overlay].
 *
 * The bars are measured first, and [content] is handed their combined height as padding — but the
 * content is laid out at the **full window size** and the bars are drawn *over* it. That is
 * deliberate: it is what lets a scrolling screen run edge to edge under a translucent bar while its
 * first and last items still clear them. The consequence is that the padding must be applied by the
 * content, normally as a `LazyColumn`'s `contentPadding`; ignoring it hides content behind the bars.
 *
 * [overlay] is composed outside the measuring pass, so it cannot see the bar heights. Anything that
 * must sit above the bottom bar — a floating action button — needs its own offset rather than
 * reading one from here.
 *
 * Bars narrower than the frame sit at its start edge, which is the right edge in a right-to-left
 * layout.
 *
 * @param modifier Modifier applied to the whole frame.
 * @param bg Fill behind [background], for the common case where no decoration is
 *   needed.
 * @param contentWindowInsets Platform insets — status bar, navigation bar, keyboard — folded into
 *   the padding [content] receives. Defaults to none, so a caller opts into insets deliberately.
 *   The left and right insets arrive as start and end padding for the current layout direction.
 * @param topBar Bar pinned to the top edge. Its measured height becomes top padding.
 * @param bottomBar Bar pinned to the bottom edge. Its measured height becomes bottom padding.
 * @param background Decorative layer behind everything: a gradient, an image, a video.
 * @param overlay Highest layer, above the bars. Position its children with `Modifier.align`.
 * @param content The screen, handed the padding the bars and insets take up.
 */
@Composable
public fun BaseLayout(
    modifier: Modifier = Modifier,
    bg: Color = Color.Transparent,
    contentWindowInsets: WindowInsets = WindowInsets(0, 0, 0, 0),
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    background: @Composable BoxScope.() -> Unit = {},
    overlay: @Composable BoxScope.() -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    Box(modifier = modifier.fillMaxSize().background(bg)) {
        // Background — painted first, below everything
        background()

        // Measured stack: bars sized first, content padded accordingly
        SubcomposeLayout(Modifier.fillMaxSize()) { constraints ->
            val layoutW = constraints.maxWidth
            val layoutH = constraints.maxHeight
            // Loose constraints so bars collapse to their intrinsic height
            val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)

            val topPlaceables = subcompose(SlotId.Top, topBar)
                .map { it.measure(looseConstraints) }
            val bottomPlaceables = subcompose(SlotId.Bottom, bottomBar)
                .map { it.measure(looseConstraints) }

            val topHeight = topPlaceables.maxOfOrNull { it.height } ?: 0
            val bottomHeight = bottomPlaceables.maxOfOrNull { it.height } ?: 0

            // Fold platform insets into the padding the content receives
            val insetsTop = contentWindowInsets.getTop(this)
            val insetsBottom = contentWindowInsets.getBottom(this)
            val horizontalInsets = startEndInsets(
                left = contentWindowInsets.getLeft(this, layoutDirection),
                right = contentWindowInsets.getRight(this, layoutDirection),
                layoutDirection = layoutDirection,
            )

            val innerPadding = PaddingValues(
                start = horizontalInsets.start.toDp(),
                top = (topHeight + insetsTop).toDp(),
                end = horizontalInsets.end.toDp(),
                bottom = (bottomHeight + insetsBottom).toDp(),
            )

            // Content fills the full window; it applies innerPadding itself
            val contentPlaceables = subcompose(SlotId.Content) {
                content(innerPadding)
            }.map { it.measure(constraints) }

            layout(layoutW, layoutH) {
                // Paint order: content → top → bottom (bars overlay content)
                contentPlaceables.forEach { it.placeRelative(0, 0) }
                topPlaceables.forEach { it.placeRelative(0, 0) }
                bottomPlaceables.forEach { it.placeRelative(0, layoutH - it.height) }
            }
        }

        // Overlay — highest z-order. Caller positions via Modifier.align(...)
        overlay()
    }
}

/**
 * Horizontal window insets expressed against the reading direction rather than the screen.
 *
 * @property start Inset on the side a line of text begins, in pixels.
 * @property end Inset on the side a line of text ends, in pixels.
 */
internal data class StartEndInsets(val start: Int, val end: Int)

/**
 * Maps physical left and right insets onto start and end for [layoutDirection].
 *
 * [WindowInsets] reports physical edges, while [PaddingValues] takes start and end. In a
 * right-to-left layout the left edge is the end, so a display cutout on the left must pad the end
 * of the content, not its start.
 *
 * @param left Inset at the physical left edge, in pixels.
 * @param right Inset at the physical right edge, in pixels.
 * @param layoutDirection Direction the content is laid out in.
 * @return The two insets keyed by start and end.
 */
internal fun startEndInsets(left: Int, right: Int, layoutDirection: LayoutDirection): StartEndInsets =
    when (layoutDirection) {
        LayoutDirection.Ltr -> StartEndInsets(start = left, end = right)
        LayoutDirection.Rtl -> StartEndInsets(start = right, end = left)
    }

/**
 * Identifies the three measured slots so `SubcomposeLayout` can keep their subcompositions apart.
 *
 * [background] and [overlay] are not here: they are composed outside the measuring pass, because
 * neither contributes to the padding the content receives.
 */
private enum class SlotId {
    /** The top bar. */
    Top,

    /** The bottom bar. */
    Bottom,

    /** The screen itself. */
    Content,
}
