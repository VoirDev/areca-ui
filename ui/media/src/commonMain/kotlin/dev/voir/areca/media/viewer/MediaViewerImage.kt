package dev.voir.areca.media.viewer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.voir.areca.foundation.LocalContentColor
import dev.voir.areca.media.image.MediaFit
import dev.voir.areca.media.image.MediaFrameStyle
import dev.voir.areca.media.image.MediaImage
import dev.voir.areca.media.image.MediaPictureScope
import dev.voir.areca.media.image.MediaPlaceholder
import dev.voir.areca.media.zoom.ZoomBehavior
import dev.voir.areca.media.zoom.ZoomLabels

/**
 * A [MediaViewer] page's picture: the model drawn whole on the viewer's ground, zoomed with the
 * page's zoom, its taps and edge swipes wired to the viewer.
 *
 * While it loads it shows [placeholder] — a blurred thumbnail reads best, since the thumbnail is
 * usually already loaded — and a spinner once slow; a failed picture shows [failedText] and a retry,
 * and the pages still swipe past it.
 *
 * @param model The application's picture model, or `null` for one that cannot be had.
 * @param contentDescription What the picture shows.
 * @param modifier Modifier applied to the picture's bounds, which fill the page.
 * @param fit How the picture fills the page; whole by default.
 * @param placeholder What stands in while it loads.
 * @param failedText The failed face's line, or `null` for the glyph alone.
 * @param retryLabel The retry button's caption.
 * @param zoomLabels Names of the zoom actions offered to a screen reader.
 * @param pictureOverlay Laid on the picture at points of it, following its zoom, or `null`.
 */
@Composable
public fun MediaViewerPageScope.MediaViewerImage(
    model: Any?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    fit: MediaFit = MediaFit.Whole,
    placeholder: MediaPlaceholder = MediaPlaceholder.None,
    failedText: String? = "This picture didn't load",
    retryLabel: String = "Try again",
    zoomLabels: ZoomLabels = ZoomLabels(),
    pictureOverlay: (@Composable MediaPictureScope.() -> Unit)? = null,
) {
    MediaImage(
        model = model,
        contentDescription = contentDescription,
        modifier = modifier.fillMaxSize(),
        fit = fit,
        placeholder = placeholder,
        zoom = ZoomBehavior.Sticky,
        zoomState = zoomState,
        onTap = ::onTap,
        onEdgeSwipe = ::onEdgeSwipe,
        failedText = failedText,
        retryLabel = retryLabel,
        zoomLabels = zoomLabels,
        style = MediaFrameStyle.bare(content = LocalContentColor.current),
        pictureOverlay = pictureOverlay,
    )
}
