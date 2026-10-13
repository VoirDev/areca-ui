package dev.voir.areca.playground.flow.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.flow.auth.AuthBranding
import dev.voir.areca.flow.auth.AuthLayout
import dev.voir.areca.flow.auth.part.AuthPasswordField
import dev.voir.areca.flow.auth.part.AuthScaffold
import dev.voir.areca.flow.auth.part.AuthTextLink
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme

/** The sample recovery link's label. */
private const val FORGOT_PASSWORD = "Forgot password?"

/** Height of the artwork a cell draws the link over; enough for one line and the scaffold's gaps. */
private val ArtworkHeight: Dp = 140.dp

/** Showcase for [AuthTextLink]. */
@Composable
fun AuthTextLinkShowcase() = ShowcaseScaffold(
    title = "Auth text link",
    summary = "A standalone pressable label on an authentication screen, such as \"Forgot password?\", " +
        "that stays legible over artwork.",
    module = "flow-auth",
    status = ShowcaseStatus.Beta,
) {
    Playground(description = "Change the label and disable it; hover and press to see the link respond.") {
        val text by textControl("text", default = FORGOT_PASSWORD)
        val enabled by booleanControl(
            key = "enabled",
            default = true,
            description = "A disabled link renders as plain disabled text and cannot be pressed.",
        )

        preview {
            AuthTextLink(text = text, onClick = {}, enabled = enabled)
        }
    }

    Variants(
        title = "Ground",
        description = "Inside a full-bleed AuthScaffold the brand colour gives way to a light foreground.",
        layout = VariantLayout.Vertical,
    ) {
        variant(name = "Page background", detail = "TextColor.Brand") {
            AuthTextLink(text = FORGOT_PASSWORD, onClick = {})
        }
        variant(name = "Over artwork", detail = "AuthLayout.FullBleed: TextColor.OnContrast") {
            AuthScaffold(
                modifier = Modifier.fillMaxWidth().height(ArtworkHeight),
                branding = AuthBranding(hero = { AuthSamples.Hero() }, layout = AuthLayout.FullBleed),
                windowInsets = WindowInsets(0, 0, 0, 0),
            ) {
                AuthTextLink(text = FORGOT_PASSWORD, onClick = {})
            }
        }
        variant(name = "Over artwork / disabled", detail = "enabled = false keeps TextColor.Disabled on artwork") {
            AuthScaffold(
                modifier = Modifier.fillMaxWidth().height(ArtworkHeight),
                branding = AuthBranding(hero = { AuthSamples.Hero() }, layout = AuthLayout.FullBleed),
                windowInsets = WindowInsets(0, 0, 0, 0),
            ) {
                AuthTextLink(text = FORGOT_PASSWORD, onClick = {}, enabled = false)
            }
        }
    }

    States(layout = VariantLayout.Vertical) {
        variant(name = "Enabled", detail = "Brand colour; responds to hover, focus and press") {
            AuthTextLink(text = FORGOT_PASSWORD, onClick = {})
        }
        variant(name = "Disabled", detail = "enabled = false: plain disabled text, not announced as a link") {
            AuthTextLink(text = FORGOT_PASSWORD, onClick = {}, enabled = false)
        }
    }

    Recipes {
        recipe(
            name = "Under the password box",
            description = "Aligned to the trailing edge, where a reader looks after failing to remember.",
        ) {
            var password by remember { mutableStateOf("") }
            Column(modifier = Modifier.fillMaxWidth()) {
                AuthPasswordField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(Modifier.height(ArecaTheme.spacing.lg))
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                    AuthTextLink(text = FORGOT_PASSWORD, onClick = {})
                }
            }
        }
        recipe(
            name = "Resend a code",
            description = "Centred under a confirmation step, disabled while a code is still on its way.",
        ) {
            var sent by remember { mutableStateOf(false) }
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                AuthTextLink(
                    text = if (sent) "Code sent. Check your inbox" else "Resend the code",
                    onClick = { sent = true },
                    enabled = !sent,
                )
            }
        }
    }

    EdgeCases {
        case(
            name = "Very long label",
            expectation = "Wraps onto a second line rather than being cut; the whole label stays pressable.",
        ) {
            AuthTextLink(
                text = "Can't get into your account? Recover it with your phone number or a backup code",
                onClick = {},
            )
        }
        case(
            name = "Narrow container",
            expectation = "Constrained to 120dp: the label wraps within it.",
        ) {
            AuthTextLink(text = FORGOT_PASSWORD, onClick = {}, modifier = Modifier.width(120.dp))
        }
    }
}
