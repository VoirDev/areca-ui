package dev.voir.areca.flow.attachments.sheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import dev.voir.areca.foundation.LocalContentColor
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.foundation.list.ListItem
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.theme.ArecaHues
import dev.voir.areca.theme.ArecaTheme

/**
 * One thing an attachment sheet offers: a glyph, what choosing it does, and the call it makes.
 *
 * [AttachmentSourceOptions] and [AttachmentOptions] build the common ones with the module's glyphs;
 * anything else a product offers — scan a document, import from a drive — is one of these of its
 * own.
 *
 * [description] and [hue] are for [AttachmentChoiceLayout.Bento], where each choice is a card with
 * room for a line on what it is for and a colour of its own. Rows draw neither: a row of an action
 * sheet is a glyph and its label, as every other action sheet lists its actions.
 *
 * @property label What choosing it does.
 * @property icon Glyph beside the label.
 * @property onClick Called when it is chosen. The sheet does not close itself.
 * @property isDestructive Whether choosing it loses something, which draws it in the danger colour.
 *   Off for the module's own removal: what keeps a file from being removed by accident is the
 *   confirmation behind the choice, not the colour of the row.
 * @property description What the choice is for, in a few words — "Snap a receipt", "PDF or
 *   document" — drawn under the label on a bento card, or `null` for none.
 * @property hue Colour of the choice's card in a bento: its glyph's disc, and a wash of it over the
 *   sheet. One of [dev.voir.areca.theme.ArecaTheme.hues], one per kind of source. [Color.Unspecified]
 *   takes the [AttachmentSourceOptions] default for the module's own sources, and grey for anything
 *   else (red for a destructive choice).
 */
@Stable
public class AttachmentChoice internal constructor(
    public val label: String,
    public val icon: ImageVector,
    public val onClick: () -> Unit,
    public val isDestructive: Boolean,
    public val description: String?,
    public val hue: Color,
    // A hue of the theme picked where the card is drawn, for the module's own sources: their
    // builders are not composable, so they cannot read the theme themselves.
    internal val defaultHue: ((ArecaHues) -> Color)?,
) {
    /**
     * A choice of the product's own.
     *
     * @param label What choosing it does.
     * @param icon Glyph beside the label.
     * @param onClick Called when it is chosen.
     * @param isDestructive Whether choosing it loses something.
     * @param description What the choice is for, on a bento card, or `null` for none.
     * @param hue Colour of its bento card, or [Color.Unspecified] for grey.
     */
    public constructor(
        label: String,
        icon: ImageVector,
        onClick: () -> Unit,
        isDestructive: Boolean = false,
        description: String? = null,
        hue: Color = Color.Unspecified,
    ) : this(label, icon, onClick, isDestructive, description, hue, defaultHue = null)
}

/** How [AttachmentSourceSheet] lays its choices out. */
public enum class AttachmentChoiceLayout {
    /**
     * Rows laid straight on the sheet, each a glyph and its label: the flat sheet every other
     * choice in the system takes. The default, and the one for choices that are equals.
     */
    Rows,

    /**
     * Cards, each washed in its choice's [AttachmentChoice.hue] and carrying its
     * [AttachmentChoice.description]: the first full width and larger, the rest side by side in one
     * row under it, all as tall as the tallest. Two choices share the row as halves.
     *
     * For a short, fixed set of sources where one leads — the camera, when a file is nearly always
     * a receipt photographed at the till — and each deserves a target the size of a thumb and a
     * line on what it is for. It is a deliberate exception to the flat sheet, so keep it to two to
     * four choices; a longer list reads better as [Rows].
     */
    Bento,
}

/**
 * The choices of a sheet as rows laid straight on it, edge to edge, each a glyph and what choosing
 * it does — the shape every action sheet takes, as [dev.voir.areca.foundation.adaptive.AdaptiveMenu]
 * lists its actions. A sheet is already a segment of its own; its choices need no block of their own.
 *
 * Draws nothing for no choices. Each row is at least the platform's minimum target tall.
 *
 * @param choices What to offer, in order.
 * @param modifier Modifier applied to the column of rows.
 */
@Composable
internal fun AttachmentChoiceList(
    choices: List<AttachmentChoice>,
    modifier: Modifier = Modifier,
) {
    if (choices.isEmpty()) return

    Column(modifier = modifier.fillMaxWidth()) {
        for (choice in choices) {
            val ink = if (choice.isDestructive) ArecaTheme.colors.fgDanger else LocalContentColor.current
            ListItem(
                title = { Text(text = choice.label, color = ink) },
                onClick = choice.onClick,
                leading = { Icon(imageVector = choice.icon, contentDescription = null, tint = ink) },
            )
        }
    }
}
