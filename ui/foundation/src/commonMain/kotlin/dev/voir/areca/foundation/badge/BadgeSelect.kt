package dev.voir.areca.foundation.badge

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.selection.toggleable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.theme.ArecaTheme

/**
 * One choice offered by [BadgeSelect], [LazyBadgeSelect], or [FlowBadgeSelect].
 *
 * @param T Type of the value the choice stands for.
 * @property label What the chip says.
 * @property value The value the choice stands for, matched against the group's `selected` list.
 * @property enabled Whether the choice can be pressed.
 * @property leading Content before the label, normally an icon.
 * @property trailing Content after the label, normally an icon.
 */
public data class BadgeOption<T>(
    public val label: @Composable () -> Unit,
    public val value: T,
    public val enabled: Boolean = true,
    public val leading: (@Composable () -> Unit)? = null,
    public val trailing: (@Composable () -> Unit)? = null,
)

/** How many choices of a badge group can be on at once, which is what assistive technology hears. */
public enum class BadgeSelectionMode {
    /**
     * Exactly one, or none yet: each chip is a radio button, and the group is one selectable group.
     * Pressing a chip reports it; the caller replaces the selection with it.
     */
    Single,

    /**
     * Any number: each chip is a checkbox. Pressing a chip reports it; the caller adds it to the
     * selection or removes it.
     */
    Multiple,
}

/**
 * A scrolling row of chips, one per choice in [options], with the chosen ones drawn filled.
 *
 * For a row of choices too long to fit, such as filters over a list. Each chip is a selection
 * control rather than a button: with [BadgeSelectionMode.Single] the row is a selectable group of
 * radio buttons, with [BadgeSelectionMode.Multiple] each chip is a checkbox. The group does not hold
 * the selection — [onSelect] reports the chip pressed, and the caller updates [selected].
 *
 * A chip is drawn filled when it is chosen and soft when it is not, so pair [BadgeStyle.Chip] with
 * [BadgeSize.Chip] for the usual filter row. Every chip reserves [BadgeSize.touchTarget].
 *
 * @param T Type of the values the choices stand for.
 * @param options Every choice, in the order shown.
 * @param onSelect Called with the choice pressed.
 * @param selected Values of the chosen choices.
 * @param modifier Modifier applied to the row, which fills the available width.
 * @param selectionMode Whether one or many choices can be on at once.
 * @param style Colors of a chip. A chosen chip is drawn filled, the rest soft. Defaults to the
 *   theme's.
 * @param size Padding, type, and touch target of a chip. Defaults to the theme's, or
 *   [BadgeSize.Default].
 * @param shape Shape of each chip.
 * @param containerPadding Padding around the chips, inside the scrolling viewport.
 * @param gap Space between neighbouring chips.
 * @param key Stable identity of a choice's value, so a chip keeps its state when [options] reorder.
 *   On Android it must be saveable in a `Bundle` (a string, a number, an enum). `null` identifies a
 *   chip by its position.
 */
@Composable
public fun <T> LazyBadgeSelect(
    options: List<BadgeOption<T>>,
    onSelect: (option: BadgeOption<T>) -> Unit,
    selected: List<T>,
    modifier: Modifier = Modifier,
    selectionMode: BadgeSelectionMode = BadgeSelectionMode.Single,
    style: BadgeStyle = LocalBadgeStyle.current ?: BadgeStyle.Default,
    size: BadgeSize = LocalBadgeSize.current ?: BadgeSize.Default,
    shape: Shape = ArecaTheme.shapes.full,
    containerPadding: PaddingValues = PaddingValues(0.dp),
    gap: Dp = ArecaTheme.spacing.md,
    key: ((value: T) -> Any)? = null,
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .groupSemantics(selectionMode),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(gap),
        contentPadding = containerPadding,
    ) {
        items(
            items = options,
            key = key?.let { valueKey -> { option: BadgeOption<T> -> valueKey(option.value) } },
        ) { option ->
            BadgeChoice(
                option = option,
                isSelected = option.value in selected,
                selectionMode = selectionMode,
                onSelect = onSelect,
                style = style,
                size = size,
                shape = shape,
            )
        }
    }
}

/**
 * A row of chips, one per choice in [options], with the chosen ones drawn filled.
 *
 * For a handful of choices that always fit on one line. Each chip is a selection control rather
 * than a button: with [BadgeSelectionMode.Single] the row is a selectable group of radio buttons,
 * with [BadgeSelectionMode.Multiple] each chip is a checkbox. The group does not hold the selection
 * — [onSelect] reports the chip pressed, and the caller updates [selected].
 *
 * A chip is drawn filled when it is chosen and soft when it is not. Every chip reserves
 * [BadgeSize.touchTarget].
 *
 * @param T Type of the values the choices stand for.
 * @param options Every choice, in the order shown.
 * @param onSelect Called with the choice pressed.
 * @param selected Values of the chosen choices.
 * @param modifier Modifier applied to the row, which fills the available width.
 * @param selectionMode Whether one or many choices can be on at once.
 * @param style Colors of a chip. A chosen chip is drawn filled, the rest soft. Defaults to the
 *   theme's.
 * @param size Padding, type, and touch target of a chip. Defaults to the theme's, or
 *   [BadgeSize.Default].
 * @param shape Shape of each chip.
 * @param gap Space between neighbouring chips.
 */
@Composable
public fun <T> BadgeSelect(
    options: List<BadgeOption<T>>,
    onSelect: (option: BadgeOption<T>) -> Unit,
    selected: List<T>,
    modifier: Modifier = Modifier,
    selectionMode: BadgeSelectionMode = BadgeSelectionMode.Single,
    style: BadgeStyle = LocalBadgeStyle.current ?: BadgeStyle.Default,
    size: BadgeSize = LocalBadgeSize.current ?: BadgeSize.Default,
    shape: Shape = ArecaTheme.shapes.full,
    gap: Dp = ArecaTheme.spacing.md,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .groupSemantics(selectionMode),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(gap),
    ) {
        options.forEach { option ->
            BadgeChoice(
                option = option,
                isSelected = option.value in selected,
                selectionMode = selectionMode,
                onSelect = onSelect,
                style = style,
                size = size,
                shape = shape,
            )
        }
    }
}

/**
 * Chips, one per choice in [options], wrapping onto as many lines as they need.
 *
 * For a set of choices that should all be on screen at once, such as tags. Each chip is a selection
 * control rather than a button: with [BadgeSelectionMode.Single] the group is a selectable group of
 * radio buttons, with [BadgeSelectionMode.Multiple] each chip is a checkbox. The group does not hold
 * the selection — [onSelect] reports the chip pressed, and the caller updates [selected].
 *
 * A chip is drawn filled when it is chosen and soft when it is not. Every chip reserves
 * [BadgeSize.touchTarget].
 *
 * @param T Type of the values the choices stand for.
 * @param options Every choice, in the order shown.
 * @param onSelect Called with the choice pressed.
 * @param selected Values of the chosen choices.
 * @param modifier Modifier applied to the group, which fills the available width.
 * @param selectionMode Whether one or many choices can be on at once.
 * @param horizontalArrangement How the chips on one line are spaced.
 * @param verticalArrangement How the lines are spaced.
 * @param itemVerticalAlignment How chips of different heights line up within a line.
 * @param maxItemsInEachRow Most chips on one line.
 * @param maxLines Most lines; chips past the last are not shown.
 * @param style Colors of a chip. A chosen chip is drawn filled, the rest soft. Defaults to the
 *   theme's.
 * @param size Padding, type, and touch target of a chip. Defaults to the theme's, or
 *   [BadgeSize.Default].
 * @param shape Shape of each chip.
 */
@Composable
public fun <T> FlowBadgeSelect(
    options: List<BadgeOption<T>>,
    onSelect: (option: BadgeOption<T>) -> Unit,
    selected: List<T>,
    modifier: Modifier = Modifier,
    selectionMode: BadgeSelectionMode = BadgeSelectionMode.Single,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    itemVerticalAlignment: Alignment.Vertical = Alignment.Top,
    maxItemsInEachRow: Int = Int.MAX_VALUE,
    maxLines: Int = Int.MAX_VALUE,
    style: BadgeStyle = LocalBadgeStyle.current ?: BadgeStyle.Default,
    size: BadgeSize = LocalBadgeSize.current ?: BadgeSize.Default,
    shape: Shape = ArecaTheme.shapes.full,
) {
    FlowRow(
        modifier = modifier
            .fillMaxWidth()
            .groupSemantics(selectionMode),
        horizontalArrangement = horizontalArrangement,
        verticalArrangement = verticalArrangement,
        itemVerticalAlignment = itemVerticalAlignment,
        maxItemsInEachRow = maxItemsInEachRow,
        maxLines = maxLines,
    ) {
        options.forEach { option ->
            BadgeChoice(
                option = option,
                isSelected = option.value in selected,
                selectionMode = selectionMode,
                onSelect = onSelect,
                style = style,
                size = size,
                shape = shape,
            )
        }
    }
}

/**
 * Marks a badge group as one selectable group when only one of its choices can be on.
 *
 * A group of checkboxes is not a selectable group, so [BadgeSelectionMode.Multiple] adds nothing.
 *
 * @param selectionMode Whether one or many choices can be on at once.
 * @return The receiver, with the group semantics appended where they apply.
 */
private fun Modifier.groupSemantics(selectionMode: BadgeSelectionMode): Modifier = when (selectionMode) {
    BadgeSelectionMode.Single -> selectableGroup()
    BadgeSelectionMode.Multiple -> this
}

/**
 * One chip of a badge group: a [Badge] capsule that is a radio button or a checkbox.
 *
 * @param T Type of the value the choice stands for.
 * @param option The choice the chip stands for.
 * @param isSelected Whether the choice is on, which draws the chip filled rather than soft.
 * @param selectionMode Whether the chip is a radio button or a checkbox.
 * @param onSelect Called with [option] when the chip is pressed.
 * @param style Colors of the chip.
 * @param size Padding, type, and touch target of the chip.
 * @param shape Shape of the chip.
 */
@Composable
private fun <T> BadgeChoice(
    option: BadgeOption<T>,
    isSelected: Boolean,
    selectionMode: BadgeSelectionMode,
    onSelect: (option: BadgeOption<T>) -> Unit,
    style: BadgeStyle,
    size: BadgeSize,
    shape: Shape,
) {
    BadgeFrame(
        modifier = Modifier,
        style = style,
        size = size,
        shape = shape,
        soft = !isSelected,
        enabled = option.enabled,
        interaction = { indication ->
            when (selectionMode) {
                BadgeSelectionMode.Single -> selectable(
                    selected = isSelected,
                    enabled = option.enabled,
                    interactionSource = null,
                    indication = indication,
                    role = Role.RadioButton,
                    onClick = { onSelect(option) },
                )

                BadgeSelectionMode.Multiple -> toggleable(
                    value = isSelected,
                    enabled = option.enabled,
                    interactionSource = null,
                    indication = indication,
                    role = Role.Checkbox,
                    onValueChange = { onSelect(option) },
                )
            }
        },
        leading = option.leading,
        trailing = option.trailing,
        content = option.label,
    )
}
