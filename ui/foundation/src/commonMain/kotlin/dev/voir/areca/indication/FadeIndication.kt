package dev.voir.areca.indication

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.Animatable as CoreAnimatable
import androidx.compose.animation.core.AnimationVector4D
import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.node.CompositionLocalConsumerModifierNode
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.currentValueOf
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import dev.voir.areca.theme.ArecaMotion
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.LocalColors
import dev.voir.areca.theme.LocalMotion
import dev.voir.areca.theme.millis
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.time.Duration
import androidx.compose.animation.core.tween as composeTween

/**
 * Indication that crossfades the element's background, plus a focus ring.
 *
 * - **Hover** — background crossfades to [hoverColor].
 * - **Press** — background crossfades to [pressedColor], quickly in and slowly out.
 * - **Focus** — [focusOutline] is drawn outside the bounds. The background is untouched, so focus
 *   never fights the hover crossfade.
 *
 * The asymmetric press timing is the point of this indication: [pressInDuration] is short so the
 * press lands immediately, and [releaseDuration] is long so the release glides. Equal timings make
 * the element feel either sluggish or twitchy.
 *
 * Every color and duration left at its default follows the theme in scope when the interaction
 * happens: the tints are the theme's primary foreground at low opacity, so the feedback darkens a
 * light surface and lightens a dark one, and the timings are the theme's motion steps. An
 * indication built once, outside composition, still answers a theme switch.
 *
 * Best on a surface whose background already carries meaning — a card, a list row, a menu item.
 * Reach for [TouchIndication] when the element is small enough that a radial reads better than a
 * fill.
 *
 * Do **not** wrap the element in `Modifier.clip(shape)`: outer clipping crops the focus ring, which
 * is drawn outside the bounds. Use `Modifier.background(color, shape)` instead.
 */
@Immutable
public data class FadeIndication(
    /**
     * Shape of the element.
     *
     * The background fill follows it and the focus ring is outset from it, so a circular element
     * gets a circular ring. Must match the shape passed to `Modifier.background(color, shape)`.
     */
    public val shape: Shape = RoundedCornerShape(8.dp),
    /**
     * Background at rest.
     *
     * [Color.Unspecified] — the default — is the theme's primary foreground at zero opacity, which
     * leaves the element's own background alone and crossfades without a hue shift.
     */
    public val color: Color = Color.Unspecified,
    /**
     * Background while hovered.
     *
     * [Color.Unspecified] — the default — is the theme's primary foreground at a light tint.
     */
    public val hoverColor: Color = Color.Unspecified,
    /**
     * Background while pressed.
     *
     * [Color.Unspecified] — the default — is the theme's primary foreground at a tint heavier than
     * hover, so the press reads as more of the same.
     */
    public val pressedColor: Color = Color.Unspecified,
    /** Focus ring drawn outside the element. */
    public val focusOutline: FocusOutline = FocusOutline(),
    /**
     * Crossfade to [pressedColor]. Short, so the press reads as a direct response.
     *
     * `null` — the default — takes the theme's [ArecaMotion.instant] step.
     */
    public val pressInDuration: Duration? = null,
    /**
     * Crossfade back to rest, and every hover crossfade. Long, so the release glides.
     *
     * `null` — the default — takes the theme's [ArecaMotion.slow] step.
     */
    public val releaseDuration: Duration? = null,
) : IndicationNodeFactory {
    /**
     * Creates the node that draws this indication for one element.
     *
     * @param interactionSource Source of the element's hover, press, and focus events.
     * @return A fresh [FadeIndicationNode] bound to [interactionSource].
     */
    override fun create(interactionSource: InteractionSource): DelegatableNode =
        FadeIndicationNode(this, interactionSource)

    /** Presets. */
    public companion object {
        /**
         * How a pressable surface on a plain background answers a press — a card, a tile, an
         * avatar, a plain button — the same way a neutral list row does.
         *
         * A flat tint of [ink] over the whole shape, so a pressed card beside a list answers the
         * finger the way the rows do. The ink is the colour of the copy on the surface rather than
         * black: a pressed surface darkens towards its own text, and a dangerous one reddens. Because
         * the copy is light on a dark theme, so is the tint.
         *
         * Not for a saturated fill — a dark tint over one reads as a smudge. Use
         * `TouchIndication.onColor` there.
         *
         * @param shape Shape of the element, which the tint follows.
         * @param ink Colour the tint is taken from.
         * @param focusOutline Focus ring drawn outside the element.
         * @return Indication for a card, a row, an avatar, a tile or a plain button.
         */
        @Composable
        public fun tint(
            shape: Shape = RoundedCornerShape(ArecaTheme.radius.lg),
            ink: Color = ArecaTheme.colors.fgBase,
            focusOutline: FocusOutline = FocusOutline(),
        ): FadeIndication = FadeIndication(
            shape = shape,
            color = ink.copy(alpha = 0f),
            hoverColor = ink.copy(alpha = TINT_HOVER_ALPHA),
            pressedColor = ink.copy(alpha = TINT_PRESSED_ALPHA),
            focusOutline = focusOutline,
        )
    }
}

/** Opacity of [FadeIndication.tint] while hovered; also a neutral list row's. */
internal const val TINT_HOVER_ALPHA = 0.045f

/** Opacity of [FadeIndication.tint] while pressed, heavier than hover so the press reads as more. */
internal const val TINT_PRESSED_ALPHA = 0.085f

/**
 * Draws one element's [FadeIndication].
 *
 * @property style Colors and timings to draw with.
 * @property interactionSource Source the hover, press, and focus events are collected from.
 */
private class FadeIndicationNode(
    private val style: FadeIndication,
    private val interactionSource: InteractionSource,
) : Modifier.Node(), DrawModifierNode, CompositionLocalConsumerModifierNode {

    /** Whether a press is currently held. Press wins over hover when both are true. */
    private var pressed = false

    /** Whether the pointer is currently over the element. */
    private var hovered = false

    /**
     * Current background color, animating between rest, hover, and pressed.
     *
     * Created on attach, because its starting value is the rest color, which may come from the theme.
     */
    private var background: CoreAnimatable<Color, AnimationVector4D>? = null

    /** In-flight crossfade. Cancelled by the next state change so a reversal does not queue. */
    private var backgroundJob: Job? = null

    /** Focus ring, which owns its own animation and path cache. */
    private val focus = FocusOutlineRenderer(style.focusOutline, style.shape)

    /** Path along the shape's edge, used to fill the background. */
    private val backgroundPath = Path()

    /** Size [backgroundPath] was last built for. */
    private var lastSize: Size = Size.Zero

    /** Layout direction [backgroundPath] was last built for; `null` until the first build. */
    private var lastLayoutDirection: LayoutDirection? = null

    /** Starts collecting the element's interactions; the collection ends when the node detaches. */
    override fun onAttach() {
        background = Animatable(resolve(style.color, alpha = 0f))
        coroutineScope.launch {
            interactionSource.interactions.collect { handle(it) }
        }
    }

    /**
     * Resolves one of the style's colors against the theme in scope.
     *
     * @param color Color from the style.
     * @param alpha Opacity of the theme's primary foreground when [color] is unspecified.
     * @return [color] itself, or the theme's ink at [alpha] when [color] is [Color.Unspecified].
     */
    private fun resolve(color: Color, alpha: Float): Color =
        if (color == Color.Unspecified) currentValueOf(LocalColors).fgBase.copy(alpha = alpha) else color

    /**
     * Turns one interaction into a background target.
     *
     * Focus is delegated and never reaches the background, so tabbing into a hovered element does
     * not disturb the hover crossfade.
     *
     * @param interaction Interaction collected from the element.
     */
    private fun handle(interaction: Interaction) {
        val motion: ArecaMotion = currentValueOf(LocalMotion)
        if (focus.handle(interaction, coroutineScope, motion)) return

        val wasPressed = pressed
        when (interaction) {
            is PressInteraction.Press -> pressed = true
            is PressInteraction.Release,
            is PressInteraction.Cancel -> pressed = false

            is HoverInteraction.Enter -> hovered = true
            is HoverInteraction.Exit -> hovered = false
            else -> return
        }

        val target = when {
            pressed -> resolve(style.pressedColor, TINT_PRESSED_ALPHA)
            hovered -> resolve(style.hoverColor, TINT_HOVER_ALPHA)
            else -> resolve(style.color, alpha = 0f)
        }
        val duration = if (pressed && !wasPressed) {
            style.pressInDuration ?: motion.instant
        } else {
            style.releaseDuration ?: motion.slow
        }
        val animatable = background ?: return
        backgroundJob?.cancel()
        backgroundJob = coroutineScope.launch {
            animatable.animateTo(target, composeTween(duration.millis, easing = motion.fade))
        }
    }

    /** Fills the background, draws the content over it, and strokes the focus ring on top. */
    override fun ContentDrawScope.draw() {
        val geometryChanged = size != lastSize || layoutDirection != lastLayoutDirection
        if (geometryChanged) {
            buildShapePath(style.shape, size, layoutDirection, this, offsetPx = 0f, backgroundPath)
        }

        val current = background?.value
        if (current != null && current.alpha > VisibilityThreshold) {
            drawPath(backgroundPath, color = current)
        }

        // Content draws over the background; the focus ring draws over the content.
        drawContent()
        focus.draw(this, geometryChanged, currentValueOf(LocalColors).accentBase)

        if (geometryChanged) {
            lastSize = size
            lastLayoutDirection = layoutDirection
        }
    }
}
