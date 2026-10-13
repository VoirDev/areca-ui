package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Traffic cone icon.
 *
 * Converted from Lucide `traffic-cone` (ISC).
 */
public val ArecaIcons.TrafficCone: ImageVector
    get() = cachedTrafficCone ?: ImageVector.Builder(
        name = "TrafficCone",
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
            moveTo(16.05f, 10.966f)
            arcTo(5f, 2.5f, 0f, false, true, 7.95f, 10.966f)
            moveTo(16.923f, 14.049f)
            lineTo(21.403f, 16.089f)
            arcTo(1f, 1f, 0f, false, true, 21.404f, 17.92f)
            lineTo(12.83f, 21.82f)
            arcTo(2f, 2f, 0f, false, true, 11.17f, 21.82f)
            lineTo(2.596f, 17.91f)
            arcTo(1f, 1f, 0f, false, true, 2.596f, 16.08f)
            lineTo(7.08f, 14.04f)
            moveTo(16.949f, 14.14f)
            arcTo(5f, 2.5f, 0f, true, true, 7.049f, 14.14f)
            lineTo(10.063f, 3.5f)
            arcTo(2f, 2f, 0f, false, true, 13.937f, 3.5f)
            close()
            moveTo(9.194f, 6.57f)
            arcTo(5f, 2.5f, 0f, false, false, 14.804f, 6.57f)
        }
    }.build().also { cachedTrafficCone = it }

/** `TrafficCone` vector, built on first access and reused afterwards. */
private var cachedTrafficCone: ImageVector? = null
