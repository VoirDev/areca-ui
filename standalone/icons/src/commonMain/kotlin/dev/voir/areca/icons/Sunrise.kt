package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Sunrise icon.
 *
 * Converted from Lucide `sunrise` (ISC).
 */
public val ArecaIcons.Sunrise: ImageVector
    get() = cachedSunrise ?: ImageVector.Builder(
        name = "Sunrise",
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
            verticalLineTo(10f)
            moveTo(4.93f, 10.93f)
            lineTo(6.34f, 12.34f)
            moveTo(2f, 18f)
            horizontalLineTo(4f)
            moveTo(20f, 18f)
            horizontalLineTo(22f)
            moveTo(19.07f, 10.93f)
            lineTo(17.66f, 12.34f)
            moveTo(22f, 22f)
            horizontalLineTo(2f)
            moveTo(8f, 6f)
            lineTo(12f, 2f)
            lineTo(16f, 6f)
            moveTo(16f, 18f)
            arcTo(4f, 4f, 0f, false, false, 8f, 18f)
        }
    }.build().also { cachedSunrise = it }

/** `Sunrise` vector, built on first access and reused afterwards. */
private var cachedSunrise: ImageVector? = null
