package dev.voir.areca.playground.showcase.chrome

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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

/**
 * A segmented picker for a small set of mutually exclusive options.
 *
 * Used by enum controls with few enough entries to show at once, and by the preview canvas toolbar.
 * Past roughly five options a dropdown reads better; the caller decides.
 *
 * @param options Available values, in display order.
 * @param selected Currently selected value.
 * @param onSelect Invoked with the requested value.
 * @param label Renders an option as its button text.
 * @param modifier Modifier applied to the picker.
 */
@Composable
fun <T> ChromeSegmented(
    options: List<T>,
    selected: T,
    onSelect: (T) -> Unit,
    label: (T) -> String,
    modifier: Modifier = Modifier,
) {
    val trackShape = RoundedCornerShape(ArecaTheme.radius.md)

    Row(
        modifier = modifier
            .clip(trackShape)
            .background(ArecaTheme.colors.bgNeutralMuted, trackShape)
            .padding(ArecaTheme.spacing.xs2),
        horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xs2),
    ) {
        options.forEach { option ->
            ChromeSegment(
                label = label(option),
                selected = option == selected,
                onClick = { onSelect(option) },
            )
        }
    }
}

@Composable
private fun ChromeSegment(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val shape = RoundedCornerShape(ArecaTheme.radius.sm)

    val background by animateColorAsState(
        targetValue = if (selected) {
            ArecaTheme.colors.bgSurface
        } else {
            ArecaTheme.colors.bgSurface.copy(alpha = 0f)
        },
        label = "chromeSegmentBackground",
    )

    Box(
        modifier = Modifier
            .clip(shape)
            .background(background, shape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                role = Role.Tab,
                onClick = onClick,
            )
            .clickCursor()
            .padding(horizontal = ArecaTheme.spacing.md, vertical = 5.dp),
        contentAlignment = Alignment.Center,
    ) {
        ChromeText(
            text = label,
            role = ChromeTextRole.Label,
            tone = if (selected) ChromeTone.Primary else ChromeTone.Muted,
            maxLines = 1,
        )
    }
}
