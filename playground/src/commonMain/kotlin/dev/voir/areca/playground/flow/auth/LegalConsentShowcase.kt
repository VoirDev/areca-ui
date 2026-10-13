package dev.voir.areca.playground.flow.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.flow.auth.AuthBranding
import dev.voir.areca.flow.auth.AuthLayout
import dev.voir.areca.flow.auth.part.AuthScaffold
import dev.voir.areca.flow.auth.part.LegalConsent
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.section.Anatomy
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme

/** The alignments the sentence's `textAlign` is offered at. */
private enum class ConsentAlignment(
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

/** Height of the artwork a cell draws the sentence over, as the footer of a short scaffold. */
private val ArtworkHeight: Dp = 160.dp

/** Showcase for [LegalConsent]. */
@Composable
fun LegalConsentShowcase() = ShowcaseScaffold(
    title = "Legal consent",
    summary = "The \"by continuing, you agree to…\" line, with the terms and the privacy policy " +
        "pressable inside one sentence.",
    module = "flow-auth",
    status = ShowcaseStatus.Beta,
) {
    Playground(
        description = "Reword every piece of the sentence; press a link to see the address handed back.",
    ) {
        val sentence by textControl("sentence", default = "By continuing, you agree to our %1\$s and %2\$s.")
        val termsLabel by textControl("termsLabel", default = "Terms of Service")
        val privacyLabel by textControl("privacyLabel", default = "Privacy Policy")
        val alignment by enumControl(
            key = "textAlign",
            default = ConsentAlignment.Center,
            options = ConsentAlignment.entries,
            optionLabel = { it.label },
        )

        preview {
            var pressed by remember { mutableStateOf<String?>(null) }
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
            ) {
                // A sentence being edited passes through states that drop a placeholder, which the
                // part refuses; the page says so instead of drawing it.
                if (holdsBothLinks(sentence)) {
                    LegalConsent(
                        links = AuthSamples.Legal,
                        onLinkClick = { pressed = it },
                        textAlign = alignment.align,
                        sentence = sentence,
                        termsLabel = termsLabel,
                        privacyLabel = privacyLabel,
                    )
                } else {
                    Text(
                        text = "The sentence needs %1\$s and %2\$s exactly once each.",
                        variant = TextVariant.BodyExtraSmall,
                        color = TextColor.Danger,
                    )
                }
                // Stands in for whatever the application does with the address: the part opens nothing.
                Text(
                    text = pressed?.let { "onLinkClick(\"$it\")" } ?: "Press a link",
                    variant = TextVariant.BodyExtraSmall,
                    color = TextColor.Subtle,
                )
            }
        }
    }

    Anatomy(description = "One sentence template and two link labels.") {
        slot(
            name = "sentence",
            description = "The whole sentence, with %1\$s for the terms link and %2\$s for the privacy " +
                "link, so a translation can reword the clause and order the documents.",
        ) {
            LegalConsent(links = AuthSamples.Legal, onLinkClick = {})
        }
        slot(
            name = "termsLabel, privacyLabel",
            description = "Link annotations inside the sentence; each hands its address from links back.",
        ) {
            LegalConsent(links = AuthSamples.Legal, onLinkClick = {})
        }
    }

    Variants(
        title = "Alignment",
        description = "textAlign places the sentence within the full width it always takes.",
        layout = VariantLayout.Vertical,
    ) {
        for (alignment in ConsentAlignment.entries) {
            variant(name = alignment.label, detail = "textAlign = TextAlign.${alignment.label}") {
                LegalConsent(links = AuthSamples.Legal, onLinkClick = {}, textAlign = alignment.align)
            }
        }
    }

    Variants(
        title = "Wording",
        description = "The sentence and the labels are the application's; the links go wherever its placeholders put them.",
        layout = VariantLayout.Vertical,
    ) {
        variant(name = "Defaults", detail = "\"By continuing, you agree to our …\"") {
            LegalConsent(links = AuthSamples.Legal, onLinkClick = {})
        }
        variant(name = "Sign-up wording", detail = "Words around and after the links changed") {
            LegalConsent(
                links = AuthSamples.Legal,
                onLinkClick = {},
                sentence = "By creating an account, you accept the %1\$s and acknowledge the %2\$s " +
                    "that apply in your region.",
            )
        }
        variant(name = "Translated", detail = "Every string passed in German") {
            LegalConsent(
                links = AuthSamples.Legal,
                onLinkClick = {},
                sentence = "Mit dem Fortfahren stimmst du unseren %1\$s und der %2\$s zu.",
                termsLabel = "Nutzungsbedingungen",
                privacyLabel = "Datenschutzerklärung",
            )
        }
        variant(name = "Privacy first", detail = "%2\$s before %1\$s: the translation orders the links") {
            LegalConsent(
                links = AuthSamples.Legal,
                onLinkClick = {},
                sentence = "Read our %2\$s, then accept our %1\$s to continue.",
            )
        }
    }

    Variants(
        title = "Ground",
        description = "In a full-bleed AuthScaffold's footer the sentence and its links turn light.",
        layout = VariantLayout.Vertical,
    ) {
        variant(name = "Page background", detail = "Muted sentence, brand links") {
            LegalConsent(links = AuthSamples.Legal, onLinkClick = {})
        }
        variant(name = "Over artwork", detail = "AuthLayout.FullBleed: all OnContrast") {
            AuthScaffold(
                modifier = Modifier.fillMaxWidth().height(ArtworkHeight),
                branding = AuthBranding(hero = { AuthSamples.Hero() }, layout = AuthLayout.FullBleed),
                windowInsets = WindowInsets(0, 0, 0, 0),
                footer = { LegalConsent(links = AuthSamples.Legal, onLinkClick = {}) },
            ) {}
        }
    }

    Recipes {
        recipe(
            name = "Under the sign-up button",
            description = "Agreement given by pressing the button above it, so no checkbox is needed.",
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg),
            ) {
                Button(text = "Create account", onClick = {}, modifier = Modifier.fillMaxWidth())
                LegalConsent(
                    links = AuthSamples.Legal,
                    onLinkClick = {},
                    sentence = "By creating an account, you agree to our %1\$s and %2\$s.",
                )
            }
        }
    }

    EdgeCases {
        case(
            name = "Narrow container",
            expectation = "Constrained to 200dp: wraps as a sentence, and a link breaks across lines with its words.",
        ) {
            LegalConsent(links = AuthSamples.Legal, onLinkClick = {}, modifier = Modifier.width(200.dp))
        }
        case(
            name = "Long wording",
            expectation = "Wraps onto as many lines as it needs; nothing is cut and the links stay pressable.",
        ) {
            LegalConsent(
                links = AuthSamples.Legal,
                onLinkClick = {},
                sentence = "By continuing, you confirm that you are at least 16 years old and agree to be " +
                    "bound by our %1\$s, and that you have read our %2\$s, which explains how we use " +
                    "your data.",
                termsLabel = "Terms of Service, including the arbitration clause",
                privacyLabel = "Privacy and Cookie Policy",
            )
        }
        case(
            name = "Ends on a link",
            expectation = "Nothing after %2\$s: the sentence ends on the privacy link.",
        ) {
            LegalConsent(
                links = AuthSamples.Legal,
                onLinkClick = {},
                sentence = "By continuing, you agree to our %1\$s and %2\$s",
            )
        }
    }
}

/**
 * Whether a sentence holds each link's placeholder exactly once, which is what [LegalConsent] needs.
 *
 * @param sentence Template being edited.
 * @return `true` when it can be drawn.
 */
private fun holdsBothLinks(sentence: String): Boolean =
    sentence.split("%1\$s").size == 2 && sentence.split("%2\$s").size == 2
