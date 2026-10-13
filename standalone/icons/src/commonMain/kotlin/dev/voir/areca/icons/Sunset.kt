package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Sunset icon.
 *
 * Converted from Lucide `sunset` (ISC).
 */
public val ArecaIcons.Sunset: ImageVector
    get() = cachedSunset ?: ImageVector.Builder(
        name = "Sunset",
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
            moveTo(12f, 10f)
            verticalLineTo(2f)
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
            moveTo(16f, 6f)
            lineTo(12f, 10f)
            lineTo(8f, 6f)
            moveTo(16f, 18f)
            arcTo(4f, 4f, 0f, false, false, 8f, 18f)
        }
    }.build().also { cachedSunset = it }

/** `Sunset` vector, built on first access and reused afterwards. */
private var cachedSunset: ImageVector? = null
