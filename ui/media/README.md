# Media

Pictures for Compose Multiplatform: frames with loading and failed faces, hotspots on pictures,
zoom that stays, springs back or follows the mouse, carousels sized every common way, thumbnail
strips, a full-screen viewer that grows pictures out of their frames, and ways to compare two
pictures. It decodes nothing itself: the application plugs in its own image loader once.

Words on a picture are foundation's `Badge`, its indicators are foundation's `PageIndicator` and
`PageCounter`, and a viewer's bar is foundation's `TopBar` — media does not repeat them. Pages of an
onboarding tour are foundation's `SwipePager`; `Carousel` is for pictures and other items in a row.

## Install

```kotlin
implementation("dev.voir.areca:media:<version>")
```

`media` passes `foundation` on (`api`): its parts take foundation types and are composed beside
foundation components. `ArecaThemeProvider` must sit above them. For the repository and
credentials, see [Getting started](../../docs/getting-started.md).

## Provide a loader

`media` has no image-loading library and takes none on. Implement `MediaImageLoader` once over the
loader the application already uses, and provide it above every screen through
`LocalMediaImageLoader`. With Coil 3:

```kotlin
object CoilMediaImageLoader : MediaImageLoader {
    @Composable
    override fun load(model: Any, maxSize: Int): MediaImageState {
        val context = LocalPlatformContext.current
        val request = remember(model, maxSize) {
            ImageRequest.Builder(context).data(model).size(maxSize).scale(Scale.FIT).build()
        }
        return rememberAsyncImagePainter(request).state.collectAsState().value.toMediaState()
    }

    @Composable
    override fun loadSquare(model: Any, side: Int): MediaImageState {
        val context = LocalPlatformContext.current
        val request = remember(model, side) {
            ImageRequest.Builder(context).data(model).size(side).scale(Scale.FILL).build()
        }
        return rememberAsyncImagePainter(request).state.collectAsState().value.toMediaState()
    }
}

private fun AsyncImagePainter.State.toMediaState(): MediaImageState = when (this) {
    is AsyncImagePainter.State.Success -> MediaImageState.Ready(painter)
    is AsyncImagePainter.State.Error -> MediaImageState.Failed
    else -> MediaImageState.Loading
}

CompositionLocalProvider(LocalMediaImageLoader provides CoilMediaImageLoader) { App() }
```

The model is the application's own — a URL, a file, a key — passed through untouched. Unprovided,
the loader fails every picture, so a screen with no loader yet shows its failed faces.

## Components

| Component | Use it for |
| --- | --- |
| `MediaFrame` | A picture's place on the page: shape, fill, aspect ratio, loading and failed faces, a scrim, an overlay and a press. One overload loads a model; the other frames content you draw yourself and says its `MediaState` |
| `MediaImage` | A picture filling the space it is given, with its faces and zoom — a viewer's page |
| `Carousel` | Pictures swiped sideways: one at a time, the next peeking, or several moving one by one or as a group; loop and autoplay |
| `CarouselArrows` | Previous and next buttons over a carousel, for a pointer |
| `ThumbnailStrip` | Small pictures, the selected one ringed, as a strip or a rail |
| `Modifier.zoomable` | Pinch, double tap and pan on any content, with keys and screen-reader actions |
| `Hotspot` | A marker on a point of a picture, in a frame's `pictureOverlay`, staying on its point as it zooms |
| `MediaViewer` | Pictures full screen in a window of their own: one or many, swiped, zoomed, closed by a swipe down, with your bars over them |
| `MediaViewerImage` | A viewer page's picture, its zoom and taps wired to the viewer |
| `ComparePair` | Two pictures whole, labelled, one above the other or side by side — a photo taken and the one on record |
| `CompareSlider` | Two pictures one over the other with a divider to drag |
| `CompareSideBySide` | Two pictures whose zooms move together |
| `CompareHold` | One picture that shows the other while held |
| `CompareFade` | Two pictures cross-faded along a track |

## Frames and faces

```kotlin
MediaFrame(
    model = product.pictureUrl,
    contentDescription = product.name,
    aspectRatio = 4f / 5f,
    fit = MediaFit.Whole,                       // a product on its fill; Fill crops a photograph
    size = MediaFrameSize.Product,              // whole pictures clear of the corners
    placeholder = MediaPlaceholder.Blurred(product.thumbnailUrl),
    zoom = ZoomBehavior.Peek,                   // pinch to look closer, springs back
    onClick = { openViewer(product, 0) },
    onClickLabel = "Open pictures",
) {
    Badge(Modifier.align(Alignment.TopStart).padding(8.dp), style = BadgeStyle.Danger, size = BadgeSize.Small) {
        Text("−20%")
    }
}
```

While loading, a frame shimmers at its own aspect ratio, so nothing moves when the picture arrives,
or shows `MediaPlaceholder.Blurred` — a thumbnail, blurred. Past 300 ms a spinner joins it. A failed
picture shows a glyph, `failedText` and a `retryLabel` button that loads it again; a frame narrower
than `MediaFrameSize.compactBelow` shows the glyph alone and retries when pressed. A frame with no
model fails without a retry.

`MediaFrameStyle` sets the fill, shape, failed face's colour and scrim (`default()`, `circle()`,
`bare()` for a picture on a ground of its own); `MediaFrameSize` sets the inset of a whole picture
(`Default`, `Product`). `MediaScrim.Bottom` darkens the bottom edge so a white `Text` title reads
over any photograph. Provide `LocalMediaFrameStyle` and `LocalMediaFrameSize` to change
every frame below; `ArecaThemeProvider` does not seed them.

Content drawn by something else — a map, a rendered page — goes in the slot overload, which draws
the faces for the `MediaState` you pass:

```kotlin
MediaFrame(aspectRatio = 1f, state = mapState.toMediaState(), onRetry = mapState::reload) {
    MapView(Modifier.fillMaxSize())
}
```

## Carousel

```kotlin
val state = rememberCarouselState(itemCount = pictures.size)

Box {
    Carousel(state = state, contentDescription = "${product.name} pictures") { index ->
        MediaFrame(
            model = pictures[index].url,
            contentDescription = pictures[index].caption,
            aspectRatio = 1f,
            fit = MediaFit.Whole,
            zoom = ZoomBehavior.Peek,
            onClick = { openViewer(index) },
            style = MediaFrameStyle.default(shape = ArecaTheme.shapes.none),
        )
    }
    PageIndicator(
        pageCount = state.pageCount,
        currentPage = state.currentPage,
        currentPageOffset = state.currentPageOffset,
        variant = PageIndicatorVariant.Shrinking(),
        style = PageIndicatorStyle.onImage(),
        modifier = Modifier.align(Alignment.BottomCenter).padding(12.dp),
    )
}
ThumbnailStrip(
    count = pictures.size,
    selectedIndex = state.currentItem,
    onSelect = { state.goToItem(it) },
) { index -> MediaFrame(model = pictures[index].thumbnailUrl, contentDescription = null) }
```

| `pageSize` | Looks like |
| --- | --- |
| `CarouselPageSize.Fill` | One picture across the width |
| `CarouselPageSize.Fraction(0.8f)` | The next picture peeks at the edge |
| `CarouselPageSize.PerPage(2)` | Two side by side; with `snap = CarouselSnap.Page` they move as a pair |
| `CarouselPageSize.Fixed(140.dp)` | Tiles the same width on every screen |

`contentPadding` keeps items off the screen's edges while they scroll under them; `itemSpacing` sets
the gap. `rememberCarouselState(loop = true)` wraps from the last page to the first. `autoplay =
4.seconds` advances on its own and holds while touched or hovered; feed
`CarouselState.autoplayProgress` to `PageIndicatorVariant.Segments` for story bars, and leave
autoplay off where the platform asks for reduced motion. `CarouselState` counts pages
(`pageCount`, `currentPage`) for indicators and items (`currentItem`, `goToItem`) for thumbnails and
a viewer; `goToItem(index, animated = false)` follows a viewer as it pages. `CarouselArrows(state)`,
laid over the carousel, gives a pointer previous and next.

## Thumbnails

`ThumbnailStrip` draws one square per item, rings the selected one and scrolls it to the middle.
`orientation = Orientation.Vertical` makes a rail beside a desktop picture. Each thumbnail is a tab
named by `label`; draw each picture with `fit = MediaFit.Fill`. `ThumbnailStripStyle.onDark()` and
`onLight()` suit a viewer's ground.

## Zoom

`ZoomBehavior.Sticky` zooms and stays — a viewer; one finger pans once zoomed and belongs to the
pager before that. `ZoomBehavior.Peek` zooms while the fingers are down, lifting the picture over
the screen, and springs back on release — a picture in a list. `ZoomBehavior.Hover` brings the
picture closer under a mouse pointer and follows it — a desktop product page; touch only taps.
`MediaImage` and `MediaFrame` take a `zoom`; any other content takes
`Modifier.zoomable(rememberZoomState(), contentSize = …)`. Double tap, `+`, `−` and `0` with focus,
Ctrl with the wheel, and screen-reader actions named by `ZoomLabels` all work without a pinch. `ZoomState` reads and resets the zoom from outside — a viewer resets a
page as it leaves.

Content that reads its own gestures — a column of document pages whose pan past an edge scrolls
it — drives the state itself: `Modifier.zoomControls(state, labels)` gives it the keys, wheel,
screen-reader actions and focus ring; `state.zoom(target, focus, pan)` and `state.panBy(pan)`
(which returns what the edges stopped) move it; `state.animateToggle(focus)` answers a double tap;
`state.settle()` ends a gesture. Draw the content with `state.scale` and `state.offset`.

```kotlin
val zoom = rememberZoomState(maxScale = 4f)
MediaImage(
    model = picture.url,
    contentDescription = picture.caption,
    zoom = ZoomBehavior.Sticky,
    zoomState = zoom,
    onTap = { chromeVisible = !chromeVisible },
    style = MediaFrameStyle.bare(),
)
```

`MediaImage(failure = { retry -> … })` draws a page of your own in place of the failed face — the
file's name and an "Open in another app" button for a picture this platform cannot decode.

## Hotspots

A frame's `pictureOverlay` lays elements on points of the picture itself — `Modifier.pinned(x, y)`,
each from `0f` to `1f` across and down the picture — so a marker stays on its chair whatever the
frame's shape and follows the picture as it zooms, keeping its size. `Hotspot` is the marker: a
pulsing dot, or a capsule with a number or a word; `selected` rings it.

```kotlin
MediaFrame(
    model = look.url,
    contentDescription = look.caption,
    aspectRatio = 3f / 2f,
    pictureOverlay = {
        look.products.forEach { product ->
            Hotspot(
                onClick = { selected = product },
                contentDescription = "${product.name}, ${product.price}",
                modifier = Modifier.pinned(product.x, product.y),
                selected = selected == product,
            )
        }
    },
)
```

## Viewer

`MediaViewer` is a window of its own over the screen: a single picture with one item, a gallery
with many. It owns the ground, the pager, zoom, the swipe down that closes it and the transition;
the bars are yours, drawn in `topBar` and `footer` over a scrim, each handed the `MediaViewerState`
(`currentIndex`, `itemCount`, `goTo`, `dismiss`, `isChromeVisible`). The top bar is foundation's
`TopBar` in `style.topBar()`, with `CloseAction` and the named actions — `ShareAction`,
`DownloadAction`, `FavoriteAction`, `OverflowAction`. A tap hides the bars
(`MediaViewerChrome.TapToToggle`); `bars = MediaViewerBars.Docked` sets them above and below the
pictures instead, for documents read to their edges. `onIndexSettle` reports the page the viewer
comes to rest on — fetch there, not on every page swiped past. `MediaViewerStyle.dark()` puts
pictures on black; `MediaViewerStyle.light()` on white, turning black as the bars hide. `notice`
shows what came of an action, and `sheets` draws above the viewer inside its window. Captions,
author lines and a gallery's action are in [flow/gallery](../../flow/gallery/README.md).

```kotlin
val transition = rememberMediaTransitionState()
var open by remember { mutableStateOf<Int?>(null) }

Carousel(state) { index ->
    MediaFrame(
        model = pictures[index].url,
        contentDescription = pictures[index].caption,
        onClick = { open = index },
        transition = transition,
        transitionKey = pictures[index].id,
    )
}

open?.let { start ->
    MediaViewer(
        itemCount = pictures.size,
        onDismiss = { open = null },
        initialIndex = start,
        transition = transition,
        key = { pictures[it].id },
        onIndexChange = { state.goToItem(it, animated = false) },
        topBar = { viewer ->
            TopBar(
                title = "${viewer.currentIndex + 1} of ${viewer.itemCount}",
                style = MediaViewerStyle.dark().topBar(),
                titleAlignment = TopBarTitleAlignment.Center,
                leading = { CloseAction(onClick = viewer::dismiss) },
                trailing = { ShareAction(onClick = { share(pictures[viewer.currentIndex]) }) },
            )
        },
        footer = { viewer -> Text(pictures[viewer.currentIndex].caption) },
    ) { index ->
        MediaViewerImage(
            model = pictures[index].url,
            contentDescription = pictures[index].caption,
            placeholder = MediaPlaceholder.Blurred(pictures[index].thumbnailUrl),
        )
    }
}
```

With a `transition`, the picture grows out of the frame keyed `transitionKey` and shrinks back into
the frame of the picture on screen as the viewer closes; keep that frame on screen through
`onIndexChange`. Frames drawn without `MediaFrame` take `Modifier.mediaTransitionSource(state, key)`.
Without a transition the viewer fades.

## Compare

Each comparison takes the two pictures as slots; draw both the same way —
`MediaImage(model, …, fit = MediaFit.Fill, style = MediaFrameStyle.bare())` — so their parts line
up. `CompareLabels` words them ("Before", "After" by default; "Your photo", "In our catalogue" for a
pair).

```kotlin
ComparePair(
    first = { MediaImage(scan.photo, "Your photo", fit = MediaFit.Fill, style = MediaFrameStyle.bare()) },
    second = { MediaImage(product.pictureUrl, product.name, style = MediaFrameStyle.bare()) },
    labels = CompareLabels(first = "Your photo", second = "In our catalogue"),
    onOpen = { index -> openViewer(listOf(scan.photo, product.pictureUrl), index) },
)

CompareSlider(
    before = { MediaImage(before, null, fit = MediaFit.Fill, style = MediaFrameStyle.bare()) },
    after = { MediaImage(after, null, fit = MediaFit.Fill, style = MediaFrameStyle.bare()) },
)
```

`CompareSlider` is an adjustable value to a screen reader and moves with the arrow keys;
`CompareSideBySide` shares one zoom between both pictures; `CompareHold` is a button that switches
while pressed or while Space is held; `CompareFade` cross-fades along a track.
