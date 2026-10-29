# Gallery flow

Parts and recipes for product galleries: captions and an author line under a picture, the
gallery's one action, and a grid page's group header and tile — worded pieces laid on
[Areca media](../../ui/media/README.md)'s carousel, viewer and compare parts. A viewer's top bar is
foundation's `TopBar` in `MediaViewerStyle.topBar()`, with its named actions.

Like every flow module it ships parts, not screens ([FLOWS.md](../FLOWS.md)): the recipes below are
the screens, to copy and edit, each a compiled page in the playground's **Gallery flow** group:
product page, product cards, rows, grid page, review photo, shop the look, photo match and the
desktop product page.

## Install

```kotlin
implementation("dev.voir.areca:flow-gallery:<version>")
```

It passes `foundation` and `media` on (`api`). Provide a
[`MediaImageLoader`](../../ui/media/README.md#provide-a-loader) above the screens.

## Parts

| Part | Package | Use it for |
| --- | --- | --- |
| `GalleryCaption` | `part` | What a picture shows, under it: a quiet kind, its title, a line |
| `GalleryAuthor` | `part` | Whose photo it is: avatar, name, a date, what they wrote |
| `GalleryPrimaryAction` | `part` | The one thing the gallery leads to — "Add to bag · $129" |
| `GalleryGroupHeader` | `grid` | A group's heading on a grid page, with its count |
| `GalleryTile` | `grid` | One square of a grid page, a reader's photo labelled with whose it is |

The foot parts take the viewer's content colour (`LocalContentColor`, which
`MediaViewer` sets from its style), so they read on a dark or a light viewer alike. Every word is a
parameter with an English default where the part cannot do without it.

## Recipes

### Product page

A hero carousel whose pictures open a detail gallery, the picture growing out of the hero and the
hero landing on the last picture seen. Playground: **Gallery flow › Product page**.

```kotlin
val hero = rememberCarouselState(itemCount = pictures.size)
val transition = rememberMediaTransitionState()
var open by remember { mutableStateOf<Int?>(null) }

Box {
    Carousel(state = hero, contentDescription = "${product.name} pictures") { index ->
        MediaFrame(
            model = pictures[index].url,
            contentDescription = pictures[index].caption,
            aspectRatio = 1f,
            fit = MediaFit.Whole,
            zoom = ZoomBehavior.Peek,
            onClick = { open = index },
            onClickLabel = "Open pictures",
            transition = transition,
            transitionKey = pictures[index].id,
            style = MediaFrameStyle.default(shape = ArecaTheme.shapes.none),
            size = MediaFrameSize.Product,
        )
    }
    PageIndicator(
        pageCount = hero.pageCount,
        currentPage = hero.currentPage,
        currentPageOffset = hero.currentPageOffset,
        variant = PageIndicatorVariant.Dots,
        style = PageIndicatorStyle.onImage(),
        modifier = Modifier.align(Alignment.BottomCenter).padding(ArecaTheme.spacing.lg),
    )
}

open?.let { start ->
    MediaViewer(
        itemCount = pictures.size,
        onDismiss = { open = null },
        initialIndex = start,
        transition = transition,
        key = { pictures[it].id },
        onIndexChange = { hero.goToItem(it, animated = false) },
        topBar = { viewer ->
            TopBar(
                title = "${viewer.currentIndex + 1} of ${viewer.itemCount}",
                style = MediaViewerStyle.dark().topBar(),
                titleAlignment = TopBarTitleAlignment.Center,
                leading = { CloseAction(onClick = viewer::dismiss) },
                trailing = {
                    ShareAction(onClick = { share(pictures[viewer.currentIndex]) })
                    FavoriteAction(selected = favourite, onClick = onFavourite)
                },
            )
        },
        footer = { viewer ->
            Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg)) {
                GalleryCaption(title = pictures[viewer.currentIndex].caption, kicker = "From the brand")
                ThumbnailStrip(
                    count = viewer.itemCount,
                    selectedIndex = viewer.currentIndex,
                    onSelect = viewer::goTo,
                    contentPadding = PaddingValues(horizontal = ArecaTheme.spacing.horizontalContentInsets),
                    style = ThumbnailStripStyle.onDark(),
                ) { index -> MediaFrame(model = pictures[index].thumbnailUrl, contentDescription = null) }
                GalleryPrimaryAction(text = "Add to bag · ${product.price}", onClick = onAddToBag)
            }
        },
    ) { index ->
        MediaViewerImage(
            model = pictures[index].url,
            contentDescription = pictures[index].caption,
            placeholder = MediaPlaceholder.Blurred(pictures[index].thumbnailUrl),
        )
    }
}
```

### Product cards

Products in a list, each card's pictures swiped on their own without opening; a tap opens that
product's gallery. Playground: **Gallery flow › Product cards**.

```kotlin
LazyVerticalGrid(columns = GridCells.Fixed(2), horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md)) {
    items(products, key = { it.id }) { product ->
        Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.sm)) {
            val state = rememberCarouselState(itemCount = product.pictures.size)
            Box {
                Carousel(state = state, contentDescription = "${product.name} pictures") { index ->
                    MediaFrame(
                        model = product.pictures[index].url,
                        contentDescription = product.pictures[index].caption,
                        aspectRatio = 4f / 5f,
                        fit = MediaFit.Whole,
                        zoom = ZoomBehavior.Peek,
                        onClick = { open(product, index) },
                        onClickLabel = "Open ${product.name}",
                    )
                }
                product.badge?.let { word ->
                    Badge(Modifier.align(Alignment.TopStart).padding(ArecaTheme.spacing.sm), style = BadgeStyle.Danger, size = BadgeSize.Small) {
                        Text(word)
                    }
                }
                PageIndicator(
                    pageCount = state.pageCount,
                    currentPage = state.currentPage,
                    currentPageOffset = state.currentPageOffset,
                    variant = PageIndicatorVariant.Dots,
                    style = PageIndicatorStyle.onImage(),
                    modifier = Modifier.align(Alignment.BottomCenter).padding(ArecaTheme.spacing.sm),
                )
            }
            Text(product.name, style = ArecaTheme.typography.labelLarge)
            Text(product.price, style = ArecaTheme.typography.bodySmall)
        }
    }
}
```

### Rows

Rows on a discovery screen: scenes with the next one peeking, products two to a page moving as a
pair, round tiles of what was viewed. Playground: **Gallery flow › Rows**.

```kotlin
val gutter = PaddingValues(horizontal = ArecaTheme.spacing.horizontalContentInsets)

Carousel(rememberCarouselState(scenes.size), pageSize = CarouselPageSize.Fraction(0.8f),
    itemSpacing = ArecaTheme.spacing.md, contentPadding = gutter) { index ->
    MediaFrame(
        model = scenes[index].url,
        contentDescription = scenes[index].title,
        aspectRatio = 4f / 3f,
        scrim = MediaScrim.Bottom,
        onClick = { open(scenes, index) },
        style = MediaFrameStyle.default(shape = ArecaTheme.shapes.lg),
    ) {
        Text(scenes[index].title, Modifier.align(Alignment.BottomStart).padding(ArecaTheme.spacing.lg),
            style = ArecaTheme.typography.labelLarge, color = ArecaTheme.baseColors.baseWhite)
    }
}

val pairs = rememberCarouselState(products.size)
Carousel(pairs, pageSize = CarouselPageSize.PerPage(2), snap = CarouselSnap.Page,
    itemSpacing = ArecaTheme.spacing.md, contentPadding = gutter) { index -> /* a product frame */ }
PageIndicator(pairs.pageCount, pairs.currentPage, currentPageOffset = pairs.currentPageOffset,
    variant = PageIndicatorVariant.Line)

Carousel(rememberCarouselState(recent.size), pageSize = CarouselPageSize.Fixed(88.dp),
    itemSpacing = ArecaTheme.spacing.md, contentPadding = gutter) { index ->
    MediaFrame(model = recent[index].url, contentDescription = recent[index].name, aspectRatio = 1f,
        fit = MediaFit.Whole, onClick = { open(recent, index) }, style = MediaFrameStyle.circle())
}
```

### Review photo

A review's photos as small squares; one opens alone, with whose photo it is under it. Playground:
**Gallery flow › Review photo**.

```kotlin
Row(horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.sm)) {
    review.photos.forEachIndexed { index, photo ->
        MediaFrame(
            model = photo.url,
            contentDescription = photo.caption,
            modifier = Modifier.size(88.dp),
            onClick = { open = index },
            onClickLabel = "Open photo",
            transition = transition,
            transitionKey = photo.id,
        )
    }
}

open?.let { index ->
    val photo = review.photos[index]
    MediaViewer(
        itemCount = 1,
        onDismiss = { open = null },
        transition = transition,
        key = { photo.id },
        topBar = { viewer ->
            TopBar(title = "", style = MediaViewerStyle.dark().topBar(),
                leading = { CloseAction(onClick = viewer::dismiss) })
        },
        footer = {
            GalleryAuthor(name = "Photo by ${review.author}", letters = review.initials,
                detail = review.date, caption = photo.caption)
        },
    ) { MediaViewerImage(model = photo.url, contentDescription = photo.caption) }
}
```

### Shop the look

A picture with its products marked on it and listed under it; a marker or a row picks one, and
the picture opens full screen with the markers following its zoom. Playground:
**Gallery flow › Shop the look**.

```kotlin
val markers: @Composable MediaPictureScope.() -> Unit = {
    look.products.forEachIndexed { index, product ->
        Hotspot(
            onClick = { picked = index },
            contentDescription = "${product.name}, ${product.price}",
            modifier = Modifier.pinned(product.x, product.y),
            selected = picked == index,
        )
    }
}

MediaFrame(
    model = look.url,
    contentDescription = look.caption,
    aspectRatio = 3f / 2f,
    zoom = ZoomBehavior.Peek,
    onClick = { open = true },
    pictureOverlay = markers,
)
// In the viewer: MediaViewerImage(model = look.url, contentDescription = look.caption, pictureOverlay = markers)
```

### Desktop product page

A wide window: a thumbnail rail beside the picture, arrows for the pointer, the picture magnified
under the mouse, a click opening the gallery. Playground: **Gallery flow › Desktop product page**.

```kotlin
Row(horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xl)) {
    ThumbnailStrip(
        count = pictures.size,
        selectedIndex = state.currentItem,
        onSelect = { state.goToItem(it) },
        modifier = Modifier.width(72.dp).fillMaxHeight(),
        orientation = Orientation.Vertical,
    ) { index -> MediaFrame(model = pictures[index].thumbnailUrl, contentDescription = null) }

    Box(Modifier.weight(1f)) {
        Carousel(state = state, modifier = Modifier.fillMaxHeight()) { index ->
            MediaFrame(
                model = pictures[index].url,
                contentDescription = pictures[index].caption,
                modifier = Modifier.fillMaxHeight(),
                fit = MediaFit.Whole,
                zoom = ZoomBehavior.Hover,
                onClick = { open = index },
                transition = transition,
                transitionKey = pictures[index].id,
                size = MediaFrameSize.Product,
            )
        }
        CarouselArrows(state, modifier = Modifier.align(Alignment.Center))
    }

    ProductDetails(Modifier.width(280.dp))
}
```

### Grid page

Every picture in its group, three across; a tile opens the viewer at that picture among all of
them, with a caption under the brand's pictures and an author under readers'. Playground:
**Gallery flow › Grid page**.

```kotlin
LazyColumn {
    groups.forEach { group ->
        item(key = group.title) { GalleryGroupHeader(title = group.title, count = "${group.pictures.size}") }
        items(group.pictures.chunked(3)) { row ->
            Row(
                Modifier.padding(horizontal = ArecaTheme.spacing.horizontalContentInsets, vertical = ArecaTheme.spacing.xs),
                horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.sm),
            ) {
                row.forEach { picture ->
                    GalleryTile(
                        model = picture.url,
                        contentDescription = picture.caption,
                        onClick = { open = all.indexOf(picture) },
                        modifier = Modifier.weight(1f),
                        label = picture.author,
                        transition = transition,
                        transitionKey = picture.id,
                    )
                }
                repeat(3 - row.size) { Spacer(Modifier.weight(1f)) }
            }
        }
    }
}
// The viewer as in the product page, its footer:
footer = { viewer ->
    val picture = all[viewer.currentIndex]
    if (picture.author != null) {
        GalleryAuthor(name = "Photo by ${picture.author}", letters = picture.author.take(1), detail = picture.date)
    } else {
        GalleryCaption(title = picture.caption, kicker = picture.group)
    }
}
```

### Photo match

The user's photo next to the catalogue's, one above the other; either opens both full screen, each
zoomed on its own. Playground: **Gallery flow › Photo match**.

```kotlin
ComparePair(
    first = { MediaImage(scan.photo, "Your photo", fit = MediaFit.Fill, style = MediaFrameStyle.bare()) },
    second = { MediaImage(product.pictureUrl, product.name, style = MediaFrameStyle.bare()) },
    labels = CompareLabels(first = "Your photo", second = "In our catalogue"),
    captions = CompareLabels(first = scan.takenAt, second = product.name),
    onOpen = { open = it },
)

open?.let { start ->
    MediaViewer(
        itemCount = 2,
        onDismiss = { open = null },
        initialIndex = start,
        topBar = { viewer ->
            TopBar(
                title = titles[viewer.currentIndex],
                style = MediaViewerStyle.dark().topBar(),
                titleAlignment = TopBarTitleAlignment.Center,
                leading = { CloseAction(onClick = viewer::dismiss) },
            )
        },
    ) { index -> MediaViewerImage(model = models[index], contentDescription = titles[index]) }
}
```
