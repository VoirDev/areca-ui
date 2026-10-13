package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Spade icon.
 *
 * Converted from Lucide `spade` (ISC).
 */
public val ArecaIcons.Spade: ImageVector
    get() = cachedSpade ?: ImageVector.Builder(
        name = "Spade",
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
            moveTo(12f, 18f)
            verticalLineTo(22f)
            moveTo(2f, 14.499f)
            arcTo(5.5f, 5.5f, 0f, false, false, 11.591f, 18.174f)
            arcTo(0.6f, 0.6f, 0f, false, true, 12.409f, 18.175f)
            arcTo(5.5f, 5.5f, 0f, false, false, 22f, 14.5f)
            curveTo(22f, 12.21f, 20.5f, 10.5f, 19f, 9f)
            lineTo(13.508f, 3.688f)
            arcTo(2f, 2f, 0f, false, false, 10.508f, 3.668f)
            lineTo(5f, 8.999f)
            curveTo(3.5f, 10.499f, 2f, 12.199f, 2f, 14.499f)
        }
    }.build().also { cachedSpade = it }

/** `Spade` vector, built on first access and reused afterwards. */
private var cachedSpade: ImageVector? = null
