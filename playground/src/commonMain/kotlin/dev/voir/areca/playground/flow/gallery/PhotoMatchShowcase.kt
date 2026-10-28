package dev.voir.areca.playground.flow.gallery

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dev.voir.areca.flow.gallery.part.GalleryCaption
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.button.ButtonStyle
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.topbar.CloseAction
import dev.voir.areca.foundation.topbar.TopBar
import dev.voir.areca.foundation.topbar.TopBarTitleAlignment
import dev.voir.areca.media.compare.CompareArrangement
import dev.voir.areca.media.compare.CompareLabels
import dev.voir.areca.media.compare.ComparePair
import dev.voir.areca.media.image.MediaFit
import dev.voir.areca.media.image.MediaFrameStyle
import dev.voir.areca.media.image.MediaImage
import dev.voir.areca.media.viewer.MediaViewer
import dev.voir.areca.media.viewer.MediaViewerImage
import dev.voir.areca.media.viewer.MediaViewerStyle
import dev.voir.areca.playground.media.SamplePicture
import dev.voir.areca.playground.media.SampleProductPictures
import dev.voir.areca.playground.showcase.FlowShowcase
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.canvas.DeviceFrame
import dev.voir.areca.playground.showcase.canvas.DeviceSize
import dev.voir.areca.playground.showcase.canvas.windowInsets
import dev.voir.areca.theme.ArecaTheme

/** Screen page: the user's photo next to the catalogue's, either opening both full screen. */
@Composable
fun PhotoMatchShowcase() = FlowShowcase(
    title = "Photo match",
    summary = "A photo the user took next to the one on record, one above the other; either opens both full screen, " +
        "each zoomed on its own.",
    module = "flow-gallery",
    status = ShowcaseStatus.Beta,
) {
    val row by booleanControl("side by side", default = false)

    preview {
        DeviceFrame(size = DeviceSize.Regular) {
            PhotoMatchRecipe(
                photo = SampleProductPictures[3],
                record = SampleProductPictures[0],
                arrangement = if (row) CompareArrangement.Row else CompareArrangement.Column,
                windowInsets = DeviceSize.Regular.windowInsets,
            )
        }
    }
}

/**
 * The recipe: the pair under a question, two answers, and a viewer over both.
 *
 * @param photo The photo the user took.
 * @param record The catalogue's picture.
 * @param arrangement One above the other, or side by side.
 * @param windowInsets The screen's insets.
 */
@Composable
private fun PhotoMatchRecipe(
    photo: SamplePicture,
    record: SamplePicture,
    arrangement: CompareArrangement,
    windowInsets: WindowInsets,
) {
    val pictures = listOf(photo to "Your photo", record to "In our catalogue")
    var open by remember { mutableStateOf<Int?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ArecaTheme.colors.bgCanvas)
            .windowInsetsPadding(windowInsets)
            .padding(ArecaTheme.spacing.horizontalContentInsets),
        verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg),
    ) {
        Text("Is this your trainer?", style = ArecaTheme.typography.headingSmall)
        ComparePair(
            first = { MediaImage(photo, "Your photo", fit = MediaFit.Fill, style = MediaFrameStyle.bare()) },
            second = { MediaImage(record, record.title, style = MediaFrameStyle.bare()) },
            arrangement = arrangement,
            labels = CompareLabels(first = "Your photo", second = "In our catalogue"),
            captions = CompareLabels(first = "Taken today at 14:02", second = "Northbound · Runner 2"),
            onOpen = { open = it },
        )
        Button("Yes, that's it", onClick = {}, modifier = Modifier.fillMaxWidth())
        Button("Not this one", onClick = {}, modifier = Modifier.fillMaxWidth(), style = ButtonStyle.soft(ActionColor.Neutral))
    }

    open?.let { start ->
        MediaViewer(
            itemCount = pictures.size,
            onDismiss = { open = null },
            initialIndex = start,
            topBar = { viewer ->
                TopBar(
                    title = pictures[viewer.currentIndex].second,
                    style = MediaViewerStyle.dark().topBar(),
                    titleAlignment = TopBarTitleAlignment.Center,
                    leading = { CloseAction(onClick = viewer::dismiss) },
                )
            },
            footer = { viewer -> GalleryCaption(title = pictures[viewer.currentIndex].first.title) },
        ) { index -> MediaViewerImage(model = pictures[index].first, contentDescription = pictures[index].second) }
    }
}
