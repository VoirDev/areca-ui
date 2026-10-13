package dev.voir.areca.foundation.icon

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toolingGraphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.LocalContentColor

/**
 * The square sizes an [Icon] is drawn at.
 *
 * Plain [Dp] values rather than a token type, because an icon has exactly one dimension to pick.
 * The steps match [ChevronSize] and [dev.voir.areca.foundation.list.ListItemSize]'s icon slots, so
 * an icon swapped between components keeps its optical weight.
 *
 * Components normally take their size from [LocalIconSize] instead of naming a step, so an icon
 * inside a small button shrinks with it.
 */
public object IconSize {
    /** 12dp. Inline with small text, such as inside a badge. */
    public val ExtraSmall: Dp = 12.dp

    /** 16dp. The default: inline with body text and inside standard controls. */
    public val Small: Dp = 16.dp

    /** 20dp. Leading icon of a list row or a large control. */
    public val Medium: Dp = 20.dp

    /** 24dp. Standalone icon buttons and top-bar actions. */
    public val Large: Dp = 24.dp

    /** 32dp. Icons that carry a block on their own, outside a [FeaturedIcon]. */
    public val ExtraLarge: Dp = 32.dp
}

/**
 * Size an [Icon] uses when it is not given one.
 *
 * A container overrides it for its subtree so icons scale with the control around them. Seeded by
 * [dev.voir.areca.theme.ArecaThemeProvider], and defaulted here so an icon renders without one.
 */
public val LocalIconSize: ProvidableCompositionLocal<Dp> = compositionLocalOf { IconSize.Small }

/**
 * Draws [painter] at a square size, tinted to the surrounding content color.
 *
 * The tint is the point: foundation icons are drawn in a single flat color so that one drawing works
 * on every surface, and [Icon] recolors it to match the text it sits beside. Pass
 * [Color.Unspecified] to keep the painter's own colors, which is what a logo or an illustration
 * wants.
 *
 * [contentDescription] is the accessibility label. Pass `null` when the icon repeats something
 * already said next to it — a chevron beside "Settings", a search glass inside a labelled field —
 * so a screen reader does not read the same thing twice.
 *
 * @param painter Painter to draw.
 * @param contentDescription Label read by assistive technology, or `null` when decorative.
 * @param modifier Modifier applied to the icon box.
 * @param tint Color applied to the painter, or [Color.Unspecified] to keep its own.
 * @param size Square size to draw at.
 */
@Composable
public fun Icon(
    painter: Painter,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current,
    size: Dp = LocalIconSize.current,
) {
    val colorFilter = remember(tint) {
        if (tint == Color.Unspecified) null else ColorFilter.tint(tint)
    }
    val semantics = if (contentDescription != null) {
        Modifier.semantics {
            this.contentDescription = contentDescription
            this.role = Role.Image
        }
    } else {
        Modifier
    }

    Box(
        modifier
            .toolingGraphicsLayer()
            .size(size)
            .paint(painter, colorFilter = colorFilter, contentScale = ContentScale.Fit)
            .then(semantics)
    )
}

/**
 * Draws [imageVector] at a square size, tinted to the surrounding content color.
 *
 * The overload to reach for with `ArecaIcons` from `dev.voir.areca:icons`, or any other vector set.
 *
 * @param imageVector Vector to draw.
 * @param contentDescription Label read by assistive technology, or `null` when decorative.
 * @param modifier Modifier applied to the icon box.
 * @param tint Color applied to the vector, or [Color.Unspecified] to keep its own.
 * @param size Square size to draw at.
 */
@Composable
public fun Icon(
    imageVector: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current,
    size: Dp = LocalIconSize.current,
) {
    Icon(
        painter = rememberVectorPainter(imageVector),
        contentDescription = contentDescription,
        modifier = modifier,
        tint = tint,
        size = size,
    )
}

/**
 * Draws [bitmap] at a square size, tinted to the surrounding content color.
 *
 * A bitmap does not scale the way a vector does, so supply one at least as large as [size] at the
 * densest display it will appear on.
 *
 * @param bitmap Bitmap to draw.
 * @param contentDescription Label read by assistive technology, or `null` when decorative.
 * @param modifier Modifier applied to the icon box.
 * @param tint Color applied to the bitmap, or [Color.Unspecified] to keep its own.
 * @param size Square size to draw at.
 */
@Composable
public fun Icon(
    bitmap: ImageBitmap,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current,
    size: Dp = LocalIconSize.current,
) {
    val painter = remember(bitmap) { BitmapPainter(bitmap) }
    Icon(
        painter = painter,
        contentDescription = contentDescription,
        modifier = modifier,
        tint = tint,
        size = size,
    )
}
