package dev.voir.areca.playground.flow.attachments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import dev.voir.areca.flow.attachments.part.AttachmentAddTile
import dev.voir.areca.flow.attachments.part.AttachmentTile
import dev.voir.areca.foundation.adaptive.AdaptiveDefaults
import dev.voir.areca.foundation.adaptive.SupportingPane
import dev.voir.areca.foundation.overlay.FlowModalHeaderInsets
import dev.voir.areca.foundation.settings.Section
import dev.voir.areca.foundation.settings.Settings
import dev.voir.areca.foundation.settings.SettingsRow
import dev.voir.areca.foundation.settings.SettingsRowIcon
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.topbar.TopBar
import dev.voir.areca.foundation.topbar.TopBarSize
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.Paperclip
import dev.voir.areca.playground.showcase.FlowShowcase
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.canvas.WindowFrame
import dev.voir.areca.playground.showcase.canvas.WindowPreset
import dev.voir.areca.playground.showcase.canvas.windowInsets
import dev.voir.areca.playground.showcase.chrome.ChromeText
import dev.voir.areca.playground.showcase.chrome.ChromeTextRole
import dev.voir.areca.playground.showcase.chrome.ChromeTone
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.horizontalContentInsets

/**
 * A saved record's details on a tablet or a desktop: the record in a readable column, its files in
 * a pane beside it when there is room and in a sheet when there is not.
 */
@Composable
fun AttachmentWideDetailsShowcase() = FlowShowcase(
    title = "Tablet and desktop",
    summary = "A saved expense's details with its files beside them on a wide window, and behind a row " +
        "of the record on a narrow one.",
    module = "flow-attachments",
    status = ShowcaseStatus.Beta,
    notes = {
        ChromeText(text = "The recipe", role = ChromeTextRole.Label)
        ChromeText(
            text = "WideExpenseDetailsRecipe in this file is the screen: foundation's SupportingPane with " +
                "the record, in a column no wider than it reads well, as its main content and the files, " +
                "as squares that wrap, as its supporting content. From a 920dp container the files are a pane at the end, always shown; below it " +
                "they move behind an Attachments row of the record and open in a sheet.",
            role = ChromeTextRole.SectionDescription,
            tone = ChromeTone.Muted,
        )
        ChromeText(
            text = "Every sheet takes its form from the chosen window and opens over the whole playground: " +
                "a sheet up to a phone on its side, a centred dialog on a tablet in landscape, a laptop " +
                "and a desktop. The viewer has the whole window on every one.",
            role = ChromeTextRole.SectionDescription,
            tone = ChromeTone.Muted,
        )
    },
) {
    val preset by enumControl(
        key = "window",
        default = WindowPreset.Laptop,
        options = WindowPreset.entries,
        optionLabel = { it.label },
    )
    val expense by enumControl(
        key = "files",
        default = SampleExpense.Many,
        options = SampleExpense.entries,
        optionLabel = { it.label },
    )

    preview {
        val attachments = rememberSampleAttachments(expense.files)

        BoxWithConstraints {
            WindowFrame(preset = preset, maxHeight = maxHeight) {
                WideExpenseDetailsRecipe(attachments = attachments, windowInsets = preset.windowInsets)
            }
        }
    }
}

/**
 * The recipe: a saved expense's details, with its files beside them on a wide window and behind a
 * row of the record on a narrow one.
 *
 * @param attachments The expense's files and which sheet is up.
 * @param windowInsets Regions the system occupies.
 */
@Composable
private fun WideExpenseDetailsRecipe(attachments: SampleAttachments, windowInsets: WindowInsets) {
    // Only read below 920dp, where the files are a sheet rather than a pane.
    var filesOpen by remember { mutableStateOf(false) }

    SupportingPane(
        supportingVisible = filesOpen,
        onSupportingDismissRequest = { filesOpen = false },
        modifier = Modifier
            .fillMaxSize()
            .background(ArecaTheme.colors.bgCanvas)
            .windowInsetsPadding(windowInsets),
        supportingHeader = {
            TopBar(
                title = "Attachments",
                subtitle = attachments.counter,
                size = TopBarSize.Compact,
                windowInsets = FlowModalHeaderInsets,
            )
        },
        main = {
            // The record stops at a readable width, centred in whatever room the files leave it.
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .wrapContentWidth()
                    .widthIn(max = AdaptiveDefaults.ReadableWidth)
                    .fillMaxWidth(),
            ) {
                Spacer(Modifier.height(ArecaTheme.spacing.xl3))

                // On the sections' own gutter, so the amount lines up with the cards under it.
                Column(modifier = Modifier.padding(horizontal = ArecaTheme.spacing.horizontalContentInsets)) {
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

                Settings {
                    Section {
                        SettingsRow(title = { Text("Category") }, value = { Text("Food") })
                        SettingsRow(title = { Text("Account") }, value = { Text("Everyday card") })
                        SettingsRow(title = { Text("Note") }, value = { Text("Weekly shop") })
                    }

                    // Beside the record there is nothing to open; without the pane, this row is the
                    // way to the files.
                    if (!isSupportingInline) {
                        Section {
                            SettingsRow(
                                title = { Text("Attachments") },
                                leading = { SettingsRowIcon(ArecaIcons.Paperclip) },
                                value = { Text(attachments.files.size.toString()) },
                                onClick = { filesOpen = true },
                            )
                        }
                    }
                }
            }
        },
        supporting = {
            // Squares that wrap rather than a strip that scrolls sideways: the pane is tall and narrow.
            // Adding is the last square; removing is in the viewer's options, as on a phone.
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .horizontalContentInsets()
                    .padding(top = ArecaTheme.spacing.sm, bottom = ArecaTheme.spacing.xl3),
                horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
                verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg),
            ) {
                attachments.files.forEach { item ->
                    key(item.key) {
                        AttachmentTile(
                            item = item,
                            onClick = { attachments.open(item) },
                            detail = item.size,
                        )
                    }
                }

                if (attachments.canAdd) {
                    AttachmentAddTile(onClick = attachments::add, caption = "Add")
                }
            }
        },
    )

    AttachmentSheetsRecipe(attachments = attachments, confirmRemoval = true)
}
