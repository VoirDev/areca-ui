package dev.voir.areca.playground.flow.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.voir.areca.flow.auth.AuthProvider
import dev.voir.areca.flow.auth.sso.SsoIconButton
import dev.voir.areca.foundation.button.IconButtonSize
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.Sizes
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme

/** The icon-button size presets the page offers, smallest first. */
private enum class SsoIconSizeOption(
    /** Label of the option in the control panel. */
    val label: String,
    /** Diameter the preset resolves to, for a cell's detail line. */
    val detail: String,
) {
    /** [IconButtonSize.Tiny]. */
    Tiny("Tiny", "20dp"),

    /** [IconButtonSize.ExtraSmall]. */
    ExtraSmall("ExtraSmall", "32dp"),

    /** [IconButtonSize.Small]. */
    Small("Small", "40dp"),

    /** [IconButtonSize.Medium]. */
    Medium("Medium", "48dp"),

    /** [IconButtonSize.Large]. */
    Large("Large", "56dp"),

    /** [IconButtonSize.ExtraLarge]. */
    ExtraLarge("ExtraLarge", "64dp"),
    ;

    /** The preset this option stands for. */
    val size: IconButtonSize
        @Composable
        get() = when (this) {
            Tiny -> IconButtonSize.Tiny
            ExtraSmall -> IconButtonSize.ExtraSmall
            Small -> IconButtonSize.Small
            Medium -> IconButtonSize.Medium
            Large -> IconButtonSize.Large
            ExtraLarge -> IconButtonSize.ExtraLarge
        }
}

/** Showcase for [SsoIconButton]. */
@Composable
fun SsoIconButtonShowcase() = ShowcaseScaffold(
    title = "SSO icon button",
    summary = "A provider's sign-in button reduced to its mark, for a compact row of alternatives " +
        "beside a form.",
    module = "flow-auth",
    status = ShowcaseStatus.Beta,
) {
    Playground {
        val provider by enumControl(
            key = "provider",
            default = AuthProvider.Apple,
            options = AuthProvider.entries,
            optionLabel = { it.name },
        )
        val contentDescription by textControl(
            key = "content description",
            default = "",
            placeholder = "Continue with …",
            description = "Read by a screen reader. Empty keeps the English default.",
        )
        val size by enumControl<SsoIconSizeOption?>(
            key = "size",
            default = null,
            options = listOf(null) + SsoIconSizeOption.entries,
            optionLabel = { it?.label ?: "Default" },
            description = "Default reads the icon-button size in scope.",
        )
        val enabled by booleanControl("enabled", default = true)
        val loading by booleanControl(
            key = "loading",
            default = false,
            description = "This provider's exchange is in flight.",
        )

        preview {
            val chosen = size
            when {
                chosen == null && contentDescription.isEmpty() -> SsoIconButton(
                    provider = provider,
                    onClick = {},
                    enabled = enabled,
                    loading = loading,
                )

                chosen == null -> SsoIconButton(
                    provider = provider,
                    onClick = {},
                    contentDescription = contentDescription,
                    enabled = enabled,
                    loading = loading,
                )

                contentDescription.isEmpty() -> SsoIconButton(
                    provider = provider,
                    onClick = {},
                    size = chosen.size,
                    enabled = enabled,
                    loading = loading,
                )

                else -> SsoIconButton(
                    provider = provider,
                    onClick = {},
                    contentDescription = contentDescription,
                    size = chosen.size,
                    enabled = enabled,
                    loading = loading,
                )
            }
        }
    }

    Variants(
        title = "Provider",
        description = "Every provider's mark on its own fill; only Email follows the theme.",
        layout = VariantLayout.Horizontal,
    ) {
        AuthProvider.entries.forEach { provider ->
            variant(
                name = provider.name,
                detail = if (provider == AuthProvider.Email) "Theme colours, envelope" else null,
            ) {
                SsoIconButton(provider = provider, onClick = {})
            }
        }
    }

    Sizes(description = "The foundation icon-button presets, smallest first; the mark scales with the circle.") {
        SsoIconSizeOption.entries.forEach { option ->
            variant(name = option.label, detail = option.detail) {
                SsoIconButton(provider = AuthProvider.Google, onClick = {}, size = option.size)
            }
        }
    }

    States(layout = VariantLayout.Horizontal) {
        variant(name = "Default") {
            SsoIconButton(provider = AuthProvider.Facebook, onClick = {})
        }
        variant(name = "Disabled", detail = "Grey fill, grey mark") {
            SsoIconButton(provider = AuthProvider.Facebook, onClick = {}, enabled = false)
        }
        variant(name = "Disabled / Google", detail = "Four-colour mark fades rather than greys") {
            SsoIconButton(provider = AuthProvider.Google, onClick = {}, enabled = false)
        }
        variant(name = "Loading", detail = "Spinner in the circle") {
            SsoIconButton(provider = AuthProvider.Facebook, onClick = {}, loading = true)
        }
    }

    Recipes {
        recipe(
            name = "Row of peers",
            description = "Several providers side by side, as an alternative offered under a form.",
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    space = ArecaTheme.spacing.lg,
                    alignment = Alignment.CenterHorizontally,
                ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                listOf(AuthProvider.Apple, AuthProvider.Google, AuthProvider.Facebook).forEach {
                    SsoIconButton(provider = it, onClick = {})
                }
            }
        }
    }

    EdgeCases(layout = VariantLayout.Horizontal) {
        case(
            name = "Email",
            expectation = "No brand of its own: an envelope in theme colours, the same diameter as its peers.",
        ) {
            SsoIconButton(provider = AuthProvider.Email, onClick = {})
        }
    }
}
