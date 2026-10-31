package dev.voir.areca.foundation.selection

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.setProgress
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.button.ButtonSize
import dev.voir.areca.foundation.button.ButtonStyle
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.theme.ArecaTheme
import kotlin.math.roundToInt

/**
 * Metrics of a [NumberStepper].
 *
 * @property minHeight Smallest height of the stepper. The buttons reserve the sizing's touch target
 *   (48dp under touch, none under a pointer), so under touch a smaller value only takes effect with
 *   a [button] size that reserves less.
 * @property minWidth Smallest width of the stepper; it grows to fit a long value.
 * @property button Size of the decrease and increase buttons.
 * @property borderWidth Width of the outline.
 * @property contentPadding Space between the outline and the buttons.
 * @property gap Space between each button and the value.
 * @property textStyle Style of the value.
 */
@Immutable
public data class NumberStepperSize(
    public val minHeight: Dp,
    public val minWidth: Dp,
    public val button: ButtonSize,
    public val borderWidth: Dp,
    public val contentPadding: PaddingValues,
    public val gap: Dp,
    public val textStyle: TextStyle,
) {
    /** The theme default and the factory for variations on it. */
    public companion object {
        /**
         * Theme default: compact buttons around a medium-weight value, 48dp tall under touch, 36dp
         * under a pointer.
         */
        public val Default: NumberStepperSize
            @Composable
            get() = default()

        /**
         * Builds a size from the current theme.
         *
         * @param minHeight Smallest height of the stepper. The sizing's default control height by
         *   default, so the stepper lines up with a medium field or button.
         * @param minWidth Smallest width of the stepper.
         * @param button Size of the decrease and increase buttons. The default is the extra-small
         *   button without its minimum width, so a one-character symbol makes a square button.
         * @param borderWidth Width of the outline.
         * @param contentPadding Space between the outline and the buttons.
         * @param gap Space between each button and the value.
         * @param textStyle Style of the value.
         * @return Size for a stepper.
         */
        @Composable
        public fun default(
            minHeight: Dp = ArecaTheme.sizing.controlMd,
            minWidth: Dp = ArecaTheme.sizing.select(touch = 120.dp, pointer = 96.dp),
            button: ButtonSize = ButtonSize.ExtraSmall.copy(minWidth = Dp.Unspecified),
            borderWidth: Dp = 1.dp,
            contentPadding: PaddingValues = PaddingValues(
                horizontal = ArecaTheme.sizing.select(
                    touch = ArecaTheme.spacing.sm,
                    pointer = ArecaTheme.spacing.xs,
                ),
            ),
            gap: Dp = ArecaTheme.sizing.select(
                touch = ArecaTheme.spacing.sm,
                pointer = ArecaTheme.spacing.xs,
            ),
            textStyle: TextStyle = ArecaTheme.typography.labelLarge,
        ): NumberStepperSize = NumberStepperSize(
            minHeight = minHeight,
            minWidth = minWidth,
            button = button,
            borderWidth = borderWidth,
            contentPadding = contentPadding,
            gap = gap,
            textStyle = textStyle,
        )
    }
}

/**
 * Colors of a [NumberStepper].
 *
 * @property bg Fill of the stepper.
 * @property content Color of the value.
 * @property border Outline of the stepper.
 * @property button Style of the decrease and increase buttons.
 * @property bgDisabled Fill while the stepper is unavailable.
 * @property contentDisabled Value color while the stepper is unavailable.
 * @property borderDisabled Outline while the stepper is unavailable.
 */
@Immutable
public data class NumberStepperStyle(
    public val bg: Color,
    public val content: Color,
    public val border: Color,
    public val button: ButtonStyle,
    public val bgDisabled: Color,
    public val contentDisabled: Color,
    public val borderDisabled: Color,
) {
    /** The theme default and the factory for variations on it. */
    public companion object {
        /** Theme default: an outlined canvas field with neutral solid buttons. */
        public val Default: NumberStepperStyle
            @Composable
            get() = default()

        /**
         * Builds a style from the current theme.
         *
         * @param bg Fill of the stepper.
         * @param content Color of the value.
         * @param border Outline of the stepper.
         * @param button Style of the decrease and increase buttons.
         * @param bgDisabled Fill while the stepper is unavailable.
         * @param contentDisabled Value color while the stepper is unavailable.
         * @param borderDisabled Outline while the stepper is unavailable.
         * @return Style for a stepper.
         */
        @Composable
        public fun default(
            bg: Color = ArecaTheme.colors.bgCanvas,
            content: Color = ArecaTheme.colors.fgBase,
            border: Color = ArecaTheme.colors.borderBase,
            button: ButtonStyle = ButtonStyle.solid(ActionColor.Neutral),
            bgDisabled: Color = ArecaTheme.colors.bgDisabled,
            contentDisabled: Color = ArecaTheme.colors.fgDisabled,
            borderDisabled: Color = ArecaTheme.colors.borderDisabled,
        ): NumberStepperStyle = NumberStepperStyle(
            bg = bg,
            content = content,
            border = border,
            button = button,
            bgDisabled = bgDisabled,
            contentDisabled = contentDisabled,
            borderDisabled = borderDisabled,
        )
    }
}

/**
 * Words a [NumberStepper] gives assistive technology for its buttons.
 *
 * The buttons draw symbols, which a screen reader would otherwise read as "minus" and "plus" or not
 * at all. The library ships no localization, so the words are parameters: pass your own
 * translations, or [English] for an English-only product.
 *
 * @property decrease Label of the button that lowers the value.
 * @property increase Label of the button that raises the value.
 */
@Immutable
public data class NumberStepperLabels(
    public val decrease: String,
    public val increase: String,
) {
    /** Ready-made label sets. */
    public companion object {
        /** English wording. */
        public val English: NumberStepperLabels = NumberStepperLabels(
            decrease = "Decrease",
            increase = "Increase",
        )
    }
}

/** Default [NumberStepperStyle] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalNumberStepperStyle: ProvidableCompositionLocal<NumberStepperStyle?> =
    compositionLocalOf { null }

/** Default [NumberStepperSize] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalNumberStepperSize: ProvidableCompositionLocal<NumberStepperSize?> =
    compositionLocalOf { null }

/**
 * A whole number the user nudges down or up by one, between optional bounds.
 *
 * The value sits between a decrease and an increase button; each button disables itself at its
 * bound, so the user can see which way the value can still go.
 *
 * Assistive technology meets the stepper as one adjustable value: the row reports the value and its
 * range, so the platform's own adjust gestures — swipe up and down, the rotor, a slider's keys —
 * move it, and each change is announced. The two buttons stay reachable on their own, labelled by
 * [labels] rather than by their symbols.
 *
 * @param value Current value.
 * @param onValueChange Called with the value the user asks for, always within [minValue] and
 *   [maxValue].
 * @param modifier Modifier applied to the stepper.
 * @param style Colors. Defaults to the theme's.
 * @param size Metrics. Defaults to the theme's.
 * @param shape Shape of the outline and fill.
 * @param minValue Lowest value the stepper offers.
 * @param maxValue Highest value the stepper offers.
 * @param labels Accessibility labels of the buttons.
 * @param enabled Whether the stepper accepts input.
 * @param decreaseContent Content of the decrease button. A minus sign by default.
 * @param increaseContent Content of the increase button. A plus sign by default.
 * @param valueContent Renders the value between the buttons. The value text by default.
 */
@Composable
public fun NumberStepper(
    value: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    style: NumberStepperStyle = LocalNumberStepperStyle.current ?: NumberStepperStyle.Default,
    size: NumberStepperSize = LocalNumberStepperSize.current ?: NumberStepperSize.Default,
    shape: Shape = ArecaTheme.shapes.md,
    minValue: Int = Int.MIN_VALUE,
    maxValue: Int = Int.MAX_VALUE,
    labels: NumberStepperLabels = NumberStepperLabels.English,
    enabled: Boolean = true,
    decreaseContent: @Composable () -> Unit = { NumberStepperSymbol(symbol = "−") },
    increaseContent: @Composable () -> Unit = { NumberStepperSymbol(symbol = "+") },
    valueContent: @Composable BoxScope.(Int) -> Unit = {
        Text(
            text = value.toString(),
            style = size.textStyle,
            color = if (enabled) style.content else style.contentDisabled,
            maxLines = 1,
        )
    },
) {
    val canDecrease = enabled && value > minValue
    val canIncrease = enabled && value < maxValue

    Row(
        modifier = modifier
            .defaultMinSize(minWidth = size.minWidth, minHeight = size.minHeight)
            .background(if (enabled) style.bg else style.bgDisabled, shape)
            .border(
                width = size.borderWidth,
                color = if (enabled) style.border else style.borderDisabled,
                shape = shape,
            )
            .semantics {
                stateDescription = value.toString()
                progressBarRangeInfo = numberStepperRangeInfo(value, minValue, maxValue)
                if (enabled) {
                    setProgress { target ->
                        val next = numberStepperProgressValue(target, minValue, maxValue)
                        if (next != value) onValueChange(next)
                        true
                    }
                } else {
                    disabled()
                }
            }
            .padding(size.contentPadding),
        horizontalArrangement = Arrangement.spacedBy(size.gap),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Button(
            onClick = { onValueChange(steppedValue(value, -1, minValue, maxValue)) },
            modifier = Modifier.semantics { contentDescription = labels.decrease },
            style = style.button,
            size = size.button,
            enabled = canDecrease,
            content = decreaseContent,
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .semantics { liveRegion = LiveRegionMode.Polite },
            contentAlignment = Alignment.Center,
        ) {
            valueContent(value)
        }

        Button(
            onClick = { onValueChange(steppedValue(value, 1, minValue, maxValue)) },
            modifier = Modifier.semantics { contentDescription = labels.increase },
            style = style.button,
            size = size.button,
            enabled = canIncrease,
            content = increaseContent,
        )
    }
}

/**
 * Default content of a stepper button: a symbol hidden from assistive technology, which hears the
 * button's label instead.
 *
 * @param symbol Character to draw.
 */
@Composable
private fun NumberStepperSymbol(symbol: String) {
    Text(
        text = symbol,
        maxLines = 1,
        modifier = Modifier.clearAndSetSemantics {},
    )
}

/**
 * The value one press of a stepper button asks for.
 *
 * Computed in `Long`, so a step at the edge of `Int` cannot wrap round to the other end.
 *
 * @param value Current value.
 * @param delta Step to take, negative to decrease.
 * @param minValue Lowest value allowed.
 * @param maxValue Highest value allowed.
 * @return [value] moved by [delta] and held within [minValue] and [maxValue].
 */
internal fun steppedValue(value: Int, delta: Int, minValue: Int, maxValue: Int): Int =
    (value.toLong() + delta).coerceIn(minValue.toLong(), maxValue.toLong()).toInt()

/**
 * The range a stepper reports to assistive technology.
 *
 * Steps are reported only while they are few enough to be meaningful: a range as wide as `Int` has
 * more steps than the platform can describe, and reports none.
 *
 * @param value Current value.
 * @param minValue Lowest value allowed.
 * @param maxValue Highest value allowed.
 * @return The value within its range, with one step per whole number where that is practical.
 */
internal fun numberStepperRangeInfo(
    value: Int,
    minValue: Int,
    maxValue: Int,
): ProgressBarRangeInfo {
    val span = maxValue.toLong() - minValue.toLong()
    val steps = if (span in 2..MaxReportedSteps) (span - 1).toInt() else 0
    return ProgressBarRangeInfo(
        current = value.toFloat(),
        range = minValue.toFloat()..maxValue.toFloat(),
        steps = steps,
    )
}

/**
 * The value an assistive-technology adjustment asks for.
 *
 * @param target Value the platform asked to set, possibly fractional or out of range.
 * @param minValue Lowest value allowed.
 * @param maxValue Highest value allowed.
 * @return [target] rounded to a whole number and held within [minValue] and [maxValue].
 */
internal fun numberStepperProgressValue(target: Float, minValue: Int, maxValue: Int): Int =
    target.roundToInt().coerceIn(minValue, maxValue)

/** Widest range, in whole numbers, whose individual steps are reported to assistive technology. */
private const val MaxReportedSteps = 1_000L
