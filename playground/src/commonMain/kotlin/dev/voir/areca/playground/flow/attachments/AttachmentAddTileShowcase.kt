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
import dev.voir.areca.flow.attachments.part.AttachmentAddTile
import dev.voir.areca.flow.attachments.part.AttachmentTile
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Sizes
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.theme.ArecaTheme

/** Showcase for [AttachmentAddTile]. */
@Composable
fun AttachmentAddTileShowcase() = ShowcaseScaffold(
    title = "Add tile",
    summary = "The square that adds a file, shaped like the files beside it.",
    module = "flow-attachments",
    status = ShowcaseStatus.Beta,
) {
    Playground(description = "Press it; a disabled square ignores presses.") {
        val caption by textControl(
            key = "caption",
            default = "Add",
            description = "Empty draws no caption.",
        )
        val label by textControl(
            key = "label",
            default = "Add attachment",
            description = "Read out in place of the plus.",
        )
        val enabled by booleanControl("enabled", default = true)
        val size by numberControl(
            key = "size",
            default = 80f,
            range = 64f..128f,
            step = 8f,
            format = { "${it.toInt()}dp" },
        )

        preview {
            var presses by remember { mutableIntStateOf(0) }

            Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md)) {
                AttachmentAddTile(
                    onClick = { presses++ },
                    size = size.dp,
                    caption = caption.ifEmpty { null },
                    label = label,
                    enabled = enabled,
                )

                Text(
                    text = "Pressed $presses times",
                    variant = TextVariant.BodySmall,
                    color = TextColor.Muted,
                )
            }
        }
    }

    States(layout = VariantLayout.Horizontal) {
        variant(name = "Default") { AttachmentAddTile(onClick = {}, caption = "Add") }
        variant(name = "Disabled", detail = "enabled = false") {
            AttachmentAddTile(onClick = {}, caption = "Add", enabled = false)
        }
        variant(name = "No caption", detail = "caption = null") { AttachmentAddTile(onClick = {}) }
    }

    Sizes(layout = VariantLayout.Horizontal) {
        variant(name = "Strip", detail = "80dp") { AttachmentAddTile(onClick = {}, size = ArecaTheme.spacing.xl8) }
        variant(name = "Details", detail = "96dp") { AttachmentAddTile(onClick = {}, size = ArecaTheme.spacing.xl9) }
    }

    EdgeCases(layout = VariantLayout.Horizontal) {
        case(
            name = "Beside files",
            expectation = "First in the row, the same size, its caption on the files' baseline.",
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md)) {
                AttachmentAddTile(onClick = {}, caption = "Add")
                AttachmentTile(item = AttachmentSamples.receipt, onClick = {})
                AttachmentTile(item = AttachmentSamples.invoice, onClick = {})
            }
        }
        case(
            name = "Long caption",
            expectation = "One line, ending in an ellipsis at the square's width.",
        ) {
            AttachmentAddTile(onClick = {}, caption = "Add another document")
        }
    }
}
