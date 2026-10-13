package dev.voir.areca.flow.auth.sso

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import dev.voir.areca.flow.auth.AuthProvider
import dev.voir.areca.flow.auth.icon.BundledIcons
import dev.voir.areca.flow.auth.icon.Mail
import dev.voir.areca.flow.auth.icon.SocialApple
import dev.voir.areca.flow.auth.icon.SocialFacebook
import dev.voir.areca.flow.auth.icon.SocialGoogle
import dev.voir.areca.theme.ArecaColors
import dev.voir.areca.theme.ArecaTheme

/**
 * How one provider's button looks.
 *
 * The provider entries follow each provider's published button guidelines rather than the theme:
 *
 * - **Apple** offers a black and a white button and asks for the one that contrasts with the
 *   ground — black on a light screen, white on a dark one.
 * - **Google** offers light, dark and neutral themes with fixed fills, strokes and text colours.
 *   The light screen takes the neutral theme (`#F2F2F2`, no stroke, `#1F1F1F` text), which sits on
 *   a white page without needing a stroke; the dark screen takes the dark theme (`#131314`, a
 *   `#8E918F` stroke, `#E3E3E3` text).
 * - **Facebook** is its brand blue, `#0866FF`, with white, in either theme. White on it is about
 *   4.8:1, above the 4.5:1 WCAG AA asks of a label.
 *
 * [AuthProvider.Email] has no owner and no guidelines, so it is the one entry resolved from the
 * theme's tokens — it is the way out of single sign-on rather than a provider, and it should look
 * like the product it is in.
 *
 * @property bg Button fill.
 * @property content Label colour on [bg].
 * @property border Stroke around the button, or [Color.Transparent] for none.
 * @property mark Provider mark, from the `Social` icons of `:standalone:icons`. Email, which has no
 *   brand, uses an envelope.
 * @property tintMark Whether the mark takes [content] as its tint.
 *
 *   Most provider marks are published as a single-colour silhouette whose own fill is the brand
 *   colour — the same colour as the fill they sit on — so drawn untinted they are invisible.
 *   Google's is the exception: it carries its own four colours, and Google forbids drawing it in
 *   one colour.
 */
@Immutable
internal data class SsoBrand(
    val bg: Color,
    val content: Color,
    val border: Color = Color.Transparent,
    val mark: ImageVector,
    val tintMark: Boolean,
)

/**
 * The brand treatment for this provider in the current theme.
 *
 * @return Colours and mark to draw the button with.
 */
internal val AuthProvider.brand: SsoBrand
    @Composable
    @ReadOnlyComposable
    get() = brand(isDark = ArecaTheme.isDark, colors = ArecaTheme.colors)

/**
 * The brand treatment for this provider against a light or a dark theme.
 *
 * @param isDark Whether the screen uses the dark theme, which decides Apple's and Google's variant.
 * @param colors Theme tokens, read only for [AuthProvider.Email].
 * @return Colours and mark to draw the button with.
 */
internal fun AuthProvider.brand(isDark: Boolean, colors: ArecaColors): SsoBrand = when (this) {
    AuthProvider.Apple -> SsoBrand(
        bg = if (isDark) Color.White else Color.Black,
        content = if (isDark) Color.Black else Color.White,
        mark = BundledIcons.SocialApple,
        tintMark = true,
    )

    AuthProvider.Google -> if (isDark) {
        SsoBrand(
            bg = GoogleDarkFill,
            content = GoogleDarkText,
            border = GoogleDarkStroke,
            mark = BundledIcons.SocialGoogle,
            tintMark = false,
        )
    } else {
        SsoBrand(
            bg = GoogleNeutralFill,
            content = GoogleNeutralText,
            mark = BundledIcons.SocialGoogle,
            tintMark = false,
        )
    }

    AuthProvider.Facebook -> SsoBrand(
        bg = FacebookBlue,
        content = Color.White,
        mark = BundledIcons.SocialFacebook,
        tintMark = true,
    )

    AuthProvider.Email -> SsoBrand(
        bg = colors.bgBrandMuted,
        content = colors.fgBrandText,
        mark = BundledIcons.Mail,
        tintMark = true,
    )
}

/** Fill of Google's neutral button theme. */
private val GoogleNeutralFill = Color(0xFFF2F2F2)

/** Label colour of Google's neutral button theme. */
private val GoogleNeutralText = Color(0xFF1F1F1F)

/** Fill of Google's dark button theme. */
private val GoogleDarkFill = Color(0xFF131314)

/** Stroke of Google's dark button theme. */
private val GoogleDarkStroke = Color(0xFF8E918F)

/** Label colour of Google's dark button theme. */
private val GoogleDarkText = Color(0xFFE3E3E3)

/** Facebook's brand blue. */
private val FacebookBlue = Color(0xFF0866FF)
