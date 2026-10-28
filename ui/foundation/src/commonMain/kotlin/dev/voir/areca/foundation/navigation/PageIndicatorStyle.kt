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
 * @property capsule Fill of a capsule drawn behind the marks, or `null` for none. Set over a
 *   picture, where marks of any colour vanish on part of it.
 */
@Immutable
public data class PageIndicatorStyle(
    public val current: Color,
    public val other: Color,
    public val capsule: Color? = null,
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
         * @param capsule Fill of a capsule behind the marks, or `null` for none.
         * @return Style for an indicator.
         */
        @Composable
        public fun default(
            current: Color = ArecaTheme.colors.accentBase,
            other: Color = ArecaTheme.colors.borderStrong,
            capsule: Color? = null,
        ): PageIndicatorStyle = PageIndicatorStyle(current = current, other = other, capsule = capsule)

        /**
         * Marks laid over a picture: white, on a translucent dark capsule, so they read on a light
         * product shot and a dark photograph alike, whatever the theme.
         *
         * @param current Fill of the mark for the page on screen.
         * @param other Fill of the marks for every other page.
         * @param capsule Fill of the capsule behind the marks.
         * @return Style for an indicator over a picture.
         */
        @Composable
        public fun onImage(
            current: Color = ArecaTheme.baseColors.baseWhite,
            other: Color = ArecaTheme.baseColors.baseWhite.copy(alpha = ON_IMAGE_OTHER_ALPHA),
            capsule: Color? = ArecaTheme.baseColors.baseBlack.copy(alpha = ON_IMAGE_CAPSULE_ALPHA),
        ): PageIndicatorStyle = PageIndicatorStyle(current = current, other = other, capsule = capsule)
    }
}

/** Default [PageIndicatorStyle] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalPageIndicatorStyle: ProvidableCompositionLocal<PageIndicatorStyle?> = compositionLocalOf { null }

/** How strongly the marks of other pages show over a picture: half, so the current one leads. */
private const val ON_IMAGE_OTHER_ALPHA = 0.5f

/** How dark the capsule under marks on a picture is: enough for white marks on a white shot. */
private const val ON_IMAGE_CAPSULE_ALPHA = 0.32f
