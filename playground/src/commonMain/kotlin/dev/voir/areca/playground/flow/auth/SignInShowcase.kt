package dev.voir.areca.playground.flow.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.input.ImeAction
import dev.voir.areca.flow.auth.AuthBranding
import dev.voir.areca.flow.auth.AuthProvider
import dev.voir.areca.flow.auth.part.AuthBanner
import dev.voir.areca.flow.auth.part.AuthEmailField
import dev.voir.areca.flow.auth.part.AuthFooterPrompt
import dev.voir.areca.flow.auth.part.AuthHeader
import dev.voir.areca.flow.auth.part.AuthPasswordField
import dev.voir.areca.flow.auth.part.AuthScaffold
import dev.voir.areca.flow.auth.part.AuthTextLink
import dev.voir.areca.flow.auth.part.LegalConsent
import dev.voir.areca.flow.auth.sso.SsoGroup
import dev.voir.areca.flow.auth.sso.SsoLayout
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
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
 * A sign-in screen, assembled from the module's parts.
 *
 * This page is the recipe. The module ships no `SignInScreen`, because a sign-in screen is four or
 * five product decisions in a trench coat — whether there is a form at all, what goes above it,
 * what goes below it, what the bottom edge says — and a composable that took all of those as
 * parameters would be harder to read than the twenty lines it replaced.
 *
 * What is drawn here is [SignInRecipe], immediately below. Copy it and change it.
 */
@Composable
fun SignInShowcase() = FlowShowcase(
    title = "Sign in",
    summary = "A sign-in screen assembled from AuthScaffold, the credential fields and SsoGroup.",
    module = "flow-auth",
    status = ShowcaseStatus.Beta,
    notes = {
        ChromeText(
            text = "The recipe",
            role = ChromeTextRole.Label,
        )
        ChromeText(
            text = "SignInRecipe in this file is the whole assembly — about forty lines. It is " +
                "meant to be copied into a product and edited, not called.",
            role = ChromeTextRole.SectionDescription,
            tone = ChromeTone.Muted,
        )
        ChromeText(
            text = "Turn the form off to see the provider-led screen: the same scaffold, the " +
                "same header, no fields and no rule above the buttons.",
            role = ChromeTextRole.SectionDescription,
            tone = ChromeTone.Muted,
        )
    },
) {
    val layout by enumControl(
        key = "layout",
        default = AuthLayoutOption.Plain,
        options = AuthLayoutOption.entries,
        optionLabel = { it.label },
        description = "Where the hero artwork sits.",
    )
    val form by booleanControl(
        key = "form",
        default = true,
        description = "Off leaves a provider-only screen.",
    )
    val providerSet by enumControl(
        key = "providers",
        default = AuthProviderSet.Google,
        options = AuthProviderSet.entries,
        optionLabel = { it.label },
    )
    val ssoLayout by enumControl(
        key = "ssoLayout",
        default = SsoLayout.Buttons,
        options = SsoLayout.entries,
        label = "sso layout",
        optionLabel = { it.name },
        description = "Icons costs one row whatever the provider count.",
    )
    val wordmark by booleanControl("wordmark", default = true)
    val forgotPassword by booleanControl("forgotPassword", default = true)
    val beforeForm by booleanControl(
        key = "beforeForm",
        default = false,
        label = "content above form",
        description = "Anything a product puts between the header and the first field.",
    )
    val afterForm by booleanControl(
        key = "afterForm",
        default = false,
        label = "content below form",
    )
    val footer by enumControl(
        key = "footer",
        default = FooterOption.Prompt,
        options = FooterOption.entries,
        optionLabel = { it.label },
    )
    val submitting by booleanControl("submitting", default = false)

    preview {
        var state by remember { mutableStateOf(SignInScreenState()) }

        DeviceFrame(size = DeviceSize.Regular) {
            SignInRecipe(
                state = state.copy(submitting = submitting),
                onStateChange = { state = it },
                providers = providerSet.providers,
                ssoLayout = ssoLayout,
                branding = AuthBranding(
                    wordmark = if (wordmark) ({ AuthSamples.Wordmark() }) else null,
                    hero = heroFor(layout),
                    layout = layout.toLayout(),
                ),
                showForm = form,
                showForgotPassword = forgotPassword,
                aboveForm = if (beforeForm) ({ InviteNotice() }) else null,
                belowForm = if (afterForm) ({ SupportNotice() }) else null,
                footer = footer,
                windowInsets = DeviceSize.Regular.windowInsets,
            )
        }
    }
}

/**
 * The recipe: a sign-in screen built from `flow-auth` parts and nothing else.
 *
 * Every parameter here is a decision this showcase wants to be able to change. A real product
 * makes most of them once, in the source, and deletes the rest — which is the point of the module
 * shipping parts rather than a screen.
 *
 * @param state Everything to draw.
 * @param onStateChange Called with the next state.
 * @param providers Providers to offer. Empty draws no provider block.
 * @param ssoLayout How the provider buttons are arranged.
 * @param branding Wordmark, hero and layout.
 * @param showForm Whether to draw the email and password fields at all.
 * @param showForgotPassword Whether to offer password recovery.
 * @param aboveForm Extra content between the header and the first field.
 * @param belowForm Extra content between the submit button and the provider block.
 * @param footer What the bottom edge says.
 * @param windowInsets Regions the system occupies.
 */
@Composable
private fun SignInRecipe(
    state: SignInScreenState,
    onStateChange: (SignInScreenState) -> Unit,
    providers: List<AuthProvider>,
    ssoLayout: SsoLayout,
    branding: AuthBranding,
    showForm: Boolean,
    showForgotPassword: Boolean,
    aboveForm: (@Composable ColumnScope.() -> Unit)?,
    belowForm: (@Composable ColumnScope.() -> Unit)?,
    footer: FooterOption,
    windowInsets: WindowInsets,
) {
    val passwordFocus = remember { FocusRequester() }
    val submit = { onStateChange(state.checked()) }

    AuthScaffold(
        branding = branding,
        windowInsets = windowInsets,
        footer = when (footer) {
            FooterOption.None -> null
            FooterOption.Prompt -> ({
                AuthFooterPrompt(
                    prompt = "New here?",
                    linkText = "Create account",
                    onLinkClick = {},
                )
            })

            FooterOption.Legal -> ({ LegalConsent(links = AuthSamples.Legal, onLinkClick = {}) })
        },
    ) {
        AuthHeader(title = "Welcome back", subtitle = "Sign in to pick up where you left off.")

        Spacer(Modifier.height(ArecaTheme.spacing.xl6))

        aboveForm?.invoke(this)

        if (showForm) {
            AuthEmailField(
                placeholder = "name@example.com",
                value = state.email,
                onValueChange = { onStateChange(state.copy(email = it, emailError = null)) },
                error = state.emailError,
                enabled = !state.busy,
                imeAction = ImeAction.Next,
                onImeAction = { passwordFocus.requestFocus() },
            )

            Spacer(Modifier.height(ArecaTheme.spacing.xl))

            AuthPasswordField(
                placeholder = "Enter your password",
                value = state.password,
                onValueChange = { onStateChange(state.copy(password = it, passwordError = null)) },
                error = state.passwordError,
                enabled = !state.busy,
                focusRequester = passwordFocus,
                imeAction = ImeAction.Done,
                onImeAction = submit,
            )

            if (showForgotPassword) {
                Spacer(Modifier.height(ArecaTheme.spacing.lg))

                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                    AuthTextLink(
                        text = "Forgot password?",
                        onClick = {},
                        enabled = !state.busy,
                    )
                }
            }

            Spacer(Modifier.height(ArecaTheme.spacing.xl2))

            AuthBanner(message = state.formError, contentDescription = AuthSamples::errorDescription)

            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.busy,
                loading = state.submitting,
                onClick = submit,
            ) {
                Text("Sign in")
            }
        }

        belowForm?.invoke(this)

        if (providers.isNotEmpty()) {
            Spacer(Modifier.height(ArecaTheme.spacing.xl2))

            SsoGroup(
                providers = providers,
                onProviderClick = {},
                layout = ssoLayout,
                enabled = !state.busy,
                busyProvider = state.busyProvider,
                // With no form above it there is nothing for a rule to separate the block from.
                dividerText = if (showForm) "or continue with" else null,
            )
        }
    }
}

/** Stand-in for the line a product might put above its form. */
@Composable
private fun InviteNotice() {
    Text(
        text = "You were invited to the Northwind workspace.",
        variant = TextVariant.BodySmall,
        color = TextColor.Subtle,
        modifier = Modifier.fillMaxWidth(),
    )
    Spacer(Modifier.height(ArecaTheme.spacing.xl))
}

/** Stand-in for the line a product might put below its form. */
@Composable
private fun SupportNotice() {
    Spacer(Modifier.height(ArecaTheme.spacing.xl))
    Text(
        text = "Trouble signing in? Contact your administrator.",
        variant = TextVariant.BodyExtraSmall,
        color = TextColor.Muted,
        modifier = Modifier.fillMaxWidth(),
    )
}

/**
 * What the bottom edge of the screen says.
 *
 * @property label Name shown in the control.
 */
internal enum class FooterOption(val label: String) {
    /** The link to the other screen. */
    Prompt("Prompt"),

    /** The terms and privacy line, for a product whose sign-in doubles as a sign-up. */
    Legal("Legal"),

    /** Nothing, leaving the bottom to the content. */
    None("None"),
}
