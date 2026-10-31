package dev.voir.areca.playground.flow.gallery

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.voir.areca.flow.gallery.part.GalleryCaption
import dev.voir.areca.flow.gallery.part.GalleryPrimaryAction
import dev.voir.areca.foundation.adaptive.AdaptiveDefaults
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.navigation.PageCounter
import dev.voir.areca.foundation.navigation.PageCounterStyle
import dev.voir.areca.foundation.navigation.PageIndicator
import dev.voir.areca.foundation.navigation.PageIndicatorStyle
import dev.voir.areca.foundation.navigation.PageIndicatorVariant
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.topbar.CloseAction
import dev.voir.areca.foundation.topbar.FavoriteAction
import dev.voir.areca.foundation.topbar.ShareAction
import dev.voir.areca.foundation.topbar.TopBar
import dev.voir.areca.foundation.topbar.TopBarTitleAlignment
import dev.voir.areca.media.carousel.Carousel
import dev.voir.areca.media.carousel.rememberCarouselState
import dev.voir.areca.media.image.MediaFit
import dev.voir.areca.media.image.MediaFrame
import dev.voir.areca.media.image.MediaFrameSize
import dev.voir.areca.media.image.MediaFrameStyle
import dev.voir.areca.media.image.MediaPlaceholder
import dev.voir.areca.media.thumbnail.ThumbnailStrip
import dev.voir.areca.media.thumbnail.ThumbnailStripStyle
import dev.voir.areca.media.viewer.MediaViewer
import dev.voir.areca.media.viewer.MediaViewerImage
import dev.voir.areca.media.viewer.MediaViewerStyle
import dev.voir.areca.media.viewer.rememberMediaTransitionState
import dev.voir.areca.media.zoom.ZoomBehavior
import dev.voir.areca.playground.media.SampleManyPictures
import dev.voir.areca.playground.media.SamplePicture
import dev.voir.areca.playground.media.SampleProductPictures
import dev.voir.areca.playground.media.SampleScene
import dev.voir.areca.playground.showcase.FlowShowcase
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.canvas.WindowPreset
import dev.voir.areca.playground.showcase.canvas.windowInsets
import dev.voir.areca.playground.showcase.chrome.ChromeText
import dev.voir.areca.playground.showcase.chrome.ChromeTextRole
import dev.voir.areca.playground.showcase.chrome.ChromeTone
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.horizontalContentInsets
import dev.voir.areca.window.AdaptiveContent
import dev.voir.areca.window.adaptive

/** Screen page: a product page whose pictures open a detail gallery, at every window. */
@Composable
fun ProductPageShowcase() = FlowShowcase(
    title = "Product page",
    summary = "A product's pictures in a hero carousel that opens a detail gallery, the picture growing out of the hero " +
        "and the hero landing on the last picture seen; from 840dp, the desktop product page.",
    module = "flow-gallery",
    status = ShowcaseStatus.Beta,
    notes = {
        ChromeText(text = "On a tablet or desktop", role = ChromeTextRole.Label)
        ChromeText(
            text = "AdaptiveProductPageRecipe is the assembly to copy: the phone page under 840dp and the desktop " +
                "product page from 840dp — a tablet on its side, a phone on its side, a laptop. An unfolded " +
                "foldable or a tablet held upright keeps the phone page, touch-first, with its hero turned " +
                "landscape so the price and the button stay above the fold; the desktop arrangement would leave " +
                "the picture smaller there than on the phone. Because the two narrow steps share one layout, " +
                "unfolding keeps the hero on the picture it was on.",
            role = ChromeTextRole.SectionDescription,
            tone = ChromeTone.Muted,
        )
    },
) {
    val preset by enumControl(
        key = "window",
        default = WindowPreset.Phone,
        options = WindowPreset.entries,
        optionLabel = { it.label },
    )
    val many by booleanControl("24 pictures", default = false)
    val light by booleanControl("light viewer", default = false)

    preview {
        GalleryWindowFrame(preset) {
            AdaptiveProductPageRecipe(
                pictures = if (many) SampleManyPictures else SampleProductPictures,
                lightViewer = light,
                windowInsets = preset.windowInsets,
            )
        }
    }
}

/**
 * The recipe to copy: the phone product page, or from an expanded window the desktop one.
 *
 * @param pictures The product's pictures.
 * @param lightViewer Whether the gallery stands the pictures on white.
 * @param windowInsets The screen's insets.
 */
@Composable
private fun AdaptiveProductPageRecipe(pictures: List<SamplePicture>, lightViewer: Boolean, windowInsets: WindowInsets) {
    AdaptiveContent(
        compact = { ProductPageRecipe(pictures, lightViewer, windowInsets) },
        expanded = { DesktopProductRecipe(pictures, lightViewer, windowInsets) },
    )
}

/**
 * The phone page: a product page's hero carousel, dots and a counter over it, opening a gallery with
 * a bar, a caption, thumbnails and the page's one action. Also drawn on a medium window, where the
 * hero turns landscape and the foot of the gallery stops at a readable width.
 *
 * @param pictures The product's pictures.
 * @param lightViewer Whether the gallery stands the pictures on white.
 * @param windowInsets The screen's insets.
 */
@Composable
private fun ProductPageRecipe(pictures: List<SamplePicture>, lightViewer: Boolean, windowInsets: WindowInsets) {
    val hero = rememberCarouselState(itemCount = pictures.size)
    val transition = rememberMediaTransitionState()
    var open by remember { mutableStateOf<Int?>(null) }
    var favourite by remember { mutableStateOf(false) }
    var notice by remember { mutableStateOf<String?>(null) }

    Column(
        Modifier
            .fillMaxSize()
            .background(ArecaTheme.colors.bgCanvas)
            .verticalScroll(rememberScrollState()),
    ) {
        Box {
            Carousel(state = hero, contentDescription = "Runner 2 pictures") { index ->
                val picture = pictures[index]
                MediaFrame(
                    model = picture,
                    contentDescription = picture.title,
                    aspectRatio = adaptive(compact = 1f, medium = 4f / 3f),
                    fit = if (picture.scene == SampleScene.Product) MediaFit.Whole else MediaFit.Fill,
                    zoom = ZoomBehavior.Peek,
                    onClick = { open = index },
                    onClickLabel = "Open pictures",
                    transition = transition,
                    transitionKey = picture.id,
                    style = MediaFrameStyle.default(shape = ArecaTheme.shapes.none),
                    size = MediaFrameSize.Product,
                )
            }
            PageIndicator(
                pageCount = hero.pageCount,
                currentPage = hero.currentPage,
                currentPageOffset = hero.currentPageOffset,
                variant = if (pictures.size > 7) PageIndicatorVariant.Shrinking() else PageIndicatorVariant.Dots,
                contentDescription = "Picture ${hero.currentItem + 1} of ${pictures.size}",
                style = PageIndicatorStyle.onImage(),
                modifier = Modifier.align(Alignment.BottomCenter).padding(ArecaTheme.spacing.lg),
            )
            PageCounter(
                currentPage = hero.currentPage,
                pageCount = hero.pageCount,
                style = PageCounterStyle.onImage(),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .windowInsetsPadding(windowInsets)
                    .padding(ArecaTheme.spacing.md),
            )
        }
        Column(
            Modifier.horizontalContentInsets().padding(vertical = ArecaTheme.spacing.xl),
            verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
        ) {
            Text("Northbound", style = ArecaTheme.typography.bodySmall, color = ArecaTheme.colors.fgMuted)
            Text("Runner 2", style = ArecaTheme.typography.headingSmall)
            Text("$129", style = ArecaTheme.typography.labelLarge)
            Button("Add to bag", onClick = {}, modifier = Modifier.fillMaxWidth())
        }
    }

    val viewerStyle = if (lightViewer) MediaViewerStyle.light() else MediaViewerStyle.dark()
    open?.let { start ->
        MediaViewer(
            itemCount = pictures.size,
            onDismiss = { open = null },
            initialIndex = start,
            transition = transition,
            key = { index -> pictures[index].id },
            onIndexChange = { index -> hero.goToItem(index, animated = false) },
            notice = notice,
            onNoticeDismiss = { notice = null },
            style = viewerStyle,
            topBar = { viewer ->
                TopBar(
                    title = "${viewer.currentIndex + 1} of ${viewer.itemCount}",
                    style = viewerStyle.topBar(),
                    titleAlignment = TopBarTitleAlignment.Center,
                    leading = { CloseAction(onClick = viewer::dismiss) },
                    trailing = {
                        ShareAction(onClick = { notice = "Link copied" })
                        FavoriteAction(selected = favourite, onClick = { favourite = !favourite })
                    },
                )
            },
            footer = { viewer ->
                Column(
                    Modifier.widthIn(max = AdaptiveDefaults.ReadableWidth).align(Alignment.CenterHorizontally),
                    verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg),
                ) {
                    GalleryCaption(title = pictures[viewer.currentIndex].title, kicker = "From the brand")
                    ThumbnailStrip(
                        count = viewer.itemCount,
                        selectedIndex = viewer.currentIndex,
                        onSelect = { viewer.goTo(it) },
                        contentPadding = PaddingValues(horizontal = ArecaTheme.spacing.horizontalContentInsets),
                        label = { index, total -> "${pictures[index].title}, ${index + 1} of $total" },
                        style = if (lightViewer) ThumbnailStripStyle.onLight() else ThumbnailStripStyle.onDark(),
                    ) { index ->
                        MediaFrame(model = pictures[index], contentDescription = null, fit = MediaFit.Fill)
                    }
                    GalleryPrimaryAction(text = "Add to bag · $129", onClick = { notice = "Added to your bag" })
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
}
