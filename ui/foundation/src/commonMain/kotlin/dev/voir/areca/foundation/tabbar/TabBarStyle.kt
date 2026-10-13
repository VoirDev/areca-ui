package dev.voir.areca.foundation.tabbar

import androidx.compose.foundation.Indication
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import dev.voir.areca.indication.TouchIndication
import dev.voir.areca.theme.ArecaTheme

/**
 * Defines the visual treatment of a [TabBar] and its item states.
 *
 * Named like every other tab family — [FloatingTabBarStyle] and
 * [dev.voir.areca.foundation.tabs.TabsStyle] — with `bg` and `content` for the fill and the
 * foreground, and a `Selected` or `Disabled` suffix for the state.
 *
 * @property bg Fill behind all tab items, running under the bar's window insets.
 * @property itemBg Fill of an enabled, unselected item.
 * @property itemBgSelected Fill of the selected item.
 * @property itemBgDisabled Fill of a disabled item.
 * @property content Content color of an enabled, unselected item.
 * @property contentSelected Content color of the selected item.
 * @property contentDisabled Content color of a disabled item.
 * @property itemShape Shape of an item's fill and of its hover, press and focus feedback.
 * @property indication Feedback shown for item pointer, touch and focus interactions.
 */
@Immutable
public data class TabBarStyle(
    public val bg: Color,
    public val itemBg: Color,
    public val itemBgSelected: Color = itemBg,
    public val itemBgDisabled: Color = itemBg,
    public val content: Color,
    public val contentSelected: Color = content,
    public val contentDisabled: Color = content,
    public val itemShape: Shape = RectangleShape,
    public val indication: Indication,
) {
    /** Theme-backed presets and builders. */
    public companion object {
        /** Theme-backed style used by [LocalTabBarStyle]. */
        public val Default: TabBarStyle
            @Composable
            get() = default()

        /**
         * Creates the default TabBar treatment.
         *
         * @param bg Fill behind all tab items.
         * @param itemBg Fill of an enabled, unselected item.
         * @param itemBgSelected Fill of the selected item.
         * @param itemBgDisabled Fill of a disabled item.
         * @param content Content color of an enabled, unselected item.
         * @param contentSelected Content color of the selected item.
         * @param contentDisabled Content color of a disabled item.
         * @param itemShape Shape of an item's fill and feedback.
         * @param indication Feedback shown for item interactions.
         * @return A TabBar style containing the supplied values.
         */
        @Composable
        public fun default(
            bg: Color = ArecaTheme.colors.bgCanvas,
            itemBg: Color = Color.Transparent,
            itemBgSelected: Color = itemBg,
            itemBgDisabled: Color = itemBg,
            content: Color = ArecaTheme.colors.fgSubtle,
            contentSelected: Color = ArecaTheme.colors.accentBase,
            contentDisabled: Color = ArecaTheme.colors.fgDisabled,
            itemShape: Shape = RectangleShape,
            indication: Indication = TouchIndication(
                shape = itemShape,
                color = ArecaTheme.colors.accentBase,
            ),
        ): TabBarStyle = TabBarStyle(
            bg = bg,
            itemBg = itemBg,
            itemBgSelected = itemBgSelected,
            itemBgDisabled = itemBgDisabled,
            content = content,
            contentSelected = contentSelected,
            contentDisabled = contentDisabled,
            itemShape = itemShape,
            indication = indication,
        )
    }
}

/** TabBar treatment supplied by the active theme, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalTabBarStyle: ProvidableCompositionLocal<TabBarStyle?> = compositionLocalOf { null }
