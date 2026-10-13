package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Shield icon.
 *
 * Converted from Lucide `shield` (ISC).
 */
public val ArecaIcons.Shield: ImageVector
    get() = cachedShield ?: ImageVector.Builder(
        name = "Shield",
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
            moveTo(20f, 13f)
            curveTo(20f, 18f, 16.5f, 20.5f, 12.34f, 21.95f)
            arcTo(1f, 1f, 0f, false, true, 11.67f, 21.94f)
            curveTo(7.5f, 20.5f, 4f, 18f, 4f, 13f)
            verticalLineTo(6f)
            arcTo(1f, 1f, 0f, false, true, 5f, 5f)
            curveTo(7f, 5f, 9.5f, 3.8f, 11.24f, 2.28f)
            arcTo(1.17f, 1.17f, 0f, false, true, 12.76f, 2.28f)
            curveTo(14.51f, 3.81f, 17f, 5f, 19f, 5f)
            arcTo(1f, 1f, 0f, false, true, 20f, 6f)
            close()
        }
    }.build().also { cachedShield = it }

/** `Shield` vector, built on first access and reused afterwards. */
private var cachedShield: ImageVector? = null
