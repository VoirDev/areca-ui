package dev.voir.areca.playground.flow.gallery

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import dev.voir.areca.flow.gallery.part.GalleryAuthor
import dev.voir.areca.flow.gallery.part.GalleryCaption
import dev.voir.areca.flow.gallery.part.GalleryPrimaryAction
import dev.voir.areca.foundation.adaptive.AdaptiveDefaults
import dev.voir.areca.foundation.topbar.CloseAction
import dev.voir.areca.foundation.topbar.TopBar
import dev.voir.areca.foundation.topbar.TopBarTitleAlignment
import dev.voir.areca.media.image.MediaFit
import dev.voir.areca.media.image.MediaFrame
import dev.voir.areca.media.image.MediaPlaceholder
import dev.voir.areca.media.thumbnail.ThumbnailStrip
import dev.voir.areca.media.thumbnail.ThumbnailStripStyle
import dev.voir.areca.media.viewer.MediaTransitionState
import dev.voir.areca.media.viewer.MediaViewer
import dev.voir.areca.media.viewer.MediaViewerImage
import dev.voir.areca.media.viewer.MediaViewerStyle
import dev.voir.areca.playground.media.SamplePicture
import dev.voir.areca.theme.ArecaTheme

/**
 * The gallery viewer the screen pages open, as the product page recipe draws it: a bar with the
 * position, a caption or an author line, thumbnails when there are several pictures, and an action,
 * the foot kept to a readable width on a wide window.
 *
 * @param pictures The pictures.
 * @param start The picture it opens on.
 * @param onDismiss Called once it has closed.
 * @param transition The transition the pictures' frames share, or `null` to fade.
 * @param onIndexChange Called with the picture on screen, so the screen beneath can follow.
 * @param author Whose photograph each picture is, or `null` for the brand's.
 * @param action The primary action's words, or `null` for none.
 * @param actionIcon The primary action's glyph, or `null`.
 * @param light Whether the pictures stand on white.
 */
@Composable
internal fun SampleGalleryViewer(
    pictures: List<SamplePicture>,
    start: Int,
    onDismiss: () -> Unit,
    transition: MediaTransitionState? = null,
    onIndexChange: ((Int) -> Unit)? = null,
    author: String? = null,
    action: String? = null,
    actionIcon: ImageVector? = null,
    light: Boolean = false,
) {
    val style = if (light) MediaViewerStyle.light() else MediaViewerStyle.dark()
    MediaViewer(
        itemCount = pictures.size,
        onDismiss = onDismiss,
        initialIndex = start,
        transition = transition,
        key = { index -> pictures[index].id },
        onIndexChange = onIndexChange,
        style = style,
        topBar = { viewer ->
            TopBar(
                title = if (viewer.itemCount > 1) "${viewer.currentIndex + 1} of ${viewer.itemCount}" else "",
                style = style.topBar(),
                titleAlignment = TopBarTitleAlignment.Center,
                leading = { CloseAction(onClick = viewer::dismiss) },
            )
        },
        footer = { viewer ->
            // Under the picture at a readable width, not stretched across a desktop window.
            Column(
                Modifier.widthIn(max = AdaptiveDefaults.ReadableWidth).align(Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg),
            ) {
                val picture = pictures[viewer.currentIndex]
                if (author != null) {
                    GalleryAuthor(name = "Photo by $author", letters = author.take(1), detail = "12 October 2026", caption = picture.title)
                } else {
                    GalleryCaption(title = picture.title, kicker = "From the brand")
                }
                if (viewer.itemCount > 1) {
                    ThumbnailStrip(
                        count = viewer.itemCount,
                        selectedIndex = viewer.currentIndex,
                        onSelect = { viewer.goTo(it) },
                        contentPadding = PaddingValues(horizontal = ArecaTheme.spacing.horizontalContentInsets),
                        label = { index, total -> "${pictures[index].title}, ${index + 1} of $total" },
                        style = if (light) ThumbnailStripStyle.onLight() else ThumbnailStripStyle.onDark(),
                    ) { index -> MediaFrame(model = pictures[index], contentDescription = null, fit = MediaFit.Fill) }
                }
                if (action != null) GalleryPrimaryAction(text = action, onClick = {}, icon = actionIcon)
            }
        },
    ) { index ->
        MediaViewerImage(
            model = pictures[index],
            contentDescription = pictures[index].title,
            placeholder = MediaPlaceholder.Blurred(pictures[index]),
        )
    }
}
