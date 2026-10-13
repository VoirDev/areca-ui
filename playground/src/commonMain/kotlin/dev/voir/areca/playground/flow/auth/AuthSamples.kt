package dev.voir.areca.playground.flow.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import dev.voir.areca.flow.auth.AuthLayout
import dev.voir.areca.flow.auth.AuthLegalLinks
import dev.voir.areca.flow.auth.AuthProvider
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.feedback.Requirement
import dev.voir.areca.foundation.feedback.RequirementState
import dev.voir.areca.foundation.icon.FeaturedIcon
import dev.voir.areca.foundation.icon.FeaturedIconStyle
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.theme.ArecaTheme

/**
 * Demo content shared by every page under `flow/`, so the pages document one product rather than
 * three invented ones.
 *
 * A consumer supplies a real wordmark and real artwork. These stand in for them and are
 * deliberately plain: a showcase that is interesting to look at stops being a test of the thing it
 * is showing.
 */
internal object AuthSamples {

    /** Terms and privacy addresses for the legal line. Never opened — the pages ignore the press. */
    val Legal = AuthLegalLinks(
        terms = "https://example.com/terms",
        privacy = "https://example.com/privacy",
    )

    /** The account that exists in the sample service. Any other address is a new one. */
    const val Account = "ada@example.com"

    /** The sample account's password. Anything else is wrong. */
    const val Password = "Areca#Palm2026"

    /** The code the sample service accepts. */
    const val GoodCode = "123456"

    /** A code the sample service has let expire. */
    const val ExpiredCode = "000000"

    /** How the sample service words a code that does not match. */
    const val WrongCodeMessage = "That code isn't right. Check your email and try again."

    /** How the sample service words a code that has run out. */
    const val ExpiredCodeMessage = "This code has expired. Send a new one below."

    /**
     * How the sample application has a failure banner read aloud: "Error", then the banner's title
     * and message, as English sentences.
     *
     * @param title Headline over the message, or `null` for none.
     * @param message Failure being explained.
     * @return The spoken description.
     */
    fun errorDescription(title: String?, message: String): String =
        listOfNotNull("Error", title, message).joinToString(". ")

    /**
     * The sample service's verdict on a complete code.
     *
     * @param code Code as typed.
     * @return The complaint, worded, or `null` when the code is right.
     */
    fun codeComplaint(code: String): String? = when (code) {
        GoodCode -> null
        ExpiredCode -> ExpiredCodeMessage
        else -> WrongCodeMessage
    }

    /**
     * The sample service's rules for a new password, checked against what was typed.
     *
     * The service's own rules, worded by the application — the module ships none.
     *
     * @param password Password as typed.
     * @param checked Whether a submit has been refused, which turns unmet rules from guidance into
     *   problems.
     * @return One requirement per rule, in reading order.
     */
    fun passwordRequirements(password: String, checked: Boolean): List<Requirement> {
        val unmet = if (checked) RequirementState.Unmet else RequirementState.Pending
        fun rule(text: String, met: Boolean) = Requirement(text, if (met) RequirementState.Met else unmet)
        return listOf(
            rule("12 or more characters", password.length >= 12),
            rule("Upper and lower case", password.any(Char::isUpperCase) && password.any(Char::isLowerCase)),
            rule("A number", password.any(Char::isDigit)),
            rule("A symbol", password.any { !it.isLetterOrDigit() }),
        )
    }

    /**
     * The mark the sample screens put above their title: a soft featured icon in the screen's tone.
     *
     * @param icon Glyph in the mark.
     * @param color Tone — brand for a step, success for an outcome, warning for a wait.
     */
    @Composable
    fun Mark(icon: ImageVector, color: ActionColor = ActionColor.Brand) {
        FeaturedIcon(imageVector = icon, contentDescription = null, style = FeaturedIconStyle.soft(color))
    }

    /** A wordmark, standing in for whatever brand mark a product puts above its forms. */
    @Composable
    fun Wordmark() {
        Text(text = "ARECA", variant = TextVariant.HeadingSmall, color = TextColor.Brand)
    }

    /**
     * Artwork for the split and full-bleed layouts.
     *
     * A gradient rather than a photograph, because a photograph in a showcase is something a reader
     * looks at instead of looking at the layout. Dark at its far end so the full-bleed layout's
     * light foreground has the ground that layout assumes.
     */
    @Composable
    fun Hero() {
        Box(
            modifier = Modifier.fillMaxSize().background(
                Brush.linearGradient(
                    colors = listOf(
                        ArecaTheme.baseColors.brand60,
                        ArecaTheme.baseColors.brand70,
                        ArecaTheme.baseColors.neutral100,
                    ),
                    start = Offset.Zero,
                    end = Offset.Infinite,
                ),
            ),
        )
    }

    /**
     * A small square of the same gradient, for [AuthLayout.HeroInline].
     *
     * Inline artwork sizes itself rather than filling a box the scaffold hands it, so this is the
     * shape that shows whether the scaffold really is leaving that decision to the slot.
     */
    @Composable
    fun Illustration() {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(ArecaTheme.shapes.xl)
                .background(
                    Brush.linearGradient(
                        listOf(ArecaTheme.baseColors.brand60, ArecaTheme.baseColors.brand70),
                    ),
                ),
        )
    }
}

/**
 * The hero placements, as something an `enumControl` can offer.
 *
 * [AuthLayout] is a sealed interface whose split variants carry a fraction, so it cannot itself be
 * an enum. This is the picker's view of it, and nothing more.
 *
 * @property label Name shown in the control.
 */
internal enum class AuthLayoutOption(val label: String) {
    /** No artwork. */
    Plain("Plain"),

    /** Artwork across the top. */
    HeroTop("Hero top"),

    /** Artwork across the bottom. */
    HeroBottom("Hero bottom"),

    /** Artwork inline, above the heading. */
    HeroInline("Hero inline"),

    /** Artwork behind everything, content pushed to the bottom. */
    FullBleed("Full bleed"),
    ;

    /**
     * @param heroFraction Share of the viewport to give the artwork. Ignored by the options that
     *   do not split the screen.
     * @return The layout this option selects.
     */
    fun toLayout(heroFraction: Float = AuthLayout.DefaultHeroFraction): AuthLayout = when (this) {
        Plain -> AuthLayout.Plain
        HeroTop -> AuthLayout.HeroTop(heroFraction)
        HeroBottom -> AuthLayout.HeroBottom(heroFraction)
        HeroInline -> AuthLayout.HeroInline
        FullBleed -> AuthLayout.FullBleed
    }
}

/**
 * Provider line-ups worth comparing, as something an `enumControl` can offer.
 *
 * Points on the axis rather than a checkbox per provider: what a reader is judging is how the block
 * looks at each length, and five independent toggles make that harder to reach, not easier.
 *
 * @property label Name shown in the control.
 * @property providers Providers the option selects, in draw order.
 */
internal enum class AuthProviderSet(
    val label: String,
    val providers: List<AuthProvider>,
) {
    /** No providers at all, which removes the block and its rule. */
    None("None", emptyList()),

    /** One provider — the most common configuration. */
    Google("Google", listOf(AuthProvider.Google)),

    /** The pair App Store review effectively requires together. */
    AppleAndGoogle("Apple + Google", listOf(AuthProvider.Apple, AuthProvider.Google)),

    /** Every provider, which is where a stack of labelled buttons stops being the right layout. */
    All(
        "All",
        listOf(
            AuthProvider.Apple,
            AuthProvider.Google,
            AuthProvider.Facebook,
            AuthProvider.Email,
        ),
    ),
}

/**
 * The artwork slot a layout option needs, or `null` for the option that draws none.
 *
 * Shared by every page under `flow/` so the pages offer the same artwork for the same option; a
 * per-page copy would drift the moment one of them gained a layout.
 *
 * @param option Layout the page's control currently selects.
 * @return Slot to pass as `AuthBranding.hero`.
 */
internal fun heroFor(option: AuthLayoutOption): (@Composable () -> Unit)? = when (option) {
    AuthLayoutOption.Plain -> null
    AuthLayoutOption.HeroInline -> ({ AuthSamples.Illustration() })
    AuthLayoutOption.HeroTop,
    AuthLayoutOption.HeroBottom,
    AuthLayoutOption.FullBleed,
    -> ({ AuthSamples.Hero() })
}

/**
 * What a sign-in screen holds — the application's own state. The module holds none: its parts take
 * values, complaints and flags as parameters.
 *
 * @property email Email as typed.
 * @property password Password as typed.
 * @property emailError The application's complaint about the email, worded, or `null`.
 * @property passwordError The application's complaint about the password, worded, or `null`.
 * @property formError Why the last attempt failed, worded, or `null`.
 * @property submitting Whether the email-and-password attempt is in flight.
 * @property busyProvider Provider whose exchange is in flight, or `null`.
 */
internal data class SignInScreenState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val formError: String? = null,
    val submitting: Boolean = false,
    val busyProvider: AuthProvider? = null,
) {
    /** Whether any attempt is in flight, which makes the whole form inert. */
    val busy: Boolean get() = submitting || busyProvider != null
}

/**
 * The sample application's own checks on a sign-in form — the kind of rule the module leaves to
 * the product.
 *
 * @return This state with its complaints brought up to date and the last failure cleared.
 */
internal fun SignInScreenState.checked(): SignInScreenState = copy(
    emailError = emailComplaint(email),
    passwordError = if (password.isEmpty()) "Enter your password." else null,
    formError = null,
)

/**
 * What a sign-up screen holds — the application's own state.
 *
 * @property name Name as typed.
 * @property email Email as typed.
 * @property password Password as typed.
 * @property passwordConfirmation Confirmation as typed.
 * @property nameError The application's complaint about the name, worded, or `null`.
 * @property emailError The application's complaint about the email, worded, or `null`.
 * @property passwordError The application's complaint about the password, worded, or `null`.
 * @property passwordConfirmationError The application's complaint about the confirmation, or `null`.
 * @property formError Why the last attempt failed, worded, or `null`.
 * @property submitting Whether the registration is in flight.
 * @property busyProvider Provider whose exchange is in flight, or `null`.
 */
internal data class SignUpScreenState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val passwordConfirmation: String = "",
    val nameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val passwordConfirmationError: String? = null,
    val formError: String? = null,
    val submitting: Boolean = false,
    val busyProvider: AuthProvider? = null,
) {
    /** Whether any attempt is in flight, which makes the whole form inert. */
    val busy: Boolean get() = submitting || busyProvider != null
}

/**
 * The sample application's own checks on a sign-up form.
 *
 * @param minimumPasswordLength Shortest password the sample service accepts.
 * @param collectName Whether the form has a name box to check.
 * @param confirmPassword Whether the form has a confirmation box to check.
 * @return This state with its complaints brought up to date and the last failure cleared.
 */
internal fun SignUpScreenState.checked(
    minimumPasswordLength: Int,
    collectName: Boolean,
    confirmPassword: Boolean,
): SignUpScreenState = copy(
    nameError = if (collectName && name.isBlank()) "Enter your name." else null,
    emailError = emailComplaint(email),
    passwordError = when {
        password.isEmpty() -> "Enter a password."
        password.length < minimumPasswordLength -> "Use at least $minimumPasswordLength characters."
        else -> null
    },
    passwordConfirmationError = when {
        !confirmPassword -> null
        passwordConfirmation.isEmpty() -> "Repeat your password."
        passwordConfirmation != password -> "The two passwords do not match."
        else -> null
    },
    formError = null,
)

/**
 * The sample application's check on an address.
 *
 * @param email Address as typed.
 * @return The complaint, worded, or `null` when there is none.
 */
private fun emailComplaint(email: String): String? = when {
    email.isBlank() -> "Enter your email address."
    '@' !in email -> "That does not look like an email address."
    else -> null
}
