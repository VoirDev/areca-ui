package dev.voir.areca.playground

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Density
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.notice.Notice
import dev.voir.areca.foundation.notice.NoticeEmphasis
import dev.voir.areca.foundation.notice.NoticeText
import dev.voir.areca.foundation.notice.NoticeTone
import dev.voir.areca.foundation.status.StatusMark
import dev.voir.areca.foundation.status.StatusMarkState
import dev.voir.areca.foundation.status.StatusScene
import dev.voir.areca.foundation.status.StatusSceneActions
import dev.voir.areca.foundation.status.StatusSceneActionsStyle
import dev.voir.areca.icons.AlertCircle
import dev.voir.areca.icons.AlertTriangle
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.CheckCircle
import dev.voir.areca.icons.CloudOff
import dev.voir.areca.icons.FileText
import dev.voir.areca.icons.InfoCircle
import dev.voir.areca.icons.Lightbulb
import dev.voir.areca.icons.RefreshCcw
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.ArecaThemeProvider
import dev.voir.areca.window.InputMode
import org.jetbrains.skia.EncodedImageFormat
import java.io.File
import kotlin.test.Test

/** Logical width of the device the scenes are rendered at. */
private const val WIDTH = 390

/** Logical height of the device the scenes are rendered at. */
private const val HEIGHT = 844

/** Pixel density the scenes are rasterised at, so the PNGs are legible when opened. */
private const val DENSITY = 2f

/** Logical height of the banners image: six titled blocks run past a phone's height. */
private const val BANNERS_HEIGHT = 1000

/** Time between the frames stepped through before the final one, in nanoseconds. */
private const val FRAME_NANOS = 50_000_000L

/**
 * How long the clock runs before the frame that is kept, in nanoseconds.
 *
 * Longer than the slowest entrance — the scene's copy waits a beat and then fades for a slow step,
 * and the success mark's tick only starts once its disc has landed.
 */
private const val SETTLE_NANOS = 3_000_000_000L

/** Where the rendered images are written. */
private val OUTPUT = File(System.getProperty("render.out") ?: "build/screens")

/**
 * Renders one composition to a PNG once its animations have settled.
 *
 * The scene's clock only moves when a frame is rendered, and an animation that another animation
 * starts — a tick that waits for its disc, copy that waits for a recomposition — begins on the frame
 * after. So the clock is stepped frame by frame to [SETTLE_NANOS] rather than jumped there once.
 *
 * @param name File name, without the extension.
 * @param dark Whether to render in the dark theme.
 * @param height Logical height of the image; defaults to a phone's.
 * @param content Composition to render.
 */
@OptIn(ExperimentalComposeUiApi::class)
private fun render(
    name: String,
    dark: Boolean = false,
    height: Int = HEIGHT,
    content: @Composable () -> Unit,
) {
    val scene = ImageComposeScene(
        width = (WIDTH * DENSITY).toInt(),
        height = (height * DENSITY).toInt(),
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
 * Renders the notice and status-scene families to `build/screens`.
 *
 * Not an assertion, for the same reason as [AuthScreenRenderTest]: there is no golden image, and one
 * would fail on every intentional change. It fails when any configuration throws during composition
 * or layout, and it is the only way to look at them — settled, in both themes — on a machine with no
 * display.
 *
 * ```bash
 * ./gradlew :playground:jvmTest --tests '*StatusRenderTest*'
 * ```
 */
class StatusRenderTest {

    /** Titled banners in each tone, busy included, in both themes. */
    @Test
    fun `notice banners render`() {
        render("notice-banners", height = BANNERS_HEIGHT) { Banners() }
        render("notice-banners-dark", dark = true, height = BANNERS_HEIGHT) { Banners() }
    }

    /** Untitled notes in each tone, one of them two lines long. */
    @Test
    fun `notice notes render`() {
        render("notice-notes") {
            Page {
                Notice(title = null, icon = ArecaIcons.InfoCircle) {
                    NoticeText("Amounts are shown in your main currency.")
                }
                Notice(title = null, icon = ArecaIcons.Lightbulb, tone = NoticeTone.Info) {
                    NoticeText("Drag a file onto this page to attach it.")
                }
                Notice(title = null, icon = ArecaIcons.CheckCircle, tone = NoticeTone.Success) {
                    NoticeText("Your changes were saved.")
                }
                Notice(title = null, icon = ArecaIcons.AlertTriangle, tone = NoticeTone.Warning) {
                    NoticeText("Rates are from yesterday; today's aren't published yet.")
                }
                Notice(title = null, icon = ArecaIcons.AlertCircle, tone = NoticeTone.Danger) {
                    NoticeText("That email and password don't match an account.")
                }
                Notice(title = null, icon = ArecaIcons.AlertCircle, tone = NoticeTone.Danger) {
                    NoticeText(
                        "We couldn't reach the server, so your changes haven't been saved yet. " +
                            "They'll be sent as soon as you're back online.",
                    )
                }
            }
        }
    }

    /** The three mark states, each settled. */
    @Test
    fun `status marks render`() {
        render("status-marks", height = 320) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                StatusMark(state = StatusMarkState.Working(ArecaIcons.FileText), contentDescription = null)
                StatusMark(state = StatusMarkState.Success, contentDescription = null)
                StatusMark(state = StatusMarkState.Stopped(ArecaIcons.AlertTriangle), contentDescription = null)
            }
        }
    }

    /** A finished import: the success mark, a brand button and a plain one. */
    @Test
    fun `status scene done renders`() {
        render("status-scene-done") {
            StatusScene(
                title = "Import complete",
                modifier = Modifier.padding(horizontal = ArecaTheme.spacing.xl2),
                mark = { StatusMark(state = StatusMarkState.Success, contentDescription = null) },
                message = "3,112 transactions were added to 4 wallets.",
                footnote = "18 rows matched transactions you already had and were skipped.",
                actions = {
                    StatusSceneActions(
                        primary = "Done",
                        onPrimary = {},
                        style = StatusSceneActionsStyle.default(primaryColor = ActionColor.Brand),
                        secondary = "View wallets",
                        onSecondary = {},
                    )
                },
            )
        }
    }

    /** A stopped import: the stopped mark and the neutral way back. */
    @Test
    fun `status scene stopped renders`() {
        render("status-scene-stopped") {
            StatusScene(
                title = "Import stopped",
                modifier = Modifier.padding(horizontal = ArecaTheme.spacing.xl2),
                mark = {
                    StatusMark(state = StatusMarkState.Stopped(ArecaIcons.AlertTriangle), contentDescription = null)
                },
                message = "The file ended partway through a row. Nothing was added.",
                actions = {
                    StatusSceneActions(
                        primary = "Try again",
                        onPrimary = {},
                        style = StatusSceneActionsStyle.default(primaryColor = ActionColor.NeutralDark),
                        secondary = "Close",
                        onSecondary = {},
                    )
                },
            )
        }
    }
}

/**
 * A padded column of blocks, the way a screen stacks them.
 *
 * @param content The blocks.
 */
@Composable
private fun Page(content: @Composable () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(ArecaTheme.spacing.xl2),
        verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg),
    ) {
        content()
    }
}

/** Titled banners: neutral subtle, info, success, warning, danger filled and a busy one. */
@Composable
private fun Banners() = Page {
    Notice(title = "Showing saved details", icon = ArecaIcons.CloudOff) {
        NoticeText("We couldn't reach the server, so these are from your last visit.")
    }
    Notice(title = "Exports now include receipts", icon = ArecaIcons.Lightbulb, tone = NoticeTone.Info) {
        NoticeText("Photos you attached are added to the archive as separate files.")
    }
    Notice(title = "Backup saved", icon = ArecaIcons.CheckCircle, tone = NoticeTone.Success) {
        NoticeText("All 1,284 transactions were written to the file.")
    }
    Notice(title = "Some rows were skipped", icon = ArecaIcons.AlertTriangle, tone = NoticeTone.Warning) {
        NoticeText("12 rows had no date and were left out of the import.")
        NoticeText("Add the dates in the file and import it again.", quiet = true)
    }
    Notice(
        title = "Nothing was imported",
        icon = ArecaIcons.AlertCircle,
        tone = NoticeTone.Danger,
        emphasis = NoticeEmphasis.Filled,
    ) {
        NoticeText("statement-2026-08.csv isn't a bank export we recognise.")
        NoticeText("Export the statement as CSV from your bank's website and try again.", quiet = true)
    }
    Notice(title = "Restoring your backup", icon = ArecaIcons.RefreshCcw, busy = true) {
        NoticeText("This can take a minute for a large ledger.")
    }
}
