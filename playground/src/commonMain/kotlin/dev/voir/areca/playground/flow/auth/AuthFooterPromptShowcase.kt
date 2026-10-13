package dev.voir.areca.playground.flow.auth

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.flow.auth.AuthBranding
import dev.voir.areca.flow.auth.AuthLayout
import dev.voir.areca.flow.auth.part.AuthFooterPrompt
import dev.voir.areca.flow.auth.part.AuthHeader
import dev.voir.areca.flow.auth.part.AuthScaffold
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.section.Anatomy
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants

/** The alignments the sentence's `textAlign` is offered at. */
private enum class PromptAlignment(
    /** Label of the option in the control panel. */
    val label: String,
    /** Alignment handed to the sentence. */
    val align: TextAlign,
) {
    /** Against the leading edge. */
    Start("Start", TextAlign.Start),

    /** Centred, the default. */
    Center("Center", TextAlign.Center),

    /** Against the trailing edge. */
    End("End", TextAlign.End),
}

/** The plain part of the sample sign-in footer. */
private const val SIGN_IN_PROMPT = "New here?"

/** The pressable part of the sample sign-in footer. */
private const val SIGN_IN_LINK = "Create account"

/** Height of the artwork a cell draws the sentence over, as the footer of a short scaffold. */
private val ArtworkHeight: Dp = 140.dp

/** Height of the short screen each recipe pins the sentence to the bottom of. */
private val RecipeHeight: Dp = 220.dp

/** Showcase for [AuthFooterPrompt]. */
@Composable
fun AuthFooterPromptShowcase() = ShowcaseScaffold(
    title = "Auth footer prompt",
    summary = "The sentence along the bottom edge of an authentication screen that offers the other " +
        "screen, with the link inside it.",
    module = "flow-auth",
    status = ShowcaseStatus.Beta,
) {
    Playground(description = "Change both halves of the sentence and where it sits in its width.") {
        val prompt by textControl("prompt", default = SIGN_IN_PROMPT)
        val linkText by textControl("linkText", default = SIGN_IN_LINK)
        val alignment by enumControl(
            key = "textAlign",
            default = PromptAlignment.Center,
            options = PromptAlignment.entries,
            optionLabel = { it.label },
        )

        preview {
            AuthFooterPrompt(
                prompt = prompt,
                linkText = linkText,
                onLinkClick = {},
                textAlign = alignment.align,
            )
        }
    }

    Anatomy(description = "One sentence: a plain part, a space, and a pressable part.") {
        slot(name = "prompt", description = "The part that is only text, in the subtle colour.") {
            AuthFooterPrompt(prompt = SIGN_IN_PROMPT, linkText = SIGN_IN_LINK, onLinkClick = {})
        }
        slot(
            name = "linkText",
            description = "The part that can be pressed — by click, tap or keyboard — in the brand colour.",
        ) {
            AuthFooterPrompt(prompt = SIGN_IN_PROMPT, linkText = SIGN_IN_LINK, onLinkClick = {})
        }
    }

    Variants(
        title = "Alignment",
        description = "textAlign places the sentence within the full width it always takes.",
        layout = VariantLayout.Vertical,
    ) {
        for (alignment in PromptAlignment.entries) {
            variant(name = alignment.label, detail = "textAlign = TextAlign.${alignment.label}") {
                AuthFooterPrompt(
                    prompt = SIGN_IN_PROMPT,
                    linkText = SIGN_IN_LINK,
                    onLinkClick = {},
                    textAlign = alignment.align,
                )
            }
        }
    }

    Variants(
        title = "Ground",
        description = "In a full-bleed AuthScaffold's footer both halves turn light, with no parameter passed.",
        layout = VariantLayout.Vertical,
    ) {
        variant(name = "Page background", detail = "Subtle prompt, brand link") {
            AuthFooterPrompt(prompt = SIGN_IN_PROMPT, linkText = SIGN_IN_LINK, onLinkClick = {})
        }
        variant(name = "Over artwork", detail = "AuthLayout.FullBleed: both OnContrast") {
            AuthScaffold(
                modifier = Modifier.fillMaxWidth().height(ArtworkHeight),
                branding = AuthBranding(hero = { AuthSamples.Hero() }, layout = AuthLayout.FullBleed),
                windowInsets = WindowInsets(0, 0, 0, 0),
                footer = {
                    AuthFooterPrompt(prompt = SIGN_IN_PROMPT, linkText = SIGN_IN_LINK, onLinkClick = {})
                },
            ) {}
        }
    }

    Recipes {
        recipe(name = "Sign-in footer", description = "Offers the sign-up screen from the bottom edge.") {
            AuthScaffold(
                modifier = Modifier.fillMaxWidth().height(RecipeHeight),
                windowInsets = WindowInsets(0, 0, 0, 0),
                footer = {
                    AuthFooterPrompt(prompt = SIGN_IN_PROMPT, linkText = SIGN_IN_LINK, onLinkClick = {})
                },
            ) {
                AuthHeader(title = "Welcome back", subtitle = "Sign in to pick up where you left off.")
            }
        }
        recipe(name = "Sign-up footer", description = "Offers the sign-in screen back.") {
            AuthScaffold(
                modifier = Modifier.fillMaxWidth().height(RecipeHeight),
                windowInsets = WindowInsets(0, 0, 0, 0),
                footer = {
                    AuthFooterPrompt(
                        prompt = "Already have an account?",
                        linkText = "Sign in",
                        onLinkClick = {},
                    )
                },
            ) {
                AuthHeader(title = "Create your account", subtitle = "It takes less than a minute.")
            }
        }
    }

    EdgeCases {
        case(
            name = "Long translation",
            expectation = "Wraps as one sentence; the link can break onto the next line with its words.",
        ) {
            AuthFooterPrompt(
                prompt = "Vous n'avez pas encore de compte pour votre espace de travail ?",
                linkText = "Créer un compte gratuitement",
                onLinkClick = {},
            )
        }
        case(
            name = "Narrow container",
            expectation = "Constrained to 160dp: the sentence wraps and stays centred.",
        ) {
            AuthFooterPrompt(
                prompt = SIGN_IN_PROMPT,
                linkText = SIGN_IN_LINK,
                onLinkClick = {},
                modifier = Modifier.width(160.dp),
            )
        }
    }
}
