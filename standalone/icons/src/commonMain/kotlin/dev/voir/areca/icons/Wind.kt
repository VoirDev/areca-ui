package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Wind icon.
 *
 * Converted from Lucide `wind` (ISC).
 */
public val ArecaIcons.Wind: ImageVector
    get() = cachedWind ?: ImageVector.Builder(
        name = "Wind",
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
            moveTo(12.8f, 19.6f)
            arcTo(2f, 2f, 0f, true, false, 14f, 16f)
            horizontalLineTo(2f)
            moveTo(17.5f, 8f)
            arcTo(2.5f, 2.5f, 0f, true, true, 19.5f, 12f)
            horizontalLineTo(2f)
            moveTo(9.8f, 4.4f)
            arcTo(2f, 2f, 0f, true, true, 11f, 8f)
            horizontalLineTo(2f)
        }
    }.build().also { cachedWind = it }

/** `Wind` vector, built on first access and reused afterwards. */
private var cachedWind: ImageVector? = null
