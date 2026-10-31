package dev.voir.areca.playground.foundation.adaptive

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.adaptive.AdaptiveDefaults
import dev.voir.areca.foundation.adaptive.AdaptiveGrid
import dev.voir.areca.foundation.topbar.TopBar
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.canvas.WindowFrame
import dev.voir.areca.playground.showcase.canvas.WindowPreset
import dev.voir.areca.playground.showcase.canvas.windowInsets
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme

/** The cap on how wide the cells grow together, as the playground offers it. */
private enum class GridMaxWidthOption(val width: Dp) {
    ContentWidth(AdaptiveDefaults.ContentWidth),
    ReadableWidth(AdaptiveDefaults.ReadableWidth),
}

/** The playground's stand-in for "no cap" on the column count. */
private const val NoColumnCap = 9f

/**
 * Showcase for [AdaptiveGrid].
 *
 * The sample is a perfume catalogue: as many columns as fit cells of the minimum width, with the
 * cells stopping at the content width on a monitor.
 */
@Composable
fun AdaptiveGridShowcase() = ShowcaseScaffold(
    title = "Adaptive grid",
    summary = "A scrolling grid of cards whose column count follows the width it is given.",
    module = "foundation",
    status = ShowcaseStatus.Beta,
) {
    Playground(description = "Change the window or the minimum cell: a column is dropped as soon as a cell would be narrower.") {
        val preset by enumControl(
            key = "window",
            default = WindowPreset.TabletLandscape,
            options = WindowPreset.entries,
            optionLabel = { it.label },
        )
        val minCellWidth by numberControl(
            key = "minCellWidth",
            default = AdaptiveDefaults.GridMinCellWidth.value,
            range = 100f..360f,
            step = 20f,
            format = { "${it.toInt()}dp" },
            description = "Narrowest a cell may be before a column is dropped.",
        )
        val maxColumns by numberControl(
            key = "maxColumns",
            default = NoColumnCap,
            range = 1f..NoColumnCap,
            step = 1f,
            format = { if (it >= NoColumnCap) "No cap" else it.toInt().toString() },
            description = "Most columns the grid lays out, however wide it is.",
        )
        val maxWidth by enumControl(
            key = "maxWidth",
            default = GridMaxWidthOption.ContentWidth,
            options = GridMaxWidthOption.entries,
            optionLabel = { "${it.name} · ${it.width.value.toInt()}dp" },
            description = "Widest the cells grow together; the rest goes to the sides.",
        )

        preview {
            WindowFrame(preset = preset) {
                CatalogueGrid(
                    preset = preset,
                    minCellWidth = minCellWidth.dp,
                    maxColumns = if (maxColumns >= NoColumnCap) Int.MAX_VALUE else maxColumns.toInt(),
                    maxWidth = maxWidth.width,
                )
            }
        }
    }

    Variants(
        title = "Every window",
        description = "Two columns on a phone, four on a tablet, six at most on a monitor, with the defaults.",
        layout = VariantLayout.Grid,
    ) {
        for (preset in WindowPreset.entries) {
            variant(preset.label, detail = "${preset.width.value.toInt()} × ${preset.height.value.toInt()}dp") {
                WindowFrame(preset = preset, maxHeight = 280.dp) { CatalogueGrid(preset = preset) }
            }
        }
    }

    EdgeCases(description = "Caps and minimums that fight the window, and too few items to fill a row.") {
        case(
            name = "maxColumns = 2 on a laptop",
            expectation = "Two columns however wide the window; the cells grow until the 1200dp cap, then centre.",
        ) {
            WindowFrame(preset = WindowPreset.Laptop, maxHeight = 280.dp) {
                CatalogueGrid(preset = WindowPreset.Laptop, maxColumns = 2)
            }
        }
        case(
            name = "Minimum wider than the window",
            expectation = "A 480dp minimum on a 344dp phone still lays out one column, the full width less the gutters.",
        ) {
            WindowFrame(preset = WindowPreset.FoldFolded, maxHeight = 320.dp) {
                CatalogueGrid(preset = WindowPreset.FoldFolded, minCellWidth = 480.dp)
            }
        }
        case(
            name = "Fewer items than columns",
            expectation = "Two perfumes on a monitor take the first two of six columns; the row is not stretched to fill.",
        ) {
            WindowFrame(preset = WindowPreset.Desktop, maxHeight = 260.dp) {
                CatalogueGrid(preset = WindowPreset.Desktop, perfumes = Perfumes.take(2))
            }
        }
    }
}

/**
 * The catalogue as an [AdaptiveGrid] under a title bar, written as a consumer would.
 *
 * @param preset Window the grid is drawn in, for its insets.
 * @param minCellWidth Narrowest a cell may be.
 * @param maxColumns Most columns the grid lays out.
 * @param maxWidth Widest the cells grow together.
 * @param perfumes The perfumes to show.
 */
@Composable
private fun CatalogueGrid(
    preset: WindowPreset,
    minCellWidth: Dp = AdaptiveDefaults.GridMinCellWidth,
    maxColumns: Int = Int.MAX_VALUE,
    maxWidth: Dp = AdaptiveDefaults.ContentWidth,
    perfumes: List<Perfume> = Perfumes,
) {
    Column(modifier = Modifier.fillMaxSize().windowInsetsPadding(preset.windowInsets)) {
        TopBar(title = "New arrivals", subtitle = "${perfumes.size} perfumes")
        AdaptiveGrid(
            modifier = Modifier.weight(1f),
            minCellWidth = minCellWidth,
            maxColumns = maxColumns,
            maxWidth = maxWidth,
            contentPadding = PaddingValues(bottom = ArecaTheme.spacing.xl),
        ) {
            items(perfumes, key = { it.id }) { PerfumeCard(it) }
        }
    }
}
