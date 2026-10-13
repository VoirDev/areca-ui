package dev.voir.areca.foundation.selection

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.icon.IconSize
import dev.voir.areca.indication.TouchIndication
import dev.voir.areca.modifier.MinimumTouchTarget
import dev.voir.areca.modifier.minimumTouchTarget
import dev.voir.areca.modifier.thenIf
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.tween

/**
 * Dimensions for one step of [Radio].
 *
 * The dot is a little under half the ring, which is what leaves a visible gap between the two. A
 * larger dot reads as a filled circle and loses the ring entirely.
 *
 * @property container Width and height of the ring as drawn.
 * @property borderWidth Width of the ring.
 * @property dotSize Diameter of the selected dot.
 * @property shape Shape of the control. Circular in every preset — a square radio is a checkbox.
 * @property touchTarget Smallest width and height an interactive radio occupies in layout. The ring
 *   is centred in the slot, and the band around it still answers a touch. Every named step reserves
 *   48dp, the minimum a finger reliably hits. [Dp.Unspecified] — the constructor's default —
 *   reserves nothing beyond the ring. A display-only radio never reserves the band.
 */
@Immutable
public data class RadioSize(
    public val container: Dp,
    public val borderWidth: Dp,
    public val dotSize: Dp,
    public val shape: Shape,
    public val touchTarget: Dp = Dp.Unspecified,
) {
    /** The steps of the radio ramp, smallest first, and the theme default. */
    public companion object {
        /** 16dp. Inline with body text, or in a dense list. */
        public val Small: RadioSize
            @Composable
            get() = RadioSize(
                container = IconSize.Small,
                borderWidth = 1.5.dp,
                dotSize = 6.dp,
                shape = ArecaTheme.shapes.full,
                touchTarget = MinimumTouchTarget,
            )

        /** 20dp. The default. */
        public val Medium: RadioSize
            @Composable
            get() = RadioSize(
                container = IconSize.Medium,
                borderWidth = 1.5.dp,
                dotSize = 8.dp,
                shape = ArecaTheme.shapes.full,
                touchTarget = MinimumTouchTarget,
            )

        /** Theme default: [Medium]. */
        public val Default: RadioSize
            @Composable
            get() = Medium

        /** 24dp. A standalone choice, or a list the user reads at arm's length. */
        public val Large: RadioSize
            @Composable
            get() = RadioSize(
                container = IconSize.Large,
                borderWidth = 2.dp,
                dotSize = 10.dp,
                shape = ArecaTheme.shapes.full,
                touchTarget = MinimumTouchTarget,
            )
    }
}

/**
 * Colors for a [Radio] across its six combinations of state and editability.
 *
 * Unlike [CheckboxStyle], the selected state keeps a transparent fill: a radio says "this one" with
 * its dot, and filling the ring as well would make a selected radio heavier than a selected
 * checkbox in the same list.
 *
 * @property unchecked Unselected and editable.
 * @property checked Selected and editable.
 * @property disabledUnchecked Unselected and unavailable.
 * @property disabledChecked Selected and unavailable.
 * @property readOnlyUnchecked Unselected, shown but not editable.
 * @property readOnlyChecked Selected, shown but not editable.
 */
@Immutable
public data class RadioStyle(
    override val unchecked: SelectionColors,
    override val checked: SelectionColors,
    override val disabledUnchecked: SelectionColors,
    override val disabledChecked: SelectionColors,
    override val readOnlyUnchecked: SelectionColors,
    override val readOnlyChecked: SelectionColors,
) : SelectionStyle {
    /** The theme default and the factory for variations on it. */
    public companion object {
        /** Theme default: an outlined ring with an accent dot. */
        public val Default: RadioStyle
            @Composable
            get() = default()

        /**
         * Builds a style from the current theme.
         *
         * Parameters are flat colors rather than [SelectionColors] triples, so overriding one color
         * stays a one-line change.
         *
         * @param uncheckedBg Fill while unselected.
         * @param uncheckedBorder Ring while unselected.
         * @param checkedBg Fill while selected.
         * @param checkedBorder Ring while selected.
         * @param dot Color of the selected dot.
         * @param disabledBg Fill while unavailable and unselected.
         * @param disabledBorder Ring while unavailable and unselected.
         * @param disabledCheckedBg Fill while unavailable and selected.
         * @param disabledCheckedBorder Ring while unavailable and selected.
         * @param disabledDot Dot color while unavailable.
         * @param readOnlyBg Fill while uneditable and unselected.
         * @param readOnlyBorder Ring while uneditable and unselected.
         * @param readOnlyCheckedBg Fill while uneditable and selected.
         * @param readOnlyCheckedBorder Ring while uneditable and selected.
         * @param readOnlyDot Dot color while uneditable.
         * @return Style covering all six combinations of state and editability.
         */
        @Composable
        public fun default(
            uncheckedBg: Color = Color.Transparent,
            uncheckedBorder: Color = ArecaTheme.colors.borderBase,
            checkedBg: Color = Color.Transparent,
            checkedBorder: Color = ArecaTheme.colors.accentBase,
            dot: Color = ArecaTheme.colors.accentBase,
            disabledBg: Color = ArecaTheme.colors.bgDisabled,
            disabledBorder: Color = ArecaTheme.colors.borderDisabled,
            disabledCheckedBg: Color = ArecaTheme.colors.bgDisabled,
            disabledCheckedBorder: Color = ArecaTheme.colors.borderDisabled,
            disabledDot: Color = ArecaTheme.colors.fgDisabled,
            readOnlyBg: Color = ArecaTheme.colors.bgMuted,
            readOnlyBorder: Color = ArecaTheme.colors.borderBase,
            readOnlyCheckedBg: Color = ArecaTheme.colors.bgMuted,
            readOnlyCheckedBorder: Color = ArecaTheme.colors.accentSubtle,
            readOnlyDot: Color = ArecaTheme.colors.accentBase,
        ): RadioStyle = RadioStyle(
            unchecked = SelectionColors(uncheckedBg, uncheckedBorder, dot),
            checked = SelectionColors(checkedBg, checkedBorder, dot),
            disabledUnchecked = SelectionColors(disabledBg, disabledBorder, disabledDot),
            disabledChecked = SelectionColors(
                disabledCheckedBg,
                disabledCheckedBorder,
                disabledDot,
            ),
            readOnlyUnchecked = SelectionColors(readOnlyBg, readOnlyBorder, readOnlyDot),
            readOnlyChecked = SelectionColors(
                readOnlyCheckedBg,
                readOnlyCheckedBorder,
                readOnlyDot,
            ),
        )
    }
}

/** Default [RadioSize] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalRadioSize: ProvidableCompositionLocal<RadioSize?> =
    compositionLocalOf { null }

/** Default [RadioStyle] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalRadioStyle: ProvidableCompositionLocal<RadioStyle?> =
    compositionLocalOf { null }

/**
 * A ring the user fills to pick one option out of several.
 *
 * Pressing a selected radio does nothing — a radio can be turned on but not off, because in a group
 * exactly one option is always chosen. Deselecting is the group's job, by selecting another.
 *
 * [enabled] and [readOnly] are different refusals. Disabled means the option is unavailable and the
 * control greys out; read-only means the choice is real and current but not the user's to change, so
 * it keeps its color. Assistive technology reads a disabled radio as unavailable and a read-only one
 * as a radio button offering no action; "selected" and "not selected" are the platform's own words.
 *
 * A `null` [onClick] makes the radio display-only: it draws its state and nothing else — no press,
 * no focus, no semantics, no reserved touch band — for an enclosing element, such as a row that is
 * itself the option, to report the state once.
 *
 * This is the bare control, and it does not own group behaviour: the caller decides which radio is
 * selected, and puts `Modifier.selectableGroup()` on the container of the group. For a radio with a
 * label and a full-width tap target, use [dev.voir.areca.foundation.list.RadioListItem].
 *
 * @param checked Whether this option is the selected one.
 * @param onClick Called when the user picks this option; never called while it is already selected.
 *   `null` makes the radio display-only.
 * @param modifier Modifier applied to the ring.
 * @param style Colors. Defaults to the theme's.
 * @param size Dimensions. Defaults to the theme's, or [RadioSize.Default].
 * @param enabled Whether the control is available.
 * @param readOnly Whether the control shows its value but rejects changes.
 * @param interactionSource Source of the radio's interactions; pass one to observe them.
 */
@Composable
public fun Radio(
    checked: Boolean,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    style: RadioStyle = LocalRadioStyle.current ?: RadioStyle.Default,
    size: RadioSize = LocalRadioSize.current ?: RadioSize.Default,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    interactionSource: MutableInteractionSource? = null,
) {
    val colors = style.resolve(selected = checked, enabled = enabled, readOnly = readOnly)
    val source = interactionSource ?: remember { MutableInteractionSource() }
    val click = onClick.takeUnless { LocalSelectionControlInert.current }
    val motion = ArecaTheme.motion

    val backgroundColor by animateColorAsState(
        targetValue = colors.bg,
        animationSpec = motion.fast.tween(motion.standard),
        label = "radioBackground",
    )
    val borderColor by animateColorAsState(
        targetValue = colors.border,
        animationSpec = motion.fast.tween(motion.standard),
        label = "radioBorder",
    )
    val dotColor by animateColorAsState(
        targetValue = colors.mark,
        animationSpec = motion.fast.tween(motion.standard),
        label = "radioDot",
    )
    val dotProgress by animateFloatAsState(
        targetValue = if (checked) 1f else 0f,
        animationSpec = motion.fast.tween(motion.standard),
        label = "radioDotProgress",
    )

    Box(
        modifier = modifier
            .thenIf(click != null) { minimumTouchTarget(size.touchTarget) }
            .size(size.container)
            .background(backgroundColor, size.shape)
            .border(size.borderWidth, borderColor, size.shape)
            .selectControl(
                selected = checked,
                role = Role.RadioButton,
                onClick = click?.let { pick -> { if (!checked) pick() } },
                enabled = enabled,
                readOnly = readOnly,
                interactionSource = source,
                indication = TouchIndication(shape = size.shape),
            ),
    ) {
        RadioDot(
            progress = dotProgress,
            color = dotColor,
            dotSize = size.dotSize,
            modifier = Modifier.size(size.container),
        )
    }
}

/**
 * Draws the selected dot, growing from the centre.
 *
 * Scaling the radius rather than fading the color is what makes selection read as something landing
 * in the ring instead of appearing there.
 *
 * @param progress How far the dot has grown, 0 to 1.
 * @param color Fill of the dot.
 * @param dotSize Diameter at full size.
 * @param modifier Modifier applied to the canvas.
 */
@Composable
private fun RadioDot(
    progress: Float,
    color: Color,
    dotSize: Dp,
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier) {
        if (progress > 0f) {
            drawCircle(
                color = color,
                radius = dotSize.toPx() * 0.5f * progress,
                center = center,
            )
        }
    }
}
