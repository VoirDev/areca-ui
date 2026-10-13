package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Bus icon.
 *
 * Converted from Lucide `bus` (ISC).
 */
public val ArecaIcons.Bus: ImageVector
    get() = cachedBus ?: ImageVector.Builder(
        name = "Bus",
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
            moveTo(8f, 6f)
            verticalLineTo(12f)
            moveTo(15f, 6f)
            verticalLineTo(12f)
            moveTo(2f, 12f)
            horizontalLineTo(21.6f)
            moveTo(18f, 18f)
            horizontalLineTo(21f)
            reflectiveCurveTo(21.5f, 16.3f, 21.8f, 15.2f)
            curveTo(21.9f, 14.8f, 22f, 14.4f, 22f, 14f)
            curveTo(22f, 13.6f, 21.9f, 13.2f, 21.8f, 12.8f)
            lineTo(20.4f, 7.8f)
            curveTo(20.1f, 6.8f, 19.1f, 6f, 18f, 6f)
            horizontalLineTo(4f)
            arcTo(2f, 2f, 0f, false, false, 2f, 8f)
            verticalLineTo(18f)
            horizontalLineTo(5f)
            moveTo(5f, 18f)
            arcTo(2f, 2f, 0f, true, true, 9f, 18f)
            arcTo(2f, 2f, 0f, true, true, 5f, 18f)
            close()
            moveTo(9f, 18f)
            horizontalLineTo(14f)
            moveTo(14f, 18f)
            arcTo(2f, 2f, 0f, true, true, 18f, 18f)
            arcTo(2f, 2f, 0f, true, true, 14f, 18f)
            close()
        }
    }.build().also { cachedBus = it }

/** `Bus` vector, built on first access and reused afterwards. */
private var cachedBus: ImageVector? = null
