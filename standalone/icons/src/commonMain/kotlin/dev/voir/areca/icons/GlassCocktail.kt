package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Glass cocktail icon.
 *
 * Converted from Tabler Icons `glass-cocktail` (MIT).
 */
public val ArecaIcons.GlassCocktail: ImageVector
    get() = cachedGlassCocktail ?: ImageVector.Builder(
        name = "GlassCocktail",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
            moveTo(8f, 21f)
            horizontalLineTo(16f)
            moveTo(12f, 15f)
            verticalLineTo(21f)
            moveTo(5f, 5f)
            arcTo(7f, 2f, 0f, true, false, 19f, 5f)
            arcTo(7f, 2f, 0f, true, false, 5f, 5f)
            moveTo(5f, 5f)
            verticalLineTo(5.388f)
            curveTo(5f, 5.82f, 5.126f, 6.241f, 5.362f, 6.594f)
            lineTo(10.362f, 14.103f)
            curveTo(10.995f, 15.054f, 12.242f, 15.286f, 13.147f, 14.62f)
            curveTo(13.338f, 14.479f, 13.505f, 14.304f, 13.638f, 14.103f)
            lineTo(18.638f, 6.594f)
            curveTo(18.874f, 6.241f, 19f, 5.82f, 19f, 5.388f)
            verticalLineTo(5f)
        }
    }.build().also { cachedGlassCocktail = it }

/** `GlassCocktail` vector, built on first access and reused afterwards. */
private var cachedGlassCocktail: ImageVector? = null
