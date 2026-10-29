package dev.voir.areca.flow.gallery.part

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import dev.voir.areca.foundation.LocalContentColor
import dev.voir.areca.foundation.avatar.Avatar
import dev.voir.areca.foundation.avatar.AvatarSize
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.button.ButtonSize
import dev.voir.areca.foundation.button.ButtonStyle
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.theme.ArecaTheme

/**
 * What a picture shows, under it in a gallery viewer's `footer`: a quiet kind above — "From the
 * brand", "Readers' photos" — its title, and a line about it.
 *
 * Takes the viewer's content colour, the kind and the line quieter. Set inside the screen's gutter.
 *
 * @param title The picture's title.
 * @param modifier Modifier applied to the block.
 * @param kicker The picture's kind, drawn in capitals above, or `null` for none.
 * @param caption A line about it, or `null` for none.
 */
@Composable
public fun GalleryCaption(
    title: String,
    modifier: Modifier = Modifier,
    kicker: String? = null,
    caption: String? = null,
) {
    val content = LocalContentColor.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = ArecaTheme.spacing.horizontalContentInsets)
            .semantics(mergeDescendants = true) {},
        verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xs2),
    ) {
        if (kicker != null) {
            Text(kicker.uppercase(), style = ArecaTheme.typography.caption, color = content.copy(alpha = QUIET_ALPHA))
        }
        Text(title, style = ArecaTheme.typography.labelLarge, color = content, maxLines = 2, overflow = TextOverflow.Ellipsis)
        if (caption != null) {
            Text(caption, style = ArecaTheme.typography.bodySmall, color = content.copy(alpha = LINE_ALPHA))
        }
    }
}

/**
 * Whose photograph a picture is, under it in a gallery viewer's `footer`: their avatar, their name
 * worded by the application — "Photo by Mara K." — a detail such as a date, and a line they wrote.
 *
 * @param name The author's name, worded as it should read.
 * @param modifier Modifier applied to the block.
 * @param letters Initials drawn in the avatar when it has no picture, or `null` for a blank one.
 * @param detail A quieter line beside the name — a date — or `null`.
 * @param caption What they wrote about the picture, or `null`.
 * @param avatar The author's picture in the avatar, or `null` for [letters].
 */
@Composable
public fun GalleryAuthor(
    name: String,
    modifier: Modifier = Modifier,
    letters: String? = null,
    detail: String? = null,
    caption: String? = null,
    avatar: (@Composable () -> Unit)? = null,
) {
    val content = LocalContentColor.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = ArecaTheme.spacing.horizontalContentInsets)
            .semantics(mergeDescendants = true) {},
        verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.sm),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md)) {
            Avatar(size = AvatarSize.Small, letters = letters, content = avatar?.let { { it() } })
            Column {
                Text(name, style = ArecaTheme.typography.labelLarge, color = content)
                if (detail != null) Text(detail, style = ArecaTheme.typography.caption, color = content.copy(alpha = QUIET_ALPHA))
            }
        }
        if (caption != null) Text(caption, style = ArecaTheme.typography.bodySmall, color = content.copy(alpha = LINE_ALPHA))
    }
}

/**
 * The one thing a gallery leads to, across a viewer's foot — "Add to bag · $129", "See the review".
 *
 * A capsule in the viewer's content colour with the ground's colour on it, so it is the loudest
 * thing on the screen whatever the viewer's ground.
 *
 * @param text What pressing does.
 * @param onClick Called when pressed.
 * @param modifier Modifier applied to the button.
 * @param icon A glyph before the words, or `null`.
 * @param enabled Whether it can be pressed — false while the product is sold out.
 */
@Composable
public fun GalleryPrimaryAction(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    enabled: Boolean = true,
) {
    val content = LocalContentColor.current
    val ground = if (content.luminance() > 0.5f) ArecaTheme.baseColors.baseBlack else ArecaTheme.baseColors.baseWhite
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = ArecaTheme.spacing.horizontalContentInsets),
        style = ButtonStyle.solid(shape = ArecaTheme.shapes.full).copy(bg = content, content = ground),
        size = ButtonSize.Large,
        enabled = enabled,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.sm)) {
            if (icon != null) Icon(icon, contentDescription = null)
            Text(text)
        }
    }
}

/** How quiet a kind or a detail is against the content colour. */
private const val QUIET_ALPHA = 0.66f

/** How quiet a caption line is against the content colour. */
private const val LINE_ALPHA = 0.82f
