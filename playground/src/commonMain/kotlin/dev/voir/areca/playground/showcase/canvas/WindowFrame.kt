package dev.voir.areca.playground.showcase.canvas

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.ArecaThemeProvider
import dev.voir.areca.window.FoldOrientation
import dev.voir.areca.window.FoldState
import dev.voir.areca.window.InputMode
import dev.voir.areca.window.ProvideWindowClass
import dev.voir.areca.window.WindowClass
import dev.voir.areca.window.WindowFold

/**
 * Named windows an adaptive screen is checked at.
 *
 * The windows a layout decision actually changes between, not a slider: each one sits in a
 * different width or height class, or is the awkward case inside one — the near-square unfolded
 * foldable, the phone on its side, the desktop window dragged narrow. Sizes are the devices'
 * logical sizes, rounded.
 *
 * @property label Name shown in the window picker.
 * @property width Logical width in density-independent pixels.
 * @property height Logical height in density-independent pixels.
 * @property input What a person presses this window's controls with.
 * @property topInset Height a status bar takes along the top edge.
 * @property bottomInset Height a home indicator or navigation bar takes along the bottom edge.
 * @property fold Where a foldable's fold runs across the window, or `null` for none.
 */
enum class WindowPreset(
    val label: String,
    val width: Dp,
    val height: Dp,
    val input: InputMode,
    val topInset: Dp = 0.dp,
    val bottomInset: Dp = 0.dp,
    val fold: WindowFold? = null,
) {
    /** An ordinary phone held upright. */
    Phone("Phone", 393.dp, 852.dp, InputMode.Touch, 54.dp, 34.dp),

    /** The same phone on its side: wide enough for two panes, too short for two bars. */
    PhoneLandscape("Phone landscape", 852.dp, 393.dp, InputMode.Touch, bottomInset = 21.dp),

    /** A book-style foldable, folded: the narrowest phone people carry. */
    FoldFolded("Fold, folded", 344.dp, 882.dp, InputMode.Touch, 36.dp, 20.dp),

    /** The same foldable opened flat: nearly square, the hardest window to lay out, a fold down its middle. */
    FoldUnfolded("Fold, unfolded", 690.dp, 830.dp, InputMode.Touch, 32.dp, 20.dp, WindowFold.vertical(at = 345.dp, height = 830.dp)),

    /** The same foldable on its side, propped half open on a table: a fold across its middle. */
    FoldTabletop(
        "Fold, tabletop",
        830.dp,
        690.dp,
        InputMode.Touch,
        24.dp,
        20.dp,
        WindowFold.horizontal(at = 345.dp, width = 830.dp, state = FoldState.HalfOpened),
    ),

    /**
     * A wider foldable opened flat and turned on its side: expanded and landscape, yet nearly square,
     * so still held from below.
     */
    FoldSideways(
        "Fold, on its side",
        841.dp,
        701.dp,
        InputMode.Touch,
        24.dp,
        20.dp,
        WindowFold.horizontal(at = 350.dp, width = 841.dp, state = FoldState.Flat),
    ),

    /** An 11-inch tablet held upright. */
    Tablet("Tablet", 820.dp, 1180.dp, InputMode.Touch, 24.dp, 20.dp),

    /** A 13-inch tablet held upright: as wide as a small tablet on its side, still held from below. */
    LargeTablet("13\" tablet", 1032.dp, 1376.dp, InputMode.Touch, 24.dp, 20.dp),

    /** The same tablet on its side. */
    TabletLandscape("Tablet landscape", 1180.dp, 820.dp, InputMode.Touch, 24.dp, 20.dp),

    /** A 13-inch tablet on its side: desktop width, still touched. */
    LargeTabletLandscape("13\" tablet landscape", 1376.dp, 1032.dp, InputMode.Touch, 24.dp, 20.dp),

    /** A desktop window dragged narrow: a phone's width under a pointer. */
    NarrowWindow("Narrow window", 420.dp, 760.dp, InputMode.Pointer),

    /** A laptop screen. */
    Laptop("Laptop", 1440.dp, 900.dp, InputMode.Pointer),

    /** A desktop monitor. */
    Desktop("Desktop", 1920.dp, 1080.dp, InputMode.Pointer),
}

/**
 * The window insets a window of this shape would report.
 *
 * @return Insets matching this window's status bar and home indicator.
 */
val WindowPreset.windowInsets: WindowInsets
    get() = WindowInsets(top = topInset, bottom = bottomInset)

/**
 * A window of a given size to render an adaptive screen inside, reporting that size as its class.
 *
 * [DeviceFrame] is for phone screens; this is for screens that change with the window. The screen
 * lays out at the preset's real size, reads the preset's [WindowClass] through `ArecaTheme.window`
 * and is sized for [input], and the drawn result is scaled down to fit the width it is given and
 * [maxHeight] — never up.
 *
 * Chrome, so it is built from theme tokens and raw Compose only. See `SHOWCASES.md` §5.
 *
 * @param preset Window to render at.
 * @param modifier Modifier applied to the frame.
 * @param input Input mode the screen reads. Defaults to the preset's.
 * @param maxHeight Tallest the drawn frame may be.
 * @param showInsetGuides Whether to tint the regions system bars would occupy, and draw the
 *   preset's fold as a line.
 * @param content The screen.
 */
@Composable
fun WindowFrame(
    preset: WindowPreset,
    modifier: Modifier = Modifier,
    input: InputMode = preset.input,
    maxHeight: Dp = 560.dp,
    showInsetGuides: Boolean = true,
    content: @Composable () -> Unit,
) {
    val shape = RoundedCornerShape(if (preset.input == InputMode.Pointer) ArecaTheme.radius.md else ArecaTheme.radius.xl2)
    val windowClass = WindowClass(DpSize(preset.width, preset.height), input, preset.fold)

    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val scale = minOf(1f, maxWidth / preset.width, maxHeight / preset.height)

        // As in DeviceFrame: the outer box takes the scaled footprint, the inner one lays out at
        // the real size, unbounded, and is drawn down into it.
        Box(modifier = Modifier.size(width = preset.width * scale, height = preset.height * scale)) {
            Box(
                modifier = Modifier
                    .wrapContentSize(align = Alignment.TopStart, unbounded = true)
                    .requiredSize(width = preset.width, height = preset.height)
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        transformOrigin = TransformOrigin(0f, 0f)
                    }
                    .clip(shape)
                    .background(ArecaTheme.colors.bgCanvas)
                    .border(1.dp, ArecaTheme.colors.borderStrong, shape),
            ) {
                ProvideWindowClass(windowClass) { ProvideFrameInput(input, content) }

                if (showInsetGuides) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .fillMaxWidth()
                            .height(preset.topInset)
                            .background(ArecaTheme.colors.bgContrast.copy(alpha = 0.06f)),
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .height(preset.bottomInset)
                            .background(ArecaTheme.colors.bgContrast.copy(alpha = 0.06f)),
                    )
                    val fold = preset.fold
                    if (fold != null) {
                        // The fold as a soft line: where it runs, not a hinge that hides anything.
                        val line = ArecaTheme.colors.bgContrast.copy(alpha = 0.12f)
                        Box(
                            modifier = if (fold.orientation == FoldOrientation.Vertical) {
                                Modifier.offset(x = fold.bounds.left).width(1.dp).fillMaxHeight().background(line)
                            } else {
                                Modifier.offset(y = fold.bounds.top).height(1.dp).fillMaxWidth().background(line)
                            },
                        )
                    }
                }
            }
        }
    }
}

/**
 * Draws [content] sized for [input]: in the page's own theme when its sizing already suits [input],
 * otherwise in a nested provider whose sizing and type follow [input], as a device would draw it.
 *
 * Like the preview canvas, the nested provider starts from the library's default tokens.
 *
 * @param input Input the content is sized for.
 * @param content Content to draw.
 */
@Composable
internal fun ProvideFrameInput(input: InputMode, content: @Composable () -> Unit) {
    if (ArecaTheme.sizing.dense == (input == InputMode.Pointer)) {
        content()
    } else {
        ArecaThemeProvider(darkTheme = ArecaTheme.isDark, inputMode = input, content = content)
    }
}
