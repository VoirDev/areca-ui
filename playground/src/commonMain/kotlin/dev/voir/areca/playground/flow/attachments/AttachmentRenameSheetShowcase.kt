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
import dev.voir.areca.flow.attachments.sheet.AttachmentRenameSheet
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

/** How long the playground's rename takes to save, so the spinner can be seen. */
private const val SaveMillis = 800L

/** Showcase for [AttachmentRenameSheet]. */
@Composable
fun AttachmentRenameSheetShowcase() = ShowcaseScaffold(
    title = "Rename sheet",
    summary = "Gives an attached file a new name, editing only the part before its extension.",
    module = "flow-attachments",
    status = ShowcaseStatus.Beta,
) {
    Playground(description = "Rename the file: the extension comes back on save, and an empty name is refused.") {
        val file by enumControl(
            key = "file",
            default = SampleFileOption.Pdf,
            options = SampleFileOption.entries.take(3),
            optionLabel = { it.label },
        )
        val refuseEmpty by booleanControl(
            key = "canSave",
            default = true,
            description = "The application refuses an empty name, which disables Save.",
        )
        val slow by booleanControl(
            key = "saving",
            default = true,
            description = "The rename takes a moment to save; Save shows its spinner meanwhile.",
        )

        preview {
            var renaming by remember { mutableStateOf<AttachmentItem?>(null) }
            var name by remember(file) { mutableStateOf(file.item.fileName) }
            var saving by remember { mutableStateOf<String?>(null) }

            LaunchedEffect(saving) {
                val saved = saving ?: return@LaunchedEffect
                delay(SaveMillis)
                name = saved
                saving = null
                renaming = null
            }

            Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md)) {
                Button(
                    text = "Rename",
                    onClick = { renaming = file.item.copy(fileName = name) },
                    style = ButtonStyle.soft(ActionColor.Neutral),
                    size = ButtonSize.Small,
                )

                Text(text = name, variant = TextVariant.BodySmall, color = TextColor.Muted)
            }

            AttachmentRenameSheet(
                item = renaming,
                onRename = { renamed ->
                    if (slow) {
                        saving = renamed
                    } else {
                        name = renamed
                        renaming = null
                    }
                },
                onDismiss = { renaming = null },
                canSave = if (refuseEmpty) ({ it.isNotBlank() }) else ({ true }),
                saving = saving != null,
            )
        }
    }

    States(layout = VariantLayout.Grid) {
        variant(name = "Default", detail = "The name without its extension, selected") {
            SheetTrigger { visible, close ->
                AttachmentRenameSheet(
                    item = AttachmentSamples.invoice.takeIf { visible },
                    onRename = { close() },
                    onDismiss = close,
                )
            }
        }
        variant(name = "Error", detail = "What the application said about the last name") {
            SheetTrigger { visible, close ->
                AttachmentRenameSheet(
                    item = AttachmentSamples.invoice.takeIf { visible },
                    onRename = {},
                    onDismiss = close,
                    error = "Another file on this expense already has this name.",
                )
            }
        }
        variant(name = "Saving", detail = "saving = true: nothing is pressable") {
            SheetTrigger { visible, close ->
                AttachmentRenameSheet(
                    item = AttachmentSamples.invoice.takeIf { visible },
                    onRename = {},
                    onDismiss = close,
                    saving = true,
                )
            }
        }
    }

    EdgeCases(layout = VariantLayout.Grid) {
        case(
            name = "No extension",
            expectation = "The whole name is edited, and saved as typed.",
        ) {
            SheetTrigger { visible, close ->
                AttachmentRenameSheet(
                    item = AttachmentSamples.contract.copy(fileName = "Lease agreement").takeIf { visible },
                    onRename = { close() },
                    onDismiss = close,
                )
            }
        }
        case(
            name = "Dot in the name",
            expectation = "\"Invoice 12.03.2026 corrected\" is edited whole, not cut at its last dot.",
        ) {
            SheetTrigger { visible, close ->
                AttachmentRenameSheet(
                    item = AttachmentSamples.contract.copy(fileName = "Invoice 12.03.2026 corrected").takeIf { visible },
                    onRename = { close() },
                    onDismiss = close,
                )
            }
        }
    }
}
