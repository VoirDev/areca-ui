package dev.voir.areca.theme

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import dev.voir.areca.window.adaptive

/**
 * The horizontal screen gutter for the width class in scope: 16dp on a compact window, 24dp on a
 * medium one and 32dp from expanded up, unless the theme's [ArecaSpacing] says otherwise. Shared
 * with every part that lines its edges up with the screen's, such as a settings section.
 */
internal val horizontalGutter: Dp
    @Composable get() {
        val spacing = ArecaTheme.spacing
        return adaptive(
            compact = spacing.horizontalContentInsets,
            medium = spacing.mediumHorizontalContentInsets,
            expanded = spacing.expandedHorizontalContentInsets,
        )
    }

/**
 * Applies the theme's horizontal screen gutter, wider on wider windows.
 *
 * Use this instead of `padding(horizontal = ArecaTheme.spacing.horizontalContentInsets)` so the
 * gutter stays one decision across the app and widens with the window. Inside a
 * [dev.voir.areca.window.WindowClassBox] it follows the box.
 *
 * @return The receiver, padded horizontally by the gutter.
 */
@Composable
public fun Modifier.horizontalContentInsets(): Modifier = padding(
    horizontal = horizontalGutter,
)

/**
 * Applies the theme's vertical screen gutter.
 *
 * Use this instead of `padding(vertical = ArecaTheme.spacing.verticalContentInsets)` so the gutter
 * stays one decision across the app.
 *
 * @return The receiver, padded vertically by the gutter.
 */
@Composable
public fun Modifier.verticalContentInsets(): Modifier = padding(
    vertical = ArecaTheme.spacing.verticalContentInsets,
)

/**
 * Applies both screen gutters, as a full-screen container normally wants.
 *
 * @return The receiver, padded by the horizontal and the vertical gutter.
 */
@Composable
public fun Modifier.contentInsets(): Modifier = padding(
    horizontal = horizontalGutter,
    vertical = ArecaTheme.spacing.verticalContentInsets,
)
