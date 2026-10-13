package dev.voir.areca.playground.showcase.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.playground.showcase.ShowcaseScope
import dev.voir.areca.playground.showcase.ShowcaseSectionContainer
import dev.voir.areca.playground.showcase.canvas.PreviewCanvas
import dev.voir.areca.playground.showcase.canvas.PreviewCanvasState
import dev.voir.areca.playground.showcase.canvas.PreviewCanvasToolbar
import dev.voir.areca.playground.showcase.canvas.rememberPreviewCanvasState
import dev.voir.areca.playground.showcase.chrome.ChromeText
import dev.voir.areca.playground.showcase.chrome.ChromeTextRole
import dev.voir.areca.theme.ArecaTheme

/** Default cell width for grid and horizontal layouts. */
private val CellMinWidth = 200.dp
private val CellMaxWidth = 320.dp

/**
 * How a set of named instances is laid out.
 */
enum class VariantLayout {
    /** Wrapping cells. Use for a matrix, such as style × color. */
    Grid,

    /** A single scrollable lane. Use for an ordered ramp, such as sizes. */
    Horizontal,

    /** Full-width rows. Use when each instance needs the width. */
    Vertical,
}

/**
 * One named instance in a variant, size, state or edge-case section.
 */
internal class VariantEntry(
    val name: String,
    val detail: String?,
    val content: @Composable () -> Unit,
)

/**
 * Builder for a set of named instances.
 *
 * A plain (non-composable) builder: it collects entries holding composable lambdas, and re-runs on
 * each composition, so entries can be generated from `for` loops over enum entries.
 */
class VariantScope internal constructor() {

    internal val entries = mutableListOf<VariantEntry>()

    /**
     * Adds one named instance.
     *
     * @param name The prop values this cell shows, written exactly — `Solid / Brand`, not
     *   `Primary button`.
     * @param detail Optional supporting line, such as the concrete dp a size resolves to.
     * @param content The instance.
     */
    fun variant(name: String, detail: String? = null, content: @Composable () -> Unit) {
        entries += VariantEntry(name, detail, content)
    }
}

/**
 * A scannable matrix of the component's visual combinations.
 *
 * Faster to read than switching playground controls repeatedly, and the section where a missing or
 * broken combination becomes obvious.
 *
 * @param title Section heading.
 * @param description One sentence saying what the reader should look for here.
 * @param layout How the cells are arranged.
 * @param canvasState Background and theme selection, shared by every cell.
 * @param content Cell declarations.
 */
@Composable
fun ShowcaseScope.Variants(
    title: String,
    description: String? = null,
    layout: VariantLayout = VariantLayout.Grid,
    canvasState: PreviewCanvasState = rememberPreviewCanvasState(),
    content: VariantScope.() -> Unit,
) {
    val scope = VariantScope().apply(content)

    ShowcaseSectionContainer(title = title, description = description) {
        PreviewCanvasToolbar(state = canvasState)
        VariantCells(entries = scope.entries, layout = layout, canvasState = canvasState)
    }
}

/**
 * The component's size ramp, side by side.
 *
 * Put the concrete dp in each cell's `detail` so the ramp can be checked against the spec without
 * opening the source.
 *
 * @param title Section heading.
 * @param description One sentence saying what the reader should look for here.
 * @param layout How the cells are arranged.
 * @param canvasState Background and theme selection, shared by every cell.
 * @param content Cell declarations, in ascending size order.
 */
@Composable
fun ShowcaseScope.Sizes(
    title: String = "Sizes",
    description: String? = "The full size ramp, smallest first.",
    layout: VariantLayout = VariantLayout.Horizontal,
    canvasState: PreviewCanvasState = rememberPreviewCanvasState(),
    content: VariantScope.() -> Unit,
) {
    val scope = VariantScope().apply(content)

    ShowcaseSectionContainer(title = title, description = description) {
        PreviewCanvasToolbar(state = canvasState)
        VariantCells(entries = scope.entries, layout = layout, canvasState = canvasState)
    }
}

/**
 * The interaction and content states the component can be in.
 *
 * Include disabled always, and loading, selected and error wherever the component has them. This is
 * the section visual QA is done against.
 *
 * @param title Section heading.
 * @param description One sentence saying what the reader should look for here.
 * @param layout How the cells are arranged.
 * @param canvasState Background and theme selection, shared by every cell.
 * @param content Cell declarations.
 */
@Composable
fun ShowcaseScope.States(
    title: String = "States",
    description: String? = "Every state the component can be in, in one place.",
    layout: VariantLayout = VariantLayout.Grid,
    canvasState: PreviewCanvasState = rememberPreviewCanvasState(),
    content: VariantScope.() -> Unit,
) {
    val scope = VariantScope().apply(content)

    ShowcaseSectionContainer(title = title, description = description) {
        PreviewCanvasToolbar(state = canvasState)
        VariantCells(entries = scope.entries, layout = layout, canvasState = canvasState)
    }
}

@Composable
internal fun VariantCells(
    entries: List<VariantEntry>,
    layout: VariantLayout,
    canvasState: PreviewCanvasState,
) {
    when (layout) {
        VariantLayout.Grid -> FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg),
            verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg),
        ) {
            entries.forEach { entry ->
                VariantCell(
                    entry = entry,
                    canvasState = canvasState,
                    modifier = Modifier.widthIn(min = CellMinWidth, max = CellMaxWidth),
                )
            }
        }

        VariantLayout.Horizontal -> LazyRow(
            horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg),
        ) {
            items(entries) { entry ->
                VariantCell(
                    entry = entry,
                    canvasState = canvasState,
                    modifier = Modifier.width(CellMinWidth),
                )
            }
        }

        VariantLayout.Vertical -> Column(
            verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg),
        ) {
            entries.forEach { entry ->
                VariantCell(
                    entry = entry,
                    canvasState = canvasState,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
private fun VariantCell(
    entry: VariantEntry,
    canvasState: PreviewCanvasState,
    modifier: Modifier = Modifier,
    cellPadding: Dp = 0.dp,
) {
    Column(
        modifier = modifier.padding(cellPadding),
        verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.sm),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ChromeText(text = entry.name, role = ChromeTextRole.Label, maxLines = 1)
            if (entry.detail != null) {
                ChromeText(text = entry.detail, role = ChromeTextRole.Detail, maxLines = 1)
            }
        }

        PreviewCanvas(
            state = canvasState,
            showToolbar = false,
            contentAlignment = Alignment.CenterStart,
            content = entry.content,
        )
    }
}

/**
 * Builder for edge cases.
 *
 * Separate from [VariantScope] because the second string means something different here: not a
 * detail about the instance, but what the reader should expect to see. Writing it down is what turns
 * a regression into an obvious failure instead of a shrug.
 */
class EdgeCaseScope internal constructor() {

    internal val entries = mutableListOf<VariantEntry>()

    /**
     * Adds one edge case.
     *
     * @param name The condition being applied — "Very long label", "Narrow container".
     * @param expectation What should happen, stated plainly: "Truncates at one line rather than
     *   wrapping."
     * @param content The instance under that condition.
     */
    fun case(name: String, expectation: String, content: @Composable () -> Unit) {
        entries += VariantEntry(name, expectation, content)
    }
}

/**
 * The conditions a component has to survive: long text, no text, overflow, a container far smaller
 * or larger than the happy path, missing optional slots.
 *
 * @param title Section heading.
 * @param description One sentence saying what the reader should look for here.
 * @param layout How the cells are arranged.
 * @param canvasState Background and theme selection, shared by every cell.
 * @param content Case declarations.
 */
@Composable
fun ShowcaseScope.EdgeCases(
    title: String = "Edge cases",
    description: String? = "What the component does when the input is not the happy path.",
    layout: VariantLayout = VariantLayout.Vertical,
    canvasState: PreviewCanvasState = rememberPreviewCanvasState(),
    content: EdgeCaseScope.() -> Unit,
) {
    val scope = EdgeCaseScope().apply(content)

    ShowcaseSectionContainer(title = title, description = description) {
        PreviewCanvasToolbar(state = canvasState)
        VariantCells(entries = scope.entries, layout = layout, canvasState = canvasState)
    }
}

/**
 * Builder for realistic compositions.
 */
class RecipeScope internal constructor() {

    internal val entries = mutableListOf<VariantEntry>()

    /**
     * Adds one composition.
     *
     * @param name What the composition is — "Form footer", "Settings row".
     * @param description Where it is used, in one line.
     * @param content The composition, built from real content rather than placeholders.
     */
    fun recipe(name: String, description: String? = null, content: @Composable () -> Unit) {
        entries += VariantEntry(name, description, content)
    }
}

/**
 * Two or three realistic uses of the component in context.
 *
 * Answers the question the variant matrix cannot: what does this look like as part of a screen.
 *
 * @param title Section heading.
 * @param description One sentence saying what the reader should look for here.
 * @param canvasState Background and theme selection, shared by every cell.
 * @param content Recipe declarations.
 */
@Composable
fun ShowcaseScope.Recipes(
    title: String = "Recipes",
    description: String? = "The component doing its actual job, in context.",
    canvasState: PreviewCanvasState = rememberPreviewCanvasState(),
    content: RecipeScope.() -> Unit,
) {
    val scope = RecipeScope().apply(content)

    ShowcaseSectionContainer(title = title, description = description) {
        PreviewCanvasToolbar(state = canvasState)
        VariantCells(
            entries = scope.entries,
            layout = VariantLayout.Vertical,
            canvasState = canvasState,
        )
    }
}
