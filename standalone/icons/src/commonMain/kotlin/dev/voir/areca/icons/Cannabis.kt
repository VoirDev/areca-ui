package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Cannabis icon.
 *
 * Converted from Lucide `cannabis` (ISC).
 */
public val ArecaIcons.Cannabis: ImageVector
    get() = cachedCannabis ?: ImageVector.Builder(
        name = "Cannabis",
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
            verticalLineTo(18f)
            moveTo(7f, 12f)
            curveTo(5.5f, 12f, 2.5f, 13.5f, 2f, 15f)
            curveTo(5.5f, 16.5f, 8f, 16f, 8f, 16f)
            curveTo(6.5f, 17.5f, 6f, 19.5f, 6f, 21f)
            curveTo(8.5f, 21f, 10.5f, 19.5f, 12f, 18f)
            curveTo(13.5f, 19.5f, 15.5f, 21f, 18f, 21f)
            curveTo(18f, 19.5f, 17.5f, 17.5f, 16f, 16f)
            curveTo(16f, 16f, 18.5f, 16.5f, 22f, 15f)
            curveTo(21.5f, 13.5f, 18.5f, 12f, 17f, 12f)
            curveTo(18.5f, 11f, 21f, 8f, 21f, 6f)
            curveTo(18.5f, 6f, 15.5f, 7.5f, 14f, 9f)
            curveTo(14f, 6.5f, 13.5f, 4f, 12f, 2f)
            curveTo(10.5f, 4f, 10f, 6.5f, 10f, 9f)
            curveTo(8.5f, 7.5f, 5.5f, 6f, 3f, 6f)
            curveTo(3f, 8f, 5.5f, 11f, 7f, 12f)
        }
    }.build().also { cachedCannabis = it }

/** `Cannabis` vector, built on first access and reused afterwards. */
private var cachedCannabis: ImageVector? = null
