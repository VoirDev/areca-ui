package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Camera off icon.
 *
 * Converted from Lucide `camera-off` (ISC).
 */
public val ArecaIcons.CameraOff: ImageVector
    get() = cachedCameraOff ?: ImageVector.Builder(
        name = "CameraOff",
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
            moveTo(14.564f, 14.558f)
            arcTo(3f, 3f, 0f, true, true, 10.442f, 10.437f)
            moveTo(2f, 2f)
            lineTo(22f, 22f)
            moveTo(20f, 20f)
            horizontalLineTo(4f)
            arcTo(2f, 2f, 0f, false, true, 2f, 18f)
            verticalLineTo(9f)
            arcTo(2f, 2f, 0f, false, true, 4f, 7f)
            horizontalLineTo(5.997f)
            arcTo(2f, 2f, 0f, false, false, 6.816f, 6.825f)
            moveTo(9.695f, 4.024f)
            arcTo(2f, 2f, 0f, false, true, 10.004f, 4f)
            horizontalLineTo(13.997f)
            arcTo(2f, 2f, 0f, false, true, 15.757f, 5.05f)
            lineTo(16.243f, 5.95f)
            arcTo(2f, 2f, 0f, false, false, 18.003f, 7f)
            horizontalLineTo(20f)
            arcTo(2f, 2f, 0f, false, true, 22f, 9f)
            verticalLineTo(16.344f)
        }
    }.build().also { cachedCameraOff = it }

/** `CameraOff` vector, built on first access and reused afterwards. */
private var cachedCameraOff: ImageVector? = null
