package dev.voir.areca.foundation.wheel

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Metrics shared by the columns of a [WheelPicker].
 *
 * Pushed down from the picker to its columns through [LocalWheelPickerSize], so a picker's columns
 * line up by construction rather than by every caller remembering to pass the same numbers. Columns
 * inside a picker share its height: a column half a row taller than its neighbour would put its
 * centre slot somewhere the picker's highlight is not.
 *
 * No colors here, deliberately — those are [WheelPickerStyle]; and no pacing, which is
 * [WheelPickerScroll]. This type answers only how big the wheel is.
 *
 * @property itemHeight Height of one row. Every row shares it, which is what lets the wheel be
 *   described by a single continuous row offset and so land exactly on a row.
 * @property visibleItemsCount How many rows a column shows at once. **Must be odd and at least
 *   one**, so a row can sit exactly in the centre.
 */
@Immutable
public data class WheelPickerSize(
    public val itemHeight: Dp = 36.dp,
    public val visibleItemsCount: Int = 5,
) {
    init {
        require(visibleItemsCount >= 1) {
            "visibleItemsCount must be at least 1, was $visibleItemsCount"
        }
        require(visibleItemsCount % 2 == 1) {
            "visibleItemsCount must be odd so one row can sit in the centre, was $visibleItemsCount"
        }
    }

    /** Rows above the centre, which is also the number below it. */
    public val sideItemsCount: Int get() = (visibleItemsCount - 1) / 2

    /** Height of a whole column. */
    public val height: Dp get() = itemHeight * visibleItemsCount

    /** The stock wheel metrics. */
    public companion object {
        /** Theme default: five 36dp rows. */
        public val Default: WheelPickerSize = WheelPickerSize()
    }
}

/** Default [WheelPickerSize] in scope, or `null` to use [WheelPickerSize.Default]. */
public val LocalWheelPickerSize: ProvidableCompositionLocal<WheelPickerSize?> =
    compositionLocalOf { null }
