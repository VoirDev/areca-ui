package dev.voir.areca.playground.flow.auth

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.voir.areca.flow.auth.AuthProvider
import dev.voir.areca.flow.auth.sso.SsoButton
import dev.voir.areca.foundation.button.ButtonSize
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Sizes
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants

/** The button size presets the page offers, smallest first. */
private enum class SsoButtonSizeOption(
    /** Label of the option in the control panel. */
    val label: String,
    /** Minimum height the preset resolves to, for a cell's detail line. */
    val detail: String,
) {
    /** [ButtonSize.ExtraSmall]. */
    ExtraSmall("ExtraSmall", "32dp tall"),

    /** [ButtonSize.Small]. */
    Small("Small", "40dp tall"),

    /** [ButtonSize.Medium], the default. */
    Medium("Medium", "48dp tall, default"),

    /** [ButtonSize.Large]. */
    Large("Large", "56dp tall"),

    /** [ButtonSize.ExtraLarge]. */
    ExtraLarge("ExtraLarge", "64dp tall"),
    ;

    /** The preset this option stands for. */
    val size: ButtonSize
        @Composable
        get() = when (this) {
            ExtraSmall -> ButtonSize.ExtraSmall
            Small -> ButtonSize.Small
            Medium -> ButtonSize.Medium
            Large -> ButtonSize.Large
            ExtraLarge -> ButtonSize.ExtraLarge
        }
}

/** Showcase for [SsoButton]. */
@Composable
fun SsoButtonShowcase() = ShowcaseScaffold(
    title = "SSO button",
    summary = "A provider's labelled sign-in button, in the provider's own brand colours, for when " +
        "single sign-on is a considered choice.",
    module = "flow-auth",
    status = ShowcaseStatus.Beta,
) {
    Playground {
        val provider by enumControl(
            key = "provider",
            default = AuthProvider.Google,
            options = AuthProvider.entries,
            optionLabel = { it.name },
        )
        val label by textControl(
            key = "label",
            default = "",
            placeholder = "Continue with …",
            description = "Empty keeps the English default, \"Continue with\" and the provider's name.",
        )
        val size by enumControl(
            key = "size",
            default = SsoButtonSizeOption.Medium,
            options = SsoButtonSizeOption.entries,
            optionLabel = { it.label },
        )
        val fill by booleanControl(
            key = "fill",
            default = true,
            description = "Expand to the available width, as under a form.",
        )
        val enabled by booleanControl("enabled", default = true)
        val loading by booleanControl(
            key = "loading",
            default = false,
            description = "This provider's exchange is in flight.",
        )

        preview {
            val modifier = if (fill) Modifier.fillMaxWidth() else Modifier
            if (label.isEmpty()) {
                SsoButton(
                    provider = provider,
                    onClick = {},
                    modifier = modifier,
                    size = size.size,
                    enabled = enabled,
                    loading = loading,
                )
            } else {
                SsoButton(
                    provider = provider,
                    onClick = {},
                    modifier = modifier,
                    label = label,
                    size = size.size,
                    enabled = enabled,
                    loading = loading,
                )
            }
        }
    }

    Variants(
        title = "Provider",
        description = "Every provider with its default label; the fills are the providers' own and " +
            "do not follow the theme, except Email's.",
        layout = VariantLayout.Vertical,
    ) {
        AuthProvider.entries.forEach { provider ->
            variant(
                name = provider.name,
                detail = if (provider == AuthProvider.Email) "Theme colours, envelope" else null,
            ) {
                SsoButton(provider = provider, onClick = {}, modifier = Modifier.fillMaxWidth())
            }
        }
    }

    Variants(
        title = "Label",
        description = "The English default, or the application's own wording.",
        layout = VariantLayout.Vertical,
    ) {
        variant(name = "Default", detail = "label not passed") {
            SsoButton(provider = AuthProvider.Apple, onClick = {}, modifier = Modifier.fillMaxWidth())
        }
        variant(name = "Custom", detail = "label = \"Sign up with Apple\"") {
            SsoButton(
                provider = AuthProvider.Apple,
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                label = "Sign up with Apple",
            )
        }
    }

    Sizes(description = "The foundation button presets, smallest first; Medium is the default.") {
        SsoButtonSizeOption.entries.forEach { option ->
            variant(name = option.label, detail = option.detail) {
                SsoButton(
                    provider = AuthProvider.Google,
                    onClick = {},
                    label = "Google",
                    size = option.size,
                )
            }
        }
    }

    States(layout = VariantLayout.Vertical) {
        variant(name = "Default") {
            SsoButton(provider = AuthProvider.Apple, onClick = {}, modifier = Modifier.fillMaxWidth())
        }
        variant(name = "Disabled", detail = "Grey fill, grey mark") {
            SsoButton(
                provider = AuthProvider.Apple,
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                enabled = false,
            )
        }
        variant(name = "Disabled / Google", detail = "The four-colour mark fades rather than greys") {
            SsoButton(
                provider = AuthProvider.Google,
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                enabled = false,
            )
        }
        variant(name = "Loading", detail = "Spinner at the end; the mark stays; presses are ignored") {
            SsoButton(
                provider = AuthProvider.Apple,
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                loading = true,
            )
        }
    }

    EdgeCases {
        case(
            name = "Very long label",
            expectation = "Wraps inside the pill; the mark stays at the start.",
        ) {
            SsoButton(
                provider = AuthProvider.Facebook,
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                label = "Mit Facebook fortfahren und dein bestehendes Konto verknüpfen",
            )
        }
        case(
            name = "Narrow container",
            expectation = "Constrained to 200dp: the label wraps rather than pushing the pill wider.",
        ) {
            SsoButton(
                provider = AuthProvider.Facebook,
                onClick = {},
                modifier = Modifier.width(200.dp),
            )
        }
        case(
            name = "Content width",
            expectation = "Without a width modifier the pill hugs its mark and label.",
        ) {
            SsoButton(provider = AuthProvider.Google, onClick = {})
        }
        case(
            name = "Email",
            expectation = "No brand of its own: theme colours and an envelope, laid out like its peers.",
        ) {
            SsoButton(provider = AuthProvider.Email, onClick = {}, modifier = Modifier.fillMaxWidth())
        }
    }
}
