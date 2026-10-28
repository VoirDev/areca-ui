package dev.voir.areca.media.image

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import dev.voir.areca.indication.TouchIndication
import dev.voir.areca.media.viewer.MediaTransitionState
import dev.voir.areca.media.viewer.hideTransitionSource
import dev.voir.areca.media.zoom.ZoomBehavior
import dev.voir.areca.media.zoom.ZoomLabels
import dev.voir.areca.media.zoom.rememberZoomState
import dev.voir.areca.modifier.clickCursor

/**
 * A picture's place on the page: its shape, its fill, its faces while it loads or after it failed,
 * a scrim and labels over it, and what pressing it does.
 *
 * This overload draws a picture through [LocalMediaImageLoader][dev.voir.areca.media.LocalMediaImageLoader],
 * asked for at the size the frame is drawn — never larger. While it loads the frame shimmers at its
 * own [aspectRatio], so nothing moves when it arrives; a slow load adds a spinner, and a failed one
 * draws a quiet glyph with [failedText] and a [retryLabel] button that loads it again. A
 * [MediaFit.Whole] picture stands on the fill, [MediaFrameSize.inset] clear of the corners.
 *
 * [overlay] is drawn above the picture — a foundation `Badge` in a corner, a heart, a page indicator —
 * and stays put while the picture zooms. With [zoom] set to [ZoomBehavior.Peek] a pinch lifts the
 * picture over the screen and lets it fall back on release.
 *
 * Pressable when [onClick] is given: name what pressing does in [onClickLabel] — "Open picture".
 * Opening a [MediaViewer][dev.voir.areca.media.viewer.MediaViewer] from it, pass the viewer's
 * [transition] and this picture's [transitionKey], and the picture grows out of the frame.
 *
 * @param model The application's picture model, or `null` for a picture that cannot be had, which
 *   draws the failed face without a retry.
 * @param contentDescription What the picture shows, or `null` for one that only decorates.
 * @param modifier Modifier applied to the frame.
 * @param aspectRatio Width over height the frame keeps, or `null` to take the size it is given.
 * @param fit How the picture fills the frame.
 * @param placeholder What stands in for the picture while it loads.
 * @param zoom What pinching or hovering the picture does: [ZoomBehavior.None], [ZoomBehavior.Peek]
 *   or [ZoomBehavior.Hover] in a frame. [ZoomBehavior.Sticky] belongs to a viewer and is taken as none.
 * @param onClick Called when the frame is pressed, or `null` for one that only shows.
 * @param onClickLabel What pressing does, read out.
 * @param scrim Gradient over the picture, under [overlay], drawn once it is ready.
 * @param failedText The failed face's line, or `null` for the glyph alone.
 * @param retryLabel The retry button's caption.
 * @param zoomLabels Names of the zoom actions offered to a screen reader.
 * @param transition The transition of the viewer this frame opens, or `null`.
 * @param transitionKey This picture's key in that viewer.
 * @param style Colors and shape.
 * @param size Metrics.
 * @param pictureOverlay Laid on the picture at points of it — [Hotspot]s — or `null`. Unlike
 *   [overlay], it moves with the picture.
 * @param overlay Drawn over the picture: labels, controls, indicators.
 */
@Composable
public fun MediaFrame(
    model: Any?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    aspectRatio: Float? = null,
    fit: MediaFit = MediaFit.Fill,
    placeholder: MediaPlaceholder = MediaPlaceholder.Skeleton,
    zoom: ZoomBehavior = ZoomBehavior.None,
    onClick: (() -> Unit)? = null,
    onClickLabel: String? = null,
    scrim: MediaScrim = MediaScrim.None,
    failedText: String? = DEFAULT_FAILED_TEXT,
    retryLabel: String = DEFAULT_RETRY_LABEL,
    zoomLabels: ZoomLabels = ZoomLabels(),
    transition: MediaTransitionState? = null,
    transitionKey: Any? = null,
    style: MediaFrameStyle = LocalMediaFrameStyle.current ?: MediaFrameStyle.Default,
    size: MediaFrameSize = LocalMediaFrameSize.current ?: MediaFrameSize.Default,
    pictureOverlay: (@Composable MediaPictureScope.() -> Unit)? = null,
    overlay: @Composable BoxScope.() -> Unit = {},
) {
    val source = if (transition != null && transitionKey != null) transition to transitionKey else null
    ModelMedia(
        model = model,
        contentDescription = contentDescription,
        modifier = modifier
            .then(if (source != null) Modifier.hideTransitionSource(source.first, source.second) else Modifier)
            .frame(aspectRatio, style, onClick, onClickLabel),
        fit = fit,
        placeholder = placeholder,
        zoom = if (zoom == ZoomBehavior.Sticky) ZoomBehavior.None else zoom,
        zoomState = rememberZoomState(),
        onTap = null,
        onEdgeSwipe = null,
        scrim = scrim,
        failedText = failedText,
        retryLabel = retryLabel,
        zoomLabels = zoomLabels,
        style = style,
        size = size,
        overlay = overlay,
        transitionSource = source,
        pictureOverlay = pictureOverlay,
        failure = null,
    )
}

/**
 * A frame around content the application draws itself — a picture from its own image library, a
 * map, a rendered page — with the same shape, fill, faces, scrim, overlay and press as the model
 * overload.
 *
 * The frame cannot tell what [content] is doing, so say it in [state]: the frame draws its loading
 * face over the content until it is [MediaState.Ready], and its failed face, with [onRetry], when it
 * is [MediaState.Failed]. The content is not inset; pad it by [MediaFrameSize.inset] for a whole
 * picture.
 *
 * @param modifier Modifier applied to the frame.
 * @param aspectRatio Width over height the frame keeps, or `null` to take the size it is given.
 * @param state What the content is doing.
 * @param onRetry Called by the failed face to try again, or `null` for no retry.
 * @param onClick Called when the frame is pressed, or `null` for one that only shows.
 * @param onClickLabel What pressing does, read out.
 * @param scrim Gradient over the content, under [overlay], drawn once it is ready.
 * @param failedText The failed face's line, or `null` for the glyph alone.
 * @param retryLabel The retry button's caption.
 * @param style Colors and shape.
 * @param size Metrics.
 * @param overlay Drawn over the content: labels, controls, indicators.
 * @param content The picture, or whatever stands in the frame.
 */
@Composable
public fun MediaFrame(
    modifier: Modifier = Modifier,
    aspectRatio: Float? = null,
    state: MediaState = MediaState.Ready,
    onRetry: (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    onClickLabel: String? = null,
    scrim: MediaScrim = MediaScrim.None,
    failedText: String? = DEFAULT_FAILED_TEXT,
    retryLabel: String = DEFAULT_RETRY_LABEL,
    style: MediaFrameStyle = LocalMediaFrameStyle.current ?: MediaFrameStyle.Default,
    size: MediaFrameSize = LocalMediaFrameSize.current ?: MediaFrameSize.Default,
    overlay: @Composable BoxScope.() -> Unit = {},
    content: @Composable BoxScope.() -> Unit,
) {
    Box(modifier = modifier.frame(aspectRatio, style, onClick, onClickLabel)) {
        Box(Modifier.fillMaxSize(), content = content)
        MediaFaces(
            state = state,
            skeleton = true,
            onRetry = onRetry,
            failedText = failedText,
            retryLabel = retryLabel,
            style = style,
            size = size,
        )
        if (state == MediaState.Ready) ScrimLayer(scrim, style.scrim)
        overlay()
    }
}

/**
 * A frame's own box: its ratio, its clip and fill, and its press.
 *
 * @param aspectRatio Width over height to keep, or `null`.
 * @param style Colors and shape.
 * @param onClick Called when pressed, or `null`.
 * @param onClickLabel What pressing does, read out.
 * @return The modifier.
 */
@Composable
private fun Modifier.frame(
    aspectRatio: Float?,
    style: MediaFrameStyle,
    onClick: (() -> Unit)?,
    onClickLabel: String?,
): Modifier = this
    .then(if (aspectRatio != null) Modifier.aspectRatio(aspectRatio) else Modifier)
    .clip(style.shape)
    .background(style.bg)
    .then(
        if (onClick == null) {
            Modifier
        } else {
            Modifier
                .clickCursor()
                .clickable(
                    interactionSource = null,
                    indication = TouchIndication(shape = style.shape),
                    onClickLabel = onClickLabel,
                    role = Role.Button,
                    onClick = onClick,
                )
        },
    )
