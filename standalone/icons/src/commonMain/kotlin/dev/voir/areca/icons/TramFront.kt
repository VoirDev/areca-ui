package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Tram front icon.
 *
 * Converted from Lucide `tram-front` (ISC).
 */
public val ArecaIcons.TramFront: ImageVector
    get() = cachedTramFront ?: ImageVector.Builder(
        name = "TramFront",
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
            moveTo(6f, 3f)
            horizontalLineTo(18f)
            arcTo(2f, 2f, 0f, false, true, 20f, 5f)
            verticalLineTo(17f)
            arcTo(2f, 2f, 0f, false, true, 18f, 19f)
            horizontalLineTo(6f)
            arcTo(2f, 2f, 0f, false, true, 4f, 17f)
            verticalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 6f, 3f)
            close()
            moveTo(4f, 11f)
            horizontalLineTo(20f)
            moveTo(12f, 3f)
            verticalLineTo(11f)
            moveTo(8f, 19f)
            lineTo(6f, 22f)
            moveTo(18f, 22f)
            lineTo(16f, 19f)
            moveTo(8f, 15f)
            horizontalLineTo(8.01f)
            moveTo(16f, 15f)
            horizontalLineTo(16.01f)
        }
    }.build().also { cachedTramFront = it }

/** `TramFront` vector, built on first access and reused afterwards. */
private var cachedTramFront: ImageVector? = null
