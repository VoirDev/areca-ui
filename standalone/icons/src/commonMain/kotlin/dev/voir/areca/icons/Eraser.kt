package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Eraser icon.
 *
 * Converted from Lucide `eraser` (ISC).
 */
public val ArecaIcons.Eraser: ImageVector
    get() = cachedEraser ?: ImageVector.Builder(
        name = "Eraser",
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
            moveTo(21f, 21f)
            horizontalLineTo(8f)
            arcTo(2f, 2f, 0f, false, true, 6.58f, 20.413f)
            lineTo(2.586f, 16.414f)
            arcTo(2f, 2f, 0f, false, true, 2.586f, 13.586f)
            lineTo(12.586f, 3.586f)
            arcTo(2f, 2f, 0f, false, true, 15.415f, 3.586f)
            lineTo(21.414f, 9.586f)
            arcTo(2f, 2f, 0f, false, true, 21.414f, 12.414f)
            lineTo(12.834f, 21f)
            moveTo(5.082f, 11.09f)
            lineTo(13.91f, 19.918f)
        }
    }.build().also { cachedEraser = it }

/** `Eraser` vector, built on first access and reused afterwards. */
private var cachedEraser: ImageVector? = null
