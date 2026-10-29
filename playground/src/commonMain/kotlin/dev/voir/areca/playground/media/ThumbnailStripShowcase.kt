package dev.voir.areca.playground.media

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.voir.areca.media.image.MediaFit
import dev.voir.areca.media.image.MediaFrame
import dev.voir.areca.media.thumbnail.ThumbnailStrip
import dev.voir.areca.media.thumbnail.ThumbnailStripStyle
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme

/** Showcase for [ThumbnailStrip]. */
@Composable
fun ThumbnailStripShowcase() = ShowcaseScaffold(
    title = "Thumbnail strip",
    summary = "Small pictures, one per item, the selected one ringed: under a carousel, along a viewer, beside a desktop picture.",
    module = "media",
    status = ShowcaseStatus.Beta,
) {
    Playground {
        val orientation by enumControl("orientation", Orientation.Horizontal, Orientation.entries, optionLabel = { it.name })
        val many by booleanControl("24 pictures", default = false)

        preview {
            val pictures = if (many) SampleManyPictures else SampleProductPictures
            var selected by remember { mutableIntStateOf(0) }
            Strip(
                pictures = pictures,
                selected = selected,
                onSelect = { selected = it },
                orientation = orientation,
                modifier = if (orientation == Orientation.Horizontal) Modifier.width(340.dp) else Modifier.height(320.dp),
            )
        }
    }

    Variants(title = "Grounds", layout = VariantLayout.Vertical) {
        variant(name = "Default", detail = "On the page") {
            var selected by remember { mutableIntStateOf(2) }
            Strip(SampleProductPictures, selected, { selected = it }, Orientation.Horizontal, Modifier.width(340.dp))
        }
        variant(name = "onDark", detail = "Along a dark viewer") {
            var selected by remember { mutableIntStateOf(2) }
            Box(Modifier.background(Color.Black).padding(vertical = ArecaTheme.spacing.lg)) {
                Strip(
                    pictures = SampleProductPictures,
                    selected = selected,
                    onSelect = { selected = it },
                    orientation = Orientation.Horizontal,
                    modifier = Modifier.width(340.dp),
                    style = ThumbnailStripStyle.onDark(),
                )
            }
        }
    }

    EdgeCases {
        case(name = "A thumbnail that fails", expectation = "Its compact failed face, still selectable.") {
            var selected by remember { mutableIntStateOf(0) }
            val pictures = SampleProductPictures.take(3).mapIndexed { index, picture ->
                if (index == 1) picture.copy(load = SampleLoad.Fails) else picture
            }
            Strip(pictures, selected, { selected = it }, Orientation.Horizontal, Modifier.width(340.dp))
        }
    }
}

/**
 * A strip of sample pictures.
 *
 * @param pictures The pictures.
 * @param selected Index of the selected one.
 * @param onSelect Selects one.
 * @param orientation Which way it runs.
 * @param modifier Modifier applied to it.
 * @param style Its look.
 */
@Composable
private fun Strip(
    pictures: List<SamplePicture>,
    selected: Int,
    onSelect: (Int) -> Unit,
    orientation: Orientation,
    modifier: Modifier,
    style: ThumbnailStripStyle = ThumbnailStripStyle.Default,
) {
    ThumbnailStrip(
        count = pictures.size,
        selectedIndex = selected,
        onSelect = onSelect,
        modifier = modifier,
        orientation = orientation,
        contentPadding = PaddingValues(ArecaTheme.spacing.xs),
        label = { index, total -> "${pictures[index].title}, ${index + 1} of $total" },
        style = style,
    ) { index ->
        MediaFrame(model = pictures[index], contentDescription = null, fit = MediaFit.Fill)
    }
}
