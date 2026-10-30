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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.lerp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.icon.IconSize
import dev.voir.areca.indication.TouchIndication
import dev.voir.areca.modifier.minimumTouchTarget
import dev.voir.areca.modifier.thenIf
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.tween

/**
 * Dimensions for one step of [Checkbox].
 *
 * The mark's stroke grows faster than the box does, so a large checkbox reads as a bolder mark
 * rather than as the same mark with more space around it.
 *
 * @property container Width and height of the box as drawn.
 * @property borderWidth Width of the box's outline.
 * @property markStrokeWidth Stroke of the check and of the indeterminate dash.
 * @property shape Shape of the box.
 * @property touchTarget Smallest width and height an interactive checkbox occupies in layout. The
 *   box is centred in the slot, and the band around it still answers a touch. Every named step
 *   reserves [dev.voir.areca.theme.ArecaSizing.touchTarget]: 48dp, the minimum a finger reliably
 *   hits, under touch, and nothing under a pointer. [Dp.Unspecified] — the constructor's default —
 *   reserves nothing beyond the box. A display-only checkbox never reserves the band.
 */
@Immutable
public data class CheckboxSize(
    public val container: Dp,
    public val borderWidth: Dp,
    public val markStrokeWidth: Dp,
    public val shape: Shape,
    public val touchTarget: Dp = Dp.Unspecified,
) {
    /** The steps of the checkbox ramp, smallest first, and the theme default. */
    public companion object {
        /** 16dp. Inline with body text, or in a dense list. */
        public val Small: CheckboxSize
            @Composable
            get() {
                val sizing = ArecaTheme.sizing
                return CheckboxSize(
                    container = IconSize.Small,
                    borderWidth = 1.5.dp,
                    markStrokeWidth = 2.dp,
                    shape = ArecaTheme.shapes.sm,
                    touchTarget = sizing.touchTarget,
                )
            }

        /** 20dp. The default. */
        public val Medium: CheckboxSize
            @Composable
            get() {
                val sizing = ArecaTheme.sizing
                return CheckboxSize(
                    container = IconSize.Medium,
                    borderWidth = 1.5.dp,
                    markStrokeWidth = 2.25.dp,
                    shape = ArecaTheme.shapes.sm,
                    touchTarget = sizing.touchTarget,
                )
            }

        /** Theme default: [Medium]. */
        public val Default: CheckboxSize
            @Composable
            get() = Medium

        /** 24dp. A standalone checkbox, such as a single consent control. */
        public val Large: CheckboxSize
            @Composable
            get() {
                val sizing = ArecaTheme.sizing
                return CheckboxSize(
                    container = IconSize.Large,
                    borderWidth = 2.dp,
                    markStrokeWidth = 2.75.dp,
                    shape = ArecaTheme.shapes.sm,
                    touchTarget = sizing.touchTarget,
                )
            }
    }
}

/**
 * Colors for a [Checkbox] across its six combinations of state and editability.
 *
 * Grouped as [SelectionColors] triples rather than a flat list of fifteen colors, so a state that
 * has no colors defined is a missing field rather than three fields nobody noticed.
 *
 * @property unchecked Unchecked and editable.
 * @property checked Checked or indeterminate, and editable.
 * @property disabledUnchecked Unchecked and unavailable.
 * @property disabledChecked Checked or indeterminate, and unavailable.
 * @property readOnlyUnchecked Unchecked, shown but not editable.
 * @property readOnlyChecked Checked or indeterminate, shown but not editable.
 */
@Immutable
public data class CheckboxStyle(
    override val unchecked: SelectionColors,
    override val checked: SelectionColors,
    override val disabledUnchecked: SelectionColors,
    override val disabledChecked: SelectionColors,
    override val readOnlyUnchecked: SelectionColors,
    override val readOnlyChecked: SelectionColors,
) : SelectionStyle {
    /** The theme default and the factory for variations on it. */
    public companion object {
        /** Theme default: an outlined empty box that fills with the accent when checked. */
        public val Default: CheckboxStyle
            @Composable
            get() = default()

        /**
         * Builds a style from the current theme.
         *
         * Parameters are flat colors rather than [SelectionColors] triples, so overriding one color
         * stays a one-line change.
         *
         * @param uncheckedBg Fill while unchecked.
         * @param uncheckedBorder Outline while unchecked.
         * @param checkedBg Fill while checked or indeterminate.
         * @param checkedBorder Outline while checked or indeterminate.
         * @param mark Color of the check and of the indeterminate dash.
         * @param disabledBg Fill while unavailable and unchecked.
         * @param disabledBorder Outline while unavailable and unchecked.
         * @param disabledCheckedBg Fill while unavailable and checked.
         * @param disabledCheckedBorder Outline while unavailable and checked.
         * @param disabledMark Mark color while unavailable.
         * @param readOnlyBg Fill while uneditable and unchecked.
         * @param readOnlyBorder Outline while uneditable and unchecked.
         * @param readOnlyCheckedBg Fill while uneditable and checked.
         * @param readOnlyCheckedBorder Outline while uneditable and checked.
         * @param readOnlyMark Mark color while uneditable.
         * @return Style covering all six combinations of state and editability.
         */
        @Composable
        public fun default(
            uncheckedBg: Color = Color.Transparent,
            uncheckedBorder: Color = ArecaTheme.colors.borderBase,
            checkedBg: Color = ArecaTheme.colors.accentBase,
            checkedBorder: Color = ArecaTheme.colors.accentBase,
            mark: Color = ArecaTheme.colors.fgOnFill,
            disabledBg: Color = ArecaTheme.colors.bgDisabled,
            disabledBorder: Color = ArecaTheme.colors.borderDisabled,
            disabledCheckedBg: Color = ArecaTheme.colors.bgDisabled,
            disabledCheckedBorder: Color = ArecaTheme.colors.borderDisabled,
            disabledMark: Color = ArecaTheme.colors.fgDisabled,
            readOnlyBg: Color = ArecaTheme.colors.bgMuted,
            readOnlyBorder: Color = ArecaTheme.colors.borderBase,
            readOnlyCheckedBg: Color = ArecaTheme.colors.accentMuted,
            readOnlyCheckedBorder: Color = ArecaTheme.colors.accentSubtle,
            readOnlyMark: Color = ArecaTheme.colors.accentBase,
        ): CheckboxStyle = CheckboxStyle(
            unchecked = SelectionColors(uncheckedBg, uncheckedBorder, mark),
            checked = SelectionColors(checkedBg, checkedBorder, mark),
            disabledUnchecked = SelectionColors(disabledBg, disabledBorder, disabledMark),
            disabledChecked = SelectionColors(
                disabledCheckedBg,
                disabledCheckedBorder,
                disabledMark,
            ),
            readOnlyUnchecked = SelectionColors(readOnlyBg, readOnlyBorder, readOnlyMark),
            readOnlyChecked = SelectionColors(
                readOnlyCheckedBg,
                readOnlyCheckedBorder,
                readOnlyMark,
            ),
        )
    }
}

/** Default [CheckboxSize] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalCheckboxSize: ProvidableCompositionLocal<CheckboxSize?> =
    compositionLocalOf { null }

/** Default [CheckboxStyle] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalCheckboxStyle: ProvidableCompositionLocal<CheckboxStyle?> =
    compositionLocalOf { null }

/**
 * A box the user ticks, with an optional indeterminate dash.
 *
 * The mark is drawn rather than rendered from an icon, so it can animate on: the check traces itself
 * from its corner and the dash grows from its centre, which reads as the control responding rather
 * than as a glyph appearing.
 *
 * [indeterminate] is a modifier on [checked], not a third value — exactly as in HTML. A checkbox is
 * only ever unchecked or checked, and an indeterminate one is a *checked* box showing a dash because
 * its children disagree. `Checkbox(checked = false, indeterminate = true)` therefore draws an empty
 * box.
 *
 * [enabled] and [readOnly] are different refusals. Disabled means the action is unavailable and the
 * control greys out; read-only means the value is real and current but not the user's to change, so
 * it keeps its color. Assistive technology reads a disabled checkbox as unavailable and a read-only
 * one as a checkbox offering no action; the state words — checked, not checked, partially checked —
 * are the platform's own, in the user's language.
 *
 * A `null` [onCheckedChange] makes the checkbox display-only: it draws its state and nothing else —
 * no press, no focus, no semantics, no reserved touch band — for an enclosing element, such as a row
 * that is itself the toggle, to report the state once.
 *
 * This is the bare control. For a checkbox with a label and a full-width tap target, use
 * [dev.voir.areca.foundation.list.CheckboxListItem].
 *
 * @param checked Whether the box is ticked.
 * @param onCheckedChange Called with the value the user is asking for, or `null` for a display-only
 *   checkbox.
 * @param modifier Modifier applied to the box.
 * @param style Colors. Defaults to the theme's.
 * @param size Dimensions. Defaults to the theme's, or [CheckboxSize.Default].
 * @param indeterminate Whether to draw a dash instead of a check, and report the mixed state. Only
 *   applies while [checked].
 * @param enabled Whether the control is available.
 * @param readOnly Whether the control shows its value but rejects changes.
 * @param interactionSource Source of the checkbox's interactions; pass one to observe them.
 */
@Composable
public fun Checkbox(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    style: CheckboxStyle = LocalCheckboxStyle.current ?: CheckboxStyle.Default,
    size: CheckboxSize = LocalCheckboxSize.current ?: CheckboxSize.Default,
    indeterminate: Boolean = false,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    interactionSource: MutableInteractionSource? = null,
) {
    val showIndeterminate = checked && indeterminate
    val colors = style.resolve(selected = checked, enabled = enabled, readOnly = readOnly)
    val source = interactionSource ?: remember { MutableInteractionSource() }
    val change = onCheckedChange.takeUnless { LocalSelectionControlInert.current }
    val motion = ArecaTheme.motion

    val backgroundColor by animateColorAsState(
        targetValue = colors.bg,
        animationSpec = motion.fast.tween(motion.standard),
        label = "checkboxBackground",
    )
    val borderColor by animateColorAsState(
        targetValue = colors.border,
        animationSpec = motion.fast.tween(motion.standard),
        label = "checkboxBorder",
    )
    val markColor by animateColorAsState(
        targetValue = colors.mark,
        animationSpec = motion.fast.tween(motion.standard),
        label = "checkboxMark",
    )
    val checkProgress by animateFloatAsState(
        targetValue = if (checked && !showIndeterminate) 1f else 0f,
        animationSpec = motion.fast.tween(motion.standard),
        label = "checkboxCheckProgress",
    )
    val indeterminateProgress by animateFloatAsState(
        targetValue = if (showIndeterminate) 1f else 0f,
        animationSpec = motion.fast.tween(motion.standard),
        label = "checkboxIndeterminateProgress",
    )

    Box(
        modifier = modifier
            .thenIf(change != null) { minimumTouchTarget(size.touchTarget) }
            .size(size.container)
            .background(backgroundColor, size.shape)
            .border(size.borderWidth, borderColor, size.shape)
            .toggleControl(
                state = checkboxToggleableState(checked = checked, indeterminate = indeterminate),
                role = Role.Checkbox,
                onClick = change?.let { onChange -> { onChange(!checked) } },
                enabled = enabled,
                readOnly = readOnly,
                interactionSource = source,
                indication = TouchIndication(shape = size.shape),
            ),
    ) {
        CheckboxMark(
            checkProgress = checkProgress,
            indeterminateProgress = indeterminateProgress,
            color = markColor,
            strokeWidth = size.markStrokeWidth,
            modifier = Modifier.size(size.container),
        )
    }
}

/**
 * Draws the check and the indeterminate dash, each at its own progress.
 *
 * The check is two strokes, not one polyline, so it can be traced: the short leg draws over the
 * first 45% of the progress and the long leg over the rest. Splitting it unevenly is what makes the
 * mark look written rather than scaled.
 *
 * Both marks are drawn as fractions of the canvas, so they scale with the box without a second set
 * of tokens.
 *
 * @param checkProgress How much of the check is drawn, 0 to 1.
 * @param indeterminateProgress How much of the dash is drawn, 0 to 1.
 * @param color Stroke color for both.
 * @param strokeWidth Stroke width for both.
 * @param modifier Modifier applied to the canvas.
 */
@Composable
private fun CheckboxMark(
    checkProgress: Float,
    indeterminateProgress: Float,
    color: Color,
    strokeWidth: Dp,
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier) {
        val strokePx = strokeWidth.toPx()
        val width = size.width
        val height = size.height

        if (checkProgress > 0f) {
            val start = Offset(width * 0.28f, height * 0.52f)
            val corner = Offset(width * 0.44f, height * 0.68f)
            val end = Offset(width * 0.74f, height * 0.34f)
            val shortLeg = (checkProgress / CheckShortLegShare).coerceIn(0f, 1f)
            val longLeg = ((checkProgress - CheckShortLegShare) / (1f - CheckShortLegShare))
                .coerceIn(0f, 1f)

            drawLine(
                color = color,
                start = start,
                end = lerp(start, corner, shortLeg),
                strokeWidth = strokePx,
                cap = StrokeCap.Round,
            )
            drawLine(
                color = color,
                start = corner,
                end = lerp(corner, end, longLeg),
                strokeWidth = strokePx,
                cap = StrokeCap.Round,
            )
        }

        if (indeterminateProgress > 0f) {
            val centerY = height * 0.5f
            val centerX = width * 0.5f
            val halfWidth = width * 0.24f * indeterminateProgress

            drawLine(
                color = color,
                start = Offset(centerX - halfWidth, centerY),
                end = Offset(centerX + halfWidth, centerY),
                strokeWidth = strokePx,
                cap = StrokeCap.Round,
            )
        }
    }
}

/** Share of the check's draw-in spent on its short leg. */
private const val CheckShortLegShare = 0.45f

/**
 * Accessibility state of a checkbox, or of a row that is one.
 *
 * @param checked Whether the checkbox is ticked.
 * @param indeterminate Whether a ticked checkbox shows the indeterminate dash.
 * @return [ToggleableState.Indeterminate] for a ticked box showing the dash, otherwise on or off.
 */
internal fun checkboxToggleableState(checked: Boolean, indeterminate: Boolean): ToggleableState =
    when {
        checked && indeterminate -> ToggleableState.Indeterminate
        checked -> ToggleableState.On
        else -> ToggleableState.Off
    }
