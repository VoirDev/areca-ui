package dev.voir.areca.foundation.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.voir.areca.foundation.list.ListItemSkeleton
import dev.voir.areca.theme.ArecaTheme

/**
 * Placeholder for a settings row whose content has not loaded.
 *
 * Sized from the list's row metrics and its icon tile, so a section of skeletons has the same
 * height as the section that replaces it and nothing below it jumps when it arrives.
 *
 * @param modifier Modifier applied to the placeholder.
 * @param showLeading Whether to reserve an icon tile.
 * @param showSubtitle Whether to draw a second line.
 * @param showTrailing Whether to reserve a trailing control.
 * @param titleWidthFraction Width of the title line, as a fraction of the text column.
 */
@Composable
public fun SettingsRowSkeleton(
    modifier: Modifier = Modifier,
    showLeading: Boolean = false,
    showSubtitle: Boolean = false,
    showTrailing: Boolean = false,
    titleWidthFraction: Float = 0.45f,
) {
    ListItemSkeleton(
        modifier = modifier,
        size = SettingsTheme.sizes.row,
        showLeading = showLeading,
        showSubtitle = showSubtitle,
        showTrailing = showTrailing,
        leadingSize = SettingsTheme.sizes.iconTile.container,
        leadingShape = ArecaTheme.shapes.tile,
        titleWidthFraction = titleWidthFraction,
    )
}
