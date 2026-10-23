package dev.voir.areca.charts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.theme.ArecaTheme

/**
 * Height and shape of a [StackedBarChart].
 *
 * Take a step — [Tiny], [ExtraSmall], [Small], [Medium], [Large] — and change one value with `copy`.
 *
 * @property height Height of the bar.
 * @property shape Shape the bar and its segments are clipped to.
 * @property segmentGap Space between neighbouring segments, showing the track. It comes out of the
 *   bar's width, shared by the segments in proportion to their shares. `0.dp` by default, so the
 *   segments touch.
 */
@Immutable
public data class StackedBarChartSize(
    public val height: Dp,
    public val shape: Shape,
    public val segmentGap: Dp = 0.dp,
) {
    /** The size steps of a [StackedBarChart]. */
    public companion object {
        /**
         * 4dp capsule with a hairline gap. A proportion line under a pair of figures: enough to read
         * as a split, too little to read as a chart.
         */
        public val Tiny: StackedBarChartSize
            @Composable
            get() = StackedBarChartSize(
                height = 4.dp,
                shape = ArecaTheme.shapes.full,
                segmentGap = ArecaTheme.spacing.xs2,
            )

        /** 8dp capsule with a hairline gap. A split inside a summary block or under a heading. */
        public val ExtraSmall: StackedBarChartSize
            @Composable
            get() = StackedBarChartSize(
                height = 8.dp,
                shape = ArecaTheme.shapes.full,
                segmentGap = ArecaTheme.spacing.xs2,
            )

        /** 12dp. A slim bar inside a list row or beside a figure. */
        public val Small: StackedBarChartSize
            @Composable
            get() = StackedBarChartSize(height = 12.dp, shape = ArecaTheme.shapes.sm)

        /** 24dp. The default: a summary bar above its legend. */
        public val Medium: StackedBarChartSize
            @Composable
            get() = StackedBarChartSize(height = 24.dp, shape = ArecaTheme.shapes.md)

        /** 40dp. A bar that is the subject of its section. */
        public val Large: StackedBarChartSize
            @Composable
            get() = StackedBarChartSize(height = 40.dp, shape = ArecaTheme.shapes.lg)
    }
}

/**
 * Default [StackedBarChartSize] in scope, or `null` to use [StackedBarChartSize.Medium].
 *
 * Not seeded by [dev.voir.areca.theme.ArecaThemeProvider] — see [LocalBarChartStyle] for why.
 */
public val LocalStackedBarChartSize: ProvidableCompositionLocal<StackedBarChartSize?> =
    compositionLocalOf { null }
