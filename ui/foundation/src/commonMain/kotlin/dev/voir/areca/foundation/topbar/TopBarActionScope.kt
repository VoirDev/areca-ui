package dev.voir.areca.foundation.topbar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.LocalContentColor
import dev.voir.areca.foundation.LocalTextStyle
import dev.voir.areca.foundation.icon.LocalIconSize

/**
 * Receiver of a [TopBar]'s leading and trailing slots, and the reason the controls in them agree.
 *
 * The bar composes each slot inside a row that has already settled every measurement a control
 * could get wrong — the space between two controls, their vertical alignment, their content color,
 * their glyph size and their distance from the screen gutter. A caller supplies only *which*
 * controls to show.
 *
 * Every ready-made control — the navigation controls ([BackAction], [CloseAction], [MenuAction]),
 * the common verbs ([SearchAction], [ShareAction], [EditAction], [DeleteAction] and the rest), the
 * worded pair ([CancelAction], [DoneAction]) and the building blocks ([IconAction], [TextAction],
 * [AvatarAction], [Action]) — is an extension on this scope rather than a free composable. That is
 * deliberate: it makes them unusable outside a bar slot, so there is no way to reach one whose
 * metrics and appearance have not come from the bar it is in. Anything else placed in a slot is
 * positioned like a control is, so wrap custom content in [Action] rather than a hand-rolled box.
 *
 * It is also a [RowScope], so `Modifier.weight` and `Modifier.align` work on anything in the slot.
 *
 * @property rowScope Row the slot's controls are placed in, which the [RowScope] members delegate
 *   to.
 * @property size Metrics of the bar this slot belongs to.
 * @property style Colors of the bar this slot belongs to.
 */
@Stable
public class TopBarActionScope internal constructor(
    private val rowScope: RowScope,
    public val size: TopBarSize,
    public val style: TopBarStyle,
) : RowScope by rowScope {
    /** Whether the bar draws its controls in containers; see [TopBarActionStyle.isContained]. */
    public val isContained: Boolean
        get() = style.actions.isContained
}

/**
 * Composes one of a bar's control rows.
 *
 * @param style Colors the controls take their content color from.
 * @param size Metrics the controls take their measurements from.
 * @param toStart Whether this is the leading row, which is pulled toward the bar's start edge
 *   rather than its end edge.
 * @param content The controls.
 */
@Composable
internal fun TopBarActionRow(
    style: TopBarStyle,
    size: TopBarSize,
    toStart: Boolean,
    content: @Composable TopBarActionScope.() -> Unit,
) {
    val contained = style.actions.isContained

    CompositionLocalProvider(
        LocalContentColor provides style.actions.content,
        LocalIconSize provides if (contained) size.containedIcon else size.icon,
        LocalTextStyle provides size.actionTextStyle,
    ) {
        Row(
            // A contained control meets the gutter with its edge, so only bare glyphs are pulled.
            modifier = Modifier.topBarEdgeInset(
                inset = if (contained) 0.dp else size.actionEdgeInset,
                toStart = toStart,
            ),
            horizontalArrangement = Arrangement.spacedBy(
                if (contained) size.containedGap else size.actionGap,
            ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TopBarActionScope(rowScope = this, size = size, style = style).content()
        }
    }
}

/**
 * Pulls a control row outward by [inset], and gives back the width it stops occupying.
 *
 * Applied to bare glyphs only; a contained bar passes `0.dp`, since its discs already meet the
 * gutter with their edge.
 *
 * A control is a glyph in the middle of a hit target, so lining its *box* up with the screen gutter
 * leaves the glyph a hit target's padding inside it — which is why a 40dp back button never looked
 * aligned with the 16dp body text below it. Every control is padded by exactly [inset], and this
 * modifier moves the row out by the same amount, so what meets the gutter is the glyph.
 *
 * The reported width shrinks along with the offset rather than only the drawing moving. That keeps
 * the bar's own arithmetic honest: the gap to the title stays the gap that was asked for, and
 * [dev.voir.areca.foundation.slot.SlotRowArrangement.Centered] still computes its symmetric band
 * from the width the row visually occupies.
 *
 * @param inset Distance to pull the row outward by.
 * @param toStart Whether the row is pulled toward the start edge rather than the end edge.
 * @return Modifier that offsets the row and reports the narrower width.
 */
private fun Modifier.topBarEdgeInset(inset: Dp, toStart: Boolean): Modifier =
    layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)
        val insetPx = inset.roundToPx()

        layout(
            width = (placeable.width - insetPx).coerceAtLeast(0),
            height = placeable.height,
        ) {
            // placeRelative, so the pull follows the reading direction: the leading row moves
            // toward the right edge in a right-to-left layout, not away from it.
            placeable.placeRelative(x = if (toStart) -insetPx else 0, y = 0)
        }
    }
