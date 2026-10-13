package dev.voir.areca.playground.flow.attachments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import dev.voir.areca.flow.attachments.AttachmentState
import dev.voir.areca.flow.attachments.part.AttachmentStrip
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.settings.Section
import dev.voir.areca.foundation.settings.Settings
import dev.voir.areca.foundation.settings.SettingsTextFieldRow
import dev.voir.areca.foundation.text.Text
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

/** A form that writes a record, with its files in a strip under its fields. */
@Composable
fun AttachmentFormShowcase() = FlowShowcase(
    title = "Form",
    summary = "A form that writes an expense, with its files in a strip under the fields: add, tag, " +
        "open and take off, all saved with the form.",
    module = "flow-attachments",
    status = ShowcaseStatus.Beta,
    notes = {
        ChromeText(text = "The recipe", role = ChromeTextRole.Label)
        ChromeText(
            text = "ExpenseFormRecipe in this file is the screen; AttachmentSheetsRecipe beside it is the " +
                "sheets and the viewer every attachments screen draws once. Press the add square: the " +
                "source sheet, then \"What is this?\" while the file copies in, then the file lands in " +
                "the strip under a veil while it saves.",
            role = ChromeTextRole.SectionDescription,
            tone = ChromeTone.Muted,
        )
        ChromeText(
            text = "On a form nothing is saved until the form is, so the cross on a square takes a file " +
                "off without asking. The limit, the largest file and the wording of every refusal are " +
                "the sample application's, not the module's.",
            role = ChromeTextRole.SectionDescription,
            tone = ChromeTone.Muted,
        )
    },
) {
    val expense by enumControl(
        key = "files",
        default = SampleExpense.Empty,
        options = SampleExpense.entries,
        optionLabel = { it.label },
    )
    val camera by booleanControl(
        key = "camera available",
        default = true,
        description = "Without a camera or a library, the only source opens straight away.",
    )
    val library by booleanControl("library available", default = true)
    val refuse by booleanControl("refuse picks as too large", default = false)
    val fail by booleanControl("attaching fails", default = false)
    val uploads by booleanControl("show uploads", default = false)

    preview {
        val attachments = rememberSampleAttachments(expense.files)
        attachments.sources = buildList {
            if (camera) add(SampleSource.Camera)
            if (library) add(SampleSource.Library)
            add(SampleSource.Files)
        }
        attachments.refusePicks = refuse
        attachments.failAttaching = fail
        attachments.showUploads = uploads

        DeviceFrame(size = DeviceSize.Regular) {
            ExpenseFormRecipe(attachments = attachments, windowInsets = DeviceSize.Regular.windowInsets)
        }
    }
}

/**
 * The recipe: a form that writes an expense, with an attachment strip under its fields.
 *
 * @param attachments The form's files and which sheet is up.
 * @param windowInsets Regions the system occupies.
 */
@Composable
private fun ExpenseFormRecipe(attachments: SampleAttachments, windowInsets: WindowInsets) {
    var amount by remember { mutableStateOf("42.80") }
    var merchant by remember { mutableStateOf("Grocery store") }
    var note by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ArecaTheme.colors.bgCanvas)
            .verticalScroll(rememberScrollState())
            .windowInsetsPadding(windowInsets)
            .imePadding(),
    ) {
        Spacer(Modifier.height(ArecaTheme.spacing.xl3))

        Text(
            text = "New expense",
            modifier = Modifier
                .horizontalContentInsets()
                .semantics { heading() },
            style = ArecaTheme.typography.headingSmall,
            color = ArecaTheme.colors.fgBase,
        )

        Spacer(Modifier.height(ArecaTheme.spacing.lg))

        Settings {
            Section {
                SettingsTextFieldRow(
                    value = amount,
                    onValueChange = { amount = it },
                    placeholder = "Amount",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Next),
                )
                SettingsTextFieldRow(
                    value = merchant,
                    onValueChange = { merchant = it },
                    placeholder = "Merchant",
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                )
                SettingsTextFieldRow(
                    value = note,
                    onValueChange = { note = it },
                    placeholder = "Note",
                    singleLine = false,
                )
            }
        }

        Spacer(Modifier.height(ArecaTheme.spacing.lg))

        // Full width, not inset: the strip scrolls under both screen edges while its first square
        // keeps to the gutter.
        AttachmentStrip(
            items = attachments.files,
            onOpen = attachments::open,
            onAdd = if (attachments.canAdd) attachments::add else null,
            // No question: nothing is saved until the form is, so taking a file off is as
            // reversible as typing.
            onRemove = attachments::remove,
            title = "Attachments",
            counter = attachments.counter,
            emptyTitle = "Add a receipt or file",
            emptyDescription = "Photos, PDFs and documents, up to ${SampleAttachments.MAX_SIZE} each",
            failure = attachments.failure,
            caption = { item -> if (item.state == AttachmentState.Adding) "Adding…" else item.label },
            addCaption = "Add",
        )

        Spacer(Modifier.height(ArecaTheme.spacing.xl3))

        Button(
            text = "Save expense",
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .horizontalContentInsets(),
        )

        Spacer(Modifier.height(ArecaTheme.spacing.xl3))
    }

    AttachmentSheetsRecipe(attachments = attachments, confirmRemoval = false)
}
