package dev.voir.areca.flow.profile.part

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import dev.voir.areca.flow.profile.ProfileSummary
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.indication.FadeIndication
import dev.voir.areca.modifier.MinimumTouchTarget
import dev.voir.areca.modifier.clickCursor
import dev.voir.areca.theme.ArecaTheme

/**
 * Who the account is, as the card a profile screen opens with: the picture, the name, how other
 * people know them, and what can be done from here.
 *
 * Centred, because the person is the subject of the screen. The picture carries the verified tick
 * when [ProfileSummary.isVerified]. Under it, the handle — or the address when there is no handle —
 * and the caption, then [actions]: a row of [ProfileCardAction] tiles such as "Edit", "Share" and
 * "QR code", sharing the card's width equally.
 *
 * @param summary Who the account is.
 * @param modifier Modifier applied to the card. Pass the page's gutter here.
 * @param verifiedDescription What the verified tick means, for assistive technology. The tick is a
 *   glyph with no words beside it, so it is always announced.
 * @param actions Actions under the identity, or `null` for none. Give each `Modifier.weight(1f)` to
 *   share the width.
 */
@Composable
public fun ProfileHeaderCard(
    summary: ProfileSummary,
    modifier: Modifier = Modifier,
    verifiedDescription: String = "Verified",
    actions: (@Composable RowScope.() -> Unit)? = null,
) {
    ProfileCardSurface(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = ArecaTheme.spacing.xl,
                    end = ArecaTheme.spacing.xl,
                    top = ArecaTheme.spacing.xl3,
                    bottom = if (actions != null) ArecaTheme.spacing.xl else ArecaTheme.spacing.xl3,
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            BadgedAvatar(
                summary = summary,
                size = heroAvatarSize(),
                badgeDiameter = ArecaTheme.spacing.xl3,
                verifiedDescription = verifiedDescription,
            )

            Spacer(Modifier.height(ArecaTheme.spacing.lg))

            Text(
                text = summary.name,
                modifier = Modifier.semantics { heading() },
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = ArecaTheme.typography.headingExtraSmall,
                color = ArecaTheme.colors.fgBase,
                textAlign = TextAlign.Center,
            )

            summary.identityLine?.let { line ->
                Spacer(Modifier.height(ArecaTheme.spacing.xs))

                Text(
                    text = line,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = ArecaTheme.typography.bodyMedium,
                    color = ArecaTheme.colors.fgSubtle,
                    textAlign = TextAlign.Center,
                )
            }

            summary.caption?.let { caption ->
                Spacer(Modifier.height(ArecaTheme.spacing.xs2))

                Text(
                    text = caption,
                    style = ArecaTheme.typography.bodySmall,
                    color = ArecaTheme.colors.fgMuted,
                    textAlign = TextAlign.Center,
                )
            }

            if (actions != null) {
                Spacer(Modifier.height(ArecaTheme.spacing.xl2))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
                    content = actions,
                )
            }
        }
    }
}

/**
 * One action on a profile card: a glyph over a short label, on a tile.
 *
 * Tiles rather than buttons, because a row of three or four filled buttons is a row of competing
 * primary actions; a row of tiles is a set of equals. The tile is the canvas colour on the card's
 * grouped fill, so it stands out from the card in both themes without taking a colour of its own.
 * The fill is drawn in the tile's shape rather than clipped to it, so the focus ring is not cut off.
 *
 * @param icon Glyph over the label.
 * @param label What the action does, in a word or two — "Edit", "Share".
 * @param onClick Called when the tile is pressed.
 * @param modifier Modifier applied to the tile. Inside a card's actions, `Modifier.weight(1f)`.
 * @param enabled Whether the tile accepts input.
 */
@Composable
public fun ProfileCardAction(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val shape = ArecaTheme.shapes.md
    val content = if (enabled) ArecaTheme.colors.fgBase else ArecaTheme.colors.fgDisabled

    Column(
        modifier = modifier
            .heightIn(min = MinimumTouchTarget)
            .background(ArecaTheme.colors.bgCanvas, shape)
            .clickCursor(enabled)
            .clickable(
                enabled = enabled,
                role = Role.Button,
                indication = FadeIndication.tint(shape),
                interactionSource = null,
                onClick = onClick,
            )
            .padding(horizontal = ArecaTheme.spacing.sm, vertical = ArecaTheme.spacing.md),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xs, Alignment.CenterVertically),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = content,
            size = ArecaTheme.spacing.xl2,
        )

        Text(
            text = label,
            style = ArecaTheme.typography.labelSmall,
            color = content,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
