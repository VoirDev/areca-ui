package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Flame icon.
 *
 * Converted from Lucide `flame` (ISC).
 */
public val ArecaIcons.Flame: ImageVector
    get() = cachedFlame ?: ImageVector.Builder(
        name = "Flame",
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
            quadTo(13f, 7f, 16f, 9.5f)
            reflectiveQuadTo(19f, 15f)
            arcTo(1f, 1f, 0f, false, true, 5f, 15f)
            arcTo(5f, 5f, 0f, false, true, 6f, 12f)
            arcTo(1f, 1f, 0f, false, false, 11f, 12f)
            curveTo(11f, 10f, 9.5f, 9f, 9.5f, 7f)
            quadTo(9.5f, 5f, 12f, 3f)
        }
    }.build().also { cachedFlame = it }

/** `Flame` vector, built on first access and reused afterwards. */
private var cachedFlame: ImageVector? = null
