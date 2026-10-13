package dev.voir.areca.flow.attachments.sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import dev.voir.areca.flow.attachments.AttachmentFormat
import dev.voir.areca.flow.attachments.AttachmentItem
import dev.voir.areca.flow.attachments.icon.AlertCircle
import dev.voir.areca.flow.attachments.icon.BundledIcons
import dev.voir.areca.flow.attachments.part.AttachmentThumbnail
import dev.voir.areca.flow.attachments.part.AttachmentTileStyle
import dev.voir.areca.flow.attachments.part.LocalAttachmentTileStyle
import dev.voir.areca.foundation.AnnounceOnChange
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.button.ButtonSize
import dev.voir.areca.foundation.button.ButtonStyle
import dev.voir.areca.foundation.feedback.Spinner
import dev.voir.areca.foundation.feedback.SpinnerSize
import dev.voir.areca.foundation.feedback.SpinnerStyle
import dev.voir.areca.foundation.notice.Notice
import dev.voir.areca.foundation.notice.NoticeText
import dev.voir.areca.foundation.notice.NoticeTone
import dev.voir.areca.foundation.overlay.FlowModalActionBar
import dev.voir.areca.foundation.overlay.FlowModalSecondaryAction
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.topbar.CloseAction
import dev.voir.areca.foundation.topbar.TopBarTitleAlignment
import dev.voir.areca.theme.ArecaTheme

/**
 * A file that has been picked and not yet attached, as the sheet that asks about it draws it.
 *
 * The application copies a picked file in the moment it is picked — its stream does not outlast the
 * pick — and attaches it once its owner has answered the sheet. This is what the sheet needs to show
 * meanwhile, worded.
 *
 * @property fileName Name the file was picked under, or `null` when the pick failed before one was
 *   known — the sheet then states only [problem].
 * @property format How the file can be drawn; see [AttachmentFormat.of].
 * @property size Its length once copied, already worded, or `null` before.
 * @property model What the application's image loader reads the copy from, for a picture of it,
 *   or `null` before the copy exists.
 * @property isCopying Whether the copy is still running. The question can be answered meanwhile;
 *   attaching waits for it.
 * @property problem Why the file cannot be attached — too large, unreadable, not allowed — already
 *   worded, or `null`. Stated in place of the question.
 */
@Immutable
public data class PendingAttachment(
    public val fileName: String?,
    public val format: AttachmentFormat = AttachmentFormat.Document,
    public val size: String? = null,
    public val model: Any? = null,
    public val isCopying: Boolean = false,
    public val problem: String? = null,
)

/**
 * Asked once a file has been picked, before it is attached: the file, and whatever the product
 * wants to know about it.
 *
 * **The file is shown at the top**, so the question is about something visible. After a camera shot
 * there is no other moment the picture is seen before it lands in the strip.
 *
 * **The question is a slot.** Most products ask what the file is — [AttachmentTagChips] — and gate
 * [attachEnabled] on an answer; others ask for a note, or ask nothing and only confirm. Whatever the
 * slot holds is the caller's state, so it resets when the caller says a new pick began. The copy
 * runs while the sheet is up, so an answer can be given before it finishes; Attach waits for both,
 * showing its spinner while the copy runs.
 *
 * **Cancel and Attach sit in the sheet's foot**, a
 * [dev.voir.areca.foundation.overlay.FlowModalActionBar] like every other sheet that commits a
 * draft, so the commit is where the reader already looks for it and the heading only names the
 * question.
 *
 * **Editing is the application's, and optional.** With [onEdit] the file's row offers a neutral
 * edit control — to crop a photo, say — once the copy exists. It is an offer beside the file rather
 * than the sheet's answer, so it takes no colour. The module does not edit anything itself; the
 * application decides per file whether to pass it: a photo yes, a PDF no. It typically takes the
 * sheet down while its editor is up, by passing a `null` [file], and puts it back with the edited
 * copy's [PendingAttachment.model].
 *
 * **A file that cannot be attached says why here**, in place of the question, because this is where
 * its owner is looking — and the only way on is the close control in the heading, which replaces
 * the foot's Cancel and Attach. The reason is announced.
 *
 * Driven by [file]: up while it is set, down when it is `null`. What it drew stays on screen while
 * it slides away.
 *
 * @param file The picked file, or `null` when the sheet is down.
 * @param onAttach Called when Attach is pressed.
 * @param onDismiss Called when the sheet is cancelled, closed, or dismissed.
 * @param title Heading while the file can be attached ("What is this?").
 * @param problemTitle Heading while it cannot.
 * @param attachText Caption of the attaching control.
 * @param cancelText Caption of the cancelling control.
 * @param closeLabel What the close control does, read out; the only control while the file cannot
 *   be attached.
 * @param copyingText Line under the file's name while it is being copied in ("Adding…"), or
 *   `null` for none.
 * @param onEdit Called when the edit control is pressed, or `null` for no edit control — the case for
 *   a file the product does not edit. The control waits for the copy.
 * @param editText Caption of the edit control.
 * @param thumbnailStyle Colours and shape of the file's square. Defaults to the
 *   [LocalAttachmentTileStyle] in scope, or [AttachmentTileStyle.Default].
 * @param attachEnabled Whether the answer is complete enough to attach — for example, whether a tag
 *   has been chosen. Attach also waits for the copy.
 * @param question What the sheet asks, under the file, or `null` to ask nothing.
 */
@Composable
public fun PendingAttachmentSheet(
    file: PendingAttachment?,
    onAttach: () -> Unit,
    onDismiss: () -> Unit,
    title: String = "Add this file?",
    problemTitle: String = "Couldn’t add this file",
    attachText: String = "Attach",
    cancelText: String = "Cancel",
    closeLabel: String = "Close",
    copyingText: String? = null,
    onEdit: (() -> Unit)? = null,
    editText: String = "Edit",
    thumbnailStyle: AttachmentTileStyle = LocalAttachmentTileStyle.current ?: AttachmentTileStyle.Default,
    attachEnabled: Boolean = true,
    question: (@Composable ColumnScope.() -> Unit)? = null,
) {
    val shown = rememberLastNonNull(file)
    val problem = shown?.problem
    val copying = shown?.isCopying == true

    // The caller's value, not the one held for the exit animation: a refusal is spoken as the sheet
    // states it, and again if the same refusal comes back on a later pick.
    AnnounceOnChange(file?.problem)

    AttachmentSheetFrame(
        visible = file != null,
        title = if (problem == null) title else problemTitle,
        onDismiss = onDismiss,
        titleAlignment = if (problem == null) TopBarTitleAlignment.Center else TopBarTitleAlignment.Start,
        trailing = if (problem == null) {
            null
        } else {
            { CloseAction(onClick = onDismiss, contentDescription = closeLabel) }
        },
        footer = if (problem == null) {
            {
                FlowModalActionBar(
                    confirmText = attachText,
                    onConfirm = onAttach,
                    enabled = attachEnabled && !copying,
                    loading = copying,
                    leading = { FlowModalSecondaryAction(text = cancelText, onClick = onDismiss) },
                )
            }
        } else {
            null
        },
    ) {
        val current = shown ?: return@AttachmentSheetFrame

        if (current.fileName != null) {
            PendingFileRow(
                file = current,
                copyingText = copyingText,
                onEdit = onEdit.takeIf { problem == null },
                editText = editText,
                style = thumbnailStyle,
            )
        }

        if (problem != null) {
            if (current.fileName != null) Spacer(Modifier.height(ArecaTheme.spacing.xl))

            Notice(
                title = null,
                modifier = Modifier.fillMaxWidth(),
                icon = BundledIcons.AlertCircle,
                tone = NoticeTone.Danger,
            ) {
                NoticeText(problem)
            }
        } else if (question != null) {
            if (current.fileName != null) Spacer(Modifier.height(ArecaTheme.spacing.xl))

            question()
        }
    }
}

/**
 * The picked file: its picture or its kind of file, its name, and its size once copied.
 *
 * @param file The picked file.
 * @param copyingText Line under the name while the copy runs, or `null` for none.
 * @param onEdit Called when the edit control is pressed, or `null` for no edit control.
 * @param editText Caption of the edit control.
 * @param style Colours and shape of the square.
 */
@Composable
private fun PendingFileRow(
    file: PendingAttachment,
    copyingText: String?,
    onEdit: (() -> Unit)?,
    editText: String,
    style: AttachmentTileStyle,
) {
    val fileName = file.fileName.orEmpty()
    val side = ArecaTheme.spacing.xl7

    Row(
        horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (file.isCopying) {
            Box(
                modifier = Modifier
                    .size(side)
                    .background(style.bg, style.shape),
                contentAlignment = Alignment.Center,
            ) {
                Spinner(style = SpinnerStyle(color = style.markContent), size = SpinnerSize.Medium)
            }
        } else {
            AttachmentThumbnail(
                item = AttachmentItem(
                    key = fileName,
                    fileName = fileName,
                    format = file.format,
                    model = file.model,
                ),
                size = side,
                marks = false,
                style = style,
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xs2),
        ) {
            Text(
                text = fileName,
                style = ArecaTheme.typography.labelMedium,
                color = ArecaTheme.colors.fgBase,
                maxLines = 1,
                overflow = TextOverflow.MiddleEllipsis,
            )

            // Its size once copied; while copying, that it is being added; after a refusal,
            // nothing — the reason is stated below in full.
            val detail = when {
                file.problem != null -> null
                file.size != null -> file.size
                file.isCopying -> copyingText
                else -> null
            }

            if (detail != null) {
                Text(
                    text = detail,
                    style = ArecaTheme.typography.bodySmall,
                    color = ArecaTheme.colors.fgMuted,
                )
            }
        }

        if (onEdit != null) {
            Button(
                text = editText,
                onClick = onEdit,
                style = ButtonStyle.soft(ActionColor.Neutral),
                size = ButtonSize.Small,
                enabled = !file.isCopying && file.model != null,
            )
        }
    }
}
