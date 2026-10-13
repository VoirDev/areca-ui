package dev.voir.areca.flow.profile.part

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import dev.voir.areca.flow.profile.ProfileSummary
import dev.voir.areca.flow.profile.icon.BundledIcons
import dev.voir.areca.flow.profile.icon.Check
import dev.voir.areca.foundation.avatar.AvatarSize
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.foundation.settings.SettingsTheme
import dev.voir.areca.indication.FadeIndication
import dev.voir.areca.modifier.clickCursor
import dev.voir.areca.theme.ArecaTheme

/**
 * The surface every profile card is drawn on: a settings section's card.
 *
 * The same fill and corner as `Section`, read from `SettingsTheme.styles.section`, so a card above
 * a settings list reads as the first block of it rather than as a different kind of surface. That is
 * the enclosing `Settings` list's style when there is one — so an application that restyles its
 * sections restyles its cards by composing them in the same list — and the theme default otherwise.
 * A card with [onClick] answers hover, press and focus with the press tint every row uses, and shows
 * the hand cursor on desktop.
 *
 * The fill is drawn in the card's shape rather than clipped to it, so the focus ring the press tint
 * draws outside the card is not cut off; only the content is clipped, so a cover band follows the
 * card's corners.
 *
 * @param modifier Modifier applied to the card.
 * @param onClick Called when the card is pressed, or `null` for a card that only states.
 * @param onClickLabel What pressing does, for assistive technology.
 * @param content Content of the card. Drawn edge to edge; the caller pads it.
 */
@Composable
internal fun ProfileCardSurface(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    onClickLabel: String? = null,
    content: @Composable BoxScope.() -> Unit,
) {
    val section = SettingsTheme.styles.section
    val click = onClick

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(section.bg, section.shape)
            .then(
                if (click != null) {
                    Modifier
                        .clickCursor()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = FadeIndication.tint(section.shape),
                            onClickLabel = onClickLabel,
                            role = Role.Button,
                            onClick = click,
                        )
                } else {
                    Modifier
                },
            ),
    ) {
        Box(modifier = Modifier.fillMaxWidth().clip(section.shape), content = content)
    }
}

/**
 * The account's avatar with the verified tick on its corner when [ProfileSummary.isVerified].
 *
 * Drawn only when true: a badge that is always present says nothing. The tick's disc is outlined in
 * the section fill the cards are drawn on, so it reads as cut out of the avatar.
 *
 * @param summary Who is signed in.
 * @param size Avatar metrics.
 * @param badgeDiameter Diameter of the tick's disc.
 * @param verifiedDescription What the tick means, for assistive technology.
 * @param modifier Modifier applied to the avatar and its badge together.
 * @param ring Ring drawn around the avatar — for an avatar laid over a cover, in the colour of
 *   whatever it overlaps — or `null` for none.
 */
@Composable
internal fun BadgedAvatar(
    summary: ProfileSummary,
    size: AvatarSize,
    badgeDiameter: Dp,
    verifiedDescription: String,
    modifier: Modifier = Modifier,
    ring: BorderStroke? = null,
) {
    val cutout = SettingsTheme.styles.section.bg

    Box(modifier = modifier) {
        ProfileAvatar(
            name = summary.name,
            image = summary.avatar,
            size = size,
            modifier = if (ring != null) Modifier.border(ring, ArecaTheme.shapes.full) else Modifier,
        )

        if (summary.isVerified) {
            val overhang = ArecaTheme.spacing.xs2

            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = overhang, y = overhang)
                    .size(badgeDiameter)
                    .background(ArecaTheme.colors.bgContrast, ArecaTheme.shapes.full)
                    .border(width = ArecaTheme.spacing.xs2, color = cutout, shape = ArecaTheme.shapes.full),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = BundledIcons.Check,
                    contentDescription = verifiedDescription,
                    tint = ArecaTheme.colors.fgOnContrast,
                    size = badgeDiameter / 2,
                )
            }
        }
    }
}

/**
 * The line under a name that says who the account is to other people: the handle, else the address.
 *
 * @receiver The summary to read it from.
 * @return The line, or `null` when the account has neither.
 */
internal val ProfileSummary.identityLine: String?
    get() = handle ?: email
