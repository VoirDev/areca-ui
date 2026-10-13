package dev.voir.areca.crop

import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.setProgress
import androidx.compose.ui.semantics.stateDescription
import dev.voir.areca.indication.FocusOutline
import dev.voir.areca.theme.ArecaTheme
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.roundToInt

/**
 * A horizontal ruler that slides under a fixed centre mark, for straightening and zooming.
 *
 * Dragging moves the ticks with the finger, so dragging left raises the value; one tick spacing of
 * travel is one [tickStep]. Ticks fade towards the ends, and a dot marks the [origin] so the way back
 * to level is always in view.
 *
 * The dial is an adjustable control for assistive technology, and takes keyboard focus: the arrow keys
 * step by half a tick, with Shift by a major tick, and Home returns to the origin. While it has focus
 * it shows foundation's focus ring in [CropDialStyle.focusRing].
 *
 * @param value Current value.
 * @param onValueChange Called with each new value, already coerced into [valueRange].
 * @param valueRange Values the dial spans.
 * @param contentDescription What the dial adjusts, such as "Angle".
 * @param modifier Modifier applied to the dial. It fills the width it is given.
 * @param tickStep Value between neighbouring ticks.
 * @param majorTickEvery How many ticks apart the longer ticks are, counted from [origin].
 * @param origin Value the dot marks and Home returns to.
 * @param stateDescription The value as it should be read aloud, such as "3.2 degrees", or `null` to
 *   let the platform read the number.
 * @param style Colors. Defaults to [LocalCropDialStyle], or [CropDialStyle.Default].
 * @param size Metrics. Defaults to [LocalCropDialSize], or [CropDialSize.Default].
 * @param interactionSource Source the dial's focus is reported to, or `null` for one of its own.
 * @param onAdjustStart Called when an adjustment begins: when a drag begins, when a key goes down —
 *   its repeats while held belong to the same adjustment — and before the change assistive technology
 *   makes.
 * @param onAdjustEnd Called when an adjustment ends: when a drag ends or is cancelled, when the held
 *   key goes up or focus leaves the dial, and right after the change assistive technology makes.
 */
@Composable
public fun CropDial(
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    contentDescription: String,
    modifier: Modifier = Modifier,
    tickStep: Float = 1f,
    majorTickEvery: Int = 5,
    origin: Float = 0f,
    stateDescription: String? = null,
    style: CropDialStyle = LocalCropDialStyle.current ?: CropDialStyle.Default,
    size: CropDialSize = LocalCropDialSize.current ?: CropDialSize.Default,
    interactionSource: MutableInteractionSource? = null,
    onAdjustStart: () -> Unit = {},
    onAdjustEnd: () -> Unit = {},
) {
    require(tickStep > 0f) { "tickStep must be positive, was $tickStep." }
    require(majorTickEvery > 0) { "majorTickEvery must be positive, was $majorTickEvery." }

    val currentValue by rememberUpdatedState(value)
    val currentOnValueChange by rememberUpdatedState(onValueChange)
    val currentOnAdjustStart by rememberUpdatedState(onAdjustStart)
    val currentOnAdjustEnd by rememberUpdatedState(onAdjustEnd)
    val source = interactionSource ?: remember { MutableInteractionSource() }
    val keyHold = remember { CropKeyHold() }
    val metrics = size
    val emit = { next: Float -> currentOnValueChange(next.coerceIn(valueRange)) }
    // An assistive-technology change is a whole adjustment of its own, and so is a key from the
    // moment it goes down until it comes up, so both are bracketed the way a drag is: a crop records
    // each as one undo step and shows its guides meanwhile.
    val step = { next: Float ->
        currentOnAdjustStart()
        emit(next)
        currentOnAdjustEnd()
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(metrics.height)
            .indication(
                source,
                focusRingIndication(ArecaTheme.shapes.md, FocusOutline(color = style.focusRing)),
            )
            .semantics {
                this.contentDescription = contentDescription
                if (stateDescription != null) this.stateDescription = stateDescription
                progressBarRangeInfo = ProgressBarRangeInfo(value.coerceIn(valueRange), valueRange)
                setProgress { target ->
                    step(target)
                    true
                }
            }
            .onFocusChanged { if (!it.isFocused && keyHold.clear()) currentOnAdjustEnd() }
            .onKeyEvent { event ->
                val delta = if (event.isShiftPressed) tickStep * majorTickEvery else tickStep / 2f
                val target = when (event.key) {
                    Key.DirectionRight, Key.DirectionUp -> currentValue + delta
                    Key.DirectionLeft, Key.DirectionDown -> currentValue - delta
                    Key.MoveHome -> origin
                    else -> return@onKeyEvent false
                }
                when (event.type) {
                    KeyEventType.KeyDown -> {
                        val wasHolding = keyHold.isHolding
                        if (keyHold.press(event.key)) {
                            if (wasHolding) currentOnAdjustEnd()
                            currentOnAdjustStart()
                        }
                        emit(target)
                    }
                    KeyEventType.KeyUp -> if (keyHold.release(event.key)) currentOnAdjustEnd()
                    else -> return@onKeyEvent false
                }
                true
            }
            .focusable(interactionSource = source)
            .pointerInput(tickStep, valueRange, metrics.tickSpacing) {
                val spacing = metrics.tickSpacing.toPx()
                var dragged = 0f
                detectHorizontalDragGestures(
                    onDragStart = {
                        dragged = currentValue
                        currentOnAdjustStart()
                    },
                    onDragEnd = { currentOnAdjustEnd() },
                    onDragCancel = { currentOnAdjustEnd() },
                ) { change, amount ->
                    change.consume()
                    dragged = (dragged - amount / spacing * tickStep).coerceIn(valueRange)
                    emit(dragged)
                }
            }
            .drawBehind { drawDial(value, valueRange, tickStep, majorTickEvery, origin, style, metrics) },
    )
}

/**
 * Draws the ticks, the origin dot and the centre mark.
 *
 * @param value Value under the centre mark.
 * @param range Values the ruler spans; no tick is drawn beyond it.
 * @param step Value between ticks.
 * @param majorEvery Ticks between major ticks.
 * @param origin Value the dot marks.
 * @param style Colors.
 * @param metrics Lengths and spacing.
 */
private fun DrawScope.drawDial(
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    step: Float,
    majorEvery: Int,
    origin: Float,
    style: CropDialStyle,
    metrics: CropDialSize,
) {
    val middle = size.width / 2f
    val spacing = metrics.tickSpacing.toPx()
    val tickWidth = metrics.tickWidth.toPx()
    val tickCentre = size.height * TickCentreShare
    val count = ((range.endInclusive - range.start) / step).roundToInt()

    for (index in 0..count) {
        val tick = range.start + index * step
        val x = middle + (tick - value) / step * spacing
        if (x < -spacing || x > size.width + spacing) continue
        val fromOrigin = (tick - origin) / step
        val isMajor = abs(fromOrigin / majorEvery - (fromOrigin / majorEvery).roundToInt()) < MajorTolerance
        val length = if (isMajor) metrics.majorTick.toPx() else metrics.minorTick.toPx()
        drawRect(
            color = style.tick.copy(alpha = style.tick.alpha * fade(x, middle) * if (isMajor) 1f else MinorTickAlpha),
            topLeft = Offset(x - tickWidth / 2f, tickCentre - length / 2f),
            size = Size(tickWidth, length),
        )
    }

    val originX = middle + (origin - value) / step * spacing
    if (originX in 0f..size.width) {
        drawCircle(
            color = style.origin.copy(alpha = style.origin.alpha * fade(originX, middle)),
            radius = metrics.originRadius.toPx(),
            center = Offset(originX, metrics.originRadius.toPx() * 2f),
        )
    }

    val indicatorLength = metrics.indicatorLength.toPx()
    val indicatorWidth = metrics.indicatorWidth.toPx()
    drawRoundRect(
        color = style.indicator,
        topLeft = Offset(middle - indicatorWidth / 2f, tickCentre - indicatorLength / 2f),
        size = Size(indicatorWidth, indicatorLength),
        cornerRadius = CornerRadius(indicatorWidth / 2f),
    )
}

/**
 * How visible a mark is at [x]: fully at the centre, fading to nothing at either end.
 *
 * @param x Horizontal position of the mark.
 * @param middle Horizontal centre of the dial.
 * @return Opacity multiplier, `0..1`.
 */
private fun fade(x: Float, middle: Float): Float = (1f - (abs(x - middle) / middle).pow(FadeExponent)).coerceIn(0f, 1f)

/** Height share at which the ticks are centred, leaving room above for the origin dot. */
private const val TickCentreShare = 0.6f

/** Strength of a minor tick relative to a major one. */
private const val MinorTickAlpha = 0.5f

/** Shape of the fade towards the ends; higher keeps more of the ruler fully visible. */
private const val FadeExponent = 2.4f

/** Tolerance for deciding that a tick lands on a major position. */
private const val MajorTolerance = 1e-3f
