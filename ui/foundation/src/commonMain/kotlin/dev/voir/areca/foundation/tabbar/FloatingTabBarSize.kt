package dev.voir.areca.foundation.tabbar

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.theme.ArecaTheme

/**
 * Defines the independent layout and content sizing used by [FloatingTabBar].
 *
 * @property height Height available to the item row and shared indicator.
 * @property outerPadding Visual distance from the surrounding screen edges after safe-area insets.
 * @property containerPadding Padding between the floating container and its item row.
 * @property itemPadding Padding inside each equal-width item cell.
 * @property itemContentGap Vertical gap between an icon and optional label.
 * @property iconSize Icon size provided to item content.
 * @property textStyle Text style provided to item content.
 * @property elevation Shadow elevation of the floating container.
 */
@Immutable
public data class FloatingTabBarSize(
    public val height: Dp,
    public val outerPadding: PaddingValues,
    public val containerPadding: PaddingValues,
    public val itemPadding: PaddingValues,
    public val itemContentGap: Dp,
    public val iconSize: Dp,
    public val textStyle: TextStyle,
    public val elevation: Dp,
) {
    /** Theme-backed presets and builders. */
    public companion object {
        /** Theme-backed size used by [LocalFloatingTabBarSize]. */
        public val Default: FloatingTabBarSize
            @Composable
            get() = default()

        /**
         * Creates the default floating TabBar sizing configuration.
         *
         * @param height Height available to the item row and indicator.
         * @param outerPadding Visual distance from the surrounding screen edges.
         * @param containerPadding Padding between the container and item row.
         * @param itemPadding Padding inside every item cell.
         * @param itemContentGap Vertical gap between an icon and optional label.
         * @param iconSize Icon size provided to item content.
         * @param textStyle Text style provided to item content.
         * @param elevation Shadow elevation of the floating container.
         * @return A floating TabBar size containing the supplied layout values.
         */
        @Composable
        public fun default(
            height: Dp = 56.dp,
            outerPadding: PaddingValues = PaddingValues(
                start = ArecaTheme.spacing.xl,
                top = 0.dp,
                end = ArecaTheme.spacing.xl,
                bottom = ArecaTheme.spacing.xl,
            ),
            containerPadding: PaddingValues = PaddingValues(ArecaTheme.spacing.xs),
            itemPadding: PaddingValues = PaddingValues(
                horizontal = ArecaTheme.spacing.xs,
                vertical = ArecaTheme.spacing.sm,
            ),
            itemContentGap: Dp = ArecaTheme.spacing.xs,
            iconSize: Dp = 22.dp,
            textStyle: TextStyle = ArecaTheme.baseTypography.xs.merge(
                fontWeight = FontWeight.Medium,
            ),
            elevation: Dp = ArecaTheme.elevation.lg,
        ): FloatingTabBarSize = FloatingTabBarSize(
            height = height,
            outerPadding = outerPadding,
            containerPadding = containerPadding,
            itemPadding = itemPadding,
            itemContentGap = itemContentGap,
            iconSize = iconSize,
            textStyle = textStyle,
            elevation = elevation,
        )
    }
}

/** Floating TabBar layout sizing supplied by the active theme. */
public val LocalFloatingTabBarSize: ProvidableCompositionLocal<FloatingTabBarSize?> =
    compositionLocalOf { null }
