package dev.voir.areca.playground.flow.gallery

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dev.voir.areca.flow.gallery.grid.GalleryGroupHeader
import dev.voir.areca.flow.gallery.grid.GalleryTile
import dev.voir.areca.flow.gallery.part.GalleryAuthor
import dev.voir.areca.flow.gallery.part.GalleryCaption
import dev.voir.areca.foundation.navigation.PageIndicator
import dev.voir.areca.foundation.navigation.PageIndicatorStyle
import dev.voir.areca.foundation.navigation.PageIndicatorVariant
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.topbar.CloseAction
import dev.voir.areca.foundation.topbar.TopBar
import dev.voir.areca.foundation.topbar.TopBarTitleAlignment
import dev.voir.areca.media.viewer.MediaViewer
import dev.voir.areca.media.viewer.MediaViewerImage
import dev.voir.areca.media.viewer.MediaViewerStyle
import dev.voir.areca.media.viewer.rememberMediaTransitionState
import dev.voir.areca.playground.media.SampleLoad
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

/** A group of a gallery's pictures, and whose photographs they are. */
private data class SampleGroup(val title: String, val pictures: List<Pair<SamplePicture, String?>>)

/** The gallery's groups: the brand's pictures, its campaigns, readers' photos. */
private val SampleGroups = listOf(
    SampleGroup("From the brand", SampleProductPictures.filter { it.scene == SampleScene.Product }.map { it to null }),
    SampleGroup(
        "Campaigns",
        listOf(SampleProductPictures[3] to null, SamplePicture(40, "Night edition", SampleShape.Bottle, 330f, SampleScene.Studio) to null),
    ),
    SampleGroup(
        "Readers' photos",
        listOf(
            SampleProductPictures[4].copy(id = 41, title = "By the sea", hue = 200f) to "Ines",
            SamplePicture(42, "My shelf", SampleShape.Bag, 20f) to "Theo",
            SamplePicture(43, "Ten miles in", hue = 90f, scene = SampleScene.Landscape, width = 1200, height = 800, load = SampleLoad.Flaky) to "Mara K.",
        ),
    ),
)

/** Screen page: a gallery's grid page, grouped, opening the viewer at a picture. */
@Composable
fun GridPageShowcase() = FlowShowcase(
    title = "Grid page",
    summary = "Every picture of a product in its group, three across, readers' photos labelled with whose they are; " +
        "a tile opens the viewer at that picture among all of them.",
    module = "flow-gallery",
    status = ShowcaseStatus.Beta,
) {
    val columns by enumControl("columns", 3, listOf(2, 3, 4))

    preview {
        DeviceFrame(size = DeviceSize.Regular) {
            GridPageRecipe(groups = SampleGroups, columns = columns, windowInsets = DeviceSize.Regular.windowInsets)
        }
    }
}

/**
 * The recipe: the groups, each headed with its count, tiles in rows, and the viewer over all of them
 * with a caption or an author under each picture.
 *
 * @param groups The groups.
 * @param columns Tiles across.
 * @param windowInsets The screen's insets.
 */
@Composable
private fun GridPageRecipe(groups: List<SampleGroup>, columns: Int, windowInsets: WindowInsets) {
    val all = groups.flatMap { group -> group.pictures.map { (picture, author) -> Triple(picture, author, group.title) } }
    val transition = rememberMediaTransitionState()
    var open by remember { mutableStateOf<Int?>(null) }

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(ArecaTheme.colors.bgCanvas),
        contentPadding = windowInsets.asPaddingValues(),
    ) {
        item { Text("Runner 2", style = ArecaTheme.typography.headingSmall, modifier = Modifier.padding(ArecaTheme.spacing.horizontalContentInsets)) }
        var first = 0
        groups.forEach { group ->
            val start = first
            item(key = group.title) { GalleryGroupHeader(title = group.title, count = group.pictures.size.toString()) }
            items(group.pictures.indices.chunked(columns)) { row ->
                Row(
                    Modifier.padding(horizontal = ArecaTheme.spacing.horizontalContentInsets, vertical = ArecaTheme.spacing.xs),
                    horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.sm),
                ) {
                    row.forEach { place ->
                        val (picture, author) = group.pictures[place]
                        GalleryTile(
                            model = picture,
                            contentDescription = picture.title,
                            onClick = { open = start + place },
                            modifier = Modifier.weight(1f),
                            label = author,
                            transition = transition,
                            transitionKey = picture.id,
                        )
                    }
                    repeat(columns - row.size) { Spacer(Modifier.weight(1f)) }
                }
            }
            first += group.pictures.size
        }
    }

    open?.let { start ->
        MediaViewer(
            itemCount = all.size,
            onDismiss = { open = null },
            initialIndex = start,
            transition = transition,
            key = { index -> all[index].first.id },
            topBar = { viewer ->
                TopBar(
                    title = "${viewer.currentIndex + 1} of ${viewer.itemCount}",
                    style = MediaViewerStyle.dark().topBar(),
                    titleAlignment = TopBarTitleAlignment.Center,
                    leading = { CloseAction(onClick = viewer::dismiss) },
                )
            },
            footer = { viewer ->
                val (picture, author, group) = all[viewer.currentIndex]
                if (author != null) {
                    GalleryAuthor(name = "Photo by $author", letters = author.take(1), detail = "12 October 2026", caption = picture.title)
                } else {
                    GalleryCaption(title = picture.title, kicker = group)
                }
                PageIndicator(
                    pageCount = viewer.itemCount,
                    currentPage = viewer.currentIndex,
                    currentPageOffset = viewer.currentIndexOffset,
                    variant = PageIndicatorVariant.Shrinking(),
                    style = PageIndicatorStyle.onImage(capsule = null),
                    modifier = Modifier.padding(PaddingValues(top = ArecaTheme.spacing.lg)).align(androidx.compose.ui.Alignment.CenterHorizontally),
                )
            },
        ) { index -> MediaViewerImage(model = all[index].first, contentDescription = all[index].first.title) }
    }
}
