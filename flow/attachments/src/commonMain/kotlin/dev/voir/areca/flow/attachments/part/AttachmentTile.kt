package dev.voir.areca.flow.attachments.part

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import dev.voir.areca.flow.attachments.AttachmentItem
import dev.voir.areca.flow.attachments.AttachmentState
import dev.voir.areca.flow.attachments.icon.BundledIcons
import dev.voir.areca.flow.attachments.icon.XClose
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.foundation.icon.IconSize
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.indication.FadeIndication
import dev.voir.areca.modifier.MinimumTouchTarget
import dev.voir.areca.modifier.clickCursor
import dev.voir.areca.theme.ArecaTheme

/**
 * One file in a strip: its square, a line saying what it is, and optionally a second line.
 *
 * **Captioned with what the file is, not what it is named.** [AttachmentItem.label] is the word to
 * scan a strip for — "Receipt" — and the name, which is rarely more than `IMG_2041.jpg`, is read out
 * with it and stated in the viewer.
 *
 * **Removing is a mark on the square**, for a place where taking a file off is as reversible as
 * typing — a form that saves nothing until it is saved. Where removing is final, leave [onRemove]
 * out and offer it behind a confirmation instead; the corner stays free for the square's own marks.
 * The mark's pressable area is the whole corner around its small disc, the platform's minimum
 * target, because a missed 24-point target on a thumbnail opens the file instead.
 *
 * A file still being added cannot be opened: there is nothing to show yet.
 *
 * **The tile reads as one control**, square and lines together, named by [description] — the label
 * and the file's name, so a strip captioned with tags still says which file is which — followed by
 * [detail], with where its bytes are as its state ([stateDescription]), since the corner disc that
 * shows it is only drawn. The whole tile takes the press; the press answer and the focus ring are
 * drawn on the square. The remove mark is a control of its own.
 *
 * @param item The file.
 * @param onClick Called when the tile is pressed — usually to open the viewer — or `null` for a tile
 *   that only shows.
 * @param modifier Modifier applied to the tile.
 * @param caption Line under the square, or `null` for none. Defaults to the file's label; pass a
 *   word of your own while the file is being added ("Adding…"). Drawn only: [description] and
 *   [stateDescription] say it to assistive technology.
 * @param detail Quieter second line — usually [AttachmentItem.size], where opening a file not yet
 *   on this device costs its size — or `null` for none. Read out after [description].
 * @param onRemove Called when the remove mark is pressed, or `null` to draw no mark.
 * @param removeLabel What the remove mark does, read out in place of its glyph.
 * @param description What the tile is called for assistive technology. Defaults to the label and
 *   the file's name ("Receipt, IMG_2041.jpg"), or the name alone when the label is the name.
 * @param stateDescription Where the file's bytes are, for assistive technology ("Uploading, 40%"),
 *   or `null` to say nothing. Defaults to English words for every state but
 *   [AttachmentState.Ready], which needs none.
 * @param style Colours and shape. Defaults to the [LocalAttachmentTileStyle] in scope, or
 *   [AttachmentTileStyle.Default].
 * @param size Side of the square.
 */
@Composable
public fun AttachmentTile(
    item: AttachmentItem,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    caption: String? = item.label,
    detail: String? = null,
    onRemove: (() -> Unit)? = null,
    removeLabel: String = "Remove ${item.label}",
    description: String = defaultDescription(item),
    stateDescription: String? = defaultStateDescription(item.state),
    style: AttachmentTileStyle = LocalAttachmentTileStyle.current ?: AttachmentTileStyle.Default,
    size: Dp = ArecaTheme.spacing.xl8,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val spoken = spokenTile(description, detail)
    val enabled = item.state != AttachmentState.Adding
    val words = Modifier.semantics(mergeDescendants = true) {
        contentDescription = spoken
        if (stateDescription != null) this.stateDescription = stateDescription
    }
    val press = onClick?.let { click ->
        Modifier
            .clickCursor(enabled = enabled)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                role = Role.Button,
                onClick = click,
            )
    } ?: Modifier

    Column(modifier = modifier.width(size).then(words).then(press)) {
        Box {
            AttachmentThumbnail(
                item = item,
                style = style,
                size = size,
                modifier = if (onClick != null) {
                    Modifier.indication(interactionSource, FadeIndication.tint(style.shape))
                } else {
                    Modifier
                },
            )

            if (onRemove != null) {
                RemoveMark(
                    label = removeLabel,
                    onClick = onRemove,
                    style = style,
                    modifier = Modifier.align(Alignment.TopEnd),
                )
            }
        }

        if (caption != null) {
            Spacer(Modifier.height(ArecaTheme.spacing.sm))

            Text(
                text = caption,
                modifier = Modifier.clearAndSetSemantics {},
                style = ArecaTheme.typography.bodySmall,
                color = style.caption,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

        if (detail != null) {
            if (caption == null) Spacer(Modifier.height(ArecaTheme.spacing.sm))

            Text(
                text = detail,
                modifier = Modifier.clearAndSetSemantics {},
                style = ArecaTheme.typography.bodyExtraSmall,
                color = style.detail,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

/**
 * What a tile is called for assistive technology: its name, then its second line when it has one.
 *
 * @param description What the tile is called.
 * @param detail The tile's second line, or `null` for none.
 * @return The words, the two joined by a comma when both are there.
 */
internal fun spokenTile(description: String, detail: String?): String =
    if (detail.isNullOrEmpty()) description else "$description, $detail"

/**
 * The cross in a square's corner.
 *
 * The pressable corner is the platform's minimum target; the press and focus answer are drawn on
 * the disc, which is what the eye finds.
 *
 * @param label What pressing it does, read out in place of the glyph.
 * @param onClick Called when it is pressed.
 * @param style Colours.
 * @param modifier Modifier applied to the pressable corner.
 */
@Composable
private fun RemoveMark(
    label: String,
    onClick: () -> Unit,
    style: AttachmentTileStyle,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .size(MinimumTouchTarget)
            .semantics { contentDescription = label }
            .clickCursor()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                role = Role.Button,
                onClick = onClick,
            ),
        contentAlignment = Alignment.TopEnd,
    ) {
        Box(
            modifier = Modifier
                .padding(ArecaTheme.spacing.sm)
                .size(ArecaTheme.spacing.xl3)
                .background(style.removeBg, CircleShape)
                .indication(interactionSource, FadeIndication.tint(CircleShape, ink = style.removeContent)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = BundledIcons.XClose,
                contentDescription = null,
                tint = style.removeContent,
                size = IconSize.Small,
            )
        }
    }
}
