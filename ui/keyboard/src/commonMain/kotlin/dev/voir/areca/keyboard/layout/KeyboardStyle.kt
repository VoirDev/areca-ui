package dev.voir.areca.keyboard.layout

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.keyboard.key.KeyStyle
import dev.voir.areca.theme.ArecaTheme

/**
 * Colors, shape, and lift of the surface a keyboard is drawn on, and the look of its keys.
 *
 * The default rounds only the top corners: the surface is docked to the bottom edge, and rounding
 * the bottom two would leave slivers of the screen showing beneath it.
 *
 * @property bg Fill behind the keys.
 * @property content Color provided to the header and footer slots, so text in them needs no color of
 *   its own. Keys take their colors from [keys], not from here.
 * @property border Hairline along the surface's whole outline. [Color.Transparent] draws none.
 * @property borderWidth Width of that hairline, and of [divider].
 * @property shape Shape of the surface, its border, and its shadow.
 * @property elevation How far the surface floats above the content it covers.
 * @property divider Hairline along the surface's top edge only, separating a docked keyboard from
 *   the content above it. [Color.Transparent] draws none.
 * @property keys Look of each kind of key.
 */
@Immutable
public data class KeyboardStyle(
    public val bg: Color,
    public val content: Color,
    public val border: Color,
    public val borderWidth: Dp,
    public val shape: Shape,
    public val elevation: Dp,
    public val divider: Color = Color.Transparent,
    public val keys: KeyStyle,
) {
    /** Theme-backed [KeyboardStyle] presets and their builders. */
    public companion object {
        /** Theme default: a raised, top-rounded surface. */
        public val Default: KeyboardStyle
            @Composable
            get() = default()

        /**
         * Builds a keyboard style from the current theme.
         *
         * @param bg Fill behind the keys.
         * @param content Color provided to the header and footer slots.
         * @param border Hairline along the surface's whole outline.
         * @param borderWidth Width of that hairline, and of [divider].
         * @param shape Shape of the surface. Defaults to top-rounded, since the surface is docked.
         * @param elevation How far the surface floats above the content it covers.
         * @param divider Hairline along the surface's top edge only.
         * @param keys Look of each kind of key.
         * @return A keyboard style containing the supplied values.
         */
        @Composable
        public fun default(
            bg: Color = ArecaTheme.colors.bgSurface,
            content: Color = ArecaTheme.colors.fgBase,
            border: Color = Color.Transparent,
            borderWidth: Dp = 1.dp,
            shape: Shape = RoundedCornerShape(
                topStart = ArecaTheme.radius.xl,
                topEnd = ArecaTheme.radius.xl,
            ),
            elevation: Dp = ArecaTheme.elevation.lg,
            divider: Color = Color.Transparent,
            keys: KeyStyle = KeyStyle.Default,
        ): KeyboardStyle = KeyboardStyle(
            bg = bg,
            content = content,
            border = border,
            borderWidth = borderWidth,
            shape = shape,
            elevation = elevation,
            divider = divider,
            keys = keys,
        )

        /**
         * A surface flush with the screen behind it: no shadow, no rounding, a hairline on top.
         *
         * For a keyboard that is permanently docked rather than one that slides over content, where
         * a floating surface would read as a layer the user cannot dismiss.
         *
         * @param bg Fill behind the keys.
         * @param border Hairline along the top edge, separating the keyboard from the content above
         *   it. Drawn as the style's [divider]; the sides and bottom stay unlined.
         * @param keys Look of each kind of key.
         * @return A flat keyboard style.
         */
        @Composable
        public fun flat(
            bg: Color = ArecaTheme.colors.bgSurface,
            border: Color = ArecaTheme.colors.borderSubtle,
            keys: KeyStyle = KeyStyle.Default,
        ): KeyboardStyle = default(
            bg = bg,
            shape = RectangleShape,
            elevation = ArecaTheme.elevation.none,
            divider = border,
            keys = keys,
        )
    }
}

/**
 * Default [KeyboardStyle] in scope, or `null` to use [KeyboardStyle.Default].
 *
 * Unlike the foundation's component defaults this is **not** seeded by
 * [dev.voir.areca.theme.ArecaThemeProvider]: that provider lives in `:foundation`, and `:foundation`
 * does not depend on `:keyboard`. Wire it yourself around a subtree that wants a different default:
 *
 * ```kotlin
 * CompositionLocalProvider(LocalKeyboardStyle provides KeyboardStyle.flat()) {
 *     // every keyboard in here is docked rather than floating
 * }
 * ```
 */
public val LocalKeyboardStyle: ProvidableCompositionLocal<KeyboardStyle?> =
    compositionLocalOf { null }
