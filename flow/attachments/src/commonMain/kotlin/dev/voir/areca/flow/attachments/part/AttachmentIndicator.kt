package dev.voir.areca.flow.attachments.part

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.Dp
import dev.voir.areca.flow.attachments.icon.BundledIcons
import dev.voir.areca.flow.attachments.icon.Paperclip
import dev.voir.areca.foundation.LocalContentColor
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.foundation.icon.IconSize
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.theme.ArecaTheme

/**
 * A paperclip, and optionally how many, on something that has files attached — the mark someone
 * hunting for "the one with the receipt" scans a list for.
 *
 * Inline and quiet: it takes the colour and the text style of the line it sits in, so it goes at
 * the start of a row's subtitle, beside a date, or after a title without restyling. Put it on the
 * line that is not the one the list is read by, so a title and its figure stay the pair a column is
 * scanned in.
 *
 * Read out as one thing, [contentDescription], rather than as a glyph and a number.
 *
 * @param modifier Modifier applied to the mark.
 * @param count How many files, already worded ("3"), or `null` for the paperclip alone.
 * @param contentDescription What the mark means, read out in its place ("3 attachments").
 * @param color Colour of the paperclip and the count. Defaults to the surrounding content colour.
 * @param size Side of the paperclip.
 */
@Composable
public fun AttachmentIndicator(
    modifier: Modifier = Modifier,
    count: String? = null,
    contentDescription: String = "Has attachments",
    color: Color = LocalContentColor.current,
    size: Dp = IconSize.ExtraSmall,
) {
    Row(
        modifier = modifier.clearAndSetSemantics { this.contentDescription = contentDescription },
        horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xs2),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = BundledIcons.Paperclip,
            contentDescription = null,
            tint = color,
            size = size,
        )

        if (count != null) {
            Text(text = count, color = color, maxLines = 1)
        }
    }
}
