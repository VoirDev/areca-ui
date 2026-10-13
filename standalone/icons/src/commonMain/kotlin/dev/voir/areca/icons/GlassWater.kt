package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Glass water icon.
 *
 * Converted from Lucide `glass-water` (ISC).
 */
public val ArecaIcons.GlassWater: ImageVector
    get() = cachedGlassWater ?: ImageVector.Builder(
        name = "GlassWater",
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
            moveTo(5.116f, 4.104f)
            arcTo(1f, 1f, 0f, false, true, 6.11f, 3f)
            horizontalLineTo(17.89f)
            arcTo(1f, 1f, 0f, false, true, 18.884f, 4.105f)
            lineTo(17.19f, 20.21f)
            arcTo(2f, 2f, 0f, false, true, 15.2f, 22f)
            horizontalLineTo(8.8f)
            arcTo(2f, 2f, 0f, false, true, 6.8f, 20.21f)
            close()
            moveTo(6f, 12f)
            arcTo(5f, 5f, 0f, false, true, 12f, 12f)
            arcTo(5f, 5f, 0f, false, false, 18f, 12f)
        }
    }.build().also { cachedGlassWater = it }

/** `GlassWater` vector, built on first access and reused afterwards. */
private var cachedGlassWater: ImageVector? = null
