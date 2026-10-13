package dev.voir.areca.playground.flow.attachments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import dev.voir.areca.flow.attachments.AttachmentItem
import dev.voir.areca.flow.attachments.AttachmentState
import dev.voir.areca.flow.attachments.sheet.AttachmentOptions
import dev.voir.areca.flow.attachments.sheet.AttachmentOptionsSheet
import dev.voir.areca.flow.attachments.sheet.AttachmentRenameSheet
import dev.voir.areca.flow.attachments.sheet.AttachmentTagChips
import dev.voir.areca.flow.attachments.viewer.AttachmentViewer
import dev.voir.areca.flow.attachments.viewer.AttachmentViewerPage
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.button.ButtonSize
import dev.voir.areca.foundation.button.ButtonStyle
import dev.voir.areca.foundation.overlay.ConfirmSheet
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.Trash
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/** Which files the playground viewer opens on. */
private enum class ViewerFiles(
    /** Label of the option in the control panel. */
    val label: String,
    /** The files. */
    val items: List<AttachmentItem>,
) {
    /** A photo, a PDF, a document and a file on the server only. */
    Expense("Expense", AttachmentSamples.expenseFiles + AttachmentSamples.statement),

    /** One file: no position, no filmstrip. */
    Single("One file", listOf(AttachmentSamples.receipt)),

    /** A file in every state the viewer draws differently. */
    States(
        "Every state",
        SampleStateOption.entries.map { option ->
            option.applyTo(AttachmentSamples.invoice).copy(key = option.name, label = option.label)
        },
    ),
}

/** Steps of the playground's simulated download, each a tenth of the file. */
private const val DownloadSteps = 10

/** Time between those steps. */
private const val DownloadStepMillis = 180L

/** Showcase for [AttachmentViewer]. */
@Composable
fun AttachmentViewerShowcase() = ShowcaseScaffold(
    title = "Viewer",
    summary = "A record's files one at a time, full screen: swiped between, zoomed, read page by page, " +
        "fetched when opened, and acted on through sheets of the application's own.",
    module = "flow-attachments",
    status = ShowcaseStatus.Beta,
) {
    Playground(
        description = "Open the viewer. Landing on the warranty downloads it; the options rename and " +
            "remove for real, and removing the last file closes the viewer.",
    ) {
        val files by enumControl(
            key = "files",
            default = ViewerFiles.Expense,
            options = ViewerFiles.entries,
            optionLabel = { it.label },
        )
        val share by booleanControl(
            key = "onShare",
            default = true,
            description = "Offered only while the file on screen is on this device.",
        )
        val options by booleanControl("onMore", default = true)
        val confirmRemoval by booleanControl(
            key = "confirm removal",
            default = true,
            description = "Ask first, as on a saved record; a form removes straight away.",
        )

        preview {
            val scope = rememberCoroutineScope()
            val record = remember(files) { mutableStateListOf<AttachmentItem>().apply { addAll(files.items) } }
            var viewing by remember { mutableStateOf<Any?>(null) }
            var optionsFor by remember { mutableStateOf<AttachmentItem?>(null) }
            var renaming by remember { mutableStateOf<AttachmentItem?>(null) }
            var removing by remember { mutableStateOf<AttachmentItem?>(null) }
            var notice by remember { mutableStateOf<String?>(null) }

            fun update(key: Any, change: (AttachmentItem) -> AttachmentItem) {
                val index = record.indexOfFirst { it.key == key }
                if (index >= 0) record[index] = change(record[index])
            }

            // The application's download: a tenth at a time, then the bytes are here.
            fun download(item: AttachmentItem) {
                scope.launch {
                    for (step in 0..DownloadSteps) {
                        update(item.key) { it.copy(state = AttachmentState.Downloading(step / DownloadSteps.toFloat())) }
                        delay(DownloadStepMillis)
                    }
                    update(item.key) { it.copy(state = AttachmentState.Ready, model = SampleFile.ReceiptPhoto) }
                }
            }

            fun remove(item: AttachmentItem) {
                record.removeAll { it.key == item.key }
            }

            Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md)) {
                Button(
                    text = "Open viewer",
                    onClick = { viewing = record.firstOrNull()?.key },
                    style = ButtonStyle.soft(ActionColor.Neutral),
                    size = ButtonSize.Small,
                    enabled = record.isNotEmpty(),
                )

                Text(
                    text = "${record.size} files on the record",
                    variant = TextVariant.BodySmall,
                    color = TextColor.Muted,
                )
            }

            if (viewing != null) {
                AttachmentViewer(
                    items = record,
                    initialKey = viewing,
                    onDismiss = { viewing = null },
                    onRequest = ::download,
                    onShare = if (share) ({ item -> notice = "The share sheet opens here for ${item.fileName}." }) else null,
                    onMore = if (options) ({ item -> optionsFor = item }) else null,
                    notice = notice,
                    onNoticeDismiss = { notice = null },
                    page = { item ->
                        AttachmentViewerPage(
                            item = item,
                            onRetry = { download(item) },
                            onOpenElsewhere = { notice = "No app on this device opens this kind of file." },
                            onSave = { notice = "Saved to Downloads." },
                            waitingText = "It’s saved to your account and will download once you’re back online.",
                            unavailableText = "It couldn’t be downloaded and won’t be tried again. You can remove it.",
                            elsewhereText = "This kind of file can’t be shown here. Open it in another app to see it.",
                        )
                    },
                    sheets = {
                        AttachmentOptionsSheet(
                            item = optionsFor,
                            choices = buildList {
                                add(AttachmentOptions.rename { renaming = optionsFor; optionsFor = null })
                                if (optionsFor?.state == AttachmentState.Ready) {
                                    add(AttachmentOptions.save { notice = "Saved to Downloads."; optionsFor = null })
                                }
                                add(
                                    AttachmentOptions.remove {
                                        val item = optionsFor
                                        optionsFor = null
                                        if (item != null) if (confirmRemoval) removing = item else remove(item)
                                    },
                                )
                            },
                            onDismiss = { optionsFor = null },
                            tagsTitle = "Type",
                            tags = {
                                AttachmentTagChips(
                                    tags = SampleTag.entries,
                                    selected = SampleTag.entries.find { it.label == optionsFor?.label },
                                    onSelect = { tag ->
                                        optionsFor?.let { item ->
                                            update(item.key) { it.copy(label = tag.label) }
                                            optionsFor = item.copy(label = tag.label)
                                        }
                                    },
                                    label = { it.label },
                                )
                            },
                        )

                        AttachmentRenameSheet(
                            item = renaming,
                            onRename = { name ->
                                renaming?.let { item -> update(item.key) { it.copy(fileName = name) } }
                                renaming = null
                            },
                            onDismiss = { renaming = null },
                            canSave = { it.isNotBlank() },
                        )

                        ConfirmSheet(
                            visible = removing != null,
                            icon = ArecaIcons.Trash,
                            title = "Remove this attachment?",
                            description = "It will be removed from this expense on all your devices.",
                            confirmText = "Remove",
                            onConfirm = {
                                removing?.let(::remove)
                                removing = null
                            },
                            onDismiss = { removing = null },
                        )
                    },
                )
            }
        }
    }

    Variants(
        title = "What is on screen",
        description = "One viewer, opened on different files: the heading, position and filmstrip follow.",
        layout = VariantLayout.Grid,
    ) {
        ViewerFiles.entries.forEach { files ->
            variant(name = files.label, detail = "${files.items.size} files") {
                SheetTrigger(label = "Open viewer") { visible, close ->
                    if (visible) AttachmentViewer(items = files.items, onDismiss = close)
                }
            }
        }
    }

    EdgeCases(layout = VariantLayout.Grid) {
        case(
            name = "Opened on the third file",
            expectation = "initialKey picks the page; the filmstrip starts scrolled to it.",
        ) {
            SheetTrigger(label = "Open viewer") { visible, close ->
                if (visible) {
                    AttachmentViewer(
                        items = AttachmentSamples.expenseFiles,
                        initialKey = AttachmentSamples.contract.key,
                        onDismiss = close,
                    )
                }
            }
        }
        case(
            name = "Very long file name",
            expectation = "The line under the file is shortened in the middle, keeping its extension.",
        ) {
            SheetTrigger(label = "Open viewer") { visible, close ->
                if (visible) {
                    AttachmentViewer(
                        items = listOf(
                            AttachmentSamples.statement.copy(
                                fileName = "Statement for the joint current account, September 2026, final copy.pdf",
                            ),
                        ),
                        onDismiss = close,
                    )
                }
            }
        }
        case(
            name = "No words for the position",
            expectation = "position = null: the heading alone, even with several files.",
        ) {
            SheetTrigger(label = "Open viewer") { visible, close ->
                if (visible) AttachmentViewer(items = AttachmentSamples.expenseFiles, onDismiss = close, position = null)
            }
        }
    }
}
