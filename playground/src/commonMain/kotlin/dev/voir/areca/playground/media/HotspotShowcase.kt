package dev.voir.areca.playground.media

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.media.image.Hotspot
import dev.voir.areca.media.image.MediaFit
import dev.voir.areca.media.image.MediaFrame
import dev.voir.areca.media.image.MediaFrameStyle
import dev.voir.areca.media.zoom.ZoomBehavior
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme

/** The marker looks a page offers. */
private enum class MarkerLook {
    /** A pulsing dot. */
    Dot,

    /** A number in a capsule. */
    Number,

    /** A word in a capsule. */
    Tag,
}

/** The points a sample picture's markers sit on, with what they name. */
private val Spots = listOf(
    Triple(0.72f, 0.3f, "The sun"),
    Triple(0.3f, 0.66f, "The near hill"),
    Triple(0.82f, 0.78f, "The far ridge"),
)

/** Showcase for [Hotspot] and a frame's picture overlay. */
@Composable
fun HotspotShowcase() = ShowcaseScaffold(
    title = "Hotspot",
    summary = "Markers on points of a picture that stay on their point whatever the frame's shape, and as it zooms.",
    module = "media",
    status = ShowcaseStatus.Beta,
) {
    Playground(description = "Change the frame's ratio: the markers stay on the sun and the hills. Pinch to peek.") {
        val look by enumControl("marker", MarkerLook.Dot, MarkerLook.entries, optionLabel = { it.name })
        val square by booleanControl("square frame", default = false)
        var picked by remember { mutableStateOf<Int?>(null) }

        preview {
            Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md)) {
                MediaFrame(
                    model = SampleProductPictures[4],
                    contentDescription = "A landscape",
                    modifier = Modifier.width(320.dp),
                    aspectRatio = if (square) 1f else 3f / 2f,
                    fit = MediaFit.Fill,
                    zoom = ZoomBehavior.Peek,
                    style = MediaFrameStyle.default(shape = ArecaTheme.shapes.lg),
                    pictureOverlay = {
                        Spots.forEachIndexed { index, (x, y, name) ->
                            Hotspot(
                                onClick = { picked = index },
                                contentDescription = name,
                                modifier = Modifier.pinned(x, y),
                                text = when (look) {
                                    MarkerLook.Dot -> null
                                    MarkerLook.Number -> "${index + 1}"
                                    MarkerLook.Tag -> name
                                },
                                selected = picked == index,
                            )
                        }
                    },
                )
                Text(picked?.let { "Picked: ${Spots[it].third}" } ?: "Press a marker", style = ArecaTheme.typography.bodySmall)
            }
        }
    }

    Variants(title = "Markers", layout = VariantLayout.Grid) {
        variant("Dot") { Hotspot(onClick = {}, contentDescription = "Dot") }
        variant("Number") { Hotspot(onClick = {}, contentDescription = "First", text = "1") }
        variant("Tag") { Hotspot(onClick = {}, contentDescription = "Ridge Chair", text = "Ridge Chair") }
        variant("Selected") { Hotspot(onClick = {}, contentDescription = "Second", text = "2", selected = true) }
    }
}
