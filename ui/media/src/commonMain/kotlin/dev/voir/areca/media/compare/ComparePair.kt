package dev.voir.areca.media.compare

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.media.image.MediaFrame
import dev.voir.areca.media.image.MediaFrameStyle
import dev.voir.areca.theme.ArecaTheme

/**
 * Two pictures shown whole, one above the other or side by side, each labelled — a photo the user
 * took next to the one on record, nothing to swipe or drag.
 *
 * Pressing either calls [onOpen] with its index, to open both in a
 * [MediaViewer][dev.voir.areca.media.viewer.MediaViewer] at that picture, where each zooms on its
 * own. Draw each picture in [first] and [second] — a
 * [MediaImage][dev.voir.areca.media.image.MediaImage] fills the frame with its own faces.
 *
 * @param first The first picture.
 * @param second The second picture.
 * @param modifier Modifier applied to the pair.
 * @param arrangement One above the other, or side by side.
 * @param aspectRatio Width over height of each frame.
 * @param labels Words over each picture, or `null` for none.
 * @param captions Lines under each picture, or `null` for none.
 * @param onOpen Called with `0` or `1` when a picture is pressed, or `null` for pictures that only show.
 * @param openLabel What pressing a picture does, read out.
 * @param style Shape, fill and label colours.
 */
@Composable
public fun ComparePair(
    first: @Composable BoxScope.() -> Unit,
    second: @Composable BoxScope.() -> Unit,
    modifier: Modifier = Modifier,
    arrangement: CompareArrangement = CompareArrangement.Column,
    aspectRatio: Float = if (arrangement == CompareArrangement.Column) 4f / 3f else 3f / 4f,
    labels: CompareLabels? = null,
    captions: CompareLabels? = null,
    onOpen: ((index: Int) -> Unit)? = null,
    openLabel: String = "Open picture",
    style: CompareStyle = CompareStyle.default(),
) {
    val spacing = Arrangement.spacedBy(ArecaTheme.spacing.lg)
    val pane: @Composable (Modifier, Int) -> Unit = { paneModifier, index ->
        Column(paneModifier, verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.sm)) {
            MediaFrame(
                modifier = Modifier.fillMaxWidth(),
                aspectRatio = aspectRatio,
                onClick = onOpen?.let { open -> { open(index) } },
                onClickLabel = openLabel,
                style = MediaFrameStyle.default(bg = style.bg, shape = style.shape),
                overlay = {
                    val label = labels?.let { if (index == 0) it.first else it.second }
                    if (label != null) {
                        CompareLabel(
                            text = label,
                            modifier = Modifier.align(Alignment.TopStart).padding(ArecaTheme.spacing.md),
                            style = style,
                        )
                    }
                },
                content = if (index == 0) first else second,
            )
            val caption = captions?.let { if (index == 0) it.first else it.second }
            if (caption != null) {
                Text(caption, style = ArecaTheme.typography.bodySmall, color = ArecaTheme.colors.fgMuted)
            }
        }
    }

    if (arrangement == CompareArrangement.Column) {
        Column(modifier, verticalArrangement = spacing) {
            pane(Modifier.fillMaxWidth(), 0)
            pane(Modifier.fillMaxWidth(), 1)
        }
    } else {
        Row(modifier, horizontalArrangement = spacing) {
            pane(Modifier.weight(1f), 0)
            pane(Modifier.weight(1f), 1)
        }
    }
}
