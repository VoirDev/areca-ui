package dev.voir.areca.flow.gallery.grid

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import dev.voir.areca.foundation.badge.Badge
import dev.voir.areca.foundation.badge.BadgeSize
import dev.voir.areca.foundation.badge.BadgeStyle
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.media.image.MediaFit
import dev.voir.areca.media.image.MediaFrame
import dev.voir.areca.media.image.MediaFrameSize
import dev.voir.areca.media.image.MediaPlaceholder
import dev.voir.areca.media.viewer.MediaTransitionState
import dev.voir.areca.theme.ArecaTheme

/**
 * A group's heading on a gallery's grid page — "From the house", "Readers' photos" — with how many
 * pictures it holds at its end.
 *
 * @param title The group's name. Read as a heading.
 * @param modifier Modifier applied to the heading.
 * @param count How many pictures it holds, worded — "12" — or `null` for none.
 */
@Composable
public fun GalleryGroupHeader(
    title: String,
    modifier: Modifier = Modifier,
    count: String? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = ArecaTheme.spacing.horizontalContentInsets,
                end = ArecaTheme.spacing.horizontalContentInsets,
                top = ArecaTheme.spacing.xl2,
                bottom = ArecaTheme.spacing.md,
            ),
        verticalAlignment = Alignment.Bottom,
    ) {
        Text(
            text = title,
            modifier = Modifier.weight(1f).semantics { heading() },
            style = ArecaTheme.typography.labelLarge,
            color = ArecaTheme.colors.fgBase,
        )
        if (count != null) Text(count, style = ArecaTheme.typography.bodySmall, color = ArecaTheme.colors.fgMuted)
    }
}

/**
 * One square of a gallery's grid page: the picture whole on the fill, a reader's photo labelled with
 * whose it is, pressed to open the viewer at it.
 *
 * Give it the viewer's [transition] and the picture's [transitionKey] so the picture grows out of
 * the tile.
 *
 * @param model The picture's model.
 * @param contentDescription What the picture shows.
 * @param onClick Called when pressed — opens the viewer at this picture.
 * @param modifier Modifier applied to the tile; give it its share of the row.
 * @param label Whose photo it is, or another short word, in the tile's corner, or `null`.
 * @param fit How the picture fills the tile: whole, as products stand, by default.
 * @param onClickLabel What pressing does, read out.
 * @param transition The viewer's transition, or `null`.
 * @param transitionKey This picture's key in the viewer.
 */
@Composable
public fun GalleryTile(
    model: Any?,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    fit: MediaFit = MediaFit.Whole,
    onClickLabel: String = "Open picture",
    transition: MediaTransitionState? = null,
    transitionKey: Any? = null,
) {
    MediaFrame(
        model = model,
        contentDescription = contentDescription,
        modifier = modifier,
        aspectRatio = 1f,
        fit = fit,
        placeholder = MediaPlaceholder.Skeleton,
        onClick = onClick,
        onClickLabel = onClickLabel,
        transition = transition,
        transitionKey = transitionKey,
        size = MediaFrameSize.default(inset = ArecaTheme.spacing.xs),
    ) {
        if (label != null) {
            Badge(
                modifier = Modifier.align(Alignment.BottomStart).padding(ArecaTheme.spacing.sm),
                style = BadgeStyle(bg = ArecaTheme.colors.bgSurface, content = ArecaTheme.colors.fgBase, border = Color.Transparent),
                size = BadgeSize.Small,
            ) { Text(label) }
        }
    }
}
