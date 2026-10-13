package dev.voir.areca.flow.attachments.part

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import dev.voir.areca.flow.attachments.icon.BundledIcons
import dev.voir.areca.flow.attachments.icon.Plus
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.foundation.icon.IconSize
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.indication.FadeIndication
import dev.voir.areca.modifier.clickCursor
import dev.voir.areca.theme.ArecaTheme

/**
 * The square that adds a file, shaped like the files beside it.
 *
 * A filled square rather than a dashed outline, so it sits in a strip as one more square instead
 * of as a hole in it. Put it **first** in a strip: a strip that ends in its add square hides the
 * square as soon as it scrolls.
 *
 * Square and caption are one control, read out as [label]; the press answer and the focus ring
 * are drawn on the square.
 *
 * @param onClick Called when it is pressed — usually to ask where the file comes from.
 * @param modifier Modifier applied to the tile.
 * @param caption Word under the square, or `null` for none. A strip that captions its files
 *   usually captions this one too ("Add"), so the squares line up.
 * @param label What pressing it does, read out in place of the plus and the caption.
 * @param style Colours and shape. Defaults to the [LocalAttachmentTileStyle] in scope, or
 *   [AttachmentTileStyle.Default].
 * @param size Side of the square; the same as the files beside it.
 * @param enabled Whether it can be pressed.
 */
@Composable
public fun AttachmentAddTile(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    caption: String? = null,
    label: String = "Add attachment",
    style: AttachmentTileStyle = LocalAttachmentTileStyle.current ?: AttachmentTileStyle.Default,
    size: Dp = ArecaTheme.spacing.xl8,
    enabled: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = modifier
            .width(size)
            .semantics(mergeDescendants = true) { contentDescription = label }
            .clickCursor(enabled = enabled)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                role = Role.Button,
                onClick = onClick,
            ),
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .background(style.bg, style.shape)
                .indication(interactionSource, FadeIndication.tint(style.shape)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = BundledIcons.Plus,
                contentDescription = null,
                tint = if (enabled) style.content else style.contentDisabled,
                size = IconSize.Large,
            )
        }

        if (caption != null) {
            Spacer(Modifier.height(ArecaTheme.spacing.sm))

            Text(
                text = caption,
                modifier = Modifier.clearAndSetSemantics {},
                style = ArecaTheme.typography.bodySmall,
                color = if (enabled) style.caption else style.captionDisabled,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}
