package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Flame kindling icon.
 *
 * Converted from Lucide `flame-kindling` (ISC).
 */
public val ArecaIcons.FlameKindling: ImageVector
    get() = cachedFlameKindling ?: ImageVector.Builder(
        name = "FlameKindling",
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
            moveTo(12f, 2f)
            curveTo(13f, 5f, 14.5f, 5.5f, 15.5f, 6.5f)
            arcTo(5f, 5f, 0f, false, true, 17f, 10f)
            arcTo(5f, 5f, 0f, true, true, 7f, 10f)
            curveTo(7f, 9.7f, 7f, 9.4f, 7.1f, 9.1f)
            arcTo(2f, 2f, 0f, true, false, 10.4f, 7.1f)
            curveTo(8f, 4.5f, 11f, 2f, 12f, 2f)
            close()
            moveTo(5f, 22f)
            lineTo(19f, 18f)
            moveTo(5f, 18f)
            lineTo(19f, 22f)
        }
    }.build().also { cachedFlameKindling = it }

/** `FlameKindling` vector, built on first access and reused afterwards. */
private var cachedFlameKindling: ImageVector? = null
