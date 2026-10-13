package dev.voir.areca.playground.flow.auth

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dev.voir.areca.flow.auth.AuthBranding
import dev.voir.areca.flow.auth.part.AuthEmailField
import dev.voir.areca.flow.auth.part.AuthFooterPrompt
import dev.voir.areca.flow.auth.part.AuthHeader
import dev.voir.areca.flow.auth.part.AuthPasswordField
import dev.voir.areca.flow.auth.part.AuthScaffold
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.field.FieldGroup
import dev.voir.areca.foundation.field.TextField
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextVariant
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
 * A sign-in screen with content of the product's own above, inside and below the form.
 *
 * The scaffold's body is an ordinary column, so there is no slot to find: a line above the form, a
 * field the module does not have, and a line below it all go where they are drawn.
 * [ContentAroundFormRecipe] is the README's "content above and below the form" recipe as compiled
 * code.
 */
@Composable
fun ContentAroundFormShowcase() = FlowShowcase(
    title = "Content around the form",
    summary = "A sign-in screen with a line above the form, a workspace field the module does not " +
        "have, and a line below it.",
    module = "flow-auth",
    status = ShowcaseStatus.Beta,
    notes = {
        ChromeText(text = "The recipe", role = ChromeTextRole.Label)
        ChromeText(
            text = "ContentAroundFormRecipe in this file. The scaffold's body is a ColumnScope, so " +
                "extra content is placed where it is drawn rather than passed to a slot.",
            role = ChromeTextRole.SectionDescription,
            tone = ChromeTone.Muted,
        )
    },
) {
    preview {
        DeviceFrame(size = DeviceSize.Regular) {
            ContentAroundFormRecipe(
                branding = AuthBranding(wordmark = { AuthSamples.Wordmark() }),
                onSubmit = {},
                onSignUp = {},
                windowInsets = DeviceSize.Regular.windowInsets,
            )
        }
    }
}

/**
 * The recipe: a sign-in form with a line above it, a workspace field inside it and a line below.
 *
 * @param branding Wordmark, hero and layout.
 * @param onSubmit Called when the form is submitted.
 * @param onSignUp Called when the footer's link is pressed.
 * @param windowInsets Regions the system occupies.
 */
@Composable
private fun ContentAroundFormRecipe(
    branding: AuthBranding,
    onSubmit: () -> Unit,
    onSignUp: () -> Unit,
    windowInsets: WindowInsets,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var workspace by remember { mutableStateOf("") }

    AuthScaffold(
        branding = branding,
        windowInsets = windowInsets,
        footer = { AuthFooterPrompt(prompt = "New here?", linkText = "Create account", onLinkClick = onSignUp) },
    ) {
        AuthHeader(title = "Sign in")

        Spacer(Modifier.height(ArecaTheme.spacing.xl6))

        // Above the form.
        Text("You were invited to the Northwind workspace.", variant = TextVariant.BodySmall)
        Spacer(Modifier.height(ArecaTheme.spacing.xl))

        AuthEmailField(value = email, onValueChange = { email = it })
        Spacer(Modifier.height(ArecaTheme.spacing.xl))
        AuthPasswordField(value = password, onValueChange = { password = it })

        // A field the module does not have.
        Spacer(Modifier.height(ArecaTheme.spacing.xl))
        FieldGroup(label = "Workspace") { field ->
            TextField(value = workspace, onValueChange = { workspace = it }, field = field, singleLine = true)
        }

        Spacer(Modifier.height(ArecaTheme.spacing.xl2))
        Button(modifier = Modifier.fillMaxWidth(), onClick = onSubmit) { Text("Sign in") }

        // Below the form.
        Spacer(Modifier.height(ArecaTheme.spacing.xl))
        Text("Trouble signing in? Contact your administrator.", variant = TextVariant.BodyExtraSmall)
    }
}
