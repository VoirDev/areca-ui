package dev.voir.areca.playground.showcase.chrome

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import dev.voir.areca.modifier.clickCursor
import dev.voir.areca.theme.ArecaTheme

private val TrackWidth = 36.dp
private val TrackHeight = 20.dp
private val ThumbSize = 14.dp

/**
 * A boolean switch in the showcase shell, used by boolean controls.
 *
 * Intentionally not the design system's `Toggle`: a control that turns a component's `enabled` prop
 * off must keep working when that component's own toggle is the thing being debugged.
 *
 * @param checked Current value.
 * @param onCheckedChange Invoked with the requested value.
 * @param modifier Modifier applied to the switch.
 * @param enabled Whether the switch responds to input.
 */
@Composable
fun ChromeSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }

    val trackColor by animateColorAsState(
        targetValue = when {
            !enabled -> ArecaTheme.colors.bgDisabled
            checked -> ArecaTheme.colors.bgBrandBase
            else -> ArecaTheme.colors.bgNeutralIntense
        },
        label = "chromeSwitchTrack",
    )
    val thumbOffset by animateDpAsState(
        targetValue = if (checked) TrackWidth - ThumbSize - 3.dp else 3.dp,
        label = "chromeSwitchThumb",
    )

    Box(
        modifier = modifier
            .width(TrackWidth)
            .height(TrackHeight)
            .clip(CircleShape)
            .background(trackColor, CircleShape)
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                indication = null,
                role = Role.Switch,
                onClick = { onCheckedChange(!checked) },
            )
            .clickCursor(enabled),
        contentAlignment = Alignment.CenterStart,
    ) {
        Box(
            modifier = Modifier
                .offset(x = thumbOffset)
                .size(ThumbSize)
                .clip(CircleShape)
                .background(ArecaTheme.colors.fgOnFill, CircleShape),
        )
    }
}

/**
 * A labelled switch row, the shape a boolean control takes in the control panel.
 *
 * @param label Control name.
 * @param checked Current value.
 * @param onCheckedChange Invoked with the requested value.
 * @param modifier Modifier applied to the row.
 * @param description Optional one-line hint under the label.
 */
@Composable
fun ChromeSwitchRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    description: String? = null,
) {
    ChromeControlRow(
        label = label,
        description = description,
        modifier = modifier.padding(vertical = ArecaTheme.spacing.xs2),
    ) {
        ChromeSwitch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
