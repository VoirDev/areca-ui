package dev.voir.areca.foundation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import dev.voir.areca.theme.ArecaTheme

/**
 * Colors of a [PageCounter].
 *
 * @property bg Fill of the capsule behind the count; [Color.Transparent] for the count alone.
 * @property content Color of the count.
 */
@Immutable
public data class PageCounterStyle(
    public val bg: Color,
    public val content: Color,
) {
    /** The theme's default style, its presets, and a builder for overriding it. */
    public companion object {
        /** Theme default: the count on a quiet capsule, for a counter on the page. */
        public val Default: PageCounterStyle
            @Composable
            get() = default()

        /**
         * Builds a style from the current theme.
         *
         * @param bg Fill of the capsule behind the count.
         * @param content Color of the count.
         * @return Style for a counter.
         */
        @Composable
        public fun default(
            bg: Color = ArecaTheme.colors.bgSubtle,
            content: Color = ArecaTheme.colors.fgBase,
        ): PageCounterStyle = PageCounterStyle(bg = bg, content = content)

        /**
         * A counter laid over a picture: white on a translucent dark capsule, readable on any
         * photograph whatever the theme.
         *
         * @param bg Fill of the capsule behind the count.
         * @param content Color of the count.
         * @return Style for a counter over a picture.
         */
        @Composable
        public fun onImage(
            bg: Color = ArecaTheme.baseColors.baseBlack.copy(alpha = ON_IMAGE_BG_ALPHA),
            content: Color = ArecaTheme.baseColors.baseWhite,
        ): PageCounterStyle = PageCounterStyle(bg = bg, content = content)
    }
}

/** Default [PageCounterStyle] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalPageCounterStyle: ProvidableCompositionLocal<PageCounterStyle?> = compositionLocalOf { null }

/** How dark the capsule of a counter on a picture is: dark enough for white text on a white shot. */
private const val ON_IMAGE_BG_ALPHA = 0.6f
