package dev.voir.areca.playground.flow.attachments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import dev.voir.areca.flow.attachments.AttachmentItem
import dev.voir.areca.flow.attachments.AttachmentState
import dev.voir.areca.flow.attachments.sheet.AttachmentOptions
import dev.voir.areca.flow.attachments.sheet.AttachmentOptionsSheet
import dev.voir.areca.flow.attachments.sheet.AttachmentTagChips
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
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme

/** Showcase for [AttachmentOptionsSheet] and the [AttachmentOptions] it offers. */
@Composable
fun AttachmentOptionsSheetShowcase() = ShowcaseScaffold(
    title = "Options sheet",
    summary = "What can be done to one attached file, headed by the file, with what it is changed " +
        "in one tap.",
    module = "flow-attachments",
    status = ShowcaseStatus.Beta,
) {
    Playground(description = "Open the sheet, change the tag — the sheet stays up — or choose an action.") {
        val file by enumControl(
            key = "file",
            default = SampleFileOption.Pdf,
            options = SampleFileOption.entries.take(3),
            optionLabel = { it.label },
        )
        val onDevice by booleanControl(
            key = "on this device",
            default = true,
            description = "Share and save are offered only for a file whose bytes are here.",
        )
        val tags by booleanControl(
            key = "tags",
            default = true,
            description = "The product tags files, so the tag leads the sheet.",
        )

        preview {
            var open by remember { mutableStateOf(false) }
            var tag by remember { mutableStateOf(SampleTag.Invoice) }
            var chosen by remember { mutableStateOf("Nothing chosen yet") }
            val item: AttachmentItem = if (onDevice) {
                file.item
            } else {
                SampleStateOption.Remote.applyTo(file.item)
            }.copy(label = tag.label)

            fun choose(what: String) {
                chosen = what
                open = false
            }

            Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md)) {
                Button(
                    text = "Open sheet",
                    onClick = { open = true },
                    style = ButtonStyle.soft(ActionColor.Neutral),
                    size = ButtonSize.Small,
                )

                Text(text = "${item.label} · $chosen", variant = TextVariant.BodySmall, color = TextColor.Muted)
            }

            AttachmentOptionsSheet(
                item = item.takeIf { open },
                choices = buildList {
                    add(AttachmentOptions.rename { choose("Chose to rename") })
                    if (item.state == AttachmentState.Ready) {
                        add(AttachmentOptions.share { choose("Chose to share") })
                        add(AttachmentOptions.save { choose("Chose to save") })
                    }
                    add(AttachmentOptions.remove { choose("Chose to remove") })
                },
                onDismiss = { open = false },
                tagsTitle = "Type".takeIf { tags },
                tags = if (tags) {
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

    Variants(
        title = "Choices",
        description = "Only what applies to the file, in the order given.",
        layout = VariantLayout.Grid,
    ) {
        variant(name = "On this device", detail = "Rename, share, save, remove") {
            SheetTrigger { visible, close ->
                AttachmentOptionsSheet(
                    item = AttachmentSamples.invoice.takeIf { visible },
                    choices = listOf(
                        AttachmentOptions.rename(onClick = close),
                        AttachmentOptions.share(onClick = close),
                        AttachmentOptions.save(onClick = close),
                        AttachmentOptions.remove(onClick = close),
                    ),
                    onDismiss = close,
                )
            }
        }
        variant(name = "On the server only", detail = "Nothing to share or save yet") {
            SheetTrigger { visible, close ->
                AttachmentOptionsSheet(
                    item = AttachmentSamples.warranty.takeIf { visible },
                    choices = listOf(
                        AttachmentOptions.rename(onClick = close),
                        AttachmentOptions.remove(onClick = close),
                    ),
                    onDismiss = close,
                )
            }
        }
        variant(name = "Document", detail = "Opened elsewhere") {
            SheetTrigger { visible, close ->
                AttachmentOptionsSheet(
                    item = AttachmentSamples.contract.takeIf { visible },
                    choices = listOf(
                        AttachmentOptions.openElsewhere(onClick = close),
                        AttachmentOptions.rename(onClick = close),
                        AttachmentOptions.remove(onClick = close),
                    ),
                    onDismiss = close,
                )
            }
        }
    }

    EdgeCases(layout = VariantLayout.Grid) {
        case(
            name = "Very long file name",
            expectation = "The heading wraps to two lines, then ends in an ellipsis.",
        ) {
            SheetTrigger { visible, close ->
                AttachmentOptionsSheet(
                    item = AttachmentSamples.statement
                        .copy(fileName = "Statement for the joint current account, September 2026, final copy.pdf")
                        .takeIf { visible },
                    choices = listOf(AttachmentOptions.remove(onClick = close)),
                    onDismiss = close,
                )
            }
        }
        case(
            name = "Label as heading",
            expectation = "title = { it.label }: the tag heads the sheet, the name goes under it.",
        ) {
            SheetTrigger { visible, close ->
                AttachmentOptionsSheet(
                    item = AttachmentSamples.receipt.takeIf { visible },
                    choices = listOf(AttachmentOptions.rename(onClick = close)),
                    onDismiss = close,
                    title = { it.label },
                    subtitle = { listOfNotNull(it.fileName, it.size).joinToString(" · ") },
                )
            }
        }
    }
}
