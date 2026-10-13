package dev.voir.areca.playground.foundation.avatar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.avatar.Avatar
import dev.voir.areca.foundation.avatar.AvatarSize
import dev.voir.areca.foundation.badge.IndicatorDot
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.Sizes
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme

/** Sizes offered by [AvatarSize], as a selectable list. */
private enum class AvatarSizeOption(val diameter: String) {
    Tiny("20dp"),
    ExtraSmall("24dp"),
    Small("32dp"),
    Medium("40dp"),
    Large("48dp"),
    ExtraLarge("56dp"),
}

/** Showcase for [Avatar]. */
@Composable
fun AvatarShowcase() = ShowcaseScaffold(
    title = "Avatar",
    summary = "Identity marker showing initials, an image slot or a loading placeholder.",
    module = "foundation",
) {
    Playground {
        val letters by textControl("letters", default = "AM", placeholder = "Initials")
        val size by enumControl(
            key = "size",
            default = AvatarSizeOption.Medium,
            options = AvatarSizeOption.entries,
            optionLabel = { it.name },
        )
        val loading by booleanControl("loading", default = false)
        val square by booleanControl(
            key = "square",
            default = false,
            description = "Shape is a parameter; the default is a circle.",
        )

        preview {
            Avatar(
                letters = letters.takeIf { it.isNotEmpty() },
                size = size.resolve(),
                loading = loading,
                shape = if (square) RoundedCornerShape(ArecaTheme.radius.md) else CircleShape,
                contentDescription = "Avatar for $letters",
            )
        }
    }

    Sizes(description = "Diameter and the initials text scale together.") {
        for (option in AvatarSizeOption.entries) {
            variant(name = option.name, detail = option.diameter) {
                Avatar(letters = "AM", size = option.resolve(), contentDescription = null)
            }
        }
    }

    States(layout = VariantLayout.Grid) {
        variant("Initials") { Avatar(letters = "AM", contentDescription = null) }
        variant("Loading") { Avatar(loading = true, contentDescription = null) }
        variant("Empty", detail = "no letters, no content") { Avatar(contentDescription = null) }
        variant("Image slot") {
            Avatar(contentDescription = null) {
                Box(Modifier.fillMaxSize().background(ArecaTheme.colors.bgBrandBase))
            }
        }
    }

    Variants(
        title = "Shapes",
        description = "Content is clipped to whatever shape is passed.",
        layout = VariantLayout.Grid,
    ) {
        variant("Circle") {
            Avatar(letters = "AM", shape = CircleShape, contentDescription = null)
        }
        variant("Rounded square") {
            Avatar(
                letters = "AM",
                shape = RoundedCornerShape(ArecaTheme.radius.md),
                contentDescription = null,
            )
        }
        variant("Square") {
            Avatar(
                letters = "AM",
                shape = RoundedCornerShape(0.dp),
                contentDescription = null,
            )
        }
    }

    Recipes {
        recipe(
            name = "With a presence dot",
            description = "IndicatorDot overlaid at the corner — the usual online marker.",
        ) {
            Box {
                Avatar(letters = "AM", size = AvatarSize.Large, contentDescription = null)
                Box(modifier = Modifier.align(Alignment.BottomEnd).offset(x = 2.dp, y = 2.dp)) {
                    IndicatorDot(color = ArecaTheme.colors.bgSuccessBase)
                }
            }
        }
        recipe(
            name = "Stacked group",
            description = "Overlapping avatars for a member list, newest first.",
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy((-10).dp)) {
                listOf("AM", "DW", "RK", "JT").forEach { initials ->
                    Avatar(
                        letters = initials,
                        size = AvatarSize.Small,
                        contentDescription = null,
                    )
                }
            }
        }
        recipe(
            name = "Identity row",
            description = "Avatar beside the name it belongs to.",
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Avatar(letters = "AM", contentDescription = null)
                Text(text = "Alex Mercer", variant = TextVariant.BodyMedium)
            }
        }
    }

    EdgeCases {
        case(
            name = "One letter",
            expectation = "Stays centred; the avatar does not shrink to the glyph.",
        ) {
            Avatar(letters = "A", contentDescription = null)
        }
        case(
            name = "More than two letters",
            expectation = "Renders what it is given — trimming to initials is the caller's job.",
        ) {
            Avatar(letters = "ABCD", contentDescription = null)
        }
        case(
            name = "Loading at the smallest size",
            expectation = "Spinner scales with the avatar rather than overflowing it.",
        ) {
            Avatar(loading = true, size = AvatarSize.Tiny, contentDescription = null)
        }
    }
}

@Composable
private fun AvatarSizeOption.resolve(): AvatarSize = when (this) {
    AvatarSizeOption.Tiny -> AvatarSize.Tiny
    AvatarSizeOption.ExtraSmall -> AvatarSize.ExtraSmall
    AvatarSizeOption.Small -> AvatarSize.Small
    AvatarSizeOption.Medium -> AvatarSize.Medium
    AvatarSizeOption.Large -> AvatarSize.Large
    AvatarSizeOption.ExtraLarge -> AvatarSize.ExtraLarge
}
