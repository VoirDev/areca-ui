package dev.voir.areca.playground.flow.gallery

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import dev.voir.areca.foundation.adaptive.AdaptiveDefaults
import dev.voir.areca.foundation.adaptive.ContentColumn
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
import dev.voir.areca.playground.showcase.canvas.WindowPreset
import dev.voir.areca.playground.showcase.canvas.windowInsets
import dev.voir.areca.playground.showcase.chrome.ChromeText
import dev.voir.areca.playground.showcase.chrome.ChromeTextRole
import dev.voir.areca.playground.showcase.chrome.ChromeTone
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.window.InputMode

/** Screen page: the product page from an expanded window up, with a rail, the picture and the details beside it. */
@Composable
fun DesktopProductShowcase() = FlowShowcase(
    title = "Desktop product page",
    summary = "A product page on a wide window: a thumbnail rail beside the picture, the details beside it, and under " +
        "a pointer arrows and the picture magnified under the mouse; a click opens the gallery.",
    module = "flow-gallery",
    status = ShowcaseStatus.Beta,
    notes = {
        ChromeText(text = "The wide half of the product page", role = ChromeTextRole.Label)
        ChromeText(
            text = "DesktopProductRecipe is what the Product page recipe draws from 840dp. Under a pointer the " +
                "picture has arrows and magnifies under the mouse; on a tablet on its side it swipes and peeks " +
                "instead. On a monitor the page stops at the content width. Pick a window under 840dp to see why " +
                "the product page keeps the phone layout there.",
            role = ChromeTextRole.SectionDescription,
            tone = ChromeTone.Muted,
        )
    },
) {
    val preset by enumControl(
        key = "window",
        default = WindowPreset.Laptop,
        options = WindowPreset.entries,
        optionLabel = { it.label },
    )

    preview {
        GalleryWindowFrame(preset) {
            DesktopProductRecipe(pictures = SampleProductPictures, lightViewer = false, windowInsets = preset.windowInsets)
        }
    }
}

/**
 * The wide product page: a rail, the picture, and the product's details beside them, no wider than
 * the content width. Arrows and hover zoom for a pointer; a swipe and a peek for touch.
 *
 * Shared with the product page, which draws it from an expanded window up.
 *
 * @param pictures The product's pictures.
 * @param lightViewer Whether the gallery stands the pictures on white.
 * @param windowInsets The screen's insets.
 */
@Composable
internal fun DesktopProductRecipe(pictures: List<SamplePicture>, lightViewer: Boolean, windowInsets: WindowInsets) {
    val state = rememberCarouselState(itemCount = pictures.size)
    val transition = rememberMediaTransitionState()
    var open by remember { mutableStateOf<Int?>(null) }
    val pointer = ArecaTheme.window.input == InputMode.Pointer

    ContentColumn(
        modifier = Modifier.fillMaxSize().background(ArecaTheme.colors.bgCanvas).windowInsetsPadding(windowInsets),
        maxWidth = AdaptiveDefaults.ContentWidth,
    ) {
        Row(
            Modifier.fillMaxSize().padding(vertical = ArecaTheme.spacing.xl3),
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
                        zoom = if (pointer) ZoomBehavior.Hover else ZoomBehavior.Peek,
                        onClick = { open = index },
                        onClickLabel = "Open pictures",
                        transition = transition,
                        transitionKey = picture.id,
                        style = MediaFrameStyle.default(shape = ArecaTheme.shapes.lg),
                        size = MediaFrameSize.Product,
                    )
                }
                // Touch swipes; arrows there would only cover the picture.
                if (pointer) CarouselArrows(state, modifier = Modifier.align(Alignment.Center))
            }

            Column(
                Modifier.width(280.dp).fillMaxHeight().verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
            ) {
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
    }

    open?.let { start ->
        SampleGalleryViewer(
            pictures = pictures,
            start = start,
            onDismiss = { open = null },
            transition = transition,
            onIndexChange = { state.goToItem(it, animated = false) },
            action = "Add to bag · $129",
            light = lightViewer,
        )
    }
}
