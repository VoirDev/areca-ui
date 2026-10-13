package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Heater icon.
 *
 * Converted from Lucide `heater` (ISC).
 */
public val ArecaIcons.Heater: ImageVector
    get() = cachedHeater ?: ImageVector.Builder(
        name = "Heater",
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
            moveTo(11f, 8f)
            curveTo(13f, 5f, 9f, 5f, 11f, 2f)
            moveTo(15.5f, 8f)
            curveTo(17.5f, 5f, 13.5f, 5f, 15.5f, 2f)
            moveTo(6f, 10f)
            horizontalLineTo(6.01f)
            moveTo(6f, 14f)
            horizontalLineTo(6.01f)
            moveTo(10f, 16f)
            verticalLineTo(12f)
            moveTo(14f, 16f)
            verticalLineTo(12f)
            moveTo(18f, 16f)
            verticalLineTo(12f)
            moveTo(20f, 6f)
            arcTo(2f, 2f, 0f, false, true, 22f, 8f)
            verticalLineTo(18f)
            arcTo(2f, 2f, 0f, false, true, 20f, 20f)
            horizontalLineTo(4f)
            arcTo(2f, 2f, 0f, false, true, 2f, 18f)
            verticalLineTo(8f)
            arcTo(2f, 2f, 0f, false, true, 4f, 6f)
            horizontalLineTo(7f)
            moveTo(5f, 20f)
            verticalLineTo(22f)
            moveTo(19f, 20f)
            verticalLineTo(22f)
        }
    }.build().also { cachedHeater = it }

/** `Heater` vector, built on first access and reused afterwards. */
private var cachedHeater: ImageVector? = null
