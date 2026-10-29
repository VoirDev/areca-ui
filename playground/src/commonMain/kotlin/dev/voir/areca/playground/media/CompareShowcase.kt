package dev.voir.areca.playground.media

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.voir.areca.media.compare.CompareArrangement
import dev.voir.areca.media.compare.CompareFade
import dev.voir.areca.media.compare.CompareHold
import dev.voir.areca.media.compare.CompareLabels
import dev.voir.areca.media.compare.ComparePair
import dev.voir.areca.media.compare.CompareSideBySide
import dev.voir.areca.media.compare.CompareSlider
import dev.voir.areca.media.image.MediaFit
import dev.voir.areca.media.image.MediaFrameStyle
import dev.voir.areca.media.image.MediaImage
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants

/** The comparisons a page offers. */
private enum class CompareMode {
    /** [ComparePair]. */
    Pair,

    /** [CompareSlider]. */
    Divider,

    /** [CompareSideBySide]. */
    SideBySide,

    /** [CompareHold]. */
    Hold,

    /** [CompareFade]. */
    Fade,
}

/** The picture before: the landscape in grey dusk. */
private val Before = SampleProductPictures[4].copy(id = 900, hue = 220f)

/** The picture after: the landscape in colour. */
private val After = SampleProductPictures[4]

/** Showcase for the compare parts. */
@Composable
fun CompareShowcase() = ShowcaseScaffold(
    title = "Compare",
    summary = "Two pictures of the same thing: a pair to open, a divider, linked zoom, hold to see, or a fade.",
    module = "media",
    status = ShowcaseStatus.Beta,
) {
    Playground {
        val mode by enumControl("mode", CompareMode.Pair, CompareMode.entries, optionLabel = { it.name })
        val down by booleanControl("vertical", default = false, description = "Column for Pair and Side by side; a divider moving down.")

        preview {
            val modifier = Modifier.width(340.dp)
            val arrangement = if (down) CompareArrangement.Column else CompareArrangement.Row
            when (mode) {
                CompareMode.Pair -> ComparePair(
                    first = { Picture(SampleProductPictures[3]) },
                    second = { Picture(SampleProductPictures[0], MediaFit.Whole) },
                    modifier = modifier,
                    arrangement = if (down) CompareArrangement.Column else CompareArrangement.Row,
                    labels = CompareLabels(first = "Your photo", second = "In our catalogue"),
                    captions = CompareLabels(first = "Taken today at 14:02", second = "Northbound · Runner 2"),
                    onOpen = {},
                )
                CompareMode.Divider -> CompareSlider(
                    before = { Picture(Before) },
                    after = { Picture(After) },
                    modifier = modifier,
                    orientation = if (down) Orientation.Vertical else Orientation.Horizontal,
                )
                CompareMode.SideBySide -> CompareSideBySide(
                    before = { Picture(Before) },
                    after = { Picture(After) },
                    modifier = modifier,
                    arrangement = arrangement,
                )
                CompareMode.Hold -> CompareHold(before = { Picture(Before) }, after = { Picture(After) }, modifier = modifier)
                CompareMode.Fade -> CompareFade(before = { Picture(Before) }, after = { Picture(After) }, modifier = modifier)
            }
        }
    }

    Variants(title = "Divider positions", layout = VariantLayout.Grid) {
        for (start in listOf(0.25f, 0.75f)) {
            variant(name = "${(start * 100).toInt()}%") {
                CompareSlider(
                    before = { Picture(Before) },
                    after = { Picture(After) },
                    modifier = Modifier.width(220.dp),
                    initialPosition = start,
                )
            }
        }
    }
}

/**
 * A sample picture filling a compare frame.
 *
 * @param picture The picture.
 * @param fit How it fills the frame.
 */
@Composable
private fun Picture(picture: SamplePicture, fit: MediaFit = MediaFit.Fill) {
    MediaImage(model = picture, contentDescription = picture.title, fit = fit, style = MediaFrameStyle.bare())
}
