package dev.voir.areca.indication

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.theme.ArecaMotion
import dev.voir.areca.theme.millis
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.time.Duration
import androidx.compose.animation.core.tween as composeTween

/**
 * Ring drawn outside an element while it holds keyboard focus.
 *
 * Every Areca indication draws the same ring, so it is described once here rather than by four
 * repeated properties on each indication. The ring is a stroke on a path outset from the element's
 * own shape, which means a circular element gets a circular ring and a pill gets a pill.
 *
 * Focus is drawn *only* as this ring. No indication changes an element's background or border on
 * focus, so a focused element that is also hovered or pressed shows all three states at once.
 */
@Immutable
public data class FocusOutline(
    /**
     * Ring color.
     *
     * [Color.Unspecified] — the default — resolves to the theme's accent at draw time, so an
     * indication constructed outside composition still gets a themed ring. Set it explicitly when
     * the accent would not read against the element, as on a solid brand button.
     */
    public val color: Color = Color.Unspecified,
    /** Ring thickness. */
    public val width: Dp = 2.dp,
    /** Gap between the element's edge and the ring's inner edge. */
    public val offset: Dp = 2.dp,
    /**
     * Fade-in and fade-out duration.
     *
     * `null` — the default — takes the theme's [ArecaMotion.fast] step at the moment focus moves,
     * so a theme that retunes its motion retunes every focus ring with it.
     */
    public val duration: Duration? = null,
)

/**
 * Draws and animates one element's [FocusOutline].
 *
 * Owned by an indication's [androidx.compose.ui.Modifier.Node]: the node forwards interactions to
 * [handle] and calls [draw] from its own draw pass. Keeping this separate is what lets every
 * indication share one focus behaviour instead of re-implementing the animatable, the job, the path
 * cache, and the stroke.
 *
 * Not thread-safe. Instances belong to a single node and are driven from its coroutine scope.
 *
 * @property style Ring geometry and timing.
 * @property shape Element shape the ring is outset from.
 */
internal class FocusOutlineRenderer(
    private val style: FocusOutline,
    private val shape: Shape,
) {
    /** Ring opacity, 0 when unfocused and 1 when focused. */
    private val alpha = Animatable(0f)

    /** In-flight fade. Cancelled by the next focus change so a reversal does not queue behind it. */
    private var fadeJob: Job? = null

    /**
     * Outset path the ring is stroked along.
     *
     * Rebuilt only when the owning node reports a geometry change or when the outset itself moves,
     * so a focused element at rest re-strokes a cached path each frame rather than rebuilding one.
     */
    private val path = Path()

    /** Outset [path] was last built for, in pixels; `NaN` until the first build. */
    private var lastOffsetPx = Float.NaN

    /**
     * Consumes [interaction] if it is a focus event.
     *
     * @param interaction Interaction observed by the owning node.
     * @param scope Node's coroutine scope, which cancels the fade when the node detaches.
     * @param motion Theme motion in scope: the fade runs on its fade easing, and on its fast step
     *   unless [FocusOutline.duration] says otherwise.
     * @return `true` when the interaction was a focus event and the caller should ignore it.
     */
    fun handle(interaction: Interaction, scope: CoroutineScope, motion: ArecaMotion): Boolean {
        val target = when (interaction) {
            is FocusInteraction.Focus -> 1f
            is FocusInteraction.Unfocus -> 0f
            else -> return false
        }
        val duration = style.duration ?: motion.fast
        fadeJob?.cancel()
        fadeJob = scope.launch {
            alpha.animateTo(target, composeTween(duration.millis, easing = motion.fade))
        }
        return true
    }

    /**
     * Strokes the ring, if it is visible at all.
     *
     * The stroke is centred on the path, so the path is outset by `offset + width / 2` to put the
     * stroke's *inner* edge at [FocusOutline.offset] from the element's edge.
     *
     * @param drawScope Scope of the owning node's draw pass.
     * @param geometryChanged Whether size or layout direction changed since the last frame.
     * @param fallbackColor Color used when [FocusOutline.color] is [Color.Unspecified].
     */
    fun draw(drawScope: ContentDrawScope, geometryChanged: Boolean, fallbackColor: Color) {
        if (alpha.value <= VisibilityThreshold) return

        with(drawScope) {
            val strokeWidthPx = style.width.toPx()
            val offsetPx = style.offset.toPx() + strokeWidthPx / 2f

            if (geometryChanged || offsetPx != lastOffsetPx) {
                buildShapePath(shape, size, layoutDirection, this, offsetPx, path)
                lastOffsetPx = offsetPx
            }

            val ringColor = if (style.color == Color.Unspecified) fallbackColor else style.color
            drawPath(
                path = path,
                color = ringColor.copy(alpha = ringColor.alpha * alpha.value),
                style = Stroke(width = strokeWidthPx),
            )
        }
    }
}

/**
 * Alpha below which an overlay is invisible and not worth drawing.
 *
 * Shared by every indication so they all stop drawing at the same point.
 */
internal const val VisibilityThreshold = 0.001f
