package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Rotate ccw square icon.
 *
 * Converted from Lucide `rotate-ccw-square` (ISC).
 */
public val ArecaIcons.RotateCcwSquare: ImageVector
    get() = cachedRotateCcwSquare ?: ImageVector.Builder(
        name = "RotateCcwSquare",
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
            moveTo(20f, 9f)
            verticalLineTo(7f)
            arcTo(2f, 2f, 0f, false, false, 18f, 5f)
            horizontalLineTo(12f)
            moveTo(15f, 2f)
            lineTo(12f, 5f)
            lineTo(15f, 8f)
            moveTo(20f, 13f)
            verticalLineTo(18f)
            arcTo(2f, 2f, 0f, false, true, 18f, 20f)
            horizontalLineTo(6f)
            arcTo(2f, 2f, 0f, false, true, 4f, 18f)
            verticalLineTo(7f)
            arcTo(2f, 2f, 0f, false, true, 6f, 5f)
            horizontalLineTo(8f)
        }
    }.build().also { cachedRotateCcwSquare = it }

/** `RotateCcwSquare` vector, built on first access and reused afterwards. */
private var cachedRotateCcwSquare: ImageVector? = null
