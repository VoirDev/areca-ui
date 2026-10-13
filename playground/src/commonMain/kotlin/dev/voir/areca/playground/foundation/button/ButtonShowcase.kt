package dev.voir.areca.playground.foundation.button

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.ButtonSize
import dev.voir.areca.foundation.button.ButtonStyle
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.modifier.thenIf
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Sizes
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants

/**
 * Showcase for [Button].
 *
 * The reference page for the section builders — `SHOWCASES.md` §4 points here.
 */
@Composable
fun ButtonShowcase() = ShowcaseScaffold(
    title = "Button",
    summary = "Primary action control with variant, color, size and loading state.",
    module = "foundation",
) {
    Playground(description = "Adjust the props a consumer passes most often.") {
        val label by textControl("label", default = "Continue", placeholder = "Button label")
        val variant by enumControl(
            key = "variant",
            default = ButtonVariant.Solid,
            options = ButtonVariant.entries,
            optionLabel = { it.name },
            description = "Style family from ButtonStyle.",
        )
        val color by enumControl(
            key = "color",
            default = ActionColor.Brand,
            options = ActionColor.entries,
            optionLabel = { it.name },
            description = "Semantic color family.",
        )
        val size by enumControl(
            key = "size",
            default = ButtonSizeOption.Medium,
            options = ButtonSizeOption.entries,
            optionLabel = { it.name },
        )
        val enabled by booleanControl("enabled", default = true)
        val loading by booleanControl("loading", default = false)
        val clickable by booleanControl("clickable", default = true)
        val fill by booleanControl(
            key = "fill",
            default = false,
            description = "Expand to the available width.",
        )
        val withLeading by booleanControl("leading", default = false, label = "leading icon")
        val withTrailing by booleanControl("trailing", default = false, label = "trailing icon")

        val resolvedSize = size.resolve()

        preview {
            Button(
                style = variant.resolve(color),
                size = resolvedSize,
                enabled = enabled,
                loading = loading,
                modifier = Modifier.thenIf(fill) { fillMaxWidth() },
                leading = if (withLeading) {
                    { Text("←", style = resolvedSize.textStyle) }
                } else {
                    null
                },
                trailing = if (withTrailing) {
                    { Text("→", style = resolvedSize.textStyle) }
                } else {
                    null
                },
                onClick = if (clickable) ({}) else null,
            ) {
                Text(label)
            }
        }
    }

    Variants(
        title = "Style × color",
        description = "Every combination a product surface picks from.",
        layout = VariantLayout.Grid,
    ) {
        for (variant in ButtonVariant.entries) {
            for (color in ActionColor.entries) {
                variant(name = "${variant.name} / ${color.name}") {
                    Button(
                        style = variant.resolve(color),
                        size = ButtonSize.Medium,
                        onClick = {},
                    ) {
                        Text("Action")
                    }
                }
            }
        }
    }

    Sizes(description = "The full size ramp, with the minimum height each resolves to.") {
        for (option in ButtonSizeOption.entries) {
            variant(name = option.name, detail = option.minHeight) {
                Button(
                    style = ButtonStyle.solid(ActionColor.Brand),
                    size = option.resolve(),
                    onClick = {},
                ) {
                    Text("Continue")
                }
            }
        }
    }

    States(description = "Keep these visible together: a regression in one rarely shows in the others.") {
        variant("Default") {
            Button(style = ButtonStyle.solid(ActionColor.Brand), onClick = {}) { Text("Continue") }
        }
        variant("Disabled") {
            Button(
                style = ButtonStyle.solid(ActionColor.Brand),
                enabled = false,
                onClick = {},
            ) {
                Text("Continue")
            }
        }
        variant("Loading") {
            Button(
                style = ButtonStyle.solid(ActionColor.Brand),
                loading = true,
                onClick = {},
            ) {
                Text("Continue")
            }
        }
        variant("Not clickable", detail = "enabled, inert") {
            Button(
                style = ButtonStyle.solid(ActionColor.Brand),
                onClick = null,
            ) {
                Text("Continue")
            }
        }
        variant("Leading and trailing") {
            Button(
                style = ButtonStyle.outlined(ActionColor.Brand),
                leading = { Text("←") },
                trailing = { Text("→") },
                onClick = {},
            ) {
                Text("Continue")
            }
        }
        variant("Long label", detail = "stays on one line") {
            Button(style = ButtonStyle.solid(ActionColor.Brand), onClick = {}) {
                Text("Save and continue to billing details")
            }
        }
    }
}

/** Style families offered by [ButtonStyle], as a selectable list. */
private enum class ButtonVariant {
    Solid,
    Soft,
    Outlined,
    Plain,
}

/** Sizes offered by [ButtonSize], as a selectable list. */
private enum class ButtonSizeOption {
    ExtraSmall,
    Small,
    Medium,
    Large,
    ExtraLarge,
}

@Composable
private fun ButtonVariant.resolve(color: ActionColor): ButtonStyle = when (this) {
    ButtonVariant.Solid -> ButtonStyle.solid(color)
    ButtonVariant.Soft -> ButtonStyle.soft(color)
    ButtonVariant.Outlined -> ButtonStyle.outlined(color)
    ButtonVariant.Plain -> ButtonStyle.plain(color)
}

@Composable
private fun ButtonSizeOption.resolve(): ButtonSize = when (this) {
    ButtonSizeOption.ExtraSmall -> ButtonSize.ExtraSmall
    ButtonSizeOption.Small -> ButtonSize.Small
    ButtonSizeOption.Medium -> ButtonSize.Medium
    ButtonSizeOption.Large -> ButtonSize.Large
    ButtonSizeOption.ExtraLarge -> ButtonSize.ExtraLarge
}

private val ButtonSizeOption.minHeight: String
    get() = when (this) {
        ButtonSizeOption.ExtraSmall -> "32dp"
        ButtonSizeOption.Small -> "40dp"
        ButtonSizeOption.Medium -> "48dp"
        ButtonSizeOption.Large -> "56dp"
        ButtonSizeOption.ExtraLarge -> "64dp"
    }
