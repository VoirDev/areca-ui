package dev.voir.areca.playground.flow.attachments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import dev.voir.areca.flow.attachments.sheet.AttachmentChoice
import dev.voir.areca.flow.attachments.sheet.AttachmentChoiceLayout
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
        "device's files, or a source of the product's own — as rows, or as a bento of cards.",
    module = "flow-attachments",
    status = ShowcaseStatus.Beta,
) {
    Playground(description = "Pick which sources the platform can complete, open the sheet, and choose one.") {
        val layout by enumControl(
            key = "layout",
            default = AttachmentChoiceLayout.Rows,
            options = AttachmentChoiceLayout.entries,
            description = "Rows lie flat on the sheet; a bento makes each source a card in its hue, the first larger.",
        )
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
                    if (custom) {
                        add(
                            AttachmentChoice(
                                label = "Scan a document",
                                icon = ArecaIcons.Scan,
                                onClick = { choose("Chose to scan") },
                                description = "Straightened and cropped",
                                hue = ArecaTheme.hues.teal,
                            ),
                        )
                    }
                },
                onDismiss = { choose("Dismissed without a choice") },
                title = title,
                subtitle = "Up to 25 MB each. Photos are resized before they’re saved.".takeIf { subtitle },
                layout = layout,
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

    Variants(
        title = "Layouts",
        description = "Rows are the flat sheet every choice takes. A bento is for a few fixed sources where " +
            "one leads: the first is a full-width card, the rest share a row, each washed in its hue with " +
            "a line on what it is for, and the subtitle sits centred under them.",
        layout = VariantLayout.Grid,
    ) {
        variant(name = "Rows", detail = "AttachmentChoiceLayout.Rows, the default") {
            SheetTrigger { visible, close ->
                AttachmentSourceSheet(
                    visible = visible,
                    options = listOf(
                        AttachmentSourceOptions.camera(onClick = close),
                        AttachmentSourceOptions.library(onClick = close),
                        AttachmentSourceOptions.files(onClick = close),
                    ),
                    onDismiss = close,
                    subtitle = "Up to 25 MB each. Photos are resized before they’re saved.",
                )
            }
        }
        variant(name = "Bento", detail = "AttachmentChoiceLayout.Bento: the camera leads") {
            SheetTrigger { visible, close ->
                AttachmentSourceSheet(
                    visible = visible,
                    options = listOf(
                        AttachmentSourceOptions.camera(description = "Snap a receipt or a bill", onClick = close),
                        AttachmentSourceOptions.library(onClick = close),
                        AttachmentSourceOptions.files(onClick = close),
                    ),
                    onDismiss = close,
                    subtitle = "Up to 25 MB each. Photos are resized before they’re saved.",
                    layout = AttachmentChoiceLayout.Bento,
                )
            }
        }
        variant(name = "Bento, two sources", detail = "No camera: the two share one row as halves") {
            SheetTrigger { visible, close ->
                AttachmentSourceSheet(
                    visible = visible,
                    options = listOf(
                        AttachmentSourceOptions.library(onClick = close),
                        AttachmentSourceOptions.files(onClick = close),
                    ),
                    onDismiss = close,
                    subtitle = "Up to 25 MB each.",
                    layout = AttachmentChoiceLayout.Bento,
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
            name = "Bento, long words",
            expectation = "Labels and descriptions wrap to two lines, then end in an ellipsis; the row of " +
                "cards stays as tall as its tallest.",
        ) {
            SheetTrigger { visible, close ->
                AttachmentSourceSheet(
                    visible = visible,
                    options = listOf(
                        AttachmentSourceOptions.camera(onClick = close),
                        AttachmentSourceOptions.library(
                            label = "Choose from the photo library on this device",
                            description = "Pictures and screenshots you have already taken, newest first",
                            onClick = close,
                        ),
                        AttachmentSourceOptions.files(onClick = close),
                    ),
                    onDismiss = close,
                    layout = AttachmentChoiceLayout.Bento,
                )
            }
        }
        case(
            name = "Bento, a source with no hue",
            expectation = "A choice of the product's own with no hue is grey; with no description, its label alone.",
        ) {
            SheetTrigger { visible, close ->
                AttachmentSourceSheet(
                    visible = visible,
                    options = listOf(
                        AttachmentSourceOptions.camera(onClick = close),
                        AttachmentChoice("Scan a document", ArecaIcons.Scan, close),
                        AttachmentSourceOptions.files(onClick = close),
                    ),
                    onDismiss = close,
                    layout = AttachmentChoiceLayout.Bento,
                )
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
