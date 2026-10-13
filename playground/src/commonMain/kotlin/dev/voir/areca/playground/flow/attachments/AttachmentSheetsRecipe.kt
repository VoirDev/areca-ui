package dev.voir.areca.playground.flow.attachments

import androidx.compose.runtime.Composable
import dev.voir.areca.flow.attachments.AttachmentState
import dev.voir.areca.flow.attachments.sheet.AttachmentOptions
import dev.voir.areca.flow.attachments.sheet.AttachmentOptionsSheet
import dev.voir.areca.flow.attachments.sheet.AttachmentRenameSheet
import dev.voir.areca.flow.attachments.sheet.AttachmentSourceOptions
import dev.voir.areca.flow.attachments.sheet.AttachmentSourceSheet
import dev.voir.areca.flow.attachments.sheet.AttachmentTagChips
import dev.voir.areca.flow.attachments.sheet.PendingAttachmentSheet
import dev.voir.areca.flow.attachments.viewer.AttachmentViewer
import dev.voir.areca.flow.attachments.viewer.AttachmentViewerPage
import dev.voir.areca.foundation.overlay.ConfirmSheet
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.Trash

/**
 * The recipe every screen with attachments draws once, beside whatever holds its files: the sheets
 * that add a file, and the viewer with the sheets that act on one.
 *
 * Shared by the Form, Details and Documents pages, and duplicated in the module README as
 * "Recipe: the sheets and the viewer". Each step is a dialog of its own, so they stack over the
 * screen rather than replacing it; the options, rename and removal sheets go inside the viewer, so
 * they stack over it too.
 *
 * @param attachments The screen's files and which sheet is up.
 * @param confirmRemoval Whether removing a file asks first: yes on a saved record, no on a form,
 *   where nothing is saved until the form is.
 * @param onEdit Called to edit the picked file, or `null` when the product does not edit this one.
 */
@Composable
internal fun AttachmentSheetsRecipe(
    attachments: SampleAttachments,
    confirmRemoval: Boolean,
    onEdit: (() -> Unit)? = null,
) {
    AttachmentSourceSheet(
        visible = attachments.choosingSource,
        options = attachments.sources.map { source ->
            when (source) {
                SampleSource.Camera -> AttachmentSourceOptions.camera { attachments.pick(source) }
                SampleSource.Library -> AttachmentSourceOptions.library { attachments.pick(source) }
                SampleSource.Files -> AttachmentSourceOptions.files { attachments.pick(source) }
            }
        },
        onDismiss = attachments::dismissSources,
        subtitle = "Up to ${SampleAttachments.MAX_SIZE} each. Photos are resized before they’re saved.",
    )

    // The tag is asked, never guessed: nothing is chosen when the sheet opens, and Attach waits
    // for one — and for the copy.
    // Down while the crop screen has the file; back up with the crop.
    PendingAttachmentSheet(
        file = attachments.pending.takeIf { attachments.editing == null },
        onAttach = attachments::attach,
        onDismiss = attachments::cancelPending,
        title = "What is this?",
        attachEnabled = attachments.pendingTag != null,
        copyingText = "Adding…",
        onEdit = onEdit,
        question = {
            AttachmentTagChips(
                tags = SampleTag.entries,
                selected = attachments.pendingTag,
                onSelect = { tag -> attachments.pendingTag = tag },
                label = { tag -> tag.label },
            )
        },
    )

    if (attachments.viewing != null) {
        AttachmentViewer(
            items = attachments.files,
            initialKey = attachments.viewing,
            onDismiss = attachments::closeViewer,
            onRequest = attachments::download,
            onShare = attachments::share,
            onMore = attachments::showOptions,
            notice = attachments.notice,
            onNoticeDismiss = attachments::dismissNotice,
            page = { item ->
                AttachmentViewerPage(
                    item = item,
                    onRetry = { attachments.download(item) },
                    onOpenElsewhere = { attachments.openElsewhere(item) },
                    onSave = { attachments.save(item) },
                    waitingText = "It’s saved to your account and will download once you’re back online.",
                    unavailableText = "It couldn’t be downloaded and won’t be tried again. You can remove it.",
                    elsewhereText = "This kind of file can’t be shown here. Open it in another app to see it.",
                )
            },
            sheets = {
                val item = attachments.optionsFor

                AttachmentOptionsSheet(
                    item = item,
                    choices = buildList {
                        add(AttachmentOptions.rename(onClick = attachments::startRename))
                        if (item?.state == AttachmentState.Ready) {
                            add(AttachmentOptions.save { attachments.save(item) })
                        }
                        add(AttachmentOptions.remove { attachments.startRemove(confirm = confirmRemoval) })
                    },
                    onDismiss = attachments::dismissOptions,
                    tagsTitle = "Type",
                    tags = {
                        AttachmentTagChips(
                            tags = SampleTag.entries,
                            selected = SampleTag.entries.find { tag -> tag.label == item?.label },
                            onSelect = { tag -> item?.let { attachments.retag(it, tag) } },
                            label = { tag -> tag.label },
                        )
                    },
                )

                AttachmentRenameSheet(
                    item = attachments.renaming,
                    onRename = attachments::rename,
                    onDismiss = attachments::dismissRename,
                    canSave = { name -> name.isNotBlank() },
                )

                ConfirmSheet(
                    visible = attachments.removing != null,
                    icon = ArecaIcons.Trash,
                    title = "Remove this attachment?",
                    description = "It will be removed from this expense on all your devices.",
                    confirmText = "Remove",
                    onConfirm = { attachments.removing?.let(attachments::remove) },
                    onDismiss = attachments::dismissRemove,
                )
            },
        )
    }
}
