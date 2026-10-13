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
 * Defines layout and content sizing used by a [TabBar].
 *
 * @property itemMinHeight Minimum height of every tab item.
 * @property itemPadding Padding inside every tab item.
 * @property iconSize Icon size provided to tab content through the foundation content local.
 * @property textStyle Text style provided to tab content through the foundation content local.
 */
@Immutable
public data class TabBarSize(
    public val itemMinHeight: Dp,
    public val itemPadding: PaddingValues,
    public val iconSize: Dp,
    public val textStyle: TextStyle,
) {
    /** Theme-backed presets and builders. */
    public companion object {
        /** Theme-backed size used by [LocalTabBarSize]. */
        public val Default: TabBarSize
            @Composable
            get() = default()

        /**
         * Creates the default TabBar sizing configuration.
         *
         * @param itemMinHeight Minimum height of every tab item.
         * @param itemPadding Padding inside every tab item.
         * @param iconSize Icon size provided to tab content.
         * @param textStyle Text style provided to tab content.
         * @return A TabBar size containing the supplied layout and content values.
         */
        @Composable
        public fun default(
            itemMinHeight: Dp = 56.dp,
            itemPadding: PaddingValues = PaddingValues(
                horizontal = ArecaTheme.spacing.xs,
                vertical = ArecaTheme.spacing.xs,
            ),
            iconSize: Dp = 22.dp,
            textStyle: TextStyle = ArecaTheme.baseTypography.sm.merge(
                fontWeight = FontWeight.Medium,
            ),
        ): TabBarSize = TabBarSize(
            itemMinHeight = itemMinHeight,
            itemPadding = itemPadding,
            iconSize = iconSize,
            textStyle = textStyle,
        )
    }
}

/** TabBar sizing supplied by the active theme. */
public val LocalTabBarSize: ProvidableCompositionLocal<TabBarSize?> = compositionLocalOf { null }
