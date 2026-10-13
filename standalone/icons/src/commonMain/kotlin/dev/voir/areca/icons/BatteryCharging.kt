package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Battery charging icon.
 *
 * Converted from Lucide `battery-charging` (ISC).
 */
public val ArecaIcons.BatteryCharging: ImageVector
    get() = cachedBatteryCharging ?: ImageVector.Builder(
        name = "BatteryCharging",
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
            moveTo(11f, 7f)
            lineTo(8f, 12f)
            horizontalLineTo(12f)
            lineTo(9f, 17f)
            moveTo(14.856f, 6f)
            horizontalLineTo(16f)
            arcTo(2f, 2f, 0f, false, true, 18f, 8f)
            verticalLineTo(16f)
            arcTo(2f, 2f, 0f, false, true, 16f, 18f)
            horizontalLineTo(13.065f)
            moveTo(22f, 14f)
            verticalLineTo(10f)
            moveTo(5.14f, 18f)
            horizontalLineTo(4f)
            arcTo(2f, 2f, 0f, false, true, 2f, 16f)
            verticalLineTo(8f)
            arcTo(2f, 2f, 0f, false, true, 4f, 6f)
            horizontalLineTo(6.936f)
        }
    }.build().also { cachedBatteryCharging = it }

/** `BatteryCharging` vector, built on first access and reused afterwards. */
private var cachedBatteryCharging: ImageVector? = null
