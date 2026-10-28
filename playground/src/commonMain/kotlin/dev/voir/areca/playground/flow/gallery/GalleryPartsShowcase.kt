package dev.voir.areca.playground.flow.gallery

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.voir.areca.flow.gallery.grid.GalleryGroupHeader
import dev.voir.areca.flow.gallery.grid.GalleryTile
import dev.voir.areca.flow.gallery.part.GalleryAuthor
import dev.voir.areca.flow.gallery.part.GalleryCaption
import dev.voir.areca.flow.gallery.part.GalleryPrimaryAction
import dev.voir.areca.foundation.LocalContentColor
import dev.voir.areca.media.viewer.MediaViewerStyle
import dev.voir.areca.playground.media.SampleProductPictures
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme

/** Showcase for the gallery's foot and grid parts. */
@Composable
fun GalleryPartsShowcase() = ShowcaseScaffold(
    title = "Parts",
    summary = "The worded pieces of a gallery: a caption or an author under a picture, the primary action, " +
        "and a grid page's header and tile.",
    module = "flow-gallery",
    status = ShowcaseStatus.Beta,
) {
    Playground(description = "A viewer's foot, on its dark or light ground. Its bar is foundation's TopBar in MediaViewerStyle.topBar().") {
        val light by booleanControl("light ground", default = false)
        val author by booleanControl("author", default = false)

        preview {
            val style = if (light) MediaViewerStyle.light() else MediaViewerStyle.dark()
            CompositionLocalProvider(LocalContentColor provides style.content) {
                Column(
                    Modifier.width(360.dp).background(style.bg).padding(vertical = ArecaTheme.spacing.xl),
                    verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg),
                ) {
                    if (author) {
                        GalleryAuthor(
                            name = "Photo by Mara K.",
                            letters = "M",
                            detail = "12 October 2026",
                            caption = "Ten miles in, still comfortable.",
                        )
                    } else {
                        GalleryCaption(title = "Laces, close up", kicker = "From the brand", caption = "Flat laces in two tones.")
                    }
                    GalleryPrimaryAction(text = "Add to bag · $129", onClick = {})
                }
            }
        }
    }

    Variants(title = "Grid", layout = VariantLayout.Vertical) {
        variant("Header and tiles", detail = "A reader's photo labelled with whose it is") {
            Column(Modifier.width(360.dp)) {
                GalleryGroupHeader(title = "Readers' photos", count = "3")
                Row(
                    Modifier.padding(horizontal = ArecaTheme.spacing.horizontalContentInsets),
                    horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.sm),
                ) {
                    GalleryTile(SampleProductPictures[0], null, onClick = {}, modifier = Modifier.weight(1f))
                    GalleryTile(SampleProductPictures[4], null, onClick = {}, modifier = Modifier.weight(1f), label = "Ines")
                    GalleryTile(SampleProductPictures[6], null, onClick = {}, modifier = Modifier.weight(1f), label = "Theo")
                }
            }
        }
    }
}
