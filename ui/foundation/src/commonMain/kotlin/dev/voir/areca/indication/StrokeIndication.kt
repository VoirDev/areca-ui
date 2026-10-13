package dev.voir.areca.indication

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.node.CompositionLocalConsumerModifierNode
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.currentValueOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import dev.voir.areca.theme.ArecaMotion
import dev.voir.areca.theme.LocalColors
import dev.voir.areca.theme.LocalMotion
import dev.voir.areca.theme.millis
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.time.Duration
import androidx.compose.animation.core.tween as composeTween

/**
 * Indication that draws a stroke around the element's outline, plus a focus ring.
 *
 * - **Hover** — the stroke draws itself clockwise around the perimeter, then fades out on exit. The
 *   next hover starts the draw again from the top edge.
 * - **Press** — the stroke thickens from [restStrokeWidth] to [pressStrokeWidth]. A press with no
 *   preceding hover — a touch — snaps the stroke to fully drawn, because watching it trace itself
 *   after a tap reads as lag.
 * - **Focus** — [focusOutline] is drawn outside the bounds, independent of the stroke.
 *
 * Best on outlined controls, where the stroke reads as the element's own border coming alive. On a
 * filled surface the stroke competes with the fill; use [FadeIndication] or [TouchIndication] there.
 *
 * Left at its defaults, [strokeColor] is the theme's primary foreground and every timing is a theme
 * motion step, both read when the interaction happens.
 *
 * Do **not** wrap the element in `Modifier.clip(shape)`: outer clipping crops both an
 * [StrokeAlignment.Outside] stroke and the focus ring. Use `Modifier.background(color, shape)`.
 */
@Immutable
public data class StrokeIndication(
    /** Outline to follow. Rounded, circular, cut-corner, and custom shapes all work. */
    public val shape: Shape = RoundedCornerShape(8.dp),
    /** Where the stroke sits relative to the element's edge. */
    public val alignment: StrokeAlignment = StrokeAlignment.Outside,
    /**
     * Stroke color.
     *
     * [Color.Unspecified] — the default — is the theme's primary foreground, so the stroke reads on
     * a light page and a dark one alike.
     */
    public val strokeColor: Color = Color.Unspecified,
    /** Stroke thickness while not pressed. */
    public val restStrokeWidth: Dp = 1.5.dp,
    /** Stroke thickness while pressed. */
    public val pressStrokeWidth: Dp = 2.5.dp,
    /** Focus ring drawn outside the element. */
    public val focusOutline: FocusOutline = FocusOutline(),
    /**
     * Time the stroke takes to trace the full perimeter on hover.
     *
     * `null` — the default — takes the theme's [ArecaMotion.deliberate] step: the trace is meant to
     * be watched.
     */
    public val drawInDuration: Duration? = null,
    /**
     * Time the stroke takes to fade out once neither hovered nor pressed.
     *
     * `null` — the default — takes the theme's [ArecaMotion.slow] step.
     */
    public val fadeOutDuration: Duration? = null,
    /**
     * Time the stroke takes to move between [restStrokeWidth] and [pressStrokeWidth].
     *
     * `null` — the default — takes the theme's [ArecaMotion.instant] step.
     */
    public val pressWidthDuration: Duration? = null,
) : IndicationNodeFactory {
    /**
     * Creates the node that draws this indication for one element.
     *
     * @param interactionSource Source of the element's hover, press, and focus events.
     * @return A fresh [StrokeIndicationNode] bound to [interactionSource].
     */
    override fun create(interactionSource: InteractionSource): DelegatableNode =
        StrokeIndicationNode(this, interactionSource)
}

/**
 * Draws one element's [StrokeIndication].
 *
 * @property style Colors, widths, and timings to draw with.
 * @property interactionSource Source the hover, press, and focus events are collected from.
 */
private class StrokeIndicationNode(
    private val style: StrokeIndication,
    private val interactionSource: InteractionSource,
) : Modifier.Node(), DrawModifierNode, CompositionLocalConsumerModifierNode {

    /** How much of the perimeter is drawn, 0 to 1. */
    private val strokeProgress = Animatable(0f)

    /** Stroke opacity. Separate from [strokeProgress] so hover-exit fades without un-drawing. */
    private val strokeAlpha = Animatable(0f)

    /** Current stroke thickness, in `Dp` units so it can animate as a plain float. */
    private val strokeWidth = Animatable(style.restStrokeWidth.value)

    /** Whether the pointer is over the element. */
    private var hovered = false

    /** Whether a press is held. */
    private var pressed = false

    /** In-flight trace, replaced when the stroke next appears. */
    private var progressJob: Job? = null

    /** In-flight visibility change, replaced by the next appearance or fade. */
    private var alphaJob: Job? = null

    /** In-flight thickness change, replaced by the next press or release. */
    private var widthJob: Job? = null

    /** Focus ring, which owns its own animation and path cache. */
    private val focus = FocusOutlineRenderer(style.focusOutline, style.shape)

    /** Measures [strokePath] so a leading portion of it can be extracted. */
    private val pathMeasure = PathMeasure()

    /** Full perimeter the stroke follows. */
    private val strokePath = Path()

    /** Leading portion of [strokePath] that is currently drawn. */
    private val partialPath = Path()

    /** Length of [strokePath], in pixels. */
    private var pathLength = 0f

    /** Size [strokePath] was last built for. */
    private var lastSize: Size = Size.Zero

    /** Layout direction [strokePath] was last built for; `null` until the first build. */
    private var lastLayoutDirection: LayoutDirection? = null

    /** Stroke offset [strokePath] was last built for, in pixels; `NaN` until the first build. */
    private var lastStrokeOffsetPx = Float.NaN

    /** Starts collecting the element's interactions; the collection ends when the node detaches. */
    override fun onAttach() {
        coroutineScope.launch {
            interactionSource.interactions.collect { handle(it) }
        }
    }

    /**
     * Turns one interaction into animation targets.
     *
     * Visibility is driven by hover *or* press, while thickness is driven by press alone, so the two
     * can change in the same event without interfering.
     *
     * @param interaction Interaction collected from the element.
     */
    private fun handle(interaction: Interaction) {
        val motion = currentValueOf(LocalMotion)
        if (focus.handle(interaction, coroutineScope, motion)) return

        val wasVisible = hovered || pressed
        when (interaction) {
            is PressInteraction.Press -> pressed = true
            is PressInteraction.Release,
            is PressInteraction.Cancel -> pressed = false

            is HoverInteraction.Enter -> hovered = true
            is HoverInteraction.Exit -> hovered = false
            else -> return
        }
        val nowVisible = hovered || pressed

        when {
            nowVisible && !wasVisible -> {
                progressJob?.cancel()
                alphaJob?.cancel()
                if (pressed && !hovered) {
                    // A touch press has no hover to trace during; snap to fully drawn.
                    progressJob = coroutineScope.launch { strokeProgress.snapTo(1f) }
                } else {
                    progressJob = coroutineScope.launch {
                        strokeProgress.snapTo(0f)
                        strokeProgress.animateTo(
                            targetValue = 1f,
                            animationSpec = composeTween(
                                (style.drawInDuration ?: motion.deliberate).millis,
                                easing = motion.pressIn,
                            ),
                        )
                    }
                }
                alphaJob = coroutineScope.launch { strokeAlpha.snapTo(1f) }
            }

            !nowVisible && wasVisible -> {
                alphaJob?.cancel()
                alphaJob = coroutineScope.launch {
                    strokeAlpha.animateTo(
                        targetValue = 0f,
                        animationSpec = composeTween(
                            (style.fadeOutDuration ?: motion.slow).millis,
                            easing = motion.fade,
                        ),
                    )
                }
            }
        }

        val targetWidth =
            if (pressed) style.pressStrokeWidth.value else style.restStrokeWidth.value
        val widthDuration = style.pressWidthDuration ?: motion.instant
        widthJob?.cancel()
        widthJob = coroutineScope.launch {
            strokeWidth.animateTo(
                targetValue = targetWidth,
                animationSpec = composeTween(widthDuration.millis, easing = motion.fade),
            )
        }
    }

    /** Draws the content, then the traced stroke and the focus ring over it. */
    override fun ContentDrawScope.draw() {
        drawContent()

        val strokeWidthPx = strokeWidth.value.dp.toPx()
        val strokeOffsetPx = when (style.alignment) {
            StrokeAlignment.Inside -> -strokeWidthPx / 2f
            StrokeAlignment.Outside -> strokeWidthPx / 2f
            StrokeAlignment.Center -> 0f
        }

        val geometryChanged = size != lastSize || layoutDirection != lastLayoutDirection

        // The offset shifts every frame of the press-width animation, so the path is rebuilt for
        // the length of that animation on each press. That is a handful of rebuilds, not a
        // per-frame cost at rest.
        if (geometryChanged || strokeOffsetPx != lastStrokeOffsetPx) {
            buildShapePath(style.shape, size, layoutDirection, this, strokeOffsetPx, strokePath)
            pathMeasure.setPath(strokePath, false)
            pathLength = pathMeasure.length
            lastStrokeOffsetPx = strokeOffsetPx
        }

        val colors = currentValueOf(LocalColors)

        // Extracting a leading segment is the Compose equivalent of animating stroke-dashoffset.
        if (strokeAlpha.value > VisibilityThreshold && strokeProgress.value > 0f && pathLength > 0f) {
            partialPath.reset()
            pathMeasure.getSegment(
                startDistance = 0f,
                stopDistance = pathLength * strokeProgress.value.coerceIn(0f, 1f),
                destination = partialPath,
                startWithMoveTo = true,
            )
            drawPath(
                path = partialPath,
                color = if (style.strokeColor == Color.Unspecified) colors.fgBase else style.strokeColor,
                alpha = strokeAlpha.value.coerceIn(0f, 1f),
                style = Stroke(
                    width = strokeWidthPx,
                    // Round ends and joins keep the growing tip from reading as a chopped edge.
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round,
                ),
            )
        }

        focus.draw(this, geometryChanged, colors.accentBase)

        if (geometryChanged) {
            lastSize = size
            lastLayoutDirection = layoutDirection
        }
    }
}
