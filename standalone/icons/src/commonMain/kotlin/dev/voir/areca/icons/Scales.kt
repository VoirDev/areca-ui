package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Scale icon.
 *
 * Converted from Lucide `scale` (ISC).
 */
public val ArecaIcons.Scales: ImageVector
    get() = cachedScales ?: ImageVector.Builder(
        name = "Scales",
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
            moveTo(12f, 3f)
            verticalLineTo(21f)
            moveTo(19f, 8f)
            lineTo(22f, 16f)
            arcTo(5f, 5f, 0f, false, true, 16f, 16f)
            close()
            verticalLineTo(7f)
            moveTo(3f, 7f)
            horizontalLineTo(4f)
            arcTo(17f, 17f, 0f, false, false, 12f, 5f)
            arcTo(17f, 17f, 0f, false, false, 20f, 7f)
            horizontalLineTo(21f)
            moveTo(5f, 8f)
            lineTo(8f, 16f)
            arcTo(5f, 5f, 0f, false, true, 2f, 16f)
            close()
            verticalLineTo(7f)
            moveTo(7f, 21f)
            horizontalLineTo(17f)
        }
    }.build().also { cachedScales = it }

/** `Scales` vector, built on first access and reused afterwards. */
private var cachedScales: ImageVector? = null
