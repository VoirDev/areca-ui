package dev.voir.areca.flow.auth.part

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import dev.voir.areca.flow.auth.icon.BundledIcons
import dev.voir.areca.flow.auth.icon.Check
import dev.voir.areca.foundation.AnnounceOnChange
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.foundation.icon.IconSize
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextLinkPrompt
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.theme.ArecaTheme

/**
 * The way to ask for another code — "Didn't get it? Send a new code" — and what happened when
 * somebody did.
 *
 * Three moments, driven by which words are passed:
 *
 * - **Nothing sent yet**, [sent] and [cooldown] both `null`: the prompt and the link.
 * - **Just sent**, [sent] given: a tick and the confirmation, announced through the platform's
 *   announcement API (see [AnnounceOnChange]), so a person who pressed the link hears that it worked
 *   without looking for the change.
 * - **Waiting**, [cooldown] given: the link is replaced by how long until another code can be asked
 *   for, on a line of its own under the confirmation when both are given. Services limit
 *   resends, and a link that fails when pressed teaches people to stop trusting it; a link that is
 *   not there until it will work does not.
 *
 * The countdown is worded by the application — "You can ask for another in 0:59" — and updated as
 * often as it likes. The part only draws the sentence it is given, and never announces it: only
 * [sent] is spoken unprompted, so a countdown ticking every second is not read out every second. A
 * screen reader that reaches the line reads the countdown as it stands.
 *
 * @param onResend Called when the link is pressed.
 * @param modifier Modifier applied to the block.
 * @param actionText The pressable words.
 * @param prompt Words before the link — "Didn't get it?" — or `null` for the link alone.
 * @param sent Confirmation that a code went out — "New code sent." — or `null` when none was just
 *   sent.
 * @param cooldown How long until another can be asked for, already worded, or `null` when one can be
 *   asked for now. While set, the link is not drawn.
 * @param textAlign Alignment of the lines within the width they are given.
 * @param enabled Whether the link can be pressed. Pass `false` while a code is being verified.
 */
@Composable
public fun AuthResendCode(
    onResend: () -> Unit,
    modifier: Modifier = Modifier,
    actionText: String = "Send a new code",
    prompt: String? = null,
    sent: String? = null,
    cooldown: String? = null,
    textAlign: TextAlign = TextAlign.Center,
    enabled: Boolean = true,
) {
    val onHero = LocalAuthOnHero.current
    val horizontal = textAlign.toHorizontalAlignment()

    AnnounceOnChange(sent)

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = horizontal,
        verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xs),
    ) {
        if (sent != null) {
            Row(
                modifier = Modifier.semantics(mergeDescendants = true) {},
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = BundledIcons.Check,
                    contentDescription = null,
                    tint = if (onHero) ArecaTheme.colors.fgOnContrast else ArecaTheme.colors.fgSuccessText,
                    size = IconSize.Small,
                )
                Spacer(Modifier.width(ArecaTheme.spacing.xs))
                Text(
                    text = sent,
                    variant = TextVariant.BodySmall,
                    color = AuthOnHero.statusColor(onHero),
                    textAlign = textAlign,
                )
            }
        }

        when {
            cooldown != null -> Text(
                text = cooldown,
                variant = TextVariant.BodySmall,
                color = AuthOnHero.statusColor(onHero),
                textAlign = textAlign,
            )

            // Two words side by side rather than one joined string, so no separator is assumed.
            !enabled && prompt != null -> FlowRow(
                modifier = Modifier.semantics(mergeDescendants = true) {},
                horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xs, horizontal),
            ) {
                Text(text = prompt, variant = TextVariant.BodySmall, color = TextColor.Disabled)
                Text(text = actionText, variant = TextVariant.BodySmall, color = TextColor.Disabled)
            }

            prompt != null -> TextLinkPrompt(
                prompt = prompt,
                linkText = actionText,
                onLinkClick = onResend,
                color = AuthOnHero.promptColor(onHero),
                linkColor = AuthOnHero.linkColor(onHero),
                textAlign = textAlign,
            )

            else -> AuthTextLink(text = actionText, onClick = onResend, enabled = enabled)
        }
    }
}
