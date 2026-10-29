package dev.voir.areca.playground.media

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.LocalContentColor
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.IconButton
import dev.voir.areca.foundation.button.IconButtonStyle
import dev.voir.areca.foundation.navigation.PageIndicator
import dev.voir.areca.foundation.navigation.PageIndicatorStyle
import dev.voir.areca.foundation.navigation.PageIndicatorVariant
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.X
import dev.voir.areca.media.image.MediaFit
import dev.voir.areca.media.image.MediaFrame
import dev.voir.areca.media.image.MediaFrameSize
import dev.voir.areca.media.image.MediaPlaceholder
import dev.voir.areca.media.thumbnail.ThumbnailStrip
import dev.voir.areca.media.thumbnail.ThumbnailStripStyle
import dev.voir.areca.media.viewer.MediaViewer
import dev.voir.areca.media.viewer.MediaViewerChrome
import dev.voir.areca.media.viewer.MediaViewerImage
import dev.voir.areca.media.viewer.MediaViewerStyle
import dev.voir.areca.media.viewer.rememberMediaTransitionState
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.theme.ArecaTheme

/** The footers a page offers. */
private enum class ViewerFooter {
    /** A thumbnail strip. */
    Thumbnails,

    /** Page dots. */
    Dots,

    /** Nothing. */
    None,
}

/** Showcase for [MediaViewer], [MediaViewerImage] and the transition into it. */
@Composable
fun MediaViewerShowcase() = ShowcaseScaffold(
    title = "Media viewer",
    summary = "Pictures full screen in a window of their own: swiped, zoomed, closed by a swipe down, grown out of their frame.",
    module = "media",
    status = ShowcaseStatus.Beta,
) {
    Playground(description = "Press a picture to open it. The bars here are plain; flow-gallery ships worded ones.") {
        val light by booleanControl("light", default = false, description = "MediaViewerStyle.light().")
        val single by booleanControl("single picture", default = false)
        val keepChrome by booleanControl("keep bars", default = false, description = "MediaViewerChrome.Always.")
        val footer by enumControl("footer", ViewerFooter.Thumbnails, ViewerFooter.entries, optionLabel = { it.name })
        val failing by booleanControl("a picture fails", default = false)

        preview {
            val pictures = SampleProductPictures.mapIndexed { index, picture ->
                if (failing && index == 2) picture.copy(load = SampleLoad.Fails) else picture
            }
            val transition = rememberMediaTransitionState()
            var open by remember { mutableStateOf<Int?>(null) }

            Row(Modifier.width(360.dp), horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.sm)) {
                pictures.take(3).forEachIndexed { index, picture ->
                    MediaFrame(
                        model = picture,
                        contentDescription = picture.title,
                        modifier = Modifier.weight(1f),
                        aspectRatio = 1f,
                        fit = if (picture.scene == SampleScene.Product) MediaFit.Whole else MediaFit.Fill,
                        size = MediaFrameSize.default(inset = ArecaTheme.spacing.md),
                        onClick = { open = index },
                        onClickLabel = "Open picture",
                        transition = transition,
                        transitionKey = picture.id,
                    )
                }
            }

            val start = open
            if (start != null) {
                val shown = if (single) listOf(pictures[start]) else pictures
                MediaViewer(
                    itemCount = shown.size,
                    onDismiss = { open = null },
                    initialIndex = if (single) 0 else start,
                    transition = transition,
                    key = { index -> shown[index].id },
                    chrome = if (keepChrome) MediaViewerChrome.Always else MediaViewerChrome.TapToToggle,
                    style = if (light) MediaViewerStyle.light() else MediaViewerStyle.dark(),
                    topBar = { state ->
                        Row(
                            Modifier.fillMaxWidth().padding(ArecaTheme.spacing.md),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            IconButton(
                                ArecaIcons.X,
                                contentDescription = "Close",
                                onClick = state::dismiss,
                                style = IconButtonStyle.plain(ActionColor.Neutral).copy(content = LocalContentColor.current),
                            )
                            Text(
                                text = if (state.itemCount > 1) "${state.currentIndex + 1} of ${state.itemCount}" else "",
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center,
                                style = ArecaTheme.typography.labelLarge,
                            )
                            Box(Modifier.width(44.dp))
                        }
                    },
                    footer = { state ->
                        Column(
                            Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
                        ) {
                            Text(shown[state.currentIndex].title, style = ArecaTheme.typography.bodyMedium)
                            if (state.itemCount > 1) {
                                when (footer) {
                                    ViewerFooter.Thumbnails -> ThumbnailStrip(
                                        count = state.itemCount,
                                        selectedIndex = state.currentIndex,
                                        onSelect = { state.goTo(it) },
                                        contentPadding = PaddingValues(horizontal = ArecaTheme.spacing.xl),
                                        style = if (light) ThumbnailStripStyle.onLight() else ThumbnailStripStyle.onDark(),
                                    ) { index ->
                                        MediaFrame(model = shown[index], contentDescription = null, fit = MediaFit.Fill)
                                    }
                                    ViewerFooter.Dots -> PageIndicator(
                                        pageCount = state.itemCount,
                                        currentPage = state.currentIndex,
                                        currentPageOffset = state.currentIndexOffset,
                                        variant = PageIndicatorVariant.Dots,
                                        style = PageIndicatorStyle.onImage(capsule = null),
                                    )
                                    ViewerFooter.None -> Unit
                                }
                            }
                        }
                    },
                ) { index ->
                    MediaViewerImage(
                        model = shown[index],
                        contentDescription = shown[index].title,
                        placeholder = MediaPlaceholder.Blurred(shown[index].copy(load = SampleLoad.Loads)),
                    )
                }
            }
        }
    }
}
