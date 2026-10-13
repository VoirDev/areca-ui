package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Wifi off icon.
 *
 * Converted from Lucide `wifi-off` (ISC).
 */
public val ArecaIcons.WifiOff: ImageVector
    get() = cachedWifiOff ?: ImageVector.Builder(
        name = "WifiOff",
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
            moveTo(12f, 20f)
            horizontalLineTo(12.01f)
            moveTo(8.5f, 16.429f)
            arcTo(5f, 5f, 0f, false, true, 15.5f, 16.429f)
            moveTo(5f, 12.859f)
            arcTo(10f, 10f, 0f, false, true, 10.17f, 10.169f)
            moveTo(19f, 12.859f)
            arcTo(10f, 10f, 0f, false, false, 16.993f, 11.336f)
            moveTo(2f, 8.82f)
            arcTo(15f, 15f, 0f, false, true, 6.177f, 6.177f)
            moveTo(22f, 8.82f)
            arcTo(15f, 15f, 0f, false, false, 10.712f, 5.056f)
            moveTo(2f, 2f)
            lineTo(22f, 22f)
        }
    }.build().also { cachedWifiOff = it }

/** `WifiOff` vector, built on first access and reused afterwards. */
private var cachedWifiOff: ImageVector? = null
