package dev.voir.areca.playground.media

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.media.image.MediaFrameStyle
import dev.voir.areca.media.image.MediaImage
import dev.voir.areca.media.zoom.ZoomBehavior
import dev.voir.areca.media.zoom.rememberZoomState
import dev.voir.areca.media.zoom.zoomable
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.theme.ArecaTheme

/** Showcase for [zoomable], [ZoomBehavior] and [MediaImage]. */
@Composable
fun ZoomShowcase() = ShowcaseScaffold(
    title = "Zoom",
    summary = "Pinch, double tap and pan once zoomed — sticky in a viewer, peeking in a list — with keys and screen-reader actions.",
    module = "media",
    status = ShowcaseStatus.Beta,
) {
    Playground(description = "Pinch, or Ctrl and scroll; double tap or double click; + − 0 with focus.") {
        val behavior by enumControl("behavior", ZoomBehavior.Sticky, ZoomBehavior.entries, optionLabel = { it.name })

        preview {
            val state = rememberZoomState()
            Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md)) {
                Box(Modifier.width(320.dp).height(400.dp).background(Color.Black)) {
                    MediaImage(
                        model = SampleProductPictures[4],
                        contentDescription = SampleProductPictures[4].title,
                        zoom = behavior,
                        zoomState = state,
                        style = MediaFrameStyle.bare(),
                    )
                }
                Text("Zoom ${(state.scale * 100).toInt()}%", style = ArecaTheme.typography.caption)
            }
        }
    }

    Recipes {
        recipe(
            name = "Any content",
            description = "Modifier.zoomable on content that is not a picture: a drawn plan, sized by its own measure.",
        ) {
            val state = rememberZoomState(maxScale = 3f)
            Box(Modifier.width(280.dp).height(200.dp).background(ArecaTheme.colors.bgSubtle)) {
                Box(Modifier.width(280.dp).height(200.dp).zoomable(state)) {
                    Text(
                        "Floor plan · double tap to zoom",
                        style = ArecaTheme.typography.bodySmall,
                    )
                }
            }
        }
    }
}
