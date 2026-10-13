package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Car icon.
 *
 * Converted from Lucide `car` (ISC).
 */
public val ArecaIcons.Car: ImageVector
    get() = cachedCar ?: ImageVector.Builder(
        name = "Car",
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
            moveTo(19f, 17f)
            horizontalLineTo(21f)
            curveTo(21.6f, 17f, 22f, 16.6f, 22f, 16f)
            verticalLineTo(13f)
            curveTo(22f, 12.1f, 21.3f, 11.3f, 20.5f, 11.1f)
            curveTo(18.7f, 10.6f, 16f, 10f, 16f, 10f)
            reflectiveCurveTo(14.7f, 8.6f, 13.8f, 7.7f)
            curveTo(13.3f, 7.3f, 12.7f, 7f, 12f, 7f)
            horizontalLineTo(5f)
            curveTo(4.4f, 7f, 3.9f, 7.4f, 3.6f, 7.9f)
            lineTo(2.2f, 10.8f)
            arcTo(3.7f, 3.7f, 0f, false, false, 2f, 12f)
            verticalLineTo(16f)
            curveTo(2f, 16.6f, 2.4f, 17f, 3f, 17f)
            horizontalLineTo(5f)
            moveTo(5f, 17f)
            arcTo(2f, 2f, 0f, true, true, 9f, 17f)
            arcTo(2f, 2f, 0f, true, true, 5f, 17f)
            close()
            moveTo(9f, 17f)
            horizontalLineTo(15f)
            moveTo(15f, 17f)
            arcTo(2f, 2f, 0f, true, true, 19f, 17f)
            arcTo(2f, 2f, 0f, true, true, 15f, 17f)
            close()
        }
    }.build().also { cachedCar = it }

/** `Car` vector, built on first access and reused afterwards. */
private var cachedCar: ImageVector? = null
