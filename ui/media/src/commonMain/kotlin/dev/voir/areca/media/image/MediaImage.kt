package dev.voir.areca.media.image

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import dev.voir.areca.media.LocalMediaImageLoader
import dev.voir.areca.media.MediaImageState
import dev.voir.areca.media.viewer.MediaTransitionState
import dev.voir.areca.media.viewer.recordTransitionSource
import dev.voir.areca.media.zoom.ZoomBehavior
import dev.voir.areca.media.zoom.ZoomLabels
import dev.voir.areca.media.zoom.ZoomState
import dev.voir.areca.media.zoom.displayEdge
import dev.voir.areca.media.zoom.rememberZoomState
import dev.voir.areca.media.zoom.zoomable
import kotlin.math.roundToInt

/**
 * A picture from the application's model, filling the space it is given, with its loading and
 * failed faces — a viewer's page, or any layout that is not a [MediaFrame].
 *
 * Loads through [LocalMediaImageLoader] at the size it is drawn. While loading it shows
 * [placeholder] and, once slow, a spinner; a failed picture shows a glyph, [failedText] and a
 * [retryLabel] button. With [zoom] set to [ZoomBehavior.Sticky] — the viewer — the ready picture
 * pinches, double-taps and pans within [zoomState]; [ZoomBehavior.Peek] lifts it and lets it fall
 * back. Hold the [zoomState] to reset the zoom as a page leaves.
 *
 * @param model The application's picture model, or `null` for one that cannot be had.
 * @param contentDescription What the picture shows, or `null` for one that only decorates.
 * @param modifier Modifier applied to the picture's bounds.
 * @param fit How the picture fills its bounds.
 * @param placeholder What stands in for it while it loads.
 * @param zoom What pinching does.
 * @param zoomState The zoom, for a caller that reads or resets it.
 * @param onTap Called on a single tap, or `null` — a viewer hides its bars.
 * @param onEdgeSwipe Called with `1` or `-1` when a swipe on the zoomed picture keeps going past its
 *   right or left edge, or `null`.
 * @param failedText The failed face's line, or `null` for the glyph alone.
 * @param retryLabel The retry button's caption.
 * @param zoomLabels Names of the zoom actions offered to a screen reader.
 * @param style Colors; a viewer passes [MediaFrameStyle.bare].
 * @param size Metrics.
 * @param pictureOverlay Laid on the picture at points of it — [Hotspot]s — following its zoom, or
 *   `null`.
 * @param failure Drawn in place of the failed face — a page of your own for a picture that cannot
 *   be drawn — handed the retry, or `null` for the face with [failedText] and [retryLabel].
 */
@Composable
public fun MediaImage(
    model: Any?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    fit: MediaFit = MediaFit.Whole,
    placeholder: MediaPlaceholder = MediaPlaceholder.Skeleton,
    zoom: ZoomBehavior = ZoomBehavior.None,
    zoomState: ZoomState = rememberZoomState(),
    onTap: (() -> Unit)? = null,
    onEdgeSwipe: ((direction: Int) -> Unit)? = null,
    failedText: String? = DEFAULT_FAILED_TEXT,
    retryLabel: String = DEFAULT_RETRY_LABEL,
    zoomLabels: ZoomLabels = ZoomLabels(),
    style: MediaFrameStyle = LocalMediaFrameStyle.current ?: MediaFrameStyle.Default,
    size: MediaFrameSize = LocalMediaFrameSize.current ?: MediaFrameSize.Default,
    pictureOverlay: (@Composable MediaPictureScope.() -> Unit)? = null,
    failure: (@Composable BoxScope.(retry: () -> Unit) -> Unit)? = null,
) {
    ModelMedia(
        model = model,
        contentDescription = contentDescription,
        modifier = modifier.background(style.bg),
        fit = fit,
        placeholder = placeholder,
        zoom = zoom,
        zoomState = zoomState,
        onTap = onTap,
        onEdgeSwipe = onEdgeSwipe,
        scrim = MediaScrim.None,
        failedText = failedText,
        retryLabel = retryLabel,
        zoomLabels = zoomLabels,
        style = style,
        size = size,
        overlay = {},
        transitionSource = null,
        pictureOverlay = pictureOverlay,
        failure = failure,
    )
}

/**
 * Loads [model] at the size it is drawn and draws it with its faces: what [MediaImage] and the model
 * overload of [MediaFrame] share.
 *
 * Retrying recomposes the loader's call under a new key, so work the loader remembered in
 * composition is started again.
 */
@Composable
internal fun ModelMedia(
    model: Any?,
    contentDescription: String?,
    modifier: Modifier,
    fit: MediaFit,
    placeholder: MediaPlaceholder,
    zoom: ZoomBehavior,
    zoomState: ZoomState,
    onTap: (() -> Unit)?,
    onEdgeSwipe: ((Int) -> Unit)?,
    scrim: MediaScrim,
    failedText: String?,
    retryLabel: String,
    zoomLabels: ZoomLabels,
    style: MediaFrameStyle,
    size: MediaFrameSize,
    overlay: @Composable BoxScope.() -> Unit,
    transitionSource: Pair<MediaTransitionState, Any>?,
    pictureOverlay: (@Composable MediaPictureScope.() -> Unit)?,
    failure: (@Composable BoxScope.(retry: () -> Unit) -> Unit)?,
) {
    BoxWithConstraints(modifier = modifier) {
        val edge = displayEdge(constraints.maxWidth, constraints.maxHeight, fallback = FALLBACK_EDGE)
        var attempt by remember(model) { mutableIntStateOf(0) }
        val picture = if (model == null) {
            MediaImageState.Failed
        } else {
            key(model, attempt) { LocalMediaImageLoader.current.load(model = model, maxSize = edge) }
        }
        val blurred = placeholder as? MediaPlaceholder.Blurred

        if (picture is MediaImageState.Loading && blurred != null) {
            BlurredStandIn(model = blurred.model, fit = fit, inset = size.inset)
        }
        if (transitionSource != null) {
            val (transition, sourceKey) = transitionSource
            val whole = fit == MediaFit.Whole && picture is MediaImageState.Ready
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(if (whole) size.inset else 0.dp)
                    .recordTransitionSource(
                        state = transition,
                        key = sourceKey,
                        pictureSize = if (whole) (picture as MediaImageState.Ready).painter.intrinsicSize else Size.Unspecified,
                    ),
            )
        }
        if (picture is MediaImageState.Ready) {
            Picture(
                painter = picture.painter,
                contentDescription = contentDescription,
                fit = fit,
                inset = size.inset,
                zoom = zoom,
                zoomState = zoomState,
                onTap = onTap,
                onEdgeSwipe = onEdgeSwipe,
                zoomLabels = zoomLabels,
                pictureOverlay = pictureOverlay,
            )
        }
        MediaFaces(
            state = picture.toMediaState(),
            skeleton = placeholder == MediaPlaceholder.Skeleton,
            onRetry = if (model != null) ({ attempt++ }) else null,
            failedText = failedText,
            retryLabel = retryLabel,
            style = style,
            size = size,
            failure = failure,
        )
        if (picture is MediaImageState.Ready) ScrimLayer(scrim, style.scrim)
        overlay()
    }
}

/**
 * A ready picture in its bounds, zoomable as [zoom] says.
 *
 * @param painter The picture.
 * @param contentDescription What it shows.
 * @param fit How it fills its bounds.
 * @param inset Space around a whole picture.
 * @param zoom What pinching does.
 * @param zoomState The zoom.
 * @param onTap Called on a single tap, or `null`.
 * @param onEdgeSwipe Called past a zoomed edge, or `null`.
 * @param zoomLabels Names of the zoom actions.
 */
@Composable
private fun Picture(
    painter: Painter,
    contentDescription: String?,
    fit: MediaFit,
    inset: Dp,
    zoom: ZoomBehavior,
    zoomState: ZoomState,
    onTap: (() -> Unit)?,
    onEdgeSwipe: ((Int) -> Unit)?,
    zoomLabels: ZoomLabels,
    pictureOverlay: (@Composable MediaPictureScope.() -> Unit)?,
) {
    val whole = fit == MediaFit.Whole
    val scale = if (whole) ContentScale.Fit else ContentScale.Crop
    // A cropped picture fills its frame, so zoom measures the frame rather than the picture.
    val contentSize = if (whole) painter.intrinsicSize else Size.Unspecified
    var bounds by remember { mutableStateOf(Rect.Zero) }

    Box(Modifier.fillMaxSize().padding(if (whole) inset else 0.dp)) {
        // The picture and what is pinned to it zoom as one, so a marker stays on its point.
        Box(
            Modifier
                .fillMaxSize()
                .then(if (zoom == ZoomBehavior.Peek) Modifier.onGloballyPositioned { bounds = it.boundsInWindow() } else Modifier)
                .zoomable(
                    state = zoomState,
                    contentSize = contentSize,
                    behavior = zoom,
                    onTap = onTap,
                    onEdgeSwipe = onEdgeSwipe,
                    labels = zoomLabels,
                )
                .then(
                    if (zoom == ZoomBehavior.Peek) {
                        Modifier.graphicsLayer { alpha = if (zoomState.isZoomed) 0f else 1f }
                    } else {
                        Modifier
                    },
                ),
        ) {
            Image(
                painter = painter,
                contentDescription = contentDescription,
                contentScale = scale,
                modifier = Modifier.fillMaxSize(),
            )
            if (pictureOverlay != null) {
                PictureOverlay(
                    pictureSize = painter.intrinsicSize,
                    fit = fit,
                    zoomState = zoomState.takeIf { zoom == ZoomBehavior.Sticky || zoom == ZoomBehavior.Hover },
                    content = pictureOverlay,
                )
            }
        }
    }

    if (zoom == ZoomBehavior.Peek && zoomState.isZoomed) {
        PeekLayer(painter = painter, contentScale = scale, bounds = bounds, zoomState = zoomState)
    }
}

/**
 * A peeked picture lifted over the whole window: drawn where it sat, scaled and moved with the
 * fingers, over a ground that darkens as it grows.
 *
 * @param painter The picture.
 * @param contentScale How it filled its frame.
 * @param bounds Where it sat, in the window.
 * @param zoomState The peek's zoom.
 */
@Composable
private fun PeekLayer(painter: Painter, contentScale: ContentScale, bounds: Rect, zoomState: ZoomState) {
    val density = LocalDensity.current
    val window = LocalWindowInfo.current.containerSize

    Popup(
        popupPositionProvider = WindowOrigin,
        properties = PopupProperties(focusable = false, clippingEnabled = false),
    ) {
        Box(Modifier.size(with(density) { DpSize(window.width.toDp(), window.height.toDp()) })) {
            Box(
                Modifier
                    .fillMaxSize()
                    .graphicsLayer { alpha = ((zoomState.scale - 1f) * PEEK_DIM_RATE).coerceIn(0f, PEEK_DIM_MAX) }
                    .background(Color.Black),
            )
            Image(
                painter = painter,
                contentDescription = null,
                contentScale = contentScale,
                modifier = Modifier
                    .offset { IntOffset(bounds.left.roundToInt(), bounds.top.roundToInt()) }
                    .size(with(density) { DpSize(bounds.width.toDp(), bounds.height.toDp()) })
                    .graphicsLayer {
                        scaleX = zoomState.scale
                        scaleY = zoomState.scale
                        translationX = zoomState.offset.x
                        translationY = zoomState.offset.y
                    },
            )
        }
    }
}

/**
 * A thumbnail of the picture, blurred, standing in while the full one loads.
 *
 * @param model The thumbnail's model.
 * @param fit How the picture will fill its bounds.
 * @param inset Space around a whole picture.
 */
@Composable
private fun BlurredStandIn(model: Any, fit: MediaFit, inset: Dp) {
    val thumbnail = LocalMediaImageLoader.current.loadSquare(model = model, side = STAND_IN_SIDE)
    if (thumbnail !is MediaImageState.Ready) return

    val whole = fit == MediaFit.Whole
    Image(
        painter = thumbnail.painter,
        contentDescription = null,
        contentScale = if (whole) ContentScale.Fit else ContentScale.Crop,
        modifier = Modifier
            .fillMaxSize()
            .padding(if (whole) inset else 0.dp)
            .blur(STAND_IN_BLUR),
    )
}

/** @return The face a frame draws for this loading state. */
private fun MediaImageState.toMediaState(): MediaState = when (this) {
    MediaImageState.Loading -> MediaState.Loading
    is MediaImageState.Ready -> MediaState.Ready
    MediaImageState.Failed -> MediaState.Failed
}

/** Places a popup at the window's origin, so it can cover the whole window. */
private object WindowOrigin : PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize,
    ): IntOffset = IntOffset.Zero
}

/** Longest edge asked for when a picture is drawn in unbounded space. */
private const val FALLBACK_EDGE = 2048

/** Side of the thumbnail a blurred stand-in is decoded at: it is blurred, so small is enough. */
private const val STAND_IN_SIDE = 64

/** How soft a blurred stand-in is. */
private val STAND_IN_BLUR = 16.dp

/** How fast the ground under a peek darkens as the picture grows. */
private const val PEEK_DIM_RATE = 0.8f

/** How dark the ground under a peek gets. */
private const val PEEK_DIM_MAX = 0.6f
