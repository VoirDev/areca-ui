package dev.voir.areca.playground.flow.gallery

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import dev.voir.areca.flow.gallery.part.GalleryCaption
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.topbar.CloseAction
import dev.voir.areca.foundation.topbar.TopBar
import dev.voir.areca.foundation.topbar.TopBarTitleAlignment
import dev.voir.areca.media.image.Hotspot
import dev.voir.areca.media.image.MediaFit
import dev.voir.areca.media.image.MediaFrame
import dev.voir.areca.media.image.MediaFrameStyle
import dev.voir.areca.media.image.MediaPictureScope
import dev.voir.areca.media.viewer.MediaViewer
import dev.voir.areca.media.viewer.MediaViewerImage
import dev.voir.areca.media.viewer.MediaViewerStyle
import dev.voir.areca.media.zoom.ZoomBehavior
import dev.voir.areca.playground.media.SamplePicture
import dev.voir.areca.playground.media.SampleProductPictures
import dev.voir.areca.playground.media.SampleShape
import dev.voir.areca.playground.showcase.FlowShowcase
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.canvas.DeviceFrame
import dev.voir.areca.playground.showcase.canvas.DeviceSize
import dev.voir.areca.playground.showcase.canvas.windowInsets
import dev.voir.areca.theme.ArecaTheme

/**
 * A product marked on a look's picture.
 *
 * @property name Its name.
 * @property price Its price, worded.
 * @property x Where it is across the picture, `0f..1f`.
 * @property y Where it is down the picture, `0f..1f`.
 * @property picture Its own picture, for the list.
 */
private data class LookItem(val name: String, val price: String, val x: Float, val y: Float, val picture: SamplePicture)

/** The look: a landscape, its sun, hills and ridge standing in for what is for sale. */
private val Look = SampleProductPictures[4]

/** What the look marks. */
private val LookItems = listOf(
    LookItem("Sun hat", "$45", 0.72f, 0.3f, SamplePicture(70, "Sun hat", SampleShape.Bag, 40f)),
    LookItem("Trail runner", "$129", 0.3f, 0.66f, SamplePicture(71, "Trail runner", SampleShape.Sneaker, 150f)),
    LookItem("Camp chair", "$85", 0.82f, 0.78f, SamplePicture(72, "Camp chair", SampleShape.Chair, 220f)),
)

/** Screen page: a look's picture with its products marked on it. */
@Composable
fun ShopTheLookShowcase() = FlowShowcase(
    title = "Shop the look",
    summary = "A picture with its products marked on it and listed under it; a marker or a row picks one, and the " +
        "picture opens full screen with the markers following its zoom.",
    module = "flow-gallery",
    status = ShowcaseStatus.Beta,
) {
    val numbered by booleanControl("numbered markers", default = false)

    preview {
        DeviceFrame(size = DeviceSize.Regular) {
            ShopTheLookRecipe(items = LookItems, numbered = numbered, windowInsets = DeviceSize.Regular.windowInsets)
        }
    }
}

/**
 * The recipe: the look with its markers, the list of what it marks, and a viewer over the look.
 *
 * @param items What the look marks.
 * @param numbered Whether markers carry numbers rather than pulse.
 * @param windowInsets The screen's insets.
 */
@Composable
private fun ShopTheLookRecipe(items: List<LookItem>, numbered: Boolean, windowInsets: WindowInsets) {
    var picked by remember { mutableStateOf<Int?>(null) }
    var open by remember { mutableStateOf(false) }
    val markers: @Composable MediaPictureScope.() -> Unit = {
        items.forEachIndexed { index, item ->
            Hotspot(
                onClick = { picked = index },
                contentDescription = "${item.name}, ${item.price}",
                modifier = Modifier.pinned(item.x, item.y),
                text = if (numbered) "${index + 1}" else null,
                selected = picked == index,
            )
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(ArecaTheme.colors.bgCanvas)
            .windowInsetsPadding(windowInsets)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
    ) {
        Text(
            "Shop the look",
            style = ArecaTheme.typography.headingSmall,
            modifier = Modifier.padding(horizontal = ArecaTheme.spacing.horizontalContentInsets),
        )
        MediaFrame(
            model = Look,
            contentDescription = "A day on the trail",
            modifier = Modifier.padding(horizontal = ArecaTheme.spacing.horizontalContentInsets),
            aspectRatio = 3f / 2f,
            fit = MediaFit.Fill,
            zoom = ZoomBehavior.Peek,
            onClick = { open = true },
            onClickLabel = "Open the look",
            style = MediaFrameStyle.default(shape = ArecaTheme.shapes.lg),
            pictureOverlay = markers,
        )
        items.forEachIndexed { index, item ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(if (picked == index) ArecaTheme.colors.bgSubtle else ArecaTheme.colors.bgCanvas)
                    .clickable { picked = index }
                    .padding(horizontal = ArecaTheme.spacing.horizontalContentInsets, vertical = ArecaTheme.spacing.sm),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
            ) {
                MediaFrame(model = item.picture, contentDescription = null, modifier = Modifier.size(56.dp), fit = MediaFit.Whole)
                Column(Modifier.weight(1f)) {
                    Text(item.name, style = ArecaTheme.typography.labelLarge)
                    Text(item.price, style = ArecaTheme.typography.bodySmall, color = ArecaTheme.colors.fgMuted)
                }
            }
        }
    }

    if (open) {
        val style = MediaViewerStyle.dark()
        MediaViewer(
            itemCount = 1,
            onDismiss = { open = false },
            style = style,
            topBar = { viewer ->
                TopBar(
                    title = "",
                    style = style.topBar(),
                    titleAlignment = TopBarTitleAlignment.Center,
                    leading = { CloseAction(onClick = viewer::dismiss) },
                )
            },
            footer = {
                val item = picked?.let { items[it] }
                GalleryCaption(title = item?.name ?: "A day on the trail", caption = item?.price ?: "Press a marker")
            },
        ) {
            MediaViewerImage(model = Look, contentDescription = "A day on the trail", fit = MediaFit.Whole, pictureOverlay = markers)
        }
    }
}
