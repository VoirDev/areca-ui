package dev.voir.areca.playground.showcase.section

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import dev.voir.areca.playground.showcase.ShowcaseScope
import dev.voir.areca.playground.showcase.ShowcaseSectionContainer
import dev.voir.areca.playground.showcase.canvas.PreviewCanvas
import dev.voir.areca.playground.showcase.canvas.PreviewCanvasState
import dev.voir.areca.playground.showcase.canvas.rememberPreviewCanvasState
import dev.voir.areca.playground.showcase.control.ControlsPanel
import dev.voir.areca.playground.showcase.control.PlaygroundScope

/**
 * One live instance with its props wired to controls.
 *
 * The section a designer or consumer lands on: it answers "what does this prop do" by letting them
 * change it. Declare controls at the top of the body, then call `preview` with the component wired
 * to them.
 *
 * The body composes inside the preview canvas and the control panel renders after it, so controls
 * are always registered before the panel reads them.
 *
 * @param title Section heading.
 * @param description One sentence saying what the reader should look for here.
 * @param canvasState Background and theme selection for the preview.
 * @param contentAlignment How the preview is placed on the canvas.
 * @param content Control declarations followed by a single `preview { }` call.
 */
@Composable
fun ShowcaseScope.Playground(
    title: String = "Playground",
    description: String? = "Adjust the props a consumer passes most often.",
    canvasState: PreviewCanvasState = rememberPreviewCanvasState(),
    contentAlignment: Alignment = Alignment.CenterStart,
    content: @Composable PlaygroundScope.() -> Unit,
) {
    val scope = remember { PlaygroundScope() }

    ShowcaseSectionContainer(title = title, description = description) {
        PreviewCanvas(
            state = canvasState,
            contentAlignment = contentAlignment,
        ) {
            scope.content()
        }

        ControlsPanel(
            specs = scope.specs,
            onReset = scope::resetAll,
        )
    }
}
