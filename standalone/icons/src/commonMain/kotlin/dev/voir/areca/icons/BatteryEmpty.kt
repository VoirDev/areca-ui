package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Battery icon.
 *
 * Converted from Lucide `battery` (ISC).
 */
public val ArecaIcons.BatteryEmpty: ImageVector
    get() = cachedBatteryEmpty ?: ImageVector.Builder(
        name = "BatteryEmpty",
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
            moveTo(22f, 14f)
            lineTo(22f, 10f)
            moveTo(4f, 6f)
            horizontalLineTo(16f)
            arcTo(2f, 2f, 0f, false, true, 18f, 8f)
            verticalLineTo(16f)
            arcTo(2f, 2f, 0f, false, true, 16f, 18f)
            horizontalLineTo(4f)
            arcTo(2f, 2f, 0f, false, true, 2f, 16f)
            verticalLineTo(8f)
            arcTo(2f, 2f, 0f, false, true, 4f, 6f)
            close()
        }
    }.build().also { cachedBatteryEmpty = it }

/** `BatteryEmpty` vector, built on first access and reused afterwards. */
private var cachedBatteryEmpty: ImageVector? = null
