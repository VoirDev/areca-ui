package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Link icon.
 *
 * Converted from Lucide `link` (ISC).
 */
public val ArecaIcons.Link: ImageVector
    get() = cachedLink ?: ImageVector.Builder(
        name = "Link",
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
            moveTo(10f, 13f)
            arcTo(5f, 5f, 0f, false, false, 17.54f, 13.54f)
            lineTo(20.54f, 10.54f)
            arcTo(5f, 5f, 0f, false, false, 13.47f, 3.47f)
            lineTo(11.75f, 5.18f)
            moveTo(14f, 11f)
            arcTo(5f, 5f, 0f, false, false, 6.46f, 10.46f)
            lineTo(3.46f, 13.46f)
            arcTo(5f, 5f, 0f, false, false, 10.53f, 20.53f)
            lineTo(12.24f, 18.82f)
        }
    }.build().also { cachedLink = it }

/** `Link` vector, built on first access and reused afterwards. */
private var cachedLink: ImageVector? = null
