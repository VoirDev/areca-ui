package dev.voir.areca.flow.attachments.sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.foundation.icon.IconSize
import dev.voir.areca.foundation.layout.HorizontalDivider
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.indication.FadeIndication
import dev.voir.areca.modifier.MinimumTouchTarget
import dev.voir.areca.modifier.clickCursor
import dev.voir.areca.theme.ArecaTheme

/**
 * One thing an attachment sheet offers: a glyph, what choosing it does, and the call it makes.
 *
 * [AttachmentSourceOptions] and [AttachmentOptions] build the common ones with the module's glyphs;
 * anything else a product offers — scan a document, import from a drive — is one of these of its
 * own.
 *
 * @property label What choosing it does.
 * @property icon Glyph beside the label.
 * @property onClick Called when it is chosen. The sheet does not close itself.
 * @property isDestructive Whether choosing it loses something, which draws it in the danger colour.
 *   Off for the module's own removal: what keeps a file from being removed by accident is the
 *   confirmation behind the choice, not the colour of the row.
 */
@Stable
public class AttachmentChoice(
    public val label: String,
    public val icon: ImageVector,
    public val onClick: () -> Unit,
    public val isDestructive: Boolean = false,
)

/**
 * A block of choices on a sheet, ruled apart on a grey set off from the white sheet.
 *
 * The shape the module's source and options sheets list their choices in. Draws nothing for no
 * choices, rather than an empty block.
 *
 * Each choice is at least the platform's minimum target tall, and its press and focus answer follow
 * the block's rounded corners at the ends rather than being cut off by them.
 *
 * @param choices What to offer, in order.
 * @param modifier Modifier applied to the block.
 * @param bg Fill of the block. The subtle grey reads on a white sheet.
 */
@Composable
internal fun AttachmentChoiceList(
    choices: List<AttachmentChoice>,
    modifier: Modifier = Modifier,
    bg: Color = ArecaTheme.colors.bgSubtle,
) {
    if (choices.isEmpty()) return

    val radius = ArecaTheme.radius.lg
    val square = ArecaTheme.radius.none

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(bg, RoundedCornerShape(radius)),
    ) {
        choices.forEachIndexed { index, choice ->
            if (index > 0) HorizontalDivider()

            val tint = if (choice.isDestructive) ArecaTheme.colors.fgDanger else ArecaTheme.colors.fgBase
            val shape = RoundedCornerShape(
                topStart = if (index == 0) radius else square,
                topEnd = if (index == 0) radius else square,
                bottomStart = if (index == choices.lastIndex) radius else square,
                bottomEnd = if (index == choices.lastIndex) radius else square,
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = MinimumTouchTarget)
                    .clickCursor()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = FadeIndication.tint(shape, ink = tint),
                        role = Role.Button,
                        onClick = choice.onClick,
                    )
                    .padding(
                        horizontal = ArecaTheme.spacing.xl,
                        vertical = ArecaTheme.spacing.lg,
                    ),
                horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = choice.icon,
                    contentDescription = null,
                    tint = tint,
                    size = IconSize.Medium,
                )

                Text(
                    text = choice.label,
                    style = ArecaTheme.typography.labelMedium,
                    color = tint,
                )
            }
        }
    }
}
