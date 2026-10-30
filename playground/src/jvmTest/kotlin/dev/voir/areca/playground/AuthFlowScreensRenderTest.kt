package dev.voir.areca.playground

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Density
import dev.voir.areca.playground.flow.auth.CodeChannel
import dev.voir.areca.playground.flow.auth.CodeStart
import dev.voir.areca.playground.flow.auth.EmailFirstPreview
import dev.voir.areca.playground.flow.auth.EmailFirstStart
import dev.voir.areca.playground.flow.auth.EmailLinkPreview
import dev.voir.areca.playground.flow.auth.LinkFlow
import dev.voir.areca.playground.flow.auth.LinkStep
import dev.voir.areca.playground.flow.auth.VerifyCodePreview
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.ArecaThemeProvider
import dev.voir.areca.window.InputMode
import org.jetbrains.skia.EncodedImageFormat
import java.io.File
import kotlin.test.Test

/** Width of the canvas one device frame is rendered on. */
private const val WIDTH = 440

/** Height of that canvas. */
private const val HEIGHT = 900

/** Frames advanced before the picture is taken, so staged entrances and animations settle. */
private const val SETTLE_FRAMES = 20

/** Time between those frames, in nanoseconds. */
private const val FRAME_NANOS = 50_000_000L

/** Where the rendered screens are written. */
private val OUTPUT = File(System.getProperty("render.out") ?: "build/screens/auth-flow")

/**
 * Renders one screen, in its device frame, to a PNG.
 *
 * @param name File name, without the extension.
 * @param dark Whether to render in the dark theme.
 * @param content The screen.
 */
@OptIn(ExperimentalComposeUiApi::class)
private fun render(name: String, dark: Boolean, content: @Composable () -> Unit) {
    val scene = ImageComposeScene(width = WIDTH, height = HEIGHT, density = Density(1f)) {
        ArecaThemeProvider(darkTheme = dark, inputMode = InputMode.Touch) {
            Box(
                modifier = Modifier.fillMaxSize().background(ArecaTheme.colors.bgSubtle),
                contentAlignment = Alignment.Center,
            ) { content() }
        }
    }

    try {
        var time = 0L
        repeat(SETTLE_FRAMES) {
            scene.render(time)
            time += FRAME_NANOS
        }
        val data = scene.render(time).encodeToData(EncodedImageFormat.PNG)!!
        OUTPUT.mkdirs()
        File(OUTPUT, "$name${if (dark) "-dark" else ""}.png").writeBytes(data.bytes)
    } finally {
        scene.close()
    }
}

/**
 * Renders every starting step of the email-first, verify-code and email-link screen pages to
 * `build/screens/auth-flow`, in both themes.
 *
 * Not an assertion, for the reason [AuthScreenRenderTest] gives: it fails when a recipe throws
 * during composition or layout, and it is the way to look at every step side by side.
 *
 * ```bash
 * ./gradlew :playground:jvmTest --tests '*AuthFlowScreensRenderTest*'
 * ```
 */
class AuthFlowScreensRenderTest {

    @Test
    fun `every email-first step renders`() {
        for (start in EmailFirstStart.entries) {
            for (dark in listOf(false, true)) {
                render("email-first-${start.name}", dark) {
                    EmailFirstPreview(start = start, sso = true, inviteOnly = false, wordmark = true)
                }
            }
        }
    }

    @Test
    fun `every code channel and state renders`() {
        for (channel in CodeChannel.entries) {
            for (start in CodeStart.entries) {
                render("verify-code-${channel.name}-${start.name}", dark = false) {
                    VerifyCodePreview(channel = channel, start = start, grouped = channel == CodeChannel.TextMessage)
                }
            }
        }
        render("verify-code-Email-Wrong", dark = true) {
            VerifyCodePreview(channel = CodeChannel.Email, start = CodeStart.Wrong, grouped = false)
        }
    }

    @Test
    fun `every link flow and step renders`() {
        for (flow in LinkFlow.entries) {
            for (step in LinkStep.entries) {
                render("email-link-${flow.name}-${step.name}", dark = false) {
                    EmailLinkPreview(flow = flow, step = step, autoplay = false)
                }
            }
        }
        render("email-link-Reset-Refused", dark = true) {
            EmailLinkPreview(flow = LinkFlow.Reset, step = LinkStep.Refused, autoplay = false)
        }
    }
}
