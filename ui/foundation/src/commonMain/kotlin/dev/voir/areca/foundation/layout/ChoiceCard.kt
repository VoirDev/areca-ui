package dev.voir.areca.foundation.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.semantics.Role
import dev.voir.areca.foundation.selection.Checkbox
import dev.voir.areca.foundation.selection.Radio
import dev.voir.areca.indication.TouchIndication
import dev.voir.areca.modifier.clickCursor
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.SurfaceOf
import androidx.compose.runtime.CompositionLocalProvider
import dev.voir.areca.foundation.badge.BadgeSize
import dev.voir.areca.foundation.badge.LocalBadgeSize

/** Whether a set of [ChoiceCard]s takes one answer or several. */
public enum class ChoiceCardMode {
    /** One of the set: each card is a radio button, marked with a radio. */
    Single,

    /** Any of the set: each card is a checkbox, marked with a checkbox. */
    Multiple,
}

/**
 * A card that is one answer of a choice — a plan, a delivery speed, an option with a price — with
 * the selection ring, the mark and the semantics of a radio button or a checkbox.
 *
 * The card takes the raised fill of the surface it is drawn on, its corner from `shapes.surface`;
 * the chosen one wears an accent ring, the others a hairline. The mark sits at the start, [content]
 * in the middle, [trailing] — a price — at the end, and a [badge] — "Best value" — straddles the top
 * edge. The whole card is the target, so put a set of them in a column with `selectableGroup()` for
 * [ChoiceCardMode.Single].
 *
 * @param selected Whether this answer is chosen.
 * @param onClick Called when the card is pressed.
 * @param modifier Modifier applied to the card.
 * @param mode Whether the set takes one answer or several.
 * @param enabled Whether the card accepts input.
 * @param showMark Whether the radio or checkbox is drawn; the ring and the semantics stay.
 * @param badge A short label on the top edge — a `Badge`, drawn at `BadgeSize.ExtraSmall` unless
 *   it is given a size — or `null` for none.
 * @param trailing Content at the end — a price over its period — or `null` for none.
 * @param content The answer: its name and a line about it.
 */
@Composable
public fun ChoiceCard(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    mode: ChoiceCardMode = ChoiceCardMode.Single,
    enabled: Boolean = true,
    showMark: Boolean = true,
    badge: (@Composable () -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val shape = ArecaTheme.shapes.surface
    val bg = ArecaTheme.surface.raised
    val colors = ArecaTheme.colors
    val ring = if (selected) colors.accentBase else colors.borderBase
    val ringWidth = if (selected) ArecaTheme.spacing.xs2 else HairlineWidth

    Box(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(bg, shape)
                .border(ringWidth, ring, shape)
                .selectable(
                    selected = selected,
                    enabled = enabled,
                    role = if (mode == ChoiceCardMode.Single) Role.RadioButton else Role.Checkbox,
                    interactionSource = null,
                    indication = TouchIndication(shape = shape),
                    onClick = onClick,
                )
                .clickCursor(enabled)
                .padding(horizontal = ArecaTheme.spacing.xl, vertical = ArecaTheme.spacing.lg),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg),
        ) {
            if (showMark) {
                // Display-only: the card reports the state once, as one control.
                when (mode) {
                    ChoiceCardMode.Single -> Radio(checked = selected, onClick = null, enabled = enabled)
                    ChoiceCardMode.Multiple -> Checkbox(checked = selected, onCheckedChange = null, enabled = enabled)
                }
            }
            SurfaceOf(bg) {
                Column(modifier = Modifier.weight(1f), content = content)
            }
            if (trailing != null) {
                SurfaceOf(bg) { trailing() }
            }
        }
        if (badge != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = ArecaTheme.spacing.xl)
                    // Centred on the top edge: half above the card, half on it.
                    .layout { measurable, constraints ->
                        val placeable = measurable.measure(constraints)
                        layout(placeable.width, placeable.height) {
                            placeable.place(0, -placeable.height / 2)
                        }
                    },
            ) {
                // A tag on the edge, not a headline: the extra-small badge unless the caller sizes it.
                CompositionLocalProvider(LocalBadgeSize provides BadgeSize.ExtraSmall) { badge() }
            }
        }
    }
}

/** Width of an unchosen card's outline. */
private val HairlineWidth = androidx.compose.ui.unit.Dp.Hairline
