# Gallery flow — `dev.voir.areca:flow-gallery`

Add it for product galleries: captions and author lines under pictures, the gallery's one action,
and a grid page of grouped tiles. A viewer's top bar is foundation's `TopBar` — never a bar of
your own.

- Artifact `dev.voir.areca:flow-gallery`; packages `dev.voir.areca.flow.gallery.part` and `.grid`.
- Passes `foundation` and `media` on (`api`); read [media.md](media.md) for the carousel, frames,
  viewer and compare it is laid on, and provide a `MediaImageLoader`.
- Parts, not screens: the screens are recipes — product page, product cards, rows, grid page,
  review photo, shop the look, photo match, desktop product page — in the module README and the
  playground's **Gallery flow** group. Copy the one closest to the screen being built.
- The product page, product cards and grid page recipes adapt to the window (below), with
  `AdaptiveContent`, `adaptive` and `AdaptiveGrid` — see "Lay out by window class, never by device"
  in [SKILL.md](../SKILL.md).
- Foot parts take `LocalContentColor`, which `MediaViewer` sets from its style; outside a
  viewer, provide it.

## Choose

| Need | Use |
| --- | --- |
| A viewer's top bar | `TopBar(title = "3 of 9", style = viewerStyle.topBar(), titleAlignment = TopBarTitleAlignment.Center, leading = { CloseAction(onClick = viewer::dismiss) }, trailing = { … })` in `MediaViewer(topBar = { viewer -> … })` |
| Share, save, favourite, more | foundation `ShareAction`, `DownloadAction`, `FavoriteAction(selected)`, `OverflowAction`; anything else `IconAction` |
| What a picture shows | `GalleryCaption(title, kicker = "From the brand", caption = …)` in the viewer's `footer` |
| Whose photo it is | `GalleryAuthor(name = "Photo by Mara K.", letters = "M", detail = date, caption = …)` |
| The gallery's one action | `GalleryPrimaryAction("Add to bag · $129", onClick)` |
| A grid page | `AdaptiveGrid(minCellWidth = 88.dp, maxColumns = adaptive(compact = 3, medium = 4, expanded = 6), horizontalSpacing = spacing.sm, verticalSpacing = spacing.none)` with a full-line `GalleryGroupHeader(title, count = "12", horizontalPadding = 0.dp)` per group and a `GalleryTile(model, contentDescription, onClick, Modifier.padding(vertical = spacing.xs), label = author, transition, transitionKey)` per picture |
| Product cards | `AdaptiveGrid(minCellWidth = 140.dp, maxColumns = adaptive(compact = 2, medium = 3, expanded = 4))` of cards, each a `Carousel` of `MediaFrame`s |
| A product page | `AdaptiveContent(compact = { ProductPage() }, expanded = { DesktopProductPage() })` — the README's two recipes |

## Signatures

```kotlin
@Composable fun GalleryCaption(title: String, modifier: Modifier = Modifier, kicker: String? = null, caption: String? = null)
@Composable fun GalleryAuthor(name: String, modifier: Modifier = Modifier, letters: String? = null,
    detail: String? = null, caption: String? = null, avatar: (@Composable () -> Unit)? = null)
@Composable fun GalleryPrimaryAction(text: String, onClick: () -> Unit, modifier: Modifier = Modifier,
    icon: ImageVector? = null, enabled: Boolean = true)

@Composable fun GalleryGroupHeader(title: String, modifier: Modifier = Modifier, count: String? = null,
    horizontalPadding: Dp = ArecaTheme.spacing.horizontalContentInsets)
@Composable fun GalleryTile(model: Any?, contentDescription: String?, onClick: () -> Unit,
    modifier: Modifier = Modifier, label: String? = null, fit: MediaFit = MediaFit.Whole,
    onClickLabel: String = "Open picture", transition: MediaTransitionState? = null, transitionKey: Any? = null)
```

## Rules

- Close the viewer with `viewer::dismiss` from the bar's `CloseAction`, so the picture shrinks back
  into its frame.
- Word everything in the app's language: titles, counts, captions, author lines and action labels
  are parameters.
- One `GalleryPrimaryAction` per gallery; leave it out where the gallery leads nowhere.

## On a tablet or desktop

- **Product page:** the phone page below 840dp, the desktop product page from 840dp. A medium
  window (unfolded foldable, tablet upright) keeps the phone page — touch-first, and the desktop
  arrangement would leave the picture smaller there — with the hero at
  `adaptive(compact = 1f, medium = 4f / 3f)`. Compact and medium share one lambda, so unfolding keeps
  the hero's position.
- **Desktop product page:** inside `ContentColumn(maxWidth = AdaptiveDefaults.ContentWidth)`.
  `CarouselArrows` and `ZoomBehavior.Hover` only when `ArecaTheme.window.input == InputMode.Pointer`;
  `ZoomBehavior.Peek` and no arrows under touch.
- **Grid page and product cards:** `AdaptiveGrid`, which keeps the gutter itself — give a header
  spanning a line `GalleryGroupHeader(horizontalPadding = 0.dp)`. On a phone they lay out exactly
  as a fixed three (tiles) or two (cards) across did.
- **Viewer:** `MediaViewer` fills the window at every size — no list-and-detail split for a
  gallery. Keep its footer to a readable width:
  `Column(Modifier.widthIn(max = AdaptiveDefaults.ReadableWidth).align(Alignment.CenterHorizontally))`.
