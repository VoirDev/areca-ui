package dev.voir.areca.playground.foundation.icon

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import dev.voir.areca.foundation.icon.Chevron
import dev.voir.areca.foundation.icon.ChevronDirection
import dev.voir.areca.foundation.icon.ChevronSize
import dev.voir.areca.foundation.list.ListItem
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.Sizes
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme

/** Sizes offered by [ChevronSize], as a selectable list. */
private enum class ChevronSizeOption { Small, Medium, Large }

/**
 * Showcase for [Chevron].
 *
 * Drawn rather than a glyph, so it animates between directions instead of swapping assets.
 */
@Composable
fun ChevronShowcase() = ShowcaseScaffold(
    title = "Chevron",
    summary = "Directional affordance that animates when its direction changes.",
    module = "foundation",
) {
    Playground(description = "Change the direction and watch it rotate rather than swap.") {
        val direction by enumControl(
            key = "direction",
            default = ChevronDirection.Right,
            options = ChevronDirection.entries,
            optionLabel = { it.name },
        )
        val size by enumControl(
            key = "size",
            default = ChevronSizeOption.Medium,
            options = ChevronSizeOption.entries,
            optionLabel = { it.name },
        )

        preview {
            Chevron(direction = direction, size = size.resolve())
        }
    }

    Variants(
        title = "Directions",
        description = "One drawn shape rotated, which is why the transition is continuous.",
        layout = VariantLayout.Grid,
    ) {
        for (direction in ChevronDirection.entries) {
            variant(name = direction.name) {
                Chevron(direction = direction)
            }
        }
    }

    Sizes(description = "Stroke width scales with the size, so the weight stays even.") {
        for (option in ChevronSizeOption.entries) {
            variant(name = option.name) {
                Chevron(direction = ChevronDirection.Right, size = option.resolve())
            }
        }
    }

    Recipes {
        recipe(
            name = "Disclosure row",
            description = "The most common use: a row that opens something else.",
        ) {
            ListItem(
                title = { Text("Billing") },
                onClick = {},
                trailing = { Chevron(direction = ChevronDirection.Right) },
            )
        }
        recipe(
            name = "Expand and collapse",
            description = "Flipping the direction animates; this is the reason it is drawn.",
        ) {
            var expanded by remember { mutableStateOf(false) }

            ListItem(
                title = { Text(if (expanded) "Click to collapse" else "Click to expand") },
                onClick = { expanded = !expanded },
                trailing = {
                    Chevron(
                        direction = if (expanded) ChevronDirection.Up else ChevronDirection.Down,
                    )
                },
            )
        }
        recipe(
            name = "Breadcrumb separator",
            description = "Small size, muted colour, between path segments.",
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = "Workspace", variant = TextVariant.BodySmall)
                Chevron(
                    direction = ChevronDirection.Right,
                    size = ChevronSize.Small,
                    color = ArecaTheme.colors.fgMuted,
                )
                Text(text = "Projects", variant = TextVariant.BodySmall)
                Chevron(
                    direction = ChevronDirection.Right,
                    size = ChevronSize.Small,
                    color = ArecaTheme.colors.fgMuted,
                )
                Text(text = "Areca", variant = TextVariant.BodySmall)
            }
        }
    }

    EdgeCases {
        case(
            name = "Inherited colour",
            expectation = "Takes the ambient content colour when none is passed.",
        ) {
            Chevron(direction = ChevronDirection.Right)
        }
        case(
            name = "Explicit colour",
            expectation = "An explicit colour wins over the ambient one.",
        ) {
            Chevron(direction = ChevronDirection.Right, color = ArecaTheme.colors.fgDanger)
        }
    }
}

@Composable
private fun ChevronSizeOption.resolve(): ChevronSize = when (this) {
    ChevronSizeOption.Small -> ChevronSize.Small
    ChevronSizeOption.Medium -> ChevronSize.Medium
    ChevronSizeOption.Large -> ChevronSize.Large
}
