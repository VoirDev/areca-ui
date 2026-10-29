package dev.voir.areca.playground.flow.gallery

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.navigation.PageIndicator
import dev.voir.areca.foundation.navigation.PageIndicatorVariant
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.media.carousel.Carousel
import dev.voir.areca.media.carousel.CarouselPageSize
import dev.voir.areca.media.carousel.CarouselSnap
import dev.voir.areca.media.carousel.rememberCarouselState
import dev.voir.areca.media.image.MediaFit
import dev.voir.areca.media.image.MediaFrame
import dev.voir.areca.media.image.MediaFrameSize
import dev.voir.areca.media.image.MediaFrameStyle
import dev.voir.areca.media.image.MediaScrim
import dev.voir.areca.playground.media.SampleManyPictures
import dev.voir.areca.playground.media.SamplePicture
import dev.voir.areca.playground.media.SampleProductPictures
import dev.voir.areca.playground.media.SampleScene
import dev.voir.areca.playground.showcase.FlowShowcase
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.canvas.DeviceFrame
import dev.voir.areca.playground.showcase.canvas.DeviceSize
import dev.voir.areca.playground.showcase.canvas.windowInsets
import dev.voir.areca.theme.ArecaTheme

/** Screen page: rows of pictures sized three ways, each opening a gallery. */
@Composable
fun RowsShowcase() = FlowShowcase(
    title = "Rows",
    summary = "Rows of pictures on a discovery screen: the next one peeking, two side by side moving as a pair, " +
        "and round tiles; each opens a gallery.",
    module = "flow-gallery",
    status = ShowcaseStatus.Beta,
) {
    val pairs by booleanControl("pairs move one by one", default = false, description = "CarouselSnap.Item instead of Page.")

    preview {
        DeviceFrame(size = DeviceSize.Regular) {
            RowsRecipe(pairsByItem = pairs, windowInsets = DeviceSize.Regular.windowInsets)
        }
    }
}

/**
 * The recipe: a titled row of scenes peeking, a row of products two to a page, and a row of round
 * tiles.
 *
 * @param pairsByItem Whether the pairs row moves one item at a time.
 * @param windowInsets The screen's insets.
 */
@Composable
private fun RowsRecipe(pairsByItem: Boolean, windowInsets: WindowInsets) {
    var open by remember { mutableStateOf<Pair<List<SamplePicture>, Int>?>(null) }
    val scenes = SampleProductPictures.filter { it.scene != SampleScene.Product } + SampleProductPictures.take(2)
    val products = SampleManyPictures.take(8)
    val gutter = PaddingValues(horizontal = ArecaTheme.spacing.horizontalContentInsets)

    Column(
        Modifier
            .fillMaxSize()
            .background(ArecaTheme.colors.bgCanvas)
            .windowInsetsPadding(windowInsets)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
    ) {
        Text("Discover", style = ArecaTheme.typography.headingSmall, modifier = Modifier.padding(gutter))

        RowTitle("Collections")
        val collections = rememberCarouselState(itemCount = scenes.size)
        Carousel(
            state = collections,
            pageSize = CarouselPageSize.Fraction(0.8f),
            itemSpacing = ArecaTheme.spacing.md,
            contentPadding = gutter,
        ) { index ->
            MediaFrame(
                model = scenes[index],
                contentDescription = scenes[index].title,
                aspectRatio = 4f / 3f,
                fit = MediaFit.Fill,
                scrim = MediaScrim.Bottom,
                onClick = { open = scenes to index },
                style = MediaFrameStyle.default(shape = ArecaTheme.shapes.lg),
            ) {
                Text(
                    scenes[index].title,
                    modifier = Modifier.align(Alignment.BottomStart).padding(ArecaTheme.spacing.lg),
                    style = ArecaTheme.typography.labelLarge,
                    color = ArecaTheme.baseColors.baseWhite,
                )
            }
        }

        RowTitle("New in")
        val pairs = rememberCarouselState(itemCount = products.size)
        Carousel(
            state = pairs,
            pageSize = CarouselPageSize.PerPage(2),
            snap = if (pairsByItem) CarouselSnap.Item else CarouselSnap.Page,
            itemSpacing = ArecaTheme.spacing.md,
            contentPadding = gutter,
        ) { index ->
            MediaFrame(
                model = products[index],
                contentDescription = products[index].title,
                aspectRatio = 4f / 5f,
                fit = MediaFit.Whole,
                onClick = { open = products to index },
                style = MediaFrameStyle.default(shape = ArecaTheme.shapes.lg),
                size = MediaFrameSize.default(inset = ArecaTheme.spacing.lg),
            )
        }
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            PageIndicator(
                pageCount = pairs.pageCount,
                currentPage = pairs.currentPage,
                currentPageOffset = pairs.currentPageOffset,
                variant = PageIndicatorVariant.Line,
            )
        }

        RowTitle("Recently viewed")
        val recent = rememberCarouselState(itemCount = products.size)
        Carousel(
            state = recent,
            pageSize = CarouselPageSize.Fixed(88.dp),
            itemSpacing = ArecaTheme.spacing.md,
            contentPadding = gutter,
        ) { index ->
            MediaFrame(
                model = products[index],
                contentDescription = products[index].title,
                aspectRatio = 1f,
                fit = MediaFit.Whole,
                onClick = { open = products to index },
                style = MediaFrameStyle.circle(),
                size = MediaFrameSize.default(inset = ArecaTheme.spacing.md),
            )
        }
    }

    open?.let { (pictures, start) ->
        SampleGalleryViewer(pictures = pictures, start = start, onDismiss = { open = null })
    }
}

/**
 * A row's title, inside the screen's gutter.
 *
 * @param title The title.
 */
@Composable
private fun RowTitle(title: String) {
    Row(Modifier.padding(horizontal = ArecaTheme.spacing.horizontalContentInsets, vertical = ArecaTheme.spacing.sm)) {
        Text(title, style = ArecaTheme.typography.labelLarge)
    }
}
