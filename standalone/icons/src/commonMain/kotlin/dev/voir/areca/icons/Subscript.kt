package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Subscript icon.
 *
 * Converted from Lucide `subscript` (ISC).
 */
public val ArecaIcons.Subscript: ImageVector
    get() = cachedSubscript ?: ImageVector.Builder(
        name = "Subscript",
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
            moveTo(4f, 5f)
            lineTo(12f, 13f)
            moveTo(12f, 5f)
            lineTo(4f, 13f)
            moveTo(20f, 19f)
            horizontalLineTo(16f)
            curveTo(16f, 17.5f, 16.44f, 17f, 17.5f, 16.5f)
            reflectiveCurveTo(20f, 15.33f, 20f, 14f)
            curveTo(20f, 13.53f, 19.83f, 13.07f, 19.52f, 12.71f)
            arcTo(2.11f, 2.11f, 0f, false, false, 16.9f, 12.27f)
            curveTo(16.48f, 12.51f, 16.16f, 12.89f, 16f, 13.34f)
        }
    }.build().also { cachedSubscript = it }

/** `Subscript` vector, built on first access and reused afterwards. */
private var cachedSubscript: ImageVector? = null
