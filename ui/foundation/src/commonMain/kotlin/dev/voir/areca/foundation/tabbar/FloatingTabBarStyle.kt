package dev.voir.areca.foundation.tabbar

import androidx.compose.foundation.Indication
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import dev.voir.areca.indication.TouchIndication
import dev.voir.areca.theme.ArecaTheme

/**
 * Defines the floating container and unselected content treatment for [FloatingTabBar].
 *
 * @property bg Background color of the floating container.
 * @property content Content color of enabled, unselected items.
 * @property contentDisabled Content color of disabled items.
 * @property shape Shape of the floating container.
 * @property itemShape Shape followed by item hover, press, and focus feedback.
 * @property indication Animated feedback shown for item pointer, touch, and focus interactions.
 */
@Immutable
public data class FloatingTabBarStyle(
    public val bg: Color,
    public val content: Color,
    public val contentDisabled: Color,
    public val shape: Shape,
    public val itemShape: Shape,
    public val indication: Indication,
) {
    /** Theme-backed presets and builders. */
    public companion object {
        /** Theme-backed style used by [LocalFloatingTabBarStyle]. */
        public val Default: FloatingTabBarStyle
            @Composable
            get() = default()

        /**
         * Creates the default floating container treatment.
         *
         * @param bg Background color of the floating container.
         * @param content Content color of enabled, unselected items.
         * @param contentDisabled Content color of disabled items.
         * @param shape Shape of the floating container.
         * @param itemShape Shape followed by item interaction feedback.
         * @param indication Animated feedback shown for item interactions.
         * @return A floating TabBar style containing the supplied visual values.
         */
        @Composable
        public fun default(
            bg: Color = ArecaTheme.colors.bgSurface,
            content: Color = ArecaTheme.colors.fgMuted,
            contentDisabled: Color = ArecaTheme.colors.fgDisabled,
            shape: Shape = ArecaTheme.shapes.xl2,
            itemShape: Shape = ArecaTheme.shapes.xl,
            indication: Indication = TouchIndication(
                shape = itemShape,
                color = ArecaTheme.colors.accentBase,
            ),
        ): FloatingTabBarStyle = FloatingTabBarStyle(
            bg = bg,
            content = content,
            contentDisabled = contentDisabled,
            shape = shape,
            itemShape = itemShape,
            indication = indication,
        )
    }
}

/** Floating TabBar visual treatment supplied by the active theme. */
public val LocalFloatingTabBarStyle: ProvidableCompositionLocal<FloatingTabBarStyle?> =
    compositionLocalOf { null }
