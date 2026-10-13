package dev.voir.areca.flow.auth.part

import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.text.input.PlatformImeOptions

/**
 * What a credential box holds, as autofill and the keyboard need to be told it.
 *
 * Each platform reads this from a different place. Android's autofill reads the `contentType`
 * semantics ([contentType]). iOS reads `UITextContentType` from the keyboard options only, and
 * Compose derives it there from the keyboard type alone, so a new password, a one-time code and a
 * name have to be passed explicitly ([platformImeOptions]).
 */
internal enum class AuthTextKind {
    /** A person's full name. */
    Name,

    /** The email address of an existing account. */
    Email,

    /** The email address an account is being created with. */
    NewEmail,

    /** A password being recalled. */
    Password,

    /** A password being chosen. */
    NewPassword,

    /** A code sent to the person, or read off an authenticator. */
    OneTimeCode,
}

/**
 * The Android autofill type for this kind of box.
 *
 * A new account's email is both a new username and an email address, so a password manager offers
 * to save it as the account's name and a keyboard still offers addresses.
 */
internal val AuthTextKind.contentType: ContentType
    get() = when (this) {
        AuthTextKind.Name -> ContentType.PersonFullName
        AuthTextKind.Email -> ContentType.EmailAddress
        AuthTextKind.NewEmail -> ContentType.NewUsername + ContentType.EmailAddress
        AuthTextKind.Password -> ContentType.Password
        AuthTextKind.NewPassword -> ContentType.NewPassword
        AuthTextKind.OneTimeCode -> ContentType.SmsOtpCode
    }

/**
 * Whether what is typed must be hidden from accessibility services that are not assistive tools.
 *
 * A password and a one-time code are; a name and an address are not.
 */
internal val AuthTextKind.isSensitive: Boolean
    get() = when (this) {
        AuthTextKind.Password, AuthTextKind.NewPassword, AuthTextKind.OneTimeCode -> true
        AuthTextKind.Name, AuthTextKind.Email, AuthTextKind.NewEmail -> false
    }

/**
 * Platform keyboard options that carry this kind to the platform's own autofill.
 *
 * @return Options naming the iOS text content type, or `null` on platforms that read the
 *   `contentType` semantics instead.
 */
internal expect fun AuthTextKind.platformImeOptions(): PlatformImeOptions?
