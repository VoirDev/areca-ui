package dev.voir.areca.foundation.feedback

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.progressSemantics
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.layout.RelativePosition
import dev.voir.areca.foundation.layout.RelativePositionLayout
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.millis
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

/**
 * Track and fill colors for [ProgressBar].
 *
 * @property track Color of the unfilled remainder.
 * @property fill Color of the completed portion.
 * @property label Color of the optional label.
 */
@Immutable
public data class ProgressBarStyle(
    public val track: Color,
    public val fill: Color,
    public val label: Color,
) {
    /** The theme's default [ProgressBarStyle], and [default] for building a variation on it. */
    public companion object {
        /** Theme default: an accent fill on a subtle track. */
        public val Default: ProgressBarStyle
            @Composable
            get() = default()

        /**
         * Builds a style from the current theme.
         *
         * @param track Color of the unfilled remainder.
         * @param fill Color of the completed portion.
         * @param label Color of the optional label.
         * @return Style built from the supplied colors.
         */
        @Composable
        public fun default(
            track: Color = ArecaTheme.colors.bgSubtle,
            fill: Color = ArecaTheme.colors.accentBase,
            label: Color = ArecaTheme.colors.fgBase,
        ): ProgressBarStyle = ProgressBarStyle(track = track, fill = fill, label = label)
    }
}

/**
 * Height, shape, and timing for [ProgressBar].
 *
 * One [shape] covers both the track and the fill: giving them different corners makes a
 * nearly-complete bar look like two stacked shapes rather than one filling up.
 *
 * @property thickness Height of the bar.
 * @property shape Corner shape of the track and the fill.
 * @property labelTextStyle Style of the optional label.
 * @property animationDuration Time the fill takes to travel to a new value.
 */
@Immutable
public data class ProgressBarSize(
    public val thickness: Dp,
    public val shape: Shape,
    public val labelTextStyle: TextStyle,
    public val animationDuration: Duration,
) {
    /** Named [ProgressBarSize] steps, the theme default, and [default] for building a variation on it. */
    public companion object {
        /** 4dp. A hairline bar, for the top of a screen or the bottom of a card. */
        public val Small: ProgressBarSize
            @Composable
            get() = ProgressBarSize(
                thickness = 4.dp,
                shape = ArecaTheme.shapes.full,
                labelTextStyle = ArecaTheme.typography.labelSmall,
                animationDuration = DefaultAnimationDuration,
            )

        /** 8dp. The default: a bar the user is watching. */
        public val Medium: ProgressBarSize
            @Composable
            get() = default()

        /** 16dp. A bar that is the subject of its screen, such as an upload in progress. */
        public val Large: ProgressBarSize
            @Composable
            get() = ProgressBarSize(
                thickness = 16.dp,
                shape = ArecaTheme.shapes.full,
                labelTextStyle = ArecaTheme.typography.labelLarge,
                animationDuration = DefaultAnimationDuration,
            )

        /** Theme default: [Medium]. */
        public val Default: ProgressBarSize
            @Composable
            get() = default()

        /**
         * Builds a size from the current theme, starting from [Medium].
         *
         * @param thickness Height of the bar.
         * @param shape Corner shape of the track and the fill.
         * @param labelTextStyle Style of the optional label.
         * @param animationDuration Time the fill takes to travel to a new value.
         * @return Size for a progress bar.
         */
        @Composable
        public fun default(
            thickness: Dp = 8.dp,
            shape: Shape = ArecaTheme.shapes.full,
            labelTextStyle: TextStyle = ArecaTheme.typography.labelMedium,
            animationDuration: Duration = DefaultAnimationDuration,
        ): ProgressBarSize = ProgressBarSize(
            thickness = thickness,
            shape = shape,
            labelTextStyle = labelTextStyle,
            animationDuration = animationDuration,
        )

        /**
         * Time the fill takes to reach a new value.
         *
         * Longer than the motion scale's [dev.voir.areca.theme.ArecaMotion.slow], because a progress
         * fill is watched rather than glanced at: a fast jump reads as the number changing, a slow
         * travel reads as work being done.
         */
        private val DefaultAnimationDuration: Duration = 500.milliseconds
    }
}

/** Default [ProgressBarSize] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalProgressBarSize: ProvidableCompositionLocal<ProgressBarSize?> =
    compositionLocalOf { null }

/** Default [ProgressBarStyle] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalProgressBarStyle: ProvidableCompositionLocal<ProgressBarStyle?> =
    compositionLocalOf { null }

/**
 * A horizontal bar showing how much of a task is done.
 *
 * Pass a fraction for [progress] when the total is known, and `null` when it is not — an
 * indeterminate bar sweeps a band along the track instead of filling. Prefer the determinate form
 * whenever a total exists: a bar that never fills is less informative than a [Spinner], because it
 * implies progress it cannot report.
 *
 * Announced to assistive technology with its value, so a screen reader reads the percentage. Pass
 * [contentDescription] to name what is progressing.
 *
 * @param progress Fraction complete, coerced into `0f..1f`. `null` for indeterminate.
 * @param modifier Modifier applied to the bar container.
 * @param label Optional text shown beside the bar, such as a percentage or a file name.
 * @param labelPosition Where [label] sits relative to the bar.
 * @param contentDescription What is progressing, read by assistive technology.
 * @param style Track, fill, and label colors. Defaults to the theme's.
 * @param size Thickness, shape, and timing. Defaults to the theme's, or [ProgressBarSize.Default].
 */
@Composable
public fun ProgressBar(
    progress: Float?,
    modifier: Modifier = Modifier,
    label: CharSequence? = null,
    labelPosition: RelativePosition = RelativePosition.BottomCenter,
    contentDescription: String? = null,
    style: ProgressBarStyle = LocalProgressBarStyle.current ?: ProgressBarStyle.Default,
    size: ProgressBarSize = LocalProgressBarSize.current ?: ProgressBarSize.Default,
) {
    val fraction = progress?.coerceIn(0f, 1f)

    val semantics = Modifier
        .then(
            if (fraction != null) {
                Modifier.progressSemantics(fraction)
            } else {
                Modifier.progressSemantics()
            }
        )
        .then(
            if (contentDescription != null) {
                Modifier.semantics { this.contentDescription = contentDescription }
            } else {
                Modifier
            }
        )

    RelativePositionLayout(
        position = labelPosition,
        overlay = label?.let {
            {
                Text(text = it, style = size.labelTextStyle.merge(color = style.label))
            }
        },
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(size.thickness)
                .clip(size.shape)
                .background(style.track)
                .then(semantics)
        ) {
            if (fraction != null) {
                DeterminateFill(fraction = fraction, style = style, size = size)
            } else {
                IndeterminateFill(style = style, size = size)
            }
        }
    }
}

/**
 * The filled portion of a determinate bar.
 *
 * @param fraction Fraction complete, already coerced into `0f..1f`.
 * @param style Colors to draw with.
 * @param size Shape and timing.
 */
@Composable
private fun DeterminateFill(
    fraction: Float,
    style: ProgressBarStyle,
    size: ProgressBarSize,
) {
    val animatedFraction by animateFloatAsState(
        targetValue = fraction,
        animationSpec = tween(durationMillis = size.animationDuration.millis),
        label = "progressFraction",
    )

    Box(
        Modifier
            .fillMaxHeight()
            .fillMaxWidth(fraction = animatedFraction)
            .clip(size.shape)
            .background(style.fill)
    )
}

/**
 * A band travelling along an indeterminate bar.
 *
 * The band starts fully off the leading edge and ends fully off the trailing one, so it appears to
 * enter and leave rather than materialising at the edges. Restarts rather than reversing: a band that
 * bounces back reads as progress being undone.
 *
 * @param style Colors to draw with.
 * @param size Shape and timing.
 */
@Composable
private fun IndeterminateFill(
    style: ProgressBarStyle,
    size: ProgressBarSize,
) {
    BoxWithConstraints(Modifier.fillMaxWidth().fillMaxHeight()) {
        val trackWidth = maxWidth
        val bandWidth = trackWidth * IndeterminateBandFraction
        val transition = rememberInfiniteTransition(label = "indeterminateProgress")

        val position by transition.animateFloat(
            initialValue = -1f,
            targetValue = 1f / IndeterminateBandFraction,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = (size.animationDuration * IndeterminateCycles).millis,
                    easing = ArecaTheme.motion.standard,
                ),
                repeatMode = RepeatMode.Restart,
            ),
            label = "indeterminateBand",
        )

        Box(
            Modifier
                .offset(x = bandWidth * position)
                .width(bandWidth)
                .fillMaxHeight()
                .clip(size.shape)
                .background(style.fill)
        )
    }
}

/** Share of the track the travelling band covers. */
private const val IndeterminateBandFraction = 0.35f

/** How many times longer one sweep is than a determinate bar's travel to a new value. */
private const val IndeterminateCycles = 3
