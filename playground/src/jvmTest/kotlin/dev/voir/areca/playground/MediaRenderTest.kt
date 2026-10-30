package dev.voir.areca.playground

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import dev.voir.areca.flow.gallery.part.GalleryAuthor
import dev.voir.areca.flow.gallery.part.GalleryPrimaryAction
import dev.voir.areca.foundation.badge.Badge
import dev.voir.areca.foundation.badge.BadgeSize
import dev.voir.areca.foundation.badge.BadgeStyle
import dev.voir.areca.foundation.navigation.PageCounter
import dev.voir.areca.foundation.navigation.PageCounterStyle
import dev.voir.areca.foundation.navigation.PageIndicator
import dev.voir.areca.foundation.navigation.PageIndicatorStyle
import dev.voir.areca.foundation.navigation.PageIndicatorVariant
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.topbar.CloseAction
import dev.voir.areca.foundation.topbar.FavoriteAction
import dev.voir.areca.foundation.topbar.OverflowAction
import dev.voir.areca.foundation.topbar.ShareAction
import dev.voir.areca.foundation.topbar.TopBar
import dev.voir.areca.foundation.topbar.TopBarTitleAlignment
import dev.voir.areca.media.LocalMediaImageLoader
import dev.voir.areca.media.carousel.Carousel
import dev.voir.areca.media.carousel.CarouselArrows
import dev.voir.areca.media.carousel.CarouselPageSize
import dev.voir.areca.media.carousel.rememberCarouselState
import dev.voir.areca.media.compare.CompareArrangement
import dev.voir.areca.media.compare.CompareLabels
import dev.voir.areca.media.compare.ComparePair
import dev.voir.areca.media.compare.CompareSideBySide
import dev.voir.areca.media.compare.CompareSlider
import dev.voir.areca.media.image.Hotspot
import dev.voir.areca.media.image.MediaFit
import dev.voir.areca.media.image.MediaFrame
import dev.voir.areca.media.image.MediaFrameSize
import dev.voir.areca.media.image.MediaFrameStyle
import dev.voir.areca.media.image.MediaImage
import dev.voir.areca.media.image.MediaScrim
import dev.voir.areca.media.thumbnail.ThumbnailStrip
import dev.voir.areca.media.thumbnail.ThumbnailStripStyle
import dev.voir.areca.media.viewer.MediaViewer
import dev.voir.areca.media.viewer.MediaViewerImage
import dev.voir.areca.media.viewer.MediaViewerStyle
import dev.voir.areca.playground.media.SampleLoad
import dev.voir.areca.playground.media.SampleMediaImageLoader
import dev.voir.areca.playground.media.SamplePicture
import dev.voir.areca.playground.media.SampleProductPictures
import dev.voir.areca.playground.media.SampleScene
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.ArecaThemeProvider
import dev.voir.areca.window.InputMode
import java.io.File
import kotlin.test.Test
import org.jetbrains.skia.EncodedImageFormat

/** Logical width of the image. */
private const val WIDTH = 390

/** Logical height of the image. */
private const val HEIGHT = 1400

/** Pixel density the image is rasterised at. */
private const val DENSITY = 2f

/** Time between the frames stepped through, in nanoseconds. */
private const val FRAME_NANOS = 50_000_000L

/** How long the clock runs before the kept frame. */
private const val SETTLE_NANOS = 1_500_000_000L

/** Where the rendered images are written. */
private val OUTPUT = File(System.getProperty("render.out") ?: "build/screens")

/**
 * Renders one composition to a PNG, with sample pictures that load at once.
 *
 * @param name File name, without the extension.
 * @param dark Whether to render in the dark theme.
 * @param content Composition to render.
 */
@OptIn(ExperimentalComposeUiApi::class)
private fun render(name: String, dark: Boolean, content: @Composable () -> Unit) {
    val scene = ImageComposeScene(
        width = (WIDTH * DENSITY).toInt(),
        height = (HEIGHT * DENSITY).toInt(),
        density = Density(DENSITY),
    ) {
        ArecaThemeProvider(darkTheme = dark, inputMode = InputMode.Touch) {
            CompositionLocalProvider(LocalMediaImageLoader provides SampleMediaImageLoader(waitMillis = 0)) {
                Box(Modifier.fillMaxSize().background(ArecaTheme.colors.bgCanvas)) { content() }
            }
        }
    }

    try {
        var time = 0L
        while (time < SETTLE_NANOS) {
            scene.render(time)
            time += FRAME_NANOS
        }
        val data = requireNotNull(scene.render(SETTLE_NANOS).encodeToData(EncodedImageFormat.PNG))
        OUTPUT.mkdirs()
        File(OUTPUT, "$name.png").writeBytes(data.bytes)
    } finally {
        scene.close()
    }
}

/**
 * Renders `:ui:media`'s frames, carousels and thumbnail strip to `build/screens`.
 *
 * Not an assertion, like the other render tests: it fails when a configuration throws during
 * composition or layout, and it is how they are looked at on a machine with no display.
 *
 * ```bash
 * ./gradlew :playground:jvmTest --tests '*MediaRenderTest*'
 * ```
 */
class MediaRenderTest {

    /** A product hero, a peeking row, frame faces and labels, and a strip, in both themes. */
    @Test
    fun `media parts render`() {
        render("media", dark = false) { Media() }
        render("media-dark", dark = true) { Media() }
    }

    /** Hotspots on a picture, and each comparison. */
    @Test
    fun `hotspots and comparisons render`() {
        render("media-compare", dark = false) { Comparisons() }
    }

    /** The viewer open on a picture, with the gallery flow's bar, caption and action. */
    @Test
    fun `gallery viewer renders`() {
        render("gallery-viewer", dark = false) { GalleryViewer() }
    }

    /** The viewer open on a picture, with plain bars and a thumbnail strip. */
    @Test
    fun `viewer renders`() {
        render("media-viewer", dark = false) { Viewer() }
    }
}

/** Hotspots, then a pair, a divider, side by side and hold. */
@Composable
private fun Comparisons() {
    val before = SampleProductPictures[4].copy(id = 900, hue = 220f)
    val after = SampleProductPictures[4]
    val fill: @Composable (SamplePicture) -> Unit = { picture ->
        MediaImage(model = picture, contentDescription = null, fit = MediaFit.Fill, style = MediaFrameStyle.bare())
    }
    Column(
        modifier = Modifier.padding(ArecaTheme.spacing.xl),
        verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xl),
    ) {
        MediaFrame(
            model = after,
            contentDescription = null,
            aspectRatio = 3f / 2f,
            style = MediaFrameStyle.default(shape = ArecaTheme.shapes.lg),
            pictureOverlay = {
                Hotspot(onClick = {}, contentDescription = "Sun", modifier = Modifier.pinned(0.72f, 0.3f))
                Hotspot(onClick = {}, contentDescription = "Hill", text = "2", selected = true, modifier = Modifier.pinned(0.3f, 0.66f))
            },
        )
        ComparePair(
            first = { fill(SampleProductPictures[3]) },
            second = { fill(SampleProductPictures[0]) },
            arrangement = CompareArrangement.Row,
            labels = CompareLabels("Your photo", "Ours"),
            captions = CompareLabels("Taken today", "Runner 2"),
        )
        CompareSlider(before = { fill(before) }, after = { fill(after) }, initialPosition = 0.4f)
        CompareSideBySide(before = { fill(before) }, after = { fill(after) }, aspectRatio = 1f)
    }
}

/** The viewer open on the second picture. */
@Composable
private fun Viewer() {
    MediaViewer(
        itemCount = SampleProductPictures.size,
        onDismiss = {},
        initialIndex = 1,
        topBar = { state ->
            Text(
                "${state.currentIndex + 1} of ${state.itemCount}",
                modifier = Modifier.fillMaxWidth().padding(ArecaTheme.spacing.xl),
                style = ArecaTheme.typography.labelLarge,
            )
        },
        footer = { state ->
            ThumbnailStrip(
                count = state.itemCount,
                selectedIndex = state.currentIndex,
                onSelect = {},
                contentPadding = PaddingValues(horizontal = ArecaTheme.spacing.xl),
                style = ThumbnailStripStyle.onDark(),
            ) { index -> MediaFrame(model = SampleProductPictures[index], contentDescription = null, fit = MediaFit.Fill) }
        },
    ) { index -> MediaViewerImage(model = SampleProductPictures[index], contentDescription = null) }
}

/** The parts, top to bottom. */
@Composable
private fun Media() {
    Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xl)) {
        Hero()
        PeekRow()
        Faces()
        Strip()
    }
}

/** A full-width hero with dots, a counter and arrows over it. */
@Composable
private fun Hero() {
    val state = rememberCarouselState(itemCount = SampleProductPictures.size, initialItem = 1)
    Box {
        Carousel(state = state) { index -> Product(SampleProductPictures[index], aspect = 1f, rounded = false) }
        CarouselArrows(state, modifier = Modifier.align(Alignment.Center))
        PageIndicator(
            pageCount = state.pageCount,
            currentPage = state.currentPage,
            variant = PageIndicatorVariant.Dots,
            style = PageIndicatorStyle.onImage(),
            modifier = Modifier.align(Alignment.BottomCenter).padding(ArecaTheme.spacing.lg),
        )
        PageCounter(
            currentPage = state.currentPage,
            pageCount = state.pageCount,
            style = PageCounterStyle.onImage(),
            modifier = Modifier.align(Alignment.TopEnd).padding(ArecaTheme.spacing.md),
        )
    }
}

/** A row of rounded pictures, the next one peeking in. */
@Composable
private fun PeekRow() {
    val state = rememberCarouselState(itemCount = SampleProductPictures.size)
    Carousel(
        state = state,
        pageSize = CarouselPageSize.Fraction(0.7f),
        itemSpacing = ArecaTheme.spacing.md,
        contentPadding = PaddingValues(horizontal = ArecaTheme.spacing.xl),
    ) { index -> Product(SampleProductPictures[index + 3 - 3], aspect = 4f / 5f, rounded = true) }
}

/** Loading, slow, failed and labelled frames side by side. */
@Composable
private fun Faces() {
    Row(
        modifier = Modifier.padding(horizontal = ArecaTheme.spacing.xl),
        horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
    ) {
        Product(SampleProductPictures[0].copy(load = SampleLoad.Hangs), aspect = 1f, rounded = true, width = 110)
        Product(SampleProductPictures[0].copy(load = SampleLoad.Fails), aspect = 1f, rounded = true, width = 110)
        MediaFrame(
            model = SampleProductPictures[6],
            contentDescription = null,
            modifier = Modifier.width(110.dp),
            aspectRatio = 1f,
            fit = MediaFit.Whole,
            size = MediaFrameSize.Product,
        ) {
            Badge(Modifier.align(Alignment.TopStart).padding(ArecaTheme.spacing.sm), style = BadgeStyle.Danger, size = BadgeSize.Small) { Text("−20%") }
        }
    }
    Row(Modifier.padding(horizontal = ArecaTheme.spacing.xl)) {
        MediaFrame(
            model = SampleProductPictures[0].copy(load = SampleLoad.Fails),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            aspectRatio = 2f,
            onClick = {},
        )
    }
    Row(Modifier.padding(horizontal = ArecaTheme.spacing.xl)) {
        MediaFrame(
            model = SampleProductPictures[4],
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            aspectRatio = 2f,
            scrim = MediaScrim.Bottom,
            style = MediaFrameStyle.default(shape = ArecaTheme.shapes.lg),
        ) {
            Text("Autumn edit", Modifier.align(Alignment.BottomStart).padding(ArecaTheme.spacing.lg), style = ArecaTheme.typography.labelLarge, color = ArecaTheme.baseColors.baseWhite)
        }
    }
}

/** A thumbnail strip, the third selected. */
@Composable
private fun Strip() {
    ThumbnailStrip(
        count = SampleProductPictures.size,
        selectedIndex = 2,
        onSelect = {},
        contentPadding = PaddingValues(horizontal = ArecaTheme.spacing.xl),
    ) { index -> MediaFrame(model = SampleProductPictures[index], contentDescription = null, fit = MediaFit.Fill) }
}

/**
 * A sample product in a frame.
 *
 * @param picture The picture.
 * @param aspect Width over height.
 * @param rounded Whether the corners round.
 * @param width Fixed width in dp, or `null` to fill.
 */
@Composable
private fun Product(picture: SamplePicture, aspect: Float, rounded: Boolean, width: Int? = null) {
    MediaFrame(
        model = picture,
        contentDescription = picture.title,
        modifier = if (width != null) Modifier.width(width.dp) else Modifier,
        aspectRatio = aspect,
        fit = if (picture.scene == SampleScene.Product) MediaFit.Whole else MediaFit.Fill,
        style = MediaFrameStyle.default(shape = if (rounded) ArecaTheme.shapes.lg else ArecaTheme.shapes.none),
        size = MediaFrameSize.Product,
    )
}

/** The viewer with flow-gallery's parts in its bars. */
@Composable
private fun GalleryViewer() {
    MediaViewer(
        itemCount = SampleProductPictures.size,
        onDismiss = {},
        initialIndex = 2,
        topBar = { state ->
            TopBar(
                title = "${state.currentIndex + 1} of ${state.itemCount}",
                style = MediaViewerStyle.dark().topBar(),
                titleAlignment = TopBarTitleAlignment.Center,
                leading = { CloseAction(onClick = state::dismiss) },
                trailing = {
                    ShareAction(onClick = {})
                    FavoriteAction(selected = true, onClick = {})
                    OverflowAction(onClick = {})
                },
            )
        },
        footer = { state ->
            Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg)) {
                GalleryAuthor(name = "Photo by Mara K.", letters = "M", detail = "12 October 2026", caption = "Ten miles in.")
                GalleryPrimaryAction(text = "Add to bag · $129", onClick = {})
            }
        },
    ) { index -> MediaViewerImage(model = SampleProductPictures[index], contentDescription = null) }
}
