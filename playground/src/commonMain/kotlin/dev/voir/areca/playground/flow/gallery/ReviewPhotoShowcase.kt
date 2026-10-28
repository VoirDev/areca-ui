package dev.voir.areca.playground.flow.gallery

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.media.image.MediaFit
import dev.voir.areca.media.image.MediaFrame
import dev.voir.areca.media.viewer.rememberMediaTransitionState
import dev.voir.areca.playground.media.SamplePicture
import dev.voir.areca.playground.media.SampleProductPictures
import dev.voir.areca.playground.media.SampleScene
import dev.voir.areca.playground.media.SampleShape
import dev.voir.areca.playground.showcase.FlowShowcase
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.canvas.DeviceFrame
import dev.voir.areca.playground.showcase.canvas.DeviceSize
import dev.voir.areca.playground.showcase.canvas.windowInsets
import dev.voir.areca.theme.ArecaTheme

/** A reader's photos under their review. */
private val ReviewPhotos = listOf(
    SampleProductPictures[4].copy(id = 60, title = "Ten miles in"),
    SamplePicture(61, "After a month", SampleShape.Sneaker, 12f),
    SamplePicture(62, "Unboxing", SampleShape.Sneaker, 12f, scene = SampleScene.Studio, width = 900, height = 1100),
)

/** Screen page: a review's photos, each opening alone. */
@Composable
fun ReviewPhotoShowcase() = FlowShowcase(
    title = "Review photo",
    summary = "A review's photos as small squares; one opens alone, full screen, with whose photo it is under it.",
    module = "flow-gallery",
    status = ShowcaseStatus.Beta,
) {
    val light by booleanControl("light viewer", default = false)
    val all by booleanControl("open all three", default = false, description = "A gallery of the review's photos instead of one.")

    preview {
        DeviceFrame(size = DeviceSize.Regular) {
            ReviewPhotoRecipe(photos = ReviewPhotos, light = light, openAll = all, windowInsets = DeviceSize.Regular.windowInsets)
        }
    }
}

/**
 * The recipe: a review with its photos, a photo opening alone with the author under it.
 *
 * @param photos The review's photos.
 * @param light Whether the viewer stands the photo on white.
 * @param openAll Whether a photo opens with the others.
 * @param windowInsets The screen's insets.
 */
@Composable
private fun ReviewPhotoRecipe(photos: List<SamplePicture>, light: Boolean, openAll: Boolean, windowInsets: WindowInsets) {
    val transition = rememberMediaTransitionState()
    var open by remember { mutableStateOf<Int?>(null) }

    Column(
        Modifier
            .fillMaxSize()
            .background(ArecaTheme.colors.bgCanvas)
            .windowInsetsPadding(windowInsets)
            .padding(ArecaTheme.spacing.horizontalContentInsets),
        verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
    ) {
        Text("Mara K.", style = ArecaTheme.typography.labelLarge)
        Text("★★★★★ · 12 October 2026", style = ArecaTheme.typography.bodySmall, color = ArecaTheme.colors.fgMuted)
        Text("Ten miles in and still comfortable. The knit dries quickly after rain.", style = ArecaTheme.typography.bodyMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.sm)) {
            photos.forEachIndexed { index, photo ->
                MediaFrame(
                    model = photo,
                    contentDescription = photo.title,
                    modifier = Modifier.size(88.dp),
                    fit = MediaFit.Fill,
                    onClick = { open = index },
                    onClickLabel = "Open photo",
                    transition = transition,
                    transitionKey = photo.id,
                )
            }
        }
    }

    open?.let { start ->
        SampleGalleryViewer(
            pictures = if (openAll) photos else listOf(photos[start]),
            start = if (openAll) start else 0,
            onDismiss = { open = null },
            transition = transition,
            author = "Mara K.",
            light = light,
        )
    }
}
