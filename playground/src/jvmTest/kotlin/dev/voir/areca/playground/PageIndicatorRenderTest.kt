package dev.voir.areca.playground

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.navigation.PageCounter
import dev.voir.areca.foundation.navigation.PageCounterStyle
import dev.voir.areca.foundation.navigation.PageIndicator
import dev.voir.areca.foundation.navigation.PageIndicatorStyle
import dev.voir.areca.foundation.navigation.PageIndicatorVariant
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.ArecaThemeProvider
import dev.voir.areca.window.InputMode
import org.jetbrains.skia.EncodedImageFormat
import java.io.File
import kotlin.test.Test

/** Logical width of the image. */
private const val WIDTH = 390

/** Logical height of the image: every variant on the page, then on pictures. */
private const val HEIGHT = 760

/** Pixel density the image is rasterised at. */
private const val DENSITY = 2f

/** Time between the frames stepped through, in nanoseconds. */
private const val FRAME_NANOS = 50_000_000L

/** How long the clock runs before the kept frame: past every mark's spring. */
private const val SETTLE_NANOS = 1_500_000_000L

/** Where the rendered images are written. */
private val OUTPUT = File(System.getProperty("render.out") ?: "build/screens")

/**
 * Renders one composition to a PNG once its springs have settled.
 *
 * @param name File name, without the extension.
 * @param dark Whether to render in the dark theme.
 * @param content Composition to render.
 */
@OptIn(ExperimentalComposeUiApi::class)
private fun render(name: String, dark: Boolean, content: @Composable () -> Unit) {
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
 * Renders every [PageIndicatorVariant] and [PageCounter], on the page and over pictures, to
 * `build/screens`.
 *
 * Not an assertion, like the other render tests: it fails when a variant throws during composition
 * or layout, and it is how they are looked at on a machine with no display.
 *
 * ```bash
 * ./gradlew :playground:jvmTest --tests '*PageIndicatorRenderTest*'
 * ```
 */
class PageIndicatorRenderTest {

    /** Each variant and the counter, in both themes. */
    @Test
    fun `page indicators render`() {
        render("page-indicators", dark = false) { Indicators() }
        render("page-indicators-dark", dark = true) { Indicators() }
    }
}

/** Every variant on the page, then the on-image style over a light and a dark picture. */
@Composable
private fun Indicators() {
    Column(
        modifier = Modifier.fillMaxSize().padding(ArecaTheme.spacing.xl),
        verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg),
    ) {
        Labelled("Elongated") { PageIndicator(pageCount = 5, currentPage = 1) }
        Labelled("Dots") { PageIndicator(pageCount = 7, currentPage = 2, variant = PageIndicatorVariant.Dots) }
        Labelled("Shrinking, start") {
            PageIndicator(pageCount = 24, currentPage = 1, variant = PageIndicatorVariant.Shrinking())
        }
        Labelled("Shrinking, middle") {
            PageIndicator(pageCount = 24, currentPage = 12, variant = PageIndicatorVariant.Shrinking())
        }
        Labelled("Line") { PageIndicator(pageCount = 9, currentPage = 3, variant = PageIndicatorVariant.Line) }
        Labelled("Segments") {
            PageIndicator(
                pageCount = 4,
                currentPage = 1,
                variant = PageIndicatorVariant.Segments,
                progress = { 0.6f },
                modifier = Modifier.fillMaxWidth(),
            )
        }
        Labelled("Counter") { PageCounter(currentPage = 2, pageCount = 9) }
        for (light in listOf(true, false)) {
            Picture(light) {
                PageIndicator(
                    pageCount = 7,
                    currentPage = 2,
                    variant = PageIndicatorVariant.Dots,
                    style = PageIndicatorStyle.onImage(),
                    modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = ArecaTheme.spacing.lg),
                )
                PageCounter(
                    currentPage = 2,
                    pageCount = 7,
                    style = PageCounterStyle.onImage(),
                    modifier = Modifier.align(Alignment.TopEnd).padding(ArecaTheme.spacing.md),
                )
            }
        }
    }
}

/**
 * A variant under its name.
 *
 * @param name The variant's name.
 * @param content The indicator.
 */
@Composable
private fun Labelled(name: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xs)) {
        Text(name, style = ArecaTheme.typography.caption, color = ArecaTheme.colors.fgMuted)
        content()
    }
}

/**
 * A stand-in picture for indicators laid over it.
 *
 * @param light Whether it is a light product shot or a dark photograph.
 * @param content What is laid over it.
 */
@Composable
private fun Picture(light: Boolean, content: @Composable androidx.compose.foundation.layout.BoxScope.() -> Unit) {
    val colors = if (light) listOf(Color(0xFFF4F1EC), Color(0xFFE2DCD2)) else listOf(Color(0xFF2B3A55), Color(0xFF7A5C61))
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .background(Brush.linearGradient(colors), ArecaTheme.shapes.lg),
        content = content,
    )
}
