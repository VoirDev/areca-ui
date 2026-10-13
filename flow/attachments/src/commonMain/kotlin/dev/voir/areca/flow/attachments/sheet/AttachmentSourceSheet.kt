package dev.voir.areca.flow.attachments.sheet

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.voir.areca.flow.attachments.icon.BundledIcons
import dev.voir.areca.flow.attachments.icon.Camera
import dev.voir.areca.flow.attachments.icon.FileText
import dev.voir.areca.flow.attachments.icon.Image
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.topbar.CloseAction
import dev.voir.areca.theme.ArecaTheme

/** The places a new file most often comes from, with the module's glyphs. */
public object AttachmentSourceOptions {
    /**
     * Take a new photograph.
     *
     * @param label What the choice says.
     * @param onClick Called when it is chosen.
     * @return The choice.
     */
    public fun camera(label: String = "Take a photo", onClick: () -> Unit): AttachmentChoice =
        AttachmentChoice(label, BundledIcons.Camera, onClick)

    /**
     * Pick a picture from the device's photo library.
     *
     * @param label What the choice says.
     * @param onClick Called when it is chosen.
     * @return The choice.
     */
    public fun library(label: String = "Choose from library", onClick: () -> Unit): AttachmentChoice =
        AttachmentChoice(label, BundledIcons.Image, onClick)

    /**
     * Pick any file from the device's files.
     *
     * @param label What the choice says.
     * @param onClick Called when it is chosen.
     * @return The choice.
     */
    public fun files(label: String = "Choose a file", onClick: () -> Unit): AttachmentChoice =
        AttachmentChoice(label, BundledIcons.FileText, onClick)
}

/**
 * Where a new file comes from: the camera, the photo library, the device's files, or anything the
 * product adds.
 *
 * It offers [options] and nothing else, in the order given. A choice that opens nothing is worse
 * than no choice, so list only what the current platform can complete — and where only one is left,
 * as on a desktop with no camera and no library of its own, skip the sheet and open that one
 * directly: a sheet with one choice is a tap for nothing.
 *
 * Capturing, picking and copying the file are the application's. The sheet reports the choice
 * through its `onClick` and does not close itself: the caller's state decides when it goes. Its way
 * out is the close control in its heading bar.
 *
 * @param visible Whether the sheet is up.
 * @param options Choices to offer, in order.
 * @param onDismiss Called when the sheet is dismissed without a choice.
 * @param title Heading of the sheet.
 * @param subtitle Line under the heading — what may be added, and how large — or `null` for none.
 * @param closeLabel What the close control does, read out.
 */
@Composable
public fun AttachmentSourceSheet(
    visible: Boolean,
    options: List<AttachmentChoice>,
    onDismiss: () -> Unit,
    title: String = "Add attachment",
    subtitle: String? = null,
    closeLabel: String = "Close",
) {
    AttachmentSheetFrame(
        visible = visible,
        title = title,
        onDismiss = onDismiss,
        trailing = { CloseAction(onClick = onDismiss, contentDescription = closeLabel) },
    ) {
        // Under the bar rather than in it: limits run to a sentence, and a bar's subtitle is one
        // truncated line.
        if (subtitle != null) {
            Text(
                text = subtitle,
                style = ArecaTheme.typography.bodySmall,
                color = ArecaTheme.colors.fgMuted,
            )

            Spacer(Modifier.height(ArecaTheme.spacing.lg))
        }

        AttachmentChoiceList(choices = options)
    }
}
