package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Droplets icon.
 *
 * Converted from Lucide `droplets` (ISC).
 */
public val ArecaIcons.Droplets: ImageVector
    get() = cachedDroplets ?: ImageVector.Builder(
        name = "Droplets",
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
            moveTo(7f, 16.3f)
            curveTo(9.2f, 16.3f, 11f, 14.47f, 11f, 12.25f)
            curveTo(11f, 11.09f, 10.43f, 9.99f, 9.29f, 9.06f)
            reflectiveCurveTo(7.29f, 6.75f, 7f, 5.3f)
            curveTo(6.71f, 6.75f, 5.86f, 8.14f, 4.71f, 9.06f)
            reflectiveCurveTo(3f, 11.1f, 3f, 12.25f)
            curveTo(3f, 14.47f, 4.8f, 16.3f, 7f, 16.3f)
            close()
            moveTo(12.56f, 6.6f)
            arcTo(10.97f, 10.97f, 0f, false, false, 14f, 3.02f)
            curveTo(14.5f, 5.52f, 16f, 7.92f, 18f, 9.52f)
            reflectiveCurveTo(21f, 13.02f, 21f, 15.02f)
            arcTo(6.98f, 6.98f, 0f, false, true, 9.09f, 19.99f)
        }
    }.build().also { cachedDroplets = it }

/** `Droplets` vector, built on first access and reused afterwards. */
private var cachedDroplets: ImageVector? = null
