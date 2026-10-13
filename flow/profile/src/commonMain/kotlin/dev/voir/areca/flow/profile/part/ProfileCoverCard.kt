package dev.voir.areca.flow.profile.part

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import dev.voir.areca.flow.profile.ProfileSummary
import dev.voir.areca.foundation.avatar.AvatarSize
import dev.voir.areca.foundation.feedback.SpinnerSize
import dev.voir.areca.foundation.settings.SettingsTheme
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.theme.ArecaTheme

/**
 * Who the account is, as a card with a cover: a band of artwork across the top, the picture
 * overlapping its lower edge, and the name, handle, bio and caption under it.
 *
 * For a profile that has more to say than a name — a bio, a status, a place — and a product that
 * lets people set a cover. [actions] sit at the end of the picture's row, just under the band, which
 * is where "Edit profile" belongs on a profile of one's own.
 *
 * The picture carries a ring in the card's fill, so it reads as cut out of the cover rather than laid
 * on top of it, and the verified tick when [ProfileSummary.isVerified].
 *
 * @param summary Who the account is.
 * @param modifier Modifier applied to the card. Pass the page's gutter here.
 * @param bio A few words the person wrote about themselves, or `null` for none. Wraps; nothing is
 *   cut.
 * @param verifiedDescription What the verified tick means, for assistive technology. The tick is a
 *   glyph with no words beside it, so it is always announced.
 * @param cover Artwork for the band — an image cropped to fill, a gradient — or `null` for the
 *   default, a quiet neutral band.
 * @param actions Controls at the end of the picture's row, or `null` for none. A `Button` with
 *   `ButtonSize.Small` fits.
 */
@Composable
public fun ProfileCoverCard(
    summary: ProfileSummary,
    modifier: Modifier = Modifier,
    bio: String? = null,
    verifiedDescription: String = "Verified",
    cover: (@Composable BoxScope.() -> Unit)? = null,
    actions: (@Composable RowScope.() -> Unit)? = null,
) {
    val cardFill = SettingsTheme.styles.section.bg
    val avatarSize = coverAvatarSize()
    val overlap = avatarSize.container / 2

    ProfileCardSurface(modifier = modifier) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    // The top of the spacing scale and a gutter: well deeper than the half of the
                    // picture laid over it, so the artwork still shows above the picture.
                    .height(ArecaTheme.spacing.xl9 + ArecaTheme.spacing.xl)
                    .background(ArecaTheme.colors.bgMuted),
            ) {
                if (cover != null) {
                    Box(modifier = Modifier.fillMaxSize(), content = cover)
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    // Lift the row by half the avatar, and give that height back to what follows,
                    // so the picture straddles the band's edge and the text starts under it.
                    .layout { measurable, constraints ->
                        val placeable = measurable.measure(constraints)
                        val lift = overlap.roundToPx()
                        layout(placeable.width, (placeable.height - lift).coerceAtLeast(0)) {
                            placeable.place(0, -lift)
                        }
                    }
                    .padding(horizontal = ArecaTheme.spacing.xl),
                verticalAlignment = Alignment.Top,
            ) {
                BadgedAvatar(
                    summary = summary,
                    size = avatarSize,
                    badgeDiameter = ArecaTheme.spacing.xl2,
                    ring = BorderStroke(ArecaTheme.spacing.xs, cardFill),
                    verifiedDescription = verifiedDescription,
                )

                if (actions != null) {
                    Spacer(Modifier.weight(1f))

                    Row(
                        // Below the band: the row was lifted by the overlap.
                        modifier = Modifier.padding(top = overlap + ArecaTheme.spacing.md),
                        horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
                        verticalAlignment = Alignment.CenterVertically,
                        content = actions,
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = ArecaTheme.spacing.xl,
                        end = ArecaTheme.spacing.xl,
                        top = ArecaTheme.spacing.md,
                        bottom = ArecaTheme.spacing.xl,
                    ),
            ) {
                Text(
                    text = summary.name,
                    modifier = Modifier.semantics { heading() },
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = ArecaTheme.typography.headingExtraSmall,
                    color = ArecaTheme.colors.fgBase,
                )

                summary.identityLine?.let { line ->
                    Spacer(Modifier.height(ArecaTheme.spacing.xs2))

                    Text(
                        text = line,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = ArecaTheme.typography.bodyMedium,
                        color = ArecaTheme.colors.fgSubtle,
                    )
                }

                if (bio != null) {
                    Spacer(Modifier.height(ArecaTheme.spacing.md))

                    Text(
                        text = bio,
                        style = ArecaTheme.typography.bodyMedium,
                        color = ArecaTheme.colors.fgBase,
                    )
                }

                summary.caption?.let { caption ->
                    Spacer(Modifier.height(ArecaTheme.spacing.md))

                    Text(
                        text = caption,
                        style = ArecaTheme.typography.bodySmall,
                        color = ArecaTheme.colors.fgMuted,
                    )
                }
            }
        }
    }
}

/**
 * The 80-point avatar a cover card overlaps its band with — a step under the header card's, since
 * the cover rather than the picture leads this card.
 *
 * @return Metrics for a cover avatar.
 */
@Composable
private fun coverAvatarSize(): AvatarSize = AvatarSize(
    container = ArecaTheme.spacing.xl8,
    icon = ArecaTheme.spacing.xl4,
    text = ArecaTheme.typography.headingMedium,
    spinner = SpinnerSize(ArecaTheme.spacing.xl2),
)
