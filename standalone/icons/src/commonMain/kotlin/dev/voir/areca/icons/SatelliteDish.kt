package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Satellite dish icon.
 *
 * Converted from Lucide `satellite-dish` (ISC).
 */
public val ArecaIcons.SatelliteDish: ImageVector
    get() = cachedSatelliteDish ?: ImageVector.Builder(
        name = "SatelliteDish",
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
            moveTo(18f, 12f)
            arcTo(6f, 6f, 0f, false, false, 12f, 6f)
            moveTo(2.824f, 10.459f)
            arcTo(8f, 8f, 0f, false, false, 13.541f, 21.176f)
            curveTo(14.099f, 20.9f, 14.164f, 20.164f, 13.724f, 19.724f)
            lineTo(4.276f, 10.276f)
            curveTo(3.836f, 9.836f, 3.1f, 9.901f, 2.824f, 10.459f)
            moveTo(22f, 12f)
            arcTo(10f, 10f, 0f, false, false, 12f, 2f)
            moveTo(9f, 15f)
            lineTo(13f, 11f)
        }
    }.build().also { cachedSatelliteDish = it }

/** `SatelliteDish` vector, built on first access and reused afterwards. */
private var cachedSatelliteDish: ImageVector? = null
