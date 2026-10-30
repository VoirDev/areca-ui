package dev.voir.areca.flow.attachments.viewer

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.voir.areca.flow.attachments.AttachmentFormat
import dev.voir.areca.flow.attachments.AttachmentItem
import dev.voir.areca.flow.attachments.AttachmentState
import dev.voir.areca.flow.attachments.attachmentPictureLoader
import dev.voir.areca.foundation.AnnounceOnChange
import dev.voir.areca.foundation.feedback.Spinner
import dev.voir.areca.foundation.feedback.SpinnerSize
import dev.voir.areca.foundation.feedback.SpinnerStyle
import dev.voir.areca.media.LocalMediaImageLoader
import dev.voir.areca.media.image.MediaFit
import dev.voir.areca.media.image.MediaFrameStyle
import dev.voir.areca.media.image.MediaImage
import dev.voir.areca.media.image.MediaPlaceholder
import dev.voir.areca.media.zoom.ZoomBehavior
import dev.voir.areca.media.zoom.ZoomLabels
import dev.voir.areca.media.zoom.rememberZoomState
import dev.voir.areca.media.zoom.zoomable
import dev.voir.areca.theme.ArecaDarkTheme

/**
 * One file in the viewer, drawn the way its format and its whereabouts allow.
 *
 * A picture is drawn zoomable and a PDF as its pages, zoomable too — the two formats every platform
 * can render — through the application's [dev.voir.areca.flow.attachments.AttachmentImageLoader] and
 * [dev.voir.areca.flow.attachments.AttachmentPdfRenderer]. A picture is asked for at the size of
 * the page it fills, not at the size it was taken. Anything else, anything those turn out not to be
 * able to draw, and any file whose bytes are not here is stated rather than drawn — its glyph, its
 * name, [detail], and under them:
 *
 * | State | Under the file |
 * | --- | --- |
 * | `Adding` | A spinner, alone: there is nothing to show yet |
 * | `Remote`, `Downloading` | A progress bar — `Remote` is asked for as its page settles, so it is only a moment |
 * | `Waiting` | A neutral notice, and Try again when [onRetry] is given |
 * | `Unavailable` | A neutral notice |
 * | A document, or undrawable | The ways to open it elsewhere, when [onOpenElsewhere] or [onSave] is given |
 *
 * A notice that takes the place of a progress bar — the download stopped — is announced.
 *
 * A picture fills the page at any size. A PDF's column of pages and a stated file are no wider than
 * [dev.voir.areca.foundation.adaptive.AdaptiveDefaults.ReadableWidth] and centred, which a phone
 * never reaches: on a tablet or a desktop a page reads as a page rather than a poster.
 *
 * Every word is a parameter: the ones the page cannot do without default to English, the rest to
 * nothing. Like the viewer, the page is dark whatever the application's theme.
 *
 * @param item The file.
 * @param modifier Modifier applied to the page.
 * @param onRetry Called when fetching a file that is not arriving is to be tried again, or `null`
 *   to offer no button.
 * @param onOpenElsewhere Called when a file this page cannot draw is to be opened in another
 *   application, or `null` to offer no button.
 * @param onSave Called when a copy of a file this page cannot draw is to be saved, or `null` to
 *   offer no button.
 * @param style Colours of the viewer. Defaults to the [LocalAttachmentViewerStyle] in scope, or
 *   [AttachmentViewerStyle.Default].
 * @param pictureDescription What a picture shows, read out in its place, or `null` for nothing.
 * @param detail Line under a stated file's name, or `null` for none. Defaults to the file's label
 *   where it is not the name, and its size.
 * @param downloadingText Words the progress of a download, `0..1` or `null` when not known. Its
 *   words for `null` also name the progress bar for assistive technology, which says the share
 *   itself.
 * @param waitingTitle What is the matter with a file that is not arriving.
 * @param waitingText More about it ("It will download once you're back online"), or `null`.
 * @param retryText Caption of the button that tries again.
 * @param unavailableTitle What is the matter with a file that is gone for good.
 * @param unavailableText More about it, or `null`.
 * @param elsewhereText What to say about a file only another application can open, or `null`.
 * @param openElsewhereText Caption of the button that opens it elsewhere.
 * @param saveText Caption of the button that saves a copy.
 * @param pageLabel Words the page of a PDF in view and the page count.
 * @param pageDescription Words one page of a PDF for assistive technology.
 * @param zoomInLabel Name of the action that brings a picture or a PDF's pages closer, offered to
 *   assistive technology in place of the pinch.
 * @param zoomOutLabel Name of the action that takes them back a step.
 * @param resetZoomLabel Name of the action that returns them to their fitted size.
 */
@Composable
public fun AttachmentViewerPage(
    item: AttachmentItem,
    modifier: Modifier = Modifier,
    onRetry: (() -> Unit)? = null,
    onOpenElsewhere: (() -> Unit)? = null,
    onSave: (() -> Unit)? = null,
    style: AttachmentViewerStyle = LocalAttachmentViewerStyle.current ?: AttachmentViewerStyle.Default,
    pictureDescription: String? = item.label,
    detail: String? = fileDetail(item),
    downloadingText: (progress: Float?) -> String = { progress ->
        if (progress == null) "Downloading" else "Downloading · ${(progress * PERCENT).toInt()}%"
    },
    waitingTitle: String = "Not on this device yet",
    waitingText: String? = null,
    retryText: String = "Try again",
    unavailableTitle: String = "This file is no longer available",
    unavailableText: String? = null,
    elsewhereText: String? = null,
    openElsewhereText: String = "Open in another app",
    saveText: String = "Save to device",
    pageLabel: (page: Int, count: Int) -> String = { page, count -> "Page $page of $count" },
    pageDescription: (page: Int, count: Int) -> String = { page, count -> "Page $page of $count" },
    zoomInLabel: String = "Zoom in",
    zoomOutLabel: String = "Zoom out",
    resetZoomLabel: String = "Reset zoom",
) {
    val elsewhere: @Composable () -> Unit = {
        AttachmentFileStatus(item = item, detail = detail, style = style, modifier = modifier) {
            OpenElsewhere(
                text = elsewhereText,
                style = style,
                openText = openElsewhereText,
                onOpen = onOpenElsewhere,
                saveText = saveText,
                onSave = onSave,
            )
        }
    }

    val stopped = rememberStoppedArriving(item.state)
    AnnounceOnChange(
        when {
            !stopped -> null
            item.state == AttachmentState.Waiting -> listOfNotNull(waitingTitle, waitingText).joinToString(" ")
            else -> listOfNotNull(unavailableTitle, unavailableText).joinToString(" ")
        },
    )

    ArecaDarkTheme {
        when (val state = item.state) {
            AttachmentState.Adding -> Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Spinner(
                    style = SpinnerStyle(color = style.controlContent),
                    size = SpinnerSize.Large,
                )
            }

            AttachmentState.Remote -> AttachmentFileStatus(item, detail, style, modifier) {
                DownloadProgress(
                    progress = null,
                    name = downloadingText(null),
                    label = downloadingText(null),
                    style = style,
                )
            }

            is AttachmentState.Downloading -> AttachmentFileStatus(item, detail, style, modifier) {
                DownloadProgress(
                    progress = state.progress,
                    name = downloadingText(null),
                    label = downloadingText(state.progress),
                    style = style,
                )
            }

            AttachmentState.Waiting -> AttachmentFileStatus(item, detail, style, modifier) {
                AwayNotice(
                    title = waitingTitle,
                    text = waitingText,
                    style = style,
                    retryText = retryText,
                    onRetry = onRetry,
                )
            }

            AttachmentState.Unavailable -> AttachmentFileStatus(item, detail, style, modifier) {
                AwayNotice(
                    title = unavailableTitle,
                    text = unavailableText,
                    style = style,
                    retryText = retryText,
                    onRetry = null,
                )
            }

            AttachmentState.Ready, is AttachmentState.Uploading -> {
                val model = item.model

                when {
                    model == null -> elsewhere()

                    item.format == AttachmentFormat.Image -> ViewerPicture(
                        model = model,
                        description = pictureDescription,
                        zoomInLabel = zoomInLabel,
                        zoomOutLabel = zoomOutLabel,
                        resetZoomLabel = resetZoomLabel,
                        style = style,
                        modifier = modifier,
                        fallback = elsewhere,
                    )

                    item.format == AttachmentFormat.Pdf -> AttachmentPdfPages(
                        model = model,
                        style = style,
                        pageLabel = pageLabel,
                        pageDescription = pageDescription,
                        zoomInLabel = zoomInLabel,
                        zoomOutLabel = zoomOutLabel,
                        resetZoomLabel = resetZoomLabel,
                        modifier = modifier,
                        fallback = elsewhere,
                    )

                    else -> elsewhere()
                }
            }
        }
    }
}

/**
 * A picture, decoded at the size of the page it is fitted into.
 *
 * Asked for at the frame's longest edge rather than at the picture's own size, which for a
 * photograph from a file picker is thousands of pixels: a viewer that swipes through a dozen
 * photographs holds only what the screen shows.
 *
 * @param model What the loader reads the picture from.
 * @param description What it shows, read out in its place.
 * @param zoomInLabel Name of the action that brings it closer.
 * @param zoomOutLabel Name of the action that takes it back a step.
 * @param resetZoomLabel Name of the action that returns it to its fitted size.
 * @param style Colours of the viewer.
 * @param modifier Modifier applied to the frame.
 * @param fallback What to show when it cannot be decoded.
 */
@Composable
private fun ViewerPicture(
    model: Any,
    description: String?,
    zoomInLabel: String,
    zoomOutLabel: String,
    resetZoomLabel: String,
    style: AttachmentViewerStyle,
    modifier: Modifier,
    fallback: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalMediaImageLoader provides attachmentPictureLoader()) {
        MediaImage(
            model = model,
            contentDescription = description,
            modifier = modifier.fillMaxSize(),
            fit = MediaFit.Whole,
            placeholder = MediaPlaceholder.None,
            zoom = ZoomBehavior.Sticky,
            zoomState = rememberZoomState(maxScale = PICTURE_MAX_SCALE),
            zoomLabels = ZoomLabels(zoomIn = zoomInLabel, zoomOut = zoomOutLabel, reset = resetZoomLabel),
            style = MediaFrameStyle.bare(content = style.controlContent),
            failure = { fallback() },
        )
    }
}

/**
 * Whether a file stopped arriving while its page was up: it was being fetched, and is now waiting or
 * gone. Stays `true` while it remains stopped, so the notice that says so is announced once.
 *
 * @param state Where the file's bytes are now.
 * @return Whether the page's notice replaced a progress bar.
 */
@Composable
private fun rememberStoppedArriving(state: AttachmentState): Boolean {
    val last = remember { ArrivalRecord() }
    val stopped = stoppedArriving(previous = last.state, current = state, wasStopped = last.stopped)

    SideEffect {
        last.state = state
        last.stopped = stopped
    }

    return stopped
}

/**
 * What [rememberStoppedArriving] saw in the last composition that was applied.
 *
 * A plain holder: it is read only in later compositions, which the file's change of state causes.
 */
private class ArrivalRecord {
    /** The file's state, or `null` before the first composition. */
    var state: AttachmentState? = null

    /** Whether it had stopped arriving. */
    var stopped: Boolean = false
}

/**
 * Whether a file has stopped arriving while it was watched.
 *
 * @param previous Its state when last drawn, or `null` when this is the first time.
 * @param current Its state now.
 * @param wasStopped What this answered when it was last drawn.
 * @return `true` when it is waiting or gone and was, when last drawn, being fetched or already
 *   stopped that way; `false` for a file first drawn waiting or gone, and for any other state.
 */
internal fun stoppedArriving(previous: AttachmentState?, current: AttachmentState, wasStopped: Boolean): Boolean {
    val isStopped = current == AttachmentState.Waiting || current == AttachmentState.Unavailable
    val wasArriving = previous == AttachmentState.Remote || previous is AttachmentState.Downloading

    return isStopped && (wasArriving || wasStopped)
}


/** Multiplier from a share to a percentage. */
private const val PERCENT = 100

/** How close a picture may be brought: far enough to read the small print on a receipt. */
private const val PICTURE_MAX_SCALE = 5f
