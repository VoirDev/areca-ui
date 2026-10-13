package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Crosshair icon.
 *
 * Converted from Lucide `crosshair` (ISC).
 */
public val ArecaIcons.Mark: ImageVector
    get() = cachedMark ?: ImageVector.Builder(
        name = "Mark",
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
            moveTo(2f, 12f)
            arcTo(10f, 10f, 0f, true, true, 22f, 12f)
            arcTo(10f, 10f, 0f, true, true, 2f, 12f)
            close()
            moveTo(22f, 12f)
            lineTo(18f, 12f)
            moveTo(6f, 12f)
            lineTo(2f, 12f)
            moveTo(12f, 6f)
            lineTo(12f, 2f)
            moveTo(12f, 22f)
            lineTo(12f, 18f)
        }
    }.build().also { cachedMark = it }

/** `Mark` vector, built on first access and reused afterwards. */
private var cachedMark: ImageVector? = null
