package dev.voir.areca.foundation.icon

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.LocalContentColor
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.tween

/**
 * Where a [Chevron] points.
 *
 * [Left] and [Right] are named for a left-to-right layout. [Chevron] mirrors them in a right-to-left
 * one, so [Right] always points towards the end of the line — into a detail, on to the next page —
 * and [Left] towards its start, whichever way the language reads.
 *
 * @property degrees Rotation from the right-facing base drawing. [Chevron] animates between these,
 *   so the values also decide which way it turns: [Up] is `-90`, not `270`, so collapsing an
 *   expanded row rotates back the way it came instead of continuing around.
 */
public enum class ChevronDirection(public val degrees: Float) {
    /** Points up. Typically an expanded disclosure. */
    Up(-90f),

    /** Points right in a left-to-right layout, left in a right-to-left one: towards the end. */
    Right(0f),

    /** Points down. Typically a collapsed disclosure or a dropdown. */
    Down(90f),

    /** Points left in a left-to-right layout, right in a right-to-left one: towards the start. */
    Left(180f),
}

/**
 * Size and stroke weight for [Chevron].
 *
 * Stroke is a separate token rather than a fraction of the size because the relationship is not
 * linear: a chevron has to stay visible at 12dp and must not turn into a wedge at 32dp, so the
 * presets flatten off at 2dp rather than scaling all the way up.
 *
 * @property container Square canvas the chevron is drawn in. Matches the equivalent [IconSize] step.
 * @property stroke Width of both lines.
 */
@Immutable
public data class ChevronSize(
    public val container: Dp,
    public val stroke: Dp,
) {
    /** The named steps, one per [IconSize] step. */
    public companion object {
        /** 12dp, for a chevron inline with small text. */
        public val ExtraSmall: ChevronSize = ChevronSize(container = IconSize.ExtraSmall, stroke = 1.2.dp)

        /** 16dp, inline with body text and inside standard controls. */
        public val Small: ChevronSize = ChevronSize(container = IconSize.Small, stroke = 1.5.dp)

        /** 20dp. The default: a list row's trailing disclosure. */
        public val Medium: ChevronSize = ChevronSize(container = IconSize.Medium, stroke = 2.dp)

        /** 24dp, for standalone controls and top-bar actions. */
        public val Large: ChevronSize = ChevronSize(container = IconSize.Large, stroke = 2.dp)

        /** 32dp, for a chevron that carries a control on its own. */
        public val ExtraLarge: ChevronSize = ChevronSize(container = IconSize.ExtraLarge, stroke = 2.dp)
    }
}

/** Default [ChevronSize] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalChevronSize: ProvidableCompositionLocal<ChevronSize?> = compositionLocalOf { null }

/**
 * A chevron that turns when [direction] changes.
 *
 * Drawn rather than rendered from a vector asset, for one reason: it animates. Swapping between
 * four vector assets would cut from one to the next, and a disclosure that snaps loses the link
 * between what was pressed and what opened. Use the vector chevrons for a static mark; use this
 * whenever the direction can change.
 *
 * The default turn is the theme's [dev.voir.areca.theme.ArecaMotion.fast] step on its standard
 * easing, with no overshoot. A chevron is a status indicator, not a toy, and overshoot at this size
 * reads as a wobble.
 *
 * [ChevronDirection.Left] and [ChevronDirection.Right] are mirrored in a right-to-left layout; the
 * vertical directions are symmetric and need no mirroring.
 *
 * @param direction Where the chevron points.
 * @param modifier Modifier applied to the chevron canvas.
 * @param color Stroke color. Defaults to the surrounding content color.
 * @param size Canvas size and stroke weight. Defaults to the theme's, or [ChevronSize.Medium].
 * @param strokeCap End treatment of both lines. Round matches the foundation icon set.
 * @param animationSpec How the rotation moves when [direction] changes.
 */
@Composable
public fun Chevron(
    direction: ChevronDirection,
    modifier: Modifier = Modifier,
    color: Color = LocalContentColor.current,
    size: ChevronSize = LocalChevronSize.current ?: ChevronSize.Medium,
    strokeCap: StrokeCap = StrokeCap.Round,
    animationSpec: AnimationSpec<Float> = ArecaTheme.motion.let { it.fast.tween(it.standard) },
) {
    val rotation by animateFloatAsState(
        targetValue = direction.degrees,
        animationSpec = animationSpec,
        label = "chevronRotation",
    )

    Canvas(modifier = modifier.size(size.container)) {
        val strokeWidth = size.stroke.toPx()
        val centerX = this.size.width / 2f
        val centerY = this.size.height / 2f

        // Inset by half the stroke so the round caps stay inside the canvas instead of clipping.
        val half = (minOf(this.size.width, this.size.height) - strokeWidth) / 2f

        // The chevron is deliberately taller than it is wide: a square one reads as an arrowhead,
        // and this ratio is what makes it read as a direction instead.
        val horizontalSpan = half * 0.75f
        val verticalSpan = half * 0.9f

        val backX = centerX - horizontalSpan / 2f
        val tipX = centerX + horizontalSpan / 2f

        val top = Offset(backX, centerY - verticalSpan)
        val tip = Offset(tipX, centerY)
        val bottom = Offset(backX, centerY + verticalSpan)

        val pivot = Offset(centerX, centerY)
        val mirrored = layoutDirection == LayoutDirection.Rtl
        withTransform({
            // Mirror first in the transform order so it applies after the rotation: a chevron
            // turned up or down stays put, and one pointing along the line flips to the other end.
            if (mirrored) {
                scale(scaleX = -1f, scaleY = 1f, pivot = pivot)
            }
            rotate(degrees = rotation, pivot = pivot)
        }) {
            drawLine(
                color = color,
                start = top,
                end = tip,
                strokeWidth = strokeWidth,
                cap = strokeCap,
            )
            drawLine(
                color = color,
                start = bottom,
                end = tip,
                strokeWidth = strokeWidth,
                cap = strokeCap,
            )
        }
    }
}
