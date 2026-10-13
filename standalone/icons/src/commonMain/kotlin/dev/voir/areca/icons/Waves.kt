package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Waves horizontal icon.
 *
 * Converted from Lucide `waves-horizontal` (ISC).
 */
public val ArecaIcons.Waves: ImageVector
    get() = cachedWaves ?: ImageVector.Builder(
        name = "Waves",
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
            quadTo(4.5f, 14f, 7f, 12f)
            reflectiveQuadTo(12f, 12f)
            reflectiveQuadTo(17f, 12f)
            reflectiveQuadTo(22f, 12f)
            moveTo(2f, 19f)
            quadTo(4.5f, 21f, 7f, 19f)
            reflectiveQuadTo(12f, 19f)
            reflectiveQuadTo(17f, 19f)
            reflectiveQuadTo(22f, 19f)
            moveTo(2f, 5f)
            quadTo(4.5f, 7f, 7f, 5f)
            reflectiveQuadTo(12f, 5f)
            reflectiveQuadTo(17f, 5f)
            reflectiveQuadTo(22f, 5f)
        }
    }.build().also { cachedWaves = it }

/** `Waves` vector, built on first access and reused afterwards. */
private var cachedWaves: ImageVector? = null
