package dev.voir.areca.playground.foundation.icon

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.icon.FeaturedIcon
import dev.voir.areca.foundation.icon.FeaturedIconSize
import dev.voir.areca.foundation.icon.FeaturedIconStyle
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.icons.AlertTriangle
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.Check
import dev.voir.areca.icons.Archive
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.Sizes
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme

/** The [FeaturedIconStyle] factories on the emphasis ladder, as a selectable list. */
private enum class FeaturedIconVariant {
    /** [FeaturedIconStyle.solid]. */
    Solid,

    /** [FeaturedIconStyle.soft]. */
    Soft,

    /** [FeaturedIconStyle.outlined]. */
    Outlined,
}

/** Sizes offered by [FeaturedIconSize], as a selectable list. */
private enum class FeaturedIconSizeOption(val container: String) {
    Tiny("24dp"),
    ExtraSmall("32dp"),
    Small("40dp"),
    Medium("48dp"),
    Large("56dp"),
    ExtraLarge("64dp"),
}

/** Showcase for [FeaturedIcon]. */
@Composable
fun FeaturedIconShowcase() = ShowcaseScaffold(
    title = "Featured icon",
    summary = "Emphasised icon container for empty states, dialogs and status callouts.",
    module = "foundation",
) {
    Playground {
        val variant by enumControl(
            key = "variant",
            default = FeaturedIconVariant.Soft,
            options = FeaturedIconVariant.entries,
            optionLabel = { it.name },
        )
        val color by enumControl(
            key = "color",
            default = ActionColor.Brand,
            options = ActionColor.entries,
            optionLabel = { it.name },
        )
        val size by enumControl(
            key = "size",
            default = FeaturedIconSizeOption.Medium,
            options = FeaturedIconSizeOption.entries,
            optionLabel = { it.name },
        )
        val circular by booleanControl(
            key = "circular",
            default = false,
            description = "Swaps the rounded square for a circle.",
        )

        preview {
            FeaturedIcon(
                imageVector = ArecaIcons.Check,
                contentDescription = null,
                style = variant.resolve(color),
                size = size.resolve(),
                shape = if (circular) CircleShape else RoundedCornerShape(ArecaTheme.radius.md),
            )
        }
    }

    Variants(
        title = "Variant × color",
        description = "Solid fills, soft tints and outlines across the semantic colors.",
        layout = VariantLayout.Grid,
    ) {
        for (variant in FeaturedIconVariant.entries) {
            for (color in ActionColor.entries) {
                variant(name = "${variant.name} / ${color.name}") {
                    FeaturedIcon(
                        imageVector = ArecaIcons.Check,
                        contentDescription = null,
                        style = variant.resolve(color),
                    )
                }
            }
        }
    }

    Sizes(description = "Container size; the icon is half the container by default.") {
        for (size in FeaturedIconSizeOption.entries) {
            variant(name = size.name, detail = size.container) {
                FeaturedIcon(
                    imageVector = ArecaIcons.Check,
                    contentDescription = null,
                    style = FeaturedIconStyle.soft(ActionColor.Brand),
                    size = size.resolve(),
                )
            }
        }
    }

    Recipes {
        recipe(
            name = "Empty state",
            description = "The usual home for a large featured icon.",
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg),
            ) {
                FeaturedIcon(
                    imageVector = ArecaIcons.Archive,
                    contentDescription = null,
                    style = FeaturedIconStyle.soft(ActionColor.Neutral),
                    size = FeaturedIconSize.ExtraLarge,
                )
                Text(text = "No archived items", variant = TextVariant.HeadingExtraSmall)
                Text(
                    text = "Items you archive will appear here for thirty days.",
                    variant = TextVariant.BodySmall,
                    color = TextColor.Subtle,
                )
            }
        }
        recipe(
            name = "Destructive dialog header",
            description = "Danger colour carries the warning before the copy does.",
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg)) {
                FeaturedIcon(
                    imageVector = ArecaIcons.AlertTriangle,
                    contentDescription = null,
                    style = FeaturedIconStyle.soft(ActionColor.Danger),
                )
                Text(text = "Delete workspace?", variant = TextVariant.HeadingExtraSmall)
                Text(
                    text = "This removes every project and cannot be undone.",
                    variant = TextVariant.BodySmall,
                    color = TextColor.Subtle,
                )
            }
        }
    }

    EdgeCases {
        case(
            name = "Circular shape",
            expectation = "Shape is a parameter; the icon stays centred and keeps its size.",
        ) {
            FeaturedIcon(
                imageVector = ArecaIcons.Check,
                contentDescription = null,
                style = FeaturedIconStyle.solid(ActionColor.Success),
                size = FeaturedIconSize.Large,
                shape = CircleShape,
            )
        }
        case(
            name = "Warning on solid",
            expectation = "Warning uses dark content on its light fill, unlike the other colors.",
        ) {
            FeaturedIcon(
                imageVector = ArecaIcons.AlertTriangle,
                contentDescription = null,
                style = FeaturedIconStyle.solid(ActionColor.Warning),
                size = FeaturedIconSize.Large,
            )
        }
    }
}

/**
 * Builds this step's style for [color].
 *
 * @param color Color family.
 * @return The featured-icon style.
 */
@Composable
private fun FeaturedIconVariant.resolve(color: ActionColor): FeaturedIconStyle = when (this) {
    FeaturedIconVariant.Solid -> FeaturedIconStyle.solid(color)
    FeaturedIconVariant.Soft -> FeaturedIconStyle.soft(color)
    FeaturedIconVariant.Outlined -> FeaturedIconStyle.outlined(color)
}

@Composable
private fun FeaturedIconSizeOption.resolve(): FeaturedIconSize = when (this) {
    FeaturedIconSizeOption.Tiny -> FeaturedIconSize.Tiny
    FeaturedIconSizeOption.ExtraSmall -> FeaturedIconSize.ExtraSmall
    FeaturedIconSizeOption.Small -> FeaturedIconSize.Small
    FeaturedIconSizeOption.Medium -> FeaturedIconSize.Medium
    FeaturedIconSizeOption.Large -> FeaturedIconSize.Large
    FeaturedIconSizeOption.ExtraLarge -> FeaturedIconSize.ExtraLarge
}
