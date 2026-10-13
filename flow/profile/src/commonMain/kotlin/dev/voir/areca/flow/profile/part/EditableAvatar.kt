package dev.voir.areca.flow.profile.part

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.style.TextOverflow
import dev.voir.areca.flow.profile.ProfileSummary
import dev.voir.areca.flow.profile.icon.BundledIcons
import dev.voir.areca.flow.profile.icon.Camera
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.indication.FadeIndication
import dev.voir.areca.indication.TouchIndication
import dev.voir.areca.modifier.clickCursor
import dev.voir.areca.theme.ArecaTheme

/**
 * The avatar on an edit screen: the picture, the mark that says it can be changed, and the handle —
 * or the address — under it.
 *
 * The picture and its badge are one tap target rather than two. The badge alone is under any
 * reasonable touch minimum, so it is the affordance and the avatar behind it is the target — and
 * pressing either lights both, because they share one interaction source. Only the avatar is
 * exposed to assistive technology, as one button labelled [changePhotoLabel].
 *
 * There is no "Change photo" caption. The camera mark already says what the tap does, and a link
 * repeating it underneath reads as a second control stacked under the first.
 *
 * @param summary Who is signed in. The handle, else the address, is drawn under the avatar.
 * @param onChangePhoto Called when the person asks to change the picture.
 * @param modifier Modifier applied to the block.
 * @param busy Whether a change is in flight, in which case the avatar shows its spinner and stops
 *   accepting taps.
 * @param changePhotoLabel What pressing the avatar does, for assistive technology. The avatar is an
 *   icon-only control, so it is always labelled.
 */
@Composable
public fun EditableAvatar(
    summary: ProfileSummary,
    onChangePhoto: () -> Unit,
    modifier: Modifier = Modifier,
    busy: Boolean = false,
    changePhotoLabel: String = "Change photo",
) {
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // No clip on this box: clipping it to a circle would take the corner off the badge that
        // hangs over the avatar's edge.
        Box {
            ProfileAvatar(
                name = summary.name,
                image = summary.avatar,
                modifier = Modifier
                    .clickCursor(enabled = !busy)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = FadeIndication.tint(ArecaTheme.shapes.full),
                        enabled = !busy,
                        onClickLabel = changePhotoLabel,
                        role = Role.Button,
                        onClick = onChangePhoto,
                    ),
                size = heroAvatarSize(),
                busy = busy,
            )

            CameraBadge(
                busy = busy,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    // Announced once, as the avatar. A second button with the same label beside
                    // the first is a screen reader stop that adds nothing. Ahead of `clickable`,
                    // so the click's own semantics are among those cleared.
                    .clearAndSetSemantics {}
                    .clickCursor(enabled = !busy)
                    .clickable(
                        interactionSource = interactionSource,
                        // The one dark disc on the page; a dark radial on it is a smudge.
                        indication = TouchIndication.onColor(ArecaTheme.shapes.full),
                        enabled = !busy,
                        onClick = onChangePhoto,
                    ),
            )
        }

        summary.identityLine?.let { line ->
            Spacer(Modifier.height(ArecaTheme.spacing.lg))

            Text(
                text = line,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = ArecaTheme.typography.bodySmall,
                color = ArecaTheme.colors.fgSubtle,
            )
        }
    }
}

/**
 * The camera mark on the avatar's corner — the whole of what says the picture can be changed.
 *
 * Greys out with the avatar while an upload runs, because a badge still inviting a tap over a
 * spinner is inviting one that will not be taken.
 *
 * @param busy Whether a change is already in flight.
 * @param modifier Modifier applied to the badge, used to align it against the avatar.
 */
@Composable
private fun CameraBadge(
    busy: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(ArecaTheme.spacing.xl4)
            .background(
                color = if (busy) ArecaTheme.colors.bgDisabled else ArecaTheme.colors.bgContrast,
                shape = ArecaTheme.shapes.full,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = BundledIcons.Camera,
            contentDescription = null,
            tint = if (busy) ArecaTheme.colors.fgDisabled else ArecaTheme.colors.fgOnContrast,
            size = ArecaTheme.spacing.xl,
        )
    }
}
