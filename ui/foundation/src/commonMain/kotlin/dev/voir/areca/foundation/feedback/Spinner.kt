package dev.voir.areca.foundation.feedback

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.progressSemantics
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.LocalContentColor
import dev.voir.areca.foundation.icon.IconSize
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.millis
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

/**
 * Diameter and stroke weight for [Spinner].
 *
 * Stroke is derived from [container] but capped: an arc that keeps thickening past 2dp starts to
 * read as a ring rather than as motion. Every preset below takes the derived stroke, so a size built
 * from a container alone and the named step for that container cannot disagree.
 *
 * @property container Square canvas the arc is drawn in. Matches the equivalent [IconSize] step.
 * @property stroke Width of the arc. Defaults to a tenth of [container], capped at 2dp.
 */
@Immutable
public data class SpinnerSize(
    public val container: Dp,
    public val stroke: Dp = spinnerStroke(container),
) {
    /** Named [SpinnerSize] steps, the theme default, and [default] for building a variation on it. */
    public companion object {
        /** 12dp. Inside a small button or inline with dense text. */
        public val ExtraSmall: SpinnerSize = SpinnerSize(container = IconSize.ExtraSmall)

        /** 16dp. Inline with body text and inside standard controls. */
        public val Small: SpinnerSize = SpinnerSize(container = IconSize.Small)

        /** 20dp. A button's loading state, a list row's pending mark. */
        public val Medium: SpinnerSize = SpinnerSize(container = IconSize.Medium)

        /** Theme default: [Medium]. */
        public val Default: SpinnerSize = Medium

        /** 24dp. A standalone spinner inside a card or a small surface. */
        public val Large: SpinnerSize = SpinnerSize(container = IconSize.Large)

        /** 32dp. A screen or a full panel waiting on its first load. */
        public val ExtraLarge: SpinnerSize = SpinnerSize(container = IconSize.ExtraLarge)

        /**
         * Builds a size, starting from [Default].
         *
         * @param container Square canvas the arc is drawn in.
         * @param stroke Width of the arc. Defaults to a tenth of [container], capped at 2dp.
         * @return Size for a spinner.
         */
        @Composable
        public fun default(
            container: Dp = Default.container,
            stroke: Dp = spinnerStroke(container),
        ): SpinnerSize = SpinnerSize(container = container, stroke = stroke)
    }
}

/**
 * Stroke width for a spinner of [container] diameter.
 *
 * A tenth of the diameter, capped at 2dp. The cap is what keeps a large spinner reading as a thin
 * arc sweeping round rather than as a thick donut rotating.
 *
 * @param container Diameter the arc is drawn in.
 * @return Stroke width to draw the arc with.
 */
internal fun spinnerStroke(container: Dp): Dp = (container / 10).coerceAtMost(2.dp)

/**
 * Color of a [Spinner]'s arc.
 *
 * A type of its own rather than a bare color so a theme can give the spinner an accent distinct from
 * the text around it — the usual case is a brand-colored spinner on a neutral surface.
 *
 * @property color Arc color. [Color.Unspecified] falls back to the surrounding content color, which
 *   is what a spinner inside a filled button wants.
 */
@Immutable
public data class SpinnerStyle(
    public val color: Color = Color.Unspecified,
) {
    /** Theme-derived [SpinnerStyle] presets, and [default] for building a variation on them. */
    public companion object {
        /** Theme default: the accent. */
        public val Default: SpinnerStyle
            @Composable
            get() = default()

        /**
         * Builds a style from the current theme.
         *
         * @param color Arc color. [Color.Unspecified] takes the surrounding content color.
         * @return Style built from the supplied color.
         */
        @Composable
        public fun default(
            color: Color = ArecaTheme.colors.accentBase,
        ): SpinnerStyle = SpinnerStyle(color = color)

        /** Takes the surrounding content color, for a spinner inside a filled or tinted container. */
        public val Inherit: SpinnerStyle = SpinnerStyle(color = Color.Unspecified)
    }
}

/** Default [SpinnerSize] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalSpinnerSize: ProvidableCompositionLocal<SpinnerSize?> = compositionLocalOf { null }

/** Default [SpinnerStyle] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalSpinnerStyle: ProvidableCompositionLocal<SpinnerStyle?> =
    compositionLocalOf { null }

/**
 * An arc that sweeps and rotates, for work of unknown duration.
 *
 * Two animations run at once and deliberately share one period: the arc rotates a full turn while
 * its sweep grows and shrinks. That is what makes the motion read as continuous rather than as a
 * loop — the arc is never in the same place at the same length twice within a cycle.
 *
 * Use it when there is no progress to report. When there is, use [ProgressBar], which tells the user
 * how long they are waiting.
 *
 * Announced to assistive technology as indeterminate progress. [contentDescription] names *what* is
 * loading; without it a screen reader says only "busy".
 *
 * @param modifier Modifier applied to the spinner canvas.
 * @param style Arc color. Defaults to the theme's, or [SpinnerStyle.Default]; a style whose color
 *   is [Color.Unspecified] takes the surrounding content color.
 * @param size Diameter and stroke. Defaults to the theme's, or [SpinnerSize.Default].
 * @param contentDescription What is loading, read by assistive technology.
 * @param period Time for one full rotation, and one full sweep cycle.
 */
@Composable
public fun Spinner(
    modifier: Modifier = Modifier,
    style: SpinnerStyle = LocalSpinnerStyle.current ?: SpinnerStyle.Default,
    size: SpinnerSize = LocalSpinnerSize.current ?: SpinnerSize.Default,
    contentDescription: String? = null,
    period: Duration = 900.milliseconds,
) {
    val resolvedColor = style.color.takeOrElse { LocalContentColor.current }

    val transition = rememberInfiniteTransition(label = "spinnerTransition")
    val motion = ArecaTheme.motion

    val rotation by transition.animateFloat(
        initialValue = 0f,
        targetValue = FullTurnDegrees,
        animationSpec = infiniteRepeatable(
            // Linear, so the rotation itself carries no rhythm — the sweep supplies all of it.
            animation = tween(
                durationMillis = period.millis,
                easing = LinearEasing,
            ),
        ),
        label = "spinnerRotation",
    )

    val sweep by transition.animateFloat(
        initialValue = MinSweepDegrees,
        targetValue = MaxSweepDegrees,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = period.millis,
                easing = motion.standard,
            ),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "spinnerSweep",
    )

    Canvas(
        modifier = modifier
            .size(size.container)
            .progressSemantics()
            .then(
                if (contentDescription != null) {
                    Modifier.semantics { this.contentDescription = contentDescription }
                } else {
                    Modifier
                }
            )
    ) {
        val strokeWidth = size.stroke.toPx()
        // The arc is stroked on its centre line, so inset by half the stroke to keep it in bounds.
        val inset = strokeWidth / 2f

        drawArc(
            color = resolvedColor,
            startAngle = rotation,
            sweepAngle = sweep,
            useCenter = false,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
            topLeft = Offset(inset, inset),
            size = Size(
                width = this.size.width - strokeWidth,
                height = this.size.height - strokeWidth,
            ),
        )
    }
}

/** One complete rotation. */
private const val FullTurnDegrees = 360f

/** Shortest the arc gets. Below this it reads as a dot rather than an arc. */
private const val MinSweepDegrees = 40f

/** Longest the arc gets. A full 360 would close the ring and hide the rotation. */
private const val MaxSweepDegrees = 300f
