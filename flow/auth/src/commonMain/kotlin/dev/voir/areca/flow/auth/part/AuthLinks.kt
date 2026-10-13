package dev.voir.areca.flow.auth.part

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import dev.voir.areca.foundation.text.TextLink
import dev.voir.areca.foundation.text.TextLinkPrompt
import dev.voir.areca.modifier.minimumTouchTarget

/**
 * The sentence across the bottom edge that offers the other screen — "New here? Create account".
 *
 * One line of a part, and it exists for one reason: it is the piece most likely to be drawn over
 * hero artwork, where a brand-coloured link on a brand-coloured background disappears. Routing it
 * through here means a screen gets the right foreground without knowing the rule exists.
 *
 * It is a convenience, not a requirement. [AuthScaffold]'s footer takes any composable, so a
 * product with a different bottom edge — a language picker, a support link, a version string —
 * passes that instead.
 *
 * @param prompt The part that is only text.
 * @param linkText The part that can be pressed.
 * @param onLinkClick Called when the link is activated by click, tap or keyboard.
 * @param modifier Modifier applied to the sentence.
 * @param textAlign Alignment within the width it is given.
 */
@Composable
public fun AuthFooterPrompt(
    prompt: String,
    linkText: String,
    onLinkClick: () -> Unit,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Center,
) {
    val onHero = LocalAuthOnHero.current

    TextLinkPrompt(
        prompt = prompt,
        linkText = linkText,
        onLinkClick = onLinkClick,
        modifier = modifier.fillMaxWidth(),
        color = AuthOnHero.promptColor(onHero),
        linkColor = AuthOnHero.linkColor(onHero),
        textAlign = textAlign,
    )
}

/**
 * A standalone pressable label on an authentication screen — "Forgot password?".
 *
 * Exists for the same reason as [AuthFooterPrompt]: it is drawn straight onto the background, so
 * over hero artwork it needs a foreground the theme's brand colour cannot give it.
 *
 * The label draws at its text size but reserves a 48dp touch target around itself, so a finger
 * that lands just above or below the words still presses it.
 *
 * @param text Label to show.
 * @param onClick Called when the link is activated by click, tap or keyboard.
 * @param modifier Modifier applied to the link.
 * @param enabled Whether the link accepts input. A disabled link renders as plain disabled text.
 */
@Composable
public fun AuthTextLink(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    TextLink(
        text = text,
        onClick = onClick,
        modifier = modifier.minimumTouchTarget(),
        enabled = enabled,
        color = AuthOnHero.linkColor(LocalAuthOnHero.current),
    )
}
