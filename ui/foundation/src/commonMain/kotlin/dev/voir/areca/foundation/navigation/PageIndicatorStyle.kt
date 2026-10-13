package dev.voir.areca.foundation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import dev.voir.areca.theme.ArecaTheme

/**
 * Colors of a [PageIndicator].
 *
 * @property current Fill of the mark for the page on screen.
 * @property other Fill of the marks for every other page.
 */
@Immutable
public data class PageIndicatorStyle(
    public val current: Color,
    public val other: Color,
) {
    /** The theme's default style, and a builder for overriding it. */
    public companion object {
        /** Theme default. */
        public val Default: PageIndicatorStyle
            @Composable
            get() = default()

        /**
         * Builds a style from the current theme.
         *
         * The other pages take the strong border grey rather than the muted fill: the marks are
         * small, and at the muted step they vanish on a white page.
         *
         * @param current Fill of the mark for the page on screen.
         * @param other Fill of the marks for every other page.
         * @return Style for an indicator.
         */
        @Composable
        public fun default(
            current: Color = ArecaTheme.colors.accentBase,
            other: Color = ArecaTheme.colors.borderStrong,
        ): PageIndicatorStyle = PageIndicatorStyle(current = current, other = other)
    }
}

/** Default [PageIndicatorStyle] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalPageIndicatorStyle: ProvidableCompositionLocal<PageIndicatorStyle?> = compositionLocalOf { null }
