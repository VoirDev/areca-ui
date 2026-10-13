package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Route icon.
 *
 * Converted from Lucide `route` (ISC).
 */
public val ArecaIcons.Route: ImageVector
    get() = cachedRoute ?: ImageVector.Builder(
        name = "Route",
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
            moveTo(3f, 19f)
            arcTo(3f, 3f, 0f, true, true, 9f, 19f)
            arcTo(3f, 3f, 0f, true, true, 3f, 19f)
            close()
            moveTo(9f, 19f)
            horizontalLineTo(17.5f)
            arcTo(3.5f, 3.5f, 0f, false, false, 17.5f, 12f)
            horizontalLineTo(6.5f)
            arcTo(3.5f, 3.5f, 0f, false, true, 6.5f, 5f)
            horizontalLineTo(15f)
            moveTo(15f, 5f)
            arcTo(3f, 3f, 0f, true, true, 21f, 5f)
            arcTo(3f, 3f, 0f, true, true, 15f, 5f)
            close()
        }
    }.build().also { cachedRoute = it }

/** `Route` vector, built on first access and reused afterwards. */
private var cachedRoute: ImageVector? = null
