package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Droplet icon.
 *
 * Converted from Lucide `droplet` (ISC).
 */
public val ArecaIcons.Droplet: ImageVector
    get() = cachedDroplet ?: ImageVector.Builder(
        name = "Droplet",
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
            moveTo(12f, 22f)
            arcTo(7f, 7f, 0f, false, false, 19f, 15f)
            curveTo(19f, 13f, 18f, 11.1f, 16f, 9.5f)
            reflectiveCurveTo(12.5f, 5.5f, 12f, 3f)
            curveTo(11.5f, 5.5f, 10f, 7.9f, 8f, 9.5f)
            curveTo(6f, 11.1f, 5f, 13f, 5f, 15f)
            arcTo(7f, 7f, 0f, false, false, 12f, 22f)
            close()
        }
    }.build().also { cachedDroplet = it }

/** `Droplet` vector, built on first access and reused afterwards. */
private var cachedDroplet: ImageVector? = null
