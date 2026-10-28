package dev.voir.areca.foundation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import dev.voir.areca.theme.ArecaTheme

/**
 * Metrics of a [PageCounter].
 *
 * @property padding Space between the capsule's edge and the count.
 * @property textStyle Type of the count. Its figures are drawn tabular whatever this says, so the
 *   capsule keeps its width as the count changes.
 * @property shape Shape of the capsule.
 */
@Immutable
public data class PageCounterSize(
    public val padding: PaddingValues,
    public val textStyle: TextStyle,
    public val shape: Shape,
) {
    /** The theme's default size, and a builder for overriding it. */
    public companion object {
        /** Theme default: a small capsule that sits in a picture's corner without covering it. */
        public val Default: PageCounterSize
            @Composable
            get() = default()

        /**
         * Builds a size from the current theme.
         *
         * @param padding Space between the capsule's edge and the count.
         * @param textStyle Type of the count.
         * @param shape Shape of the capsule.
         * @return Size for a counter.
         */
        @Composable
        public fun default(
            padding: PaddingValues = PaddingValues(
                horizontal = ArecaTheme.spacing.md,
                vertical = ArecaTheme.spacing.xs2,
            ),
            textStyle: TextStyle = ArecaTheme.typography.labelSmall,
            shape: Shape = ArecaTheme.shapes.full,
        ): PageCounterSize = PageCounterSize(padding = padding, textStyle = textStyle, shape = shape)
    }
}

/** Default [PageCounterSize] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalPageCounterSize: ProvidableCompositionLocal<PageCounterSize?> = compositionLocalOf { null }
