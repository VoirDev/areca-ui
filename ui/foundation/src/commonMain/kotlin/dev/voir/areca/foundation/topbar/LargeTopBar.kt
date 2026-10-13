package dev.voir.areca.foundation.topbar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

/**
 * A heading bar that opens with the screen's name spelled out and trades it for content.
 *
 * Two bands: a compact row of controls that never leaves, and a headline beneath it that scrolls
 * away as the content comes up. While the headline shrinks, the same name fades in on the compact
 * row, so the screen is never without its title — only ever without the large version of it.
 *
 * Hand it a state from [rememberTopBarScrollState] with
 * [TopBarScrollBehavior.ExitUntilCollapsed] and attach that state's connection to the scrolling
 * content. Any other behavior leaves the headline where it is: [TopBarScrollBehavior.Pinned] gives
 * a static large header, and [TopBarScrollBehavior.EnterAlways] slides the whole bar, headline and
 * all, out of the way like any other bar.
 *
 * Under [TopBarScrollBehavior.ExitUntilCollapsed] the bar reports the headline's height as the
 * state's travel, so the collapse ends exactly when the headline has gone and the compact row is all
 * that is left — a bar left as tall as [TopBarSize.minHeight], never shorter. Under
 * [TopBarScrollBehavior.EnterAlways] it reports its whole height instead, and only that.
 *
 * The collapse is read in layout and draw, never in composition, so scrolling does not recompose
 * the bar.
 *
 * Both the headline and the compact title are announced as headings, and the compact one is what a
 * screen reader reaches once the headline has gone.
 *
 * @param title The screen's name, drawn large and then small.
 * @param scrollState State of the content scrolling beneath the bar, from
 *   [rememberTopBarScrollState].
 * @param modifier Modifier applied to the bar.
 * @param subtitle Line of detail under the headline. Fades out with it.
 * @param style Background, content, control and rule colors. Defaults to the theme's.
 * @param size Padding, gaps, glyph size, heights and text styles. Defaults to the theme's.
 * @param windowInsets Platform insets the bar pads itself by. None by default.
 * @param leading Controls before the compact title, normally a back control.
 * @param trailing Controls after the compact title.
 * @param bottom Band under the headline, inside the bar. Stays when the headline has gone, which is
 *   what makes a pinned tab strip under a large title work.
 */
@Composable
public fun LargeTopBar(
    title: String,
    scrollState: TopBarScrollState,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    style: TopBarStyle = LocalTopBarStyle.current ?: TopBarStyle.Default,
    size: TopBarSize = LocalTopBarSize.current ?: TopBarSize.Default,
    windowInsets: WindowInsets = WindowInsets(0, 0, 0, 0),
    leading: (@Composable TopBarActionScope.() -> Unit)? = null,
    trailing: (@Composable TopBarActionScope.() -> Unit)? = null,
    bottom: (@Composable () -> Unit)? = null,
) {
    // A lambda rather than a value: the fraction changes on every scrolled pixel, and reading it
    // here would recompose the whole bar each frame. Only the collapsing behavior collapses the
    // headline; under the others the headline stays whole and the compact title stays hidden.
    val collapsed: () -> Float = remember(scrollState) {
        {
            if (scrollState.behavior == TopBarScrollBehavior.ExitUntilCollapsed) {
                scrollState.collapsedFraction
            } else {
                0f
            }
        }
    }

    TopBarSurface(
        modifier = modifier,
        style = style,
        size = size,
        windowInsets = windowInsets,
        scrollState = scrollState,
        bottom = bottom,
    ) {
        TopBar(
            size = size,
            // The compact row is already inside a surface that draws the fill, the rule and the
            // insets. Drawing them again would put a second border across the middle of the bar and
            // pad it away from the status bar twice.
            style = style.copy(
                bg = Color.Transparent,
                border = Color.Transparent,
                scrolledBorder = Color.Transparent,
            ),
            // Centred, because the compact title arrives beside controls the user has been
            // looking at since the screen opened — sliding in off to one side would read as a
            // different title rather than the same one.
            titleAlignment = TopBarTitleAlignment.Center,
            leading = leading,
            trailing = trailing,
        ) {
            // Faded rather than swapped in, so the compact title arrives as the headline leaves
            // instead of appearing the instant the collapse begins.
            Box(modifier = Modifier.graphicsLayer { alpha = collapsed() }) {
                TopBarTitle.Default(
                    title = title,
                    alignment = TopBarTitleAlignment.Center,
                    style = style,
                    size = size,
                )
            }
        }

        Headline(
            title = title,
            subtitle = subtitle,
            collapsedFraction = collapsed,
            style = style,
            size = size,
            onHeightMeasured = { height ->
                // The surface measures the whole bar under EnterAlways; the headline is the travel
                // only while collapsing. One writer per behavior, so the range never flips between
                // the two heights from frame to frame.
                if (scrollState.behavior == TopBarScrollBehavior.ExitUntilCollapsed) {
                    scrollState.updateCollapseRange(height)
                }
            },
        )
    }
}

/**
 * The band that scrolls away: the screen's name at headline size.
 *
 * Its height is driven rather than animated. The headline is measured at its natural size, then
 * laid out at whatever fraction of that is left and placed so its bottom edge stays on the band's —
 * which is what makes it slide up behind the compact row rather than shrink in place.
 *
 * @param title The screen's name.
 * @param subtitle Line of detail under it, or `null`.
 * @param collapsedFraction Reads `0` fully expanded through `1` fully collapsed. Called only in
 *   layout and draw.
 * @param style Colors the headline takes.
 * @param size Metrics the headline's style and padding come from.
 * @param onHeightMeasured Called with the headline's natural height in pixels, which is how far the
 *   bar may collapse.
 */
@Composable
private fun Headline(
    title: String,
    subtitle: String?,
    collapsedFraction: () -> Float,
    style: TopBarStyle,
    size: TopBarSize,
    onHeightMeasured: (Float) -> Unit,
) {
    val direction = LocalLayoutDirection.current
    val gutter = PaddingValues(
        start = size.padding.calculateStartPadding(direction),
        end = size.padding.calculateEndPadding(direction),
        bottom = size.padding.calculateBottomPadding(),
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clipToBounds()
            .layout { measurable, constraints ->
                val placeable = measurable.measure(constraints)
                val visible = (placeable.height * (1f - collapsedFraction()))
                    .roundToInt()
                    .coerceIn(0, placeable.height)

                layout(placeable.width, visible) {
                    placeable.place(x = 0, y = visible - placeable.height)
                }
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .onSizeChanged { onHeightMeasured(it.height.toFloat()) }
                // Fades out over the first half of the collapse, so the headline is gone before it
                // reaches the compact row rather than sliding through it at half opacity.
                .graphicsLayer { alpha = (1f - collapsedFraction() * 2f).coerceIn(0f, 1f) }
                // What is left of the bar's expanded height once the compact row has taken its
                // share, so a one-word title opens the screen as tall as a long one does.
                .heightIn(min = (size.largeMinHeight - size.minHeight).coerceAtLeast(0.dp))
                .padding(gutter)
                .semantics(mergeDescendants = true) { heading() }
        ) {
            TopBarTitle.Default(
                title = title,
                subtitle = subtitle,
                alignment = TopBarTitleAlignment.Start,
                style = style,
                size = size.copy(textStyle = size.largeTextStyle),
            )
        }
    }
}
