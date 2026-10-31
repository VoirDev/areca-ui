package dev.voir.areca.playground

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Density
import dev.voir.areca.flow.auth.AuthBranding
import dev.voir.areca.flow.auth.AuthLayout
import dev.voir.areca.flow.auth.part.AuthHeader
import dev.voir.areca.flow.auth.part.AuthScaffold
import dev.voir.areca.flow.profile.sheet.AvatarSourceOptions
import dev.voir.areca.flow.profile.sheet.AvatarSourceSheet
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.topbar.BackAction
import dev.voir.areca.foundation.topbar.LocalTopBarSize
import dev.voir.areca.foundation.topbar.TextAction
import dev.voir.areca.foundation.topbar.TopBar
import dev.voir.areca.foundation.topbar.TopBarProgress
import dev.voir.areca.foundation.topbar.TopBarSize
import dev.voir.areca.foundation.topbar.TopBarTitleAlignment
import dev.voir.areca.playground.flow.auth.AuthSamples
import dev.voir.areca.theme.ArecaThemeProvider
import dev.voir.areca.window.InputMode
import org.jetbrains.skia.EncodedImageFormat
import java.io.File
import kotlin.test.Test

/** Logical width of the phone the screens are rendered at. */
private const val WIDTH = 390

/** Logical height of the phone the screens are rendered at. */
private const val HEIGHT = 520

/** Pixel density the scenes are rasterised at. */
private const val DENSITY = 2f

/** Where the rendered screens are written. */
private val OUTPUT = File(System.getProperty("render.out") ?: "build/screens")

/**
 * Renders a screen to a PNG after letting sheets finish opening.
 *
 * @param name File name, without the extension.
 * @param screen The screen.
 */
@OptIn(ExperimentalComposeUiApi::class)
private fun render(name: String, screen: @Composable () -> Unit) {
    val scene = ImageComposeScene(
        width = (WIDTH * DENSITY).toInt(),
        height = (HEIGHT * DENSITY).toInt(),
        density = Density(DENSITY),
    ) {
        ArecaThemeProvider(darkTheme = false, inputMode = InputMode.Touch) { screen() }
    }

    try {
        var time = 0L
        repeat(60) {
            scene.render(time)
            time += 50_000_000L
        }
        val data = scene.render(time).encodeToData(EncodedImageFormat.PNG)!!
        OUTPUT.mkdirs()
        File(OUTPUT, "$name.png").writeBytes(data.bytes)
    } finally {
        scene.close()
    }
}

/** Writes the flows' new top bars as PNGs, for checking them by eye. */
class TopBarFlowsRenderTest {
    /** A sign-up step: back, the step, a skip and a progress rule over the form. */
    @Test
    fun authStep() = render("auth-step") {
        AuthScaffold(
            windowInsets = WindowInsets(0, 0, 0, 0),
            topBar = {
                TopBar(
                    title = "Step 2 of 3",
                    modifier = Modifier.fillMaxWidth(),
                    size = (LocalTopBarSize.current ?: TopBarSize.Default).copy(bottomPadding = PaddingValues()),
                    titleAlignment = TopBarTitleAlignment.Center,
                    leading = { BackAction(onClick = {}) },
                    trailing = { TextAction(text = "Skip", onClick = {}, color = ActionColor.Neutral) },
                    bottom = { TopBarProgress(progress = 2f / 3f) },
                )
            },
        ) {
            AuthHeader(title = "Verify your email", subtitle = "We sent a code to name@example.com.")
        }
    }

    /** A back control over full-bleed artwork, drawn as a scrim bar by the scaffold. */
    @Test
    fun authOverHero() = render("auth-hero-back") {
        AuthScaffold(
            branding = AuthBranding(hero = { AuthSamples.Hero() }, layout = AuthLayout.FullBleed),
            windowInsets = WindowInsets(0, 0, 0, 0),
            topBar = { TopBar(modifier = Modifier.fillMaxWidth(), leading = { BackAction(onClick = {}) }) {} },
        ) {
            AuthHeader(title = "Welcome back")
        }
    }

    /** The avatar source sheet, headed by a modal bar. */
    @Test
    fun avatarSheet() = render("profile-avatar-sheet") {
        AvatarSourceSheet(
            visible = true,
            options = listOf(
                AvatarSourceOptions.camera {},
                AvatarSourceOptions.library {},
                AvatarSourceOptions.remove {},
            ),
            onDismiss = {},
        )
    }
}
