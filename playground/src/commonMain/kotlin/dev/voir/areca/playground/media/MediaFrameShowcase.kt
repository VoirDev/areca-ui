package dev.voir.areca.playground.media

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.badge.Badge
import dev.voir.areca.foundation.badge.BadgeSize
import dev.voir.areca.foundation.badge.BadgeStyle
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.media.image.MediaFit
import dev.voir.areca.media.image.MediaFrame
import dev.voir.areca.media.image.MediaFrameSize
import dev.voir.areca.media.image.MediaFrameStyle
import dev.voir.areca.media.image.MediaPlaceholder
import dev.voir.areca.media.image.MediaScrim
import dev.voir.areca.media.image.MediaState
import dev.voir.areca.media.zoom.ZoomBehavior
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme

/** The frame presets a page offers. */
private enum class FrameLook {
    /** [MediaFrameStyle.Default] with [MediaFrameSize.Default]. */
    Default,

    /** [MediaFrameSize.Product]: the picture whole, clear of the corners. */
    Product,

    /** [MediaFrameStyle.circle]. */
    Circle,
}

/** Showcase for [MediaFrame], with foundation badges laid on it. */
@Composable
fun MediaFrameShowcase() = ShowcaseScaffold(
    title = "Media frame",
    summary = "A picture's place on the page: shape, fill, loading and failed faces, scrim, labels and press.",
    module = "media",
    status = ShowcaseStatus.Beta,
) {
    Playground(description = "Pinch, or Ctrl and scroll, with peek on; Retry on a flaky picture loads it.") {
        val load by enumControl("load", SampleLoad.Loads, SampleLoad.entries, optionLabel = { it.name })
        val look by enumControl("look", FrameLook.Product, FrameLook.entries, optionLabel = { it.name })
        val fit by enumControl("fit", MediaFit.Whole, MediaFit.entries, optionLabel = { it.name })
        val placeholder by enumControl(
            key = "placeholder",
            default = "Skeleton",
            options = listOf("Skeleton", "Blurred", "None"),
        )
        val scrim by enumControl("scrim", MediaScrim.None, MediaScrim.entries, optionLabel = { it.name })
        val label by booleanControl("label", default = true)
        val peek by booleanControl("peek", default = true, description = "ZoomBehavior.Peek.")

        preview {
            val picture = SampleProductPictures[0].copy(load = load)
            MediaFrame(
                model = picture,
                contentDescription = picture.title,
                modifier = Modifier.width(260.dp),
                aspectRatio = 1f,
                fit = fit,
                placeholder = when (placeholder) {
                    "Blurred" -> MediaPlaceholder.Blurred(picture.copy(load = SampleLoad.Loads))
                    "None" -> MediaPlaceholder.None
                    else -> MediaPlaceholder.Skeleton
                },
                zoom = if (peek) ZoomBehavior.Peek else ZoomBehavior.None,
                onClick = {},
                onClickLabel = "Open picture",
                scrim = scrim,
                style = if (look == FrameLook.Circle) MediaFrameStyle.circle() else MediaFrameStyle.Default,
                size = if (look == FrameLook.Product) MediaFrameSize.Product else MediaFrameSize.Default,
            ) {
                if (label) {
                    Badge(
                        modifier = Modifier.align(Alignment.TopStart).padding(ArecaTheme.spacing.md),
                        style = BadgeStyle.Brand,
                        size = BadgeSize.Small,
                    ) { Text("New") }
                }
            }
        }
    }

    Variants(
        title = "Fit",
        description = "A product stands whole on the fill; a photograph is cropped to cover the frame.",
        layout = VariantLayout.Grid,
    ) {
        for (fit in MediaFit.entries) {
            for (picture in listOf(SampleProductPictures[0], SampleProductPictures[4])) {
                variant(name = "${fit.name} / ${picture.title}") {
                    MediaFrame(
                        model = picture,
                        contentDescription = picture.title,
                        modifier = Modifier.width(160.dp),
                        aspectRatio = 4f / 5f,
                        fit = fit,
                        size = MediaFrameSize.Product,
                    )
                }
            }
        }
    }

    Variants(
        title = "Badges on a picture",
        description = "Foundation's Badge in a frame's overlay; a title on a photograph is plain Text over MediaScrim.Bottom.",
        layout = VariantLayout.Grid,
    ) {
        val labels = listOf(
            "Surface" to "Mara K.",
            "Brand" to "New",
            "Danger" to "−20%",
            "Neutral" to "Sold out",
        )
        for ((name, word) in labels) {
            variant(name = name) {
                MediaFrame(
                    model = SampleProductPictures[6],
                    contentDescription = null,
                    modifier = Modifier.width(140.dp),
                    aspectRatio = 1f,
                    fit = MediaFit.Whole,
                    size = MediaFrameSize.Product,
                ) {
                    Badge(
                        modifier = Modifier.align(Alignment.TopStart).padding(ArecaTheme.spacing.md),
                        style = when (name) {
                            "Brand" -> BadgeStyle.Brand
                            "Danger" -> BadgeStyle.Danger
                            "Neutral" -> BadgeStyle.NeutralDark
                            else -> BadgeStyle(bg = ArecaTheme.colors.bgSurface, content = ArecaTheme.colors.fgBase, border = Color.Transparent)
                        },
                        size = BadgeSize.Small,
                    ) { Text(word) }
                }
            }
        }
        variant(name = "On image, with scrim") {
            MediaFrame(
                model = SampleProductPictures[4],
                contentDescription = null,
                modifier = Modifier.width(220.dp),
                aspectRatio = 4f / 3f,
                scrim = MediaScrim.Bottom,
            ) {
                Text(
                    text = "Autumn edit",
                    style = ArecaTheme.typography.labelLarge,
                    color = ArecaTheme.baseColors.baseWhite,
                    modifier = Modifier.align(Alignment.BottomStart).padding(ArecaTheme.spacing.lg),
                )
            }
        }
    }

    States {
        variant("Loading", detail = "Skeleton at the frame's ratio") {
            Frame(SampleProductPictures[0].copy(load = SampleLoad.Hangs))
        }
        variant("Loading, blurred", detail = "The thumbnail, blurred, until the picture arrives") {
            Frame(
                SampleProductPictures[3].copy(load = SampleLoad.Hangs),
                MediaPlaceholder.Blurred(SampleProductPictures[3]),
            )
        }
        variant("Failed", detail = "Glyph, line and Retry") {
            Frame(SampleProductPictures[0].copy(load = SampleLoad.Fails))
        }
        variant("Failed, compact", detail = "A thumbnail: the glyph alone, pressable to retry") {
            MediaFrame(
                model = SampleProductPictures[0].copy(load = SampleLoad.Fails),
                contentDescription = null,
                modifier = Modifier.width(56.dp),
                aspectRatio = 1f,
            )
        }
        variant("Slot content", detail = "State said by the caller") {
            MediaFrame(modifier = Modifier.width(200.dp), aspectRatio = 1f, state = MediaState.Loading) {}
        }
    }

    EdgeCases {
        case(name = "No model", expectation = "The failed face, with no retry: there is nothing to retry.") {
            MediaFrame(model = null, contentDescription = null, modifier = Modifier.width(200.dp), aspectRatio = 1f)
        }
        case(name = "Very wide frame", expectation = "A whole picture stays whole, centred on the fill.") {
            Box(Modifier.fillMaxWidth()) {
                MediaFrame(
                    model = SampleProductPictures[0],
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(),
                    aspectRatio = 4f,
                    fit = MediaFit.Whole,
                )
            }
        }
    }
}

/**
 * A square frame for a state cell.
 *
 * @param picture The picture, with its load behaviour.
 * @param placeholder What stands in while it loads.
 */
@Composable
private fun Frame(picture: SamplePicture, placeholder: MediaPlaceholder = MediaPlaceholder.Skeleton) {
    MediaFrame(
        model = picture,
        contentDescription = picture.title,
        modifier = Modifier.width(200.dp),
        aspectRatio = 1f,
        fit = MediaFit.Whole,
        placeholder = placeholder,
        size = MediaFrameSize.Product,
    )
}
