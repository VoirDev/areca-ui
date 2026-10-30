package dev.voir.areca.playground

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.keyboard.KeyboardController
import dev.voir.areca.keyboard.KeyboardHost
import dev.voir.areca.keyboard.KeyboardScrim
import dev.voir.areca.keyboard.key.Key
import dev.voir.areca.keyboard.key.KeyRole
import dev.voir.areca.keyboard.key.KeyRow
import dev.voir.areca.keyboard.key.KeyboardGrids
import dev.voir.areca.keyboard.key.appendRow
import dev.voir.areca.keyboard.layout.KeyboardLayout
import dev.voir.areca.keyboard.layout.KeyboardStyle
import dev.voir.areca.keyboard.rememberKeyboardTarget
import dev.voir.areca.keyboard.keyboardTarget
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.ArecaThemeProvider
import dev.voir.areca.window.InputMode
import org.jetbrains.skia.EncodedImageFormat
import java.io.File
import kotlin.test.Test

/** Logical width of the device the keyboard is rendered at. */
private const val WIDTH = 390

/** Logical height of the device the keyboard is rendered at. */
private const val HEIGHT = 760

/** Pixel density the scenes are rasterised at. */
private const val DENSITY = 2f

/** Time between the frames stepped through before the final one, in nanoseconds. */
private const val FRAME_NANOS = 50_000_000L

/** How long the clock runs before the kept frame: past the keyboard's entrance. */
private const val SETTLE_NANOS = 1_500_000_000L

/** Where the rendered images are written. */
private val OUTPUT = File(System.getProperty("render.out") ?: "build/screens")

/**
 * Renders one composition to a PNG once it has settled.
 *
 * @param name File name, without the extension.
 * @param dark Whether to render in the dark theme.
 * @param content Composition to render.
 */
@OptIn(ExperimentalComposeUiApi::class)
private fun render(name: String, dark: Boolean = false, content: @Composable () -> Unit) {
    val scene = ImageComposeScene(
        width = (WIDTH * DENSITY).toInt(),
        height = (HEIGHT * DENSITY).toInt(),
        density = Density(DENSITY),
    ) {
        ArecaThemeProvider(darkTheme = dark, inputMode = InputMode.Touch) {
            Box(Modifier.fillMaxSize().background(ArecaTheme.colors.bgCanvas)) { content() }
        }
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
 * Renders an open keyboard host to `build/screens`, in both themes and with a scrim.
 *
 * Like the other render tests it has no golden image; it fails when a configuration throws, and it
 * is how the keyboard is looked at on a machine with no display.
 *
 * ```bash
 * ./gradlew :playground:jvmTest --tests '*KeyboardRenderTest*'
 * ```
 */
class KeyboardRenderTest {

    /** The calculator open over a compressed screen, light and dark, and the flat surface under a scrim. */
    @Test
    fun `open keyboard renders`() {
        render("keyboard-calculator") { Screen(compressContent = true, scrim = null, flat = false) }
        render("keyboard-calculator-dark", dark = true) {
            Screen(compressContent = true, scrim = null, flat = false)
        }
        render("keyboard-scrim-flat") {
            Screen(compressContent = false, scrim = KeyboardScrim.default(dismissOnTap = false), flat = true)
        }
    }

    /**
     * A screen with one target and the calculator keyboard open for it.
     *
     * @param compressContent Whether the content makes room for the keyboard.
     * @param scrim Tint between the content and the keyboard.
     * @param flat Whether the keyboard surface is the flat one.
     */
    @Composable
    private fun Screen(compressContent: Boolean, scrim: KeyboardScrim?, flat: Boolean) {
        val target = rememberKeyboardTarget(key = "amount")
        val controller = remember(target) {
            KeyboardController().apply { open(target, scroll = false, stealFocus = false) }
        }

        KeyboardHost(
            keyboardContent = {
                KeyboardLayout(
                    grid = KeyboardGrids.calculator(onAction = {}, decimalSeparator = ',')
                        .appendRow(
                            KeyRow(
                                Key.text("A label far too long for its key", id = "long") {},
                                Key.text("000", id = "thousands", role = KeyRole.Info) {},
                            ),
                        ),
                    style = if (flat) KeyboardStyle.flat() else KeyboardStyle.Default,
                    windowInsets = WindowInsets(0, 0, 0, 0),
                    header = {
                        Text(
                            text = "1 234,56",
                            modifier = Modifier.fillMaxWidth(),
                            variant = TextVariant.HeadingSmall,
                            textAlign = TextAlign.End,
                        )
                    },
                )
            },
            controller = controller,
            compressContent = compressContent,
            scrim = scrim,
        ) {
            Column(Modifier.fillMaxSize().padding(16.dp)) {
                Text("Amount", variant = TextVariant.BodySmall)
                Text(
                    text = "1 234,56",
                    modifier = Modifier.fillMaxWidth().keyboardTarget(controller, target),
                    variant = TextVariant.HeadingLarge,
                )
            }
        }
    }
}
