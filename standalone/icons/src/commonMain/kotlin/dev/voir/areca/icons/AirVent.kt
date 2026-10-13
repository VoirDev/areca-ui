package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Air vent icon.
 *
 * Converted from Lucide `air-vent` (ISC).
 */
public val ArecaIcons.AirVent: ImageVector
    get() = cachedAirVent ?: ImageVector.Builder(
        name = "AirVent",
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
            moveTo(18f, 17.5f)
            arcTo(2.5f, 2.5f, 0f, true, true, 14f, 19.53f)
            verticalLineTo(12f)
            moveTo(6f, 12f)
            horizontalLineTo(4f)
            arcTo(2f, 2f, 0f, false, true, 2f, 10f)
            verticalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 4f, 3f)
            horizontalLineTo(20f)
            arcTo(2f, 2f, 0f, false, true, 22f, 5f)
            verticalLineTo(10f)
            arcTo(2f, 2f, 0f, false, true, 20f, 12f)
            horizontalLineTo(18f)
            moveTo(6f, 8f)
            horizontalLineTo(18f)
            moveTo(6.6f, 15.572f)
            arcTo(2f, 2f, 0f, true, false, 10f, 17f)
            verticalLineTo(12f)
        }
    }.build().also { cachedAirVent = it }

/** `AirVent` vector, built on first access and reused afterwards. */
private var cachedAirVent: ImageVector? = null
