package dev.voir.areca.playground.registry

import dev.voir.areca.playground.flow.auth.AuthBannerShowcase
import dev.voir.areca.playground.flow.auth.AuthCodeFieldShowcase
import dev.voir.areca.playground.flow.auth.AuthEmailFieldShowcase
import dev.voir.areca.playground.flow.auth.AuthFooterPromptShowcase
import dev.voir.areca.playground.flow.auth.AuthHeaderShowcase
import dev.voir.areca.playground.flow.auth.AuthIdentityChipShowcase
import dev.voir.areca.playground.flow.auth.AuthNameFieldShowcase
import dev.voir.areca.playground.flow.auth.AuthPasswordFieldShowcase
import dev.voir.areca.playground.flow.auth.AuthPasswordRequirementsShowcase
import dev.voir.areca.playground.flow.auth.AuthProgressShowcase
import dev.voir.areca.playground.flow.auth.AuthResendCodeShowcase
import dev.voir.areca.playground.flow.auth.AuthScaffoldShowcase
import dev.voir.areca.playground.flow.auth.AuthTextLinkShowcase
import dev.voir.areca.playground.flow.auth.ContentAroundFormShowcase
import dev.voir.areca.playground.flow.auth.EmailFirstShowcase
import dev.voir.areca.playground.flow.auth.EmailLinkShowcase
import dev.voir.areca.playground.flow.auth.LegalConsentShowcase
import dev.voir.areca.playground.flow.auth.SignInShowcase
import dev.voir.areca.playground.flow.auth.SignUpShowcase
import dev.voir.areca.playground.flow.auth.SsoButtonShowcase
import dev.voir.areca.playground.flow.auth.SsoGroupShowcase
import dev.voir.areca.playground.flow.auth.SsoIconButtonShowcase
import dev.voir.areca.playground.flow.auth.SsoShowcase
import dev.voir.areca.playground.flow.auth.VerifyCodeShowcase
import dev.voir.areca.playground.showcase.ShowcaseStatus

/**
 * Screen and part pages for `:flow:auth`, in the [ShowcaseGroup.AuthFlow] group.
 *
 * Source order is for reading only: the sidebar sorts each section by title.
 */
internal val AuthFlowShowcases: List<ShowcaseEntry> = listOf(
    ShowcaseEntry(
        id = "flow-auth/sign-in",
        group = ShowcaseGroup.AuthFlow,
        section = ShowcaseSection.AuthScreens,
        title = "Sign in",
        summary = "Email and password sign-in, with optional single sign-on and recovery.",
        status = ShowcaseStatus.Beta,
        keywords = listOf(
            "auth", "sign in", "login", "log in", "password", "sso", "oauth", "screen",
        ),
        content = { SignInShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-auth/sign-up",
        group = ShowcaseGroup.AuthFlow,
        section = ShowcaseSection.AuthScreens,
        title = "Sign up",
        summary = "Account registration, with configurable fields and a legal line.",
        status = ShowcaseStatus.Beta,
        keywords = listOf(
            "auth", "sign up", "register", "registration", "account", "onboarding", "screen",
        ),
        content = { SignUpShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-auth/content-around-form",
        group = ShowcaseGroup.AuthFlow,
        section = ShowcaseSection.AuthScreens,
        title = "Content around the form",
        summary = "A sign-in screen with a line above the form, a workspace field the module does not " +
            "have, and a line below it.",
        status = ShowcaseStatus.Beta,
        keywords = listOf(
            "content above form", "content below form", "custom field", "workspace", "invitation",
            "sign in", "screen",
        ),
        content = { ContentAroundFormShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-auth/sso",
        group = ShowcaseGroup.AuthFlow,
        section = ShowcaseSection.AuthScreens,
        title = "Single sign-on",
        summary = "Provider buttons in both treatments, on a screen with no form at all.",
        status = ShowcaseStatus.Beta,
        keywords = listOf(
            "sso", "oauth", "social", "apple", "google", "facebook", "twitter", "provider",
        ),
        content = { SsoShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-auth/email-first",
        group = ShowcaseGroup.AuthFlow,
        section = ShowcaseSection.AuthScreens,
        title = "Email first",
        summary = "One address box that leads to a password for a known account or a code for a new " +
            "one, with single sign-on beside it.",
        status = ShowcaseStatus.Beta,
        keywords = listOf(
            "identifier first", "email first", "sign in", "sign up", "passwordless", "magic code",
            "otp", "rate limit", "screen",
        ),
        content = { EmailFirstShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-auth/verify-code",
        group = ShowcaseGroup.AuthFlow,
        section = ShowcaseSection.AuthScreens,
        title = "Verify code",
        summary = "A code screen for email, text message or an authenticator app, verifying on the last digit.",
        status = ShowcaseStatus.Beta,
        keywords = listOf(
            "otp", "one-time code", "2fa", "two-step", "mfa", "sms code", "authenticator",
            "verification", "screen",
        ),
        content = { VerifyCodeShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-auth/email-link",
        group = ShowcaseGroup.AuthFlow,
        section = ShowcaseSection.AuthScreens,
        title = "Email link",
        summary = "The pages an emailed link opens — reset, registration, invitation, email change — " +
            "from checking the link to how it ended.",
        status = ShowcaseStatus.Beta,
        keywords = listOf(
            "reset password", "magic link", "invitation", "invite", "email change", "confirm email",
            "expired link", "landing", "screen",
        ),
        content = { EmailLinkShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-auth/progress",
        group = ShowcaseGroup.AuthFlow,
        section = ShowcaseSection.AuthLayout,
        title = "Progress",
        summary = "One line saying what the screen is waiting on — a provider, a redirect, a link " +
            "check — with a spinner beside it.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("auth progress", "waiting", "redirecting", "spinner", "loading", "status line"),
        content = { AuthProgressShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-auth/code-field",
        group = ShowcaseGroup.AuthFlow,
        section = ShowcaseSection.AuthFields,
        title = "Code field",
        summary = "The box for a code sent by email or text message, with one-time-code autofill and a " +
            "callback when the last digit lands.",
        status = ShowcaseStatus.Beta,
        keywords = listOf(
            "auth code field", "otp", "verification code", "one-time code", "sms code", "autofill", "pin",
        ),
        content = { AuthCodeFieldShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-auth/identity-chip",
        group = ShowcaseGroup.AuthFlow,
        section = ShowcaseSection.AuthFields,
        title = "Identity chip",
        summary = "The account a step of an email-first flow is about, with the way back to choose " +
            "another right beside it.",
        status = ShowcaseStatus.Beta,
        keywords = listOf(
            "auth identity chip", "account", "email chip", "change email", "use a different email",
            "verified", "username",
        ),
        content = { AuthIdentityChipShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-auth/password-requirements",
        group = ShowcaseGroup.AuthFlow,
        section = ShowcaseSection.AuthFields,
        title = "Password requirements",
        summary = "The rules a new password has to meet, ticked off as it is typed and turned into the " +
            "reason after a refused submit.",
        status = ShowcaseStatus.Beta,
        keywords = listOf(
            "auth password requirements", "password rules", "password strength", "checklist",
            "new password", "criteria",
        ),
        content = { AuthPasswordRequirementsShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-auth/resend-code",
        group = ShowcaseGroup.AuthFlow,
        section = ShowcaseSection.AuthLinks,
        title = "Resend code",
        summary = "The way to ask for another code, the confirmation that one went out, and the wait " +
            "before the next.",
        status = ShowcaseStatus.Beta,
        keywords = listOf(
            "auth resend code", "resend", "didn't get it", "send a new code", "cooldown", "countdown",
        ),
        content = { AuthResendCodeShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-auth/banner",
        group = ShowcaseGroup.AuthFlow,
        section = ShowcaseSection.AuthLayout,
        title = "Banner",
        summary = "Says why the last attempt failed, just above the button that tries again, and " +
            "announces it to assistive technology.",
        status = ShowcaseStatus.Beta,
        keywords = listOf(
            "auth banner", "error", "failure", "alert", "sign in failed", "form error", "live region", "message",
        ),
        content = { AuthBannerShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-auth/header",
        group = ShowcaseGroup.AuthFlow,
        section = ShowcaseSection.AuthLayout,
        title = "Header",
        summary = "The title and one-line subtitle that open an authentication screen, with the " +
            "title marked as the screen's heading.",
        status = ShowcaseStatus.Beta,
        keywords = listOf(
            "auth header", "title", "subtitle", "heading", "sign in title", "welcome", "screen header", "login",
        ),
        content = { AuthHeaderShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-auth/scaffold",
        group = ShowcaseGroup.AuthFlow,
        section = ShowcaseSection.AuthLayout,
        title = "Scaffold",
        summary = "The frame every authentication screen is built in: insets, scrolling, hero " +
            "placement and a footer pinned to the bottom edge.",
        status = ShowcaseStatus.Beta,
        keywords = listOf(
            "auth scaffold", "auth layout", "hero", "branding", "wordmark", "full bleed", "window insets",
            "onboarding screen", "footer", "AuthBranding", "AuthLayout",
        ),
        content = { AuthScaffoldShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-auth/email-field",
        group = ShowcaseGroup.AuthFlow,
        section = ShowcaseSection.AuthFields,
        title = "Email field",
        summary = "The email box of a sign-in or sign-up form, with the email keyboard, the right " +
            "autofill type for either and an error the application words.",
        status = ShowcaseStatus.Beta,
        keywords = listOf(
            "email", "address", "login", "sign in", "username", "autofill", "text field", "form",
        ),
        content = { AuthEmailFieldShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-auth/legal-consent",
        group = ShowcaseGroup.AuthFlow,
        section = ShowcaseSection.AuthFields,
        title = "Legal consent",
        summary = "The \"by continuing, you agree to…\" line, with the terms and the privacy policy " +
            "pressable inside one sentence.",
        status = ShowcaseStatus.Beta,
        keywords = listOf(
            "terms", "privacy policy", "legal", "consent", "agreement", "terms of service", "disclaimer",
        ),
        content = { LegalConsentShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-auth/name-field",
        group = ShowcaseGroup.AuthFlow,
        section = ShowcaseSection.AuthFields,
        title = "Name field",
        summary = "The full-name box of a sign-up form, with name autofill, capitalised words and an " +
            "error the application words.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("full name", "name input", "sign up", "register", "autofill", "text field", "form"),
        content = { AuthNameFieldShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-auth/password-field",
        group = ShowcaseGroup.AuthFlow,
        section = ShowcaseSection.AuthFields,
        title = "Password field",
        summary = "The password box of a sign-in or sign-up form, masked with a reveal toggle, with " +
            "the autofill type for recalling or choosing a password.",
        status = ShowcaseStatus.Beta,
        keywords = listOf(
            "password", "secret", "show password", "confirm password", "new password", "autofill", "login",
        ),
        content = { AuthPasswordFieldShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-auth/footer-prompt",
        group = ShowcaseGroup.AuthFlow,
        section = ShowcaseSection.AuthLinks,
        title = "Footer prompt",
        summary = "The sentence along the bottom edge of an authentication screen that offers the " +
            "other screen, with the link inside it.",
        status = ShowcaseStatus.Beta,
        keywords = listOf(
            "footer", "create account", "sign up link", "already have an account", "prompt",
            "switch screen",
        ),
        content = { AuthFooterPromptShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-auth/text-link",
        group = ShowcaseGroup.AuthFlow,
        section = ShowcaseSection.AuthLinks,
        title = "Text link",
        summary = "A standalone pressable label on an authentication screen, such as \"Forgot " +
            "password?\", that stays legible over artwork.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("auth text link", "link", "forgot password", "recover", "resend code", "text button", "hyperlink"),
        content = { AuthTextLinkShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-auth/sso-button",
        group = ShowcaseGroup.AuthFlow,
        section = ShowcaseSection.AuthSso,
        title = "SSO button",
        summary = "A provider's labelled sign-in button, in the provider's own brand colours, for " +
            "when single sign-on is a considered choice.",
        status = ShowcaseStatus.Beta,
        keywords = listOf(
            "sign in with google", "apple sign in", "social login", "oauth", "provider button",
            "continue with",
        ),
        content = { SsoButtonShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-auth/sso-group",
        group = ShowcaseGroup.AuthFlow,
        section = ShowcaseSection.AuthSso,
        title = "SSO group",
        summary = "A block of provider buttons, stacked or in a row, optionally under a labelled rule.",
        status = ShowcaseStatus.Beta,
        keywords = listOf(
            "social login", "sso", "or continue with", "provider list", "divider", "oauth buttons",
            "sign in options", "SsoLayout",
        ),
        content = { SsoGroupShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-auth/sso-icon-button",
        group = ShowcaseGroup.AuthFlow,
        section = ShowcaseSection.AuthSso,
        title = "SSO icon button",
        summary = "A provider's sign-in button reduced to its mark, for a compact row of " +
            "alternatives beside a form.",
        status = ShowcaseStatus.Beta,
        keywords = listOf(
            "social icon", "provider mark", "sign in with apple", "google logo button", "oauth icon",
            "compact sso",
        ),
        content = { SsoIconButtonShowcase() },
    ),
)
