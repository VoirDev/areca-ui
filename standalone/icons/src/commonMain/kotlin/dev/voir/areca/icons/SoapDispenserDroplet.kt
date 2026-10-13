package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Soap dispenser droplet icon.
 *
 * Converted from Lucide `soap-dispenser-droplet` (ISC).
 */
public val ArecaIcons.SoapDispenserDroplet: ImageVector
    get() = cachedSoapDispenserDroplet ?: ImageVector.Builder(
        name = "SoapDispenserDroplet",
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
            moveTo(10.5f, 2f)
            verticalLineTo(6f)
            moveTo(14f, 2f)
            horizontalLineTo(7f)
            arcTo(2f, 2f, 0f, false, false, 5f, 4f)
            moveTo(19.29f, 14.76f)
            arcTo(6.67f, 6.67f, 0f, false, true, 17f, 11f)
            arcTo(6.6f, 6.6f, 0f, false, true, 14.71f, 14.76f)
            curveTo(13.56f, 15.68f, 13f, 16.8f, 13f, 17.95f)
            curveTo(13f, 20.17f, 14.8f, 22f, 17f, 22f)
            reflectiveCurveTo(21f, 20.17f, 21f, 17.95f)
            curveTo(21f, 16.79f, 20.43f, 15.69f, 19.29f, 14.76f)
            moveTo(9.607f, 21f)
            horizontalLineTo(6f)
            arcTo(2f, 2f, 0f, false, true, 4f, 19f)
            verticalLineTo(12f)
            arcTo(2f, 2f, 0f, false, true, 6f, 10f)
            horizontalLineTo(13f)
            verticalLineTo(7f)
            arcTo(1f, 1f, 0f, false, false, 12f, 6f)
            horizontalLineTo(9f)
            arcTo(1f, 1f, 0f, false, false, 8f, 7f)
            verticalLineTo(10f)
        }
    }.build().also { cachedSoapDispenserDroplet = it }

/** `SoapDispenserDroplet` vector, built on first access and reused afterwards. */
private var cachedSoapDispenserDroplet: ImageVector? = null
