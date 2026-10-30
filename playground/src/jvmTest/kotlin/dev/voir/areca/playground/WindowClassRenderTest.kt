package dev.voir.areca.playground

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Alignment
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.unit.Density
import androidx.compose.runtime.Composable
import dev.voir.areca.playground.foundation.adaptive.AdaptiveGridShowcase
import dev.voir.areca.playground.foundation.adaptive.AdaptiveMenuShowcase
import dev.voir.areca.playground.foundation.adaptive.AdaptiveNavigationShowcase
import dev.voir.areca.playground.foundation.adaptive.AdaptiveSheetShowcase
import dev.voir.areca.playground.foundation.adaptive.ContentColumnShowcase
import dev.voir.areca.playground.foundation.adaptive.ListDetailShowcase
import dev.voir.areca.playground.foundation.adaptive.SupportingPaneShowcase
import dev.voir.areca.playground.flow.attachments.AttachmentWideDetailsShowcase
import dev.voir.areca.playground.flow.auth.SignInSplitShowcase
import dev.voir.areca.playground.flow.profile.AccountSettingsShowcase
import dev.voir.areca.foundation.button.IconButton
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.Download
import dev.voir.areca.playground.foundation.layout.ScrollbarShowcase
import dev.voir.areca.playground.foundation.overlay.TooltipShowcase
import dev.voir.areca.playground.foundation.window.WindowClassShowcase
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.ArecaThemeProvider
import dev.voir.areca.window.InputMode
import org.jetbrains.skia.EncodedImageFormat
import java.io.File
import kotlin.test.Test

/** Logical width of the page, wide enough for the variant grid to hold three windows a row. */
private const val WIDTH = 1400

/** Logical height of the page: the playground, the grid of every window and the edge cases. */
private const val HEIGHT = 3400

/** Where the rendered images are written. */
private val OUTPUT = File(System.getProperty("render.out") ?: "build/screens")

/**
 * Renders the window class page and the adaptive layout pages to `build/screens`.
 *
 * Not an assertion: there is no golden image. It fails when a window throws during composition or
 * layout, and it is how the sample is looked at in every window at once on a machine with no
 * display.
 *
 * ```bash
 * ./gradlew :playground:jvmTest --tests '*WindowClassRenderTest*'
 * ```
 */
class WindowClassRenderTest {

    /** The whole page, in both themes. */
    @Test
    fun windowClassPage() {
        render("window-class-light", dark = false) { WindowClassShowcase() }
        render("window-class-dark", dark = true) { WindowClassShowcase() }
    }

    /** Each adaptive layout's page, light. */
    @Test
    fun adaptivePages() {
        render("adaptive-navigation", dark = false) { AdaptiveNavigationShowcase() }
        render("adaptive-list-detail", dark = false) { ListDetailShowcase() }
        render("adaptive-supporting-pane", dark = false) { SupportingPaneShowcase() }
        render("adaptive-content-column", dark = false) { ContentColumnShowcase() }
        render("adaptive-grid", dark = false) { AdaptiveGridShowcase() }
        render("adaptive-sheet", dark = false) { AdaptiveSheetShowcase() }
        render("adaptive-menu", dark = false) { AdaptiveMenuShowcase() }
    }

    /** The flows' tablet and desktop pages, at their default windows. */
    @Test
    fun wideFlowPages() {
        render("flow-auth-sign-in-split", dark = false) { SignInSplitShowcase() }
        render("flow-profile-account-settings", dark = false) { AccountSettingsShowcase() }
        render("flow-attachments-wide-details", dark = false) { AttachmentWideDetailsShowcase() }
    }

    /** The pointer-only affordances' pages, and a tooltip showing on a hovered icon button. */
    @OptIn(ExperimentalComposeUiApi::class)
    @Test
    fun pointerPages() {
        render("pointer-tooltip", dark = false) { TooltipShowcase() }
        render("pointer-scrollbar", dark = false) { ScrollbarShowcase() }

        val scene = ImageComposeScene(width = 480, height = 200, density = Density(2f)) {
            ArecaThemeProvider(inputMode = InputMode.Pointer) {
                Box(Modifier.fillMaxSize().background(ArecaTheme.colors.bgCanvas), contentAlignment = Alignment.Center) {
                    IconButton(ArecaIcons.Download, contentDescription = "Download as PDF", onClick = {})
                }
            }
        }
        try {
            scene.render(0L)
            scene.sendPointerEvent(PointerEventType.Enter, Offset(240f, 100f))
            scene.sendPointerEvent(PointerEventType.Move, Offset(240f, 100f))
            scene.render(100_000_000L)
            // The tooltip's rest delay runs on the scene's real-time dispatcher, not its frame clock.
            Thread.sleep(700)
            for (frame in 8L..11L) scene.render(frame * 100_000_000L)
            val data = requireNotNull(scene.render(1_200_000_000L).encodeToData(EncodedImageFormat.PNG))
            OUTPUT.mkdirs()
            File(OUTPUT, "pointer-tooltip-hovered.png").writeBytes(data.bytes)
        } finally {
            scene.close()
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    private fun render(name: String, dark: Boolean, page: @Composable () -> Unit) {
        val scene = ImageComposeScene(width = WIDTH, height = HEIGHT, density = Density(1f)) {
            ArecaThemeProvider(darkTheme = dark, inputMode = InputMode.Touch) {
                Box(Modifier.fillMaxSize().background(ArecaTheme.colors.bgCanvas)) { page() }
            }
        }
        try {
            scene.render(0L)
            val data = requireNotNull(scene.render(1_000_000_000L).encodeToData(EncodedImageFormat.PNG))
            OUTPUT.mkdirs()
            File(OUTPUT, "$name.png").writeBytes(data.bytes)
        } finally {
            scene.close()
        }
    }
}
