package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Search x icon.
 *
 * Converted from Lucide `search-x` (ISC).
 */
public val ArecaIcons.SearchX: ImageVector
    get() = cachedSearchX ?: ImageVector.Builder(
        name = "SearchX",
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
            moveTo(13.5f, 8.5f)
            lineTo(8.5f, 13.5f)
            moveTo(8.5f, 8.5f)
            lineTo(13.5f, 13.5f)
            moveTo(3f, 11f)
            arcTo(8f, 8f, 0f, true, true, 19f, 11f)
            arcTo(8f, 8f, 0f, true, true, 3f, 11f)
            close()
            moveTo(21f, 21f)
            lineTo(16.7f, 16.7f)
        }
    }.build().also { cachedSearchX = it }

/** `SearchX` vector, built on first access and reused afterwards. */
private var cachedSearchX: ImageVector? = null
