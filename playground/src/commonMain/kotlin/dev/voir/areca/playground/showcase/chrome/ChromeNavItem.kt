package dev.voir.areca.playground.showcase.chrome

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import dev.voir.areca.modifier.clickCursor
import dev.voir.areca.theme.ArecaTheme

/**
 * One selectable page in the sidebar.
 *
 * @param label Page title.
 * @param selected Whether this is the page currently shown.
 * @param onClick Invoked when the row is chosen.
 * @param modifier Modifier applied to the row.
 * @param trailing Optional trailing content, such as a status badge.
 */
@Composable
fun ChromeNavItem(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    trailing: (@Composable () -> Unit)? = null,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val hovered by interactionSource.collectIsHoveredAsState()
    val shape = RoundedCornerShape(ArecaTheme.radius.md)

    val background by animateColorAsState(
        targetValue = when {
            selected -> ArecaTheme.colors.bgBrandMuted
            hovered -> ArecaTheme.colors.bgNeutralMuted
            else -> ArecaTheme.colors.bgCanvas.copy(alpha = 0f)
        },
        label = "chromeNavItemBackground",
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(background, shape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                role = Role.Tab,
                onClick = onClick,
            )
            .clickCursor()
            .padding(
                horizontal = ArecaTheme.spacing.lg,
                vertical = ArecaTheme.spacing.md,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
    ) {
        Box(modifier = Modifier.weight(1f)) {
            ChromeText(
                text = label,
                role = ChromeTextRole.Label,
                tone = if (selected) ChromeTone.Accent else ChromeTone.Secondary,
                maxLines = 1,
            )
        }

        trailing?.invoke()
    }
}

/**
 * A collapsible group heading in the sidebar: a disclosure chevron, the group name and how many
 * pages it holds.
 *
 * @param label Group name.
 * @param count How many pages the group holds, shown whether or not it is expanded.
 * @param expanded Whether the group's pages are listed below it.
 * @param onToggle Invoked when the heading is chosen, to expand or collapse the group.
 * @param modifier Modifier applied to the heading.
 */
@Composable
fun ChromeNavGroupToggle(
    label: String,
    count: Int,
    expanded: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val hovered by interactionSource.collectIsHoveredAsState()
    val shape = RoundedCornerShape(ArecaTheme.radius.md)

    val background by animateColorAsState(
        targetValue = if (hovered) {
            ArecaTheme.colors.bgNeutralMuted
        } else {
            ArecaTheme.colors.bgCanvas.copy(alpha = 0f)
        },
        label = "chromeNavGroupToggleBackground",
    )
    val chevronRotation by animateFloatAsState(
        targetValue = if (expanded) 90f else 0f,
        label = "chromeNavGroupToggleChevron",
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = ArecaTheme.spacing.xs)
            .clip(shape)
            .background(background, shape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                role = Role.Button,
                onClick = onToggle,
            )
            .semantics { stateDescription = if (expanded) "Expanded" else "Collapsed" }
            .clickCursor()
            .padding(
                horizontal = ArecaTheme.spacing.lg,
                vertical = ArecaTheme.spacing.md,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
    ) {
        Box(modifier = Modifier.rotate(chevronRotation)) {
            ChromeText(text = "›", role = ChromeTextRole.Label, tone = ChromeTone.Muted, maxLines = 1)
        }

        Box(modifier = Modifier.weight(1f)) {
            ChromeText(text = label, role = ChromeTextRole.Label, tone = ChromeTone.Primary, maxLines = 1)
        }

        ChromeText(
            text = count.toString(),
            role = ChromeTextRole.Detail,
            tone = ChromeTone.Muted,
            maxLines = 1,
        )
    }
}

/**
 * A section heading inside an expanded sidebar group.
 *
 * @param label Section name.
 * @param modifier Modifier applied to the heading.
 */
@Composable
fun ChromeNavSectionHeader(
    label: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = ArecaTheme.spacing.lg,
                top = ArecaTheme.spacing.md,
                bottom = ArecaTheme.spacing.xs,
            ),
    ) {
        ChromeText(
            text = label.uppercase(),
            role = ChromeTextRole.Detail,
            tone = ChromeTone.Muted,
            maxLines = 1,
        )
    }
}
