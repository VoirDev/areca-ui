package dev.voir.areca.foundation.selection

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.icon.IconSize
import dev.voir.areca.theme.ArecaTheme

/**
 * Metrics of a [SlidingSelector]'s options.
 *
 * @property gap Space between options.
 * @property itemPadding Padding inside an option.
 * @property itemMinWidth Smallest width of an option.
 * @property itemMinHeight Smallest height of an option.
 * @property iconSize Size given to icons inside an option.
 * @property textStyle Style given to an option's label.
 */
@Immutable
public data class SlidingSelectorSize(
    public val gap: Dp,
    public val itemPadding: PaddingValues,
    public val itemMinWidth: Dp,
    public val itemMinHeight: Dp,
    public val iconSize: Dp,
    public val textStyle: TextStyle,
) {
    /** Theme-backed presets and builders. */
    public companion object {
        /** Theme default: options 32dp tall, with medium labels. */
        public val Default: SlidingSelectorSize
            @Composable
            get() = default()

        /** Options 28dp tall, with small labels, for a dense row such as a filter bar. */
        public val Small: SlidingSelectorSize
            @Composable
            get() = default(
                itemPadding = PaddingValues(
                    horizontal = ArecaTheme.spacing.md,
                    vertical = ArecaTheme.spacing.xs2,
                ),
                itemMinWidth = 28.dp,
                itemMinHeight = 28.dp,
                iconSize = IconSize.ExtraSmall,
                textStyle = ArecaTheme.typography.labelSmall,
            )

        /** Options 40dp tall, with large labels. */
        public val Large: SlidingSelectorSize
            @Composable
            get() = default(
                itemPadding = PaddingValues(
                    horizontal = ArecaTheme.spacing.xl,
                    vertical = ArecaTheme.spacing.sm,
                ),
                itemMinWidth = 40.dp,
                itemMinHeight = 40.dp,
                iconSize = IconSize.Medium,
                textStyle = ArecaTheme.typography.labelLarge,
            )

        /**
         * Builds a size from the current theme.
         *
         * @param gap Space between options.
         * @param itemPadding Padding inside an option.
         * @param itemMinWidth Smallest width of an option.
         * @param itemMinHeight Smallest height of an option.
         * @param iconSize Size given to icons inside an option.
         * @param textStyle Style given to an option's label.
         * @return Size built from the supplied metrics.
         */
        @Composable
        public fun default(
            gap: Dp = ArecaTheme.spacing.xs2,
            itemPadding: PaddingValues = PaddingValues(
                horizontal = ArecaTheme.spacing.lg,
                vertical = ArecaTheme.spacing.xs,
            ),
            itemMinWidth: Dp = 32.dp,
            itemMinHeight: Dp = 32.dp,
            iconSize: Dp = IconSize.Small,
            textStyle: TextStyle = ArecaTheme.typography.labelMedium,
        ): SlidingSelectorSize = SlidingSelectorSize(
            gap = gap,
            itemPadding = itemPadding,
            itemMinWidth = itemMinWidth,
            itemMinHeight = itemMinHeight,
            iconSize = iconSize,
            textStyle = textStyle,
        )
    }
}

/** Default [SlidingSelectorSize] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalSlidingSelectorSize: ProvidableCompositionLocal<SlidingSelectorSize?> =
    compositionLocalOf { null }
