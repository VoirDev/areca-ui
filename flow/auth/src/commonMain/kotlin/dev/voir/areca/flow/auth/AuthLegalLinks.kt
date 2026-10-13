package dev.voir.areca.flow.auth

import androidx.compose.runtime.Immutable

/**
 * Addresses of the documents the legal line links to.
 *
 * Held as opaque strings rather than parsed URLs: the module never opens them. It reports which of
 * the two was pressed and the application decides whether that means a browser, an in-app reader,
 * or a screen of its own.
 *
 * Each property defaults to a key naming its document, so an application that routes by key rather
 * than by address can pass `AuthLegalLinks()` and receive `"terms"` or `"privacy"` back.
 *
 * @property terms Terms of service, as the application wants to receive it back.
 * @property privacy Privacy policy, as the application wants to receive it back.
 */
@Immutable
public data class AuthLegalLinks(
    public val terms: String = "terms",
    public val privacy: String = "privacy",
)
