package dev.voir.areca.flow.attachments.part

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.Dp
import dev.voir.areca.flow.attachments.AttachmentFormat
import dev.voir.areca.flow.attachments.AttachmentItem
import dev.voir.areca.flow.attachments.AttachmentState
import dev.voir.areca.flow.attachments.attachmentPictureLoader
import dev.voir.areca.foundation.feedback.Spinner
import dev.voir.areca.foundation.feedback.SpinnerSize
import dev.voir.areca.foundation.feedback.SpinnerStyle
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.foundation.icon.IconSize
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.media.MediaImageState
import dev.voir.areca.theme.ArecaTheme

/**
 * The square a file is recognised by: the file itself where it can be drawn, its kind of file
 * where it cannot.
 *
 * **A picture is the only honest thumbnail.** Where there is no picture — a spreadsheet, a PDF, or
 * any file whose bytes are not on this device — the square says what sort of file it is, a glyph
 * and its extension, rather than inventing a stand-in. The glyph is drawn first and covered by the
 * picture once it decodes, so a picture still loading, or one that cannot be decoded, still says
 * what the file is instead of leaving an empty square.
 *
 * The picture comes from [LocalMediaImageLoader][dev.voir.areca.media.LocalMediaImageLoader]'s
 * [loadSquare][dev.voir.areca.media.MediaImageLoader.loadSquare], asked for at the
 * size that covers the square — never at the file's, which for a photograph is thousands of pixels
 * on its long edge.
 *
 * **Marks** say the one thing about the bytes that matters on sight. A disc in the corner shows
 * them arriving or leaving (a ring filling), there to be fetched, or gone; a veil and a spinner
 * cover a file still being added, with the picture visible under it, so the square reads as *this
 * file, not finished yet* rather than an empty slot.
 *
 * Not pressable by itself: [AttachmentTile] and
 * [AttachmentRow][dev.voir.areca.flow.attachments.row.AttachmentRow] decide what pressing does.
 *
 * Silent to assistive technology but for what its caller sets through [modifier]: the part that
 * presses it names the file and says where its bytes are.
 *
 * @param item The file.
 * @param modifier Modifier applied to the square.
 * @param style Colours and shape. Defaults to the [LocalAttachmentTileStyle] in scope, or
 *   [AttachmentTileStyle.Default].
 * @param size Side of the square.
 * @param marks Whether to draw the extension, the corner disc and the veil. Leave them off where
 *   the square is too small for them to annotate anything without covering it, or where the state
 *   is said elsewhere, as at the end of a row.
 */
@Composable
public fun AttachmentThumbnail(
    item: AttachmentItem,
    modifier: Modifier = Modifier,
    style: AttachmentTileStyle = LocalAttachmentTileStyle.current ?: AttachmentTileStyle.Default,
    size: Dp = ArecaTheme.spacing.xl8,
    marks: Boolean = true,
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(style.shape)
            .background(style.bg),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clearAndSetSemantics {},
            contentAlignment = Alignment.Center,
        ) {
            FilePlaceholder(
                icon = item.format.icon(),
                extension = item.extension.takeIf { marks },
                style = style,
                glyphSize = if (marks) IconSize.Large else IconSize.Medium,
            )

            val model = item.model
            if (item.format == AttachmentFormat.Image && model != null) {
                val side = with(LocalDensity.current) { size.roundToPx() }
                val picture = attachmentPictureLoader().loadSquare(model = model, side = side)

                if (picture is MediaImageState.Ready) {
                    Image(
                        painter = picture.painter,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }

            if (marks && item.state.hasGlyph()) {
                CornerMark(state = item.state, style = style)
            }

            if (marks && item.state == AttachmentState.Adding) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(style.veil),
                    contentAlignment = Alignment.Center,
                ) {
                    Spinner(style = SpinnerStyle(color = style.markContent), size = SpinnerSize.Large)
                }
            }
        }
    }
}

/**
 * A file that cannot be drawn: the glyph for its kind and, where there is room, its extension.
 *
 * @param icon Glyph for the kind of file.
 * @param extension Extension to print under it, or `null` for none.
 * @param style Colours.
 * @param glyphSize Side of the glyph.
 */
@Composable
private fun FilePlaceholder(
    icon: ImageVector,
    extension: String?,
    style: AttachmentTileStyle,
    glyphSize: Dp,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xs),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = style.glyph,
            size = glyphSize,
        )

        if (extension != null) {
            Text(
                text = extension,
                style = ArecaTheme.typography.labelSmall,
                color = style.extension,
                maxLines = 1,
            )
        }
    }
}

/**
 * The disc in the square's corner that says where the bytes are.
 *
 * @param state Where they are.
 * @param style Colours.
 */
@Composable
private fun BoxScope.CornerMark(state: AttachmentState, style: AttachmentTileStyle) {
    Box(
        modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(ArecaTheme.spacing.sm)
            .size(ArecaTheme.spacing.xl3)
            .background(style.markBg, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        AttachmentStateGlyph(
            state = state,
            color = style.markContent,
            size = IconSize.Small,
            ringThickness = style.ringThickness,
        )
    }
}
