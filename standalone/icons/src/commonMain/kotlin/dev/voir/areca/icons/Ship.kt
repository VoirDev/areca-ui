package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Ship icon.
 *
 * Converted from Lucide `ship` (ISC).
 */
public val ArecaIcons.Ship: ImageVector
    get() = cachedShip ?: ImageVector.Builder(
        name = "Ship",
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
            moveTo(12f, 2f)
            verticalLineTo(4f)
            moveTo(12f, 9.189f)
            verticalLineTo(13f)
            moveTo(19f, 12f)
            verticalLineTo(6f)
            arcTo(2f, 2f, 0f, false, false, 17f, 4f)
            horizontalLineTo(7f)
            arcTo(2f, 2f, 0f, false, false, 5f, 6f)
            verticalLineTo(12f)
            moveTo(19.38f, 19f)
            arcTo(11.6f, 11.6f, 0f, false, false, 21f, 13f)
            lineTo(12.812f, 9.361f)
            arcTo(2f, 2f, 0f, false, false, 11.188f, 9.361f)
            lineTo(3f, 13.001f)
            arcTo(11.6f, 11.6f, 0f, false, false, 5.81f, 20.761f)
            moveTo(2f, 20f)
            curveTo(2.6f, 20.5f, 3.2f, 21f, 4.5f, 21f)
            curveTo(7f, 21f, 7f, 19f, 9.5f, 19f)
            curveTo(10.8f, 19f, 11.4f, 19.5f, 12f, 20f)
            reflectiveCurveTo(13.2f, 21f, 14.5f, 21f)
            curveTo(17f, 21f, 17f, 19f, 19.5f, 19f)
            curveTo(20.8f, 19f, 21.4f, 19.5f, 22f, 20f)
        }
    }.build().also { cachedShip = it }

/** `Ship` vector, built on first access and reused afterwards. */
private var cachedShip: ImageVector? = null
