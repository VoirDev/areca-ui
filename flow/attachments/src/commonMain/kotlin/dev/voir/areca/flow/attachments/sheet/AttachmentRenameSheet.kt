package dev.voir.areca.flow.attachments.sheet

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import dev.voir.areca.flow.attachments.AttachmentItem
import dev.voir.areca.flow.attachments.fileExtension
import dev.voir.areca.foundation.field.FieldGroup
import dev.voir.areca.foundation.field.TextField
import dev.voir.areca.foundation.overlay.FlowModalActionBar
import dev.voir.areca.foundation.overlay.FlowModalSecondaryAction
import dev.voir.areca.foundation.topbar.TopBarTitleAlignment

/**
 * Gives an attached file a new name.
 *
 * **Only the part before the extension is edited.** The extension is what the file is to every
 * application it is later handed to, and a rename that dropped it would turn a PDF into a file
 * nothing knows how to open. It is put back when the name is saved, and the box opens focused with
 * the whole editable part selected, so typing replaces it.
 *
 * **Cancel and Save sit in the sheet's foot**, a
 * [dev.voir.areca.foundation.overlay.FlowModalActionBar] like every other sheet that commits a
 * draft. The sheet keeps clear of the keyboard, so the foot rides on top of the one the box opens
 * with and stays in reach.
 *
 * Whether a name is acceptable is the application's: [canSave] enables Save — pass
 * `{ it.isNotBlank() }` to refuse an empty one — and [error] states what was wrong with the last
 * attempt, under the box.
 *
 * Driven by [item]: up while it is set, down when it is `null`. The box starts afresh for every
 * file it opens on, and only then: an [item] that changes while the sheet is up — the same file,
 * its upload a step further — keeps what was typed.
 *
 * @param item The file, or `null` when the sheet is down.
 * @param onRename Called with the whole new name, trimmed, extension restored.
 * @param onDismiss Called when the sheet is cancelled or dismissed.
 * @param title Heading of the sheet.
 * @param label Label of the box.
 * @param saveText Caption of the saving control.
 * @param cancelText Caption of the cancelling control.
 * @param canSave Whether the name in the box, without its extension, may be saved.
 * @param error Why the last name could not be used, already worded, or `null`.
 * @param saving Whether the rename is being saved: Save shows its spinner and nothing is pressable.
 */
@Composable
public fun AttachmentRenameSheet(
    item: AttachmentItem?,
    onRename: (String) -> Unit,
    onDismiss: () -> Unit,
    title: String = "Rename",
    label: String = "Name",
    saveText: String = "Save",
    cancelText: String = "Cancel",
    canSave: (String) -> Boolean = { true },
    error: String? = null,
    saving: Boolean = false,
) {
    val shown = rememberLastNonNull(item)
    val (stem, suffix) = remember(shown?.fileName) { splitFileName(shown?.fileName.orEmpty()) }
    var name by remember {
        mutableStateOf(TextFieldValue(text = stem, selection = TextRange(0, stem.length)))
    }
    val focus = remember { FocusRequester() }
    val enabled = !saving && canSave(name.text.trim())
    val save = { if (enabled) onRename(name.text.trim() + suffix) }

    // Every opening starts afresh — the same file reopened after a cancel included — with the
    // editable part selected so typing replaces it. Keyed on the file's identity, so a live item
    // that changes while the sheet is up keeps what was typed; and on the caller's value rather than
    // the one held for the exit animation, so closing leaves the box as it was while it slides away.
    LaunchedEffect(item?.key) {
        if (item != null) name = TextFieldValue(text = stem, selection = TextRange(0, stem.length))
    }

    AttachmentSheetFrame(
        visible = item != null,
        title = title,
        onDismiss = { if (!saving) onDismiss() },
        titleAlignment = TopBarTitleAlignment.Center,
        footer = {
            FlowModalActionBar(
                confirmText = saveText,
                onConfirm = save,
                enabled = enabled,
                loading = saving,
                leading = {
                    FlowModalSecondaryAction(text = cancelText, onClick = onDismiss, enabled = !saving)
                },
            )
        },
    ) {
        if (shown == null) return@AttachmentSheetFrame

        // Here rather than beside the reset: the modal composes its content only once it is shown,
        // so this is the first point the box exists to take focus. Waiting a frame lets the sheet's
        // window attach before the keyboard is asked for.
        LaunchedEffect(item?.key) {
            if (item != null) {
                withFrameNanos { }
                focus.requestFocus()
            }
        }

        FieldGroup(label = label, error = error) { field ->
            TextField(
                value = name,
                onValueChange = { edited -> name = edited },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focus),
                field = field,
                enabled = !saving,
                singleLine = true,
                // A file name is not a sentence: `invoice-03.pdf` must not come back `Invoice-03.pdf`.
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    autoCorrectEnabled = false,
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions(onDone = { save() }),
            )
        }
    }
}

/**
 * Splits a file name into the part a person edits and the extension that is kept.
 *
 * Uses the same rule as the extension printed on a square, so a name with a dot in it
 * (`Invoice 12.03.2026 corrected`) is edited whole rather than cut at its last dot.
 *
 * @param fileName Name the file goes by.
 * @return The editable part, and the extension with its dot — empty when there is none.
 */
internal fun splitFileName(fileName: String): Pair<String, String> {
    val extension = fileExtension(fileName) ?: return fileName to ""
    val suffix = ".$extension"

    return fileName.removeSuffix(suffix) to suffix
}
