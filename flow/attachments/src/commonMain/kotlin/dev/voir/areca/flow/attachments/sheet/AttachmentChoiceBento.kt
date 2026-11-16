package dev.voir.areca.flow.attachments.sheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.text.style.TextOverflow
import dev.voir.areca.foundation.icon.FeaturedIcon
import dev.voir.areca.foundation.icon.FeaturedIconSize
import dev.voir.areca.foundation.icon.FeaturedIconStyle
import dev.voir.areca.foundation.layout.Card
import dev.voir.areca.foundation.layout.CardSize
import dev.voir.areca.foundation.layout.CardStyle
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.indication.FadeIndication
import dev.voir.areca.theme.ArecaTheme

/**
 * The choices of a sheet as a small bento of cards: the first full width and larger, the rest side
 * by side under it — see [AttachmentChoiceLayout.Bento] for when.
 *
 * The cards are washed in their choice's hue over the sheet's ground rather than raised in grey, so
 * they read as the sheet's answers rather than as content set into it, and each carries its glyph
 * on a solid disc of that hue. A card inside a sheet steps down to the inner corner. The row of
 * smaller cards is as tall as its tallest, so a label that wraps in one does not leave the others
 * short. Two choices share that row as halves: a lead over a single card would be two stacked bars
 * of different heights.
 *
 * Draws nothing for no choices. The column keeps to the sheet's gutter.
 *
 * @param choices What to offer, in order; the first leads.
 * @param modifier Modifier applied to the column of cards.
 */
@Composable
internal fun AttachmentChoiceBento(
    choices: List<AttachmentChoice>,
    modifier: Modifier = Modifier,
) {
    if (choices.isEmpty()) return

    val lead = choices.first().takeIf { choices.size != 2 }
    val rest = if (lead == null) choices else choices.drop(1)

    Column(
        modifier = modifier.fillMaxWidth().sheetGutter(),
        verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
    ) {
        if (lead != null) {
            BentoCard(choice = lead, large = true, modifier = Modifier.fillMaxWidth())
        }

        if (rest.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
            ) {
                for (choice in rest) {
                    BentoCard(
                        choice = choice,
                        large = false,
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                    )
                }
            }
        }
    }
}

/**
 * One choice as a card: its glyph on a solid disc of its hue, its label and its description, on a
 * wash of the same hue.
 *
 * @param choice The choice offered.
 * @param large Whether it is the lead card, which takes a larger disc and label.
 * @param modifier Modifier applied to the card.
 */
@Composable
private fun BentoCard(
    choice: AttachmentChoice,
    large: Boolean,
    modifier: Modifier = Modifier,
) {
    val hue = choice.resolvedHue()
    // A wash rather than a fill: the solid disc carries the hue, and the label keeps the sheet's ink.
    // The dark sheet takes a heavier share, or the wash disappears into it.
    val wash = lerp(
        ArecaTheme.surface.ground,
        hue,
        if (ArecaTheme.isDark) DARK_WASH_SHARE else LIGHT_WASH_SHARE,
    )
    val shape = ArecaTheme.shapes.lg

    Card(
        modifier = modifier,
        style = CardStyle.default(bg = wash, shape = shape, indication = FadeIndication.tint(shape)),
        size = CardSize.default(),
        onClick = choice.onClick,
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxWidth().padding(padding),
            verticalArrangement = Arrangement.spacedBy(
                if (large) ArecaTheme.spacing.xl else ArecaTheme.spacing.lg,
            ),
        ) {
            FeaturedIcon(
                imageVector = choice.icon,
                contentDescription = null,
                style = FeaturedIconStyle.solidHue(hue),
                size = if (large) FeaturedIconSize.Medium else FeaturedIconSize.Small,
                shape = ArecaTheme.shapes.full,
            )

            Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xs2)) {
                Text(
                    text = choice.label,
                    variant = if (large) TextVariant.LabelLarge else TextVariant.LabelMedium,
                    color = if (choice.isDestructive) TextColor.Danger else TextColor.Base,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )

                if (choice.description != null) {
                    Text(
                        text = choice.description,
                        variant = TextVariant.BodyExtraSmall,
                        color = TextColor.Muted,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}

/**
 * The hue a choice's card is drawn in: its own, else its source's default, else grey — or red for a
 * choice that loses something.
 *
 * @return The resolved hue.
 */
@Composable
private fun AttachmentChoice.resolvedHue(): Color {
    val hues = ArecaTheme.hues
    return hue.takeOrElse {
        defaultHue?.invoke(hues) ?: if (isDestructive) hues.red else hues.gray
    }
}

/** How much of its hue a card mixes into a light sheet: a wash, under the solid disc. */
private const val LIGHT_WASH_SHARE = 0.12f

/** The same in the dark, where a lighter mix disappears into the sheet. */
private const val DARK_WASH_SHARE = 0.22f
