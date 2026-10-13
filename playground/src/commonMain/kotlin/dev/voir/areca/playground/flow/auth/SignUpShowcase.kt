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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.input.ImeAction
import dev.voir.areca.flow.auth.AuthBranding
import dev.voir.areca.flow.auth.AuthProvider
import dev.voir.areca.flow.auth.part.AuthBanner
import dev.voir.areca.flow.auth.part.AuthEmailField
import dev.voir.areca.flow.auth.part.AuthFooterPrompt
import dev.voir.areca.flow.auth.part.AuthHeader
import dev.voir.areca.flow.auth.part.AuthNameField
import dev.voir.areca.flow.auth.part.AuthPasswordField
import dev.voir.areca.flow.auth.part.AuthScaffold
import dev.voir.areca.flow.auth.part.LegalConsent
import dev.voir.areca.flow.auth.sso.SsoGroup
import dev.voir.areca.flow.auth.sso.SsoLayout
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.text.Text
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
 * A registration screen, assembled from the module's parts.
 *
 * The same shape as the sign-in page, and deliberately: the two screens share a scaffold, a header
 * and a provider block, and differ only in which boxes they collect and what sits under the button.
 * Seeing that in two files that look alike is the argument for shipping parts.
 */
@Composable
fun SignUpShowcase() = FlowShowcase(
    title = "Sign up",
    summary = "A registration screen, with the boxes it collects chosen by the product.",
    module = "flow-auth",
    status = ShowcaseStatus.Beta,
    notes = {
        ChromeText(text = "The recipe", role = ChromeTextRole.Label)
        ChromeText(
            text = "SignUpRecipe in this file is the whole assembly. Turning a box off removes " +
                "it and re-routes the keyboard's Next action to whatever really is next.",
            role = ChromeTextRole.SectionDescription,
            tone = ChromeTone.Muted,
        )
        ChromeText(
            text = "The minimum password length is the product's, not the module's — it has to " +
                "match whatever the service enforces or the form contradicts it.",
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
    )
    val name by booleanControl("name field", default = true)
    val confirmation by booleanControl("confirm password", default = true)
    val legal by booleanControl("legal line", default = true)
    val providerSet by enumControl(
        key = "providers",
        default = AuthProviderSet.None,
        options = AuthProviderSet.entries,
        optionLabel = { it.label },
    )
    val ssoLayout by enumControl(
        key = "ssoLayout",
        default = SsoLayout.Icons,
        options = SsoLayout.entries,
        label = "sso layout",
        optionLabel = { it.name },
    )
    val wordmark by booleanControl("wordmark", default = false)
    val minimumLength by numberControl(
        key = "minimumPasswordLength",
        default = 8f,
        range = 4f..16f,
        step = 1f,
        label = "min password length",
        format = { "${it.toInt()} characters" },
    )
    val submitting by booleanControl("submitting", default = false)

    preview {
        var state by remember { mutableStateOf(SignUpScreenState()) }

        DeviceFrame(size = DeviceSize.Regular) {
            SignUpRecipe(
                state = state.copy(submitting = submitting),
                onStateChange = { state = it },
                collectName = name,
                confirmPassword = confirmation,
                showLegal = legal,
                minimumPasswordLength = minimumLength.toInt(),
                providers = providerSet.providers,
                ssoLayout = ssoLayout,
                branding = AuthBranding(
                    wordmark = if (wordmark) ({ AuthSamples.Wordmark() }) else null,
                    hero = heroFor(layout),
                    layout = layout.toLayout(),
                ),
                windowInsets = DeviceSize.Regular.windowInsets,
            )
        }
    }
}

/**
 * The recipe: a registration screen built from `flow-auth` parts and nothing else.
 *
 * @param state Everything to draw.
 * @param onStateChange Called with the next state.
 * @param collectName Whether to draw the name box.
 * @param confirmPassword Whether to draw the confirmation box.
 * @param showLegal Whether to draw the terms and privacy line under the button.
 * @param minimumPasswordLength Shortest password the sample application's own check accepts.
 * @param providers Providers to offer. Empty draws no provider block.
 * @param ssoLayout How the provider buttons are arranged.
 * @param branding Wordmark, hero and layout.
 * @param windowInsets Regions the system occupies.
 */
@Composable
private fun SignUpRecipe(
    state: SignUpScreenState,
    onStateChange: (SignUpScreenState) -> Unit,
    collectName: Boolean,
    confirmPassword: Boolean,
    showLegal: Boolean,
    minimumPasswordLength: Int,
    providers: List<AuthProvider>,
    ssoLayout: SsoLayout,
    branding: AuthBranding,
    windowInsets: WindowInsets,
) {

    val emailFocus = remember { FocusRequester() }
    val passwordFocus = remember { FocusRequester() }
    val confirmationFocus = remember { FocusRequester() }

    val submit = {
        onStateChange(
            state.checked(
                minimumPasswordLength = minimumPasswordLength,
                collectName = collectName,
                confirmPassword = confirmPassword,
            ),
        )
    }

    AuthScaffold(
        branding = branding,
        windowInsets = windowInsets,
        footer = {
            AuthFooterPrompt(
                prompt = "Already have an account?",
                linkText = "Sign in",
                onLinkClick = {},
            )
        },
    ) {
        AuthHeader(title = "Create your account", subtitle = "It takes less than a minute.")

        Spacer(Modifier.height(ArecaTheme.spacing.xl6))

        if (collectName) {
            AuthNameField(
                placeholder = "Enter your full name",
                value = state.name,
                onValueChange = { onStateChange(state.copy(name = it, nameError = null)) },
                error = state.nameError,
                enabled = !state.busy,
                onImeAction = { emailFocus.requestFocus() },
            )

            Spacer(Modifier.height(ArecaTheme.spacing.xl))
        }

        AuthEmailField(
            placeholder = "name@example.com",
            value = state.email,
            onValueChange = { onStateChange(state.copy(email = it, emailError = null)) },
            error = state.emailError,
            enabled = !state.busy,
            newAccount = true,
            focusRequester = emailFocus,
            onImeAction = { passwordFocus.requestFocus() },
        )

        Spacer(Modifier.height(ArecaTheme.spacing.xl))

        AuthPasswordField(
            placeholder = "Create a password",
            value = state.password,
            onValueChange = { onStateChange(state.copy(password = it, passwordError = null)) },
            error = state.passwordError,
            enabled = !state.busy,
            newAccount = true,
            focusRequester = passwordFocus,
            imeAction = if (confirmPassword) ImeAction.Next else ImeAction.Done,
            onImeAction = { if (confirmPassword) confirmationFocus.requestFocus() else submit() },
        )

        if (confirmPassword) {
            Spacer(Modifier.height(ArecaTheme.spacing.xl))

            AuthPasswordField(
                placeholder = "Enter password again",
                value = state.passwordConfirmation,
                onValueChange = {
                    onStateChange(
                        state.copy(passwordConfirmation = it, passwordConfirmationError = null),
                    )
                },
                error = state.passwordConfirmationError,
                enabled = !state.busy,
                newAccount = true,
                label = "Confirm password",
                focusRequester = confirmationFocus,
                imeAction = ImeAction.Done,
                onImeAction = submit,
            )
        }

        Spacer(Modifier.height(ArecaTheme.spacing.xl2))

        AuthBanner(message = state.formError, contentDescription = AuthSamples::errorDescription)

        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.busy,
            loading = state.submitting,
            onClick = submit,
        ) {
            Text("Create account")
        }

        if (providers.isNotEmpty()) {
            Spacer(Modifier.height(ArecaTheme.spacing.xl2))

            SsoGroup(
                dividerText = "or continue with",
                providers = providers,
                onProviderClick = {},
                layout = ssoLayout,
                enabled = !state.busy,
                busyProvider = state.busyProvider,
            )
        }

        if (showLegal) {
            Spacer(Modifier.height(ArecaTheme.spacing.xl))

            LegalConsent(links = AuthSamples.Legal, onLinkClick = {})
        }
    }
}
