package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Plane icon.
 *
 * Converted from Lucide `plane` (ISC).
 */
public val ArecaIcons.Plane: ImageVector
    get() = cachedPlane ?: ImageVector.Builder(
        name = "Plane",
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
            moveTo(17.8f, 19.2f)
            lineTo(16f, 11f)
            lineTo(19.5f, 7.5f)
            curveTo(21f, 6f, 21.5f, 4f, 21f, 3f)
            curveTo(20f, 2.5f, 18f, 3f, 16.5f, 4.5f)
            lineTo(13f, 8f)
            lineTo(4.8f, 6.2f)
            curveTo(4.3f, 6.1f, 3.9f, 6.3f, 3.7f, 6.7f)
            lineTo(3.4f, 7.2f)
            curveTo(3.2f, 7.7f, 3.3f, 8.2f, 3.7f, 8.5f)
            lineTo(9f, 12f)
            lineTo(7f, 15f)
            horizontalLineTo(4f)
            lineTo(3f, 16f)
            lineTo(6f, 18f)
            lineTo(8f, 21f)
            lineTo(9f, 20f)
            verticalLineTo(17f)
            lineTo(12f, 15f)
            lineTo(15.5f, 20.3f)
            curveTo(15.8f, 20.7f, 16.3f, 20.8f, 16.8f, 20.6f)
            lineTo(17.3f, 20.4f)
            curveTo(17.7f, 20.1f, 17.9f, 19.7f, 17.8f, 19.2f)
            close()
        }
    }.build().also { cachedPlane = it }

/** `Plane` vector, built on first access and reused afterwards. */
private var cachedPlane: ImageVector? = null
