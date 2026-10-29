package dev.voir.areca.playground.flow.gallery

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.media.carousel.Carousel
import dev.voir.areca.media.carousel.CarouselArrows
import dev.voir.areca.media.carousel.rememberCarouselState
import dev.voir.areca.media.image.MediaFit
import dev.voir.areca.media.image.MediaFrame
import dev.voir.areca.media.image.MediaFrameSize
import dev.voir.areca.media.image.MediaFrameStyle
import dev.voir.areca.media.thumbnail.ThumbnailStrip
import dev.voir.areca.media.viewer.rememberMediaTransitionState
import dev.voir.areca.media.zoom.ZoomBehavior
import dev.voir.areca.playground.media.SamplePicture
import dev.voir.areca.playground.media.SampleProductPictures
import dev.voir.areca.playground.media.SampleScene
import dev.voir.areca.playground.showcase.FlowShowcase
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.theme.ArecaTheme

/** Screen page: a product page on a wide window, with a rail, arrows and hover zoom. */
@Composable
fun DesktopProductShowcase() = FlowShowcase(
    title = "Desktop product page",
    summary = "A product page on a wide window: a thumbnail rail beside the picture, arrows for the pointer, the " +
        "picture magnified under the mouse, and a click opening the gallery.",
    module = "flow-gallery",
    status = ShowcaseStatus.Beta,
) {
    val hover by booleanControl("hover zoom", default = true, description = "ZoomBehavior.Hover on the picture.")

    preview {
        // A desktop window, not a phone: the page is drawn in a window-sized frame instead of DeviceFrame.
        Box(
            Modifier
                .size(width = 960.dp, height = 560.dp)
                .border(1.dp, ArecaTheme.colors.borderBase, ArecaTheme.shapes.md),
        ) {
            DesktopProductRecipe(pictures = SampleProductPictures, hoverZoom = hover)
        }
    }
}

/**
 * The recipe: a rail, the picture with arrows, and the product's details beside them.
 *
 * @param pictures The product's pictures.
 * @param hoverZoom Whether the picture magnifies under the mouse.
 */
@Composable
private fun DesktopProductRecipe(pictures: List<SamplePicture>, hoverZoom: Boolean) {
    val state = rememberCarouselState(itemCount = pictures.size)
    val transition = rememberMediaTransitionState()
    var open by remember { mutableStateOf<Int?>(null) }

    Row(
        Modifier.fillMaxWidth().fillMaxHeight().background(ArecaTheme.colors.bgCanvas).padding(ArecaTheme.spacing.xl3),
        horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xl),
    ) {
        ThumbnailStrip(
            count = pictures.size,
            selectedIndex = state.currentItem,
            onSelect = { state.goToItem(it) },
            modifier = Modifier.width(72.dp).fillMaxHeight(),
            orientation = Orientation.Vertical,
            label = { index, total -> "${pictures[index].title}, ${index + 1} of $total" },
        ) { index -> MediaFrame(model = pictures[index], contentDescription = null, fit = MediaFit.Fill) }

        Box(Modifier.weight(1f).fillMaxHeight()) {
            Carousel(state = state, modifier = Modifier.fillMaxHeight(), contentDescription = "Runner 2 pictures") { index ->
                val picture = pictures[index]
                MediaFrame(
                    model = picture,
                    contentDescription = picture.title,
                    modifier = Modifier.fillMaxHeight(),
                    fit = if (picture.scene == SampleScene.Product) MediaFit.Whole else MediaFit.Fill,
                    zoom = if (hoverZoom) ZoomBehavior.Hover else ZoomBehavior.None,
                    onClick = { open = index },
                    onClickLabel = "Open pictures",
                    transition = transition,
                    transitionKey = picture.id,
                    style = MediaFrameStyle.default(shape = ArecaTheme.shapes.lg),
                    size = MediaFrameSize.Product,
                )
            }
            CarouselArrows(state, modifier = Modifier.align(Alignment.Center))
        }

        Column(Modifier.width(280.dp), verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md)) {
            Text("Northbound", style = ArecaTheme.typography.bodySmall, color = ArecaTheme.colors.fgMuted)
            Text("Runner 2", style = ArecaTheme.typography.headingSmall)
            Text("$129", style = ArecaTheme.typography.labelLarge)
            Button("Add to bag", onClick = {}, modifier = Modifier.fillMaxWidth())
            Text(
                "A light everyday runner with a recycled knit upper.",
                style = ArecaTheme.typography.bodySmall,
                color = ArecaTheme.colors.fgMuted,
            )
        }
    }

    open?.let { start ->
        SampleGalleryViewer(
            pictures = pictures,
            start = start,
            onDismiss = { open = null },
            transition = transition,
            onIndexChange = { state.goToItem(it, animated = false) },
            action = "Add to bag · $129",
        )
    }
}
