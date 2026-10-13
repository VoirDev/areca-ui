package dev.voir.areca.playground.flow.attachments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import dev.voir.areca.flow.attachments.AttachmentItem
import dev.voir.areca.flow.attachments.row.AttachmentRow
import dev.voir.areca.foundation.settings.Section
import dev.voir.areca.foundation.settings.Settings
import dev.voir.areca.foundation.settings.SettingsRow
import dev.voir.areca.foundation.settings.SettingsRowIcon
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.Plus
import dev.voir.areca.playground.showcase.FlowShowcase
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.canvas.DeviceFrame
import dev.voir.areca.playground.showcase.canvas.DeviceSize
import dev.voir.areca.playground.showcase.canvas.windowInsets
import dev.voir.areca.playground.showcase.chrome.ChromeText
import dev.voir.areca.playground.showcase.chrome.ChromeTextRole
import dev.voir.areca.playground.showcase.chrome.ChromeTone
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.horizontalContentInsets

/** A record's files read by name, grouped by what they are, on the settings list. */
@Composable
fun AttachmentDocumentsShowcase() = FlowShowcase(
    title = "Documents",
    summary = "A record's files as a list read by name, grouped by what they are, for records that " +
        "carry many documents.",
    module = "flow-attachments",
    status = ShowcaseStatus.Beta,
    notes = {
        ChromeText(text = "The recipe", role = ChromeTextRole.Label)
        ChromeText(
            text = "DocumentsRecipe in this file is the screen: AttachmentRows in foundation Sections, one " +
                "section per tag, beside a row of the application's own that adds a file. Where a strip " +
                "asks someone to open each square to find the one they want, a list is read by name.",
            role = ChromeTextRole.SectionDescription,
            tone = ChromeTone.Muted,
        )
        ChromeText(
            text = "Retag a file from the viewer's options and it moves to its new section; where its bytes " +
                "are is said at each row's end.",
            role = ChromeTextRole.SectionDescription,
            tone = ChromeTone.Muted,
        )
    },
) {
    val expense by enumControl(
        key = "files",
        default = SampleExpense.Many,
        options = SampleExpense.entries,
        optionLabel = { it.label },
    )

    preview {
        val attachments = rememberSampleAttachments(expense.files)

        DeviceFrame(size = DeviceSize.Regular) {
            DocumentsRecipe(attachments = attachments, windowInsets = DeviceSize.Regular.windowInsets)
        }
    }
}

/**
 * The recipe: a record's files as sections of rows, one per tag, and a row that adds one.
 *
 * @param attachments The record's files and which sheet is up.
 * @param windowInsets Regions the system occupies.
 */
@Composable
private fun DocumentsRecipe(attachments: SampleAttachments, windowInsets: WindowInsets) {
    // Sections in the product's own order of tags, each with its files in the order they were added.
    val sections: List<Pair<String, List<AttachmentItem>>> = SampleTag.entries
        .map { tag -> tag.label to attachments.files.filter { it.label == tag.label } }
        .filter { (_, files) -> files.isNotEmpty() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ArecaTheme.colors.bgCanvas)
            .verticalScroll(rememberScrollState())
            .windowInsetsPadding(windowInsets),
    ) {
        Spacer(Modifier.height(ArecaTheme.spacing.xl3))

        Text(
            text = "Documents",
            modifier = Modifier
                .horizontalContentInsets()
                .semantics { heading() },
            style = ArecaTheme.typography.headingSmall,
            color = ArecaTheme.colors.fgBase,
        )

        Spacer(Modifier.height(ArecaTheme.spacing.lg))

        Settings(modifier = Modifier.fillMaxWidth()) {
            Section(
                footer = attachments.counter?.let { counter -> { Text("$counter files") } },
            ) {
                SettingsRow(
                    title = { Text("Add a document") },
                    leading = { SettingsRowIcon(ArecaIcons.Plus) },
                    enabled = attachments.canAdd,
                    onClick = attachments::add,
                )
            }

            sections.forEach { (tag, files) ->
                Section(header = { Text(tag) }) {
                    files.forEach { item ->
                        AttachmentRow(
                            item = item,
                            onClick = { attachments.open(item) },
                            title = item.fileName,
                            subtitle = item.size,
                            onClickLabel = "Open",
                        )
                    }
                }
            }
        }
    }

    AttachmentSheetsRecipe(attachments = attachments, confirmRemoval = true)
}
