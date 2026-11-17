package dev.voir.areca.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Categorical colours: one per kind of thing, for the tiles that tell a long list of unrelated rows
 * apart — a settings menu, categories of spending, a set of tags.
 *
 * Unlike [ArecaColors], whose families mean something (brand, danger, success), a hue means only
 * "this is a different thing": pick one per concept and keep it wherever that concept appears. Every
 * hue carries a white glyph at 3:1 or more, so a solid tile reads in both themes; the soft tile
 * mixes it into the surface. Read them with [ArecaTheme.hues], draw them with
 * `FeaturedIconStyle.solidHue` / `softHue` or `SettingsRowIcon(hue = …)`, and replace the set with
 * `ArecaThemeProvider(hues = …)`.
 */
@Immutable
public data class ArecaHues(
    public val blue: Color = Color(0xFF0A69FA),
    public val indigo: Color = Color(0xFF4F5BD5),
    public val purple: Color = Color(0xFF7C3AED),
    public val pink: Color = Color(0xFFD6336C),
    public val red: Color = Color(0xFFDB340B),
    public val orange: Color = Color(0xFFD9480F),
    public val yellow: Color = Color(0xFFB26205),
    public val green: Color = Color(0xFF008557),
    public val teal: Color = Color(0xFF0C7C8C),
    public val gray: Color = Color(0xFF727679),
) {
    /** Every hue, in a stable order, for assigning one to each item of a set by its index. */
    public val all: List<Color>
        get() = listOf(blue, indigo, purple, pink, red, orange, yellow, green, teal, gray)
}

internal val LocalHues = staticCompositionLocalOf { ArecaHues() }
