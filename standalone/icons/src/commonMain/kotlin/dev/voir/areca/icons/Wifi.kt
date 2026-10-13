package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Wifi icon.
 *
 * Converted from Lucide `wifi` (ISC).
 */
public val ArecaIcons.Wifi: ImageVector
    get() = cachedWifi ?: ImageVector.Builder(
        name = "Wifi",
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
            moveTo(2f, 8.82f)
            arcTo(15f, 15f, 0f, false, true, 22f, 8.82f)
            moveTo(5f, 12.859f)
            arcTo(10f, 10f, 0f, false, true, 19f, 12.859f)
            moveTo(8.5f, 16.429f)
            arcTo(5f, 5f, 0f, false, true, 15.5f, 16.429f)
        }
    }.build().also { cachedWifi = it }

/** `Wifi` vector, built on first access and reused afterwards. */
private var cachedWifi: ImageVector? = null
