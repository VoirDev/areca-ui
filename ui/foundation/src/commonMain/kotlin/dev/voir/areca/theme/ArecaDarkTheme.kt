package dev.voir.areca.theme

import androidx.compose.runtime.Composable

/**
 * Puts [content] in the dark theme whatever the theme around it, for a region that is always dark: a
 * photo editor, a media viewer, a camera.
 *
 * Unlike a nested `ArecaThemeProvider(darkTheme = true)`, which starts from the defaults, it keeps
 * everything the application set on the theme around it — palette, shapes, spacing, elevation,
 * motion and type — and changes only the direction: the colors become [ArecaTheme.darkColors], and
 * component defaults are resolved for them again.
 *
 * A theme that is already dark is left as it is, with any component defaults the application set in
 * it.
 *
 * @param content Content composed in the dark theme.
 */
@Composable
public fun ArecaDarkTheme(content: @Composable () -> Unit) {
    if (ArecaTheme.isDark) {
        content()
        return
    }
    ArecaThemeProvider(
        darkTheme = true,
        baseColors = ArecaTheme.baseColors,
        colors = ArecaTheme.darkColors,
        radius = ArecaTheme.radius,
        shapes = ArecaTheme.shapes,
        spacing = ArecaTheme.spacing,
        elevation = ArecaTheme.elevation,
        motion = ArecaTheme.motion,
        fonts = ArecaTheme.fonts,
        baseTypography = ArecaTheme.baseTypography,
        typography = ArecaTheme.typography,
        content = content,
    )
}
