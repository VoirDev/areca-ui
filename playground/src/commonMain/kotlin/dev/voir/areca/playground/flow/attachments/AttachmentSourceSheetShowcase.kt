package dev.voir.areca.playground.flow.attachments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import dev.voir.areca.flow.attachments.sheet.AttachmentChoice
import dev.voir.areca.flow.attachments.sheet.AttachmentSourceOptions
import dev.voir.areca.flow.attachments.sheet.AttachmentSourceSheet
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.button.ButtonSize
import dev.voir.areca.foundation.button.ButtonStyle
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.Scan
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme

/** Showcase for [AttachmentSourceSheet] and the [AttachmentSourceOptions] it offers. */
@Composable
fun AttachmentSourceSheetShowcase() = ShowcaseScaffold(
    title = "Source sheet",
    summary = "The sheet that asks where a new file comes from: the camera, the photo library, the " +
        "device's files, or a source of the product's own.",
    module = "flow-attachments",
    status = ShowcaseStatus.Beta,
) {
    Playground(description = "Pick which sources the platform can complete, open the sheet, and choose one.") {
        val camera by booleanControl(
            key = "Camera",
            default = true,
            description = "Offer it only where the platform can take a photograph.",
        )
        val library by booleanControl("Library", default = true)
        val files by booleanControl("Files", default = true)
        val custom by booleanControl(
            key = "Custom source",
            default = false,
            description = "A source of the product's own, beside the built-in ones.",
        )
        val title by textControl("title", default = "Add attachment")
        val subtitle by booleanControl("subtitle", default = true)

        preview {
            var visible by remember { mutableStateOf(false) }
            var chosen by remember { mutableStateOf("Nothing chosen yet") }

            fun choose(what: String) {
                chosen = what
                visible = false
            }

            Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md)) {
                Button(
                    text = "Open sheet",
                    onClick = { visible = true },
                    style = ButtonStyle.soft(ActionColor.Neutral),
                    size = ButtonSize.Small,
                )

                Text(text = chosen, variant = TextVariant.BodySmall, color = TextColor.Muted)
            }

            AttachmentSourceSheet(
                visible = visible,
                options = buildList {
                    if (camera) add(AttachmentSourceOptions.camera { choose("Chose the camera") })
                    if (library) add(AttachmentSourceOptions.library { choose("Chose the library") })
                    if (files) add(AttachmentSourceOptions.files { choose("Chose a file") })
                    if (custom) add(AttachmentChoice("Scan a document", ArecaIcons.Scan, { choose("Chose to scan") }))
                },
                onDismiss = { choose("Dismissed without a choice") },
                title = title,
                subtitle = "Up to 25 MB each. Photos are resized before they’re saved.".takeIf { subtitle },
            )
        }
    }

    Variants(
        title = "Sources",
        description = "Only what the platform can complete, in the order given.",
        layout = VariantLayout.Grid,
    ) {
        variant(name = "Camera / Library / Files", detail = "A phone") {
            SheetTrigger { visible, close ->
                AttachmentSourceSheet(
                    visible = visible,
                    options = listOf(
                        AttachmentSourceOptions.camera(onClick = close),
                        AttachmentSourceOptions.library(onClick = close),
                        AttachmentSourceOptions.files(onClick = close),
                    ),
                    onDismiss = close,
                )
            }
        }
        variant(name = "Library / Files", detail = "A tablet with no camera") {
            SheetTrigger { visible, close ->
                AttachmentSourceSheet(
                    visible = visible,
                    options = listOf(
                        AttachmentSourceOptions.library(onClick = close),
                        AttachmentSourceOptions.files(onClick = close),
                    ),
                    onDismiss = close,
                )
            }
        }
    }

    EdgeCases(layout = VariantLayout.Grid) {
        case(
            name = "No sources",
            expectation = "No empty block: the heading and Cancel. Better still, show no add square.",
        ) {
            SheetTrigger { visible, close ->
                AttachmentSourceSheet(visible = visible, options = emptyList(), onDismiss = close)
            }
        }
        case(
            name = "Long title",
            expectation = "Wraps to two lines, then ends in an ellipsis.",
        ) {
            SheetTrigger { visible, close ->
                AttachmentSourceSheet(
                    visible = visible,
                    options = listOf(AttachmentSourceOptions.files(onClick = close)),
                    onDismiss = close,
                    title = "Add a receipt, an invoice, a warranty card or any other document to this expense",
                )
            }
        }
    }
}
