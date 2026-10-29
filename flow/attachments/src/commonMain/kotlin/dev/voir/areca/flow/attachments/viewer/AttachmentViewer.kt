package dev.voir.areca.flow.attachments.viewer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import dev.voir.areca.flow.attachments.AttachmentItem
import dev.voir.areca.flow.attachments.AttachmentState
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.media.viewer.MediaViewer
import dev.voir.areca.media.viewer.MediaViewerBars
import dev.voir.areca.media.viewer.MediaViewerChrome
import dev.voir.areca.media.viewer.MediaViewerStyle
import dev.voir.areca.theme.ArecaTheme
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * A record's files, one at a time, full screen.
 *
 * **Every file, not just the one pressed.** It opens on [initialKey] and swipes to the others, with
 * a filmstrip under them to jump; a receipt and its invoice are usually read together.
 *
 * **Opening a file is asking for it.** Files are fetched on demand, so when a page whose bytes are
 * only on the server ([AttachmentState.Remote]) settles on screen, [onRequest] is called — once per
 * landing, and never for a file merely swiped past.
 *
 * **Everything done to a file is the application's.** The viewer reports [onShare] and [onMore]
 * for the file on screen; the options sheet, the rename sheet and the removal question go in
 * [sheets], inside the viewer's own window, so they stack above the file rather than under it.
 * Sharing is offered only for a file whose bytes are here. What came of it — a file that no
 * application opens, a save that failed — is [notice], a line over the file, announced as it
 * appears, that the viewer clears through [onNoticeDismiss] after [noticeDuration] — longer where
 * the platform's accessibility settings ask for more time. When the last file goes, so does the
 * viewer.
 *
 * **Dark, edge to edge.** The viewer is dark whatever the application's theme — its bar, its pages
 * and the sheets in [sheets] are drawn in a dark scope of the application's theme — and its window
 * runs behind the status and navigation bars, painting them with the style's [AttachmentViewerStyle.bg]
 * and asking for light bar icons. On iOS the status bar's text keeps the style the application's
 * view controller gives it.
 *
 * [page] draws each file; the default is [AttachmentViewerPage] with English words and no way to
 * open a file elsewhere. Pass your own to word it and wire those buttons:
 *
 * ```kotlin
 * page = { item ->
 *     AttachmentViewerPage(
 *         item = item,
 *         onRetry = { viewModel.download(item.key) },
 *         onOpenElsewhere = { handoff.open(item) },
 *         waitingTitle = stringResource(Res.string.not_here_yet),
 *     )
 * }
 * ```
 *
 * @param items The files, in order.
 * @param onDismiss Called when the viewer is closed, and when [items] becomes empty.
 * @param modifier Modifier applied to the viewer's frame.
 * @param initialKey The [AttachmentItem.key] of the file to open on, or `null` for the first.
 * @param onRequest Called with a file whose bytes are only on the server when its page settles.
 * @param onShare Called with the file on screen when sharing is asked for, or `null` to offer no
 *   sharing.
 * @param onMore Called with the file on screen when its options are asked for, or `null` to offer
 *   no options button.
 * @param notice What came of the last thing done to a file, already worded, or `null`. Announced as
 *   it appears.
 * @param onNoticeDismiss Called once [notice] has been up for [noticeDuration], to clear it, or
 *   `null` to leave it for the application to clear.
 * @param noticeDuration How long [notice] stays before [onNoticeDismiss] is called, before the
 *   platform's accessibility settings lengthen it.
 * @param title Heading over a file.
 * @param position Words where the file on screen sits among the rest, given its index and the
 *   count, or `null` to say nothing. Not drawn for a single file.
 * @param caption Line under a file, or `null` for none. Defaults to the file's name where the
 *   heading is not already the name, and its size.
 * @param closeLabel What the close button does, read out.
 * @param shareLabel What the share button does, read out.
 * @param moreLabel What the options button does, read out.
 * @param filmstripLabel Words a file of the filmstrip for assistive technology.
 * @param style Colours of the viewer. Defaults to the [LocalAttachmentViewerStyle] in scope, or
 *   [AttachmentViewerStyle.Default].
 * @param page Draws one file.
 * @param sheets Sheets drawn above the viewer, inside its window.
 */
@Composable
public fun AttachmentViewer(
    items: List<AttachmentItem>,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    initialKey: Any? = null,
    onRequest: (AttachmentItem) -> Unit = {},
    onShare: ((AttachmentItem) -> Unit)? = null,
    onMore: ((AttachmentItem) -> Unit)? = null,
    notice: String? = null,
    onNoticeDismiss: (() -> Unit)? = null,
    noticeDuration: Duration = 3.seconds,
    title: (AttachmentItem) -> String = { item -> item.label },
    position: ((index: Int, count: Int) -> String)? = { index, count -> "${index + 1} of $count" },
    caption: (AttachmentItem) -> String? = { item -> viewerCaption(item) },
    closeLabel: String = "Close",
    shareLabel: String = "Share",
    moreLabel: String = "More options",
    filmstripLabel: (item: AttachmentItem, index: Int, count: Int) -> String = { item, index, count ->
        "${item.label}, ${index + 1} of $count"
    },
    style: AttachmentViewerStyle = LocalAttachmentViewerStyle.current ?: AttachmentViewerStyle.Default,
    page: @Composable (AttachmentItem) -> Unit = { item ->
        AttachmentViewerPage(item = item, onRetry = { onRequest(item) }, style = style)
    },
    sheets: @Composable () -> Unit = {},
) {
    val latestRequest by rememberUpdatedState(onRequest)
    var index by remember { mutableIntStateOf(items.indexOfFirst { item -> item.key == initialKey }.coerceAtLeast(0)) }

    MediaViewer(
        itemCount = items.size,
        onDismiss = onDismiss,
        modifier = modifier,
        initialIndex = index,
        key = { page -> items[page].key.toString() },
        onIndexChange = { page -> index = page },
        // Keyed on the settled page, not the current one: a swipe passes through the pages between,
        // and none of those was asked for.
        onIndexSettle = { page ->
            val settled = items.getOrNull(page)
            if (settled?.state == AttachmentState.Remote) latestRequest(settled)
        },
        // A file is read, not looked at: its heading and filmstrip stay up, above and below it, so
        // nothing covers a page's edges.
        chrome = MediaViewerChrome.Always,
        bars = MediaViewerBars.Docked,
        notice = notice,
        onNoticeDismiss = onNoticeDismiss,
        noticeDuration = noticeDuration,
        style = MediaViewerStyle.dark(bg = style.bg).copy(noticeBg = style.pillBg, noticeContent = style.pillContent),
        topBar = { viewer ->
            val current = items.getOrNull(viewer.currentIndex)
            if (current != null) {
                val here = current.state == AttachmentState.Ready || current.state is AttachmentState.Uploading
                AttachmentViewerTopBar(
                    title = title(current),
                    position = position?.takeIf { items.size > 1 }?.invoke(viewer.currentIndex, items.size),
                    style = style,
                    closeLabel = closeLabel,
                    onClose = viewer::dismiss,
                    shareLabel = shareLabel,
                    onShare = onShare?.takeIf { here }?.let { share -> { share(current) } },
                    moreLabel = moreLabel,
                    onMore = onMore?.let { more -> { more(current) } },
                )
            }
        },
        footer = { viewer ->
            val line = items.getOrNull(viewer.currentIndex)?.let(caption)
            if (!line.isNullOrEmpty()) {
                Text(
                    text = line,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ArecaTheme.spacing.horizontalContentInsets),
                    style = ArecaTheme.typography.bodySmall,
                    color = style.detail,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.MiddleEllipsis,
                )
            }
            if (items.size > 1) {
                Spacer(Modifier.height(ArecaTheme.spacing.lg))
                AttachmentFilmstrip(
                    items = items,
                    current = viewer.currentIndex,
                    position = { viewer.currentIndex + viewer.currentIndexOffset },
                    onSelect = { page -> viewer.goTo(page) },
                    label = filmstripLabel,
                    style = style,
                )
            }
        },
        sheets = sheets,
    ) { at ->
        Box(Modifier.fillMaxSize()) { page(items[at]) }
    }
}

/**
 * The viewer's default line under a file: its name where the heading, its label, is something
 * else, and its size.
 *
 * @param item The file.
 * @return The line, or `null` when there is nothing to add to the heading.
 */
internal fun viewerCaption(item: AttachmentItem): String? =
    listOfNotNull(item.fileName.takeIf { name -> name != item.label }, item.size)
        .joinToString(" · ")
        .ifEmpty { null }
