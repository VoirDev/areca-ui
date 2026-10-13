package dev.voir.areca.playground.flow.attachments

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import dev.voir.areca.flow.attachments.AttachmentFormat
import dev.voir.areca.flow.attachments.AttachmentItem
import dev.voir.areca.flow.attachments.AttachmentState
import dev.voir.areca.flow.attachments.sheet.PendingAttachment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/** Where the sample application can take a new file from. */
internal enum class SampleSource {
    /** A new photograph. */
    Camera,

    /** A picture from the photo library. */
    Library,

    /** Any file from the device's files. */
    Files,
}

/**
 * Stands in for an application's attachments ViewModel on the recipe pages: the files of one
 * expense, the file being added, and which sheet is up.
 *
 * Everything a real application does with the platform — picking, copying, uploading, downloading,
 * sharing — is simulated with short delays, so the recipes can be clicked through offline. The rules
 * a product would apply live here too, worded here, because the module has none: the limit, the
 * largest file, and whether a file may be attached untagged.
 *
 * @param scope Scope the simulated work runs in; the page's.
 * @param initial Files the expense starts with.
 */
@Stable
internal class SampleAttachments(private val scope: CoroutineScope, initial: List<AttachmentItem>) {

    /** The expense's files, in the order they were added. */
    val files = mutableStateListOf<AttachmentItem>().apply { addAll(initial) }

    /** Whether the sheet asking where a new file comes from is up. */
    var choosingSource by mutableStateOf(false)
        private set

    /** The picked file waiting for its tag, or `null`. */
    var pending by mutableStateOf<PendingAttachment?>(null)
        private set

    /** The tag chosen for [pending], or `null` while none is. */
    var pendingTag by mutableStateOf<SampleTag?>(null)

    /** Key of the file the viewer is open on, or `null` while it is closed. */
    var viewing by mutableStateOf<Any?>(null)
        private set

    /** The file whose options sheet is up, or `null`. */
    var optionsFor by mutableStateOf<AttachmentItem?>(null)
        private set

    /** The file being renamed, or `null`. */
    var renaming by mutableStateOf<AttachmentItem?>(null)
        private set

    /** The file whose removal is being confirmed, or `null`. */
    var removing by mutableStateOf<AttachmentItem?>(null)
        private set

    /** What came of the last thing done to a file in the viewer, or `null`. */
    var notice by mutableStateOf<String?>(null)
        private set

    /** Why the last change to the files could not be made, or `null`. */
    var failure by mutableStateOf<String?>(null)
        private set

    /** Where a new file can come from on this device. Set from the page's controls. */
    var sources: List<SampleSource> = SampleSource.entries

    /** Whether the next pick is refused as too large. Set from the page's controls. */
    var refusePicks: Boolean = false

    /** Whether new files report their upload before they are ready. Set from the page's controls. */
    var showUploads: Boolean = false

    /** Whether saving a new file fails, taking it off again. Set from the page's controls. */
    var failAttaching: Boolean = false

    /** The picked file open in the crop screen, or `null`. The pending sheet is down meanwhile. */
    var editing by mutableStateOf<PendingAttachment?>(null)
        private set

    /** Whether camera and library picks come back as full photographs that can be cropped. */
    var photographs: Boolean = false

    /** Counter for the keys and names of files added here. */
    private var added = 0

    /** Whether another file may be added. */
    val canAdd: Boolean
        get() = files.size < LIMIT

    /** How many files there are against the limit, worded. */
    val counter: String?
        get() = "${files.size} of $LIMIT".takeIf { files.isNotEmpty() }

    /** Starts adding a file: asks where it comes from, or opens the only source there is. */
    fun add() {
        failure = null
        val only = sources.singleOrNull()
        if (only != null) pick(only) else choosingSource = true
    }

    /** Closes the source sheet without a choice. */
    fun dismissSources() {
        choosingSource = false
    }

    /**
     * Picks a file from [source] and copies it in, asking what it is meanwhile.
     *
     * @param source Where it comes from.
     */
    fun pick(source: SampleSource) {
        choosingSource = false
        pendingTag = null
        val picked = pickedFile(source)
        pending = PendingAttachment(fileName = picked.fileName, format = picked.format, isCopying = true)

        scope.launch {
            delay(COPY_MILLIS)
            pending = PendingAttachment(
                fileName = picked.fileName,
                format = picked.format,
                size = if (refusePicks) "31.4 MB" else picked.size,
                model = picked.model,
                problem = "This file is larger than 25 MB.".takeIf { refusePicks },
            )
        }
    }

    /** Takes the picked file to the crop screen. */
    fun startEdit() {
        editing = pending
    }

    /** Leaves the crop screen without a crop, back to the pending sheet. */
    fun cancelEdit() {
        editing = null
    }

    /**
     * Swaps the picked file for its crop and goes back to the pending sheet.
     *
     * @param cropped The rendered crop, which becomes the file's model.
     */
    fun finishEdit(cropped: ImageBitmap) {
        pending = editing?.copy(model = cropped)
        editing = null
    }

    /** Drops the picked file. */
    fun cancelPending() {
        pending = null
    }

    /** Attaches the picked file under its tag, saving it for a moment first. */
    fun attach() {
        val file = pending ?: return
        val tag = pendingTag ?: return
        val key = "added-${added++}"
        pending = null

        files += AttachmentItem(
            key = key,
            fileName = file.fileName.orEmpty(),
            label = tag.label,
            size = file.size,
            format = file.format,
            state = AttachmentState.Adding,
            model = file.model,
        )

        scope.launch {
            delay(SAVE_MILLIS)
            if (failAttaching) {
                files.removeAll { it.key == key }
                failure = "${file.fileName} couldn’t be attached. Try again."
                return@launch
            }
            if (showUploads) {
                for (step in 1..STEPS) {
                    update(key) { it.copy(state = AttachmentState.Uploading(step / STEPS.toFloat())) }
                    delay(STEP_MILLIS)
                }
            }
            update(key) { it.copy(state = AttachmentState.Ready) }
        }
    }

    /**
     * Opens the viewer.
     *
     * @param item The file to open on.
     */
    fun open(item: AttachmentItem) {
        viewing = item.key
    }

    /** Closes the viewer. */
    fun closeViewer() {
        viewing = null
    }

    /**
     * Fetches a file whose bytes are on the server, a tenth at a time.
     *
     * @param item The file.
     */
    fun download(item: AttachmentItem) {
        scope.launch {
            for (step in 0..STEPS) {
                update(item.key) { it.copy(state = AttachmentState.Downloading(step / STEPS.toFloat())) }
                delay(STEP_MILLIS)
            }
            update(item.key) { it.copy(state = AttachmentState.Ready, model = SampleFile.ReceiptPhoto) }
        }
    }

    /**
     * Opens a file's options sheet.
     *
     * @param item The file.
     */
    fun showOptions(item: AttachmentItem) {
        optionsFor = item
    }

    /** Closes the options sheet. */
    fun dismissOptions() {
        optionsFor = null
    }

    /**
     * Changes what a file is said to be; the options sheet stays up on it.
     *
     * @param item The file.
     * @param tag Its new tag.
     */
    fun retag(item: AttachmentItem, tag: SampleTag) {
        update(item.key) { it.copy(label = tag.label) }
        if (optionsFor?.key == item.key) optionsFor = item.copy(label = tag.label)
    }

    /** Moves from the options sheet to renaming its file. */
    fun startRename() {
        renaming = optionsFor
        optionsFor = null
    }

    /**
     * Renames the file being renamed.
     *
     * @param fileName Its new name, extension included.
     */
    fun rename(fileName: String) {
        renaming?.let { item -> update(item.key) { it.copy(fileName = fileName) } }
        renaming = null
    }

    /** Closes the rename sheet. */
    fun dismissRename() {
        renaming = null
    }

    /**
     * Moves from the options sheet to removing its file: asks first when [confirm], removes at once
     * when not.
     *
     * @param confirm Whether removing asks first — on a saved record, not on a form.
     */
    fun startRemove(confirm: Boolean) {
        val item = optionsFor ?: return
        optionsFor = null
        if (confirm) removing = item else remove(item)
    }

    /**
     * Takes a file off.
     *
     * @param item The file.
     */
    fun remove(item: AttachmentItem) {
        files.removeAll { it.key == item.key }
        removing = null
        if (files.isEmpty()) viewing = null
    }

    /** Closes the removal question. */
    fun dismissRemove() {
        removing = null
    }

    /**
     * Stands in for handing a file to the share sheet.
     *
     * @param item The file.
     */
    fun share(item: AttachmentItem) {
        notice = "The share sheet opens here for ${item.fileName}."
    }

    /**
     * Stands in for saving a copy of a file.
     *
     * @param item The file.
     */
    fun save(item: AttachmentItem) {
        optionsFor = null
        notice = "${item.fileName} saved to Downloads."
    }

    /**
     * Stands in for opening a file in another application, which on this desktop finds none.
     *
     * @param item The file.
     */
    fun openElsewhere(item: AttachmentItem) {
        notice = "No app on this device opens ${item.fileName}."
    }

    /** Clears [notice]. */
    fun dismissNotice() {
        notice = null
    }

    /**
     * Replaces the file with [key] by [change] of it.
     *
     * @param key The file.
     * @param change How it changes.
     */
    private fun update(key: Any, change: (AttachmentItem) -> AttachmentItem) {
        val index = files.indexOfFirst { it.key == key }
        if (index >= 0) files[index] = change(files[index])
    }

    /**
     * The file a pick from [source] comes back with.
     *
     * @param source Where it came from.
     * @return The file, untagged.
     */
    private fun pickedFile(source: SampleSource): AttachmentItem = when (source) {
        SampleSource.Camera -> AttachmentSamples.receipt.copy(
            fileName = "IMG_${2102 + added}.jpg",
            size = "1.6 MB",
            model = if (photographs) SampleFile.LandscapePhoto else SampleFile.ReceiptPhoto,
        )
        SampleSource.Library -> AttachmentSamples.receipt.copy(
            fileName = "IMG_${1874 + added}.jpg",
            size = "2.2 MB",
            model = if (photographs) SampleFile.LandscapePhoto else SampleFile.ReceiptPhoto,
        )
        SampleSource.Files -> AttachmentSamples.invoice.copy(
            fileName = "Invoice INV-2026-${433 + added}.pdf",
            format = AttachmentFormat.Pdf,
        )
    }

    companion object {
        /** How many files one expense may carry. */
        const val LIMIT = 20

        /** The largest file one expense may carry, worded. */
        const val MAX_SIZE = "25 MB"

        /** How long a pick takes to copy in. */
        private const val COPY_MILLIS = 1_200L

        /** How long attaching a file takes to save. */
        private const val SAVE_MILLIS = 700L

        /** Steps of a simulated transfer. */
        private const val STEPS = 10

        /** Time between those steps. */
        private const val STEP_MILLIS = 150L
    }
}

/**
 * Remembers the sample application for one recipe page.
 *
 * @param initial Files the expense starts with; a new list starts afresh.
 * @return The sample application.
 */
@Composable
internal fun rememberSampleAttachments(initial: List<AttachmentItem>): SampleAttachments {
    val scope = rememberCoroutineScope()
    return remember(initial) { SampleAttachments(scope, initial) }
}

/**
 * The files an expense on a recipe page starts with.
 *
 * @property label Name shown in the control.
 * @property files The files.
 */
internal enum class SampleExpense(val label: String, val files: List<AttachmentItem>) {
    /** No files yet. */
    Empty("None", emptyList()),

    /** A receipt and its invoice. */
    Some("Two", listOf(AttachmentSamples.receipt, AttachmentSamples.invoice)),

    /** Every sample file, one of them on the server only. */
    Many("Five", AttachmentSamples.expenseFiles + AttachmentSamples.statement),
}
