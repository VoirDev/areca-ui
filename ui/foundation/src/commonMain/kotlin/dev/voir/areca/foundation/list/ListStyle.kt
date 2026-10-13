package dev.voir.areca.foundation.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.theme.ArecaTheme

/**
 * Colors of a list's own surface, as opposed to its rows'.
 *
 * A list is a container that happens to hold rows: it can have a background, a border and hairlines
 * between its rows, none of which belong to any one row. [ListItemStyle] is the row's half.
 *
 * [Color.Unspecified] means "draw nothing" rather than "draw transparent", so the default list adds
 * no layer at all over whatever it is placed on.
 *
 * @property bg Background behind every row.
 * @property border Border around the whole list.
 * @property separator Hairline drawn at each boundary between two rows.
 */
@Immutable
public data class ListStyle(
    public val bg: Color,
    public val border: Color,
    public val separator: Color,
) {
    /** Theme-backed presets and builders. */
    public companion object {
        /** Theme default: no surface of its own, with base-border hairlines when separated. */
        public val Default: ListStyle
            @Composable
            get() = default()

        /**
         * Builds a style from the current theme.
         *
         * @param bg Background behind every row.
         * @param border Border around the whole list.
         * @param separator Hairline drawn between rows.
         * @return A list style containing the supplied colors.
         */
        @Composable
        public fun default(
            bg: Color = Color.Unspecified,
            border: Color = Color.Unspecified,
            separator: Color = ArecaTheme.colors.borderBase,
        ): ListStyle = ListStyle(bg = bg, border = border, separator = separator)
    }
}

/**
 * Metrics of a list's own surface.
 *
 * [separatorInset] exists because a hairline that runs to the very edge of a rounded list crosses
 * its corner. Insetting it stops the line short of the curve.
 *
 * @property shape Shape of the list's surface; the first and last row draw their states inside its
 *   corners.
 * @property borderWidth Width of the list's border. `0.dp` draws none.
 * @property separatorThickness Thickness of the hairlines between rows.
 * @property separatorInset Distance between each hairline and both list edges.
 */
@Immutable
public data class ListSize(
    public val shape: Shape,
    public val borderWidth: Dp,
    public val separatorThickness: Dp,
    public val separatorInset: Dp,
) {
    /** Theme-backed presets and builders. */
    public companion object {
        /** Theme default: square and borderless, with hairline separators flush to the edges. */
        public val Default: ListSize
            @Composable
            get() = default()

        /**
         * Builds a size from the current theme.
         *
         * @param shape Shape of the list's surface.
         * @param borderWidth Width of the list's border.
         * @param separatorThickness Thickness of the hairlines between rows.
         * @param separatorInset Distance between each hairline and both list edges.
         * @return A list size containing the supplied metrics.
         */
        @Composable
        public fun default(
            shape: Shape = RectangleShape,
            borderWidth: Dp = 0.dp,
            separatorThickness: Dp = 1.dp,
            separatorInset: Dp = 0.dp,
        ): ListSize = ListSize(
            shape = shape,
            borderWidth = borderWidth,
            separatorThickness = separatorThickness,
            separatorInset = separatorInset,
        )
    }
}

/** Default [ListStyle] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalListStyle: ProvidableCompositionLocal<ListStyle?> = compositionLocalOf { null }

/** Default [ListSize] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalListSize: ProvidableCompositionLocal<ListSize?> = compositionLocalOf { null }
