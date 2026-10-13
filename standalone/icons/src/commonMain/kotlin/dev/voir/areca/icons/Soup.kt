package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Soup icon.
 *
 * Converted from Lucide `soup` (ISC).
 */
public val ArecaIcons.Soup: ImageVector
    get() = cachedSoup ?: ImageVector.Builder(
        name = "Soup",
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
            moveTo(11.248f, 3f)
            curveTo(11.521f, 3.1f, 12.056f, 3.53f, 11.995f, 4.36f)
            curveTo(11.945f, 5.19f, 11.057f, 5.56f, 11.006f, 6.38f)
            curveTo(10.946f, 7.16f, 11.339f, 7.62f, 11.733f, 8f)
            moveTo(16.252f, 3f)
            curveTo(16.52f, 3.1f, 17.046f, 3.53f, 16.997f, 4.36f)
            curveTo(16.937f, 5.19f, 16.074f, 5.56f, 16.004f, 6.38f)
            curveTo(15.954f, 7.16f, 16.342f, 7.62f, 16.729f, 8f)
            moveTo(19.5f, 12f)
            lineTo(22f, 6f)
            moveTo(4f, 12f)
            arcTo(1f, 1f, 0f, false, false, 3.01f, 13.133f)
            arcTo(9f, 9f, 0f, false, false, 12f, 21f)
            arcTo(9f, 9f, 0f, false, false, 20.99f, 13.133f)
            arcTo(1f, 1f, 0f, false, false, 20f, 12f)
            close()
            moveTo(6.252f, 3f)
            curveTo(6.52f, 3.1f, 7.046f, 3.53f, 6.997f, 4.36f)
            curveTo(6.937f, 5.19f, 6.074f, 5.56f, 6.004f, 6.38f)
            curveTo(5.954f, 7.16f, 6.342f, 7.62f, 6.739f, 8f)
            moveTo(7f, 21f)
            horizontalLineTo(17f)
        }
    }.build().also { cachedSoup = it }

/** `Soup` vector, built on first access and reused afterwards. */
private var cachedSoup: ImageVector? = null
