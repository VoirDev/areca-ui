package dev.voir.areca.foundation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import dev.voir.areca.theme.ArecaTheme

/**
 * Metrics of a [PageIndicator].
 *
 * Every mark is [dot] tall. The mark for the page on screen stretches to [currentLength] wide, so
 * the current page reads by shape as well as by color.
 *
 * @property dot Diameter of a mark for a page that is not on screen, and the height of every mark.
 * @property currentLength Width of the mark for the page on screen.
 * @property spacing Gap between neighbouring marks.
 * @property shape Shape of every mark.
 */
@Immutable
public data class PageIndicatorSize(
    public val dot: Dp,
    public val currentLength: Dp,
    public val spacing: Dp,
    public val shape: Shape,
) {
    /** The theme's default size, and a builder for overriding it. */
    public companion object {
        /** Theme default: small enough to sit under a hero without competing with its button. */
        public val Default: PageIndicatorSize
            @Composable
            get() = default()

        /**
         * Builds a size from the current theme.
         *
         * @param dot Diameter of a mark for a page that is not on screen.
         * @param currentLength Width of the mark for the page on screen.
         * @param spacing Gap between neighbouring marks.
         * @param shape Shape of every mark.
         * @return Size for an indicator.
         */
        @Composable
        public fun default(
            dot: Dp = ArecaTheme.spacing.sm,
            currentLength: Dp = ArecaTheme.spacing.xl2,
            spacing: Dp = ArecaTheme.spacing.sm,
            shape: Shape = ArecaTheme.shapes.full,
        ): PageIndicatorSize = PageIndicatorSize(
            dot = dot,
            currentLength = currentLength,
            spacing = spacing,
            shape = shape,
        )
    }
}

/** Default [PageIndicatorSize] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalPageIndicatorSize: ProvidableCompositionLocal<PageIndicatorSize?> = compositionLocalOf { null }
