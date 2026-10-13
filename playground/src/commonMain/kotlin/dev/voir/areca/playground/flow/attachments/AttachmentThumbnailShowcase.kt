package dev.voir.areca.playground.flow.attachments

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import dev.voir.areca.flow.attachments.part.AttachmentThumbnail
import dev.voir.areca.flow.attachments.part.AttachmentTileStyle
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Sizes
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme

/** Showcase for [AttachmentThumbnail]. */
@Composable
fun AttachmentThumbnailShowcase() = ShowcaseScaffold(
    title = "Thumbnail",
    summary = "The square a file is recognised by: the file itself where it can be drawn, its kind " +
        "of file where it cannot, and a mark for where its bytes are.",
    module = "flow-attachments",
    status = ShowcaseStatus.Beta,
) {
    Playground(description = "Pick a file and a state; the picture comes from the sample loader in scope.") {
        val file by enumControl(
            key = "file",
            default = SampleFileOption.Photo,
            options = SampleFileOption.entries,
            optionLabel = { it.label },
        )
        val state by enumControl(
            key = "state",
            default = SampleStateOption.Ready,
            options = SampleStateOption.entries,
            optionLabel = { it.label },
        )
        val progress by numberControl(
            key = "progress",
            default = 0.42f,
            range = 0f..1f,
            step = 0.05f,
            format = { "${(it * 100).toInt()}%" },
            description = "Share of an upload or a download done.",
        )
        val marks by booleanControl(
            key = "marks",
            default = true,
            description = "The extension, the corner disc and the veil; off where the square is too small.",
        )
        val size by numberControl(
            key = "size",
            default = 80f,
            range = 40f..128f,
            step = 8f,
            format = { "${it.toInt()}dp" },
        )

        preview {
            AttachmentThumbnail(
                item = state.applyTo(file.item, progress),
                size = size.dp,
                marks = marks,
            )
        }
    }

    Variants(
        title = "Formats",
        description = "Only a decoded picture is drawn as itself; everything else is its glyph and extension.",
        layout = VariantLayout.Horizontal,
    ) {
        SampleFileOption.entries.forEach { option ->
            variant(name = option.label, detail = option.item.fileName) {
                AttachmentThumbnail(item = option.item, size = ArecaTheme.spacing.xl8)
            }
        }
    }

    States(
        description = "The corner disc says where the bytes are; a veil and a spinner cover a file still being added.",
        layout = VariantLayout.Horizontal,
    ) {
        SampleStateOption.entries.forEach { option ->
            variant(name = option.label) {
                AttachmentThumbnail(
                    item = option.applyTo(AttachmentSamples.receipt),
                    size = ArecaTheme.spacing.xl8,
                )
            }
        }
    }

    Sizes(
        description = "The sides the module's own parts use; the loader is asked for the picture at this size.",
        layout = VariantLayout.Horizontal,
    ) {
        variant(name = "Row", detail = "40dp, marks off") {
            AttachmentThumbnail(item = AttachmentSamples.invoice, size = ArecaTheme.spacing.xl5, marks = false)
        }
        variant(name = "Compact", detail = "64dp") {
            AttachmentThumbnail(item = AttachmentSamples.invoice, size = ArecaTheme.spacing.xl7)
        }
        variant(name = "Strip", detail = "80dp") {
            AttachmentThumbnail(item = AttachmentSamples.invoice, size = ArecaTheme.spacing.xl8)
        }
        variant(name = "Details", detail = "96dp") {
            AttachmentThumbnail(item = AttachmentSamples.invoice, size = ArecaTheme.spacing.xl9)
        }
    }

    EdgeCases(layout = VariantLayout.Horizontal) {
        case(
            name = "No extension",
            expectation = "The glyph alone; nothing is invented to fill the space.",
        ) {
            AttachmentThumbnail(
                item = AttachmentSamples.contract.copy(fileName = "Lease agreement"),
                size = ArecaTheme.spacing.xl8,
            )
        }
        case(
            name = "Dot in the name",
            expectation = "\"Invoice 12.03.2026 corrected\" has no extension worth printing.",
        ) {
            AttachmentThumbnail(
                item = AttachmentSamples.contract.copy(fileName = "Invoice 12.03.2026 corrected"),
                size = ArecaTheme.spacing.xl8,
            )
        }
        case(
            name = "Marks on a small square",
            expectation = "At 40dp the disc covers most of the square; pass marks = false there.",
        ) {
            AttachmentThumbnail(
                item = SampleStateOption.Remote.applyTo(AttachmentSamples.receipt),
                size = ArecaTheme.spacing.xl5,
            )
        }
        case(
            name = "White square",
            expectation = "A container of the surface colour, for a strip on a grey sheet.",
        ) {
            AttachmentThumbnail(
                item = AttachmentSamples.invoice,
                size = ArecaTheme.spacing.xl8,
                style = AttachmentTileStyle.default(bg = ArecaTheme.colors.bgSurface),
            )
        }
    }
}
