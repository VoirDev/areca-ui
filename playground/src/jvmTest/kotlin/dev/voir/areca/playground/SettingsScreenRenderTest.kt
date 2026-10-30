package dev.voir.areca.playground

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Density
import dev.voir.areca.playground.foundation.settings.AccountSettingsSample
import dev.voir.areca.playground.foundation.settings.AutoLockSample
import dev.voir.areca.playground.foundation.settings.ChatSettingsSample
import dev.voir.areca.playground.foundation.settings.DependentSettingsSample
import dev.voir.areca.playground.foundation.settings.DevicesSettingsSample
import dev.voir.areca.playground.foundation.settings.LoadingSettingsSample
import dev.voir.areca.playground.foundation.settings.PrivacySettingsSample
import dev.voir.areca.playground.foundation.settings.SettingsNavigatorSample
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.ArecaThemeProvider
import dev.voir.areca.window.InputMode
import org.jetbrains.skia.EncodedImageFormat
import java.io.File
import kotlin.test.Test

/** Logical width of the device the screens are rendered at. */
private const val WIDTH = 390

/** Pixel density the scenes are rasterised at, so the PNGs are legible when opened. */
private const val DENSITY = 2f

/** Where the rendered screens are written. */
private val OUTPUT = File(System.getProperty("render.out") ?: "build/screens")

/**
 * Renders one settings screen to a PNG, on the canvas a settings screen is placed on.
 *
 * @param name File name, without the extension.
 * @param height Logical height of the scene.
 * @param dark Whether to render in the dark theme.
 * @param content Screen to render.
 */
@OptIn(ExperimentalComposeUiApi::class)
private fun render(name: String, height: Int, dark: Boolean, content: @Composable () -> Unit) {
    ImageComposeScene(
        width = (WIDTH * DENSITY).toInt(),
        height = (height * DENSITY).toInt(),
        density = Density(DENSITY),
    ) {
        ArecaThemeProvider(darkTheme = dark, inputMode = InputMode.Touch) {
            Box(Modifier.fillMaxSize().background(ArecaTheme.colors.bgCanvas)) {
                Column {
                    Spacer(Modifier.height(ArecaTheme.spacing.xl3))
                    content()
                }
            }
        }
    }.let { scene ->
        try {
            // Two frames, so the separators drawn from the first placement are in the picture.
            scene.render(0)
            val data = scene.render(16_000_000).encodeToData(EncodedImageFormat.PNG)!!
            OUTPUT.mkdirs()
            File(OUTPUT, "$name${if (dark) "-dark" else ""}.png").writeBytes(data.bytes)
        } finally {
            scene.close()
        }
    }
}

/**
 * Renders every settings recipe to `build/screens`, in both themes.
 *
 * Not an assertion — see [AuthScreenRenderTest] for why. It fails when any configuration throws
 * during composition or layout, and it is the only way to look at these screens on a machine with no
 * display. Menus are popups, which an image scene does not draw; the playground shows those.
 *
 * ```bash
 * ./gradlew :playground:jvmTest --tests '*SettingsScreenRenderTest*'
 * ```
 */
class SettingsScreenRenderTest {

    /** Renders [content] once per theme. */
    private fun renderBoth(name: String, height: Int, content: @Composable () -> Unit) {
        listOf(false, true).forEach { dark -> render(name, height, dark, content) }
    }

    @Test
    fun account() = renderBoth("settings-account", height = 1180) { AccountSettingsSample() }

    @Test
    fun chat() = renderBoth("settings-chat", height = 1500) { ChatSettingsSample() }

    @Test
    fun devices() = renderBoth("settings-devices", height = 1120) { DevicesSettingsSample() }

    @Test
    fun privacy() = renderBoth("settings-privacy", height = 360) { PrivacySettingsSample() }

    @Test
    fun smaller() {
        renderBoth("settings-autolock", height = 380) { AutoLockSample() }
        renderBoth("settings-dependent", height = 300) { DependentSettingsSample() }
        renderBoth("settings-navigator", height = 320) { SettingsNavigatorSample() }
        renderBoth("settings-loading", height = 300) { LoadingSettingsSample() }
    }
}
