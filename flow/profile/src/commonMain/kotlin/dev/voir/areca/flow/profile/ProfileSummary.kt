package dev.voir.areca.flow.profile

import androidx.compose.runtime.Immutable

/**
 * Who is signed in, already worded for display.
 *
 * Everything the identity parts draw — the hero, the summary card, the greeting, the editable
 * avatar — comes from this one value, so the surfaces that introduce an account cannot disagree
 * about whose it is.
 *
 * @property name Name the account is shown by. Also the source of the initials drawn when there is
 *   no picture, reduced the way `Avatar` reduces any name.
 * @property handle Public handle, already formatted — "@ada" — or `null` when the account has none.
 *   Cards show it in preference to [email], since it is what other people know the account by.
 * @property email Address the account signs in with, or `null` when it has none.
 * @property isVerified Whether `ProfileHeaderCard` and `ProfileCoverCard` draw the verified tick on
 *   the picture; the compact surfaces — the summary card, the greeting, an account row, the editable
 *   avatar — never draw it. What verified means — a confirmed address, a checked identity, an
 *   official account — is the product's.
 * @property avatar Picture model handed to the [AvatarImageLoader] in scope — a URL, bytes, a file,
 *   whatever that loader understands — or `null` when the account has no picture. The module never
 *   looks inside it.
 * @property caption Quiet line under the handle or address, such as "Member since March 2024" or
 *   "last seen recently", or `null` to draw none. Worded by the application, which knows the date
 *   and the locale.
 */
@Immutable
public data class ProfileSummary(
    public val name: String,
    public val handle: String? = null,
    public val email: String? = null,
    public val isVerified: Boolean = false,
    public val avatar: Any? = null,
    public val caption: String? = null,
)
