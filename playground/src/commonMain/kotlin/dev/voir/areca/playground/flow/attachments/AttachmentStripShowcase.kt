package dev.voir.areca.playground.flow.attachments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import dev.voir.areca.flow.attachments.AttachmentItem
import dev.voir.areca.flow.attachments.part.AttachmentStrip
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.theme.ArecaTheme

/** Where the playground strip sits, which decides the size of its squares and what it offers. */
private enum class StripPlacement(
    /** Label of the option in the control panel. */
    val label: String,
) {
    /** On the form that writes the record: add square, remove marks, captions only. */
    Form("Form"),

    /** On the record's details: larger squares with their sizes, nothing to add or remove. */
    Details("Details"),
}

/** How many files the playground record may carry. */
private const val SampleLimit = 6

/** Showcase for [AttachmentStrip]. */
@Composable
fun AttachmentStripShowcase() = ShowcaseScaffold(
    title = "Strip",
    summary = "A record's files as a row of squares that scrolls sideways, from nothing to many, on " +
        "the form that writes the record or on its details.",
    module = "flow-attachments",
    status = ShowcaseStatus.Beta,
) {
    Playground(description = "Add and remove files: the counter follows, and the add square goes at the limit.") {
        val placement by enumControl(
            key = "placement",
            default = StripPlacement.Form,
            options = StripPlacement.entries,
            optionLabel = { it.label },
        )
        val start by numberControl(
            key = "files",
            default = 2f,
            range = 0f..4f,
            step = 1f,
            format = { it.toInt().toString() },
            description = "How many files the record starts with.",
        )
        val title by booleanControl("title", default = true)
        val counter by booleanControl("counter", default = true)
        val failure by booleanControl(
            key = "failure",
            default = false,
            description = "Why the last change could not be made, announced under the strip.",
        )

        preview {
            val files = remember(start) {
                mutableStateListOf<AttachmentItem>().apply { addAll(AttachmentSamples.expenseFiles.take(start.toInt())) }
            }
            var added by remember(start) { mutableIntStateOf(0) }
            var opened by remember { mutableIntStateOf(0) }
            val form = placement == StripPlacement.Form

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
            ) {
                AttachmentStrip(
                    items = files,
                    onOpen = { opened++ },
                    onAdd = if (form && files.size < SampleLimit) {
                        {
                            val next = AttachmentSamples.filesToAdd[added % AttachmentSamples.filesToAdd.size]
                            files += next.copy(key = "${next.key}-${added++}")
                        }
                    } else {
                        null
                    },
                    onRemove = if (form) ({ item -> files.remove(item) }) else null,
                    title = "Attachments".takeIf { title },
                    counter = "${files.size} of $SampleLimit".takeIf { counter && files.isNotEmpty() },
                    emptyTitle = "Add a receipt or file",
                    emptyDescription = "Photos, PDFs and documents, up to 25\u00A0MB each",
                    failure = "This file is larger than 25 MB.".takeIf { failure },
                    tileSize = if (form) ArecaTheme.spacing.xl8 else ArecaTheme.spacing.xl9,
                    detail = { item -> item.size.takeUnless { form } },
                    addCaption = "Add",
                )

                Text(
                    text = "Opened $opened times",
                    variant = TextVariant.BodySmall,
                    color = TextColor.Muted,
                    modifier = Modifier.padding(horizontal = ArecaTheme.spacing.horizontalContentInsets),
                )
            }
        }
    }

    States(layout = VariantLayout.Vertical) {
        variant(name = "Empty", detail = "The add square and what can go there") {
            AttachmentStrip(
                items = emptyList(),
                onOpen = {},
                onAdd = {},
                title = "Attachments",
                emptyTitle = "Add a receipt or file",
                emptyDescription = "Photos, PDFs and documents, up to 25\u00A0MB each",
            )
        }
        variant(name = "With files", detail = "Add square first, then every file") {
            AttachmentStrip(
                items = AttachmentSamples.expenseFiles,
                onOpen = {},
                onAdd = {},
                onRemove = {},
                title = "Attachments",
                counter = "4 of 20",
                addCaption = "Add",
            )
        }
        variant(name = "At the limit", detail = "onAdd = null") {
            AttachmentStrip(
                items = AttachmentSamples.expenseFiles,
                onOpen = {},
                onRemove = {},
                title = "Attachments",
                counter = "4 of 4",
            )
        }
        variant(name = "Every state", detail = "Adding, uploading, ready, remote, downloading, waiting, gone") {
            AttachmentStrip(
                items = SampleStateOption.entries.map { option ->
                    option.applyTo(AttachmentSamples.receipt).copy(key = option.name, label = option.label)
                },
                onOpen = {},
                title = "Attachments",
            )
        }
        variant(name = "Failure", detail = "Stated under the strip and announced") {
            AttachmentStrip(
                items = AttachmentSamples.expenseFiles.take(2),
                onOpen = {},
                onAdd = {},
                onRemove = {},
                title = "Attachments",
                failure = "This file is larger than 25 MB.",
                addCaption = "Add",
            )
        }
    }

    Recipes {
        recipe(
            name = "On a form",
            description = "Under the note of a new expense: add, remove, nothing saved until the form is.",
        ) {
            AttachmentStrip(
                items = AttachmentSamples.expenseFiles.take(2),
                onOpen = {},
                onAdd = {},
                onRemove = {},
                title = "Attachments",
                counter = "2 of 20",
                addCaption = "Add",
            )
        }
        recipe(
            name = "On a record's details",
            description = "Inside a padded sheet: larger squares with their sizes, reaching past the " +
                "padding with bleed = true.",
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(ArecaTheme.shapes.lg)
                    .background(ArecaTheme.colors.bgSurface)
                    .padding(ArecaTheme.spacing.horizontalContentInsets),
            ) {
                AttachmentStrip(
                    items = AttachmentSamples.expenseFiles,
                    onOpen = {},
                    title = "Attachments",
                    tileSize = ArecaTheme.spacing.xl9,
                    detail = { item -> item.size },
                    bleed = true,
                )
            }
        }
    }

    EdgeCases(layout = VariantLayout.Vertical) {
        case(
            name = "Nothing to show",
            expectation = "No files, no add square and no failure: nothing is drawn, not even the title.",
        ) {
            AttachmentStrip(items = emptyList(), onOpen = {}, title = "Attachments")
        }
        case(
            name = "Failure with no files",
            expectation = "The failure alone, where the file that failed would have been.",
        ) {
            AttachmentStrip(
                items = emptyList(),
                onOpen = {},
                title = "Attachments",
                failure = "The file couldn’t be attached. Try again.",
            )
        }
        case(
            name = "Many files",
            expectation = "Scrolls sideways under both edges; the first square keeps to the gutter.",
        ) {
            AttachmentStrip(
                items = AttachmentSamples.expenseFiles + AttachmentSamples.filesToAdd,
                onOpen = {},
                onAdd = {},
                title = "Attachments",
                counter = "8 of 20",
                addCaption = "Add",
            )
        }
        case(
            name = "Long title",
            expectation = "The title ends in an ellipsis; the counter is never pushed off.",
        ) {
            AttachmentStrip(
                items = AttachmentSamples.expenseFiles.take(1),
                onOpen = {},
                title = "Receipts, invoices and every other document for this expense",
                counter = "1 of 20",
            )
        }
    }
}
