package dev.voir.areca.playground.flow.attachments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import dev.voir.areca.flow.attachments.part.AttachmentStrip
import dev.voir.areca.foundation.settings.Section
import dev.voir.areca.foundation.settings.Settings
import dev.voir.areca.foundation.settings.SettingsRow
import dev.voir.areca.foundation.settings.SettingsRowIcon
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.Paperclip
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

/** A saved record's details, with its files as larger squares and every change saved at once. */
@Composable
fun AttachmentDetailsShowcase() = FlowShowcase(
    title = "Details",
    summary = "A saved expense's details, with its files as larger squares that open the viewer, " +
        "where every change is saved as soon as it is made.",
    module = "flow-attachments",
    status = ShowcaseStatus.Beta,
    notes = {
        ChromeText(text = "The recipe", role = ChromeTextRole.Label)
        ChromeText(
            text = "ExpenseDetailsRecipe in this file is the screen. The squares state each file's size, " +
                "because here they are what was opened to be looked at, and a file not yet on this " +
                "device costs its size to open. Nothing on the strip adds or removes: adding is the " +
                "Attach row, removing is in the viewer's options.",
            role = ChromeTextRole.SectionDescription,
            tone = ChromeTone.Muted,
        )
        ChromeText(
            text = "Open the warranty, which is on the server only: landing on it downloads it. Removing " +
                "a file asks first, since on a saved record it is immediate.",
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
    val refuse by booleanControl("refuse picks as too large", default = false)
    val uploads by booleanControl(
        key = "show uploads",
        default = true,
        description = "A new file reports its upload in its square's corner before it is ready.",
    )

    preview {
        val attachments = rememberSampleAttachments(expense.files)
        attachments.refusePicks = refuse
        attachments.showUploads = uploads

        DeviceFrame(size = DeviceSize.Regular) {
            ExpenseDetailsRecipe(attachments = attachments, windowInsets = DeviceSize.Regular.windowInsets)
        }
    }
}

/**
 * The recipe: a saved expense's details, with its files and a way to attach another.
 *
 * @param attachments The expense's files and which sheet is up.
 * @param windowInsets Regions the system occupies.
 */
@Composable
private fun ExpenseDetailsRecipe(attachments: SampleAttachments, windowInsets: WindowInsets) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ArecaTheme.colors.bgCanvas)
            .verticalScroll(rememberScrollState())
            .windowInsetsPadding(windowInsets),
    ) {
        Spacer(Modifier.height(ArecaTheme.spacing.xl3))

        Column(modifier = Modifier.horizontalContentInsets()) {
            Text(
                text = "−42.80 €",
                modifier = Modifier.semantics { heading() },
                style = ArecaTheme.typography.headingLarge,
                color = ArecaTheme.colors.fgBase,
            )
            Text(
                text = "Grocery store · 27 September, 14:32",
                style = ArecaTheme.typography.bodyMedium,
                color = ArecaTheme.colors.fgMuted,
            )
        }

        Spacer(Modifier.height(ArecaTheme.spacing.xl3))

        // A step larger than the form's squares, each stating its size, and nothing to add or
        // remove on the strip itself. It draws nothing at all while there are no files.
        AttachmentStrip(
            items = attachments.files,
            onOpen = attachments::open,
            title = "Attachments",
            failure = attachments.failure,
            tileSize = ArecaTheme.spacing.xl9,
            caption = { item -> item.label },
            detail = { item -> item.size },
        )

        Spacer(Modifier.height(ArecaTheme.spacing.lg))

        Settings {
            Section {
                SettingsRow(title = { Text("Category") }, value = { Text("Food") })
                SettingsRow(title = { Text("Account") }, value = { Text("Everyday card") })
            }

            Section {
                SettingsRow(
                    title = { Text("Attach a file") },
                    leading = { SettingsRowIcon(ArecaIcons.Paperclip) },
                    enabled = attachments.canAdd,
                    onClick = attachments::add,
                )
            }
        }
    }

    AttachmentSheetsRecipe(attachments = attachments, confirmRemoval = true)
}
