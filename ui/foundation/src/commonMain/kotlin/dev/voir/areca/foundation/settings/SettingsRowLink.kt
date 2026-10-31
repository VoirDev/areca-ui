package dev.voir.areca.foundation.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Constraints
import dev.voir.areca.foundation.list.LocalListItemState
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.indication.FadeIndication
import dev.voir.areca.modifier.clickCursor
import dev.voir.areca.theme.ArecaTheme

/**
 * A second action at the end of a pressable settings row: "Verify" beside an unverified address,
 * "Resend" beside a pending invitation.
 *
 * The row itself is the main action — pressing an address row changes the address — so the link is
 * set apart from the row's value by an underline rather than by color: it is in the row's own ink,
 * like every other piece of copy on the card. Its target is at least the sizing's
 * [touch target][dev.voir.areca.theme.ArecaSizing.touchTarget] each way — 48dp under touch, none
 * under a pointer — however short its word, because the row around it is pressable too and a near
 * miss would land on the row; the target overhangs the line rather than making the row taller, so
 * the link lines up with the title like any other trailing content.
 *
 * Put it in the row's `trailing` slot. When the row has only one action, make the row the action and
 * name it in the row's `value` instead — "Link", "Download" — rather than adding a link.
 *
 * @param text What the link says.
 * @param onClick Called when the link is pressed.
 * @param modifier Modifier applied to the link's target.
 * @param enabled Whether the link accepts a press; drawn in the disabled ink when not, and always
 * disabled in a disabled row.
 * @param onClickLabel What pressing does, for assistive technology, when [text] alone does not say.
 */
@Composable
public fun SettingsRowLink(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClickLabel: String? = null,
) {
    val rowEnabled = enabled && LocalListItemState.current.enabled
    val style = SettingsTheme.styles.row
    val shape = ArecaTheme.shapes.md
    val minTarget = ArecaTheme.sizing.touchTarget

    Layout(
        modifier = modifier,
        content = {
            Box(
                modifier = Modifier
                    .clickCursor(enabled = rowEnabled)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = FadeIndication.tint(shape),
                        enabled = rowEnabled,
                        onClickLabel = onClickLabel,
                        role = Role.Button,
                        onClick = onClick,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = text,
                    style = SettingsTheme.sizes.row.valueTextStyle,
                    color = if (rowEnabled) style.title else style.titleDisabled,
                    textDecoration = TextDecoration.Underline,
                    maxLines = 1,
                )
            }
        },
    ) { measurables, constraints ->
        val target = measurables.single()
        // The line's own height is what the row lays out around; the target grows past it.
        val lineHeight = target.minIntrinsicHeight(Constraints.Infinity)
        val placeable = target.measure(
            constraints.copy(
                minWidth = minTarget.roundToPx().coerceAtMost(constraints.maxWidth),
                minHeight = minTarget.roundToPx().coerceAtMost(constraints.maxHeight),
            )
        )
        val height = lineHeight.coerceIn(constraints.minHeight, constraints.maxHeight)

        layout(placeable.width, height) {
            placeable.place(0, (height - placeable.height) / 2)
        }
    }
}
