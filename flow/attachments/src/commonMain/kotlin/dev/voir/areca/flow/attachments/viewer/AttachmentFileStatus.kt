package dev.voir.areca.flow.attachments.viewer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import dev.voir.areca.flow.attachments.AttachmentItem
import dev.voir.areca.flow.attachments.icon.BundledIcons
import dev.voir.areca.flow.attachments.icon.CloudOff
import dev.voir.areca.flow.attachments.icon.Download
import dev.voir.areca.flow.attachments.icon.LinkExternal
import dev.voir.areca.flow.attachments.part.icon
import dev.voir.areca.foundation.adaptive.AdaptiveDefaults
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.button.ButtonStyle
import dev.voir.areca.foundation.feedback.ProgressBar
import dev.voir.areca.foundation.feedback.ProgressBarStyle
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.foundation.icon.IconSize
import dev.voir.areca.foundation.notice.Notice
import dev.voir.areca.foundation.notice.NoticeStyle
import dev.voir.areca.foundation.notice.NoticeText
import dev.voir.areca.foundation.notice.NoticeTone
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.theme.ArecaTheme

/**
 * A file the viewer cannot draw right now, stated instead: the glyph for its kind, its name, a line
 * about it, and whatever there is to do about it.
 *
 * One layout for every such case — arriving, unreachable, gone, or a kind of file only another
 * application can open — so moving between them changes what is under the file and nothing above
 * it. [AttachmentViewerPage] fills it, inside its own dark scope. Centred, and no wider than
 * [AdaptiveDefaults.ReadableWidth], so on a wide window it stays a column rather than a banner.
 *
 * @param item The file.
 * @param detail Line under the name — what the file is and how large — or `null` for none.
 * @param style Colours of the viewer it is drawn in.
 * @param modifier Modifier applied to the frame.
 * @param content What to show under the file: progress, a notice, or the ways to open it elsewhere.
 */
@Composable
internal fun AttachmentFileStatus(
    item: AttachmentItem,
    detail: String?,
    style: AttachmentViewerStyle,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = ArecaTheme.spacing.xl3),
        contentAlignment = Alignment.Center,
    ) {
        // Capped where a phone never reaches, so on a tablet or a desktop the notice and the
        // buttons under the file keep a readable width rather than spanning the window.
        Column(
            modifier = Modifier
                .widthIn(max = AdaptiveDefaults.ReadableWidth)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .size(ArecaTheme.spacing.xl7)
                    .background(style.controlBg, ArecaTheme.shapes.tile),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = item.format.icon(),
                    contentDescription = null,
                    tint = style.controlContent,
                    size = IconSize.Large,
                )
            }

            Spacer(Modifier.height(ArecaTheme.spacing.xl2))

            Text(
                text = item.fileName,
                style = ArecaTheme.typography.labelLarge,
                color = style.title,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )

            if (!detail.isNullOrEmpty()) {
                Spacer(Modifier.height(ArecaTheme.spacing.xs2))

                Text(
                    text = detail,
                    style = ArecaTheme.typography.bodySmall,
                    color = style.detail,
                    textAlign = TextAlign.Center,
                )
            }

            Spacer(Modifier.height(ArecaTheme.spacing.xl3))

            content()
        }
    }
}

/**
 * The line under a stated file's name: its label where that is not the name already above it, and
 * its size.
 *
 * @param item The file.
 * @return The line, or `null` when there is nothing to add to the name.
 */
internal fun fileDetail(item: AttachmentItem): String? =
    listOfNotNull(item.label.takeIf { label -> label != item.fileName }, item.size)
        .joinToString(" · ")
        .ifEmpty { null }

/**
 * How much of a download has arrived.
 *
 * The theme's contrast fill on the raised track rather than the brand's: it is the one moving thing
 * on a neutral screen, and the brand would make it the loudest.
 *
 * Read out once: the bar is named by [name] and says its own share, and the line under it, which
 * says the same, is only drawn.
 *
 * @param progress Share that has arrived, `0..1`, or `null` when not known.
 * @param name What the bar is, for assistive technology, without the share.
 * @param label Line under the bar, worded for [progress].
 * @param style Colours of the viewer.
 */
@Composable
internal fun DownloadProgress(progress: Float?, name: String, label: String, style: AttachmentViewerStyle) {
    ProgressBar(
        progress = progress,
        modifier = Modifier.width(ArecaTheme.spacing.xl9 * 2),
        contentDescription = name,
        style = ProgressBarStyle.default(track = style.controlBg, fill = ArecaTheme.colors.bgContrast),
    )

    Spacer(Modifier.height(ArecaTheme.spacing.md))

    Text(
        text = label,
        modifier = Modifier.clearAndSetSemantics {},
        style = ArecaTheme.typography.bodySmall,
        color = style.detail,
    )
}

/**
 * A file that is wanted but not arriving, or that will never arrive: a neutral notice and, when the
 * application can try again, the way to ask.
 *
 * Neutral rather than a warning, even for a file that is gone: nothing the person did is wrong, and
 * a red block would say otherwise. [AttachmentViewerPage] announces it when it replaces a progress
 * bar someone may be waiting on.
 *
 * @param title What is the matter.
 * @param text More about it, or `null` for none.
 * @param style Colours of the viewer.
 * @param retryText Caption of the button that tries again.
 * @param onRetry Called when trying again is asked for, or `null` to offer no button.
 */
@Composable
internal fun AwayNotice(
    title: String,
    text: String?,
    style: AttachmentViewerStyle,
    retryText: String,
    onRetry: (() -> Unit)?,
) {
    Notice(
        title = title,
        modifier = Modifier.fillMaxWidth(),
        icon = BundledIcons.CloudOff,
        tone = NoticeTone.Neutral,
        style = NoticeStyle.of(tone = NoticeTone.Neutral, bg = style.controlBg),
    ) {
        if (text != null) NoticeText(text)
    }

    if (onRetry != null) {
        Spacer(Modifier.height(ArecaTheme.spacing.xl))

        Button(text = retryText, onClick = onRetry, style = style.secondaryButton())
    }
}

/**
 * A file only another application can open, and the ways to get it there.
 *
 * @param text What to say about it, or `null` for nothing.
 * @param style Colours of the viewer.
 * @param openText Caption of the button that opens it elsewhere.
 * @param onOpen Called when opening it elsewhere is asked for, or `null` to offer no button.
 * @param saveText Caption of the button that saves a copy.
 * @param onSave Called when saving a copy is asked for, or `null` to offer no button.
 */
@Composable
internal fun OpenElsewhere(
    text: String?,
    style: AttachmentViewerStyle,
    openText: String,
    onOpen: (() -> Unit)?,
    saveText: String,
    onSave: (() -> Unit)?,
) {
    if (text != null) {
        Text(
            text = text,
            style = ArecaTheme.typography.bodySmall,
            color = style.detail,
            textAlign = TextAlign.Center,
        )
    }

    if (onOpen == null && onSave == null) return

    if (text != null) Spacer(Modifier.height(ArecaTheme.spacing.xl3))

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
    ) {
        if (onOpen != null) {
            Button(
                onClick = onOpen,
                modifier = Modifier.fillMaxWidth(),
                style = ButtonStyle.solid(ActionColor.NeutralDark, shape = CircleShape),
                leading = { Icon(imageVector = BundledIcons.LinkExternal, contentDescription = null) },
            ) {
                Text(openText)
            }
        }

        if (onSave != null) {
            Button(
                onClick = onSave,
                modifier = Modifier.fillMaxWidth(),
                style = style.secondaryButton(),
                leading = { Icon(imageVector = BundledIcons.Download, contentDescription = null) },
            ) {
                Text(saveText)
            }
        }
    }
}
