package dev.voir.areca.flow.attachments.viewer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculateCentroid
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.geometry.isSpecified
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChanged
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import dev.voir.areca.flow.attachments.AttachmentPdfDocument
import dev.voir.areca.flow.attachments.LocalAttachmentPdfRenderer
import dev.voir.areca.foundation.adaptive.AdaptiveDefaults
import dev.voir.areca.foundation.feedback.Spinner
import dev.voir.areca.foundation.feedback.SpinnerSize
import dev.voir.areca.foundation.feedback.SpinnerStyle
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.media.zoom.ZoomLabels
import dev.voir.areca.media.zoom.rememberZoomState
import dev.voir.areca.media.zoom.zoomControls
import dev.voir.areca.theme.ArecaTheme
import kotlinx.coroutines.launch

/**
 * A PDF, drawn as a column of pages that can be brought closer.
 *
 * **Scrolled down, not paged across.** Sideways is already how the viewer moves between files, so a
 * document's own pages go the other way — the way every reader lays out a PDF. A pill over the
 * bottom says which page is in view.
 *
 * **Zoomed like a picture.** Two fingers pinch the column closer around them and a double tap
 * brings it closer or back; once zoomed, one finger moves around the page, and past the top or the
 * bottom of what is in view it scrolls the column on. A zoomed column draws its pages again at
 * twice the width, so small print sharpens rather than blurs. A screen reader, the keyboard and
 * Ctrl with the mouse wheel zoom it as they do a picture; see [zoomControls].
 *
 * Opened through [LocalAttachmentPdfRenderer] and closed when it leaves the screen. Each page is
 * drawn when it scrolls into view, at the column's width, with a blank sheet of A4's proportions in
 * its place meanwhile so the column does not jump as pages arrive. The column is the page's width
 * less a gutter, up to [AdaptiveDefaults.ReadableWidth] and centred beyond it, so on a wide window a
 * page is read, not scrolled past like a poster. Where the renderer cannot open
 * the file — none is provided, the platform has none, or it is not really a PDF — [fallback] takes
 * the page instead.
 *
 * @param model What the renderer opens.
 * @param style Colours of the viewer.
 * @param pageLabel Words the page in view and the page count ("Page 2 of 5").
 * @param pageDescription Words one page for assistive technology.
 * @param zoomInLabel Name of the action that brings the pages closer.
 * @param zoomOutLabel Name of the action that takes them back a step.
 * @param resetZoomLabel Name of the action that returns them to the column's width.
 * @param modifier Modifier applied to the column.
 * @param fallback What to show when the file cannot be drawn here.
 */
@Composable
internal fun AttachmentPdfPages(
    model: Any,
    style: AttachmentViewerStyle,
    pageLabel: (page: Int, count: Int) -> String,
    pageDescription: (page: Int, count: Int) -> String,
    zoomInLabel: String,
    zoomOutLabel: String,
    resetZoomLabel: String,
    modifier: Modifier = Modifier,
    fallback: @Composable () -> Unit,
) {
    val renderer = LocalAttachmentPdfRenderer.current
    val opened by produceState<PdfOpening>(PdfOpening.Opening, model, renderer) {
        value = renderer.open(model)?.let(PdfOpening::Open) ?: PdfOpening.Failed
    }

    when (val state = opened) {
        PdfOpening.Opening -> Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Spinner(style = SpinnerStyle(color = style.controlContent), size = SpinnerSize.Large)
        }

        PdfOpening.Failed -> fallback()

        is PdfOpening.Open -> {
            DisposableEffect(state.document) {
                onDispose { state.document.close() }
            }

            PdfPages(
                document = state.document,
                style = style,
                pageLabel = pageLabel,
                pageDescription = pageDescription,
                zoomInLabel = zoomInLabel,
                zoomOutLabel = zoomOutLabel,
                resetZoomLabel = resetZoomLabel,
                modifier = modifier,
            )
        }
    }
}

/**
 * The open document's pages.
 *
 * @param document The document.
 * @param style Colours of the viewer.
 * @param pageLabel Words the page in view and the page count.
 * @param pageDescription Words one page for assistive technology.
 * @param zoomInLabel Name of the action that brings the pages closer.
 * @param zoomOutLabel Name of the action that takes them back a step.
 * @param resetZoomLabel Name of the action that returns them to the column's width.
 * @param modifier Modifier applied to the column.
 */
@Composable
private fun PdfPages(
    document: AttachmentPdfDocument,
    style: AttachmentViewerStyle,
    pageLabel: (page: Int, count: Int) -> String,
    pageDescription: (page: Int, count: Int) -> String,
    zoomInLabel: String,
    zoomOutLabel: String,
    resetZoomLabel: String,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    val zoom = rememberZoomState(maxScale = MAX_SCALE)
    val scope = rememberCoroutineScope()
    val page by remember { derivedStateOf { listState.firstVisibleItemIndex + 1 } }
    val sharp by remember { derivedStateOf { zoom.isZoomed } }
    val gutter = ArecaTheme.spacing.xl3
    val count = document.pageCount

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .clipToBounds()
            .zoomControls(
                state = zoom,
                labels = ZoomLabels(zoomIn = zoomInLabel, zoomOut = zoomOutLabel, reset = resetZoomLabel),
            )
            .pointerInput(zoom) {
                detectTapGestures(onDoubleTap = { focus -> scope.launch { zoom.animateToggle(focus) } })
            }
            // Read before the column, so a pinch and a zoomed drag reach neither its scrolling nor
            // the pager's.
            .pointerInput(zoom, listState) {
                awaitEachGesture {
                    awaitFirstDown(requireUnconsumed = false, pass = PointerEventPass.Initial)

                    do {
                        val event = awaitPointerEvent(PointerEventPass.Initial)
                        val fingers = event.changes.count { change -> change.pressed }

                        if (fingers > 1) {
                            val centroid = event.calculateCentroid()
                            zoom.zoom(
                                target = zoom.scale * event.calculateZoom(),
                                focus = if (centroid.isSpecified) centroid else zoom.frame.center,
                                pan = event.calculatePan(),
                            )
                        } else if (zoom.isZoomed) {
                            val remainder = zoom.panBy(event.calculatePan())
                            // A drag past the zoomed view's top or bottom moves on through the
                            // pages, at the zoomed speed.
                            listState.dispatchRawDelta(-remainder.y / zoom.scale)
                        }

                        if (fingers > 1 || zoom.isZoomed) {
                            event.changes.forEach { change ->
                                if (change.positionChanged()) change.consume()
                            }
                        }
                    } while (event.changes.any { change -> change.pressed })

                    zoom.settle()
                }
            },
    ) {
        // The window's width less the gutters, up to a readable width: on a phone that is the
        // whole page, and on a tablet or a desktop an A4 page stays the height of a page a person
        // reads rather than a poster scrolled through a screen at a time.
        val column = (maxWidth - gutter * 2).coerceIn(0.dp, AdaptiveDefaults.ReadableWidth)
        val side = (maxWidth - column) / 2
        val columnWidth = with(LocalDensity.current) { column.roundToPx() }
        val widthPx = if (sharp) columnWidth * SHARP_SCALE else columnWidth

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = zoom.scale
                    scaleY = zoom.scale
                    translationX = zoom.offset.x
                    translationY = zoom.offset.y
                },
            state = listState,
            contentPadding = PaddingValues(horizontal = side, vertical = ArecaTheme.spacing.lg),
            verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg),
        ) {
            items(count = count) { index ->
                val bitmap by produceState<ImageBitmap?>(null, document, index, widthPx) {
                    value = document.render(index, widthPx)
                }
                val shape = RoundedCornerShape(ArecaTheme.radius.sm)
                val drawn = bitmap

                if (drawn != null) {
                    Image(
                        bitmap = drawn,
                        contentDescription = pageDescription(index + 1, count),
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(drawn.width.toFloat() / drawn.height)
                            .clip(shape),
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(A4_RATIO)
                            .background(style.controlBg, shape),
                    )
                }
            }
        }

        if (count > 1) {
            Text(
                text = pageLabel(page, count),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = ArecaTheme.spacing.xl)
                    .background(style.pillBg, CircleShape)
                    .padding(horizontal = ArecaTheme.spacing.lg, vertical = ArecaTheme.spacing.xs),
                style = ArecaTheme.typography.labelSmall,
                color = style.pillContent,
            )
        }
    }
}

/** How far opening the document has come. */
private sealed interface PdfOpening {
    /** Reading and opening. */
    data object Opening : PdfOpening

    /** It cannot be drawn here. */
    data object Failed : PdfOpening

    /**
     * Open.
     *
     * @property document The document, closed when the page leaves the screen.
     */
    class Open(val document: AttachmentPdfDocument) : PdfOpening
}

/** Width over height of an A4 page. */
private const val A4_RATIO = 1f / 1.414f

/** How close a pinch may bring the pages: a footnote at a phone's width, readable. */
private const val MAX_SCALE = 3f

/** How many times the column's width a zoomed column's pages are drawn at. */
private const val SHARP_SCALE = 2
