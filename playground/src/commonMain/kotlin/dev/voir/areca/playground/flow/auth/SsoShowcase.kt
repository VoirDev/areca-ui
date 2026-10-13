package dev.voir.areca.playground.flow.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import dev.voir.areca.flow.auth.AuthBranding
import dev.voir.areca.flow.auth.AuthLayout
import dev.voir.areca.flow.auth.AuthProvider
import dev.voir.areca.flow.auth.part.AuthHeader
import dev.voir.areca.flow.auth.part.AuthScaffold
import dev.voir.areca.flow.auth.part.LegalConsent
import dev.voir.areca.flow.auth.sso.SsoGroup
import dev.voir.areca.flow.auth.sso.SsoLayout
import dev.voir.areca.playground.showcase.FlowShowcase
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.canvas.DeviceFrame
import dev.voir.areca.playground.showcase.canvas.DeviceSize
import dev.voir.areca.playground.showcase.canvas.windowInsets
import dev.voir.areca.playground.showcase.chrome.ChromeText
import dev.voir.areca.playground.showcase.chrome.ChromeTextRole
import dev.voir.areca.playground.showcase.chrome.ChromeTone
import dev.voir.areca.theme.ArecaTheme

/**
 * The provider buttons, on a screen that is nothing but providers.
 *
 * A form is not the only way into an account, and for several products it is not the main one.
 * This page is that case: header, artwork, a block of providers and a legal line, with no fields
 * anywhere. It is also where the two [SsoLayout] treatments are compared, because the choice
 * between them only makes sense against a real provider count.
 */
@Composable
fun SsoShowcase() = FlowShowcase(
    title = "Single sign-on",
    summary = "Provider buttons in both treatments, on a screen with no form at all.",
    module = "flow-auth",
    status = ShowcaseStatus.Beta,
    notes = {
        ChromeText(text = "Brand colours follow the providers", role = ChromeTextRole.Label)
        ChromeText(
            text = "Each provider's fill and mark follow its published button guidelines rather " +
                "than ArecaTheme. Switch the preview to dark: Apple turns white and Google takes " +
                "its dark theme, as their guidelines ask; Facebook keeps its blue.",
            role = ChromeTextRole.SectionDescription,
            tone = ChromeTone.Muted,
        )
        ChromeText(text = "Choosing a treatment", role = ChromeTextRole.Label)
        ChromeText(
            text = "Buttons when single sign-on is the offer. Icons when it is the alternative " +
                "to a form — four providers cost four rows as buttons and one row as icons.",
            role = ChromeTextRole.SectionDescription,
            tone = ChromeTone.Muted,
        )
    },
) {
    val providerSet by enumControl(
        key = "providers",
        default = AuthProviderSet.All,
        options = AuthProviderSet.entries,
        optionLabel = { it.label },
    )
    val ssoLayout by enumControl(
        key = "layout",
        default = SsoLayout.Buttons,
        options = SsoLayout.entries,
        optionLabel = { it.name },
    )
    val hero by booleanControl("hero artwork", default = true)
    val rule by booleanControl(
        key = "rule",
        default = false,
        label = "labelled rule",
        description = "A block with nothing above it has nothing to be separated from.",
    )
    val legal by booleanControl("legal line", default = true)
    val busy by booleanControl(
        key = "busy",
        default = false,
        description = "An exchange in flight: that button spins, the rest go inert.",
    )

    preview {
        val providers = providerSet.providers

        DeviceFrame(size = DeviceSize.Regular) {
            AuthScaffold(
                branding = AuthBranding(
                    hero = if (hero) ({ AuthSamples.Hero() }) else null,
                    layout = if (hero) {
                        AuthLayout.FullBleed
                    } else {
                        AuthLayout.Plain
                    },
                ),
                windowInsets = DeviceSize.Regular.windowInsets,
                footer = if (legal) {
                    ({ LegalConsent(links = AuthSamples.Legal, onLinkClick = {}) })
                } else {
                    null
                },
            ) {
                AuthHeader(
                    title = "Welcome back",
                    subtitle = "Sign in to pick up where you left off.",
                )

                Spacer(Modifier.height(ArecaTheme.spacing.xl6))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
                ) {
                    SsoGroup(
                        providers = providers,
                        onProviderClick = {},
                        layout = ssoLayout,
                        enabled = !busy,
                        busyProvider = if (busy) providers.firstOrNull() else null,
                        dividerText = if (rule) "or continue with" else null,
                    )
                }
            }
        }
    }
}
