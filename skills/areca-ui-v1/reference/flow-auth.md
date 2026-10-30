# Auth flow — `dev.voir.areca:flow-auth`

Add it for sign-in, sign-up, email-first, code verification, pages an emailed link opens (reset,
finish registering, invitation, confirm address) and provider (SSO) buttons.

- Artifact `dev.voir.areca:flow-auth`. Depends on `foundation` as `api`. Needs `ArecaThemeProvider`.
- It ships **parts, a scaffold and recipes, not screens**. Copy the recipe below for the screen and
  edit it; never wrap the parts in a configurable `SignInScreen(…)`.
- The parts only draw and report intents. The app owns form state, validation, wording of complaints,
  networking, tokens, sessions and navigation. Flows never depend on each other, a navigation
  library, DI or `dev.voir.areca:viewmodel`.
- The provider marks are bundled; `dev.voir.areca:icons` is not needed (recipes that draw
  `ArecaIcons.*` need it).

| Package | Holds |
| --- | --- |
| `dev.voir.areca.flow.auth` | `AuthBranding`, `AuthLayout`, `AuthProvider`, `AuthLegalLinks` |
| `dev.voir.areca.flow.auth.part` | `AuthScaffold`, `AuthScaffoldDefaults`, `AuthHeader`, `AuthNameField`, `AuthEmailField`, `AuthPasswordField`, `AuthCodeField`, `AuthIdentityChip`, `AuthPasswordRequirements`, `AuthResendCode`, `AuthBanner`, `AuthProgress`, `AuthTextLink`, `AuthFooterPrompt`, `LegalConsent` |
| `dev.voir.areca.flow.auth.sso` | `SsoGroup`, `SsoButton`, `SsoIconButton`, `SsoLayout` |

## Choose a part

| Need | Use | Types |
| --- | --- | --- |
| The screen frame: insets, keyboard inset, scrolling, gutter, artwork, top bar, pinned footer, a column capped on larger windows | `AuthScaffold(branding, topBar, footer) { … }` (body is a `ColumnScope`) | `AuthBranding`, `AuthLayout`, `AuthScaffoldDefaults` |
| Title, subtitle and an optional mark; title is a heading | `AuthHeader(title, subtitle = …, icon = …)` | — |
| Name, email, password boxes with autofill, keyboard and IME set | `AuthNameField`, `AuthEmailField`, `AuthPasswordField` | — |
| A code by email or text, a digit per cell, one-time-code autofill, `onComplete` on the last digit | `AuthCodeField(value, onValueChange, onComplete = …)` | — |
| The account a step is about, with "Change" | `AuthIdentityChip(identifier, onChange = …)` | — |
| A new password's rules, ticked while typing | `AuthPasswordRequirements(requirements)` | foundation `Requirement`, `RequirementState`, `RequirementListLabels` |
| "Didn't get it? Send a new code", the sent line, the cooldown | `AuthResendCode(onResend, prompt, sent, cooldown)` | — |
| Why the last attempt failed (or why to wait), above the button, announced | `AuthBanner(message, title = …, tone = …)` | foundation `NoticeTone` |
| One line saying what the screen waits on | `AuthProgress(text)` | — |
| A standalone link over any ground (48 dp target) | `AuthTextLink(text, onClick)` | — |
| "No account? Sign up" as one sentence | `AuthFooterPrompt(prompt, linkText, onLinkClick)` | — |
| Terms and privacy, pressable in one translatable sentence | `LegalConsent(links, onLinkClick, sentence = …)` | `AuthLegalLinks` |
| Provider buttons, labelled or icon-only, as a group | `SsoGroup(providers, onProviderClick, layout = …)` | `AuthProvider`, `SsoLayout` |
| One provider button / one provider mark | `SsoButton(provider, onClick)` / `SsoIconButton(provider, onClick)` | `AuthProvider` |

For a field the module lacks (a workspace), use foundation `FieldGroup { field -> TextField(…, field = field) }`
in the scaffold body. `AuthCodeField` and `AuthPasswordRequirements` are built on foundation
`OtpField` and `RequirementList`, usable outside auth.

## Signatures

```kotlin
// dev.voir.areca.flow.auth
AuthBranding(
    wordmark: (@Composable () -> Unit)? = null,
    hero: (@Composable () -> Unit)? = null,
    layout: AuthLayout = AuthLayout.Plain,
)
AuthLayout.Plain | HeroTop(fraction: Float = 0.42f) | HeroBottom(fraction: Float = 0.42f) | HeroInline | FullBleed
    | Split(fraction: Float = 0.42f, fallback: AuthLayout = HeroTop())
    // fraction within AuthLayout.MinHeroFraction (0.1f) .. MaxHeroFraction (0.8f); height share for
    // HeroTop/HeroBottom, width share for Split. Split draws from WidthClass.Expanded (840dp) up while
    // HeightClass > Compact (ArecaTheme.window); elsewhere it draws `fallback`.
AuthProvider.Apple | Google | Facebook | Email
AuthLegalLinks(terms: String = "terms", privacy: String = "privacy")   // handed back to onLinkClick

// dev.voir.areca.flow.auth.part
@Composable fun AuthScaffold(
    modifier: Modifier = Modifier,
    branding: AuthBranding = AuthBranding(),
    gutter: Dp = ArecaTheme.spacing.xl3,
    windowInsets: WindowInsets = WindowInsets.systemBars,
    topBar: (@Composable () -> Unit)? = null,      // pinned above the form; first screen of a flow has none
    overlay: (@Composable BoxScope.() -> Unit)? = null,
    footer: (@Composable () -> Unit)? = null,      // pinned to the bottom edge; null = none
    maxWidth: Dp = AuthScaffoldDefaults.MaxWidth,  // 480.dp incl. gutter; applies past compact width only
    content: @Composable ColumnScope.() -> Unit,
)
object AuthScaffoldDefaults { val MaxWidth: Dp /* 480.dp */ }
@Composable fun AuthHeader(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    textAlign: TextAlign = TextAlign.Center,
    icon: (@Composable () -> Unit)? = null,        // e.g. a FeaturedIcon
)
@Composable fun AuthNameField(
    value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier,
    error: String? = null, focusRequester: FocusRequester? = null,
    imeAction: ImeAction = ImeAction.Next, onImeAction: (() -> Unit)? = null,
    label: String = "Full name", placeholder: String? = null, enabled: Boolean = true,
)
@Composable fun AuthEmailField(
    value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier,
    error: String? = null, newAccount: Boolean = false, focusRequester: FocusRequester? = null,
    imeAction: ImeAction = ImeAction.Next, onImeAction: (() -> Unit)? = null,
    label: String = "Email", placeholder: String? = null, enabled: Boolean = true,
)
@Composable fun AuthPasswordField(
    value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier,
    error: String? = null, newAccount: Boolean = false, focusRequester: FocusRequester? = null,
    imeAction: ImeAction = ImeAction.Done, onImeAction: (() -> Unit)? = null,
    label: String = "Password", placeholder: String? = null,
    revealDescription: String = "Show password", hideDescription: String = "Hide password",
    enabled: Boolean = true,
)
@Composable fun AuthCodeField(
    value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier,
    error: String? = null, focusRequester: FocusRequester? = null,
    imeAction: ImeAction = ImeAction.Done, onImeAction: (() -> Unit)? = null,
    label: String = "Verification code", assistive: String? = null,
    length: Int = 6,
    groupSize: Int? = null,                        // 3 for "123 456"; null for one run
    onComplete: ((String) -> Unit)? = null,        // called when the last digit lands
    enabled: Boolean = true,
)
@Composable fun AuthIdentityChip(
    identifier: String,
    modifier: Modifier = Modifier,
    onChange: (() -> Unit)? = null,                // null = no Change button
    changeLabel: String = "Change",
    onChangeLabel: String? = null,                 // what pressing does, read after the caption
    verified: Boolean = false,
    verifiedDescription: String = "Verified",
    label: String? = null,                         // "Account" when the page's only mention of it
    enabled: Boolean = true,
)
@Composable fun AuthPasswordRequirements(
    requirements: List<Requirement>,               // Requirement(text, RequirementState.Pending | Met | Unmet)
    modifier: Modifier = Modifier,
    columns: Int = 2,
    contentDescription: String? = null,
    labels: RequirementListLabels = RequirementListLabels.English,
)
@Composable fun AuthResendCode(
    onResend: () -> Unit,
    modifier: Modifier = Modifier,
    actionText: String = "Send a new code",
    prompt: String? = null,                        // "Didn't get it?"
    sent: String? = null,                          // announced when it appears
    cooldown: String? = null,                      // replaces the link while non-null; never announced
    textAlign: TextAlign = TextAlign.Center,
    enabled: Boolean = true,
)
@Composable fun AuthBanner(
    message: String?,                              // null hides it (animated)
    modifier: Modifier = Modifier,
    title: String? = null,
    tone: NoticeTone = NoticeTone.Danger,
    icon: ImageVector = /* bundled alert circle */,
    contentDescription: (title: String?, message: String) -> String = { t, m -> if (t == null) m else "$t. $m" },
)
@Composable fun AuthProgress(text: String, modifier: Modifier = Modifier)
@Composable fun AuthTextLink(text: String, onClick: () -> Unit, modifier: Modifier = Modifier, enabled: Boolean = true)
@Composable fun AuthFooterPrompt(
    prompt: String, linkText: String, onLinkClick: () -> Unit,
    modifier: Modifier = Modifier, textAlign: TextAlign = TextAlign.Center,
)
@Composable fun LegalConsent(
    links: AuthLegalLinks,
    onLinkClick: (String) -> Unit,                 // receives links.terms or links.privacy
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Center,
    sentence: String = "By continuing, you agree to our %1\$s and %2\$s.",
    termsLabel: String = "Terms of Service",
    privacyLabel: String = "Privacy Policy",
)

// dev.voir.areca.flow.auth.sso
@Composable fun SsoGroup(
    providers: List<AuthProvider>,                 // only what this platform can complete
    onProviderClick: (AuthProvider) -> Unit,
    modifier: Modifier = Modifier,
    layout: SsoLayout = SsoLayout.Buttons,         // Buttons (labelled pills) | Icons (row of marks)
    busyProvider: AuthProvider? = null,
    dividerText: String? = null,                   // rule above the providers, e.g. "or"; null = none
    providerLabel: (AuthProvider) -> String = { /* "Continue with Apple" … */ },
    enabled: Boolean = true,
)
@Composable fun SsoButton(
    provider: AuthProvider, onClick: () -> Unit, modifier: Modifier = Modifier,
    label: String = /* "Continue with <Provider>" */,
    size: ButtonSize = LocalButtonSize.current ?: ButtonSize.Medium,
    enabled: Boolean = true, loading: Boolean = false,
)
@Composable fun SsoIconButton(
    provider: AuthProvider, onClick: () -> Unit, modifier: Modifier = Modifier,
    contentDescription: String = /* "Continue with <Provider>" */,
    size: IconButtonSize = LocalIconButtonSize.current ?: IconButtonSize.Large,
    enabled: Boolean = true, loading: Boolean = false,
)
```

## Rules

- **Complaints are text.** Every field takes `error: String?`, already worded; the parts never
  validate. Clear a field's error when its value changes.
- **Busy state:** pass `enabled = !busy` to every part and `loading` to the button in flight;
  `busyProvider` spins one provider while the rest disable.
- **IME:** chain boxes with `focusRequester` + `onImeAction = { next.requestFocus() }`; the last box
  submits with `onImeAction = onSubmit`. Without `onImeAction` the platform action runs.
- `newAccount = true` on the email and password fields of a registration or new-password form, so
  password managers offer to generate and save.
- **Providers:** list only what the platform can complete (Apple on iOS, Google where Credential
  Manager runs; nothing native on the JVM). The module draws and reports; acquiring tokens is the
  app's. `AuthProvider.Email` is the way into the form — navigate, do not start an exchange.
  `SsoLayout.Buttons` when SSO is the offer; `SsoLayout.Icons` when it is the alternative to a form.
  Provider colours follow each provider's guidelines; do not restyle them.
- **Layouts:** `Plain` for a long form; `HeroTop` / `HeroBottom(fraction)` for artwork; `HeroInline`
  for an illustration above the heading; `FullBleed` puts artwork behind everything and flips the
  parts' foregrounds to `fgOnContrast` — put a scrim inside `hero` if the artwork is light;
  `Split(fraction, fallback)` for an app that also runs on tablets or desktops.
- **Larger windows:** never branch on the window in an auth screen; the scaffold does it from
  `ArecaTheme.window`. Compact width (under 600dp) draws exactly the phone screen. Wider, the column
  (wordmark, body, footer) stops at `maxWidth` (480dp) and sits centred, the top bar's controls line
  up with it, and with no band of artwork the body is centred in the height above the footer.
  `HeroTop` / `HeroBottom` bands still span the window; `FullBleed` art fills it. `Split` draws the
  hero as a full-height panel on the start side (fraction of the width) with the column centred
  beside it from 840dp wide when the height is not compact (so not a phone on its side), else its
  `fallback`. Pass `hero` artwork that crops to any shape (`ContentScale.Crop`, `fillMaxSize`). The
  body sits in a `WindowClassBox`, so parts inside see the column's width class. Preview at
  `ProvideWindowClass(WindowClass(DpSize(1440.dp, 900.dp), InputMode.Pointer)) { … }`.
- **Top bar:** a screen reached from another takes foundation `TopBar` in `topBar` with
  `BackAction`. The scaffold lands its controls on the gutter and uses `TopBarStyle.Scrim` over
  `FullBleed`. When changing the bar's size, start from `LocalTopBarSize.current ?: TopBarSize.Default`
  so the gutter survives.
- **Legal sentence:** one template with `%1$s` (terms) and `%2$s` (privacy), passed unformatted; a
  template missing or repeating a placeholder is refused.
- Confirming a new email address from a link takes an explicit button press, never automatic on open
  (mail scanners open links).
- Spacing between parts: `Spacer(Modifier.height(ArecaTheme.spacing.xl6))` under the header,
  `xl` between boxes, `xl2` before the primary button.

## Words and accessibility

- Required words default to English — field `label`s, `revealDescription` / `hideDescription`,
  `changeLabel`, `verifiedDescription`, `actionText`, `LegalConsent` `sentence` / `termsLabel` /
  `privacyLabel`, provider labels (`providerLabel`, `label`, `contentDescription`). Pass the app's
  strings for every one.
- Optional words default to `null` and draw nothing: `placeholder`, `dividerText`, `onChangeLabel`,
  `prompt`, `sent`, `cooldown`, `assistive`, banner `title`.
- `AuthBanner.contentDescription` joins title and message for screen readers — localise it and add a
  leading "Error" there if wanted.
- Built in: autofill content types (Android semantics, iOS `UITextContentType`: `name`, `username`,
  `password`, `newPassword`, `oneTimeCode`), keyboards, heading role on the title, 48 dp targets,
  sensitive-data marking on password and code, `AnnounceOnChange` on the banner, the resend `sent` line
  and `AuthProgress`. `AuthCodeField` is one field to a screen reader.

## Recipes

### Sign in

```kotlin
data class SignInState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val formError: String? = null,
    val submitting: Boolean = false,
    val busyProvider: AuthProvider? = null,
) { val busy: Boolean get() = submitting || busyProvider != null }

@Composable
fun SignInScreen(
    state: SignInState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
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
            onValueChange = onEmailChange,
            error = state.emailError,
            enabled = !state.busy,
            label = stringResource(Res.string.email),
            onImeAction = { passwordFocus.requestFocus() },
        )
        Spacer(Modifier.height(ArecaTheme.spacing.xl))
        AuthPasswordField(
            value = state.password,
            onValueChange = onPasswordChange,
            error = state.passwordError,
            enabled = !state.busy,
            focusRequester = passwordFocus,
            label = stringResource(Res.string.password),
            revealDescription = stringResource(Res.string.show_password),
            hideDescription = stringResource(Res.string.hide_password),
            onImeAction = onSubmit,
        )
        Spacer(Modifier.height(ArecaTheme.spacing.lg))
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
            AuthTextLink(stringResource(Res.string.forgot_password), onForgotPassword, enabled = !state.busy)
        }
        Spacer(Modifier.height(ArecaTheme.spacing.xl2))
        AuthBanner(message = state.formError)
        Button(
            text = stringResource(Res.string.sign_in_submit),
            onClick = onSubmit,
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.busy,
            loading = state.submitting,
        )
        Spacer(Modifier.height(ArecaTheme.spacing.xl2))
        SsoGroup(
            providers = platformProviders,              // e.g. listOf(AuthProvider.Apple, AuthProvider.Google)
            onProviderClick = onProviderClick,
            enabled = !state.busy,
            busyProvider = state.busyProvider,
            dividerText = stringResource(Res.string.or),
            providerLabel = { providerLabels.getValue(it) },
        )
    }
}
```

The check is the app's (ViewModel): set `emailError` / `passwordError` from its own strings on
submit, then call the service; map a refused sign-in to `formError`.

### On a tablet or desktop

The sign-in recipe above with one change — the branding. The scaffold draws the split from an
expanded width up and the fallback on phones, the foldable and a tablet held upright:

```kotlin
AuthScaffold(
    branding = AuthBranding(
        wordmark = { Wordmark() },
        hero = { Artwork() },                       // crops to any shape: a tall panel or a wide band
        layout = AuthLayout.Split(),                // Split(fraction = 0.42f, fallback = AuthLayout.HeroTop())
    ),
    footer = { AuthFooterPrompt(prompt = signUpPrompt, linkText = signUpLink, onLinkClick = onSignUp) },
) {
    // The sign-in body, unchanged: AuthHeader, AuthEmailField, AuthPasswordField, the forgot link,
    // AuthBanner, the Button and SsoGroup.
}
```

### Providers only

```kotlin
AuthScaffold(
    branding = AuthBranding(hero = { Artwork() }, layout = AuthLayout.FullBleed),
    footer = { LegalConsent(links = AuthLegalLinks(), onLinkClick = ::openDocument, sentence = legalSentence, termsLabel = termsLabel, privacyLabel = privacyLabel) },
) {
    AuthHeader(title = title, subtitle = subtitle)
    Spacer(Modifier.height(ArecaTheme.spacing.xl6))
    SsoGroup(
        providers = listOf(AuthProvider.Apple, AuthProvider.Google, AuthProvider.Email),
        onProviderClick = { if (it == AuthProvider.Email) onUseEmail() else onProviderClick(it) },
        providerLabel = { providerLabels.getValue(it) },
    )
}
```

### Email first: address, password, code, new password, outcome

Each step is its own screen, sequenced by the app's navigation.

```kotlin
// Address
AuthScaffold(branding = branding, footer = { LegalConsent(links = legalLinks, onLinkClick = ::openDocument, sentence = legalSentence) }) {
    AuthHeader(
        title = continueTitle,
        subtitle = continueSubtitle,
        icon = { FeaturedIcon(ArecaIcons.LogIn, contentDescription = null, style = FeaturedIconStyle.soft(ActionColor.Brand)) },
    )
    Spacer(Modifier.height(ArecaTheme.spacing.xl6))
    AuthBanner(message = state.banner, title = state.bannerTitle, contentDescription = { t, m -> listOfNotNull(errorWord, t, m).joinToString(". ") })
    AuthEmailField(value = state.email, onValueChange = onEmailChange, error = state.emailError, enabled = !state.busy,
        placeholder = emailPlaceholder, imeAction = ImeAction.Done, onImeAction = onContinue)
    Spacer(Modifier.height(ArecaTheme.spacing.xl2))
    Button(text = continueLabel, onClick = onContinue, modifier = Modifier.fillMaxWidth(), enabled = !state.busy, loading = state.lookingUp)
    Spacer(Modifier.height(ArecaTheme.spacing.xl2))
    SsoGroup(providers = providers, onProviderClick = onProviderClick, enabled = !state.busy,
        busyProvider = state.busyProvider, dividerText = orLabel, layout = SsoLayout.Icons)
}

// Password for a known account; rate limit as a warning banner and a disabled button saying how long
AuthScaffold(branding = branding, topBar = { TopBar(modifier = Modifier.fillMaxWidth(), leading = { BackAction(onClick = onChangeEmail) }) {} }) {
    AuthHeader(title = passwordTitle, subtitle = welcomeBack)
    Spacer(Modifier.height(ArecaTheme.spacing.xl6))
    AuthIdentityChip(identifier = state.email, onChange = onChangeEmail, changeLabel = changeLabel, onChangeLabel = useAnotherEmail, enabled = !state.busy)
    Spacer(Modifier.height(ArecaTheme.spacing.xl))
    AuthBanner(message = if (state.limited) limitedMessage else null, title = tooManyAttempts, tone = NoticeTone.Warning, icon = ArecaIcons.Clock)
    AuthPasswordField(value = state.password, onValueChange = onPasswordChange, error = state.passwordError,
        enabled = !state.busy && !state.limited, onImeAction = onSignIn)
    Spacer(Modifier.height(ArecaTheme.spacing.xl2))
    if (state.limited) {
        Button(text = tryAgainIn(state.retryIn), onClick = null, modifier = Modifier.fillMaxWidth(), enabled = false,
            leading = { Icon(ArecaIcons.Clock, contentDescription = null) })
    } else {
        Button(text = signInLabel, onClick = onSignIn, modifier = Modifier.fillMaxWidth(), enabled = !state.busy, loading = state.busy)
    }
}

// Code: verifies itself on the last digit
val codeFocus = remember { FocusRequester() }
LaunchedEffect(Unit) { codeFocus.requestFocus() }
AuthScaffold(branding = branding, topBar = { TopBar(modifier = Modifier.fillMaxWidth(), leading = { BackAction(onClick = onBack) }) {} }) {
    AuthHeader(title = checkEmailTitle, subtitle = codeSentSubtitle)
    Spacer(Modifier.height(ArecaTheme.spacing.xl6))
    AuthIdentityChip(identifier = state.email, onChange = onChangeEmail, enabled = !state.busy)
    Spacer(Modifier.height(ArecaTheme.spacing.xl))
    AuthCodeField(value = state.code, onValueChange = onCodeChange, onComplete = onVerify, error = state.codeError,
        enabled = !state.busy, focusRequester = codeFocus, label = codeLabel)
    Spacer(Modifier.height(ArecaTheme.spacing.xl))
    AuthResendCode(onResend = onResend, actionText = sendNewCode, prompt = didntGetIt,
        sent = if (state.resent) newCodeSent else null, cooldown = state.cooldown?.let(::askAgainIn), enabled = !state.busy)
    Spacer(Modifier.height(ArecaTheme.spacing.xl2))
    Button(text = verifyLabel, onClick = { onVerify(state.code) }, modifier = Modifier.fillMaxWidth(), enabled = !state.busy, loading = state.busy)
}

// New account's password with the service's rules: Pending until the first submit, Unmet after
AuthIdentityChip(identifier = state.email, verified = true, verifiedDescription = verifiedWord)
Spacer(Modifier.height(ArecaTheme.spacing.xl))
AuthPasswordField(value = state.password, onValueChange = onPasswordChange, error = state.passwordError, newAccount = true)
Spacer(Modifier.height(ArecaTheme.spacing.md))
val unmet = if (state.triedToSubmit) RequirementState.Unmet else RequirementState.Pending
AuthPasswordRequirements(
    requirements = serviceRules.map { Requirement(it.text, if (it.accepts(state.password)) RequirementState.Met else unmet) },
    contentDescription = requirementsLabel,
    labels = appRequirementLabels,
)

// Outcome or waiting step: header only, the next move in the footer
AuthScaffold(
    branding = branding,
    footer = {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xl)) {
            AuthProgress(text = waitingForGoogle)
            Button(text = cancelLabel, onClick = onCancel, modifier = Modifier.fillMaxWidth(), style = ButtonStyle.outlined(ActionColor.Neutral))
        }
    },
) { AuthHeader(title = continueWithGoogle, subtitle = finishInWindow) }
```

### Verify a code (email, text message, authenticator)

```kotlin
AuthScaffold(branding = branding, topBar = { TopBar(modifier = Modifier.fillMaxWidth(), leading = { BackAction(onClick = onBack) }) {} }) {
    AuthHeader(title = title, subtitle = subtitle)
    Spacer(Modifier.height(ArecaTheme.spacing.xl6))
    if (destination != null) {                     // address or number the code went to
        AuthIdentityChip(identifier = destination, onChange = onBack, enabled = !busy)
        Spacer(Modifier.height(ArecaTheme.spacing.xl))
    }
    AuthCodeField(value = code, onValueChange = onCodeChange, onComplete = onVerify, error = codeError,
        groupSize = groupSize /* 3 for SMS "123 456", null for one run */, enabled = !busy)
    if (onResend != null) {                        // an authenticator cannot resend
        Spacer(Modifier.height(ArecaTheme.spacing.xl))
        AuthResendCode(onResend = onResend, prompt = didntGetIt, sent = sent, cooldown = cooldown, enabled = !busy)
    }
    Spacer(Modifier.height(ArecaTheme.spacing.xl2))
    Button(text = verifyLabel, onClick = { onVerify(code) }, modifier = Modifier.fillMaxWidth(), enabled = !busy, loading = busy)
    if (alternative != null) {                     // "Use a recovery code"
        Spacer(Modifier.height(ArecaTheme.spacing.xl))
        AuthTextLink(text = alternative, onClick = onAlternative, enabled = !busy)
    }
}
```

### Pages an emailed link opens (reset, finish registering, invitation, confirm address)

```kotlin
// While the link is checked: the shape of the form to come
AuthScaffold(branding = branding, footer = { AuthProgress(text = checkingLink) }) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md)) {
        Skeleton(width = 48.dp, height = 48.dp)
        Skeleton(width = 220.dp, height = 28.dp)
        Skeleton(count = 2, textStyle = ArecaTheme.typography.bodySmall)
    }
    Spacer(Modifier.height(ArecaTheme.spacing.xl6))
    Skeleton(height = 48.dp)
}

// Reset: the account comes from the link, shown with a label
AuthScaffold(branding = branding, footer = { Text(linkWorksOnce, variant = TextVariant.BodyExtraSmall, color = TextColor.Muted) }) {
    AuthHeader(title = setNewPassword, subtitle = signedOutEverywhere)
    Spacer(Modifier.height(ArecaTheme.spacing.xl6))
    AuthBanner(message = state.failure, title = somethingWentWrong)
    AuthIdentityChip(identifier = account, label = accountLabel)
    Spacer(Modifier.height(ArecaTheme.spacing.xl))
    AuthPasswordField(value = state.password, onValueChange = onPasswordChange, error = state.passwordError, newAccount = true, label = newPasswordLabel)
    Spacer(Modifier.height(ArecaTheme.spacing.md))
    AuthPasswordRequirements(requirements = requirements)
    Spacer(Modifier.height(ArecaTheme.spacing.xl2))
    Button(text = updatePassword, onClick = onSubmit, modifier = Modifier.fillMaxWidth(), enabled = !state.busy, loading = state.busy)
}
```

Every ending — done, already done (409), expired, incomplete, rate limited (429) — is the outcome
shape above with one button in the footer; a rate limit disables it and says how long.

### Steps, custom footer, content around the form

```kotlin
// One step of several: back, the step, skip, progress along the bar's edge
topBar = {
    TopBar(
        title = stepOf(step, steps),
        modifier = Modifier.fillMaxWidth(),
        size = (LocalTopBarSize.current ?: TopBarSize.Default).copy(bottomPadding = PaddingValues()),
        titleAlignment = TopBarTitleAlignment.Center,
        leading = { BackAction(onClick = onBack) },
        trailing = { TextAction(text = skipLabel, onClick = onSkip, color = ActionColor.Neutral) },
        bottom = { TopBarProgress(progress = step / steps.toFloat()) },
    )
}

// Any footer
footer = {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        AuthTextLink(languageName, onClick = ::pickLanguage)
        AuthTextLink(needHelp, onClick = ::openSupport)
    }
}

// The body is a ColumnScope: put extra content and fields where they go
AuthScaffold(branding = branding) {
    AuthHeader(title = title)
    Spacer(Modifier.height(ArecaTheme.spacing.xl6))
    Text(invitedToWorkspace, variant = TextVariant.BodySmall)
    Spacer(Modifier.height(ArecaTheme.spacing.xl))
    AuthEmailField(…)
    Spacer(Modifier.height(ArecaTheme.spacing.xl))
    FieldGroup(label = workspaceLabel) { field -> TextField(value = workspace, onValueChange = onWorkspaceChange, field = field) }
    Spacer(Modifier.height(ArecaTheme.spacing.xl2))
    Button(text = signInLabel, onClick = onSubmit, modifier = Modifier.fillMaxWidth())
}
```
