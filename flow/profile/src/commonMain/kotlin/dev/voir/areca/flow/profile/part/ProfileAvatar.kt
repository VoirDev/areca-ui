package dev.voir.areca.flow.profile.part

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import dev.voir.areca.flow.profile.AvatarImageLoader
import dev.voir.areca.flow.profile.AvatarImageState
import dev.voir.areca.flow.profile.LocalAvatarImageLoader
import dev.voir.areca.foundation.avatar.Avatar
import dev.voir.areca.foundation.avatar.AvatarSize
import dev.voir.areca.foundation.avatar.AvatarStyle
import dev.voir.areca.foundation.feedback.SpinnerSize
import dev.voir.areca.theme.ArecaTheme

/**
 * The account's picture, or the initials that stand in for it.
 *
 * What the picture is doing decides the appearance: the avatar's spinner over the initials while
 * [AvatarImageState.Loading], the picture once [AvatarImageState.Ready], and the initials alone for
 * [AvatarImageState.Failed] or no picture at all. By default [state] is what the [AvatarImageLoader]
 * in scope reports for [image], so the module draws the picture without knowing how it was fetched;
 * an application that already holds decoded pictures, or a preview, passes [state] itself and leaves
 * [image] out.
 *
 * @param name Name the initials are taken from.
 * @param modifier Modifier applied to the avatar.
 * @param image Picture model for the loader in scope, or `null` when the account has no picture.
 * @param state What the picture is doing, or `null` when the account has no picture. Defaults to the
 *   loader's answer for [image].
 * @param size Container, text and spinner metrics.
 * @param busy Whether to draw the spinner regardless of [state] — while a new picture uploads, for
 *   instance.
 */
@Composable
public fun ProfileAvatar(
    name: String,
    modifier: Modifier = Modifier,
    image: Any? = null,
    state: AvatarImageState? = image?.let { LocalAvatarImageLoader.current.load(it) },
    size: AvatarSize = AvatarSize.ExtraLarge,
    busy: Boolean = false,
) {
    Avatar(
        modifier = modifier,
        size = size,
        style = AvatarStyle.default(
            // A step back from the grouped-region grey, so the circle still reads as one when the
            // avatar sits on a section or a card rather than on the canvas.
            bg = ArecaTheme.colors.bgMuted,
            content = ArecaTheme.colors.fgBase,
        ),
        letters = name,
        loading = busy || state == AvatarImageState.Loading,
        content = (state as? AvatarImageState.Ready)?.let { ready ->
            {
                Image(
                    painter = ready.painter,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
            }
        },
    )
}

/**
 * The 96-point avatar a profile screen leads with.
 *
 * Areca's largest preset is 64 points, which is a marker beside a row rather than the subject of a
 * screen. This is built from the theme's own steps rather than bare numbers: the top of the spacing
 * scale for the container, the heading role for the initials, and a spinner one step down.
 *
 * @return Metrics for a hero avatar.
 */
@Composable
internal fun heroAvatarSize(): AvatarSize = AvatarSize(
    container = ArecaTheme.spacing.xl9,
    icon = ArecaTheme.spacing.xl4,
    text = ArecaTheme.typography.headingLarge,
    spinner = SpinnerSize(ArecaTheme.spacing.xl3),
)
