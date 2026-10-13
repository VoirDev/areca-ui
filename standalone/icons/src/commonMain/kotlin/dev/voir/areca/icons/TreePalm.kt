package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Tree palm icon.
 *
 * Converted from Lucide `tree-palm` (ISC).
 */
public val ArecaIcons.TreePalm: ImageVector
    get() = cachedTreePalm ?: ImageVector.Builder(
        name = "TreePalm",
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
            moveTo(13f, 8f)
            curveTo(13f, 5.24f, 10.54f, 3f, 7.5f, 3f)
            reflectiveCurveTo(2f, 5.24f, 2f, 8f)
            horizontalLineTo(4f)
            lineTo(5f, 7f)
            lineTo(6f, 8f)
            horizontalLineTo(10f)
            moveTo(13f, 7.14f)
            arcTo(5.82f, 5.82f, 0f, false, true, 16.5f, 6f)
            curveTo(19.54f, 6f, 22f, 8.24f, 22f, 11f)
            horizontalLineTo(19f)
            lineTo(18f, 10f)
            lineTo(17f, 11f)
            horizontalLineTo(14f)
            moveTo(5.89f, 9.71f)
            curveTo(3.74f, 11.86f, 3.59f, 15.18f, 5.54f, 17.14f)
            lineTo(9.78f, 12.89f)
            lineTo(10.48f, 12.19f)
            lineTo(11.19f, 11.48f)
            lineTo(13.31f, 9.36f)
            curveTo(11.36f, 7.4f, 8.04f, 7.56f, 5.89f, 9.71f)
            moveTo(11f, 15.5f)
            curveTo(11.5f, 18f, 10.83f, 20f, 10f, 22f)
            horizontalLineTo(14f)
            curveTo(16f, 16.5f, 13.5f, 10f, 13f, 8f)
        }
    }.build().also { cachedTreePalm = it }

/** `TreePalm` vector, built on first access and reused afterwards. */
private var cachedTreePalm: ImageVector? = null
