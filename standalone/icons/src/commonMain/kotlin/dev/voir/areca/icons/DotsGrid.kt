package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Grip icon.
 *
 * Converted from Lucide `grip` (ISC).
 */
public val ArecaIcons.DotsGrid: ImageVector
    get() = cachedDotsGrid ?: ImageVector.Builder(
        name = "DotsGrid",
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
            moveTo(11f, 5f)
            arcTo(1f, 1f, 0f, true, true, 13f, 5f)
            arcTo(1f, 1f, 0f, true, true, 11f, 5f)
            close()
            moveTo(18f, 5f)
            arcTo(1f, 1f, 0f, true, true, 20f, 5f)
            arcTo(1f, 1f, 0f, true, true, 18f, 5f)
            close()
            moveTo(4f, 5f)
            arcTo(1f, 1f, 0f, true, true, 6f, 5f)
            arcTo(1f, 1f, 0f, true, true, 4f, 5f)
            close()
            moveTo(11f, 12f)
            arcTo(1f, 1f, 0f, true, true, 13f, 12f)
            arcTo(1f, 1f, 0f, true, true, 11f, 12f)
            close()
            moveTo(18f, 12f)
            arcTo(1f, 1f, 0f, true, true, 20f, 12f)
            arcTo(1f, 1f, 0f, true, true, 18f, 12f)
            close()
            moveTo(4f, 12f)
            arcTo(1f, 1f, 0f, true, true, 6f, 12f)
            arcTo(1f, 1f, 0f, true, true, 4f, 12f)
            close()
            moveTo(11f, 19f)
            arcTo(1f, 1f, 0f, true, true, 13f, 19f)
            arcTo(1f, 1f, 0f, true, true, 11f, 19f)
            close()
            moveTo(18f, 19f)
            arcTo(1f, 1f, 0f, true, true, 20f, 19f)
            arcTo(1f, 1f, 0f, true, true, 18f, 19f)
            close()
            moveTo(4f, 19f)
            arcTo(1f, 1f, 0f, true, true, 6f, 19f)
            arcTo(1f, 1f, 0f, true, true, 4f, 19f)
            close()
        }
    }.build().also { cachedDotsGrid = it }

/** `DotsGrid` vector, built on first access and reused afterwards. */
private var cachedDotsGrid: ImageVector? = null
