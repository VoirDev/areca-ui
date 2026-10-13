package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Infinity icon.
 *
 * Converted from Lucide `infinity` (ISC).
 */
public val ArecaIcons.Infinity: ImageVector
    get() = cachedInfinity ?: ImageVector.Builder(
        name = "Infinity",
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
            moveTo(6f, 16f)
            curveTo(11f, 16f, 13f, 8f, 18f, 8f)
            arcTo(4f, 4f, 0f, false, true, 18f, 16f)
            curveTo(13f, 16f, 11f, 8f, 6f, 8f)
            arcTo(4f, 4f, 0f, true, false, 6f, 16f)
        }
    }.build().also { cachedInfinity = it }

/** `Infinity` vector, built on first access and reused afterwards. */
private var cachedInfinity: ImageVector? = null
