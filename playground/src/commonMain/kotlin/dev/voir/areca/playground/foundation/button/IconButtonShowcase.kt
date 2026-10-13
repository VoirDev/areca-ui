package dev.voir.areca.playground.foundation.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.foundation.button.IconButton
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.IconButtonSize
import dev.voir.areca.foundation.button.IconButtonStyle
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.Cross
import dev.voir.areca.icons.DotsHorizontal
import dev.voir.areca.icons.Plus
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.Sizes
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme

/** Style families offered by [IconButtonStyle], as a selectable list. */
private enum class IconButtonVariant { Solid, Soft, Outlined, Plain }

/** Sizes offered by [IconButtonSize], as a selectable list. */
private enum class IconButtonSizeOption { Tiny, ExtraSmall, Small, Medium, Large, ExtraLarge }

/** Showcase for [IconButton]. */
@Composable
fun IconButtonShowcase() = ShowcaseScaffold(
    title = "Icon button",
    summary = "A button whose whole content is an icon, for actions that need no label.",
    module = "foundation",
) {
    Playground {
        val variant by enumControl(
            key = "variant",
            default = IconButtonVariant.Solid,
            options = IconButtonVariant.entries,
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
            default = IconButtonSizeOption.Medium,
            options = IconButtonSizeOption.entries,
            optionLabel = { it.name },
        )
        val enabled by booleanControl("enabled", default = true)
        val loading by booleanControl("loading", default = false)
        val circular by booleanControl("circular", default = false)

        preview {
            IconButton(
                onClick = {},
                style = variant.resolve(color).copy(
                    shape = if (circular) CircleShape else ArecaTheme.shapes.md,
                ),
                size = size.resolve(),
                enabled = enabled,
                loading = loading,
            ) {
                Icon(imageVector = ArecaIcons.Plus, contentDescription = "Add")
            }
        }
    }

    Variants(
        title = "Variant × color",
        description = "The same matrix Button offers, applied to an icon-only target.",
        layout = VariantLayout.Grid,
    ) {
        for (variant in IconButtonVariant.entries) {
            for (color in ActionColor.entries) {
                variant(name = "${variant.name} / ${color.name}") {
                    IconButton(onClick = {}, style = variant.resolve(color)) {
                        Icon(imageVector = ArecaIcons.Plus, contentDescription = null)
                    }
                }
            }
        }
    }

    Sizes(description = "Container and icon scale together; the hit target follows the container.") {
        for (size in IconButtonSizeOption.entries) {
            variant(name = size.name) {
                IconButton(
                    onClick = {},
                    style = IconButtonStyle.solid(ActionColor.Brand),
                    size = size.resolve(),
                ) {
                    Icon(imageVector = ArecaIcons.Plus, contentDescription = null)
                }
            }
        }
    }

    States(layout = VariantLayout.Grid) {
        variant("Default") {
            IconButton(onClick = {}) { Icon(imageVector = ArecaIcons.Plus, contentDescription = null) }
        }
        variant("Disabled") {
            IconButton(onClick = {}, enabled = false) {
                Icon(imageVector = ArecaIcons.Plus, contentDescription = null)
            }
        }
        variant("Loading") {
            IconButton(onClick = {}, loading = true) {
                Icon(imageVector = ArecaIcons.Plus, contentDescription = null)
            }
        }
        variant("Not clickable", detail = "enabled, inert") {
            IconButton(onClick = null) {
                Icon(imageVector = ArecaIcons.Plus, contentDescription = null)
            }
        }
    }

    Recipes {
        recipe(
            name = "Toolbar cluster",
            description = "Plain style keeps a row of actions quiet until hovered.",
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xs)) {
                IconButton(onClick = {}, style = IconButtonStyle.plain(ActionColor.Neutral)) {
                    Icon(imageVector = ArecaIcons.Plus, contentDescription = "Add")
                }
                IconButton(onClick = {}, style = IconButtonStyle.plain(ActionColor.Neutral)) {
                    Icon(imageVector = ArecaIcons.DotsHorizontal, contentDescription = "More")
                }
                IconButton(onClick = {}, style = IconButtonStyle.plain(ActionColor.Danger)) {
                    Icon(imageVector = ArecaIcons.Cross, contentDescription = "Remove")
                }
            }
        }
        recipe(
            name = "Dismiss affordance",
            description = "A circular plain button in the corner of a surface.",
        ) {
            IconButton(
                onClick = {},
                style = IconButtonStyle.soft(ActionColor.Neutral, shape = CircleShape),
                size = IconButtonSize.Small,
            ) {
                Icon(imageVector = ArecaIcons.Cross, contentDescription = "Close")
            }
        }
    }

    EdgeCases {
        case(
            name = "Smallest size",
            expectation = "Still a usable hit target; the icon shrinks with the container.",
        ) {
            IconButton(onClick = {}, size = IconButtonSize.Tiny) {
                Icon(imageVector = ArecaIcons.Plus, contentDescription = null)
            }
        }
        case(
            name = "Loading at the smallest size",
            expectation = "Spinner fits inside rather than overflowing the container.",
        ) {
            IconButton(onClick = {}, size = IconButtonSize.Tiny, loading = true) {
                Icon(imageVector = ArecaIcons.Plus, contentDescription = null)
            }
        }
    }
}

@Composable
private fun IconButtonVariant.resolve(color: ActionColor): IconButtonStyle = when (this) {
    IconButtonVariant.Solid -> IconButtonStyle.solid(color)
    IconButtonVariant.Soft -> IconButtonStyle.soft(color)
    IconButtonVariant.Outlined -> IconButtonStyle.outlined(color)
    IconButtonVariant.Plain -> IconButtonStyle.plain(color)
}

@Composable
private fun IconButtonSizeOption.resolve(): IconButtonSize = when (this) {
    IconButtonSizeOption.Tiny -> IconButtonSize.Tiny
    IconButtonSizeOption.ExtraSmall -> IconButtonSize.ExtraSmall
    IconButtonSizeOption.Small -> IconButtonSize.Small
    IconButtonSizeOption.Medium -> IconButtonSize.Medium
    IconButtonSizeOption.Large -> IconButtonSize.Large
    IconButtonSizeOption.ExtraLarge -> IconButtonSize.ExtraLarge
}
