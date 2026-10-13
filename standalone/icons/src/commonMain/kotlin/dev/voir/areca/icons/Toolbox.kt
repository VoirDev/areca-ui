package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Toolbox icon.
 *
 * Converted from Lucide `toolbox` (ISC).
 */
public val ArecaIcons.Toolbox: ImageVector
    get() = cachedToolbox ?: ImageVector.Builder(
        name = "Toolbox",
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
            moveTo(16f, 12f)
            verticalLineTo(16f)
            moveTo(16f, 6f)
            verticalLineTo(4f)
            arcTo(2f, 2f, 0f, false, false, 14f, 2f)
            horizontalLineTo(10f)
            arcTo(2f, 2f, 0f, false, false, 8f, 4f)
            verticalLineTo(6f)
            moveTo(17f, 6f)
            arcTo(2f, 2f, 0f, false, true, 18.414f, 6.586f)
            lineTo(21.414f, 9.586f)
            arcTo(2f, 2f, 0f, false, true, 22f, 11f)
            verticalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 20f, 21f)
            horizontalLineTo(4f)
            arcTo(2f, 2f, 0f, false, true, 2f, 19f)
            verticalLineTo(11f)
            arcTo(2f, 2f, 0f, false, true, 2.586f, 9.586f)
            lineTo(5.586f, 6.586f)
            arcTo(2f, 2f, 0f, false, true, 7f, 6f)
            close()
            moveTo(2f, 14f)
            horizontalLineTo(22f)
            moveTo(8f, 12f)
            verticalLineTo(16f)
        }
    }.build().also { cachedToolbox = it }

/** `Toolbox` vector, built on first access and reused afterwards. */
private var cachedToolbox: ImageVector? = null
