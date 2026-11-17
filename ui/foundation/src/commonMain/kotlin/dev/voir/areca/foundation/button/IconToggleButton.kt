package dev.voir.areca.foundation.button

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.selection.toggleable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.indication.reshapedTo
import dev.voir.areca.modifier.clickCursor
import dev.voir.areca.modifier.minimumTouchTarget

/**
 * An icon that is on or off — a favourite star, a pin, a mute — announced as a toggle with its state
 * rather than as a button.
 *
 * Draws [checkedImageVector] while [checked] (a filled star), [imageVector] otherwise, in
 * [checkedStyle] and [style] respectively. [contentDescription] names the thing toggled ("Favourite"),
 * and assistive technology adds whether it is on.
 *
 * @param checked Whether it is on.
 * @param onCheckedChange Called with the value the user asks for.
 * @param imageVector Glyph while off.
 * @param contentDescription What is toggled, already localized.
 * @param modifier Modifier applied to the button.
 * @param checkedImageVector Glyph while on. Defaults to [imageVector].
 * @param style Look while off. Defaults to the plain neutral icon button.
 * @param checkedStyle Look while on. Defaults to the plain brand icon button.
 * @param size Metrics. Defaults to the theme's.
 * @param enabled Whether it accepts input.
 * @param interactionSource Source of its interactions; pass one to observe them.
 */
@Composable
public fun IconToggleButton(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    imageVector: ImageVector,
    contentDescription: String,
    modifier: Modifier = Modifier,
    checkedImageVector: ImageVector = imageVector,
    style: IconButtonStyle = IconButtonStyle.plain(ActionColor.Neutral),
    checkedStyle: IconButtonStyle = IconButtonStyle.plain(ActionColor.Brand),
    size: IconButtonSize = LocalIconButtonSize.current ?: IconButtonSize.Medium,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
) {
    val current = if (checked) checkedStyle else style
    IconButton(
        // The toggle below owns the press; the button only draws.
        onClick = null,
        modifier = modifier
            .minimumTouchTarget(size.touchTarget)
            .toggleable(
                value = checked,
                enabled = enabled,
                role = Role.Checkbox,
                interactionSource = interactionSource,
                indication = current.indication.reshapedTo(current.shape),
                onValueChange = onCheckedChange,
            )
            .semantics { this.contentDescription = contentDescription }
            .clickCursor(enabled),
        style = current,
        size = size,
        enabled = enabled,
    ) {
        Icon(imageVector = if (checked) checkedImageVector else imageVector, contentDescription = null)
    }
}
