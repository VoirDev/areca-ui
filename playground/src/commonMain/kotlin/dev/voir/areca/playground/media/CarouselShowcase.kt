package dev.voir.areca.playground.media

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.navigation.PageCounter
import dev.voir.areca.foundation.navigation.PageCounterStyle
import dev.voir.areca.foundation.navigation.PageIndicator
import dev.voir.areca.foundation.navigation.PageIndicatorStyle
import dev.voir.areca.foundation.navigation.PageIndicatorVariant
import dev.voir.areca.media.carousel.Carousel
import dev.voir.areca.media.carousel.CarouselArrows
import dev.voir.areca.media.carousel.CarouselPageSize
import dev.voir.areca.media.carousel.CarouselSnap
import dev.voir.areca.media.carousel.CarouselState
import dev.voir.areca.media.carousel.rememberCarouselState
import dev.voir.areca.media.image.MediaFit
import dev.voir.areca.media.image.MediaFrame
import dev.voir.areca.media.image.MediaFrameSize
import dev.voir.areca.media.image.MediaFrameStyle
import dev.voir.areca.media.thumbnail.ThumbnailStrip
import dev.voir.areca.media.zoom.ZoomBehavior
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme
import kotlin.time.Duration.Companion.seconds

/** Page sizes a page offers. */
private enum class PageSizeOption(val size: CarouselPageSize) {
    /** [CarouselPageSize.Fill]. */
    Fill(CarouselPageSize.Fill),

    /** [CarouselPageSize.Fraction] of 0.8. */
    Peek(CarouselPageSize.Fraction(0.8f)),

    /** [CarouselPageSize.PerPage] of two. */
    TwoUp(CarouselPageSize.PerPage(2)),

    /** [CarouselPageSize.Fixed] at 140 dp. */
    Tiles(CarouselPageSize.Fixed(140.dp)),
}

/** Indicators a page offers, by variant, or a counter. */
private enum class IndicatorOption {
    /** [PageIndicatorVariant.Dots]. */
    Dots,

    /** [PageIndicatorVariant.Shrinking]. */
    Shrinking,

    /** [PageIndicatorVariant.Line]. */
    Line,

    /** [PageIndicatorVariant.Segments], filled by autoplay. */
    Segments,

    /** [PageCounter]. */
    Counter,

    /** Nothing. */
    None,
}

/** Showcase for [Carousel], [CarouselArrows] and [CarouselState]. */
@Composable
fun CarouselShowcase() = ShowcaseScaffold(
    title = "Carousel",
    summary = "Pictures swiped sideways inside a screen: one at a time, peeking, or several moving one by one or as a group.",
    module = "media",
    status = ShowcaseStatus.Beta,
) {
    Playground(description = "Indicators, arrows and thumbnails all follow and drive the same CarouselState.") {
        val pageSize by enumControl("page size", PageSizeOption.Fill, PageSizeOption.entries, optionLabel = { it.name })
        val snap by enumControl("snap", CarouselSnap.Item, CarouselSnap.entries, optionLabel = { it.name })
        val indicator by enumControl("indicator", IndicatorOption.Dots, IndicatorOption.entries, optionLabel = { it.name })
        val many by booleanControl("24 pictures", default = false)
        val loop by booleanControl("loop", default = false)
        val autoplay by booleanControl("autoplay", default = false)
        val arrows by booleanControl("arrows", default = false)
        val thumbnails by booleanControl("thumbnails", default = false)

        preview {
            val pictures = if (many) SampleManyPictures else SampleProductPictures
            val state = rememberCarouselState(itemCount = pictures.size, loop = loop)
            val fill = pageSize.size == CarouselPageSize.Fill

            Column(Modifier.width(360.dp), verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg)) {
                Box {
                    Carousel(
                        state = state,
                        pageSize = pageSize.size,
                        snap = snap,
                        itemSpacing = if (fill) 0.dp else ArecaTheme.spacing.md,
                        contentPadding = if (fill) PaddingValues(0.dp) else PaddingValues(horizontal = ArecaTheme.spacing.xl),
                        autoplay = if (autoplay) 4.seconds else null,
                        contentDescription = "Runner 2 pictures",
                    ) { index ->
                        Picture(pictures[index], rounded = !fill)
                    }
                    if (arrows) CarouselArrows(state, modifier = Modifier.align(Alignment.Center))
                    Indicator(state, indicator, modifier = Modifier.align(Alignment.BottomCenter).padding(ArecaTheme.spacing.lg))
                }
                if (thumbnails) {
                    ThumbnailStrip(
                        count = pictures.size,
                        selectedIndex = state.currentItem,
                        onSelect = { state.goToItem(it) },
                        label = { index, total -> "${pictures[index].title}, ${index + 1} of $total" },
                    ) { index ->
                        MediaFrame(model = pictures[index], contentDescription = null, fit = MediaFit.Fill)
                    }
                }
            }
        }
    }

    Variants(
        title = "Page sizes",
        description = "The same pictures sized four ways; swipe each.",
        layout = VariantLayout.Vertical,
    ) {
        for (option in PageSizeOption.entries) {
            variant(name = option.name) {
                val state = rememberCarouselState(itemCount = SampleProductPictures.size)
                val fill = option == PageSizeOption.Fill
                Carousel(
                    state = state,
                    modifier = Modifier.width(360.dp),
                    pageSize = option.size,
                    itemSpacing = if (fill) 0.dp else ArecaTheme.spacing.md,
                    contentPadding = PaddingValues(horizontal = if (fill) 0.dp else ArecaTheme.spacing.xl),
                ) { index ->
                    Picture(SampleProductPictures[index], rounded = !fill)
                }
            }
        }
    }

    Recipes {
        recipe(
            name = "Two by two",
            description = "PerPage(2) with CarouselSnap.Page: the pair moves together, and the dots count pairs.",
        ) {
            val state = rememberCarouselState(itemCount = SampleProductPictures.size)
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Carousel(
                    state = state,
                    modifier = Modifier.width(360.dp),
                    pageSize = CarouselPageSize.PerPage(2),
                    snap = CarouselSnap.Page,
                    itemSpacing = ArecaTheme.spacing.md,
                    contentPadding = PaddingValues(horizontal = ArecaTheme.spacing.xl),
                ) { index ->
                    Picture(SampleProductPictures[index], rounded = true)
                }
                PageIndicator(
                    pageCount = state.pageCount,
                    currentPage = state.currentPage,
                    currentPageOffset = state.currentPageOffset,
                    variant = PageIndicatorVariant.Dots,
                    modifier = Modifier.padding(top = ArecaTheme.spacing.lg),
                )
            }
        }
        recipe(
            name = "Stories",
            description = "Autoplay with segments, filled by CarouselState.autoplayProgress; a touch holds it.",
        ) {
            val state = rememberCarouselState(itemCount = 4)
            Box(Modifier.width(240.dp)) {
                Carousel(state = state, autoplay = 3.seconds) { index ->
                    MediaFrame(
                        model = SampleProductPictures[3 + index % 3],
                        contentDescription = null,
                        aspectRatio = 9f / 14f,
                        style = MediaFrameStyle.default(shape = ArecaTheme.shapes.lg),
                    )
                }
                PageIndicator(
                    pageCount = state.pageCount,
                    currentPage = state.currentPage,
                    variant = PageIndicatorVariant.Segments,
                    progress = { state.autoplayProgress },
                    style = PageIndicatorStyle.onImage(capsule = null),
                    modifier = Modifier.fillMaxWidth().align(Alignment.TopCenter).padding(ArecaTheme.spacing.md),
                )
            }
        }
    }

    EdgeCases {
        case(name = "One picture", expectation = "Nothing to swipe to; indicators draw nothing past one page.") {
            val state = rememberCarouselState(itemCount = 1)
            Box(Modifier.width(240.dp)) {
                Carousel(state = state) { Picture(SampleProductPictures[0], rounded = true) }
                Indicator(state, IndicatorOption.Dots, Modifier.align(Alignment.BottomCenter).padding(ArecaTheme.spacing.md))
            }
        }
        case(name = "A picture that fails", expectation = "Its page shows the failed face; the rest still swipe.") {
            val pictures = listOf(SampleProductPictures[0], SampleProductPictures[1].copy(load = SampleLoad.Fails))
            val state = rememberCarouselState(itemCount = pictures.size, initialItem = 1)
            Carousel(state = state, modifier = Modifier.width(240.dp)) { index -> Picture(pictures[index], rounded = true) }
        }
    }
}

/**
 * A carousel's picture: a product whole on its fill, a scene cropped, peeking with a pinch.
 *
 * @param picture The picture.
 * @param rounded Whether the frame rounds its corners, for items narrower than the carousel.
 */
@Composable
private fun Picture(picture: SamplePicture, rounded: Boolean) {
    MediaFrame(
        model = picture,
        contentDescription = picture.title,
        aspectRatio = if (rounded) 4f / 5f else 1f,
        fit = if (picture.scene == SampleScene.Product) MediaFit.Whole else MediaFit.Fill,
        zoom = ZoomBehavior.Peek,
        onClick = {},
        onClickLabel = "Open picture",
        style = MediaFrameStyle.default(shape = if (rounded) ArecaTheme.shapes.lg else ArecaTheme.shapes.none),
        size = MediaFrameSize.Product,
    )
}

/**
 * The indicator a page chose, over the picture.
 *
 * @param state The carousel it follows.
 * @param option Which indicator.
 * @param modifier Modifier applied to it.
 */
@Composable
private fun Indicator(state: CarouselState, option: IndicatorOption, modifier: Modifier) {
    if (state.pageCount <= 1) return
    when (option) {
        IndicatorOption.None -> Unit
        IndicatorOption.Counter -> PageCounter(
            currentPage = state.currentPage,
            pageCount = state.pageCount,
            style = PageCounterStyle.onImage(),
            modifier = modifier,
        )
        else -> PageIndicator(
            pageCount = state.pageCount,
            currentPage = state.currentPage,
            currentPageOffset = state.currentPageOffset,
            variant = when (option) {
                IndicatorOption.Shrinking -> PageIndicatorVariant.Shrinking()
                IndicatorOption.Line -> PageIndicatorVariant.Line
                IndicatorOption.Segments -> PageIndicatorVariant.Segments
                else -> PageIndicatorVariant.Dots
            },
            progress = { state.autoplayProgress },
            onPageClick = { state.goToPage(it) },
            style = PageIndicatorStyle.onImage(),
            modifier = if (option == IndicatorOption.Segments) modifier.fillMaxWidth() else modifier,
        )
    }
}
