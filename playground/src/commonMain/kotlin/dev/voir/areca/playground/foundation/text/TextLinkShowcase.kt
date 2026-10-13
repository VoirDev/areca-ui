package dev.voir.areca.playground.foundation.text

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextLink
import dev.voir.areca.foundation.text.TextLinkDecoration
import dev.voir.areca.foundation.text.TextLinkPrompt
import dev.voir.areca.foundation.text.linkStyles
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.foundation.text.appendTextLink
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants

/** Colors a link is offered in. [TextColor] also carries roles a link never uses. */
private val LinkColors = listOf(
    TextColor.Brand,
    TextColor.Base,
    TextColor.Subtle,
    TextColor.Muted,
    TextColor.Danger,
    TextColor.Success,
    TextColor.Info,
    TextColor.Warning,
)

/** Showcase for [TextLink], [TextLinkPrompt] and [appendTextLink]. */
@Composable
fun TextLinkShowcase() = ShowcaseScaffold(
    title = "Text link",
    summary = "Navigates or acts from within running text, standalone or inline.",
    module = "foundation",
) {
    Playground {
        val label by textControl("text", default = "View invoice", placeholder = "Link text")
        val color by enumControl(
            key = "color",
            default = TextColor.Brand,
            options = LinkColors,
            optionLabel = { it.name },
        )
        val decoration by enumControl(
            key = "decoration",
            default = TextLinkDecoration.OnInteraction,
            options = TextLinkDecoration.entries,
            optionLabel = { it.name },
        )
        val enabled by booleanControl("enabled", default = true)

        preview {
            TextLink(
                text = label,
                color = color,
                decoration = decoration,
                enabled = enabled,
                onClick = {},
            )
        }
    }

    Variants(
        title = "Colors",
        description = "Hover each: the resting and hovered colors are different tokens.",
        layout = VariantLayout.Grid,
    ) {
        for (color in LinkColors) {
            variant(name = color.name) {
                TextLink(text = color.name, color = color, onClick = {})
            }
        }
    }

    Variants(
        title = "Decorations",
        description = "When the underline appears.",
        layout = VariantLayout.Grid,
    ) {
        for (decoration in TextLinkDecoration.entries) {
            variant(name = decoration.name) {
                TextLink(text = "View invoice", decoration = decoration, onClick = {})
            }
        }
    }

    States {
        variant("Enabled") { TextLink(text = "View invoice", onClick = {}) }
        variant("Disabled") { TextLink(text = "View invoice", enabled = false, onClick = {}) }
    }

    Recipes {
        recipe(
            name = "Prompt",
            description = "A question with its answer as the link — sign-up and sign-in footers.",
        ) {
            TextLinkPrompt(
                prompt = "Don't have an account yet?",
                linkText = "Sign up",
                modifier = Modifier.fillMaxWidth(),
                onLinkClick = {},
            )
        }
        recipe(
            name = "Destructive prompt",
            description = "The same shape carrying a warning rather than an invitation.",
        ) {
            TextLinkPrompt(
                prompt = "Deleting this workspace cannot be undone.",
                linkText = "Delete workspace",
                modifier = Modifier.fillMaxWidth(),
                linkColor = TextColor.Danger,
                decoration = TextLinkDecoration.OnInteraction,
                onLinkClick = {},
            )
        }
        recipe(
            name = "Inline in a sentence",
            description = "Built with appendTextLink so the links flow with the surrounding text.",
        ) {
            val terms = buildAnnotatedString {
                append("By continuing you agree to our ")
                appendTextLink(
                    text = "Terms of Service",
                    styles = TextColor.Brand.linkStyles,
                    onClick = {},
                )
                append(" and ")
                appendTextLink(
                    text = "Privacy Policy",
                    styles = TextColor.Brand.linkStyles,
                    onClick = {},
                )
                append('.')
            }

            Text(text = terms, variant = TextVariant.BodySmall, color = TextColor.Subtle)
        }
    }

    EdgeCases {
        case(
            name = "Click feedback",
            expectation = "Every link above is live; this one reports what was clicked.",
        ) {
            var lastClicked by remember { mutableStateOf<String?>(null) }

            Text(
                text = lastClicked?.let { "Last clicked: $it" } ?: "Nothing clicked yet",
                variant = TextVariant.BodySmall,
                color = TextColor.Muted,
            )
            TextLink(text = "Click me", onClick = { lastClicked = "Click me" })
        }
        case(
            name = "Long link in a narrow column",
            expectation = "Wraps across lines with the underline following each line.",
        ) {
            val sentence = buildAnnotatedString {
                append("Read the ")
                appendTextLink(
                    text = "full terms and conditions for enterprise customers",
                    styles = TextColor.Brand.linkStyles,
                    onClick = {},
                )
                append(" before continuing.")
            }

            Text(text = sentence, variant = TextVariant.BodySmall)
        }
    }
}
