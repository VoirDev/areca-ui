package dev.voir.areca.foundation.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.icon.FeaturedIcon
import dev.voir.areca.foundation.icon.FeaturedIconSize
import dev.voir.areca.foundation.icon.FeaturedIconStyle
import dev.voir.areca.foundation.list.LocalListItemState
import dev.voir.areca.theme.ArecaTheme

/** Opacity of a tile in a disabled row; its color would otherwise outshout the shaded text. */
private const val DISABLED_TILE_ALPHA = 0.4f

/**
 * A glyph on a colored tile, for the leading slot of a settings row.
 *
 * A bare glyph in the leading slot takes the row's tone, which is right for a row of actions. A tile
 * gives each row its own color instead — a green phone, an orange handle, a blue birthday — which is
 * how a long screen of unrelated settings stays scannable: the eye finds the color before it reads.
 * Pick one color per concept and keep it everywhere that concept appears.
 *
 * Neutral by default, like the rest of a settings list; give a tile a color when the color carries
 * meaning, and the brand only where the screen needs an accent.
 *
 * The tile follows its row: it fades with a disabled row rather than staying at full color. It is
 * always solid; for a softer tile pass [FeaturedIconStyle.soft] to the overload taking a style.
 *
 * @param imageVector Glyph to draw on the tile.
 * @param modifier Modifier applied to the tile.
 * @param color Color family of the tile.
 * @param contentDescription Label for assistive technology, or `null` when the row's title already
 * says what the glyph means.
 * @param shape Shape of the tile.
 * @param size Size of the tile and its glyph; defaults to the list's.
 */
@Composable
public fun SettingsRowIcon(
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
    color: ActionColor = ActionColor.Neutral,
    contentDescription: String? = null,
    shape: Shape = ArecaTheme.shapes.tile,
    size: FeaturedIconSize = SettingsTheme.sizes.iconTile,
) {
    SettingsRowIcon(
        imageVector = imageVector,
        style = FeaturedIconStyle.solid(color),
        modifier = modifier,
        contentDescription = contentDescription,
        shape = shape,
        size = size,
    )
}

/**
 * A glyph on a tile of the caller's own colours, for the leading slot of a settings row.
 *
 * For a tile whose colour is not one of the theme's color families — a decorative hue per subject,
 * say, so a menu is scanned by colour without a green tile reading as "all fine". Everything else
 * matches the tile above: the same shape, the same size, the same fade in a disabled row, so a
 * tile of either kind lines up with the other.
 *
 * @param imageVector Glyph to draw on the tile.
 * @param style Fill and glyph colours of the tile.
 * @param modifier Modifier applied to the tile.
 * @param contentDescription Label for assistive technology, or `null` when the row's title already
 * says what the glyph means.
 * @param shape Shape of the tile.
 * @param size Size of the tile and its glyph; defaults to the list's.
 */
@Composable
public fun SettingsRowIcon(
    imageVector: ImageVector,
    style: FeaturedIconStyle,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    shape: Shape = ArecaTheme.shapes.tile,
    size: FeaturedIconSize = SettingsTheme.sizes.iconTile,
) {
    val enabled = LocalListItemState.current.enabled

    FeaturedIcon(
        imageVector = imageVector,
        contentDescription = contentDescription,
        modifier = modifier.alpha(if (enabled) 1f else DISABLED_TILE_ALPHA),
        style = style,
        size = size,
        shape = shape,
    )
}

/**
 * Any visual — a flag, a photo, a brand mark — at the size and in the shape of an icon tile, for
 * the leading slot of a settings row.
 *
 * Every leading visual in a section should be one size and one shape, or the column of visuals
 * stops reading as a column and the text beside them starts at a ragged edge. Glyphs go on a
 * [SettingsRowIcon] tile; anything else goes in this box, which crops it to the same tile. Pass
 * [ArecaTheme.shapes.full][dev.voir.areca.theme.ArecaShapes.full] for a person, whose picture is round
 * wherever it appears — but keep the size, so the text still starts where the other rows' does.
 *
 * Like the icon tile, it fades with a disabled row.
 *
 * @param modifier Modifier applied to the tile.
 * @param shape Shape the visual is cropped to.
 * @param size Width and height of the tile; defaults to the list's icon tile.
 * @param content The visual, centred and cropped to the tile.
 */
@Composable
public fun SettingsRowTile(
    modifier: Modifier = Modifier,
    shape: Shape = ArecaTheme.shapes.tile,
    size: Dp = SettingsTheme.sizes.iconTile.container,
    content: @Composable BoxScope.() -> Unit,
) {
    val enabled = LocalListItemState.current.enabled

    Box(
        modifier = modifier
            .size(size)
            .alpha(if (enabled) 1f else DISABLED_TILE_ALPHA)
            // A visual, never a control, so cropping it costs no focus ring.
            .clip(shape),
        contentAlignment = Alignment.Center,
        content = content,
    )
}
