package dev.voir.areca.playground.showcase

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.voir.areca.playground.showcase.canvas.DeviceSize
import dev.voir.areca.playground.showcase.canvas.LocalDeviceScale
import dev.voir.areca.playground.showcase.canvas.LocalPlaygroundDarkTheme
import dev.voir.areca.playground.showcase.canvas.PreviewCanvasToolbar
import dev.voir.areca.playground.showcase.canvas.PreviewCanvasState
import dev.voir.areca.playground.showcase.canvas.PreviewTheme
import dev.voir.areca.playground.showcase.canvas.rememberPreviewCanvasState
import dev.voir.areca.playground.showcase.chrome.ChromeBadge
import dev.voir.areca.playground.showcase.chrome.ChromeBadgeTone
import dev.voir.areca.playground.showcase.chrome.ChromeLevel
import dev.voir.areca.playground.showcase.chrome.ChromeSurface
import dev.voir.areca.playground.showcase.chrome.ChromeText
import dev.voir.areca.playground.showcase.chrome.ChromeTextRole
import dev.voir.areca.playground.showcase.control.ControlsPanel
import dev.voir.areca.playground.showcase.control.PlaygroundScope
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.ArecaThemeProvider

/** Width of the control column. Wide enough for a segmented picker of four, and no wider. */
private val ControlColumnWidth = 340.dp

/** Breathing room around the device inside the preview area. */
private val PreviewPadding = 24.dp

/**
 * The page a flow module's showcase is built in: one screen, and the configuration beside it.
 *
 * Deliberately not [ShowcaseScaffold]. A component page is a sequence of sections because a
 * component has axes — styles, sizes, states — that are only legible side by side. A screen has
 * none of that. It has one appearance at a time, and the only question worth answering is what a
 * given configuration looks like, at full size, right now. Ten thumbnails of a sign-in form teach
 * a reader less than one they can change.
 *
 * So there is no variant matrix, no size ramp, no state list and no edge-case row here. There is a
 * phone, and there are the controls that drive it.
 *
 * The preview scales itself down when the window is too short for the device, so the whole screen
 * is always in view rather than cropped at the fold — the bottom edge is exactly where a pinned
 * footer has to be checked.
 *
 * @param title Screen or part being documented.
 * @param summary One sentence saying what it is for.
 * @param modifier Modifier applied to the page.
 * @param module Owning artifact, shown as a badge.
 * @param status API maturity, shown as a badge.
 * @param device Phone shape the preview renders at.
 * @param canvasState Theme selection for the preview.
 * @param notes Prose under the controls — the recipe this page renders, what to look at, what is
 *   deliberately absent.
 * @param content Control declarations followed by a single `preview { }` call, exactly as in a
 *   component page's playground section.
 */
@Composable
fun FlowShowcase(
    title: String,
    summary: String,
    modifier: Modifier = Modifier,
    module: String? = null,
    status: ShowcaseStatus = ShowcaseStatus.Stable,
    device: DeviceSize = DeviceSize.Regular,
    canvasState: PreviewCanvasState = rememberPreviewCanvasState(),
    notes: (@Composable ColumnScope.() -> Unit)? = null,
    content: @Composable PlaygroundScope.() -> Unit,
) {
    val scope = remember { PlaygroundScope() }

    Row(modifier = modifier.fillMaxSize()) {
        FlowPreview(
            device = device,
            canvasState = canvasState,
            modifier = Modifier.weight(1f).fillMaxHeight(),
        ) {
            scope.content()
        }

        // Composes after the preview, so every control the body declared is already registered.
        Column(
            modifier = Modifier
                .width(ControlColumnWidth)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
                .padding(ArecaTheme.spacing.xl2),
            verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xl2),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md)) {
                FlowShowcaseHeader(title = title, summary = summary, module = module, status = status)
            }

            PreviewCanvasToolbar(state = canvasState, showBackground = false)

            ControlsPanel(specs = scope.specs, onReset = scope::resetAll)

            if (notes != null) {
                ChromeSurface(modifier = Modifier.fillMaxWidth(), level = ChromeLevel.Inset) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(ArecaTheme.spacing.xl),
                        verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
                        content = notes,
                    )
                }
            }
        }
    }
}

/**
 * The area the device sits in, scaled to fit whatever height the window has.
 *
 * @param device Phone shape to render at.
 * @param canvasState Theme selection for the preview.
 * @param modifier Modifier applied to the preview area.
 * @param content The screen, which is expected to wrap itself in a device frame.
 */
@Composable
private fun FlowPreview(
    device: DeviceSize,
    canvasState: PreviewCanvasState,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val dark = when (canvasState.theme) {
        PreviewTheme.Inherit -> LocalPlaygroundDarkTheme.current
        PreviewTheme.Light -> false
        PreviewTheme.Dark -> true
    }

    ArecaThemeProvider(darkTheme = dark) {
        BoxWithConstraints(
            modifier = modifier.background(ArecaTheme.colors.bgSubtle),
            contentAlignment = Alignment.Center,
        ) {
            val available = maxHeight - PreviewPadding * 2
            // Never scale up: a phone drawn larger than life is a phone nobody can judge.
            val scale = (available / device.height).coerceAtMost(1f)

            Box(
                modifier = Modifier.padding(PreviewPadding),
                contentAlignment = Alignment.Center,
            ) {
                // Pages read this instead of choosing a scale, so one page cannot drift from the
                // fit the window actually allows.
                CompositionLocalProvider(LocalDeviceScale provides scale) {
                    content()
                }
            }
        }
    }
}

/**
 * Title, badges and summary, stacked for the narrow control column.
 *
 * @param title Screen or part being documented.
 * @param summary One sentence saying what it is for.
 * @param module Owning artifact.
 * @param status API maturity.
 */
@Composable
private fun FlowShowcaseHeader(
    title: String,
    summary: String,
    module: String?,
    status: ShowcaseStatus,
) {
    ChromeText(text = title, role = ChromeTextRole.PageTitle)

    Row(
        horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (module != null) {
            ChromeBadge(text = module, tone = ChromeBadgeTone.Neutral)
        }
        if (status != ShowcaseStatus.Stable) {
            ChromeBadge(text = status.name, tone = ChromeBadgeTone.Caution)
        }
    }

    ChromeText(text = summary, role = ChromeTextRole.PageSummary)
}
