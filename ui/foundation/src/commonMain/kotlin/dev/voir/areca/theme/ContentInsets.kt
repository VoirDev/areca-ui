package dev.voir.areca.theme

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Applies the theme's horizontal screen gutter.
 *
 * Use this instead of `padding(horizontal = ArecaTheme.spacing.horizontalContentInsets)` so the
 * gutter stays one decision across the app.
 *
 * @return The receiver, padded horizontally by the gutter.
 */
@Composable
public fun Modifier.horizontalContentInsets(): Modifier = padding(
    horizontal = ArecaTheme.spacing.horizontalContentInsets,
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
    horizontal = ArecaTheme.spacing.horizontalContentInsets,
    vertical = ArecaTheme.spacing.verticalContentInsets,
)
