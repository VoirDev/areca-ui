package dev.voir.areca.flow.auth.part

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import dev.voir.areca.foundation.AnnounceOnChange
import dev.voir.areca.foundation.feedback.Spinner
import dev.voir.areca.foundation.feedback.SpinnerSize
import dev.voir.areca.foundation.feedback.SpinnerStyle
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.theme.ArecaTheme

/**
 * One line saying the screen is waiting on something that is not the person — "Waiting for
 * Google…", "Returning to the app…", "Checking your link…" — with a spinner beside it.
 *
 * For the screens with nothing to press while they wait: a provider's window is open somewhere
 * else, a redirect is under way, a link is being checked. A button with a spinner in it would offer
 * an action that is not there; a spinner with no words leaves a person guessing what they are
 * waiting for, and whether it is worth waiting.
 *
 * The line is announced when it is first composed and whenever [text] changes, through the
 * platform's announcement API (see [AnnounceOnChange]), so assistive technology says what the screen
 * is waiting on when it starts waiting.
 *
 * @param text What the screen is waiting on, worded, ending in an ellipsis by convention.
 * @param modifier Modifier applied to the line.
 */
@Composable
public fun AuthProgress(
    text: String,
    modifier: Modifier = Modifier,
) {
    val onHero = LocalAuthOnHero.current
    val color = AuthOnHero.statusColor(onHero)

    AnnounceOnChange(text)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .semantics(mergeDescendants = true) {},
        horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.sm, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spinner(
            size = SpinnerSize.Small,
            style = SpinnerStyle(
                color = if (onHero) ArecaTheme.colors.fgOnContrast else ArecaTheme.colors.fgSubtle,
            ),
        )
        Text(text = text, variant = TextVariant.BodySmall, color = color)
    }
}
