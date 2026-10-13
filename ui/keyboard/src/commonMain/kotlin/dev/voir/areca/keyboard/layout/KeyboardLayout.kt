package dev.voir.areca.keyboard.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import dev.voir.areca.foundation.LocalContentColor
import dev.voir.areca.keyboard.key.KeyboardGrid
import dev.voir.areca.keyboard.key.KeyboardKeys

/**
 * The surface a keyboard is drawn on: a styled panel with optional header and footer slots.
 *
 * Pass this as a [dev.voir.areca.keyboard.KeyboardHost]'s `keyboardContent`. It supplies the fill,
 * shape, elevation, safe-area padding and internal spacing, and leaves what goes inside entirely to
 * the caller — a [dev.voir.areca.keyboard.key.KeyboardGrid] through the grid overload, or anything
 * at all through this one.
 *
 * [header] and [footer] are plain slots with no assumptions about their contents. A header is
 * normally the value being edited — a running total, a formula, the digits entered so far — and a
 * footer a toolbar, a hint, or a suggestion strip.
 *
 * The surface clears the bottom of the system bars and display cutout by default, so its last row is
 * not under Android's gesture pill or an iPhone's home indicator. The platform IME is left out — a
 * keyboard drawn while it is showing or animating away would otherwise pad itself by the IME's
 * height and jump with it. Only the bottom edge is taken: a keyboard spans the full width by design,
 * and padding its sides would leave the surface's fill short of the screen edges.
 *
 * @param modifier Modifier applied to the surface, which fills the available width.
 * @param style Colors, shape, and lift of the surface. Defaults to [LocalKeyboardStyle], or
 *   [KeyboardStyle.Default].
 * @param size Spacing between the surface's edge, its slots, and its rows. Defaults to
 *   [LocalKeyboardSize], or [KeyboardSize.Default].
 * @param windowInsets Insets the surface clears, so its last row is not under a system bar. The fill
 *   still extends beneath them.
 * @param header Content above the keys.
 * @param footer Content below the keys.
 * @param content The keys, or whatever else the keyboard is made of.
 */
@Composable
public fun KeyboardLayout(
    modifier: Modifier = Modifier,
    style: KeyboardStyle = LocalKeyboardStyle.current ?: KeyboardStyle.Default,
    size: KeyboardSize = LocalKeyboardSize.current ?: KeyboardSize.Default,
    windowInsets: WindowInsets = WindowInsets.systemBars
        .union(WindowInsets.displayCutout)
        .only(WindowInsetsSides.Bottom),
    header: (@Composable ColumnScope.() -> Unit)? = null,
    footer: (@Composable ColumnScope.() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(style.elevation, style.shape)
            .background(style.bg, style.shape)
            .border(style.borderWidth, style.border, style.shape)
            .drawBehind {
                val width = style.borderWidth.toPx()
                drawLine(
                    color = style.divider,
                    start = Offset(0f, width / 2),
                    end = Offset(this.size.width, width / 2),
                    strokeWidth = width,
                )
            }
            // A press landing in the gap between two keys must not reach whatever is behind the
            // keyboard — without this it would dismiss a host drawing a tap-to-close scrim.
            .pointerInput(Unit) {
                awaitEachGesture {
                    awaitFirstDown(requireUnconsumed = false).consume()
                }
            }
            // Applied after the fill, so the surface still reaches the screen's edge while its
            // contents stop short of the system bars.
            .windowInsetsPadding(windowInsets)
            .padding(size.padding),
    ) {
        CompositionLocalProvider(LocalContentColor provides style.content) {
            if (header != null) {
                header()
                Spacer(Modifier.height(size.headerGap))
            }

            content()

            if (footer != null) {
                Spacer(Modifier.height(size.footerGap))
                footer()
            }
        }
    }
}

/**
 * The surface a keyboard is drawn on, holding a [KeyboardGrid].
 *
 * The overload to reach for: a keyboard is nearly always a grid of keys inside a styled panel. See
 * the slot-based [KeyboardLayout] for a keyboard that is not a grid.
 *
 * @param grid Keys to draw.
 * @param modifier Modifier applied to the surface, which fills the available width.
 * @param style Colors, shape, and lift of the surface, and the look of its keys. Defaults to
 *   [LocalKeyboardStyle], or [KeyboardStyle.Default].
 * @param size Spacing between the surface's edge, its slots, and its rows, and the metrics of its
 *   keys. Defaults to [LocalKeyboardSize], or [KeyboardSize.Default].
 * @param windowInsets Insets the surface clears, so its last row is not under a system bar. See the
 *   slot-based [KeyboardLayout] for the default.
 * @param header Content above the keys.
 * @param footer Content below the keys.
 */
@Composable
public fun KeyboardLayout(
    grid: KeyboardGrid,
    modifier: Modifier = Modifier,
    style: KeyboardStyle = LocalKeyboardStyle.current ?: KeyboardStyle.Default,
    size: KeyboardSize = LocalKeyboardSize.current ?: KeyboardSize.Default,
    windowInsets: WindowInsets = WindowInsets.systemBars
        .union(WindowInsets.displayCutout)
        .only(WindowInsetsSides.Bottom),
    header: (@Composable ColumnScope.() -> Unit)? = null,
    footer: (@Composable ColumnScope.() -> Unit)? = null,
) {
    KeyboardLayout(
        modifier = modifier,
        style = style,
        size = size,
        windowInsets = windowInsets,
        header = header,
        footer = footer,
    ) {
        KeyboardKeys(
            grid = grid,
            style = style.keys,
            size = size,
        )
    }
}
