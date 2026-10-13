package dev.voir.areca.playground.showcase.section

import androidx.compose.runtime.Composable
import dev.voir.areca.playground.showcase.ShowcaseScope
import dev.voir.areca.playground.showcase.ShowcaseSectionContainer
import dev.voir.areca.playground.showcase.canvas.PreviewCanvasState
import dev.voir.areca.playground.showcase.canvas.PreviewCanvasToolbar
import dev.voir.areca.playground.showcase.canvas.rememberPreviewCanvasState

/**
 * Builder for a component's slots.
 */
class AnatomyScope internal constructor() {

    internal val entries = mutableListOf<VariantEntry>()

    /**
     * Names one slot and shows the component with that slot filled.
     *
     * @param name The slot's parameter name, exactly — `leading`, not "the icon on the left".
     * @param description What belongs in it, and whether it is required.
     * @param content The component demonstrating that slot.
     */
    fun slot(name: String, description: String, content: @Composable () -> Unit) {
        entries += VariantEntry(name, description, content)
    }
}

/**
 * The component's slots, named and demonstrated one at a time.
 *
 * Slot semantics are the part of a component API that source alone explains badly: `leading` and
 * `trailing` say where, not what. This section says what.
 *
 * @param title Section heading.
 * @param description One sentence saying what the reader should look for here.
 * @param canvasState Background and theme selection, shared by every cell.
 * @param content Slot declarations, in the order the component lays them out.
 */
@Composable
fun ShowcaseScope.Anatomy(
    title: String = "Anatomy",
    description: String? = "What each slot is for, shown one at a time.",
    canvasState: PreviewCanvasState = rememberPreviewCanvasState(),
    content: AnatomyScope.() -> Unit,
) {
    val scope = AnatomyScope().apply(content)

    ShowcaseSectionContainer(title = title, description = description) {
        PreviewCanvasToolbar(state = canvasState)
        VariantCells(
            entries = scope.entries,
            layout = VariantLayout.Vertical,
            canvasState = canvasState,
        )
    }
}
