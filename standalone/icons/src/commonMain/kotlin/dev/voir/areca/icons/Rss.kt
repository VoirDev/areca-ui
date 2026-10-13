package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Rss icon.
 *
 * Converted from Lucide `rss` (ISC).
 */
public val ArecaIcons.Rss: ImageVector
    get() = cachedRss ?: ImageVector.Builder(
        name = "Rss",
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
            moveTo(4f, 11f)
            arcTo(9f, 9f, 0f, false, true, 13f, 20f)
            moveTo(4f, 4f)
            arcTo(16f, 16f, 0f, false, true, 20f, 20f)
            moveTo(4f, 19f)
            arcTo(1f, 1f, 0f, true, true, 6f, 19f)
            arcTo(1f, 1f, 0f, true, true, 4f, 19f)
            close()
        }
    }.build().also { cachedRss = it }

/** `Rss` vector, built on first access and reused afterwards. */
private var cachedRss: ImageVector? = null
