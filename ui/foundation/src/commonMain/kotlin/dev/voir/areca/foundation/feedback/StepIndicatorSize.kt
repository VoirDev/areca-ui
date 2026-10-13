package dev.voir.areca.foundation.feedback

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import dev.voir.areca.theme.ArecaTheme

/**
 * Metrics of a [StepIndicator].
 *
 * @property thickness Height of every segment.
 * @property segmentLength Width of every segment, or [Dp.Unspecified] for segments that share the
 * width the indicator is given.
 * @property spacing Gap between neighbouring segments.
 * @property shape Shape of every segment.
 */
@Immutable
public data class StepIndicatorSize(
    public val thickness: Dp,
    public val segmentLength: Dp,
    public val spacing: Dp,
    public val shape: Shape,
) {
    /** Named [StepIndicatorSize] steps, and [default] for building a variation on them. */
    public companion object {
        /**
         * Fixed-length segments, the indicator as wide as its steps: the size for the middle of a
         * top bar, where it has to leave room for the controls either side.
         */
        public val Compact: StepIndicatorSize
            @Composable
            get() = default()

        /** Segments sharing whatever width the indicator is given, for a bar across a screen. */
        public val Fill: StepIndicatorSize
            @Composable
            get() = default(segmentLength = Dp.Unspecified)

        /**
         * Builds a size from the current theme.
         *
         * @param thickness Height of every segment.
         * @param segmentLength Width of every segment, or [Dp.Unspecified] to share the width.
         * @param spacing Gap between neighbouring segments.
         * @param shape Shape of every segment.
         * @return Size for an indicator.
         */
        @Composable
        public fun default(
            thickness: Dp = ArecaTheme.spacing.xs,
            segmentLength: Dp = ArecaTheme.spacing.xl5,
            spacing: Dp = ArecaTheme.spacing.sm,
            shape: Shape = ArecaTheme.shapes.full,
        ): StepIndicatorSize = StepIndicatorSize(
            thickness = thickness,
            segmentLength = segmentLength,
            spacing = spacing,
            shape = shape,
        )
    }
}

/** Default [StepIndicatorSize] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalStepIndicatorSize: ProvidableCompositionLocal<StepIndicatorSize?> =
    compositionLocalOf { null }
