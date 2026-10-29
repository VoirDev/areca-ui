# Media — `dev.voir.areca:media`

Add it when a screen shows pictures from data: a product's photos, a carousel of looks, thumbnails,
a picture to zoom.

- Artifact `dev.voir.areca:media`; packages `dev.voir.areca.media` (loader),
  `.image`, `.zoom`, `.carousel`, `.thumbnail`, `.viewer`, `.compare`.
- Passes `foundation` on (`api`). Needs the app's `ArecaThemeProvider` above it.
- It loads nothing itself: implement `MediaImageLoader` once over the app's loader (Coil:
  `ImageRequest.size(maxSize).scale(Scale.FIT)` in `load`, `.size(side).scale(Scale.FILL)` in
  `loadSquare`) and provide it with `LocalMediaImageLoader`. Unprovided, every picture fails.
- Its locals (`LocalMediaFrameStyle`, `LocalMediaFrameSize`, `LocalThumbnailStripStyle`,
  `LocalThumbnailStripSize`, `LocalMediaViewerStyle`) are not seeded by `ArecaThemeProvider`.
- It repeats nothing foundation has: words on a picture are `Badge`, indicators are
  `PageIndicator` / `PageCounter`, a viewer's bar is `TopBar`. An onboarding tour is `SwipePager`,
  not `Carousel`.
- Indicators are foundation's: `PageIndicator` (`PageIndicatorVariant`, `PageIndicatorStyle.onImage()`)
  and `PageCounter`, fed from `CarouselState`.

## Choose

| Need | Use |
| --- | --- |
| A picture from a model in a shaped frame, with loading and failed faces | `MediaFrame(model, contentDescription, aspectRatio = …, fit = MediaFit.Whole / Fill)` |
| A product standing whole on its fill | `fit = MediaFit.Whole`, `size = MediaFrameSize.Product` |
| A blurred thumbnail while the full picture loads | `placeholder = MediaPlaceholder.Blurred(thumbnailModel)` |
| A word on a picture — "New", "−20%", whose photo it is | foundation `Badge(Modifier.align(…).padding(…), style = BadgeStyle.Brand / Danger / NeutralDark, size = BadgeSize.Small) { Text(…) }` in the frame's trailing `overlay` |
| A title over a photograph | `scrim = MediaScrim.Bottom` + a white `Text` in the `overlay` |
| The viewer's top bar | foundation `TopBar(title, style = viewerStyle.topBar(), titleAlignment = TopBarTitleAlignment.Center, leading = { CloseAction(onClick = viewer::dismiss) }, trailing = { ShareAction(…); DownloadAction(…); FavoriteAction(…); OverflowAction(…) })` |
| Content drawn by something else, with the same faces | `MediaFrame(state = MediaState.Loading / Ready / Failed, onRetry = …) { … }` |
| A picture filling a page, zoomable — a viewer | `MediaImage(model, contentDescription, zoom = ZoomBehavior.Sticky, zoomState = …, style = MediaFrameStyle.bare())` |
| Pinch to look closer in a list, springing back | `zoom = ZoomBehavior.Peek` on `MediaFrame` or `MediaImage` |
| Magnify under the mouse on desktop | `zoom = ZoomBehavior.Hover` on `MediaFrame` or `MediaImage` |
| A page of your own when a picture fails | `MediaImage(…, failure = { retry -> … })` |
| Zoom on content that is not a picture | `Modifier.zoomable(rememberZoomState(), contentSize = …)` |
| Pictures swiped sideways | `Carousel(rememberCarouselState(n)) { i -> MediaFrame(…) }` |
| The next picture peeking | `pageSize = CarouselPageSize.Fraction(0.8f)`, `contentPadding`, `itemSpacing` |
| Two side by side moving as a pair | `pageSize = CarouselPageSize.PerPage(2)`, `snap = CarouselSnap.Page` |
| Fixed-width tiles | `pageSize = CarouselPageSize.Fixed(140.dp)` |
| Stories | `autoplay = 4.seconds` + `PageIndicator(variant = Segments, progress = { state.autoplayProgress })` |
| Arrows for a pointer | `CarouselArrows(state)` laid over the carousel |
| A marker on a point of a picture | `pictureOverlay = { Hotspot(onClick, contentDescription, Modifier.pinned(x, y), text = null / "1" / "Chair") }` on `MediaFrame`, `MediaImage` or `MediaViewerImage` |
| Pictures full screen, one or many | `MediaViewer(itemCount, onDismiss, initialIndex, topBar = { s -> … }, footer = { s -> … }) { i -> MediaViewerImage(model, contentDescription) }` |
| Grow the picture out of its frame | `rememberMediaTransitionState()` passed to the frames (`transition`, `transitionKey`) and to `MediaViewer(transition, key)` |
| A photo taken next to the one on record | `ComparePair(first, second, labels = CompareLabels("Your photo", "In our catalogue"), onOpen)` |
| Before and after | `CompareSlider` (divider), `CompareSideBySide` (linked zoom), `CompareHold`, `CompareFade` |
| Thumbnails under or beside pictures | `ThumbnailStrip(count, selectedIndex = state.currentItem, onSelect = state::goToItem) { i -> MediaFrame(…, fit = MediaFit.Fill) }` |

## Signatures

```kotlin
interface MediaImageLoader {
    @Composable fun load(model: Any, maxSize: Int): MediaImageState
    @Composable fun loadSquare(model: Any, side: Int): MediaImageState   // default: load(model, side * 2)
    data object None : MediaImageLoader                                  // fails everything
}
// MediaImageState: Loading, Ready(painter), Failed. LocalMediaImageLoader (static).

@Composable fun MediaFrame(model: Any?, contentDescription: String?, modifier: Modifier = Modifier,
    aspectRatio: Float? = null, fit: MediaFit = MediaFit.Fill,
    placeholder: MediaPlaceholder = MediaPlaceholder.Skeleton, zoom: ZoomBehavior = ZoomBehavior.None,
    onClick: (() -> Unit)? = null, onClickLabel: String? = null, scrim: MediaScrim = MediaScrim.None,
    failedText: String? = "Couldn't load this picture", retryLabel: String = "Retry",
    zoomLabels: ZoomLabels = ZoomLabels(), style: MediaFrameStyle = …, size: MediaFrameSize = …,
    overlay: @Composable BoxScope.() -> Unit = {})
// `zoom = Sticky` is treated as None in a frame; use MediaImage for a viewer.

@Composable fun MediaFrame(modifier: Modifier = Modifier, aspectRatio: Float? = null,
    state: MediaState = MediaState.Ready, onRetry: (() -> Unit)? = null, onClick: (() -> Unit)? = null,
    onClickLabel: String? = null, scrim: MediaScrim = MediaScrim.None, failedText: String? = …,
    retryLabel: String = "Retry", style: MediaFrameStyle = …, size: MediaFrameSize = …,
    overlay: @Composable BoxScope.() -> Unit = {}, content: @Composable BoxScope.() -> Unit)
// Slot content is not inset; pad it by size.inset for a whole picture.

@Composable fun MediaImage(model: Any?, contentDescription: String?, modifier: Modifier = Modifier,
    fit: MediaFit = MediaFit.Whole, placeholder: MediaPlaceholder = MediaPlaceholder.Skeleton,
    zoom: ZoomBehavior = ZoomBehavior.None, zoomState: ZoomState = rememberZoomState(),
    onTap: (() -> Unit)? = null, onEdgeSwipe: ((direction: Int) -> Unit)? = null,
    failedText: String? = …, retryLabel: String = "Retry", zoomLabels: ZoomLabels = ZoomLabels(),
    style: MediaFrameStyle = …, size: MediaFrameSize = …,
    pictureOverlay: (@Composable MediaPictureScope.() -> Unit)? = null,
    failure: (@Composable BoxScope.(retry: () -> Unit) -> Unit)? = null)   // replaces the failed face


// MediaFit: Whole, Fill. MediaScrim: None, Top, Bottom, Both. MediaState: Loading, Ready, Failed.
// MediaPlaceholder: Skeleton, Blurred(model), None.
// MediaFrameStyle(bg, shape, content, scrim): Default, default(…), circle(bg), bare(content).
// MediaFrameSize(inset, failureGlyph, compactBelow): Default, Product, default(…).

@Composable fun rememberZoomState(maxScale: Float = 5f): ZoomState
// ZoomState: maxScale, scale, offset, isZoomed, canZoomIn, frame, content, zoomIn(), zoomOut(), reset(),
// suspend animateReset(), suspend animateToggle(focus), zoom(target, focus, pan), panBy(pan): Offset
// (the part the edges stopped), settle(). For content with its own gestures:
// @Composable fun Modifier.zoomControls(state: ZoomState, labels: ZoomLabels = ZoomLabels()): Modifier
@Composable fun Modifier.zoomable(state: ZoomState, contentSize: Size = Size.Unspecified,
    behavior: ZoomBehavior = ZoomBehavior.Sticky, onTap: (() -> Unit)? = null,
    onEdgeSwipe: ((direction: Int) -> Unit)? = null, labels: ZoomLabels = ZoomLabels()): Modifier
// ZoomBehavior: None, Sticky (stays; viewer), Peek (springs back; caller draws the lifted content),
// Hover (magnifies under a mouse pointer; desktop).
// ZoomLabels(zoomIn = "Zoom in", zoomOut = "Zoom out", reset = "Reset zoom").

@Composable fun rememberCarouselState(itemCount: Int, initialItem: Int = 0, loop: Boolean = false): CarouselState
// CarouselState: itemCount, loop, pageCount, currentPage, currentPageOffset, currentItem,
// isScrollInProgress, autoplayProgress, next(), previous(), goToItem(item, animated = true),
// goToPage(page, animated = true).
@Composable fun Carousel(state: CarouselState, modifier: Modifier = Modifier,
    pageSize: CarouselPageSize = CarouselPageSize.Fill, snap: CarouselSnap = CarouselSnap.Item,
    itemSpacing: Dp = 0.dp, contentPadding: PaddingValues = PaddingValues(0.dp),
    autoplay: Duration? = null, userScrollEnabled: Boolean = true, contentDescription: String? = null,
    key: ((index: Int) -> Any)? = null, item: @Composable (index: Int) -> Unit)
// CarouselPageSize: Fill, Fraction(fraction), Fixed(width), PerPage(count). CarouselSnap: Item, Page.
@Composable fun CarouselArrows(state: CarouselState, modifier: Modifier = Modifier,
    previousLabel: String = "Previous picture", nextLabel: String = "Next picture",
    style: IconButtonStyle = …, size: IconButtonSize = IconButtonSize.Small)

@Composable fun ThumbnailStrip(count: Int, selectedIndex: Int, onSelect: (index: Int) -> Unit,
    modifier: Modifier = Modifier, orientation: Orientation = Orientation.Horizontal,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    label: (index: Int, count: Int) -> String = { i, n -> "Picture ${i + 1} of $n" },
    style: ThumbnailStripStyle = …, size: ThumbnailStripSize = …, thumbnail: @Composable (index: Int) -> Unit)
// ThumbnailStripStyle(ring, shape, otherAlpha): Default, default(…), onDark(), onLight().
// ThumbnailStripSize(side, spacing, ringWidth, ringGap): Default, default(…).
```

```kotlin
@Composable fun Hotspot(onClick: () -> Unit, contentDescription: String, modifier: Modifier = Modifier,
    text: String? = null, selected: Boolean = false, style: HotspotStyle = HotspotStyle.default())
// MediaPictureScope.pinned(x, y): x and y are fractions of the picture, 0f..1f. HotspotStyle(bg, content, ring).
// MediaFrame(model, …, transition: MediaTransitionState? = null, transitionKey: Any? = null,
//     pictureOverlay: (@Composable MediaPictureScope.() -> Unit)? = null, overlay = …); MediaImage takes pictureOverlay too.

@Composable fun rememberMediaTransitionState(): MediaTransitionState
fun Modifier.mediaTransitionSource(state: MediaTransitionState, key: Any, pictureSize: Size = Size.Unspecified): Modifier

@Composable fun MediaViewer(itemCount: Int, onDismiss: () -> Unit, modifier: Modifier = Modifier,
    initialIndex: Int = 0, transition: MediaTransitionState? = null, key: (index: Int) -> Any = { it },
    onIndexChange: ((index: Int) -> Unit)? = null, onIndexSettle: ((index: Int) -> Unit)? = null,
    chrome: MediaViewerChrome = MediaViewerChrome.TapToToggle, bars: MediaViewerBars = MediaViewerBars.Overlay,
    notice: String? = null, onNoticeDismiss: (() -> Unit)? = null, noticeDuration: Duration = 3.seconds,
    showChromeLabel: String = "Show controls", closeLabel: String = "Close",
    style: MediaViewerStyle = LocalMediaViewerStyle.current ?: MediaViewerStyle.dark(),
    backdrop: (@Composable BoxScope.(index: Int) -> Unit)? = null,
    topBar: @Composable BoxScope.(state: MediaViewerState) -> Unit = {},
    footer: @Composable ColumnScope.(state: MediaViewerState) -> Unit = {},
    sheets: @Composable () -> Unit = {}, page: @Composable MediaViewerPageScope.(index: Int) -> Unit)
// MediaViewerState: itemCount, currentIndex, currentIndexOffset, isChromeVisible, isZoomed,
// toggleChrome(), showChrome(), goTo(index, animated = true), dismiss().
// MediaViewerPageScope: zoomState, isCurrent, onTap(), onEdgeSwipe(direction).
// MediaViewerStyle(bg, bgChromeHidden, content, scrim, noticeBg, noticeContent, dark): dark(bg), light(bg);
// topBar(): the TopBarStyle for a foundation TopBar over the pictures (Scrim actions dark, Floating light).
// MediaViewerChrome: TapToToggle, Always. MediaViewerBars: Overlay (over pictures, scrims), Docked (above and below; documents).
@Composable fun MediaViewerPageScope.MediaViewerImage(model: Any?, contentDescription: String?,
    modifier: Modifier = Modifier, fit: MediaFit = MediaFit.Whole,
    placeholder: MediaPlaceholder = MediaPlaceholder.None, failedText: String? = "This picture didn't load",
    retryLabel: String = "Try again", zoomLabels: ZoomLabels = ZoomLabels(),
    pictureOverlay: (@Composable MediaPictureScope.() -> Unit)? = null)

@Composable fun ComparePair(first: @Composable BoxScope.() -> Unit, second: @Composable BoxScope.() -> Unit,
    modifier: Modifier = Modifier, arrangement: CompareArrangement = CompareArrangement.Column,
    aspectRatio: Float = …, labels: CompareLabels? = null, captions: CompareLabels? = null,
    onOpen: ((index: Int) -> Unit)? = null, openLabel: String = "Open picture", style: CompareStyle = …)
@Composable fun CompareSlider(before: @Composable BoxScope.() -> Unit, after: @Composable BoxScope.() -> Unit,
    modifier: Modifier = Modifier, aspectRatio: Float = 3f / 2f, orientation: Orientation = Orientation.Horizontal,
    initialPosition: Float = 0.5f, labels: CompareLabels? = CompareLabels(),
    contentDescription: String = "Before and after", style: CompareStyle = …)
@Composable fun CompareSideBySide(before, after, modifier: Modifier = Modifier,
    arrangement: CompareArrangement = CompareArrangement.Row, aspectRatio: Float = …,
    labels: CompareLabels? = CompareLabels(), maxScale: Float = 5f, zoomLabels: ZoomLabels = ZoomLabels(), style = …)
@Composable fun CompareHold(before, after, modifier: Modifier = Modifier, aspectRatio: Float = 3f / 2f,
    labels: CompareLabels? = CompareLabels(), holdLabel: String = "Hold to see before", style = …)
@Composable fun CompareFade(before, after, modifier: Modifier = Modifier, aspectRatio: Float = 3f / 2f,
    initialFraction: Float = 0.5f, labels: CompareLabels? = CompareLabels(),
    contentDescription: String = "Fade between before and after", style = …)
// CompareLabels(first = "Before", second = "After"). CompareArrangement: Column, Row.
// CompareStyle(shape, bg, divider, knob, knobContent, labelBg, labelContent): default(shape, bg).
```

## Rules

- Load through the frame, never decode in the screen: the frame asks the loader for the size it is
  drawn at, retries by recomposing the call, and draws the faces.
- Give every frame its `aspectRatio` (or a fixed size) so loading reserves the space.
- Pressing a picture is the frame's `onClick` with an `onClickLabel`; the carousel never decides what
  a press opens.
- One indicator reads out; give it `contentDescription` ("Picture 3 of 9"). Over a picture use the
  `onImage()` styles.
- `Peek` in lists and carousels, `Sticky` only in a full-screen viewer.
- The viewer's bars are the app's — foundation's `TopBar` above, flow-gallery's caption, author and
  action below: close with `state.dismiss()` so the picture shrinks back into its frame; follow the
  viewer with `onIndexChange` so that frame is on screen.
- Draw compared pictures the same way (`fit = MediaFit.Fill`) so their parts line up.
- Never build a viewer bar or a label of your own: `TopBar(style = viewerStyle.topBar())` with
  `CloseAction` and the named actions, and `Badge` in a frame's `overlay`.
- `Hover` only where a mouse is the pointer (desktop, web); it does nothing on touch.
