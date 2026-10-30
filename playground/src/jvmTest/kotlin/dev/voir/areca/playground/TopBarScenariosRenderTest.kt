package dev.voir.areca.playground

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.playground.foundation.topbar.TopBarAppearances
import dev.voir.areca.playground.foundation.topbar.TopBarScenarios
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.ArecaThemeProvider
import dev.voir.areca.window.InputMode
import org.jetbrains.skia.EncodedImageFormat
import java.io.File
import kotlin.test.Test

/** Logical width of the device the bars are rendered at. */
private const val WIDTH = 390

/** Pixel density the scenes are rasterised at. */
private const val DENSITY = 2f

/** Where the rendered bars are written. */
private val OUTPUT = File(System.getProperty("render.out") ?: "build/screens")

/**
 * Renders a column of labelled bars to a PNG, for checking the top bar presets by eye.
 *
 * @param name File name, without the extension.
 * @param height Logical height of the picture.
 * @param dark Whether to render in the dark theme.
 * @param rows The labelled bars.
 */
@OptIn(ExperimentalComposeUiApi::class)
private fun renderBars(
    name: String,
    height: Int,
    dark: Boolean,
    rows: List<Pair<String, @Composable () -> Unit>>,
) {
    val scene = ImageComposeScene(
        width = (WIDTH * DENSITY).toInt(),
        height = (height * DENSITY).toInt(),
        density = Density(DENSITY),
    ) {
        ArecaThemeProvider(darkTheme = dark, inputMode = InputMode.Touch) {
            Column(
                Modifier.fillMaxSize().background(ArecaTheme.colors.bgCanvas),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                rows.forEach { (label, bar) ->
                    Text(
                        text = label,
                        modifier = Modifier.fillMaxWidth().background(ArecaTheme.colors.bgMuted)
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        style = ArecaTheme.typography.labelSmall,
                        color = ArecaTheme.colors.fgMuted,
                    )
                    bar()
                }
            }
        }
    }

    try {
        var time = 0L
        repeat(10) {
            scene.render(time)
            time += 50_000_000L
        }
        val data = scene.render(time).encodeToData(EncodedImageFormat.PNG)!!
        OUTPUT.mkdirs()
        File(OUTPUT, "$name${if (dark) "-dark" else ""}.png").writeBytes(data.bytes)
    } finally {
        scene.close()
    }
}

/** Writes the top bar scenario and appearance catalogues as PNGs. */
class TopBarScenariosRenderTest {
    /** Every scenario, light and dark. */
    @Test
    fun scenarios() {
        listOf(false, true).forEach { dark ->
            renderBars(
                name = "topbar-scenarios",
                height = 1000,
                dark = dark,
                rows = TopBarScenarios.map { "${it.name} — ${it.recipe}" to it.bar },
            )
        }
    }

    /** Every appearance, light and dark. */
    @Test
    fun appearances() {
        listOf(false, true).forEach { dark ->
            renderBars(
                name = "topbar-appearances",
                height = 330,
                dark = dark,
                rows = TopBarAppearances.map { "${it.name} — ${it.recipe}" to it.bar },
            )
        }
    }
}
