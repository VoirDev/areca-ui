package dev.voir.areca.flow.attachments.sheet

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.voir.areca.flow.attachments.AttachmentItem
import dev.voir.areca.flow.attachments.icon.BundledIcons
import dev.voir.areca.flow.attachments.icon.Download
import dev.voir.areca.flow.attachments.icon.LinkExternal
import dev.voir.areca.flow.attachments.icon.Pencil
import dev.voir.areca.flow.attachments.icon.Share
import dev.voir.areca.flow.attachments.icon.Trash
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.topbar.CloseAction
import dev.voir.areca.theme.ArecaTheme

/** The things most often done to an attached file, with the module's glyphs. */
public object AttachmentOptions {
    /**
     * Give the file a new name, usually through [AttachmentRenameSheet].
     *
     * @param label What the choice says.
     * @param onClick Called when it is chosen.
     * @return The choice.
     */
    public fun rename(label: String = "Rename", onClick: () -> Unit): AttachmentChoice =
        AttachmentChoice(label, BundledIcons.Pencil, onClick)

    /**
     * Hand the file to the platform's share sheet.
     *
     * @param label What the choice says.
     * @param onClick Called when it is chosen.
     * @return The choice.
     */
    public fun share(label: String = "Share", onClick: () -> Unit): AttachmentChoice =
        AttachmentChoice(label, BundledIcons.Share, onClick)

    /**
     * Save a copy of the file somewhere on the device.
     *
     * @param label What the choice says.
     * @param onClick Called when it is chosen.
     * @return The choice.
     */
    public fun save(label: String = "Save to device", onClick: () -> Unit): AttachmentChoice =
        AttachmentChoice(label, BundledIcons.Download, onClick)

    /**
     * Open the file in another application.
     *
     * @param label What the choice says.
     * @param onClick Called when it is chosen.
     * @return The choice.
     */
    public fun openElsewhere(label: String = "Open in another app", onClick: () -> Unit): AttachmentChoice =
        AttachmentChoice(label, BundledIcons.LinkExternal, onClick)

    /**
     * Take the file off. Not tinted: what keeps a file from being removed by accident is the
     * confirmation behind this choice where removing is final, not the colour of the row.
     *
     * @param label What the choice says.
     * @param onClick Called when it is chosen.
     * @return The choice.
     */
    public fun remove(label: String = "Remove attachment", onClick: () -> Unit): AttachmentChoice =
        AttachmentChoice(label, BundledIcons.Trash, onClick)
}

/**
 * What can be done to one attached file, headed by the file itself.
 *
 * **What the file is comes first, when the product asks.** Changing it — a file added in a hurry
 * under the wrong tag — is the change most often made, so a [tags] slot for [AttachmentTagChips]
 * sits above the choices and changes it in one tap, with the sheet staying up. The rest are
 * [choices], only those that apply: saving a copy of a file that is not on this device has nothing
 * to save.
 *
 * Driven by [item]: up while it is set, down when it is `null`. What it drew stays on screen while
 * it slides away. It does not close itself when a choice is made; its way out is the close control
 * in its heading bar.
 *
 * @param item The file, or `null` when the sheet is down.
 * @param choices What can be done, in order.
 * @param onDismiss Called when the sheet is dragged or tapped away.
 * @param title Heading of the sheet. Defaults to the file's name.
 * @param subtitle Line under the heading. Defaults to the file's size.
 * @param tagsTitle Quiet label over [tags] ("Type"), or `null` for none.
 * @param closeLabel What the close control does, read out.
 * @param tags What the file is, as chips, or `null` when the product does not tag files.
 */
@Composable
public fun AttachmentOptionsSheet(
    item: AttachmentItem?,
    choices: List<AttachmentChoice>,
    onDismiss: () -> Unit,
    title: (AttachmentItem) -> String = { file -> file.fileName },
    subtitle: (AttachmentItem) -> String? = { file -> file.size },
    tagsTitle: String? = null,
    closeLabel: String = "Close",
    tags: (@Composable ColumnScope.() -> Unit)? = null,
) {
    val shown = rememberLastNonNull(item)

    AttachmentSheetFrame(
        visible = item != null,
        title = shown?.let(title).orEmpty(),
        subtitle = shown?.let(subtitle),
        onDismiss = onDismiss,
        trailing = { CloseAction(onClick = onDismiss, contentDescription = closeLabel) },
    ) {
        if (tags != null) {
            if (tagsTitle != null) {
                Text(
                    text = tagsTitle,
                    style = ArecaTheme.typography.bodySmall,
                    color = ArecaTheme.colors.fgMuted,
                )

                Spacer(Modifier.height(ArecaTheme.spacing.md))
            }

            tags()

            if (choices.isNotEmpty()) Spacer(Modifier.height(ArecaTheme.spacing.xl2))
        }

        AttachmentChoiceList(choices = choices)
    }
}
