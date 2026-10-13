package dev.voir.areca.playground.flow.attachments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import dev.voir.areca.flow.attachments.part.AttachmentTile
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.section.Anatomy
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.theme.ArecaTheme

/** Showcase for [AttachmentTile]. */
@Composable
fun AttachmentTileShowcase() = ShowcaseScaffold(
    title = "Tile",
    summary = "One file in a strip: its square, what it is, and optionally its size and a remove mark.",
    module = "flow-attachments",
    status = ShowcaseStatus.Beta,
) {
    Playground(description = "Press the square and the remove mark; a file still being added ignores presses.") {
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
        val caption by textControl(
            key = "caption",
            default = "Receipt",
            description = "Empty draws no caption.",
        )
        val detail by booleanControl(
            key = "detail",
            default = false,
            description = "The file's size as a second line, as on a record's details.",
        )
        val removable by booleanControl(
            key = "onRemove",
            default = true,
            description = "The remove mark, for a form that saves nothing until it is saved.",
        )
        val pressable by booleanControl("onClick", default = true)
        val size by numberControl(
            key = "size",
            default = 80f,
            range = 64f..128f,
            step = 8f,
            format = { "${it.toInt()}dp" },
        )

        preview {
            var opened by remember { mutableIntStateOf(0) }
            var removed by remember { mutableIntStateOf(0) }
            val item = state.applyTo(file.item)

            Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md)) {
                AttachmentTile(
                    item = item,
                    onClick = if (pressable) ({ opened++ }) else null,
                    size = size.dp,
                    caption = caption.ifEmpty { null },
                    detail = item.size.takeIf { detail },
                    onRemove = if (removable) ({ removed++ }) else null,
                )

                Text(
                    text = "Opened $opened times · removed $removed times",
                    variant = TextVariant.BodySmall,
                    color = TextColor.Muted,
                )
            }
        }
    }

    Anatomy(description = "A square, a caption, an optional second line and an optional remove mark.") {
        slot(
            name = "Caption",
            description = "The file's label — the word to scan a strip for — not its name.",
        ) {
            AttachmentTile(item = AttachmentSamples.receipt, onClick = {})
        }
        slot(
            name = "Detail",
            description = "A quieter second line; usually the size, where opening costs a download.",
        ) {
            AttachmentTile(
                item = AttachmentSamples.warranty,
                onClick = {},
                size = ArecaTheme.spacing.xl9,
                detail = AttachmentSamples.warranty.size,
            )
        }
        slot(
            name = "Remove mark",
            description = "A small disc; its pressable area is the whole corner around it.",
        ) {
            AttachmentTile(item = AttachmentSamples.invoice, onClick = {}, onRemove = {})
        }
    }

    States(layout = VariantLayout.Horizontal) {
        SampleStateOption.entries.forEach { option ->
            variant(name = option.label) {
                AttachmentTile(
                    item = option.applyTo(AttachmentSamples.receipt),
                    onClick = {},
                    caption = if (option == SampleStateOption.Adding) "Adding…" else AttachmentSamples.receipt.label,
                    detail = AttachmentSamples.receipt.size,
                )
            }
        }
        variant(name = "Not pressable", detail = "onClick = null") {
            AttachmentTile(item = AttachmentSamples.invoice, onClick = null)
        }
    }

    EdgeCases(layout = VariantLayout.Horizontal) {
        case(
            name = "Long label",
            expectation = "One line, ending in an ellipsis at the square's width.",
        ) {
            AttachmentTile(
                item = AttachmentSamples.contract.copy(label = "Tenancy agreement, second copy"),
                onClick = {},
            )
        }
        case(
            name = "No caption",
            expectation = "The square alone; a detail line, if any, moves up.",
        ) {
            AttachmentTile(item = AttachmentSamples.receipt, onClick = {}, caption = null)
        }
        case(
            name = "Label is the name",
            expectation = "No chosen word: the name is the caption, read out once.",
        ) {
            AttachmentTile(
                item = AttachmentSamples.receipt.copy(label = AttachmentSamples.receipt.fileName),
                onClick = {},
            )
        }
        case(
            name = "Side by side",
            expectation = "Captions line up; the remove mark never overlaps the next square.",
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md)) {
                AttachmentSamples.expenseFiles.take(3).forEach { item ->
                    AttachmentTile(item = item, onClick = {}, onRemove = {})
                }
            }
        }
    }
}
