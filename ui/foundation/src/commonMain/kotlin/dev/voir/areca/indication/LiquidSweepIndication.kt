package dev.voir.areca.indication

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Easing
import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
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
 * Indication that sweeps horizontally across the element, plus a focus ring.
 *
 * - **Hover** — an underline draws along the bottom edge from the start, and retracts on exit.
 * - **Press** — a low-opacity fill sweeps the full element from start to end behind the content,
 *   then fades out on release. Every press restarts the sweep rather than continuing the last one.
 * - **Focus** — [focusOutline] is drawn outside the bounds.
 *
 * Both sweeps run in reading order: left to right in a left-to-right layout, right to left in a
 * right-to-left one. Left at its defaults, [color] is the theme's primary foreground and every
 * timing is a theme motion step, both read when the interaction happens.
 *
 * The directional motion makes a press feel like a deliberate, physical commit, which suits a
 * primary call to action. It is too theatrical for dense rows or repeated controls — use
 * [TouchIndication] there.
 *
 * The fill and underline are rectangles in the element's bounding box, clipped to [shape]. A rounded
 * rectangle reads well, because the underline picks up the bottom corners. A circle or a custom path
 * does not, because the underline degenerates into a sliver at the geometric bottom; choose another
 * indication for those.
 *
 * Do **not** wrap the element in `Modifier.clip(shape)`: the sweep is clipped internally, and outer
 * clipping would crop the focus ring. Use `Modifier.background(color, shape)`.
 */
@Immutable
public data class LiquidSweepIndication(
    /**
     * Shape of the element.
     *
     * The sweep and underline are clipped to it and the focus ring is outset from it. Must match the
     * shape passed to `Modifier.background(color, shape)`.
     */
    public val shape: Shape = RoundedCornerShape(8.dp),
    /**
     * Color of both the underline and the press fill.
     *
     * [Color.Unspecified] — the default — is the theme's primary foreground.
     */
    public val color: Color = Color.Unspecified,
    /** Thickness of the hover underline. */
    public val underlineThickness: Dp = 2.dp,
    /** Peak opacity of the press fill. */
    public val pressFillAlpha: Float = 0.10f,
    /** Focus ring drawn outside the element. */
    public val focusOutline: FocusOutline = FocusOutline(),
    /**
     * Time the underline takes to draw across on hover, and to retract on exit.
     *
     * `null` — the default — takes the theme's [ArecaMotion.slow] step.
     */
    public val hoverDuration: Duration? = null,
    /**
     * Time the press fill takes to sweep the full width.
     *
     * `null` — the default — takes the theme's [ArecaMotion.slow] step.
     */
    public val pressFillDuration: Duration? = null,
    /**
     * Time the press fill takes to fade out after release.
     *
     * `null` — the default — takes the theme's [ArecaMotion.medium] step.
     */
    public val pressFadeDuration: Duration? = null,
) : IndicationNodeFactory {
    /**
     * Creates the node that draws this indication for one element.
     *
     * @param interactionSource Source of the element's hover, press, and focus events.
     * @return A fresh [LiquidSweepIndicationNode] bound to [interactionSource].
     */
    override fun create(interactionSource: InteractionSource): DelegatableNode =
        LiquidSweepIndicationNode(this, interactionSource)
}

/**
 * Draws one element's [LiquidSweepIndication].
 *
 * @property style Colors, sizes, and timings to draw with.
 * @property interactionSource Source the hover, press, and focus events are collected from.
 */
private class LiquidSweepIndicationNode(
    private val style: LiquidSweepIndication,
    private val interactionSource: InteractionSource,
) : Modifier.Node(), DrawModifierNode, CompositionLocalConsumerModifierNode {

    /** How far the hover underline has drawn, as a fraction of the element's width. */
    private val underlineProgress = Animatable(0f)

    /** How far the press fill has swept, as a fraction of the element's width. */
    private val fillProgress = Animatable(0f)

    /** Press fill opacity. Snaps to full on press, fades out on release. */
    private val fillAlpha = Animatable(0f)

    /** In-flight underline draw or retract, replaced by the next hover change. */
    private var underlineJob: Job? = null

    /** In-flight press sweep, restarted by the next press. */
    private var fillProgressJob: Job? = null

    /** In-flight press fill visibility change, replaced by the next press or release. */
    private var fillAlphaJob: Job? = null

    /** Focus ring, which owns its own animation and path cache. */
    private val focus = FocusOutlineRenderer(style.focusOutline, style.shape)

    /** Path along the shape's edge, used to clip the fill and the underline. */
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
     * Turns one interaction into animation targets. Underline, fill, and focus run independently.
     *
     * @param interaction Interaction collected from the element.
     */
    private fun handle(interaction: Interaction) {
        val motion = currentValueOf(LocalMotion)
        if (focus.handle(interaction, coroutineScope, motion)) return

        when (interaction) {
            is PressInteraction.Press -> {
                fillProgressJob?.cancel()
                fillAlphaJob?.cancel()
                fillProgressJob = coroutineScope.launch {
                    fillProgress.snapTo(0f)
                    fillProgress.animateTo(
                        targetValue = 1f,
                        animationSpec = composeTween(
                            (style.pressFillDuration ?: motion.slow).millis,
                            easing = motion.pressIn,
                        ),
                    )
                }
                fillAlphaJob = coroutineScope.launch { fillAlpha.snapTo(style.pressFillAlpha) }
            }

            is PressInteraction.Release,
            is PressInteraction.Cancel -> {
                // Only the alpha fades. The sweep keeps advancing underneath, which reads as the
                // press settling rather than stopping dead.
                fillAlphaJob?.cancel()
                fillAlphaJob = coroutineScope.launch {
                    fillAlpha.animateTo(
                        targetValue = 0f,
                        animationSpec = composeTween(
                            (style.pressFadeDuration ?: motion.medium).millis,
                            easing = motion.fade,
                        ),
                    )
                }
            }

            // Drawing in leads with the pointer; retracting trails it.
            is HoverInteraction.Enter -> animateUnderline(1f, motion.pressIn, motion)
            is HoverInteraction.Exit -> animateUnderline(0f, motion.fade, motion)
        }
    }

    /**
     * Draws or retracts the underline to [target], replacing any animation already running.
     *
     * @param target Fraction of the width the underline settles at.
     * @param easing Easing the change runs on.
     * @param motion Theme motion in scope, which supplies the default duration.
     */
    private fun animateUnderline(target: Float, easing: Easing, motion: ArecaMotion) {
        val duration = style.hoverDuration ?: motion.slow
        underlineJob?.cancel()
        underlineJob = coroutineScope.launch {
            underlineProgress.animateTo(
                targetValue = target,
                animationSpec = composeTween(duration.millis, easing = easing),
            )
        }
    }

    /** Draws the press fill and the underline under the content, and the focus ring over it. */
    override fun ContentDrawScope.draw() {
        val geometryChanged = size != lastSize || layoutDirection != lastLayoutDirection
        if (geometryChanged) {
            buildShapePath(style.shape, size, layoutDirection, this, offsetPx = 0f, shapePath)
        }

        val colors = currentValueOf(LocalColors)
        val ink = if (style.color == Color.Unspecified) colors.fgBase else style.color
        val rtl = layoutDirection == LayoutDirection.Rtl

        val fillVisible = fillAlpha.value > VisibilityThreshold && fillProgress.value > 0f
        val underlineVisible = underlineProgress.value > 0f
        if (fillVisible || underlineVisible) {
            clipPath(shapePath) {
                if (fillVisible) {
                    val width = size.width * fillProgress.value
                    drawRect(
                        color = ink,
                        topLeft = Offset(if (rtl) size.width - width else 0f, 0f),
                        size = Size(width, size.height),
                        alpha = fillAlpha.value.coerceIn(0f, 1f),
                    )
                }
                if (underlineVisible) {
                    val thicknessPx = style.underlineThickness.toPx()
                    val width = size.width * underlineProgress.value
                    drawRect(
                        color = ink,
                        topLeft = Offset(if (rtl) size.width - width else 0f, size.height - thicknessPx),
                        size = Size(width, thicknessPx),
                    )
                }
            }
        }

        // Content draws over the sweep; the focus ring draws over the content.
        drawContent()
        focus.draw(this, geometryChanged, colors.accentBase)

        if (geometryChanged) {
            lastSize = size
            lastLayoutDirection = layoutDirection
        }
    }
}
