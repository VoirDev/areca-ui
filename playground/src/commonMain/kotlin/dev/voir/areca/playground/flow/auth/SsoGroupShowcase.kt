package dev.voir.areca.playground.flow.auth

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.voir.areca.flow.auth.AuthBranding
import dev.voir.areca.flow.auth.AuthLayout
import dev.voir.areca.flow.auth.AuthProvider
import dev.voir.areca.flow.auth.part.AuthHeader
import dev.voir.areca.flow.auth.part.AuthScaffold
import dev.voir.areca.flow.auth.part.LegalConsent
import dev.voir.areca.flow.auth.sso.SsoGroup
import dev.voir.areca.flow.auth.sso.SsoLayout
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.canvas.DeviceFrame
import dev.voir.areca.playground.showcase.canvas.DeviceSize
import dev.voir.areca.playground.showcase.canvas.windowInsets
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme

/** The rule label the page uses wherever it shows one. */
private const val RuleText: String = "or continue with"

/** Showcase for [SsoGroup] and the [SsoLayout] it arranges its buttons in. */
@Composable
fun SsoGroupShowcase() = ShowcaseScaffold(
    title = "SSO group",
    summary = "A block of provider buttons, stacked or in a row, optionally under a labelled rule.",
    module = "flow-auth",
    status = ShowcaseStatus.Beta,
) {
    Playground {
        val providerSet by enumControl(
            key = "providers",
            default = AuthProviderSet.AppleAndGoogle,
            options = AuthProviderSet.entries,
            optionLabel = { it.label },
            description = "None draws nothing at all, rule included.",
        )
        val layout by enumControl(
            key = "layout",
            default = SsoLayout.Buttons,
            options = SsoLayout.entries,
            optionLabel = { it.name },
            description = "Icons costs one row whatever the provider count.",
        )
        val rule by booleanControl(
            key = "rule",
            default = true,
            description = "Off passes dividerText = null.",
        )
        val ruleText by textControl("rule text", default = RuleText)
        val customLabels by booleanControl(
            key = "custom labels",
            default = false,
            description = "providerLabel: button text, or a mark's accessibility label.",
        )
        val busyProvider by enumControl<AuthProvider?>(
            key = "busy provider",
            default = null,
            options = listOf(null) + AuthProvider.entries,
            optionLabel = { it?.name ?: "None" },
            description = "Spins that provider's button.",
        )
        val enabled by booleanControl(
            key = "enabled",
            default = true,
            description = "Turn off while any attempt is in flight.",
        )

        preview {
            if (customLabels) {
                SsoGroup(
                    providers = providerSet.providers,
                    onProviderClick = {},
                    layout = layout,
                    enabled = enabled,
                    busyProvider = busyProvider,
                    dividerText = ruleText.takeIf { rule },
                    providerLabel = { it.signInLabel },
                )
            } else {
                SsoGroup(
                    providers = providerSet.providers,
                    onProviderClick = {},
                    layout = layout,
                    enabled = enabled,
                    busyProvider = busyProvider,
                    dividerText = ruleText.takeIf { rule },
                )
            }
        }
    }

    Variants(
        title = "Layout",
        description = "Buttons grow a row per provider; Icons stay one row at every count.",
        layout = VariantLayout.Vertical,
    ) {
        val sets = listOf(AuthProviderSet.Google, AuthProviderSet.AppleAndGoogle, AuthProviderSet.All)
        SsoLayout.entries.forEach { ssoLayout ->
            sets.forEach { set ->
                variant(name = "${ssoLayout.name} / ${set.label}") {
                    SsoGroup(
                        providers = set.providers,
                        onProviderClick = {},
                        layout = ssoLayout,
                        dividerText = RuleText,
                    )
                }
            }
        }
    }

    Variants(
        title = "Rule",
        description = "A labelled rule when the block follows something, none when it stands alone.",
        layout = VariantLayout.Vertical,
    ) {
        variant(name = "Rule", detail = "dividerText = \"$RuleText\"") {
            SsoGroup(
                providers = AuthProviderSet.AppleAndGoogle.providers,
                onProviderClick = {},
                dividerText = RuleText,
            )
        }
        variant(name = "No rule", detail = "dividerText = null") {
            SsoGroup(providers = AuthProviderSet.AppleAndGoogle.providers, onProviderClick = {})
        }
    }

    Variants(
        title = "Provider labels",
        description = "The English default, or the application's own wording; on a mark it is the " +
            "accessibility label.",
        layout = VariantLayout.Vertical,
    ) {
        variant(name = "Default", detail = "providerLabel not passed") {
            SsoGroup(providers = AuthProviderSet.AppleAndGoogle.providers, onProviderClick = {})
        }
        variant(name = "Custom / Buttons", detail = "\"Sign in with …\"") {
            SsoGroup(
                providers = AuthProviderSet.AppleAndGoogle.providers,
                onProviderClick = {},
                providerLabel = { it.signInLabel },
            )
        }
        variant(name = "Custom / Icons", detail = "Heard, not seen") {
            SsoGroup(
                providers = AuthProviderSet.AppleAndGoogle.providers,
                onProviderClick = {},
                layout = SsoLayout.Icons,
                providerLabel = { it.signInLabel },
            )
        }
    }

    States(layout = VariantLayout.Vertical) {
        SsoLayout.entries.forEach { ssoLayout ->
            variant(name = "Default / ${ssoLayout.name}") {
                SsoGroup(
                    providers = AuthProviderSet.AppleAndGoogle.providers,
                    onProviderClick = {},
                    layout = ssoLayout,
                    dividerText = RuleText,
                )
            }
            variant(
                name = "Busy / ${ssoLayout.name}",
                detail = "busyProvider = Google, enabled = false",
            ) {
                SsoGroup(
                    providers = AuthProviderSet.AppleAndGoogle.providers,
                    onProviderClick = {},
                    layout = ssoLayout,
                    enabled = false,
                    busyProvider = AuthProvider.Google,
                    dividerText = RuleText,
                )
            }
            variant(name = "Disabled / ${ssoLayout.name}", detail = "enabled = false") {
                SsoGroup(
                    providers = AuthProviderSet.AppleAndGoogle.providers,
                    onProviderClick = {},
                    layout = ssoLayout,
                    enabled = false,
                    dividerText = RuleText,
                )
            }
        }
    }

    Recipes {
        recipe(
            name = "Over full-bleed artwork",
            description = "Inside AuthScaffold with AuthLayout.FullBleed the rule turns light; the " +
                "buttons keep their own fills.",
        ) {
            DeviceFrame(size = DeviceSize.Compact, scale = 0.6f) {
                AuthScaffold(
                    branding = AuthBranding(
                        hero = { AuthSamples.Hero() },
                        layout = AuthLayout.FullBleed,
                    ),
                    windowInsets = DeviceSize.Compact.windowInsets,
                    footer = { LegalConsent(links = AuthSamples.Legal, onLinkClick = {}) },
                ) {
                    AuthHeader(title = "Welcome back")
                    Spacer(Modifier.height(ArecaTheme.spacing.xl6))
                    SsoGroup(
                        providers = AuthProviderSet.All.providers,
                        onProviderClick = {},
                        layout = SsoLayout.Icons,
                        dividerText = RuleText,
                    )
                }
            }
        }
    }

    EdgeCases {
        case(
            name = "No providers",
            expectation = "Draws nothing — no rule and no empty space — even with dividerText given.",
        ) {
            SsoGroup(providers = emptyList(), onProviderClick = {}, dividerText = RuleText)
        }
        case(
            name = "Long rule text",
            expectation = "The lines either side shorten; the label stays on the rule.",
        ) {
            SsoGroup(
                providers = AuthProviderSet.Google.providers,
                onProviderClick = {},
                dividerText = "oder mit einem bestehenden Konto fortfahren",
            )
        }
        case(
            name = "Every provider as icons in a phone column",
            expectation = "Constrained to 312dp, a 360dp phone's column: all four marks fit on one row.",
        ) {
            SsoGroup(
                providers = AuthProviderSet.All.providers,
                onProviderClick = {},
                modifier = Modifier.width(312.dp),
                layout = SsoLayout.Icons,
                dividerText = RuleText,
            )
        }
        case(
            name = "Icons wider than their container",
            expectation = "Constrained to 160dp: the marks wrap onto a second centred row at full size " +
                "rather than being squeezed or clipped.",
        ) {
            SsoGroup(
                providers = AuthProviderSet.All.providers,
                onProviderClick = {},
                modifier = Modifier.width(160.dp),
                layout = SsoLayout.Icons,
            )
        }
        case(
            name = "Narrow container",
            expectation = "Constrained to 220dp: the pills fill it and long labels wrap.",
        ) {
            SsoGroup(
                providers = AuthProviderSet.AppleAndGoogle.providers,
                onProviderClick = {},
                modifier = Modifier.width(220.dp),
                dividerText = RuleText,
            )
        }
    }
}

/** This provider's label in the wording a product that says "Sign in" would use. */
private val AuthProvider.signInLabel: String
    get() = when (this) {
        AuthProvider.Apple -> "Sign in with Apple"
        AuthProvider.Google -> "Sign in with Google"
        AuthProvider.Facebook -> "Log in with Facebook"
        AuthProvider.Email -> "Use email instead"
    }
