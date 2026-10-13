package dev.voir.areca.flow.auth.sso

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import dev.voir.areca.flow.auth.AuthProvider
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.button.ButtonSize
import dev.voir.areca.foundation.button.ButtonStyle
import dev.voir.areca.foundation.button.IconButton
import dev.voir.areca.foundation.button.IconButtonSize
import dev.voir.areca.foundation.button.IconButtonStyle
import dev.voir.areca.foundation.button.LocalButtonSize
import dev.voir.areca.foundation.button.LocalIconButtonSize
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.foundation.icon.LocalIconSize
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.indication.FocusOutline
import dev.voir.areca.indication.TouchIndication
import dev.voir.areca.theme.ArecaTheme

/**
 * A provider's sign-in button, with its label.
 *
 * The full-width treatment: a pill in the provider's own colour, its mark on the left and a label
 * that names it. Use it when the button is a considered choice among a few — beneath a form, or as
 * the primary offer on a landing screen.
 *
 * The colours follow each provider's published button guidelines rather than the theme's tokens:
 * Apple and Google switch to their own dark variants on a dark theme, Facebook keeps its blue.
 *
 * @param provider Provider to draw.
 * @param onClick Called when the button is pressed.
 * @param modifier Modifier applied to the button.
 * @param label Text on the button. Defaults to "Continue with" and the provider's name.
 * @param size Padding, type and minimum bounds.
 * @param enabled Whether the button accepts input.
 * @param loading Whether this provider's exchange is in flight: a spinner at the trailing end, and
 *   presses are ignored. The mark stays, so the button still says whose exchange it is.
 */
@Composable
public fun SsoButton(
    provider: AuthProvider,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    label: String = provider.defaultLabel,
    size: ButtonSize = LocalButtonSize.current ?: ButtonSize.Medium,
    enabled: Boolean = true,
    loading: Boolean = false,
) {
    val brand = provider.brand

    Button(
        modifier = modifier,
        onClick = onClick,
        size = size,
        enabled = enabled,
        loading = loading,
        style = ButtonStyle(
            bg = brand.bg,
            bgDisabled = ArecaTheme.colors.bgDisabled,
            border = brand.border,
            content = brand.content,
            contentDisabled = ArecaTheme.colors.fgDisabled,
            extra = brand.content,
            extraDisabled = ArecaTheme.colors.fgDisabled,
            shape = CircleShape,
            indication = TouchIndication(
                shape = CircleShape,
                color = brand.content,
                focusOutline = FocusOutline(color = ArecaTheme.colors.accentBase),
            ),
        ),
        leading = { ProviderMark(brand = brand, enabled = enabled) },
    ) {
        Text(label)
    }
}

/**
 * A provider's sign-in button, reduced to its mark.
 *
 * The compact treatment: a circle in the provider's colour holding nothing but the mark, meant to
 * sit in a row of its peers under a rule. Use it when single sign-on is an alternative offered
 * beside a form rather than the main event — four labelled buttons stacked above a form take more
 * height than the form does, and stop reading as alternatives to each other.
 *
 * The mark alone is not a label, so the button carries the provider's name as its accessibility
 * label. The label sits on the button itself rather than on the mark, so it survives [loading]
 * replacing the mark with a spinner: a busy button is still announced as the provider's.
 *
 * @param provider Provider to draw.
 * @param onClick Called when the button is pressed.
 * @param modifier Modifier applied to the button.
 * @param contentDescription Accessibility label. Defaults to "Continue with" and the provider's
 *   name; a screen reader needs one, since the mark alone names nothing.
 * @param size Diameter and mark size.
 * @param enabled Whether the button accepts input.
 * @param loading Whether this provider's exchange is in flight, which replaces the mark with a
 *   spinner and ignores presses.
 */
@Composable
public fun SsoIconButton(
    provider: AuthProvider,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String = provider.defaultLabel,
    size: IconButtonSize = LocalIconButtonSize.current ?: IconButtonSize.Large,
    enabled: Boolean = true,
    loading: Boolean = false,
) {
    val brand = provider.brand

    IconButton(
        modifier = modifier.semantics { this.contentDescription = contentDescription },
        onClick = onClick,
        size = size,
        enabled = enabled,
        loading = loading,
        style = IconButtonStyle(
            bg = brand.bg,
            bgDisabled = ArecaTheme.colors.bgDisabled,
            border = brand.border,
            content = brand.content,
            contentDisabled = ArecaTheme.colors.fgDisabled,
            shape = CircleShape,
            indication = TouchIndication(
                shape = CircleShape,
                color = brand.content,
                focusOutline = FocusOutline(color = ArecaTheme.colors.accentBase),
            ),
        ),
    ) {
        ProviderMark(brand = brand, enabled = enabled, size = size.icon)
    }
}

/**
 * A provider's mark, drawn as its owner allows.
 *
 * A single-colour mark takes the button's content colour, and goes grey with the label when the
 * button is disabled. A multi-coloured mark is never recoloured — Google forbids a one-colour "G" —
 * so a disabled button fades it instead: a full-colour mark at full strength on a grey fill would
 * read as available.
 *
 * @param brand Provider treatment holding the mark and its colours.
 * @param enabled Whether the button accepts input.
 * @param size Edge length of the mark. Defaults to the size the enclosing button provides.
 */
@Composable
private fun ProviderMark(
    brand: SsoBrand,
    enabled: Boolean,
    size: Dp = LocalIconSize.current,
) {
    Icon(
        imageVector = brand.mark,
        contentDescription = null,
        modifier = if (!enabled && !brand.tintMark) Modifier.alpha(DisabledMarkAlpha) else Modifier,
        tint = when {
            !brand.tintMark -> Color.Unspecified
            enabled -> brand.content
            else -> ArecaTheme.colors.fgDisabled
        },
        size = size,
    )
}

/** Opacity of a multi-coloured mark on a disabled button, which may not be drawn in grey. */
private const val DisabledMarkAlpha = 0.38f

/**
 * This provider's English label, for a button the application has not worded.
 *
 * @return Line naming the provider, such as "Continue with Google".
 */
internal val AuthProvider.defaultLabel: String
    get() = when (this) {
        AuthProvider.Apple -> "Continue with Apple"
        AuthProvider.Google -> "Continue with Google"
        AuthProvider.Facebook -> "Continue with Facebook"
        AuthProvider.Email -> "Continue with Email"
    }
