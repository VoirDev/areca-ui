package dev.voir.areca.playground.showcase.chrome

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
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
 * Emphasis levels for a shell button.
 */
enum class ChromeButtonTone {
    /** Accent-filled; at most one per area. */
    Accent,

    /** Bordered, neutral fill. */
    Outline,

    /** No fill or border until hovered. */
    Ghost,
}

/**
 * A button in the showcase shell — resetting controls, toolbar actions, dismissing a panel.
 *
 * Never used inside a preview: previews contain the design system's own `Button`.
 *
 * @param label Button text.
 * @param onClick Invoked on click.
 * @param modifier Modifier applied to the button.
 * @param tone Emphasis level.
 * @param enabled Whether the button responds to input.
 */
@Composable
fun ChromeButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tone: ChromeButtonTone = ChromeButtonTone.Outline,
    enabled: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val hovered by interactionSource.collectIsHoveredAsState()
    val shape = RoundedCornerShape(ArecaTheme.radius.md)

    val background = when {
        !enabled -> ArecaTheme.colors.bgDisabled
        tone == ChromeButtonTone.Accent && hovered -> ArecaTheme.colors.bgBrandIntense
        tone == ChromeButtonTone.Accent -> ArecaTheme.colors.bgBrandBase
        hovered -> ArecaTheme.colors.bgNeutralMuted
        tone == ChromeButtonTone.Outline -> ArecaTheme.colors.bgSurface
        else -> ArecaTheme.colors.bgSurface.copy(alpha = 0f)
    }
    val contentTone = when {
        !enabled -> ChromeTone.Muted
        tone == ChromeButtonTone.Accent -> ChromeTone.OnAccent
        else -> ChromeTone.Primary
    }

    Box(
        modifier = modifier
            .clip(shape)
            .background(background, shape)
            .then(
                if (tone == ChromeButtonTone.Outline) {
                    Modifier.border(1.dp, ArecaTheme.colors.borderBase, shape)
                } else {
                    Modifier
                }
            )
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                indication = null,
                role = Role.Button,
                onClick = onClick,
            )
            .clickCursor(enabled)
            .padding(
                horizontal = ArecaTheme.spacing.lg,
                vertical = ArecaTheme.spacing.md,
            ),
        contentAlignment = Alignment.Center,
    ) {
        ChromeText(text = label, role = ChromeTextRole.Label, tone = contentTone, maxLines = 1)
    }
}
