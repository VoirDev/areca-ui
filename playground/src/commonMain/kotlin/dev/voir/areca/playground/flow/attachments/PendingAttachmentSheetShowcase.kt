package dev.voir.areca.playground.flow.attachments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import dev.voir.areca.flow.attachments.AttachmentItem
import dev.voir.areca.flow.attachments.sheet.AttachmentTagChips
import dev.voir.areca.flow.attachments.sheet.PendingAttachment
import dev.voir.areca.flow.attachments.sheet.PendingAttachmentSheet
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.button.ButtonSize
import dev.voir.areca.foundation.button.ButtonStyle
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.theme.ArecaTheme
import kotlinx.coroutines.delay

/** How the playground's pick turns out. */
private enum class PickOutcome(
    /** Label of the option in the control panel. */
    val label: String,
) {
    /** Copied in after a moment, ready to attach. */
    Copied("Copied"),

    /** Refused once copied: larger than the product allows. */
    TooLarge("Too large"),

    /** Failed before the file was even named. */
    Failed("Pick failed"),
}

/** How long the playground's copy takes, so the spinner and the waiting Attach can be seen. */
private const val CopyMillis = 1_500L

/**
 * [item] as the sheet shows it once the copy has finished.
 *
 * @receiver The file that was picked.
 * @return The pending file, copied.
 */
private fun AttachmentItem.copied(): PendingAttachment = PendingAttachment(
    fileName = fileName,
    format = format,
    size = size,
    model = model,
)

/** Showcase for [PendingAttachmentSheet] and the [PendingAttachment] it draws. */
@Composable
fun PendingAttachmentSheetShowcase() = ShowcaseScaffold(
    title = "Pending file sheet",
    summary = "Asked once a file has been picked, before it is attached: the file, and what the " +
        "product wants to know about it.",
    module = "flow-attachments",
    status = ShowcaseStatus.Beta,
) {
    Playground(description = "Simulate a pick: the copy runs while the sheet is up, and Attach waits for it.") {
        val file by enumControl(
            key = "file",
            default = SampleFileOption.Photo,
            options = SampleFileOption.entries.take(3),
            optionLabel = { it.label },
        )
        val outcome by enumControl(
            key = "outcome",
            default = PickOutcome.Copied,
            options = PickOutcome.entries,
            optionLabel = { it.label },
        )
        val ask by booleanControl(
            key = "question",
            default = true,
            description = "Ask what the file is, and attach only once it is answered.",
        )

        preview {
            var pending by remember { mutableStateOf<PendingAttachment?>(null) }
            var tag by remember { mutableStateOf<SampleTag?>(null) }
            var result by remember { mutableStateOf("Nothing attached yet") }
            val picked = file.item

            // The application's copy, finishing a moment after the pick.
            LaunchedEffect(pending?.isCopying) {
                if (pending?.isCopying == true) {
                    delay(CopyMillis)
                    pending = when (outcome) {
                        PickOutcome.Copied -> picked.copied()
                        PickOutcome.TooLarge -> picked.copied().copy(problem = "This file is larger than 25 MB.")
                        PickOutcome.Failed -> pending
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md)) {
                Button(
                    text = "Pick a file",
                    onClick = {
                        tag = null
                        pending = if (outcome == PickOutcome.Failed) {
                            PendingAttachment(fileName = null, problem = "The file couldn’t be read. Try another one.")
                        } else {
                            PendingAttachment(fileName = picked.fileName, format = picked.format, isCopying = true)
                        }
                    },
                    style = ButtonStyle.soft(ActionColor.Neutral),
                    size = ButtonSize.Small,
                )

                Text(text = result, variant = TextVariant.BodySmall, color = TextColor.Muted)
            }

            PendingAttachmentSheet(
                file = pending,
                onAttach = {
                    result = "Attached ${picked.fileName}" + (tag?.let { " as ${it.label}" } ?: "")
                    pending = null
                },
                onDismiss = { pending = null },
                title = if (ask) "What is this?" else "Add this file?",
                attachEnabled = !ask || tag != null,
                copyingText = "Adding…",
                question = if (ask) {
                    {
                        AttachmentTagChips(
                            tags = SampleTag.entries,
                            selected = tag,
                            onSelect = { chosen -> tag = chosen },
                            label = { it.label },
                        )
                    }
                } else {
                    null
                },
            )
        }
    }

    States(layout = VariantLayout.Grid) {
        variant(name = "Copying", detail = "Spinner in the square; Attach waits") {
            SheetTrigger { visible, close ->
                PendingAttachmentSheet(
                    file = PendingAttachment(fileName = "IMG_2041.jpg", isCopying = true).takeIf { visible },
                    onAttach = close,
                    onDismiss = close,
                    copyingText = "Adding…",
                )
            }
        }
        variant(name = "Ready", detail = "The picture, its name and size") {
            SheetTrigger { visible, close ->
                PendingAttachmentSheet(
                    file = AttachmentSamples.receipt.copied().takeIf { visible },
                    onAttach = close,
                    onDismiss = close,
                )
            }
        }
        variant(name = "Tag not chosen", detail = "attachEnabled = false") {
            SheetTrigger { visible, close ->
                PendingAttachmentSheet(
                    file = AttachmentSamples.invoice.copied().takeIf { visible },
                    onAttach = close,
                    onDismiss = close,
                    title = "What is this?",
                    attachEnabled = false,
                    question = {
                        AttachmentTagChips(
                            tags = SampleTag.entries,
                            selected = null,
                            onSelect = {},
                            label = { it.label },
                        )
                    },
                )
            }
        }
        variant(name = "Refused", detail = "The reason, announced, and Close") {
            SheetTrigger { visible, close ->
                PendingAttachmentSheet(
                    file = AttachmentSamples.statement.copied()
                        .copy(problem = "This file is larger than 25 MB.")
                        .takeIf { visible },
                    onAttach = close,
                    onDismiss = close,
                )
            }
        }
    }

    EdgeCases(layout = VariantLayout.Grid) {
        case(
            name = "Pick failed",
            expectation = "No file row, since there is no file: the heading, the reason and Close.",
        ) {
            SheetTrigger { visible, close ->
                PendingAttachmentSheet(
                    file = PendingAttachment(
                        fileName = null,
                        problem = "The file couldn’t be read. Try another one.",
                    ).takeIf { visible },
                    onAttach = close,
                    onDismiss = close,
                )
            }
        }
        case(
            name = "Very long file name",
            expectation = "One line, shortened in the middle so the extension stays readable.",
        ) {
            SheetTrigger { visible, close ->
                PendingAttachmentSheet(
                    file = AttachmentSamples.statement.copied()
                        .copy(fileName = "Statement for the joint current account, September 2026, final copy.pdf")
                        .takeIf { visible },
                    onAttach = close,
                    onDismiss = close,
                )
            }
        }
    }
}
