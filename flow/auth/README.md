# `flow-auth`

Parts for building authentication screens, and recipes for putting them together.

```kotlin
implementation("dev.voir.areca:flow-auth:<version>")
```

Areca is published to GitHub Packages, so the build needs its repository first; see
[Installing](../../README.md#installing).

## What this is not

It is not a `SignInScreen`.

A sign-in screen is five product decisions wearing a trench coat: whether there is a form at all,
what sits above it, what sits below it, which providers appear and in what shape, and what the
bottom edge says. A composable taking all of those as parameters is harder to read than the thirty
lines it replaces, and it is wrong for the sixth product — the one that needs a workspace field
between the email and the password.

So the module ships the pieces, each carrying the things applications reliably get wrong, and this
file ships the assemblies. **Copy a recipe and edit it.** That is the intended use.

## The pieces

| | |
| --- | --- |
| `AuthScaffold` | Window insets, the keyboard inset, scrolling, the gutter, hero placement, an optional top bar, a footer pinned to the bottom edge |
| `AuthHeader` | Title and subtitle, and an optional mark above them; the title carries the heading role |
| `AuthNameField` `AuthEmailField` `AuthPasswordField` | One credential box each, with autofill, keyboard and IME already right |
| `AuthCodeField` | A code sent by email or text message, one digit to a cell, with one-time-code autofill and a callback when the last digit lands |
| `AuthIdentityChip` | The account a step is about — "ada@example.com" — with the way back to use another |
| `AuthPasswordRequirements` | A new password's rules, ticked off while typing and turned into the reason after a refused submit |
| `AuthResendCode` | "Didn't get it? Send a new code", the confirmation that one went out, and the cooldown before the next |
| `AuthBanner` | Why the last attempt failed — or why the form has to wait — above the button, announced when it appears |
| `AuthProgress` | One line saying what the screen is waiting on: a provider, a redirect, a link check |
| `AuthTextLink` `AuthFooterPrompt` | A link and a link-in-a-sentence that stay legible over artwork |
| `LegalConsent` | Terms and privacy, pressable inside one translatable sentence |
| `SsoGroup` `SsoButton` `SsoIconButton` | Provider buttons, labelled or icon-only |

`AuthCodeField` and `AuthPasswordRequirements` are built on two foundation components that are
useful outside authentication too: `OtpField` (`foundation.field`) and `RequirementList`
(`foundation.feedback`).

The parts only draw. Nothing here holds a form's values, decides what is valid, reaches the network,
stores a credential or knows what a session is. See
[`../FLOWS.md`](../FLOWS.md).

## Recipe: sign in

The state is the application's own — what is typed, what is wrong with it, what is in flight:

```kotlin
data class SignInState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val formError: String? = null,
    val submitting: Boolean = false,
    val busyProvider: AuthProvider? = null,
) {
    val busy: Boolean get() = submitting || busyProvider != null
}
```

```kotlin
@Composable
fun SignInScreen(
    state: SignInState,
    onStateChange: (SignInState) -> Unit,
    onSubmit: () -> Unit,
    onForgotPassword: () -> Unit,
    onProviderClick: (AuthProvider) -> Unit,
    onSignUp: () -> Unit,
) {
    val passwordFocus = remember { FocusRequester() }

    AuthScaffold(
        branding = AuthBranding(wordmark = { Wordmark() }),
        footer = {
            AuthFooterPrompt(
                prompt = stringResource(Res.string.sign_up_prompt),
                linkText = stringResource(Res.string.sign_up_link),
                onLinkClick = onSignUp,
            )
        },
    ) {
        AuthHeader(title = stringResource(Res.string.sign_in_title), subtitle = stringResource(Res.string.sign_in_subtitle))

        Spacer(Modifier.height(ArecaTheme.spacing.xl6))

        AuthEmailField(
            value = state.email,
            onValueChange = { onStateChange(state.copy(email = it, emailError = null)) },
            error = state.emailError,
            enabled = !state.busy,
            imeAction = ImeAction.Next,
            onImeAction = { passwordFocus.requestFocus() },
        )

        Spacer(Modifier.height(ArecaTheme.spacing.xl))

        AuthPasswordField(
            value = state.password,
            onValueChange = { onStateChange(state.copy(password = it, passwordError = null)) },
            error = state.passwordError,
            enabled = !state.busy,
            focusRequester = passwordFocus,
            imeAction = ImeAction.Done,
            onImeAction = onSubmit,
        )

        Spacer(Modifier.height(ArecaTheme.spacing.lg))

        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
            AuthTextLink(stringResource(Res.string.forgot_password), onForgotPassword, enabled = !state.busy)
        }

        Spacer(Modifier.height(ArecaTheme.spacing.xl2))

        AuthBanner(message = state.formError)

        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.busy,
            loading = state.submitting,
            onClick = onSubmit,
        ) {
            Text(stringResource(Res.string.sign_in_submit))
        }

        Spacer(Modifier.height(ArecaTheme.spacing.xl2))

        SsoGroup(
            providers = listOf(AuthProvider.Apple, AuthProvider.Google),
            onProviderClick = onProviderClick,
            enabled = !state.busy,
            busyProvider = state.busyProvider,
        )
    }
}
```

The fields take their complaint already worded, as `error: String?`, and draw it under the box.
Deciding it is the application's: a check on submit, the server's answer, or both, in whatever
language the product speaks.

```kotlin
// The application's view model — the module has no equivalent. `getString` from Compose resources
// suspends, so the check runs in the view model's scope.
fun submit() {
    viewModelScope.launch {
        val emailError = if (state.email.isBlank()) getString(Res.string.email_required) else null
        val passwordError = if (state.password.isEmpty()) getString(Res.string.password_required) else null
        state = state.copy(emailError = emailError, passwordError = passwordError, formError = null)
        if (emailError == null && passwordError == null) signIn(state.email, state.password)
    }
}
```

## Recipe: providers only, no form

Drop the fields and the button. Everything else is the same screen.

```kotlin
AuthScaffold(
    branding = AuthBranding(hero = { Artwork() }, layout = AuthLayout.FullBleed),
    footer = { LegalConsent(links = legalLinks, onLinkClick = ::openDocument) },
) {
    AuthHeader(title = stringResource(Res.string.sign_in_title), subtitle = stringResource(Res.string.sign_in_subtitle))

    Spacer(Modifier.height(ArecaTheme.spacing.xl6))

    SsoGroup(
        providers = listOf(AuthProvider.Apple, AuthProvider.Google, AuthProvider.Email),
        onProviderClick = onProviderClick,
        // Nothing above it, so nothing for a rule to separate it from.
        dividerText = null,
    )
}
```

`AuthProvider.Email` is the way out of single sign-on and into the form, not a fourth provider.
Handle it by navigating rather than by starting an exchange.

## Recipe: content above and below the form

The scaffold's body is an ordinary `ColumnScope`. There is no slot to find — put the content where
it goes. The playground's
[`ContentAroundFormShowcase`](../../playground/src/commonMain/kotlin/dev/voir/areca/playground/flow/auth/ContentAroundFormShowcase.kt)
draws this recipe.

```kotlin
AuthScaffold(branding = branding, footer = { … }) {
    AuthHeader(title = stringResource(Res.string.sign_in_title))

    Spacer(Modifier.height(ArecaTheme.spacing.xl6))

    // Above the form.
    Text("You were invited to the Northwind workspace.", variant = TextVariant.BodySmall)
    Spacer(Modifier.height(ArecaTheme.spacing.xl))

    AuthEmailField(…)
    Spacer(Modifier.height(ArecaTheme.spacing.xl))
    AuthPasswordField(…)

    // A field the module does not have.
    Spacer(Modifier.height(ArecaTheme.spacing.xl))
    FieldGroup(label = "Workspace") { field -> TextField(value = …, field = field) }

    Spacer(Modifier.height(ArecaTheme.spacing.xl2))
    Button(modifier = Modifier.fillMaxWidth(), onClick = onSubmit) { Text("Sign in") }

    // Below the form.
    Spacer(Modifier.height(ArecaTheme.spacing.xl))
    Text("Trouble signing in? Contact your administrator.", variant = TextVariant.BodyExtraSmall)
}
```

## Recipe: a way back, and steps

The first screen of a flow has no bar. Every screen reached from another takes one in `topBar` —
a foundation `TopBar`, pinned above the form rather than scrolling with it. The scaffold hands it
two defaults: its controls land on the form's `gutter`, and over `AuthLayout.FullBleed` artwork it
draws as `TopBarStyle.Scrim`, a dark disc that reads over the picture. Pass a `size` or `style` to
the bar to override either.

```kotlin
// Reached from sign-in: back, and nothing else.
AuthScaffold(
    branding = branding,
    topBar = { TopBar(modifier = Modifier.fillMaxWidth(), leading = { BackAction(onClick = onBack) }) {} },
) { … }

// One step of several: back, the step, a skip, and progress along the bar's edge.
AuthScaffold(
    branding = branding,
    topBar = {
        TopBar(
            title = stringResource(Res.string.step_of, step, steps),
            modifier = Modifier.fillMaxWidth(),
            size = (LocalTopBarSize.current ?: TopBarSize.Default).copy(bottomPadding = PaddingValues()),
            titleAlignment = TopBarTitleAlignment.Center,
            leading = { BackAction(onClick = onBack) },
            trailing = { TextAction(text = stringResource(Res.string.skip), onClick = onSkip, color = ActionColor.Neutral) },
            bottom = { TopBarProgress(progress = step / steps.toFloat()) },
        )
    },
) { … }
```

Read the size from `LocalTopBarSize` when changing it, as above, so the gutter the scaffold set
survives the change.

## Recipe: email first

One address box first; the service decides what comes next. A known account gets its password, a
new address gets a code and then a password, and an invite-only product turns a new address away.
Each step is its own screen, sequenced by the application's own state — the module has no
navigation and no idea which step is next.

The address step. Single sign-on sits beside the form, and the banner explains a failed lookup or a
provider window that was closed:

```kotlin
AuthScaffold(
    branding = branding,
    footer = { LegalConsent(links = legalLinks, onLinkClick = ::openDocument) },
) {
    AuthHeader(
        title = "Continue to Areca",
        subtitle = "Sign in or create an account with your email.",
        icon = { FeaturedIcon(ArecaIcons.LogIn, contentDescription = null, style = FeaturedIconStyle.soft(ActionColor.Brand)) },
    )

    Spacer(Modifier.height(ArecaTheme.spacing.xl6))

    AuthBanner(
        message = state.banner,
        title = state.bannerTitle,
        // What a screen reader hears, worded and joined by the application.
        contentDescription = { title, message -> listOfNotNull("Error", title, message).joinToString(". ") },
    )

    AuthEmailField(
        value = state.email,
        onValueChange = onEmailChange,
        error = state.emailError,
        enabled = !state.busy,
        placeholder = "name@example.com",
        imeAction = ImeAction.Done,
        onImeAction = onContinue,
    )

    Spacer(Modifier.height(ArecaTheme.spacing.xl2))

    Button(text = "Continue", onClick = onContinue, modifier = Modifier.fillMaxWidth(), enabled = !state.busy, loading = state.lookingUp)

    Spacer(Modifier.height(ArecaTheme.spacing.xl2))

    SsoGroup(providers = providers, onProviderClick = onProviderClick, enabled = !state.busy, busyProvider = state.busyProvider, dividerText = "or")
}
```

The password step names the account it is asking about, with the way back beside it. A rate limit
is a warning banner and a button that says how long:

```kotlin
AuthScaffold(
    branding = branding,
    topBar = { TopBar(modifier = Modifier.fillMaxWidth(), leading = { BackAction(onClick = onChangeEmail) }) {} },
) {
    AuthHeader(title = "Enter your password", subtitle = "Welcome back.", icon = { /* Lock mark */ })

    Spacer(Modifier.height(ArecaTheme.spacing.xl6))

    AuthIdentityChip(identifier = state.email, onChange = onChangeEmail, onChangeLabel = "Use a different email", enabled = !state.busy)

    Spacer(Modifier.height(ArecaTheme.spacing.xl))

    AuthBanner(
        message = if (state.limited) "For your security, wait 2 minutes or reset your password." else null,
        title = "Too many attempts",
        tone = NoticeTone.Warning,
        icon = ArecaIcons.Clock,
    )

    AuthPasswordField(
        value = state.password,
        onValueChange = onPasswordChange,
        error = state.passwordError,
        enabled = !state.busy && !state.limited,
        imeAction = ImeAction.Done,
        onImeAction = onSignIn,
    )

    Spacer(Modifier.height(ArecaTheme.spacing.lg))

    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
        AuthTextLink(text = "Forgot password?", onClick = onForgotPassword, enabled = !state.busy)
    }

    Spacer(Modifier.height(ArecaTheme.spacing.xl2))

    if (state.limited) {
        Button(
            text = "Try again in ${state.retryIn}",
            onClick = null,
            modifier = Modifier.fillMaxWidth(),
            enabled = false,
            leading = { Icon(ArecaIcons.Clock, contentDescription = null) },
        )
    } else {
        Button(text = "Sign in", onClick = onSignIn, modifier = Modifier.fillMaxWidth(), enabled = !state.busy, loading = state.busy)
    }
}
```

The code step verifies itself when the last digit lands; the button stays for anyone who wants to
press something. The resend line swaps its link for the countdown while another code cannot be
sent:

```kotlin
val codeFocus = remember { FocusRequester() }
LaunchedEffect(Unit) { codeFocus.requestFocus() }

AuthScaffold(branding = branding, topBar = { /* BackAction */ }) {
    AuthHeader(
        title = "Check your email",
        subtitle = "Enter the 6-digit code we sent to confirm your email. It expires in 10 minutes.",
        icon = { /* Mail mark */ },
    )

    Spacer(Modifier.height(ArecaTheme.spacing.xl6))

    AuthIdentityChip(identifier = state.email, onChange = onChangeEmail, enabled = !state.busy)

    Spacer(Modifier.height(ArecaTheme.spacing.xl))

    AuthCodeField(
        value = state.code,
        onValueChange = onCodeChange,
        onComplete = onVerify,
        error = state.codeError,
        enabled = !state.busy,
        focusRequester = codeFocus,
    )

    Spacer(Modifier.height(ArecaTheme.spacing.xl))

    AuthResendCode(
        onResend = onResend,
        prompt = "Didn't get it?",
        sent = if (state.resent) "New code sent." else null,
        cooldown = state.cooldown?.let { "You can ask for another in $it." },
        enabled = !state.busy,
    )

    Spacer(Modifier.height(ArecaTheme.spacing.xl2))

    Button(text = "Verify", onClick = { onVerify(state.code) }, modifier = Modifier.fillMaxWidth(), enabled = !state.busy, loading = state.busy)
}
```

The new account's password shows its rules under the box. They are the service's rules, worded
by the application; report each unmet one as `Pending` until the first submit and `Unmet` after:

```kotlin
AuthIdentityChip(identifier = state.email, verified = true)

Spacer(Modifier.height(ArecaTheme.spacing.xl))

AuthPasswordField(value = state.password, onValueChange = onPasswordChange, error = state.passwordError, newAccount = true)

Spacer(Modifier.height(ArecaTheme.spacing.md))

val unmet = if (state.triedToSubmit) RequirementState.Unmet else RequirementState.Pending
AuthPasswordRequirements(
    requirements = serviceRules.map { rule ->
        Requirement(rule.text, if (rule.accepts(state.password)) RequirementState.Met else unmet)
    },
    contentDescription = "Password requirements",
)
```

A step with nothing to fill in — signed in, a reset link sent, waiting on a provider's window,
turned away — is a header and a footer. What to do next sits against the bottom edge:

```kotlin
AuthScaffold(
    branding = branding,
    footer = {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xl)) {
            AuthProgress(text = "Waiting for Google…")
            Button(text = "Cancel", onClick = onCancel, modifier = Modifier.fillMaxWidth(), style = ButtonStyle.outlined(ActionColor.Neutral))
        }
    },
) {
    AuthHeader(
        title = "Continue with Google",
        subtitle = "Finish signing in in the Google window. This page updates when you're done.",
        icon = { /* LinkExternal mark */ },
    )
}
```

## Recipe: verify a code

The code step above, generalised: a code sent by email, sent by text message, or read off an
authenticator. The screen is the same; what changes is the words, how the code is grouped, whether
there is a destination to show and change, and whether another code can be sent at all. An
authenticator cannot be asked for one, so it offers a recovery code instead:

```kotlin
AuthScaffold(branding = branding, topBar = { /* BackAction */ }) {
    AuthHeader(title = title, subtitle = subtitle, icon = { /* the channel's mark */ })

    Spacer(Modifier.height(ArecaTheme.spacing.xl6))

    if (destination != null) {
        AuthIdentityChip(identifier = destination, onChange = onBack, enabled = !busy)
        Spacer(Modifier.height(ArecaTheme.spacing.xl))
    }

    // Match how the channel prints the code: 3 for a text message's two groups of three, null for one run.
    AuthCodeField(value = code, onValueChange = onCodeChange, onComplete = onVerify, error = codeError, groupSize = groupSize, enabled = !busy)

    if (onResend != null) {
        Spacer(Modifier.height(ArecaTheme.spacing.xl))
        AuthResendCode(onResend = onResend, prompt = "Didn't get it?", sent = sent, cooldown = cooldown, enabled = !busy)
    }

    Spacer(Modifier.height(ArecaTheme.spacing.xl2))

    Button(text = "Verify", onClick = { onVerify(code) }, modifier = Modifier.fillMaxWidth(), enabled = !busy, loading = busy)

    if (alternative != null) {
        Spacer(Modifier.height(ArecaTheme.spacing.xl))
        AuthTextLink(text = alternative, onClick = onAlternative, enabled = !busy)
    }
}
```

## Recipe: pages an emailed link opens

Reset a password, finish registering, accept an invitation, confirm a new address. Four flows, one
shape: the link is checked, the page asks for the one thing the link was for, and then says how it
went.

While the link is checked, draw the shape of the form to come, so it arrives where the eye already
is, and say what the page is waiting on:

```kotlin
AuthScaffold(branding = branding, footer = { AuthProgress(text = "Checking your link…") }) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md)) {
        Skeleton(width = 48.dp, height = 48.dp)
        Skeleton(width = 220.dp, height = 28.dp)
        Skeleton(count = 2, textStyle = ArecaTheme.typography.bodySmall)
    }
    Spacer(Modifier.height(ArecaTheme.spacing.xl6))
    Skeleton(height = 48.dp)
}
```

The account comes from the link, so it is shown — with a label, since it is the page's only mention
of it — rather than asked for:

```kotlin
AuthScaffold(branding = branding, footer = { Text("This link works once. Didn't ask for a reset? Close this page.", …) }) {
    AuthHeader(title = "Set a new password", subtitle = "You'll be signed out on every device.", icon = { /* Lock mark */ })

    Spacer(Modifier.height(ArecaTheme.spacing.xl6))

    AuthBanner(message = state.failure, title = "Something went wrong")

    AuthIdentityChip(identifier = account, label = "Account")

    Spacer(Modifier.height(ArecaTheme.spacing.xl))

    AuthPasswordField(value = state.password, onValueChange = onPasswordChange, error = state.passwordError, newAccount = true, label = "New password")

    Spacer(Modifier.height(ArecaTheme.spacing.md))

    AuthPasswordRequirements(requirements = requirements)

    Spacer(Modifier.height(ArecaTheme.spacing.xl2))

    Button(text = "Update password", onClick = onSubmit, modifier = Modifier.fillMaxWidth(), enabled = !state.busy, loading = state.busy)
}
```

Confirming a new address takes one explicit press rather than happening when the link opens:
mail scanners open links too, and an address that changed because a scanner looked at the message
is a takeover.

Every ending — done, already done (409), expired, incomplete, rate limited (429) — is the outcome
shape from the email-first recipe with one button in the footer. A rate limit disables the button
and says how long on it.

## Recipe: a custom footer

`footer` takes any composable. `AuthFooterPrompt` is a convenience for the common case, not a
requirement.

```kotlin
AuthScaffold(
    branding = branding,
    footer = {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            AuthTextLink("English (UK)", onClick = ::pickLanguage)
            AuthTextLink("Need help?", onClick = ::openSupport)
        }
    },
) { … }
```

Pass `footer = null` for no bottom edge at all.

## Layouts

`AuthBranding.layout` places the artwork. Five values, and they are the arrangements the design
system's onboarding set actually uses rather than an open-ended layout system:

| | |
| --- | --- |
| `Plain` | No artwork. The only one that gives the whole viewport to a long form |
| `HeroTop(fraction)` | Artwork across the top; scrolls away with the content so a keyboard leaves the form usable |
| `HeroBottom(fraction)` | Content on top, artwork below. Buttons within thumb reach |
| `HeroInline` | Artwork sized by its own slot, above the heading. For an illustration, not a photograph |
| `FullBleed` | Artwork behind everything, content against the bottom edge |

`FullBleed` flips every foreground the parts draw straight onto the background — headings, field
labels, links, the rule, the legal line — to `fgOnContrast`. That assumes artwork dark enough to
carry it. Artwork that is not should carry a scrim inside the `hero` slot; a lighter foreground is
not the fix.

## Words

There is no string table. Every word a part draws is a parameter of that part, and the application
passes its own — from its `composeResources`, in its language — where it composes the part:

```kotlin
AuthEmailField(
    value = state.email,
    onValueChange = onEmailChange,
    error = state.emailError,
    label = stringResource(Res.string.email),
    placeholder = stringResource(Res.string.email_placeholder),
)

SsoGroup(
    providers = providers,
    onProviderClick = onProviderClick,
    dividerText = stringResource(Res.string.or_continue_with),
    providerLabel = { provider -> providerLabels.getValue(provider) },
)
```

Two kinds of words, two rules:

- **Words a part cannot do without** — a field's label, a provider button's text, the legal
  sentence, the password reveal control's label — have a plain English default, so a part works
  before the application has translated it. Override every one of them in a localised application.
- **Words that are optional** — placeholders, the rule above the providers, the identity chip's
  action label — default to `null`, and `null` draws nothing. The part never invents copy the
  product did not write.

Where a part joins words into one sentence, the joining is a function the application can
replace, because a translation decides the order and the punctuation. `AuthBanner`'s
`contentDescription` receives the title and the message and returns what a screen reader says —
the place to add a leading "Error", too. Nothing else a part draws is joined: the resend line's
confirmation and countdown are separate lines.

Complaints under the fields are always the application's: a field takes `error: String?`, worded.

The legal line is one sentence, not fragments around its links, so a translation can put the two
documents in its own order. `%1$s` marks the terms link and `%2$s` the privacy link — the
placeholders Android and Compose resources already use — and the template is passed unformatted:

```kotlin
// <string name="legal_consent">By continuing, you agree to our %1$s and %2$s.</string>
LegalConsent(
    links = legalLinks,
    onLinkClick = ::openDocument,
    sentence = stringResource(Res.string.legal_consent),
    termsLabel = stringResource(Res.string.terms_of_service),
    privacyLabel = stringResource(Res.string.privacy_policy),
)
```

A sentence that leaves out either placeholder, or repeats one, is refused rather than drawn without
a document.

## Providers

The module draws the button and reports the press. Acquiring an identity token is platform work —
Credential Manager on Android, `ASAuthorization` on iOS, nothing at all on the JVM — and none of it
is here.

```kotlin
onProviderClick = { provider ->
    when (provider) {
        AuthProvider.Google -> scope.launch { googleTokens.request()?.let(viewModel::signIn) }
        AuthProvider.Email -> navigate(SignIn)
        else -> Unit
    }
}
```

List only what the current platform can complete. A provider absent from `providers` is not drawn,
which is why it is a list rather than a flag per provider.

`SsoGroup` has two treatments. `SsoLayout.Buttons` — labelled pills, stacked — when single sign-on
is the offer. `SsoLayout.Icons` — a row of circular marks — when it is the alternative to a form:
four providers cost four rows as buttons and one row as icons, and marks that do not fit the width
wrap onto another centred row.

Provider colours follow each provider's published button guidelines rather than the theme's
tokens; the marks are the `Social` icons of `:standalone:icons`. Apple is black on a light theme and
white on a dark one. Google is its neutral theme (`#F2F2F2`, `#1F1F1F` text) on a light theme and its
dark theme (`#131314`, a `#8E918F` stroke, `#E3E3E3` text) on a dark one, and its four-colour "G" is
never recoloured — a disabled Google button fades it instead of greying it. Facebook's fill is its
brand blue, `#0866FF`, in both, which keeps the white label above 4.5:1. Typefaces are the theme's,
not the providers' own; check the result against each provider's current terms before shipping.

`AuthProvider.Apple` is the usual way an iOS app meets App Store Review Guideline 4.8: an app
whose accounts can be set up through Google or Facebook must also offer a login service that
collects only a name and an email address, lets the address stay private, and does not track for
advertising without consent.

## Accessibility

The parts carry what applications usually miss, so a screen built from them gets it for free:
autofill content types on every credential box, a keyboard matched to what is being typed, an IME
action that goes somewhere, a heading role on the title, an announced failure banner, and an
accessibility label on every icon-only provider button that stays while it is busy.

Autofill types reach each platform where it reads them. Android reads the `contentType` semantics
on each box; iOS reads `UITextContentType` from the keyboard options, which the boxes set to `name`,
`username`, `password`, `newPassword` and `oneTimeCode`. The desktop has no platform autofill.

`newAccount = true` on the email and password fields is what makes a password manager offer to
generate and save rather than to fill, on Android and iOS alike. Set it on a registration form.

Without `onImeAction`, a box keeps the platform's own keyboard action: Next moves focus to the next
box and Done closes the keyboard. Pass a callback to chain boxes or submit, and it runs in place of
the platform's action. The email box also turns autocorrection and automatic capitalisation off,
so an address is not "corrected" on the way in.

Every control a finger presses on its own reserves a 48dp touch target, including `AuthTextLink`,
which draws at its text size. A link inside a sentence — the footer prompt, the legal line — is
part of the text and is not padded out.

The identity chip's "Change" button keeps its visible caption as its accessible name.
`onChangeLabel = "Use a different email"` labels what pressing it does, read after the caption,
rather than replacing what the screen reader calls the button.

`AuthCodeField` carries the one-time-code autofill type, so a keyboard offers the code it just read
from a message, and it is one field to a screen reader — one value, not six boxes of a digit each.
The password and code boxes are marked as sensitive data, which keeps their contents from
accessibility services that are not assistive tools.

The failure banner, the resend confirmation and `AuthProgress` are announced through foundation's
`AnnounceOnChange` when they appear and whenever their text changes — Android posts
through the view, iOS posts a VoiceOver announcement — so pressing "Send a new code" or starting a
wait is heard without anyone looking for what changed. A live region would stay silent here,
because Compose announces one only when an existing node changes, and these messages appear by
being composed. The resend countdown is drawn but never announced, so it can tick every second.
On the desktop nothing is announced; a screen reader reads each message when it reaches it.

## Where to look

The playground's **Auth flow** group has a page for every part, sectioned by what it does, and
renders these recipes live under **Screens** —
[`SignInShowcase`](../../playground/src/commonMain/kotlin/dev/voir/areca/playground/flow/auth/SignInShowcase.kt),
[`EmailFirstShowcase`](../../playground/src/commonMain/kotlin/dev/voir/areca/playground/flow/auth/EmailFirstShowcase.kt),
[`VerifyCodeShowcase`](../../playground/src/commonMain/kotlin/dev/voir/areca/playground/flow/auth/VerifyCodeShowcase.kt),
[`EmailLinkShowcase`](../../playground/src/commonMain/kotlin/dev/voir/areca/playground/flow/auth/EmailLinkShowcase.kt),
[`ContentAroundFormShowcase`](../../playground/src/commonMain/kotlin/dev/voir/areca/playground/flow/auth/ContentAroundFormShowcase.kt)
and their siblings hold the same assemblies as compiled code, so they cannot drift from something
that builds. `AuthFlowScreensRenderTest` draws every step of the last three, in both themes, to
`playground/build/screens/auth-flow`.
