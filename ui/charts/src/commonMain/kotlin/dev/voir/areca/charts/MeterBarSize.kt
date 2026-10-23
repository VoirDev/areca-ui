package dev.voir.areca.charts

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Thickness and marker metrics of a [MeterBar].
 *
 * Take a step — [Slim], [Medium], [Large] — and change one value with `copy`. The bar is a capsule:
 * its ends are rounded by half its [height].
 *
 * @property height Thickness of the track and the fill.
 * @property markerWidth Width of the reference marker.
 * @property markerOverhang How far the marker reaches past the bar, above and below.
 */
@Immutable
public data class MeterBarSize(
    public val height: Dp,
    public val markerWidth: Dp,
    public val markerOverhang: Dp,
) {
    /** The size steps of a [MeterBar]. */
    public companion object {
        /** 4dp. A nested row under its parent, such as a subcategory under a category. */
        public val Slim: MeterBarSize = MeterBarSize(height = 4.dp, markerWidth = 2.dp, markerOverhang = 3.dp)

        /** 6dp. The default: a list row's amount against its reference. */
        public val Medium: MeterBarSize = MeterBarSize(height = 6.dp, markerWidth = 2.dp, markerOverhang = 3.dp)

        /** 10dp. A bar that is the subject of its section, such as two totals compared. */
        public val Large: MeterBarSize = MeterBarSize(height = 10.dp, markerWidth = 2.dp, markerOverhang = 3.dp)
    }
}

/**
 * Default [MeterBarSize] in scope, or `null` to use [MeterBarSize.Medium].
 *
 * Not seeded by [dev.voir.areca.theme.ArecaThemeProvider] — see [LocalBarChartStyle] for why.
 */
public val LocalMeterBarSize: ProvidableCompositionLocal<MeterBarSize?> =
    compositionLocalOf { null }
