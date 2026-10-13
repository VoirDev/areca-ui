package dev.voir.areca.flow.auth

/**
 * A way of getting into an account that is not this screen's email-and-password form.
 *
 * The module draws the button and reports the press. Acquiring an identity token is platform work
 * — Credential Manager on Android, `ASAuthorization` on iOS, nothing at all on the JVM — and none
 * of it lives here. See `flow/FLOWS.md` §5.
 *
 * An application offers the providers it can actually complete by passing them to
 * [dev.voir.areca.flow.auth.sso.SsoGroup] as its `providers` list; a provider absent from that list
 * is not drawn. That is why the group takes a list rather than a flag per provider: which providers
 * appear, in what order, is one decision rather than several.
 */
public enum class AuthProvider {
    /**
     * Sign in with Apple.
     *
     * The usual way an iOS app meets App Store Review Guideline 4.8: an app whose primary account
     * can be set up through another provider must also offer an equivalent login service that
     * collects no more than a name and an email address, lets the person keep the address
     * private, and does not track them for advertising without consent.
     */
    Apple,

    /** Sign in with Google. */
    Google,

    /** Log in with Facebook. */
    Facebook,

    /**
     * Continue with an email address instead of a provider.
     *
     * Not an identity provider at all, but it is a peer of the others where it appears: on a
     * landing screen whose primary offer is single sign-on, this is the way out of it and into the
     * form. Handling it normally means navigating to the sign-in screen rather than starting an
     * exchange.
     */
    Email,
}
