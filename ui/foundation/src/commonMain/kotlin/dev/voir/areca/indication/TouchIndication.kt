package dev.voir.areca.indication

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.node.CompositionLocalConsumerModifierNode
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.currentValueOf
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import dev.voir.areca.theme.ArecaMotion
import dev.voir.areca.theme.LocalColors
import dev.voir.areca.theme.LocalMotion
import dev.voir.areca.theme.millis
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.sqrt
import kotlin.time.Duration
import androidx.compose.animation.core.tween as composeTween

/**
 * Touch-first indication: a hover tint, a radial that expands from the finger, and a focus ring.
 *
 * - **Hover** — [color] fills the whole element at [hoverAlpha], following [shape].
 * - **Press** — a circle expands from the exact press position, clipped to [shape], and fades out on
 *   release. It layers over the hover tint rather than replacing it.
 * - **Focus** — [focusOutline] is drawn outside the element's bounds. Nothing inside the element
 *   changes.
 *
 * The three channels animate independently, so pressing while hovered while focused stacks all three
 * instead of one cancelling another.
 *
 * Left at its defaults, [color] is the theme's primary foreground and every timing is a theme motion
 * step, both read when the interaction happens — so the press darkens a light surface, lightens a
 * dark one, and follows a theme that retunes its motion.
 *
 * This is the default indication for anything the user taps. Reach for [FadeIndication] on a surface
 * whose background already carries the state, [StrokeIndication] on an outlined control, and
 * [LiquidSweepIndication] on a deliberate, commit-style action.
 *
 * Do **not** wrap the element in `Modifier.clip(shape)`. The radial is clipped internally, and outer
 * clipping would also crop the focus ring, which is drawn outside the bounds. Use
 * `Modifier.background(color, shape)` for a shaped fill that does not clip its children.
 */
@Immutable
public data class TouchIndication(
    /**
     * Shape of the element.
     *
     * The hover tint fills it, the press radial is clipped to it, and the focus ring is outset from
     * it. Must match the shape passed to `Modifier.background(color, shape)` and `Modifier.border`.
     */
    public val shape: Shape = RoundedCornerShape(8.dp),
    /**
     * Color of both the hover tint and the press radial.
     *
     * [Color.Unspecified] — the default — is the theme's primary foreground, which is the neutral
     * answer on the theme's own surfaces: dark on a light page, light on a dark one. Over a
     * saturated fill — a wallet's own colour, a filled button — pass white instead, or use
     * [onColor]: a dark radial on a saturated field reads as a smudge rather than as a press.
     */
    public val color: Color = Color.Unspecified,
    /** Opacity of the hover tint, which covers the whole shape. */
    public val hoverAlpha: Float = 0.03f,
    /**
     * Peak opacity of the press radial.
     *
     * Deliberately light. A press is an acknowledgement, not an event: much heavier, a radial
     * crossing a white card reads as a shadow sweeping over it, and on a list of cards the feedback
     * draws more attention than the content. Raise it per call site for a control that genuinely
     * needs a louder answer.
     */
    public val pressMaxAlpha: Float = 0.07f,
    /** Focus ring drawn outside the element. */
    public val focusOutline: FocusOutline = FocusOutline(),
    /**
     * Time the press radial takes to reach full size.
     *
     * `null` — the default — takes the theme's [ArecaMotion.deliberate] step. The radial keeps
     * growing under a released press while it fades, so most of that time is the settle, not lag.
     */
    public val pressInDuration: Duration? = null,
    /**
     * Time the press radial takes to fade out after release.
     *
     * `null` — the default — takes the theme's [ArecaMotion.medium] step.
     */
    public val pressFadeDuration: Duration? = null,
    /**
     * Hover tint fade-in and fade-out duration.
     *
     * `null` — the default — takes the theme's [ArecaMotion.medium] step.
     */
    public val hoverDuration: Duration? = null,
) : IndicationNodeFactory {
    /**
     * Creates the node that draws this indication for one element.
     *
     * @param interactionSource Source of the element's hover, press, and focus events.
     * @return A fresh [TouchIndicationNode] bound to [interactionSource].
     */
    override fun create(interactionSource: InteractionSource): DelegatableNode =
        TouchIndicationNode(this, interactionSource)

    /** Presets. */
    public companion object {
        /**
         * The same press, drawn in white for a surface that is already carrying colour.
         *
         * @param shape Shape of the element.
         * @return Indication suited to a saturated or inverted fill.
         */
        public fun onColor(shape: Shape = RoundedCornerShape(8.dp)): TouchIndication =
            TouchIndication(shape = shape, color = Color.White)
    }
}

/**
 * Draws one element's [TouchIndication].
 *
 * @property style Colors, opacities, and timings to draw with.
 * @property interactionSource Source the hover, press, and focus events are collected from.
 */
private class TouchIndicationNode(
    private val style: TouchIndication,
    private val interactionSource: InteractionSource,
) : Modifier.Node(), DrawModifierNode, CompositionLocalConsumerModifierNode {

    /** Hover tint opacity. */
    private val hoverAlpha = Animatable(0f)

    /** Press radial visibility. Snaps to full on press, fades out on release. */
    private val pressAlpha = Animatable(0f)

    /** Press radial expansion, 0 at the press point and 1 at the furthest corner. */
    private val pressProgress = Animatable(0f)

    /**
     * Where the radial expands from.
     *
     * Held in a snapshot state so that reading it in [draw] re-runs the draw pass when it changes.
     */
    private var pressPosition by mutableStateOf(Offset.Zero)

    /** In-flight hover fade, replaced by the next hover change. */
    private var hoverJob: Job? = null

    /** In-flight press visibility change, replaced by the next press or release. */
    private var pressAlphaJob: Job? = null

    /** In-flight radial expansion, restarted by the next press. */
    private var pressProgressJob: Job? = null

    /** Focus ring, which owns its own animation and path cache. */
    private val focus = FocusOutlineRenderer(style.focusOutline, style.shape)

    /** Path along the shape's edge, used to fill the hover tint and clip the press radial. */
    private val shapePath = Path()

    /** Size [shapePath] was last built for. */
    private var lastSize: Size = Size.Zero

    /** Layout direction [shapePath] was last built for; `null` until the first build. */
    private var lastLayoutDirection: LayoutDirection? = null

    /** Starts collecting the element's interactions; the collection ends when the node detaches. */
    override fun onAttach() {
        coroutineScope.launch {
            interactionSource.interactions.collect { handle(it) }
        }
    }

    /**
     * Turns one interaction into animation targets.
     *
     * Hover, press, and focus are independent: overlapping interactions stack rather than cancel.
     *
     * @param interaction Interaction collected from the element.
     */
    private fun handle(interaction: Interaction) {
        val motion = currentValueOf(LocalMotion)
        if (focus.handle(interaction, coroutineScope, motion)) return

        when (interaction) {
            is PressInteraction.Press -> {
                pressPosition = interaction.pressPosition
                pressProgressJob?.cancel()
                pressAlphaJob?.cancel()
                pressProgressJob = coroutineScope.launch {
                    pressProgress.snapTo(0f)
                    pressProgress.animateTo(
                        targetValue = 1f,
                        animationSpec = composeTween(
                            (style.pressInDuration ?: motion.deliberate).millis,
                            easing = motion.pressIn,
                        ),
                    )
                }
                // Visibility snaps in: a fade-in would read as lag under the finger.
                pressAlphaJob = coroutineScope.launch { pressAlpha.snapTo(style.pressMaxAlpha) }
            }

            is PressInteraction.Release,
            is PressInteraction.Cancel -> {
                // Only the alpha fades. The radius keeps expanding underneath, which reads as the
                // press settling rather than stopping dead.
                pressAlphaJob?.cancel()
                pressAlphaJob = coroutineScope.launch {
                    pressAlpha.animateTo(
                        targetValue = 0f,
                        animationSpec = composeTween(
                            (style.pressFadeDuration ?: motion.medium).millis,
                            easing = motion.fade,
                        ),
                    )
                }
            }

            is HoverInteraction.Enter -> animateHover(style.hoverAlpha, motion)
            is HoverInteraction.Exit -> animateHover(0f, motion)
        }
    }

    /**
     * Fades the hover tint to [target], replacing any fade already running.
     *
     * @param target Hover tint opacity to settle at.
     * @param motion Theme motion in scope.
     */
    private fun animateHover(target: Float, motion: ArecaMotion) {
        val duration = style.hoverDuration ?: motion.medium
        hoverJob?.cancel()
        hoverJob = coroutineScope.launch {
            hoverAlpha.animateTo(
                targetValue = target,
                animationSpec = composeTween(duration.millis, easing = motion.fade),
            )
        }
    }

    /** Draws the hover tint and the press radial under the content, and the focus ring over it. */
    override fun ContentDrawScope.draw() {
        val geometryChanged = size != lastSize || layoutDirection != lastLayoutDirection
        if (geometryChanged) {
            buildShapePath(style.shape, size, layoutDirection, this, offsetPx = 0f, shapePath)
        }

        val colors = currentValueOf(LocalColors)
        val ink = if (style.color == Color.Unspecified) colors.fgBase else style.color

        if (hoverAlpha.value > VisibilityThreshold) {
            drawPath(
                path = shapePath,
                color = ink.copy(alpha = ink.alpha * hoverAlpha.value),
            )
        }

        if (pressAlpha.value > VisibilityThreshold && pressProgress.value > 0f) {
            clipPath(shapePath) {
                drawCircle(
                    color = ink,
                    radius = maxRadiusFromPoint(pressPosition, size) * pressProgress.value,
                    center = pressPosition,
                    alpha = pressAlpha.value.coerceIn(0f, 1f),
                )
            }
        }

        // Content draws over the overlays; the focus ring draws over the content.
        drawContent()
        focus.draw(this, geometryChanged, colors.accentBase)

        if (geometryChanged) {
            lastSize = size
            lastLayoutDirection = layoutDirection
        }
    }
}

/**
 * Distance from [point] to the furthest corner of a rectangle of [size].
 *
 * This is the radius at which a circle centred on the press covers the whole element.
 *
 * @param point Press position inside the element.
 * @param size Element size.
 * @return The radius, in the same units as [point] and [size].
 */
private fun maxRadiusFromPoint(point: Offset, size: Size): Float {
    val dx = max(point.x, size.width - point.x)
    val dy = max(point.y, size.height - point.y)
    return sqrt(dx * dx + dy * dy)
}
