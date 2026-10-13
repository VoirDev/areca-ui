package dev.voir.areca.playground.showcase.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.voir.areca.playground.showcase.chrome.ChromeSegmented
import dev.voir.areca.playground.showcase.chrome.ChromeText
import dev.voir.areca.playground.showcase.chrome.ChromeTextRole
import dev.voir.areca.playground.showcase.chrome.ChromeTone
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.ArecaThemeProvider

/**
 * Whether the playground shell is currently rendering in the dark theme.
 *
 * Provided by the app shell; read by [PreviewCanvas] to resolve [PreviewTheme.Inherit].
 */
val LocalPlaygroundDarkTheme = staticCompositionLocalOf { false }

/**
 * Backgrounds a preview can be placed on.
 *
 * A component that looks correct on one background is not necessarily correct on the others, which
 * is the entire reason this is switchable: elevation, transparency and border treatments only reveal
 * themselves against the right ground.
 */
enum class PreviewBackground {
    /** The surface a card or sheet would sit on. */
    Surface,

    /** The page background. */
    Canvas,

    /** The muted background used for grouped or inset regions. */
    Subtle,

    /** The inverted surface, for checking content that must survive on it. */
    Contrast,

    /** Alternating squares, for checking transparency and shadow spill. */
    Checkerboard,
}

/**
 * Theme a preview renders in, independent of the shell around it.
 */
enum class PreviewTheme {
    /** Follow the shell. */
    Inherit,
    Light,
    Dark,
}

/**
 * Presentation state for one preview canvas.
 *
 * @param initialBackground Background the canvas starts on.
 * @param initialTheme Theme the canvas starts in.
 */
@Stable
class PreviewCanvasState(
    initialBackground: PreviewBackground = PreviewBackground.Surface,
    initialTheme: PreviewTheme = PreviewTheme.Inherit,
) {
    var background by mutableStateOf(initialBackground)
    var theme by mutableStateOf(initialTheme)
}

/**
 * Remembers a [PreviewCanvasState] across recomposition and configuration changes.
 *
 * @param initialBackground Background the canvas starts on.
 * @param initialTheme Theme the canvas starts in.
 */
@Composable
fun rememberPreviewCanvasState(
    initialBackground: PreviewBackground = PreviewBackground.Surface,
    initialTheme: PreviewTheme = PreviewTheme.Inherit,
): PreviewCanvasState = rememberSaveable(
    saver = androidx.compose.runtime.saveable.listSaver(
        save = { listOf(it.background.name, it.theme.name) },
        restore = {
            PreviewCanvasState(
                initialBackground = PreviewBackground.valueOf(it[0]),
                initialTheme = PreviewTheme.valueOf(it[1]),
            )
        },
    ),
) {
    PreviewCanvasState(initialBackground, initialTheme)
}

/**
 * The surface every preview, variant and example renders inside.
 *
 * Owns background, theme override, padding and the resting border, so a page author passes content
 * and nothing else. Pages must not wrap previews in their own surface — see `SHOWCASES.md` §7.
 *
 * @param modifier Modifier applied to the canvas.
 * @param state Background and theme selection.
 * @param showToolbar Whether to show the background and theme switches above the canvas.
 * @param contentAlignment How content is placed inside the canvas.
 * @param content Preview content, rendered in the selected theme.
 */
@Composable
fun PreviewCanvas(
    modifier: Modifier = Modifier,
    state: PreviewCanvasState = rememberPreviewCanvasState(),
    showToolbar: Boolean = true,
    contentAlignment: Alignment = Alignment.CenterStart,
    content: @Composable () -> Unit,
) {
    val dark = when (state.theme) {
        PreviewTheme.Inherit -> LocalPlaygroundDarkTheme.current
        PreviewTheme.Light -> false
        PreviewTheme.Dark -> true
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
    ) {
        if (showToolbar) {
            PreviewCanvasToolbar(state = state)
        }

        // Inside the override, so the canvas ground and border belong to the theme being previewed.
        ArecaThemeProvider(darkTheme = dark) {
            val shape = RoundedCornerShape(ArecaTheme.radius.md)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clip(shape)
                    .then(
                        if (state.background == PreviewBackground.Checkerboard) {
                            Modifier
                        } else {
                            Modifier.background(state.background.color, shape)
                        }
                    )
                    .border(1.dp, ArecaTheme.colors.borderBase, shape),
            ) {
                if (state.background == PreviewBackground.Checkerboard) {
                    Checkerboard(modifier = Modifier.matchParentSize())
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(ArecaTheme.spacing.xl3),
                    contentAlignment = contentAlignment,
                ) {
                    content()
                }
            }
        }
    }
}

/**
 * The background and theme switches for a canvas.
 *
 * Public so a section holding several canvases — a variant grid, a state list — can show one shared
 * toolbar instead of repeating it above every cell.
 *
 * @param state Selection the switches read and write.
 * @param modifier Modifier applied to the toolbar row.
 * @param showBackground Whether to offer the background switch. `false` for a preview that paints
 *   its own ground — a whole screen — where the choice would change nothing visible.
 */
@Composable
fun PreviewCanvasToolbar(
    state: PreviewCanvasState,
    modifier: Modifier = Modifier,
    showBackground: Boolean = true,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (showBackground) {
            ChromeText(
                text = "Canvas",
                role = ChromeTextRole.Detail,
                tone = ChromeTone.Muted,
            )
            ChromeSegmented(
                options = PreviewBackground.entries,
                selected = state.background,
                onSelect = { state.background = it },
                label = { it.label },
            )
        }

        Box(modifier = Modifier.weight(1f))

        ChromeSegmented(
            options = PreviewTheme.entries,
            selected = state.theme,
            onSelect = { state.theme = it },
            label = { it.name },
        )
    }
}

@Composable
private fun Checkerboard(modifier: Modifier = Modifier) {
    val light = ArecaTheme.colors.bgSurface
    val dark = ArecaTheme.colors.bgMuted
    val cell = with(androidx.compose.ui.platform.LocalDensity.current) { 8.dp.toPx() }

    Canvas(modifier = modifier.fillMaxSize()) {
        drawRect(color = light)
        var y = 0f
        var row = 0
        while (y < size.height) {
            var x = if (row % 2 == 0) 0f else cell
            while (x < size.width) {
                drawRect(
                    color = dark,
                    topLeft = Offset(x, y),
                    size = Size(
                        width = minOf(cell, size.width - x),
                        height = minOf(cell, size.height - y),
                    ),
                )
                x += cell * 2
            }
            y += cell
            row++
        }
    }
}

private val PreviewBackground.label: String
    get() = when (this) {
        PreviewBackground.Surface -> "Surface"
        PreviewBackground.Canvas -> "Canvas"
        PreviewBackground.Subtle -> "Subtle"
        PreviewBackground.Contrast -> "Contrast"
        PreviewBackground.Checkerboard -> "Checker"
    }

private val PreviewBackground.color: Color
    @Composable
    @ReadOnlyComposable
    get() = when (this) {
        PreviewBackground.Surface -> ArecaTheme.colors.bgSurface
        PreviewBackground.Canvas -> ArecaTheme.colors.bgCanvas
        PreviewBackground.Subtle -> ArecaTheme.colors.bgSubtle
        PreviewBackground.Contrast -> ArecaTheme.colors.bgContrast
        PreviewBackground.Checkerboard -> Color.Transparent
    }
