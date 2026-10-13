package dev.voir.areca.playground.flow.auth

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.voir.areca.flow.auth.AuthBranding
import dev.voir.areca.flow.auth.AuthLayout
import dev.voir.areca.flow.auth.part.AuthProgress
import dev.voir.areca.flow.auth.part.AuthScaffold
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants

/** Showcase for [AuthProgress]. */
@Composable
fun AuthProgressShowcase() = ShowcaseScaffold(
    title = "Auth progress",
    summary = "One line saying what the screen is waiting on — a provider, a redirect, a link " +
        "check — with a spinner beside it.",
    module = "flow-auth",
    status = ShowcaseStatus.Beta,
) {
    Playground(description = "Change what the screen is waiting on.") {
        val text by textControl("text", default = "Waiting for Google…")

        preview {
            AuthProgress(text = text)
        }
    }

    Variants(
        title = "Moments",
        description = "The waits an authentication flow has, each worded by the application.",
        layout = VariantLayout.Vertical,
    ) {
        variant(name = "Provider window open") { AuthProgress(text = "Waiting for Google…") }
        variant(name = "Signed in, redirecting") { AuthProgress(text = "Returning to Areca…") }
        variant(name = "Link opened") { AuthProgress(text = "Checking your link…") }
    }

    Variants(
        title = "Ground",
        description = "Inside a full-bleed AuthScaffold the spinner and the line turn light.",
        layout = VariantLayout.Vertical,
    ) {
        variant(name = "Over artwork", detail = "AuthLayout.FullBleed") {
            AuthScaffold(
                modifier = Modifier.fillMaxWidth().height(96.dp),
                branding = AuthBranding(hero = { AuthSamples.Hero() }, layout = AuthLayout.FullBleed),
                windowInsets = WindowInsets(0, 0, 0, 0),
            ) {
                AuthProgress(text = "Returning to Areca…")
            }
        }
    }

    EdgeCases {
        case(
            name = "Long line in a narrow container",
            expectation = "Constrained to 200dp: the text wraps beside the spinner, which stays centred on it.",
        ) {
            AuthProgress(
                text = "Finishing signing you in with your organisation's identity provider…",
                modifier = Modifier.width(200.dp),
            )
        }
    }
}
