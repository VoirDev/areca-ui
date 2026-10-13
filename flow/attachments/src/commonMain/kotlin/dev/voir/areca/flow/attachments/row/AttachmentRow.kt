package dev.voir.areca.flow.attachments.row

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import dev.voir.areca.flow.attachments.AttachmentItem
import dev.voir.areca.flow.attachments.AttachmentState
import dev.voir.areca.flow.attachments.part.AttachmentStateGlyph
import dev.voir.areca.flow.attachments.part.AttachmentThumbnail
import dev.voir.areca.flow.attachments.part.AttachmentTileStyle
import dev.voir.areca.flow.attachments.part.LocalAttachmentTileStyle
import dev.voir.areca.flow.attachments.part.defaultStateDescription
import dev.voir.areca.flow.attachments.part.hasGlyph
import dev.voir.areca.foundation.icon.IconSize
import dev.voir.areca.foundation.settings.SettingsRow
import dev.voir.areca.foundation.settings.SettingsRowStatus
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.theme.ArecaTheme

/**
 * One file as a row of a list: its square at the start, what it is, a line about it, and where its
 * bytes are at the end.
 *
 * For a place where files are read by name rather than recognised by sight — a record with a dozen
 * documents, a shared folder, the files on a support ticket — where a strip of squares would ask
 * someone to open each one to find the one they want. A foundation `SettingsRow`, so it goes in a
 * `Settings` `Section` beside any row the application writes itself, with the list's look, press
 * answer and hairlines.
 *
 * The square is small and drawn without its marks; where the bytes are is said at the row's end
 * instead — a ring filling while they move, a cloud while they are elsewhere — and a file still
 * being added shows the list's spinner there and cannot be pressed. The same is said to assistive
 * technology as the row's state, [stateDescription].
 *
 * @param item The file.
 * @param onClick Called when the row is pressed — usually to open the viewer — or `null` for a row
 *   that only states.
 * @param modifier Modifier applied to the row.
 * @param title First line. Defaults to the file's label; pass [AttachmentItem.fileName] where names
 *   are what the list is read by. A long one is shortened in the middle, keeping its extension.
 * @param subtitle Second line — the name, the size, who added it and when, already worded — or
 *   `null` for none.
 * @param onClickLabel What pressing does, for assistive technology ("Open"), or `null` for none.
 * @param stateDescription Where the file's bytes are, for assistive technology ("Not downloaded"),
 *   or `null` to say nothing. Defaults to English words for every state but
 *   [AttachmentState.Ready], which needs none.
 * @param style Colours and shape of the square, and the stroke of the ring at the row's end.
 *   Defaults to the [LocalAttachmentTileStyle] in scope, or else to the settings list's tile shape
 *   on the inset fill, so the square reads against a section's card in either theme.
 * @param thumbnailSize Side of the square.
 * @param enabled Whether the row accepts input.
 * @param trailing Content at the row's very end — an options button, a chevron — or `null` for
 *   none.
 */
@Composable
public fun AttachmentRow(
    item: AttachmentItem,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    title: String = item.label,
    subtitle: String? = null,
    onClickLabel: String? = null,
    stateDescription: String? = defaultStateDescription(item.state),
    style: AttachmentTileStyle = LocalAttachmentTileStyle.current ?: AttachmentTileStyle.default(
        bg = ArecaTheme.colors.bgMuted,
        shape = ArecaTheme.shapes.tile,
    ),
    thumbnailSize: Dp = ArecaTheme.spacing.xl5,
    enabled: Boolean = true,
    trailing: (@Composable BoxScope.() -> Unit)? = null,
) {
    val adding = item.state == AttachmentState.Adding

    SettingsRow(
        title = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.MiddleEllipsis,
            )
        },
        modifier = if (stateDescription != null) {
            modifier.semantics { this.stateDescription = stateDescription }
        } else {
            modifier
        },
        subtitle = subtitle?.let { line ->
            {
                Text(text = line, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        },
        value = if (item.state.hasGlyph()) {
            {
                AttachmentStateGlyph(
                    state = item.state,
                    color = ArecaTheme.colors.fgMuted,
                    size = IconSize.Medium,
                    ringThickness = style.ringThickness,
                )
            }
        } else {
            null
        },
        leading = {
            AttachmentThumbnail(
                item = item,
                style = style,
                size = thumbnailSize,
                marks = false,
            )
        },
        trailing = trailing,
        status = if (adding) SettingsRowStatus.Loading else SettingsRowStatus.Idle,
        enabled = enabled,
        onClick = onClick.takeUnless { adding },
        onClickLabel = onClickLabel,
    )
}
