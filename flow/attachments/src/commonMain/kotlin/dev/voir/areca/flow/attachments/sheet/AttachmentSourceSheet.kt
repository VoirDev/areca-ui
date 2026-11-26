package dev.voir.areca.flow.attachments.sheet

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import dev.voir.areca.flow.attachments.icon.BundledIcons
import dev.voir.areca.flow.attachments.icon.Camera
import dev.voir.areca.flow.attachments.icon.FileText
import dev.voir.areca.flow.attachments.icon.Image
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.foundation.topbar.CloseAction
import dev.voir.areca.theme.ArecaTheme

/**
 * The places a new file most often comes from, with the module's glyphs.
 *
 * Each carries a line on what it is for and a hue of its own — the camera blue, the library purple,
 * the files orange, from [ArecaTheme.hues] — which only [AttachmentChoiceLayout.Bento] draws. The
 * descriptions are English; pass the application's own words.
 */
public object AttachmentSourceOptions {
    /**
     * Take a new photograph.
     *
     * @param label What the choice says.
     * @param description What it is for, on a bento card, or `null` for none.
     * @param hue Colour of its bento card; [Color.Unspecified] takes [ArecaTheme.hues]' blue.
     * @param onClick Called when it is chosen.
     * @return The choice.
     */
    public fun camera(
        label: String = "Take a photo",
        description: String? = "Snap a receipt or a page",
        hue: Color = Color.Unspecified,
        onClick: () -> Unit,
    ): AttachmentChoice = AttachmentChoice(
        label = label,
        icon = BundledIcons.Camera,
        onClick = onClick,
        isDestructive = false,
        description = description,
        hue = hue,
        defaultHue = { hues -> hues.blue },
    )

    /**
     * Pick a picture from the device's photo library.
     *
     * @param label What the choice says.
     * @param description What it is for, on a bento card, or `null` for none.
     * @param hue Colour of its bento card; [Color.Unspecified] takes [ArecaTheme.hues]' purple.
     * @param onClick Called when it is chosen.
     * @return The choice.
     */
    public fun library(
        label: String = "Choose from library",
        description: String? = "From your photos",
        hue: Color = Color.Unspecified,
        onClick: () -> Unit,
    ): AttachmentChoice = AttachmentChoice(
        label = label,
        icon = BundledIcons.Image,
        onClick = onClick,
        isDestructive = false,
        description = description,
        hue = hue,
        defaultHue = { hues -> hues.purple },
    )

    /**
     * Pick any file from the device's files.
     *
     * @param label What the choice says.
     * @param description What it is for, on a bento card, or `null` for none.
     * @param hue Colour of its bento card; [Color.Unspecified] takes [ArecaTheme.hues]' orange.
     * @param onClick Called when it is chosen.
     * @return The choice.
     */
    public fun files(
        label: String = "Choose a file",
        description: String? = "PDF or document",
        hue: Color = Color.Unspecified,
        onClick: () -> Unit,
    ): AttachmentChoice = AttachmentChoice(
        label = label,
        icon = BundledIcons.FileText,
        onClick = onClick,
        isDestructive = false,
        description = description,
        hue = hue,
        defaultHue = { hues -> hues.orange },
    )
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
 * **Rows or a bento.** By default the choices are rows laid straight on the sheet, as every other
 * choice is. [AttachmentChoiceLayout.Bento] draws them as cards instead — the first larger, each in
 * its [AttachmentChoice.hue] with its [AttachmentChoice.description] — for a product whose sources
 * are few and fixed and where one leads, such as the camera of an expense tracker.
 *
 * @param visible Whether the sheet is up.
 * @param options Choices to offer, in order. In a bento the first leads.
 * @param onDismiss Called when the sheet is dismissed without a choice.
 * @param title Heading of the sheet.
 * @param subtitle What may be added, and how large, or `null` for none. Under the heading for rows;
 *   centred under the cards for a bento, where it is a footnote to them rather than a lead-in.
 * @param closeLabel What the close control does, read out.
 * @param layout How the choices are laid out.
 */
@Composable
public fun AttachmentSourceSheet(
    visible: Boolean,
    options: List<AttachmentChoice>,
    onDismiss: () -> Unit,
    title: String = "Add attachment",
    subtitle: String? = null,
    closeLabel: String = ArecaTheme.labels.close,
    layout: AttachmentChoiceLayout = AttachmentChoiceLayout.Rows,
) {
    AttachmentSheetFrame(
        visible = visible,
        title = title,
        onDismiss = onDismiss,
        trailing = { CloseAction(onClick = onDismiss, contentDescription = closeLabel) },
    ) {
        when (layout) {
            AttachmentChoiceLayout.Rows -> {
                // Under the bar rather than in it: limits run to a sentence, and a bar's subtitle is
                // one truncated line.
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        modifier = Modifier.sheetGutter(),
                        style = ArecaTheme.typography.bodySmall,
                        color = ArecaTheme.colors.fgMuted,
                    )

                    Spacer(Modifier.height(ArecaTheme.spacing.md))
                }

                AttachmentChoiceList(choices = options)
            }

            AttachmentChoiceLayout.Bento -> {
                AttachmentChoiceBento(choices = options)

                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        modifier = Modifier
                            .fillMaxWidth()
                            .sheetGutter()
                            .padding(top = ArecaTheme.spacing.lg),
                        variant = TextVariant.BodyExtraSmall,
                        color = TextColor.Muted,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}
