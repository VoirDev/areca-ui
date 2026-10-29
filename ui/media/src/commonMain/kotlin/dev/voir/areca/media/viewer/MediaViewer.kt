package dev.voir.areca.media.viewer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.AccessibilityManager
import androidx.compose.ui.platform.LocalAccessibilityManager
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.compose.ui.window.Dialog
import dev.voir.areca.foundation.AnnounceOnChange
import dev.voir.areca.foundation.LocalContentColor
import dev.voir.areca.foundation.overlay.edgeToEdgeDialogProperties
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.media.zoom.ZoomState
import dev.voir.areca.media.zoom.rememberZoomState
import dev.voir.areca.platform.SystemBars
import dev.voir.areca.theme.ArecaDarkTheme
import dev.voir.areca.theme.ArecaTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

/** Where a [MediaViewer]'s bars sit. */
public enum class MediaViewerBars {
    /** Over the pictures, fading from a scrim at the screen's edges — pictures meant to fill the screen. */
    Overlay,

    /** Above and below the pictures, which take the height between — documents read to their edges. */
    Docked,
}

/** What a tap on a [MediaViewer]'s picture does to its bars. */
public enum class MediaViewerChrome {
    /** Hides the bars so the picture is alone; another tap brings them back. */
    TapToToggle,

    /** Nothing: the bars always show. */
    Always,
}

/**
 * Pictures full screen, one at a time, in a window of their own: a single picture or a whole
 * gallery, swiped, zoomed, and closed by a swipe down.
 *
 * **Parts, not a screen.** The viewer owns the window, the ground, the pager, the gestures and the
 * transition; what is drawn on it is the application's. [page] draws each item — usually
 * [MediaViewerImage] — and [topBar] and [footer] draw the bars over the picture, each handed the
 * [MediaViewerState] to read the position from and to close or move the viewer with. One item is a
 * single picture view; many are a detail gallery. The bars fade over a scrim of [style], so they
 * read on any picture.
 *
 * **Gestures.** Swipe for the next picture; pinch or double tap to zoom, where one finger pans until
 * the picture is back at its fitted size and a swipe past its edge turns the page; tap to hide the
 * bars ([chrome]); swipe down on a picture at its fitted size to close — it follows the finger and
 * the ground fades. Escape closes and the arrow keys page; with the bars hidden a screen reader is
 * offered [showChromeLabel], and always [closeLabel].
 *
 * **Opening from a frame.** Given a [transition] whose sources were marked with
 * [mediaTransitionSource], the picture grows out of the source keyed [key] of [initialIndex] and
 * shrinks back into the source of the picture on screen when the viewer closes — the screen
 * beneath, told through [onIndexChange], can keep that source on screen. Without one, it fades.
 *
 * **What came of an action** — "Saved to your photos" — is [notice], a line over the picture,
 * announced as it appears and cleared through [onNoticeDismiss] after [noticeDuration], longer where
 * the platform's accessibility settings ask. Sheets the bars open go in [sheets], inside the
 * viewer's window, so they stack above the picture.
 *
 * @param itemCount Number of items; the viewer closes when it reaches zero.
 * @param onDismiss Called once the viewer has closed, after its closing transition.
 * @param modifier Modifier applied to the viewer's frame.
 * @param initialIndex Item the viewer opens on.
 * @param transition Where the pictures sit on the screen beneath, or `null` to fade.
 * @param key The key of an item's source in [transition], and its page's key.
 * @param onIndexChange Called with the item on screen as it changes.
 * @param onIndexSettle Called with the item the pages come to rest on — not those swiped past — to
 *   fetch what a page needs once it is landed on.
 * @param chrome What a tap on the picture does to the bars.
 * @param bars Whether the bars sit over the pictures or above and below them.
 * @param notice What came of the last action, already worded, or `null`.
 * @param onNoticeDismiss Called once [notice] has shown for [noticeDuration], or `null` to leave it.
 * @param noticeDuration How long [notice] shows before [onNoticeDismiss].
 * @param showChromeLabel Name of the action that brings hidden bars back, for a screen reader.
 * @param closeLabel Name of the action that closes the viewer, for a screen reader.
 * @param style Ground and bar colours.
 * @param backdrop Drawn over the ground, under the pictures, for the item on screen — a blurred copy
 *   of it — or `null`.
 * @param topBar The bar over the picture's top edge.
 * @param footer What sits over its bottom edge: words, an indicator or thumbnails, an action.
 * @param sheets Sheets drawn above the viewer, inside its window.
 * @param page Draws the item at an index.
 */
@Composable
public fun MediaViewer(
    itemCount: Int,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    initialIndex: Int = 0,
    transition: MediaTransitionState? = null,
    key: (index: Int) -> Any = { index -> index },
    onIndexChange: ((index: Int) -> Unit)? = null,
    onIndexSettle: ((index: Int) -> Unit)? = null,
    chrome: MediaViewerChrome = MediaViewerChrome.TapToToggle,
    bars: MediaViewerBars = MediaViewerBars.Overlay,
    notice: String? = null,
    onNoticeDismiss: (() -> Unit)? = null,
    noticeDuration: Duration = 3.seconds,
    showChromeLabel: String = "Show controls",
    closeLabel: String = "Close",
    style: MediaViewerStyle = LocalMediaViewerStyle.current ?: MediaViewerStyle.dark(),
    backdrop: (@Composable BoxScope.(index: Int) -> Unit)? = null,
    topBar: @Composable BoxScope.(state: MediaViewerState) -> Unit = {},
    footer: @Composable ColumnScope.(state: MediaViewerState) -> Unit = {},
    sheets: @Composable () -> Unit = {},
    page: @Composable MediaViewerPageScope.(index: Int) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(initialPage = initialIndex.coerceIn(0, (itemCount - 1).coerceAtLeast(0))) { itemCount }
    val latestDismiss by rememberUpdatedState(onDismiss)
    val latestKey by rememberUpdatedState(key)
    val enter = remember { Animatable(0f) }
    val drag = remember { Animatable(0f) }
    var closing by remember { mutableStateOf(false) }
    var pageBounds by remember { mutableStateOf(Rect.Zero) }
    var flight by remember { mutableStateOf<MediaTransitionSource?>(null) }

    val close: () -> Unit = {
        if (!closing) {
            closing = true
            scope.launch {
                val sourceKey = latestKey(pagerState.currentPage)
                flight = transition?.source(sourceKey)?.takeIf { it.bounds.overlaps(pageBounds) }
                transition?.hidden = if (flight != null) sourceKey else null
                launch { drag.animateTo(0f, tween(CLOSE_MILLIS)) }
                enter.animateTo(0f, tween(CLOSE_MILLIS, easing = FastOutSlowInEasing))
                transition?.hidden = null
                latestDismiss()
            }
        }
    }
    val state = remember(pagerState) { MediaViewerState(pagerState, scope) { close() } }

    LaunchedEffect(Unit) {
        snapshotFlow { pageBounds }.first { !it.isEmpty }
        val sourceKey = latestKey(pagerState.currentPage)
        flight = transition?.source(sourceKey)
        transition?.hidden = if (flight != null) sourceKey else null
        enter.animateTo(1f, tween(OPEN_MILLIS, easing = FastOutSlowInEasing))
        transition?.hidden = null
        flight = null
    }
    LaunchedEffect(itemCount) { if (itemCount == 0) latestDismiss() }
    LaunchedEffect(pagerState.currentPage) { onIndexChange?.invoke(pagerState.currentPage) }
    val latestSettle by rememberUpdatedState(onIndexSettle)
    LaunchedEffect(pagerState.settledPage) {
        if (itemCount > 0) latestSettle?.invoke(pagerState.settledPage)
        state.zooms.forEach { (index, zoom) -> if (index != pagerState.settledPage) zoom.reset() }
    }
    val accessibility = LocalAccessibilityManager.current
    LaunchedEffect(notice, onNoticeDismiss) {
        val timeout = noticeTimeout(noticeDuration, accessibility)
        if (notice != null && onNoticeDismiss != null && timeout != null) {
            delay(timeout)
            onNoticeDismiss()
        }
    }

    Dialog(onDismissRequest = close, properties = edgeToEdgeDialogProperties(dismissOnClickOutside = false)) {
        val themed: @Composable (@Composable () -> Unit) -> Unit = { content ->
            if (style.dark) ArecaDarkTheme { content() } else content()
        }
        themed {
            val bg = if (state.isChromeVisible) style.bg else style.bgChromeHidden
            SystemBars(bg)
            CompositionLocalProvider(LocalContentColor provides style.content) {
                ViewerFrame(
                    state = state,
                    modifier = modifier,
                    enter = enter,
                    drag = drag,
                    closing = closing,
                    flight = flight,
                    pageBounds = pageBounds,
                    onPageBounds = { pageBounds = it },
                    close = close,
                    key = key,
                    chrome = chrome,
                    bars = bars,
                    notice = notice,
                    showChromeLabel = showChromeLabel,
                    closeLabel = closeLabel,
                    style = style,
                    bg = bg,
                    backdrop = backdrop,
                    topBar = topBar,
                    footer = footer,
                    page = page,
                )
                sheets()
            }
        }
    }
}

/**
 * The viewer's window content: ground, backdrop, pages and bars, moved by the transition and the
 * swipe down.
 */
@Composable
private fun ViewerFrame(
    state: MediaViewerState,
    modifier: Modifier,
    enter: Animatable<Float, *>,
    drag: Animatable<Float, *>,
    closing: Boolean,
    flight: MediaTransitionSource?,
    pageBounds: Rect,
    onPageBounds: (Rect) -> Unit,
    close: () -> Unit,
    key: (Int) -> Any,
    chrome: MediaViewerChrome,
    bars: MediaViewerBars,
    notice: String?,
    showChromeLabel: String,
    closeLabel: String,
    style: MediaViewerStyle,
    bg: Color,
    backdrop: (@Composable BoxScope.(Int) -> Unit)?,
    topBar: @Composable BoxScope.(MediaViewerState) -> Unit,
    footer: @Composable ColumnScope.(MediaViewerState) -> Unit,
    page: @Composable MediaViewerPageScope.(Int) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    val dismissPx = with(density) { DISMISS_DISTANCE.toPx() }
    val flingPx = with(density) { DISMISS_FLING.toPx() }
    val focus = remember { FocusRequester() }
    val showBars by remember {
        derivedStateOf { state.isChromeVisible && enter.value == 1f && drag.value == 0f && !closing }
    }
    LaunchedEffect(Unit) { focus.requestFocus() }

    Box(
        modifier = modifier
            .fillMaxSize()
            .semantics {
                customActions = buildList {
                    if (!state.isChromeVisible) add(CustomAccessibilityAction(showChromeLabel) { state.showChrome(); true })
                    add(CustomAccessibilityAction(closeLabel) { close(); true })
                }
            }
            .focusRequester(focus)
            .focusable()
            .onPreviewKeyEvent { event ->
                if (event.type != KeyEventType.KeyDown) return@onPreviewKeyEvent false
                when (event.key) {
                    Key.Escape -> close()
                    Key.DirectionRight -> state.goTo(state.currentIndex + 1)
                    Key.DirectionLeft -> state.goTo(state.currentIndex - 1)
                    else -> return@onPreviewKeyEvent false
                }
                true
            },
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .graphicsLayer {
                    val dragged = (abs(drag.value) / (size.height * GROUND_FADE_SHARE)).coerceAtMost(1f)
                    alpha = enter.value * (1f - dragged)
                }
                .background(bg),
        )
        if (backdrop != null && state.itemCount > 0) {
            Box(Modifier.fillMaxSize().graphicsLayer { alpha = enter.value }) { backdrop(state.currentIndex) }
        }

        val pager: @Composable (Modifier) -> Unit = { pagerModifier ->
            HorizontalPager(
                state = state.pagerState,
                modifier = pagerModifier
                    .fillMaxSize()
                    .onGloballyPositioned { onPageBounds(it.boundsInWindow()) }
                    .graphicsLayer {
                        val dragScale = 1f - (abs(drag.value) / size.height * DRAG_SHRINK).coerceAtMost(MAX_DRAG_SHRINK)
                        val source = flight
                        if (source != null && !pageBounds.isEmpty) {
                            val target = fitted(pageBounds, source.aspectRatio)
                            val from = fitted(source.bounds, source.aspectRatio)
                            val startScale = from.width / target.width
                            val shift = from.center - pageBounds.center - (target.center - pageBounds.center) * startScale
                            val scale = lerp(startScale, 1f, enter.value) * dragScale
                            scaleX = scale
                            scaleY = scale
                            translationX = lerp(shift.x, 0f, enter.value)
                            translationY = lerp(shift.y, 0f, enter.value) + drag.value
                        } else {
                            val scale = lerp(ENTER_SCALE, 1f, enter.value) * dragScale
                            scaleX = scale
                            scaleY = scale
                            alpha = enter.value
                            translationY = drag.value
                        }
                    }
                    .draggable(
                        orientation = Orientation.Vertical,
                        enabled = !state.isZoomed && !closing,
                        state = rememberDraggableState { delta -> scope.launch { drag.snapTo(drag.value + delta) } },
                        onDragStopped = { velocity ->
                            if (abs(drag.value) > dismissPx || abs(velocity) > flingPx) close() else drag.animateTo(0f)
                        },
                    ),
                pageSpacing = PAGE_GAP,
                userScrollEnabled = !state.isZoomed,
                key = { index -> key(index) },
            ) { index ->
                val zoom = rememberZoomState()
                SideEffect { state.zooms[index] = zoom }
                DisposableEffect(index) { onDispose { state.zooms.remove(index) } }
                val pageScope = remember(index, zoom, chrome) { ViewerPage(zoom, state, index, chrome) }
                Box(Modifier.fillMaxSize()) { pageScope.page(index) }
            }
        }

        if (bars == MediaViewerBars.Docked) {
            Column(Modifier.fillMaxSize()) {
                val barAlpha = Modifier.graphicsLayer { alpha = if (showBars) 1f else 0f }
                Box(
                    barAlpha
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal)),
                ) { topBar(state) }
                Box(Modifier.weight(1f).fillMaxWidth()) { pager(Modifier) }
                Column(
                    barAlpha
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal))
                        .padding(top = ArecaTheme.spacing.lg, bottom = ArecaTheme.spacing.xl),
                ) { footer(state) }
            }
        } else {
            pager(Modifier)
            OverlayBars(state, showBars, style, topBar, footer)
        }

        AnnounceOnChange(notice)
        if (notice != null) {
            Text(
                text = notice,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .windowInsetsPadding(WindowInsets.safeDrawing)
                    .padding(bottom = NOTICE_LIFT, start = ArecaTheme.spacing.xl, end = ArecaTheme.spacing.xl)
                    .background(style.noticeBg, ArecaTheme.shapes.full)
                    .padding(horizontal = ArecaTheme.spacing.xl, vertical = ArecaTheme.spacing.md),
                style = ArecaTheme.typography.bodySmall,
                color = style.noticeContent,
                textAlign = TextAlign.Center,
            )
        }
    }
}

/** The bars laid over the pictures, each fading from a scrim at its edge of the screen. */
@Composable
private fun BoxScope.OverlayBars(
    state: MediaViewerState,
    showBars: Boolean,
    style: MediaViewerStyle,
    topBar: @Composable BoxScope.(MediaViewerState) -> Unit,
    footer: @Composable ColumnScope.(MediaViewerState) -> Unit,
) {
    AnimatedVisibility(showBars, Modifier.align(Alignment.TopCenter), enter = fadeIn(), exit = fadeOut()) {
        Box(
            Modifier
                .fillMaxWidth()
                .background(Brush.verticalGradient(listOf(style.scrim, style.scrim.copy(alpha = 0f))))
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal)),
        ) { topBar(state) }
    }
    AnimatedVisibility(showBars, Modifier.align(Alignment.BottomCenter), enter = fadeIn(), exit = fadeOut()) {
        Column(
            Modifier
                .fillMaxWidth()
                .background(Brush.verticalGradient(listOf(style.scrim.copy(alpha = 0f), style.scrim)))
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal))
                .padding(top = ArecaTheme.spacing.xl4, bottom = ArecaTheme.spacing.xl),
        ) { footer(state) }
    }
}

/** A page's scope: its zoom, and what its taps and edge swipes do. */
private class ViewerPage(
    override val zoomState: ZoomState,
    private val state: MediaViewerState,
    private val index: Int,
    private val chrome: MediaViewerChrome,
) : MediaViewerPageScope {
    override val isCurrent: Boolean
        get() = state.currentIndex == index

    override fun onTap() {
        if (chrome == MediaViewerChrome.TapToToggle) state.toggleChrome()
    }

    override fun onEdgeSwipe(direction: Int) {
        state.goTo(index + direction)
    }
}

/**
 * The rectangle a picture of [aspectRatio] fills when fitted whole into [bounds], centred.
 *
 * @param bounds The space.
 * @param aspectRatio Width over height, or `null` for a picture that fills it.
 * @return The picture's rectangle.
 */
internal fun fitted(bounds: Rect, aspectRatio: Float?): Rect {
    if (aspectRatio == null || aspectRatio <= 0f || bounds.isEmpty) return bounds
    val wide = bounds.width / bounds.height > aspectRatio
    val size = if (wide) Size(bounds.height * aspectRatio, bounds.height) else Size(bounds.width, bounds.width / aspectRatio)
    return Rect(Offset(bounds.center.x - size.width / 2f, bounds.center.y - size.height / 2f), size)
}

/**
 * How long a notice stays, once the platform's accessibility settings have had their say.
 *
 * @param duration How long the application asked for.
 * @param accessibilityManager The platform's settings, or `null` to keep [duration].
 * @return How long it stays, or `null` to stay until cleared.
 */
internal fun noticeTimeout(duration: Duration, accessibilityManager: AccessibilityManager?): Duration? {
    if (accessibilityManager == null) return duration
    val recommended = accessibilityManager.calculateRecommendedTimeoutMillis(
        originalTimeoutMillis = duration.inWholeMilliseconds,
        containsIcons = false,
        containsText = true,
        containsControls = false,
    )
    return recommended.milliseconds.takeIf { it.isFinite() }
}

/** How long the picture takes to grow out of its source. */
private const val OPEN_MILLIS = 320

/** How long it takes to shrink back. */
private const val CLOSE_MILLIS = 260

/** How far a swipe down goes before releasing it closes the viewer. */
private val DISMISS_DISTANCE = 110.dp

/** How fast a swipe down must be thrown, per second, to close the viewer from any distance. */
private val DISMISS_FLING = 1200.dp

/** Gap between pictures while they are swiped. */
private val PAGE_GAP = 16.dp

/** Space between the bottom edge and a notice, clear of the footer. */
private val NOTICE_LIFT = 120.dp

/** Share of the screen's height a swipe down travels before the ground has faded away. */
private const val GROUND_FADE_SHARE = 0.6f

/** How much a picture shrinks per screen height it is dragged down. */
private const val DRAG_SHRINK = 0.5f

/** The most a dragged picture shrinks. */
private const val MAX_DRAG_SHRINK = 0.25f

/** Scale a picture opening without a source grows from. */
private const val ENTER_SCALE = 0.96f
