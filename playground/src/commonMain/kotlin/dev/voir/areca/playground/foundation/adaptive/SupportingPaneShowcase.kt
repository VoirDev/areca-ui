package dev.voir.areca.playground.foundation.adaptive

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.adaptive.AdaptiveGrid
import dev.voir.areca.foundation.adaptive.SupportingPane
import dev.voir.areca.foundation.badge.BadgeOption
import dev.voir.areca.foundation.badge.BadgeSelectionMode
import dev.voir.areca.foundation.badge.BadgeSize
import dev.voir.areca.foundation.badge.BadgeStyle
import dev.voir.areca.foundation.badge.FlowBadgeSelect
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.button.ButtonStyle
import dev.voir.areca.foundation.layout.VerticalDivider
import dev.voir.areca.foundation.overlay.FlowModalHeaderInsets
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.foundation.topbar.FilterAction
import dev.voir.areca.foundation.topbar.TopBar
import dev.voir.areca.foundation.topbar.TopBarSize
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.canvas.WindowFrame
import dev.voir.areca.playground.showcase.canvas.WindowPreset
import dev.voir.areca.playground.showcase.canvas.windowInsets
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.horizontalContentInsets

/**
 * Showcase for [SupportingPane].
 *
 * The sample is a perfume catalogue: results in an adaptive grid, and the family filters beside
 * them from 920dp or behind a "Filters" action below that.
 */
@Composable
fun SupportingPaneShowcase() = ShowcaseScaffold(
    title = "Supporting pane",
    summary = "Main content with supporting content beside it when there is room, and in a sheet when there is not.",
    module = "foundation",
    status = ShowcaseStatus.Beta,
) {
    Playground(
        description = "Below 920dp the filters move behind the Filters action. The sheet opens over the whole " +
            "playground window, and takes its form — sheet or dialog — from the chosen window.",
    ) {
        val preset by enumControl(
            key = "window",
            default = WindowPreset.Laptop,
            options = WindowPreset.entries,
            optionLabel = { it.label },
        )
        val header by booleanControl(
            key = "supportingHeader",
            default = true,
            description = "Above the supporting content, in the pane and in the sheet.",
        )

        preview {
            WindowFrame(preset = preset) {
                CatalogueScreen(preset = preset, withHeader = header)
            }
        }
    }

    Variants(
        title = "Every window",
        description = "The filters beside the results from 920dp; below it, a Filters action in the bar instead.",
        layout = VariantLayout.Grid,
    ) {
        for (preset in WindowPreset.entries) {
            variant(preset.label, detail = "${preset.width.value.toInt()} × ${preset.height.value.toInt()}dp") {
                WindowFrame(preset = preset, maxHeight = 280.dp) { CatalogueScreen(preset = preset) }
            }
        }
    }

    States(description = "The supporting content's two homes, and the results narrowed by it.") {
        variant("Inline, nothing chosen", detail = "isSupportingInline = true") {
            WindowFrame(preset = WindowPreset.TabletLandscape, maxHeight = 280.dp) {
                CatalogueScreen(preset = WindowPreset.TabletLandscape)
            }
        }
        variant("Inline, two families chosen", detail = "Woody, Amber") {
            WindowFrame(preset = WindowPreset.TabletLandscape, maxHeight = 280.dp) {
                CatalogueScreen(
                    preset = WindowPreset.TabletLandscape,
                    initialFamilies = setOf(PerfumeFamily.Woody, PerfumeFamily.Amber),
                )
            }
        }
        variant("In a sheet, two families chosen", detail = "isSupportingInline = false; the count is on the action") {
            WindowFrame(preset = WindowPreset.Phone, maxHeight = 280.dp) {
                CatalogueScreen(
                    preset = WindowPreset.Phone,
                    initialFamilies = setOf(PerfumeFamily.Woody, PerfumeFamily.Amber),
                )
            }
        }
    }

    EdgeCases(description = "Windows either side of 920dp, and a container that is not the window.") {
        case(
            name = "Tablet held upright",
            expectation = "820dp is under 920dp: the results take the width and the filters open in a sheet.",
        ) {
            WindowFrame(preset = WindowPreset.Tablet, maxHeight = 320.dp) {
                CatalogueScreen(preset = WindowPreset.Tablet)
            }
        }
        case(
            name = "Phone on its side",
            expectation = "852dp is under 920dp: a sheet again, and the grid keeps one short row on screen.",
        ) {
            WindowFrame(preset = WindowPreset.PhoneLandscape, maxHeight = 240.dp) {
                CatalogueScreen(preset = WindowPreset.PhoneLandscape)
            }
        }
        case(
            name = "800dp column on a desktop",
            expectation = "The layout is given 800dp, so the filters leave it; opened, they are a dialog, since the window is a desktop.",
        ) {
            WindowFrame(preset = WindowPreset.Desktop, maxHeight = 300.dp) {
                Row(modifier = Modifier.fillMaxSize()) {
                    Box(modifier = Modifier.width(800.dp).fillMaxHeight()) {
                        CatalogueScreen(preset = WindowPreset.Desktop)
                    }
                    VerticalDivider()
                    Box(modifier = Modifier.weight(1f).fillMaxHeight().padding(32.dp)) {
                        Text("Your bag is empty", variant = TextVariant.HeadingSmall)
                    }
                }
            }
        }
    }
}

/**
 * The catalogue screen, written as a consumer would: the chosen families and whether the sheet is
 * open are the screen's state, and [SupportingPane] decides where the filters go.
 *
 * @param preset Window the screen is drawn in, for its insets.
 * @param withHeader Whether the filters carry a title bar.
 * @param initialFamilies Families chosen when the screen first draws.
 */
@Composable
private fun CatalogueScreen(
    preset: WindowPreset,
    withHeader: Boolean = true,
    initialFamilies: Set<PerfumeFamily> = emptySet(),
) {
    var families by remember { mutableStateOf(initialFamilies) }
    var filtersOpen by remember { mutableStateOf(false) }
    val results = Perfumes.filter { families.isEmpty() || it.family in families }

    SupportingPane(
        supportingVisible = filtersOpen,
        onSupportingDismissRequest = { filtersOpen = false },
        modifier = Modifier.fillMaxSize().windowInsetsPadding(preset.windowInsets),
        supportingHeader = if (withHeader) {
            { TopBar(title = "Filters", size = TopBarSize.Compact, windowInsets = FlowModalHeaderInsets) }
        } else {
            null
        },
        main = {
            Column(modifier = Modifier.fillMaxSize()) {
                TopBar(
                    title = "Perfumes",
                    subtitle = "${results.size} of ${Perfumes.size}",
                    trailing = if (isSupportingInline) {
                        null
                    } else {
                        {
                            FilterAction(
                                onClick = { filtersOpen = true },
                                badge = families.size.takeIf { it > 0 }?.toString(),
                            )
                        }
                    },
                )
                AdaptiveGrid(
                    modifier = Modifier.weight(1f),
                    minCellWidth = 150.dp,
                    contentPadding = PaddingValues(bottom = ArecaTheme.spacing.xl),
                ) {
                    items(results, key = { it.id }) { PerfumeCard(it) }
                }
            }
        },
        supporting = {
            FamilyFilters(
                chosen = families,
                onChange = { families = it },
                onDone = if (isSupportingInline) null else ({ filtersOpen = false }),
            )
        },
    )
}

/**
 * The filters: family chips, a way to clear them and, in the sheet, a way to close it.
 *
 * @param chosen Families chosen.
 * @param onChange Called with the new set when a chip is pressed or the set is cleared.
 * @param onDone Closes the sheet, or `null` when the filters are on screen beside the results.
 */
@Composable
private fun FamilyFilters(
    chosen: Set<PerfumeFamily>,
    onChange: (Set<PerfumeFamily>) -> Unit,
    onDone: (() -> Unit)?,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalContentInsets()
            .padding(vertical = ArecaTheme.spacing.lg),
        verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg),
    ) {
        Text("Family", variant = TextVariant.BodySmall, color = TextColor.Muted)
        FlowBadgeSelect(
            options = PerfumeFamily.entries.map { BadgeOption(label = { Text(it.name) }, value = it) },
            onSelect = { option ->
                onChange(if (option.value in chosen) chosen - option.value else chosen + option.value)
            },
            selected = chosen.toList(),
            selectionMode = BadgeSelectionMode.Multiple,
            horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
            verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
            style = BadgeStyle.Chip,
            size = BadgeSize.Chip,
        )
        Row(horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md)) {
            Button(
                text = "Clear",
                onClick = { onChange(emptySet()) },
                enabled = chosen.isNotEmpty(),
                style = ButtonStyle.soft(ActionColor.Neutral),
            )
            if (onDone != null) {
                Button(text = "Show results", onClick = onDone)
            }
        }
    }
}
