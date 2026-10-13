package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Sun icon.
 *
 * Converted from Lucide `sun` (ISC).
 */
public val ArecaIcons.Sun: ImageVector
    get() = cachedSun ?: ImageVector.Builder(
        name = "Sun",
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
            moveTo(8f, 12f)
            arcTo(4f, 4f, 0f, true, true, 16f, 12f)
            arcTo(4f, 4f, 0f, true, true, 8f, 12f)
            close()
            moveTo(12f, 2f)
            verticalLineTo(4f)
            moveTo(12f, 20f)
            verticalLineTo(22f)
            moveTo(4.93f, 4.93f)
            lineTo(6.34f, 6.34f)
            moveTo(17.66f, 17.66f)
            lineTo(19.07f, 19.07f)
            moveTo(2f, 12f)
            horizontalLineTo(4f)
            moveTo(20f, 12f)
            horizontalLineTo(22f)
            moveTo(6.34f, 17.66f)
            lineTo(4.93f, 19.07f)
            moveTo(19.07f, 4.93f)
            lineTo(17.66f, 6.34f)
        }
    }.build().also { cachedSun = it }

/** `Sun` vector, built on first access and reused afterwards. */
private var cachedSun: ImageVector? = null
