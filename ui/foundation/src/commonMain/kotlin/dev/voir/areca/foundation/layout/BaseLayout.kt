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
import dev.voir.areca.theme.ArecaTheme
import androidx.compose.ui.graphics.takeOrElse
import dev.voir.areca.theme.ProvideSurface
import dev.voir.areca.theme.SurfaceLevel
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import dev.voir.areca.foundation.feedback.ToastHost
import dev.voir.areca.foundation.feedback.ToastState
import dev.voir.areca.theme.SurfaceOf

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
 * A [floatingAction] is measured with the bars: it sits at the end edge above the bottom bar (or
 * above the navigation bar when there is none), and its height joins the bottom padding, so the last
 * item scrolls clear of it. A [toastState]'s toasts rise above both. [overlay] is composed outside
 * the measuring pass and cannot see the bar heights; keep it for what floats over everything.
 *
 * Bars narrower than the frame sit at its start edge, which is the right edge in a right-to-left
 * layout.
 *
 * @param modifier Modifier applied to the whole frame.
 * @param bg Fill behind [background]. [Color.Unspecified], the default, is the page of the theme's
 *   surface layout — grey for [dev.voir.areca.theme.SurfaceLayout.Grouped], white for `Plain`;
 *   the sheet's white inside a sheet — so a screen is opaque: one screen never shows through
 *   another while navigation slides them over each other (the iOS back swipe). Pass
 *   `Color.Transparent` only for a frame drawn over something meant to show through. Everything in
 *   the frame is drawn on the page surface: a card in it takes the page's raised fill.
 * @param contentWindowInsets Platform insets — status bar, navigation bar, keyboard — folded into
 *   the padding [content] receives. Defaults to none, so a caller opts into insets deliberately.
 *   The left and right insets arrive as start and end padding for the current layout direction.
 * @param topBar Bar pinned to the top edge. Its measured height becomes top padding.
 * @param bottomBar Bar pinned to the bottom edge. Its measured height becomes bottom padding.
 * @param background Decorative layer behind everything: a gradient, an image, a video.
 * @param overlay Highest layer, above the bars. Position its children with `Modifier.align`.
 * @param floatingAction The screen's floating primary action — a `Button` with a leading glyph —
 *   at the end edge above the bottom bar. `null` for none.
 * @param toastState Toasts shown above the bottom bar and the floating action. `null` for none;
 *   a screen that draws its own `ToastHost` passes none.
 * @param content The screen, handed the padding the bars, the floating action and the insets take
 *   up.
 */
@Composable
public fun BaseLayout(
    modifier: Modifier = Modifier,
    bg: Color = Color.Unspecified,
    contentWindowInsets: WindowInsets = WindowInsets(0, 0, 0, 0),
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    background: @Composable BoxScope.() -> Unit = {},
    overlay: @Composable BoxScope.() -> Unit = {},
    floatingAction: (@Composable () -> Unit)? = null,
    toastState: ToastState? = null,
    content: @Composable (PaddingValues) -> Unit,
): Unit = ProvideSurface(SurfaceLevel.Page) {
    val pageBg = bg.takeOrElse { ArecaTheme.surface.ground }
    // A page given another ground — white in a grouped layout — is laid out as the step of the
    // ladder that ground is, so its top bar and cards follow it instead of the default page.
    SurfaceOf(pageBg) {
    Box(modifier = modifier.fillMaxSize().background(pageBg)) {
        // Background — painted first, below everything
        background()

        val floatingActionMargin = ArecaTheme.spacing.xl
        val actionSafeInsets = WindowInsets.safeDrawing
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

            // The floating action clears the bottom bar, or the navigation bar when there is none.
            val actionPlaceables = if (floatingAction == null) {
                emptyList()
            } else {
                subcompose(SlotId.FloatingAction, floatingAction).map { it.measure(looseConstraints) }
            }
            val actionHeight = actionPlaceables.maxOfOrNull { it.height } ?: 0
            val actionMargin = floatingActionMargin.roundToPx()
            val actionBottom = maxOf(bottomHeight, actionSafeInsets.getBottom(this)) + actionMargin

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
                // With a floating action, the content clears it wherever it sits — above the
                // bottom bar, or above the navigation bar when there is none.
                bottom = if (actionHeight > 0) {
                    maxOf(bottomHeight + insetsBottom, actionBottom + actionHeight).toDp()
                } else {
                    (bottomHeight + insetsBottom).toDp()
                },
            )

            // Content fills the full window; it applies innerPadding itself
            val contentPlaceables = subcompose(SlotId.Content) {
                content(innerPadding)
            }.map { it.measure(constraints) }

            // Toasts rise above whatever covers the bottom edge.
            val toastPlaceables = if (toastState == null) {
                emptyList()
            } else {
                val toastBottom = (if (actionHeight > 0) actionBottom + actionHeight else bottomHeight).toDp()
                subcompose(SlotId.Toast) {
                    Box(Modifier.fillMaxSize().padding(bottom = toastBottom)) {
                        ToastHost(
                            state = toastState,
                            windowInsets = if (toastBottom.value > 0f) {
                                WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal)
                            } else {
                                WindowInsets.safeDrawing
                            },
                        )
                    }
                }.map { it.measure(constraints) }
            }

            val actionEndInset = maxOf(
                horizontalInsets.end,
                startEndInsets(
                    left = actionSafeInsets.getLeft(this, layoutDirection),
                    right = actionSafeInsets.getRight(this, layoutDirection),
                    layoutDirection = layoutDirection,
                ).end,
            )

            layout(layoutW, layoutH) {
                // Paint order: content → top → bottom → floating action → toasts
                contentPlaceables.forEach { it.placeRelative(0, 0) }
                topPlaceables.forEach { it.placeRelative(0, 0) }
                bottomPlaceables.forEach { it.placeRelative(0, layoutH - it.height) }
                actionPlaceables.forEach {
                    it.placeRelative(
                        x = layoutW - it.width - actionMargin - actionEndInset,
                        y = layoutH - actionBottom - it.height,
                    )
                }
                toastPlaceables.forEach { it.placeRelative(0, 0) }
            }
        }

        // Overlay — highest z-order. Caller positions via Modifier.align(...)
        overlay()
    }
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

    /** The floating action. */
    FloatingAction,

    /** The toast host. */
    Toast,
}
