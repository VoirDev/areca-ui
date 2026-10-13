package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Engine icon.
 *
 * Converted from Lucide `engine` (ISC).
 */
public val ArecaIcons.Engine: ImageVector
    get() = cachedEngine ?: ImageVector.Builder(
        name = "Engine",
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
            moveTo(10f, 3f)
            horizontalLineTo(16f)
            moveTo(13f, 3f)
            verticalLineTo(7f)
            moveTo(2f, 10f)
            verticalLineTo(16f)
            moveTo(2f, 13f)
            horizontalLineTo(6f)
            moveTo(6f, 16f)
            arcTo(2f, 2f, 0f, false, false, 8f, 18f)
            horizontalLineTo(9f)
            arcTo(2f, 2f, 0f, false, true, 10.6f, 18.8f)
            lineTo(10.9f, 19.2f)
            arcTo(2f, 2f, 0f, false, false, 12.5f, 20f)
            horizontalLineTo(14.764f)
            arcTo(2f, 2f, 0f, false, false, 16.553f, 18.894f)
            lineTo(18.223f, 15.553f)
            arcTo(1f, 1f, 0f, false, true, 19.118f, 15f)
            horizontalLineTo(21f)
            arcTo(1f, 1f, 0f, false, false, 22f, 14f)
            verticalLineTo(10f)
            arcTo(1f, 1f, 0f, false, false, 21f, 9f)
            horizontalLineTo(17.5f)
            arcTo(1f, 1f, 0f, false, true, 16.7f, 8.6f)
            lineTo(15.8f, 7.4f)
            arcTo(1f, 1f, 0f, false, false, 15f, 7f)
            horizontalLineTo(11f)
            arcTo(1f, 1f, 0f, false, false, 10.2f, 7.4f)
            lineTo(9.3f, 8.6f)
            arcTo(1f, 1f, 0f, false, true, 8.5f, 9f)
            horizontalLineTo(7f)
            arcTo(1f, 1f, 0f, false, false, 6f, 10f)
            close()
        }
    }.build().also { cachedEngine = it }

/** `Engine` vector, built on first access and reused afterwards. */
private var cachedEngine: ImageVector? = null
