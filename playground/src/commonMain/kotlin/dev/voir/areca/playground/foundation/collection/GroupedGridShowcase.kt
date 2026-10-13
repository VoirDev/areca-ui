package dev.voir.areca.playground.foundation.collection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.collection.LazyHorizontalGroupedGrid
import dev.voir.areca.foundation.collection.LazyVerticalGroupedGrid
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme

/** A grid cell in the examples. */
private data class Track(val id: String, val title: String)

private val SampleData: Map<String, List<Track>> = linkedMapOf(
    "Popular" to List(6) { Track("popular_$it", "Popular ${it + 1}") },
    "Trending" to List(5) { Track("trending_$it", "Trending ${it + 1}") },
    "Recommended" to List(7) { Track("recommended_$it", "Recommended ${it + 1}") },
)

/**
 * Showcase for [LazyVerticalGroupedGrid] and [LazyHorizontalGroupedGrid].
 *
 * One page for both: they take the same data shape and the same group and item slots, and differ
 * only in scroll axis. Splitting them made the two pages near-identical.
 */
@Composable
fun GroupedGridShowcase() = ShowcaseScaffold(
    title = "Grouped grid",
    summary = "Lazy grid whose items are grouped under headers, scrolling vertically or horizontally.",
    module = "foundation",
) {
    Playground(description = "Both grids scroll, so each example is given a bounded height.") {
        val horizontal by booleanControl(
            key = "horizontal",
            default = false,
            description = "Swap the vertical grid for the horizontal one.",
        )
        val columns by numberControl(
            key = "columns / rows",
            default = 2f,
            range = 1f..5f,
            step = 1f,
            format = { it.toInt().toString() },
        )

        preview {
            if (horizontal) {
                LazyHorizontalGroupedGrid(
                    data = SampleData,
                    rows = columns.toInt(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(320.dp),
                    group = { GroupHeader(it) },
                    item = { TrackCard(it) },
                )
            } else {
                LazyVerticalGroupedGrid(
                    data = SampleData,
                    columns = GridCells.Fixed(columns.toInt()),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(320.dp),
                    group = { GroupHeader(it) },
                    item = { TrackCard(it) },
                )
            }
        }
    }

    Variants(
        title = "Scroll axis",
        description = "Same data and slots; only the axis and the cell count parameter differ.",
        layout = VariantLayout.Vertical,
    ) {
        variant("Vertical", detail = "GridCells.Fixed(2)") {
            LazyVerticalGroupedGrid(
                data = SampleData,
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp),
                group = { GroupHeader(it) },
                item = { TrackCard(it) },
            )
        }
        variant("Horizontal", detail = "rows = 2") {
            LazyHorizontalGroupedGrid(
                data = SampleData,
                rows = 2,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp),
                group = { GroupHeader(it) },
                item = { TrackCard(it) },
            )
        }
    }

    Variants(
        title = "Column strategies",
        description = "GridCells decides how the vertical grid divides its width.",
        layout = VariantLayout.Vertical,
    ) {
        variant("Fixed(3)", detail = "three equal columns") {
            LazyVerticalGroupedGrid(
                data = SampleData,
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp),
                group = { GroupHeader(it) },
                item = { TrackCard(it) },
            )
        }
        variant("Adaptive(160dp)", detail = "as many columns as fit") {
            LazyVerticalGroupedGrid(
                data = SampleData,
                columns = GridCells.Adaptive(160.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp),
                group = { GroupHeader(it) },
                item = { TrackCard(it) },
            )
        }
    }

    EdgeCases {
        case(
            name = "Single group",
            expectation = "Still renders its header; the grid does not special-case one group.",
        ) {
            LazyVerticalGroupedGrid(
                data = mapOf("Only group" to SampleData.values.first()),
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                group = { GroupHeader(it) },
                item = { TrackCard(it) },
            )
        }
        case(
            name = "Empty group",
            expectation = "Header renders with nothing under it rather than being skipped.",
        ) {
            LazyVerticalGroupedGrid(
                data = linkedMapOf(
                    "Has items" to SampleData.values.first().take(2),
                    "Empty" to emptyList(),
                ),
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                group = { GroupHeader(it) },
                item = { TrackCard(it) },
            )
        }
        case(
            name = "No content padding",
            expectation = "Items sit flush to the edges; the default inset is a parameter.",
        ) {
            LazyVerticalGroupedGrid(
                data = mapOf("Flush" to SampleData.values.first().take(4)),
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentPadding = PaddingValues(0.dp),
                group = { GroupHeader(it) },
                item = { TrackCard(it) },
            )
        }
    }
}

@Composable
private fun GroupHeader(title: String) {
    Text(
        text = title,
        variant = TextVariant.HeadingExtraSmall,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = ArecaTheme.spacing.md),
    )
}

@Composable
private fun TrackCard(track: Track) {
    Box(
        modifier = Modifier
            .size(width = 140.dp, height = 88.dp)
            .clip(RoundedCornerShape(ArecaTheme.radius.md))
            .background(ArecaTheme.colors.bgSubtle)
            .padding(ArecaTheme.spacing.lg),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = track.title, variant = TextVariant.BodySmall, color = TextColor.Subtle)
    }
}
