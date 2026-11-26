package dev.voir.areca.foundation.adaptive

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import dev.voir.areca.foundation.list.ListItemColor
import dev.voir.areca.foundation.list.ListItemStyle
import dev.voir.areca.theme.ArecaTheme

/**
 * Colours, shape and lift of an [AdaptiveMenu], and the look of its rows.
 *
 * The card fields — [bg], [border], [shape], [shadowElevation] — apply to the dropdown form; the
 * sheet form takes the sheet's own. The row styles apply to both.
 *
 * @property bg Fill of the card.
 * @property border Outline of the card; `Color.Transparent` for none, so the lift alone sets it
 *   apart from what it covers.
 * @property shape Shape of the card's fill, outline and shadow.
 * @property shadowElevation How far the card floats above what it covers.
 * @property item Look of an ordinary action's row.
 * @property destructiveItem Look of a destructive action's row: the danger ink on its label and its
 *   glyph.
 */
@Immutable
public data class AdaptiveMenuStyle(
    public val bg: Color,
    public val border: Color,
    public val shape: Shape,
    public val shadowElevation: Dp,
    public val item: ListItemStyle,
    public val destructiveItem: ListItemStyle,
) {
    /** The theme's default [AdaptiveMenuStyle], and [default] for building a variation on it. */
    public companion object {
        /** Theme default: a card in the surface shape, lifted, with no outline. */
        public val Default: AdaptiveMenuStyle
            @Composable
            get() = default()

        /**
         * Builds a style from the current theme.
         *
         * @param bg Fill of the card. Defaults to the ground of a sheet, which menus share.
         * @param border Outline of the card. Defaults to none.
         * @param shape Shape of the card. Defaults to `ArecaTheme.shapes.surface`, every card's.
         * @param shadowElevation Lift of the card. Defaults to `ArecaTheme.elevation.md`.
         * @param item Look of an ordinary row. Defaults to a plain neutral row.
         * @param destructiveItem Look of a destructive row. Defaults to a plain danger row.
         * @return Style built from the supplied values.
         */
        @Composable
        public fun default(
            bg: Color = ArecaTheme.surfaces.sheet.page.ground,
            border: Color = Color.Transparent,
            shape: Shape = ArecaTheme.shapes.surface,
            shadowElevation: Dp = ArecaTheme.elevation.md,
            item: ListItemStyle = ListItemStyle.plain(),
            destructiveItem: ListItemStyle = ListItemStyle.plain(color = ListItemColor.Danger),
        ): AdaptiveMenuStyle = AdaptiveMenuStyle(
            bg = bg,
            border = border,
            shape = shape,
            shadowElevation = shadowElevation,
            item = item,
            destructiveItem = destructiveItem,
        )
    }
}

/** The [AdaptiveMenuStyle] an [AdaptiveMenu] takes when none is passed; `null` unless provided. */
public val LocalAdaptiveMenuStyle: ProvidableCompositionLocal<AdaptiveMenuStyle?> =
    compositionLocalOf { null }
