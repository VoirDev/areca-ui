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
 * Take a step — [Small], [Medium], [Large] — and change one value with `copy`.
 *
 * @property height Height of the bar.
 * @property shape Shape the bar and its segments are clipped to.
 */
@Immutable
public data class StackedBarChartSize(
    public val height: Dp,
    public val shape: Shape,
) {
    /** The size steps of a [StackedBarChart]. */
    public companion object {
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
