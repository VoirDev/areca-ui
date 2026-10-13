package dev.voir.areca.flow.auth.part

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.theme.ArecaTheme

/**
 * The title and the line under it that open an authentication screen.
 *
 * The title carries a heading role, so a screen reader can jump to it and so the screen announces
 * what it is on arrival. Nothing else on these screens is a heading — the field labels belong to
 * their controls — which makes this the one landmark worth getting right.
 *
 * Both foregrounds flip when the header is drawn over full-bleed artwork, which it learns from the
 * scaffold rather than from a parameter: a consumer composing this part by hand inside
 * [AuthScaffold] gets the right treatment without knowing the rule exists.
 *
 * @param title What this screen is.
 * @param modifier Modifier applied to the header column.
 * @param subtitle One line saying what it is for, or `null` for a title on its own.
 * @param textAlign Alignment of both lines, and of [icon].
 * @param icon Mark above the title — normally a foundation `FeaturedIcon` saying what kind of
 *   screen this is: an envelope for "check your email", a tick for "you're signed in" — or `null`
 *   for none. Decorative: the title already says it, so give the mark no content description.
 */
@Composable
public fun AuthHeader(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    textAlign: TextAlign = TextAlign.Center,
    icon: (@Composable () -> Unit)? = null,
) {
    val onHero = LocalAuthOnHero.current

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = textAlign.toHorizontalAlignment(),
    ) {
        if (icon != null) {
            icon()
            Spacer(Modifier.height(ArecaTheme.spacing.xl2))
        }

        Text(
            text = title,
            variant = TextVariant.HeadingLarge,
            color = if (onHero) TextColor.OnContrast else TextColor.Base,
            textAlign = textAlign,
            modifier = Modifier
                .fillMaxWidth()
                .semantics { heading() },
        )

        if (subtitle != null) {
            Spacer(Modifier.height(ArecaTheme.spacing.sm))

            Text(
                text = subtitle,
                variant = TextVariant.BodySmall,
                // Full contrast rather than subtle over artwork: a dimmed foreground that reads on a page
                // background disappears on a photograph.
                color = if (onHero) TextColor.OnContrast else TextColor.Subtle,
                textAlign = textAlign,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
