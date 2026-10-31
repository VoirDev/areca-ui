package dev.voir.areca.playground.showcase.canvas

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.window.InputMode
import dev.voir.areca.window.ProvideWindowClass
import dev.voir.areca.window.WindowClass

/**
 * Phone sizes a screen showcase can be checked at.
 *
 * Three, not a slider: the questions a screen has to answer are "does the form fit on the smallest
 * phone people still carry", "does it look right on an ordinary one" and "does the footer still
 * reach the bottom on a tall one". A continuous width invites fiddling and answers none of them.
 *
 * @property label Name shown in the size picker.
 * @property width Logical width in density-independent pixels.
 * @property height Logical height in density-independent pixels.
 * @property topInset Height the status bar would occupy on a device this shape.
 * @property bottomInset Height the home indicator or navigation bar would occupy.
 */
enum class DeviceSize(
    val label: String,
    val width: Dp,
    val height: Dp,
    val topInset: Dp,
    val bottomInset: Dp,
) {
    /** A small, short phone — the one a long form fails on first. */
    Compact("Compact", 360.dp, 640.dp, 24.dp, 16.dp),

    /** The ordinary modern phone. */
    Regular("Regular", 390.dp, 844.dp, 47.dp, 34.dp),

    /** A large phone, where a bottom-pinned footer has the furthest to travel. */
    Tall("Tall", 430.dp, 932.dp, 59.dp, 34.dp),
}

/**
 * The window insets a device of this shape would report.
 *
 * Screens read `WindowInsets.systemBars`, which on the desktop playground is zero, so a screen
 * previewed here would sit flush against the frame's edges and hide exactly the padding bug the
 * preview exists to catch. Parts that accept their insets take this instead.
 *
 * @return Insets matching this device's status bar and home indicator.
 */
val DeviceSize.windowInsets: WindowInsets
    get() = WindowInsets(top = topInset, bottom = bottomInset)

/**
 * Factor a device frame draws itself at when its caller does not say.
 *
 * Provided by the page container, which is the only thing that knows how much height the window
 * has. A page that picked its own would be wrong on every window but the author's.
 */
val LocalDeviceScale = compositionLocalOf { 1f }

/**
 * A phone-shaped window to render a whole screen inside.
 *
 * Screens fill the viewport they are given, which in a showcase page would be the whole browser or
 * desktop window — a shape no phone has. This constrains one to a real device's proportions so that
 * what a reader judges is what a person would see.
 *
 * Chrome, so it is built from theme tokens and raw Compose only, with no Areca component in it. A
 * frame made of `Card` would stop drawing the day `Card` broke, which is the day you most need to
 * look at a screen. See `SHOWCASES.md` §5.
 *
 * The screen always lays out at the device's real dimensions. [scale] shrinks the drawn result
 * afterwards, so a row of phones fits in a variant cell without any of them reflowing into a shape
 * no phone has — which would make the comparison meaningless. The screen reads the phone's size as
 * its window class, under touch. A screen that changes with the window is checked in [WindowFrame].
 *
 * @param modifier Modifier applied to the frame.
 * @param size Device shape to render at.
 * @param scale Factor the rendered frame is drawn at. Layout is unaffected; only the pixels
 *   shrink. Defaults to whatever fit the page container worked out.
 * @param showInsetGuides Whether to tint the status-bar and home-indicator regions, so a reader can
 *   see whether content is correctly clear of them.
 * @param content The screen.
 */
@Composable
fun DeviceFrame(
    modifier: Modifier = Modifier,
    size: DeviceSize = DeviceSize.Regular,
    scale: Float = LocalDeviceScale.current,
    showInsetGuides: Boolean = true,
    content: @Composable () -> Unit,
) {
    val shape = RoundedCornerShape(ArecaTheme.radius.xl2)

    // The outer box takes the space the scaled result occupies; the inner one measures at full
    // device size and is drawn down into it. `size` alone would be clamped to the outer box, laying
    // the screen out at the scaled size and then shrinking it a second time — so the inner box is
    // unbounded, pinned to the top-left corner the scale shrinks towards.
    Box(modifier = modifier.size(width = size.width * scale, height = size.height * scale)) {
        Box(
            modifier = Modifier
                .wrapContentSize(align = Alignment.TopStart, unbounded = true)
                .requiredSize(width = size.width, height = size.height)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    transformOrigin = TransformOrigin(0f, 0f)
                }
                .clip(shape)
                .background(ArecaTheme.colors.bgCanvas)
                .border(1.dp, ArecaTheme.colors.borderStrong, shape),
        ) {
            // The screen asks the window class what it is drawn in, and the answer is this phone,
            // not the desktop window the playground runs in — touched, whatever the page is sized for.
            ProvideWindowClass(WindowClass(DpSize(size.width, size.height), InputMode.Touch)) {
                ProvideFrameInput(InputMode.Touch, content)
            }

            if (showInsetGuides) {
                InsetGuide(
                    height = size.topInset,
                    modifier = Modifier.align(Alignment.TopCenter),
                )
                InsetGuide(
                    height = size.bottomInset,
                    modifier = Modifier.align(Alignment.BottomCenter),
                )
                HomeIndicator(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = size.bottomInset / 3),
                )
            }
        }
    }
}

/**
 * A translucent band marking a region a system bar would occupy.
 *
 * Faint on purpose: it has to be visible enough to show that content is clear of it and quiet
 * enough not to be mistaken for part of the screen being reviewed.
 *
 * @param height Height of the region.
 * @param modifier Modifier applied to the band.
 */
@Composable
private fun InsetGuide(
    height: Dp,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .background(ArecaTheme.colors.bgContrast.copy(alpha = 0.06f)),
    )
}

/**
 * The pill at the bottom edge of a modern phone, drawn so the bottom inset reads as a real thing
 * rather than as empty padding.
 *
 * @param modifier Modifier applied to the pill.
 */
@Composable
private fun HomeIndicator(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .width(134.dp)
            .height(5.dp)
            .clip(ArecaTheme.shapes.full)
            .background(ArecaTheme.colors.fgBase.copy(alpha = 0.3f)),
    )
}
