package dev.voir.areca.foundation.tabbar

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.theme.ArecaTheme

/**
 * Defines the geometry and active color palette of the shared [FloatingTabBar] indicator.
 *
 * The selected content color belongs to the indicator because filled indicators need a contrast
 * palette while underline indicators normally retain an accent foreground.
 */
@Immutable
public sealed interface FloatingTabBarIndicatorStyle {
    /** Color drawn by the shared indicator. */
    public val color: Color

    /** Content color provided to the selected tab. */
    public val contentSelected: Color

    /** Shape used to clip the indicator. */
    public val shape: Shape

    /**
     * Fixed-size line centered beneath the selected tab.
     *
     * @property color Color of the underline.
     * @property contentSelected Content color of the selected tab.
     * @property shape Shape of the underline ends.
     * @property width Fixed underline width.
     * @property height Fixed underline height.
     * @property bottomPadding Space between the underline and the floating container's bottom edge.
     */
    @Immutable
    public data class Underline(
        override val color: Color,
        override val contentSelected: Color,
        override val shape: Shape,
        public val width: Dp,
        public val height: Dp,
        public val bottomPadding: Dp,
    ) : FloatingTabBarIndicatorStyle

    /**
     * Rounded surface that occupies the selected tab cell after applying [padding].
     *
     * @property color Background color of the filled indicator.
     * @property contentSelected Contrast content color of the selected tab.
     * @property shape Shape of the filled indicator.
     * @property padding Insets between the indicator and its tab cell.
     */
    @Immutable
    public data class Fill(
        override val color: Color,
        override val contentSelected: Color,
        override val shape: Shape,
        public val padding: PaddingValues,
    ) : FloatingTabBarIndicatorStyle

    /** Theme-backed presets and builders. */
    public companion object {
        /**
         * Creates the default underline indicator treatment.
         *
         * @param color Color of the underline.
         * @param contentSelected Content color of the selected tab.
         * @param shape Shape of the underline ends.
         * @param width Fixed underline width.
         * @param height Fixed underline height.
         * @param bottomPadding Space above the container's bottom edge.
         * @return An underline indicator style using the supplied geometry and colors.
         */
        @Composable
        public fun underline(
            color: Color = ArecaTheme.colors.accentBase,
            contentSelected: Color = ArecaTheme.colors.accentBase,
            shape: Shape = CircleShape,
            width: Dp = 32.dp,
            height: Dp = 3.dp,
            bottomPadding: Dp = ArecaTheme.spacing.xs,
        ): Underline = Underline(
            color = color,
            contentSelected = contentSelected,
            shape = shape,
            width = width,
            height = height,
            bottomPadding = bottomPadding,
        )

        /**
         * Creates the default filled indicator treatment.
         *
         * @param color Background color of the filled indicator.
         * @param contentSelected Contrast content color of the selected tab.
         * @param shape Shape of the filled indicator.
         * @param padding Insets between the indicator and its tab cell.
         * @return A filled indicator style using the supplied geometry and colors.
         */
        @Composable
        public fun fill(
            color: Color = ArecaTheme.colors.accentBase,
            contentSelected: Color = ArecaTheme.colors.fgOnFill,
            shape: Shape = ArecaTheme.shapes.xl,
            padding: PaddingValues = PaddingValues(ArecaTheme.spacing.xs),
        ): Fill = Fill(
            color = color,
            contentSelected = contentSelected,
            shape = shape,
            padding = padding,
        )
    }
}
