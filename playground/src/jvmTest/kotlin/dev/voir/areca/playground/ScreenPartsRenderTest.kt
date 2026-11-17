package dev.voir.areca.playground

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.feedback.Fallback
import dev.voir.areca.foundation.feedback.FallbackTone
import dev.voir.areca.foundation.feedback.SkeletonBlock
import dev.voir.areca.foundation.feedback.SkeletonGroup
import dev.voir.areca.foundation.feedback.rememberToastState
import dev.voir.areca.foundation.layout.BaseLayout
import dev.voir.areca.foundation.layout.BottomActionBar
import dev.voir.areca.foundation.layout.Card
import dev.voir.areca.foundation.layout.PanelLayout
import dev.voir.areca.foundation.overlay.FlowModalTopBar
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.foundation.topbar.TopBar
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.Plus
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.ArecaThemeProvider
import dev.voir.areca.window.InputMode
import org.jetbrains.skia.EncodedImageFormat
import java.io.File
import kotlin.test.Test

/** Logical width of the device the screens are rendered at. */
private const val WIDTH = 390

/** Logical height of the device the screens are rendered at. */
private const val HEIGHT = 844

/** Pixel density the screens are rasterised at. */
private const val DENSITY = 2f

/** Time between the frames stepped through before the kept one, in nanoseconds. */
private const val FRAME_NANOS = 50_000_000L

/** How long the clock runs before the kept frame, in nanoseconds. */
private const val SETTLE_NANOS = 1_500_000_000L

/** Where the rendered images are written. */
private val OUTPUT = File(System.getProperty("render.out") ?: "build/screens")

/**
 * Renders [content] to a PNG once its animations have settled.
 *
 * @param name File name, without the extension.
 * @param dark Whether to render in the dark theme.
 * @param content The screen.
 */
@OptIn(ExperimentalComposeUiApi::class)
private fun render(name: String, dark: Boolean = false, content: @Composable () -> Unit) {
    val scene = ImageComposeScene(
        width = (WIDTH * DENSITY).toInt(),
        height = (HEIGHT * DENSITY).toInt(),
        density = Density(DENSITY),
    ) {
        ArecaThemeProvider(darkTheme = dark, inputMode = InputMode.Touch) { content() }
    }
    try {
        var time = 0L
        while (time < SETTLE_NANOS) {
            scene.render(time)
            time += FRAME_NANOS
        }
        val data = requireNotNull(scene.render(SETTLE_NANOS).encodeToData(EncodedImageFormat.PNG))
        OUTPUT.mkdirs()
        File(OUTPUT, "$name.png").writeBytes(data.bytes)
    } finally {
        scene.close()
    }
}

/**
 * Renders the screen-level parts to `build/screens/screen-parts-*.png`: a frame with a floating
 * action and a toast above a bottom action bar, the two `Fallback` tones, a panel page, a sheet
 * header and block skeletons.
 *
 * Not an assertion: it fails when a part throws, and it is where the parts are looked at.
 *
 * ```bash
 * ./gradlew :playground:jvmTest --tests '*ScreenPartsRenderTest*'
 * ```
 */
class ScreenPartsRenderTest {

    @Test
    fun `a frame with a floating action, a toast and a bottom action bar renders`() {
        render("screen-parts-frame") { FrameScene() }
        render("screen-parts-frame-dark", dark = true) { FrameScene() }
    }

    @Test
    fun `fallbacks, a sheet header and block skeletons render`() {
        render("screen-parts-states") { StatesScene() }
    }

    @Test
    fun `a panel page renders`() {
        render("screen-parts-panel") { PanelScene() }
    }
}

@Composable
private fun FrameScene() {
    val toasts = rememberToastState()
    LaunchedEffect(Unit) { toasts.show("Transaction saved") }
    BaseLayout(
        topBar = { TopBar(title = "Wallets") },
        bottomBar = {
            BottomActionBar(
                actionText = "Save",
                onAction = {},
                caption = "The file uploads when the import starts.",
                ready = false,
            )
        },
        floatingAction = { Button(text = "Add", onClick = {}, leading = { dev.voir.areca.foundation.icon.Icon(ArecaIcons.Plus, null) }) },
        toastState = toasts,
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(ArecaTheme.spacing.xl),
            verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
        ) {
            repeat(3) { index ->
                Card(modifier = Modifier.fillMaxWidth()) { inner ->
                    Text("Wallet ${index + 1}", modifier = Modifier.padding(inner))
                }
            }
        }
    }
}

@Composable
private fun StatesScene() {
    BaseLayout { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            FlowModalTopBar(title = "Choose a currency", onClose = {})
            Fallback(
                title = "No transactions yet",
                body = "Transactions you add appear here.",
                actionText = "Show all wallets",
                onAction = {},
            )
            Fallback(
                title = "Couldn't load transactions",
                body = "Check your connection and try again.",
                tone = FallbackTone.Failure,
                actionText = "Try again",
                onAction = {},
            )
            SkeletonGroup(
                contentDescription = "Loading",
                modifier = Modifier.padding(ArecaTheme.spacing.xl),
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg)) {
                    SkeletonBlock(Modifier.size(48.dp), shape = ArecaTheme.shapes.full, animated = false)
                    SkeletonBlock(Modifier.weight(1f).height(48.dp), animated = false)
                    SkeletonBlock(Modifier.size(48.dp).aspectRatio(1f), shape = ArecaTheme.shapes.tile, animated = false)
                }
            }
        }
    }
}

@Composable
private fun PanelScene() {
    BaseLayout(topBar = { TopBar(title = "Analytics") }) { padding ->
        PanelLayout(
            contentPadding = padding,
            header = {
                Column(Modifier.padding(ArecaTheme.spacing.xl)) {
                    Text("Spent in November", variant = TextVariant.BodySmall)
                    Text("€1,284.50", variant = TextVariant.HeadingLarge)
                }
            },
            panel = {
                Column(
                    modifier = Modifier.padding(ArecaTheme.spacing.xl),
                    verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
                ) {
                    Text("Categories", variant = TextVariant.HeadingExtraSmall)
                    Card(modifier = Modifier.fillMaxWidth()) { inner ->
                        Text("A card on the panel", modifier = Modifier.padding(inner))
                    }
                }
            },
        )
    }
}
