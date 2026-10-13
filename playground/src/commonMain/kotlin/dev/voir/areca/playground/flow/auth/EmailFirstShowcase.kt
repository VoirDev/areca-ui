package dev.voir.areca.playground.flow.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import dev.voir.areca.flow.auth.AuthBranding
import dev.voir.areca.flow.auth.AuthProvider
import dev.voir.areca.flow.auth.part.AuthBanner
import dev.voir.areca.flow.auth.part.AuthCodeField
import dev.voir.areca.flow.auth.part.AuthEmailField
import dev.voir.areca.flow.auth.part.AuthHeader
import dev.voir.areca.flow.auth.part.AuthIdentityChip
import dev.voir.areca.flow.auth.part.AuthPasswordField
import dev.voir.areca.flow.auth.part.AuthPasswordRequirements
import dev.voir.areca.flow.auth.part.AuthProgress
import dev.voir.areca.flow.auth.part.AuthResendCode
import dev.voir.areca.flow.auth.part.AuthScaffold
import dev.voir.areca.flow.auth.part.AuthTextLink
import dev.voir.areca.flow.auth.part.LegalConsent
import dev.voir.areca.flow.auth.sso.SsoGroup
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.button.ButtonStyle
import dev.voir.areca.foundation.feedback.RequirementState
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.foundation.notice.NoticeTone
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.foundation.topbar.BackAction
import dev.voir.areca.foundation.topbar.TopBar
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.Check
import dev.voir.areca.icons.Clock
import dev.voir.areca.icons.Key
import dev.voir.areca.icons.LinkExternal
import dev.voir.areca.icons.Lock
import dev.voir.areca.icons.LogIn
import dev.voir.areca.icons.Mail
import dev.voir.areca.icons.UserX
import dev.voir.areca.playground.showcase.FlowShowcase
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.canvas.DeviceFrame
import dev.voir.areca.playground.showcase.canvas.DeviceSize
import dev.voir.areca.playground.showcase.canvas.windowInsets
import dev.voir.areca.playground.showcase.chrome.ChromeText
import dev.voir.areca.playground.showcase.chrome.ChromeTextRole
import dev.voir.areca.playground.showcase.chrome.ChromeTone
import dev.voir.areca.theme.ArecaTheme
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

/**
 * An email-first sign-in: one address box, then the password for an account that exists or a code
 * for one that does not, then a password for the new account.
 *
 * Each step is its own recipe below — [EmailStepRecipe], [PasswordStepRecipe], [CodeStepRecipe],
 * [CreatePasswordStepRecipe], [ProviderEmailRecipe] and [OutcomeRecipe] — and this page sequences
 * them with plain state, the way FLOWS.md asks: no navigation library, no view model. The sample
 * service that answers them is the page's, not the module's.
 */
@Composable
fun EmailFirstShowcase() = FlowShowcase(
    title = "Email first",
    summary = "One address box that leads to a password for a known account or a code for a new " +
        "one, with single sign-on beside it.",
    module = "flow-auth",
    status = ShowcaseStatus.Beta,
    notes = {
        ChromeText(text = "The recipes", role = ChromeTextRole.Label)
        ChromeText(
            text = "One recipe per step in this file: EmailStepRecipe, PasswordStepRecipe, " +
                "CodeStepRecipe, CreatePasswordStepRecipe, ProviderEmailRecipe and OutcomeRecipe. " +
                "The page sequences them with plain state.",
            role = ChromeTextRole.SectionDescription,
            tone = ChromeTone.Muted,
        )
        ChromeText(text = "Demo data", role = ChromeTextRole.Label)
        ChromeText(
            text = "${AuthSamples.Account} has an account; ${AuthSamples.Password} signs in, and the " +
                "third wrong password hits the rate limit. Any other address is new: code " +
                "${AuthSamples.GoodCode} works, ${AuthSamples.ExpiredCode} has expired. Google signs in " +
                "at once; Facebook shares no email, so it asks for one.",
            role = ChromeTextRole.SectionDescription,
            tone = ChromeTone.Muted,
        )
    },
) {
    val start by enumControl(
        key = "step",
        default = EmailFirstStart.Email,
        options = EmailFirstStart.entries,
        optionLabel = { it.label },
        description = "Where the flow starts. From there it is live.",
    )
    val sso by booleanControl("sso", default = true, description = "Offer single sign-on beside the form.")
    val inviteOnly by booleanControl(
        key = "inviteOnly",
        default = false,
        label = "invite only",
        description = "A new address is turned away instead of sent a code.",
    )
    val wordmark by booleanControl("wordmark", default = true)

    preview {
        EmailFirstPreview(start = start, sso = sso, inviteOnly = inviteOnly, wordmark = wordmark)
    }
}

/**
 * The page's live preview: the flow, started at [start], answered by the sample service.
 *
 * Internal rather than private so the render test can draw every starting step.
 *
 * @param start Where the flow starts.
 * @param sso Whether single sign-on is offered beside the form.
 * @param inviteOnly Whether a new address is turned away.
 * @param wordmark Whether the wordmark is drawn.
 */
@Composable
internal fun EmailFirstPreview(start: EmailFirstStart, sso: Boolean, inviteOnly: Boolean, wordmark: Boolean) {
    var state by remember(start) { mutableStateOf(start.state) }

    // The sample service: answers whatever the screen asked for after a short wait.
    LaunchedEffect(state.request) {
        if (state.request == 0 || state.work == null) return@LaunchedEffect
        delay(ServiceDelay)
        state = state.answered(inviteOnly = inviteOnly)
    }

    // The resend cooldown, counted down a second at a time.
    LaunchedEffect(state.cooldown) {
        if (state.cooldown <= 0) return@LaunchedEffect
        delay(1.seconds)
        state = state.copy(cooldown = state.cooldown - 1)
    }

    DeviceFrame(size = DeviceSize.Regular) {
        EmailFirstFlow(
            state = state,
            onStateChange = { state = it },
            providers = if (sso) SsoProviders else emptyList(),
            branding = AuthBranding(wordmark = if (wordmark) ({ AuthSamples.Wordmark() }) else null),
            windowInsets = DeviceSize.Regular.windowInsets,
        )
    }
}

/**
 * Draws whichever step the flow is on, and turns each step's intents into the next state.
 *
 * The sequencing a product does with its own navigation. Kept apart from the recipes so each recipe
 * reads as one screen.
 *
 * @param state Where the flow is and what it holds.
 * @param onStateChange Called with the next state.
 * @param providers Single sign-on providers to offer on the first step.
 * @param branding Wordmark, hero and layout.
 * @param windowInsets Regions the system occupies.
 */
@Composable
private fun EmailFirstFlow(
    state: EmailFirstState,
    onStateChange: (EmailFirstState) -> Unit,
    providers: List<AuthProvider>,
    branding: AuthBranding,
    windowInsets: WindowInsets,
) {
    val backToEmail = { onStateChange(EmailFirstState(email = state.email)) }
    val submit = { work: EmailFirstWork, next: EmailFirstState -> onStateChange(next.started(work)) }

    when (state.screen) {
        EmailFirstScreen.Email -> EmailStepRecipe(
            email = state.email,
            emailError = state.emailError,
            bannerTitle = state.bannerTitle,
            banner = state.banner,
            busy = state.busy,
            providers = providers,
            busyProvider = state.provider.takeIf { state.busy },
            onEmailChange = { onStateChange(state.copy(email = it, emailError = null)) },
            onContinue = {
                val complaint = emailComplaint(state.email)
                if (complaint != null) {
                    onStateChange(state.copy(emailError = complaint, banner = null, bannerTitle = null))
                } else {
                    submit(EmailFirstWork.LookUp, state.copy(email = state.email.trim(), banner = null, bannerTitle = null))
                }
            },
            onProviderClick = { provider ->
                submit(
                    EmailFirstWork.ProviderSignIn,
                    EmailFirstState(screen = EmailFirstScreen.ProviderWaiting, provider = provider),
                )
            },
            branding = branding,
            windowInsets = windowInsets,
        )

        EmailFirstScreen.ProviderWaiting -> OutcomeRecipe(
            mark = ArecaIcons.LinkExternal,
            markColor = ActionColor.Neutral,
            title = "Continue with ${providerName(state.provider)}",
            subtitle = "Finish signing in in the ${providerName(state.provider)} window. This page " +
                "updates when you're done.",
            progress = "Waiting for ${providerName(state.provider)}…",
            button = "Cancel",
            onButton = { onStateChange(EmailFirstState()) },
            branding = branding,
            windowInsets = windowInsets,
        )

        EmailFirstScreen.ProviderEmail -> ProviderEmailRecipe(
            providerName = providerName(state.provider),
            email = state.email,
            emailError = state.emailError,
            busy = state.busy,
            onEmailChange = { onStateChange(state.copy(email = it, emailError = null)) },
            onSendCode = {
                val complaint = emailComplaint(state.email)
                if (complaint != null) {
                    onStateChange(state.copy(emailError = complaint))
                } else {
                    submit(EmailFirstWork.SendCode, state.copy(email = state.email.trim()))
                }
            },
            onCancel = { onStateChange(EmailFirstState()) },
            branding = branding,
            windowInsets = windowInsets,
        )

        EmailFirstScreen.Password -> PasswordStepRecipe(
            email = state.email,
            password = state.password,
            passwordError = state.passwordError,
            limited = state.limited,
            busy = state.busy,
            onPasswordChange = { onStateChange(state.copy(password = it, passwordError = null)) },
            onSignIn = {
                if (state.password.isEmpty()) {
                    onStateChange(state.copy(passwordError = "Enter your password."))
                } else {
                    submit(EmailFirstWork.SignIn, state)
                }
            },
            onForgotPassword = { onStateChange(state.copy(screen = EmailFirstScreen.ResetSent)) },
            onChangeEmail = backToEmail,
            branding = branding,
            windowInsets = windowInsets,
        )

        EmailFirstScreen.ResetSent -> OutcomeRecipe(
            mark = ArecaIcons.Mail,
            markColor = ActionColor.Brand,
            title = "Check your email",
            subtitle = "We sent a link to reset your password to ${state.email}. It works once.",
            button = "Back to sign in",
            onButton = backToEmail,
            note = "Didn't get it? Check your spam folder.",
            branding = branding,
            windowInsets = windowInsets,
        )

        EmailFirstScreen.Code -> CodeStepRecipe(
            email = state.email,
            providerName = state.provider?.let(::providerName),
            code = state.code,
            codeError = state.codeError,
            sent = state.resent,
            cooldown = state.cooldown.takeIf { it > 0 }?.let(::cooldownText),
            busy = state.busy,
            onCodeChange = { onStateChange(state.copy(code = it, codeError = null)) },
            onVerify = { code ->
                if (code.length < CodeLength) {
                    onStateChange(state.copy(codeError = "Enter the 6-digit code from your email."))
                } else {
                    submit(EmailFirstWork.Verify, state.copy(code = code))
                }
            },
            onResend = {
                onStateChange(state.copy(code = "", codeError = null, resent = true, cooldown = ResendCooldownSeconds))
            },
            onChangeEmail = backToEmail,
            branding = branding,
            windowInsets = windowInsets,
        )

        EmailFirstScreen.CreatePassword -> CreatePasswordStepRecipe(
            email = state.email,
            password = state.password,
            passwordError = state.passwordError,
            requirementsChecked = state.passwordChecked,
            busy = state.busy,
            onPasswordChange = { onStateChange(state.copy(password = it, passwordError = null)) },
            onCreate = {
                val unmet = AuthSamples.passwordRequirements(state.password, checked = true)
                    .any { it.state != RequirementState.Met }
                if (unmet) {
                    onStateChange(
                        state.copy(
                            passwordChecked = true,
                            passwordError = "Choose a password that meets every rule below.",
                        ),
                    )
                } else {
                    submit(EmailFirstWork.Create, state)
                }
            },
            onBack = backToEmail,
            branding = branding,
            windowInsets = windowInsets,
        )

        EmailFirstScreen.SignedIn -> OutcomeRecipe(
            mark = ArecaIcons.Check,
            markColor = ActionColor.Success,
            title = "You're signed in",
            subtitle = state.provider?.let { "Signed in with ${providerName(it)}." } ?: "Welcome back to Areca.",
            progress = "Returning to Areca…",
            link = "Not redirected? Continue to Areca",
            onLink = {},
            branding = branding,
            windowInsets = windowInsets,
        )

        EmailFirstScreen.Welcome -> OutcomeRecipe(
            mark = ArecaIcons.Check,
            markColor = ActionColor.Success,
            title = "Welcome to Areca",
            subtitle = state.provider?.let { "Your account is ready and linked to ${providerName(it)}." }
                ?: "Your account is ready.",
            progress = "Returning to Areca…",
            link = "Not redirected? Continue to Areca",
            onLink = {},
            branding = branding,
            windowInsets = windowInsets,
        )

        EmailFirstScreen.InviteOnly -> OutcomeRecipe(
            mark = ArecaIcons.UserX,
            markColor = ActionColor.Neutral,
            title = "You need an invitation",
            subtitle = "Areca is invite-only, and ${state.email} doesn't have an account yet. Ask " +
                "whoever manages Areca to invite you.",
            button = "Use a different email",
            onButton = backToEmail,
            branding = branding,
            windowInsets = windowInsets,
        )
    }
}

/**
 * The recipe for the first step: the address, a failure banner, and single sign-on beside it.
 *
 * @param email Address as typed.
 * @param emailError The application's complaint about the address, or `null`.
 * @param bannerTitle Headline of the failure banner, or `null`.
 * @param banner Why the last attempt failed, or `null`.
 * @param busy Whether an attempt is in flight.
 * @param providers Providers to offer. Empty draws no provider block.
 * @param busyProvider Provider whose exchange is in flight, or `null`.
 * @param onEmailChange Called on every edit of the address.
 * @param onContinue Called to look the address up.
 * @param onProviderClick Called with the provider pressed.
 * @param branding Wordmark, hero and layout.
 * @param windowInsets Regions the system occupies.
 */
@Composable
private fun EmailStepRecipe(
    email: String,
    emailError: String?,
    bannerTitle: String?,
    banner: String?,
    busy: Boolean,
    providers: List<AuthProvider>,
    busyProvider: AuthProvider?,
    onEmailChange: (String) -> Unit,
    onContinue: () -> Unit,
    onProviderClick: (AuthProvider) -> Unit,
    branding: AuthBranding,
    windowInsets: WindowInsets,
) {
    AuthScaffold(
        branding = branding,
        windowInsets = windowInsets,
        footer = { LegalConsent(links = AuthSamples.Legal, onLinkClick = {}) },
    ) {
        AuthHeader(
            title = "Continue to Areca",
            subtitle = "Sign in or create an account with your email.",
            icon = { AuthSamples.Mark(ArecaIcons.LogIn) },
        )

        Spacer(Modifier.height(ArecaTheme.spacing.xl6))

        AuthBanner(message = banner, title = bannerTitle, contentDescription = AuthSamples::errorDescription)

        AuthEmailField(
            value = email,
            onValueChange = onEmailChange,
            error = emailError,
            enabled = !busy,
            placeholder = "name@example.com",
            imeAction = ImeAction.Done,
            onImeAction = onContinue,
        )

        Spacer(Modifier.height(ArecaTheme.spacing.xl2))

        Button(
            text = "Continue",
            onClick = onContinue,
            modifier = Modifier.fillMaxWidth(),
            enabled = !busy,
            loading = busy && busyProvider == null,
        )

        if (providers.isNotEmpty()) {
            Spacer(Modifier.height(ArecaTheme.spacing.xl2))

            SsoGroup(
                providers = providers,
                onProviderClick = onProviderClick,
                enabled = !busy,
                busyProvider = busyProvider,
                dividerText = "or",
            )
        }
    }
}

/**
 * The recipe for a known account: which account, its password, the way to recover it, and the
 * rate limit.
 *
 * @param email The account being signed in to.
 * @param password Password as typed.
 * @param passwordError The service's complaint about the password, or `null`.
 * @param limited Whether the service is refusing attempts for a while.
 * @param busy Whether the attempt is in flight.
 * @param onPasswordChange Called on every edit.
 * @param onSignIn Called to sign in.
 * @param onForgotPassword Called to send a reset link.
 * @param onChangeEmail Called to go back and use another address.
 * @param branding Wordmark, hero and layout.
 * @param windowInsets Regions the system occupies.
 */
@Composable
private fun PasswordStepRecipe(
    email: String,
    password: String,
    passwordError: String?,
    limited: Boolean,
    busy: Boolean,
    onPasswordChange: (String) -> Unit,
    onSignIn: () -> Unit,
    onForgotPassword: () -> Unit,
    onChangeEmail: () -> Unit,
    branding: AuthBranding,
    windowInsets: WindowInsets,
) {
    AuthScaffold(
        branding = branding,
        windowInsets = windowInsets,
        topBar = { TopBar(modifier = Modifier.fillMaxWidth(), leading = { BackAction(onClick = onChangeEmail) }) {} },
    ) {
        AuthHeader(
            title = "Enter your password",
            subtitle = "Welcome back. Sign in to continue to Areca.",
            icon = { AuthSamples.Mark(ArecaIcons.Lock) },
        )

        Spacer(Modifier.height(ArecaTheme.spacing.xl6))

        AuthIdentityChip(
            identifier = email,
            onChange = onChangeEmail,
            onChangeLabel = "Use a different email",
            enabled = !busy,
        )

        Spacer(Modifier.height(ArecaTheme.spacing.xl))

        AuthBanner(
            message = if (limited) "For your security, wait 2 minutes or reset your password." else null,
            title = "Too many attempts",
            tone = NoticeTone.Warning,
            icon = ArecaIcons.Clock,
        )

        AuthPasswordField(
            value = password,
            onValueChange = onPasswordChange,
            error = passwordError,
            enabled = !busy && !limited,
            imeAction = ImeAction.Done,
            onImeAction = onSignIn,
        )

        Spacer(Modifier.height(ArecaTheme.spacing.lg))

        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
            AuthTextLink(text = "Forgot password?", onClick = onForgotPassword, enabled = !busy)
        }

        Spacer(Modifier.height(ArecaTheme.spacing.xl2))

        if (limited) {
            // The wait, said on the button that cannot be pressed until it is over.
            Button(
                text = "Try again in 1:58",
                onClick = null,
                modifier = Modifier.fillMaxWidth(),
                enabled = false,
                leading = { Icon(imageVector = ArecaIcons.Clock, contentDescription = null) },
            )
        } else {
            Button(
                text = "Sign in",
                onClick = onSignIn,
                modifier = Modifier.fillMaxWidth(),
                enabled = !busy,
                loading = busy,
            )
        }
    }
}

/**
 * The recipe for a code sent to confirm an address: which address, the code, and a way to ask for
 * another.
 *
 * @param email The address the code went to.
 * @param providerName Provider being finished with this code, or `null` when the code confirms a
 *   new account.
 * @param code Code as typed.
 * @param codeError The service's complaint about the code, or `null`.
 * @param sent Whether a new code was just sent.
 * @param cooldown How long until another can be asked for, worded, or `null`.
 * @param busy Whether the code is being verified.
 * @param onCodeChange Called on every edit.
 * @param onVerify Called with the code to verify — by the last digit, or by the button.
 * @param onResend Called to send a new code.
 * @param onChangeEmail Called to go back and use another address.
 * @param branding Wordmark, hero and layout.
 * @param windowInsets Regions the system occupies.
 */
@Composable
private fun CodeStepRecipe(
    email: String,
    providerName: String?,
    code: String,
    codeError: String?,
    sent: Boolean,
    cooldown: String?,
    busy: Boolean,
    onCodeChange: (String) -> Unit,
    onVerify: (String) -> Unit,
    onResend: () -> Unit,
    onChangeEmail: () -> Unit,
    branding: AuthBranding,
    windowInsets: WindowInsets,
) {
    val codeFocus = remember { FocusRequester() }
    LaunchedEffect(Unit) { codeFocus.requestFocus() }

    AuthScaffold(
        branding = branding,
        windowInsets = windowInsets,
        topBar = { TopBar(modifier = Modifier.fillMaxWidth(), leading = { BackAction(onClick = onChangeEmail) }) {} },
    ) {
        AuthHeader(
            title = "Check your email",
            subtitle = if (providerName != null) {
                "Enter the 6-digit code we sent to finish signing in with $providerName. It expires in 10 minutes."
            } else {
                "Enter the 6-digit code we sent to confirm your email. It expires in 10 minutes."
            },
            icon = { AuthSamples.Mark(ArecaIcons.Mail) },
        )

        Spacer(Modifier.height(ArecaTheme.spacing.xl6))

        AuthIdentityChip(
            identifier = email,
            onChange = onChangeEmail,
            onChangeLabel = "Use a different email",
            enabled = !busy,
        )

        Spacer(Modifier.height(ArecaTheme.spacing.xl))

        AuthCodeField(
            value = code,
            onValueChange = onCodeChange,
            onComplete = onVerify,
            error = codeError,
            enabled = !busy,
            focusRequester = codeFocus,
        )

        Spacer(Modifier.height(ArecaTheme.spacing.xl))

        AuthResendCode(
            onResend = onResend,
            prompt = "Didn't get it?",
            sent = if (sent) "New code sent." else null,
            cooldown = cooldown,
            enabled = !busy,
        )

        Spacer(Modifier.height(ArecaTheme.spacing.xl2))

        Button(
            text = "Verify",
            onClick = { onVerify(code) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !busy,
            loading = busy,
        )
    }
}

/**
 * The recipe for a new account's password: the confirmed address, the password and its rules.
 *
 * @param email The confirmed address.
 * @param password Password as typed.
 * @param passwordError The application's complaint, or `null`.
 * @param requirementsChecked Whether a submit was refused, which turns unmet rules red.
 * @param busy Whether the account is being created.
 * @param onPasswordChange Called on every edit.
 * @param onCreate Called to create the account.
 * @param onBack Called to leave the flow.
 * @param branding Wordmark, hero and layout.
 * @param windowInsets Regions the system occupies.
 */
@Composable
private fun CreatePasswordStepRecipe(
    email: String,
    password: String,
    passwordError: String?,
    requirementsChecked: Boolean,
    busy: Boolean,
    onPasswordChange: (String) -> Unit,
    onCreate: () -> Unit,
    onBack: () -> Unit,
    branding: AuthBranding,
    windowInsets: WindowInsets,
) {
    AuthScaffold(
        branding = branding,
        windowInsets = windowInsets,
        topBar = { TopBar(modifier = Modifier.fillMaxWidth(), leading = { BackAction(onClick = onBack) }) {} },
    ) {
        AuthHeader(
            title = "Create your password",
            subtitle = "Your email is confirmed. Choose a password to finish creating your account.",
            icon = { AuthSamples.Mark(ArecaIcons.Key) },
        )

        Spacer(Modifier.height(ArecaTheme.spacing.xl6))

        AuthIdentityChip(identifier = email, verified = true)

        Spacer(Modifier.height(ArecaTheme.spacing.xl))

        AuthPasswordField(
            value = password,
            onValueChange = onPasswordChange,
            error = passwordError,
            enabled = !busy,
            newAccount = true,
            imeAction = ImeAction.Done,
            onImeAction = onCreate,
        )

        Spacer(Modifier.height(ArecaTheme.spacing.md))

        AuthPasswordRequirements(
            requirements = AuthSamples.passwordRequirements(password, checked = requirementsChecked),
            contentDescription = "Password requirements",
        )

        Spacer(Modifier.height(ArecaTheme.spacing.xl2))

        Button(
            text = "Create account",
            onClick = onCreate,
            modifier = Modifier.fillMaxWidth(),
            enabled = !busy,
            loading = busy,
        )
    }
}

/**
 * The recipe for a provider that signed somebody in without a verified address: ask for one, and
 * send it a code.
 *
 * @param providerName The provider, as the reader knows it.
 * @param email Address as typed.
 * @param emailError The application's complaint about the address, or `null`.
 * @param busy Whether the code is being sent.
 * @param onEmailChange Called on every edit.
 * @param onSendCode Called to send the code.
 * @param onCancel Called to abandon the provider and start again.
 * @param branding Wordmark, hero and layout.
 * @param windowInsets Regions the system occupies.
 */
@Composable
private fun ProviderEmailRecipe(
    providerName: String,
    email: String,
    emailError: String?,
    busy: Boolean,
    onEmailChange: (String) -> Unit,
    onSendCode: () -> Unit,
    onCancel: () -> Unit,
    branding: AuthBranding,
    windowInsets: WindowInsets,
) {
    AuthScaffold(branding = branding, windowInsets = windowInsets) {
        AuthHeader(
            title = "Confirm your email",
            subtitle = "$providerName didn't share a verified email. Enter the email you want to use " +
                "with Areca and we'll send you a code.",
            icon = { AuthSamples.Mark(ArecaIcons.Mail) },
        )

        Spacer(Modifier.height(ArecaTheme.spacing.xl6))

        AuthEmailField(
            value = email,
            onValueChange = onEmailChange,
            error = emailError,
            enabled = !busy,
            newAccount = true,
            placeholder = "name@example.com",
            imeAction = ImeAction.Done,
            onImeAction = onSendCode,
        )

        Spacer(Modifier.height(ArecaTheme.spacing.xl2))

        Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md)) {
            Button(
                text = "Send code",
                onClick = onSendCode,
                modifier = Modifier.fillMaxWidth(),
                enabled = !busy,
                loading = busy,
            )
            Button(
                text = "Cancel",
                onClick = onCancel,
                modifier = Modifier.fillMaxWidth(),
                style = ButtonStyle.plain(ActionColor.Neutral),
                enabled = !busy,
            )
        }
    }
}

/**
 * The recipe for a step with nothing to fill in: a result, or a wait on something else.
 *
 * The header says what happened; what to do about it sits in the footer, against the bottom edge
 * where a thumb already is.
 *
 * @param mark Glyph in the header's mark.
 * @param markColor Tone of the mark: success for a result, neutral for a wait or a dead end.
 * @param title What happened.
 * @param subtitle What it means for the reader.
 * @param branding Wordmark, hero and layout.
 * @param windowInsets Regions the system occupies.
 * @param progress What the screen is waiting on, or `null` when it is not waiting.
 * @param button Caption of the one button, or `null` for none.
 * @param onButton Called when the button is pressed.
 * @param link A way on for when the wait does not end by itself, or `null` for none.
 * @param onLink Called when the link is pressed.
 * @param note A last, quiet line, or `null` for none.
 */
@Composable
private fun OutcomeRecipe(
    mark: ImageVector,
    markColor: ActionColor,
    title: String,
    subtitle: String,
    branding: AuthBranding,
    windowInsets: WindowInsets,
    progress: String? = null,
    button: String? = null,
    onButton: () -> Unit = {},
    link: String? = null,
    onLink: () -> Unit = {},
    note: String? = null,
) {
    AuthScaffold(
        branding = branding,
        windowInsets = windowInsets,
        footer = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xl),
            ) {
                progress?.let { AuthProgress(text = it) }
                button?.let {
                    Button(
                        text = it,
                        onClick = onButton,
                        modifier = Modifier.fillMaxWidth(),
                        style = ButtonStyle.outlined(ActionColor.Neutral),
                    )
                }
                link?.let { AuthTextLink(text = it, onClick = onLink) }
                note?.let {
                    Text(text = it, variant = TextVariant.BodySmall, color = TextColor.Subtle)
                }
            }
        },
    ) {
        AuthHeader(
            title = title,
            subtitle = subtitle,
            icon = { AuthSamples.Mark(mark, markColor) },
        )
    }
}

/** The steps of an email-first flow, each a screen of its own. */
internal enum class EmailFirstScreen {
    /** The address, with single sign-on beside it. */
    Email,

    /** A provider's own window is open, somewhere else. */
    ProviderWaiting,

    /** A provider signed somebody in without a verified address. */
    ProviderEmail,

    /** The password of an account that exists. */
    Password,

    /** A reset link went to the address. */
    ResetSent,

    /** A code went to a new address, or to one a provider did not verify. */
    Code,

    /** A password for the new account. */
    CreatePassword,

    /** An existing account is signed in. */
    SignedIn,

    /** A new account is created and signed in. */
    Welcome,

    /** A new address, turned away by an invite-only product. */
    InviteOnly,
}

/** What the sample service has been asked to do. */
internal enum class EmailFirstWork {
    /** Find out whether the address has an account. */
    LookUp,

    /** Check a password. */
    SignIn,

    /** Check a code. */
    Verify,

    /** Create an account with a password. */
    Create,

    /** Finish a provider's exchange. */
    ProviderSignIn,

    /** Send a code to an address a provider did not verify. */
    SendCode,
}

/**
 * Everything the sample application holds for an email-first flow. The module holds none of it.
 *
 * @property screen The step on screen.
 * @property email Address as typed.
 * @property password Password as typed.
 * @property code Code as typed.
 * @property emailError Complaint about the address, or `null`.
 * @property passwordError Complaint about the password, or `null`.
 * @property codeError Complaint about the code, or `null`.
 * @property bannerTitle Headline of the failure banner, or `null`.
 * @property banner Why the last attempt failed, or `null`.
 * @property limited Whether the service is refusing password attempts for a while.
 * @property passwordChecked Whether a new password was refused, which turns unmet rules red.
 * @property resent Whether a new code was just sent.
 * @property cooldown Seconds until another code can be asked for.
 * @property attempts Wrong passwords so far.
 * @property provider Provider in use, or `null` for the email route.
 * @property work What the service is doing, or `null` when it is idle.
 * @property request Increments with every request, so the service answers each one once. Zero for
 *   a state the page started in, which the service leaves alone — a busy starting step stays busy.
 */
internal data class EmailFirstState(
    val screen: EmailFirstScreen = EmailFirstScreen.Email,
    val email: String = "",
    val password: String = "",
    val code: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val codeError: String? = null,
    val bannerTitle: String? = null,
    val banner: String? = null,
    val limited: Boolean = false,
    val passwordChecked: Boolean = false,
    val resent: Boolean = false,
    val cooldown: Int = 0,
    val attempts: Int = 0,
    val provider: AuthProvider? = null,
    val work: EmailFirstWork? = null,
    val request: Int = 0,
) {
    /** Whether the service is working, which makes the step inert. */
    val busy: Boolean get() = work != null

    /**
     * This state with a request to the service in flight.
     *
     * @param work What was asked for.
     * @return The busy state.
     */
    fun started(work: EmailFirstWork): EmailFirstState = copy(work = work, request = request + 1)

    /**
     * The sample service's answer to the request in flight.
     *
     * @param inviteOnly Whether a new address is turned away.
     * @return The state after the answer.
     */
    fun answered(inviteOnly: Boolean): EmailFirstState {
        val idle = copy(work = null)
        return when (work) {
            null -> this
            EmailFirstWork.LookUp -> when {
                email.equals(AuthSamples.Account, ignoreCase = true) -> idle.copy(screen = EmailFirstScreen.Password)
                inviteOnly -> idle.copy(screen = EmailFirstScreen.InviteOnly)
                else -> idle.copy(screen = EmailFirstScreen.Code, code = "", resent = false)
            }
            EmailFirstWork.SignIn -> when {
                password == AuthSamples.Password -> idle.copy(screen = EmailFirstScreen.SignedIn)
                attempts + 1 >= MaxAttempts -> idle.copy(limited = true, attempts = attempts + 1, passwordError = null)
                else -> idle.copy(
                    attempts = attempts + 1,
                    passwordError = "Incorrect password. Try again, or reset it.",
                )
            }
            EmailFirstWork.Verify -> when (val complaint = AuthSamples.codeComplaint(code)) {
                null -> if (provider != null) {
                    idle.copy(screen = EmailFirstScreen.Welcome)
                } else {
                    idle.copy(screen = EmailFirstScreen.CreatePassword, password = "", passwordChecked = false)
                }
                else -> idle.copy(codeError = complaint)
            }
            EmailFirstWork.Create -> idle.copy(screen = EmailFirstScreen.Welcome)
            EmailFirstWork.ProviderSignIn -> when (provider) {
                // Facebook does not always share an address — an account opened with a phone
                // number has none — which is the case worth drawing.
                AuthProvider.Facebook -> idle.copy(screen = EmailFirstScreen.ProviderEmail, email = "")
                else -> idle.copy(screen = EmailFirstScreen.SignedIn)
            }
            EmailFirstWork.SendCode -> idle.copy(screen = EmailFirstScreen.Code, code = "", resent = false)
        }
    }
}

/**
 * Where the page's flow can start, so every step and failure is one click away. The render test
 * draws every one.
 *
 * @property label Name in the control.
 * @property state The state the flow starts in.
 */
internal enum class EmailFirstStart(val label: String, val state: EmailFirstState) {
    /** An empty address box. */
    Email("Email", EmailFirstState()),

    /** An address the application refused. */
    EmailInvalid(
        "Email · invalid",
        EmailFirstState(email = "ada@example", emailError = "Enter an email address like name@example.com."),
    ),

    /** The address being looked up. */
    EmailLookingUp("Email · looking up", EmailFirstState(email = AuthSamples.Account, work = EmailFirstWork.LookUp)),

    /** The service could not be reached. */
    EmailFailed(
        "Email · network error",
        EmailFirstState(
            email = AuthSamples.Account,
            bannerTitle = "Something went wrong",
            banner = "We couldn't reach Areca. Check your connection and try again.",
        ),
    ),

    /** A provider's window was closed or failed. */
    ProviderFailed(
        "Email · provider error",
        EmailFirstState(
            bannerTitle = "Google sign-in didn't finish",
            banner = "The window was closed or Google returned an error. Try again, or continue with your email.",
        ),
    ),

    /** A provider's window is open. */
    ProviderWaiting(
        "Provider · waiting",
        EmailFirstState(screen = EmailFirstScreen.ProviderWaiting, provider = AuthProvider.Google),
    ),

    /** A provider shared no verified address. */
    ProviderEmail(
        "Provider · no email",
        EmailFirstState(screen = EmailFirstScreen.ProviderEmail, provider = AuthProvider.Facebook),
    ),

    /** A known account's password. */
    Password("Password", EmailFirstState(screen = EmailFirstScreen.Password, email = AuthSamples.Account)),

    /** A wrong password. */
    PasswordWrong(
        "Password · wrong",
        EmailFirstState(
            screen = EmailFirstScreen.Password,
            email = AuthSamples.Account,
            password = "areca2025",
            passwordError = "Incorrect password. Try again, or reset it.",
            attempts = 1,
        ),
    ),

    /** A password being checked. */
    PasswordSigningIn(
        "Password · signing in",
        EmailFirstState(
            screen = EmailFirstScreen.Password,
            email = AuthSamples.Account,
            password = AuthSamples.Password,
            work = EmailFirstWork.SignIn,
        ),
    ),

    /** Too many wrong passwords. */
    PasswordLimited(
        "Password · rate limited",
        EmailFirstState(
            screen = EmailFirstScreen.Password,
            email = AuthSamples.Account,
            password = "areca2024",
            limited = true,
            attempts = MaxAttempts,
        ),
    ),

    /** A reset link went out. */
    ResetSent("Reset link sent", EmailFirstState(screen = EmailFirstScreen.ResetSent, email = AuthSamples.Account)),

    /** A code went to a new address. */
    Code("Code", EmailFirstState(screen = EmailFirstScreen.Code, email = NewAccount)),

    /** A wrong code. */
    CodeWrong(
        "Code · wrong",
        EmailFirstState(
            screen = EmailFirstScreen.Code,
            email = NewAccount,
            code = "482913",
            codeError = AuthSamples.WrongCodeMessage,
        ),
    ),

    /** An expired code. */
    CodeExpired(
        "Code · expired",
        EmailFirstState(
            screen = EmailFirstScreen.Code,
            email = NewAccount,
            code = AuthSamples.ExpiredCode,
            codeError = AuthSamples.ExpiredCodeMessage,
        ),
    ),

    /** A code being checked. */
    CodeVerifying(
        "Code · verifying",
        EmailFirstState(
            screen = EmailFirstScreen.Code,
            email = NewAccount,
            code = AuthSamples.GoodCode,
            work = EmailFirstWork.Verify,
        ),
    ),

    /** A new code just went out. */
    CodeResent(
        "Code · resent",
        EmailFirstState(screen = EmailFirstScreen.Code, email = NewAccount, resent = true, cooldown = ResendCooldownSeconds),
    ),

    /** A new account's password. */
    CreatePassword("Create password", EmailFirstState(screen = EmailFirstScreen.CreatePassword, email = NewAccount)),

    /** A new password that breaks the rules, after a submit. */
    CreatePasswordRefused(
        "Create password · refused",
        EmailFirstState(
            screen = EmailFirstScreen.CreatePassword,
            email = NewAccount,
            password = "areca",
            passwordChecked = true,
            passwordError = "Choose a password that meets every rule below.",
        ),
    ),

    /** The account being created. */
    CreatePasswordCreating(
        "Create password · creating",
        EmailFirstState(
            screen = EmailFirstScreen.CreatePassword,
            email = NewAccount,
            password = AuthSamples.Password,
            work = EmailFirstWork.Create,
        ),
    ),

    /** An existing account signed in. */
    SignedIn("Signed in", EmailFirstState(screen = EmailFirstScreen.SignedIn, email = AuthSamples.Account)),

    /** A new account created. */
    Welcome("Account created", EmailFirstState(screen = EmailFirstScreen.Welcome, email = NewAccount)),

    /** A new address, turned away. */
    InviteOnly("Invite only", EmailFirstState(screen = EmailFirstScreen.InviteOnly, email = NewAccount)),
}

/**
 * The sample application's check on an address.
 *
 * @param email Address as typed.
 * @return The complaint, worded, or `null` when there is none.
 */
private fun emailComplaint(email: String): String? = when {
    email.isBlank() -> "Enter your email address."
    !Regex("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$").matches(email.trim()) -> "Enter an email address like name@example.com."
    else -> null
}

/**
 * A provider as the sample application names it in a sentence.
 *
 * @param provider The provider, or `null`.
 * @return Its name.
 */
private fun providerName(provider: AuthProvider?): String = when (provider) {
    AuthProvider.Apple -> "Apple"
    AuthProvider.Facebook -> "Facebook"
    AuthProvider.Google, AuthProvider.Email, null -> "Google"
}

/**
 * The resend countdown, worded the way the sample application words it.
 *
 * @param seconds Seconds left.
 * @return The sentence.
 */
private fun cooldownText(seconds: Int): String =
    "You can ask for another in ${seconds / 60}:${(seconds % 60).toString().padStart(2, '0')}."

/** Providers the page offers beside the form. Facebook is the one that shares no address. */
private val SsoProviders = listOf(AuthProvider.Apple, AuthProvider.Google, AuthProvider.Facebook)

/** An address the sample service has never seen. */
private const val NewAccount = "new@example.com"

/** Digits in the sample service's codes. */
private const val CodeLength = 6

/** Wrong passwords the sample service takes before it stops taking any. */
private const val MaxAttempts = 3

/** Seconds the sample service makes somebody wait between two codes. */
private const val ResendCooldownSeconds = 59

/** How long the sample service takes to answer. */
private val ServiceDelay = 900.milliseconds
